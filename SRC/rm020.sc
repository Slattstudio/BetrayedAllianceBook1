;;; Sierra Script 1.0 - (do not remove this comment)
;
; *                            South of N. Castle                                *
(script# 20)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use follow)

(public
	rm020 0
)

(local


	prepareForBattle =  0
	deadMan =  0
	message =  0
	countdown =  -40
	assault =  0
	targetted =  0
	
	side = 0	; 1 for left, 2 for right
	
)                 ; has ego been seen and is now being trailed

(instance rm020 of Rm
	(properties
		picture scriptNumber
		north 0
		east 21
		south 0
		west 25
	)
	
	(method (init)
		(super init:)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		(soldier1 init: setScript: soldierScript ignoreActors:)
		(soldier2 init: setScript: soldierDown)
		(self setScript: RoomScript setRegions: 204)
		(switch gPreviousRoomNumber
			(103
				(if (not gRanAway)
					(if (> (gEgo x?) 160)   ; Ego on Left
						(gEgo posn: 260 150 loop: 2)
						;(= gPreviousRoomNumber 21)
						(= side 2)
					else
						(gEgo posn: 60 150 loop: 2)
						;(= gPreviousRoomNumber 25)
						(= side 1)
					)
					(= deadMan 1)
					(deadSoldier
						init:
						posn: (gEgo x?) (+ (gEgo y?) 3)
						ignoreActors:
					)
					(soldier1 init: hide:)
					(soldierProp1 init: z: countdown ignoreActors: setPri: 6)
					(soldierProp2 init: z: countdown ignoreActors: setPri: 6)
					(soldierProp3 init: z: countdown ignoreActors: setPri: 6)
					(= assault 1)
					(soldierDown changeState: 1)
				else
					; Print(20 7) /* Run away! */
					(= gEgoRunning 1)
					(RunningCheck)
					(if (> (gEgo x?) 160)   ; Ego on Right
						(gEgo
							posn: 242 153
							loop: 1
							setMotion: MoveTo 319 153
						)
						(if (> gEnNum 1)
							(soldier3 init: posn: 150 130)
							(soldier4 init: posn: 155 140)
							(soldier5 init: posn: 145 150)
							(soldier2 init: hide:)
							(soldierDown changeState: 6)
						)
						(= side 2)
					else
						(gEgo posn: 79 153 loop: 0 setMotion: MoveTo 1 153)
						(if (> gEnNum 1)
							(soldier3 init: posn: 150 130)
							(soldier4 init: posn: 155 140)
							(soldier5 init: posn: 145 150)
							(soldier2 init: hide:)
							(soldierDown changeState: 6)
						)
						(= side 1)
					)
					(= gRanAway 0)
				)
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
			)
			(21
				(gEgo posn: 282 150 loop: 1)
				(RoomScript changeState: 1)
				(= side 2)
			)
			(25
				(gEgo posn: 35 150 loop: 0)
				(RoomScript changeState: 1)
				(= side 1)
			)
			(else 
				(gEgo posn: 35 150 loop: 0)
			)
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			(1 (= cycles 4))
			(2
				(if (not g20Entered)
					(PrintOther 20 30)
					(PrintOther 20 31)
					(= g20Entered 1)
				)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (< (gEgo distanceTo: soldier1) 30)
			(if (not deadMan)
				(if (not prepareForBattle)
					(soldierScript changeState: 3)
				)
			)
		)
		(if deadMan
			(if (< (gEgo distanceTo: soldier3) 30)
				(if (not prepareForBattle)
					(soldierScript changeState: 3)
				)
			)
			(if (< (gEgo distanceTo: soldier4) 30)
				(if (not prepareForBattle)
					(soldierScript changeState: 3)
				)
			)
			(if (< (gEgo distanceTo: soldier5) 30)
				(if (not prepareForBattle)
					(soldierScript changeState: 3)
				)
			)
		)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 40)
		)
		(if (& (gEgo onControl:) ctlBROWN)
			(if (not deadMan)
				(if (not targetted) (soldierScript changeState: 1))
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) (soldier1 nsLeft?))
						(< (pEvent x?) (soldier1 nsRight?))
						(> (pEvent y?) (soldier1 nsTop?))
						(< (pEvent y?) (soldier1 nsBottom?))
					)
					(PrintOther 20 32)
					(return)
				)
				(if
					(and
						(> (pEvent x?) (soldier2 nsLeft?))
						(< (pEvent x?) (soldier2 nsRight?))
						(> (pEvent y?) (soldier2 nsTop?))
						(< (pEvent y?) (soldier2 nsBottom?))
					)
					(PrintOther 20 33)
					(return)
				)
				(if
					(and
						(> (pEvent x?) (deadSoldier nsLeft?))
						(< (pEvent x?) (deadSoldier nsRight?))
						(> (pEvent y?) (deadSoldier nsTop?))
						(< (pEvent y?) (deadSoldier nsBottom?))
					)
					(PrintOther 20 34)
				)
				(if
					(==
						ctlNAVY
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                  ; castle
					(PrintOther 20 35)
				)
			)
		)

		(if (Said 'talk,ask/man,soldier,captain')
			(if gDisguised
				(PrintSoldier 20 41)
			else
				(PrintSoldier 20 40)
			)
		)
		(if (Said '(ask<about)/*')
			(PrintSoldier 20 40)		
		)
		(if
			(or
				(Said 'give/ring[/man,guard]')
				(Said 'give/man,guard/ring')
				(Said 'propose[/marriage,man,guard]')
			)
			(if (gEgo has: 18)
				(PrintOther 20 28)	
			else
				(PrintDHI)
			)
		)
		(if (Said 'wear,use,(put<on)/goggles')
			(if (gEgo has: INV_GOGGLES)
				(cond 
					(gDisguised (PrintSoldier 0 53))
					(prepareForBattle (PrintOther 20 1))
					(else
						(PrintOther 0 80)
						(= gDisguised 1)
						(= gEgoRunning 0)
						(RunningCheck)
						(gEgo setMotion: NULL)
					)
				)
			else
				(PrintDHI)
			)
		)
		(if (Said 'look>')
			(if (Said '/soldier,man') (PrintOther 20 33))
			(if (Said '/castle') (PrintOther 20 35))
			(if (Said '/mountain') (PrintOther 20 37))
			(if (Said '[/!*]') (PrintOther 20 30))
		; this will handle just "look" by itself
		)
		(if (Said 'search,examine/body,body')
			(if deadMan (PrintOther 20 38) else (PrintOther 20 39))
		)
	)
)

