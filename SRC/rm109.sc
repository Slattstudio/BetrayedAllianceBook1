;;; Sierra Script 1.0 - (do not remove this comment)
; + 5 SCORE  //
(script# 109)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use rev)

(public
	rm109 0
)

(local

; Rotating Prison Tower



	; (use "sciaudio")
	pressedRight =  0
	pressedLeft =  0
	goingToStorage =  0
	fromBottom =  0
	
	skyStop
	skyStopSwitch =  0
	enemyVisible =  0
	enemyNumber =  1
	aiming =  0
	myEvent
	dartShot =  0
	[dartEvent 4]
	enemyDarted =  0
	ultimatum =  0
	steps =  0
	newRoom =  1
	; snd
	handsOff =  1
)

(instance rm109 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		
		(self setScript: RoomScript)
		(SetUpEgo)
		(gEgo init: ignoreActors: ignoreControl: ctlWHITE)
		
		(bottomStairs
			init:
			setPri: 1
			setScript: goToStorage
			ignoreActors:
		)
		(topStairs
			init:
			setPri: 1
			ignoreActors:
			setScript: enemyScript
		)
		(banister
			init:
			setPri: 2
			ignoreActors:
			setScript: dartScript
		)
		(window
			init:
			setScript: windowScript
			setPri: 1
			ignoreActors:
			hide:
		)
		(sky init: setPri: 0 ignoreActors: hide:)
		; (= gMap 1)
		
		;(gTheMusic number: 109 loop: -1 play:)
		(= gArcStl 1)
		
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 115 189 loop: 0)
			)
			(103
				(= newRoom 0)
				(= enemyNumber [gArray 0])
				(gTheMusic number: 109 loop: -1 play:)
				(if (> 3 enemyNumber)
					(= handsOff 0)
					
					(alterEgo init: posn: 160 182 loop: 0)
					(gEgo hide:)
					(= enemyNumber [gArray 0])
					(++ enemyNumber)
					(= fromBottom [gArray 1])
					(if [gArray 2]
						(window
							init:
							setScript: windowScript
							setPri: 1
							ignoreActors:
							hide:
						)
						(sky init: setPri: 0 ignoreActors: hide:)
					else
						(window
							init:
							setScript: windowScript
							setPri: 1
							ignoreActors:
							posn: [gArray 3] [gArray 4]
						)
						(sky
							init:
							setPri: 0
							ignoreActors:
							posn: [gArray 5] (sky y?)
						)
					)
				else
					;(RoomScript changeState: 1)
					(gEgo hide:)
					(= enemyNumber [gArray 0])
					(= enemyNumber 4)
					(= fromBottom [gArray 1])
					(window init: hide:)
					(sky init: hide:)
					(dartScript changeState: 9)
				)
			)
		)
	)
)

