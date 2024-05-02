;;; Sierra Script 1.0 - (do not remove this comment)

(script# 16)
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
	rm016 0
)

; Entrance to Cave to Hermit's Hideaway (alternate)




(instance rm016 of Rm
	(properties
		picture 26
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
				(gEgo posn: 29 43 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0
				(= cycles 1)
				(gEgo setMotion: MoveTo 29 45)
			)
			(1)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) 14)
						(< (pEvent x?) 32)
						(> (pEvent y?) 1)
						(< (pEvent y?) 42)
					)
					(Print {There is an opening overhead.} #at 80 30)
				)
			)
		)
	)
	
	; (if(Said())
	; )
	(method (doit)
		(super doit:)
		(if (== (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 30)
		)
	)
)
