;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; jump.sc
; Contains a motion class to allows actors to jump.
(script# JUMP_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use cycle)





(class Jump of Motion
	(properties
		client 0
		caller 0
		x 20000
		y 20000
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
		gx 0
		gy 3
		xStep 20000
		yStep 0
		signal 0
		illegalBits 0
		waitApogeeX 1
		waitApogeeY 1
	)
	
	(method (init theClient theCaller &tmp theHeading)
		(= client theClient)
		(if (== argc 2) (= caller theCaller))
		(= illegalBits (client illegalBits?))
		(= signal (client signal?))
		(client illegalBits: 0 setPri:)
		(if (== xStep 20000)
			(= theHeading (client heading?))
			(cond 
				(
					(or
						(> theHeading 330)
						(> theHeading 30)
						(and (< 150 theHeading) (< theHeading 210))
					)
					(= xStep 0)
				)
				((< theHeading 180) (= xStep (client xStep?)))
				(else (= xStep (- (client xStep?))))
			)
		)
		(if (or (not waitApogeeX) (>= (* xStep gx) 0))
			(= waitApogeeX 0)
		)
		(if (or (not waitApogeeY) (>= (* yStep gy) 0))
			(= waitApogeeY 0)
		)
		(self setTest:)
	)
	
	(method (doit &tmp theXStep theYStep)
		(= xLast (client x?))
		(= yLast (client y?))
		(client x: (+ xLast xStep) x: (+ yLast yStep))
		(= theXStep xStep)
		(= theYStep yStep)
		(= xStep (+ xStep gx))
		(= yStep (+ yStep gy))
		(if
			(and
				(not waitApogeeX)
				(!= x 20000)
				(<= 0 (* dx (- (client x?) x)))
			)
			(client x: x)
			(return (self moveDone:))
		)
		(if
			(and
				(not waitApogeeY)
				(!= y 20000)
				(<= 0 (* dy (- (client y?) y)))
			)
			(client y: y)
			(return (self moveDone:))
		)
		(if (<= (* theXStep xStep) 0)
			(= waitApogeeX 0)
			(self setTest:)
		)
		(if (<= (* theYStep yStep) 0)
			(= waitApogeeY 0)
			(self setTest:)
		)
	)
	
	(method (moveDone)
		(client illegalBits: illegalBits signal: signal)
		(if caller (= gCastMotionCue TRUE) (= completed TRUE))
	)
	
	(method (motionCue)
		(client mover: NULL)
		(if (and completed (IsObject caller)) (caller cue:))
		(self dispose:)
	)
	
	(method (setTest)
		(if
		(or (> (client x?) x) (== (client x?) x) (> xStep 0))
			(= dx -1)
		else
			(= dx 1)
		)
		(cond 
			((> (client y?) y) (= dx -1))
			((or (== (client y?) y) (> xStep 0)) (= dx 1))
		)
		(if
		(or (> (client y?) y) (== (client y?) y) (> yStep 0))
			(= dy -1)
		else
			(= dy 1)
		)
	)
)


(class JumpTo of Jump
	(properties
		client 0
		caller 0
		x 20000
		y 20000
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
		gx 0
		gy 3
		xStep 20000
		yStep 0
		signal 0
		illegalBits 0
		waitApogeeX 1
		waitApogeeY 1
	)
	
	(method (init theClient theX theY theCaller &tmp dx dy)
		(= client theClient)
		(= x theX)
		(= y theY)
		(if
		(and (== x (theClient x?)) (== y (theClient y?)))
			(= illegalBits (client illegalBits?))
			(= signal (client signal?))
			(self moveDone:)
		)
		(= dx (- x (theClient x?)))
		(= dy (- y (theClient y?)))
		(SetJump self dx dy gy)
		(if (not dx) (= x 20000))
		(if (not dy) (= y 20000))
		(switch argc
			(3 (super init: theClient))
			(4
				(super init: theClient theCaller)
			)
		)
	)
	
	(method (moveDone)
		(if (!= x 20000) (client x: x))
		(if (!= y 20000) (client y: y))
		(super moveDone:)
	)
)
