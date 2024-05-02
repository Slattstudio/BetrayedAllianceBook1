;;; Sierra Script 1.0 - (do not remove this comment)
; +5 SCORE // gInt +5 //
(script# 31)
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
	rm031 0
)

(local

; Cave with OGRE



	; (use "sciaudio")
	prepareForBattle =  0
	trollGone =  0
	walkBack =  0
	ogreFollowing =  0
	crossedLine =  0
	ogreStanding =  0
	trollDead =  0
	guess =  0
	guessAgain
	coinShining = 0
)              ; used as a variable if the player clicks thru the dialog without an answer
; snd

(instance rm031 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(SetUpEgo)
		(gEgo init: setScript: proposeScript)
		; (send gTheMusic:prevSignal(0)stop())
		(= gEgoRunning 0)
		(RunningCheck)
		(cyclops init: setScript: cycWalk xStep: 4 yStep: 3)
		(gEgo observeControl: ctlBROWN)
		(if (< gTrollHealth 1)      ; troll defeated in combat
			(= trollDead 1)
			(= g31TrollGone 1)
			(cyclops posn: 130 145 loop: 10)
		)
		(if (not gRanAway)
			(if (not trollDead) (cycWalk changeState: 8))
		)
		(if g31TrollGone
			(= trollGone 1)
			(gEgo ignoreControl: ctlBROWN)
		)
		(self setScript: RoomScript setRegions: 204)
		(switch gPreviousRoomNumber
			
			(25
				(gEgo posn: 262 135 loop: 1)
				(gTheMusic number: 58 loop: -1 play:)
			)

			(71       
				(gEgo posn: 112 115 loop: 2)
				(gTheMusic number: 58 loop: -1 play:)
			)

			(32
				(gEgo posn: 43 132 loop: 0)
				(= crossedLine 3)
			)                     ; avoids triggering Ogre
			(36
				(gEgo posn: 43 132 loop: 0)
				(= crossedLine 3)
			)                     ; avoids triggering Ogre
			(103

				(if gRanAway
					; (cycWalk:changeState(0))
					(= gEgoRunning 1)
					(RunningCheck)
					(gEgo posn: 218 140 loop: 1 setMotion: MoveTo 300 140)
					(= gRanAway 0)
					(cyclops init: posn: 170 130 setCycle: Walk cycleSpeed: 0)
					(= ogreFollowing 1)
					(gTheMusic number: 59 loop: -1 play:)
				else
					; (cyclops:init()posn(1 1)hide())
					(= trollGone 1)
					(= g31TrollGone 1)
					(= trollDead 1)
					(gEgo ignoreControl: ctlBROWN posn: 150 145)
					(gTheMusic number: 58 loop: -1 play:)
				)
			)
			(else 
				(gEgo posn: 262 135 loop: 1)
			)
		)
		(alterEgo init: hide: ignoreActors: setScript: noLeaving)
		(coin init: ignoreActors: setPri: 3 setScript: coinShine)
		(if g31Gold (coin hide:))
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState &tmp dyingScript button)
		(= state mainState)
		(switch state
			(0)     ; = cycles 1
			(1 (= cycles 22))
			(2)
			; (send gTheMusic:prevSignal(0)stop()number(31)loop(-1)play())
			(4
				(= cycles 5)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(= ogreFollowing 0)
				(cyclops setMotion: NULL)
				(PrintOther 31 13)
			)
			(5
				(= gBatNum 300)
				(= gBruteStr 1)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gRoom newRoom: 103)
			)
