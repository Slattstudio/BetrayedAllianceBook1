;;; Sierra Script 1.0 - (do not remove this comment)
; +4 SCORE // gInt +3 //
(script# 63)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use follow)

(public
	rm063 0
)

(local

; Squirrel Music Puzzle



	; (use "sciaudio")
	mimic =  0 ; When TRUE, the squirrel will mirror your movements
; Determines if a floorsquare has been stepped on
	OneD =  0
	TwoD =  0
	ThreeD =  0
	FourD =  0
	FiveD =  0
	SixD =  0
	SevenD =  0
	EightD =  0
	NineD =  0
; Used to make the squirrel stop if you hit up, down, left, or right twice
	movingUP =  0
	movingDOWN =  0
	movingLEFT =  0
	movingRIGHT =  0
	movingUPRIGHT =  0
	movingDOWNLEFT =  0
	movingUPLEFT =  0
	movingDOWNRIGHT =  0
	; inPos = 0
	egoInPosn =  0
	squirrelInPosn =  0
	; snd
	[noteArray 8] ; from 0 to 7
	blocksDown =  0
	doorOpenning =  0
	activated =  0 ; Makes it so that the blocks play music
	goingToActivate =  0
	movingTwo =  0
	
	cutScene = 0 ; = 1 when giving acorn, = 2 when petting squirrel
)            

(instance rm063 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 21
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 204)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
			(21 
				(gEgo posn: 20 128 loop: 0)
				(gTheMusic number: 65 loop: -1 play:)	
			)
			(72
				(gEgo posn: 254 128 loop: 1)
				(squirrel loop: 1 posn: 255 100)
				;(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
			)
		)

		(SetUpEgo)
		(gEgo init: setScript: mimicScript2)
		(= gEgoRunning 0)
		(RunningCheck)
		(alterEgo init: hide: ignoreActors:)
		
		(squirrel
			init:
			setCycle: Walk
			ignoreActors:
			ignoreControl: ctlRED
			setScript: mimicScript
		)
		(tileA
			init:
			ignoreActors:
			setPri: 1
			setScript: tileScriptA
		)
		(tileB
			init:
			ignoreActors:
			setPri: 1
			setScript: tileScriptB
		)
		(tileC
			init:
			ignoreActors:
			setPri: 1
			setScript: tileScriptC
		)
		(tileD
			init:
			ignoreActors:
			setPri: 1
			setScript: tileScriptD
		)
		(tileE
			init:
			ignoreActors:
			setPri: 1
			setScript: tileScriptE
		)
		(tileF
			init:
			ignoreActors:
			setPri: 1
			setScript: tileScriptF
		)
		(tileG
			init:
			ignoreActors:
			setPri: 1
			setScript: tileScriptG
		)
		(tileH
			init:
			ignoreActors:
			setPri: 1
			setScript: tileScriptH
		)
		(tileI
			init:
			ignoreActors:
			setPri: 1
			setScript: tileScriptI
		)
		(if (not g63PuzSol)
			(wall
				init:
				setScript: wallScript
				ignoreActors:
				ignoreControl: ctlWHITE
				setPri: 1
			)
			(gEgo observeControl: ctlRED)
		)
		(if (== (IsOwnedBy INV_ACORN 63) FALSE)
			(= activated 0)
		else
			(= activated 1)
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 1))
			(1
				(if (not [g65Grove 0] )
					(ProgramControl)
					(SetCursor 997 (HaveMouse))
					(= gCurrentCursor 997)
					(gEgo setMotion: MoveTo 30 128 self)
				)
			)
			(2 (= cycles 2))
			(3
				(PrintOther 63 2) ; #width 280 #at -1 20)
				(= [g65Grove 0] 1)
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
					(checkEvent
						pEvent
						(wall nsLeft?)
						(wall nsRight?)
						(wall nsTop?)
						(wall nsBottom?)
					)
					(if g63PuzSol else (PrintOther 63 16))
				)
				(cond 
					(
						(checkEvent
							pEvent
							(squirrel nsLeft?)
							(squirrel nsRight?)
							(squirrel nsTop?)
							(squirrel nsBottom?)
						)
						(PrintOther 63 0) ; #width 280 #at -1 20)
						(if (== (IsOwnedBy INV_ACORN 63) FALSE)
							(PrintOther 63 12)
						)
					)
					(                     ; #width 280 #at -1 20)
						(==
							ctlNAVY
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)
						(PrintOther 63 1)
					)
					(                    ; #width 280 #at -1 20)
						(==
							ctlFUCHSIA
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                                                        ; sign
						(= gWndColor 0)
						(= gWndBack 7)
						(Print 63 15 #at -1 10 #font 4)
						(= gWndColor 0)
						(= gWndBack 15)
					)
				)
			)
		)
		
		(if (Said 'run')
			(PrintOther 18 66)	
		)
		(if (Said 'look,read>')
			(if (Said '/squirrel,animal,creature')
				(Print 63 0 #width 280 #at -1 20)
				(if (== (IsOwnedBy INV_ACORN 63) FALSE)
					(PrintOther 63 12)
				)
			)              
			(if (Said '/ground,panel') (PrintOther 63 1))
			(if (Said '/arrow')
				(PrintOther 63 28)
				(if (not g63PuzSol)
					(PrintOther 63 29)	
				)	
			)  
			(if (Said '/mountain')
				(PrintOther 63 2)
			)
			(if (Said '/stone,message,sign')
				(= gWndColor 0)
				(= gWndBack 7)
				(Print 63 15 #at -1 10 #font 4)
				(= gWndColor 0)
				(= gWndBack 15)
			)
			(if (Said '/tile') (PrintOther 63 13))
			(if (Said '/pillar') (PrintOther 63 26))
			(if (Said '/door,wall')
				(if g63PuzSol 
					(PrintOther 63 27)
				else
					(PrintOther 63 16)
				)
			)
			(if (Said '[/!*]') (PrintOther 63 3))
		; this will handle just "look" by itself
		)                        ; #width 280 #at -1 8)
		(if (Said 'talk,ask/squirrel,animal,creature')
			(if (== (IsOwnedBy INV_ACORN 63) TRUE)
				(PrintOther 63 22)
			else
				(PrintOther 63 4)
			)
		)
		(if (Said 'touch/squirrel,animal,creature')
			(if (< (gEgo x?) 160)
				(if (<= (gEgo distanceTo: squirrel) 35)
					(= cutScene 2)	; petting
					(wallScript changeState: 3)
				else
					(PrintNCE)
				)	
			else
				(Print 63 21)
			)
		)
		(if (Said 'run')
			(if (not mimic)
				(if (not gEgoRunning)
					(gEgo view: 230 xStep: 5 yStep: 4)
					(= gEgoRunning 1)
					(= [gArray 0] (pEvent x?))
					(= [gArray 1] (pEvent y?))
					(runProc)
				else
					(PrintOther 0 39)
				)
			else
				(PrintOther 63 5)
			)
		)
		(if (Said 'copy,copycat/cat')
			(if (not g63PuzSol)
				(if (== (IsOwnedBy INV_ACORN 63) TRUE)
					(if (< (gEgo x?) 160)
						(if (not mimic)
							(PrintOther 63 6)
							(mimicScript changeState: 1)
							(= gNoClick 1)
						else
							; Print(63 10)
							(PrintOther 63 6)
							(mimicScript changeState: 1)
							(= gNoClick 1)
						)
					else
						(PrintOther 63 18)
					)
				else
					(PrintOther 63 7)
				)
			else
				(PrintOther 63 8)
			)
		)
		(if (or (Said 'give,use/acorn')
				(Said 'feed/squirrel,animal'))
			(if (< (gEgo x?) 160)
				(if (<= (gEgo distanceTo: squirrel) 35)
					(if (gEgo has: INV_ACORN)
						(= cutScene 1)
						(wallScript changeState: 3)
						
					else
						(PrintOther 63 23)
					)
				else
					(PrintNCE)	
				)
			else
				(Print 63 21)
			)
		)
; (if(Said('open/door'))
;            (wallScript:changeState(1))
;        )
		(if (Said 'open,push,pull,hit/door')
			(if g63PuzSol (PrintItIs) else (PrintOther 63 17))
		)
		(if (Said 'use/map')
			(Print {This isn't a good place to use that.})
		)
		(if (== (pEvent type?) evJOYSTICK)
			(if mimic
				(if (not gProgramControl)
					(squirrelMotion pEvent 1 DOWN movingUP)
					(squirrelMotion pEvent 5 UP movingDOWN)
					(squirrelMotion pEvent 3 LEFT movingRIGHT)
					(squirrelMotion pEvent 7 RIGHT movingLEFT)
					
					; need to add diagonals
					
					(squirrelMotion pEvent 2 DOWNLEFT movingUPRIGHT) 
					(squirrelMotion pEvent 4 UPLEFT  movingDOWNRIGHT)
					(squirrelMotion pEvent 6 UPRIGHT movingDOWNLEFT)
					(squirrelMotion pEvent 8 DOWNRIGHT movingUPLEFT)
				)
			)
		)		
		(if (or (Said 'listen[/!*]')
				(Said 'listen/voice,song'))
			(if [g65Grove 1]
				(PrintOther 63 24)	
			else
				(PrintOther 63 25)
				(Print 65 45  #at -1 20 #title {A distant voice:})
			)
		)
	)
	
	(method (doit)
		(super doit:)

		(if (== [noteArray 0] 1)
			(if (== [noteArray 1] 1)
				(if (== [noteArray 2] 2)
					(if (== [noteArray 3] 2)
						(if (== [noteArray 4] 3)
							(if (== [noteArray 5] 3)
								(if (== [noteArray 6] 4)
									(if (== [noteArray 7] 4)
										(if (not doorOpenning)
											(if activated
												(if (not g63PuzSol)
													(wallScript changeState: 1)
													(squirrel setMotion: NULL)
													(= mimic 0)
													(= gNoClick 0)
													(= g63PuzSol 1)
													(= gInt (+ gInt 3))
													(= movingTwo 0)
													(gGame changeScore: 3)
													
												)
											)
										)
									)
								)
							)
						)
					)
				)
			)
		)
		(if movingTwo
			(if (== (gEgo isStopped:) TRUE)
				(squirrel
					setMotion: MoveTo (squirrel x?) (squirrel y?)
					cel: 0
				)
			)
		)
		(if (not cutScene)
			(cond 
				(mimic)
				; stuff??
				((not goingToActivate)
					(if (>= (gEgo distanceTo: squirrel) 30)
						(squirrel setMotion: Follow gEgo)
					else
						(squirrel setMotion: NULL)
						(squirrel cel: 0)
					)
				)
			)
			(gEgo observeControl: ctlWHITE)
		else
			(squirrel setMotion: NULL)
			(squirrel cel: 0)
			(gEgo ignoreControl: ctlWHITE)
		)

		(if (& (gEgo onControl:) ctlCYAN) (gRoom newRoom: 72))
		(if activated
			; Walked on Block 1
			(if (EgoOrSquirrelOnColor ctlNAVY)
				(if (not OneD)
					(tileScriptA changeState: 1)
					(= [noteArray blocksDown] 2)
					(++ blocksDown)
					;(playSciAudio {music\\note2.mp3})
				)
			)
			; (tileA:cel(1))
			; Walked on Block 2
			(if (EgoOrSquirrelOnColor ctlGREEN)
				(if (not TwoD)
					(tileScriptB changeState: 1)
					(= [noteArray blocksDown] 3)
					(++ blocksDown)
					;(playSciAudio {music\\note3.mp3})
				)
			)
			; (tileA:cel(1))
			; Walked on Block 3
			(if (EgoOrSquirrelOnColor ctlTEAL)
				(if (not ThreeD)
					(tileScriptC changeState: 1)
					(= [noteArray blocksDown] 4)
					(++ blocksDown)
					;(playSciAudio {music\\note4.mp3})
				)
			)
			; (tileA:cel(1))
			; Walked on Block 4
			(if (EgoOrSquirrelOnColor ctlMAROON)
				(if (not FourD)
					(tileScriptD changeState: 1)
					(= [noteArray blocksDown] 1)
					(++ blocksDown)
				)
			)
; = snd aud
;                playSciAudio("music\\note1.mp3")
			; (tileA:cel(1))
			; Walked on Block 5
			(if (EgoOrSquirrelOnColor ctlPURPLE) (if (not FiveD)))
			; (tileScriptE:changeState(1))
			; (tileA:cel(1))
			; Walked on Block 6
			(if (EgoOrSquirrelOnColor ctlBROWN)
				(if (not SixD)
					(tileScriptF changeState: 1)
					(= [noteArray blocksDown] 1)
					(++ blocksDown)
					;(playSciAudio {music\\note5.mp3})
				)
			)
			; (tileA:cel(1))
			; Walked on Block 7
			(if (EgoOrSquirrelOnColor ctlSILVER)
				(if (not SevenD)
					(tileScriptG changeState: 1)
					(= [noteArray blocksDown] 4)
					(++ blocksDown)
					;(playSciAudio {music\\note8.mp3})
				)
			)
			; (tileA:cel(1))
			; Walked on Block 8
			(if (EgoOrSquirrelOnColor ctlGREY)
				(if (not EightD)
					(tileScriptH changeState: 1)
					(= [noteArray blocksDown] 3)
					(++ blocksDown)
					;(playSciAudio {music\\note7.mp3})
				)
			)
			; (tileA:cel(1))
			; Walked on Block 9
			(if (EgoOrSquirrelOnColor ctlBLUE)
				(if (not NineD)
					(tileScriptI changeState: 1)
					(= [noteArray blocksDown] 2)
					(++ blocksDown)
					;(playSciAudio {music\\note6.mp3})
				)
			)
		)
	)
)

; (tileA:cel(1)) ; End (if(activated)
(procedure (squirrelMotion pEvent dir1 dir2 movingVar)
	(if (== (pEvent message?) dir1)
		(if movingVar
			(squirrel setMotion: MoveTo (squirrel x?) (squirrel y?))
			(= movingVar 0)
		else
			(squirrel setDirection: dir2)
			(= movingVar 1)
		)
	)
)

(procedure (playSciAudio)
)

; = snd aud (send snd:
;        command("play")
;        fileName(file)
;        volume("100")
;        loopCount("0")
;        init()
;    )
(procedure (playxSciAudioGroveEntrance)
)

; = snd aud (send snd:
;        command("playx")
;        fileName("music\\groveentrance.mp3")
;        volume("70")
;        loopCount("0")
;        init()
;    )
(procedure (EgoOrSquirrelOnColor color)
	(if
		(or
			(& (gEgo onControl:) color)
			(& (squirrel onControl:) color)
		)
		(return TRUE)
	else
		(return FALSE)
	)
)

(instance tileScriptA of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= seconds 6)
				(tileA cel: 1)
				(= OneD 1)
			)
			(2
				(tileA cel: 0)
				(= OneD 0)
				(-- blocksDown)
			)
		)
	)
)

