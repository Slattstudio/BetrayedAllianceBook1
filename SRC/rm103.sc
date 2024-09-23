;;; Sierra Script 1.0 - (do not remove this comment)

(script# 103)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use menubar)

(public
	rm103 0
)

(local
; ARCADE-STYLE BATTLE SYSTEM
; (INSPIRED BY QUEST FOR GLORY BATTLE SYSTEMS)
; BY RYAN SLATTERY



	; (use "sciaudio")
; VARIABLES CONCERNING THE PLAYER'S ATTACK AND DEFENCE
	pAttacking =  0             ; TRUE when player attacks (disallows other functions, i.e. blocking)
	pAttackReady =  0           ; When TRUE player can attack. As gAgility increases, the time becomes less
	pDodging =  0               ; Same as attacking, only for blocking
	pDodgeReady =  1            ; ^^ Dodging takes less time than attacking
	pCounterAttackReady =  0    ; Allows player to counterattack if he blocks successfully. Chance goes up with gLuck
	pressedLeft =  0            ; Counter attack is achieved by hitting left and right simultaneously, which is not
	pressedRight =  0           ; ...possible for SCI, these values allow it to "remember" the button being pressed
; VARIABLES CONCERNING THE TARGETING SYSTEM
	targetHead =  1     ; If TRUE, player attacks opponent's head. Hits are critical (x2), but accuracy is bad
	targetBody =  0     ; If TRUE, player attacks body. Hits are normal and accuracy is good
	targetLegs =  0     ; If TRUE. player attacks lags. Damage & accuracy are less, but has chance to cripple opponent
	targetChosen =  0   ; Allows player to switch target. See handleEvent method.
	targetPos =  34     ; Variable that acts as the y-axis for the target instance, among others.
	targetWordsPos =  3 ; Variable used for the loop selection of the words that describe which body part is targetted
	accuracy =  40      ; Variable equals the percent chance to hit. Fluctuates with targetted body part
	pHealthBarVar       ; Variable derived from percentage of gHealth/gMaxHealth to determine the loop of health bar
	oHealth             ; Variable for the Opponent's life total
	oHealthBarVar       ; Same as with pHealthBarVar, but for the opponent
	oCrippled =  0      ; Variable allows only for opponent to be "crippled" once, after three shots to the legs
	playerDeath =  0    ; Used to prevent doit method from changing a state indefinitely when player dies
	opponentDeath =  0  ; ^^ same idea
	; opponentAttack = 0  //
	monsterPower        ; Variable whose value determines the damage opponent does, based from the gBattleNumber
	monsterSpeed        ; Same as ^^ but uses a different equation to get a different value, still from gBattleNumber
	rando               ; Variable used for generating random numbers via = rando Random(y z)
	fakeOut             ; Similar to rando, a variable used to randomize when the opponent fakes an attack
	oMiss               ; ^^ Randomized varibale for opponent missing you.
	oldVolume           ; The particular track was too loud, so I turn down the volume, but use this variable to return it after the battle
	htext               ; //
	htext2              ; All used to show and hide the hit rate percentage at different locations //
	htext3              ; //
	boxText             ; Used to show various messages in the box at the bottom of the screen
	oldSpeed            ; Used to reset the games "normal" speed after the battle is over
	opponentNumber      ; Used to give the player more experience if battled multiple enemies
	canRun =  0
	snd
	
	gameLoss = 0
	countPile = 0
)
; --------------------------------------------------------------------------------------------------------------
; Note: Each creature is determined by the gBattleNumber, all multiples of 100. So the view of each opponent
; ...is view(gBattleNumber) and all characteristics are developed from their number, which increases. This
; ...allows for one room to host opponents of varying views and strengths.
; --------------------------------------------------------------------------------------------------------------

(instance rm103 of Rm
	(properties
		picture scriptNumber
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(= oldSpeed gSpeed) ; To remember the previous game speed so that it can be reset when battle is over
		(gGame setSpeed: 4)         ; Set the speed and tempo of the battle
		(= gArcStl 1)  ; When (1) TRUE it removes the retype function of the spacebar (see User Script)
; EQUATIONS TO DETERMINE POWER/SPEED/LIFE CHARACTERISTICS OF DIFFERENT ENEMIES AND THE PLAYER
		(= monsterPower (- (/ gBatNum 25) 1)) ; 100 = 3, 200 = 7, 300 = 11, 400 = 15, 500 = 19, 600 = 23
		(= monsterSpeed (/ 120 (+ (/ gBatNum 100) 4))) ; x =  80 / [ (gBattleNumber/100) + 3 ]
		; 100 = 24, 200 = 20, 300 = 17, 400 = 15, 500 = 13, 600 = 12
		(= pHealthBarVar
			(/ (* 25 (/ (* 100 gHlth) gMaxHlth)) 100)
		)                                                                ; Determins view number of life gauge
		; x =  [25 ( 100gHealth / gMaxHealth ) ] / 100
		(= oHealth gBatNum)
		(if (not (== oHealth gOpHealth)) (= oHealth gOpHealth))
		(if (== gBatNum 300) (= oHealth gTrollHealth)) ; against the troll
		(= oHealthBarVar
			(/ (* 25 (/ (* 100 oHealth) gBatNum)) 100)
		)
		(= opponentNumber gEnNum)
		(= gRanAway 0)
		(if gSpdDmn (/ monsterSpeed 2))                ; Decreases "down" time of opponent by half (/2)
		(if gBruteStr (* monsterPower 2))             ; Increases Power of opponent (x2)
		(if gLifeDoub (* oHealth 2))                     ; Increases Life of opponent (x2)
		(if (gEgo has: 10) (= accuracy (+ accuracy 15)))   ; Increase accuracy if ego has goggles
		(attackKey init: setScript: playerAttack)
		(defendKey init: setScript: playerDefend)
		(counterKey init: setScript: counterAttack)
		; (target:init()setPri(15))
		(attackWord init:)
		(defendWord init:)
		(counterWord init: hide:)
; setScript(targetScript)
		(player init:)
		(playerArc init: setPri: 7)
		(opponent init: view: gBatNum setScript: compAttack)
		; (bodyImage:init())
		(bodyWords init: setPri: 15 cel: 4)
		; (space:init())
		; (length:init())
		(if (gEgo has: 10)
			(percent init: setPri: 15 cel: 1)
		else
			(percent init: setPri: 15 cel: 0)
		)
		(pHealthBar init: cel: pHealthBarVar)
		(oHealthBar init: cel: oHealthBarVar)
		(Display
			{Health}
			dsFONT
			4
			dsCOORD
			24
			18
			dsCOLOUR
			9
			dsBACKGROUND
			clTRANSPARENT
		)
		(Display
			{Enemy}
			dsFONT
			4
			dsCOORD
			262
			18
			dsCOLOUR
			12
			dsBACKGROUND
			clTRANSPARENT
		)
		(TheMenuBar state: DISABLED)
		; Display("Player HP:" dsCOORD 26 132  dsCOLOUR 1 /* dark blue*/ dsBACKGROUND clTRANSPARENT) //dsFONT 4)
		; Display("Opponent HP:" dsCOORD 221 132  dsCOLOUR 4 /* dark red*/ dsBACKGROUND clTRANSPARENT) //dsFONT 4)
		(Display
			{Attack Area:}
			dsCOORD
			238
			52
			dsCOLOUR
			4 ; dark red
			dsBACKGROUND
			clTRANSPARENT
			dsFONT
			4
		)
		(Display
			{Hit %}
			dsCOORD
			238
			39
			dsCOLOUR
			1
; dark blue
			dsBACKGROUND
			clTRANSPARENT
			dsFONT
			4
		)
		(Display
			{Press [Space]}
			dsCOORD
			238
			94
			dsCOLOUR
			1
; NAVY
			dsBACKGROUND
			clTRANSPARENT
			dsFONT
			4
		)
		; (if (gDeadWeight)
		(accuracyShift)
	)
)

; )

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState dyingScript)
		(= state mainState)
		(switch state
			(0
				(= cycles 2)
				(gTheMusic prevSignal: 0 number: 103 play: loop: -1)
			)
			(1
				(= cycles 1)
				(if (== gStr 15)
;					(= gWndColor 14)
;					(= gWndBack 4)
;					; Print(103 0 #font 4)
;					; Print(103 1 #font 4)
;					(Print 103 5 #font 4 #at -1 120)
;					(= gWndColor 0) ; clBLACK
;					(= gWndBack 15)
					(= gWndColor 14)
					(= gWndBack 4)
					(Print 103 5 #font 4 #title {Prepare for Battle} #at -1 120 #button {Ok} )
					(= gWndColor 0) ; clBLACK
					(= gWndBack 15)
				)
			)                                                 ; clWHITE
			(2 (= cycles 3))
; (if(== gBatNum 200)
;                    = snd aud (send snd:
;                        command("playx")
;                        fileName("music\\mybattle.mp3")
;                        volume("70")
;                        loopCount("0")
;                        init()
;                    )
;                )(else
;                    = snd aud (send snd:
;                        command("playx")
;                        fileName("music\\battle.mp3")
;                        volume("70")
;                        loopCount("0")
;                        init()
;                    )
;                )
			(3 (= canRun 1)
			)
			; dying
			(4	(= cycles 2)
				(= gArcStl 0)
				(ProgramControl)	
			)
			(5
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 607
					register:
						{You have been bested by your brutal opponent. Better shape up before fighting a battle like this again.}
				)
				(gGame setScript: dyingScript)		
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evJOYSTICK)
			(if (and (not gameLoss)
					(not opponentDeath))
				(if (== (pEvent message?) 1)    ; If pressed the UP arrow
					(if pAttackReady
						(if (not pDodging) (playerAttack changeState: 3))
					)
				)
				(if (== (pEvent message?) 5)     ; If pressed the DOWN arrow
					(if pDodgeReady
						(if (not pAttacking) (playerDefend changeState: 2))
					)
				)
				(if (== (pEvent message?) 7)     ; If pressed the LEFT arrow
					(if pCounterAttackReady
						(= pressedLeft 1)
						(if pressedRight (counterAttack changeState: 2))
					)
				)
				(if (== (pEvent message?) 3)     ; If pressed the RIGHT arrow
					(if pCounterAttackReady
						(= pressedRight 1)
						(if pressedLeft (counterAttack changeState: 2))
					)
				)
			)
		)
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_ESCAPE)
				(if (not gNoRun)
					(if canRun
						(= gRanAway 1)
						(gTheMusic prevSignal: 0 stop:)

						(= gOpHealth oHealth)
						(if (== gBatNum 300) (= gTrollHealth oHealth)) ; against the troll
						(Print 103 2 #at -1 120)

						(compAttack changeState: 13)
					)
				else  ; end canRun
					(Print 103 4)
				)
			)
			(if (== (pEvent message?) $0020)  ; If pressed the SPACEBAR
				(if targetHead
					(if (not targetChosen)
						(= targetChosen 1)
						(= accuracy (+ accuracy 40))
						(= targetHead 0)
						(= targetBody 1)
						(++ targetWordsPos)
						(= targetPos (+ targetPos 16))
						; (targetScript:changeState(2))
						(bodyWords cel: 1)
						(accuracyShift)
					)
				)
				(if targetBody
					(if (not targetChosen)
						(= targetChosen 1)
						(= accuracy (- accuracy 10))
						(= targetBody 0)
						(= targetLegs 1)
						(++ targetWordsPos)
						(= targetPos (+ targetPos 23))
						; (targetScript:changeState(2))
						(bodyWords cel: 2)
						(accuracyShift)
					)
				)
				(if targetLegs
					(if (not targetChosen)
						(= targetChosen 1)
						(= accuracy (- accuracy 30))
						(= targetLegs 0)
						(= targetHead 1)
						(-- targetWordsPos)
						(-- targetWordsPos)
						(= targetPos (- targetPos 39))
						; (targetScript:changeState(2))
						(bodyWords cel: 0)
						(accuracyShift)
					)
				)
			)
			(= targetChosen 0)
		)
	)
	
	(method (doit &tmp [HP 50] dyingScript)
		(super doit:)
		
		; hide and show cursor
		(if (not gameLoss)
			(SetCursor 998 (HaveMouse))
			(= gCurrentCursor 998)
		else
			(SetCursor 999 (HaveMouse))
			(= gCurrentCursor 999)
		)                                                     
		; show "press esc to escape
		(if (< gHlth 11)
			(if (not gNoRun)
				(Display {Press [Esc] to escape} dsCOORD 110 158 dsCOLOUR 12 dsBACKGROUND clTRANSPARENT dsFONT 4)
				(Display {[Esc]} dsCOORD 139 158 dsCOLOUR 14 dsBACKGROUND clTRANSPARENT dsFONT 4)
			)	
		)
		
; Visualization of the health bar and numerical Health indicator
		(if gDeadWeight (= accuracy 100))
		(= pHealthBarVar
			(/ (* 25 (/ (* 100 gHlth) gMaxHlth)) 100)
		)
		(= oHealthBarVar
			(/ (* 25 (/ (* 100 oHealth) gBatNum)) 100)
		)
		(pHealthBar cel: pHealthBarVar)
		(oHealthBar cel: oHealthBarVar)
		(if (== (oHealthBar cel?) 0)
			(if (> oHealth 0) (oHealthBar cel: 1))
		)
		(if (== (pHealthBar cel?) 0)
			(if (> gHlth 0) (pHealthBar cel: 1))
		)
		(if (> gEnNum 1)
			(opponent2 init: view: gBatNum)
		else
			(opponent2 hide:)
		)
		(if (== gBatNum 99)
			(if (< oHealth gBatNum)
				(speechBubble init: show: cel: 0 setPri: -1)
				(if (< oHealth (/ gBatNum 2))
					(speechBubble init: show: cel: 1 setPri: -1)
				)
			else
				(speechBubble hide:)
			)
		)
; Handling death of Player
		(if (<= gHlth 0)
			(= gHlth 0)
			(gTheMusic fade:)
			(if (not gameLoss)
				(RoomScript changeState: 4) ; dying
				(= gameLoss 1)
			)
		)
		; Death of Opponent //
		(if (<= oHealth 0)
			(= oHealth 0)
			(if (== gBatNum 300) (= gTrollHealth 0)) ; against the troll
			(if (== gPreviousRoomNumber 20) 
				(if (not countPile)
					(++ g20PileOfBodies)
					(if (> g20PileOfBodies 7)
						(= g20PileOfBodies 7)	
					)
					(= countPile 1)
				)
			)
			(cond 
				((> gEnNum 1) (-- gEnNum) (goAgain))
				((not opponentDeath) (compAttack changeState: 10))
			)
		)
; Visualize when attacking and defending are possible
; Attacking
		(if pAttackReady
			(if (not pDodging)
				(if (not pAttacking)
					(attackKey cel: 0)
					(attackWord cel: 1)
				else
					(attackKey cel: 1)
					(attackWord cel: 1)
				)
			else
				(attackKey cel: 1)
				(attackWord cel: 0)
			)
		else
			(attackKey cel: 1)
			(attackWord cel: 0)
		)
; Defending
		(if pDodgeReady
			(if (not pAttacking)
				(if (not pDodging)
					(defendKey cel: 0)
					(defendWord cel: 1)
				else
					(defendKey cel: 1)
					(defendWord cel: 0)
				)
			else
				(defendKey cel: 1)
				(defendWord cel: 0)
			)
		else
			(defendKey cel: 1)
			(defendWord cel: 0)
		)
	)
)

