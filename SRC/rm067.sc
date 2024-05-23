;;; Sierra Script 1.0 - (do not remove this comment)
; + 1 SCORE //
(script# 67)
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
	rm067 0
)

(local

; Thieve's Den
	picOrig =  1
	falling =  0
	climbing =  0
	candle =  0
	tableMoved =  0
	plateTripped =  0
	step1 =  3
	step2 =  3
	step3 =  3
	step4 =  3
	step5 =  3
	step6 =  3
	step7 =  3
	step8 =  3
	step9 =  3
	
	canLook = 0
)
; 0 = red, 1 = yellow, 2 = blue

(instance rm067 of Rm
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
		(SetUpEgo)
		(gEgo init:)
		
		(alterEgo init: hide: ignoreActors: ignoreControl: ctlWHITE)
		(gears init: cycleSpeed: 3 ignoreActors: setPri: 0)
		
		
		
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 260 118 loop: 2)
			)
			(41
				(gTheMusic number: 67 loop: -1 play:)
				(= climbing 1)
				(gEgo init: posn: 280 123 loop: 1 hide:)
				(alterEgo show: view: 245 posn: 284 80 ignoreControl: ctlWHITE ignoreActors:)
				(RoomScript changeState: 1)
			
			)
			(70
				(= canLook 1)
				; = dark 1
				(= step1 [gStepArray 0])
				(= step2 [gStepArray 1])
				(= step3 [gStepArray 2])
				(= step4 [gStepArray 3])
				(= step5 [gStepArray 4])
				(= step6 [gStepArray 5])
				(= step7 [gStepArray 6])
				(= step8 [gStepArray 7])
				(= step9 [gStepArray 8])
				(if g67_70dark
					(= candle 1)
					(DrawPic 900)
					(= picOrig 0)
					(table init: hide: cel: 0)
					(gears hide:)
					(gEgo init: posn: 59 146 loop: 0 hide:)
					(block1
						init:
						ignoreActors:
						setPri: 1
						loop: 1
						cel: step1
						hide:
					)
					(block2
						init:
						ignoreActors:
						setPri: 1
						loop: 1
						cel: step2
						hide:
					)
					(block3
						init:
						ignoreActors:
						setPri: 1
						loop: 1
						cel: step3
						hide:
					)
					(block4
						init:
						ignoreActors:
						setPri: 1
						loop: 1
						cel: step4
						hide:
					)
					(block5
						init:
						ignoreActors:
						setPri: 1
						loop: 1
						cel: step5
						hide:
					)
					(block6
						init:
						ignoreActors:
						setPri: 1
						loop: 1
						cel: step6
						hide:
					)
					(block7
						init:
						ignoreActors:
						setPri: 1
						loop: 1
						cel: step7
						hide:
					)
					(block8
						init:
						ignoreActors:
						setPri: 1
						loop: 1
						cel: step8
						hide:
					)
					(block9
						init:
						ignoreActors:
						setPri: 1
						loop: 1
						cel: step9
						hide:
					)
				else
					(= candle 1)
					(= picOrig 1)
					(table init: cel: 0)
					(gEgo init: posn: 59 146 loop: 0)
					(block1 init: ignoreActors: setPri: 1 loop: 1 cel: step1)
					(block2 init: ignoreActors: setPri: 1 loop: 1 cel: step2)
					(block3 init: ignoreActors: setPri: 1 loop: 1 cel: step3)
					(block4 init: ignoreActors: setPri: 1 loop: 1 cel: step4)
					(block5 init: ignoreActors: setPri: 1 loop: 1 cel: step5)
					(block6 init: ignoreActors: setPri: 1 loop: 1 cel: step6)
					(block7 init: ignoreActors: setPri: 1 loop: 1 cel: step7)
					(block8 init: ignoreActors: setPri: 1 loop: 1 cel: step8)
					(block9 init: ignoreActors: setPri: 1 loop: 1 cel: step9)
				)
			)
		)
		(glow init: hide: ignoreActors: setScript: picScript)
		(shadowleft
			init:
			hide:
			ignoreActors:
			setScript: fallScript
		)
		(shadowright init: hide: ignoreActors:)
		
		(if g67TableMoved
			(table init: cel: 0 posn: 269 123)
			(gEgo observeControl: ctlFUCHSIA)
			(= tableMoved 1)
		else
			(table init:)
		)
		(if (> step1 2)
			(block1 init: ignoreActors: setPri: 1 setCycle: Fwd)
			(block2 init: ignoreActors: setPri: 1 setCycle: Fwd)
			(block3 init: ignoreActors: setPri: 1 setCycle: Fwd)
			(block4 init: ignoreActors: setPri: 1 setCycle: Fwd)
			(block5 init: ignoreActors: setPri: 1 setCycle: Fwd)
			(block6 init: ignoreActors: setPri: 1 setCycle: Fwd)
			(block7 init: ignoreActors: setPri: 1 setCycle: Fwd)
			(block8 init: ignoreActors: setPri: 1 setCycle: Fwd)
			(block9 init: ignoreActors: setPri: 1 setCycle: Fwd)
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			; entering the first time
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				
				(if (not g67TableMoved)
					(alterEgo view: 235 setCycle: Walk yStep: 5 setMotion: MoveTo 280 114 self)
				else
					(alterEgo view: 235 setCycle: Walk yStep: 5 setMotion: MoveTo 280 104 self)
				)
			)
			(2
				(if (not g67TableMoved)
					(= cycles 3)
					(alterEgo hide:)
					(gEgo show:)		
				else
					
					(alterEgo hide:)
					(gEgo show: posn: (alterEgo x?)(alterEgo y?) setMotion: MoveTo 275 130 self setPri: 13 ignoreControl: ctlWHITE ignoreControl: ctlFUCHSIA)
				)
			)
			(3
				(if g67TableMoved
					(gEgo setPri: -1 observeControl: ctlWHITE observeControl: ctlFUCHSIA)
					(PlayerControl)
					(SetCursor 999 (HaveMouse))
					(= gCurrentCursor 999)	
					(= canLook 1)	
				else
					(= seconds 2)
					(if (not g70GotMap)
						(ShakeScreen 2)
						(PrintOther 67 0)
						(gears setCycle: Fwd)
						(gTheSoundFX number: 203 play:)
					)
				)
			)
			(4
				(= seconds 1)
				(if (not g70GotMap)
					(DrawPic 167)
					(gears loop: 5)
					(block1 view: 167)
					(block2 view: 167)
					(block3 view: 167)
					(block4 view: 167)
					(block5 view: 167)
					(block6 view: 167)
					(block7 view: 167)
					(block8 view: 167)
					(block9 view: 167)
				)
			)
			(5
				(= cycles 2)
				(if (not g70GotMap)
					(gears setCycle: CT)
					(= g67_70dark 1)
				)
			)
			(6
				(if (not g70GotMap) (PrintOther 67 25))
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			
			(7
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(alterEgo
					init:
					show:
					posn: (gEgo x?) (gEgo y?)
					view: 0
					setCycle: Walk
					setMotion: MoveTo 278 111 RoomScript
					ignoreControl: ctlWHITE
					setPri: 14
				)
				(table ignoreActors:)
			)
			(8
				(alterEgo
					view: 235
					posn: 278 111
					setCycle: Walk
					yStep: 3
					setMotion: MoveTo 280 60 RoomScript
				)
			)
			(9
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gRoom newRoom: 41)
			)
			; stepping on the pressure plate and creating darkness again
			(10
				(= seconds 2)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(ShakeScreen 2)
				(gears setCycle: Fwd)
				(gTheSoundFX number: 203 play:)
			)
			(11
				(= seconds 2)
				(PrintOther 67 0)
				(DrawPic 167)
				(gears loop: 5)
				(block1 view: 167)
				(block2 view: 167)
				(block3 view: 167)
				(block4 view: 167)
				(block5 view: 167)
				(block6 view: 167)
				(block7 view: 167)
				(block8 view: 167)
				(block9 view: 167)
			)
			(12
			;	(= cycles 2)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gears setCycle: CT)
				(= g67_70dark 1)
				(DrawPic 67)
				(block1 view: 67)
				(block2 view: 67)
				(block3 view: 67)
				(block4 view: 67)
				(block5 view: 67)
				(block6 view: 67)
				(block7 view: 67)
				(block8 view: 67)
				(block9 view: 67)
			)
			; moving the table
			(13
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				
				(gEgo setMotion: MoveTo (table x?)(- (table y?) 10) self)	
			)
			(14	
				(PrintOther 67 19)
				(table cel: 0 posn: 269 122)
				(gEgo observeControl: ctlFUCHSIA)
				(= tableMoved 1)
				(= g67TableMoved 1)
				(gGame changeScore: 1)	
				
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
				(if canLook
					(if
						(and
							(> (pEvent x?) (table nsLeft?))
							(< (pEvent x?) (table nsRight?))
							(> (pEvent y?) (table nsTop?))
							(< (pEvent y?) (table nsBottom?))
						)
						(if (<= (gEgo distanceTo: table) 40)
							(if (not tableMoved)
								(PrintOther 67 1)
								(Print
									67
									27
									#title
									{It reads:}
									#font
									4
									#width
									120
									#at
									60
									-1
								)
							else
								(PrintOther 67 3)
								(return)
							)
						else
							(PrintOther 67 4)
						)
					else
						(if
							(and
								(> (pEvent x?) 257)   ; trip-plate
								(< (pEvent x?) 288)
								(> (pEvent y?) 110)
								(< (pEvent y?) 123)
							)
							(if (not tableMoved) (PrintOther 67 5))
						)                   ; #width 280 #at -1 20)
						(if
							(checkEvent
								pEvent
								(gears nsLeft?)
								(gears nsRight?)
								(gears nsTop?)
								(gears nsBottom?)
							)                                                                                   ; gears
							(PrintOther 67 22)
						)
					)
					(if
						(and
							(> (pEvent x?) 265)   ; ladder
							(< (pEvent x?) 292)
							(> (pEvent y?) 54)
							(< (pEvent y?) 107)
						)
						(PrintOther 67 6)
					)                ; #width 280 #at -1 20)
					(if
						(and
							(> (pEvent x?) 39)   ; shimmering floor
							(< (pEvent x?) 201)
							(> (pEvent y?) 111)
							(< (pEvent y?) 163)
						)
						(PrintOther 67 7)
					)
				)
			)
		)                           ; #width 280 #at -1 20) ; end candle
		(if (or (Said 'look,use,read,open/portal,map')
				(Said 'map'))
			(Print 0 88)
		)
		(if (Said 'take/stock')
			(if candle (PrintOther 67 10) else (PrintOther 67 12))
		)
		(if (Said 'use,turn/gear')
			(PrintOther 67 30)	
		)
		(if (Said '(look,read)>')
			(if candle
				(if (Said '/gear') (PrintOther 67 26))
				(if (Said '/table')
					(if (<= (gEgo distanceTo: table) 40)
						(if (not tableMoved)
							(PrintOther 67 1)
							(Print
								67
								27
								#title
								{It reads:}
								#font
								4
								#width
								120
								#at
								60
								-1
							)
						else
							(PrintOther 67 8)
						)
					else
						(PrintOther 67 4)
					)
				)
				(if (Said '/paper,note,letter')
					(if (<= (gEgo distanceTo: table) 40)
						(PrintOther 67 1)
						(Print
							67
							27
							#title
							{It reads:}
							#font
							4
							#width
							120
							#at
							60
							-1
						)
					else
						(PrintOther 67 4)
					)
				)
				(if (Said '/floor,ground') (PrintOther 67 9))
				(if (Said '/wall,gears') (PrintOther 67 22))
				(if (Said '/plate,trap') (PrintOther 67 18))
				(if (Said '/pit,hole,chasm') (PrintOther 67 31))
				(if (Said '/path') 
					(PrintOther 67 34)
				)
				(if (Said '/ladder') 
					(if g67TableMoved
						(PrintOther 67 33)
					else
						(PrintOther 67 32)	
					)
				)
				(if (Said '[/!*]') (PrintOther 67 10))
				; this will handle just "look" by itself
			else
				(if (Said '[/!*]') (PrintOther 67 12))
			)
		)
		(if (Said '(step,climb)<over/plate,trap')
			(if g67_70dark
				(PrintOther 67 29)		
			else
				(if g67TableMoved
					(PrintOK)
					(alterEgo posn: (gEgo x?) (gEgo y?))
					(RoomScript changeState: 7)
				else	
					(PrintOther 67 28)
				)	
			)	
		)
		(if (or (Said 'feel/around')
				(Said 'start,ignite,light/fire'))
			(if (not candle)
				(PrintOther 67 13)
				(= candle 1)
				(= canLook 1)
				(gGame changeScore: 1)
				(table cel: 0)
				(gEgo posn: 255 144 loop: 2)
				
				(= climbing 0)
			else
				(PrintOther 67 35)
			)	
		)
		(if (Said '(pick<up),take,use,light>')
			(if (Said '/candle,light,metal')
				(if (not candle)
					(PrintOther 67 13)
					(= candle 1)
					(= canLook 1)
					(gGame changeScore: 1)
					(table cel: 0)
					(gEgo posn: 255 144 loop: 2)
					
					(= climbing 0)
				else
					(PrintATI)
				)
			)
			(if (Said '/note,letter,paper')
				(if (not candle)
					(PrintOther 67 17)
				else
					(PrintOther 67 21)
					(Print
						67
						27
						#title
						{It reads:}
						#font
						4
						#width
						120
						#at
						60
						-1
					)
				)
			)
		)
		(if (Said 'use/kit')
			(if (not candle)
				(PrintOther 67 15)
			else
				(PrintOther 67 16)
			)
		)
		(if (or (Said 'climb') (Said 'use/ladder'))
			(cond 
				((not tableMoved)
					(cond 
						(g67_70dark (PrintOther 67 14))
						((not gHardMode) (PrintOK) (RoomScript changeState: 7)) ; The opening is shut. You need to find a way to open it first.
						(else (PrintNCE))          ; animation
					)
				)
				((not g67_70dark)
					(if (<= (gEgo distanceTo: table) 30)
						(PrintOK)
						(alterEgo posn: (gEgo x?) (gEgo y?))
						(RoomScript changeState: 7)
					else                           ; animation
						(PrintNCE)
					)
				)
				(else (PrintOther 67 24))
			)
		)
		(if (or (Said 'stop,plug/gear')
				(Said 'put/bar,rod/gear'))
				(if g67_70dark
					(PrintOther 67 36)		
				else	; print hint to use table
					(PrintOther 67 37)
				)	
		)
		(if
			(or (Said 'move/table') 
				(Said 'put,place/table')
				(Said 'cover/plate,trap'))
			(if (not tableMoved)
				(if (<= (gEgo distanceTo: table) 30)
					(PrintOther 67 19)
					(table cel: 0 posn: 269 122)
					(gEgo observeControl: ctlFUCHSIA)
					(= tableMoved 1)
					(= g67TableMoved 1)
					(gGame changeScore: 1)
				else
					(PrintNCE)
				)
			else
				(PrintOther 67 20)
			)
		)
	)
	
