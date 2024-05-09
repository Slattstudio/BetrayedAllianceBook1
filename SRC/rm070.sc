;;; Sierra Script 1.0 - (do not remove this comment)
; + 4 SCORE // + 3 gInt //
(script# 70)
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
	rm070 0
)

(local

; Thieve's Den Safe



	index ; used in a for loop to check safeCombination array values against gSteps array
	colorMatch =  0 ; for each step that matches the combination this will tick up one in the for loop, when equals 9, safe is cracked
	myEvent
	followAlterEgo =  0
	safeOut =  0
	exit =  0
	overCircle =  0
	circleColor =  0 ; 0 = red, 1 = yellow, 2 = blue
	select =  0
	reset =  0
	[safeCombination 9] ; array that the player inputs into the dial
	[solution 9] = [1 1 2 1 1 0 0 0 0]
	comboNum =  0 ; will tick up each time a color is placed (used in the safeCombination array)
	restored =  0
	animation =  0
	overlay =  0
	picOrig = 0
)               ; when the graphic of the puzzle is on screen

(instance rm070 of Rm
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
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
			(67
				(gEgo posn: 280 130 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		
		(dust init: hide: ignoreActors:)
		(gears init: hide: cycleSpeed: 3 ignoreActors: setPri: 0)
		(table init: hide: ignoreActors: setPri: 6)
		(grave init: hide: ignoreActors: setPri: 9)
		(locker init: hide: ignoreActors:)
		(alterEgo init: hide: setScript: barScript)
		(safeFace
			init:
			hide:
			ignoreActors:
			setPri: 13
			setScript: puzzleScript
		)
		(safeTurner init: hide: ignoreActors: setPri: 15)
		(safeTurnerSelector init: hide: ignoreActors: setPri: 14)
		(safeReset init: hide: ignoreActors: setPri: 15)
		(num1
			init:
			hide:
			ignoreActors:
			setPri: 15
			setCycle: Fwd
			setScript: shovelScript
		)
		(num2 init: hide: ignoreActors: setPri: 15)
		(num3 init: hide: ignoreActors: setPri: 15)
		(num4 init: hide: ignoreActors: setPri: 15)
		(num5 init: hide: ignoreActors: setPri: 15)
		(num6 init: hide: ignoreActors: setPri: 15)
		(num7 init: hide: ignoreActors: setPri: 15)
		(num8 init: hide: ignoreActors: setPri: 15)
		(num9 init: hide: ignoreActors: setPri: 15)
		
		(if g67_70dark
			(glow init: hide: ignoreActors: setPri: 12)
			(shadowleft init: hide: ignoreActors: setPri: 12)
			(shadowright init: hide: ignoreActors: setPri: 12)
			(gEgo hide:)
			(DrawPic 900)
		else
			(gears show:)
			(table show:)
			(grave show:)
			(locker show:)
		)
	)
)

; (close:init()hide()posn(154 20)setPri(15))
; (writing:init()hide()cel(1)posn(154 30)setPri(15))

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 1))
			(1
				(if (not picOrig)
					(DrawPic 70)
					(locker show:)
					(grave show:)
					(table show:)
					(gears show:)
					(if (or (gEgo has: INV_METAL_BAR) g70Safe)
						(locker
							loop: 1
							cel: 3
							setPri: 7
							posn: (- (locker x?) 12) (+ (locker y?) 18)
						)
						(= safeOut 1)
						(gEgo observeControl: ctlRED)
					else
						(locker loop: 0 cel: 0)
					)
				)	
				
			)
			; looking at papers
			(2      ; paper on table
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo (table x?) (+ (table y?) 5) self
					ignoreControl: ctlWHITE
				)
			)
			(3 (= cycles 2) (gEgo loop: 3)) ; ego face the table
			(4
				(PrintOther 70 54)
				(Print 70 40 #title {It reads:} #font 4 #width 100 #at 120 -1)
				(= [g70Notes 0] 1)
				(PrintAll)
				(gEgo observeControl: ctlWHITE)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				
			)
			(5      ; paper in sarcophagus
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo ignoreControl: ctlWHITE)
				(if (> (gEgo y?) (- (grave y?) 3))    ; move to the bottom of the grave
					(gEgo
						setMotion: MoveTo (gEgo x?) (+ (grave y?) 3) self
					)
				else
					(gEgo
						setMotion: MoveTo (gEgo x?) (- (grave y?) 8) self
					)
				)
			)
			(6      ; move to top or bottom of sarcophagus
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo ignoreControl: ctlWHITE)
				(if (> (gEgo y?) (- (grave y?) 3))     ; move to the bottom of the grave
					(gEgo
						setMotion: MoveTo (grave x?) (+ (grave y?) 3) self
					)
				else
					(gEgo
						setMotion: MoveTo (grave x?) (- (grave y?) 8) self
					)
				)
			)
			(7
				(= cycles 2)     ; ego face the sarcophagus
				(if (> (gEgo y?) (grave y?))    ; ego on bottom
					(gEgo loop: 3)
				else
					(gEgo loop: 2)
				)
			)
			(8
				(PrintOther 70 47)
				(Print
					70
					41
					#title
					{It reads:}
					#font
					4
					#width
					100
					#at
					150
					-1
				)
				(= [g70Notes 1] 1)
				(PrintAll)
				(gEgo observeControl: ctlWHITE)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(9      ; checking locker
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo ignoreControl: ctlRED ignoreControl: ctlWHITE)
				(if (> (gEgo x?) (locker x?))       ; ego further right than the locker
					(gEgo
						setMotion: MoveTo (+ (locker x?) 16) (- (locker y?) 5) self
					)
				else
					(gEgo
						setMotion: MoveTo (- (locker x?) 25) (- (locker y?) 7) self
					)
				)
			)
			(10      ; (= cycles 2)
				(= animation 1)
				(gEgo hide:)
				(if (> (gEgo x?) (locker x?))
					(alterEgo view: 232 loop: 1)
				else
					(alterEgo view: 232 loop: 0)
				)
				(alterEgo
					show:
					posn: (gEgo x?) (gEgo y?)
					cel: 0
					setCycle: End self
					cycleSpeed: 3
				)
			)
			(11
				(if (not g70GotMap)
					(getMap)
					(Print 70 45)
				)
				(Print 70 42
					#title {It reads:}
					#font 4
					#width 100
					#at 40 -1)
				(= [g70Notes 2] 1)
				(PrintAll)
							
				(alterEgo setCycle: Beg self)
			)
			(12
				(gEgo
					show:
					loop: (alterEgo loop?)
					observeControl: ctlWHITE
					observeControl: ctlRED
				)
				(alterEgo hide:)
				(= animation 0)
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
					(==
						ctlSILVER
						(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
					)
					(if (not [gMissingBooks 4]) (PrintOther 70 1))
				)
				(if safeOut
					(if (checkEvent pEvent 201 214 81 98)
						(if (& (gEgo onControl:) ctlGREY)
							(dialScreen)
						else
							(PrintOther 70 29)
						)
					)
				)
				(cond 
					(
						(checkEvent
							pEvent
							(locker nsLeft?)
							(locker nsRight?)
							(locker nsTop?)
							(locker nsBottom?)
						)
						(if safeOut
							(if (> (pEvent y?) 107)                                                                                       ; locker
								(lookLocker)
							)
						else
							(lookLocker)	
						)
					)
					(
						(checkEvent
							pEvent
							(gears nsLeft?)
							(gears nsRight?)
							(gears nsTop?)
							(gears nsBottom?)
						)                                                                                       ; gears
						(PrintOther 67 22)
					)
					(
						(checkEvent
							pEvent
							(table nsLeft?)
							(table nsRight?)
							(table nsTop?)
							(table nsBottom?)
						)                                                                                       ; gears
						(if (<= (gEgo distanceTo: table) 40) 
							(self changeState: 2)
						else
							(PrintOther 70 48)
						)
					)
					(
						(checkEvent
							pEvent
							(grave nsLeft?)
							(grave nsRight?)
							(grave nsTop?)
							(grave nsBottom?)
						)                                                                                       ; gears
						(if (<= (gEgo distanceTo: grave) 40) 
							(self changeState: 5)
						else
							(PrintOther 70 49)
						)
					)
				)
			)
		)
		(if (== (pEvent type?) evJOYSTICK)
			(if overlay
				(if (== (pEvent message?) 1) (reverseInput)) ; If pressed the UP arrow
				(if (== (pEvent message?) 5) (forwardInput)) ; If pressed the UP arrow
				(if (== (pEvent message?) 3) (turnRight)) ; If pressed the RIGHT arrow
				(if (== (pEvent message?) 7) (turnLeft)) ; If pressed the LEFT arrow
			)
		)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if exit (resetComb) (closeControls))
			(if reset (resetComb))
			; INPUTTING OF COLORS WITH THE DIAL
			(if select
				(if (& (pEvent modifiers?) emRIGHT_BUTTON)
					(reverseInput)
					(return)
				)
				(forwardInput)
			)
			(cond 
				((& (pEvent modifiers?) emRIGHT_BUTTON) (if overCircle (turnRight)))
				(overCircle (turnLeft))
			)
		)
		(if (Said 'use/map') (Print 70 26)) ; This isn't a good place to use that.
		(if (Said 'use,push,turn/dial,puzzle,combination,button')
			(if safeOut
				(if (& (gEgo onControl:) ctlGREY)
					(dialScreen)
				else
					(PrintOther 70 29)
				)
			else
				(PrintOther 70 28)
			)
		)
		(if (Said 'use,turn/gear')
			(PrintOther 67 30)	
		)
		(if (Said 'look,search>')
			(if (Said '/locker,cabinet,hole') (lookLocker))
			(if (Said '/floor,ground')
				(if (& (gEgo onControl:) ctlSILVER)       ; diggable area
					(PrintOther 70 22)
				else
					(PrintOther 70 35)
				)
			)
			(if (Said '/wall, gear')
				(PrintOther 67 22)
				(if safeOut
					(if (& (gEgo onControl:) ctlGREY)
						(dialScreen)
					else
						(PrintOther 70 27)
					)
				else
					(PrintOther 70 33)
				)
			)
			(if (Said '/dial,puzzle,combination,button')
				(if safeOut
					(if (& (gEgo onControl:) ctlGREY)
						(dialScreen)
					else
						(PrintOther 70 29)
					)
				else
					(PrintOther 70 28)
				)
			)
			(if (Said '/handle,bar,metal')
				(if (not (gEgo has: INV_METAL_BAR))
					(if (== (IsOwnedBy INV_METAL_BAR 50) TRUE)
						(PrintATI)		
					else 
						(PrintOther 70 0)
					)
				else
					(Print 0 3 #title "metal bar" #icon 252)
				)
			)
			(if (Said '/grave,sarcophagus,box')
				(if (<= (gEgo distanceTo: grave) 44)
					(if (> (gEgo x?) 150)      ; is ego farter right than the grave?
						(self changeState: 5)
					else
						(self changeState: 6)
					)
				else
					(PrintNCE)
				)
			)
			(if (Said '[/!*]') (PrintOther 70 7))
		; this will handle just "look" by itself
		)
		(if
			(Said
				'look,search,(pick<up),take,read/paper,note,letter,page,table'
			)
			(cond 
				((<= (gEgo distanceTo: table) 40) (self changeState: 2))
				((<= (gEgo distanceTo: grave) 40) (self changeState: 5))
				((or (gEgo has: INV_METAL_BAR) g70Safe) (self changeState: 9))
				(else (PrintNCE))
			)
		)
		(if
			(Said
				'open,(pick<up),take,pull/bar,handle,locker,cabinet,door'
			)
			(if (not (gEgo has: 3))
				(if (<= (gEgo distanceTo: locker) 40)
					(barScript changeState: 1)
				else
					(PrintNCE)
				)
			else
				(PrintOther 70 9)
			)
		)
		(if (or (Said 'dig') (Said 'use/shovel'))
			(if (& (gEgo onControl:) ctlSILVER)
				(shovelScript changeState: 1)
			else
				(PrintNCE)
			)
		)
	)
	
	(method (doit &tmp dyingScript)
		(super doit:)
		(if (< gTorchOut 3000)
			(-- gTorchOut)
			(glow cel: (/ gTorchOut 500))
			(switch gTorchOut
				(1995 (PrintOther 70 43))
				(995 (PrintOther 70 44))
				(0
					(= dyingScript (ScriptID DYING_SCRIPT))
					(dyingScript
						caller: 610
						register:
							{your supply of air expired, and so have you. Thanks for playing Betrayed Alliance!}
					)
					(gGame setScript: dyingScript)
				)
			)
		)
		(gears show:)
		(if gMap  ; safe is open
			(if overlay (gEgo setMotion: NULL) (= gArcStl 1))
		)
		(if (& (gEgo onControl:) ctlNAVY)
			(if (not restored)
				(= restored 1)
				(RoomScript changeState: 1)
			)
		)
		(= myEvent (Event new: evNULL))
		(if gMap
			(if overlay
				(if
					(checkEvent
						myEvent
						(+ (safeTurner nsLeft?) 10)
						(- (safeTurner nsRight?) 10)
						(+ (safeTurner nsTop?) 11)
						(+ (safeTurner nsBottom?) 1)
					)
					(SetCursor 990 (HaveMouse))
					(= gCurrentCursor 990)                        ; turn
					(= overCircle 1)
					(= select 0)
					(= exit 0)
					(= reset 0)
					(safeFace cel: 0)
				else
					(= overCircle 0)
					(if
						(checkEvent
							myEvent
							(safeReset nsLeft?)
							(safeReset nsRight?)
							(+ (safeReset nsTop?) 8)
							(+ (safeReset nsBottom?) 12)
						)
						(safeReset cel: 1)
						(= overCircle 0)
						(= select 0)
						(= exit 0)
						(= reset 1)
						(SetCursor 999 (HaveMouse))
						(= gCurrentCursor 999)
					else                                              ; pointer
						(safeReset cel: 0)
						(= reset 0)
						(if
							(checkEvent
								myEvent
								(safeFace nsLeft?)
								(safeFace nsRight?)
								(+ (safeFace nsTop?) 6)
								(+ (safeFace nsBottom?) 50)
							)
							(= overCircle 0)
							(= select 1)
							(= exit 0)
							(= reset 0)
							(SetCursor 989 (HaveMouse))
							(= gCurrentCursor 989)                        ; submit
							(safeFace cel: 1)
						else
							(= select 0)
							(SetCursor 999 (HaveMouse))
							(= gCurrentCursor 999)                        ; pointer
							(safeFace cel: 0)
						)
					)
				)
				(if (> (myEvent x?) 125)
					(SetCursor 992 (HaveMouse))
					(= gCurrentCursor 992)
					(= overCircle 0)
					(= select 0)
					(= exit 1)
					(= reset 0)
				else
					(= exit 0)
				)
			)
		)
		(myEvent dispose:)
		(if g67_70dark
			(glow show: posn: (gEgo x?) (+ (gEgo y?) 55))
			(shadowleft
				show:
				posn: (- (gEgo x?) 145) (+ (gEgo y?) 55)
			)
			(shadowright
				show:
				posn: (+ (gEgo x?) 145) (+ (gEgo y?) 55)
			)
			(if (not animation) (gEgo show:))
		else
			(glow hide:)
			(shadowleft hide:)
			(shadowright hide:)
			(if (not animation) (gEgo show:))
		)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 67)
		)
	)
)


