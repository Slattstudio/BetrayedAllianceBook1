;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; disposeload.sc
; Contains a procedure for disposing/loading resources.
(script# DISPOSELOAD_SCRIPT)
(include sci.sh)
(include game.sh)

(public
	DisposeLoad 0
)



(procedure (DisposeLoad rsType rsNumbers &tmp i num)
	(= argc (- argc 2))
	(for ( (= i 0)) (<= i argc)  ( (++ i)) (= num [rsNumbers i]) (if rsType (Load rsType num) else (DisposeScript num)))
)
