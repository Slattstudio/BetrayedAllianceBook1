;;; Sierra Script 1.0 - (do not remove this comment)

(script# 12)
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
	rm012 0
)




(instance rm012 of Rm
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
)
