;;; Sierra Script 1.0 - (do not remove this comment)
; + 3 SCORE // gInt +3 //
(script# 38)
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
	rm038 0
)

(local

; Booby-Trapped Cave - Top Level



	LN =  1           ; Level Number
	CN =  1           ; Column Number
	column
	level
; 1-2-3-4-5-6-7-8-9-10-11-12
	block1posn =  6
; Y-O-U-_-M-A-Y-_-P-A--S--S-
	block2posn =  3

	block3posn =  4
; Visual represetation of positions
	block4posn =  5

	block5posn =  1
; |----------------------------- |
	block6posn =  11
; |   A   |   U   |      |   M   |
	block7posn =  9
; |------------------------------|
	block8posn =  2
; |   Y   |   S   |  P   |  O    |
	block9posn =  8
; |------------------------------|
	block10posn =  7
; |       |   Y   |  A   |   S   |
	block11posn =  10
; |------------------------------|
	block12posn =  12

	myEvent
	leversVis =  0
	quadrant
	changer
	solved =  0	; floor puzzle sovled
	exit =  0
	quicksand =  0
	climbing =  0
	[stringName 20] ; for use with the goggles
	torchInSconce =  0
	walkBack =  0
)                ; ego walks back if he doesn't have the torch

(instance rm038 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 204)
		(switch gPreviousRoomNumber
			(37 (gEgo posn: 45 126 loop: 3))
			(else 
				(gEgo posn: 45 126 loop: 3)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		; (glow:init()setCycle(Fwd)cycleSpeed(2)hide()setScript(sandScript))
		(glow
			init:
			setCycle: Fwd
			cycleSpeed: 4
			hide:
			setScript: sandScript
			setPri: 1
		)
		(glow2 init: setCycle: Fwd cycleSpeed: 4 hide: setPri: 0)
		(torch init:)
		(lever init: setScript: climbScript)
		(sand
			init:
			hide:
			ignoreActors:
			setScript: leverScript
			setPri: 2
		)
		(bow init: hide: ignoreActors:)
		(if g38Solved
; 1-2-3-4-5-6-7-8-9-10-11-12
			(= block1posn 1)
; Y-O-U-_-M-A-Y-_-P-A--S--S-
			(= block2posn 2)

			(= block3posn 3)
; Visual represetation of positions
			(= block4posn 4)

			(= block5posn 5)
; |----------------------------- |
			(= block6posn 6)
; |   A   |   U   |      |   M   |
			(= block7posn 7)
; |------------------------------|
			(= block8posn 8)
; |   Y   |   S   |  P   |  O    |
			(= block9posn 9)
; |------------------------------|
			(= block10posn 10)
; |       |   Y   |  A   |   S   |
			(= block11posn 11)
; |------------------------------|
			(= block12posn 12)
		)

		(block1 init: ignoreActors: setPri: 2 cel: block1posn)
		(block2 init: ignoreActors: setPri: 2 cel: block2posn)
		(block3 init: ignoreActors: setPri: 2 cel: block3posn)
		(block4 init: ignoreActors: setPri: 2 cel: block4posn)
		(block5 init: ignoreActors: setPri: 2 cel: block5posn)
		(block6 init: ignoreActors: setPri: 2 cel: block6posn)
		(block7 init: ignoreActors: setPri: 2 cel: block7posn)
		(block8 init: ignoreActors: setPri: 2 cel: block8posn)
		(block9 init: ignoreActors: setPri: 2 cel: block9posn)
		(block10 init: ignoreActors: setPri: 2 cel: block10posn)
		(block11 init: ignoreActors: setPri: 2 cel: block11posn)
		(block12 init: ignoreActors: setPri: 2 cel: block12posn)
		(if g38Solved (= solved 1))
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		(if (not torchInSconce)
			(if (not quicksand)
				(if (not climbing)
					(switch (gEgo loop?)
						(0          ; faceing right
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
						(1          ; facing left
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
				else
					(glow hide:)
					(glow2 hide:)
				)
			else
				(glow hide:)
				(glow2 hide:)
			)
		else   ; torch in sconce
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
		(if (& (gEgo onControl:) ctlMAROON)
			(if torchInSconce
				(if (not walkBack)
					(climbScript changeState: 5)
					(= walkBack 1)
				)
			else
				(gRoom newRoom: 37)
			)
		)
		(if (not solved)
			(if (& (gEgo onControl:) ctlSILVER)
				(if (not quicksand) (sandScript changeState: 1))
			)
		)
		(= myEvent (Event new: evNULL))
		(if leversVis
			(gEgo setMotion: NULL)
			(cond 
				((checkEvent myEvent buttons1)
; (if((> (send myEvent:x) (buttons1:nsLeft))and
;                (< (send myEvent:x) (buttons1:nsRight))and
;                (> (send myEvent:y) (buttons1:nsTop))and
;                (< (send myEvent:y) (buttons1:nsBottom)))
					(select init: show: cel: 0 posn: 40 84 setPri: 15)
					(= quadrant 3)
					(= exit 0)
				)
				((checkEvent myEvent buttons2)
; (if((> (send myEvent:x) (buttons2:nsLeft))and
;                    (< (send myEvent:x) (buttons2:nsRight))and
;                    (> (send myEvent:y) (buttons2:nsTop))and
;                    (< (send myEvent:y) (buttons2:nsBottom))))
					(select init: show: cel: 0 posn: 75 84 setPri: 15)
					(= quadrant 4)
					(= exit 0)
				)
				((checkEvent myEvent lever1)
					(select init: show: cel: 1 posn: 40 52 setPri: 15)
					(= quadrant 2)
					(= exit 0)
				)
				((checkEvent myEvent lever2)
					(select init: show: cel: 1 posn: 75 52 setPri: 15)
					(= quadrant 1)
					(= exit 0)
				)
				((checkEvent myEvent close) (= exit 1) (= quadrant 0))
				(else
					(writing hide:)
					(select hide:)
					(= quadrant 0)
					(= exit 1)
				)
			)
		)
		(myEvent dispose:)
		(buttons2 cel: (- LN 1))
		(buttons1 cel: (- CN 1))
		(block1 cel: block1posn)
		(block2 cel: block2posn)
		(block3 cel: block3posn)
		(block4 cel: block4posn)
		(block5 cel: block5posn)
		(block6 cel: block6posn)
		(block7 cel: block7posn)
		(block8 cel: block8posn)
		(block9 cel: block9posn)
		(block10 cel: block10posn)
		(block11 cel: block11posn)
		(block12 cel: block12posn)
		; (if(not(solved))
		(if (or (== block1posn 1) (== block1posn 7))
			(if (== block2posn 2)
				(if (== block3posn 3)
					(if (or (== block4posn 4) (== block4posn 8))
						(if (== block5posn 5)
							(if (or (== block6posn 6) (== block6posn 10))
								(if (or (== block7posn 7) (== block7posn 1))
									(if (or (== block8posn 4) (== block8posn 8))
										(if (== block9posn 9)
											(if (or (== block10posn 10) (== block10posn 6))
												(if (or (== block11posn 11) (== block11posn 12))
													(if (or (== block12posn 11) (== block12posn 12))
														(if (not solved)
															(sandScript changeState: 7)
															(if (not g38Solved)
																(= solved 1)
																(= g38Solved 1)
																(= gInt (+ gInt 3))
																(gGame changeScore: 3)
															else
																(= solved 1)
															)
														)
													else
														(broke)
													)
												else
													(broke)
												)
											else
												(broke)
											)
										else
											(broke)
										)
									else
										(broke)
									)
								else
									(broke)
								)
							else
								(broke)
							)
						else
							(broke)
						)
					else
						(broke)
					)
				else
					(broke)
				)
			else
				(broke)
			)
		)
	)
	
	; )
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (Said '(pick<up),take>')
			(if (Said '/(torch,light,candle)')
				(if torchInSconce
					(if (<= (gEgo distanceTo: torch) 40)
						(PrintOK)
						(= torchInSconce 0)
						(torch loop: 6)
					else
						(PrintNCE)
					)
				else
					(PrintOther 37 1)
				)
			)
		)
; You already have it.
		(if (Said 'put,place,return,replace/torch')
			(if (not torchInSconce)
				(if (<= (gEgo distanceTo: torch) 40)
					(PrintOK)
					(= torchInSconce 1)
					(torch loop: 5 setCycle: Fwd cycleSpeed: 2 setPri: 4)
				else
					(PrintNCE)
				)
			else
				(PrintOther 37 6)
			)
		)
; You don't have it.
		(if (or (Said 'climb') (Said 'use/ladder'))
			(if (& (gEgo onControl:) ctlRED)
				(if torchInSconce
					(climbScript changeState: 1)
				else
					(climbScript changeState: 7)
				)
			else
				(PrintNCE)
			)
		)
		(if (Said '(put<on),use,wear/goggle')
			(if (gEgo has: INV_GOGGLES)
				(if (not solved)
					(closeControls)
					(EditPrint
						@stringName
						14
						{Which phrase would you like to locate?}
						#at
						-1
						20
					)
					(if (== (StrCmp @stringName {you may pass}) 0)
						(PrintOther 38 3) ; #width 280 #at -1 150) // Your goggles search the letters on the floor for any combination of letters spelling 'you may pass'.
						(PrintOther 38 4) ; #width 280 #at -1 150) //Through the miracle of science you know with scientific precision the correct combination, which you enter happily.
						(leverScript changeState: 5)
					else
						(PrintOther 38 5)
					)                    ; Nothing happens. There must be a different phrase.
					(= stringName 0)
				else
					(PrintOther 38 20)
				)
			else
				(PrintOther 38 6)
			)
		)                         ; Nice idea, but you don't have any goggles.
		(if (Said '(look,play,use)/(panel,control,puzzle)')
			(if (not solved)
				(if (<= (gEgo distanceTo: lever) 45)
					(if (not leversVis)
						(buttons1 init: show: setPri: 15)
						(buttons2 init: show: setPri: 15)
						(lever1 init: show: setPri: 15)
						(lever2 init: show: setPri: 15)
						(close init: show: posn: 97 22 setPri: 15)
						(= leversVis 1)
						(= gMap 1)
					)
				else
					(PrintOther 38 7)
				)
			else
				(PrintOther 38 20)
			)
		)                            ; #width 280 #at -1 150) // There look to be some controls for the letters on the floor.
		(if (Said 'use/map') (Print 38 8)) ; This isn't a good place to use that.
		(if (Said 'look,read>')
			(if (Said '/paper,note,letter,sign')
				(Print
					38
					0
					#title
					{It reads:}
					#width
					100
					#font
					4
					#at
					120
					-1
				)
				(if (not gHardMode)
					(Print 38 2 #width 280 #at -1 150 #title {Hint:})
				)
			)
			(if (Said '/floor,tile,sand')
				(if g38Solved
					(PrintOther 38 19)
				else
					(Print 38 9 #title {You think:} #at -1 150)
				)
			)                                              ; There looks to be a puzzle on the floor ahead. Perhpas I need to spell a phrase.
			(if (Said '/wall') (PrintOther 38 10)) ; #at -1 150) //On the wall you see some controls which you assume must be for the floor puzzle. To the left you see a note.
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(if g38Solved
					(PrintOther 38 11)
				else                  ; #width 280 #at -1 150)
					(PrintOther 38 12)
				)
			)           
		)                         
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if exit (closeControls))
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (checkEvent pEvent sand) (PrintOther 38 9))
				(if (checkBox pEvent 93 100 74 84)
; (if((> (send pEvent:x) (69))and   // paper
;                    (< (send pEvent:x) (79))and
;                    (> (send pEvent:y) (71))and
;                    (< (send pEvent:y) (83)))
					(PrintOther 38 0)
					(if (not gHardMode)
						(Print 38 2 #width 280 #at -1 150 #title {Hint:})
					)
				)
				(if (checkBox pEvent 258 286 7 74) (PrintOther 38 14))

				(if (checkEvent pEvent lever)
					(if (<= (gEgo distanceTo: lever) 45)
						(if (not solved)
							(if (not leversVis)
								(buttons1 init: show: setPri: 15)
								(buttons2 init: show: setPri: 15)
								(lever1 init: show: setPri: 15)
								(lever2 init: show: setPri: 15)
								(close init: show: posn: 97 22 setPri: 15)
								(= leversVis 1)
								(= gMap 1)
							)
						else
							(PrintOther 38 20)
						)
					else
						(PrintOther 38 7)
					)
				)
			)                            ; #at -1 150) //"There look to be some controls for the letters on the floor." #at -1 150)
			(switch quadrant
				(1
					(= column 0)
					(= level 1)
					(leverScript changeState: 3)
					(shift)
				)
				(2
					(= level 0)
					(= column 1)
					(leverScript changeState: 1)
					(shift)
				)
				(3          ; columns
					(++ CN)
					(if (== CN 5) (= CN 1))
				)
				(4          ; rows
					(++ LN)
					(if (== LN 4) (= LN 1))
				)
			)
		)
	)
)

(procedure (closeControls)
	(buttons1 hide:)
	(buttons2 hide:)
	(lever1 hide:)
	(lever2 hide:)
	(close hide:)
	(writing hide:)
	(select hide:)
	(PlayerControl)
	(SetCursor 999 (HaveMouse))
	(= gCurrentCursor 999)
	(= leversVis 0)
	(= quadrant 0)
	(= level 0)
	(= column 0)
	(= gMap 0)
)

(procedure (broke)
	(if solved (sandScript changeState: 10) (= solved 0))
)

(procedure (shift)
	(cond 
		(level
			(switch LN
				(1
					(= changer block4posn)
					(= block4posn block3posn)
					(= block3posn block2posn)
					(= block2posn block1posn)
					(= block1posn changer)
				)
				(2
					(= changer block8posn)
					(= block8posn block7posn)
					(= block7posn block6posn)
					(= block6posn block5posn)
					(= block5posn changer)
				)
				(3
					(= changer block12posn)
					(= block12posn block11posn)
					(= block11posn block10posn)
					(= block10posn block9posn)
					(= block9posn changer)
				)
			)
		)
		(column
			(switch CN
				(1
					(= changer block9posn)
					(= block9posn block5posn)
					(= block5posn block1posn)
					(= block1posn changer)
				)
				(2
					(= changer block10posn)
					(= block10posn block6posn)
					(= block6posn block2posn)
					(= block2posn changer)
				)
				(3
					(= changer block11posn)
					(= block11posn block7posn)
					(= block7posn block3posn)
					(= block3posn changer)
				)
				(4
					(= changer block12posn)
					(= block12posn block8posn)
					(= block8posn block4posn)
					(= block4posn changer)
				)
			)
		)
	)
)

(instance climbScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 272 80 self
					ignoreControl: ctlWHITE
				)
			)
			(2
				(if (gEgo has: 15)
					(gEgo put: 15 38)
					(PrintOther 38 16)
					(bow show:)
				)
				(= climbing 1)
				(gEgo hide:)
				(alterEgo
					init:
					show:
					view: 235
					posn: (gEgo x?) (gEgo y?)
					setCycle: Walk
					cycleSpeed: 0
					yStep: 4
					setMotion: MoveTo (gEgo x?) 28 self
					ignoreControl: ctlWHITE
					setPri: 14
				)
			)
			(3
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gRoom newRoom: 135)
			)
			; walking back without torch
			(5
				(ProgramControl)
				(gEgo
					setMotion: MoveTo (gEgo x?) (- (gEgo y?) 10) self
				)
			)
			(6
				(PlayerControl)
				(PrintOther 38 15)
				(= walkBack 0)
			)
			; Putting the torch up before climmbing
			(7
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 234 85 self
					ignoreControl: ctlWHITE
				)
			)
			(8 (= cycles 2) (gEgo loop: 3))
			(9
				(PrintOther 38 17)
				(torch loop: 5 setCycle: Fwd cycleSpeed: 2 setPri: 4)
				(= torchInSconce 1)
				(self changeState: 1)
				(gEgo put: 15 38)
				(bow show:)
			)
		)
	)
)

