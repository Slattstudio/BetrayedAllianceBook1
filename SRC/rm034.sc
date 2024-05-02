;;; Sierra Script 1.0 - (do not remove this comment)
; Score +2 // gInt +2 //
(script# 34)
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
	rm034 0
)

(local

; Climbing Western Mountain



	comingIn =  1
	wtm
	rockDown =  0
	marblesDown =  0
	fallingDown =  0
)

(instance rm034 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self
			setScript: RoomScript
			setRegions: 200
			setRegions: 204
		)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 285 135 loop: 1)
			)
		)
		; (send gTheMusic:prevSignal(0)stop())
		(SetUpEgo)
		(= gEgoRunning 0)
		(RunningCheck)
		(gEgo init:)
		(alterEgo
			init:
			hide:
			setScript: JumpScript
			ignoreActors:
			ignoreControl: ctlWHITE
		)
		(deathSplash
			init:
			hide:
			ignoreActors:
			setScript: fallScript
		)
		(pathway
			init:
			setPri: 4
			ignoreActors:
			setScript: pickUpScript
		)
		(if gMer
			(boulder init: ignoreActors: setPri: 0 posn: 203 96)
			; (send gEgo:ignoreControl(ctlSILVER))
			(= rockDown 1)
		else
			(boulder
				init:
				setScript: RockFallScript
				ignoreControl: ctlWHITE
				ignoreActors:
				setPri: 0
			)
			(planet
				init:
				ignoreControl: ctlWHITE
				ignoreActors:
				setPri: 1
				setScript: OrbitScript
			)
		)
		(if (== (IsOwnedBy INV_MARBLES 34) TRUE)
			(boulder init: ignoreActors: setPri: 0 posn: 203 96)
			; (send gEgo:ignoreControl(ctlSILVER))
			(= rockDown 1)
			(= marblesDown 1)
			(sun init: show: posn: 241 55 setCycle: CT setPri: 2)
			(planet posn: 245 53)
			(OrbitScript changeState: 3)
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 1))
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 247 135 RoomScript
					ignoreControl: ctlWHITE
				)
			)
			(2
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gEgo observeControl: ctlWHITE)
				(= comingIn 0)
			)
			(3
				(= cycles 10)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 314 23 ignoreControl: ctlWHITE)
			)
			(4
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gRoom newRoom: 26)
			)
			(6
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 273 48 RoomScript
					ignoreControl: ctlWHITE
				)
			)
			(7
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gEgo observeControl: ctlWHITE)
				(= wtm 0)
			)
			(8
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 247 135 RoomScript
					ignoreControl: ctlWHITE
				)
			)
			(9
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gEgo observeControl: ctlWHITE)
				(= wtm 0)
			)
		)
	)
	
	(method (handleEvent pEvent &tmp [buffer 50])
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) (boulder nsLeft?))
						(< (pEvent x?) (boulder nsRight?))
						(> (pEvent y?) (boulder nsTop?))
						(< (pEvent y?) (boulder nsBottom?))
					)
					(if rockDown
						(Print 34 17)
					else
; Rolling Rock.
						(Print 34 0 #width 280 #at -1 65)
					)
				)
				(if
					(and
						(> (pEvent x?) 155)
						(< (pEvent x?) 222)
						(> (pEvent y?) 31)
						(< (pEvent y?) 60)
					)
					(Print 34 1 #width 280 #at -1 65)
				)
			)
		)
