;;; Sierra Script 1.0 - (do not remove this comment)

(script# 13)
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
	rm013 0
	Showviews 1
	Hide 2
)

(local


	checkpoint =  0
	doorOpen =  0
	doorBroken =  0
)

(instance rm013 of Rm
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
				(gEgo posn: 250 130 loop: 1)
			)
			(50 (gEgo posn: 120 100))
		)
		(SetUpEgo)
		(gEgo init: hide: ignoreActors:)
		(julyn init: loop: 1)
		(glassDoor init: ignoreActors:)
		(bed init: ignoreActors: setPri: 0)
		(roomDoor init:)
		(sheet init: loop: 1 ignoreActors: setPri: 4)
		(table
			init:
			loop: 2
			setCycle: Fwd
			cycleSpeed: 3
			ignoreActors:
		)
		(bookShelf init:)
		(eliade init: loop: 1)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState intro)
		(= state intro)
		(switch state
			(0 (= cycles 1))
			(1
				(= cycles 1)
				(hero init: cel: 1)
				(ProgramControl)
			)
			; (roomDoor:setCycle(End)ignoreActors())
			; = doorOpen 1
			(2
				(= cycles 25)
				(julyn setCycle: Walk setMotion: MoveTo 140 100)
			)
			(3
				(= cycles 9)
				(hero init: cel: 0)
			)
			(4
				(= cycles 10)
				(julyn loop: 3)
			)
			(5 (= cycles 8) (Print 13 0))
			; (roomDoor:setCycle(Beg)cycleSpeed(1))//closes door behind her
			; = doorOpen 0
; How are you feeling, Julyn?"#title"You ask
			(6
				(= cycles 8)
				(Print 13 1)
; A little better...thank you..."#title"Julyn says
				(Print 13 2)
			)
; Could I ask you a favor?"#title"Julyn says
			(7
				(= cycles 8)
				(Print 13 3)
; I think I could use some time alone. It's been such a long day."#title"Julyn says
				(Print 13 4)
			)
; Would you mind?"#title"Julyn says
			(8 (= cycles 8) (Print 13 5))
; Are you sure? You know it's my job to keep watch over you."#title"You say
			(9
				(= cycles 25)
				(Print 13 6)
; I know. I'll be fine."#title"Julyn says
				(Print 13 7)
			)
; It's just a big day tomorrow and I need some personal time."#title"Julyn says
			(10
				(= cycles 8)
				(hero loop: 5 setCycle: End)
			)
			(11 (= cycles 8) (Print 13 8))
; As you wish."#title"You say
			(12
				(= cycles 30)
				(hero setCycle: Beg)
			)
			; (send gEgo:setCycle(Walk)setMotion(MoveTo 70 95))//walks to the candle
			(13
				(= cycles 5)
				(gRoom newRoom: 50)
			)
			(14
				(= cycles 8)
				(gEgo hide: ignoreActors:)
				(ProgramControl)
			)
			(15
				(= cycles 15)
				(julyn setCycle: Walk setMotion: MoveTo 75 90)
			)                                                ; walks to the candle
			(16
				(= cycles 13)
				(if (not checkpoint)
					(julyn view: 2 loop: 3 cel: 0 setCycle: End) ; blows out candle
					(table loop: 5 cel: 0 setCycle: End cycleSpeed: 1)
					(= checkpoint 1)
				)
			)                      ; checkpoint to evade problems with changestate
			(17
				(= cycles 14)
				(julyn view: 308 setCycle: Walk setMotion: MoveTo 98 120)
			)                                                          ; walk to bed
			(18
				(= cycles 20)
				(julyn
					setCycle: Walk
					setMotion: MoveTo 68 120
					ignoreControl: ctlWHITE
				)
			)
			(19
				(= cycles 20)
				(julyn
					posn: 60 120
					view: 2
					loop: 0
					cel: 0
					setCycle: End
					cycleSpeed: 2
				)
			)
			(20
				(= cycles 5)
				(julyn view: 2 loop: 1 cel: 0 setCycle: End cycleSpeed: 2)
			)                                                          ; sitting on bed
			(21
				(= cycles 25)
				(julyn view: 2 loop: 2 cel: 0 setCycle: Fwd cycleSpeed: 3)
			)                                                          ; yawning
			(22
				(= cycles 20)
				(julyn posn: 58 120 view: 2 loop: 4 cel: 0 setCycle: End)
			)                                                         ; laying down
			(23
				(= cycles 30)     ; CHANGING SCENE TO OUTSIDE THE ROOM//
				(Hide)
				(DrawPic 1 dpOPEN_RIGHT dpCLEAR 0)
				(gGame setSpeed: 7)
				(eliadeShadow
					init:
					ignoreActors:
					setCycle: Walk
					setMotion: MoveTo 150 179
				)
			)
			(24
				(= cycles 23)
				(eliadeShadow
					init:
					ignoreActors:
					setCycle: Walk
					setMotion: MoveTo 80 175
				)
			)
			(25
				(= cycles 35)     ; BACK INSIDE THE ROOM
				(eliadeShadow hide:)
				(DrawPic 13 dpOPEN_LEFT dpCLEAR 0)
				(gGame setSpeed: 4)
				(Showviews)  ; show all the views in the room
				(zzz
					init:
					loop: 2
					setCycle: Fwd
					cycleSpeed: 2
					ignoreActors:
					setPri: 1
				)
			)                                                                       ; sleeping
			(26
				(= cycles 30)
				(eliade setCycle: Walk setMotion: MoveTo 290 120)
			)                                                       ; eliade comes to the window
			(27
				(= cycles 25)
				(eliade view: 8 loop: 0 cel: 0 setCycle: End)
			)
			(28
				(= cycles 15)
				(if (not doorBroken)
					; (send gTheMusic:prevSignal(0)stop()number(101)loop(-1)play())
					(eliade loop: 1 cel: 0 setCycle: End) ; breaking glass
					(glassDoor setCycle: End cycleSpeed: 1)
					(= doorBroken 1)
					(zzz hide:)
					(julyn setCycle: Beg)
				)
			)                            ; /waking up
			(29
				(= cycles 10)
				(eliade loop: 2 cel: 0 setCycle: End)
				(glassDoor setPri: 1)
				(julyn view: 2 loop: 0 cel: 5 setCycle: Beg cycleSpeed: 2)
			)                                                          ; getting out of bed
			(30
				(= cycles 5)
				(eliade
					view: 7
					loop: 1
					cel: 1
					setCycle: Walk
					setMotion: MoveTo 200 120
				)
				(julyn
					setCycle: Walk
					cycleSpeed: 0
					view: 308
					loop: 0
					cel: 0
					setMotion: MoveTo 120 120
				)
			)
			(31
				(= cycles 4)
				(eliade setMotion: Follow julyn)
				(julyn observeControl: ctlWHITE setMotion: MoveTo 100 100)
			)
			(32
				(= cycles 3)
				(gRoom newRoom: 1)
			)
		)
	)
)

