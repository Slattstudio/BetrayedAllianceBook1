;;; Sierra Script 1.0 - (do not remove this comment)
; + 2 Score //
(script# 73)
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
	rm073 0
)

(local

; Bridge in the Cavern of Madness

	myEvent
	target =  0
	shot =  0
	falling =  0
	washout =  0
	num =  0
	killedByBugs =  0
	bugsDodge =  0
	directionX =  0
	directionY =  0
	
	aiming = 0	; true when player types "shoot dart" to target areas.
)

(instance rm073 of Rm
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
				(gEgo posn: 230 170 loop: 3)
			)
			(58      ; Cavern of Madness (river)
				(gEgo posn: 230 170 loop: 3)
			)
			(59 (gEgo posn: 130 91 loop: 2) ; Cavern of Madness (ghost)
				(gTheMusic number: 58 loop: -1 play:)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(= gEgoRunning 0)
		(RunningCheck)
		(waterButton init: setScript: dartScript)
		(water
			init:
			hide:
			setPri: 2
			setScript: floodScript
			ignoreActors:
		)
		(waterSplash
			init:
			hide:
			setPri: 2
			ignoreActors:
			setScript: fallScript
		)
		(dart
			init:
			hide:
			ignoreActors:
			xStep: 15
			yStep: 15
			setPri: 4
			ignoreControl: ctlWHITE
			setScript: fleshEatingScript
		)
		(alterEgo
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
		)
		(invisibleTarget init: hide: ignoreActors: ignoreControl: ctlWHITE)
		
		(if (not g73Wash)
			(bugs
				init:
				ignoreControl: ctlWHITE
				ignoreActors:
				setCycle: Fwd
				setPri: 2
				setScript: bugCircleScript
			)
			(boneProp1
				init:
				ignoreControl: ctlWHITE
				ignoreActors:
				setPri: 2
			)
			(boneProp2
				init:
				ignoreControl: ctlWHITE
				ignoreActors:
				setPri: 2
			)
			(boneProp3
				init:
				ignoreControl: ctlWHITE
				ignoreActors:
				setPri: 2
			)
		else
			(bugs
				init:
				hide:
				ignoreControl: ctlWHITE
				posn: 1 1
				setScript: bugCircleScript
			)
			(boneProp1 init: hide:)
			(boneProp2 init: hide:)
			(boneProp3 init: hide:)
			(= washout 1)
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 1))
			(1
				(if (== gPreviousRoomNumber 58)
					(ProgramControl)
					(SetCursor 997 (HaveMouse))
					(= gCurrentCursor 997)
					(gEgo setMotion: MoveTo 218 155 self)
				)
			)
			(2
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(if (not g73Wash)
					(PrintOther 73 1)
					(if (gEgo has: INV_BLOW_DART_GUN) (PrintOther 73 2))
				)
			)
			(3	; move ego into shooting position
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 200 160 self ignoreControl: ctlWHITE)		
			)
			(4
				(PlayerControl)
				(SetCursor 994 (HaveMouse))
				(= gCurrentCursor 994)
				(gEgo hide:)
				(alterEgo show: view: 241 posn: (gEgo x?)(gEgo y?))			
			)
		)
	)
	
	(method (handleEvent pEvent &tmp dyingScript)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) (bugs nsLeft?))
						(< (pEvent x?) (bugs nsRight?))
						(> (pEvent y?) (bugs nsTop?))
						(< (pEvent y?) (bugs nsBottom?))
					)
					(if (not aiming)
						(PrintOther 73 3)
					)
				)
				(if
					(and
						(> (pEvent x?) (waterButton nsLeft?))
						(< (pEvent x?) (waterButton nsRight?))
						(> (pEvent y?) (waterButton nsTop?))
						(< (pEvent y?) (waterButton nsBottom?))
					)
					(if (not aiming) 
						(PrintOther 73 4)
					)
				)
			)
			(if aiming
				(if target
					(dartScript changeState: 1)
					(= aiming 0)	
				else
					(if (> (pEvent y?) 130)	; click too close or behind gEgo
						(PrintOther 73 21)
					else
						(dartScript changeState: 4)
						(= aiming 0)	
					)	
				)
				(if (not aiming)
					(alterEgo hide:)
					(gEgo show: loop: 3 observeControl: ctlWHITE)
				)	
			)
		)
		(if (== (pEvent type?) evKEYBOARD)
			(if aiming
				(if
					(or
						(== (pEvent message?) $0065)
						(== (pEvent message?) $0078)
						(== (pEvent message?) $0063)
						(== (pEvent message?) $0071)
					)                               ; lowercase c, e, x, or q
					(dartScript changeState: 6)
				)
			)	
		)
		(if (Said 'use,shoot,blow/dart,dartgun,gun,(dart<gun)')
			(if (gEgo has: INV_BLOW_DART_GUN)
				(if (> gApple 0)
					(if (> (gEgo y?) 114)
						; move into position
						(PrintOther 73 5)
						(RoomScript changeState: 3)
						(= gMap 1)
						(= gArcStl 1)
						(= aiming 1)
					else
						(PrintOther 73 22)
					)
				else
					(PrintOther 73 20)
				)
			else
				(PrintDHI)
			)
		)
		(if (Said 'look>')
			(if (Said '/bridge')
				(if washout (PrintOther 73 6) else (PrintOther 73 7))
			)
			(if (Said '/bone')
				(if washout (PrintOther 73 8) else (PrintOther 73 9))
			)
			(if (Said '/flies')
				(if washout (PrintOther 73 6) else (PrintOther 73 10))
			)
			(if (Said '/grate,tunnel,wall') (PrintOther 73 23))
			(if (Said '/switch') (PrintOther 73 11))
			(if (Said '[/!*]') (PrintOther 73 12))
			; this will handle just "look" by itself
		)
