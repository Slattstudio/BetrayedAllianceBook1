;;; Sierra Script 1.0 - (do not remove this comment)

(script# 19)
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
	rm019 0
)

(local
; Wizard's Magic Room (ALT)



	rando
	kicked =  0
	book =  0
	doorOpen =  0
	onRed =  0
)

(instance rm019 of Rm
	(properties
		picture 18
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 200)
		(switch gPreviousRoomNumber
			(else 
				(gEgo init: posn: 115 135 loop: 1)
				(RoomScript changeState: 3)
			)
		)
		(SetUpEgo)
		(gEgo init: observeControl: ctlMAROON)
		(portal init: ignoreActors: setPri: 1)
		(orb
			init:
			setCycle: Fwd
			cycleSpeed: 3
			setScript: crystalBall
		)
		(candleA
			init:
			setCycle: Fwd
			setScript: candleFlicker
			setPri: 9
		)
		(candleB init: setCycle: Fwd setPri: 9)
		(mask init: setPri: 10)
		(door init: setPri: 8 ignoreActors:)
		(if (not (gEgo has: INV_MARBLES))
			(planet
				init:
				ignoreActors:
				setPri: -1
				xStep: 2
				setCycle: Fwd
				setScript: orbit
			)
			(sun init: ignoreActors: setPri: -1 setScript: sunShot)
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			(1
				(gEgo hide:)
				(alterEgo
					init:
					view: 409
					loop: 1
					posn: 140 130
					setCycle: End RoomScript
					cycleSpeed: 2
					ignoreActors:
				)
			)
			(2
				(gEgo show: loop: 2)
				(alterEgo hide:)
			)
			(3
				(gEgo hide:)
				(alterEgo
					init:
					view: 17
					loop: 0
					cel: 10
					posn: 115 135
					setCycle: Beg RoomScript
					cycleSpeed: 2
					ignoreActors:
				)
			)
			(4
				(gEgo show: loop: 2)
				(alterEgo hide:)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) (sun nsLeft?))
						(< (pEvent x?) (sun nsRight?))
						(> (pEvent y?) (sun nsTop?))
						(< (pEvent y?) (sun nsBottom?))
					)
					(if (gEgo has: 9) else (Print 18 5))
				)
				(if
					(and
						(> (pEvent x?) (orb nsLeft?))
						(< (pEvent x?) (orb nsRight?))
						(> (pEvent y?) (orb nsTop?))
						(< (pEvent y?) (orb nsBottom?))
					)
					(Print 19 0)
				)
; All I can see in it is my reflection.
				(if
					(and
						(> (pEvent x?) (mask nsLeft?))
						(< (pEvent x?) (mask nsRight?))
						(> (pEvent y?) (mask nsTop?))
						(< (pEvent y?) (mask nsBottom?))
					)
					(Print 19 1)
				)
; That is one ugly mask.
				(if
					(and
						(> (pEvent x?) 90)      ; Carpet on wall
						(< (pEvent x?) 128)
						(> (pEvent y?) 37)
						(< (pEvent y?) 100)
					)
					(Print 18 6)
				)
				(if
					(and
						(> (pEvent x?) 277)      ; books on shelf
						(< (pEvent x?) 294)
						(> (pEvent y?) 107)
						(< (pEvent y?) 130)
					)
					(if (< book 35)
						(Print 18 book)
						(++ book)
					else
						(= book 30)
						(Print 18 book)
						(++ book)
					)
				)
				(if
					(and
						(> (pEvent x?) 148)      ; Mirror
						(< (pEvent x?) 183)
						(> (pEvent y?) 68)
						(< (pEvent y?) 121)
					)
					(Print 18 7)
				)
			)
		)
		(if (Said 'open/door')
			(if (not doorOpen)
				(if (<= (gEgo distanceTo: door) 35)
					(door setCycle: End)
					(gEgo ignoreControl: ctlMAROON)
					(= doorOpen 1)
				else
					(PrintNCE)
				)
			else
				(Print 19 2)
			)
		)
	)
	
; It already is.
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) (| $0100 $0080))
			(gEgo mirrorEgo: 123)
		)
		(if (not (& (gEgo onControl:) (| $0100 $0080)))
			(gEgo mirrorEgoDispose:)
		)
		(if (== (gEgo onControl:) $1000)      ; RED
			(if onRed
			else
				(ShakeScreen 4)
				(Print 18 42 #width 280 #at -1 8)
				(= onRed 1)
			)
		else
			(= onRed 0)
		)
		(if (not kicked)
			(if (<= (gEgo distanceTo: sun) 9)
				(sunShot changeState: 2)
			)
		)
		(if (>= (gEgo distanceTo: door) 35)
			(if doorOpen
				(door setCycle: Beg)
				(= doorOpen 0)
				(gEgo observeControl: ctlMAROON)
			)
		)
	)
)