; Riddles and Ego SQUISHING
			(6
				(ProgramControl)
				(gEgo
					setMotion: MoveTo (+ (cyclops x?) 8) (+ (cyclops y?) 6) self
					ignoreControl: ctlWHITE
					ignoreControl: ctlBROWN
				)
			)
			(7
				(= cycles 2)
				(gEgo hide:)
				(alterEgo
					show:
					view: 350
					loop: 0
					cel: 0
					posn: (gEgo x?) (gEgo y?)
				)
			)
			(8
				(PlayerControl)
				(if guessAgain
					(= gWndColor 0)
; clBLACK
					(= gWndBack 15)                             ; clWHITE
					(Print {You forgot to guess! Try again.})
					(= guessAgain 0)
				else
					(= gWndColor 14)
; clYELLOW
					(= gWndBack 4)                                ; clMAROON
					(Print 31 2 #title {Troll says:} #width 280 #at -1 20)
					(= gWndColor 0)
; clBLACK
					(= gWndBack 15)
				)                                               ; clWHITE
				(= button
					(Print 31 10 #button { Yes_} 1 #button { No_} 0 #at -1 10)
				)
				(= gWndColor 14)
; clYELLOW
				(= gWndBack 4)                                ; clMAROON
				(if (== button 1)
					(= guess
						(GetNumber
							{Tell me, how high is the tallest pole? A small shrimp like you will never be able to find out!}
						)
					)
					(if (== guess 51)
						(PrintOgre 31 14)
						(= trollGone 1)
						(= g31TrollGone 1)
						(if ogreStanding
							; sit down
							(cycWalk changeState: 6)
						else
							(cycWalk changeState: 9)
						)
						(gGame changeScore: 5)
						(gEgo
							show:
							ignoreControl: ctlBROWN
							observeControl: ctlWHITE
						)
						(alterEgo hide:)
						(PlayerControl)
						(= gInt (+ gInt 5))
					else
						; FormatPrint("guess: %u" guess)
						(if (== guess 65535)
							(= guessAgain 1)
							(RoomScript changeState: 7)
							(return)
						)
						(= guess 0)
						(if (== g31Guess 2)
							(RoomScript changeState: 10)
						else
							(PrintOgre 31 15)
							(gEgo
								show:
								loop: 3
								observeControl: ctlWHITE
								observeControl: ctlBROWN
							)
							(alterEgo hide:)
							(PlayerControl)
						)
						(++ g31Guess)
					)
				else
					(PrintOgre 31 30)
					(gEgo
						show:
						loop: 3
						observeControl: ctlWHITE
						observeControl: ctlBROWN
					)
					(alterEgo hide:)
					(PlayerControl)
				)
			)
			(9
				(ProgramControl)
				(gEgo
					setMotion: MoveTo (+ (cyclops x?) 8) (+ (cyclops y?) 6) RoomScript
					ignoreControl: ctlWHITE
					ignoreControl: ctlBROWN
				)
			)
			(10
				(= cycles 10)
				(= trollGone 1) ; dying, but this will prevent the doit method from interfering with the animation
				(cyclops loop: 3 cel: 0 setCycle: End cycleSpeed: 3)
			)
			(11
				(= cycles 10)
				(ShakeScreen 1)
				(cyclops loop: 2 cel: 0 setCycle: End)
				(alterEgo setCycle: End)
			)
			(12
				(= gWndColor 0)
; clBLACK
				(= gWndBack 15)                             ; clWHITE
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 607
					register: {Couldn't quite measure up, huh?}
				)
				(gGame setScript: dyingScript)
			)
		)
	)
	
	(method (handleEvent pEvent &tmp button dyingScript)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(cond 
					(
						(and
							(> (pEvent x?) (cyclops nsLeft?))
							(< (pEvent x?) (cyclops nsRight?))
							(> (pEvent y?) (cyclops nsTop?))
							(< (pEvent y?) (cyclops nsBottom?))
						)
						(if trollDead (PrintOther 31 34) else (PrintOther 31 1))
					)
					(                    ; #at -1 28 #width 270) /* This giant cyclops looks big, mean, and hungry. Best not get too close without protection. */
						(==
							ctlNAVY
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                  ; bones
						(PrintOther 31 28)
						(if (not g31Gold)
							(if (& (gEgo onControl:) ctlRED)
								(PrintOther 31 8)
							else
								(PrintOther 31 7)
							)
						)
					)
					((checkEvent pEvent 94 126 55 105) (PrintOther 31 16)) ; recess leading beyind the ogre.
				)
				(if (checkEvent pEvent 4 44 56 124) (PrintOther 31 17)) ; The passage to the west
			)
		)
		(if (Said 'smell[/!*]')
			(if (gEgo has: INV_MEAT)
				(PrintOther 31 40)		
			else
				(PrintOther 31 39)	
			)
		)
		(if
			(or
				(Said 'give/ring[/ogre,creature]')
				(Said 'give/ogre,creature/ring')
				(Said 'propose[/ogre,creature]')
			)
			(if (gEgo has: 18)
				(proposeScript changeState: 1)
			else
				(PrintDHI)
			)
		)
		(if (Said 'show,throw,give,use/meat,chicken')
			(if (gEgo has: INV_MEAT)
				(PrintOther 31 37)
			else
				(PrintDHI)
			)
		)
		(if (Said 'fight,attack/troll,monster')
			(cond 
				(trollDead (Print {The troll is dead.}))
				((<= (gEgo distanceTo: cyclops) 35)
					(if (gEgo has: INV_MEAT)
						(if (not trollGone)
							(PrintOther 31 35)
						else
							(PrintOther 31 36)
						)
					else
						(PrintOther 31 18)
					)
				)
				(else (PrintNCE))
			)
		)
		(if (Said '(ask<about)>')
			(if (Said '/riddle') (OgrePrintCheck 20))
			(if (Said '/bone,skull') (OgrePrintCheck 19))
			; PrintOgre(31 19)
			(if (Said '/pole,pillar') (OgrePrintCheck 21))
			; PrintOgre(31 21)
			(if (Said '/gold,treasure') (OgrePrintCheck 22))
			; PrintOgre(31 22)
			(if (Said '/*') (OgrePrintCheck 23))
		)
		; PrintOther(31 23)
		(if
			(or
				(Said 'talk/troll,monster')
				(Said
					'(tell,answer,guess,solve)/(number,riddle,question)'
				)
			)
			(cond 
				(trollDead (Print {The troll is dead.}))
				((not g31TrollGone)
					(if (<= (gEgo distanceTo: cyclops) 55)
						(if (gEgo has: INV_MEAT)
							(RoomScript changeState: 6)
						else
							(PrintOther 31 29)
						)
					else
						(PrintNCE)
					)
				)
				(else (PrintOther 31 24))
			)
		)
		(if (Said 'use/map') (PrintOther 31 25))
		(if
		(or (Said 'use/ruler') (Said 'measure/pole,pillar'))
			(PrintOther 31 26)
		)
		(if
			(or
				(Said 'throw/bomb')
				(Said 'use,light/bomb,stick')
				(Said 'use/kit/stick')
			)
			(cond 
				(trollDead (Print {There's no need for that here.}))
				((gEgo has: 12) (PrintOther 31 27))
				(else (Print 36 0))
			)
		)
; You can't throw what you don't have!
		(if
			(or
				(Said 'search/bone,pile')
				(Said 'take,(pick<up)/coin,gold')
			)
			(if (not g31Gold)
				(if (& (gEgo onControl:) ctlRED)
					; picking up the coin
					(PrintOK)
					(coinShine cycles: 0)
					(if coinShining
						(coinShine changeState: 9)	
					else
						(coinShine changeState: 3)
					)
				else
					(PrintNCE)
				)
			else
				(PrintOther 31 11)
			)
		)
		(if (or (Said 'search,loot/troll,monster,ogre,body')  (Said 'take/spoils'))
			(if trollDead
				(if (<= (gEgo distanceTo: cyclops) 40)
					(noLeaving changeState: 4)
				else
					(PrintNCE)
				)
			else
				(PrintOther 31 32)
			)
		)
		(if (Said 'look>')
			(if (Said '/ogre,troll,monster')
				(if trollDead
					(if (< (gEgo distanceTo: cyclops) 40)
						(noLeaving changeState: 4)	
					else 
						(PrintOther 31 34)
					)
				else 
					(PrintOther 31 1)
				)
			)                        ; #at -1 28 #width 270)
			(if (Said '/bone,pile')
				(PrintOther 31 28)
				(if (not g31Gold)
					(if (& (gEgo onControl:) ctlRED)
						(PrintOther 31 8)
					else
						(PrintOther 31 7)
					)
				)
			)
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(PrintOther 31 5)
				(cond 
					(trollDead)
					((not trollGone) (PrintOther 31 6))
				)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (not trollDead)
			; Ogre standing up at attention
			(if (< (gEgo x?) 225)
				(if (not ogreFollowing)
					(if (not trollGone)
						(if (== crossedLine 0)
							(cycWalk cycles: 0 changeState: 3)
							(= crossedLine 1)
						)
					)
				)
			)
			; Ogre coming after player
			(if (< (gEgo x?) 205)
				(if (not ogreFollowing)
					(if (not trollGone)
						(if (== crossedLine 1)
							(cycWalk changeState: 1)
							(= crossedLine 2)
						)
					)
				)
			)
			; If ogre angry
			(if ogreFollowing
				(cyclops
					setCycle: Walk
					setMotion: MoveTo (gEgo x?) (gEgo y?)
				)
			)
		)
		(if (& (gEgo onControl:) ctlSILVER)
			(if (== (IsOwnedBy 12 36) TRUE)
				(gRoom newRoom: 32)
			else
				(gRoom newRoom: 36)
			)
		)
		(if (& (gEgo onControl:) ctlMAROON)
			(if (gEgo has: 12)     ; explosives
				(if (not walkBack) (noLeaving changeState: 1))
			else
				(gRoom newRoom: 25)
			)
		)
		(if (& (gEgo onControl:) ctlGREY) (gRoom newRoom: 71))
		(if (<= (gEgo distanceTo: cyclops) 25)
			(if (not trollGone)
				(if (not (gEgo has: INV_MEAT))
					(if (not prepareForBattle)
						(RoomScript changeState: 4)
						(= prepareForBattle 1)
					)
				else
					(cycWalk cycles: 0)
					(if ogreStanding
						(cyclops loop: 9 cel: 0)
					else
						(cyclops loop: 9 cel: 1 setCycle: NULL)
					)
				)
			)
		)
	)
)

(instance cycWalk of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)     ; = cycles 3
			(1
				(= seconds 3)
				(if (not trollGone)
					(if (not (gEgo has: INV_MEAT))
						(= gWndColor 14)
; clYELLOW
						(= gWndBack 4)                                ; clMAROON
						(Print {Ah, fresh meat!} #dispose #at 100 20)
						(= gWndColor 0) ; clBLACK
						(= gWndBack 15) ; clWHITE
						(cyclops setCycle: Walk cycleSpeed: 0)
						(= ogreFollowing 1)
						(gTheMusic number: 59 loop: -1 play:)
					else
						(= gWndColor 14) ; clYELLOW
						(= gWndBack 4)  ; clMAROON
						(Print {Ach! That stench!?} #dispose #at 100 20)
						(= gWndColor 0) ; clBLACK
						(= gWndBack 15)
					)
				)
			)                           ; clWHITE
			(2
				; (cyclops:setMotion(MoveTo 270 175))
				(gPrintDlg dispose:)
				(if (gEgo has: INV_MEAT)
					(if (not g31TrollGone) (PrintOgre 31 2))
				)
			)                           ; #title "Troll says:" #width 280 #at -1 28)
			; Ogre standing at attention
			(3
				(ProgramControl)
				(cyclops loop: 8 cel: 0 setCycle: End self cycleSpeed: 4)
			)
			(4
				(PlayerControl)
				(PrintOther 31 12)
				(= ogreStanding 1)
			)
			; Ogre sitting
			(6
				(cyclops loop: 8 cel: 2 setCycle: End self cycleSpeed: 3)
			)
			(7
				(self changeState: 9)
				(= ogreStanding 0)
			)
			; Ogre eating
			(8
				(= cycles (Random 10 40))    ; chewing
				(cyclops loop: 4 setCycle: Fwd cycleSpeed: 3)
			)
			(9      ; bringing food to mouth
				(cyclops loop: 5 cel: 0 setCycle: End self)
			)
			(10      ; trying to pull meat from bone
				(cyclops loop: 6 cel: 0 setCycle: End self)
			)
			(11      ; snapping meat from bone
				(cyclops loop: 7 cel: 0 setCycle: End self)
			)
			(12 (= cycles 7))
			(13 (self changeState: 8)) ; back to chewing
		)
	)
)

