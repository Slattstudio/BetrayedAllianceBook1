;;; Sierra Script 1.0 - (do not remove this comment)
; + 1 SCORE
(script# 57)
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
	rm057 0
)

(local

; East Cliff - Dead Tree



	entering =  1
	flying =  0
	falling =  0
)

(instance rm057 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self
			setScript: RoomScript
			setRegions: 200
			setRegions: 204
		)
		(SetUpEgo)
		(gEgo init:)
		(alterEgo init: hide: setScript: hangGlideScript)
		(actionEgo
			init:
			hide:
			ignoreControl: ctlWHITE
			setScript: fallScript
		)
		(if (not (gEgo has: INV_ACORN))
			(if (== (IsOwnedBy INV_ACORN 63) FALSE)
				(acorn init: ignoreActors: setScript: acornScript)
			)
		)
		(wind
			init:
			hide:
			ignoreActors:
			setScript: windScript
			setPri: 15
		)
		(wind2
			init:
			hide:
			ignoreActors:
			setScript: windScript2
			setPri: 15
		)
		(windAcorn init: hide: ignoreActors: setPri: 15)
		(windLimbs init: hide: ignoreActors: setPri: 15)
		(limbs
			init:
			ignoreActors:
			setScript: limbScript
			setPri: 7
		)
		(= gEgoRunning 0)
		(RunningCheck)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 8 140 loop: 0)
				(RoomScript changeState: 1)
			)
			(22
				(gEgo posn: 8 140 loop: 0)
				(RoomScript changeState: 1)
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
			; walking into the room
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 48 104 self)
			)
			(2
				; ProgramControl() (SetCursor(997 HaveMouse()) = gCurrentCursor 997)
				(gEgo setMotion: MoveTo 77 104 self)
			)
			(3
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= entering 0)
			)
			; Print("You can really feel the wind blowing up here!" #width 280 #at -1 20)
			; exiting the room
			(4
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 43 105 self)
				(= entering 1)
			)
			(5
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 8 140 self)
			)
			(6
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gRoom newRoom: 22)
			)
			; Print("You can really feel the wind blowing up here!" #width 280 #at -1 20)
			; taking the acorn
			(7
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo (- (acorn x?) 10) (+ (acorn y?) 35) self)
			)
			(8
				(gEgo hide:)
				(alterEgo
					init:
					view: 335
					cel: 0
					loop: 0
					show:
					posn: (gEgo x?) (gEgo y?)
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(9 (= cycles 10))
			(10
				(Print 57 1 #icon 269 #title {Acorn})
				(alterEgo setCycle: Beg self)
				(acorn hide:)
				(gEgo get: INV_ACORN)
				(gGame changeScore: 1)
			)
			(11
				(alterEgo hide:)
				(gEgo show: x: (- (gEgo x?) 3) observeControl: ctlWHITE loop: 0)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
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
							(> (pEvent x?) (acorn nsLeft?))
							(< (pEvent x?) (+ (acorn nsRight?) 5))
							(> (pEvent y?) (- (acorn nsTop?) 2))
							(< (pEvent y?) (+ (acorn nsBottom?) 2))
						)
						(if (not (gEgo has: INV_ACORN)) (PrintOther 57 2))
					)
					(
						(==
							ctlBROWN
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                      ; Fence to Scientist's house
						(PrintOther 57 3)
					)
				)
			)
		)
		
		(if (Said 'feel[/wind]') (PrintOther 64 13))
		(if (Said 'look>')
			(if (Said '/acorn')
				(if (not (gEgo has: INV_ACORN))
					(if (== (IsOwnedBy INV_ACORN 63) FALSE)
						(PrintOther 57 2)
					else
						(PrintOther 57 9)	
					)
				else
					(Print 0 17 #title "acorn" #icon 269)
				)
			)
			(if (Said '/tree')
				(PrintOther 57 3)
				(if (not (gEgo has: INV_ACORN))
					(if (== (IsOwnedBy INV_ACORN 63) FALSE)
						(PrintOther 57 2)
					)
				)
			)
			(if (Said '/cliff') (PrintOther 57 5))
			(if (Said '/wind') (PrintOther 57 8))
			(if (Said '[/!*]') (PrintOther 57 6))
			; this will handle just "look" by itself
		)
		(if (Said 'climb') (PrintOther 57 7))
		(if (Said 'pick,take/acorn')
			(if (not (gEgo has: INV_ACORN))
				(if (== (IsOwnedBy INV_ACORN 63) FALSE)
					(if (& (gEgo onControl:) ctlBLUE)
						; (if(<=(send gEgo:distanceTo(acorn))75)
						(RoomScript changeState: 7)
					else
						(PrintNCE)
					)
				else
					(PrintATI)
				)
			else
				(PrintATI)
			)
		)
		
		(if
			(or
				(Said 'tie/kite/(bar,handle)')
				(Said 'tie/(bar,handle)/kite')
				(Said 'make,build/hangglider')
				(Said 'use,combine/(kite,bar)/(kite,bar)')
				(Said 'fly/kite/bar')
			)
			(cond 
				((gEgo has: 25) (PrintOK) (hangGlideScript changeState: 1))
				((gEgo has: INV_KITE)
					(if (gEgo has: INV_METAL_BAR)
						(PrintOther 57 11)
						(hangGlideScript changeState: 1)
					else
						(PrintOther 57 12)
					)
				)
				((gEgo has: INV_METAL_BAR) (PrintOther 57 13))
				(else (PrintOther 57 14))
			)
		)
		(if (Said 'use,fly/kite,bar')
			(cond 
				((gEgo has: 25) (PrintOK) (hangGlideScript changeState: 1))
				((gEgo has: INV_KITE)
					(cond 
						((gEgo has: INV_METAL_BAR)
							(if (not gHardMode)
								(PrintOther 57 11)
								(hangGlideScript changeState: 1)
							else
								(PrintOther 57 15)
							)
						)
						((not gHardMode) (PrintOther 57 15))
						(else (PrintOther 57 15))
					)
				)
				(else (PrintOther 57 17))
			)
		)
		(if (or (Said 'use,fly/glider') (Said 'fly'))
			(if (gEgo has: 25)
				(PrintOK)
				(hangGlideScript changeState: 1)
			else
				(PrintDHI)
			)
		)
	)
)

