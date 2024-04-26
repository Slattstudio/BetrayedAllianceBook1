;;; Sierra Script 1.0 - (do not remove this comment)
; +2 SCORE //
(script# 36)
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
	rm036 0
)


; ROOM WITH TWO POLES - NO LIGHT (rm w/light 32)




(instance rm036 of Rm
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
		)
		(SetUpEgo)
		(gEgo init:)
		(= gEgoRunning 0)
		(RunningCheck)
		
		(rocks init: setPri: -1 setScript: rockfallScript)
		(mobileRocks
			init:
			hide:
			setPri: -1
			ignoreActors:
			ignoreControl: ctlWHITE
		)
		(bombThrower init: hide: ignoreActors:)
		(alterEgo
			init:
			hide:
			setScript: searchScript
			ignoreActors:
		)
		(deadGuy init:)
		(dynamite init: hide: ignoreControl: ctlWHITE)
		(skull init:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (handleEvent pEvent &tmp dyingScript)
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
						(PrintOther 36 23)
					)
					((checkEvent pEvent 75 122 87 107) (PrintOther 36 22)) ; altar
					((checkEvent pEvent 118 138 21 106) (PrintOther 36 19)) ; tall pillar
				)
				(if (checkEvent pEvent 52 73 82 107) (PrintOther 36 18)) ; short pillar
				(if (checkEvent pEvent 78 152 168 183)    ; straw bed
					(if (& (gEgo onControl:) ctlNAVY)
						(searchScript changeState: 6)	
					else
						(PrintOther 36 24)
					)
				)
				; bones
				(if
					(and
						(> (pEvent x?) (deadGuy nsLeft?))
						(< (pEvent x?) (deadGuy nsRight?))
						(> (pEvent y?) (deadGuy nsTop?))
						(< (pEvent y?) (deadGuy nsBottom?))
					)
					(if (<= (gEgo distanceTo: deadGuy) 35)
						(searchScript changeState: 1)
					else
						(PrintOther 36 7)
					)
				)
				(if
					(and
						(> (pEvent x?) (rocks nsLeft?))
						(< (pEvent x?) (rocks nsRight?))
						(> (pEvent y?) (rocks nsTop?))
						(< (pEvent y?) (rocks nsBottom?))
					)
					(PrintOther 36 20)
				)
			)
		)
		(if (Said 'run') (Print 0 88))
		(if (Said 'smell[/!*]')
			(if (gEgo has: INV_MEAT)
				(PrintOther 31 40)		
			else
				(PrintOther 31 39)	
			)
		)
		(if (Said 'use/ruler')
			(if (gEgo has: INV_RULER)
				(Print 36 17)
			else
				(PrintOther 36 15)
			)
		)
		(if (Said 'measure/pole,pillar')
			(if (gEgo has: INV_RULER)
				(PrintOther 36 16)
			else
				(PrintOther 36 15)
			)
		)
		(if (Said 'climb>')
			(if (Said '/pillar,pole') (PrintOther 36 12))
			(if (Said '/table,altar') (PrintOther 36 13))
			(if (Said '/*') (PrintOther 36 14))
		)
		; (if((Said('throw/bomb'))or(Said('use,light/bomb,stick'))or(Said('use/kit/stick')))
		(if
			(or
				(Said 'light/bomb,fuse/kit,fire,metal,flint')
				(Said 'use,combine/kit,fire,metal,flint/bomb')
				(Said 'use,combine/bomb/kit,fire,metal,flint')
			)
			; PrintOK()
			(if (gEgo has: 12)
				(rockfallScript changeState: 1)
			else
				(Print 36 0)
			)
		)
; You can't throw what you don't have!
		(if
			(or
				(Said 'use,throw/bomb,stick')
				(Said 'light/bomb,stick,fuse')
			)
			(if (gEgo has: 12)
				(rockfallScript changeState: 1)
			else
				(PrintOther 36 10)
			)
		)
		(if (Said 'use/kit,metal,flint')
			(if (gEgo has: 12)
				(if (not gHardMode)
					(rockfallScript changeState: 1)
				else
					(PrintOther 36 9)
				)
			else
				(PrintOther 36 9)
			)
		)
; (if(Said('(pick<up),take/bomb'))
;            (if (== IsOwnedBy(12 gRoomNumber) TRUE)
;                Print(36 2)
;            )(else
;                PrintOK()
;                (send gEgo:get(12))
;                (send gGame:changeScore(1))
;            )
;        )
		(if (Said 'search/bone')
			(if (& (gEgo onControl:) ctlRED)
				(searchScript changeState: 1)
			else
				(PrintNCE)
			)
		)
		(if (Said 'search/straw,bed')
			(if (& (gEgo onControl:) ctlNAVY)
				(searchScript changeState: 6)
			else
				(PrintNCE)
			)
		)
		(if (Said 'search/pile')
			(if (& (gEgo onControl:) ctlRED)
				(searchScript changeState: 1)
			else
				(if (& (gEgo onControl:) ctlNAVY)
					(searchScript changeState: 6)
				else
					(PrintNCE)
				)
			)
		)
		(if (Said 'use/map')
			(Print {This isn't a good place to use that.})
		)
		(if (Said 'take,(pick<up)>')
			(if (Said '/bone') (PrintOther 36 27))
			(if (Said '/rock') (PrintOther 36 30))
			(if (Said '/skull') (PrintOther 36 28))
			(if (Said '/*') (PrintOther 36 29))
		)
		(if (Said 'look>')
			(if (Said '/bone,pile')
				(if (not (gEgo has: INV_EXPLOSIVE))
					(if (& (gEgo onControl:) ctlRED)
						(searchScript changeState: 1)
					else
						(PrintOther 36 7)
					)
				)
			)
			(if (Said '/(pole,pillar)<tall') (PrintOther 36 19))
			(if (Said '/(pole,pillar)<short,small')
				(PrintOther 36 18)
			)
			(if (Said '/pole,pillar') (PrintOther 36 25))
			(if (Said '/skull') (PrintOther 36 23))
			(if (Said '/straw,bed') (PrintOther 36 24))
			(if (Said '/rock,light,sunlight,wall,ceiling,hole')
				(PrintOther 36 20)
			)
			(if (Said '*')
				(PrintOther 36 33)	
			)
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(PrintOther 36 26)
				(PrintOther 36 31)
			)
		)
	)
)

