;;; Sierra Script 1.0 - (do not remove this comment)

(script# 101)
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
	rm101 0
)

(local
; ARCADE-STYLE BOSS FIGHT/SPEED CHALLENGE
; BY RYAN SLATTERY




; ROOM VARIABLES
	rMovingUp =  0      ; Will equal TRUE (1) when moving UP. When True, allows player to stop by pressing UP arrow
	rMovingDown =  0    ; Same as rMovingUp, but for moving DOWN and using the DOWN arrow
	rPlayerLife =  6    ; If 0, player dies. Subtracts by ONE (-1) by two means in the Doit Method.
	rCompLife =  3      ; Life total of the Flying Beast. Subtracts by ONE (-1) when you shoot an explosive under-
	; neath the belly of the Beast.
	rLaserPower =  4    ; Starts half-full. Ranges from 0 - 8 in value. When full (8), player can shoot a laser
	randoRock1          ; Random Variables used to...
	randoRock2          ; set the trajectories of the...
	randoRock3          ; explosive rocks, well, randomly
	rockHit1 =  0       ; If TRUE (1) you got hit by Rock1. Used to prevent rock from damaging every cycle
	rockHit2 =  0       ; Same as ^^ except with Rock2
	rockHit3 =  0       ; Same as ^^ you get the idea
	randoBomb           ; Similar to RandoRock, except it is for fireballs from the Beast
	bombHit1 =  0       ; Similar to RockHit, except ^^
	bombHit2 =  0       ; See ^^
	countdownminutes =  2   ; Used to set a speed challenge and add extra tension
	countdownseconds =  30  ; ^^
	milsec =  15
	countmilsec =  5
)                           ; For complete understanding of how the timer works visit www.scicommunity.com
; in the sci syntax section in the forums. This code is mainly used from that source.

(instance rm101 of Rm
	(properties
		picture scriptNumber
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		; (MenuBar:hide())
		(gGame setSpeed: 3)
		; (send gTheMusic:prevSignal(0)stop()number(103)loop(-1)play())
		(WriteMin)  ; See Procedures @ 365...used to visualize the time challenge
		(WriteSec)  ; ^^
		(keys init:)    ; Show the player which buttons do what action
		(bomb1 init: ignoreActors: ignoreControl: ctlWHITE hide:)   ; Fireballs of the beast
		(bomb2 init: ignoreActors: ignoreControl: ctlWHITE hide:)   ; ^^
		(gargoyle
			init:
			setScript: gargFlight
			setCycle: Fwd
			cycleSpeed: 4
		)
		(zeroTimer init: setPri: 17 hide:)  ; Shows a 0 (ZERO) when seconds are at 9 or less. Ex: 1:07, not 1: 7
		(boat init: setCycle: Fwd ignoreActors:)
		(laser init: hide: ignoreActors:)
		(lavaFlowLight init: setCycle: Fwd ignoreActors:)   ; Attempts to add the the illusion of motion
		(lavaFlowDark init: setCycle: Fwd ignoreActors:)    ; ^^
		(rock1
			init:
			setScript: foreground
			posn: 100 153
			setCycle: CT
			xStep: 8
			setMotion: MoveTo 0 152
			setPri: 14
			ignoreActors:
			ignoreControl: ctlWHITE
		)
		(playerLifeBar
			init:
			cel: rPlayerLife
			setPri: 16
			ignoreActors:
		)
		(compLifeBar
			init:
			cel: rCompLife
			setPri: 16
			ignoreActors:
		)
		(powerBar
			init:
			setScript: increaseLaser
			cel: rLaserPower
			setPri: 17
		)
		(city init: ignoreActors:)
		(distanceMarker init: setPri: 17)   ; Ended up not using this as a distance marker...but it looks cool!
		(sharpRock1
			init:
			setScript: dangerRocks
			xStep: 7
			setCycle: Walk
			setMotion: MoveTo 1 130
			setPri: 14
			ignoreActors:
		)
		(infoButton init:)
		(Display
			{Move Up}
			dsCOORD
			52
			5
			dsCOLOUR
			1
; dark blue
			dsBACKGROUND
			clTRANSPARENT
			dsFONT
			4
		)
		(Display
			{Move Down}
			dsCOORD
			52
			19
			dsCOLOUR
			1
; dark blue
			dsBACKGROUND
			clTRANSPARENT
			dsFONT
			4
		)
		(Display
			{Shoot (when full)}
			dsCOORD
			75
			33
			dsCOLOUR
			1
; dark blue
			dsBACKGROUND
			clTRANSPARENT
			dsFONT
			4
		)
		(Display
			{Time:}
			dsCOORD
			217
			33
			dsCOLOUR
			1
; dark blue
			dsBACKGROUND
			clTRANSPARENT
			dsFONT
			4
		)
		(Display
			{Help}
			dsCOORD
			252
			8
			dsCOLOUR
			9
; blue
			dsBACKGROUND
			clTRANSPARENT
		)                                                                 ; dsFONT 4
		(Display
			{(Press Enter)}
			dsCOORD
			238
			18
			dsCOLOUR
			1
; dark blue
			dsBACKGROUND
			clTRANSPARENT
			dsFONT
			4
		)
		(gGame setCursor: gNormalCursor 1 229 17)
	)
)
                                                       ; sets cursor on the help button

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0
				(= cycles 35)
				(= gArcStl 1)  ; When (1) TRUE it removes the retype function of the spacebar (see User Script)
				(= gTimeCh 1)
			)                ; When (1) TRUE it disallows the player to change speed at all! (see Menubar)
			(1
				(= cycles 30)
				(city posn: (- (city x?) 1) 85)
			)                                   ; Adds to the illusion of motion with a slow city in the b/g
			(2 (RoomScript changeState: 0))
			(4
				(= cycles 2)    ; If player clicked "help" button or pressed ENTER
				(infoButton cel: 1)
			)                       ; Visually show the button press...this is all just for aesthetic appeal ;)
			(5
				(= cycles 1)
				(infoButton cel: 0)
			)                       ; Show button "pop up" after the 2 cycle push
			(6
				(Print 101 2 #title {Help}) ; Now actually show the player the instructions
				(RoomScript changeState: 0)
			)
		)
	)
	
