;;; Sierra Script 1.0 - (do not remove this comment)

(script# 76)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use menubar)

(public
	rm076 0
)

(local
; ENDING  Story-book Horse Running



	; (use "sciaudio")
	htext =  0
	htext2 =  0
	htext3 =  0
	; stateSwitch = 0
	; snd
	movingRight =  1
	riding =  0
	upDown =  0
	passUp =  0
	inForest =  0
)

(instance rm076 of Rm
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
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
		)
		; (MenuBar:hide())
		(TheMenuBar state: DISABLED)
		(SetUpEgo)
		(gEgo init: hide: posn: 1 1)
		(= gArcStl 1)
		(left init: posn: 109 125 setCycle: CT)
		(right init: posn: 198 125 setCycle: CT)
		(top init: setPri: 5)
		(bottom init:)
		(leah init: hide:)
		(egoRiding init: hide:)
		(horse init: hide:)
		(soldier init: hide: setPri: 4)
		(soldier2 init: hide:)
		(SetCursor 998 (HaveMouse))
		(= gCurrentCursor 998)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState intro)
		(= state intro)
		(switch state
			(0 (= seconds 2))
			(1
				(= seconds 1)    ; 3
				(goToLandscape)
			)
			(2
				(= seconds 2)
				(= riding 1)
				(horse show: setCycle: Fwd cycleSpeed: 1)
			)
			(3
				(= seconds 5)
				(= passUp 1)
				(= htext
					(Display
						76
						3
						dsCOORD
						57
						130
						dsALIGN
						alCENTER
						dsCOLOUR
						clYELLOW
						dsBACKGROUND
						clBLACK
						dsWIDTH
						200
						dsSAVEPIXELS
					)
				)
				(= htext2
					(Display
						76
						0
						dsCOORD
						70
						138
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						170
						dsSAVEPIXELS
					)
				)
			)
			(4
				(= seconds 1)    ; 9
				(Display {} dsRESTOREPIXELS htext)
				(Display {} dsRESTOREPIXELS htext2)
			)
			(5
				(= seconds 4)    ; 10
				; goToPortrait()
				(= htext
					(Display
						76
						3
						dsCOORD
						48
						130
						dsALIGN
						alCENTER
						dsCOLOUR
						clYELLOW
						dsBACKGROUND
						clBLACK
						dsWIDTH
						210
						dsSAVEPIXELS
					)
				)
				(= htext2
					(Display
						76
						1
						dsCOORD
						70
						138
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						170
						dsSAVEPIXELS
					)
				)
				(= htext3
					(Display
						76
						2
						dsCOORD
						70
						147
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						170
						dsSAVEPIXELS
					)
				)
			)
			(6
				(= seconds 1)    ; 15
				(Display {} dsRESTOREPIXELS htext)
				(Display {} dsRESTOREPIXELS htext2)
				(Display {} dsRESTOREPIXELS htext3)
			)
			(7
				(= seconds 4)    ; 10
				; goToPortrait()
				(= htext
					(Display
						@gName
						dsCOORD
						48
						130
						dsALIGN
						alCENTER
						dsCOLOUR
						clYELLOW
						dsBACKGROUND
						clBLACK
						dsWIDTH
						210
						dsSAVEPIXELS
					)
				)
				(= htext2
					(Display
						76
						4
						dsCOORD
						70
						138
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						170
						dsSAVEPIXELS
					)
				)
				(= htext3
					(Display
						76
						5
						dsCOORD
						70
						147
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						170
						dsSAVEPIXELS
					)
				)
			)
			(8
				(= seconds 1)    ; 15
				(Display {} dsRESTOREPIXELS htext)
				(Display {} dsRESTOREPIXELS htext2)
				(Display {} dsRESTOREPIXELS htext3)
			)
			(9
				(= seconds 4)
				(goToPortrait)
				(= htext
					(Display
						76
						3
						dsCOORD
						48
						130
						dsALIGN
						alCENTER
						dsCOLOUR
						clYELLOW
						dsBACKGROUND
						clBLACK
						dsWIDTH
						210
						dsSAVEPIXELS
					)
				)
				(= htext2
					(Display
						76
						6
						dsCOORD
						70
						138
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						170
						dsSAVEPIXELS
					)
				)
				(= htext3
					(Display
						76
						7
						dsCOORD
						70
						147
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						170
						dsSAVEPIXELS
					)
				)
				(= passUp 0)
			)
			(10
				(= seconds 1)     ; 15
				(Display {} dsRESTOREPIXELS htext)
				(Display {} dsRESTOREPIXELS htext2)
				(Display {} dsRESTOREPIXELS htext3)
			)

			(11
				(gRoom newRoom: 99)
				(= inForest 1)
				(= riding 0)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(SetCursor 998 (HaveMouse))
		(= gCurrentCursor 998)
		(cond 
			(riding
				(if movingRight
					(if (> upDown 2)
						(leah
							init:
							show:
							loop: 5
							posn: (+ (horse x?) 3) (- (horse y?) 17)
							setPri: 14
						)
						(egoRiding
							show:
							loop: 1
							posn: (- (horse x?) 6) (- (horse y?) 25)
							setPri: 15
						)
					else
						(leah
							init:
							show:
							loop: 5
							posn: (+ (horse x?) 3) (- (horse y?) 16)
							setPri: 14
						)
						(egoRiding
							show:
							loop: 1
							posn: (- (horse x?) 6) (- (horse y?) 24)
							setPri: 15
						)
					)
				else
					(leah
						init:
						show:
						loop: 4
						posn: (horse x?) 2 (- (horse y?) 17)
						setPri: 14
					)
				)
			)
			(inForest
				(top hide:)
				(bottom hide:)
				(left hide:)
				(right hide:)
				(if (> (leah x?) 50)
					(leah posn: (- (leah x?) 3) (+ (leah y?) 3))
					(egoRiding
						posn: (- (egoRiding x?) 3) (+ (egoRiding y?) 3)
					)
					(horse hide: posn: (- (horse x?) 3) (+ (horse y?) 3))
				else
					(gRoom newRoom: 600)
				)
			)
		)
		(++ upDown)
		(if (== upDown 4) (= upDown 0))
		(if passUp
			(soldier posn: (- (soldier x?) 3) (soldier y?))
			(soldier2 posn: (- (soldier2 x?) 5) (soldier2 y?))
			(if (< (soldier x?) 140)
				(soldier setCycle: Fwd loop: 0)
				(if (< (soldier x?) 70)
					(soldier hide:)
					(if (< (soldier x?) 40)
						(soldier show: posn: 240 60 setCycle: CT loop: 2)
					)
				)
			)
			(if (< (soldier2 x?) 140)
				(soldier2 setCycle: Fwd loop: 0)
				(if (< (soldier2 x?) 70)
					(soldier2 hide:)
					(if (< (soldier2 x?) 40)
						(soldier2 show: posn: 240 110 setCycle: CT loop: 3)
					)
				)
			)
		else
			(soldier2 hide:)
			(soldier hide:)
		)
	)
)

