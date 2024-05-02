;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; rm001.sc
; Contains the first room of your game. 
(script# 1)
(include sci.sh)
(include game.sh)
(use main)
(use controls)
(use cycle)
(use game)
(use feature)
(use obj)
(use inv)
(use jump)
(use dpath)

(public
	rm001 0
)




(instance rm001 of Rm
	(properties
		picture scriptNumber
		; Set up the rooms to go to/come from here
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		; same in every script, starts things up
		(super init:)
		(self setScript: RoomScript)
		; Check which room ego came from and position it
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
		)
		; Set up the ego
		(SetUpEgo)
		(gGame setSpeed: 4)
		(gEgo init: hide:)
		(princess init:)
		(faerie1 init: loop: 1 setCycle: Fwd cycleSpeed: 2)
		(faerie2 init: loop: 2 setCycle: Fwd cycleSpeed: 2)
	)
)

;
;         * Set up the room's music to play here *
; (send gTheMusic:prevSignal(0)stop()number(100)loop(-1)play())
;
;         * Add the rest of your initialization stuff here *

(instance RoomScript of Script
	(properties)
	
	(method (changeState intro)
		(= state intro)
		(switch state
			(0 (= cycles 35))
			(1
				(= cycles 15)
				(Print 1 0)
; Oh dear, Princess Julyn, you look so depressed..."#title"Faerie #1
				(Print 1 1)
			)
; Yeah, like a goblin who got back his IQ test. hehe."#title"Faerie #2
			(2 (= cycles 20) (Print 1 2))
; It's just...tomorrow's my wedding day."#title"Julyn says
			(3
				(= cycles 15)
				(Print 1 3)
; But my darling, that's not bad news."#title"Faerie #1
				(Print 1 4)
			)
; Right! Why that's as happy as a wizard with a new spell! hehe."#title"Faerie #2
			(4 (= cycles 1))
			; Print(#title"Faerie #2")
			(5
				(= cycles 15)
				(Print 1 5)
; Yes I know. But this wedding is not out of love...but for an alliance with another Kingdom."#title"Julyn says
				(Print 1 6)
			)
; It was my father's idea...without my input at all."#title"Julyn says
			(6
				(= cycles 25)
				(Print 1 7)
; Well my lovely, you should sleep. Things won't be quite so bad as you think."#title"Faerie #1
				(Print 1 8)
			)
; It's true! At least he's rich! hehe. "#title"Faerie #2
			(7 (= cycles 40) (Print 1 9))
; Thanks for being good friends. I think I will get some sleep."#title"Julyn says
			(8 (gRoom newRoom: 38))
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
