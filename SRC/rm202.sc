;;; Sierra Script 1.0 - (do not remove this comment)

(script# 202)
(include sci.sh)
(include game.sh)
(use controls)
(use game)
(use main)
(use obj)

(public
	timeline 0
)

; GLOBAL CLOCK FOR TIME OF DAY
; (QUEST FOR GLORY-LIKE TIME DAY TIMER)
; BY RYAN SLATTERY
; Special thanks to Troflip for his tutorial on Regions

(instance timeline of Rgn
	(properties)
	
	(method (init)
		(super init:)
		(self setScript: timeScript)
	)
)

(instance timeScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= seconds 1))
			(1
				(= seconds 1)
				(if (< gTimeOfDay 1000)
					(++ gTimeOfDay)
				else
					; ++ gDayNumber
					(= gTimeOfDay 1)
				)
			)
			(2 (timeScript changeState: 0))
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (Said 'jump')
			(cond 
				((< gTimeOfDay 333) (Print 202 0))
				((< gTimeOfDay 667) (Print 202 1))
; Morning
				((< gTimeOfDay 1000) (Print 202 2))
; Noon
			)
		)
	)
)
