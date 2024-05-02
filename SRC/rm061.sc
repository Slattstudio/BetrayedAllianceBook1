;;; Sierra Script 1.0 - (do not remove this comment)

(script# 61)
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
	rm061 0
)

(local
; Eastern Bridge (North)



	falling =  0
	moving =  0
)

(instance rm061 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 64
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
				(gEgo posn: 150 130 loop: 1)
			)
			(49
				(gEgo posn: 106 102 loop: 2)
			)
			(64 (gEgo posn: 15 134 loop: 0))
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		; (waterFallTop:init()setCycle(Fwd)cycleSpeed(2))
		; (waterFallTop2:init()setCycle(Fwd)cycleSpeed(2))
		(egoFall
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
		)
		(waterFallTop3
			init:
			hide:
			setCycle: Fwd
			cycleSpeed: 2
			setScript: stepAsideScript
		)
		(soldier
			init:
			setScript: soldierScript
			ignoreControl: ctlWHITE
			ignoreActors:
			xStep: 2
			setPri: 9
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (checkEvent pEvent 89 124 27 71) (PrintOther 29 16)) ; dock
				(cond 
					(
						(and
							(> (pEvent x?) (soldier nsLeft?))
							(< (pEvent x?) (soldier nsRight?))
							(> (pEvent y?) (soldier nsTop?))
							(< (pEvent y?) (soldier nsBottom?))
						)
						(PrintOther 29 0)
					)
					((checkEvent pEvent 148 230 163 189) (PrintOther 29 4)) ; bridge
				)
			)
		)
		(if (Said 'look>')
			(if (Said '/man,soldier,guard') (PrintOther 29 0))
			(if (Said '/bridge') (PrintOther 29 12))
			(if (Said '/building,dock') (PrintOther 29 16))
			(if (Said '[/!*]') (PrintOther 29 13))
			; this will handle just "look" by itself
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
		(if (Said 'climb')
			(Print {There isn't a good place to do that here.})
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
		(if (or (Said 'fight') (Said 'use,draw,(pull<out)/sword') )
			(PrintOther 29 15)
		)
	)
	
	(method (changeState mainState &tmp dyingScript)
		(= state mainState)
		(switch state
			(0)
			(3
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				
				;(egoFall hide:)
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 603
					register:
						{Quite a graceless fall! You'll have to practice if you expect to wow any olympic judges. Until then though, maybe just work on not dying.}
				)
				(gGame setScript: dyingScript)
			)
			(4
				(ProgramControl)
				(gEgo hide:)
				(egoFall
					show:
					view: 23
					posn: (gEgo x?) (gEgo y?)
					yStep: 7
					setMotion: MoveTo (gEgo x?) 180 self
				)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)
			; (send gTheMusic:prevSignal(0)stop()number(903)play())
			(5 (= cycles 30)
				(egoFall hide:)
			)
			(6 	(= cycles 20)
				(ShakeScreen 1)
				(gTheSoundFX number: 202 play:)
			)
			(7
				(RoomScript changeState: 3)
			)
		; (send gTheMusic:prevSignal(0)stop())
		)
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 49)
		)
		(if (== (gEgo onControl:) ctlSILVER)
			(if (not falling)
				(RoomScript changeState: 4)
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
					setMotion: MoveTo 144 150 soldierScript
					setCycle: Walk
					cycleSpeed: 1
				)
			)
			(2
				(soldier setMotion: MoveTo 220 150 soldierScript)
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
				(gEgo setMotion: MoveTo (gEgo x?) 120 stepAsideScript)
			)
			(2 (= cycles 5) (gEgo loop: 2))
			(3
				(= cycles 4)
				(PrintSoldier 29 1)
			)                      ; #title "Soldier:") /* You are not allowed past this bridge." #title "Soldier: */
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
	(= gWndColor 3)
	(= gWndBack 1)
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

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 10)
)

(instance soldier of Act
	(properties
		y 144
		x 150
		view 333
	)
)

(instance waterFallTop of Prop
	(properties
		y 41
		x 300
		view 22
		loop 1
	)
)

(instance waterFallTop2 of Prop
	(properties
		y 41
		x 320
		view 22
		loop 1
	)
)

(instance waterFallTop3 of Prop
	(properties
		y 105
		x 330
		view 22
		loop 10
	)
)

(instance egoFall of Act
	(properties
		y 130
		x 40
		view 33
	)
)
