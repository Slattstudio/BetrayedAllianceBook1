;;; Sierra Script 1.0 - (do not remove this comment)
; + 1 SCORE
(script# 64)
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
	rm064 0
)

(local

; East Bridge Kite Scene



	ropePull =  0
	falling =  0
)

(instance rm064 of Rm
	(properties
		picture scriptNumber
		north 0
		east 61
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
				(gEgo posn: 300 136 loop: 1)
			)
			(61
				(gEgo posn: 300 136 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		(egoFall
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
		)
		(wind
			init:
			hide:
			ignoreActors:
			setScript: windScript
			setPri: 15
		)
		(wind2
			init:
			hide:
			ignoreActors:
			setScript: windScript2
			setPri: 15
		)
		(windKite init: hide: ignoreActors: setPri: 15)
		(kite
			init:
			hide:
			setScript: kiteScript
			setPri: 14
			ignoreControl: ctlWHITE
		)
		(note init:)
		(rope init: hide: ignoreActors: setScript: getKiteScript)
		(kiteScript changeState: 1)
		(if (not (gEgo has: INV_KITE))
			(if (== (IsOwnedBy 4 50) FALSE)
				(kite show:)
				(rope show:)
			)
		)
		
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState &tmp dyingScript)
		(= state mainState)
		(switch state
			(0)
			(3
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				;(ShakeScreen 3)
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 603
					register:
						{Quite a graceless fall! You'll have to practice if you expect to wow any olympic judges. Until then though, maybe just work on not dying.}
				)
				(gGame setScript: dyingScript)
			)
			(4
				(ProgramControl)
				(gEgo hide:)
				(egoFall
					show:
					view: 23
					posn: (gEgo x?) (gEgo y?)
					yStep: 7
					setMotion: MoveTo (gEgo x?) 180 self
				)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)
			; (send gTheMusic:prevSignal(0)stop()number(903)play())
			(5
				(= cycles 20)
				(egoFall hide:)
			)
			(6 	(= cycles 20)
				(ShakeScreen 1)
				(gTheSoundFX number: 202 play:)
			)
			(7
				(RoomScript changeState: 3)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(==
						ctlMAROON
						(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
					)
					(PrintOther 64 1)
				)
				(cond 
					(
						(and
							(> (pEvent x?) (kite nsLeft?))
							(< (pEvent x?) (kite nsRight?))
							(> (pEvent y?) (kite nsTop?))
							(< (pEvent y?) (kite nsBottom?))
						)
						(PrintOther 64 7)
					)
					(
						(and
							(> (pEvent x?) 50)       ; tavern
							(< (pEvent x?) 69)
							(> (pEvent y?) 14)
							(< (pEvent y?) 26)
						)
						(PrintOther 64 2)
					)
				)
			)
		)
		(if (Said 'read/letter,note,paper,notice')
			(if (<= (gEgo distanceTo: note) 80)
				(getKiteScript changeState: 1)
			else
				(PrintOther 64 8)
			)
		)
		(if (Said 'look>')
			(if (Said '/kite')
				(cond 
					((gEgo has: INV_KITE) (Print 0 4 #title "kite" #icon 255))
					((== (IsOwnedBy INV_KITE 50) FALSE) (PrintOther 64 4))
					(else (PrintOther 64 3))
				)
			)
			(if (Said '/letter,note,paper')
				(if (<= (gEgo distanceTo: note) 80)
					(getKiteScript changeState: 1)
				else
					(PrintOther 64 8)
				)
			)
			(if (Said '/view') (PrintOther 64 5))
			(if (Said '/library') (PrintOther 64 15))
			(if (Said '/pub,bar,tavern') (PrintOther 64 2))
			(if (Said '/building') (PrintOther 64 14))
			(if (Said '/water,river,lake,sea') (PrintOther 64 6))
			(if (Said '/fence')
				(cond 
					((gEgo has: INV_KITE) (PrintOther 64 12))
					((== (IsOwnedBy INV_KITE 50) FALSE) (PrintOther 64 7))
					(else (PrintOther 64 12))
				)
			)
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(PrintOther 64 0)
				(if
					(or
						(not (gEgo has: INV_KITE))
						(== (IsOwnedBy INV_KITE 50) TRUE)
					)
					(PrintOther 64 4)
				)
			)
		)
		(if (Said '(pick<up),(pull<in),take/kite')
			(if (not (gEgo has: INV_KITE))
				(if (== (IsOwnedBy INV_KITE 50) FALSE)
					(if (<= (gEgo distanceTo: rope) 125)
						(PrintOK)
						(getKiteScript changeState: 4)
					else
						(PrintNCE)
					)
				)
			else
				(PrintATI)
			)
		)
		(if (Said 'feel[/wind]') (PrintOther 64 13))
		(if (Said 'fly,use/kite,glider')
			(if (or (gEgo has: INV_KITE) (gEgo has:INV_GLIDER))
				(PrintOther 64 11)
			else
				(Print {You don't have a kite.} #width 280 #at -1 10)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlSILVER)
			(if (not falling)
				(RoomScript changeState: 4)
				(= falling 1)
			)
		)
		(cond 
			(ropePull
				(cond 
					((< (kite y?) 98) (rope cel: 0))
					((< (kite y?) 112) (rope cel: 1))
					((< (kite y?) 126) (rope cel: 2))
					((< (kite y?) 132) (rope hide:))
				)
			)
			((< (kite x?) 88) (rope cel: 0))
			((< (kite x?) 96) (rope cel: 1))
			((< (kite x?) 104) (rope cel: 2))
			((< (kite x?) 112) (rope cel: 3))
			((< (kite x?) 121) (rope cel: 4))
		)
	)
)

(instance kiteScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(if (not ropePull)
					(kite
						setMotion: MoveTo (Random 80 120) (Random 62 68) kiteScript
					)
				)
			)
			(2
				(if (not ropePull)
					(kite
						setMotion: MoveTo (Random 80 120) (Random 62 68) kiteScript
					)
				)
			)
			(3 (kiteScript changeState: 1))
		)
	)
)

(instance getKiteScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			; moving to read note
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 154 138 self
					ignoreControl: ctlWHITE
				)
			)
			(2 (= cycles 2) (gEgo loop: 1))
			(3
				(Print 64 9 #at 10 -1 #width 100 #font 4)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			; Moving to take the kite
			(4
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 134 138 self
					ignoreControl: ctlWHITE
				)
			)
			(5 (= cycles 2) (gEgo loop: 3))
			(6
				(= cycles 1)
				(Print 64 10 #icon 255 #title {Kite})
				(kite hide:)
				(rope hide:)
				(gEgo get: INV_KITE observeControl: ctlWHITE)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(7 (gGame changeScore: 1))
		)
	)
)

