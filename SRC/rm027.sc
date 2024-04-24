;;; Sierra Script 1.0 - (do not remove this comment)
; Score +2 // gInt + 1 //
(script# 27)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use user)

(public
	rm027 0
)

(local

; Western Waterfall Area

	holdingVine =  0
	falling =  0
	moveAndPull =  0
	smallRockFalling = 0
	
)

(instance rm027 of Rm
	(properties
		picture scriptNumber
		north 0
		east 29
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
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 112 loop: 2)
			)
			(29
				(gEgo posn: 290 112 loop: 1)
			)
			(34
				(= gEgoRunning 0)
				(RunningCheck)
				(gEgo posn: 60 112 loop: 0)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		; (send gTheMusic:prevSignal(0)stop()number(27)loop(-1)play())
		(egoFall
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
		)
		(smallRock init: hide: setScript: smallRockScript ignoreControl: ctlWHITE ignoreActors:)
		(deathSplash init: hide: ignoreActors: setPri: 1)
		(waterFallTop init: setCycle: Fwd cycleSpeed: 2)
		(waterFallMiddle init: setCycle: Fwd cycleSpeed: 2)
		(waterFallBottom
			init:
			setCycle: Fwd
			cycleSpeed: 2
			setPri: 5
		)
		(waves
			init:
			setCycle: Fwd
			cycleSpeed: 2
			setScript: RockScript
			setPri: 2
		)
		(sign init:)
		(if g27Passage
			(vine init: setScript: VineScript hide: ignoreActors:)
			(rock
				init:
				posn: 35 128
				ignoreControl: ctlWHITE
				ignoreActors:
				setPri: 6
			)
			(gEgo ignoreControl: ctlWHITE observeControl: ctlGREEN)
		else
			(vine
				init:
				setPri: 14
				setScript: VineScript
				ignoreActors:
			)
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
				(vine setCycle: End RoomScript)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)
			(2
				(vine loop: 6 cel: 0)
				(egoFall
					show:
					yStep: 7
					setMotion: MoveTo 35 170 RoomScript
					setPri: 14
				)
			)
			(3
				(egoFall hide:)
				(deathSplash
					show:
					posn: (egoFall x?) 200
					setCycle: End self
					setPri: 14
					cycleSpeed: 3
				)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(ShakeScreen 1)
				(gTheSoundFX number: 202 play:)
			)
			(4
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 603
					register:
						{It appears the vine wasn't quite strong enough to carry your weight. Testing a vine's strength, perhaps, is a precaution worth taking.}
				)
				(gGame setScript: dyingScript)
			)
			(5
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(egoFall
					show:
					view: 23
					posn: (gEgo x?) (gEgo y?)
					yStep: 7
					setMotion: MoveTo (gEgo x?) 180 RoomScript
				)
			)
			; (send gTheMusic:prevSignal(0)stop()number(903)play())
			(6
				(egoFall hide:)
				(deathSplash
					show:
					posn: (egoFall x?) 200
					setCycle: End self
					setPri: 14
					cycleSpeed: 3
				)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gTheSoundFX number: 202 play:)
			)
			(7
				; (send gTheMusic:prevSignal(0)stop())
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 603
					register:
						{Going for a swim in heavy armor? Didn't you have a sinking feeling about doing that?}
				)
				(gGame setScript: dyingScript)
			)
			; go to and grab vine
			(8
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 66 111 self
					ignoreControl: ctlWHITE
				)
			)
			(9
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gEgo hide:)
				(vine loop: 5 posn: 54 112)
				(= holdingVine 1)
				(User canControl: FALSE)
				(gEgo setMotion: NULL observeControl: ctlWHITE)
				(if moveAndPull (VineScript changeState: 2))
			)
			; releasing vine
			(10
				(User canControl: TRUE)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					show:
					ignoreControl: ctlWHITE
					setMotion: MoveTo 100 111 self
				)
				(vine loop: 4 posn: 40 83)
				(= holdingVine 0)
			)
			(11
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gEgo observeControl: ctlWHITE)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) (vine nsLeft?))
						(< (pEvent x?) (vine nsRight?))
						(> (pEvent y?) (vine nsTop?))
						(< (pEvent y?) (vine nsBottom?))
					)
					(if (not g27Passage)
						(if (not holdingVine) (PrintOther 27 0))
					)
				)
				(if
					(and
						(> (pEvent x?) (rock nsLeft?))
						(< (pEvent x?) (rock nsRight?))
						(> (pEvent y?) (rock nsTop?))
						(< (pEvent y?) (rock nsBottom?))
					)
					(PrintOther 27 1)
				)
				(if
					(and
						(> (pEvent x?) (sign nsLeft?))
						(< (pEvent x?) (sign nsRight?))
						(> (pEvent y?) (sign nsTop?))
						(< (pEvent y?) (sign nsBottom?))
					)
					(= gWndColor 0)   ; clBLACK
					(= gWndBack 6)   ; clBROWN
					(Print 27 4 #title {It Reads:} #width 100 #at 200 20)
					(= gWndColor 0)   ; clBLACK
					(= gWndBack 15)
				)                     ; clWHITE
				(if
					(and
						(> (pEvent x?) 307)    ; Bridge
						(< (pEvent x?) 319)
						(> (pEvent y?) 66)
						(< (pEvent y?) 132)
					)
					(PrintOther 27 9)
				)
			)
		)
		(if (Said 'look,read>')
			(if (Said '/vine') (PrintOther 27 0))
			(if (Said '/river,waterfall')
				(PrintOther 27 2)
				(PrintOther 27 3)
			)
			(if (Said '/bridge') (PrintOther 27 9))
			(if (Said '/rock')
				(if g27Passage
					(PrintOther 27 1)
				else
					(PrintOther 27 10)
				)
				(if smallRockFalling
					(PrintOther 27 15)
				)	
			)
			(if (Said '/sign')
				(= gWndColor 0)   ; clBLACK
				(= gWndBack 6)   ; clBROWN
				(Print 27 4 #title {It Reads:} #width 100 #at 200 20)
				(= gWndColor 0)   ; clBLACK
				(= gWndBack 15)
			)                     ; clWHITE
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(PrintOther 27 11)
				(if (not g27Passage)
					(PrintOther 27 12)
				)
			)
		)
		(if (Said 'take,touch/vine')
			(if (not g27Passage)
				(if (& (gEgo onControl:) ctlRED)
					(if (not holdingVine)
						(PrintOK)
						(self changeState: 8)
					else
						(Print {You already are.})
					)
				else
					(PrintOther 27 5)
				)
			else
				(PrintOther 27 5)
			)
		)
		(if (Said 'release,drop/[vine]')
			(if holdingVine
				(self changeState: 10)
			else
				(Print {You aren't holding a vine.})
			)
		)
		(if (Said 'swing')
			(if holdingVine
				(RoomScript changeState: 1)
			else
				(PrintOther 27 6)
			)
		)
		(if (Said 'climb')
			(if holdingVine
				(PrintOther 27 7)
			else
				(PrintOther 27 6)
			)
		)
		(if (Said 'use/vine') (PrintOther 27 8))
		(if (Said 'pull,test,yank/vine,durability')
			(if (not g27Passage)
				(cond 
					(holdingVine (VineScript changeState: 2))
					((& (gEgo onControl:) ctlRED) (PrintOK) (= moveAndPull 1) (self changeState: 8))
					(else (PrintNCE))
				)
			else
				(PrintOther 27 14)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (== (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 26)
		)
		(if (== (gEgo onControl:) ctlGREY) (gRoom newRoom: 29))
		(if (== (gEgo onControl:) ctlBROWN)
			(gRoom newRoom: 34)
		)
		(if (== (gEgo onControl:) ctlMAROON)
			(if (not falling)
				(RoomScript changeState: 5)
				(= falling 1)
			)
		)
	)
)

(instance VineScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(2
				(= cycles 10)
				(vine loop: 7 setCycle: End cycleSpeed: 2)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)
			(3
				(ShakeScreen 2)
				(vine loop: 8)
				(tempEgo init: setCycle: End VineScript)
				(RockScript changeState: 2)
			)
			(4
				(vine loop: 6)
				(tempEgo hide:)
				(gEgo show: posn: 69 111)
				(vineFall
					init:
					yStep: 4
					setMotion: MoveTo 48 190 VineScript
					setPri: 14
					ignoreActors:
				)
			)
			(5
				(vineFall
					view: 31
					xStep: 1
					setMotion: MoveTo 313 190 VineScript
				)
			)
			(6 (vineFall hide:))
		)
	)
)

