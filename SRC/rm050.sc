;;; Sierra Script 1.0 - (do not remove this comment)
; + 3 SCORE // + gInt 3 //
(script# 50)
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
	rm050 0
)

(local

; Scientist's House Outside



	doorOpen =  0
	onBrown =  0
	onSilver =  0
	onPurple =  0
	onGreen =  0
	onBlue =  0
	blast =  0
	zapped =  0
	birdGone =  1
	birdFlying =  0
	tentacleState =  0 ; 0 - tentacle in water, 1 - sticking out, 2 - grabbing you!
	moving =  0
)              ; is ego going up or down the stairs

(instance rm050 of Rm
	(properties
		picture scriptNumber
		north 0
		east 22
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
		(gEgo init:)
		(SetUpEgo)
		;(= gEgoRunning 0)
		(RunningCheck)
		(alterEgo
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
		)
		(switch gPreviousRoomNumber
			(66
				(gEgo posn: 123 131 loop: 0 get: 4 put: 4 50)
				(RoomScript changeState: 6)
				(= moving 1)
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
			)
			(22
				(if (< (gEgo y?) 91)   ; glider
					(gEgo init: hide:)
					(alterEgo show: view: 60 posn: 262 10 xStep: 7 setMotion: MoveTo 159 15 RoomScript ignoreControl: ctlWHITE setPri: 15)
				else
					(gEgo posn: 305 140 loop: 1)
				)
			)
			(114
				(gEgo posn: 107 134 loop: 1)
				(if g110Solved
					(doorScript changeState: 1)
				)
			)
			(else 
				(gEgo posn: 305 140 loop: 1)
			)
		)

		(energy2 init: hide: ignoreActors: setScript: knockScript)
		(sparks init: hide: ignoreActors: setScript: damageScript)
		(kite init: hide: setScript: doorScript)
		(bird
			init:
			hide:
			ignoreControl: ctlWHITE
			ignoreActors:
			setScript: birdScript
		)
		(tentacle
			init:
			hide:
			ignoreActors:
			setScript: tentacleScript
			setPri: 13
		)
		(water init: setCycle: Fwd cycleSpeed: 4)
		
		(if g110Solved (= doorOpen 1))
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(kite
					show:
					xStep: 7
					setMotion: MoveTo 1 20
					ignoreControl: ctlWHITE
					ignoreActors:
					setPri: 15
				)
				(alterEgo
					init:
					show:
					view: 91
					posn: (alterEgo x?) (+ (alterEgo y?) 20)
					yStep: 5
					xStep: 5
					setMotion: MoveTo 131 62 RoomScript
					ignoreControl: ctlWHITE
					setPri: 15
					ignoreActors:
				)
			)
			(2
				(alterEgo view: 92 setMotion: MoveTo 107 134 RoomScript)
			)
			(3
				(= cycles 6)
				(alterEgo hide:)
				(gEgo show: posn: 107 134 loop: 1)
			)
			(4
				(= cycles 3)
				(kite hide:)
				(gEgo get: 3 put: 3 50)
				(gEgo put: 4 50)
				(gEgo put: 25 50)
				(gGame changeScore: 3)
				(= gInt (+ gInt 3))
				(= gArcStl 0)
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
			)
			(5
				(Print 50 2 #width 280 #at -1 20)
			)                                    ; Your makeshift glider drops you off right in front of what you think is a door with nine weird panels on it.
			(6       ; going up and down the stairs
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(if (> (gEgo x?) 150)     ; ego on the right
					(gEgo
						setMotion: MoveTo 118 130 self
						ignoreControl: ctlWHITE
					)
				else
					(gEgo
						setMotion: MoveTo 175 155 self
						ignoreControl: ctlWHITE
					)
				)
			)
			(7
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(= moving 0)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(==
						ctlNAVY
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                  ; warning sign
					(if (> (pEvent x?) 210)
						(PrintOther 50 12)
					else
						(PrintOther 50 4) ; the house itself
						
					)
				)
				(if
					(==
						ctlGREEN
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                   ; pole
					(if (not g110Solved)
						(PrintOther 50 11)
					else
						(PrintOther 50 13)
					)
				)
				(if
					(==
						ctlFUCHSIA
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                     ; Barrel
					(if tentacleState   ; if the tentacle is visible
						(PrintOther 50 14)
					else
						(PrintOther 50 36)
					)
				)
				(if
					(==
						ctlWHITE
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                   ; fence
					(PrintOther 50 15)
				)
				(if
					(==
						ctlTEAL
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                  ; fence w/ sign
					(PrintOther 50 16)
				)
				(if
					(==
						ctlRED
						(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
					)
					(PrintOther 50 17)
				)
				(if
					(==
						ctlMAROON
						(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
					)
					(PrintOther 50 18)
				)
				(if
					(checkEvent
						pEvent
						(bird nsLeft?)
						(bird nsRight?)
						(bird nsTop?)
						(bird nsBottom?)
					)
					(if (not birdGone) (PrintOther 50 33))
				)
				(if (checkEvent pEvent 96 111 90 129)  ; door
					(PrintOther 50 19)
				)
				(if (checkEvent pEvent 80 88 102 117)  ; panel
					(PrintOther 50 20)
				)
			)
		)
		(if (not g110Solved)
			(if (or (Said 'listen[/!*]')
				(Said 'listen/hum,pole'))
				(PrintOther 50 9)
			)
			(if (== (IsOwnedBy 3 50) TRUE)	; handglidered in
				(if (or (Said 'look,use,read,open/portal,map')	; disable using map in this area if puzzle not solved AND had used hanglider
					(Said 'map'))
				(Print 50 38)
				)		
			)
		)
		(if (Said 'look>')
			(if (Said '/house,building') (PrintOther 50 4))
			(if (Said '/pole,glass')
				(if (not g110Solved)
					(PrintOther 50 11)
				else
					(PrintOther 50 13)
				)
			)
			(if (Said '/door,panel,screen')
				(cond 
					(doorOpen (PrintOther 50 32))
					((& (gEgo onControl:) ctlNAVY) (knockScript changeState: 1))
; It already is
					(else (PrintOther 50 19) (PrintOther 50 20))
				)
			)
			(if (Said '/metal,pipe') (PrintOther 50 17))
			(if (Said '/water,hole') (PrintOther 50 18))
			(if (Said '/sign') (PrintOther 50 12))
			(if (Said '/bird')
				(if birdGone (PrintOther 50 34) else (PrintOther 50 33))
			)
			(if (Said '/barrel')
				(if tentacleState   ; if the tentacle is visible
					(PrintOther 50 14)
				else
					(PrintOther 50 36)
				)
			)
			(if (Said '/monster,creature,tentacle')
				(if tentacleState   ; if the tentacle is visible
					(PrintOther 50 37)
				else
					(PrintOther 50 34)
				)
			)
			(if (Said '[/!*]') (PrintOther 50 21))
		; this will handle just "look" by itself
		)
		(if (Said 'take>')
			(if (Said '/bird') (PrintOther 50 35))
		)
		(if (Said 'knock')
			(cond 
				(doorOpen
					(Print {Come on in!} #title {A voice says:} #at -1 20)
					(gRoom newRoom: 66)
				)
				((& (gEgo onControl:) ctlNAVY)
					(if (not g110Solved)
						(knockScript changeState: 1)
					else
						(Print {Come on in!} #title {A voice says:} #at -1 20)
						(gRoom newRoom: 66)
					)
				)
				(else (PrintNCE))
			)
		)
		(if (Said 'ring,use,press/doorbell,bell,(bell<door)')
			(Print {What's a doorbell?} #title {You think:} #at -1 20)
		)
		(if (Said 'throw/rock')
			(PrintOther 50 23)
			(if (not g110Solved) (PrintOther 50 22))
		)
		(if (Said 'use,fly/kite,glider')
			(if (or (gEgo has: INV_KITE) (gEgo has:INV_GLIDER))
				(Print 50 24 #title {You think:} #at -1 20)
			else
				(PrintDHI)
			)
		)
		(if (Said 'crawl')
			(Print 50 25 #title {You think:} #at -1 20)
		)
		(if (Said 'move,break,touch,take/pole')
			(Print {No way I'm touching those things!})
		)
; (if(Said('run'))
;            Print("Moving slowly and carefully is more prudent."#width 280 #at -1 20)
;        )
		(if (Said 'open,enter,use/door,building,panel,house')
			(if (& (gEgo onControl:) ctlNAVY)
				(if doorOpen
					(gRoom newRoom: 66)
				else
					(knockScript changeState: 1)
				)
			else
				(PrintNCE)
			)
		)
	)
	
	(method (doit)
		(super doit:)
; (if(& (send gEgo:onControl()) ctlMAROON)
;            (send gRoom:newRoom(66))
;        )
		(if (<= (gEgo distanceTo: bird) 60)
			(if (not birdGone)
				(if (not birdFlying)
					(birdScript cycles: 0 changeState: 7)
					(= birdGone 1)
				)
			)
		)
		(if (& (gEgo onControl:) ctlLIME)
			(if (< tentacleState 2)
				(= tentacleState 2)
				(tentacleScript changeState: 5)
			)
		)
		(cond 
			((& (gEgo onControl:) ctlGREEN)
				(if (not tentacleState)
					(= tentacleState 1)
					(tentacleScript changeState: 1)
				)
			)
			(tentacleState (= tentacleState 0) (tentacleScript changeState: 3))
		)
		(if (& (gEgo onControl:) ctlSILVER)
			(if (not moving) (self changeState: 6) (= moving 1))
		)
		(if (not g110Solved)
			(if (& (gEgo onControl:) ctlGREY)
				(laserShot)
			else
				(= blast 0)
; (if(& (send gEgo:onControl()) ctlBROWN)
;                (if(not(onBrown))
;                    (sparks:show()posn(310 166)loop(1)setCycle(Fwd)cycleSpeed(3))
;                    = onBrown 1
;                )
;            )(else
;                (sparks:hide())
;                = onBrown 0
;            )
;            (if(& (send gEgo:onControl()) ctlSILVER)
;                (if(not(onSilver))
;                    (energy1:show()posn(239 161)loop(0)setCycle(Fwd)cycleSpeed(3)) //1
;                    (laser:posn(248 151)loop(0)hide()ignoreActors())
;                    = onSilver 1
;                )
;                laserShot()
;
;            )(else
;                (energy1:hide())
;                = onSilver 0
;            )
				(if (& (gEgo onControl:) ctlPURPLE)
					(if (not onPurple)
						(energy2
							show:
							posn: 266 27
							loop: 0
							setCycle: Fwd
							cycleSpeed: 3
						)
						(if (< (gEgo y?) 136) ; ego near top of walkable area
							(laser posn: 233 188 hide: loop: 1 ignoreActors:)
						else
							(laser posn: 233 188 hide: loop: 0 ignoreActors:)
						)
						(= onPurple 1)
					)
				else
					; laserShot()
					(energy2 hide:)
					(= onPurple 0)
				)
			)
		)
	)
)

; (if(& (send gEgo:onControl()) ctlGREEN)
;                (if(not(onGreen))
;                    (energy3:show()posn(134 136)loop(0)setCycle(Fwd)cycleSpeed(3))
;                    (laser:posn(202 126)loop(2)hide()ignoreActors())
;                    = onGreen 1
;                )
;                laserShot()
;            )(else
;                (energy3:hide())
;                = onGreen 0
;            )
;            (if(& (send gEgo:onControl()) ctlBLUE)
;                (if(not(onBlue))
;                    (energy4:show()posn(84 120)loop(0)setCycle(Fwd)cycleSpeed(3))
;                    (laser:posn(177 111)loop(3)hide()ignoreActors())
;                    = onBlue 1
;                )
;                laserShot()
;            )(else
;                (energy4:hide())
;                = onBlue 0
;            )
(instance birdScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0 (= cycles (Random 20 100)))
			(1      ; fly to pipe
				(= birdGone 0)
				(= birdFlying 1)
				(bird
					show:
					posn: 320 58
					view: 120
					setCycle: Walk
					cycleSpeed: 0
					xStep: 6
					yStep: 3
					setMotion: MoveTo 192 77 self
				)
			)
			(2      ; descend to pipe
				(bird view: 121 loop: 1 setMotion: MoveTo 166 103 self)
			)
			(3
				(= cycles 20)    ; landed
				(bird view: 122 loop: 3 setCycle: CT)
				(= birdFlying 0)
			)
			(4
				(= cycles 14)     ; pecking/ preening
				(bird cycleSpeed: 3 setCycle: Fwd)
			)
			(5
				(= cycles (Random 20 120))
				(bird loop: 3 setCycle: CT)
			)
			(6 (self changeState: 4))
				; fly away
			(7
				(bird
					view: 120
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo 320 58 self
				)
				(= birdFlying 1)
			)
			(8
				(bird hide:)
				(= birdFlying 0)
			)
		)
	)
)

(instance doorScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(= cycles 10)
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
			)
			(2
				(= cycles 10)
				(PrintOther 50 26)
			)
			(3
				(= cycles 10)
				(Print 50 27 #title {Voice from inside:} #at -1 20)
			)
			(4
				(= cycles 1)
				(Print 50 10)
				; (door:yStep(3)setMotion(MoveTo 117 44 doorScript)ignoreControl(ctlWHITE)setPri(15))
				(= doorOpen 1)
			)
			(5
				(= cycles 15)
				(Print 50 28 #title {Voice from inside:} #at -1 20)
			)
			(6
				(= cycles 2)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(Print 50 29 #title {You think:} #at -1 20)
			)
			(7 (gRoom newRoom: 66))
		)
	)
)

(instance knockScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 118 133 knockScript
					ignoreControl: ctlWHITE
				)
			)
			(2
				(gEgo hide:)
				(alterEgo
					show:
					posn: (gEgo x?) (gEgo y?)
					view: 335
					loop: 3
					setCycle: End knockScript
					cycleSpeed: 2
				)
			)
			(3
				(= cycles 7)
				(alterEgo loop: 4 setCycle: Fwd)
			)
			(4
				(= cycles 1)
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
			)
			(5 (openDoorProc))
		)
	)
)

(instance damageScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(1 (= cycles 2))
			(2
				(= cycles 8)
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo hide:)
				(alterEgo
					init:
					show:
					loop: (gEgo loop?)
					view: 415
					posn: (gEgo x?) (gEgo y?)
					setCycle: Fwd
				)
				(gTheSoundFX number: 206 play:)	
			)
			(3
				(= gDeathIconEnd 1)
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 273
					register:
						{Guess that yellow sign should've been heeded! You'll have to try a different approach next time.}
				)
				(gGame setScript: dyingScript)
			)
		)
	)
)

(instance tentacleScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			; tentacle comes out
			(1
				(tentacle
					show:
					loop: 0
					cel: 0
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(2
				(tentacle loop: 1 setCycle: Fwd cycleSpeed: 3)
			)
			; tentacle goes away
			(3
				(tentacle
					show:
					loop: 0
					cel: 2
					setCycle: Beg self
					cycleSpeed: 2
				)
			)
			(4 (tentacle hide:))
			; getting captured
			(5
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo (+ (tentacle x?) 12) (tentacle y?) self
					ignoreControl: ctlWHITE
				)
			)
			(6	(= cycles 20)
				(tentacle
					show:
					loop: 2
					cel: 0
					setCycle: End self
					cycleSpeed: 2
				)
				(gEgo hide:)				
			)
			(7	
				(gTheSoundFX number: 202 play:)	
			)
			(8
				(= cycles 14)	
			)
			(9
				;(gTheSoundFX number: 202 play:)
				(= gDeathIconEnd 1)
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 608
					register:
						{Note to self: don't get too close to barrels of water with tentacles sticking out of them. Thanks for playing Betrayed Alliance!}
				)
				(gGame setScript: dyingScript)
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

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 290 #at -1 10)
)

(procedure (openDoorProc)
	(PrintOther 50 30)
	(PrintOther 50 31)
	(gRoom newRoom: 114)
)

(procedure (laserShot)
	; (if(& (send gEgo:onControl()) ctlGREY)
	(if (not blast)
		(laser init: show: cel: 0 setCycle: End)
		(= blast 1)
		(damageScript changeState: 1)
	)
)

; (else
;        = blast 0
;    )
; (instance door of Act
;    (properties y 89 x 117 view 59)
; )
; (instance energy1 of Prop
;    (properties y 89 x 117 view 240)
; )
(instance bird of Act
	(properties
		y 15
		x 159
		view 120
	)
)

(instance energy2 of Prop
	(properties
		y 96
		x 117
		view 240
	)
)

; (instance energy3 of Prop
;    (properties y 89 x 117 view 240)
; )
; (instance energy4 of Prop
;    (properties y 89 x 117 view 240)
; )
(instance sparks of Prop
	(properties
		y 89
		x 117
		view 240
	)
)

(instance laser of Prop
	(properties
		y 140
		x 117
		view 108
	)
)

(instance tentacle of Prop
	(properties
		y 172
		x 132
		view 125
	)
)

(instance alterEgo of Act
	(properties
		y 141
		x 262
		view 60
	)
)

(instance kite of Act
	(properties
		y 15
		x 159
		view 90
	)
)
(instance water of Prop
	(properties
		y 124
		x 188
		view 140
	)
)
