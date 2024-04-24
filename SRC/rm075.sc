;;; Sierra Script 1.0 - (do not remove this comment)

(script# 75)
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
	rm075 0
)



; (use "sciaudio")
; snd

(instance rm075 of Rm
	(properties
		picture 900
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
				(gEgo posn: 150 130 loop: 1 hide:)
			)
		)
		(SetUpEgo)
		(gEgo init: hide:)
		
		;(gTheMusic fade:)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 10))
			(1
				(= cycles 15)
				(Print
					{You crawl your way through an increasingly tight tunnel. And is it just you or are the whispers getting closer?}
				)
			)
			(2
				(= cycles 20)
				(Print
					{You frantically claw with your fingers into the muddy earth mixed and rocks.}
				)
				(Print
					{You hear your pack of darts fall to the ground, but at this point who cares?}
				)
				(= gApple 0)
			)
			(3 (gRoom newRoom: 53))
		)
	)
)
