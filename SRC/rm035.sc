;;; Sierra Script 1.0 - (do not remove this comment)

(script# 35)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use door)
(use feature)
(use game)
(use inv)
(use main)
(use obj)

(public
	rm035 0
)

(local
; N Castle Storeroom



	lEgoX
	lEgoY
	maleControl =  1
)

(instance rm035 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 204)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 30 155 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(switcher init: setScript: walkScript)
		(julyn init:)
		(guard init: setScript: guardScript)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_RETURN)
				(= lEgoX (gEgo x?))
				(= lEgoY (gEgo y?))
				(if maleControl
					(= gAnotherEgo 1)
					(= maleControl 0)
					(gEgo posn: (julyn x?) (julyn y?) view: 308 loop: 1)
					(julyn posn: lEgoX lEgoY view: 903 loop: 0)
					(switcher loop: 1)
				else
					(= gAnotherEgo 0)
					(= maleControl 1)
					(gEgo posn: (julyn x?) (julyn y?) view: 0 loop: 0)
					(julyn posn: lEgoX lEgoY view: 308 loop: 1)
					(switcher loop: 0)
				)
			)
		)
; (if(==(send pEvent:type())evMOUSEBUTTON)
;            (if(& (send pEvent:modifiers) emRIGHT_BUTTON)
;                (if((> (send pEvent:x) (cyclops:nsLeft))and
;                    (< (send pEvent:x) (cyclops:nsRight))and
;                    (> (send pEvent:y) (cyclops:nsTop))and
;                    (< (send pEvent:y) (cyclops:nsBottom)))
;                    Print(35 0) /* This diabolic figure looks quite large and even more dangerous.
; )
;            )
;        )
		(if (Said))
	)
)

(instance walkScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(2 (= cycles 1))
			(3)
		)
	)
)

; (send gEgo:setMotion(MoveTo (-(send gEgo:x))(send gEgo:y)))
(instance guardScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1))
			(1
				(guard
					setCycle: Walk
					cycleSpeed: 1
					xStep: 2
					setMotion: MoveTo 100 140 guardScript
				)
			)
			(2
				(guard setMotion: MoveTo 240 140 guardScript)
			)
			(3 (guardScript changeState: 1))
		)
	)
)

(instance julyn of Act
	(properties
		y 140
		x 270
		view 308
		loop 1
	)
)

(instance guard of Act
	(properties
		y 140
		x 200
		view 310
		loop 1
	)
)

(instance switcher of Prop
	(properties
		y 40
		x 150
		view 573
	)
)