(instance tileScriptB of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= seconds 6)
				(tileB cel: 1)
				(= TwoD 1)
			)
			(2
				(tileB cel: 0)
				(= TwoD 0)
				(-- blocksDown)
			)
		)
	)
)

(instance tileScriptC of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= seconds 6)
				(tileC cel: 1)
				(= ThreeD 1)
			)
			(2
				(tileC cel: 0)
				(= ThreeD 0)
				(-- blocksDown)
			)
		)
	)
)

(instance tileScriptD of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= seconds 6)
				(tileD cel: 1)
				(= FourD 1)
			)
			(2
				(tileD cel: 0)
				(= FourD 0)
				(-- blocksDown)
			)
		)
	)
)

(instance tileScriptE of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= seconds 6)
				(tileE cel: 1)
				(= FiveD 1)
			)
			(2 (tileE cel: 0) (= FiveD 0))
		)
	)
)

(instance tileScriptF of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= seconds 6)
				(tileF cel: 1)
				(= SixD 1)
			)
			(2
				(tileF cel: 0)
				(= SixD 0)
				(-- blocksDown)
			)
		)
	)
)

(instance tileScriptG of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= seconds 6)
				(tileG cel: 1)
				(= SevenD 1)
			)
			(2
				(tileG cel: 0)
				(= SevenD 0)
				(-- blocksDown)
			)
		)
	)
)