; (send gRoom:newRoom(69))

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 1)
			)	
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(if (== fromBottom 0)
					(gEgo setMotion: MoveTo 160 182 self)
				)
			)
			; (send gTheMusic:prevSignal(0)stop()number(109)loop(-1)play())
			(2
				(= cycles 5)
				(gEgo hide:)
				(alterEgo init: posn: 160 182)
			)
			(3
				(= cycles 1)
				(= newRoom 0)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(4
				(Print 109 1)
; Ascending the western tower even at night has it's dangers. There are surely some midnight guards about.
				(if gApple (Print 109 5)
					(= gWndColor 0) ; clYELLOW
					(= gWndBack 14) ; clDARKBLUE
					(Print 109 7 #font 4 #title {Using your darts:} #at -1 20 #width 140 #button {Ok} )
					(= gWndColor 0) ; clBLACK
					(= gWndBack 15)	
				)
				(= handsOff 0)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (> (window x?) 250) (window hide:) (sky hide:))
		(= myEvent (Event new: evNULL))
		(cond 
			(aiming
				(if (< (myEvent y?) 120)
					(if (gEgo has: INV_BLOW_DART_GUN)
						(SetCursor 994 (HaveMouse))
						(= gCurrentCursor 994)
					)
				else
					(SetCursor 999 (HaveMouse))
					(= gCurrentCursor 999)
				)
			)
			((not newRoom) (SetCursor 999 (HaveMouse)) (= gCurrentCursor 999))
			(else (SetCursor 997 (HaveMouse)) (= gCurrentCursor 997))
		)
		(myEvent dispose:)
		(if (< enemyNumber 4)
			(if (< (alterEgo distanceTo: closeEnemy) 24)
				(= [gArray 0] enemyNumber)
				(= [gArray 1] fromBottom)
				(= [gArray 2] skyStopSwitch)
				(= [gArray 3] (window x?))
				(= [gArray 4] (window y?))
				(= [gArray 5] (sky x?))
				(= gBatNum 100)
				(= gNoRun 1)
				(= gOpHealth gBatNum)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gRoom newRoom: 103)
			)
			(if (< (alterEgo distanceTo: enemy) 15)
				(= [gArray 0] enemyNumber)
				(= [gArray 1] fromBottom)
				(= [gArray 2] skyStopSwitch)
				(= [gArray 3] (window x?))
				(= [gArray 4] (window y?))
				(= [gArray 5] (sky x?))
				(= gBatNum 99)
				(= gLifeDoub 1)
				(= gDeadWeight 1)
				(= gNoRun 1)
				(= gOpHealth gBatNum)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gRoom newRoom: 103)
			)
		)
		(if (and (== fromBottom 0) (== pressedLeft 1))
			(if (not goingToStorage)
				(windowScript changeState: 4)
				(goToStorage changeState: 1)
			)
		)
		(if
		(and (== (window x?) 253) (== (window y?) 68))
			(if (not skyStopSwitch)
				(= skyStop fromBottom)
				(= skyStopSwitch 1)
			)
		)
		(if pressedRight (++ fromBottom))
		(if pressedLeft (if (> fromBottom 0) (-- fromBottom)))
		(if (> fromBottom 40)
			(if (== enemyNumber 1)
				(if (not enemyVisible) (enemyScript changeState: 1))
			)
		)
		(if (> fromBottom 160)
			(if (== enemyNumber 2)
				(if (not enemyVisible) (enemyScript changeState: 1))
			)
		)
		(if (> fromBottom 290)
			(if (== enemyNumber 3)
				(if (not enemyVisible) (enemyScript changeState: 1))
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)

		(if (== (pEvent type?) evMOUSEBUTTON)
			(if
				(and
					(> (pEvent x?) 70)
					(< (pEvent x?) 88)
					(> (pEvent y?) 167)
					(< (pEvent y?) 182)
				)                          ; If clicked on 'left/down' button
				(if (not newRoom) (if (not handsOff) (moveLeft)))
			)
			(if
				(and
					(> (pEvent x?) 229)
					(< (pEvent x?) 247)
					(> (pEvent y?) 167)
					(< (pEvent y?) 182)
				)                          ; If clicked on 'right/up' button
				(if (not newRoom) (if (not handsOff) (moveRight)))
			)
			(if
				(and
					(> (pEvent x?) 229)
					(< (pEvent x?) 247)
					(> (pEvent y?) 147)
					(< (pEvent y?) 163)
				)                          ; If clicked on 'target' button
				(if (not newRoom)
					(if (not handsOff)
						(if (gEgo has: INV_BLOW_DART_GUN)
							(cursorProc)
						else
							(PrintOther 109 6)
						)
					)
				)
			)
			(if aiming
				(if (== gCurrentCursor 994)
					(if (not handsOff)
						(if (>= gApple 1)
							(if (== dartShot 0)
								(++ dartShot)
								(-- gApple)
								(= [dartEvent 0] (pEvent x?))
								(= [dartEvent 1] (pEvent y?))
								(dartScript changeState: 1)
							)
						else
							(PrintOther 109 2)
						)
					)
				)
			)
		)
; Oh dart, you're out of drats!
		(if (== (pEvent type?) evJOYSTICK)
			(if
			(or (== (pEvent message?) 3) (== (pEvent message?) 2)) ; If pressed the RIGHT arrow ; RIGHT/UP diagonal button
				(if (not handsOff) (moveRight))
			)
			(if
			(or (== (pEvent message?) 7) (== (pEvent message?) 6) (== (pEvent message?) 5)) ; If pressed the LEFT arrow ; LEFT/DOWN diagonal button ; DOWN
				(if (not handsOff) (moveLeft))
			)
			(if (== (pEvent message?) 1)    ; If pressed the UP arrow
				(if (not handsOff)
					(if (gEgo has: INV_BLOW_DART_GUN)
						(cursorProc)
					else
						; PrintOther(109 6)
						(moveRight)
					)
				)
			)
		)
	)
)