(instance windKiteScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1))
			; acorn blowing in the wind
			(1
				(if (not (gEgo has: INV_KITE))
					(if (== (IsOwnedBy INV_KITE 50) FALSE)
						(windKite
							show:
							cel: 0
							posn: (kite x?) (kite y?)
							setCycle: End
							cycleSpeed: 1
						)
					)
				)
			)
			(2 (= cycles (Random 20 70)))
			(3 (self changeState: 1))
		)
	)
)

(instance windScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles (Random 1 10)))
			(1
				(wind
					show:
					cel: 0
					posn: (Random 70 270) (Random 50 170)
					setCycle: End self
					cycleSpeed: 1
				)
			)
			(2 (self changeState: 0))
		)
	)
)

(instance windScript2 of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles (Random 1 15)))
			(1
				(wind
					show:
					cel: 0
					posn: (Random 70 270) (Random 50 170)
					setCycle: End self
					cycleSpeed: 1
				)
			)
			(2 (self changeState: 0))
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

(instance kite of Act
	(properties
		y 65
		x 100
		view 75
	)
)

(instance rope of Prop
	(properties
		y 129
		x 105
		view 76
	)
)

(instance note of Prop
	(properties
		y 118
		x 141
		view 76
		loop 2
	)
)

(instance egoFall of Act
	(properties
		y 130
		x 40
		view 33
	)
)

(instance windKite of Prop
	(properties
		y 150
		x 181
		view 58
		loop 3
	)
)

(instance wind of Prop
	(properties
		y 150
		x 181
		view 58
		loop 3
	)
)

(instance wind2 of Prop
	(properties
		y 150
		x 181
		view 58
		loop 3
	)
)
