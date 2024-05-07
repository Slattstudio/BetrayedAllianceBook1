;;; Sierra Script 1.0 - (do not remove this comment)
; +2 Score
(script# 44)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)

(public
	rm044 0
)

(local

; Inside the Tavern



	dartNum =  0
	boardOpen =  0
	myEvent
	whichPage =  0 ; reward 1, julyn 2, prizes 3, gameHelp 4, close 5
	sitting =  0
	talkingTo =  0 ; 0 no one, 1 girl, 2 old man, 3 sailor
	
)

(instance rm044 of Rm
	(properties
		picture scriptNumber
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 204)
		(SetUpEgo)
		(= gEgoRunning 0)
		(RunningCheck)
		
		
		
		(switch gPreviousRoomNumber
			(else 
				(gEgo init: posn: 83 149 loop: 3)
			)
			(28
				(gEgo
					init:
					posn: 83 160
					loop: 3
					setMotion: MoveTo 83 149 self
				)
				(gTheMusic number: 44 loop: -1 play:)
			)
			(47
				(gEgo
					init:
					posn: 230 145
					loop: 1
					setMotion: MoveTo 222 145
				)
			)
			(105
				(gEgo init: hide: posn: 168 135 ignoreControl: ctlWHITE)
				(egoAction init: show: posn: 168 135 ignoreActors:)
				(RoomScript changeState: 8)
			)
			(111
				(gEgo init: posn: 155 103 loop: 0)
			)
		)
		
		(server init: ignoreControl: ctlWHITE ignoreActors:)
		(dartMan
			init:
			setScript: dartScript
			ignoreControl: ctlWHITE
			ignoreActors:
		)
		(board init:)
		(oldMan
			init:
			setScript: drinkScript
			setPri: 9
			ignoreActors:
		)
		; (chair:init()ignoreActors())
		(dart1 init: hide: ignoreControl: ctlWHITE)
		(dart2 init: hide: ignoreControl: ctlWHITE)
		(dart3 init: hide: ignoreControl: ctlWHITE)
		(boardClose init: hide: ignoreActors: setPri: 15)
	)
)

