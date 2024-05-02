;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; Follow.sc
; A motion class which allows actors to follow other actors.
(script# FOLLOW_SCRIPT)
(include sci.sh)
(include game.sh)
(use cycle)





(class Follow of Motion
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
		who 0
		distance 20
	)
	
	(method (init theClient theWho theDistance)
		(if (>= argc 1)
			(= client theClient)
			(if (>= argc 2)
				(= who theWho)
				(if (>= argc 3) (= distance theDistance))
			)
		)
		(if (> (client distanceTo: who) distance)
			(super init: client (who x?) (who y?))
		)
	)
	
	(method (doit &tmp angle)
		(if (> (client distanceTo: who) distance)
			(super doit:)
			(if (== b-moveCnt 0)
				(super init: client (who x?) (who y?))
			)
		else
			(= xLast (client x?))
			(= yLast (client y?))
			(= angle (GetAngle xLast yLast (who x?) (who y?)))
			(if (client looper?)
				(client doit: client angle)
				(client looper?)
			else
				(DirLoop client angle)
			)
		)
	)
	
	(method (moveDone)
	)
	
	(method (setTarget)
		(cond 
			(argc (super setTarget: &rest))
			((not (self onTarget:)) (super setTarget: (who x?) (who y?)))
		)
	)
	
	(method (onTarget)
		(return (<= (client distanceTo: who) distance))
	)
)
