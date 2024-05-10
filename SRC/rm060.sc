;;; Sierra Script 1.0 - (do not remove this comment)
; + 3 SCORE // gInt + 1 //
(script# 60)
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
	rm060 0
)

(local

; Eastern Wall - WELL



	countdown =  -40
	number =  0
	walkingIn =  0
	run =  0
	portalVis =  0
	marbleInBucket =  0
	bucketUp =  0
	wishMade =  0
)

(instance rm060 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 49
		west 45
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
		(RunningCheck)
		(alterEgo
			init:
			hide:
			ignoreActors:
			setScript: shovelScript
		)
		(handle
			init:
			setPri: 9
			ignoreActors:
			setScript: wellScript
		)
		(rope init: setPri: 9 ignoreActors:)
		(symbolRock init: ignoreActors: ignoreControl: ctlWHITE)
		(if (not gRuin) ; not discovered portal
			(portal
				init:
				ignoreActors:
				setPri: 1
				loop: 1
				setScript: teleScript
			)
			(gEgo observeControl: ctlRED)
		else
			(portal
				init:
				ignoreActors:
				setPri: 1
				loop: 1
				setScript: teleScript
			)
			(symbolRock x: 297)
			(gEgo observeControl: ctlBLUE)
			(= portalVis 1)
		)
		(switch gPreviousRoomNumber
			(45
				(if (not gTeleporting)
					(gEgo posn: 20 134 loop: 0)
				else
					(gEgo hide: posn: 120 179)
					(teleScript changeState: 1)
				)
			)
			(49
				(if (not gTeleporting)
					(gEgo posn: 120 179 loop: 3)
				else
					(gEgo hide: posn: 120 179)
					(teleScript changeState: 1)
				)
			)
			(22
				(if (not gTeleporting)
					(if gEgoRunning (= run 1))
					(= gEgoRunning 0)
					(gEgo init: posn: 120 70 hide:)
					(egoProp init: posn: 120 46 loop: 2 z: countdown)
					(RoomScript changeState: 1)
				else
					(gEgo hide: posn: 120 179)
					(teleScript changeState: 1)
				)
			)
			(else 
				(gEgo hide: posn: 120 179)
				(teleScript changeState: 1)
				
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
			; Entering the room from the hill
			(1 (= cycles 2) (gEgo hide:))
			(2
				(= cycles 1)
				(= walkingIn 1)
				(ProgramControl)
				(elevation)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)
			(3
				(= cycles 1)
				(elevation)
				(if (not (>= countdown -9))
					(RoomScript changeState: 2)
				)
			)
			(4
				(egoProp hide:)
				(gEgo show: posn: 120 58 setMotion: MoveTo 120 82 self)
			)
			(5
				(PlayerControl)
				(= walkingIn 0)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(if run (= gEgoRunning 1) (RunningCheck))
			)
			; Moving the stone
			(6
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo (- (symbolRock x?) 35) (symbolRock y?) self
				)
				(gEgo ignoreControl: ctlRED)
			)
			(7
				(alterEgo
					show:
					view: 113
					setCycle: Walk
					posn: (gEgo x?) (gEgo y?)
					setMotion: MoveTo 257 (gEgo y?)
					cycleSpeed: 0
				)
				(gEgo hide:)
				(symbolRock setMotion: MoveTo 297 (symbolRock y?) self)
			)
			(8
				(= cycles 2)
				(alterEgo hide:)
				(gEgo
					show:
					loop: 0
					posn: (alterEgo x?) (alterEgo y?)
					observeControl: ctlWHITE
				)
			)
			(9
				(PrintOther 60 11)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gGame changeScore: 1)
				(gEgo observeControl: ctlBLUE)
				(= gRuin 1)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(==
						ctlGREY
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                  ; well
					(PrintOther 60 2)
				)
				(if
					(checkEvent
						pEvent
						(symbolRock nsLeft?)
						(symbolRock nsRight?)
						(symbolRock nsTop?)
						(symbolRock nsBottom?)
					)
					(PrintOther 60 3)
				)
				(if (checkEvent pEvent 35 63 46 67)       ; sign
					(= gWndColor 0) ; clYELLOW
					(= gWndBack 6) ; clDARKBLUE
					(if (> (gEgo x?) 160)     ; if eago on right of screen
						(Print 60 5 #at 20 -1 #width 140 #title {Sign:})
					else
						(Print 60 5 #at 160 -1 #width 140 #title {Sign:})
					)
					(= gWndColor 0) ; clBLACK
					(= gWndBack 15)
				)
			)
		)                         ; clWHITE
		(if (Said 'look<in/well')
			(if (& (gEgo onControl:) ctlGREY)
				(PrintOther 60 23)
			else
				(PrintNCE)
			)
		)
		(if (Said 'look<in/bucket')
			(if (& (gEgo onControl:) ctlGREY)
				(PrintOther 60 24)
			else
				(PrintNCE)
			)
		)
		(if (Said 'take,cut,remove/rope')
			(if (or (& (gEgo onControl:) ctlGREY)	
					(& (gEgo onControl:) ctlNAVY))
				(PrintOther 60 31)		
			else
				(PrintNCE)
			)
		)
		(if (Said 'take,remove,(pick<up)>')
			(if (Said '/bucket')
				(if (or (& (gEgo onControl:) ctlGREY)	
						(& (gEgo onControl:) ctlNAVY))
					(if bucketUp
						(PrintOther 60 29)	
					else
						(PrintOther 60 30)
					)
				else
					(PrintNCE)
				)
			)	
		)
		(if (Said 'look,read>')
			(if (Said '/well') (PrintOther 60 2))
			(if (Said '/symbol,graffiti,marking') (PrintOther 60 25))
			(if (Said '/rock')
				(PrintOther 60 3)
			)
			(if (Said '/bucket')
				(if (or (& (gEgo onControl:) ctlGREY)	
						(& (gEgo onControl:) ctlNAVY))
					(if bucketUp
						(PrintOther 60 27)	
					else
						(PrintOther 60 28)
					)
				else
					(PrintOther 60 26)
				)
			)
			(if (Said '/sign,post')
				(= gWndColor 0) ; clYELLOW
				(= gWndBack 6) ; clDARKBLUE
				(if (> (gEgo x?) 160)     ; if eago on right of screen
					(Print 60 5 #at 20 -1 #width 140 #title {Sign:})
				else
					(Print 60 5 #at 160 -1 #width 140 #title {Sign:})
				)
				(= gWndColor 0) ; clBLACK
				(= gWndBack 15)
			)                 ; clWHITE
			(if (Said '[/!*]') (PrintOther 60 0))
			; this will handle just "look" by itself
		)
		(if
		(or (Said 'make/wish') (Said 'throw,toss,drop/coin'))
			(if (> gGold 0)
				(if
				(and (& (gEgo onControl:) ctlGREY) (> (gEgo y?) 124))
					(wellScript changeState: 13)
				else
					(PrintNCE)
				)
			else
				(PrintOther 60 21)
			)
		)
		(if (Said '(climb,jump,get,go<in),enter/well,bucket')
			(PrintOther 60 4)
		)
		(if (Said 'touch,push,move/symbol')
			(if (<= (gEgo distanceTo: symbolRock) 30)
				(PrintOther 60 13)
			else
				(PrintNCE)
			)
		)
		(if (Said 'touch,push,move/rock')
			(if (not gRuin)
				(cond 
					((& (gEgo onControl:) ctlSILVER) (PrintOK) (RoomScript changeState: 6))
					((<= (gEgo distanceTo: symbolRock) 30) (PrintOther 60 12))
					(else (PrintNCE))
				)
			else
				(PrintOther 60 10)
			)
		)
		(if (Said 'use/well')
			(if (& (gEgo onControl:) ctlGREY)
				(if (not gHardMode)
					(wellScript changeState: 1)
				else
					(PrintOther 60 6)
				)
			else
				(PrintNCE)
			)
		)
		(if (Said 'raise,lift/bucket')
			(if bucketUp
				(PrintItIs)
			else
				(if (& (gEgo onControl:) ctlGREY)
					(wellScript changeState: 1)	
				else
					(PrintNCE)
				)
			)	
		)
		(if (Said 'lower,drop/bucket')
			(if (not bucketUp)
				(PrintItIs)
			else
				(if (& (gEgo onControl:) ctlGREY)
					(wellScript changeState: 1)	
				else
					(PrintNCE)
				)
			)	
		)
		(if
			(Said 'use,turn/pulley,handle')
				
			(if (& (gEgo onControl:) ctlGREY)
				(wellScript changeState: 1)
			else
				(PrintNCE)
			)
		)
		(if (Said 'rub,feel,hold/marble,sun,shooter')
			(if (gEgo has: 9)
				(if (not gNep) (Print 0 95) else (Print 0 94))
			else
				(PrintDHI)
			)
		)
		(if
			(or
				(Said 'drop/marble')
				(Said 'put<marble/in<well,bucket')
			)
			(if (& (gEgo onControl:) ctlGREY)
				(if (not marbleInBucket)
					(if (gEgo has: INV_MARBLES)
						(if (not bucketUp)
							(= marbleInBucket 1)
							(wellScript changeState: 1)
						else
							(= marbleInBucket 1)
							(wellScript changeState: 1)
						)
					else
						(Print
							{You don't have any marbles to put in.}
							#width
							280
							#at
							-1
							10
						)
					)
				)
			else
				(PrintNCE)
			)
		)
		(if
		(or (Said 'drop/*') (Said 'put<*/in<well,bucket'))
			(PrintOther 60 20)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(if (not walkingIn) (gRoom newRoom: 22))
		)
		(if (<= (gEgo distanceTo: portal) 20)
			(if portalVis (portal loop: 1))
		)
	)
)

; (send gGame:changeScore(1))
(procedure (elevation)
	(++ countdown)
	(egoProp setCycle: Fwd z: countdown)
	(if (== countdown -10) (RoomScript changeState: 4))
)

(instance teleScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				; (send gEgo:hide())
				
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
				(alterEgo
					show:
					setCycle: Beg teleScript
					cycleSpeed: 2
					ignoreActors:
				)
				(gTheSoundFX number: 205 play:)
				(= gTeleporting 0)
			)
			(2
				(gEgo show: posn: 257 179 loop: 2)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
)

(instance shovelScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 226 177 shovelScript)
			)
			(2
				(= cycles 25)
				(gEgo hide:)
				(alterEgo
					show:
					view: 419
					loop: 0
					posn: 226 177
					setCycle: Fwd
					cycleSpeed: 2
				)
			)
			(3
				(alterEgo hide:)
				(gEgo show: loop: 0)
				(portal setCycle: End shovelScript)  ; activate portal
				(gGame changeScore: 1)
				(= gRuin 1)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(4
				(= portalVis 1)
				(PrintOther 60 7)
			)
		)
	)
)