(procedure (Showviews)
	(julyn init: show:)
	(glassDoor init: show:)
	(bed init: show:)
	(roomDoor init: show:)
	(sheet init: show:)
	(table init: show:)
	(bookShelf init: show:)
	(eliade init: show:)
)

(procedure (Hide)
	(julyn hide:)
	(glassDoor hide:)
	(bed hide:)
	(roomDoor hide:)
	(sheet hide:)
	(table hide:)
	(bookShelf hide:)
	(eliade hide:)
	(hero hide:)
)

(instance glassDoor of Prop
	(properties
		y 143
		x 264
		view 800
	)
)

(instance roomDoor of Prop
	(properties
		y 74
		x 106
		view 802
	)
)

(instance bed of Prop
	(properties
		y 155
		x 40
		view 107
	)
)

(instance sheet of Prop
	(properties
		y 125
		x 50
		view 107
	)
)

(instance table of Prop
	(properties
		y 91
		x 73
		view 101
	)
)

(instance bookShelf of Prop
	(properties
		y 85
		x 170
		view 102
	)
)

(instance eliade of Act
	(properties
		y 120
		x 308
		view 7
	)
)

(instance zzz of Prop
	(properties
		y 87
		x 48
		view 107
	)
)

(instance eliadeShadow of Act
	(properties
		y 174
		x 240
		view 5
	)
)

(instance hero of Act
	(properties
		y 84
		x 135
		view 307
	)
)

(instance julyn of Act
	(properties
		y 130
		x 250
		view 308
	)
)
