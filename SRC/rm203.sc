;;; Sierra Script 1.0 - (do not remove this comment)

(script# 203)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use main)
(use obj)

(public
	oxygenTimer 0
)

; Region for Caves and Storage Room
; BY RYAN SLATTERY

(instance oxygenTimer of Rgn
	(properties)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
	)
	
	(method (doit)
		(super doit:)
	)
)
