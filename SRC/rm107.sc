;;; Sierra Script 1.0 - (do not remove this comment)

(script# 107)
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
	rm107 0
	submitScore 1
)

(local
; ARCADE-GAME: SAIL BOAT XTREME
; BY RYAN SLATTERY

; note:   uses the same coding ideas as room 101


                                  ; (use "sciaudio")
	rMovingUp =  0
	rMovingDown =  0
	boatHit1 =  0
	boatHit2 =  0
	boatHit3 =  0
	boatHit4 =  0
	ringHit =  0
	bombHit =  0
	randoBoat1
	randoBoat2
	randoBoat3
	randoBoat4
	canDamage =  1 ; mark this false when using the nuke
	gotNuke =  1
	randoNuke
	rPlayerLife =  0
	showHearts =  50 ; reduces by one each cycle - shows them with hit
	gameOver =  1
	htext
	htext2
	myEvent     ; checks to see what coordinates the cursor is at
	playOn =  1 ; cursor over "Play" button
	quitNow =  0 ; cursor over the "Quit" button
	scores =  0 ; cursor over the "High Score" button
	paid =  0
	totalRings =  0
	riar =  0 ; "rings in a row"
	accumulatedScore =  0
	scoreAddition =  0
	oldVolume
	newScore
	oldSpeed      ; Will equal gSpeed, so game can be put back to normal speed after puzzle solved
	readInstructions =  0
	sfx = 1	
	
	[newName 17]
	;newName = 0
)
; WARNING: This script contained a string section, which is not supported. It may not compile.
; snd