(instance noLeaving of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= walkBack 0)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo (- (gEgo x?) 10) (gEgo y?) noLeaving
				)
			)
			(2
				(= cycles 1)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(3 (PrintOther 31 3))
			; searhing the bones
			(4
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo (+ (cyclops x?) 25) (+ (cyclops y?) 1) self
					ignoreControl: ctlWHITE
				)
			)
			(5
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					loop: 1
					posn: (gEgo x?) (gEgo y?)
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(6
				(= cycles 8)
				(PrintOther 31 33)
			)
			(7
				(alterEgo setCycle: Beg self)
			)
			(8
				(gEgo show: observeControl: ctlWHITE)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
)

(instance coinShine of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0
				(coin setCycle: End self cycleSpeed: 2)
				(= coinShining 1)
			)
			(1 (= cycles (Random 40 100))
				(= coinShining 0)	
			)
			(2 (self changeState: 0))
			(3      ; move to coin
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(self cycles: 0)
				(gEgo
					setMotion: MoveTo 153 115 self
					ignoreControl: ctlWHITE
				)
			)
			(4
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
			(5
				(= cycles 6)
				(PrintOther 31 4)
				(coin hide:)
				; (= coinVis 0)
				(++ gGold)
				(= g31Gold 1)
			)
			(6
				(alterEgo setCycle: Beg self)
			)
			(7
				(alterEgo hide:)
				(gEgo
					show:
					setMotion: MoveTo (gEgo x?) (+ (gEgo y?) 10) self
				)
			)
			(8
				(gEgo observeControl: ctlWHITE)
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
			)
			(9	(= cycles 70); waiting for coin to finish shining
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(= coinShining 0)		
			)
			(10
				(self cycles: 0)
				(self changeState: 3)	
			)
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
				(if (> (gEgo x?) (cyclops x?))
					(alterEgo loop: 1)	
				else
					(alterEgo loop: 0)
				)
			)
			(2 (= cycles 10)
				
			)
			(3
				(PrintOgre 31 38)
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

(procedure (OgrePrintCheck textRes)
	(cond 
		(trollDead (Print {The troll is dead.}))
		((<= (gEgo distanceTo: cyclops) 35)
			(if (gEgo has: INV_MEAT)
				(if (not g31TrollGone)
					(PrintOgre 31 textRes)
				else
					(PrintOgre 31 31)
				)
			else
				(PrintOther 31 23)
			)
		)
		(else (PrintNCE))
	)
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 100)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
	)
)

(procedure (PrintOgre textRes textResIndex)
	(= gWndColor 14)
; clYELLOW
	(= gWndBack 4)                                ; clMAROON
	(Print
		textRes
		textResIndex
		#width
		280
		#at
		-1
		20
		#title
		{Troll:}
	)
	(= gWndColor 0)
; clBLACK
	(= gWndBack 15)
)
                                                ; clWHITE
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

(instance cyclops of Act
	(properties
		y 130
		x 97
		view 28
		loop 5
	)
)

(instance alterEgo of Prop
	(properties
		y 1
		x 1
		view 350
	)
)

(instance coin of Prop
	(properties
		y 114
		x 168
		view 94
	)
)
