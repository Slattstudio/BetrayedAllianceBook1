;;; Sierra Script 1.0 - (do not remove this comment)
; SCORE + 5 // gInt + 1 //
(script# 62)
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
	rm062 0
)

(local

; Inside the Dockhouse



	doorOpen =  0
	fullCheck =  0
	manAsleep =  1
	upDown =  0
	counter =  0
	sunDown =  0
	message =  60
	repeat_ =  76
	[stringName 20]
	name =  0
)

(instance rm062 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 204)
		
		(SetUpEgo)
		(gEgo init: observeControl: ctlRED)
		(= gEgoRunning 0)
		(RunningCheck)
		
		(gTheMusic number: 46 loop: -1 play:)
		
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 60 137 loop: 1)
			)
			(49
				(gEgo posn: 80 131 loop: 0 setMotion: MoveTo 90 131)
				(door init: cel: 2 setCycle: Beg cycleSpeed: 2)
			)
		)
		; (RoomScript:changeState(1))
		(door init: setPri: 8 ignoreActors: setScript: proposeScript)
		(man init: ignoreActors: setPri: 10)
		(lostPaper
			init:
			hide:
			ignoreActors:
			setScript: searchScript
		)
		; (paperPile1:init()setScript(searchScript))
		; (paperPile2:init())
		; (paperPile3:init())
		(marble
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
			setScript: orbit
		)
		(sun init: hide: ignoreActors:)
		(alterEgo
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
			setCycle: Walk
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(1      ; marble
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 167 139 self)
			)
			(2 (= cycles 2) (gEgo loop: 2))
			(3
				(= cycles 40)
				(PrintOther 62 49) ; #at -1 20)
				(sun show: posn: (gEgo x?) (+ (gEgo y?) 10))
				(= sunDown 1)
				(orbit changeState: 1)
				(marble show:)
			)
			; (PlayerControl)
			(4      ; picking up marbles
				; (ProgramControl) (SetCursor(997 1) = gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 153 147 self
					ignoreControl: ctlWHITE
				)
			)
			(5
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					loop: 0
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(6
				(= cycles 6)
				(PrintOther 62 50)
				(Print {Oh, grow up now!})
				(= sunDown 0)
				(marble hide:)
				(sun hide:)
				(= gUra 1)
				(++ gMarbleNum)
				((gInv at: 9) count: gMarbleNum)
				(gGame changeScore: 1)
			)
			(7
				(alterEgo setCycle: Beg self)
			)
			(8
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(alterEgo hide:)
				(gEgo show: observeControl: ctlWHITE)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 49)
		)
		(if (& (gEgo onControl:) ctlSILVER)
			(= manAsleep 0)
			(cond 
				((< (gEgo x?) 101) (man loop: 1 cel: 0))
				((> (gEgo x?) 204) (man loop: 1 cel: 1))
				(else (man loop: 2 cel: 0))
			)
			(if (> (gEgo y?) 134)
				(gEgo setPri: 11)
			else
				(gEgo setPri: -1)
			)
			(alterEgo setPri: (gEgo priority?))
		else
			(= manAsleep 1)
			(if (< counter 8) (++ counter) else (= counter 0))
			(if (> 4 counter) (= upDown 1) else (= upDown 0))
			(man loop: 0 cel: upDown)
			(gEgo setPri: -1)
		)
		(if (>= (gEgo distanceTo: door) 35)
			(if doorOpen
				(door setCycle: Beg)
				(= doorOpen 0)
				(gEgo observeControl: ctlRED)
				(gTheSoundFX number: 204 play:)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(checkEvent
						pEvent
						(man nsLeft?)
						(man nsRight?)
						(man nsTop?)
						(man nsBottom?)
					)
					(if manAsleep (PrintOther 62 2) else (PrintOther 62 3))
				)
				(if (checkEvent pEvent 215 234 42 78)       ; ladder
					(PrintOther 62 46)
				)
				(if (checkEvent pEvent 202 230 150 174)       ; chest
					(PrintOther 62 47)
				)
				(if
					(==
						ctlGREEN
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                    ; single papers
					(PrintOther 62 44)
				)
				(if
					(==
						ctlBLUE
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                    ; desk and book
					(PrintOther 62 45)
				)
				; packages
				(if
					(or
						(==
							ctlTEAL
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)
						(==
							ctlFUCHSIA
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)
					)
					(PrintOther 62 48)
				)
				; Papers
				(if
					(or
						(==
							ctlNAVY
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)
						(==
							ctlYELLOW
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)
					)
					(= message (Random 60 77))
					(if (== message repeat_)
						(++ message)
						(if (> message 76) (= message (Random 60 77)))
					)
					(cond 
						((== message 64) (Print {} #title {A postcard?} #icon 994) (Print 62 64))
						((== message 74) (Print 62 message #width 100 #font 4))
						(else (Print 62 message #width 180 #at -1 -1 #font 4))
					)
					(= repeat_ message)
				)
			)
		)
		(if (Said 'rub,feel,hold/marble,sun,shooter')
			(if (gEgo has: 9)
				(if (not gUra) (Print 0 95) else (Print 0 94))
			else
				(PrintDHI)
			)
		)
		(if
			(or
				(Said 'give/ring[/man]')
				(Said 'give/man/ring')
				(Said 'propose[/marriage,man]')
			)
			(if (gEgo has: 18)
				; kneeling animation
				(if (not manAsleep)
					(proposeScript changeState: 1)
				else
					(PrintNCE)
				)	
			else
				(PrintDHI)
			)
		)
		(if
			(Said
				'((ask<for),(fill<out),(pick<up),request,take)/form'
			)
			(if manAsleep
				(PrintNCE)
			else
				(PrintMan 62 6)
				(Print 62 7)
			)
		)                   ; You quickly decide that you don't really want a form anyway!
		(if (Said 'take,find>')
			(if (Said '/letter')
				(PrintOther 62 8) ; #width 280 #at -1 8) // With all the clutter you can't find the one you want.
				(PrintOther 62 9)
			)                    ; #width 280 #at -1 8) // If only there were some way to see past all the mess.
			(if (Said '/fish') (PrintOther 62 51)) ; With all the clutter you can't find the one you want.
			(if (Said '/package')
				(PrintOther 62 8) ; #width 280 #at -1 8) // With all the clutter you can't find the one you want.
				(PrintOther 62 9)
			)
		)                        ; #width 280 #at -1 8) // If only there were some way to see past all the mess.
		(if (Said 'run') (Print 0 88))
		(if (or (Said 'look,use,read,open/portal,map')
				(Said 'map'))
			(Print 0 88)
		)
		(if (Said 'leave/name')
			(if (not manAsleep)
				(PrintMan 62 26) ; #width 280 #at -1 20 #title {He says:})
			else              ; clWHITE
				(Print 62 27)
			)
		)
		(if (Said 'open/chest') (PrintOther 62 47))
		(if (Said 'read/sign')
			(Print
				62
				0
				#title
				{It reads:}
				#width
				280
				#at
				-1
				20
				#font
				4
			)
		)
		(if (Said 'look>')
			(if (Said '/window')
				(PrintOther 62 55)	
				(PrintOther 62 56)
			)
			(if (Said '/paper') (Print 62 11 #width 280 #at -1 8))
			(if (Said '/letter')
				(Print 62 8 #width 280 #at -1 8)
				(Print 62 9 #width 280 #at -1 8)
			)
			(if (Said 'ladder')
				(PrintOther 62 47)
			)
			(if (Said '/chest') (PrintOther 62 47))
			(if (Said '/book')
				(PrintOther 62 52)
			)
			(if (Said '/package')
				(Print 62 8 #width 280 #at -1 8) ; With all the clutter you can't find the one you want.
				(Print 62 9 #width 280 #at -1 8)
			)                                   ; If only there were some way to see past all the mess.
			(if (Said '/man') (Print 62 12 #width 280 #at -1 8)) ; Despite the piles and mess the attendant seems quite undisturbed.
			(if (Said '/mess')
				(Print 62 23 #width 280 #at -1 10)
			)
			; If you're looking for something in this mess you're going to need something more than just your eyes.
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(Print 62 14 #width 280 #at -1 8)
				(Print 62 15 #width 280 #at -1 8)
			)
		)
		(if (Said 'talk/man,official')
			(if (not manAsleep)
				(PrintMan 62 16) ; #width 280 #at -1 20 #title {He says:}) ; Thanks for choosing the Royal Dock Service, please fill out a form if you wish to import or export goods. For mail services, please leave a name and address and we'll accommodate you as quickly as we can.
			else              ; clWHITE
				(Print {No one responds.} #at -1 10)
			)
		)
		(if (Said '(ask<about)>')
			(cond 
				((not manAsleep)
					(if (Said '/mail,letter')
						(PrintMan 62 17) ; #width 280 #at -1 20 #title {He says:})
					)
					; If you're looking for your mail rest assured we are currently processing all letters and will be delivering them whenever possible.
					(if (Said '/man')
						(PrintMan 62 18) ; #width 280 #at -1 20 #title {He says:})
					)
					; As you can see I'm swamped with work here. If you need something, take a form.
					(if (Said '/princess')
						(PrintMan 62 19) ; #width 280 #at -1 20 #title {He says:})
					)
					; If you want to send the Princess a message please fill out a form.
					(if (Said '/form')
						(PrintMan 62 21) ; #width 280 #at -1 20 #title {He says:})
					)
					; There are plenty of forms on the desk. Fill one out and I will help you when I get around to it.
					(if (Said '/dock')
						(PrintMan 62 22)
					)
					; If you wish to process imports or exports you'll find a form on my desk. If you wish to fish, please fill out the form for a license.
					(if (Said '/mess')
						(PrintMan 62 24)
					)
					; As you can see I'm quite busy. Ask me something useful or leave me to my work.
					(if (Said '/cave')
						(PrintMan 62 30)
					)
					(if (Said '/job')
						(PrintMan 62 54)
					)
					; As you can see I'm quite busy. Ask me something useful or leave me to my work.
					(if (Said '/ogre,monster')
						(PrintMan 62 31)
					)
					; As you can see I'm quite busy. Ask me something useful or leave me to my work.
					(if (Said '/wizard')
						(PrintMan 62 32)
					)
					; As you can see I'm quite busy. Ask me something useful or leave me to my work.
					(if (Said '/*')
						(PrintMan 62 25)
					)
				)
				((Said '/*') (Print {No one responds.} #at -1 20)) ; clWHITE
			)
		)
		(if (Said 'leave/address')
			(= gWndColor 7)
			(= gWndBack 8)
			(Print 62 28 #width 280 #at -1 20 #title {He says:})
			(= gWndColor 0) ; clBLACK
			(= gWndBack 15)
		)                 ; clWHITE
		(if (Said 'open/door')
			(if (not doorOpen)
				(if (<= (gEgo distanceTo: door) 30)
					(door setCycle: End)
					(gEgo ignoreControl: ctlRED)
					(= doorOpen 1)
					(gTheSoundFX number: 204 play:)
				else
					(PrintNCE)
				)
			else
; (if (& (send gEgo:onControl()) ctlNAVY)
;                        Print("It's locked. Not to allude to sour grapes or anything, but there's proabably nothing but paper back there anyway." #width 280 #at -1 20)
;                    )(else
;                        PrintNCE()
;                    )
;                )
				(Print 18 35 #width 280 #at -1 8)
			)
		)
		(if (Said 'knock')
			(if (<= (gEgo distanceTo: door) 35)
				(if (not doorOpen)
					(PrintOther 62 33)
				else                  ; It's customary to knock on doors from the outside, but hey, knock yourself out!
					(Print {Um, it's open.})
				)
			else
				(PrintNCE)
			)
		)
; (if (& (send gEgo:onControl()) ctlNAVY)
;                    Print("Quite thankfully no one responds from the closet.")
;                )(else
;                    PrintNCE()
;                )
;            )
		(if
			(Said
				'(search,use,find,(put<on),wear,sort)/(name,goggles,letter,jasper,gallagos,paper,mail)'
			)
			(if (gEgo has: INV_GOGGLES)
				(searchScript changeState: 1)
			else
				; (send gEgo:get(INV_LETTER))
				(Print
					{There's no way you can find the letter in this mess. Forget finding a needle in a haystack. That's easy! Just burn the haystack.}
					#width
					280
					#at
					-1
					20
				)
				(Print
					{For a moment you had an idea, but...no, fire is not always the answer.}
					#width
					280
					#at
					-1
					20
				)
				(Print
					{Maybe someone has a tool that can make this process easier...}
					#title
					{You think:}
					#width
					280
					#at
					-1
					20
				)
			)
		)
		(if (Said 'use,drop,put,throw,shoot/marble')
			(if (not gUra)
				(if (gEgo has: 9)
					(if (gEgo inRect: 89 136 225 165)
						(if (not sunDown)
							(self changeState: 1)
						else
							(Print {It's already on the floor.})
						)
					else
						(PrintNCE)
					)
				else
					(Print {You don't seem to have any marbles.})
					(Print {Don't take it too personally.})
				)
			else
				(Print {There is no reason to do this now.})
			)
		)
		(if (Said '(pick<up),take/book') (PrintOther 62 52))
		(if (Said '(pick<up),take/mail')
			(Print {What mail are you looking for?})
		)
		(if (Said '(pick<up),take/marble')
			(if sunDown
				(if (<= (gEgo distanceTo: sun) 40)
					(self changeState: 4)
				else
					(PrintNCE)
				)
			else
				(Print {What marble are you talking about?})
			)
		)
	)
)

(instance searchScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles 10)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				; (send gEgo:ignoreControl(ctlWHITE))
				(gEgo hide:)
				(alterEgo show: view: 907 posn: (gEgo x?) (gEgo y?))
				(DrawPic 162 0 1 0)
			)
			; (lostPaper:show())
			(2
				(PrintOther 62 40) ; Whoa! What a strange sight.
				(EditPrint
					@stringName
					14
					{Which name would you like to locate?}
					#at
					-1
					20
				)
				(cond 
					(
						(or
							(== (StrCmp @stringName {jasper}) 0)
							(== (StrCmp @stringName {Jasper}) 0)
						)
						(= name 1)
					)
					(
						(or
							(== (StrCmp @stringName {gallagos}) 0)
							(== (StrCmp @stringName {Gallagos}) 0)
						)
						(= name 2)
					)
					(
						(or
							(== (StrCmp @stringName {colin}) 0)
							(== (StrCmp @stringName {Colin}) 0)
						)
						(= name 3)
					)
					(else (= name 0))
				)
				(= stringName 0)
				(switch name
					(1
						(if
							(or
								(not (gEgo has: INV_LETTER))
								(== (IsOwnedBy INV_LETTER 28) TRUE)
							)
							(PrintOther 62 41) ; There! You spy something red amongst the clutter. These things really work!
							(lostPaper show:)
						else
							(PrintOther 62 37) ; There is no other letter by that name.
							(self changeState: 8)
							(return)
						)
					)                             ; exit goggles-view
					(2
						(if (not g62Letter)
							(PrintOther 62 41) ; There! You spy something red amongst the clutter. These things really work!
							(lostPaper show:)
						else
							(PrintOther 62 37) ; There is no other letter by that name.
							(self changeState: 8)
							(return)
						)
					)                             ; exit goggles-view
					(3
						(if (not g62Package)
							(PrintOther 62 41) ; There! You spy something red amongst the clutter. These things really work!
							(lostPaper show:)
						else
							(PrintOther 62 38)
							(self changeState: 8)
							(return)
						)
					)                             ; exit goggles-view
					(0
						(PrintOther 62 42)
						(self changeState: 8)
						(return)
					)
				)                             ; exit goggles-view
				(if (> (gEgo y?) 124)     ; below desk
					(if (> (gEgo x?) 147)     ; and right of desk
						(if (> (gEgo x?) 230)     ; extreme right
							(if (< (gEgo y?) 134)     ; odd position at desk
								(self changeState: 3)
							else                        ; walk down, but still far right
								(self changeState: 4)
							)
						else                            ; walk to left area near door
							(self changeState: 4)
						)
					else                            ; walk to left area near door
						(self changeState: 5)
					)
				else                            ; walk straight to paper
					(self changeState: 5)
				)
			)                               ; walk straight to paper
			(3
				(alterEgo
					view: 85
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo 237 148 self
				)
				(gEgo posn: 237 148)
			)                                                                                ; walk to paper
			(4
				(alterEgo
					view: 85
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo 110 148 self
				)
			)                                                                                ; walk to paper
			(5
				(alterEgo
					view: 85
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo 119 119 self
				)
			)                                                                                ; walk to paper
			(6
				(= cycles 10)
				(alterEgo view: 907 loop: 3)
			)
			(7
				(= cycles 2)
				(searchForLetter)
			)
			(8
				(DrawPic 62 0 1 0)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(alterEgo hide:)
				(gEgo show: posn: (alterEgo x?) (alterEgo y?) loop: 2)
			)
		)
	)
)

(procedure (searchForLetter)
	(if (gEgo has: INV_GOGGLES)
		(cond 
			((== name 1)
				(if (not (gEgo has: INV_LETTER))
					(Print 62 34 #icon 258 #title {Letter}) ; With the help of the scientist's Word-Finding-Goggles you located the missing letter!
					(gEgo get: INV_LETTER)
					(gGame changeScore: 2)
					(PrintOther 62 43)
				else
					(PrintOther 62 37)
				)
			)
			((== name 2)              ; There is no other letter by that name.
				(if (not g62Letter)
					(PrintOther 62 34)
					(Print 62 35 #icon 276 #title {Letter})    ; It is addressed to 'my guardian friend,' from Gallagos.
					(gEgo get: 24)
					(= g62Letter 1)
					(++ gInt)
					(gGame changeScore: 1)
					(PrintOther 62 43)
				else
					(PrintOther 62 37)
				)
			)
			((== name 3)                  ; There is no other letter by that name.
				(if (not g62Package)
					(Print 62 36 #icon 270 #title {Package})         ; With the help of the scientist's Word-Finding-Goggles you located a missing package!
					(= g62Package 1)
					(gEgo get: 19)
					(PrintOther 62 43)
					(gGame changeScore: 1)
				else
					(PrintOther 62 38)
				)
			)
			((== name 0) (PrintOther 62 29))  ; There is no other package by that name.
		)
		(lostPaper hide:)
	else
		(PrintOther 62 39)
	)                     ; It's too cluttered to find anything!
	(= fullCheck 0)
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
				(if (> (gEgo x?) (man x?))
					(alterEgo loop: 1)	
				else
					(alterEgo loop: 0)
				)
			)
			(2 (= cycles 10)
				
			)
			(3
				(PrintMan 62 53)
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
	(Print textRes textResIndex #width 280 #at -1 10)
)

(procedure (PrintMan textRes textResIndex)
	(= gWndColor 7)
	(= gWndBack 8)				
	(Print textRes textResIndex #width 280 #at -1 20 #title "He says:")
	(= gWndColor 0) ; clBLACK
	(= gWndBack 15)
)

(instance orbit of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(= cycles 15)
				(marble
					setMotion: MoveTo (+ (sun x?) 2) (+ (sun y?) 8) orbit
				)
			)
			(2
				(= cycles 15)
				(marble
					setMotion: MoveTo (- (sun x?) 4) (+ (sun y?) 7) orbit
				)
			)
			(3
				(= cycles 15)
				(marble
					setMotion: MoveTo (- (sun x?) 12) (+ (sun y?) 4) orbit
				)
			)
			(4
				(= cycles 15)
				(marble
					setMotion: MoveTo (- (sun x?) 20) (- (sun y?) 2) orbit
				)
			)
			(5
				(= cycles 15)
				(marble
					setMotion: MoveTo (- (sun x?) 12) (- (sun y?) 8) orbit
				)
			)
			(6
				(= cycles 15)
				(marble
					setMotion: MoveTo (- (sun x?) 4) (- (sun y?) 11) orbit
				)
			)
			(7
				(= cycles 15)
				(marble
					setMotion: MoveTo (+ (sun x?) 2) (- (sun y?) 12) orbit
				)
			)
			(8
				(= cycles 15)
				(marble
					setMotion: MoveTo (+ (sun x?) 8) (- (sun y?) 11) orbit
				)
			)
			(9
				(= cycles 15)
				(marble
					setMotion: MoveTo (+ (sun x?) 16) (- (sun y?) 8) orbit
				)
			)
			(10
				(= cycles 15)
				(marble
					setMotion: MoveTo (+ (sun x?) 24) (- (sun y?) 2) orbit
				)
			)
			(11
				(= cycles 15)
				(marble
					setMotion: MoveTo (+ (sun x?) 16) (+ (sun y?) 4) orbit
				)
			)
			(12
				(= cycles 15)
				(marble
					setMotion: MoveTo (+ (sun x?) 8) (+ (sun y?) 7) orbit
				)
			)
			(13
				(= cycles 15)
				(orbit changeState: 1)
			)
		)
	)
)

(instance door of Prop
	(properties
		y 132
		x 63
		view 36
		loop 5
	)
)

(instance man of Prop
	(properties
		y 105
		x 190
		view 73
	)
)

(instance lostPaper of Prop
	(properties
		y 96
		x 114
		view 93
	)
)

; (instance paperPile1 of Prop
;    (properties y 121 x 129 view 73 loop 4)
; )
; (instance paperPile2 of Prop
;    (properties y 123 x 145 view 73 loop 3)
; )
; (instance paperPile3 of Prop
;    (properties y 122 x 176 view 73 loop 3 cel 1)
; )
(instance marble of Act
	(properties
		y 123
		x 226
		view 20
	)
)

(instance sun of Prop
	(properties
		y 160
		x 230
		view 21
	)
)

(instance alterEgo of Act
	(properties
		y 160
		x 230
		view 85
	)
)