; HANDLING KEYBOARD EVENTS TO MOVE NON-EGO VIEW
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if
				(and
					(> (pEvent x?) (infoButton nsLeft?))
					(< (pEvent x?) (infoButton nsRight?))
					(> (pEvent y?) (infoButton nsTop?))
					(< (pEvent y?) (infoButton nsBottom?))
				)                                            ; If clicked on Info button view
				(RoomScript changeState: 4)
			)
		)
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_RETURN)
				(RoomScript changeState: 4)
			)
			(if (== (pEvent message?) $0020)  ; If pressed the SPACEBAR
				(if (>= rLaserPower 8)  ; If power gauge is FULL
					(laser
						posn: 180 (- (boat y?) 2)
						show:
						cel: 0
						setCycle: End
					)                                                       ; posn the laser with the boat and show it
					(if (> (sharpRock1 x?) (boat x?)) ; If a rock is in FRONT of the boat...
						(if
							(and
								(> (+ (laser y?) 10) (sharpRock1 y?))
								(< (laser y?) (sharpRock1 y?))
							)                                                                    ; and the laser hits it...
							(ShakeScreen 1)     ; visually show the contact of laser and rock
							(sharpRock1
								view: 509
								cel: 0
								setCycle: End
								setMotion: MoveTo (sharpRock1 x?) (sharpRock1 y?)
							)                                                                                       ; Explode the Rock
							(= rockHit1 1) ; Makes sure a busted rock doesn't harm YOU!
							(if
								(and
									(> (+ (sharpRock1 x?) 15) (gargoyle x?))
									(< (- (sharpRock1 x?) 25) (gargoyle x?))
								)                                                                               ; Was the rock under the Beast?
								(-- rCompLife) ; Do the Beast some damage
								(gargFlight changeState: 5)
							)
						)
					)                                       ; Show the Beast in PAIN! gargFlight @ 338
					(if (> (sharpRock2 x?) (boat x?)) ; Same as all that up there, but for a different rock
						(if
							(and
								(> (+ (laser y?) 12) (sharpRock2 y?))
								(< (laser y?) (sharpRock2 y?))
							)
							(ShakeScreen 1)
							(sharpRock2
								view: 509
								cel: 0
								setCycle: End
								setMotion: MoveTo (sharpRock2 x?) (sharpRock2 y?)
							)
							(= rockHit2 1)
							(if
								(and
									(> (+ (sharpRock2 x?) 15) (gargoyle x?))
									(< (- (sharpRock2 x?) 25) (gargoyle x?))
								)
								(-- rCompLife)
								(gargFlight changeState: 5)
							)
						)
					)
					(if (> (sharpRock3 x?) (boat x?)) ; SAME ^^
						(if
							(and
								(> (+ (laser y?) 12) (sharpRock3 y?))
								(< (laser y?) (sharpRock3 y?))
							)
							(ShakeScreen 1)
							(sharpRock3
								view: 509
								cel: 0
								setCycle: End
								setMotion: MoveTo (sharpRock3 x?) (sharpRock3 y?)
							)
							(= rockHit3 1)
							(if
								(and
									(> (+ (sharpRock3 x?) 15) (gargoyle x?))
									(< (- (sharpRock3 x?) 25) (gargoyle x?))
								)
								(-- rCompLife)
								(gargFlight changeState: 5)
							)
						)
					)
					(increaseLaser changeState: 5)
				else                                ; Reduce the laser in increments, so it goes down visually nice and aesthetically pleasing!
					(increaseLaser changeState: 5)
				)
			)
		)                                           ; Reduces laser-power even if you didn't shoot...don't be trigger happy kids!
		(if (== (pEvent type?) evJOYSTICK)
			(if (== (pEvent message?) 1)    ; If pressed the UP arrow
				(if (not rMovingUp) ; if stationary or moving DOWN
					(boat setMotion: MoveTo 55 0)   ; Move boat UP
					(= rMovingUp 1) ; When TRUE, hitting up again will stop boat
					(= rMovingDown 0)
				else                ; If moving UP, obviously NOT moving DOWN
					(boat setMotion: MoveTo 55 (boat y?)) ; STOP the BOAT!!!
					(= rMovingUp 0)
				)
			)                       ; Since Stopped, not moving up
			(if (== (pEvent message?) 5)    ; If pressed the DOWN arrow
				(if (not rMovingDown)   ; ALL this is the same as the UP stuff, just completely opposite
					(boat setMotion: MoveTo 55 180)
					(= rMovingDown 1)
					(= rMovingUp 0)
				else
					(boat setMotion: MoveTo 55 (boat y?))
					(= rMovingDown 0)
				)
			)
		)
	)
	