; (if(Said('ask/number'))   Test code to see if the Array would work...it does :)
;            FormatPrint("%u" step1)
;            FormatPrint("you have %u apples" gStepArray[0])
;            FormatPrint("you have %u apples" gStepArray[1])
;            FormatPrint("you have %u apples" gStepArray[2])
;            FormatPrint("you have %u apples" gStepArray[3])
;            FormatPrint("you have %u apples" gStepArray[4])
;            FormatPrint("you have %u apples" gStepArray[5])
;            FormatPrint("you have %u apples" gStepArray[6])
;            FormatPrint("you have %u apples" gStepArray[7])
;            FormatPrint("you have %u apples" gStepArray[8])
;        )
	(method (doit)
		(super doit:)
		
		(if (< step1 3)
			(if (< step2 3)
				(if (< step3 3)
					(if (< step4 3)
						(if (< step5 3)
							(if (< step6 3)
								(if (< step7 3)
									(if (< step8 3) (block9 loop: 1))
									(block8 loop: 1)
								)
								(block7 loop: 1)
							)
							(block6 loop: 1)
						)
						(block5 loop: 1)
					)
					(block4 loop: 1)
				)
				(block3 loop: 1)
			)
			(block2 loop: 1)
		)		

		(if g67_70dark
			(if (== gTorchOut 3001)
				(= gTorchOut 2500)	
			)
			(glow cel: (/ gTorchOut 501))
			
			(cond 
				(candle
					(if (not falling) (gEgo show:))
					(glow show: posn: (gEgo x?) (+ (gEgo y?) 55))
					(shadowleft
						show:
						posn: (- (gEgo x?) 145) (+ (gEgo y?) 55)
					)
					(shadowright
						show:
						posn: (+ (gEgo x?) 145) (+ (gEgo y?) 55)
					)
					(if (not picOrig) (picScript changeState: 2))
				)
				(picOrig (picScript changeState: 1))
				((not candle) (picScript changeState: 1))
			; (send gEgo:hide())
			)
		)
		(if (& (gEgo onControl:) ctlRED)       ; trip-plate
			(if (not g67_70dark)
				(if (not climbing)
					(if gHardMode
						(if (not plateTripped)
							(self changeState: 10) ; back in darkness
							(= plateTripped 1)
						)
					else
						(if (not plateTripped)
							(self changeState: 13)
						)
					)
				)
			)
		)
		(if (& (gEgo onControl:) ctlCYAN)
			(if (not falling) (fallScript changeState: 1))
		)
		(if (& (gEgo onControl:) ctlNAVY)
			(if (== step1 3)
				(= step1 (block1 cel?)) ; step one will equal 0,1, or 2
				(block1 setCycle: Cycle cel: step1)
			)
		)                                          ; makes the block stop shimmering
		(if (& (gEgo onControl:) ctlGREEN)
			(if (== step2 3)
				(= step2 (block2 cel?)) ; step two will equal 0,1, or 2
				(block2 setCycle: Cycle cel: step2)
			)
		)                                          ; makes the block stop shimmering
		(if (& (gEgo onControl:) ctlTEAL)
			(if (== step3 3)
				(= step3 (block3 cel?)) ; step three will equal 0,1, or 2
				(block3 setCycle: Cycle cel: step3)
			)
		)                                          ; makes the block stop shimmering
		(if (& (gEgo onControl:) ctlMAROON)
			(if (== step4 3)
				(= step4 (block4 cel?)) ; step three will equal 0,1, or 2
				(block4 setCycle: Cycle cel: step4)
			)
		)                                          ; makes the block stop shimmering
		(if (& (gEgo onControl:) ctlPURPLE)
			(if (== step5 3)
				(= step5 (block5 cel?)) ; step three will equal 0,1, or 2
				(block5 setCycle: Cycle cel: step5)
			)
		)                                          ; makes the block stop shimmering
		(if (& (gEgo onControl:) ctlBROWN)
			(if (== step6 3)
				(= step6 (block6 cel?)) ; step three will equal 0,1, or 2
				(block6 setCycle: Cycle cel: step6)
			)
		)                                          ; makes the block stop shimmering
		(if (& (gEgo onControl:) ctlSILVER)
			(if (== step7 3)
				(= step7 (block7 cel?)) ; step three will equal 0,1, or 2
				(block7 setCycle: Cycle cel: step7)
			)
		)                                          ; makes the block stop shimmering
		(if (& (gEgo onControl:) ctlGREY)
			(if (== step8 3)
				(= step8 (block8 cel?)) ; step three will equal 0,1, or 2
				(block8 setCycle: Cycle cel: step8)
			)
		)                                          ; makes the block stop shimmering
		(if (& (gEgo onControl:) ctlBLUE)
			(if (== step9 3)
				(= step9 (block9 cel?)) ; step three will equal 0,1, or 2
				(block9 setCycle: Cycle cel: step9) ; makes the block stop shimmering
				(= [gStepArray 0] step1)
				(= [gStepArray 1] step2)
				(= [gStepArray 2] step3)
				(= [gStepArray 3] step4)
				(= [gStepArray 4] step5)
				(= [gStepArray 5] step6)
				(= [gStepArray 6] step7)
				(= [gStepArray 7] step8)
				(= [gStepArray 8] step9)
			)
		)
		(if (& (gEgo onControl:) ctlLIME) (gRoom newRoom: 70))
	)
)

