;;; Sierra Script 1.0 - (do not remove this comment)

(script# 52)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)

(public
	rm052 0
)

; NULL ROOM




(instance rm052 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(glow init: hide: ignoreActors:)
		(shadowleft init: hide: ignoreActors:)
		(shadowright init: hide: ignoreActors:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (handleEvent pEvent &tmp str) ; Local variable
		(super handleEvent: pEvent)
		(switch (pEvent type?)
			(evKEYBOARD
				(MapKeyToDir pEvent)
				(if (== (pEvent message?) KEY_D) ; Check if B was pressed
					(PrintOK) ; switch to Brian
					(pEvent claimed: TRUE)
				) ; if B
				(if (== (pEvent message?) KEY_E) ; Check if E was pressed
					(PrintOK) ; switch to Ego
					(pEvent claimed: TRUE)
				)
			)
		)
	)
	       ; if ; evKEYBOARD
	; For fun intercept the Mouse event as well
	; evMOUSEBUTTON ; switch ; method
	(method (doit)
		(super doit:)
		(glow show: posn: (gEgo x?) (+ (gEgo y?) 55))
		(shadowleft
			show:
			posn: (- (gEgo x?) 137) (+ (gEgo y?) 55)
		)
		(shadowright
			show:
			posn: (+ (gEgo x?) 137) (+ (gEgo y?) 55)
		)
	)
)

(instance glow of Prop
	(properties
		y 35
		x 195
		view 62
	)
)

(instance shadowleft of Prop
	(properties
		y 35
		x 195
		view 62
		loop 1
	)
)

(instance shadowright of Prop
	(properties
		y 35
		x 195
		view 62
		loop 1
	)
)
