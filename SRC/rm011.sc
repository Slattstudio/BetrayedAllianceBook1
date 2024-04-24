;;; Sierra Script 1.0 - (do not remove this comment)

(script# 11)
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
	rm011 0
)




(instance rm011 of Rm
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
		(gEgo init: hide:)
		; (princess:init())
		; (faerie1:init()loop(1)setCycle(Fwd)cycleSpeed(2))
		; (faerie2:init()loop(2)setCycle(Fwd)cycleSpeed(2))
		(rope init: ignoreActors: setPri: 0)
		(star1 init: loop: 5 setCycle: Fwd)
		(star2 init: loop: 5 setCycle: Fwd)
		(eliade
			init:
			ignoreActors:
			posn: 7 40
			loop: 2
			setCycle: Walk
		)
	)
)
                                                                    ; setMotion(MoveTo 100 177))

(instance RoomScript of Script
	(properties)
	
	(method (changeState intro)
		(= state intro)
		(switch state
			(0 (= cycles 1))
			(1
				(= cycles 12)
				(ProgramControl)
				(eliade setMotion: MoveTo 30 40)
			)
			(2 (= cycles 15))
			(3
				(= cycles 15)
				(eliade hide:)
				(jump init: loop: 3 setCycle: End cycleSpeed: 1)
			)
			(4
				(= cycles 5)
				(eliade
					posn: 46 50
					xStep: 2
					yStep: 4
					show:
					cel: 2
					setCycle: Walk
					setMotion: MoveTo 46 150
				)                                                                                     ; sliding down rope
				(jump hide:)
			)
			(5
				(= cycles 13)
				(rope setCycle: End cycleSpeed: 2)
			)                                    ; rope moves under wieght
			(6
				(= cycles 10)
				(eliade
					view: 9
					yStep: 5
					loop: 0
					cel: 0
					setCycle: End
					cycleSpeed: 2
					setMotion: MoveTo 46 173
				)
			)                                                                                          ; eliade drops
			(7
				(= cycles 10)
				(eliade loop: 1 cel: 0 setCycle: End cycleSpeed: 1)
			)
			(8
				(= cycles 25)
				(eliade hide:)
				(runaway
					init:
					setCycle: Walk
					xStep: 4
					setMotion: MoveTo 150 179
				)
			)
			(9
				(= cycles 18)
				(runaway setMotion: MoveTo 260 175)
			)
			(10
				(= cycles 14)
				(runaway setMotion: MoveTo 380 169)
			)
			(11 (= cycles 10))
			(12
				(= cycles 10)
				(runaway hide:)
			)
			(13 (= cycles 10))
			(14
				(= cycles 10)
				(gRoom newRoom: 37)
			)
		)
	)
)

(instance princess of Prop
	(properties
		y 38
		x 30
		view 12
	)
)

(instance faerie1 of Prop
	(properties
		y 29
		x 52
		view 12
	)
)

(instance faerie2 of Prop
	(properties
		y 35
		x 47
		view 12
	)
)

(instance eliade of Act
	(properties
		y 120
		x 308
		view 5
	)
)

(instance rope of Act
	(properties
		y 162
		x 55
		view 6
	)
)

(instance jump of Prop
	(properties
		y 47
		x 39
		view 5
	)
)

(instance runaway of Act
	(properties
		y 171
		x 46
		view 5
	)
)

(instance star1 of Prop
	(properties
		y 28
		x 35
		view 6
	)
)

(instance star2 of Prop
	(properties
		y 47
		x 270
		view 6
	)
)