(instance rockfallScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(gEgo setMotion: MoveTo 160 125 rockfallScript)
			)
			(2
				(PrintOther 36 5)
				(gEgo hide:)
				(bombThrower
					show:
					x: (gEgo x?)
					y: (gEgo y?)
					loop: 3
					cel: 0
					setCycle: End rockfallScript
					cycleSpeed: 3
				)
			)
			(3
				(bombThrower loop: 4 cel: 0 setCycle: End self)
			)
			(4
				(= cycles 12)
				(bombThrower loop: 11 cel: 0 setCycle: Fwd cycleSpeed: 2)
			)
			(5
				(bombThrower loop: 5 cel: 0)
				(dynamite
					init:
					show:
					posn: (gEgo x?) (- (gEgo y?) 10)
					xStep: 6
					yStep: 6
					setCycle: Fwd
					setMotion: MoveTo 55 5 self
					ignoreControl: ctlWHITE
					ignoreActors:
				)
			)
			(6
				(ShakeScreen 5)
				(dynamite hide:)
				(rocks setCycle: End rockfallScript)
				(gTheSoundFX number: 207 play:)	
			)
			(7
				(Print 36 4 #width 280 #at -1 140)
				(rocks hide:)
				(mobileRocks
					show:
					yStep: 10
					setCycle: Walk
					setMotion: MoveTo 72 110 rockfallScript
					setPri: 1
				)
				(gEgo put: 12 36)
			)
			(8 (gRoom newRoom: 32))
		)
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 31)
		)
		(if (< (gEgo x?) 219)     ; deep into the darkness
			(= gEgoStoppedView 103)
			(= gEgoView 104)
		else
			(= gEgoStoppedView 903)
			(= gEgoView 0)
		)
	)
)

(instance searchScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo (+ (deadGuy x?) 20) (+ (deadGuy y?) 5) self ignoreControl: ctlWHITE)
			)
			(2
				(gEgo hide:)
				(alterEgo show: view: 232 loop: 1 posn: (gEgo x?) (gEgo y?) setCycle: End searchScript cycleSpeed: 2)
			)
			(3
				(= cycles 8)
				(if (not (gEgo has: INV_EXPLOSIVE))
					(Print 36 3 #icon 264 #title {Explosives})
					(gEgo get: INV_EXPLOSIVE)
					(gGame changeScore: 2)
				else
					(PrintOther 36 8)
				)
			)
			(4
				(alterEgo setCycle: Beg searchScript)
			)
			(5
				(gEgo show: loop: 1 observeControl: ctlWHITE)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			; searching the straw bed
			(6
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 147 174 self ignoreControl: ctlWHITE)
			)
			(7
				(gEgo hide:)
				(alterEgo show: view: 136 loop: 1 posn: (gEgo x?) (gEgo y?) setCycle: End self cycleSpeed: 2)
			)
			(8	(= cycles 8)
				
			)
			(9
				(PrintOther 36 32)
				(alterEgo setCycle: Beg self)
			)
			(10
				(gEgo show: loop: 1 observeControl: ctlWHITE)
				(alterEgo hide:)
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

(instance rocks of Prop
	(properties
		y 40
		x 72
		view 48
	)
)

(instance bombThrower of Prop
	(properties
		y 37
		x 52
		view 407
	)
)

(instance mobileRocks of Act
	(properties
		y 40
		x 72
		view 52
	)
)

(instance dynamite of Act
	(properties
		y 130
		x 140
		view 51
	)
)

(instance deadGuy of Prop
	(properties
		y 108
		x 202
		view 98
		loop 1
	)
)

(instance skull of Prop
	(properties
		y 90
		x 100
		view 98
		loop 3
	)
)

(instance alterEgo of Prop
	(properties
		y 164
		x 68
		view 232
		loop 1
	)
)