; (if(Said('fly'))
;            (if(send gEgo:has(INV_KITE)) // 4
;                (if(send gEgo:has(INV_METAL_BAR)) // 3
;                    Print("With the help of the twine in your STUPID kit, you secure your metal bar to the kite and get into position for take off.")
;                    (hangGlideScript:changeState(1))
;                )(else
;                    Print("You're kite is strong enough to carry you, but you need a handle of some kind to hold on to.")
;                )
;            )(else
;                Print("How do you intend to do that?")
;            )
;        )
(instance hangGlideScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(gEgo
					setMotion: MoveTo 175 95 hangGlideScript
					ignoreControl: ctlWHITE
				)
			)
			(2
				(= flying 1)
				(alterEgo
					show:
					view: 231
					posn: (gEgo x?) (gEgo y?)
					xStep: 7
					setCycle: Walk
					setMotion: MoveTo 60 95 hangGlideScript
					ignoreActors:
					ignoreControl: ctlWHITE
				)
				(gEgo hide:)
			)
			(3
				; (alterEgo:setMotion(NULL))
				(alterEgo
					posn: (alterEgo x?) (alterEgo y?)
					view: 60
					xStep: 10
					setMotion: MoveTo 10 95 hangGlideScript
				)
				(= flying 0)
			)
			(4 (gRoom newRoom: 22))
		)
	)
	
	(method (doit)
		(super doit:)
		(if flying
			(glider
				init:
				show:
				posn: (- (alterEgo x?) 14) (- (alterEgo y?) 30)
				setPri: 15
			)
		else
			(glider hide:)
		)
		(if (& (gEgo onControl:) ctlMAROON)         ; leaving the room
			(if (not entering) (RoomScript changeState: 4))
		)
		(if (& (gEgo onControl:) ctlSILVER)         ; fall from bottom
			(if (not falling)
				(fallScript changeState: 1)
				(= falling 1)
			)
		)
		(if (& (gEgo onControl:) ctlGREY)       ; fall from  top
			(if (not falling)
				(fallScript changeState: 4)
				(= falling 1)
			)
		)
	)
)