(instance rm107 of Rm
	(properties
		picture 902
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(= oldSpeed gSpeed)
		(gGame setSpeed: 3)
		(= gTimeCh 1)
		(= gArcStl 1)
		(boat
			init:
			setCycle: Fwd
			cycleSpeed: 1
			setScript: bigBoats
		)
		(fence init: setPri: 14 setScript: fenceRun)
		(fenceRun changeState: 1)
		
		(three21 init: hide: setScript: ringScript)
		(buttonBlue init: setScript: pressSpace)
		(buttonYellow init: setScript: pressA)
		(joyStick init: setPri: 15)
		(hearts
			init:
			hide:
			setPri: 15
			ignoreActors:
			setScript: nukeTimer
		)
		(bombDisplay init:)
		(boatArt
			init:
			hide:
			setPri: 14
			ignoreActors:
			setCycle: Fwd
			cycleSpeed: 2
		)
		(titleSailboat init: hide: setPri: 13 ignoreActors:)
		(titleXtreme
			init:
			hide:
			setPri: 13
			ignoreActors:
			setCycle: Fwd
			cycleSpeed: 2
		)

		(gTheMusic prevSignal: 0 stop:)
		(gGame setCursor: gNormalCursor SET_CURSOR_VISIBLE 279 31)      ; sets cursor off the play field

		(bigBoats changeState: 10)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0
				(Display
					{Score:}
					dsCOORD
					200
					13
					dsCOLOUR
					15
; white
					dsBACKGROUND
					clTRANSPARENT
					dsFONT
					4
				)
			)
			(1
				(= cycles 1)
				(if (not readInstructions)
					(= gWndColor 11)
					(= gWndBack 1)
					(Print 107 3 #font 4 #at 35 20 #width 240)
; Instructions:\n\nCollect as many rings as you can while avoiding the other ships. Bonus points are accumulated for the number of rings you collect in a row. You have only 5 life points, so beware of the speed round!
					(= gWndColor 0)
					(= gWndBack 15)
				)
			)
			; (boat: posn((boat:x)101))
			; (send gTheMusic:prevSignal(0)stop()number(107)loop(-1)play())
; = snd aud (send snd:
;                    command("playx")
;                    fileName("music\\sailboat.mp3")
;                    volume("70")
;                    loopCount("0")
;                    init()
;                )
			(2
				(= seconds 14)
				(three21 hide:)
				(Display {} dsRESTOREPIXELS htext2)
				(Display
					{Next Ring:}
					dsCOORD
					126
					13
					dsCOLOUR
					14
; yellow
					dsBACKGROUND
					clTRANSPARENT
					dsFONT
					4
				)
				; Display("Life:"
				; dsCOORD 214 39 dsCOLOUR 10 /* light green*/ dsBACKGROUND clTRANSPARENT dsFONT 4
				; )
				(Display
					{Score:}
					dsCOORD
					200
					13
					dsCOLOUR
					15
; white
					dsBACKGROUND
					clTRANSPARENT
					dsFONT
					4
				)
				(Display
					{Bombs:}
					dsCOORD
					50
					13
					dsCOLOUR
					12
; red
					dsBACKGROUND
					clTRANSPARENT
					dsFONT
					4
				)
				(gGame setSpeed: 3)
			)
			(3
				(three21
					show:
					loop: 0
					cel: 0
					setCycle: End self
					cycleSpeed: 11
					setPri: 15
				)
			)
			(4
				(= seconds 4)
				(three21
					show:
					loop: 1
					setCycle: Fwd
					cycleSpeed: 1
					setPri: 15
				)
				(gGame setSpeed: 1)
			)                             ; 1
			(5
				(= cycles 1)
				(three21 hide:)
			)
			(6 (RoomScript changeState: 2))
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)

		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) $0073)   ; lowercase s
				(if  gameOver
					(if sfx
						(= sfx 0)
						(sfxView loop: 1)	
					else
						(= sfx 1)
						(sfxView loop: 0)	
					)
				)
			)
			(if (== (pEvent message?) $0061)   ; lowercase a
				(cond 
					(gameOver)
					; (newGame())
					(showHearts (= showHearts 0))
					(else (= showHearts 40))
				)
				(pressA changeState: 1)
			)
			(if (== (pEvent message?) KEY_SPACE)
				(cond 
					(gameOver)
					; (newGame())
					(gotNuke (-- gotNuke) (nukeTimer changeState: 1))
				)
				(pressSpace changeState: 1)
			)
		)
		(if (== (pEvent type?) evJOYSTICK)
			(if (== (pEvent message?) 1)    ; If pressed the UP arrow
				(cond 
					(gameOver
						(cond 
							(playOn (= playOn 0) (= scores 1))
							(quitNow (= quitNow 0) (= playOn 1))
							(scores (= scores 0) (= quitNow 1))
						)
						(= rMovingUp 1)
						(playSFX 211)
						;(gTheSoundFX number: 211 play:)
					)
					((not rMovingUp)    ; if stationary or moving DOWN
						(boat setMotion: MoveTo 55 0)   ; Move boat UP
						(= rMovingUp 1) ; When TRUE, hitting up again will stop boat
						(= rMovingDown 0)
					)
					(else (boat setMotion: MoveTo 55 (boat y?)) (= rMovingUp 0)) ; If moving UP, obviously NOT moving DOWN ; STOP the BOAT!!!
				)
			)                           ; Since Stopped, not moving up
			(if (== (pEvent message?) 5)    ; If pressed the DOWN arrow
				(cond 
					(gameOver
						(cond 
							(playOn (= playOn 0) (= quitNow 1))
							(quitNow (= quitNow 0) (= scores 1))
							(scores (= scores 0) (= playOn 1))
						)
						(= rMovingDown 1)
						(playSFX 211)
						;(gTheSoundFX number: 211 play:)
					)
					((not rMovingDown)      ; ALL this is the same as the UP stuff, just completely opposite
						(boat setMotion: MoveTo 55 180)
						(= rMovingDown 1)
						(= rMovingUp 0)
					)
					(else (boat setMotion: MoveTo 55 (boat y?)) (= rMovingDown 0))
				)
			)
			(if (or (== (pEvent message?) 3) 
					(== (pEvent message?) 7)) 
				(if  gameOver
					(if sfx
						(= sfx 0)
						(sfxView loop: 1)	
					else
						(= sfx 1)
						(sfxView loop: 0)	
					)
				)
			)
		)
		(if (== (pEvent message?) KEY_RETURN)
			(playSFX 208)
			;(gTheSoundFX number: 208 play:)
			(if gameOver (newGame))
		)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if
				(or
					(checkEvent
						pEvent
						(play nsLeft?)
						(play nsRight?)
						(play nsTop?)
						(play nsBottom?)
					)
					(checkEvent
						pEvent
						(quit nsLeft?)
						(quit nsRight?)
						(quit nsTop?)
						(quit nsBottom?)
					)
					(checkEvent
						pEvent
						(hiScore nsLeft?)
						(hiScore nsRight?)
						(hiScore nsTop?)
						(hiScore nsBottom?)
					)
				)
				(if gameOver (newGame))
			)
			(if (checkEvent pEvent
					(sfxView nsLeft?)
					(sfxView nsRight?)
					(sfxView nsTop?)
					(sfxView nsBottom?)
				)
				(if gameOver
					(if sfx
						(= sfx 0)
						(sfxView loop: 1)	
					else
						(= sfx 1)
						(sfxView loop: 0)	
					)
				)
			)
		)
	)
	
	(method (doit &tmp [textstring 50])
		(super doit:)
		; set a small delay so the player doesn't trigger a newgame right after they lose
		(if (and gameOver (< gameOver 11)) (++ gameOver))
		(if (== (IsOwnedBy 11 30) TRUE)
			(= accumulatedScore 9999)
		)
		(bombDisplay cel: gotNuke)
		; Showing the Joystick moving
		(cond 
			(rMovingUp (joyStick cel: 1))
			(rMovingDown (joyStick cel: 2))
			(else (joyStick cel: 0))
		)
		; set motion to 0 when boat hits a barrier
		(if (or (boat isStopped:) gameOver)
			(= rMovingUp 0)
			(= rMovingDown 0)
		)
		; updating the hearts number and location
		(if showHearts
			(hearts show:)
			(-- showHearts)
		else
			(hearts hide:)
		)
		(hearts
			cel: rPlayerLife
			posn: (boat x?) (- (boat y?) 15)
		)
		(if (not gameOver)
			(if (<= rPlayerLife 0)
				(DrawPic 902 dpOPEN_RIGHT dpCLEAR 0)
				(= gameOver 1)
				(gTheMusic prevSignal: 0 stop:)
				(Display
					{Score:}
					dsCOORD
					200
					13
					dsCOLOUR
					15
; blue
					dsBACKGROUND
					clTRANSPARENT
					dsFONT
					4
				)
				(bigBoats changeState: 10)
				(ringScript changeState: 4)
				(RoomScript changeState: 7)
			)

			(if (> riar 4)
				(ringNumber show: cel: 20)
				(= riar 5)
			else
				(ringNumber init: show: cel: riar)
			)
		)
		(if gameOver
			(ringNumber hide:)
			(bombItem hide:)
			(= myEvent (Event new: evNULL))
			(cond 
				(
					(checkEvent
						myEvent
						(play nsLeft?)
						(play nsRight?)
						(play nsTop?)
						(+ (play nsBottom?) 10)
					)
					(= quitNow 0)
					(= scores 0)
					(= playOn 1)
					(myEvent claimed: TRUE)
				)
				(
					(checkEvent
						myEvent
						(quit nsLeft?)
						(quit nsRight?)
						(quit nsTop?)
						(+ (quit nsBottom?) 10)
					)
					(= playOn 0)
					(= scores 0)
					(= quitNow 1)
					(myEvent claimed: TRUE)
				)
				(
					(checkEvent
						myEvent
						(hiScore nsLeft?)
						(hiScore nsRight?)
						(hiScore nsTop?)
						(+ (hiScore nsBottom?) 10)
					)
					(= quitNow 0)
					(= playOn 0)
					(= scores 1)
					(myEvent claimed: TRUE)
				)
				(else)
			; selectorZero()
			)
			(myEvent dispose:)
			(if playOn (play cel: 1) else (play cel: 0))
			(if quitNow (quit cel: 1) else (quit cel: 0))
			(if scores (hiScore cel: 1) else (hiScore cel: 0))
		)
		(if canDamage
			(if (<= (boat distanceTo: obstacle1) 9) ; If Rock is too close...
				(if (not boatHit1)
					(= boatHit1 1)  ; But makes sure he doesn't have to pay more than 1 life pt for it.
					(boatHitProc boatHit1)
				)
				;(if (not boatHit1)  ; ...and it hasn't already done you damage...
				;	(-- rPlayerLife) ; ...it deals you 1 damage
				;	(ShakeScreen 1)     ; Visually show the player his blunder
				;	(gTheSoundFX number: 209 play:)
				;	(= showHearts 50)					
				;)
			)
			(if (<= (boat distanceTo: obstacle2) 9) ; Same as ^^
				(if (not boatHit2)
					(= boatHit2 1)
					(boatHitProc boatHit2)
				)
			)
			(if (<= (boat distanceTo: obstacle3) 9) ; Yup...same^^
				(if (not boatHit3)
					(= boatHit3 1)
					(boatHitProc boatHit3)
				)
			)
			(if (<= (boat distanceTo: obstacle4) 9) ; Yup...same^^
				(if (not boatHit4)
					(= boatHit4 1)
					(boatHitProc boatHit4)
				)
			)
		)
		(if (<= (boat distanceTo: ring) 9) ; Yup...same^^
			(if (not ringHit)
				(++ totalRings)
				(= ringHit 1)
				(playSFX 208)
				;(gTheSoundFX number: 208 play:)
				(ring hide:)
				(addScore)
			)
		)
; = snd aud (send snd:
;                    command("play")
;                    fileName("music\\Coin.mp3")
;                    volume("100")
;                    loopCount("0")
;                    init()
;                )
		(if (<= (boat distanceTo: bombItem) 9)
			(if (not bombHit)
				(= bombHit 1)
				(if (< gotNuke 3) (++ gotNuke))
				(bombItem hide:)
				(playSFX 211)
				;(gTheSoundFX number: 211 play:)
			)
		)
		(Format @textstring {%2d} accumulatedScore)
		(Display
			@textstring
			dsCOORD
			230
			13
			dsCOLOR
			14
			dsBACKGROUND
			0
			dsWIDTH
			40
		)
	)
)