; (if(Said('open/door'))
;            (floodScript:changeState(1))
;        )
		(if (Said 'jump')
			(PrintOther 73 13)
			(PrintOther 73 14)
		)
		(if (Said 'run') (Print 0 88))
		(if (or (Said 'look,use,read,open/portal,map')
				(Said 'map'))
			(Print 0 88)
		)
		(if (or (Said 'listen[/!*]')
				(Said 'listen/voice'))
			(PrintWhisper)
			(if (== gListened 74)
				(PrintOther 58 75)	
			)
			(if (== gListened 75)
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 604
					register:
						{Where the voice came from, you cannot say. But you dwelt too deeply on despairing thoughts, driving you to lose your mind and your life.}
				)
				(gGame setScript: dyingScript)	
			)
		)
	)
	
	(method (doit)
		; (var dyingScript)
		(super doit:)
		
		(= myEvent (Event new: evNULL))
		(invisibleTarget posn: (myEvent x?)(myEvent y?))
		
		(if (not shot)
			(if (gEgo has: 8) ; INV_BLOW_DART_GUN
				(cond 
					(
						(and
							(> (myEvent x?) (waterButton nsLeft?))
							(< (myEvent x?) (waterButton nsRight?))
							(> (myEvent y?) (waterButton nsTop?))
							(< (myEvent y?) (waterButton nsBottom?))
						)
						;(SetCursor 996 (HaveMouse))
						;(= gCurrentCursor 996)
						(= target 1)
					)
					(
						(and
							(> (myEvent x?) (bugs nsLeft?))
							(< (myEvent x?) (bugs nsRight?))
							(> (myEvent y?) (bugs nsTop?))
							(< (myEvent y?) (bugs nsBottom?))
						)
						(if (not washout)
							;(SetCursor 996 (HaveMouse))
							;(= gCurrentCursor 996)
							(= target 2)
						)
					)
					(else
						(= target 0)
						;(SetCursor 999 (HaveMouse))
						;(= gCurrentCursor 999)
					)
				)
			)
		)
		(myEvent dispose:)
		
		(if (& (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 59)
		)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 58)
		)
		(if (& (gEgo onControl:) ctlGREY)
			(if (not falling)
				(if (> (gEgo y?) 120) 
					(alterEgo setPri: 4)
				else
					(if (> (gEgo x?) 166) 
						(alterEgo setPri: 1)
					else
						(alterEgo setPri: 2)
					)
				)
				(fallScript changeState: 1)
			)
		)
		(if (<= (gEgo distanceTo: bugs) 12)
			(if (not g73Wash)
				(if (not killedByBugs)
					(fleshEatingScript changeState: 1)
				)
			)
		)