(procedure (goToLandscape)
	(left
		view: 993
		setCycle: End
		cycleSpeed: 1
		setMotion: MoveTo 64 125
	)
	(right
		view: 993
		setCycle: End
		cycleSpeed: 1
		setMotion: MoveTo 243 125
	)
	(top setCycle: End cycleSpeed: 2 setMotion: MoveTo 154 22)
	(bottom
		setCycle: End
		cycleSpeed: 2
		ignoreActors:
		setMotion: MoveTo 154 114
	)
)

(procedure (goToPortrait)
	(left
		view: 992
		setCycle: Beg
		cycleSpeed: 2
		setMotion: MoveTo 109 125
	)
	(right
		view: 992
		setCycle: Beg
		cycleSpeed: 2
		setMotion: MoveTo 198 125
	)
	(top setCycle: Beg cycleSpeed: 2 setMotion: MoveTo 154 11)
	(bottom
		setCycle: Beg
		cycleSpeed: 2
		ignoreActors:
		setMotion: MoveTo 154 125
	)
)

(instance horse of Act
	(properties
		y 90
		x 150
		view 325
	)
)

(instance leah of Prop
	(properties
		y 150
		x 38
		view 318
		loop 4
	)
)

(instance egoRiding of Prop
	(properties
		y 150
		x 38
		view 427
	)
)

(instance left of Act
	(properties
		y 125
		x 109
		view 993
		loop 1
	)
)

(instance right of Act
	(properties
		y 125
		x 198
		view 993
	)
)

(instance top of Act
	(properties
		y 11
		x 154
		view 993
		loop 2
	)
)

(instance bottom of Act
	(properties
		y 125
		x 154
		view 993
		loop 2
	)
)

(instance soldier of Prop
	(properties
		y 125
		x 97
		view 323
		loop 2
	)
)

(instance soldier2 of Prop
	(properties
		y 178
		x 72
		view 333
		loop 3
	)
)
