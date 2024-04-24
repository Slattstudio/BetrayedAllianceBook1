;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; user.sc
; Contains the classes to handle user input and the main character (ego).
(script# USER_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use controls)
(use sound)
(use cycle)
(use menubar)
(use feature)
(use obj)


(local



	[inputStr 51]
	maxInputLen
)

(instance uEvt of Event
	(properties)
)


(class User of Obj
	(properties
		alterEgo 0
		canInput 0
		controls 0
		echo $0020
		prevDir 0
		prompt {Enter Input:}
		inputLineAddr 0
		x -1
		y -1
		blocks 1
		mapKeyToDir 1
		curEvent 0
	)
	
	(method (init pInputStr maxLen)
		(if argc
			(= inputLineAddr pInputStr)
		else
			(= inputLineAddr @inputStr)
		)
		(if (== argc 2)
			(= maxInputLen maxLen)
		else
			(= maxInputLen 45)
		)
		(= curEvent uEvt)
	)
	
	(method (doit)
		(if (== 0 gSetRegions)
			(curEvent
				type: 0
				message: 0
				modifiers: 0
				y: 0
				x: 0
				claimed: 0
			)
			(GetEvent evALL_EVENTS curEvent)
			(self handleEvent: curEvent)
		)
	)
	
	(method (canControl fCONTROLS)
		(if argc (= controls fCONTROLS) (= prevDir CENTER))
		(return controls)
	)
	
	(method (getInput pEvent &tmp prevSound strLen)
		(if (!= (pEvent type?) evKEYBOARD) (= inputStr 0))
		(if (!= (pEvent message?) echo)
			(Format @inputStr {%c} (pEvent message?))
		)
		(= prevSound (Sound pause: blocks))
		(= strLen
			(EditPrint @inputStr maxInputLen prompt #at x y)
		)
		(Sound pause: prevSound)
		(return strLen)
	)
	
	(method (said pEvent)
		(gSFeatures add: gCast gFeatures)
		(if TheMenuBar (gSFeatures addToFront: TheMenuBar))
		(gSFeatures addToEnd: gGame handleEvent: pEvent release:)
		(if
			(and
				(== (pEvent type?) evSAID)
				(not (pEvent claimed?))
			)
			(gGame pragmaFail: @inputStr)
		)
	)
	
	(method (handleEvent pEvent &tmp evType)
		(if (pEvent type?)
			(= gUserEvent pEvent)
			(= evType (pEvent type?))
			(if mapKeyToDir (MapKeyToDir pEvent))
			(if TheMenuBar (TheMenuBar handleEvent: pEvent evType))
			(GlobalToLocal pEvent)
			(if (not (pEvent claimed?))
				(gGame handleEvent: pEvent evType)
			)
			(if
				(and
					controls
					(not (pEvent claimed?))
					(gCast contains: alterEgo)
				)
				(alterEgo handleEvent: pEvent)
			)
			(if (and canInput (not (pEvent claimed?)))
				(if
					(or
						(== (pEvent message?) echo)
						(and
							(<= $0020 (pEvent message?))
							(<= (pEvent message?) 255)
						)
					)
					(if (not gArcStl)
						(if
						(and (self getInput: pEvent) (Parse @inputStr pEvent))
							(pEvent type: evSAID)
							(self said: pEvent)
						)
					)
				)
			)
		)
		(= gUserEvent NULL)
	)
)


(instance egoMirror of Act
	(properties
		y 1
		x 1
		view 0
	)
)

(class Ego of Act
	(properties
		y 0
		x 0
		z 0
		heading 0
		yStep 2
		view 0
		loop 0
		cel 0
		priority 0
		underBits 0
		signal $2000
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		lsTop 0
		lsLeft 0
		lsBottom 0
		lsRight 0
		brTop 0
		brLeft 0
		brBottom 0
		brRight 0
		cycleSpeed 0
		script 0
		cycler 0
		timer 0
		illegalBits $8000
		xLast 0
		yLast 0
		xStep 3
		moveSpeed 0
		blocks 0
		baseSetter 0
		mover 0
		looper 0
		viewer 0
		avoider 0
		edgeHit 0
	)
	
	(method (init)
		(super init:)
		(if (not cycler) (self setCycle: Walk))
	)
	
	(method (mirrorEgo mirror_y &tmp y_diff pos_y)
		(cond 
			((not gWizRm)
				(cond 
					((gEgo has: 7) (egoMirror view: 313))
					((gEgo isStopped:) (egoMirror view: 903))
					(else (egoMirror view: 0))
				)
			)
			((gEgo isStopped:) (egoMirror view: 903))
			(else (egoMirror view: 0))
		)
		(= y_diff (gEgo y?))
		(-= y_diff mirror_y)
		(= pos_y mirror_y)
		(-= pos_y y_diff)
		(egoMirror init: posn: (gEgo x?) pos_y)
		(if (== (gEgo loop?) 0) (egoMirror loop: 0))
		(if (== (gEgo loop?) 1) (egoMirror loop: 1))
		(if (== (gEgo loop?) 2) (egoMirror loop: 3))
		(if (== (gEgo loop?) 3) (egoMirror loop: 2))
		(egoMirror cel: (gEgo cel?))
	)
	
	(method (mirrorEgoDispose)
		(egoMirror dispose:)
	)
	
	(method (doit)
		(super doit:)
		(cond 
			((<= x 5) (= edgeHit EDGE_LEFT))
			((<= y (gRoom horizon?)) (= edgeHit EDGE_TOP))
			((>= x 314) (= edgeHit EDGE_RIGHT))
			((>= y 186) (= edgeHit EDGE_BOTTOM))
			(else (= edgeHit EDGE_NONE))
		)
	)
	
	(method (handleEvent pEvent &tmp direction)
		(if (not (super handleEvent: pEvent))
			(switch (pEvent type?)
				(evMOUSEBUTTON
					; Make sure it's the left button
					(if
						(and
							(not (& (pEvent modifiers?) emRIGHT_BUTTON))
							(User controls?)
						)
						(if (not gMap)
							(if (not gNoClick)
								(self setMotion: MoveTo (pEvent x?) (pEvent y?))
								(= gRunClick 1)
								(User prevDir: CENTER)
								(pEvent claimed: TRUE)
							)
						)
					)
				)
				(evJOYSTICK
					(if (not gMap)
						(= direction (pEvent message?))
						(if
						(and (== direction (User prevDir?)) (IsObject mover))
							(= direction CENTER)
						)
						(= gRunClick 0)
						(User prevDir: direction)
						(self setDirection: direction)
						(pEvent claimed: TRUE)
					)
				)
			)
		)
		(return (pEvent claimed?))
	)
	
	(method (get items &tmp i invItem)
		(for ( (= i 0)) (< i argc)  ( (++ i)) (= invItem (gInv at: [items i])) (invItem moveTo: self))
	)
	
	(method (put item newOwner &tmp invItem)
		(if (self has: item)
			(= invItem (gInv at: item))
			(if (== argc 1)
				(invItem moveTo: -1)
			else
				(invItem moveTo: newOwner)
			)
		)
	)
	
	(method (has item &tmp invItem)
		(= invItem (gInv at: item))
		(if invItem (invItem ownedBy: self))
	)
)
