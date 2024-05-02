;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; timer.sc
; Contains the timer class for setting up your events.
(script# TIMER_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use obj)





(class Timer of Obj
	(properties
		cycleCnt -1
		seconds -1
		lastTime -1
		client 0
	)
	
	; Check if it's a class or instance
	; If it's an instance, use it, otherwise, create an instance ; one sec ; one min


	(procedure (CueClient &tmp hClient)
		(= hClient client)
		(= client 0)
		(if
		(and (IsObject hClient) (hClient respondsTo: #timer))
			(hClient timer: 0)
		)
		(if (hClient respondsTo: #cue) (hClient cue:))
	)
	
	
	(method (new)
		(if (== self Timer)
			(return (super new:))
		else
			(return self)
		)
	)
	
	(method (init theClient &tmp hTimer)
		(= client theClient)
		(gTimers add: self)
		(if (theClient respondsTo: #timer)
			(= hTimer (theClient timer?))
			(if (IsObject hTimer) (hTimer dispose:))
			(theClient timer: self)
		)
	)
	
	(method (doit &tmp theTime)
		(if (!= cycleCnt -1)
			(if (not (-- cycleCnt)) (CueClient))
		else
			(= theTime (GetTime gtTIME_OF_DAY))
			(if (!= lastTime theTime)
				(= lastTime theTime)
				(if (not (-- seconds)) (CueClient))
			)
		)
	)
	
	(method (dispose)
		(if
		(and (IsObject client) (client respondsTo: #timer))
			(client timer: NULL)
		)
		(= client NULL)
	)
	
	(method (set theClient sec min hour &tmp hObj cycleCounter speed)
		(= speed gSpeed)
		(if (== speed 0) (= speed 1))
		(= cycleCounter (/ (* sec 60) speed))
		(if (> argc 2)
			(= cycleCounter
				(+ cycleCounter (/ (* min 3600) speed))
			)
		)
		(if (> argc 3)
			(= cycleCounter
				(+ cycleCounter (* (/ (* hour 3600) speed) 60))
			)
		)
		(if (& objectInfo $8000)
			(= hObj (self new:))
		else
			(= hObj self)
		)
		(hObj init: theClient cycleCnt: cycleCounter)
		(return hObj)
	)
	
	(method (setCycle theClient cycleCounter &tmp hObj)
		(if (& objectInfo $8000)
			(= hObj (self new:))
		else
			(= hObj self)
		)
		(hObj init: theClient cycleCnt: cycleCounter)
		(return hObj)
	)
	
	(method (setReal theClient milliSec sec min &tmp hTimer theSeconds)
		(= theSeconds milliSec)
		(if (> argc 2)
			(= theSeconds (+ theSeconds (* sec 60)))
		)
		(if (> argc 3)
			(= theSeconds (+ theSeconds (* min 3600)))
		)
		(if (& objectInfo $8000)
			(= hTimer (self new:))
		else
			(= hTimer self)
		)
		(hTimer init: theClient seconds: theSeconds)
		(return hTimer)
	)
	
	(method (delete)
		(if (== client NULL)
			(gTimers delete: self)
			(super dispose:)
		)
	)
)

(class TO of Obj
	(properties
		timeLeft 0
	)
	
	(method (doit)
		(if timeLeft (-- timeLeft))
	)
	
	(method (set newTime)
		(= timeLeft newTime)
	)
)