; (dart4:init()hide()ignoreControl(ctlWHITE))

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState button)
		(= state mainState)
		(switch state
; (case 1 // entering
;                ProgramControl() (SetCursor(997 1) = gCurrentCursor 997)
;                (send gEgo:setMotion(MoveTo 83 149 self))
;            )
;            (case 2
;                PlayerControl() (SetCursor(999 1) = gCurrentCursor 999)
;            )
			(3           ; going to sit
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 167 135 RoomScript
					ignoreControl: ctlWHITE
				)
				(= sitting 1)
			)
			(4
				(gEgo hide:)
				(egoAction
					init:
					show:
					posn: (gEgo x?) (gEgo y?)
					setCycle: End RoomScript
					cycleSpeed: 1
					ignoreActors:
				)
			)
			(5
				; add a yes/no prompt to play chess
				(= gVertButton 1)
				(= button (Print 44 91 #title {Chess}
						#button {Yes} 1
						#button {No } 0
 					)
 				)
				(if button
				  	(PlayChess)	
				else
					(self cue:)  
				)
				(= gVertButton 0)
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
			)
			(6	; standing up
				(egoAction setCycle: Beg RoomScript)
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
			)
			(7
				(egoAction hide:)
				(gEgo
					show:
					loop: 3
					cel: 3
					posn: (egoAction x?) (egoAction y?)
					observeControl: ctlWHITE
				)
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(= sitting 0)
			)
			(8
				(egoAction setCycle: Beg RoomScript)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
			)
			(9
				(egoAction hide:)
				(gEgo show: loop: 3 cel: 3 observeControl: ctlWHITE)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(= sitting 0)
				(if (== g105Solved 1) 
					(PrintMan 44 20)
					(Print 44 92)
				)
			)
			; moving from top to bottom on the right side of the table
			(10
				(ProgramControl)(SetCursor 997 1)(= gCurrentCursor 997)
				(if (> (gEgo y?)  125) ; at the bottom
					(gEgo setMotion: MoveTo 194 111 self ignoreControl: ctlWHITE)
				else
					(gEgo setMotion: MoveTo 214 146 self ignoreControl: ctlWHITE)
				)		
			)
			(11
				(PlayerControl)(SetCursor 999 1)(= gCurrentCursor 999)	
				(gEgo observeControl: ctlWHITE)
				(= sitting 0)
			)
		)
	)
	                                ; #at -1 20 #title "Old man says:")
	(method (handleEvent pEvent button)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if boardOpen
				(switch whichPage
					(1        ; reward
						(Print 44 17 #width 280 #at -1 5 #font 4)
					)
					(2        ; julyn
						(Print 44 11 #width 280 #at -1 5 #font 4)
					)
					(3        ; prizes
						(= gWndColor 4) ; clMAROON
						(= gWndBack 11) ; clCYAN
						(Print 44 12 #width 280 #at -1 5 #font 4)
						(Print 44 13 #width 280 #at -1 25 #font 4)
						(= gWndColor 0) ; clBLACK
						(= gWndBack 15)
					)                 ; clWHITE
					(4        ; gamehelp
						(Print 44 18 #width 280 #at -1 5 #font 4)
					)
					(5 (CloseIt)) ; exit
				)
			)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(checkEvent
						pEvent
						(server nsLeft?)
						(server nsRight?)
						(server nsTop?)
						(server nsBottom?)
					)
					(PrintOther 44 0)
				else
					(if
						(checkEvent
							pEvent
							(board nsLeft?)
							(board nsRight?)
							(board nsTop?)
							(board nsBottom?)
						)
						(if (<= (gEgo distanceTo: board) 39)
							(= gMap 1)
							(= boardOpen 1)
							(boardClose show: setPri: 13 ignoreActors:)
							(page1 init: show: setPri: 13 ignoreActors:)
							(page2 init: show: setPri: 13 ignoreActors:)
							(page3 init: show: setPri: 13 ignoreActors:)
							(page4 init: show: setPri: 13 ignoreActors:)
							(page5 init: show: setPri: 13 ignoreActors:)
						else
							(PrintOther 44 1)
						)
					)
					; Print(44 1 #width 290 #at -1 20)
					(if
						(checkEvent
							pEvent
							(dartMan nsLeft?)
							(dartMan nsRight?)
							(dartMan nsTop?)
							(dartMan nsBottom?)
						)
						(PrintOther 44 3)
					)
					(if
						(==
							ctlNAVY
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                  ; dartboard
						(PrintOther 44 4)
					)
					(cond 
						((checkEvent pEvent 150 168 108 114) (PrintOther 44 5)) ; chessboard
						(
							(checkEvent
								pEvent
								(oldMan nsLeft?)
								(oldMan nsRight?)
								(oldMan nsTop?)
								(oldMan nsBottom?)
							)
							(PrintOther 44 2)
						)
					)
					(if
						(==
							ctlGREEN
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                   ; axe
						(PrintOther 44 6)
					)
					(if
						(==
							ctlTEAL
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                  ; broom
						(PrintOther 44 61)
					)
				)
			)
		)        ; end else for click on waiter
		(if (Said 'smell[/!*]')
			(PrintOther 44 87)	
		)
		(if (Said 'give/flower')
			(if (gEgo has: INV_FLOWER)
				(if (== talkingTo 1)
					(if (not [gFlowerGiven 7])
						(PrintRose 44 72)
						(= [gFlowerGiven 7] 1)
						(-- gFlowers)
						(if (== gFlowers 0) (gEgo put: 21))
						((gInv at: 21) count: gFlowers)
						(gGame changeScore: 2)
						(PrintRose 44 77)
					else
						(PrintOther 44 74)
					)
				else
					(PrintNCE)
				)
			else
				(PrintDHI)
			)
		)
		(if (Said 'wear,use,(put<on)/goggles')
			(if (gEgo has: INV_GOGGLES)
				(if gDisguised
					(Print 0 53)
				else
					(PrintOther 0 80)
					(= gDisguised 1)
					(gEgo setMotion: NULL)
				)
			else
				(PrintDHI)
			)
		)
		(if
			(or
				(Said 'give/ring[/rose,woman]')
				(Said 'give/rose,woman/ring')
				(Said 'propose[/marriage,rose,woman]')
			)
			(if (gEgo has: 18)
				;(proposeScript changeState: 1)	
				(if (<= (gEgo distanceTo: server) 39)
					(PrintRose 44 86)
				else
					(PrintNCE)
				)
			else
				(PrintDHI)
			)
		)
		(if (Said 'take, (pick<up) >')
			(if (Said '/broom')
				(PrintOther 44 60)
				(PrintOther 44 62)
			)
			(if (Said '/axe') (PrintOther 44 64))
			(if (Said '/pitchfork') (PrintOther 44 88))
		)
		(if (Said 'talk>')
			(if (Said '/man,patron')
				(if (> talkingTo 1) (PrintMan 44 37) else (PrintNCE)) ; #width 290 #at -1 12 #title "He says:")
			)
			(if (Said '/woman,girl,waitress,daughter,rose')
				(if (== talkingTo 1)
					(if gDisguised
						(PrintRose 44 79)
					else
						(PrintRose 44 38)
						(PrintRose 44 39)
					)
				else
					(PrintNCE)
				)
			)
		)
		(cond 
			(talkingTo
				(if (Said 'buy/potion')
					(if (== talkingTo 1) ; girl
						(if [gFlowerGiven 7]
							(if (and (> gFlask gFullFlask) gFlask) ; More flasks than full flasks? ; Have at least 1 flask?
								(if (> gGold 4)
									(PrintRose 44 16) ; #width 290 #at -1 12 #title "She says:")
									(= gGold (- gGold 5))
									(++ gFullFlask)
								else
									(Print 44 40)
								)
							else
								(Print 44 41)
							)
						else
							(PrintRose 44 76)
						)
					else
						(PrintMan 44 42)
					)
				)
				(if (Said 'buy/flask')
					(if (== talkingTo 1) ; girl
						(PrintRose 44 46)
					else             ; #width 290 #at -1 12 #title "She says:")
						(PrintMan 44 43)
					)
				)
				(if (Said '(ask<about)>')
					(if (Said '/woman,rose,name')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 78)
							(PrintRose 44 39)
						else
							(PrintMan 44 37)
						)
					)
					(if (Said '/mother,family,deborah')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 38)
							(PrintRose 44 39)
						else
							(PrintMan 44 37)
						)
					)
					(if (Said '/princess,cave')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 55)
						else
							(PrintMan 44 37)
						)
					)
					(if (Said '/tristan')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 81)
						else
							(PrintMan 44 37)
						)
					)
					(if (Said '/carmyle')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 56)
						else
							(PrintMan 44 37)
						)
					)
					(if (Said '/wizard')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 57)
						else
							(PrintMan 44 37)
						)
					)
					(if (Said '/father,jasper')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 47)
							(PrintRose 44 48)
						else
							(PrintMan 44 37)
						)
					)
					(if (Said '/chest')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 82)
							(PrintRose 44 83)	
						else
							(PrintMan 44 37)
						)
					)
					(if (Said '/key,lock')
						(if (== talkingTo 1) ; girl
							(if (or (gEgo has: 26) (== (IsOwnedBy 26 47) TRUE))
								(PrintRose 44 85)	
							else
								(PrintRose 44 84)
							)
						else
							(PrintMan 44 37)
						)
					)
					(if (Said '/letter')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 9)
							(PrintRose 44 66)
						else
							(PrintMan 44 37)
						)
					)
					(if (Said '/flask')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 49)
							(PrintRose 44 14)
						else
							(PrintMan 44 37)
						)
					)
					(if (Said '/titanite')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 15)
						else
							(PrintMan 44 37)
						)
					)
					(if (Said '/flower')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 75)
						else
							(PrintMan 44 37)
						)
					)
					(if (Said '/potion')
						(if (== talkingTo 1) ; girl
							(if [gFlowerGiven 7]
								(if (and (> gFlask gFullFlask) gFlask) ; More flasks than full flasks? ; Have at least 1 flask?
									(PrintRose 44 70)
								else
									(PrintRose 44 68)
								)
							else                    ; need a flask
								(PrintRose 44 44)
								(PrintRose 44 68) ; need a flask
								(PrintRose 44 69)
							)
						else                    ; need a flower
							(PrintMan 44 37)
						)
					)
					(if (Said '/book')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 54)
						else             ; I had a book on potion making. I think it's upstairs.
							(PrintMan 44 37)
						)
					)
					(if (Said '/tavern,bar')
						(if (== talkingTo 1) ; girl
							(PrintRose 44 58)
						else             ; I had a book on potion making. I think it's upstairs.
							(PrintMan 44 59)
						)
					)
					(if (Said '/chess,board')
						(if (== talkingTo 2) ; old man
							(if (not g105Solved)
								;(PrintMan 44 31)
								(= gVertButton 1)
								(= button (Print 44 91 #title {Chess}
										#button {Yes} 1
										#button {No } 0
				 					)
				 				)
								(if button
								  	(PlayChess)	
								else
									;(self cue:)  
								)
								(= gVertButton 0)
							else
								(PrintMan 44 20)
							)
						else
							(Print 44 43 #width 280 #at -1 10 #title {The reply:})
						)
					)
					(if (Said '/dart,game')
						(if (== talkingTo 3) ; sailor
							(PrintMan 44 45)
						else
							(Print 44 43 #width 280 #at -1 10 #title {The reply:})
						)
					)
					(if (Said '/*')
						(Print 44 43 #width 280 #at -1 10 #title {The reply:})
					)
				)
			)
			((Said '(ask<about)/*') (PrintNCE))
		)
		(if (Said 'run') (Print 0 88))
		(if (Said 'use/map') (Print 0 88))
		(if (Said 'look,read>')
			(if (Said '/(board<dart),dart') (PrintOther 44 22))
			(if (Said '/table,chess,(board<chess)')
				(if (not g105Solved)
					(PrintOther 44 26)
				else
					; Print(44 26 #width 280 #at -1 8)
					(PrintOther 44 27)
				)
			)
			; Print(44 27 #width 280 #at -1 8)
			(if (Said '/board,notice,wall')
				(cond 
					((<= (gEgo distanceTo: board) 39)
						(= gMap 1)
						(= boardOpen 1)
						(boardClose init: show: setPri: 13 ignoreActors:)
						(page1 init: show: setPri: 13 ignoreActors:)
						(page2 init: show: setPri: 13 ignoreActors:)
						(page3 init: show: setPri: 13 ignoreActors:)
						(page4 init: show: setPri: 13 ignoreActors:)
						(page5 init: show: setPri: 13 ignoreActors:)
					)
					(sitting
						(if (not g105Solved)
							(PrintMan 44 31)
							(gRoom newRoom: 105)
						else
							(Print 44 32)
						)
					)
					(else (PrintOther 44 1))
				)
			)

			(if (Said '/barrel') (PrintOther 44 63))
			(if (Said '/axe')
				(PrintOther 44 23)
				(PrintOther 44 24)
				(PrintOther 44 25)
			)

			(if (Said '/girl,bartender') (PrintOther 44 0))

			(if (Said '/sailor') (PrintOther 44 3))

			(if (Said '/man') (PrintOther 44 28))

			(if (Said '/fireplace') (PrintOther 44 80))
			(if (Said '/ashes') (PrintOther 44 89))
			(if (Said '[/!*]') (PrintOther 44 29))

		)
		(if (Said 'sit')
			(cond 
				(sitting (Print 44 7))
				((& (gEgo onControl:) ctlSILVER) (RoomScript changeState: 3))
				(else (PrintNCE))
			)
		)
		(if (Said 'stand')
			(if sitting
				(RoomScript changeState: 6)
			else
				(Print 44 7)
			)
		)
		(if (or (Said 'play[/game]')
				(Said 'challenge/man')
			)
			(cond 
				((== talkingTo 2)
					(PlayChess)
				)
				((<= (gEgo distanceTo: dartMan) 35)
					(if (> gGold 0)
						(if (> gDartsWon 3)
							(Print 44 8)
						else            ; #title "Sailor says:")
							(PrintMan 44 33) ; #at -1 10 #title "Sailor says:")
							(gRoom newRoom: 111)
						)
					else
						(Print 44 34)
					)
				)
				((<= (gEgo distanceTo: oldMan) 35) (Print 44 35))
				(else (PrintNCE))
			)
		)
		(if (or (Said 'play/chess')
				(Said 'help/man')
				(Said 'move/piece')
				)
			(PlayChess)
		)
		(if (Said 'play/dart')
			(if (<= (gEgo distanceTo: dartMan) 35)
				(if (> gGold 0)
					(if (> gDartsWon 2)
						(PrintMan 44 8)
					else
						(PrintMan 44 33)
						(gRoom newRoom: 111)
					)
				)
			else
				(PrintNCE)
			)
		)
		(if (Said 'play') (PrintOther 44 67))
	)
	
; (if( (Said('climb')) or (Said('use/ladder')) )
;           (if(== (send gEgo: onControl()) ctlSILVER)
;                    (LadderScript:changeState(3))
;            )(else
;                PrintNCE()
;            )
;       )
	(method (doit)
		(super doit:)
		(if boardOpen
			(= myEvent (Event new: evNULL))
			(cond 
				((checkPage page1 1))
				((checkPage page2 2))
				((checkPage page3 3))
				((checkPage page4 4))
				((checkPage page5 5))
				(else (selectorZero))
			)
			(if
				(checkEvent
					myEvent
					(- (boardClose nsLeft?) 7)
					(boardClose nsRight?)
					(+ (boardClose nsTop?) 10)
					(+ (boardClose nsBottom?) 10)
				)
			; selectorZero()
			else
				(= whichPage 5)
			)                     ; close
			(myEvent dispose:)
		)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 28)
		)
		(if (& (gEgo onControl:) ctlRED)
			(if (not sitting)
				(RoomScript changeState: 10)
				(= sitting 1)
			)
		)
		(if (& (gEgo onControl:) ctlYELLOW)
			(if (not sitting)
				(= sitting 1)
				(LadderScript changeState: 3)
			)
		)

		(cond 
			((& (gEgo onControl:) ctlBLUE) (= talkingTo 1))
			((& (gEgo onControl:) ctlNAVY) (= talkingTo 3)) ; woman
			(
				(or              ; sailor
					(& (gEgo onControl:) ctlGREY)
					(& (gEgo onControl:) ctlSILVER)
				)
				(= talkingTo 2)
			)
			(else (= talkingTo 0))   ; old man
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 10)
)

