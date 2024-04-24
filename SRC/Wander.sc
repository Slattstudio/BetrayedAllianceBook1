;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; wander.sc
; Contains a movement class to allow actors to randomly wander around the screen.
(script# WANDER_SCRIPT)
(include sci.sh)
(include game.sh)
(use cycle)





(class Wander of Motion
	(properties
		client 0
		caller 0
		x 0
		y 0
		dx 0
		dy 0
		b-moveCnt 0
		b-i1 0
		b-i2 0
		b-di 0
		b-xAxis 0
		b-incr 0
		completed 0
		xLast 0
		yLast 0
		distance 30
	)
	
	(method (init theClient theDistance)
		(if (>= argc 1)
			(= client theClient)
			(if (>= argc 2) (= distance theDistance))
		)
		(self setTarget:)
		(super init: client)
	)
	
	(method (doit)
		(super doit:)
		(if (client isStopped:) (self moveDone:))
	)
	
	(method (moveDone)
		(self init:)
	)
	
	(method (setTarget &tmp dist2)
		(= dist2 (* distance 2))
		(= x (+ (client x?) (- distance (Random 0 dist2))))
		(= y (+ (client y?) (- distance (Random 0 dist2))))
	)
	
	(method (onTarget)
		(return FALSE)
	)
)
