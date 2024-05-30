;;; Sierra Script 1.0 - (do not remove this comment)
; + 5 SCORE
(script# 37)
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
	rm037 0
)

(local

; Booby-Trapped Cave



	hasTorch =  0
	eyeOpen =  0
	onSilver =  0
	onGrey =  0
	onFire =  0
	faceBlocked =  0
	inHole =  0
	dying =  0
	top =  0
	message =  0
	arrowHit =  0
	arrowShot =  0
	arrowOnWall =  0
	ropeSwing =  0
	trigger =  0
	runningToDeath =  0
	swinging =  0
	bowOnActive =  0
	statueHasArrow =  1
	
	ignoreTrap = 0
)
; bowOnFloor = 50 // countdown every cycle, when 0, shakescreen to show the trap is being tripped.

(instance rm037 of Rm
	(properties
		picture scriptNumber
		north 0
		east 71
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 204)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 300 170 loop: 1)
			)
			(38
				(gEgo posn: 24 156 loop: 2)
				(= hasTorch 1)
				(torch loop: 6)
			)
		)
		(SetUpEgo)
		(= gEgoRunning 0)
		(RunningCheck)
		
		(gEgo init:)
		(glow init: setCycle: Fwd cycleSpeed: 4 hide: setPri: 1)
		(glow2 init: setCycle: Fwd cycleSpeed: 4 hide: setPri: 0)
		(face
			init:
			setCycle: Fwd
			cycleSpeed: 2
			setScript: flameThrow
		)
		(torch
			init:
			setCycle: Fwd
			cycleSpeed: 2
			setScript: fallDownHole
		)
		(statue1 init: setScript: moveStatue)
		(statue2 init: setScript: arrowScript)
		(bow init: ignoreActors: setPri: 1 setPri: 4)
		(rope init: ignoreActors: setPri: 3)
		
		(ropeTop init: ignoreActors:)
		(alterEgo
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
		)
		(arrow
			init:
			hide:
			yStep: 4
			ignoreActors:
			ignoreControl: ctlWHITE
		)
		(tileTrapBroken
			init:
			setPri: 0
			cel: 1
			ignoreActors:
			setPri: 3
		)
		(tileTrapActive init: setPri: 0 ignoreActors: setPri: 3)
		
		; if Ego has the arrow
		(if (gEgo has: 16)
			(statue2 cel: 1)
			(= statueHasArrow 0)
		)
		; if the statue shot and the arrow is in the wall
		(if (== (IsOwnedBy 16 37) TRUE)
			(arrow show: posn: 180 184)
			(statue2 cel: 1)
			(= arrowOnWall 1)
			(= arrowShot 1)
			(= statueHasArrow 0)
		)
		; if Ego has shot the arrow
		(if (== (IsOwnedBy 16 38) TRUE)
			(statue2 cel: 1)
			(rope init: cel: 2)
			(ropeTop
				init:
				x: (+ (ropeTop x?) 7)
				loop: 4
				setCycle: Fwd
				cycleSpeed: 4
			)
			(= ropeSwing 1)
			(= arrowShot 1)
			(= statueHasArrow 0)
		)
		; if ego has bow or it's been dropped off in the next room
		(if
		(or (gEgo has: 15) (if (== (IsOwnedBy 15 38) TRUE)))
			(bow hide:)
		)
		(if g37Statue
			(= faceBlocked 1)
			(statue1 posn: (+ (face x?) 2) (statue1 y?))
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(PC) ; ProgramControl() (SetCursor(997 HaveMouse()) = gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo (+ (bow x?) 15) (- (bow y?) 2) RoomScript
					ignoreControl: ctlWHITE
				)
			)
			(2       ; pick-up animation
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					loop: 1
					posn: (gEgo x?) (gEgo y?)
					setCycle: End RoomScript
					cycleSpeed: 2
				)
			)
			(3
				(Print 37 41 #icon 267 #title {Golden Bow})
				(gEgo get: 15)
				(bow hide:)
				(alterEgo setCycle: Beg RoomScript)
			)
			(4
				(alterEgo hide:)
				(gEgo show: loop: 1 observeControl: ctlWHITE)
				(PlC)
			)         ; PlayerControl() (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
			; return without torch
			(5
				(PC) ; ProgramControl()(SetCursor(997 HaveMouse()) = gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo (gEgo x?) (+ (gEgo y?) 15) self
					ignoreControl: ctlWHITE
				)
			)
			(6 (= cycles 4))
			(7 (= cycles 4) (Print 37 17))
			(8
				(PlC) ; PlayerControl() (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
				(gEgo observeControl: ctlWHITE)
			)
			; rope being severed
			(9
				(PC) ; ProgramControl()(SetCursor(997 HaveMouse()) = gCurrentCursor 997)
				(gEgo setMotion: MoveTo 140 169 self)
			)
			(10
				(= cycles 2)
				(gEgo loop: 1)
			)
			(11
				(gEgo hide:)
				(alterEgo
					show:
					view: 407
					loop: 6
					posn: (gEgo x?) (gEgo y?)
					setCycle: End self
					cycleSpeed: 4
				)
			)
			(12
				(alterEgo loop: 7)
				(arrow
					init:
					show:
					view: 56
					posn: (gEgo x?) (- (gEgo y?) 20)
					xStep: 10
					setMotion: MoveTo 1 130 self
				)
			)
			(13 (= cycles 1))
			(14
				(alterEgo hide:)
				(gEgo
					show:
					posn: (+ (gEgo x?) 4) (- (gEgo y?) 2)
					put: 16 38
				)
				(rope setCycle: End cycleSpeed: 2)
				(ropeTop
					x: (+ (ropeTop x?) 7)
					loop: 4
					setCycle: Fwd
					cycleSpeed: 4
				)
				(PlC) ; PlayerControl() (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
				(= ropeSwing 1)
			)
			; walking over trap
			(15
				(PC)
				(= ignoreTrap 1)
				(if (& (gEgo onControl:) ctlGREEN)
					(gEgo setMotion: MoveTo 147 165 self)
				else
					(gEgo setMotion: MoveTo 184 165 self)
				)	
			)
			(16
				(PlC)
				(= ignoreTrap 0)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (checkEvent pEvent face)
					(if eyeOpen (PrintOther 37 1) else (PrintOther 37 0))
; There's a strange face on the wall.
				)
; There's a strange face on the wall.
				(if (checkEvent pEvent statue1) (PrintOther 37 18)) ; #at -1 28)
				(if
				(or (checkEvent pEvent torch) (checkEvent pEvent glow))
					(if (not hasTorch) (PrintOther 37 50))
				)                         ; #at -1 28)
				(if (checkEvent pEvent statue2)
					(if arrowShot
						(PrintOther 37 19)
					else                  ; #at -1 28)
						(PrintOther 37 20)
					)
				)                         ; #at -1 28)
				(if (checkEvent pEvent bow)
					(if
					(or (gEgo has: 15) (if (== (IsOwnedBy 16 38) TRUE)))
					; Print(37 21 #at -1 28)
					else
						(PrintOther 37 21)
					)
				)                         ; #at -1 28)
				; hole in the ground
				(if (checkBox pEvent 77 117 152 188) (PrintOther 37 22)) ; #at -1 28)
				; ground smoothed over
				(if (checkBox pEvent 193 236 151 155)
					(PrintOther 37 23)
				)                     ; #at -1 28)
				; rope Hook
				(if (checkBox pEvent 95 103 48 61) (PrintOther 37 48)) ; #at -1 28)
				; rope wound around metal
				(if (checkBox pEvent 60 71 136 147) (PrintOther 37 49)) ; #at -1 28)
				; passageway
				(if (checkBox pEvent 10 37 94 140) (PrintOther 37 38))
			)
		)                             ; #at -1 28)
		(if (Said 'open,close/face,furnace') (PrintOther 37 24)) ; #width 280 #at -1 8)
		(if (Said 'search/statue')
			(cond 
				((<= (gEgo distanceTo: statue1) 20) (PrintOther 37 32))
				((<= (gEgo distanceTo: statue2) 20) ; #width 280 #at -1 8)
					(if arrowShot
						(PrintOther 37 33)
					else                  ; #width 280 #at -1 8)
						(PrintOther 37 34)
					)
				)
				(else (PrintNCE))         ; #width 280 #at -1 8)
			)
		)
		(if (Said 'look>')
			(if (Said '/(torch,light,candle,lantern)')
				(PrintOther 37 24)
			)                     ; #width 280 #at -1 8)
			(if (Said '/face,furnace')
				(if eyeOpen (PrintOther 37 1) else (PrintOther 37 0)) ; #at -1 28)
			)                        ; #at -1 28)
			(if (Said '/floor,ground') (PrintOther 37 37)) ; #width 280 #at -1 8)
			(if (Said '/plate,tile,trap,button') (PrintOther 37 42)) ; #width 280 #at -1 8)
			(if (Said '/darkness,tunnel,path') (PrintOther 37 38)) ; #width 280 #at -1 8)
			(if (Said '/statue,suit,armor') 
				; has the arrow been shot?
				(if (or (gEgo has: 16)
					(== (IsOwnedBy 16 37) TRUE)
					(== (IsOwnedBy 16 38) TRUE))
					(PrintOther 37 58)
				else
					(PrintOther 37 25)
				)
			) 
			(if (Said '/wall') (PrintOther 37 35)) ; #width 280 #at -1 8)
			(if (Said '/hole,chasm') (PrintOther 37 26)) ; #width 280 #at -1 8)
			(if (Said '/arrow,bow') (PrintOther 37 27)) ; #width 280 #at -1 8)
			(if (Said '/rope') (PrintOther 37 28)) ; #width 280 #at -1 8)
			(if (Said '[/!*]') (PrintOther 37 30))
			; this will handle just "look" by itself ; #width 280 #at -1 8)
		)                         ; #width 280 #at -1 8)
		(if (Said '(pick<up),take>')
			(if (Said '/(torch,light,candle,lantern,fire)')
				(if (not hasTorch)
					(if (<= (gEgo distanceTo: torch) 40)
						(PrintOK)
						(= hasTorch 1)
						(torch loop: 6)
					else
						(PrintNCE)
					)
				else
					(PrintOther 37 1)
				)
			)
; You already have it.
			(if (Said '/bow')
				(if (not (gEgo has: 15))
					(if (<= (gEgo distanceTo: bow) 40)
						(RoomScript changeState: 1)
					else
						(PrintNCE)
					)
				else
					(PrintOther 37 45)
				)
			)
			(if (Said '/arrow')
				(if (not (gEgo has: 16))
					(cond 
						((== (IsOwnedBy 16 38) TRUE) (PrintOther 37 47))
						((<= (gEgo distanceTo: arrow) 20)
							(if arrowOnWall
								(Print 37 3 #icon 268 #title {Golden Arrow})
; You pull the arrow vigorously from the wall. The tip is still relatively sharp!
								(gEgo get: 16)
								(arrow hide:)
							else
								(Print 37 4)
							)
						)
						(else (PrintNCE))
; The statue's grip is too strong.
					)
				else                       ; not close enough
					(PrintOther 37 1)
				)
			)
		)
; You already have it.
		(if (Said 'put,place,return,replace/torch,fire')
			(if hasTorch
				(if (<= (gEgo distanceTo: torch) 40)
					(PrintOK)
					(= hasTorch 0)
					(torch loop: 5 setCycle: Fwd)
				else
					(PrintNCE)
				)
			else
				(PrintOther 37 6)
			)
		)
; You don't have it.
		(if (Said 'slide,throw/torch')
			(if hasTorch (PrintOther 37 53) else (PrintDHI))
		)
		(if (Said 'crawl,slide') (PrintOther 37 54))
		(if (Said 'push,move,slide/statue,armor,statue')
			(if (not faceBlocked)
				(if (<= (gEgo distanceTo: statue1) 20)
					(moveStatue changeState: 1)
				else
					(PrintNCE)
				)
			else
				(PrintOther 37 7)
			)
		)
; There is no reason to do that now.
		(if
			(or
				(Said
					'disarm,trip,trigger,activate/trap,switch,button,plate'
				)
				(Said 'put,drop,use/bow/trap,switch,button,plate,ground')
				(Said 'drop/bow')
			)
			(if (gEgo has: 15)     ; bow
				(if statueHasArrow
					(if (& (gEgo onControl:) ctlGREEN)
						(moveStatue changeState: 5)
					else
						(PrintNCE)
					)
				else
					(PrintOther 37 44)
				)
			else                      ; It's already disarmed.
				(PrintOther 37 8)
			)
		)
; You need something both long and heavy to trip the trap.
		(if
			(or
				(Said 'shoot,fire/(arrow,bow,target)')
				(Said 'use/bow')
				(Said 'shoot/rope')
			)
			(if (gEgo has: 15)     ; bow
				(if (gEgo has: 16)     ; arrow
					(self changeState: 9)
				else
					(PrintOther 37 9)
				)
			else
; You don't have any arrows.
				(PrintOther 37 10)
			)
		)
; You don't have a bow, not to mention any arrows.
		(if (or (Said 'take/rope') (Said 'swing'))
			(if ropeSwing
				(if (& (gEgo onControl:) ctlRED)
					(fallDownHole changeState: 4)
				else
					(PrintNCE)
				)
			else
				(PrintOther 37 11)
			)
		)
; You can't reach it from there.
		(if (Said 'cut/rope')
			(if ropeSwing
				(PrintOther 37 51)
			else
				(PrintOther 37 52)
			)
		)
		(if (Said '(walk,step)<over/trap,plate,switch,button')
			(if (or (& (gEgo onControl:) ctlGREEN) (& (gEgo onControl:) ctlCYAN))
				(PrintOther 37 55)
				(self changeState: 15)
			else
				(PrintNCE)
			)	
		)
		(if (Said 'jump')
			(if (& (gEgo onControl:) ctlRED)
				(PrintOther 37 39)
			else
				(if (or (& (gEgo onControl:) ctlGREEN) (& (gEgo onControl:) ctlCYAN))
					(PrintOther 37 55)
					(self changeState: 15)
				else
					(PrintOther 37 40)
				)
			)
		)
		(if (or (Said 'look,use,read,open/portal,map')
				(Said 'map'))
			(Print 0 88)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (not (gEgo has: INV_BOW)) (tileTrapBroken cel: 1))
; (if(== bowOnFloor 0)
;                ShakeScreen(1)
;                = bowOnFloor 50
;            )(else
;                --bowOnFloor
;            )
		(cond 
			((not swinging)
				(cond 
					((not dying)
						(if hasTorch
							(if onFire
								(if inHole
									(glow hide:)
									(glow2 hide:)
								else
									(glow
										show:
										posn: (- (alterEgo x?) 4) (- (alterEgo y?) 3)
										ignoreActors:
									)
									(glow2
										show:
										posn: (- (alterEgo x?) 4) (+ (alterEgo y?) 10)
										ignoreActors:
									)
								)
							else
								(switch (gEgo loop?)
									(0 ; faceing right
										(glow
											show:
											posn: (+ (gEgo x?) 10) (- (gEgo y?) 10)
											ignoreActors:
										)
										(glow2
											show:
											posn: (+ (gEgo x?) 10) (+ (gEgo y?) 3)
											ignoreActors:
										)
									)
									(1 ; facing left
										(glow
											show:
											posn: (- (gEgo x?) 10) (- (gEgo y?) 10)
											ignoreActors:
										)
										(glow2
											show:
											posn: (- (gEgo x?) 10) (+ (gEgo y?) 3)
											ignoreActors:
										)
									)
									(else 
										(glow
											show:
											posn: (gEgo x?) (- (gEgo y?) 10)
											ignoreActors:
										)
										(glow2
											show:
											posn: (gEgo x?) (+ (gEgo y?) 3)
											ignoreActors:
										)
									)
								)
							)
						else
							(glow
								show:
								posn: (- (torch x?) 2) (+ (torch y?) 11)
								ignoreActors:
							)
							(glow2
								show:
								posn: (- (torch x?) 2) (+ (torch y?) 24)
								ignoreActors:
							)
						)
					)
					(hasTorch (glow hide:) (glow2 hide:))
				)
			)
			(hasTorch (glow hide:) (glow2 hide:))
		)
		(if (& (gEgo onControl:) ctlBROWN)      ; The hole
			(if (not dying) (fallDownHole changeState: 1))
		)
		(if (& (gEgo onControl:) ctlFUCHSIA)      ; To eastern caves
			(if	(gEgo has: INV_BOW)
				; leave bow
				(PrintOther 37 59)
				(gEgo put: INV_BOW 37)  
			)
			(if	hasTorch
				; leave torch
				(PrintOther 37 60)  
			)
			(gRoom newRoom: 71)
		)
		(cond 
			((& (gEgo onControl:) ctlNAVY) (tileTrapBroken cel: 1)) ; The broken tile Trap
			((gEgo has: INV_BOW) (tileTrapBroken cel: 0))
		)
		(if (not ignoreTrap)
			(if (or (& (gEgo onControl:) ctlBLUE) bowOnActive) ; The trigger for the arrow to shoot
				(tileTrapActive cel: 1)
				; (if(& (alterEgo:onControl()) ctlBLUE) // The trigger for the arrow to shoot
				(if statueHasArrow
					(cond 
						((> arrowShot 0)
							(if (== arrowShot 1)
								(if trigger (++ arrowShot) (arrowScript changeState: 1))
							)
						)
						((not trigger) (arrowScript changeState: 10))
					)
				)
			else
				(tileTrapActive cel: 0)
			)
		)	
		(if (& (gEgo onControl:) ctlMAROON)      ; The go between from top screen to bottom
			(cond 
				(hasTorch (gRoom newRoom: 38))
				((not message) (self changeState: 5))
			)
		)
		(cond 
			(hasTorch
				(cond 
					((and (> (gEgo x?) 210) (< (gEgo x?) 260)) ; Opens eyes for player to see trap
						(cond 
							((and (> (gEgo x?) 230) (< (gEgo x?) 240)) ; furnace blows fire
								(if (not onSilver)
									(if eyeOpen
										(if (not faceBlocked) (flameThrow changeState: 2))
									)
								)
							)
							((not faceBlocked)
								(if (not onGrey)
									(face loop: 2 setCycle: End cycleSpeed: 2)
									(= eyeOpen 1)
									(= onGrey 1)
								)
							)
						)
					)
					(onGrey (faceChange changeState: 2) (= eyeOpen 0) (= onGrey 0))
				)
			)
			(onGrey (faceChange changeState: 2) (= eyeOpen 0) (= onGrey 0))
		)
	)
)

(instance flameThrow of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(1)
			(2
				(= cycles 2)
				(PC) ; ProgramControl()(SetCursor(997 HaveMouse()) = gCurrentCursor 997)
				(fire init: show: ignoreActors:)
				(= onSilver 1)
			)
			(3
				(= onFire 1)
				(fire loop: 4 setCycle: Fwd cycleSpeed: 2)
				(gEgo hide:)
				(alterEgo
					init:
					show:
					view: 231
					posn: (gEgo x?) (gEgo y?)
					setCycle: Walk
					ignoreControl: ctlWHITE
					ignoreActors:
					xStep: 5
					setMotion: MoveTo 115 165 flameThrow
				)
				(= runningToDeath 1)
			)
			(4
				(= cycles 9)
				(alterEgo
					view: 123
					yStep: 5
					setMotion: MoveTo 110 189 flameThrow
				)
			)
			(5
				(= cycles 2)
				(alterEgo hide:)
				(= inHole 1)
				(PlC)
			)         ; PlayerControl() (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
			(6
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 606
					register:
						{That just goes to show that you can be 'on fire' and 'hit rock bottom' at the same time!}
				)
				(gGame setScript: dyingScript)
			)
		)
	)
)

(instance faceChange of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1)
			(2 (face loop: 2 setCycle: Beg))
			(3 (face loop: 1 setCycle: Fwd))
		)
	)
)