(procedure (PrintReply textRes textResIndex)
	(Print
		textRes
		textResIndex
		#width
		280
		#at
		-1
		20
		#title
		(Format {%s} textRes (+ talkingTo 50))
	)
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

(instance LadderScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(3       ; Moving up the ladder
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 290 106 self
					ignoreControl: ctlWHITE
				)
			)
			(4
				(gEgo setMotion: MoveTo 235 20 self)
			)
			(5
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= gEgoRunning 0)
				(RunningCheck)
				(gRoom newRoom: 47)
			)
		)
	)
)
(procedure (PlayChess)
	(if (not (== g105Solved 1))
		(if (== g105Solved -2)
			(PrintMan 105 4)		
		else
			(if (== g105Solved -1)
				(PrintMan 44 90)
			else 
				(PrintMan 44 31) ; #at -1 20 #title "Old man says:")
			)
			(gRoom newRoom: 105)
		)
	else
		(Print 44 32)
		(if sitting
			(RoomScript changeState: 6)	; stand up
		)
	)	
)
(procedure (CloseIt)
	(boardClose hide:)
	(page1 hide:)
	(page2 hide:)
	(page3 hide:)
	(page4 hide:)
	(page5 hide:)
	(= gMap 0)
	(= boardOpen 0)
)