; (if(Said('use/map'))
;            (if(marblesDown)
;                Print(34 9 #width 280 #at -1 8)
;            )(else
;                ()
;            )
;        )
		(if (Said 'look>')
			(if (Said '/rock,boulder')
				(if rockDown (PrintOther 34 17) else (PrintOther 34 0))
			)                        ; #width 280 #at -1 8)
			(if (Said '/marble')
				(if rockDown
					(if marblesDown
						(PrintOther 34 16)
					else
						(Format @buffer {Marbles that act upon each other with solar system-like gravitational forces. You have %u.} gMarbleNum)
						(Print @buffer #title "marbles" #icon 261)
					)
				else
					(PrintOther 34 14)
				)
			)
			(if (Said '/cliff,ground') (PrintOther 34 13))
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(if rockDown (PrintOther 34 18) else (PrintOther 34 19))
			)
		)
		(if (Said 'remove,(take<off)/armor')
			; Print("Perhaps I could jump across without my armor..." #title "You think:" #at -1 10 #width 280)
			; Print("Also perhaps there are starving flesh-eating Griffins with razor-sharp claws over the clearing..." #title "You think:" #at -1 10 #width 280)
			(PrintOther 34 21)
			(PrintOther 34 22)
		)
		(if (Said 'run') (Print 34 2 #at -1 10))
		(if (Said 'climb') (Print 34 11 #at -1 10))
		(if (or
				(Said 'use/shovel[/marble]')
				(Said 'hit,dig,remove/rock,marble/shovel') 
			)
			(PrintOther 34 27)
		)
		(if (Said 'jump')
			(if (not rockDown)
				(if (== (gEgo onControl:) ctlGREY)
					(JumpScript changeState: 2)
				else
					(PrintNCE)
				)
			else
				(Print {There's no need to jump now.})
			)
		)
		(if (Said 'rub,feel,hold/marble,sun,shooter')
			(if (gEgo has: 9)
				(if (not gMer) (Print 0 95) else (Print 0 94))
			else
				(PrintDHI)
			)
		)
		(if (Said 'use/marble')
			(if (gEgo has: 9)
				(if (== (gEgo onControl:) ctlGREY)
					(if (not gHardMode)
						(if (not gMer)
							(Print 34 3 #at -1 65)
						else
							(Print {Wheeee!} #at -1 55)
						)
						; Print(34 4 #at -1 65)
						(RockFallScript changeState: 2)
					else
						(PrintOther 34 5)
					)
				else
					(PrintOther 34 12)
				)
			else
				(PrintOther 34 23)
			)
		)
		(if (Said 'drop/marble,shooter[/!*]')
			(if (gEgo has: 9)
				(PrintOther 34 10)
			else
				(PrintDHI)
			)
		)
		(if (or (Said 'shoot,roll/marble,shooter')
				(Said 'put,drop/marble,shooter[/ground,incline,hill,cave,rock]'))
			(if (gEgo has: 9)
				(if (== (gEgo onControl:) ctlGREY)
					; (if(not(rockDown))
					(if (not gMer)
						(PrintOther 34 3)
					else
						(Print {Wheeee!})
					)
					; Print(34 4 #at -1 65)
					(RockFallScript changeState: 2)
				else
					(PrintNCE)
				)
			else
				(PrintDHI)
			)
		)
		
		(if (Said '(pick<up),take,get/marble,shooter')
			(if rockDown
				(if (not (gEgo has: 9))
					(if (<= (gEgo distanceTo: sun) 20)
						(pickUpScript changeState: 1)
					else
						(PrintNCE #at -1 65)
					)
				else
					(PrintATI #at -1 65)
				)
			else
				(Print 34 7 #at -1 65)
			)
		)
		(if (Said 'take,push/boulder')
			(if (not rockDown)
				(PrintOther 34 24)
				(PrintOther 34 22)
			else
				(PrintOther 34 25)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (not fallingDown)
			(if (< (gEgo y?) 58)
				(gEgo setPri: 3)
			else
				(gEgo setPri: -1)
			)
		)
		(if (& (gEgo onControl:) ctlBLUE)
			(if (not fallingDown)
				(fallScript changeState: 1)
				(= fallingDown 1)
			)
		)
		(if (& (gEgo onControl:) ctlSILVER)
			(if (not rockDown)
				(if (not fallingDown)
					(fallScript changeState: 1)
					(= fallingDown 1)
				)
			)
		)
		(if (& (gEgo onControl:) ctlNAVY)
			(if (not fallingDown)
				(fallScript changeState: 4)
				(= fallingDown 1)
			)
		)
		(if (& (gEgo onControl:) ctlMAROON)
			(cond 
				(gMer
					(if (== wtm 0)
						(Print 34 8 #width 280 #at -1 80)
						(++ wtm)
						(RoomScript changeState: 3)
					)
				)
				((== wtm 0) (Print 34 9) (++ wtm) (RoomScript changeState: 6))
			)
		)
		(if (& (gEgo onControl:) ctlRED)
			(if (not comingIn)
				(if rockDown
					(if (not gMer)
						(if (== wtm 0)
							(Print 34 9 #width 280 #at -1 80)
							(++ wtm)
							(RoomScript changeState: 8)
						)
					else
						(gRoom newRoom: 27)
					)
				else
					(gRoom newRoom: 27)
				)
			)
		)
	)
)

(instance RockFallScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(2
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(sun
					init:
					posn: 151 26
					show:
					ignoreControl: ctlWHITE
					ignoreActors:
					setPri: 1
					setCycle: Fwd
					setMotion: MoveTo 199 33 RockFallScript
				)
				(gEgo loop: 3 put: INV_MARBLES 34)
			)
			(3
				(if (not rockDown)
					(ShakeScreen 3)
					(planet setCycle: Fwd setMotion: MoveTo 229 36)
					(boulder setCycle: Fwd yStep: 5 setMotion: MoveTo 203 96)
					(gTheSoundFX number: 200 play:)
				)
				(sun setMotion: MoveTo 229 36 RockFallScript)
				(gEgo loop: 0)
			)
			(4
				(if (not rockDown)
					(planet setCycle: Fwd setMotion: MoveTo 246 55)
					(ShakeScreen 2)
					(boulder setCycle: CT)
					; (send gEgo:ignoreControl(ctlSILVER))
					(= rockDown 1)
					(gGame changeScore: 2)
					(= gInt (+ gInt 2))
				)
				(sun setMotion: MoveTo 241 55 RockFallScript)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(5
				(if (not gMer) (OrbitScript changeState: 3))
				(sun setCycle: CT setPri: 2)
				(= marblesDown 1)
			)
		)
	)
)

(instance JumpScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(2
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 173 58 JumpScript)
			)
			(3
				(gEgo hide:)
				(alterEgo
					init:
					show:
					posn: (gEgo x?) (gEgo y?)
					view: 416
					ignoreActors:
					ignoreControl: ctlWHITE
					xStep: 5
					yStep: 6
					setMotion: MoveTo 189 45 JumpScript
				)
			)
			(4
				(alterEgo view: 417 setMotion: MoveTo 217 60 JumpScript)
			)
			(5
				(alterEgo view: 418 setMotion: MoveTo 265 136 JumpScript)
			)
			(6
				(= cycles 1)
				(alterEgo hide:)
				(gEgo show: posn: (alterEgo x?) (alterEgo y?))
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(7
				(Print
					{It seems with all of your heavy armor even this narrow jump is too great. Try shooting for another solution.}
					#width
					280
					#at
					-1
					8
				)
			)
		)
	)
)

(instance OrbitScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(2
				(planet setMotion: MoveTo 245 57 OrbitScript)
			)
			(3
				(planet setMotion: MoveTo 243 58 OrbitScript)
			)
			(4
				(planet setMotion: MoveTo 240 57 OrbitScript)
			)
			(5
				(planet setMotion: MoveTo 238 55 OrbitScript)
			)
			(6
				(planet setMotion: MoveTo 240 53 OrbitScript)
			)
			(7
				(planet setMotion: MoveTo 243 52 OrbitScript)
			)
			(8
				(planet setMotion: MoveTo 245 53 OrbitScript)
			)
			(9
				(planet setMotion: MoveTo 245 54 OrbitScript)
			)
			(10
				(OrbitScript changeState: 2)
			)
		)
	)
)

(instance pickUpScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 260 53 pickUpScript
					ignoreControl: ctlWHITE
				)
			)
			(2
				(gEgo hide:)
				(alterEgo
					init:
					show:
					view: 232
					loop: 1
					posn: 260 53
					setCycle: End pickUpScript
					cycleSpeed: 1
				)
			)
			(3
				(= cycles 8)
				(sun hide:)
				(planet hide:)
				(gEgo get: 9)
				(if (not gMer)
					(++ gMarbleNum)
					((gInv at: 9) count: gMarbleNum)
				)
				(= gMer 1)
				(= marblesDown 0)
				(PrintOther 34 26)
			)
			(4
				(alterEgo setCycle: Beg pickUpScript)
			)
			(5
				(gEgo show: get: INV_MARBLES observeControl: ctlWHITE)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
)

(instance fallScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0)
			(1        ; falling from bottom
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(if (> (gEgo y?) 95)     ; if lower lever
					(alterEgo setPri: 5)
				else
					(alterEgo setPri: 3)
				)
				(alterEgo
					show:
					view: 23
					loop: 3
					cel: 0
					posn: (+ (gEgo x?) 10) (gEgo y?)
					yStep: 7
					setMotion: MoveTo (+ (gEgo x?) 10) 174 self
				)
			)
			(2
				(alterEgo hide:)
				(if (> (gEgo y?) 95)
					(deathSplash setPri: 14)
				else
					(deathSplash setPri: 1)
					(ShakeScreen 1)
				)
				(deathSplash
					show:
					posn: (+ (gEgo x?) 10) 200
					setCycle: End self
					cycleSpeed: 2
				)
				(gTheSoundFX number: 202 play:)
			)
			(3 (self changeState: 10))
			(4       ; falling from top
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(if (> (gEgo y?) 71)     ; if lower lever
					(alterEgo setPri: 3)
				else
					(alterEgo setPri: 1)
				)
				(alterEgo
					show:
					view: 88
					cel: 0
					posn: (+ (gEgo x?) 10) (gEgo y?)
					yStep: 8
					setMotion: MoveTo (+ (gEgo x?) 10) 186 self
				)
			)
			(5
				(alterEgo hide:)
				(deathSplash
					show:
					posn: (+ (gEgo x?) 10) (alterEgo y?)
					setCycle: End self
					setPri: 1
					cycleSpeed: 2
				)
				(ShakeScreen 1)
				(gTheSoundFX number: 200 play:)
			)
			(6 (self changeState: 10))
			(10
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 610
					register:
						{You like to forge your own path. Unfortunately your path was off THE path. Thanks for playing Betrayed Alliance!}
				)
				(gGame setScript: dyingScript)
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

(instance boulder of Act
	(properties
		y 35
		x 195
		view 29
	)
)

(instance planet of Act
	(properties
		y 33
		x 195
		view 30
	)
)

(instance sun of Act
	(properties
		y 26
		x 151
		view 21
	)
)

(instance pathway of Prop
	(properties
		y 152
		x 294
		view 34
	)
)

(instance alterEgo of Act
	(properties
		y 60
		x 165
		view 246
	)
)

(instance deathSplash of Prop
	(properties
		y 187
		x 147
		view 87
		loop 1
	)
)
