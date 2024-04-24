;;; Sierra Script 1.0 - (do not remove this comment)

(script# 900)
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
	rm900 0
)

(local
; Prologue Intro



	; (use "sciaudio")
	htext =  0
	htext2 =  0
	htext3 =  0
	
	escText1 = 0
	escText2 = 0
	escText3 = 0
	
	stateSwitch =  0
	snd
)

(instance rm900 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		; (MenuBar:hide())
		(TheMenuBar state: DISABLED)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else )
		)
		(SetUpEgo)
		; (send gEgo:init()hide())
		(= gArcStl 1)
		(left init: loop: 1 posn: 113 125 setCycle: CT setScript: skipIntro)
		(right init: loop: 0 posn: 202 125 setCycle: CT)
		(top init: loop: 2)
		(bottom init: loop: 2)

		(gTheMusic prevSignal: 0 stop:)
		
		(gGame setSpeed: 5)
		(SetCursor 998 (HaveMouse))
		(= gCurrentCursor 998)
	
	
		(= escText1 (Display
			{Press}
			dsCOORD 260 2
			dsCOLOUR 1
			dsBACKGROUND clTRANSPARENT
			dsFONT 4
			dsSAVEPIXELS
		))
		(= escText2 (Display
			{Esc}
			dsCOORD 288 2
			dsCOLOUR 9
			dsBACKGROUND clTRANSPARENT
			dsFONT 4
			dsSAVEPIXELS
		))
		(= escText3 (Display
			{to skip}
			dsCOORD 267 12
			dsCOLOUR 1
			dsBACKGROUND clTRANSPARENT
			dsFONT 4
			dsSAVEPIXELS
		))
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		(SetCursor 998 (HaveMouse))
		(= gCurrentCursor 998)
	)
	
	(method (changeState intro)
		(= state intro)
		(switch state
			(0 (= seconds 2))
			(1
				
				(= seconds 1)    ; 3
				(goToLandscape)
				(I init: cel: 7 setCycle: Beg)
				(= htext
					(Display
						900
						0
						dsCOORD
						61
						130
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						200
						dsSAVEPIXELS
					)
				)
			)
			(2
				(= seconds 4)    ; 7
				(peace init: setCycle: End)
				
				
			)
			(3
				(= seconds 1)    ; 8
				(peace setCycle: Beg)
				(I setCycle: End cycleSpeed: 1)
				
				(Display {} dsRESTOREPIXELS escText1)
				(Display {} dsRESTOREPIXELS escText2)
				(Display {} dsRESTOREPIXELS escText3)
			)
			(4
				(= seconds 1)    ; 9
				(Display {} dsRESTOREPIXELS htext)
			)
			(5
				(= seconds 1)    ; 10
				(goToPortrait)
				(= htext
					(Display
						900
						1
						dsCOORD
						52
						130
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						210
						dsSAVEPIXELS
					)
				)
			)
			(6
				(= seconds 4)    ; 14
				(clock init: setCycle: End cycleSpeed: 1)
			)
			(7
				(= seconds 1)    ; 15
				(Display {} dsRESTOREPIXELS htext)
				(goToLandscape)
			)
			(8
				(= seconds 3)    ; 18
				(peace loop: 1 cel: 0 show: posn: 116 103 setCycle: End)
				(= htext
					(Display
						900
						2
						dsCOORD
						54
						130
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						210
						dsSAVEPIXELS
					)
				)
			)
			(9
				(= seconds 4)    ; 22
				(= htext2
					(Display
						900
						3
						dsCOORD
						74
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
				(ambition init: loop: 2 setCycle: End)
			)
			(10
				(= seconds 1)     ; 23
				(Display {} dsRESTOREPIXELS htext)
				(Display {} dsRESTOREPIXELS htext2)
			)
			(11
				(= seconds 4)     ; 27
				(peace setCycle: Beg)
				(ambition init: setCycle: Beg)
				(= htext
					(Display
						900
						4
						dsCOORD
						54
						130
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						210
						dsSAVEPIXELS
					)
				)
			)
			(12
				(= seconds 1)     ; 28
				(Display {} dsRESTOREPIXELS htext)
			)
			(13
				(= seconds 3)     ; 31
				(flags init: setCycle: End)
				(= htext
					(Display
						900
						5
						dsCOORD
						54
						130
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						210
						dsSAVEPIXELS
					)
				)
			)
			(14
				(= seconds 4)     ; 35
				(= htext2
					(Display
						900
						6
						dsCOORD
						74
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
			(15
				(= seconds 5)     ; 40
				(= htext3
					(Display
						900
						7
						dsCOORD
						34
						147
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						250
						dsSAVEPIXELS
					)
				)
			)
			(16
				(= seconds 1)     ; 41
				(if (not stateSwitch)
					(flags setCycle: Beg)
					(Display {} dsRESTOREPIXELS htext)
					(Display {} dsRESTOREPIXELS htext2)
					(Display {} dsRESTOREPIXELS htext3)
					(= stateSwitch 1)
				)
			)
			(17
				(= seconds 3)     ; 44
				(= htext
					(Display
						900
						8
						dsCOORD
						64
						130
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						190
						dsSAVEPIXELS
					)
				)
			)
			(18
				(= seconds 1)     ; 45
				(Display {} dsRESTOREPIXELS htext)
			)
			(19
				(= seconds 4)     ; 49
				(peace
					view: 987
					loop: 0
					cel: 0
					posn: 158 103
					setCycle: End
				)
				(= htext
					(Display
						900
						9
						dsCOORD
						64
						130
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						190
						dsSAVEPIXELS
					)
				)
			)
			(20
				(= seconds 1)     ; 50
				(Display {} dsRESTOREPIXELS htext)
			)
			(21
				(= seconds 3)     ; 53
				(= htext
					(Display
						900
						10
						dsCOORD
						59
						130
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						210
						dsSAVEPIXELS
					)
				)
			)
			(22
				(= seconds 1)     ; 54
				(goToPortrait)
				(peace setCycle: Beg)
				(Display {} dsRESTOREPIXELS htext)
			)
			(23
				(= seconds 8)     ; 58
				(Julyn init: setCycle: End)
				(= htext
					(Display
						900
						11
						dsCOORD
						54
						137
						dsALIGN
						alCENTER
						dsCOLOUR
						clCYAN
						dsBACKGROUND
						clBLACK
						dsWIDTH
						210
						dsSAVEPIXELS
					)
				)
			)
			(24 
				
				(skipIntro changeState: 2)
				
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON))
; (if((> (send pEvent:x) (skip:nsLeft))and
;                (< (send pEvent:x) (skip:nsRight))and
;                (> (send pEvent:y) (skip:nsTop))and
;                (< (send pEvent:y) (skip:nsBottom)))
;                = gArcStl 0
;                (send gRoom:newRoom(10))
;                (skipIntro:changeState(2))
;            )
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_ESC)
				(gTheMusic fade:)
				(= gArcStl 0)
				(gRoom newRoom: 10)
			)
		)
	)
)

; (skipIntro:changeState(2))
(instance skipIntro of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0	(= cycles 10)
			)
			(1
				(gTheMusic number: 901 play:)	
			)
			
			(2
				(= cycles 15)
				(gTheMusic fade:)
			)
			; (send gTheMusic:prevSignal(0)stop())
			(3
				(= gArcStl 0)
				(gRoom newRoom: 10)
			)
		)
	)
)