(procedure (PrintRose textRes textResIndex)
	(= gWndColor 14)
	(= gWndBack 8)
	(Print
		textRes
		textResIndex
		#width
		280
		#at
		-1
		20
		#title
		{She says:}
	)
	(= gWndColor 0) ; clBLACK
	(= gWndBack 15)
)
                    ; clWHITE
(procedure (PrintMan textRes textResIndex)
	(Print
		textRes
		textResIndex
		#width
		280
		#at
		-1
		20
		#title
		{He says:}
	)
)

(procedure (dartThrow view)
	(view
		init:
		show:
		posn: 150 68
		xStep: 8
		setMotion: MoveTo (Random 206 216) (Random 71 86)
		ignoreActors:
		ignoreControl: ctlWHITE
	)
)

(procedure (checkPage view pageWin)
	(if
		(checkEvent
			myEvent
			(+ (view nsLeft?) 3)
			(+ (view nsRight?) 3)
			(view nsTop?)
			(+ (view nsBottom?) 7)
		)
		(selectorZero)
		(view cel: 1 setPri: 15)
		(= whichPage pageWin)
	)
)

(procedure (selectorZero)
	(= whichPage 0)
	(page1 cel: 0)
	(page2 cel: 0)
	(page3 cel: 0)
	(page4 cel: 0)
	(page5 cel: 0)
)

