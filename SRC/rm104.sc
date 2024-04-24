;;; Sierra Script 1.0 - (do not remove this comment)

(script# 104)
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
	rm104 0
)

; Old Demo Room




(instance rm104 of Rm
	(properties
		picture 141
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
		(gEgo init: loop: 3 cel: 2)
		(daughter1 init: loop: 1)
		(daughter2 init: loop: 0 cel: 4)
		(king init:)
		(guard init:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState intro)
		(= state intro)
		(switch state
			(0
				(= cycles 1)
				(ProgramControl)
			)
			(1
				(= cycles 10)
				(daughter1 setCycle: Walk setMotion: MoveTo 215 87)
				(daughter2 setCycle: Walk setMotion: MoveTo 100 87)
			)
			; (guard:setCycle(Walk)setMotion(MoveTo 100 100))
			(2
				(= cycles 10)
				(daughter1 setCycle: Walk setMotion: MoveTo 210 75)
				(daughter2 setCycle: Walk setMotion: MoveTo 105 75)
			)
			(3
				(= cycles 10)
				(daughter1 setCycle: Walk setMotion: MoveTo 169 75)
				(daughter2 setCycle: Walk setMotion: MoveTo 133 77)
			)
			(4
				(= cycles 10)
				(daughter1 init: loop: 2)
				(daughter2 init: loop: 2 cel: 4)
			)
			(5 (= cycles 10) (Print 104 0))
; Glenn, it pains me to see to my eldest daughter missing."#title"King Kerezty says
			(6
				(= cycles 10)
				(king setCycle: End cycleSpeed: 2)
			)
			(7 (= cycles 10) (Print 104 1))
; My daughters are my most prized possesions."#title"King Kerezty says
			(8
				(= cycles 10)
				(Print 104 2)
; I know you are close to our family, and especially to Julyn..."#title"King Kerezty says
				(Print 104 3)
			)
; ...but you must be punished for allowing this to happen. On all days, you had to let something happen on this, our biggest day!"#title"King Kerezty says
			(9
				(= cycles 22)
				(king setCycle: Beg cycleSpeed: 2)
				(Print 104 4)
; Gilson, take Glenn away."#title"King Kerezty says
				(guard setCycle: Walk setMotion: MoveTo 100 100)
			)
			(10 (gRoom newRoom: 53))
		)
	)
)

(instance daughter1 of Act
	(properties
		y 87
		x 240
		view 304
	)
)

(instance daughter2 of Act
	(properties
		y 87
		x 78
		view 305
	)
)

(instance king of Prop
	(properties
		y 79
		x 151
		view 350
	)
)

(instance guard of Act
	(properties
		y 102
		x 20
		view 306
	)
)