(instance playerAttack of Script
	(properties)
	
	(method (changeState newState &tmp rand)
		(= state newState)
		(switch state
			(0 (= cycles 1))
			(1
				(= cycles (- 25 (/ gAg 4)))
			)
			(2 (= pAttackReady 1))
			(3
				(= cycles 4)
				(= pAttacking 1)
				(= pAttackReady 0)
				(player loop: 0 cel: 0 setCycle: End)
				(Display {} dsRESTOREPIXELS boxText)
			)
			(4
				(= cycles 4)
				(player loop: 2 cel: 0)
				(playerArc cel: 0 setCycle: End)
				(= rando (Random 1 100))
				(if (> rando accuracy)
					(opponent loop: 0 setCycle: End)
				else
					; hitting the opponent
					; perhaps change the OP's state, to avoid them attacking the player immediately after being hit
					(= rand (Random 1 10))
					(if (> rand 5) ; 4o% chance
						(compAttack changeState: 14)
					else
						(compAttack changeState: 1)
					)
					(if (not opponentDeath)
						(opponent loop: 3 setCycle: Fwd)
					)
					(cond 
						(targetHead (ShakeScreen 2) (= oHealth (- (- oHealth gStr) gStr)))
						(targetLegs
							(legHitProc)
						)
						(else (= oHealth (- oHealth gStr)))
					)
				)
			)
			(5
				(player loop: 0 cel: 0 setCycle: CT)
				(= pAttacking 0)
				(if (not opponentDeath)
					(opponent loop: 0 setCycle: CT)
				)
				(playerAttack changeState: 0)
			)
			; rerouted if defending to have a cool off after attempting to block
			(7
				(= cycles 10)
				(= pAttackReady 0)
			)
			(8 (self changeState: 2))
		)
	)
)

