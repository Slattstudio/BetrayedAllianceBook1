;;; Sierra Script 1.0 - (do not remove this comment)
; + 2 SCORE //
(script# 24)
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
	rm024 0
)

(local

; Pond Near Castle



	; (use "sciaudio")
	; (use "user")
	earShot =  0
)
; snd

(instance rm024 of Rm
	(properties
		picture scriptNumber
		north 0
		east 23
		south 25
		west 0
	)
	
	(method (init)
		(super init:)
		(self
			setScript: RoomScript
			setRegions: 200
			setRegions: 204
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(alterEgo init: hide: ignoreActors:)
		(portal init: ignoreActors: setPri: 0 setScript: proposeScript)
		(fisher init: setScript: teleScript)
		(floater init: setCycle: Fwd cycleSpeed: 6)
		(fish1
			init:
			ignoreActors:
			setScript: fish1Script
			ignoreControl: ctlWHITE
		)
		(fish2
			init:
			ignoreActors:
			setScript: fish2Script
			ignoreControl: ctlWHITE
		)
		(fish3
			init:
			ignoreActors:
			setScript: fish3Script
			ignoreControl: ctlWHITE
		)
		;(gTheMusic fade:)
		
		(switch gPreviousRoomNumber
			(23
; = snd aud (send snd:
;                    command("stop")
;                    fileName("music\\leah.mp3")
;                    fadeOutMillisecs("4000")
;                    loopCount("0")
;                    init()
;
;                )
				; = musicPlaying 0
				(if (not gTeleporting)
					(gEgo posn: 310 150 loop: 1)
				else
					(gEgo hide:)
					(teleScript changeState: 1)
				)
			)
			(25
				(if (not gTeleporting)
					(gEgo posn: 150 170 loop: 3)
				else
					(gEgo hide:)
					(teleScript changeState: 1)
				)
			)
			(else 
				(gEgo hide:)
				(teleScript changeState: 1)
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
			)
		)
; = snd aud (send snd:
;                command("stop")
;                fileName("music\\wizardsroom.mp3")
;                fadeOutMillisecs("4000")
;                loopCount("0")
;                init()
;                )
;                = musicPlaying 0
		(if [gFlowerGiven 1] (fisher loop: 3))
		(fish1Script changeState: 1)
		(fish2Script changeState: 1)
		(fish3Script changeState: 1)
		
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) (fisher nsLeft?))
						(< (pEvent x?) (fisher nsRight?))
						(> (pEvent y?) (fisher nsTop?))
						(< (pEvent y?) (fisher nsBottom?))
					)
					(PrintOther 24 0)
				)
				; Print(24 0 #width 280 #at -1 20)
				(if
					(and
						(> (pEvent x?) 95)
						(< (pEvent x?) 248)
						(> (pEvent y?) 123)
						(< (pEvent y?) 157)
					)                                                       ; Pond
					(if
						(and
							(> (pEvent x?) 155)
							(< (pEvent x?) 186)
							(> (pEvent y?) 143)
							(< (pEvent y?) 156)
						)                                                       ; Symbol in pond
						(PrintOther 24 1)
					else
						; Print(24 1 #width 280 #at -1 20)
						(PrintOther 24 12)
					)
				)
				; Print(24 12) /* Water */
				(if
					(and
						(> (pEvent x?) 155)
						(< (pEvent x?) 194)
						(> (pEvent y?) 65)
						(< (pEvent y?) 85)
					)                                                     ; Niche
					(PrintOther 24 24)
				)
			)
		)
		(if (Said '(take<off),remove/armor') 
			(PrintOther 24 32)
		)
		; Print(24 24 #width 280 #at -1 20)
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
		(if (Said 'give/flower')
			(if (<= (gEgo distanceTo: fisher) 30)
				(if (gEgo has: 21)
					(if [gFlowerGiven 1]
						(PrintOther 24 40)
					else
						(PrintBob 24 41)
						(= [gFlowerGiven 1] 1)
						(-- gFlowers)
						(if (== gFlowers 0) (gEgo put: 21))
						((gInv at: 21) count: gFlowers)
						(gGame changeScore: 1)
					)
				else
					(PrintDHI)
				)
			else
				(PrintNCE)
			)
		)
		(if
			(or
				(Said 'give/ring[/man,bobby]')
				(Said 'give/man,bobby/ring')
				(Said 'propose[/marriage,bobby]')
			)
			(if (gEgo has: 18)
				; kneeling animation
				(if (<= (gEgo distanceTo: fisher) 40)	
					(proposeScript changeState: 1)
				else
					(PrintNCE)
				)	
			else
				(PrintDHI)
			)
		)
		(if (Said 'smell[/!*]')
			(if (<= (gEgo distanceTo: fisher) 50)
				(PrintOther 24 49)	
			else
				(PrintOther 24 50)
			)
		)
		(if (Said 'get,go/fish,fishing') (PrintOther 24 25))
		; Print(24 25 #width 280 #at -1 20)
		(if (Said 'fish') (PrintOther 24 25))
		; Print(24 25 #width 280 #at -1 20)
		(if
			(Said
				'sleep,enter,(climb<in),(go<in),(get<in),(crawl<in)/niche,hole,cave'
			)
			(PrintOther 24 26)
		)
		; Print(24 26 #width 280 #at -1 20)
		(if
			(or
				(Said 'swim,(get<in),(jump<in)/pond,water')
				(Said 'swim')
			)
			(PrintOther 24 27)
		)
		(if (Said 'look>')
			(if (Said '/flower')
				(if [gFlowerGiven 1]
					(PrintOther 0 98)
				else
					(PrintOther 0 97)
				)
			)
			(if (Said '/man,bobby') (PrintOther 24 0))
			; Print(24 0 #width 280 #at -1 20)
			(if (Said '/symbol') (PrintOther 24 1))
			; Print(24 1 #width 280 #at -1 20)
			(if (Said '/pond') (PrintOther 24 21))
			; Print(24 21 #width 280 #at -1 20)
			(if (Said '/niche,cave') (PrintOther 24 24))
			; Print(24 24 #width 280 #at -1 20)
			(if (Said '[/!*]') (PrintOther 24 22))
		; this will handle just "look" by itself
		)
		; Print(24 22 #width 280 #at -1 8)
		(if (Said 'talk/man,bobby')
			(if (<= (gEgo distanceTo: fisher) 30)
				(if gDisguised
					(PrintBob 24 38)
					(if (== [gMissingBooks 1] 0)
						(PrintBob 24 39)
						(Print 24 18 #icon 984 1 0)
						(= [gMissingBooks 1] 1)
						(gGame changeScore: 1)
						(if (not (gEgo has: 20)) (gEgo get: 20))
					)
				else
					(PrintBob 24 2)
					(PrintBob 24 3)
					(PrintBob 24 4)
				)
			else
				(PrintNCE)
			)
		)
		(if (Said '(ask<about)>')
			(cond 
				(earShot
					(if (Said '/monster,ogre')
						(PrintBob 24 5)
						(PrintBob 24 30)
					)
					(if (Said '/cave') (PrintBob 24 20) (PrintBob 24 51))
					(if (Said '/princess') (PrintBob 24 6) (PrintBob 24 7))
					(if (Said '/pond,fish')
						(PrintBob 24 13)
						(PrintBob 24 37)
					)
					(if (Said '/name')
						(PrintBob 24 2)
					)
					; Print(24 13 #width 280 #at -1 20 #title "Bobby says:")
					(if (Said '/hermit,henry') (PrintBob 24 31))
					(if (Said '/symbol') (PrintBob 24 8))
					; Print(24 8 #width 280 #at -1 20 #title "Bobby says:")
					(if (Said '/family,father,king') (PrintBob 24 9))
					; Print(24 9 #width 280 #at -1 20 #title "Bobby says:")
					(if (Said '/bait') (PrintBob 24 10))
					; Print(24 10 #width 280 #at -1 20 #title "Bobby says:")
					(if (Said '/wizard') (PrintBob 24 14))
					; Print(24 14 #width 280 #at -1 20 #title "Bobby says:")
					(if (Said '/weather') (PrintBob 24 15))
					; Print(24 15 #width 280 #at -1 12 #title "Bobby says:")
					(if (Said '/carmyle') (PrintBob 24 33))
					; Print(24 20 #width 280 #at -1 20 #title "Bobby says:")
					(if (Said '/meat') (PrintBob 24 34))
					; Print(24 20 #width 280 #at -1 20 #title "Bobby says:")
					(if (Said '/library')
						(if (== [gMissingBooks 1] 0)
							(PrintBob 24 35)
						else
							(PrintBob 24 36)
						)
					)
					; Print(24 20 #width 280 #at -1 20 #title "Bobby says:")
					(if (Said '/hair,beard,mustache,book')
						(PrintBob 24 16)
						; Print(24 16 #width 280 #at -1 12 #title "Bobby says:")
						(if (== [gMissingBooks 1] 0)
							(PrintBob 24 17)
							; Print(24 17 #width 280 #at -1 12 #title "Bobby says:")
							(Print 24 18 #icon 984 1 0)
							; Print(24 28)
							(= [gMissingBooks 1] 1)
							(gGame changeScore: 1)
							(if (not (gEgo has: 20)) (gEgo get: 20))
						)
					)
					
					(if (Said '/man, fisher,bobby')
						(PrintBob 24 2)
						(PrintBob 24 4)
					)
					
					(if (Said '/soldier') (PrintBob 24 47))
					(if (Said '/leah') (PrintBob 0 92))
					(if (Said '/sammy') (PrintBob 24 42)(PrintBob 24 52))
					(if (Said '/deborah') (PrintBob 24 43))
					(if (Said '/rose') (PrintBob 0 92))
					(if (Said '/sarah') (PrintBob 0 92))
					(if (Said '/hans') (PrintBob 24 44))
					(if (Said '/lex') (PrintBob 0 91))
					(if (Said '/colin') (PrintBob 24 45))
					(if (Said '/longeau') (PrintBob 24 46))
					(if (Said '/moon') (PrintBob 0 91))
					(if (Said '/gyre') (PrintBob 0 91))
					(if (Said '/*') (PrintBob 24 11))
				)
				((Said '/*') (PrintOther 24 19))
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (<= (gEgo distanceTo: fisher) 45)
			(if [gFlowerGiven 1]
				(fisher loop: 4)
			else
				(fisher loop: 1)
			)
			(= earShot 1)
		else
			(if [gFlowerGiven 1]
				(fisher loop: 3)
			else
				(fisher loop: 0)
			)
			(= earShot 0)
		)
	)
)

; (if(& (send gEgo:onControl()) ctlSILVER)
;            (if(not(musicPlaying))
;                = snd aud (send snd:
;                command("playx")
;                fileName("music\\leah.mp3")
;                volume("70")
;                loopCount("-1")
;                init()
;                )
;                = musicPlaying 1
;            )
;        )(else
;            (if(musicPlaying)
;            = snd aud (send snd:
;                command("stop")
;                fileName("music\\leah.mp3")
;                fadeOutMillisecs("4000")
;                loopCount("0")
;                init()
;            )
;            = musicPlaying 0
;            )
;        )
; (procedure (PrintItem itemNumber)
;    (if(send gEgo:has(itemNumber))
;        Print(scriptNumber (+ 30 itemNumber) #width 280 #at -1 8)
;    )(else
;        PrintDHI()
;    )
; )
(instance teleScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				; (send gEgo:hide())
				(alterEgo
					show:
					setCycle: Beg teleScript
					cycleSpeed: 2
					ignoreActors:
				)
				(gTheSoundFX number: 205 play:)

				(= gTeleporting 0)
			)
			(2
				;(gTheMusic fade:)
				(gEgo show: posn: 150 104 loop: 2)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
)

(procedure (PrintBob textRes textResIndex)
	(= gWndColor 11)
	(= gWndBack 1)
	(Print
		textRes
		textResIndex
		#title
		{He says:}
		#width
		280
		#at
		-1
		20
	)
	(= gWndColor 0) ; clBLACK
	(= gWndBack 15)
)
                  ; clWHITE
(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 8)
)

; (instance upDown of Script
;    (properties)
;    (method (changeState newState)
;        = state newState
;        (switch (state)
;            (case 0 = cycles Random(1 9)
;                (fisher:cel(0))
;                (floater:cel(0)posn(90 141))
;            )(case 1 = cycles Random (1 9)
;                (floater:posn(90 140))
;            )(case 2 = cycles 1
;                (if(not(> 10 Random(1 55)))
;                    (upDown:changeState(0))
;                )
;            )(case 3 = cycles 6
;                (floater:cel(1)posn(90 144))
;                (fisher:cel(1))
;            )(case 4
;                (upDown:changeState(0))
;            )
;        )
;    )
; )
(instance fish1Script of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(fish1
					setCycle: Walk
					setMotion: MoveTo (Random 110 240) (Random 130 153) self
				)
			)
			(2
				(fish1
					setMotion: MoveTo (Random 110 240) (Random 130 153) self
				)
			)
			(3 (fish1Script changeState: 1))
		)
	)
)

(instance fish2Script of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(fish2
					setCycle: Walk
					setMotion: MoveTo (Random 110 240) (Random 130 153) self
				)
			)
			(2
				(fish2
					setMotion: MoveTo (Random 110 240) (Random 130 153) self
				)
			)
			(3 (fish2Script changeState: 1))
		)
	)
)

(instance fish3Script of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(fish3
					setCycle: Walk
					setMotion: MoveTo (Random 110 240) (Random 130 153) self
				)
			)
			(2
				(fish3
					setMotion: MoveTo (Random 110 240) (Random 130 153) self
				)
			)
			(3 (fish3Script changeState: 1))
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
				(if (> (gEgo x?) (fisher x?))
					(alterEgo loop: 1)	
				else
					(alterEgo loop: 0)
				)
			)
			(2 (= cycles 10)
				
			)
			(3
				(PrintBob 24 48)
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
; (instance fish4Script of Script
;    (properties)
;    (method (changeState newState)
;        = state newState
;        (switch (state)
;            (case 1
;                (mercuryFish:setCycle(Walk)setMotion(MoveTo ( Random(110 240) (Random(130 153)) fish4Script) ))
;            )(case 2
;                (mercuryFish:setMotion(MoveTo ( Random(110 240) (Random(130 153)) fish4Script) ))
;            )(case 3
;                (fish4Script:changeState(1))
;            )
;        )
;    )
; )
(instance alterEgo of Prop
	(properties
		y 104
		x 150
		view 128
		cel 10
	)
)

(instance fisher of Prop
	(properties
		y 145
		x 56
		view 27
	)
)

(instance floater of Prop
	(properties
		y 140
		x 90
		view 27
		loop 2
	)
)

(instance fish1 of Act
	(properties
		y 150
		x 150
		view 520
	)
)

(instance fish2 of Act
	(properties
		y 160
		x 170
		view 521
	)
)

(instance fish3 of Act
	(properties
		y 155
		x 160
		view 522
	)
)

; (instance mercuryFish of Act
;    (properties y 152 x 140 view 523)
; )
(instance portal of Prop
	(properties
		y 114
		x 150
		view 16
		loop 1
	)
)
