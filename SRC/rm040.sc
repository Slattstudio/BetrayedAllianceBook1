;;; Sierra Script 1.0 - (do not remove this comment)

(script# 40)
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
	rm040 0
)

; Outside Graveyard




(instance rm040 of Rm
	(properties
		picture scriptNumber
		north 0
		east 28
		south 29
		west 26
	)
	
	(method (init)
		(super init:)
		(self
			setScript: RoomScript
			setRegions: 200
			setRegions: 204
		)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 165 loop: 1)
			)
			(26 (gEgo posn: 30 165 loop: 0))
			(42
				(gEgo posn: 150 165 loop: 2)
				;(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
			)
			(29
				(gEgo posn: 150 178 loop: 3)
			)
			(28
				(gEgo posn: 300 165 loop: 1)
			)
			(20 (gEgo posn: 17 120 loop: 2))
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		(if (not g102Solved) (lid init:)) ; lid closed
		(if g41Coffin (slab init:))
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) 153)    ; Mausoleum
						(< (pEvent x?) 184)
						(> (pEvent y?) 64)
						(< (pEvent y?) 102)
					)
					(Print
						{The Carmyle mausoleum. Legends have been passed that at night voices were sometimes heard from under the earth.}
						#at
						-1
						28
					)
				)
				(if
					(and
						(> (pEvent x?) 68)    ; Graveyard
						(< (pEvent x?) 289)
						(> (pEvent y?) 103)
						(< (pEvent y?) 133)
					)
					(Print
						{A creepy place for sure, not helped by the sordid past of the Carmyle family.}
						#at
						-1
						28
					)
				)
				(cond 
					(
						(and
							(> (pEvent x?) 0) ; Dead Tree
							(< (pEvent x?) 13)
							(> (pEvent y?) 46)
							(< (pEvent y?) 168)
						)
						(Print
							{Other than sending chills down your spine, there is nothing special about this tree.}
							#at
							-1
							28
						)
					)
					(
						(and
							(> (pEvent x?) 0)    ; Landscape
							(< (pEvent x?) 0)
							(> (pEvent y?) 319)
							(< (pEvent y?) 63)
						)
						(Print
							{The beautiful expanse behind the graveyard helps lift the dreariness from your heart.}
							#at
							-1
							28
						)
					)
				)
			)
		)
		(if (Said 'look>')
			(if (Said '/tree')
				(Print
					{It doesn't exactly inspire the cheeriest feelings.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '/graveyard,grave,headstone')
				(Print
					{It's a grey, still, and unnerving place. There are many gravestones of prominent members of the Carmyle family.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '/mausoleum')
				(Print
					{The Carmyle mausoleum. Legends have been passed that at night voices were sometimes heard from under the earth.}
					#width
					280
					#at
					-1
					28
				)
			)
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(Print
					{Shivers run through your spine even during the daytime. The Carmyle family was once wealthy and wanted their own graveyard.}
					#width
					280
					#at
					-1
					8
				)
				(Print
					{The townspeople thought it was a great idea also, so long as it wasn't in the village.}
					#width
					280
					#at
					-1
					8
				)
			)
		)
	)
	
	(method (doit)
		(super doit:)
; (if(>(send gEgo:y)170)
;            (if(<=(send gEgo:distanceTo(ghost))35)
;                (if(g40Sword)
;                    (if(not(ghostVisible))
;                        (RoomScript:changeState(2))
;                        = ghostVisible 1
;                    )
;                )
;            )(else
;                (if(ghostVisible)
;                    (RoomScript:changeState(4))
;                    = ghostVisible 0
;                )
;            )
;        )
		(if (== (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 42)
		)
		(if (& (gEgo onControl:) ctlGREY) (gRoom newRoom: 20))
	)
)

; (instance GhostScript of Script
;    (properties)
;    (method (changeState newState)
;        = state newState
;        (switch (state)
;            (case 1
;                ProgramControl()
;                (SetCursor(997 HaveMouse()) = gCurrentCursor 997)
;                (send gEgo:hide())
;                (egoAlt:init()show()view(410)loop(1)cel(0)posn(27 180)setCycle(End GhostScript)cycleSpeed(2)ignoreActors())
;            )(case 2
;                (egoAlt:view(409)loop(1)cel(0)setCycle(End GhostScript)cycleSpeed(2)posn(48 178))
;            )(case 3
;                (egoAlt:hide())
;                (send gEgo:show()posn(48 178))
;                Print(40 3) /* Those who take the sword from me...
; Print(40 4) /* Death befriends them by the sea. */
; = g40Sword 1
;                PlayerControl()
;                (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
;            )
;        )
;    )
; )
; (instance ghost of Prop
;  (properties
;    y 180
;    x 17
;    view 40 loop 2
;  )
; )
(instance egoAlt of Prop
	(properties
		y 180
		x 27
		view 410
		loop 1
	)
)

(instance lid of Prop
	(properties
		y 56
		x 157
		view 101
		loop 3
	)
)

(instance slab of Prop
	(properties
		y 54
		x 152
		view 101
		loop 6
	)
)
