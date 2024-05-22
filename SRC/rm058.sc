;;; Sierra Script 1.0 - (do not remove this comment)
; Score +2 // gInt + 1 //
(script# 58)
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
	rm058 0
)

(local

; Cavern of Madness (river)



	; (use "sciaudio")
	ghostVisible
	sunDown =  0
	falling =  0
	snd
	searchBody =  2 ; 1 for fresh body, 0 for skeleton
	dartsVisible =  0
)

(instance rm058 of Rm
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
		(gEgo init:)
		(= gEgoRunning 0)
		(RunningCheck)
		(marble init: ignoreActors: setScript: orbit)
		(if (== gPlu 1) (marble hide:))
		(ghost init: ignoreActors:)
		(chain init:)
		(sun init: hide: ignoreActors:)
		(skeleton init: setScript: fallScript ignoreActors:)
		(deadGuy init: setScript: hoverScript)
		(note init: ignoreActors:)
		(alterEgo
			init:
			ignoreActors:
			ignoreControl: ctlWHITE
			hide:
		)
		(dartsOnRiver
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
			setScript: dartScript
		)
		(if (< gApple 4) ; darts
			(if (gEgo has: 8)
				(dartScript changeState: 1)
				(= dartsVisible 1)
			)
		)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
			(53
				(gEgo posn: 180 30 loop: 1)
				(fallScript changeState: 1)
			
; = snd aud (send snd:
;                    command("playx")
;                    fileName("music\\graveyard.mp3")
;                    volume("70")
;                    loopCount("-1")
;                    init()
;                )
				(gTheMusic number: 58 loop: -1 play:)	
			)
			(73
				(gEgo posn: 211 100 loop: 2)
			)
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			(1       ; Searching the Body of DeadGuy
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(if (== searchBody 1)
					(gEgo
						setMotion: MoveTo 180 164 RoomScript
						ignoreControl: ctlWHITE
						setPri: 15
					)
				)
				(if (== searchBody 0)
					(gEgo
						setMotion: MoveTo 268 154 RoomScript
						ignoreControl: ctlWHITE
					)
				)
			)
			(2
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					loop: searchBody
					posn: (gEgo x?) (gEgo y?)
					setCycle: End RoomScript
					cycleSpeed: 2
					setPri: 15
				)
			)
			(3
				(alterEgo setCycle: Beg RoomScript)
				(if (== searchBody 1)
					(if (gEgo has: INV_BLOCK)
						(Print {Another search yields no additional bounty.})
					else
						(Print 58 0 #width 280 #at -1 10)
						(Print 58 42 #icon 265 #title {Block})
						(Print 58 43 #width 280 #at -1 10)
						(Print {} #icon 550 4 0 #title {The Image:})
						(Print 58 1 #icon 260 #title {Blow Dart Gun})
						(gEgo get: INV_BLOCK)
						(gEgo get: INV_BLOW_DART_GUN)
						(gGame changeScore: 2)
						(= gApple (+ gApple 10))
						(= [gArtwork 0] 1)
					)
				)
				(if (== searchBody 0)
					(Print 58 46 #width 280 #at -1 20)
				)
			)                                         ; The bones appear to be quite old. If you don't find a way out soon, you worry this fate may be yours.
			(4
				(gEgo show: observeControl: ctlWHITE loop: 1 setPri: -1)
				(alterEgo hide: setPri: -1)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= searchBody 2)
			)
			(5
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 213 112 RoomScript
					ignoreControl: ctlWHITE
				)
			)
			(6
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(Print 34 9)
				(= sunDown 1)
			)
			(8
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(if sunDown
					(if (> (+ (gEgo x?) 15) (sun x?))
						(gEgo
							setMotion: MoveTo (+ (sun x?) 20) (sun y?) self
							ignoreControl: ctlWHITE
						)
						(alterEgo view: 232 loop: 1)
					else
						(gEgo
							setMotion: MoveTo (- (sun x?) 16) (sun y?) self
							ignoreControl: ctlWHITE
						)
						(alterEgo view: 232 loop: 0)
					)
				else    ; if you're just picking up the marble without the sun
					(gEgo
						setMotion: MoveTo (- (marble x?) 16) (marble y?) self
						ignoreControl: ctlWHITE
					)
					(alterEgo view: 232 loop: 0)
				)
			)
			(9
				(gEgo hide:)
				(alterEgo show: posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2)
			)
			(10
				(alterEgo setCycle: Beg RoomScript)
				; retrive the marbles
				(Print 58 34 #at -1 20)
				(Print 58 44 #at -1 20)
				(= sunDown 0)
				(marble hide:)
				(sun hide:)
				(= gPlu 1)
				(++ gMarbleNum)
				((gInv at: 9) count: gMarbleNum)
				(gGame changeScore: 1)
				(++ gInt)
			)
			(11
				(gEgo
					show:
					observeControl: ctlWHITE
					loop: (alterEgo loop?)
				)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			; looking at the note
			(12
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)	
				(gEgo setMotion: MoveTo (+ (note x?) 20) (note y?) self ignoreControl: ctlWHITE)
			)
			(13
				(gEgo hide:)
				(alterEgo show: view: 232 loop: 1 posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2)
			)
			(14
				(alterEgo setCycle: Beg self)
				(PrintOther 58 68)
				(Print 58 69 #at 30 -1 #width 100 #font 4)
			)
			(15
				(gEgo
					show:
					observeControl: ctlWHITE
					loop: (alterEgo loop?)
				)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
	
	(method (handleEvent pEvent &tmp dyingScript)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(cond 
					(
						(checkEvent
							pEvent
							(dartsOnRiver nsLeft?)
							(dartsOnRiver nsRight?)
							(dartsOnRiver nsTop?)
							(dartsOnRiver nsBottom?)
						)
						(PrintOther 58 47 #at -1 20)
					)
					(                           ; A small box is floating on the stream.
						(==
							ctlNAVY
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                  ; stream
						(PrintOther 58 4)
					)
				)
				(cond 
					(
						(checkEvent
							pEvent
							(skeleton nsLeft?)
							(skeleton nsRight?)
							(skeleton nsTop?)
							(skeleton nsBottom?)
						)
						;(if (<= (gEgo distanceTo: skeleton) 35)
						;	(= searchBody 0)
						;	(RoomScript changeState: 1)
						;else
							(PrintOther 58 5)
						;)
					)
					(                    ; #at -1 20)
						(checkEvent
							pEvent
							(deadGuy nsLeft?)
							(deadGuy nsRight?)
							(deadGuy nsTop?)
							(deadGuy nsBottom?)
						)
						(if (<= (gEgo distanceTo: deadGuy) 35)
							(= searchBody 1)
							(RoomScript changeState: 1)
						else
							(PrintOther 58 6)
						)
					)
					(                    ; #at -1 20)
						(checkEvent
							pEvent
							(note nsLeft?)
							(note nsRight?)
							(note nsTop?)
							(note nsBottom?)
						)
						(if (<= (gEgo distanceTo: note) 35)
							(RoomScript changeState: 12)
						else
							(PrintOther 58 67)
						)
					)
					(                        ; #at -1 20)
						(checkEvent
							pEvent
							(ghost nsLeft?)
							(ghost nsRight?)
							(ghost nsTop?)
							(ghost nsBottom?)
						)
						(if ghostVisible
							(PrintOther 58 8)
						else                     ; #width 280 #at -1 20) /*  */
							; Print(40 1) /* There is a sword lying against a dead tree. */
							(PrintOther 58 7)
						)
					)
				)
			)
		)                                        ; #at -1 20)
		(if
		(Said 'use,shoot,blow/dart,dartgun,gun,(dart<gun)')
			(if (gEgo has: INV_BLOW_DART_GUN)
				(PrintOther 58 48)
			else
				(PrintDHI)
			)
		)
		(if (Said 'look>')
			(if (Said '/chain,rope') (PrintOther 58 63))
			(if (Said '/ground') (PrintOther 58 9)) ; #width 280 #at -1 20)
			(if (Said '/body,corpse,man') (PrintOther 58 6))
			(if (Said '/skeleton,bone') (PrintOther 58 5))
			(if (Said '/note,letter,paper')
				(if (<= (gEgo distanceTo: note) 35)
					(RoomScript changeState: 12)
				else
					(PrintOther 58 67)
				)	
			)
			(if ghostVisible
				(if (Said 'give,place,leave,drop/flower')
					(if (gEgo has: 21)
						(PrintGhost 58 70)
					else
						(PrintDHI)
					)
				)
			)	
			(if (Said '/ghost')
				(if ghostVisible
					(PrintOther 58 8)
				else
					(PrintOther 58 60)
				)
			)
			(if (Said '/sword') (PrintOther 58 7))
			(if (Said '/water,river')
				(PrintOther 58 4)
				(if dartsVisible (PrintOther 58 47))
			)
			(if (Said '/rock') (PrintOther 58 59))
			(if (Said '/book')
				(if dartsVisible
					(PrintOther 58 57)
				else
					(PrintOther 58 58)
				)
			)
			(if (Said '/box,dart')
				(if dartsVisible
					(PrintOther 58 49)
				else                  ; #width 280 #at -1 8)
					; Print("How convenient!")
					(PrintOther 58 50)
				)
			)
			(if (Said '/book')
				(if dartsVisible
					(PrintOther 58 57)
				else                      ; #width 280 #at -1 8)
					(PrintOther 58 58)
				)
			)
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(PrintOther 58 10) ; #width 280 #at -1 8)
				(PrintOther 58 56)
			)
		)                         ; #width 280 #at -1 8)
		(if (Said 'search,examine/body,corpse,man')
			(cond 
				((<= (gEgo distanceTo: deadGuy) 45) (= searchBody 1) (RoomScript changeState: 1))
				;((<= (gEgo distanceTo: skeleton) 45) (= searchBody 0) (RoomScript changeState: 1))
				(else (= searchBody 2) (PrintNCE))
			; Print("The bones appear to be quite old. If you don't find a way out soon, you worry this fate may be yours." #width 280 #at -1 20)
			)
		)
		(if (Said '(pick<up),search,take/skeleton,bone')
			(PrintOther 58 38)
		)
		(if (Said '(pick<up),take,read/note,paper,letter')
			(if (<= (gEgo distanceTo: note) 35)
				(RoomScript changeState: 12)
			else
				(PrintNCE)
			)	
		) 
		(if (Said 'tickle,touch/skeleton,bone')  
			(PrintOther 58 78)		
		)               
		
		(if (Said '(pick<up),take,climb/chain,rope')
			(PrintOther 58 64)
		)
		(if (Said '(pick<up),take/sword')
			(if (<= (gEgo distanceTo: ghost) 27)
				(if (not g40Sword)
					(ghostScript changeState: 1)
					(hoverScript changeState: 1)
				else
					(PrintGhost 40 2)
				)
			else
				(PrintNCE)
			)
		)
		(if (Said '(pick<up),take/box,dart')
			(if (<= (gEgo distanceTo: dartsOnRiver) 70)
				(if dartsVisible
					(dartScript changeState: 3)
				else
					(PrintOther 58 52)
				)
			else                      ; There's nothing to pick up right now.
				(PrintNCE)
			)
		)
		
		(if (Said 'attack/ghost,man') (PrintOther 58 40)) ; #width 280 #at -1 20)
		(if (Said '(ask<about)>')
			(if ghostVisible
				(if (Said '/ghost')
					(PrintGhost 58 14)
					(PrintGhost 58 16)
				)
				(if (Said '/name')
					(PrintGhost 58 66)	
				)
				(if (Said '/sword') (PrintGhost 58 37))
				(if (Said '/voice,sound,haunting,curse')
					(PrintGhost 58 15)
					(PrintGhost 58 45)
				)
				(if (Said '/water') (PrintGhost 58 41))
				(if (Said '/betrayal,friend,sin,guilt')
					(PrintGhost 58 16)
					(PrintGhost 58 17)
				)
				(if (Said '/relic,buyer')
					(PrintGhost 58 18)
					(PrintGhost 58 19)
				)
				(if (Said '/death')
					(PrintGhost 58 79)	
				)
				(if (Said '/carmyle,longeau') (PrintGhost 58 20)(PrintGhost 58 21))
				(if (Said '/body,man,corpse') (PrintGhost 58 21))
				(if (Said '/skeleton') (PrintGhost 58 22))
				(if (Said '/cave,place') (PrintGhost 58 23))
				(if (Said '/princess') (PrintGhost 58 24))
				(if (Said '/wizard')
					(PrintGhost 58 25)
					(PrintGhost 58 26)
				)
				(if (Said '/weather') (PrintGhost 58 27))
				(if (Said '/*') (PrintGhost 58 28))
			else
				(= gWndColor 0) ; clBLACK
				(= gWndBack 15) ; clWHITE
				(if (Said '/*') (PrintOther 58 13))
			)
		)
		(if (Said 'talk/skeleton,corpse,(man<dead)')
			(Print 58 76)	
		)                             
		(if (Said 'talk/ghost,man')
			(if ghostVisible
				(PrintGhost 58 12) ; #width 280 #at -1 20 #title "He says:")
				(PrintGhost 58 65)
			else
				(PrintOther 58 13)
			)
		)                         ; #width 280 #at -1 20)
		(if (Said 'rub,feel,hold/marble,sun,shooter')
			(if (gEgo has: 9)
				(if (not gPlu) (Print 0 95) else (Print 0 94))
			else
				(PrintDHI)
			)
		)
		(if (Said 'use,drop,put,throw/marble')
			(if (not gPlu)
				(if (gEgo has: 9)
					(if (not sunDown)
						(Print 58 29 #at -1 20)
						(sun show: posn: 180 130)
						(= sunDown 1)
						(orbit changeState: 1)
					else
						(PrintOther 58 30)
					)
				else
					(PrintOther 58 31)
					(PrintOther 58 32)
				)
			else
				(PrintOther 58 33)
			)
		)
		(if (or (Said 'look,use,read,open/portal,map')
				(Said 'map'))
			(Print 0 88)
		)
		(if (Said '(pick<up),take/bone,skeleton')
			(PrintOther 58 77)	
		)
		(if (Said '(pick<up),take/marble')
			(cond 
				(sunDown
					(if (<= (gEgo distanceTo: sun) 40)
						; This is where an animation would be good.
						(RoomScript changeState: 8)
					else
						(PrintNCE)
					)
				)
				((not gPlu) ; can pick up the marble if you are simply close enough
					(if (<= (gEgo distanceTo: marble) 15)
						(PrintOther 58 61)
						(RoomScript changeState: 8)
					else
						(PrintOther 58 35)
					)
				)
				(else (PrintATI))
			)
		)
		(if (Said 'climb') (PrintOther 58 39))
		(if (or (Said 'listen[/!*]')
				(Said 'listen/voice'))
			(PrintWhisper)
			(if (== gListened 74)
				(PrintOther 58 75)	
			)
			(if (== gListened 75)
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 604
					register:
						{Where the voice came from, you cannot say. But you dwelt too deeply on despairing thoughts, driving you to lose your mind and your life.}
				)
				(gGame setScript: dyingScript)	
			)
		)
	)
	                          ; #width 280 #at -1 20)
	(method (doit)
		(super doit:)
		(if falling
			(SetCursor 997 (HaveMouse))
			(= gCurrentCursor 997)
		)
		(if (& (gEgo onControl:) ctlMAROON)
			(if sunDown
				(RoomScript changeState: 5)
			else
				(gRoom newRoom: 73)
			)
		)
		(cond 
			((<= (gEgo distanceTo: ghost) 45)
				(if g40Sword
					(if (not ghostVisible)
						(hoverScript changeState: 1)
						(= ghostVisible 1)
					)
				)
			)
			(ghostVisible (hoverScript changeState: 4) (= ghostVisible 0))
		)
	)
)

(procedure (PrintGhost textRes textResIndex)
	(= gWndColor 11)
	(= gWndBack 0)
	(Print textRes textResIndex #width 280 #at -1 20 #title {He says:})
	(= gWndColor 0) ; clBLACK
	(= gWndBack 15)
)
(procedure (PrintWhisper)
	(= gWndColor 9)
	(= gWndBack 0)
	(Print 58 gListened #width 280 #at -1 20 #title {A voice within your mind:})
	(= gWndColor 0) ; clBLACK
	(= gWndBack 15)
	(++ gListened)
)
                    ; clWHITE
(instance ghostScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(alterEgo
					init:
					show:
					view: 410
					loop: 1
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					setCycle: End ghostScript
					cycleSpeed: 2
					ignoreActors:
				)
			)
			(2
				(alterEgo
					view: 409
					loop: 1
					cel: 0
					setCycle: End ghostScript
					cycleSpeed: 2
					posn: (+ (gEgo x?) 10) (gEgo y?)
				)
			)
			(3
				(alterEgo hide:)
				(gEgo show: posn: (alterEgo x?) (alterEgo y?))
				(PrintGhost 58 36) ; #width 280 #at -1 20 #title "He says:")
				(= g40Sword 1)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
)

(instance hoverScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			(1
				(ghost loop: 1 setCycle: End hoverScript cycleSpeed: 2)
				(= ghostVisible 1)
				(gTheSoundFX number: 205 play:)
			)
			(2
				(ghost loop: 0 setCycle: Fwd)
			)
			(4
				(ghost loop: 1 cel: 4 setCycle: Beg hoverScript)
				(= ghostVisible 0)
				(gTheSoundFX number: 205 play:)
			)
			(5 (ghost loop: 2))
		)
	)
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

(instance fallScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(gEgo hide:)
				; (alterEgo:show()view(23)cel(2)posn( (send gEgo:x)(send gEgo:y) )yStep(7)setMotion(MoveTo 180 120 fallScript)setPri(14)ignoreControl(ctlWHITE))
				(alterEgo
					show:
					view: 129
					loop: 2
					posn: (chain x?) 10
					yStep: 2
					setCycle: Walk
					cycleSpeed: 2
					setMotion: MoveTo (chain x?) 100 fallScript
					setPri: 14
					ignoreControl: ctlWHITE
				)
				(= falling 1)
				(chain setCycle: Fwd cycleSpeed: 4)
			)
			(2
				(= cycles 10)   ; waiting a moment
				(alterEgo cel: 5)
				(chain setCycle: CT)
			)
			(3      ; looking down
				(alterEgo
					view: 129
					loop: 1
					cel: 0
					setCycle: End self
					cycleSpeed: 3
				)
			)
			(4      ; drop down
				(if (not (gEgo has: INV_BLOW_DART_GUN))
					(PrintOther 58 62)
				)
				(alterEgo
					view: 130
					yStep: 6
					setMotion: MoveTo (chain x?) 150 self
				)
				(chain setCycle: Fwd)
			)
			(5
				(= cycles 2)
				(chain setCycle: CT)
				; ShakeScreen(2)
				; (alterEgo:view(409)posn(173 138)loop(0)cel(0)setCycle(Fwd)cycleSpeed(3))
				(alterEgo
					view: 131
					loop: 0
					cel: 0
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(6 (ShakeScreen 2)
				(gTheSoundFX number: 200 play:)
			)
			(7
				(= falling 0)
				(alterEgo hide:)
				(gEgo show: x: (alterEgo x?) y: (alterEgo y?))
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				
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
				(= cycles 1)
				(dartsOnRiver show:)
			)
			(2
				(dartsOnRiver setMotion: MoveTo 77 165)
			)
			(3
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion:
						MoveTo
						(+ (dartsOnRiver x?) 20)
						(- (dartsOnRiver y?) 4)
						dartScript
					ignoreControl: ctlWHITE
				)
			)
			(4
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					loop: 1
					posn: (gEgo x?) (gEgo y?)
					setCycle: End dartScript
					cycleSpeed: 2
				)
			)
			(5
				(PrintOther 58 54) ; You take a small, wet box from the water. It contains ten darts.
				(PrintOther 58 55) ; You can keep track of these in your Character Stats in the Menubar.
				(= gApple (+ gApple 10))
				(dartsOnRiver hide:)
				(= dartsVisible 0)
				(alterEgo setCycle: Beg dartScript)
			)
			(6
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(alterEgo hide:)
				(gEgo show: observeControl: ctlWHITE)
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 100)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
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

(instance ghost of Prop
	(properties
		y 145
		x 80
		view 40
		loop 2
	)
)

(instance marble of Act
	(properties
		y 154
		x 207
		view 20
	)
)

(instance chain of Prop
	(properties
		view 156
		x 180
		y 60
		loop 1
	)
)

(instance skeleton of Prop
	(properties
		y 177
		x 270
		view 411
		loop 1
	)
)

(instance deadGuy of Prop
	(properties
		y 167
		x 147
		view 411
		loop 2
		cel 0
	)
)
(instance note of Prop
	(properties
		y 137
		x 183
		view 411
		loop 3
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
		y 180
		x 27
		view 410
		loop 1
	)
)

(instance dartsOnRiver of Act
	(properties
		y 170
		x 1
		view 9
	)
)
