;;; Sierra Script 1.0 - (do not remove this comment)

(script# 21)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use door)
(use feature)
(use game)
(use inv)
(use main)
(use obj)

(public
	rm021 0
)

(local
; East of N. Castle



	randoFat
	randoOld
	; switchSides = 0
	[odds 4] = [1 3 5 7]
	[evens 4] = [0 2 4 6]
)

(instance rm021 of Rm
	(properties
		picture scriptNumber
		north 0
		east 63
		south 22
		west 20
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
				(gEgo posn: 150 130 loop: 1)
			)
			(20 (gEgo posn: 35 161 loop: 0))
			(22 (gEgo posn: 80 165 loop: 3))
			(53
				(gEgo posn: 150 137 loop: 2)
			)
			(63
				(gEgo posn: 300 150 loop: 1)
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		(oldMan
			init:
			ignoreActors:
			setScript: battleScript
			ignoreControl: ctlWHITE
		)
		(fatMan init: ignoreActors: ignoreControl: ctlWHITE)
		(= gNoClick 0) ; Reallows clicking to move if squirrel puzzle started but not finished.
		(= gEnNum 0) ; Resets amount of enemies to normal
		(battleScript cue:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (Said 'look>')
			(if (Said '/fence,men,soldier,field')
				(PrintOther 21 21)
			)
			(if (Said '/skull, well') (PrintOther 21 22))
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(PrintOther 21 20)
				(PrintOther 21 25)
			)
		)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				; skull and well
				(if (checkEvent pEvent 237 266 53 89)       ; skull
					(PrintOther 21 22)
				)
				(if (checkEvent pEvent 296 319 121 161)       ; stones to the east
					(PrintOther 21 23)
				)
				(if
					(or
						(checkEvent
							pEvent
							(oldMan nsLeft?)
							(oldMan nsRight?)
							(oldMan nsTop?)
							(oldMan nsBottom?)
						)
						(checkEvent
							pEvent
							(fatMan nsLeft?)
							(fatMan nsRight?)
							(fatMan nsTop?)
							(fatMan nsBottom?)
						)
					)
					(PrintOther 21 24)
				)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 53)
		)
	)
)

(instance battleScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(1
				(fatMan setCycle: End self cycleSpeed: 4)
				(oldMan setCycle: End cycleSpeed: 4)
			)
			(2
				; (if(switchSides)
				(= randoOld (Random 0 3))
				(oldMan loop: [odds randoOld] cel: 0 setCycle: End)
				(= randoFat (Random 0 3))
				(fatMan loop: [evens randoFat] cel: 0 setCycle: End self)
			)
			(3
				(= randoOld (Random 0 3))
				(oldMan loop: [odds randoOld] cel: 0 setCycle: End)
				(= randoFat (Random 0 3))
				(fatMan loop: [evens randoFat] cel: 0 setCycle: End self)
			)
; (if(switchSides)
;                    = randoFat Random (1 5)
;                    (fatMan:loop(evens[randoFat])cel(0)setCycle(End fatManScript))
;                    (if (== randoFat 4)
;                        (oldMan:loop(7)cel(0)setCycle(End))
;                        = switchSides 0
;                    )(else
;                        = randoOld Random (1 5)
;                        (if (== randoOld 4)
;                            (++ randoOld)
;                        )
;                        (oldMan:loop(odds[randoOld])cel(0)setCycle(End))
;                    )
;                )(else
;                    = randoFat Random (1 5)
;                    (fatMan:loop(odds[randoFat])cel(0)setCycle(End fatManScript))
;                    (if (== randoFat 4)
;                        (oldMan:loop(8)cel(0)setCycle(End))
;                        = switchSides 1
;                    )(else
;                        = randoOld Random (1 5)
;                        (if (== randoOld 4)
;                            (++ randoOld)
;                        )
;                        (oldMan:loop(evens[randoOld])cel(0)setCycle(End))
;                    )
;                )
			(4 (self changeState: 2))
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
	(if (> (gEgo y?) 100)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
	)
)

(instance oldMan of Act
	(properties
		y 72
		x 70
		view 227
		loop 1
	)
)

(instance fatMan of Act
	(properties
		y 72
		x 50
		view 226
	)
)