(instance playerDefend of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(2
				(= cycles 1)
				(player loop: 0 cel: 0 setCycle: End)
			)
			(3
				(= cycles 5)
				(= pDodging 1)
				(= pDodgeReady 0)
				(playerAttack changeState: 7)
			)
			(4
				(= cycles (Random 7 12))
				(= pDodging 0)
				(= pDodgeReady 0)
				(player loop: 0 setCycle: CT)
			)
			(5 (= pDodgeReady 1))
		)
	)
)

(instance counterAttack of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(2
				(= cycles 8)
				(= pAttacking 1)
				(counterNull)
				(player loop: 2 cel: 0)
				(if (not opponentDeath)
					(opponent loop: 3 cel: 0 setCycle: Fwd)
				)
				(if targetLegs
					(legHitProc)
				else
					;(= oHealth (- oHealth gStr)))
					(= oHealth (- oHealth (+ gStr (/ gStr 2))))
				)
				(ShakeScreen 2)
				(= gExp (+ gExp gLuk))
			)
			(3
				(= pAttacking 0)
				(player loop: 0 setCycle: CT)
				(if (not opponentDeath)
					(opponent loop: 0 setCycle: CT)
				)
			)
			(4
				(= cycles 8)
				(= rando (Random 1 100))
				(if (< rando (+ 20 (/ gLuk 2)))   ; Counter-attack will function 27% - 75% depending on gLuck
					(= pCounterAttackReady 1)
					(counterKey loop: 3 cel: 0 setCycle: Fwd)
					(counterWord show:)
				)
			)
			(5 (counterNull))
		)
	)
)