; DOIT METHOD
	(method (doit &tmp dyingScript)
		(super doit:)
; UPDATING SCREEN INFO
		(playerLifeBar cel: rPlayerLife)    ; In the Doit Method this is updated every CYCLE, which is v quickly
		(powerBar cel: rLaserPower)         ; Measures and shows visually if laser gauge is FULL or 5/8 full or etc
		(compLifeBar cel: rCompLife)
		(if (<= rPlayerLife 0)  ; If you're life is out...
			(= dyingScript (ScriptID DYING_SCRIPT)) ; ...YOU DIE
			(dyingScript
				caller: 253
				register:
					{After coming this far, you die a miserable death in the cursed river. Too bad you had to take Julyn with you.}
			)
			(gGame setScript: dyingScript)
		)
		(if (<= rCompLife 0) ; If Opponent is out of LIFE pts...
			(= gTimeCh 0)
			(= gArcStl 0)
			(gGame restart:)
		)                           ; ...the game restarts? ...uh...This will have to change in development!
; HANDLING DAMAGE TO BOAT
		(if (<= (boat distanceTo: sharpRock1) 9) ; If Rock is too close...
			(if (not rockHit1)  ; ...and it hasn't already done you damage...
				(-- rPlayerLife) ; ...it deals you 1 damage
				(ShakeScreen 3)     ; Visually show the player his blunder
				(= rockHit1 1)
			)
		)                       ; But makes sure he doesn't have to pay more than 1 life pt for it.
		(if (<= (boat distanceTo: sharpRock2) 9) ; Same as ^^
			(if (not rockHit2)
				(-- rPlayerLife)
				(ShakeScreen 3)
				(= rockHit2 1)
			)
		)
		(if (<= (boat distanceTo: sharpRock3) 9) ; Yup...same^^
			(if (not rockHit3)
				(-- rPlayerLife)
				(ShakeScreen 3)
				(= rockHit3 1)
			)
		)
		(if (<= (boat distanceTo: bomb1) 7) ; Same except that these are fireballs, not rocks
			(if (not bombHit1)
				(-- rPlayerLife)
				(ShakeScreen 1)     ; Screen shakes less here, but damage is equal to RockHits
				(= bombHit1 1)
			)
		)
		(if (<= (boat distanceTo: bomb2) 7) ; ^^^^^^
			(if (not bombHit2)
				(-- rPlayerLife)
				(ShakeScreen 1)
				(= bombHit2 1)
			)
		)
; Thanks to Cloudee1 for this Timer - found at www.scicommunity.com
		(if (== countmilsec 0)
			(if (== countdownseconds 0)
				(if (== countdownminutes 0)
					(= dyingScript (ScriptID DYING_SCRIPT))
					(dyingScript
						caller: 253
						register:
							{You gave the necromancers enough time to seal you in the underworld forever. Next time, glance at your watch to make sure you will arrive on time.}
					)
					(gGame setScript: dyingScript)
				else
					(= countdownminutes (-- countdownminutes))
					(= countdownseconds 59)
					(WriteMin)
					(WriteSec)
					(= countmilsec milsec)
				)
			else
				(= countdownseconds (-- countdownseconds))
				(WriteSec)
				(= countmilsec milsec)
			)
		)
		(= countmilsec (-- countmilsec))
		; My litte addition so a zero can be used at times...not the best way...but decent enough...
		(if
		(and (<= countdownseconds 9) (> countdownseconds 0))
			(zeroTimer show:)
		else
			(zeroTimer hide:)
		)
	)
)

