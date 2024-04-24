;;; Sierra Script 1.0 - (do not remove this comment)
; Score +3 // gInt +1 //
(script# 32)
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
	rm032 0
)


; Inside Cave Two Poles (light)




(instance rm032 of Rm
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
				(gEgo posn: 270 120 loop: 1)
			)
			(36
				(gEgo posn: 160 125 loop: 1)
				(RoomScript changeState: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(= gEgoRunning 0)
		(RunningCheck)
		(deadGuy init:)
		(skull init:)
		(alterEgo init: hide: ignoreActors: setScript: measureScript)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			(1 (= cycles 3))
			(2
				(= cycles 5)
				(ShakeScreen 4)
				(gGame changeScore: 3)
				(++ gInt)
				(gTheSoundFX number: 200 play:)	
			)
			(3
				(if (not gHardMode) (PrintOther 32 0))
			)
		)
	)
	
; The light casts shadows onto the ground. Perhaps this can shed some light on the troll's riddle.
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (checkEvent pEvent 283 319 57 127)    ; recess leading ack to the ogre.
					(PrintOther 36 21)
				)
				(cond 
					(
						(checkEvent
							pEvent
							(skull nsLeft?)
							(skull nsRight?)
							(skull nsTop?)
							(skull nsBottom?)
						)                                                                             ; skull
						(PrintOther 32 2)
					)
					((checkEvent pEvent 75 122 87 107) (PrintOther 32 3)) ; altar
					((checkEvent pEvent 118 138 21 106)           ; tall pillar
						(PrintOther 32 4)
						(if (not g31TrollGone) (PrintOther 32 5))
					)
					(
						(==
							ctlNAVY
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                          ; SMALL PILLAR shadow
						(PrintOther 32 15)
					)
					(
						(==
							ctlGREEN
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                               ; tall PILLAR  shadow
						(PrintOther 32 15)
					)
				)
				(if (checkEvent pEvent 52 73 82 107) (PrintOther 32 6)) ; short pillar
				(if (checkEvent pEvent 78 152 168 183)    ; straw bed
					(PrintOther 36 24)
				)
				; bones
				(if
					(and
						(> (pEvent x?) (deadGuy nsLeft?))
						(< (pEvent x?) (deadGuy nsRight?))
						(> (pEvent y?) (deadGuy nsTop?))
						(< (pEvent y?) (deadGuy nsBottom?))
					)
					(PrintOther 32 1)
				)
			)
		)
		(if (Said 'smell[/!*]')
			(if (gEgo has: INV_MEAT)
				(PrintOther 31 40)		
			else
				(PrintOther 31 39)	
			)
		)
		(if (Said 'use/map')
			(Print {This isn't a good place to use that.})
		)
		(if (Said 'talk/skull')
			(PrintOther 32 22)
		)
		(if (Said 'stand,(get<on)/table,altar')
			(PrintOther 32 19)
		)
		(if (Said 'use/ruler')
			(if (gEgo has: INV_RULER)
				(PrintOther 32 7)
			else
				(PrintOther 32 13)
			)
		)
		(if (Said 'search/bone,pile') (PrintOther 32 1))
		(if
			(or
				(Said 'throw/bomb')
				(Said 'use,light/bomb,stick')
				(Said 'use/kit/stick')
			)
			(PrintOther 32 8)
		)
		(if (Said 'measure/((pole,pillar)<(big,long))')
			(if (gEgo has: INV_RULER)
				(if
					(or
						(& (gEgo onControl:) ctlGREY)
						(if (& (gEgo onControl:) ctlSILVER))
					)
					(measureScript changeState: 12)
				else
					(PrintNCE)
				)
			else
				(PrintOther 32 13)
			)
		)
		(if (Said 'measure/((pole,pillar)<(small,short))')
			(if (gEgo has: INV_RULER)
				(if
					(or
						(& (gEgo onControl:) ctlGREY)
						(if (& (gEgo onControl:) ctlSILVER))
					)
					(measureScript changeState: 9)
				else
					(PrintNCE)
				)
			else
				(PrintOther 32 13)
			)
		)
		(if (Said 'measure/pole,pillar')
			(if (gEgo has: INV_RULER)
				(cond 
					((& (gEgo onControl:) ctlGREY) (measureScript changeState: 9)) ; Near Small Pole
					((& (gEgo onControl:) ctlSILVER)          ; Near Tall Pole
						(measureScript changeState: 12)
						;(PrintOther 32 14)						
					)
					(else (PrintNCE))
				)
			else
				(PrintOther 32 13)
			)
		)
		(if (Said 'measure/(shadow<(short,small))')
			(if (gEgo has: INV_RULER)
				(if
					(or
						(& (gEgo onControl:) ctlGREY)
						(if (& (gEgo onControl:) ctlSILVER))
					)
					(measureScript changeState: 1)
					;(PrintOther 32 11)
				else
					(PrintNCE)
				)
			else
				(PrintOther 32 13)
			)
		)
		(if (Said 'measure/(shadow<(big,long))')
			(if (gEgo has: INV_RULER)
				(if
					(or
						(& (gEgo onControl:) ctlGREY)
						(if (& (gEgo onControl:) ctlSILVER))
					)
					(measureScript changeState: 5)
					;(PrintOther 32 12)
				else
					(PrintNCE)
				)
			else
				(PrintOther 32 13)
			)
		)
		(if (Said 'measure/shadow')
			(if (gEgo has: INV_RULER)
				(cond 
					((& (gEgo onControl:) ctlGREY) (measureScript changeState: 1)) ; Near Small Pole
					((& (gEgo onControl:) ctlSILVER) (measureScript changeState: 5)) ; Near Tall Pole
					(else (PrintNCE))
				)
			else
				(PrintOther 32 13)
			)
		)
		(if (Said 'measure/*')
			(PrintOther 32 24)			
		)
		(if (Said 'climb>')
			(if (Said '/pillar,pole') (PrintOther 36 12))
			(if (Said '/table,altar') (PrintOther 36 13))
			(if (Said '/*') (PrintOther 36 14))
		)
		(if (Said 'take>')
			(if (Said '/bone') (PrintOther 36 27))
			(if (Said '/skull') (PrintOther 32 23))
			(if (Said '/*') (PrintOther 36 29))
		)
		(if (Said 'look>')
			(if (Said '/bone,pile') (PrintOther 32 1))
			(if (Said '/(pole,pillar)<tall')
				(PrintOther 36 4)
				(if (not g31TrollGone) (PrintOther 36 5))
			)
			(if (Said '/(pole,pillar)<short,small')
				(PrintOther 32 6)
			)
			(if (Said '/table,altar') (PrintOther 32 3))
			(if (Said '/ground,floor') (PrintOther 32 26))
			(if (Said '/skull,head') (PrintOther 32 2))
			(if (Said '/pole,pillar') (PrintOther 32 17))
			(if (Said '/rock, light') (PrintOther 32 9))
			(if (Said '/shadow') (PrintOther 32 18))
			(if (Said '/skull') (PrintOther 32 2))
			(if (Said '/straw,bed') (PrintOther 36 24))
			(if (Said '[/!*]') (PrintOther 32 20))
			; this will handle just "look" by itself
		)
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 31)
		)
	)
)
(instance measureScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
			)
			; move to measure small shadow
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				
				(gEgo setMotion: MoveTo 92 114 self ignoreControl: ctlWHITE)	
			)
			(2
				(gEgo hide:)
				(alterEgo show: view: 232 loop: 1 cel: 0 posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2
				)	
			)
			(3
				(PrintOther 32 11)
				(alterEgo setCycle: Beg self)	
			)
			(4
				(alterEgo hide:)
				(gEgo show: loop: 1 observeControl: ctlWHITE)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)	
			)
			; measuring the large shadow
			(5
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				
				(gEgo setMotion: MoveTo 128 119 self ignoreControl: ctlWHITE)	
			)
			(6
				(gEgo hide:)
				(alterEgo show: view: 232 loop: 0 cel: 0 posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2)	
			)
			(7
				(PrintOther 32 12)
				(alterEgo setCycle: Beg self)	
			)
			(8
				(alterEgo hide:)
				(gEgo show: loop: 0 observeControl: ctlWHITE)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)	
			)
			; measureing the small pillar
			(9
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				
				(gEgo setMotion: MoveTo 64 114 self ignoreControl: ctlWHITE)		
			)
			(10	(= cycles 4)
				(gEgo loop: 3)	
			)
			(11
				(PrintOther 32 10)
				
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)			
			)
			; measureing the tall pillar
			(12
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				
				(gEgo setMotion: MoveTo 129 114 self ignoreControl: ctlWHITE)		
			)
			(13	(= cycles 4)
				(gEgo loop: 3)	
			)
			(14
				(PrintOther 32 14)
				(if gHardMode
					(Print
						{There must be a better way.}
						#width
						280
						#at
						-1
						10
						#title
						{You think:}
					)
				else
					(PrintOther 32 16)
				)
				
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)			
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
		y 1
		x 1
		view 350
	)
)
(instance skull of Prop
	(properties
		y 90
		x 100
		view 98
		loop 2
	)
)

(instance deadGuy of Prop
	(properties
		y 108
		x 202
		view 98
	)
)
