;;; Sierra Script 1.0 - (do not remove this comment)

(script# 200)
(include sci.sh)
(include game.sh)
(use controls)
(use feature)
(use cycle)
(use game)
(use main)
(use obj)

(public
	mapRegion 0
)

(local
; TELEPORT MAP REGION
; (ALTERNATE METHOD FOR WORLD TRAVEL)
; BY RYAN SLATTERY
; Special thanks to Troflip for his tutorial on Regions




	myEvent
	; forest = 0
; grave = 0
;    ruins = 0
;    pond = 0
;    magic = 0
;
;    exit = 0
	destination =  0 ; magic 1, pond 2, grave 3, ruins 4, exit 5
	room =  0
	mapOpen =  0
)
(instance mapRegion of Rgn
	(properties)
	
	(method (handleEvent pEvent &tmp button)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evKEYBOARD)
			(keyGo pEvent $006d gWizRm 18 8)
			(keyGo pEvent $0070 gPond 24 9)
			(keyGo pEvent $0067 gGrave 42 6)
			(keyGo pEvent $0077 gRuin 60 7)

			(if
				(or
					(== (pEvent message?) $0065)
					(== (pEvent message?) $0078)
					(== (pEvent message?) $0063)
					(== (pEvent message?) $0071)
				)                                                                                                      ; lowercase c, e, x, or q
				(if mapOpen (mapClose changeState: 1))
			)
		)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if gMap
				(switch destination
					(1 (whereGo 18 8)) ; magic
					(2 (whereGo 24 9)) ; pond
					(3 (whereGo 42 6)) ; grave
					(4 (whereGo 60 7)) ; ruins
					(5 (mapClose changeState: 1)) ; exit
				)
			)
		)
		(if (or (Said 'look,use,read,open/portal,map')
				(Said 'map'))
			(if (gEgo has: 7)
				(map init: show: setPri: 13 setScript: mapClose)
				(gEgo hide: setMotion: NULL loop: 2)
				(alterEgo
					init:
					show:
					posn: (gEgo x?) (gEgo y?)
					view: 17
					loop: 1
					cel: 0
					setCycle: End
				)
				(if gRuin (ruinsDot init: show: setPri: 14))
				(if gGrave (graveDot init: show: setPri: 14))
				(if gWizRm (magicDot init: show: setPri: 14))
				(if gPond (pondDot init: show: setPri: 14))
				(= mapOpen 1)
				(= gMap 1)
				(= gArcStl 1)
			else
				(PrintDHI)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if gMap
			(= myEvent (Event new: evNULL))
			; (procedure (checkButton view celNum posX posY whichExit)
			(cond 
				((checkButton magicDot 4 150 16 1) (SetCursor 999 (HaveMouse)) (= gCurrentCursor 999))
				((checkButton pondDot 5 165 38 2) (SetCursor 999 (HaveMouse)) (= gCurrentCursor 999))
				((checkButton graveDot 2 192 91 3) (SetCursor 999 (HaveMouse)) (= gCurrentCursor 999))
				((checkButton ruinsDot 6 295 84 4) (SetCursor 999 (HaveMouse)) (= gCurrentCursor 999))
				(
					; (if (checkButton(closeMap 1 299 13 5))  // exit
					(checkEvent
						myEvent
						(map nsLeft?)
						(map nsRight?)
						(+ (map nsTop?) 6)
						(+ (map nsBottom?) 10)
					)
					(SetCursor 999 (HaveMouse))
					(= gCurrentCursor 999)
				)
				(else
					(SetCursor 992 (HaveMouse))
					(= gCurrentCursor 992)
					(selectorZero)
					(= destination 5)
				)
			)                                   ; exit
			(myEvent dispose:)
		)
	)
)

(instance mapClose of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			; closing the map
			(1
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(alterEgo setCycle: Beg self)
				(= gMap 0)
				(= gArcStl 0)
				(= mapOpen 0)
			)
			(2
				(map hide:)
				(ruinsDot hide:)
				(graveDot hide:)
				(magicDot hide:)
				(pondDot hide:)
				(writing hide:)
				(alterEgo hide:)
				(gEgo show:)
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
			)
			(4
				(= gTeleporting 1)
				(gEgo hide:)
				(alterEgo
					loop: 0
					cel: 0
					setCycle: End mapClose
					cycleSpeed: 2
				)
				(gTheMusic prevSignal: 0 stop:)
				(gTheSoundFX number: 205 play:)
				(ProgramControl)
				(SetCursor 997 1)
				(map hide:)
				(ruinsDot hide:)
				(graveDot hide:)
				(magicDot hide:)
				(pondDot hide:)
				(writing hide:)
				(= gCurrentCursor 997)
			)
			(5
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				;(= gTeleporting 1)
				(gRoom newRoom: room)
			)
		)
	)
)

(procedure (keyGo event letter condition x y)
	(if (== (event message?) letter)
		(if condition (if mapOpen (whereGo x y)))
	)
)

(procedure (checkButton view celNum posX posY whichExit)
	(if
		(checkEvent
			myEvent
			(view nsLeft?)
			(view nsRight?)
			(+ (view nsTop?) 6)
			(+ (view nsBottom?) 10)
		)
		(selectorZero)
		(writing
			init:
			cel: celNum
			show:
			setPri: 15
			posn: posX posY
		)
		(= destination whichExit)
	else
		(= destination 0)
		(selectorZero)
	)
)

(procedure (whereGo rmNum textRes &tmp button)
	(= button
		(Print
			200
			textRes
			#title
			{Travel}
			#button
			{Yes}
			1
			#button
			{Nah}
			0
		)
	)
	(if (== button 1)
		(if (== gRoomNumber rmNum)
			(Print 200 15)
		else
			(= gMap 0)
			(= gArcStl 0)
			(= mapOpen 0)
			(= room rmNum)
			(mapClose changeState: 4)
		)
	)
)

(procedure (selectorZero)
	(writing hide:)
	; (frstDot:cel(0))
	(graveDot cel: 0)
	(ruinsDot cel: 0)
	(magicDot cel: 0)
	(pondDot cel: 0)
	; = forest 0
; = grave 0
;    = ruins 0
;    = magic 0
;    = exit 0
;    = pond 0
	(= destination 0)
)

(procedure (checkEvent pEvent x1 x2 y1 y2)
	(if
		(and
			(> (pEvent x?) x1)
			(< (pEvent x?) x2)
			(> (pEvent y?) y1)
			(< (pEvent y?) y2)
		)
		(return TRUE)
	else
		(return FALSE)
	)
)

(instance map of Prop
	(properties
		y 128
		x 213
		view 954
	)
)

(instance ruinsDot of Prop
	(properties
		view 954
		loop 2
		y 86
		x 278
	)
)

(instance graveDot of Prop
	(properties
		view 954
		loop 2
		y 93
		x 161
	)
)

(instance magicDot of Prop
	(properties
		view 954
		loop 2
		y 17
		x 120
	)
)

(instance pondDot of Prop
	(properties
		view 954
		loop 2
		y 40
		x 148
	)
)

(instance writing of Prop
	(properties
		view 954
		loop 3
	)
)

(instance alterEgo of Prop
	(properties
		view 17
		loop 1
	)
)