(instance picScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(DrawPic 900)
				(= picOrig 0)
				(gEgo hide:)
				(block1 hide:)
				(block2 hide:)
				(block3 hide:)
				(block4 hide:)
				(block5 hide:)
				(block6 hide:)
				(block7 hide:)
				(block8 hide:)
				(block9 hide:)
				(table hide:)
				(gears hide:)
				(= gMap 1)
			)
			(2
				(DrawPic 67)
				(= picOrig 1)
				(gEgo show:)
				(block1 show: view: 67)
				(block2 show: view: 67)
				(block3 show: view: 67)
				(block4 show: view: 67)
				(block5 show: view: 67)
				(block6 show: view: 67)
				(block7 show: view: 67)
				(block8 show: view: 67)
				(block9 show: view: 67)
				(table show:)
				(gears show:)
				(= gMap 0)
			)
		)
	)
)

(instance fallScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0)
			(1
				(= falling 1)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(if (and (> (gEgo x?) 80) (< (gEgo x?) 150)) ; if ego falling in the middle
					(alterEgo setPri: 13 view: 124 posn: (gEgo x?) (+ (gEgo y?) 10))
					(if (== (gEgo loop?) 0) ; walking right
						(alterEgo view: 124)	
					else
						(alterEgo view: 123)
					)	
				else
					(if (> (gEgo x?) 160)	; falling on the right
						(gEgo posn: 187 (+ (gEgo y?) 10))
						(if (== (gEgo loop?) 0) ; walking right
							(alterEgo view: 124)	
						else
							(alterEgo view: 123)
						)
						(alterEgo posn: (gEgo x?) (gEgo y?))
					else
						(gEgo posn: 56 (+ (gEgo y?) 10))	; falling on the left
						(alterEgo view: 123 posn: (gEgo x?) (gEgo y?))
					)
					(alterEgo setPri: 0 )	
				)
				
				(alterEgo show: yStep: 7 setMotion: MoveTo (gEgo x?) 160 fallScript)
				
			)
			(2
				(alterEgo setMotion: MoveTo (gEgo x?) 165 fallScript)
				(alterEgo hide:)
			)
			(3
				(= cycles 20)	
			)
			(4	(= cycles 14)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(ShakeScreen 2)
				(gTheSoundFX number: 200 play:)
				
			)
			(5
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 606
					register:
						{What's worse than being trapped in the sarcophagus of a dead man? You guessed it! Making it your own.}
				)
				(gGame setScript: dyingScript)
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

(instance alterEgo of Act
	(properties
		y 154
		x 66
		view 23
	)
)

(instance table of Prop
	(properties
		y 165
		x 244
		view 68
		loop 1
		cel 1
	)
)

(instance gears of Prop
	(properties
		y 125
		x 236
		view 67
		loop 3
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

(instance block1 of Prop
	(properties
		y 154
		x 186
		view 67
		loop 1
	)
)

(instance block2 of Prop
	(properties
		y 154
		x 156
		view 67
	)
)

(instance block3 of Prop
	(properties
		y 140
		x 152
		view 67
	)
)

(instance block4 of Prop
	(properties
		y 126
		x 148
		view 67
	)
)

(instance block5 of Prop
	(properties
		y 126
		x 118
		view 67
	)
)

(instance block6 of Prop
	(properties
		y 126
		x 88
		view 67
	)
)

(instance block7 of Prop
	(properties
		y 140
		x 92
		view 67
	)
)

(instance block8 of Prop
	(properties
		y 140
		x 62
		view 67
	)
)

(instance block9 of Prop
	(properties
		y 154
		x 66
		view 67
	)
)
