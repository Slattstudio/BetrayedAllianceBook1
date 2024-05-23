;;; Sierra Script 1.0 - (do not remove this comment)
; +12 Score //
(script# 46)
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
	rm046 0
)

(local

; Inside Library



	; (use "sciaudio")
	comingIn =  1
	rando
	left =  0
	right =  0
	walking =  0
	angleWalk =  0
	bookTemp
	i
	[bookString 100]
	; snd
	allBookCheck
	takingTest =  0
	testImmediately =  0
	
	combatHint = 0
	
	armorNum = 0
	armorI
)                       ; determines if librarian is already at the testing spot

(instance rm046 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 45
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 204)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 160 182 loop: 3)
				(= comingIn 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(= gEgoRunning 0)
		(RunningCheck)
		
		(gTheMusic number: 62 loop: -1 play:)
		
		(librarian
			init:
			loop: 2
			setCycle: Walk
			setScript: LibrarianScript
			ignoreControl: ctlWHITE
			setPri: 10
		)
		(books1
			init:
			setPri: 12
			setScript: liftBooks
			ignoreControl: ctlWHITE
			ignoreActors:
		)
		(books2
			init:
			setPri: 12
			ignoreControl: ctlWHITE
			ignoreActors:
		)
		(flower init: hide: setPri: 11)
		(alterEgo init: hide: ignoreActors: setScript: proposeScript)
		
		(if [gFlowerGiven 4] (flower show:))
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState &tmp [buffer 100])
		(= state mainState)
		(switch state
			(0 (= cycles 1))
			(1
				(liftBooks changeState: 1)
; = snd aud (send snd:
;                    command("playx")
;                    fileName("music\\library.mp3")
;                    volume("70")
;                    loopCount("0")
;                    init()
;                )
				; (send gTheMusic:prevSignal(0)stop()number(46)loop(-1)play())
				(if comingIn
					(ProgramControl)
					(SetCursor 997 (HaveMouse))
					(= gCurrentCursor 997)
					(gEgo setMotion: MoveTo 160 165 RoomScript)
				)
			)
			(2
				(PlayerControl)
				(= comingIn 0)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(3
				(= seconds 3)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(Print 46 4 #width 280 #at -1 20)
				(Print 46 5 #width 280 #at -1 20)
			)
			(4
				(FormatPrint
					{After what seemed like an eternity you finish your exam. Your score is %u out of 100.}
					gInt
				)
				(if (> gInt 0)
					(if (not (gEgo has: 14))
						(gEgo get: 14)
						(PrintHans 46 6) ; #title "Librarian:" #width 280 #at -1 20)
						(= gWndColor 1)
						(= gWndBack 11)
						(Print 46 7 #title {Librarian:} #at -1 20 #icon 266)
						(= gWndColor 0)
						(= gWndBack 15)
						(gGame changeScore: 1)
					)
					(if (> gInt 19)
						(if (not g46Flask)   ; changed from a flask to Titanite
							(= gWndColor 1)
							(= gWndBack 11)
							(Print 46 38 #title {Librarian:} #at -1 20 #icon 210)
							(= gWndColor 0)
							(= gWndBack 15)
							(gGame changeScore: 2)
							(gEgo get: 23)
							(= g46Flask 1)
						)
						(if (> gInt 34)
							(if (not [gArmor 3])
								(PrintHans 46 39) ; #title "Librarian:" #width 280 #at -1 20)
								(Print 46 102 #title {Armor Bonus:} #icon 277 1)
								(= gStr (+ gStr 3))
								(= gDef (+ gDef 5))
								(= [gArmor 3] 1)
								
								(if (not (gEgo has: 27))
									(gEgo get: 27)
								)
								(for ( (= armorI 0)) (< armorI 4)  ( (++ armorI)) (if (> [gArmor armorI] 0) (++ armorNum))) ; calculate how many armor piece
								((gInv at: 27) count: armorNum)
								(= gArmorLoop 3)	

								(gGame changeScore: 3)
							)
						)
					)
				)
				(PrintHans 46 8) ; #title "Librarian:" #width 280 #at -1 20)
				(PlayerControl)
				(= takingTest 0)
				; (= testImmediately 0)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gEgo observeControl: ctlWHITE)
			)
			(5
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(if testImmediately
					(gEgo
						setMotion: MoveTo 96 152 self
						ignoreControl: ctlWHITE
					)
				else
					(gEgo setMotion: MoveTo 96 152 ignoreControl: ctlWHITE)
				)
			)
			(6
				(gEgo loop: 1)
				(RoomScript changeState: 3)
			)
			; walking to the upper area
			(7
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(if (> (gEgo x?) 160)     ; ego on the right
					(gEgo
						setMotion: MoveTo 244 124 self
						ignoreControl: ctlWHITE
					)
				else   ; ego on the left
					(gEgo
						setMotion: MoveTo 73 124 self
						ignoreControl: ctlWHITE
					)
				)
			)
			(8
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gEgo observeControl: ctlWHITE)
				(if (> (gEgo x?) 160)
					(gEgo loop: 1)
				else
					(gEgo loop: 0)
				)
				(= angleWalk 0)
			)
			; walking to the lower area
			(9
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(if (< (gEgo x?) 160)     ; ego on the right
					(gEgo
						setMotion: MoveTo 37 166 self
						ignoreControl: ctlWHITE
					)
				else   ; ego on the left
					(gEgo
						setMotion: MoveTo 272 166 self
						ignoreControl: ctlWHITE
					)
				)
			)
			(10
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gEgo observeControl: ctlWHITE)
				(if (> (gEgo x?) 160)
					(gEgo loop: 1)
				else
					(gEgo loop: 0)
				)
				(= angleWalk 0)
			)
			; walking to the scroll to read it
			(11         ; walk to scroll
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(= angleWalk 1)
				(gEgo
					setMotion: MoveTo 54 140 self
					ignoreControl: ctlWHITE
				)
			)
			(12
				(= cycles 2)        ; face the scroll
				(gEgo loop: 1)
			)
			(13
				(Print
					46
					90
					#title
					{The Arcane Symbol:}
					#width
					120
					#at
					-1
					-1
					#font
					4
				)
				(gEgo setMotion: MoveTo 35 167 self)
			)
			(14
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gEgo observeControl: ctlWHITE)
				(= angleWalk 0)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(==
						ctlLIME
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)
					(if (& (gEgo onControl:) ctlRED)
						(self changeState: 11)
					else
						(PrintOther 46 91)
					)
				)
				(if
					(checkEvent
						pEvent
						(librarian nsLeft?)
						(librarian nsRight?)
						(librarian nsTop?)
						(librarian nsBottom?)
					)                                                                                                   ; Librarian
					(PrintOther 46 9)
				)
				(if (checkEvent pEvent 98 109 112 120)  ; BA manual
					(Print 46 105 #icon 981 #width 140)
					(Print 46 106 #font 4 #at -1 140)
				)
				(if (checkEvent pEvent 284 301 121 129)  ; Sierra Adventure
					(PrintOther 46 104)
				)                       
				(if (checkEvent pEvent 69 79 127 136)  ; IQ Test on desk
					(PrintOther 46 11)
				)                     ; #width 280 #at -1 20)
				(if (checkEvent pEvent 227 246 126 137)  ; Book about combat
					(PrintOther 46 17) ; #width 280 #at -1 8)
					(if (& (gEgo onControl:) ctlGREY)
						(if combatHint
							(PrintOther 46 110)
							(-- combatHint)	
						else
							(PrintOther 46 19)
							(++ combatHint)	
						)
					else
						(PrintOther 46 87)
					)
				)
				(if (checkEvent pEvent 191 204 116 121) ; list of missing books
					(ListMissingBooks)
				)                      
				(if
					(or
						(checkEvent pEvent 61 121 44 107) ; books left
						(checkEvent pEvent 201 263 44 107)
					)                                   ; books right
					(= bookTemp (Random 20 37))
					; (if(>(bookTemp Random(0 50)) )
					(Print 46 bookTemp #width 280 #at -1 10)
				)
			)
		)
		; )
		(if (Said 'run') (Print 0 88))
		(if (or (Said 'look,use,read,open/portal,map')
				(Said 'map'))
			(Print 0 88)
		)
		(if (Said 'wear,use,(put<on)/goggles')
			(if (gEgo has: INV_GOGGLES)
				(if gDisguised
					(Print 0 53)
				else
					(PrintOther 0 80)
					(= gDisguised 1)
					(= gEgoRunning 0)
					(RunningCheck)
					(gEgo setMotion: NULL)
				)
			else
				(PrintDHI)
			)
		)
		(if (Said 'smell[/!*]')
			(PrintOther 46 114)	
		)
		(if (Said 'give/flower')
			(if (gEgo has: 21)
				(if [gFlowerGiven 4]
					(PrintOther 30 48)
				else
					(PrintHans 46 89)
					(= [gFlowerGiven 4] 1)
					(-- gFlowers)
					(if (== gFlowers 0) (gEgo put: 21))
					((gInv at: 21) count: gFlowers)
					(flower show:)
					(gGame changeScore: 1)
				)
			else
				(PrintDHI)
			)
		)
		(if
			(or
				(Said 'give/ring[/man,hans,librarian]')
				(Said 'give/man,hans,librarian/ring')
				(Said 'propose[/marriage,man,hans,librarian]')
			)
			(if (gEgo has: 18)
				; kneeling animation
				(proposeScript changeState: 1)	
			else
				(PrintDHI)
			)
		)
		(if (Said 'look,read>')
			(if (Said '/flower')
				(if [gFlowerGiven 4]
					(PrintOther 0 98)
				else
					(PrintOther 0 97)
				)
			)
			(if (Said '/man,librarian') (PrintOther 46 14)) ; #width 280 #at -1 8)
			(if (Said '/scroll')
				(if (& (gEgo onControl:) ctlRED)
					(self changeState: 11)
				else
					(PrintOther 46 91)
				)
			)
			(if (Said '/paper,note')
				(if (> (gEgo x?) 158)	; closer to book list
					(if (or (& (gEgo onControl:) ctlBROWN) (& (gEgo onControl:) ctlFUCHSIA))
						(ListMissingBooks)	
					else
						(PrintOther 46 109)
					)	
				else
					(PrintOther 46 15) ; #width 280 #at -1 8)
					(PrintOther 46 16)
				)
			) 
			(if (Said '/quiz')
				(PrintOther 46 15) ; #width 280 #at -1 8)
				(PrintOther 46 16)
			)                     ; #title "You think:" #width 280 #at -1 8)
			(if (Said '/window') (PrintOther 46 13)) ; #width 280 #at -1 8)
			(if (Said '/book')
				(if (& (gEgo onControl:) ctlGREY)
					(PrintOther 46 17)
					(if combatHint
						(PrintOther 46 110)
						(-- combatHint)	
					else
						(PrintOther 46 19)
						(++ combatHint)	
					)
				else
					(if (& (gEgo onControl:) ctlPURPLE)
						(PrintOther 46 104)	
					else
						(if (or (& (gEgo onControl:) ctlMAROON) (& (gEgo onControl:) ctlCYAN))
							(Print 46 105 #icon 981 #width 140)
							(Print 46 106 #font 4 #at -1 140)		
						else
							(if (< (gEgo y?) 136) ; ego up around the bookcases
								; random book
								(= bookTemp (Random 20 37))
								(Print 46 bookTemp #width 280 #at -1 10)
							else
								(PrintOther 46 88)
							)
						)
					)
				)
			)
			(if (Said '/table,counter') (PrintOther 46 85)) ; #width 280 #at -1 8)
			(if (Said '/list')
				(Print {Missing Books:})
				(= allBookCheck 5)
				(for ( (= i 0)) (< i 5)  ( (++ i)) (if (< [gMissingBooks i] 2)
					(switch i
						(0 (PrintOther 46 54))    ; #at -1 20)
						; ++allBookCheck
						(1 (PrintOther 46 55))    ; #at -1 20)
						; ++allBookCheck
						(2 (PrintOther 46 56))    ; #at -1 20)
						; ++allBookCheck
						(3 (PrintOther 46 57))    ; #at -1 20)
						; ++allBookCheck
						(4 (PrintOther 46 58))
					)
				else                              ; #at -1 20)
					; ++allBookCheck
					(-- allBookCheck)
					(if (== allBookCheck 0) (Print 46 81))
				))
			)
			(if (Said '[/!*]') (PrintOther 46 18))
			; this will handle just "look" by itself ; #width 280 #at -1 8)
		)
		(if (Said '(tell<about),give,return/book')
			(Print 46 60)
			(for ( (= i 0)) (< i 5)  ( (++ i)) (if (== [gMissingBooks i] 1)
				(switch i
					(0
						(PrintHans 46 61)    ; #title "Librarian:" #width 280 #at -1 20)
						(= [gMissingBooks 0] 2)
						(gGame changeScore: 1)
					)
					(1
						(PrintHans 46 62)    ; #title "Librarian:" #width 280 #at -1 20)
						(= [gMissingBooks 1] 2)
						(gGame changeScore: 1)
					)
					(2
						(PrintHans 46 63)    ; #title "Librarian:" #width 280 #at -1 20)
						(= [gMissingBooks 2] 2)
						(gGame changeScore: 1)
					)
					(3
						(PrintHans 46 64)    ; #title "Librarian:" #width 280 #at -1 20)
						(= [gMissingBooks 3] 2)
						(gGame changeScore: 1)
					)
					(4
						(PrintHans 46 65)    ; #title "Librarian:" #width 280 #at -1 20)
						(= [gMissingBooks 4] 2)
						(gGame changeScore: 1)
					)
				)
			))
			(bookCheck)
			(PrintHans 46 59)
		)                    ; #title "Librarian:" #width 280 #at -1 20)
		(if (Said '(ask<about)>')
			(if (Said '/(book<(missing,lost)),list')
				(PrintHans 46 67) ; #title "Librarian:" #width 280 #at -1 20)
				(for ( (= i 0)) (< i 5)  ( (++ i)) (if (< [gMissingBooks i] 2)
					(switch i
						(0 (PrintHans 46 73))    ; #title "Librarian:" #width 280 #at -1 20) //A young woman borrowed New Notions on Lotions and Potions best I can remember. She seemed nervous her mom would find out.
						(1 (PrintHans 46 74))    ; #title "Librarian:" #width 280 #at -1 20) // Last I saw the book on King Herman's Mustache, a portly man who smelled a little fishy was checking it out.
						(2 (PrintHans 46 75))    ; #title "Librarian:" #width 280 #at -1 20) // The book on Myths and Legends was an interesting case. The man who checked it out covered his face and head with a hood. He also showed interest in a book on chess endgames.
						(3 (PrintHans 46 76))    ; #title "Librarian:" #width 280 #at -1 20) // I have to say, the woman who checked out the book on the history of the Carmyle family may have been the most gorgeous woman I've ever seen. And what beautiful hair!
						(4 (PrintHans 46 77))
					)
				))                               ; #title "Librarian:" #width 280 #at -1 20) // The book, The Curse of the Relic, has been lost for years. I can't even recall when.
; (else
;                        --allBookCheck
;                        (if(== allBookCheck 0)
;                            Print("You've returned every missing book! Even if you fail your quest, at least you're a hero to the library!" #title "Librarian:" #at -1 20)
;                        )
;                    )
				(bookCheck)
			)
			(if (Said '/exam,test') (PrintHans 46 1)) ; #title "Librarian:" #width 280 #at -1 20)
			(if (Said '/prize,reward') (PrintHans 46 2)) ; #title "Librarian:" #width 150 #at -1 20)
			(if (Said '/weights,lifting,gym') (PrintHans 46 107))
			(if (Said '/volume,book')
				(PrintHans 46 3) ; #title "Librarian:" #width 280 #at -1 20)
				(PrintHans 46 41)
			)                    ; #title "Librarian:" #width 280 #at -1 20)
			(if (Said '/ruler,mustache,herman')
				(PrintHans 46 43) ; #title "Librarian:" #width 280 #at -1 20)
				(if (== [gMissingBooks 1] 0) (PrintHans 46 44))
			)                        ; #title "Librarian:" #width 280 #at -1 20)
			(if (Said '/wizard') (PrintHans 46 45)) ; #title "Librarian:" #width 280 #at -1 20)
			(if (Said '/magnet,metal') (PrintHans 46 80)) ; #title "Librarian:" #width 280 #at -1 20)
			(if (Said '/muscle') (PrintHans 46 84)) ; #title "Librarian:" #width 280 #at -1 20) // There are different kinds of strength. A powerful body keeps a strong mind alive. The one serves the other.
			(if (Said '/princess') (PrintHans 46 46)) ; #title "Librarian:" #width 280 #at -1 20)
			(if (Said '/prince') (PrintHans 46 47)) ; #title "Librarian:" #width 280 #at -1 20)
			(if (Said '/carmyle') (PrintHans 46 48)) ; #title "Librarian:" #width 280 #at -1 20)
			(if (Said '/cave') (PrintHans 46 49)) ; #title "Librarian:" #width 280 #at -1 20)
			(if (Said '/ogre,monster') (PrintHans 46 78)) ; #title "Librarian:" #width 280 #at -1 20)
			(if (Said '/legend')
				(PrintHans 46 50) ; #title "Librarian:" #width 280 #at -1 20)
				(PrintHans 46 51) ; #title "Librarian:" #width 280 #at -1 20)
				(PrintHans 46 52)
			)    
			(if (Said '/shelah,kingdom') (PrintHans 46 113))              
			(if (Said '/ishvi,war') (PrintHans 46 112))
			; peoples of shelah
			(if (Said '/king') (PrintHans 46 111))
			(if (Said '/bobby') (PrintHans 46 92))
			(if (Said '/leah') (PrintHans 46 93))
			(if (Said '/sammy') (PrintHans 0 91))
			(if (Said '/deborah, woman') (PrintHans 46 94))
			(if (Said '/rose') (PrintHans 46 95))
			(if (Said '/sarah') (PrintHans 46 96))
			(if (Said '/man,hans') (PrintHans 46 97)) ; #title "Librarian:" #width 280 #at -1 20)
			(if (Said '/name') (PrintHans 46 97))
			(if (Said '/lex') (PrintHans 0 91))
			(if (Said '/colin') (PrintHans 46 98))
			(if (Said '/longeau') (PrintHans 46 99))
			(if (Said '/moon,tor') (PrintHans 0 91))
			(if (Said '/gyre') (PrintHans 46 100))
			(if (Said '/*') (PrintHans 46 53))
		)                        ; #title "Librarian:" #width 280 #at -1 20)
		(if (Said 'take,(pick<up)>')
			(if (Said '/book') (PrintOther 46 88))
		)
		(if (Said 'take/exam,test')
			(if
				(or
					(& (gEgo onControl:) ctlMAROON)
					(& (gEgo onControl:) ctlSILVER)
					(& (gEgo onControl:) ctlGREY)
					(& (gEgo onControl:) ctlBROWN)
				)
				(= takingTest 1)
				(PrintHans 46 86)
				(RoomScript changeState: 5)
			else
				(PrintNCE)
			)
		)
		(if (Said 'open/book') (Print 46 66 #at -1 20))
		(if (Said 'talk/man,librarian')
			(if gDisguised (PrintHans 46 101) else (PrintHans 46 0))
		)
	)
	                            ; #title "Librarian:" #width 280 #at -1 20)
	(method (doit)
		(super doit:)
		(cond 
			((== (gEgo onControl:) ctlSILVER) (gEgo setPri: 13))
			; (send gRoom:newRoom(45))
			((& (gEgo onControl:) ctlBROWN) (gEgo setPri: 14))
			(else (gEgo setPri: -1))
		)
		(alterEgo setPri: (gEgo priority?))
		(if (== (gEgo onControl:) ctlBLUE)       ; walking to the top
			(if (not angleWalk)
				(RoomScript changeState: 7)
				(= angleWalk 1)
			)
		)
		(if (== (gEgo onControl:) ctlNAVY)       ; walking to the bottom
			(if (not angleWalk)
				(RoomScript changeState: 9)
				(= angleWalk 1)
			)
		)
		; controlling where the librarian stands
		(cond 
			((> (gEgo y?) 150)
				(if (not walking)
					(cond 
						((& (gEgo onControl:) ctlMAROON)  ; on the left side
							(if (not left)
								(if right
									(LibrarianScript changeState: 10) ; walk to top
									(= walking 1)
								else
									(LibrarianScript changeState: 1)
									(= walking 1)
								)
							)
						)
						((& (gEgo onControl:) ctlGREY)      ; on the right
							(if (not right)
								(if left
									(LibrarianScript changeState: 7)
									(= walking 1)
								else
									(LibrarianScript changeState: 4)
									(= walking 1)
								)
							)
						)
						((not walking)
							(cond 
								(left (LibrarianScript changeState: 7) (= walking 1))
								(right (LibrarianScript changeState: 10) (= walking 1))
							)
						)
					)
				)
			)
			((not walking)
				(cond 
					(left (LibrarianScript changeState: 7) (= walking 1)) ; walk to top
					(right (LibrarianScript changeState: 10) (= walking 1)) ; walk to top
				)
			)
		)
	)
)

(procedure (ListMissingBooks)
	(Print {Missing Books:})
		(= allBookCheck 5)
		(for ( (= i 0)) (< i 5)  ( (++ i)) (if (< [gMissingBooks i] 2)
			(switch i
				(0 (Print 46 54 #at -1 20))
				; ++allBookCheck
				(1 (Print 46 55 #at -1 20))
				; ++allBookCheck
				(2 (Print 46 56 #at -1 20))
				; ++allBookCheck
				(3 (Print 46 57 #at -1 20))
				; ++allBookCheck
				(4 (Print 46 58 #at -1 20))
			)
		else
			; ++allBookCheck
			(-- allBookCheck)
			(if (== allBookCheck 0) (Print 46 81))
		)
	)	
)

(instance LibrarianScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1 (moveHans 70 127)) ; 0-2 moving to the left side
			(2
				(moveHans 51 148)
				(= left 1)
			)
			(3
				(librarian loop: 0 cel: 0)
				(= walking 0)
				(= right 0)
				(= testImmediately 1)
				(if takingTest (RoomScript cue:))
			)
			(4 (moveHans 245 127)) ; 3-5 moving to the right side
			(5
				(moveHans 251 148)
				(= right 1)
			)
			(6
				(librarian loop: 1 cel: 0)
				(= walking 0)
				(= left 0)
			)
			(7       ; 6-8 moving from left to center
				(moveHans 70 127)
				(= testImmediately 0)
			)
			(8 (moveHans 159 127))
			(9
				(librarian loop: 2 cel: 0)
				(= left 0)
				(= walking 0)
				(liftBooks changeState: 1)
			)
			(10 (moveHans 245 127)) ; moving from right to center
			(11 (moveHans 159 127))
			(12
				(librarian loop: 2 cel: 0)
				(= right 0)
				(= walking 0)
				(liftBooks changeState: 1)
			)
		)
	)
)

(instance liftBooks of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1 (= cycles (Random 30 100)))
			(2
				(if (not left)
					(if (not right)
						(if (not walking)
							(= walking 1)
							(librarian
								loop: 4
								cel: 0
								setCycle: End self
								cycleSpeed: 3
							)
						)
					)
				)
			)
			(3
				(= cycles 20)
				(librarian loop: 5 cel: 0 setCycle: End cycleSpeed: 3)
				(books1 setMotion: MoveTo 146 100 yStep: 2)
				(books2 setMotion: MoveTo 172 100 yStep: 2)
			)
			(4
				(librarian setCycle: Beg self cycleSpeed: 3)
				(books1 setMotion: MoveTo 145 117)
				(books2 setMotion: MoveTo 173 117)
			)
			(5
				(= cycles 10)
				(librarian loop: 2 cel: 0)
				(= walking 0)
			)
			(6 (self changeState: 1))
		)
	)
)
(instance proposeScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				
				(alterEgo show: posn: (gEgo x?)(gEgo y?) view: 233 cel: 0 setCycle: End self cycleSpeed: 2)
				(if (> (gEgo x?) (librarian x?))
					(alterEgo loop: 1)	
				else
					(alterEgo loop: 0)
				)
			)
			(2 (= cycles 10)
				
			)
			(3
				(PrintHans 46 108)
				(self cue:)	
			)
			(4
				(alterEgo setCycle: Beg self)	
			)
			(5
				(gEgo show:)
				(alterEgo hide:)
				
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)	
			)
		)
	)
)

(procedure (moveHans moveX moveY)
	(librarian
		setMotion: MoveTo moveX moveY LibrarianScript
		setCycle: Walk
		cycleSpeed: 0
	)
)

(procedure (bookCheck)
	(= allBookCheck 0)
	(for ( (= i 0)) (< i 5)  ( (++ i)) (if (== [gMissingBooks i] 2)
		(switch i
			(0 (++ allBookCheck))
			(1 (++ allBookCheck))
			(2 (++ allBookCheck))
			(3 (++ allBookCheck))
			(4 (++ allBookCheck))
		)
	) (if (== allBookCheck 5)
		(PrintHans 46 83)                ; #title "Librarian:" #at -1 20) // You've returned every missing book! Even if you fail your quest, at least you're a hero to the library!
		(if (not (gEgo has: 11))
			(if (== (IsOwnedBy 11 30) FALSE)
				(gEgo get: 11)
				(= gWndColor 1)
				(= gWndBack 11)
				(Print 46 79 #title {Librarian:} #at -1 20 #icon 263)
				(Print 46 80 #title {Librarian:} #at -1 20)
				(= gWndColor 0)
				(= gWndBack 15)
			)
		)
	))
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

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 16)
)

(procedure (PrintHans textRes textResIndex)
	(= gWndColor 1) ; cl
	(= gWndBack 11) ; clDARKBLUE
	(Print
		textRes
		textResIndex
		#title
		{Librarian:}
		#width
		280
		#at
		-1
		26
	)
	(= gWndColor 0) ; clBLACK
	(= gWndBack 15)
)
                  ; clWHITE
                  
(instance alterEgo of Prop
	(properties
		y 104
		x 150
		view 128
		cel 10
	)
)
(instance librarian of Act
	(properties
		y 127
		x 160
		view 321
	)
)

(instance books1 of Act
	(properties
		y 117
		x 145
		view 54
	)
)

(instance books2 of Act
	(properties
		y 117
		x 173
		view 54
	)
)

(instance flower of Prop
	(properties
		y 119
		x 186
		view 97
	)
)
