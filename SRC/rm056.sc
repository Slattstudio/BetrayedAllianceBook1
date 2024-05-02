;;; Sierra Script 1.0 - (do not remove this comment)

(script# 56)
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
	rm056 0
)

; Old Demo Room




(instance rm056 of Rm
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
			(51
				(gEgo posn: 260 150 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState intro)
		(= state intro)
		(switch state
			(0 (= cycles 1))
			(1
				(= cycles 7)
				(gEgo setMotion: MoveTo 250 140)
			)
			(2 (= cycles 10))
		)
	)
	
	(method (handleEvent pEvent &tmp dyingScript)
		(super handleEvent: pEvent)
		(if (Said 'look') (Print 56 1))
	)
	
; It's too dark to see anything.
	(method (doit)
		(super doit:)
		(if (== (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 51)
		)
	)
)