; PlayerControl() (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
;                    = dyingScript ScriptID(DYING_SCRIPT)
;                    (send dyingScript:caller(605)
;                    register("As the bugs eat the flesh off your body you wonder 'does this bug me?' A few moments later you know that answer is 'no', but that's because your dead.")
;                    )
;                    (send gGame:setScript(dyingScript))
;                    = killedByBugs 1
		(if (<= (dart distanceTo: bugs) 12)
			(if (not bugsDodge)
				; PlayerControl() (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
				(bugCircleScript changeState: 1)
				(= bugsDodge 1)
			)
		)
		(if (> num 2) (= num 0) else (++ num))
	)
)

(instance bugCircleScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(bugs loop: 1 setCycle: End bugCircleScript)
			)
			(2
				(bugs loop: 0 setCycle: Fwd)
				(= bugsDodge 0)
			)
		)
	)
)

(instance dartScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				
				(-- gApple)
				(gTheSoundFX number: 204 play:)
				(cond 
					((== target 1) ; water
						(dart
							show:
							view: 57
							posn: (gEgo x?) (- (gEgo y?) 30)
							setMotion: MoveTo 207 79 self
						)
						(= shot 1)
					)
					((== target 2) ; bugs
						(dart
							show:
							view: 57
							posn: (gEgo x?) (- (gEgo y?) 30)
							setMotion: MoveTo 160 95 self
						)
						(= shot 1)
					)
				)
			)
			(2
				; (dart:hide())
				(if (== target 1)
					(floodScript changeState: 1)
					(dart hide:)
					(= shot 0)
				)
				(if (== target 2)
					(= directionX (- (bugs x?) (gEgo x?)))
					(= directionX (+ (bugs x?) directionX))
					(= directionY (- (gEgo y?) (bugs y?)))
					(dart setMotion: MoveTo directionX directionY self)
				)
			)
			(3
				(dart hide:)
				(= shot 0)
				(PrintOther 73 15)
				
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= gMap 0)
				(= gArcStl 0)
				
			)
			; missed
			(4
				(dart show: view: 57 posn: (gEgo x?) (- (gEgo y?) 30) setMotion: MoveTo (+ (invisibleTarget x?) 6) (invisibleTarget y?) self)
				(= shot 1)	
				(gTheSoundFX number: 204 play:)
			)
			(5
				(dart hide:)
				(dart posn: 1 1)
				(= shot 0)
				;(PrintOther 73 15)
				
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= gMap 0)
				(= gArcStl 0)
			
			)
			(6 (= cycles 2)
				; if player cancels shooting a dart	
			)
			(7
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= gMap 0)
				(= gArcStl 0)
				(alterEgo hide:)
				(gEgo show: loop: 3 observeControl: ctlWHITE)
				(= aiming 0)	
			)
		)
	)
)

(instance fleshEatingScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles 10)
				(= killedByBugs 1)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(bugs setPri: 15)
				(PrintOther 73 16)
			)
			(2       ; = cycles 10
				(gEgo hide:)
				(alterEgo
					show:
					posn: (gEgo x?) (gEgo y?)
					view: 906
					loop: 1
					cel: 0
					setCycle: End self
					cycleSpeed: 2
				)
				(bugs loop: 1 setCycle: Fwd)
			)
			(3
				(= cycles 10)
				(alterEgo loop: 0 cel: 0 setCycle: CT)
			)
			(4       ; = cycles 10
				(alterEgo setCycle: End self cycleSpeed: 3)
				(bugs loop: 0 setCycle: Fwd)
				(gTheSoundFX number: 203 play:)
			)
			(5
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 605
					register:
						{Those flesh-eating insects made short work of you. Next time keep your distance!}
				)
				(gGame setScript: dyingScript)
			)
		)
	)
)