(instance barScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo (- (locker x?) 10) (+ (locker y?) 3) barScript
				)
			)
			(2
				(= cycles 20)     ; pulling on the bar
				(= animation 1)
				(gEgo hide:)
				(alterEgo
					show:
					posn: (gEgo x?) (gEgo y?)
					view: 410
					loop: 4
					cel: 0
					setCycle: End
					cycleSpeed: 5
				)
			)
			(3          ; breaking the bar off
				(alterEgo
					loop: 3
					posn: (- (alterEgo x?) 20) (+ (alterEgo y?) 5)
					cel: 0
					setCycle: End self
					cycleSpeed: 3
				)
				(locker cel: 1)
				(gTheSoundFX number: 204 play:)
			)
			(4      ; locker falling over
				(gGame changeScore: 1)
				(locker
					loop: 1
					posn: (- (locker x?) 12) (+ (locker y?) 18)
					cel: 0
					setCycle: End self
					cycleSpeed: 2
					setPri: 7
				)                                                                                                      ; locker falling over
				(gEgo
					get: 3
					posn: (- (alterEgo x?) 10) (+ (alterEgo y?) 3)
				)
			)
			(5
				(= cycles 20)    ; dust plummes          	
				(ShakeScreen 1)
				(gTheSoundFX number: 200 play:)
				(dust
					show:
					posn: (locker x?) (locker y?)
					cel: 0
					setCycle: End
					cycleSpeed: 3
				)
			)
			(6          ; getting up								
				(PrintOther 70 15)
				(alterEgo
					view: 409
					posn: (- (alterEgo x?) 15) (alterEgo y?)
					loop: 1
					cel: 0
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(7
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(alterEgo hide:)
				(gEgo show: loop: 2 posn: (alterEgo x?) (alterEgo y?))
				(= safeOut 1)
				(gEgo observeControl: ctlRED)
				(= animation 0)
				(Print 70 16 #icon 252 #title {Metal Bar} #at -1 20)
				(PrintOther 70 30)
			)
		)
	)
)

