;;; Sierra Script 1.0 - (do not remove this comment)

(script# 29)
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
	rm029 0
)

(local
; Western Bridge



	falling =  0
	moving =  0
)

(instance rm029 of Rm
	(properties
		picture scriptNumber
		north 0
		east 43
		south 0
		west 27
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
				(gEgo posn: 150 80 loop: 1)
			)
			(27 (gEgo posn: 10 80 loop: 0))
			; (send gTheMusic:prevSignal(0)stop())
			(40 (gEgo posn: 140 60 loop: 2))
			(43 (gEgo posn: 290 80 loop: 1))
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		(deathSplash init: hide: ignoreActors: setPri: 6)
		(soldier
			init:
			setScript: soldierScript
			ignoreControl: ctlWHITE
			ignoreActors:
			xStep: 2
		)
		(egoFall
			init:
			hide:
			setScript: stepAsideScript
			ignoreControl: ctlWHITE
			ignoreActors:
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState &tmp dyingScript)
		(= state mainState)
		(switch state
			(0)
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(egoFall
					show:
					view: 23
					posn: (gEgo x?) (gEgo y?)
					yStep: 8
					setMotion: MoveTo (gEgo x?) 180 RoomScript
					setPri: 6
				)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)
			; (send gTheMusic:prevSignal(0)stop()number(903)play())
			(2
				(egoFall hide:)
				(deathSplash
					show:
					posn: (egoFall x?) 200
					setCycle: End self
					cycleSpeed: 3
				)
				(gTheSoundFX number: 202 play:)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(3
				; (send gTheMusic:prevSignal(0)stop())
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 603
					register:
						{Going for a swim in heavy armor? Didn't you have a sinking feeling about doing that?}
				)
				(gGame setScript: dyingScript)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(cond 
					(
						(and
							(> (pEvent x?) (soldier nsLeft?))
							(< (pEvent x?) (soldier nsRight?))
							(> (pEvent y?) (soldier nsTop?))
							(< (pEvent y?) (soldier nsBottom?))
						)
						(Print 29 0 #width 290 #at -1 28)
					)
					(
						(and
							(> (pEvent x?) 1)    ; Bridge
							(< (pEvent x?) 90)
							(> (pEvent y?) 111)
							(< (pEvent y?) 189)
						)
						(PrintOther 29 4)
					)
				)
			)
		)
		(if (Said 'wear,use,(put<on)/goggles')
			(if (gEgo has: INV_GOGGLES)
				(if gDisguised
					(Print 0 53)
				else
					(PrintOther 0 80)
					(= gDisguised 1)
					(= gEgoRunning 0)
					(RunningCheck)
					(gEgo setMotion: NULL)
				)
			else
				(PrintDHI)
			)
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
		(if (Said 'talk/man,soldier,guard')
			(if (<= (gEgo distanceTo: soldier) 60)
				(= gWndColor 3)
				(= gWndBack 1)
				(if gDisguised
					(PrintSoldier 29 10)
				else
					(PrintSoldier 29 5)
				)
				(= gWndColor 0)
				(= gWndBack 15)
			else
				(PrintNCE)
			)
		)
		(if (Said '(ask<about)>')
			(if (<= (gEgo distanceTo: soldier) 60)
				(if (Said '/bridge') (PrintSoldier 29 5))
				(if (Said '/princess') (PrintSoldier 29 6))
				(if (Said '/prince') (PrintSoldier 29 7))
				(if (Said '/war') (PrintSoldier 29 8))
				(if (Said '/king') (PrintSoldier 29 9))
				(if (Said '/carmyle') (PrintSoldier 29 2))
				(if (Said '/wizard') (PrintSoldier 29 3))
				(if (Said '/*') (PrintSoldier 29 11))
			else
				(PrintNCE)
			)
		)
		(if (Said 'look>')
			(if (Said '/soldier,man,guard') (PrintOther 29 0))
			(if (Said '/bridge') (PrintOther 29 12))
			(if (Said '[/!*]') (PrintOther 29 13))
			; this will handle just "look" by itself
		)
		(if (Said 'fight') (PrintOther 29 15))
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 40)
		)
		(if (== (gEgo onControl:) ctlMAROON)
			(if (not falling)
				(RoomScript changeState: 1)
				(= falling 1)
			)
		)
		(if (& (gEgo onControl:) ctlGREY)
			(if (not moving)
				(stepAsideScript changeState: 1)
				(soldierScript changeState: 4)
				(= moving 1)
			)
		)
	)
)

(instance soldierScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1))
			(1
				(soldier
					setMotion: MoveTo 10 105 soldierScript
					setCycle: Walk
					cycleSpeed: 1
				)
			)
			(2
				(soldier setMotion: MoveTo 52 105 soldierScript)
			)
			(3
				(soldierScript changeState: 1)
			)
			(4
				(soldier setMotion: NULL loop: 3)
			)
		)
	)
)

(instance stepAsideScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo (gEgo x?) (- (gEgo y?) 15) stepAsideScript
				)
			)
			(2 (= cycles 5) (gEgo loop: 2))
			(3
				(= cycles 4)
				(= gWndColor 3)
				(= gWndBack 1)
				(if gDisguised
					(PrintSoldier 29 10)
				else
					(PrintSoldier 29 1)
				)                      ; #title "Soldier:" #at -1 130) /* You are not allowed past this bridge." #title "Soldier: */
				(= gWndColor 0)
				(= gWndBack 15)
			)
			(4
				(soldierScript changeState: 1)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= moving 0)
			)
		)
	)
)

(procedure (PrintSoldier textRes textResIndex)
	(= gWndColor 3)
	(= gWndBack 1)
	(Print
		textRes
		textResIndex
		#width
		280
		#at
		-1
		130
		#title
		{Soldier:}
	)
	(= gWndColor 0)
	(= gWndBack 15)
)

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 130)
)

(instance deathSplash of Prop
	(properties
		y 187
		x 147
		view 87
		loop 1
	)
)

(instance egoFall of Act
	(properties
		y 130
		x 40
		view 33
	)
)

(instance soldier of Act
	(properties
		y 105
		x 52
		view 323
	)
)