(instance bigBoats of Script
	(properties)
	                             ; regulates how 3 boats come at you AGAIN and AGAIN and AGAIN...
	(method (changeState newState)
		(= state newState)
		(switch state
			(1 (= cycles 6))
			(2
				(= cycles (Random 8 15))
				(= boatHit2 0)
				(= randoBoat2 (Random 76 119)) ; (100 143)
				(obstacle2
					init:
					show:
					view: 514
					posn: 305 randoBoat2
					xStep: (Random 7 9)
					setCycle: Fwd
					setMotion: MoveTo 1 (- randoBoat2 (Random 1 15))
					setPri: 14
					ignoreActors:
					ignoreControl: ctlWHITE
				)
			)
			(3 (= cycles 6))
			(4
				(= cycles (Random 8 15))
				(= boatHit3 0)
				(= randoBoat3 (Random 76 106)) ; (100 130)
				(obstacle3
					init:
					show:
					view: 514
					posn: 305 randoBoat3
					xStep: (Random 6 8)
					setCycle: Fwd
					setMotion: MoveTo 1 (+ randoBoat3 (Random 5 30))
					setPri: 14
					ignoreActors:
					ignoreControl: ctlWHITE
				)
			)
			(5 (= cycles 6))
			(6
				(= cycles 7)
				(= boatHit4 0)
				(= randoBoat4 (Random 76 101)) ; (90 125)
				(obstacle4
					init:
					show:
					view: 514
					posn: 305 randoBoat4
					xStep: (Random 6 8)
					setCycle: Fwd
					setMotion: MoveTo 1 (+ randoBoat4 (Random 5 30))
					setPri: 14
					ignoreActors:
					ignoreControl: ctlWHITE
				)
			)
			(7
				(= cycles 6)
				(= boatHit1 0)
				(= randoBoat1 (Random 0 1)) ; This one is either at the TOP or on the BOTTOM...but always FAST
				(if randoBoat1
					(obstacle1
						init:
						show:
						view: 514
						posn: 281 77
; (305 101)
						xStep: 10
						setCycle: Fwd
						setMotion: MoveTo 1 77
						setPri: 14
						ignoreActors:
					)
				else
					(obstacle1
						init:
						show:
						view: 514
						posn: 281 125
; (305 149)
						xStep: 10
						setCycle: Fwd
						setMotion: MoveTo 1 125
						setPri: 14
						ignoreActors:
					)
				)
			)
			(8 (bigBoats changeState: 1))
			(10
				(= cycles 4)
				(gGame setSpeed: 4)
				(= boatHit1 1)
				(= boatHit2 1)
				(= boatHit3 1)
				(= boatHit4 1)
				(= ringHit 1)
				(= rMovingUp 0)
				(= rMovingDown 0)
				(gGame setCursor: 999)
				(obstacle1 hide:)
				(obstacle2 hide:)
				(obstacle3 hide:)
				(obstacle4 hide:)
				(three21 hide:)
				(ring hide:)
				(boat hide: posn: 55 125 setMotion: MoveTo 55 135)
				(fence hide:)
				(bombItem hide: setMotion: NULL)
				(= gotNuke 0)
				(nukeTimer changeState: 7)
; = htext Display("Game Over"
;                    dsCOORD 123 74
;                    dsCOLOUR clRED
;                    dsBACKGROUND clBLACK
;                    dsSAVEPIXELS
;                )
				(play init: show: ignoreActors:)
				(quit init: show: ignoreActors:)
				(hiScore init: show: ignoreActors:)
				(sfxView init: show: ignoreActors:)
				(boatArt show:)
				(titleSailboat show:)
				(titleXtreme show:)
			)
			(11
				(if (== (IsOwnedBy 11 30) TRUE)
					(= accumulatedScore 9999)
				)
				(submitScore)
			)
		)
	)
)