(instance dartScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
				(= cycles 10)
				(dartMan loop: 4 cel: 0)
			)
			(1
				(= cycles 6)
				(dartMan cel: 1)
			)
			(2
				(= cycles 6)
				(++ dartNum)
				(dartMan cel: 2) ; Throwing Dart
				(cond 
					((== dartNum 1) (dartThrow dart1))
					((== dartNum 2) (dartThrow dart2))
					; (dart1:init()show()posn(127 100)xStep(8)setMotion(MoveTo Random(186 200) Random(85 105))ignoreActors()ignoreControl(ctlWHITE))
					((== dartNum 3) (dartThrow dart3))
				; (dart2:init()show()posn(127 100)xStep(8)setMotion(MoveTo Random(186 200) Random(85 105))ignoreActors()ignoreControl(ctlWHITE))
				)
			)
			; (dart3:init()show()posn(127 100)xStep(8)setMotion(MoveTo Random(186 200) Random(85 105))ignoreActors()ignoreControl(ctlWHITE))
; (else
;                            (if(== dartNum 4)
;                                dartThrow(dart4)
;                                //dart4:init()show()posn(127 100)xStep(8)setMotion(MoveTo Random(186 200) Random(85 105))ignoreActors()ignoreControl(ctlWHITE))
;                            )
;                        )
			(3
				(= cycles 5)
				(if (== dartNum 3) (self changeState: 6))
				(dartMan cel: 0)
			)
			(4
				(= cycles 5)
				(dartMan cel: 1)
			)
			(5
				(if (> 5 (Random 1 25))
					(self changeState: 1)
				else
					(self changeState: 3)
				)
			)
			(6
				(= cycles 10)
				(dartMan loop: 0)
			)
			(7
				(dartMan
					setCycle: Walk
					setMotion: MoveTo 200 104 self
					ignoreActors:
				)
			)
			(8 (= cycles 6))
			(9
				(= cycles 6)
				(dart1 hide:)
				(dart2 hide:)
				(dart3 hide:)
				(= dartNum 0)
			)
			(10
				(dartMan setMotion: MoveTo 138 104 self)
			)
			(11 (self changeState: 0))
		)
	)
)