(instance goToStorage of Script
	(properties)
	
	(method (changeState newState &tmp button)
		(= state newState)
		(switch state
			(1
				(= button
					(Print
						{Do you want to go back to the storage room?}
						#button
						{Yes}
						1
						#button
						{No}
						0
					)
				)
				(switch button
					(1
						(= goingToStorage 1)
						(ProgramControl)
						(SetCursor 997 (HaveMouse))
						(= gCurrentCursor 997)
						(= handsOff 1)
						(gEgo
							show:
							loop: 1
							posn: (alterEgo x?) (alterEgo y?)
							setMotion: MoveTo 115 189 goToStorage
						)
						(alterEgo hide:)
						(bottomStairs setCycle: CT)
						(topStairs setCycle: CT)
						(banister setCycle: CT)
					)
					(else
						(= pressedLeft 0)
						(= pressedRight 0)
						(alterEgo view: 903 loop: 0)
						(bottomStairs setCycle: CT)
						(topStairs setCycle: CT)
						(banister setCycle: CT)
					)
				)
			)
			(2
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= gArcStl 0)
				(gRoom newRoom: 135)
			)
		)
	)
)

(instance windowScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(= cycles 1)
				(if (not skyStopSwitch)
					(window posn: (+ (window x?) 3) (window y?))
					(++ steps)
					(if (== steps 4)
						(window posn: (window x?) (+ (window y?) 1))
						(= steps 0)
					)
					(if (< skyStop fromBottom)
						(sky posn: (+ (sky x?) 1) (sky y?))
					)
					(ditherSaver)
				)
				(if enemyVisible)
			; (enemy:posn( (+ (enemy:x) 1)(enemy:y)) )
			)
			(2
				(= cycles 3)
				(window setMotion: NULL)
			)
			(3
				(windowScript changeState: 1)
			)
			(4 (window setMotion: NULL))
			(6
				(= cycles 1)
				(if skyStopSwitch
					(if (> skyStop fromBottom)
						(window posn: (- (window x?) 3) (window y?))
						(-- steps)
						(if (== steps 0)
							(window posn: (window x?) (- (window y?) 1))
							(= steps 4)
						)
						(ditherSaver)
						(sky posn: (- (sky x?) 1) (sky y?))
						(= skyStopSwitch 0)
					)
				else
					(window posn: (- (window x?) 3) (window y?))
					(-- steps)
					(if (== steps 0)
						(window posn: (window x?) (- (window y?) 1))
						(= steps 4)
					)
					(ditherSaver)
					(sky posn: (- (sky x?) 1) (sky y?))
				)
				(if enemyVisible)
			; (enemy:posn( (- (enemy:x) 1)(enemy:y)) )
			)
			(7
				(= cycles 3)
				(window setMotion: NULL)
			)
			(8
				(windowScript changeState: 6)
			)
		)
	)
)

