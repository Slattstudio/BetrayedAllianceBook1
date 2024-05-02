;;; Sierra Script 1.0 - (do not remove this comment)

(script# 8)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use sciAudio)
(use menubar)

(public
	rm008 0
)

(local


	snd
)

(instance rm008 of Rm
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
				(gEgo posn: 150 130 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: hide:)
		(SetCursor 998 (HaveMouse))
		(= gCurrentCursor 998)
		
		(TheMenuBar state: DISABLED hide:)
		
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		(SetCursor 998 (HaveMouse))
		(= gCurrentCursor 998)
	)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 1))
			(1 (= cycles 4))
			(2
				(= cycles 7)
				(Print {You make your way through the cave with Julyn.})
				(Print
					{She tells you that Lt. Gyre kidnapped her and framed you.}
				)
				(Print
					{He desired to put the peace treaty between Shelah and Ishvi on hold thinking he could lead Shelah to military victory.}
				)
			)
			(3
				(= cycles 7)
				(Print
					{Up to this point you hadn't thought of what you would do when you found Julyn.}
				)
				(Print
					{As the entrance of the cave grows closer you wonder where you could find refuge.}
				)
			)
			(4 (gRoom newRoom: 125))
		)
	)
)

(instance aud of sciAudio
	(properties)
	
	(method (init)
		(super init:)
	)
)