(instance drinkScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles (Random 15 60)))
			(1
				(oldMan setCycle: End self cycleSpeed: 2)
			)
			(2
				(= cycles (Random 4 12))
				(oldMan loop: 1 setCycle: Fwd)
			)
			(3
				(oldMan loop: 0 cel: 2 setCycle: Beg self)
			)
			(4 (drinkScript changeState: 0))
		)
	)
)

(instance server of Act
	(properties
		y 142
		x 27
		view 312
	)
)

(instance dartMan of Act
	(properties
		y 104
		x 138
		view 303
		loop 4
	)
)

(instance dart1 of Act
	(properties
		y 68
		x 150
		view 46
	)
)

(instance dart2 of Act
	(properties
		y 68
		x 150
		view 46
	)
)

(instance dart3 of Act
	(properties
		y 68
		x 150
		view 46
	)
)

(instance board of Prop
	(properties
		y 79
		x 127
		view 45
	)
)

(instance oldMan of Prop
	(properties
		y 109
		x 149
		view 39
	)
)

; (instance chair of Prop
;    (properties y 158 x 227 view 47)
; )
(instance boardClose of Prop
	(properties
		y 90
		x 155
		view 956
	)
)

(instance page1 of Prop
	(properties
		y 54
		x 115
		view 956
		loop 2
	)
)

(instance page2 of Prop
	(properties
		y 78
		x 154
		view 956
		loop 3
	)
)

(instance page3 of Prop
	(properties
		y 81
		x 195
		view 956
		loop 4
	)
)

(instance page4 of Prop
	(properties
		y 70
		x 121
		view 956
		loop 5
	)
)

(instance page5 of Prop
	(properties
		y 31
		x 212
		view 956
		loop 6
	)
)

(instance egoAction of Prop
	(properties
		y 1
		x 1
		view 408
	)
)
