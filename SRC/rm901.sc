;;; Sierra Script 1.0 - (do not remove this comment)

(script# 901)
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
	rm901 0
)

; Credits




(instance rm901 of Rm
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
		(back init: setScript: princessScript)
		; (send gTheMusic:prevSignal(0)stop()number(800)loop(0)play())
		(gGame setCursor: gNormalCursor SET_CURSOR_VISIBLE 279 31)
	)
)
                                                                        ; sets cursor on the skip button

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 20))
			(1
				(= seconds 6)
				(job init:)
				(ryan init:)
			)
			(2
				(= seconds 6)
				(job cel: 1)
				(brandon init:)
				(ryan y: 122)
			)
			(3
				(= seconds 6)
				(job cel: 2)
				(brandon hide:)
				(ryan y: 100)
			)
			(4
				(= seconds 6)
				(job cel: 3 posn: 155 80)
				(alex init: cel: 0 y: 140)
				(ryan loop: 1 cel: 2 y: 110)
			)
			(5
				(= seconds 2)
				(job hide:)
				(ryan hide:)
				(alex hide:)
			)
			(6
				(= gDemo 0)
				(gRoom newRoom: 800)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if
				(and
					(> (pEvent x?) (back nsLeft?))
					(< (pEvent x?) (back nsRight?))
					(> (pEvent y?) (back nsTop?))
					(< (pEvent y?) (back nsBottom?))
				)
				(= gDemo 0)
				(gRoom newRoom: 800)
			)
		)
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_RETURN)
				(= gDemo 0)
				(gRoom newRoom: 800)
			)
		)
	)
)

(instance princessScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= seconds 3))
			(1
				(princess
					init:
					setCycle: Walk
					setMotion: MoveTo 174 152 princessScript
					ignoreActors:
				)
				(ego
					init:
					setCycle: Walk
					setMotion: MoveTo 151 152
					ignoreActors:
				)
			)
			(2
				(= seconds 2)
				(ego view: 407 cel: 0)
			)
			(3
				(= seconds 3)
				(princess
					view: 406
					posn: 169 152
					cel: 0
					loop: 0
					setCycle: End
				)
				(ego setCycle: End cycleSpeed: 2)
			)
			(4
				(princess setCycle: Beg)
				(ego
					loop: 1
					cel: 0
					setCycle: End princessScript
					cycleSpeed: 5
				)
				(julyn
					init:
					setCycle: Walk
					setMotion: MoveTo 167 150
					ignoreActors:
				)
			)
			(5
				(= seconds 3)
				(ego loop: 2 setCycle: Fwd cycleSpeed: 2)
				(princess
					view: 318
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo 320 142
				)
			)
			(6
				(= seconds 3)
				(julyn view: 2 loop: 3 cel: 0 setCycle: End cycleSpeed: 1)
				(ego loop: 0 cel: 0 setCycle: End cycleSpeed: 3)
			)
			(7
				(= seconds 2)
				(julyn setCycle: Beg)
				(ego
					loop: 1
					cel: 0
					setCycle: End princessScript
					cycleSpeed: 5
				)
			)
			(8
				(= seconds 4)
				(julyn
					view: 308
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo 0 152
				)
				(ego
					view: 0
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo 320 152
				)
			)
			(9
				(= seconds 3)
				(kq2
					init:
					setCycle: Walk
					cycleSpeed: 2
					setMotion: MoveTo 266 150
					ignoreActors:
				)
				(trodoss
					init:
					setCycle: Walk
					setMotion: MoveTo 54 150
					ignoreActors:
				)
			)
			(10
				(kq2 setMotion: MoveTo 320 120)
				(trodoss setMotion: MoveTo 0 120)
			)
		)
	)
)

(instance trodoss of Act
	(properties
		y 162
		x 0
		view 315
	)
)

(instance kq2 of Act
	(properties
		y 162
		x 320
		view 43
	)
)

(instance julyn of Act
	(properties
		y 162
		x 320
		view 308
	)
)

(instance princess of Act
	(properties
		y 152
		x 320
		view 318
		loop 1
	)
)

(instance ego of Act
	(properties
		y 150
		x 0
		view 0
	)
)

(instance job of Prop
	(properties
		y 60
		x 155
		view 985
	)
)

(instance ryan of Prop
	(properties
		y 100
		x 155
		view 985
		loop 1
	)
)

(instance alex of Prop
	(properties
		y 150
		x 160
		view 985
		loop 1
		cel 2
	)
)

(instance brandon of Prop
	(properties
		y 92
		x 160
		view 985
		loop 1
		cel 1
	)
)

(instance back of Prop
	(properties
		y 39
		x 286
		view 985
		loop 2
	)
)