(instance tileScriptH of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= seconds 6)
				(tileH cel: 1)
				(= EightD 1)
			)
			(2
				(tileH cel: 0)
				(= EightD 0)
				(-- blocksDown)
			)
		)
	)
)

(instance tileScriptI of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= seconds 6)
				(tileI cel: 1)
				(= NineD 1)
			)
			(2
				(tileI cel: 0)
				(= NineD 0)
				(-- blocksDown)
			)
		)
	)
)

(instance mimicScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= mimic 1)
				(= activated 0)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 34 132 self)
				(mimicScript2 changeState: 1)
			)
			(2
				(= cycles 3)
				(= egoInPosn 1)
				(gEgo loop: 0)
				(if squirrelInPosn
					(PlayerControl)
					(SetCursor 999 (HaveMouse))
					(= gCurrentCursor 999)
					(= activated 1)
					(= egoInPosn 0)
					(= squirrelInPosn 0)
					(= movingTwo 1)
				else
					(squirrel
						setMotion: MoveTo 184 132 mimicScript2
						ignoreControl: ctlWHITE
					)
				)
			)
			(3          ; failsafe as sometimes they were triggering together and not giving play control
				(if gProgramControl (self changeState: 2))
			)
			
			(4
				(= goingToActivate 1)
				(squirrel setMotion: MoveTo 106 129 self)
			)
			(5
				(= cycles 5)
				(ShakeScreen 2)
				(squirrel cel: 0)
			)
			(6
				(PrintOther 63 14)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= activated 1)
				(gGame changeScore: 1)
				(= goingToActivate 0)
			)
		)
	)
)