(instance compAttack of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1))
			(1
				(= cycles
					(Random (- monsterSpeed 4) (- monsterSpeed 1))
				)
				(= fakeOut (Random 1 10))
			)
			(2
				(= cycles 6)
				(if (> fakeOut 7)
					(compAttack changeState: 5)
				else
					(opponent loop: 4 cel: 0 setCycle: End)
				)
			)
			(3
				(= cycles 4)
				(opponent loop: 2 setCycle: CT) ; attack
				(if pDodging
					(counterAttack changeState: 4)
				else
					(= oMiss (Random 1 10))
					(if (> oMiss 2) (playerHit))
				)
			)
			(4
				(opponent loop: 0 setCycle: CT)
				(player loop: 0 setCycle: CT)
				(compAttack changeState: 0)
			)
			(5
				(= cycles 6)
				(opponent loop: 1 cel: 0 setCycle: End)
			)
			(6
				(= cycles 6)
				(opponent loop: 0 cel: 0)
			)
			(7
				(= cycles 3)
				(opponent loop: 1 cel: 0 setCycle: End)
			)
			(8
				(= cycles 4)
				(opponent loop: 2 setCycle: CT) ; attack
				(if pDodging
					(counterAttack changeState: 4)
				else
					(= oMiss (Random 1 10))
					(if (> oMiss 2) (playerHit))
				)
			)
			(9
				(opponent loop: 0 setCycle: CT)
				(player loop: 0 setCycle: CT)
				(compAttack changeState: 0)
			)
			(10
				(= cycles 20)
				(= opponentDeath 1)
				(ProgramControl)
				(gTheMusic prevSignal: 0 number: 104 loop: 0 play:)
				(= gNoRun 1)
				(opponent posn: (+ (opponent x?) 7) (opponent y?) loop: 5 cel: 0 setCycle: End cycleSpeed: 2)
			)
			; MusicFade()
			(11
				(= cycles 3)
				(= gameLoss 1)
				(cond 
					((== opponentNumber 3) (reward) (reward) (reward))
					((== opponentNumber 2) (reward) (reward))
					(else (reward))
				)
				(levelUp)
				(levelUp)
				(levelUp)
				(levelUp)
				
; Victory
				(PlayerControl)
			)
			(12 (= cycles 7)
				(Print 103 3 #at -1 120)	
			)
			(13
				(gGame setSpeed: oldSpeed)
				(= gArcStl 0)
				(= gSpdDmn 0)
				(= gBruteStr 0)
				(= gLifeDoub 0)
				(= gDeadWeight 0)
				(= gNoRun 0)
				(TheMenuBar state: ENABLED)
				(= gNoRun 0)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gRoom newRoom: gPreviousRoomNumber)
			)
			; quick counter after being hit
			(14 (= cycles 7))
			; PrintOK()	
			(15 (self changeState: 2))
		)
	)
)

