;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; cycle.sc
; Contains classes for views, props, acts and their descendants which handles 
; animation cycling.
(script# CYCLE_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use obj)





(class Cycle of Obj
	(properties
		client 0
		caller 0
		cycleDir cdFORWARD
		cycleCnt 0
		completed 0
	)
	
	(method (init newClient)
		(if argc (= client newClient))
		(= cycleCnt 0)
		(= completed FALSE)
	)
	
	(method (nextCel)
		(++ cycleCnt)
		(if (<= cycleCnt (client cycleSpeed?))
			(client cel?)
		else
			(= cycleCnt 0)
			(if (& (client signal?) $1000)
				(client cel?)
			else
				(+ (client cel?) cycleDir)
			)
		)
	)
	
	(method (cycleDone)
	)
	
	(method (motionCue)
		(client cycler: NULL)
		(if (and completed (IsObject caller)) (caller cue:))
		(self dispose:)
	)
)


(class Fwd of Cycle
	(properties
		client 0
		caller 0
		cycleDir cdFORWARD
		cycleCnt 0
		completed 0
	)
	
	(method (doit &tmp theCel)
		(= theCel (self nextCel:))
		(if (> theCel (client lastCel:))
			(self cycleDone:)
		else
			(client cel: theCel)
		)
	)
	
	(method (cycleDone)
		(client cel: 0)
	)
)


(class Walk of Fwd
	(properties
		client 0
		caller 0
		cycleDir cdFORWARD
		cycleCnt 0
		completed 0
	)
	
	(method (doit)
		(if (not (client isStopped:)) (super doit:))
	)
)


(class CT of Cycle
	(properties
		client 0
		caller 0
		cycleDir cdFORWARD
		cycleCnt 0
		completed 0
		endCel 0
	)
	
	(method (init theClient theEndCel theCycleDir theCaller &tmp theLastCel)
		(super init: theClient)
		(= cycleDir theCycleDir)
		(if (== argc 4) (= caller theCaller))
		(= theLastCel (client lastCel:))
		(if (> theEndCel theLastCel)
			(= endCel theLastCel)
		else
			(= endCel theEndCel)
		)
	)
	
	(method (doit &tmp theNextCel theLastCel)
		(= theLastCel (client lastCel:))
		(if (> endCel theLastCel) (= endCel theLastCel))
		(= theNextCel (self nextCel:))
		(cond 
			((> theNextCel theLastCel) (client cel: 0))
			((< theNextCel 0) (client cel: theLastCel))
			(else (client cel: theNextCel))
		)
		(if
		(and (== cycleCnt 0) (== endCel (client cel?)))
			(self cycleDone:)
		)
	)
	
	(method (cycleDone)
		(= completed TRUE)
		(if caller
			(= gCastMotionCue TRUE)
		else
			(self motionCue:)
		)
	)
)


(class End of CT
	(properties
		client 0
		caller 0
		cycleDir cdFORWARD
		cycleCnt 0
		completed 0
		endCel 0
	)
	
	(method (init theClient theCaller)
		(if (== argc 2)
			(super
				init: theClient (theClient lastCel:) cdFORWARD theCaller
			)
		else
			(super
				init: theClient (theClient lastCel:) cdFORWARD NULL
			)
		)
	)
)


(class Beg of CT
	(properties
		client 0
		caller 0
		cycleDir cdFORWARD
		cycleCnt 0
		completed 0
		endCel 0
	)
	
	(method (init theClient theCaller)
		(if (== argc 2)
			(super init: theClient 0 cdBACKWARD theCaller)
		else
			(super init: theClient 0 cdBACKWARD NULL)
		)
	)
)


(class Motion of Obj
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
	)
	
	(method (init theClient theX theY theCaller &tmp theCycler theLooper)
		(if (>= argc 1)
			(= client theClient)
			(if (>= argc 2)
				(= x theX)
				(if (>= argc 3)
					(= y theY)
					(if (>= argc 4) (= caller theCaller))
				)
			)
		)
		(= completed FALSE)
		(= b-moveCnt 0)
		(= xLast 0)
		(= yLast 0)
		(= theCycler (client cycler?))
		(if theCycler (theCycler cycleCnt: 0))
		(client
			heading: (GetAngle (client x?) (client y?) x y)
		)
		(if (client looper?)
			(= theLooper (client looper?))
			(theLooper doit: client (client heading?))
		else
			(DirLoop client (client heading?))
		)
		(InitBresen self)
	)
	
	(method (doit &tmp [s 40] t)
		(if (== b-moveCnt (client moveSpeed?))
			(= xLast (client x?))
			(= yLast (client y?))
		)
		(DoBresen self)
		(Animate)
		(if (and (== x (client x?)) (== y (client y?)))
			(self moveDone:)
		)
	)
	
	(method (moveDone)
		(= completed TRUE)
		(if caller
			(= gCastMotionCue TRUE)
		else
			(self motionCue:)
		)
	)
	
	(method (setTarget newX newY)
		(if argc (= x newX) (= y newY))
	)
	
	(method (onTarget)
		(if (and (== (client x?) x) (== (client y?) y))
			(return TRUE)
		)
		(return FALSE)
	)
	
	(method (motionCue)
		(client mover: NULL)
		(if (and completed (IsObject caller)) (caller cue:))
		(self dispose:)
	)
)


(class MoveTo of Motion
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
	)
	
	(method (init)
		(super init: &rest)
	)
	
	(method (onTarget)
		(if
			(and
				(<= (Abs (- (client x?) x)) (client xStep?))
				(<= (Abs (- (client y?) y)) (client yStep?))
			)
			(return TRUE)
		)
	)
)
