;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; rev.sc
; Contains a cycle class for reverse animation. 
(script# REV_SCRIPT)
(include sci.sh)
(include game.sh)
(use cycle)





(class Rev of Cycle
	(properties
		client 0
		caller 0
		cycleDir cdBACKWARD
		cycleCnt 0
		completed 0
	)
	
	(method (doit &tmp nCel)
		(= nCel (self nextCel:))
		(if (< nCel 0)
			(self cycleDone:)
		else
			(client cel: nCel)
		)
	)
	
	(method (cycleDone)
		(client cel: (client lastCel:))
	)
)
