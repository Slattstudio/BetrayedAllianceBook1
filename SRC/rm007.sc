;;; Sierra Script 1.0 - (do not remove this comment)

(script# 7)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use sciaudio)

(public
	rm007 0
)

(local
; Empty Room for Music


	snd
)


(instance rm007 of Rm
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
			(24
				(gEgo hide:)
				(= snd aud)
				(snd
					command: {playx}
					fileName: {music\\leah.mp3}
					volume: {70}
					loopCount: {-1}
					init:
				)
				(RoomScript changeState: 1)
			)
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: hide:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			(1 (= cycles 1))
			(2 (gGame newRoom: 23))
		)
	)
)

(instance aud of sciAudio
	(properties)
	
	(method (init)
		(super init:)
	)
)
