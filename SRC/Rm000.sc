/******************************************************************************/
(include "sci.sh")
(include "game.sh")
/******************************************************************************/
(script 0)
/******************************************************************************/
(use "controls")
(use "cycle")
(use "feature")
(use "game")
(use "inv")
(use "main")
(use "obj")
(use "sciAudio")
/******************************************************************************/
(instance public rm000 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	(method (init)
		(super:init())
		(self:setScript(RoomScript))

		(switch(gPreviousRoomNumber)
			(default
				(send gEgo:
					posn(150 130)
					loop(1)
				)
			)
		)

		SetUpEgo()
		(send gEgo:init())
	)
)
/******************************************************************************/
(instance RoomScript of Script
	(properties)
)
/******************************************************************************/
