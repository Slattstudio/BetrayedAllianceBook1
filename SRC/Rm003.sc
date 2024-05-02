;;; Sierra Script 1.0 - (do not remove this comment)

(script# 3)
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
	rm003 0
)




(instance rm003 of Rm
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
		(princess init: setCycle: Walk)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 1))
			(1
				(princess setMotion: MoveTo 0 80 RoomScript)
			)
			(2 (princess hide:))
		)
	)
)

(instance princess of Act
	(properties
		y 80
		x 60
		view 308
	)
)