(instance shovelScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 244 133 shovelScript)
			)
			(2
				(= cycles 25)       ; digging animation
				(= animation 1)
				(gEgo hide:)
				(alterEgo
					show:
					view: 419
					loop: 1
					posn: 244 133
					setCycle: Fwd
					cycleSpeed: 2
				)
			)
			(3       ; bend over to pick up something
				(alterEgo view: 232 loop: 1 cel: 0 setCycle: End self)
			)
			(4
				(alterEgo setCycle: Beg self)
				(if (not [gMissingBooks 4])
					(Print 70 23 #icon 984 1 1) ; You've unearthed a book	
					(= [gMissingBooks 4] 1)
					(gGame changeScore: 1)
				else
					(PrintOther 70 37)
				)
			)                           ; no further reward
			(5
				(gEgo show: loop: 1)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= animation 0)
			)
		)
	)
)

(instance puzzleScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(= cycles 10)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(= gTorchOut 3001)
			)                      ; stops it from going down		
			(2
				(= cycles 1)
				(PrintOther 70 2)
			)
			(3
				(= seconds 3)
				(closeControls)
				(ShakeScreen 1)
				(gEgo setMotion: NULL)
				(gears setCycle: Fwd)
				(gTheSoundFX number: 203 play:)
			)
			(4
				(= seconds 1)
				(= g67_70dark 0)
				(gears setCycle: CT)
			)
			(5
				(PrintOther 70 17) ; opening complete!
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
)

