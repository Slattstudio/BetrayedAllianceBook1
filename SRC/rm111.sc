;;; Sierra Script 1.0 - (do not remove this comment)
; +1 SCORE // luck + 5 // int + 1 // Agi + 3 //
(script# 111)
(include sci.sh)
(include game.sh)
(use main)
(use controls)
(use cycle)
(use game)
(use feature)
(use obj)
(use inv)
(use jump)
(use dpath)

(public
	rm111 0
)

(local

; Darts Mini-game
; Created by "Gumby" for Betrayed Alliance



	wager =  0
	snd
	;[xCoord 20] = [180 207 153 226 126 245 101 66 81 238 58 97 237 61 224 77 178 203 123 151]
	;[yCoord 20] = [5 157 173 36 4 88 159 115 33 113 88 16 59 62 139 141 170 17 170 1]
	[velocities 11] = [-50 -40 -30 -20 -10 0 10 20 30 40 50]
	[boardNum 2]
	[openPro 4] = [128 6 183 34]
	[openAvg 4] = [90 0 218 50]
	[openBad 4] = [60 0 250 95]
	[normalPro 4] = [138 39 170 56]
	[normalAvg 4] = [117 121 176 156]
	[normalBad 4] = [103 46 207 145]
	[single1 2] = [176 35]
	[single2 2] = [195 145]
	[single3 2] = [156 158]
	[single4 2] = [209 58]
	[single5 2] = [136 35]
	[single6 2] = [222 95]
	[single7 2] = [116 145]
	[single8 2] = [92 113]
	[single9 2] = [102 57]
	[single10 2] = [219 113]
	[single11 2] = [90 95]
	[single12 2] = [116 43]
	[single13 2] = [218 75]
	[single14 2] = [92 75]
	[single15 2] = [210 131]
	[single16 2] = [102 132]
	[single17 2] = [176 155]
	[single18 2] = [194 43]
	[single19 2] = [135 156]
	[single20 2] = [156 31]
	[double1 2] = [181 22]
	[double2 2] = [203 158]
	[double3 2] = [155 173]
	[double4 2] = [222 50]
	[double5 2] = [131 20]
	[double6 2] = [238 95]
	[double7 2] = [107 158]
	[double8 2] = [77 119]
	[double9 2] = [89 49]
	[double10 2] = [234 118]
	[double11 2] = [73 95]
	[double12 2] = [107 32]
	[double13 2] = [233 71]
	[double14 2] = [77 71]
	[double15 2] = [222 140]
	[double16 2] = [89 142]
	[double17 2] = [180 169]
	[double18 2] = [204 33]
	[double19 2] = [129 168]
	[double20 2] = [156 18]
	[triple1 2] = [171 49]
	[triple2 2] = [186 134]
	[triple3 2] = [156 143]
	[triple4 2] = [197 67]
	[triple5 2] = [140 49]
	[triple6 2] = [207 95]
	[triple7 2] = [126 134]
	[triple8 2] = [107 109]
	[triple9 2] = [144 66]
	[triple10 2] = [204 109]
	[triple11 2] = [105 95]
	[triple12 2] = [126 56]
	[triple13 2] = [204 80]
	[triple14 2] = [107 80]
	[triple15 2] = [204 109]
	[triple16 2] = [114 123]
	[triple17 2] = [171 140]
	[triple18 2] = [186 56]
	[triple19 2] = [140 140]
	[triple20 2] = [156 47]
	; game specific variables
	gameInitPoints   ; 301?
	playerPoints     ; will be initialized to gameInitPoints
	computerPoints   ; same here
	currentRoundPoints  ; total points of throws for this round (3 throws)
	currentThrower  ; 0 = player, 1 = computer
	throwCount      ; 1 through 3 (current thrower)
	roundThrowCount ; 1 through 6 (overall round count, used for resetting the board & removing all the darts)
	computerSkill   ; 0 = Pro, 1 = Avg, 2 = Bad
	missPixels      ; number of pixels to add/subtract from the target based on skill level
	dartSound =  0      ; corresponding number of the sound resource for the dart sound
	
	[playerScoreboard 50]
	[computerFgscoreboard 50]
	[playerPointsDisp 10]
	[computerPointsDisp 10]
	pScorePix
	cScorePix
	computerWon
	playerWon
	computerBusted
	playerBusted
	; throwHarder
	intro =  1 ; set to 0 after instructions to allow change of cursor
	scoreDisplayed =  0
)

(instance rm111 of Rm
	(properties
		picture scriptNumber
		; Set up the rooms to go to/come from here
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init &tmp i)
		; same in every script, starts things up
		(super init:)
		(self setScript: RoomScript)
		(= gArcStl 1)
		
		(Scoreboard addToPic:)
		
		; game setup
		(= gameInitPoints 301)
		; = gameInitPoints 50
		(= computerSkill 1)
		(if (== gDartsWon 0) 
			(gGame changeScore: 1)
			(++ gDartsWon)
		)
		
		(if (== computerSkill 0) (= missPixels 5))
		(if (== computerSkill 1) (= missPixels 10))
		(if (== computerSkill 2) (= missPixels 15))
		(= playerPoints gameInitPoints)
		(= computerPoints gameInitPoints)
		(= computerWon FALSE)
		(= playerWon FALSE)
		(= currentThrower 0) ; the player, currently configured to throw first.
		(if (== currentThrower 0)
			(ThrownDart1 view: 583 loop: 2)
			(ThrownDart2 view: 583 loop: 2)
			(ThrownDart3 view: 583 loop: 2)
			(ThrownDart4 view: 583 loop: 1)
			(ThrownDart5 view: 583 loop: 1)
			(ThrownDart6 view: 583 loop: 1)
			(DartHand view: 586)
		else
			(ThrownDart1 view: 583 loop: 1)
			(ThrownDart2 view: 583 loop: 1)
			(ThrownDart3 view: 583 loop: 1)
			(ThrownDart4 view: 583 loop: 2)
			(ThrownDart5 view: 583 loop: 2)
			(ThrownDart6 view: 583 loop: 2)
			(DartHand view: 585)
		)
		(= throwCount 0)
		(= roundThrowCount 0)
		; = throwHarder FALSE
		(= computerBusted FALSE)
		(= playerBusted FALSE)
		; = snd aud
		(ThrownDart1 init: hide:)
		(ThrownDart2 init: hide:)
		(ThrownDart3 init: hide:)
		(ThrownDart4 init: hide:)
		(ThrownDart5 init: hide:)
		(ThrownDart6 init: hide:)
		(DartHand init:)
		(Exit init:)
		; display the initial scoreboard values
		
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState &tmp [buffer 300])
		(= state mainState)
		(switch state
			(0 (= cycles 2))
			(1
				(= cycles 1)
				(Format
					@buffer
					{How much gold would you like to bet (between 1-20)?\nYou have %u gold.\nType 0 to leave.}
					gGold
				)
				(= wager (GetNumber @buffer)) ; 111 7 //"How much gold would you like to bet (between 1-20)?"
				; #title "The Wager"
				(if (== wager 0) (Print 111 8) (= gArcStl 0) (gRoom newRoom: 44))
				(cond 
					((> wager gGold) (Print 111 9) (RoomScript changeState: 0))
					((> wager 20) (Print 111 10) (RoomScript changeState: 0))
					((< wager 0) (Print 111 11) (RoomScript changeState: 0))
					(else
						(if (> wager 0)
							(if (> wager 6)
								(if (> wager 13) (= computerSkill 0))
								(= computerSkill 1)
							)
							(= computerSkill 2)
						)
						(= gGold (- gGold wager))
					)
				)
			)
			(2
				(if
					(Print
						111
						0 ; "Let's play 301, with double-in and double-out rules.  Do you want detailed instructions?"
						#title
						{Instructions}
						#button
						{ Yes_}
						1
						#button
						{__No__}
						0
					)
					(Print 111 1 #title {The Board}) ; "The red circle in the middle is called the 'bulls eye' and is worth 50 points. It counts as a double.\n\nThe green cicle surrounding it is worth 25 points.\n\nThe numbers 1 through 20 placed around the board are the nominals for darts hitting the respective fields or sectors."
					(Print 111 2 #title {The Board}) ; "The outer thin ring closest to the actual numbers are doubles, and worth double the specified nominal of their sector.\n\nThe inner thin ring is worth three times the nominal score, so the highest possible score on a single dart would be triple 20 (the small red field halfway between the number 20 and bullseye) that is worth 60 points."
					(Print 111 3 #title {Beginning play: Doubling In}) ; "Players will throw three darts per turn, in rotation. Each player must first land one of his darts in the narrow 'double' ring around the outer edge of the numbered pie-shaped wedges, or in the red 'double bull' before any of his points will be scored."
					(Print 111 4 #title {Winning the game: Doubling Out}) ; "A player must end up with exactly zero points to go 'out' and win. The final dart must be in the outer double ring or the inner double bull. If, in the course of throwing three darts, a player's score becomes negative, he is said to 'bust.' None of his darts count for that round and his turn ends."
					(Print 111 5 #title {Winning the game: Doubling Out}) ; "Also, if his score is reduced to 1 point in the course of his turn, this is also a bust, as it is not possible to double out with only 1 point remaining. Finally, a player will bust if he reaches exactly zero without scoring a double on his final dart."
					(Print 111 6 #title {Dart throwing}) ; "To throw a dart, click and hold the left mouse button.  'Drag' the cursor up or down the screen and release the mouse button.  Two factors dictate where the dart will land:\n(1) How quickly you click and then release (velocity)\n(2) How close to vertical the line is (accuracy)"
				)
				(= intro 0)
			)
		)
	)
	
	(method (doit &tmp mbResult wildX wildY savePix)
		(super doit:)
		(if (not scoreDisplayed)
			
			(Format @playerPointsDisp {%d} playerPoints)
			(= pScorePix
				(Display
					@playerPointsDisp
					dsCOORD
					270
					32
					dsCOLOUR
					clRED
					dsBACKGROUND
					clTRANSPARENT
					dsALIGN
					alCENTER
					dsWIDTH
					30
					dsFONT
					3
					dsSAVEPIXELS
				)
			)
			(Format @computerPointsDisp {%d} computerPoints)
			(= cScorePix
				(Display
					@computerPointsDisp
					dsCOORD
					270
					57
					dsCOLOUR
					clNAVY
					dsBACKGROUND
					clTRANSPARENT
					dsALIGN
					alCENTER
					dsWIDTH
					30
					dsFONT
					3
					dsSAVEPIXELS
				)
			)
			(= scoreDisplayed 1)
		)
		(if computerBusted
			(= savePix
				(Display
					{Opponent Busted!}
					dsCOORD
					112
					88
					dsCOLOUR
					clRED
					dsBACKGROUND
					clGREY
					dsALIGN
					alCENTER
					dsWIDTH
					90
					dsFONT
					3
					dsSAVEPIXELS
				)
			)
			(Wait 120)
			(Display {} dsRESTOREPIXELS savePix)
			(= computerBusted FALSE)
		)
		(if playerBusted
			(= savePix
				(Display
					{You Busted!}
					dsCOORD
					112
					88
					dsCOLOUR
					clRED
					dsBACKGROUND
					clGREY
					dsALIGN
					alCENTER
					dsWIDTH
					90
					dsFONT
					3
					dsSAVEPIXELS
				)
			)
			(Wait 120)
			(Display {} dsRESTOREPIXELS savePix)
			(= playerBusted FALSE)
		)
;
; 	    (if (throwHarder)
; 	       = savePix
;           Display(
; 			    "Throw harder"
; 			    dsCOORD 112 88
; 			    dsCOLOUR clRED
; 			    dsBACKGROUND clGREY
; 			    dsALIGN alCENTER
; 			    dsWIDTH 90
; 			    dsFONT 3
; 			    dsSAVEPIXELS
; 		   )
; 	       Wait(120)
;           Display("" dsRESTOREPIXELS savePix)
; 	       = throwHarder FALSE
; 		)
		(if (or playerWon computerWon)
			(if playerWon
				(= gGold (+ gGold wager wager))
				(= savePix
					(Display
						{You won!}
						dsCOORD
						112
						88
						dsCOLOUR
						clRED
						dsBACKGROUND
						clGREY
						dsALIGN
						alCENTER
						dsWIDTH
						90
						dsFONT
						3
						dsSAVEPIXELS
					)
				)
				(Wait 120)
				(Display {} dsRESTOREPIXELS savePix)
			)
			(if computerWon
				(= savePix
					(Display
						{You lost...}
						dsCOORD
						112
						88
						dsCOLOUR
						clRED
						dsBACKGROUND
						clGREY
						dsALIGN
						alCENTER
						dsWIDTH
						90
						dsFONT
						3
						dsSAVEPIXELS
					)
				)
				(Wait 120)
				(Display {} dsRESTOREPIXELS savePix)
			)
			(SetCursor 999 TRUE)      ; set the cursor to the 'arrow'
			(repeat
				(if playerWon
					(++ gDartsWon)
					(= intro 1)
					(cond 
						((== gDartsWon 4)
							(= gLuk (+ gLuk 5))
							(= gAg (+ gAg 3))
							(= gInt (+ gInt 1))
						)
						
					)
					(= mbResult
						(Print
							111
							13
							#title
							{You won!}
							#button
							{Play Again}
							1
							#button
							{__Quit__}
							2
						)
					)
				else
					(= mbResult
						(Print
							111
							13
							#title
							{You lost...}
							#button
							{Play Again}
							1
							#button
							{__Quit__}
							2
						)
					)
				)
				(switch mbResult
					(1
						(if (not (> gDartsWon 3))
							(rm111 init:)
							(= scoreDisplayed 0)
							(return TRUE)
						else
							(Print 111 14 #title {Sailor says:})
							(= gArcStl 0)
							(gRoom newRoom: 44)
							(return TRUE)
						)
					)
					(2
						(= gArcStl 0)
						(gRoom newRoom: 44)
						(return TRUE)
					)
				)
			)
		)
		(if (== roundThrowCount 6)
			(Wait 120)
			(= roundThrowCount 0)
			(ThrownDart1 hide:)
			(ThrownDart2 hide:)
			(ThrownDart3 hide:)
			(ThrownDart4 hide:)
			(ThrownDart5 hide:)
			(ThrownDart6 hide:)
		)
		(if (== currentThrower 1)   ; computer throwing
			; FormatPrint("Throw %d" roundThrowCount)
			(Wait (Random 60 120))
			; One in 10 throws are 'wild'
			(if (== (Random 0 10) 0)
				(= wildX (Random -20 20))
				(= wildY (Random -20 20))
			)
			(= wildX 0)
			(= wildY 0)
			(cond 
				((== computerPoints gameInitPoints)  ; opening throw
					(if (== computerSkill 0)
						(ThrowDart
							hitX: (Random (+ wildX [openPro 0]) (+ wildY [openPro 2]))
						)
						(ThrowDart
							hitY: (Random (+ wildX [openPro 1]) (+ wildY [openPro 3]))
						)
					)
					(if (== computerSkill 1)
						(ThrowDart
							hitX: (Random (+ wildX [openAvg 0]) (+ wildY [openAvg 2]))
						)
						(ThrowDart
							hitY: (Random (+ wildX [openAvg 1]) (+ wildY [openAvg 3]))
						)
					)
					(if (== computerSkill 2)
						(ThrowDart
							hitX: (Random (+ wildX [openBad 0]) (+ wildY [openBad 2]))
						)
						(ThrowDart
							hitY: (Random (+ wildX [openBad 1]) (+ wildY [openBad 3]))
						)
					)
				)
				((<= computerPoints 40)     ; closing throw
					; if the number is odd, aim for the 1
					(if (< (* (/ computerPoints 2) 2) computerPoints)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [single1 0] missPixels))
									(+ wildX missPixels [single1 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [single1 1] missPixels))
									(+ wildY missPixels [single1 1])
								)
						)
					)
					(if (== computerPoints 40)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double20 0] missPixels))
									(+ wildX missPixels [double20 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double20 1] missPixels))
									(+ wildY missPixels [double20 1])
								)
						)
					)
					(if (== computerPoints 38)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double19 0] missPixels))
									(+ wildX missPixels [double19 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double19 1] missPixels))
									(+ wildY missPixels [double19 1])
								)
						)
					)
					(if (== computerPoints 36)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double18 0] missPixels))
									(+ wildX missPixels [double18 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double18 1] missPixels))
									(+ wildY missPixels [double18 1])
								)
						)
					)
					(if (== computerPoints 34)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double17 0] missPixels))
									(+ wildX missPixels [double17 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double17 1] missPixels))
									(+ wildY missPixels [double17 1])
								)
						)
					)
					(if (== computerPoints 32)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double16 0] missPixels))
									(+ wildX missPixels [double16 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double16 1] missPixels))
									(+ wildY missPixels [double16 1])
								)
						)
					)
					(if (== computerPoints 30)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double15 0] missPixels))
									(+ wildX missPixels [double15 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double15 1] missPixels))
									(+ wildY missPixels [double15 1])
								)
						)
					)
					(if (== computerPoints 28)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double14 0] missPixels))
									(+ wildX missPixels [double14 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double14 1] missPixels))
									(+ wildY missPixels [double14 1])
								)
						)
					)
					(if (== computerPoints 26)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double13 0] missPixels))
									(+ wildX missPixels [double13 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double13 1] missPixels))
									(+ wildY missPixels [double13 1])
								)
						)
					)
					(if (== computerPoints 24)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double12 0] missPixels))
									(+ wildX missPixels [double12 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double12 1] missPixels))
									(+ wildY missPixels [double12 1])
								)
						)
					)
					(if (== computerPoints 22)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double11 0] missPixels))
									(+ wildX missPixels [double11 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double11 1] missPixels))
									(+ wildY missPixels [double11 1])
								)
						)
					)
					(if (== computerPoints 20)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double10 0] missPixels))
									(+ wildX missPixels [double10 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double10 1] missPixels))
									(+ wildY missPixels [double10 1])
								)
						)
					)
					(if (== computerPoints 18)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double9 0] missPixels))
									(+ wildX missPixels [double9 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double9 1] missPixels))
									(+ wildY missPixels [double9 1])
								)
						)
					)
					(if (== computerPoints 16)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double8 0] missPixels))
									(+ wildX missPixels [double8 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double8 1] missPixels))
									(+ wildY missPixels [double8 1])
								)
						)
					)
					(if (== computerPoints 14)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double7 0] missPixels))
									(+ wildX missPixels [double7 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double7 1] missPixels))
									(+ wildY missPixels [double7 1])
								)
						)
					)
					(if (== computerPoints 12)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double6 0] missPixels))
									(+ wildX missPixels [double6 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double6 1] missPixels))
									(+ wildY missPixels [double6 1])
								)
						)
					)
					(if (== computerPoints 10)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double5 0] missPixels))
									(+ wildX missPixels [double5 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double5 1] missPixels))
									(+ wildY missPixels [double5 1])
								)
						)
					)
					(if (== computerPoints 8)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double4 0] missPixels))
									(+ wildX missPixels [double4 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double4 1] missPixels))
									(+ wildY missPixels [double4 1])
								)
						)
					)
					(if (== computerPoints 6)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double3 0] missPixels))
									(+ wildX missPixels [double3 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double3 1] missPixels))
									(+ wildY missPixels [double3 1])
								)
						)
					)
					(if (== computerPoints 4)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double2 0] missPixels))
									(+ wildX missPixels [double2 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double2 1] missPixels))
									(+ wildY missPixels [double2 1])
								)
						)
					)
					(if (== computerPoints 2)
						(ThrowDart
							hitX:
								(Random
									(+ wildX (- [double1 0] missPixels))
									(+ wildX missPixels [double1 0])
								)
						)
						(ThrowDart
							hitY:
								(Random
									(+ wildY (- [double1 1] missPixels))
									(+ wildY missPixels [double1 1])
								)
						)
					)
				)
				(else                       ; normal throw
					(if (== computerSkill 0)
						(ThrowDart
							hitX: (Random (+ wildX [normalPro 0]) (+ wildX [normalPro 2]))
						)
						(ThrowDart
							hitY: (Random (+ wildY [normalPro 1]) (+ wildY [normalPro 3]))
						)
					)
					(if (== computerSkill 1)
						(ThrowDart
							hitX: (Random (+ wildX [normalAvg 0]) (+ wildX [normalAvg 2]))
						)
						(ThrowDart
							hitY: (Random (+ wildY [normalAvg 1]) (+ wildY [normalAvg 3]))
						)
					)
					(if (== computerSkill 2)
						(ThrowDart
							hitX: (Random (+ wildX [normalBad 0]) (+ wildX [normalBad 2]))
						)
						(ThrowDart
							hitY: (Random (+ wildY [normalBad 1]) (+ wildY [normalBad 3]))
						)
					)
				)
			)

			(ThrowDart Compute:)
			(ThrowDart updateDisplay:)
		)  ; end computer throwing
		(if (not intro)
			(if
				(and
					(!= gCurrentCursor 995)
					(== currentThrower 0)
					(!= roundThrowCount 6)
					(!= roundThrowCount 3)
				)
				(SetCursor 995 TRUE)
			)                       ; players turn, set the cursor to the 'target'
			(if
			(and (!= gCurrentCursor 997) (== currentThrower 1))
				(SetCursor 997 TRUE)
			)
		else                        ; computers turn set the cursor to the 'wait' & disable control
			(SetCursor 999 (HaveMouse))
			(= gCurrentCursor 999)
		)
	)
	
	(method (handleEvent pEvent &tmp button)
		(super handleEvent: pEvent)
		(if
			(and
				(> (pEvent x?) (Exit nsLeft?))
				(< (pEvent x?) (Exit nsRight?))
				(> (pEvent y?) (Exit nsTop?))
				(< (pEvent y?) (Exit nsBottom?))
				(== (pEvent type?) evMOUSEBUTTON)
				
			)
			(= button
				(Print
					111
					15
					#button
					{ Yes_}
					0
					#button
					{ No_}
					1
					#button
					{Instructions}
					2
				)
			)
			(switch button
				(0
					(= gArcStl 0)
					(gRoom newRoom: gPreviousRoomNumber)
				)
				(2
					(Print 111 1 #title {The Board}) ; "The red circle in the middle is called the 'bulls eye' and is worth 50 points. It counts as a double.\n\nThe green cicle surrounding it is worth 25 points.\n\nThe numbers 1 through 20 placed around the board are the nominals for darts hitting the respective fields or sectors."
					(Print 111 2 #title {The Board}) ; "The outer thin ring closest to the actual numbers are doubles, and worth double the specified nominal of their sector.\n\nThe inner thin ring is worth three times the nominal score, so the highest possible score on a single dart would be triple 20 (the small red field halfway between the number 20 and bullseye) that is worth 60 points."
					(Print 111 3 #title {Beginning play: Doubling In}) ; "Players will throw three darts per turn, in rotation. Each player must first land one of his darts in the narrow 'double' ring around the outer edge of the numbered pie-shaped wedges, or in the red 'double bull' before any of his points will be scored."
					(Print 111 4 #title {Winning the game: Doubling Out}) ; "A player must end up with exactly zero points to go 'out' and win. The final dart must be in the outer double ring or the inner double bull. If, in the course of throwing three darts, a player's score becomes negative, he is said to 'bust.' None of his darts count for that round and his turn ends."
					(Print 111 5 #title {Winning the game: Doubling Out}) ; "Also, if his score is reduced to 1 point in the course of his turn, this is also a bust, as it is not possible to double out with only 1 point remaining. Finally, a player will bust if he reaches exactly zero without scoring a double on his final dart."
					(Print 111 6 #title {Dart throwing})
				)
			)
		)
		(if (== currentThrower 0)
			(ThrowDart leftClicked: pEvent)   ; let the player interactively throw
			; After the dart is thrown, update the display
			(if
				(and
					(> (ThrowDart mouseUpX?) -1)
					(> (ThrowDart mouseUpY?) -1)
				)
				(ThrowDart updateDisplay:)
			)
		)
	)
)

(instance Scoreboard of Prop
	(properties
		view 583
		loop 3
		x 285
		y 78
		priority 15
	)
)

(instance Exit of Prop
	(properties
		y 182
		x 30
		view 158
	)
)

; players darts
(instance ThrownDart1 of Prop
	(properties
		view 583
		loop 2
	)
)

(instance ThrownDart2 of Prop
	(properties
		view 583
		loop 2
	)
)

(instance ThrownDart3 of Prop
	(properties
		view 583
		loop 2
	)
)

; computer darts
(instance ThrownDart4 of Prop
	(properties
		view 583
		loop 1
	)
)

(instance ThrownDart5 of Prop
	(properties
		view 583
		loop 1
	)
)

(instance ThrownDart6 of Prop
	(properties
		view 583
		loop 1
	)
)

(instance DartHand of Act
	(properties
		x 160
		y 160
	)
	
	(method (init)
		(super init:)
		(self setScript: dartScript)
	)
)

(instance dartScript of Script
	(properties)
	
	(method (doit &tmp myEvent)
		(super doit:)
		(= myEvent (Event new: evNULL))
		(client setMotion: MoveTo (+ (myEvent x?) 19) 160)
		(myEvent dispose:)
	)
)

(class ThrowDart
	(properties
		mouseDownX -1
		mouseDownY -1
		mouseDownTime -1
		mouseUpX -1
		mouseUpY -1
		mouseUpTime -1
		accuracy -1
		velocity -1
		lineLen -1
		hitX -1      ; x-coord of where dart hit
		hitY -1      ; y-coord of where dart hit
		hitPoints -1 ; points earned
		hitColor -1  ; control color
		hitArea -1   ; 1, 2 or 3 (single, double, triple)
	)
	
	(method (leftClicked pEvent)
		(if (not (self Throw: pEvent))
			; bad throw, reset all values
			(= mouseDownX -1)
			(= mouseDownY -1)
			(= mouseUpX -1)
			(= mouseUpY -1)
			(return TRUE)
		)
		; = hitX (send pEvent:x)   // testing
		; = hitY (send pEvent:y)   // testing
		(self Compute:)
	)
	
	; (self:updateDisplay())
	(method (Compute &tmp onColor)
		(= onColor (OnControl ocSPECIAL hitX hitY))
		(= hitPoints -1)
		(= hitColor -1)
		(= hitArea -1)
		; doubles, triples, bulls
		(if
		(or (== onColor ctlGREEN) (== onColor ctlMAROON))
			; bulls
			(if
				(and
					(> hitX 145)
					(< hitX 166)
					(> hitY 85)
					(< hitY 106)
				)
				(if (== onColor ctlGREEN)
					(= hitPoints 25)
					(= hitArea 1)
				else
					(= hitPoints 50)
					(= hitArea 2)
				)                ; center bullseye counts as a double
				(= hitColor onColor)
				(return TRUE)
			)
			; triples
			(if
				(and
					(> hitX 100)
					(< hitX 212)
					(> hitY 43)
					(< hitY 146)
				)
				(if (== onColor ctlGREEN) ; trip green
					(if (< hitX 108) (= hitPoints 11))
					(if (> hitX 203) (= hitPoints 6))
					(if (and (< hitX 121) (< hitY 74)) (= hitPoints 9))
					(if (and (< hitX 121) (> hitY 115)) (= hitPoints 16))
					(if (and (< hitX 149) (< hitY 54)) (= hitPoints 5))
					(if (and (< hitX 149) (> hitY 135)) (= hitPoints 19))
					(if (and (> hitX 190) (< hitY 74)) (= hitPoints 4))
					(if (and (> hitX 190) (> hitY 115)) (= hitPoints 15))
					(if (and (> hitX 163) (< hitY 54)) (= hitPoints 1))
					(if (and (> hitX 163) (> hitY 135)) (= hitPoints 17))
				else  ; trip maroon
					(if (< hitY 50) (= hitPoints 20))
					(if (> hitY 139) (= hitPoints 3))
					(if (and (< hitX 113) (< hitY 88)) (= hitPoints 14))
					(if (and (< hitX 113) (> hitY 100)) (= hitPoints 8))
					(if (and (< hitX 136) (< hitY 63)) (= hitPoints 12))
					(if (and (< hitX 136) (> hitY 126)) (= hitPoints 7))
					(if (and (> hitX 176) (< hitY 63)) (= hitPoints 18))
					(if (and (> hitX 176) (> hitY 126)) (= hitPoints 2))
					(if (and (> hitX 199) (< hitY 88)) (= hitPoints 13))
					(if (and (> hitX 199) (> hitY 100)) (= hitPoints 10))
				)
				(= hitPoints (* hitPoints 3))
				(= hitColor onColor)
				(= hitArea 3)
				(return TRUE)
			)
			; doubles
			(if (== onColor ctlGREEN) ; double green
				(if (< hitX 77) (= hitPoints 11))
				(if (> hitX 232) (= hitPoints 6))
				(if (and (< hitX 100) (< hitY 61)) (= hitPoints 9))
				(if (and (< hitX 100) (> hitY 127)) (= hitPoints 16))
				(if (and (< hitX 145) (< hitY 30)) (= hitPoints 5))
				(if (and (< hitX 145) (> hitY 160)) (= hitPoints 19))
				(if (and (> hitX 167) (< hitY 30)) (= hitPoints 1))
				(if (and (> hitX 167) (> hitY 160)) (= hitPoints 17))
				(if (and (> hitX 210) (< hitY 61)) (= hitPoints 4))
				(if (and (> hitX 210) (> hitY 127)) (= hitPoints 15))
			else   ; double maroon
				(if (< hitY 22) (= hitPoints 20))
				(if (> hitY 168) (= hitPoints 3))
				(if (and (< hitX 87) (< hitY 86)) (= hitPoints 14))
				(if (and (< hitX 87) (> hitY 105)) (= hitPoints 8))
				(if (and (< hitX 122) (< hitY 44)) (= hitPoints 12))
				(if (and (< hitX 122) (> hitY 145)) (= hitPoints 7))
				(if (and (> hitX 190) (< hitY 44)) (= hitPoints 18))
				(if (and (> hitX 190) (> hitY 145)) (= hitPoints 2))
				(if (and (> hitX 223) (< hitY 86)) (= hitPoints 13))
				(if (and (> hitX 223) (> hitY 105)) (= hitPoints 10))
			)
			(= hitPoints (* hitPoints 2))
			(= hitColor onColor)
			(= hitArea 2)
			(return TRUE)
		)
		; singles
		(if (< hitY 95)
			(switch onColor
				(ctlBLUE (= hitPoints 12))
				(ctlYELLOW (= hitPoints 5))
				(ctlNAVY (= hitPoints 20))
				(ctlGREY (= hitPoints 1))
				(ctlPURPLE (= hitPoints 18))
			)
		else
			(switch onColor
				(ctlBLUE (= hitPoints 2))
				(ctlYELLOW (= hitPoints 17))
				(ctlNAVY (= hitPoints 3))
				(ctlGREY (= hitPoints 19))
				(ctlPURPLE (= hitPoints 7))
			)
		)
		(if (< hitX 156)
			(switch onColor
				(ctlLIME (= hitPoints 16))
				(ctlBROWN (= hitPoints 8))
				(ctlCYAN (= hitPoints 11))
				(ctlSILVER (= hitPoints 14))
				(ctlRED (= hitPoints 9))
			)
		else
			(switch onColor
				(ctlLIME (= hitPoints 4))
				(ctlBROWN (= hitPoints 13))
				(ctlCYAN (= hitPoints 6))
				(ctlSILVER (= hitPoints 10))
				(ctlRED (= hitPoints 15))
			)
		)
		(= hitColor onColor)
	)
	
	(method (Throw pEvent &tmp offsetX offsetY)
		; difference between upper left corner of mouse cursor & the center
		(= offsetX 9)
		(= offsetY 11)
		(if
			(and
				(== (pEvent type?) evMOUSEBUTTON)
				;(== currentThrower 0)
				(not (& (pEvent modifiers?) emRIGHT_BUTTON))
			)                                                                          ; left-click, mouse down
			(= mouseDownX (+ offsetX (pEvent x?)))
			(= mouseDownY (+ offsetY (pEvent y?)))
			(= mouseDownTime (GetTime))
			; reset the mouse up values
			(= mouseUpX -1)
			(= mouseUpY -1)
			; display the dart hand
			(DartHand x: (+ mouseDownX 11))
			; (DartHand:y( - mouseDownY 10))  // put the hand under the mouse cursor
			(DartHand y: 150) ; pin the hand to bottom of the screen
			; switch throwers
			(if (== currentThrower 0) ; player
				(DartHand view: 586)
			else                      ; computer
				(DartHand view: 585)
			)
			(DartHand show:)
			(pEvent claimed: TRUE)
			(return TRUE)
		)
		(if
			(and
				(== (pEvent type?) evMOUSERELEASE)
				;(== currentThrower 0)
				(not (& (pEvent modifiers?) emRIGHT_BUTTON))
			)                                                                           ; left-click, mouse up
			(= mouseUpX (+ offsetX (pEvent x?)))
			(= mouseUpY (+ offsetY (pEvent y?)))
			(= mouseUpTime (GetTime))
			(= accuracy (- mouseDownX mouseUpX))
			(= velocity
				(/
					(Abs (- mouseDownY mouseUpY))
					(- mouseUpTime mouseDownTime)
				)
			)
			
			; switch to the dartless hand
			(DartHand view: 584)
			; quitely do nothing if the throw is too wimpy
			(if
				(or
					(<
						(GetDistance mouseDownX mouseDownY mouseUpX mouseUpY)
						20
					)
					(== velocity 0)
				)
				; = throwHarder TRUE
				(pEvent claimed: TRUE)
				(return FALSE)
			)
			(if (< 10 velocity) (= velocity 10))
			(= hitX (+ (* accuracy -2) mouseUpX))
			; this works for both throwing 'up' & 'down' the screen
			(= hitY
				(+
					(/
						(* (- mouseUpY mouseDownY) [velocities velocity])
						100
					)
					mouseUpY
				)
			)
			; make sure that the hitX & hitY are reasonable....
			(if (> hitX 320) (= hitX 320))
			(if (< hitX 0) (= hitX 0))
			(if (> hitY 200) (= hitY 200))
			(if (< hitY 0) (= hitY 0))
			; for display, 'normalize' the accuracy & velocity values
			(if (> accuracy 10) (= accuracy 10))
			(if (< accuracy -10) (= accuracy -10))
			(= accuracy (- 10 (Abs accuracy)))
			(= velocity (Abs velocity))
			(pEvent claimed: TRUE)
			(return TRUE)
		)
		(return FALSE)
	)
	
	(method (updateDisplay)
		; increment the throw count
		(++ throwCount)
		; increment the round throw count
		(++ roundThrowCount)
		(switch roundThrowCount
			(1
				(ThrownDart1 x: hitX)
				(ThrownDart1 y: (+ hitY 5))
				(ThrownDart1 show:)
			)
			(2
				(ThrownDart2 x: hitX)
				(ThrownDart2 y: (+ hitY 5))
				(ThrownDart2 show:)
			)
			(3
				(ThrownDart3 x: hitX)
				(ThrownDart3 y: (+ hitY 5))
				(ThrownDart3 show:)
			)
			(4
				(ThrownDart4 x: hitX)
				(ThrownDart4 y: (+ hitY 5))
				(ThrownDart4 show:)
			)
			(5
				(ThrownDart5 x: hitX)
				(ThrownDart5 y: (+ hitY 5))
				(ThrownDart5 show:)
			)
			(6
				(ThrownDart6 x: hitX)
				(ThrownDart6 y: (+ hitY 5))
				(ThrownDart6 show:)
			)
		)
		; if player missed, set points to zero
		(if (== hitPoints -1) (= hitPoints 0))
		; compute points for this throw
		(cond 
			((== currentThrower 0)
				(if (> hitPoints 0)
					(if (<= (- playerPoints hitPoints) 1)    ; end of round/game checking
						(if
						(and (== (- playerPoints hitPoints) 0) (== hitArea 2))   ; score is zero & hit a double
							(= roundThrowCount 3)
							(= playerWon TRUE)
						else
							(= playerBusted TRUE)
							(= roundThrowCount 3)
						)
						; (Dart:hitPoints(0))  // Don't do this, we need to be able to revert the round points
						(= throwCount 3)
					)                   ; end the round
					(if
					(and (== playerPoints gameInitPoints) (!= hitArea 2))  ; must open with a double, no points for you!
						(= hitPoints 0)
					)
					(= playerPoints (- playerPoints hitPoints))
				)
			)
			((> hitPoints 0)
				(if (<= (- computerPoints hitPoints) 1)        ; end of round/game checking
					(if
						(and
							(== (- computerPoints hitPoints) 0)
							(== hitArea 2)
						)                                                        ; score is zero & hit a double
						(= roundThrowCount 6)
						(= computerWon TRUE)
					else
						(= computerBusted TRUE)
						(= roundThrowCount 6)
					)
					; (Dart:hitPoints(0)) // Don't do this, we need to be able to revert the r
					(= throwCount 3)
				)                       ; end the round
				(if
				(and (== computerPoints gameInitPoints) (!= hitArea 2))      ; must open with a double, no points for you!
					(= hitPoints 0)
				)
				(= computerPoints (- computerPoints hitPoints))
				; end of game checking
				(if (== computerPoints 0) (= computerWon TRUE))
			)
		)
		(= currentRoundPoints (+ currentRoundPoints hitPoints))
		; Make sure we didn't get 1 or less (and nobody won).  If we did, revert all the points for the round
		(cond 
			((== currentThrower 0)     ; end of players round
				(if (and (<= playerPoints 1) (== playerWon FALSE))
					(= playerPoints (+ playerPoints currentRoundPoints))
				)
			)
			(
			(and (<= computerPoints 1) (== computerWon FALSE)) (= computerPoints (+ computerPoints currentRoundPoints))) ; end of computers round
		)
		; update the scoreboard
		(if (== currentThrower 0)
			(Display {} dsRESTOREPIXELS pScorePix)
			(Format @playerPointsDisp {%d} playerPoints)
			(= pScorePix
				(Display
					@playerPointsDisp
					dsCOORD
					270
					32
					dsCOLOUR
					clRED
					dsBACKGROUND
					clTRANSPARENT
					dsALIGN
					alCENTER
					dsWIDTH
					30
					dsFONT
					3
					dsSAVEPIXELS
				)
			)
		else
			(Display {} dsRESTOREPIXELS cScorePix)
			(Format @computerPointsDisp {%d} computerPoints)
			(= cScorePix
				(Display
					@computerPointsDisp
					dsCOORD
					270
					57
					dsCOLOUR
					clNAVY
					dsBACKGROUND
					clTRANSPARENT
					dsALIGN
					alCENTER
					dsWIDTH
					30
					dsFONT
					3
					dsSAVEPIXELS
				)
			)
		)
		; reset the dart
		; (Dart:mouseUpX(-1))
		; (Dart:mouseUpY(-1))
		(= mouseUpX -1)
		(= mouseUpY -1)
		; end of players round
		(if (== throwCount 3)
			(if (== currentThrower 0)
				(= currentThrower 1)
			else
				(= currentThrower 0)
			)
			; reset for next round
			(= currentRoundPoints 0)
			(= throwCount 0)
		)
	)
	
	(method (resetDarts)
		; end of round (both players have thrown)
		(if (== roundThrowCount 6)
			(= roundThrowCount 0)
			(ThrownDart1 hide:)
			(ThrownDart2 hide:)
			(ThrownDart3 hide:)
			(ThrownDart4 hide:)
			(ThrownDart5 hide:)
			(ThrownDart6 hide:)
		)
	)
)
