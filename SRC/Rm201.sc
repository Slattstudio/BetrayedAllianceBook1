;;; Sierra Script 1.0 - (do not remove this comment)

(script# 201)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use main)
(use obj)

(public
	cursorMenu 0
)

(local
; Cursor Changer thingie for fun
; BY RYAN SLATTERY

	myEvent
)
(instance cursorMenu of Rgn
	(properties)
	
	(method (doit)
		(super doit:)
		(= myEvent (Event new: evNULL))
		(if (> (myEvent y?) 12)
			(SetCursor 995 (HaveMouse))
			(= gCurrentCursor 995)
		else
			(SetCursor 999 (HaveMouse))
			(= gCurrentCursor 999)
		)
		(myEvent dispose:)
	)
)