(procedure (lookLocker)
	(if safeOut
		(if (<= (gEgo distanceTo: locker) 50)
			(RoomScript changeState: 9)
		else
			(PrintOther 70 32)
		)
	else                        ; print you see a hole	
		(PrintOther 70 33)
	)
)
                          ; locker standing up right	
(procedure (closeControls)
	(= overlay 0)
	(= gMap 0)
	(= gArcStl 0)
	(safeFace hide:)
	(safeTurner hide:)
	(safeTurnerSelector hide:)
	(safeReset hide:)
	(num1 hide:)
	(num2 hide:)
	(num3 hide:)
	(num4 hide:)
	(num5 hide:)
	(num6 hide:)
	(num7 hide:)
	(num8 hide:)
	(num9 hide:)
	(SetCursor 999 (HaveMouse))
	(= gCurrentCursor 999)
)

(procedure (resetComb)
	(= comboNum 0)
	(num1 loop: 4 cel: 0 setCycle: Fwd)
	(num2 loop: 4 cel: 0 setCycle: CT)
	(num3 loop: 4 cel: 0 setCycle: CT)
	(num4 loop: 4 cel: 0 setCycle: CT)
	(num5 loop: 4 cel: 0 setCycle: CT)
	(num6 loop: 4 cel: 0 setCycle: CT)
	(num7 loop: 4 cel: 0 setCycle: CT)
	(num8 loop: 4 cel: 0 setCycle: CT)
	(num9 loop: 4 cel: 0 setCycle: CT)
)