; EXTRA CHANGESTATE METHODS
(instance foreground of Script
	(properties)
	                            ; Create the illusion of foreground movement (at a fast pace)
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 6))
			(1 (= cycles 6))
			(2
				(= cycles 6)
				(rock1
					show:
					posn: 305 153
					xStep: 8
					setMotion: MoveTo 0 153
					setPri: 14
					ignoreActors:
					ignoreControl: ctlWHITE
				)
			)
			(3 (= cycles 6))
			(4
				(= cycles 6)
				(rock2
					posn: 305 154
					init:
					xStep: 8
					setMotion: MoveTo 0 154
					setPri: 14
					ignoreActors:
					ignoreControl: ctlWHITE
				)
			)
			(5 (= cycles 6))
			(6
				(= cycles 6)
				(rock3
					posn: 305 154
					init:
					xStep: 8
					setMotion: MoveTo 0 154
					setPri: 14
					ignoreActors:
					ignoreControl: ctlWHITE
				)
			)
			(7 (foreground changeState: 1))
		)
	)
)

(instance dangerRocks of Script
	(properties)
	                                ; regulates how 3 rocks come at you AGAIN and AGAIN and AGAIN...
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 6))
			(1
				(= cycles (Random 8 15))
				(= rockHit2 0)
				(= randoRock2 (Random 100 143))
				(sharpRock2
					init:
					view: 508
					posn: 305 randoRock2
					xStep: (Random 7 9)
					setCycle: Fwd
					setMotion: MoveTo 1 (- randoRock2 (Random 1 15))
					setPri: 14
					ignoreActors:
					ignoreControl: ctlWHITE
				)
			)
			(2 (= cycles 6))
			(3
				(= cycles (Random 8 15))
				(= rockHit3 0)
				(= randoRock3 (Random 100 130))
				(sharpRock3
					init:
					view: 508
					posn: 305 randoRock3
					xStep: (Random 6 8)
					setCycle: Fwd
					setMotion: MoveTo 1 (+ randoRock3 (Random 5 30))
					setPri: 14
					ignoreActors:
					ignoreControl: ctlWHITE
				)
			)
			(4 (= cycles 6))
			(5 (= cycles 7))
			(6
				(= cycles 6)
				(= rockHit1 0)
				(= randoRock1 (Random 0 1)) ; This on is either at the TOP or on the BOTTOM...but always FAST
				(if randoRock1
					(sharpRock1
						view: 508
						posn: 305 101
						xStep: 10
						setCycle: Fwd
						setMotion: MoveTo 1 101
						setPri: 14
						ignoreActors:
					)
				else
					(sharpRock1
						view: 508
						posn: 305 149
						xStep: 10
						setCycle: Fwd
						setMotion: MoveTo 1 149
						setPri: 14
						ignoreActors:
					)
				)
			)
			(7 (dangerRocks changeState: 1))
		)
	)
)

(instance increaseLaser of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 5))
			(1 (= cycles 4))
			(2
				(= cycles 3)
				(if (< rLaserPower 8) (++ rLaserPower))
			)
			(3
				(= cycles 4)
				(increaseLaser changeState: 1)
			)
			(5
				(= cycles 1)
				(if (> rLaserPower 0) (-- rLaserPower))
			)
			(6
				(= cycles 1)
				(if (> rLaserPower 0)
					(increaseLaser changeState: 5)
				else
					(increaseLaser changeState: 2)
				)
			)
		)
	)
)