(instance mimicScript2 of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(squirrel
					setMotion: MoveTo 184 132 self
					ignoreControl: ctlWHITE
				)
			)
			(2
				(++ squirrelInPosn)
				(= gEgoRunning 0)
				(RunningCheck)
				(squirrel loop: 1 cel: 0 observeControl: ctlWHITE)
				(if egoInPosn
					(PlayerControl)
					(SetCursor 999 (HaveMouse))
					(= gCurrentCursor 999)
					(= activated 1)
					(= egoInPosn 0)
					(= squirrelInPosn 0)
					(= movingTwo 1)
				)
			)
		)
	)
)

(instance wallScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= activated 0)
				(= doorOpenning 1)
				(ShakeScreen 2)
				(wall setCycle: End self cycleSpeed: 2)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gTheSoundFX number: 203 play:)
			)
			(2
				(PrintOther 63 11) ; #at -1 10) // A pathway opens up in the rocks. You think you can discern a faint singing passing through the cavern.
				(gEgo ignoreControl: ctlRED)
				(squirrel ignoreControl: ctlRED)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				;(playxSciAudioGroveEntrance)
			)
			; moving to give squirrel acorn
			(3
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				
				(gEgo setMotion: MoveTo (- (squirrel x?) 15) (squirrel y?) self)	
			)
			; bend down to squirrel
			(4
				(gEgo hide:)
				(alterEgo show: view: 232 posn: (gEgo x?)(gEgo y?) setCycle: End self cycleSpeed: 2)
				(squirrel loop: 1)	
			)
			(5	(= cycles 6)
				(if (== cutScene 1)
					(PrintOther 63 9)
					(gEgo put: INV_ACORN 63)
					(mimicScript changeState: 4)
				else
					(if (== cutScene 2)
						(if (== (IsOwnedBy INV_ACORN 63) FALSE)
							(PrintOther 63 19)	
						else
							(PrintOther 63 20)
						)	
					)
				)	
			)
			(6
				(alterEgo setCycle: Beg self)	
			)
			(7
				(alterEgo hide:)
				(gEgo show: loop: 0 observeControl: ctlWHITE)

				(if (not (== cutScene 2))
					(mimicScript changeState: 4)	; squirrel to activate panels
				else
					(PlayerControl)
					(SetCursor 999 (HaveMouse))
					(= gCurrentCursor 999)	
				)
				(= cutScene 0)
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

(instance alterEgo of Prop
	(properties
		y 150
		x 150
		view 0
	)
)
(instance squirrel of Act
	(properties
		y 150
		x 150
		view 61
	)
)

(instance wall of Act
	(properties
		y 135
		x 251
		view 64
	)
)

(instance tileA of Prop
	(properties
		y 122
		x 64
		view 63
	)
)

(instance tileB of Prop
	(properties
		y 122
		x 92
		view 63
	)
)

(instance tileC of Prop
	(properties
		y 122
		x 120
		view 63
	)
)

(instance tileD of Prop
	(properties
		y 137
		x 79
		view 63
	)
)

(instance tileE of Prop
	(properties
		y 137
		x 107
		view 63
		loop 1
	)
)

(instance tileF of Prop
	(properties
		y 137
		x 135
		view 63
	)
)

(instance tileG of Prop
	(properties
		y 152
		x 94
		view 63
	)
)

(instance tileH of Prop
	(properties
		y 152
		x 122
		view 63
	)
)

(instance tileI of Prop
	(properties
		y 152
		x 150
		view 63
	)
)