(instance ringScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles 45)
				(if (not ringHit) (= riar 0))
				(= ringHit 0)
				(ring
					init:
					show:
					posn: 310 (Random 87 125)
; (101 149)
					xStep: (Random 6 8)
					setCycle: Fwd
					setMotion: MoveTo 1 (Random 87 125)
					setPri: 14
					ignoreActors:
					ignoreControl: ctlWHITE
				)
			)
			(2 (ringScript changeState: 1))
			(4)
		)
	)
)

(instance fenceRun of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1  (= cycles 1)
				(fence posn: 156 129))
			; (fence:posn(151 153))
			(2
				(= cycles 1)
				; (fence:posn(147 153))
				(fence posn: 152 129)
			)
			(3
				(= cycles 1)
				; (fence:posn(143 153))
				(fence posn: 148 129)
			)
			(4
				(= cycles 1)
				; (fence:posn(155 153))
				(fence posn: 160 129)
			)
			(5 (fenceRun changeState: 1))
		)
	)
)

(instance pressA of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles 4)
				(buttonYellow cel: 1)				
			)
			(2
				(buttonYellow cel: 0)
				(if (> gameOver 10) 
					(playSFX 208)
					;(gTheSoundFX number: 208 play:)
					(newGame)
				)
			)
		)
	)
)