(instance floodScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles 5)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(ShakeScreen 2)
				(gTheSoundFX number: 202 play:)
			)
			(2
				(water
					show:
					cel: 0
					setCycle: End floodScript
					cycleSpeed: 2
				)
			)
			(3
				(= washout 1)
				(bugs yStep: 3 setMotion: MoveTo 125 156)
				(boneProp1 xStep: 2 yStep: 5 setMotion: MoveTo 110 156)
				(boneProp2 xStep: 3 yStep: 3 setMotion: MoveTo 125 156)
				(boneProp3 xStep: 2 yStep: 4 setMotion: MoveTo 135 156)
				(water hide:)
				(waterSplash
					show:
					cel: 0
					setCycle: End floodScript
					cycleSpeed: 2
				)
			)
			(4
				(= cycles 6)
				(waterSplash hide:)
				(if (not g73Wash)
					(PrintOther 73 17)
				else
					(PlayerControl)
					(SetCursor 999 (HaveMouse))
					(= gCurrentCursor 999)
					
					(= gMap 0)
					(= gArcStl 0)
				)
			)
			(5
				(if (not g73Wash)
					(PrintOther 73 18)
					(gGame changeScore: 2)
					(= g73Wash 1)
					(PlayerControl)
					(SetCursor 999 (HaveMouse))
					(= gCurrentCursor 999)
					
					(= gMap 0)
					(= gArcStl 0)
				)
			)
		)
	)
)

(instance fallScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0)
			(1       ; = cycles 5
				(= falling 1)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(if (== (gEgo loop?) 0) ; if ego walking right
					(alterEgo view: 124 posn: (+ (gEgo x?) 8) (+ (gEgo y?) 5) show: yStep: 9 setMotion: MoveTo (+ (gEgo x?) 8) 185 self)	
				else
					(alterEgo view: 123 posn: (- (gEgo x?) 8) (+ (gEgo y?) 5) show: yStep: 9 setMotion: MoveTo (- (gEgo x?) 8) 185 self)		
				)
			)
			(2
				(= cycles 7)
				(alterEgo hide:)
			)
			(3	(= cycles 10)
				(ShakeScreen 2)
				(gTheSoundFX number: 200 play:)
			)
			(4
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 606
					register:
						{Don't you think you're going a little overboard? It's okay, you won't fall for it again.}
				)
				(gGame setScript: dyingScript)
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 100)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
	)
)
(procedure (PrintWhisper)
	(= gWndColor 9)
	(= gWndBack 0)
	(Print 58 gListened #width 280 #at -1 20 #title {A voice within your mind:})
	(= gWndColor 0) ; clBLACK
	(= gWndBack 15)
	(++ gListened)
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

(instance waterButton of Prop
	(properties
		y 79
		x 207
		view 331
		loop 5
	)
)

(instance water of Prop
	(properties
		y 138
		x 169
		view 80
	)
)

(instance waterSplash of Prop
	(properties
		y 175
		x 100
		view 80
	)
)

(instance dart of Act
	(properties
		y 135
		x 161
		view 57                       ; 157 for horizontal
	)
)

(instance bugs of Act
	(properties
		y 105
		x 168
		view 78
	)
)

(instance boneProp1 of Act
	(properties
		y 108
		x 162
		view 79
		loop 1
	)
)

(instance boneProp2 of Act
	(properties
		y 110
		x 169
		view 79
		loop 2
	)
)

(instance boneProp3 of Act
	(properties
		y 104
		x 168
		view 79
		loop 3
	)
)

(instance alterEgo of Act
	(properties
		y 154
		x 66
		view 23
	)
)
(instance invisibleTarget of Act
	(properties
		y 175
		x 100
		view 80
	)
)