(instance enemyScript of Script
	(properties)
	
	(method (changeState newState &tmp button)
		(= state newState)
		(switch state
			(1
				(= enemyVisible 1)
				(switch enemyNumber
					(1
						(enemy
							init:
							show:
							posn: 90 59
							setPri: 2
							xStep: 2
							setCycle: Walk
							setMotion: MoveTo 253 86 enemyScript
						)
					)
					(2
						(enemy
							init:
							show:
							posn: 90 59
							setPri: 2
							xStep: 3
							setCycle: Walk
							setMotion: MoveTo 253 86 enemyScript
						)
					)
					(3
						(enemy
							init:
							show:
							view: 328
							posn: 90 59
							setPri: 2
							xStep: 1
							setCycle: Walk
							setMotion: MoveTo 200 73 enemyScript
						)
					)
				)
			)
			(2
				(= cycles 10)
				(if (== enemyNumber 3)
					(enemy loop: 2)
					(enemyScript changeState: 7)
				)
			)
			(3
				(if (>= 2 enemyNumber)
					(closeEnemy
						init:
						setCycle: Walk
						setMotion: MoveTo (alterEgo x?) (alterEgo y?)
					)
				)
			)
			(4 (= cycles 10))
			(5
				(enemy loop: 3 setCycle: End enemyScript)
			)
			(6
				(enemy hide:)
				(= enemyVisible 0)
				(++ enemyNumber)
			)
			(7
				(= cycles 15)
				(= pressedRight 0)
				(= pressedLeft 0)
				(alterEgo view: 903 loop: 0)
				(bottomStairs setCycle: CT)
				(topStairs setCycle: CT)
				(banister setCycle: CT)
				(windowScript changeState: 4)
			)
			(8
				(= ultimatum 1)
				(Print 109 3)
; This great knight in full mail seems to be taking a stand.
				(= button
					(Print
						{Do you want to go up and contend with this champion?}
						#button
						{I am ready!}
						1
						#button
						{Um, Just a moment}
						2
					)
				)
				(switch button
					(1 (dartScript changeState: 5))
					(2)
				)
			)
		)
	)
)

(instance dartScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(= cycles 2)
				(alterEgo cel: 1)
			)
			(2
				(alterEgo cel: 0)
				(dart
					init:
					show:
					posn: 161 135
					setPri: 4
					xStep: 7
					yStep: 9
					setMotion: MoveTo (+ [dartEvent 0] 8) (+ [dartEvent 1] 22) dartScript
					ignoreActors:
				)
			)
			(3 (= cycles 1) (dart hide:))
			(4
				(if
					(and
						(> (+ (dart nsLeft?) 3) (enemy nsLeft?))
						(< (- (dart nsRight?) 3) (enemy nsRight?))
						(> (+ (dart nsTop?) 5) (enemy nsTop?))
						(< (- (dart nsBottom?) 10) (enemy nsBottom?))
					)
					(if (== enemyNumber 3)
						(Print {You hear a distant clank.} #at -1 10)
						(Print
							{He must be impervious to your powerful poisonous projectiles!}
							#title
							{panic stations!}
							#at
							-1
							20
						)
					else
						(enemy setMotion: NULL loop: 2 cel: 0 setCycle: End)
						(enemyScript changeState: 4)
					)
				else
				; Print(109 4) /* Missed */
				)
				(-- dartShot)
			)
			(5 (= cycles 1) 
				(= handsOff 1)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)
			(6
				(alterEgo hide:)
				(gEgo
					posn: (alterEgo x?) (alterEgo y?)
					show:
					setMotion: MoveTo 253 135 dartScript
				)
			)
			(7 (= cycles 10))
			(8
				(gEgo hide:)
				(alterEgo
					show:
					view: 329
					posn: 253 86
					setCycle: Walk
					setMotion: MoveTo (enemy x?) (enemy y?)
				)
			)
			(9
				(= cycles 2)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(= handsOff 1)
				(gEgo hide:)
				(alterEgo
					init:
					show:
					view: 329
					posn: 200 73
					setCycle: Walk
					setMotion: MoveTo 90 59 dartScript
					setPri: 2
				)
				(gGame changeScore: 5)
			)
			(10 (Print 109 0))
; As you look up from your bested opponent you see the chamber you seek. Julyn will soon be in your sight!
			(11
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= gArcStl 0)
				(gRoom newRoom: 69)
			)
		)
	)
)