(instance pressSpace of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles 4)
				(buttonBlue cel: 1)
			)
			(2
				(buttonBlue cel: 0)
				(if (> gameOver 10) 
					(playSFX 208)
					;(gTheSoundFX number: 208 play:)
					(newGame)
				)
			)
		)
	)
)

(instance nukeTimer of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles 6)
				(= canDamage 0)
				(ShakeScreen 1)
				(playSFX 209)
				;(gTheSoundFX number: 209 play:)
				(obstacle1 setMotion: NULL view: 526 cel: 0 setCycle: End)
				(obstacle2 setMotion: NULL view: 526 cel: 0 setCycle: End)
				(obstacle3 setMotion: NULL view: 526 cel: 0 setCycle: End)
				(obstacle4 setMotion: NULL view: 526 cel: 0 setCycle: End)
			)
			(2
				(= cycles 6)
				(obstacle1 posn: 1 90)
				(obstacle2 posn: 1 90)
				(obstacle3 posn: 1 90)
				(obstacle4 posn: 1 90)
			)
			(3 (= canDamage 1) (self cue:))
			(4 (= cycles (Random 40 60)))
			(5
				(= cycles 7)
				(= randoNuke (Random 1 3))
				(if (> randoNuke 2)
					(= bombHit 0)
					(bombItem
						init:
						show:
						posn: 310 (Random 87 125)
; (101 149)
						xStep: (Random 5 7)
						setCycle: Fwd
						setMotion: MoveTo 1 (Random 87 125)
						setPri: 14
						ignoreActors:
						ignoreControl: ctlWHITE
					)
				)
			)
			(6 (self changeState: 4))
		)
	)
)

; (procedure (selectorZero)
; = playOn 0
; = quitNow 0
; = scores 0
; )
(procedure (addScore)
	(++ riar)
	(if (> riar 9) (= riar 10))
	(= scoreAddition (* riar 5))
	(= accumulatedScore (+ accumulatedScore scoreAddition))
)

