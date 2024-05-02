;;; Sierra Script 1.0 - (do not remove this comment)
; + 2 SCORE
(script# 72)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use follow)

(public
	rm072 0
)


; Passage to Secret Grove



; (use "sciaudio")
; snd

(instance rm072 of Rm
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
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 150 loop: 1)
			)
			(63      ; Squirrel Music Puzzle
				(gEgo posn: 27 150 loop: 0)
				(squirrel posn: 1 150 setCycle: Walk ignoreActors:)
				
			)
			(65      ; Hidden "Grove"
				(gEgo posn: 294 150 loop: 1)
				(squirrel posn: 319 150 setCycle: Walk ignoreActors:)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(= gEgoRunning 0)
		(RunningCheck)
		(squirrel init:)
		(alterEgo init: hide: ignoreActors:)
		
		;(gTheMusic prevSignal: 0 fade:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1      ; Digging animation
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 155 146 RoomScript ignoreControl: ctlWHITE)
			)
			(2
				(= cycles 18)
				(gEgo hide:)
				(alterEgo
					show:
					view: 419
					loop: 0
					posn: (gEgo x?) (gEgo y?)
					setCycle: Fwd
					cycleSpeed: 2
				)
			)
			(3
				(alterEgo
					show:
					view: 232
					cel: 0
					setCycle: End RoomScript
					cycleSpeed: 2
				)
			)
			(4
				(= cycles 2)
				(if (== (IsOwnedBy INV_RING 69) FALSE)
					(if (not (gEgo has: INV_RING))
						(Print 72 0 #icon 209 #title {Ring}) ; "You unearth a ring, which you carefully store away.")
						(gEgo get: INV_RING)
						(gGame changeScore: 2)
					else
						(PrintOther 72 1)
					)
				else                     ; "You find nothing else but dirt and rocks.")
					(PrintOther 72 1)
				)
			)                        ; "You find nothing else but dirt and rocks.")
			(5
				(alterEgo setCycle: Beg RoomScript)
			)
			(6
				(alterEgo hide:)
				(gEgo show: loop: 0 observeControl: ctlWHITE)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(cond 
					(
						(and
							(> (pEvent x?) (squirrel nsLeft?))
							(< (pEvent x?) (squirrel nsRight?))
							(> (pEvent y?) (squirrel nsTop?))
							(< (pEvent y?) (squirrel nsBottom?))
						)
						(PrintOther 72 2)
					)
					(
						(and         ; Cute little guy seems to have taken a liking to you. Maybe you still smell like acorns.
							(> (pEvent x?) 148)    ; rock
							(< (pEvent x?) 166)
							(> (pEvent y?) 105)
							(< (pEvent y?) 128)
						)
						(PrintOther 72 3)
					)
				)
			)
		) 
		(if (or (Said 'listen[/!*]')
				(Said 'listen/voice,song'))
			(if g65Grove
				(PrintOther 72 10)	
			else
				(PrintOther 72 11)
				(Print 65 45  #at -1 20 #title {A distant voice:})
			)
		)                               
		(if (Said 'look,read>')
			(if (Said '/rock') (PrintOther 72 3))
			(if (Said '/wall')
				(PrintOther 72 8)	
			)
			(if (Said '/grass,ground') (PrintOther 72 4)) ; While dark in the cave you do notice that the dirt near the rock with the inscription seems to have been recently disturbed.
			(if (Said '/squirrel') (PrintOther 72 2))
			(if (Said '[/!*]') (PrintOther 72 5))
		; this will handle just "look" by itself
		)                        ; Sunlight floods the dark tunnel at both ends. At the eastern side of the short passage you can see clear water and grass.
; (if (Said('/*'))
;                // this will handle "look anyword"
;                Print("There's nothing particularly interesting about it." #width 280 #at -1 8)
;            )
		(if (Said 'move,push/rock') (PrintOther 72 6)) ; The rock is firmly in place, although the dirt near it seems a little loose.
		(if (or (Said 'use/shovel') (Said 'dig'))
			(if (gEgo has: INV_SHOVEL)
				(RoomScript changeState: 1)
			else
				(Print {You don't have a shovel.})
			)
		)
		(if (Said 'use/map')
			(Print {This isn't a good place to use that.})
		)
		(if (Said 'take,(pick<up)/ring')
			(if
				(or
					(== (IsOwnedBy INV_RING 69) TRUE)
					(if (gEgo has: INV_RING))
				)
				(PrintATI)
			else
				(PrintOther 72 7)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (>= (gEgo distanceTo: squirrel) 30)
			(squirrel setMotion: Follow gEgo)
		else
			(squirrel setMotion: NULL)
			(squirrel cel: 0)
		)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 63)
		)
		(if (& (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 65)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 10)
)

(instance squirrel of Act
	(properties
		y 150
		x 150
		view 61
	)
)

(instance alterEgo of Prop
	(properties
		y 150
		x 150
		view 0
	)
)
