;;; Sierra Script 1.0 - (do not remove this comment)

(script# 100)
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
	rm100 0
)

; River Twygs??




(instance rm100 of Rm
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
		(grim init:)
		(boat init:)
		(C init: setScript: cFlash)
		(waveA init: setCycle: Fwd cycleSpeed: 2)
		(rockA init: setCycle: Fwd cycleSpeed: 4)
	)
)


(instance RoomScript of Script
	(properties)
)

(instance cFlash of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 8))
			(1
				(= cycles (Random 1 15))
				(C cel: 1)
			)
			(2
				(= cycles (Random 1 15))
				(C cel: 0)
			)
			(3 (cFlash changeState: 1))
		)
	)
)

(instance grim of Prop
	(properties
		y 149
		x 230
		view 401
	)
)

(instance boat of Prop
	(properties
		y 160
		x 170
		view 517
	)
)

(instance C of Prop
	(properties
		y 78
		x 301
		view 15
		loop 1
	)
)

(instance waveA of Prop
	(properties
		y 121
		x 243
		view 247
		loop 2
	)
)

(instance rockA of Prop
	(properties
		y 160
		x 60
		view 508
	)
)
