;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; dcicon.sc
; Contains an extension of the DIcon class from controls.sc which allows 
; animation.
(script# DCICON_SCRIPT)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)





(class DCIcon of DIcon
	(properties
		type ctlICON
		state 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		key 0
		said 0
		value 0
		view 0
		loop 0
		cel 0
		cycler 0
		cycleSpeed 6
		signal 0
	)
	
	(method (init)
		(= cycler (Fwd new:))
		(cycler init: self)
	)
	
	(method (dispose)
		(if cycler (cycler dispose:))
		(super dispose:)
	)
	
	(method (cycle &tmp theCel)
		(if cycler
			(= theCel cel)
			(cycler doit:)
			(if (!= cel theCel) (self draw:))
		)
	)
	
	(method (lastCel)
		(return (- (NumCels self) 1))
	)
)