(instance acornScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1))
			; acorn blowing in the wind
			(1
				(if (not (gEgo has: INV_ACORN))
					(if (== (IsOwnedBy INV_ACORN 63) FALSE)
						(acorn loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)
						(windAcorn
							show:
							cel: 0
							posn: (acorn x?) (acorn y?)
							setCycle: End
							cycleSpeed: 1
						)
					)
				)
			)
			(2 (= cycles (Random 20 70)))
			(3 (self changeState: 1))
		)
	)
)

(instance fallScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0)
			(1      ; = cycles 2  // falling from bottom
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(actionEgo
					show:
					view: 23
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					yStep: 8
					setMotion: MoveTo (gEgo x?) 190 self
					setPri: 15
				)
			)
			(2
				(= cycles 15)
				(actionEgo hide:)
			)
			; (deathSplash:show()posn((actionEgo:x)200)setCycle(End self) setPri(14)cycleSpeed(2))
			(3
				(ShakeScreen 1)
				(self changeState: 6)
				(gTheSoundFX number: 200 play:)
			)
			(4       ; falling from top
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(actionEgo
					show:
					view: 88
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					yStep: 7
					setMotion: MoveTo (gEgo x?) 186 self
					setPri: 1
				)
			)
			(5
				(= cycles 1)
				(ShakeScreen 1)
				(actionEgo hide:)
				(gTheSoundFX number: 200 play:)
			)
			(6 
				(= cycles 10)
			)
			(7	
				(self changeState: 14)
				
			)
			
			(14
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 609
					register:
						{You made a bold step there. A boulder step would be avoiding falling to your rocky death.}
				)
				(gGame setScript: dyingScript)
			)
		)
	)
)

(instance limbScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles (Random 1 40)))
			(1
				(= cycles 15)
				(windAcorn
					show:
					cel: 0
					posn: (limbs x?) (- (limbs y?) 15)
					setCycle: End
					cycleSpeed: 1
				)
				(limbs setCycle: Fwd cycleSpeed: 3)
			)
			(2
				(= cycles 25)
				(limbs setCycle: CT)
			)
			(3 (self changeState: 0))
		)
	)
)

(instance windScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles (Random 1 10)))
			(1
				(wind
					show:
					cel: 0
					posn: (Random 70 270) (Random 50 170)
					setCycle: End self
					cycleSpeed: 1
				)
			)
			(2 (self changeState: 0))
		)
	)
)

(instance windScript2 of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles (Random 1 15)))
			(1
				(wind
					show:
					cel: 0
					posn: (Random 70 270) (Random 50 170)
					setCycle: End self
					cycleSpeed: 1
				)
			)
			(2 (self changeState: 0))
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

(instance actionEgo of Act
	(properties
		y 1
		x 1
		view 408
	)
)

(instance alterEgo of Act
	(properties
		y 150
		x 181
		view 231
	)
)

(instance glider of Prop
	(properties
		y 150
		x 181
		view 60
	)
)

(instance limbs of Prop
	(properties
		y 36
		x 160
		view 58
		loop 4
	)
)

(instance acorn of Prop
	(properties
		y 64
		x 160
		view 58
	)
)

(instance windAcorn of Prop
	(properties
		y 150
		x 181
		view 58
		loop 3
	)
)

(instance windLimbs of Prop
	(properties
		y 150
		x 181
		view 58
		loop 3
	)
)

(instance wind of Prop
	(properties
		y 150
		x 181
		view 58
		loop 3
	)
)

(instance wind2 of Prop
	(properties
		y 150
		x 181
		view 58
		loop 3
	)
)