(instance wellScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1      ; Using the well
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 174 131 self
					ignoreControl: ctlWHITE
				)
				(if (== marbleInBucket 1)
					(if bucketUp (PrintOther 60 8) else)
				; Print("You drop the marble down the well into the bucket." #width 280 #at -1 8)
				)
			)
			(2          ; prepping to crank
				(gEgo hide:)
				(handle hide:)
				(alterEgo
					show:
					view: 111
					loop: 2
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(3
				(if bucketUp
					(self changeState: 8)
				else                       ; move the bucket down
					(alterEgo loop: 3 setCycle: Fwd cycleSpeed: 1) ; better to have a lever animation (bucket as well...)
					(rope setCycle: End self cycleSpeed: 3)
					(if (== marbleInBucket 1) (PrintOther 60 9))
					(gTheSoundFX number: 203 play:)
				)
			)
			(4
				(alterEgo loop: 2 cel: 2 setCycle: Beg self cycleSpeed: 2)
				;(gTheSoundFX stop:)
			)
			(5
				(gEgo show: setMotion: MoveTo 223 136 self)
				(alterEgo hide:)
				(handle show:)
			)
			(6 (= cycles 2) (gEgo loop: 3))
			(7
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= bucketUp 1)
				(cond 
					((or (not g60Well) (< gGold 9))
						(PrintOther 60 15)
						(PrintOther 60 16)
						(++ gGold)
						(= g60Well 1)
						(if marbleInBucket
							(if (not gNep)
								(PrintOther 60 17)
								(++ gNep)
								(= marbleInBucket 0)
								(++ gMarbleNum)
								((gInv at: 9) count: gMarbleNum)
								(gGame changeScore: 2)
								(++ gInt)
							else
								(PrintOther 60 18)
								(= marbleInBucket 0)
							)
						)
					)
					((not marbleInBucket) (PrintOther 60 19))
					((not gNep)
						(PrintOther 60 17)
						(++ gNep)
						(= marbleInBucket 0)
						(++ gMarbleNum)
						((gInv at: 9) count: gMarbleNum)
						(gGame changeScore: 2)
						(++ gInt)
					)
					(else (PrintOther 60 18) (= marbleInBucket 0))
				)
				(gEgo observeControl: ctlWHITE)
			)
			; putting the bucket back down
			(8
				(alterEgo loop: 3 setCycle: Fwd cycleSpeed: 1)
				(rope setCycle: Beg self cycleSpeed: 3)
				(gTheSoundFX number: 203 play:)
			)
			(9
				(alterEgo loop: 2 cel: 2 setCycle: Beg self cycleSpeed: 2)
				;(gTheSoundFX stop:)
			)
			(10
				(if marbleInBucket
					(= marbleInBucket 2)
					(= bucketUp 0)
					(self changeState: 1)
				else
					(gEgo show: setMotion: MoveTo 173 139 self)
					(alterEgo hide:)
					(handle show:)
				)
			)
			(11
				(= cycles 2)
				(gEgo loop: 2)
			)
			(12
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gEgo observeControl: ctlWHITE)
				(= bucketUp 0)
			)
			(13      ; Making a wish
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 222 134 self
					ignoreControl: ctlWHITE
				)
			)
			(14
				(= cycles 2)
				(gEgo loop: 3)
			)
			(15
				(PrintOther 60 22)
				(-- gGold)
				(++ wishMade)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gEgo observeControl: ctlWHITE)
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

(instance egoProp of Prop
	(properties
		y 65
		x 150
		view 0
	)
)

(instance alterEgo of Act
	(properties
		y 179
		x 257
		view 128
		cel 10
	)
)

(instance portal of Prop
	(properties
		y 178
		x 242
		view 16
	)
)

(instance handle of Prop
	(properties
		y 101
		x 181
		view 111
	)
)

(instance rope of Prop
	(properties
		y 107
		x 221
		view 111
		loop 1
	)
)

(instance symbolRock of Act
	(properties
		y 178
		x 242
		view 112
	)
)