(procedure (dialScreen)
	(if g67_70dark
		(= overlay 1)
		(= gMap 1)
		(safeFace show:)
		(safeTurner show:)
		(safeTurnerSelector show:)
		(safeReset show:)
		(num1 show:)
		(num2 show:)
		(num3 show:)
		(num4 show:)
		(num5 show:)
		(num6 show:)
		(num7 show:)
		(num8 show:)
		(num9 show:)
	else
		(PrintOther 70 46)
	)
)

(procedure (forwardInput)
	(if (< comboNum 9)
		(= [safeCombination comboNum] circleColor)
		(switch comboNum
			(0
				(num1 loop: 3 setCycle: Cycle cel: circleColor)
				(num2 setCycle: Fwd)
			)
			(1
				(num2 loop: 3 setCycle: Cycle cel: circleColor)
				(num3 setCycle: Fwd)
			)
			(2
				(num3 loop: 3 setCycle: Cycle cel: circleColor)
				(num4 setCycle: Fwd)
			)
			(3
				(num4 loop: 3 setCycle: Cycle cel: circleColor)
				(num5 setCycle: Fwd)
			)
			(4
				(num5 loop: 3 setCycle: Cycle cel: circleColor)
				(num6 setCycle: Fwd)
			)
			(5
				(num6 loop: 3 setCycle: Cycle cel: circleColor)
				(num7 setCycle: Fwd)
			)
			(6
				(num7 loop: 3 setCycle: Cycle cel: circleColor)
				(num8 setCycle: Fwd)
			)
			(7
				(num8 loop: 3 setCycle: Cycle cel: circleColor)
				(num9 setCycle: Fwd)
			)
			(8
				(num9 loop: 3 setCycle: Cycle cel: circleColor)
				(for ( (= index 0)) (< index 9)  ( (++ index)) (if (== [solution index] [safeCombination index])
					(++ colorMatch)
				))
				(if (== colorMatch 9)
					(puzzleScript changeState: 1)
				else
					(= colorMatch 0)
				)
			)
		)
		(++ comboNum)
	)
)