(instance soldierScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles 1)
				(= targetted 1)
			)
			(2
				(if gDisguised (PrintSoldier 20 2))
				(soldier1 setCycle: Walk setMotion: Follow gEgo)
			)
			(3
				(= cycles 5)
				(gEgo setMotion: NULL)
				(= prepareForBattle 1)
				(PrintOther 20 0)
				(= gBatNum 100)
				(if assault (= gEnNum 3))
				(= gOpHealth gBatNum)
			)
			(4 
				(switch side
					(1
						(gEgo posn: 60 150 hide:)			
					)
					(2
						(gEgo posn: 260 150 hide:)	
					)
				)
				(gRoom newRoom: 103)
			)
		)
	)
)

(instance soldierDown of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1 (= cycles 2))
			; (send gEgo:hide())
			(2
				(= cycles 2)
				(ProgramControl)
				(elevation)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)
			(3
				(= cycles 1)
				(if (not message) (PrintOther 20 8) (= message 1))
; You've bested your opponent, although you've never felt worse about it. You hate to have to injure a member of your own army.
				(elevation)
				(if (not (>= countdown -9))
					(soldierDown changeState: 2)
				)
			)
			(4
				(= cycles 20)
				
				(soldierProp1 hide:)
				(soldierProp2 hide:)
				(soldierProp3 hide:)
				(soldier3
					init:
					posn: (soldierProp1 x?) (soldierProp1 y?)
					setCycle: Walk
					setMotion: MoveTo 135 130
					ignoreActors:
				)
				(soldier4
					init:
					posn: (soldierProp2 x?) (soldierProp2 y?)
					setCycle: Walk
					setMotion: MoveTo 154 135
					ignoreActors:
				)
				(soldier5
					init:
					posn: (soldierProp3 x?) (soldierProp3 y?)
					setCycle: Walk
					setMotion: MoveTo 172 130
					ignoreActors:
				)
			)
			(5
				(PrintSoldier 20 9)
; There is the Traitor men! Capture him!" #title "Captain:
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				
				(soldierProp1 hide:)
				(soldierProp2 hide:)
				(soldierProp3 hide:)
				(soldier3
					setCycle: Walk
					setMotion: Follow gEgo
					ignoreActors:
				)
				(soldier4
					setCycle: Walk
					setMotion: Follow gEgo
					ignoreActors:
				)
				(soldier5
					setCycle: Walk
					setMotion: Follow gEgo
					ignoreActors:
				)
				(= assault 1)
			)
			(6
				(soldier3
					setCycle: Walk
					setMotion: Follow gEgo
					ignoreActors:
				)
				(soldier4
					setCycle: Walk
					setMotion: Follow gEgo
					ignoreActors:
				)
				(soldier5
					setCycle: Walk
					setMotion: Follow gEgo
					ignoreActors:
				)
				(= assault 1)
				(= deadMan 1)
			)
		)
	)
)