(instance fallDownHole of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(1
				;(= cycles 6)
				(= dying 1)
				(alterEgo
					show:
					posn: (gEgo x?) (gEgo y?)
					yStep: 5
					setCycle: End
				)
				(if (> (gEgo x?) 100)
					(alterEgo view: 123 setMotion: MoveTo 110 189 self)
				else
					(alterEgo view: 124 setMotion: MoveTo 80 189 self)
				)
				(gEgo hide:)
				;(= hasTorch 0)
			)
			(2
				(= cycles 2)
				(alterEgo hide:)
				(= inHole 1)
				(PlC)
			)         ; PlayerControl() (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
			(3
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 606
					register:
						{Watch out hero! You can hollow out your niche later! After all, there's a hole lot cleft for you to do!}
				)
				(gGame setScript: dyingScript)
			)
			; swing over the hole
			(4
				(PC) ; ProgramControl()(SetCursor(997 HaveMouse()) = gCurrentCursor 997)
				(if (> (gEgo x?) 90)
					(gEgo
						setMotion: MoveTo 136 169 self
						ignoreControl: ctlWHITE
					)
				else
					(gEgo
						setMotion: MoveTo 56 167 self
						ignoreControl: ctlWHITE
					)
				)
			)
			(5
				(gEgo hide:)
				(= swinging 1)
				(ropeTop hide:)
				(if (> (gEgo x?) 90)
					(alterEgo
						show:
						posn: 94 170
						view: 109
						loop: 0
						cel: 0
						setCycle: End self
						cycleSpeed: 3
					)
				else
					(alterEgo
						show:
						posn: 94 170
						view: 109
						cel: 0
						loop: 2
						setCycle: End self
						cycleSpeed: 3
					)
				)
			)
			(6
				(if (> (gEgo x?) 90)
					(alterEgo loop: 1 cel: 0 setCycle: End self)
				else
					(alterEgo loop: 3 cel: 0 setCycle: End self)
				)
			)
			(7
				(PlC) ; PlayerControl() (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
				(alterEgo hide:)
				(ropeTop show: loop: 4 setCycle: Fwd cycleSpeed: 4)
				(= swinging 0)
				(if (> (gEgo x?) 90)
					(gEgo
						show:
						posn: 54 167
						setMotion: MoveTo 50 167
						loop: 1
						observeControl: ctlWHITE
					)
				else
					(gEgo
						show:
						posn: 136 167
						setMotion: MoveTo 141 167
						loop: 0
						observeControl: ctlWHITE
					)
				)
				(if (not g37Complete)
					(gGame changeScore: 5)
					(= g37Complete 1)
				)
			)
		)
	)
)