(procedure (moveRight)
	(= aiming 0)
	(cond 
		(ultimatum (enemyScript changeState: 8))
		(pressedRight
			(= pressedRight 0)
			(= pressedLeft 0)
			(alterEgo view: 903 loop: 0)
			(bottomStairs setCycle: CT)
			(topStairs setCycle: CT)
			(banister setCycle: CT)
			(windowScript changeState: 4)
		)
		(else
			(= pressedLeft 0)
			(= pressedRight 1)
			(alterEgo view: 0 loop: 0 setCycle: Fwd)
			(bottomStairs setCycle: Fwd cycleSpeed: 4)
			(topStairs setCycle: Fwd cycleSpeed: 6)
			(banister setCycle: Fwd cycleSpeed: 6)
			(windowScript changeState: 1)
		)
	)
)

(procedure (ditherSaver)
	(cond 
		((== (mod (window x?) 2) 0) ; x is yes
			(if (== (mod (window y?) 2) 0) ; y is yes	
				(window view: 589)
			else ; y is no
				(window view: 579)
			)
		)
		((== (mod (window y?) 2) 0) (window view: 579)) ; x is no ; y is yes
		(else (window view: 589)) ; y is no
	)
)

(procedure (moveLeft &tmp button)
	(= aiming 0)
	(cond 
		(ultimatum
			(= button
				(Print
					{Are you sure you want to go all the way back down to the storage room?}
					#button
					{Yes}
					1
					#button
					{ No_}
					2
				)
			)
			(switch button
				(1
					(SetCursor 999 (HaveMouse))
					(= gCurrentCursor 999)
					(= gArcStl 0)
					(gRoom newRoom: 135)
				)
				(2)
			)
		)
		(pressedLeft
			(= pressedLeft 0)
			(= pressedRight 0)
			(alterEgo view: 903 loop: 1)
			(bottomStairs setCycle: CT)
			(topStairs setCycle: CT)
			(banister setCycle: CT)
			(windowScript changeState: 4)
		)
		(else
			(= pressedLeft 1)
			(= pressedRight 0)
			(alterEgo view: 0 loop: 1 setCycle: Fwd)
			(bottomStairs setCycle: Rev cycleSpeed: 4)
			(topStairs setCycle: Rev cycleSpeed: 6)
			(banister setCycle: Rev cycleSpeed: 6)
			(windowScript changeState: 6)
		)
	)
)

(procedure (cursorProc)
	(if (not ultimatum)
		(= pressedLeft 0)
		(= pressedRight 0)
		(bottomStairs setCycle: CT)
		(topStairs setCycle: CT)
		(banister setCycle: CT)
		(alterEgo view: 241 setCycle: CT)
		(windowScript changeState: 4)
		(= aiming 1)
	)
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 100)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
	)
)

(instance bottomStairs of Prop
	(properties
		y 194
		x 159
		view 578
	)
)

(instance topStairs of Prop
	(properties
		y 94
		x 156
		view 578
		loop 1
	)
)

(instance banister of Prop
	(properties
		y 94
		x 156
		view 578
		loop 2
	)
)

(instance alterEgo of Act
	(properties
		y 194
		x 159
		view 903
	)
)

(instance window of Act
	(properties
		y 57
		x 109
		view 589
	)
)

(instance sky of Prop
	(properties
		y 63
		x 155
		view 580
	)
)

(instance enemy of Act
	(properties
		y 59
		x 90
		view 327
	)
)

(instance closeEnemy of Act
	(properties
		y 145
		x 253
		view 322
	)
)

(instance dart of Act
	(properties
		y 135
		x 161
		view 57
	)
)

(instance dart2 of Act
	(properties
		y 135
		x 161
		view 57
	)
)