(procedure (playerHit)
	(if (not gDeadWeight)
		(= rando
			(- (+ monsterPower (Random 0 3)) (/ gDef 15))
		)
		(if (> 0 rando) (= rando 0))
		(= gHlth (- gHlth rando))
		(if (< gHlth 1) (= gHlth 0))
		(player loop: 3 setCycle: CT)
		(ShakeScreen 1)
	)
)
(procedure (legHitProc)
	(= oHealth (- oHealth (- gStr (/ gStr 4))))
	(++ oCrippled)
	(if (== oCrippled 3)
		(ShakeScreen 2)
		(= accuracy (+ accuracy 20))
		(= monsterSpeed (+ monsterSpeed 5))
		(= boxText
			(Display
				{Opponent Crippled}
				dsCOORD
				120
				148
				dsCOLOUR
				12
				dsBACKGROUND
				clTRANSPARENT
				dsSAVEPIXELS
				dsFONT
				4
			)
		)
	)	
)	

(procedure (counterNull)
	(= pCounterAttackReady 0)
	(= pressedLeft 0)
	(= pressedRight 0)
	(counterKey loop: 0 cel: 2 setCycle: Cycle)
	(counterWord hide:)
)

(procedure (accuracyShift &tmp [perc 100])
	(if targetBody
		(percent hide:)
		(Display {} dsRESTOREPIXELS htext3)
		(if gDeadWeight (= accuracy 100))
		(Format @perc {%u} accuracy)
		(= htext
			(Display
				@perc
				dsCOORD
				262
				39
				dsCOLOUR
				9
				dsBACKGROUND
				clTRANSPARENT
				dsWIDTH
				25
				dsSAVEPIXELS
				dsFONT
				4
			)
		)
	else
		(if targetLegs
			(percent hide:)
			(Display {} dsRESTOREPIXELS htext)
			(if gDeadWeight (= accuracy 100))
			(Format @perc {%u} accuracy)
			(= htext2
				(Display
					@perc
					dsCOORD
					262
					39
					dsCOLOUR
					9
					dsBACKGROUND
					clTRANSPARENT
					dsWIDTH
					25
					dsSAVEPIXELS
					dsFONT
					4
				)
			)
		)
		(if targetHead
			(percent hide:)
			(Display {} dsRESTOREPIXELS htext2)
			(if gDeadWeight (= accuracy 100))
			(Format @perc {%u} accuracy)
			(= htext3
				(Display
					@perc
					dsCOORD
					262
					39
					dsCOLOUR
					9
					dsBACKGROUND
					clTRANSPARENT
					dsWIDTH
					25
					dsSAVEPIXELS
					dsFONT
					4
				)
			)
		)
	)
)

