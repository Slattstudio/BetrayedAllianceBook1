;;; Sierra Script 1.0 - (do not remove this comment)
; +1 Score  //
(script# 26)
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
	rm026 0
)

(local

; Entrance to Cave to Hermit's Hideaway



	; (use "sciaudio")
	countdown =  -40
	number =  0
	walkingIn =  0
	run =  0
	canClimb =  0
	falling =  0
)
; snd

(instance rm026 of Rm
	(properties
		picture scriptNumber
		north 0
		east 40
		south 27
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
		(RunningCheck)
		(egoFall
			init:
			hide:
			ignoreActors:
			setScript: fallingScript
		)
		(alterEgo init: hide: ignoreActors: ignoreControl: ctlWHITE)
		(egoClimb init: hide: ignoreActors: setPri: 9)
		(ladder init: hide: setScript: LadderScript setPri: 9)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
			(68
				(if gEgoRunning (= run 1))
				(= gEgoRunning 0)
				(gEgo init: posn: 150 65 hide:)
				(egoProp init: posn: 150 53 loop: 2 z: countdown)
				(= walkingIn 1)
				(RoomScript changeState: 1)
				;(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
				
			)
			(27
				(gEgo posn: 150 170 loop: 3)
			)
			; (send gTheMusic:prevSignal(0)stop())
			(30
				(gEgo posn: 27 44 loop: 0)    ; ignoreControl(ctlMAROON)ignoreControl(ctlWHITE))
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)

				(= canClimb 1)
				(RoomScript changeState: 7)
			)
			(34 (gEgo posn: 29 43 loop: 1 hide:)				
				(RoomScript changeState: 10)
				; (send gTheMusic:prevSignal(0)stop())
			)
			(40
				(gEgo posn: 300 120 loop: 1)
			)
		)
		; (send gTheMusic:prevSignal(0)stop())
		
		(= gFollowed 0)
		(= gRanAway 0)
		(if [g107Solved 3] (= canClimb 1))
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			(1 (= cycles 2) (gEgo hide:))
			(2
				(= cycles 1)
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
				(gEgo show: setMotion: MoveTo 150 82 RoomScript)
			)
			(5
				(= cycles 2)
				(PlayerControl)
				(= walkingIn 0)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(if run (= gEgoRunning 1) (RunningCheck))
			)
			(6
				(if (not g26Intro)
					(Print 26 2 #width 290 #at -1 5) ; As you approach over the hill you hear faint voices and laughter from overhead.
					(= g26Intro 1)
				)
			)
			(7 (= cycles 1))
			(8
				(= seconds 2)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)
			(9
				(if (not [g107Solved 3])
					(= gWndColor 14)
					(= gWndBack 3)
					(Print 26 3 #title {Henry:} #at 80 30) ; Oh by the way. 'Ere's a ladder.
					(= gWndColor 0)
					(= gWndBack 15)
					(LadderScript changeState: 1)
					(gGame changeScore: 1)
					(= [g107Solved 3] 1)
				)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			; entering from above
			(10
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(alterEgo show: posn: 29 10 view: 130 yStep: 6 setMotion: MoveTo 29 40 self)	
			)
			(11	(= cycles 2)
				(alterEgo view: 131 loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)
				(gTheSoundFX number: 200 play:)
			)
			(12 
				(ShakeScreen 2)
				
			)
			(13
				(alterEgo hide:)
				(gEgo show: x: (alterEgo x?) y: (alterEgo y?) loop: 1)
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
				(if
					(and
						(> (pEvent x?) 14)    ; cave opening
						(< (pEvent x?) 32)
						(> (pEvent y?) 1)
						(< (pEvent y?) 42)
					)
					(Print 26 4 #width 280 #at -1 8)
				)
				(if
					(==
						ctlWHITE
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)
					(Print 26 5 #width 280 #at -1 30)
				)                                    ; The deadened tree to the east stands as an ill omen of the Carmyle Family Graveyard.
				(if
					(and
						(> (pEvent x?) 294)    ; towards the graveyard
						(< (pEvent x?) 313)
						(> (pEvent y?) 34)
						(< (pEvent y?) 65)
					)
					; Print(26 6 #at -1 30) // Yeah, it's a rock.
					(Print 26 7 #width 280 #at -1 8) ; It looks like a sedimentary rock, but don't take it for granite.
					(Print 26 8 #width 280 #at -1 8) ; No laugh? Was the delivery too rocky?
					(Print 26 9 #width 280 #at -1 8)
				)
			)
		)                                           ; What can I say, geology jokes have their faults.
		(if (Said 'look>')
			(if (Said '/cave,door,entrance')
				(Print 26 4 #width 280 #at -1 8)
			)
			(if (Said '/tree') (Print 26 5 #width 280 #at -1 8))
			(if (Said '/rock')
				(Print 26 7 #width 280 #at -1 8) ; It looks like a sedimentary rock, but don't take it for granite.
				(Print 26 8 #width 280 #at -1 8) ; No laugh? Was the delivery too rocky?
				(Print 26 9 #width 280 #at -1 8)
			)                                   ; What can I say, geology jokes have their faults.
			(if (Said '[/!*]') (Print 26 10 #width 280 #at -1 8))
			; this will handle just "look" by itself ; On the clearing you see a dead tree opening up to the cemetery and overhead there is a cave entrance. The faint sound a falling water drifts to your ears.
		)                                        ; There's nothing interesting about it.
		;(if (Said 'show/ladder') (LadderScript changeState: 1))
		(if (Said 'listen')
			(Print
				{The voices are too faint to make out, but they're definitely coming from the cave.}
				#width
				280
				#at
				-1
				8
			)
		)
		(if (or (Said 'climb') (Said 'use/ladder'))
			(cond 
				(canClimb
					(cond 
						((== (gEgo onControl:) ctlGREY) (LadderScript changeState: 3))
						((& (gEgo onControl:) ctlRED) (LadderScript changeState: 6))
						(else (PrintNCE))
					)
				)
				((== (gEgo onControl:) ctlGREY) (LadderScript changeState: 9))
				((== (gEgo onControl:) ctlGREY) (PrintOther 26 0))
				(else (PrintOther 26 13))
			)
		)
	)
	
	; Print(26 0) /* That doesn't seem to be possible. */
	(method (doit)
		(super doit:)
		(if (== (gEgo onControl:) ctlSILVER)
			(if (not walkingIn) (gRoom newRoom: 68))
		)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 30)
		)
		(if (== (gEgo onControl:) ctlGREEN)
			(if (not falling)
				(fallingScript changeState: 5)
				(= falling 1)
			)
		)
		(if (and (> (gEgo x?) 40) (< (gEgo y?) 58))
			(if (not falling)
				(fallingScript changeState: 5)
				(= falling 1)
			)
		)
		; below priority coding is to allow the map to be drawn over the tree and ego behind it
		(if (> (gEgo y?) 145)
			(gEgo setPri: 12)	
		else
			(gEgo setPri: -1)
		)
	)
)

(instance fallingScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0)
			(4)
			; (egoFall:show()view(23)cel(0)x(send gEgo:x)y(send gEgo:y)setCycle(End fallingScript)setPri(9))
			(5
				(gEgo hide:)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(egoFall
					show:
					view: 127
					x: (gEgo x?)
					y: (gEgo y?)
					yStep: 10
					ignoreControl: ctlWHITE
					setMotion: MoveTo (gEgo x?) 132 self
					setPri: 9
				)
			)
			(6
				(= seconds 2)
				(ShakeScreen 4)
				(egoFall
					view: 409
					posn: (gEgo x?) 132
					loop: 0
					cel: 0
					setCycle: Fwd
					cycleSpeed: 3
				)
				(gTheSoundFX number: 200 play:)
				; Fall damage
				(= gHlth (- gHlth 4))
				(if (< gHlth 1)
					(= dyingScript (ScriptID DYING_SCRIPT))
					(dyingScript
						caller: 609
						register:
							{Ouch! That fall hurt just a bit too much. Be wary of your health when deciding not to climb.}
					)
					(gGame setScript: dyingScript)
				)
			)
			(7
				(if canClimb (PrintOther 26 1) else (PrintOther 26 12))
; Ouch! I guess gravity IS faster than simply climbing down.
				(egoFall loop: 1 setCycle: End fallingScript)
			)
			(8
				(egoFall hide:)
				(gEgo show: x: (egoFall x?) y: (egoFall y?))
				(LadderScript changeState: 7)
			)
		)
	)
)

(procedure (elevation)
	(++ countdown)
	(egoProp setCycle: Fwd z: countdown)
	(if (== countdown -10) (RoomScript changeState: 4))
)

(instance LadderScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(ladder init: show: setPri: 9 setCycle: End LadderScript)
				(gTheSoundFX number: 205 play:)
			)
			(2
				(ladder setCycle: Beg)
				(= canClimb 1)
			)
			(3       ; Moving up the ladder
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					ignoreControl: ctlWHITE
					setMotion: MoveTo 30 132 LadderScript
				)
			)
			(4
				(gEgo hide:)
				(egoClimb
					init:
					show:
					posn: 30 132
					show:
					setPri: 9
					ignoreActors:
					ignoreControl: ctlWHITE
					setCycle: Walk
					yStep: 4
					setMotion: MoveTo 30 54 LadderScript
				)
			)
			(5
				(gEgo show: loop: 1 posn: 30 46 observeControl: ctlWHITE)
				(egoClimb hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= gEgoRunning 0)
				(RunningCheck)
			)
			(6
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(egoClimb
					init:
					show:
					posn: 30 56
					setPri: 9
					ignoreActors:
					ignoreControl: ctlWHITE
					setCycle: Walk
					yStep: 4
					setMotion: MoveTo 30 132 LadderScript
				)
				(gEgo hide:)
			)
			(7
				(gEgo
					show:
					posn: (gEgo x?) 132
					ignoreControl: ctlWHITE
					setMotion: MoveTo 45 132 self
				)
				(egoClimb hide:)
			)
			(8
				(gEgo observeControl: ctlWHITE)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= falling 0)
			)
			(9       ; Failing to Climb
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					ignoreControl: ctlWHITE
					setMotion: MoveTo 30 132 LadderScript
				)
			)
			(10
				(= cycles 15)
				(gEgo hide:)
				(egoClimb
					init:
					show:
					posn: 38 132
					show:
					setPri: 9
					ignoreActors:
					ignoreControl: ctlWHITE
					setCycle: Fwd
				)
			)
			(11
				(gEgo
					show:
					loop: 1
					posn: (gEgo x?) (gEgo y?)
					setMotion: MoveTo 50 132 LadderScript
				)
				(egoClimb hide:)
				(= gEgoRunning 0)
				(RunningCheck)
			)
			(12
				(gEgo observeControl: ctlWHITE)
				(Print 26 0)
; That doesn't seem to be possible.
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
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

(instance alterEgo of Act
	(properties
		y 180
		x 27
		view 410
		loop 1
	)
)
(instance egoProp of Prop
	(properties
		y 65
		x 150
		view 0
	)
)

(instance egoClimb of Act
	(properties
		y 150
		x 38
		view 245
	)
)

(instance ladder of Prop
	(properties
		y 140
		x 27
		view 35
	)
)

(instance egoFall of Act
	(properties
		y 130
		x 40
		view 23
	)
)

(instance egoStand of Prop
	(properties
		y 130
		x 40
		view 23
	)
)