(procedure (goToLandscape)
	(left
		view: 993
		setCycle: End
		cycleSpeed: 1
		setMotion: MoveTo 68 125
	)
	(right
		view: 993
		setCycle: End
		cycleSpeed: 1
		setMotion: MoveTo 247 125
	)
	(top setCycle: End cycleSpeed: 2 setMotion: MoveTo 158 22)
	(bottom
		setCycle: End
		cycleSpeed: 2
		ignoreActors:
		setMotion: MoveTo 158 114
	)
)

(procedure (goToPortrait)
	(left
		view: 992
		setCycle: Beg
		cycleSpeed: 2
		setMotion: MoveTo 113 125
	)
	(right
		view: 992
		setCycle: Beg
		cycleSpeed: 2
		setMotion: MoveTo 202 125
	)
	(top setCycle: Beg cycleSpeed: 2 setMotion: MoveTo 158 11)
	(bottom
		setCycle: Beg
		cycleSpeed: 2
		ignoreActors:
		setMotion: MoveTo 158 125
	)
)

(instance left of Act
	(properties
		y 125
		x 113
		view 993
	)
)

(instance right of Act
	(properties
		y 125
		x 202
		view 993
	)
)

(instance top of Act
	(properties
		y 11
		x 158
		view 993
	)
)

(instance bottom of Act
	(properties
		y 125
		x 158
		view 993
	)
)

(instance Julyn of Prop
	(properties
		y 114
		x 158
		view 991
	)
)

(instance peace of Prop
	(properties
		y 103
		x 158
		view 990
	)
)

(instance ambition of Prop
	(properties
		y 103
		x 200
		view 990
	)
)

(instance flags of Prop
	(properties
		y 103
		x 158
		view 988
	)
)

(instance clock of Prop
	(properties
		y 60
		x 158
		view 989
	)
)

(instance I of Prop
	(properties
		y 149
		x 58
		view 996
	)
)
