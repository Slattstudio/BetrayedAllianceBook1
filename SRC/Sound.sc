;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; sound.sc
; Contains the sound class, used for all sound/music related needs.
(script# SOUND_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use obj)

(class Sound of Obj
	(properties
		state 0
		number 0
		priority 0
		loop 1
		handle 0
		signal 0
		prevSignal 0
		client 0
		owner 0
	)
	
	(method (new theOwner &tmp hObj)
		(= hObj (super new:))
		(if argc
			(hObj owner: theOwner yourself:)
		else
			(hObj owner: NULL yourself:)
		)
	)
	
	(method (init)
		(= signal 0)
		(= prevSignal 0)
		(= state 0)
		(gSounds add: self)
		(DoSound sndINIT self)
	)
	
	(method (dispose fKEEP_CLIENT)
		(if (and argc (not fKEEP_CLIENT)) (= client NULL))
		(gSounds delete: self)
		(if handle (DoSound sndDISPOSE handle) (= handle NULL))
		(super dispose:)
	)
	
	(method (play theClient)
		(self stop:)
		(if (not loop) (= loop 1))
		(self init:)
		(if argc (= client theClient) else (= client NULL))
		(DoSound sndPLAY self)
	)
	
	(method (playMaybe)
		(self play: &rest)
		(if (== state 2) (self dispose:))
	)
	
	(method (stop fKEEP_CLIENT)
		(if (and argc (not fKEEP_CLIENT)) (= client NULL))
		(if handle (DoSound sndSTOP handle))
	)
	
	(method (check)
		(if signal
			(if (IsObject client) (client cue: self))
			(= prevSignal signal)
			(= signal 0)
		)
	)
	
	(method (pause pSound)
		(DoSound sndPAUSE pSound)
	)
	
	(method (changeState)
		(DoSound sndUPDATE self)
	)
	
	(method (clean anOwner)
		(if (or (not owner) (== owner anOwner))
			(self dispose:)
		)
	)
	
	(method (fade pSound)
		(if (and argc (not pSound)) (= client 0))
		(DoSound sndFADE handle)
	)
)