(instance RockScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(2
				(rock
					init:
					yStep: 8
					setMotion: MoveTo 31 128 RockScript
					ignoreActors:
					ignoreControl: ctlWHITE
					setPri: 6
				)
			)
			(3
				(= cycles 5)
				(ShakeScreen 2)
				(gTheSoundFX number: 200 play:)
				(gEgo ignoreControl: ctlWHITE)
				(= holdingVine 0)
				(= g27Passage 1)
				(gGame changeScore: 2)
				(++ gInt)
			)
			(4
				(= gEgoRunning 0)
				(RunningCheck)
				(gEgo setMotion: MoveTo 34 111 RockScript)
			)
			(5
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(PlayerControl)
				(gEgo observeControl: ctlGREEN ignoreControl: ctlWHITE)
			)
		)
	)
)
(instance smallRockScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles (Random 10 70))
			)
			(1
				(smallRock show: posn: (Random 28 42) 1 setCycle: Walk xStep: 5 yStep: 8 setMotion: MoveTo 41 38 self)
				(= smallRockFalling 1)	
			)
			(2
				(smallRock setMotion: MoveTo (+ (- (smallRock x?) 10)(Random 10 30)) 170 self)	
			)
			(3 (= cycles (Random 100 300))
				(smallRock hide:)
				(= smallRockFalling 0)		
			)
			(4
				(self changeState: 1)	
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

(instance deathSplash of Prop
	(properties
		y 187
		x 147
		view 87
		loop 1
	)
)

(instance waterFallTop of Prop
	(properties
		y 41
		x 15
		view 22
	)
)

(instance waterFallMiddle of Prop
	(properties
		y 83
		x 27
		view 22
		loop 1
	)
)

(instance waterFallBottom of Prop
	(properties
		y 178
		x 26
		view 22
		loop 2
	)
)

(instance waves of Prop
	(properties
		y 190
		x 89
		view 22
		loop 3
	)
)

(instance vine of Prop
	(properties
		y 83
		x 40
		view 22
		loop 4
	)
)

(instance sign of Prop
	(properties
		y 121
		x 283
		view 251
		loop 2
	)
)

(instance egoFall of Act
	(properties
		y 130
		x 40
		view 33
	)
)

(instance vineFall of Act
	(properties
		y 130
		x 48
		view 32
	)
)

(instance tempEgo of Prop
	(properties
		y 110
		x 62
		view 22
		loop 9
	)
)

(instance rock of Act
	(properties
		y 1
		x 31
		view 34
	)
)
(instance smallRock of Act
	(properties
		y 1
		x 31
		view 139
	)
)
