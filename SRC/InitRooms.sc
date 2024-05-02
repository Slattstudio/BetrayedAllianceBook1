;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; initrooms.sc
; Contains a room which initializes the rooms and calls the first room.
(script# INITROOMS_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use controls)
(use game)
(use obj)
(use menubar)
(use cycle)

(public
	InitRooms 0
)




(instance InitRooms of Rm
	(properties
		script 0
		number 0
		timer 0
		keep 0
		initialized 0
		picture 0
		style 6
		horizon 0
		controls 0
		north 0
		east 0
		south 0
		west 0
		curPic 0
		picAngle 0
		vanishingX 160
		vanishingY 35536
	)
	
	(method (init)
		(ProgramControl)
		(= gProgramControl FALSE)
		(SL disable:)
		(TheMenuBar hide:)
		(super init:)
		(gEgo
			view: 0
			posn: -1 -1
			setStep: 1 1
			setMotion: MoveTo 3000 100
			setCycle: Walk
			init:
		)
	)
	
	(method (doit)
		(super doit:)
		(gGame setSpeed: 3)
		(= gTimeSeconds 0)
		(= gTimeMinutes 0)
		(= gTimeHours 0)
		(= gEgoView 0)
		(TheMenuBar draw:)
		(SL enable:)
		; (send gRoom:newRoom(9))
		(gRoom newRoom: 803)
	)
)