(procedure (goAgain)
	(FormatPrint
		{You've beaten your opponent, but there are %u more.}
		gEnNum
	)
	(= oHealth gBatNum)
	(= oCrippled 0)
	(= countPile 0)
	(cond 
		(targetHead (= accuracy 40))
		(targetBody (= accuracy 80))
		(targetLegs (= accuracy 70))
	)
	(if (gEgo has: 10) (= accuracy (+ accuracy 15)))
	(accuracyShift)
)

; (bodyImage:setCycle(CT))
(procedure (reward)
	(= gExp (+ gExp (- (/ gBatNum 2) 10)))
	(if (or gSpdDmn gBruteStr)
		(= gExp (+ gExp (- (/ gBatNum 2) 10)))
	)
)

(procedure (levelUp)
	(if (>= gExp gDQ)
		(= gDQ (+ gDQ (/ gDQ 3))) ; Raise experience need by 1/3 of what it was previously.
		(++ gStr)
		(++ gLuk)
		(++ gAg)
		(++ gMaxHlth)
		(++ gHlth)
		(switch (Random 0 3) ; Random extra case 3 being lucky hit.
			(0 (++ gStr))
			(1 (++ gLuk))
			(2 (++ gAg))
			(3
				(++ gStr)
				(++ gLuk)
				(++ gAg)
				(++ gMaxHlth)
				(++ gHlth)
			)
		)
	)
)

(instance attackKey of Prop
	(properties
		y 69
		x 47
		view 515
		loop 1
		cel 1
	)
)

(instance defendKey of Prop
	(properties
		y 83
		x 47
		view 515
		loop 2
	)
)

(instance counterKey of Prop
	(properties
		y 83
		x 47
		view 515
		cel 2
	)
)

; (instance target of Prop
;    (properties y 114 x 270 view 515 loop 7)
; )
; (instance bodyImage of Prop
;    (properties y 150 x 270 view 516 loop 2)
; )
(instance bodyWords of Prop
	(properties
		y 90
		x 252
		view 516
		loop 3
	)
)

(instance percent of Prop
	(properties
		y 102
		x 221
		view 518
	)
)

(instance space of Prop
	(properties
		y 166
		x 272
		view 502
		loop 2
		cel 2
	)
)

(instance attackWord of Prop
	(properties                 ; sword icon
		y 54
		x 48
		view 515
		loop 4
	)
)

(instance defendWord of Prop
	(properties                 ; shield icon
		y 98
		x 48
		view 515
		loop 5
	)
)

(instance counterWord of Prop
	(properties
		y 82
		x 91
		view 515
		loop 6
	)
)

(instance pHealthBar of Prop
	(properties
		y 35
		x 50
		view 159
	)
)

(instance oHealthBar of Prop
	(properties
		y 35
		x 265
		view 160
	)
)

(instance player of Prop
	(properties
		y 108
		x 147
		view 216
	)
)

(instance playerArc of Prop
	(properties
		y 74
		x 147
		view 216
		loop 4
		cel 2
	)
)

(instance opponent of Prop
	(properties
		y 95
		x 167
	)
)

(instance opponent2 of Prop
	(properties
		y 41
		x 190
	)
)

(instance speechBubble of Prop
	(properties
		y 53
		x 205
		view 516
		loop 7
	)
)