(instance gargFlight of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 5))
			(1
				(= cycles (Random 25 40))
				(gargoyle
					view: 510
					setCycle: Fwd
					cycleSpeed: 3
					xStep: 2
					setMotion: MoveTo (Random 170 240) 93
				)
			)
			(2
				(= cycles (Random 20 40))
				(= randoBomb (Random 1 10))
				(if (> randoBomb 3)
					(= bombHit1 0)
					(= bombHit2 0)
					(bomb1
						posn: (- (gargoyle x?) 13) (- (gargoyle y?) 13)
						show:
						setCycle: Fwd
						xStep: 5
						yStep: 2
						setMotion: MoveTo 0 (Random 125 160)
					)
					(bomb2
						posn: (- (gargoyle x?) 13) (- (gargoyle y?) 13)
						show:
						setCycle: Fwd
						xStep: 4
						yStep: 2
						setMotion: MoveTo 0 (Random 105 135)
					)
				)
			)
			(3
				(= cycles 20)
				(gargFlight changeState: 1)
			)
			(5
				(= cycles 8)
				(gargoyle
					view: 511
					cycleSpeed: 3
					setMotion: MoveTo (gargoyle x?) (- (gargoyle y?) 1)
				)
			)
			(6
				(= cycles 1)
				(gargFlight changeState: 1)
			)
		)
	)
)


; Again, see www.scicommunity.com for the skinny on this code...in the sci syntax forum
(procedure (WriteMin &tmp [textstring 50])
	(Format @textstring {%2d} countdownminutes)
	(Display
		@textstring
		dsCOORD
		240
		33
		dsCOLOR
		9
		dsBACKGROUND
		0
		dsWIDTH
		14
	)
)

(procedure (WriteSec &tmp [textstring 50])
	(if (< countdownseconds 0) (= countdownseconds 0))
	(Format @textstring {%2d} countdownseconds)
	(Display
		@textstring
		dsCOORD
		256
		33
		dsCOLOR
		9
		dsBACKGROUND
		0
		dsWIDTH
		20
	)
)


(instance boat of Act
	(properties
		y 125
		x 55
		view 501
	)
)

(instance laser of Prop
	(properties
		y 125
		x 180
		view 504
		loop 2
	)
)

(instance lavaFlowLight of Prop
	(properties
		y 100
		x 152
		view 502
	)
)

(instance lavaFlowDark of Prop
	(properties
		y 125
		x 200
		view 502
		loop 1
	)
)

(instance rock1 of Act
	(properties
		y 153
		x 305
		view 505
	)
)

(instance rock2 of Act
	(properties
		y 153
		x 305
		view 506
	)
)

(instance rock3 of Act
	(properties
		y 153
		x 305
		view 507
	)
)

(instance sharpRock1 of Act
	(properties
		y 130
		x 305
		view 508
	)
)

(instance sharpRock2 of Act
	(properties
		y 150
		x 305
		view 508
	)
)

(instance sharpRock3 of Act
	(properties
		y 130
		x 305
		view 508
	)
)

(instance playerLifeBar of Prop
	(properties
		y 155
		x 10
		view 503
	)
)

(instance compLifeBar of Prop
	(properties
		y 155
		x 302
		view 503
		loop 1
	)
)

(instance zeroTimer of Prop
	(properties
		y 39
		x 257
		view 503
		loop 3
	)
)

(instance powerBar of Prop
	(properties
		y 47
		x 17
		view 503
		loop 2
	)
)

(instance distanceMarker of Prop
	(properties
		y 58
		x 155
		view 504
		loop 1
	)
)

(instance city of Prop
	(properties
		y 85
		x 177
		view 504
	)
)

(instance keys of Prop
	(properties
		y 42
		x 52
		view 502
		loop 2
		cel 1
	)
)

(instance infoButton of Prop
	(properties
		y 25
		x 227
		view 998
		loop 7
	)
)

(instance gargoyle of Act
	(properties
		y 93
		x 215
		view 510
	)
)

(instance bomb1 of Act
	(properties
		y 83
		x 170
		view 222
	)
)

(instance bomb2 of Act
	(properties
		y 0
		x 0
		view 222
	)
)