(instance moveStatue of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(PC) ; ProgramControl()(SetCursor(997 HaveMouse()) = gCurrentCursor 997)
				(gEgo setMotion: MoveTo 177 162 self)
			)
			(2
				; Moving the statue in front of the furnace
				(gEgo setMotion: MoveTo (- (face x?) 15) 164 self)
				(statue1
					setMotion: MoveTo (+ (face x?) 2) (statue1 y?)
					ignoreControl: ctlWHITE
				)
				(= faceBlocked 1)
				(= g37Statue 1)
				
			)
			(3 (PlC) ; PlayerControl() (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
			; disarming the arrow trap
				(PrintOther 37 57)
			)
			(5
				(PC) ; ProgramControl()(SetCursor(997 HaveMouse()) = gCurrentCursor 997)
				(gEgo setMotion: MoveTo 193 171 self)
				(= statueHasArrow 0)
			)
			(6 (= cycles 3) (gEgo loop: 2))
			(7
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					loop: 1
					posn: (gEgo x?) (gEgo y?)
					setCycle: End self
					cycleSpeed: 3
				)
			)
			(8
				(= cycles 7)
				(bow
					show:
					posn: (- (alterEgo x?) 26) (- (alterEgo y?) 4)
				)
				(= arrowHit 0)
				(= bowOnActive 1)
				(arrowScript changeState: 1)
			)
			(9
				(alterEgo setCycle: Beg self)
				(bow hide:)
				(= bowOnActive 0)
			)
			(10
				(alterEgo hide:)
				(gEgo show:)
			)
		)
	)
)