(procedure (newGame button &tmp [buffer 200] [anotherBuffer 300])
	(cond 
		(quitNow
			(= button
				(Print
					{Are you sure you want to quit?}
					#title
					{Quit?}
					#button
					{yes}
					1
					#button
					{ No_}
					0
				)
			)
			(if button
				(gGame setSpeed: oldSpeed)
				(gRoom newRoom: 30)
				(= gTimeCh 0)
				(= gArcStl 0)
			)
		)
		(playOn
			(cond 
				((> gGold 1)
					(Format
						@buffer
						{It costs 1 gold coin to play.\n\nYou have %u gold coins left.\n\nDo you want to pay the gold coin?}
						gGold
					)
					(= button
						(Print
							@buffer
							#title
							{Play Game?}
							#at
							-1
							30
							#button
							{Yes}
							1
							#button
							{ No_}
							0
							#button
							{Instructions}
							2
						)
					)
					(if (== button 1) (= paid 1) (-- gGold))
					(if (== button 2)
						(Print
							107
							3
							#title
							{Instructions:}
							#font
							4
							#at
							-1
							24
							#width
							240
						)
					)
				)
				((== gGold 1)
					(Print 107 1)
					(= button
						(Print
							{Do you want to pay the gold coin?}
							#title
							{Play On?}
							#button
							{Yes}
							1
							#button
							{ No_}
							0
						)
					)
					(if (== button 1) (= paid 1) (-- gGold))
				)
				(else (Print 107 2))
			)
			(if paid
				(= rPlayerLife 5)
				(= totalRings 0)
				(= gameOver 0)
				(= accumulatedScore 0)
				(= riar 0)
				; (send gTheMusic:prevSignal(0)stop()number(107)loop(-1)play())
; = snd aud (send snd:
;                                command("playx")
;                                fileName("music\\sailboat.mp3")
;                                volume("70")
;                                loopCount("0")
;                                init()
;                            )
				; (send gGame:setCursor(gNormalCursor SET_CURSOR_VISIBLE 279 31))
				(gTheMusic number: 107 loop: -1 play:)	
				(gGame setCursor: 998)
				(play hide:)
				(quit hide:)
				(hiScore hide:)
				(sfxView hide:)
				(boatArt hide:)
				(titleSailboat hide:)
				(titleXtreme hide:)
				; (selector:hide())
				(Display {} dsRESTOREPIXELS htext)
				(DrawPic 107 dpOPEN_RIGHT dpCLEAR 0)
				(fence show:)
				; (boat:show()posn(55 125))
				(boat show: posn: (boat x?) 101)
				(bigBoats changeState: 1)
				(RoomScript changeState: 2)
				(ringScript changeState: 1)
				(nukeTimer changeState: 3)
				(= gotNuke 1)
				(= paid 0)
				(= showHearts 50)
			else
			; (send gRoom:newRoom(30))
			; = gTimeCh 0
			; = gArcStl 0
			)
		)
		(scores
			(= gWndColor 11)                ; clYELLOW
			(= gWndBack 1)                  ; clBLACK
			(Format
				@anotherBuffer
				{Rank__Score__Name\n\n____1______%u_____%s\n____2______%u_____%s\n____3______%u_____%s\n____4______%u_____%s\n____5______%u_____%s\n}
				gScore1
				gScoreName1
				gScore2
				gScoreName2
				gScore3
				gScoreName3
				gScore4
				gScoreName4
				gScore5
				gScoreName5
			)
			(Print @anotherBuffer #title {High Scores} #at -1 30)
			(= gWndColor 0)                 ; clBLACK
			(= gWndBack 15)
		)
	)
)
                                            ; clWHITE
(procedure (submitScore)
	(if (>= accumulatedScore gScore5)
		(Print 107 4)
		;(EditPrint @newName 17 {Enter Name})
		;(= gNewScoreName @newName)
		(cond 
			((>= accumulatedScore gScore1)
				(= [g107Solved 0] 1) ; Beat Erasmus
				(= [g107Solved 1] 1) ; Beat Henry
				(= gScore5 gScore4)
				(= gScoreName5 gScoreName4)
				(= gScore4 gScore3)
				(= gScoreName4 gScoreName3)
				(= gScore3 gScore2)
				(= gScoreName3 gScoreName2)
				(= gScore2 gScore1)
				(= gScoreName2 gScoreName1)
				(= gScore1 accumulatedScore)
				;(= gScoreName1 @newName)
				(= gScoreName1 @gName)
			)
			((>= accumulatedScore gScore2)
				(= [g107Solved 1] 1) ; Beat Henry
				(= gScore5 gScore4)
				(= gScoreName5 gScoreName4)
				(= gScore4 gScore3)
				(= gScoreName4 gScoreName3)
				(= gScore3 gScore2)
				(= gScoreName3 gScoreName2)
				(= gScore2 accumulatedScore)
				;(= gScoreName2 @newName)
				(= gScoreName2 @gName)
			)
			((>= accumulatedScore gScore3)
				(= gScore5 gScore4)
				(= gScoreName5 gScoreName4)
				(= gScore4 gScore3)
				(= gScoreName4 gScoreName3)
				(= gScore3 accumulatedScore)
				;(= gScoreName3 @newName)
				(= gScoreName3 @gName)
			)
			((>= accumulatedScore gScore4)
				(= gScore5 gScore4)
				(= gScoreName5 gScoreName4)
				(= gScore4 accumulatedScore)
				;(= gScoreName4 @newName)
				(= gScoreName5 @gName)
			)
			((>= accumulatedScore gScore5)
				(= gScore5 accumulatedScore)
				;(= gScoreName5 @newName)
				(= gScoreName5 @gName)
			)
		)
	)
)
(procedure (playSFX num)
	(if sfx
		(gTheSoundFX number: num play:)
	)	
)
(procedure (boatHitProc whichBoat)
		(-- rPlayerLife)
		(ShakeScreen 1)
		(playSFX 210)
		;(gTheSoundFX number: 210 play:)
		(= showHearts 50)	
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

(instance boat of Act
	(properties
		y 101
		x 60
		view 513
	)
)

(instance boatArt of Prop
	(properties
		y 101
		x 100
		view 527
		loop 2
	)
)

(instance titleSailboat of Prop
	(properties
		y 57
		x 100
		view 527
		loop 3
	)
)

(instance titleXtreme of Prop
	(properties
		y 132
		x 100
		view 527
		loop 4
	)
)

(instance hearts of Prop
	(properties
		y 101
		x 60
		view 524
		loop 4
	)
)

(instance fence of Prop
	(properties
		y 129
		x 160
		view 512
	)
)

(instance buttonBlue of Prop
	(properties
		y 180
		x 222
		view 524
		loop 1
	)
)

(instance buttonYellow of Prop
	(properties
		y 180
		x 256
		view 524
		loop 2
	)
)

(instance joyStick of Prop
	(properties
		y 181
		x 70
		view 524
		loop 3
	)
)

; (instance coin of Prop
;    (properties y 34 x 90 view 986 loop 1)
; )
(instance ringNumber of Prop
	(properties
		y 20
		x 180
		view 955
	)
)

(instance three21 of Prop
	(properties
		y 55
		x 160
		view 527
	)
)

(instance ring of Act
	(properties
		y 153
		x 305
		view 519
	)
)

(instance play of Prop
	(properties
		y 58
		x 235
		view 986
		loop 5
	)
)

(instance quit of Prop
	(properties
		y 93
		x 235
		view 986
		loop 4
	)
)

(instance hiScore of Prop
	(properties
		y 128
		x 235
		view 986
		loop 6
	)
)
(instance sfxView of Prop
	(properties
		y 92
		x 280
		view 164
		loop 0
	)
)

(instance obstacle1 of Act
	(properties
		y 153
		x 305
		view 514
	)
)

(instance obstacle2 of Act
	(properties
		y 153
		x 305
		view 514
	)
)

(instance obstacle3 of Act
	(properties
		y 153
		x 305
		view 514
	)
)

(instance obstacle4 of Act
	(properties
		y 153
		x 305
		view 514
	)
)

(instance bombItem of Act
	(properties
		y 153
		x 305
		view 525
	)
)

(instance bombDisplay of Act
	(properties
		y 19
		x 100
		view 524
		loop 5
	)
)