(instance leverScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(lever1 setCycle: End leverScript)
			)
			(2
				(lever1 setCycle: Beg)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(3
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(lever2 setCycle: End leverScript)
			)
			(4
				(lever2 setCycle: Beg)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(5
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					ignoreControl: ctlWHITE
					setMotion: MoveTo 85 108 leverScript
				)
			)
			(6 (= cycles 7) (gEgo loop: 3))
			(7
				(= cycles 9)
				(gEgo observeControl: ctlWHITE)
; 1-2-3-4-5-6-7-8-9-10-11-12
				(= block1posn 1)
; Y-O-U-_-M-A-Y-_-P-A--S--S-
				(= block2posn 2)

				(= block3posn 3)
; Visual represetation of positions
				(= block4posn 4)

				(= block5posn 5)
; |----------------------------- |
				(= block6posn 6)
; |   A   |   U   |      |   M   |
				(= block7posn 7)
; |------------------------------|
				(= block8posn 8)
; |   Y   |   S   |  P   |  O    |
				(= block9posn 9)
; |------------------------------|
				(= block10posn 10)
; |       |   Y   |  A   |   S   |
				(= block11posn 11)
; |------------------------------|
				(= block12posn 12)
			)

			(8
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
)

(instance sandScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(1
				(= quicksand 1)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(alterEgo
					init:
					show:
					view: 903
					posn: (gEgo x?) (gEgo y?)
					setMotion: MoveTo 167 115
				)
				(sand
					show:
					setCycle: End sandScript
					cycleSpeed: 2
					setPri: 2
				)
				(block1 hide:)
				(block2 hide:)
				(block3 hide:)
				(block4 hide:)
				(block5 hide:)
				(block6 hide:)
				(block7 hide:)
				(block8 hide:)
				(block9 hide:)
				(block10 hide:)
				(block11 hide:)
				(block12 hide:)
			)
			(2
				(sand loop: 8 setCycle: Fwd)
				(alterEgo
					view: 577
					setCycle: Fwd
					setMotion: MoveTo 169 169 sandScript
					ignoreControl: ctlWHITE
				)
			)
			(3
				(sand loop: 7 cel: 3 setCycle: Beg sandScript)
			)
			(4
				(= seconds 2)
				(sand hide:)
				(block1 show:)
				(block2 show:)
				(block3 show:)
				(block4 show:)
				(block5 show:)
				(block6 show:)
				(block7 show:)
				(block8 show:)
				(block9 show:)
				(block10 show:)
				(block11 show:)
				(block12 show:)
				(= block1posn 4)
				(= block2posn 1)
				(= block3posn 3)
				(= block4posn 5)
				(= block5posn 4)
				(= block6posn 4)
				(= block7posn 4)
				(= block8posn 4)
				(= block9posn 1)
				(= block10posn 3)
				(= block11posn 5)
				(= block12posn 4)
			)
			(5
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 601                ; 601 no good? 253
					register:
						{This trap spells your doom! The trick is to go against the grain, when you spell the words, you may pass.}
				)
				(gGame setScript: dyingScript)
			)
			(7
				(= seconds 2)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(closeControls)
			)
			(8
				(PrintOther 38 1)
; There is a loud clank that signals that the passageway is secure.
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(10
				(= cycles 5)
				(ShakeScreen 1)
			)
			(11 (PrintOther 38 18))
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

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 140)
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

(instance torch of Prop
	(properties
		y 59
		x 231
		view 412
		loop 6
	)
)

(instance block1 of Prop
	(properties
		y 102
		x 125
		view 576
	)
)

(instance block2 of Prop
	(properties
		y 99
		x 145
		view 576
	)
)

(instance block3 of Prop
	(properties
		y 96
		x 165
		view 576
	)
)

(instance block4 of Prop
	(properties
		y 93
		x 185
		view 576
	)
)


(instance block5 of Prop
	(properties
		y 108
		x 132
		view 576
	)
)

(instance block6 of Prop
	(properties
		y 105
		x 152
		view 576
	)
)

(instance block7 of Prop
	(properties
		y 102
		x 172
		view 576
	)
)

(instance block8 of Prop
	(properties
		y 99
		x 192
		view 576
	)
)


(instance block9 of Prop
	(properties
		y 114
		x 139
		view 576
	)
)

(instance block10 of Prop
	(properties
		y 111
		x 159
		view 576
	)
)

(instance block11 of Prop
	(properties
		y 108
		x 179
		view 576
	)
)

(instance block12 of Prop
	(properties
		y 105
		x 199
		view 576
	)
)

(instance lever of Prop
	(properties
		y 74
		x 74
		view 576
		loop 1
		cel 1
	)
)

(instance buttons1 of Prop
	(properties
		y 84
		x 40
		view 576
		loop 2
	)
)

(instance buttons2 of Prop
	(properties
		y 84
		x 76
		view 576
		loop 3
	)
)

(instance lever1 of Prop
	(properties
		y 52
		x 40
		view 576
		loop 4
	)
)

(instance lever2 of Prop
	(properties
		y 52
		x 76
		view 576
		loop 5
	)
)

(instance select of Prop
	(properties
		y 52
		x 76
		view 576
		loop 6
	)
)

(instance close of Prop
	(properties
		view 954
		loop 1
	)
)

(instance writing of Prop
	(properties
		view 954
		loop 3
	)
)

(instance alterEgo of Act
	(properties
		view 577
	)
)

(instance sand of Prop
	(properties
		y 116
		x 162
		view 576
		loop 7
	)
)

(instance bow of Prop
	(properties
		y 95
		x 252
		view 55
		loop 2
	)
)