(instance arrowScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(1
				(= trigger 0)
				; (ShakeScreen(1))
				(= arrowShot 1)
				(PC) ; ProgramControl()(SetCursor(997 HaveMouse()) = gCurrentCursor 997)
				(statue2 setCycle: End arrowScript)
				(gTheSoundFX number: 204 play:)
			)
			(2
				(if (not arrowHit)
					(arrow
						init:
						posn: (+ (statue2 x?) 5) (- (statue1 y?) 20)
						show:
						yStep: 6
						setMotion: MoveTo 180 184 self
						ignoreActors:
						ignoreControl: ctlWHITE
					)
				else
					(arrow
						init:
						posn: (+ (statue2 x?) 5) (- (statue1 y?) 20)
						show:
						yStep: 6
						xStep: 4
						setMotion: MoveTo (gEgo x?) (- (gEgo y?) 10) self
						ignoreActors:
						ignoreControl: ctlWHITE
					)
				)
			)
			; (self:cycles(9))
			(3
				(if (not arrowHit)
					(PlC) ; PlayerControl() (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
					(= arrowOnWall 1)
					; put the arrow into room 37 to keep track of its state
					(gEgo get: 16)
					(gEgo put: 16 37)
					(Print 37 43 #at -1 8 #width 280)
					(self cycles: 1)
				else
					(Print 37 13)
; Ouch! The arrow caught you in a very unpleasant area!
					(arrow hide:)
					(if (not onFire)
						(gEgo hide:)
						(alterEgo
							init:
							show:
							view: 231
							posn: (gEgo x?) (gEgo y?)
							setCycle: Walk
							ignoreControl: ctlWHITE
							ignoreActors:
							xStep: 5
							setMotion: MoveTo 126 165 self
						)
						(= runningToDeath 1)
					)
				)
			)
			(4
				(= cycles 9)
				(if arrowHit
					(alterEgo
						view: 123
						yStep: 5
						setMotion: MoveTo 110 189 self
					)
				)
			)
			(5
				(= cycles 2)
				(if arrowHit (= inHole 1) (PlC) (alterEgo hide:)) ; PlayerControl() (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
			)
			(6
				(if arrowHit
					(if (not onFire)
						(= dyingScript (ScriptID DYING_SCRIPT))
						(dyingScript
							caller: 606
							register: {You've hit rock bottom!}
						)
						(gGame setScript: dyingScript)
					)
				)
			)
			(7
				(PC) ; ProgramControl()(SetCursor(997 HaveMouse()) = gCurrentCursor 997)
				(gEgo setMotion: MoveTo 140 169 arrowScript)
			)
			(8
				(gEgo hide:)
				(alterEgo
					posn: (gEgo x: (gEgo y?))
					view: 407
					setCycle: End arrowScript
				)
			)
			(9
				(alterEgo loop: 7)
				(arrow
					view: 56
					posn: (gEgo x?) (gEgo y?)
					setMotion: MoveTo 1 90
				)
				; rope cut animation
				(PlC)
			)         ; PlayerControl() (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
			(10
				(= cycles 7)
				(= trigger 1)
				; (ShakeScreen(1))
				(PC)
			)        ; ProgramControl()(SetCursor(997 HaveMouse()) = gCurrentCursor 997)
			(11
				(if (not runningToDeath) (PlC)) ; PlayerControl() (SetCursor(999 HaveMouse()) = gCurrentCursor 999)
				; Print(37 14) /* This trap must be broken. */
				(++ arrowShot)
				(= arrowHit 1)
			)
		)
	)
)

; (case 7 // Ego hit with arrow
;                (send gEgo:hide())
;                (alterEgo:init()show()view(231)posn((send gEgo:x)(send gEgo:y))setCycle(Walk)ignoreControl(ctlWHITE)ignoreActors()xStep(5)setMotion(MoveTo 115 165 arrowScript))
;            )
(procedure (PC)
	(ProgramControl)
	(SetCursor 997 (HaveMouse))
	(= gCurrentCursor 997)
)

(procedure (PlC)
	(PlayerControl)
	(SetCursor 999 (HaveMouse))
	(= gCurrentCursor 999)
)

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 10)
)

(procedure (checkEvent event view)
	(if
		(and
			(> (event x?) (view nsLeft?))
			(< (event x?) (view nsRight?))
			(> (event y?) (view nsTop?))
			(< (event y?) (view nsBottom?))
		)
	)
)

(procedure (checkBox pEvent x1 x2 y1 y2)
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

(instance glow of Prop
	(properties
		y 1
		x 1
		view 412
		loop 7
	)
)

(instance glow2 of Prop
	(properties
		y 1
		x 1
		view 412
		loop 8
	)
)

(instance face of Prop
	(properties
		y 145
		x 230
		view 412
		loop 1
	)
)

(instance fire of Prop
	(properties
		y 172
		x 219
		view 412
		loop 3
	)
)

(instance torch of Prop
	(properties
		y 125
		x 265
		view 412
		loop 5
	)
)

(instance tileTrapBroken of Prop
	(properties
		y 183
		x 205
		view 55
		loop 7
	)
)

(instance tileTrapActive of Prop
	(properties
		y 183
		x 165
		view 55
		loop 6
	)
)

(instance statue1 of Act
	(properties
		y 155
		x 185
		view 55
	)
)

(instance statue2 of Prop
	(properties
		y 155
		x 145
		view 55
		loop 1
	)
)

(instance bow of Prop
	(properties
		y 175
		x 206
		view 55
		loop 2
	)
)

(instance rope of Prop
	(properties
		y 167
		x 82
		view 55
		loop 3
	)
)

(instance ropeTop of Prop
	(properties
		y 132
		x 93
		view 55
		loop 5
	)
)

(instance arrow of Act
	(properties
		y 155
		x 143
		view 49
	)
)

(instance alterEgo of Act
	(properties
		y 1
		x 1
		view 231
	)
)
