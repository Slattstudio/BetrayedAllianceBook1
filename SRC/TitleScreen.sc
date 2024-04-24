;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; titlescreen.sc
; Contains the title screen room.
(script# TITLESCREEN_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use game)
(use menubar)
(use obj)
(use cycle)
(use user)
(use controls)
(use feature)

(public
	TitleScreen 0
)

(local


	; (use "sciaudio")
	htext
	htext2
	snd
)

(instance TitleScreen of Rm
	(properties
		picture 900
	)
	
	(method (init)
		; Set up the title screen
		(ProgramControl)
		(= gProgramControl FALSE)
		(gGame setSpeed: 3)
		(SL disable:)
		(TheMenuBar hide:)
		(super init:)
		(self setScript: RoomScript)
		(gEgo init: hide:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1))
; (case 1 = cycles 1
;                = gWndColor 14 //clYELLOW
;                = gWndBack 1 //clDARKBLUE
;                Print("The game you are now playing was produced through the efforts of many people: designers, artists, programmers, musicians, and lots of other hard-working folks.\n(actually, for the most part, just me)\n\nIf you make copies of this software for any reason other than to make a personal backup, you are not only breaking the law, but raising the cost of software for all legitimate users.\n(Actually, just send it to whoever you want!)\n\nPLEASE DO MAKE COPIES OF THIS GAME!" #mode alCENTER #width 280)
;                = gWndColor 0 //clBlack
;                = gWndBack 15 //clWhite
;            )
			(1
				(gRoom newRoom: INITROOMS_SCRIPT)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_RETURN)
				(gRoom newRoom: INITROOMS_SCRIPT)
			)
		)
	)
)