(procedure (reverseInput)
	(if (> comboNum 0)
		(switch comboNum
			(0)
			(1
				(num2 setCycle: CT)
				(num1 loop: 4 setCycle: Fwd)
			)
			(2
				(num3 loop: 4 setCycle: CT)
				(num2 loop: 4 setCycle: Fwd)
			)
			(3
				(num4 loop: 4 setCycle: CT)
				(num3 loop: 4 setCycle: Fwd)
			)
			(4
				(num5 loop: 4 setCycle: CT)
				(num4 loop: 4 setCycle: Fwd)
			)
			(5
				(num6 loop: 4 setCycle: CT)
				(num5 loop: 4 setCycle: Fwd)
			)
			(6
				(num7 loop: 4 setCycle: CT)
				(num6 loop: 4 setCycle: Fwd)
			)
			(7
				(num8 loop: 4 setCycle: CT)
				(num7 loop: 4 setCycle: Fwd)
			)
			(8
				(num9 loop: 4 setCycle: CT)
				(num8 loop: 4 setCycle: Fwd)
			)
			(9 (num9 loop: 4 setCycle: Fwd))
		)
		(-- comboNum)
	)
	(return)
)
(procedure (PrintAll)
	(if (and [g70Notes 0][g70Notes 1][g70Notes 2])
		(PrintOther 70 38)
		(Print 70 50 #font 4)		
	else
		(PrintOther 70 39)
	)
)
(procedure (turnRight)
	(if (> circleColor 0)
		(-- circleColor)
	else
		(= circleColor 2)
	)
	(safeTurner cel: circleColor)
)

(procedure (turnLeft)
	(if (< circleColor 2)
		(++ circleColor)
	else
		(= circleColor 0)
	)
	(safeTurner cel: circleColor)
)

(procedure (getMap)
	(= g70GotMap 1)
	(gGame changeScore: 2)
	(= gInt (+ gInt 3))
	(PrintOther 70 18)
	(Print {} #icon 82 1 0 #title {The Image:})
	(Print 70 19 #font 4 #title {The note reads:})
	(= [gArtwork 1] 1)
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

(instance table of Prop
	(properties
		y 125
		x 280
		view 68
	)
)

(instance dust of Prop
	(properties
		y 110
		x 208
		view 133
		loop 2
	)
)

(instance grave of Prop
	(properties
		y 145
		x 130
		view 133
		loop 3
		cel 1
	)
)

(instance locker of Prop
	(properties
		y 110
		x 208
		view 133
	)
)

(instance alterEgo of Prop
	(properties
		y 143
		x 188
		view 232
	)
)

(instance gears of Prop
	(properties
		y 125
		x 180
		view 67
		loop 2
	)
)

(instance glow of Prop
	(properties
		y 35
		x 195
		view 62
	)
)

(instance shadowleft of Prop
	(properties
		y 35
		x 195
		view 62
		loop 1
	)
)

(instance shadowright of Prop
	(properties
		y 35
		x 195
		view 62
		loop 1
	)
)

(instance safeFace of Prop
	(properties
		y 85
		x 80
		view 582
		loop 1
	)
)

(instance safeTurner of Prop
	(properties
		y 77
		x 80
		view 582
		loop 2
	)
)

(instance safeTurnerSelector of Prop
	(properties
		y 160
		x 80
		view 582
		loop 0
		cel 2
	)
)

(instance safeReset of Prop
	(properties
		y 133
		x 44
		view 582
		loop 5
	)
)

(instance num1 of Prop
	(properties
		y 117
		x 56
		view 582
		loop 4
	)
)

(instance num2 of Prop
	(properties
		y 117
		x 67
		view 582
		loop 4
	)
)

(instance num3 of Prop
	(properties
		y 117
		x 78
		view 582
		loop 4
	)
)

(instance num4 of Prop
	(properties
		y 117
		x 89
		view 582
		loop 4
	)
)

(instance num5 of Prop
	(properties
		y 117
		x 100
		view 582
		loop 4
	)
)

(instance num6 of Prop
	(properties
		y 152
		x 65
		view 582
		loop 4
	)
)

(instance num7 of Prop
	(properties
		y 152
		x 76
		view 582
		loop 4
	)
)

(instance num8 of Prop
	(properties
		y 152
		x 87
		view 582
		loop 4
	)
)

(instance num9 of Prop
	(properties
		y 152
		x 98
		view 582
		loop 4
	)
)