(instance sunShot of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(2
				(= cycles 10)
				(= kicked 1)
				(= rando (Random 35 55))
				(cond 
					((> (gEgo x?) (sun x?))    ; Ego on the Right
						(if (> (sun x?) 50)
							(sun
								setCycle: Walk
								xStep: 5
								setMotion: MoveTo (- (sun x?) rando) (sun y?) sunShot
							)
						else
							(sun
								setCycle: Walk
								xStep: 5
								setMotion: MoveTo (+ (sun x?) rando) (sun y?) sunShot
							)
						)
					)
					((> (sun x?) 265) ; Ego on the Left
						(sun
							setCycle: Walk
							xStep: 5
							setMotion: MoveTo (- (sun x?) rando) (sun y?) sunShot
						)
					)
					(else
						(sun
							setCycle: Walk
							xStep: 5
							setMotion: MoveTo (+ (sun x?) rando) (sun y?) sunShot
						)
					)
				)
			)
			(3 (sun cel: 0) (= kicked 0))
		)
	)
)

(instance orbit of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
				(= cycles 15)
				(planet
					setMotion: MoveTo (+ (sun x?) 2) (+ (sun y?) 8) orbit
				)
			)
			(1
				(= cycles 15)
				(planet
					setMotion: MoveTo (- (sun x?) 4) (+ (sun y?) 7) orbit
				)
			)
			(2
				(= cycles 15)
				(planet
					setMotion: MoveTo (- (sun x?) 12) (+ (sun y?) 4) orbit
				)
			)
			(3
				(= cycles 15)
				(planet
					setMotion: MoveTo (- (sun x?) 20) (- (sun y?) 2) orbit
				)
			)
			(4
				(= cycles 15)
				(planet
					setMotion: MoveTo (- (sun x?) 12) (- (sun y?) 8) orbit
				)
			)
			(5
				(= cycles 15)
				(planet
					setMotion: MoveTo (- (sun x?) 4) (- (sun y?) 11) orbit
				)
			)
			(6
				(= cycles 15)
				(planet
					setMotion: MoveTo (+ (sun x?) 2) (- (sun y?) 12) orbit
				)
			)
			(7
				(= cycles 15)
				(planet
					setMotion: MoveTo (+ (sun x?) 8) (- (sun y?) 11) orbit
				)
			)
			(8
				(= cycles 15)
				(planet
					setMotion: MoveTo (+ (sun x?) 16) (- (sun y?) 8) orbit
				)
			)
			(9
				(= cycles 15)
				(planet
					setMotion: MoveTo (+ (sun x?) 24) (- (sun y?) 2) orbit
				)
			)
			(10
				(= cycles 15)
				(planet
					setMotion: MoveTo (+ (sun x?) 16) (+ (sun y?) 4) orbit
				)
			)
			(11
				(= cycles 15)
				(planet
					setMotion: MoveTo (+ (sun x?) 8) (+ (sun y?) 7) orbit
				)
			)
			(12
				(= cycles 15)
				(orbit changeState: 0)
			)
		)
	)
)

(instance crystalBall of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles (Random 50 120)))
			(1
				(= cycles 15)
				(orb loop: 1 cel: 0 setCycle: End RoomScript)
			)
			(2
				(orb loop: 0 setCycle: Fwd)
				(crystalBall changeState: 0)
			)
		)
	)
)

(instance candleFlicker of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
				(= cycles (Random 10 30))
				(candleA cycleSpeed: (Random 1 3))
				(candleB cycleSpeed: (Random 1 3))
			)
			(1
				(= cycles (Random 5 15))
				(candleA cycleSpeed: (Random 1 4))
			)
			(2
				(= cycles (Random 5 15))
				(candleB cycleSpeed: (Random 1 4))
			)
			(3
				(candleFlicker changeState: 0)
			)
		)
	)
)

(instance alterEgo of Act
	(properties
		y 66
		x 166
		view 313
	)
)

(instance door of Prop
	(properties
		y 155
		x 50
		view 36
	)
)

(instance planet of Act
	(properties
		y 170
		x 235
		view 20
	)
)

(instance sun of Act
	(properties
		y 160
		x 230
		view 21
	)
)

(instance portal of Prop
	(properties
		y 138
		x 108
		view 16
		loop 1
	)
)

(instance orb of Prop
	(properties
		y 128
		x 74
		view 18
	)
)

(instance mask of Prop
	(properties
		y 63
		x 164
		view 18
		loop 2
	)
)

(instance candleA of Prop
	(properties
		y 107
		x 195
		view 250
		loop 1
	)
)

(instance candleB of Prop
	(properties
		y 107
		x 237
		view 250
		loop 1
	)
)