; (case 1 = cycles 5
;                (soldier3:init()setPri(1)setMotion(MoveTo 135 120)setCycle(Walk))
;                (soldier4:init()setPri(1)setMotion(MoveTo 152 120)setCycle(Walk))
;                (soldier5:init()setPri(1)setMotion(MoveTo 172 120)setCycle(Walk))
;            )(case 2 = cycles 1
;
;            )(case 3 = cycles 1
;            )
(procedure (elevation)
	(++ countdown)
	(soldierProp1 setCycle: Fwd z: countdown)
	(soldierProp2 setCycle: Fwd z: countdown)
	(soldierProp3 setCycle: Fwd z: countdown)
)

; (if (== countdown -10)
;        (RoomScript:changeState(4))
;    )
(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 100)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
	)
)

(procedure (checkEvent pEvent x1 x2 y1 y2)
	(if
		(and
			(> (pEvent x?) x1)
			(< (pEvent x?) x2)
			(> (pEvent y?) y1)
			(< (pEvent y?) y2)
		)
		(return TRUE)
	else
		(return FALSE)
	)
)

(procedure (PrintSoldier textRes textResIndex)
	(= gWndColor 15)
	(= gWndBack 8)
	(Print
		textRes
		textResIndex
		#width
		280
		#at
		-1
		20
		#title
		{Soldier:}
	)
	(= gWndColor 0)
	(= gWndBack 15)
)

(instance soldier1 of Act
	(properties
		y 130
		x 180
		view 322
		loop 2
	)
)

(instance soldier2 of Act
	(properties
		y 130
		x 130
		view 306
		loop 2
	)
)

(instance soldierProp1 of Prop
	(properties
		y 120
		x 137
		view 324
		loop 2
	)
)

(instance soldierProp2 of Prop
	(properties
		y 120
		x 154
		view 322
		loop 2
	)
)

(instance soldierProp3 of Prop
	(properties
		y 120
		x 170
		view 324
		loop 2
	)
)

(instance soldier3 of Act
	(properties
		y 115
		x 137
		view 324
		loop 2
	)
)

(instance soldier4 of Act
	(properties
		y 116
		x 154
		view 322
		loop 2
	)
)

(instance soldier5 of Act
	(properties
		y 115
		x 170
		view 324
		loop 2
	)
)

(instance deadSoldier of Prop
	(properties
		y 130
		x 130
		view 322
		loop 4
	)
)
