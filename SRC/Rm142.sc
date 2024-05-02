;;; Sierra Script 1.0 - (do not remove this comment)

(script# 142)
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
	rm142 0
)

; Inside the Graveyard (NIGHT)




(instance rm142 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 10))
			(1
				(douglas init: setCycle: End RoomScript cycleSpeed: 2)
			)
			(2 (douglas view: 315 loop: 1))
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
; (if(==(send pEvent:type())evMOUSEBUTTON)
;            (if(& (send pEvent:modifiers) emRIGHT_BUTTON)
;                (if((> (send pEvent:x) (arcade:nsLeft))and
;                    (< (send pEvent:x) (arcade:nsRight))and
;                    (> (send pEvent:y) (arcade:nsTop))and
;                    (< (send pEvent:y) (arcade:nsBottom)))
;                    Print(142 0) /* Wow! The latest version of Sail Boat Xtreme. Looks like it only cost one gold to play.
; )
;            )
;        )
		(if (Said))
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 41)
		)
		(if (& (gEgo onControl:) ctlGREY) (gRoom newRoom: 40))
	)
)

(instance douglas of Act
	(properties
		y 150
		x 280
		view 41                        ; 315 is Douglas sprite
	)
)
