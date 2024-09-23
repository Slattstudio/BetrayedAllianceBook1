;;; Sierra Script 1.0 - (do not remove this comment)
;
;                            Hill south of Tavern                               *
(script# 43)
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
	rm043 0
)

(local


	falling =  0    ; player falling off the edge
	; 1 = falling to death
	; 2 = falling to ledge
	walkingDown =  0 ; player moving up or down the path
	; 1 - walking down
	; 2 - walking up
	coinVis =  1
)

(instance rm043 of Rm
	(properties
		picture scriptNumber
		north 0
		east 48
		south 0
		west 29
	)
	
	(method (init)
		(super init:)
		(self
			setScript: RoomScript
			setRegions: 200
			setRegions: 204
		)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 60 loop: 2)
			)
			(28 (gEgo posn: 150 60 loop: 2))
			; (send gTheMusic:prevSignal(0)stop())
			(29 (gEgo posn: 20 60 loop: 0))
			(48 (gEgo posn: 300 60 loop: 1))
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		(alterEgo
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
			setScript: walkingScript
		)
		(deathSplash
			init:
			hide:
			ignoreActors:
			setPri: 1
			setScript: pickUpScript
		)
		
		(ripple1 init: ignoreActors: setCycle: Fwd cycleSpeed: 6)
		(ripple2 init: ignoreActors: setCycle: Fwd cycleSpeed: 6)
		(ripple3 init: ignoreActors: setCycle: Fwd cycleSpeed: 6)
		
		(coin init: ignoreActors: setPri: 3 setScript: coinShine)
		(if (and g43Gold (> gGold 5))
			(coin hide:)
			(= coinVis 0)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		
		(if (== (pEvent type?) evJOYSTICK)
			(if (== (pEvent message?) 1) ; if pressed UP to run back up the hill
				(if (== walkingDown 1)
					(++ walkingDown)	; set walkingUp to 2
					(switch (walkingScript state:)
						(1
							(walkingScript changeState: 7)
						)
						(2
							(walkingScript changeState: 6)
						)
						(3
							(walkingScript changeState: 5)
						)
					)			
				)
			)
			(if (== (pEvent message?) 5) ; if pressed Down to run back down the hill
				(if (== walkingDown 2)
					(-- walkingDown)	; set walkingDown to 1
					(switch (walkingScript state:)
						(5
							(walkingScript changeState: 3)
						)
						(6
							(walkingScript changeState: 2)
						)
						(7
							(walkingScript changeState: 1)
						)
					)			
				)
			)
		)
		
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) 74)        ; tavern
						(< (pEvent x?) 113)
						(> (pEvent y?) 27)
						(< (pEvent y?) 51)
					)
					(PrintOther 43 0)
				)                    ; "Over the hill is a tiny hut. The smoky aroma of cooking meat is noticeable even from here." #at -1 28)
				(if
					(and
						(> (pEvent x?) 93)        ; hill
						(< (pEvent x?) 201)
						(> (pEvent y?) 100)
						(< (pEvent y?) 147)
					)
					(PrintOther 43 1)
				)                    ; "The incline of this hill is quite steep. Watch your step." #at -1 28)
				(if
					(or
						(==
							ctlGREEN
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                ; dock
						(==
							ctlYELLOW
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)
					)
					(PrintOther 43 2) ; This small jetty allows for smaller vessels to land and patronize the local tavern. It's also a nice spot for fishing.
					(if coinVis (PrintOther 43 6))
				)
			)
		)
		(if (Said 'look>')
			(if (Said '/house,bar,hut') (PrintOther 43 0))
			(if (Said '/hill') (PrintOther 43 1))
			(if (Said '/dock')
				(PrintOther 43 2)
				(if coinVis (PrintOther 43 6))
			)
			(if (Said '/coin,glimmer') (PrintOther 43 5))
			(if (Said '[/!*]') (PrintOther 43 3))
		; this will handle just "look" by itself
		)                        ; "The wind is soft upon your face and the smell of fresh water fills your nostrils. At the bottom of the hill is a jetty and a nice place for fishing during more peaceful times.
		(if (or (Said 'fish') (Said 'go/fish'))
			(Print {Bored already?} #at -1 8)
		)
		(if (or (Said 'swim') (Said 'go/swimming'))
			(PrintOther 43 4)
		)
		(if (Said 'take,(pick<up)/coin,gold')
			(if coinVis
				(if (> (gEgo y?) 132)
					(pickUpScript changeState: 1)
				else
					(PrintNCE)
				)
			else
				(Print {There's nothing to take.})
			)
		)
	)
	
	(method (changeState mainState &tmp dyingScript)
		(= state mainState)
		(switch state
			(0)
			(1       ; Falling from cliff
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(alterEgo
					show:
					view: 23
					loop: 2
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					yStep: 7
					setMotion: MoveTo (gEgo x?) 175 self
					setPri: 15
				)
			)
			(2       ; splash
				(alterEgo hide:)
				(deathSplash
					show:
					posn: (gEgo x?) 189
					setCycle: End self
					setPri: 1
					cycleSpeed: 3
				)
				(if (== falling 3) ; falling from the bottom
					(deathSplash y: 199)	
				)
				(gTheSoundFX number: 202 play:)
			)
			(3       ; death
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 603
					register:
						{Going for a swim in heavy armor? Didn't you have a sinking feeling about doing that?}
				)
				(gGame setScript: dyingScript)
			)
			(4
				(gEgo hide: setMotion: MoveTo (gEgo x?) (gEgo y?))
				(alterEgo
					init:
					view: 23
					cel: 0
					x: (gEgo x?)
					y: (gEgo y?)
					setCycle: End RoomScript
					ignoreControl: ctlWHITE
				)
			)
			; (send gTheMusic:prevSignal(0)stop()number(903)play())
			(5
				(alterEgo
					yStep: 7
					setMotion: MoveTo (alterEgo x?) 170 RoomScript
				)
			)
			(6 (RoomScript changeState: 3))
			; (send gTheMusic:prevSignal(0)stop())
			(7
				(= cycles 20)       ; ego hits grounds
				(ShakeScreen 2)
				(alterEgo
					view: 409
					loop: 0
					cel: 0
					setMotion: NULL
					setCycle: Fwd
					cycleSpeed: 3
					setPri: 6
				)
				(gTheSoundFX number: 200 play:)
				(= gHlth (- gHlth 2))
				(if (< gHlth 1)
					(= dyingScript (ScriptID DYING_SCRIPT))
					(dyingScript
						caller: 609
						register:
							{Ouch! That fall hurt just a bit too much. Be wary of your health when deciding not to climb.}
					)
					(gGame setScript: dyingScript)
				)
			)
			(8       ; ego stands up
				(alterEgo loop: 1 setCycle: End self)
			)
			(9
				(= cycles 7)      ; ego gets ready to walk
				(alterEgo hide: setPri: -1)
				(gEgo show: posn: (alterEgo x?) (alterEgo y?))
			)
			(10
				(= walkingDown 1)
				(if (> (gEgo x?) 140)     ; ego on right side
					(self changeState: 13)
				else
					(self cue:)
				)
			)
			(11
				(gEgo setMotion: MoveTo 216 98 self setPri: 8)
			)                                                       ; walk to right side
			(12
				(gEgo setMotion: MoveTo 213 92 self setPri: -1)
			)
			(13
				(gEgo setMotion: MoveTo 123 73 self)
			)
			(14
				(= walkingDown 0)
				(= falling 0)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 28)
		)
		(if (not walkingDown)
			(cond 
				((& (gEgo onControl:) ctlBLUE) (if (not falling) (self changeState: 1) (= falling 3)))
				((& (gEgo onControl:) ctlNAVY) (if (not falling) (self changeState: 1) (= falling 2)))
				((& (gEgo onControl:) ctlRED) (if (not falling) (self changeState: 1) (= falling 1)))
			)
			(if (& (alterEgo onControl:) ctlSILVER)
				(if (== falling 2) (self changeState: 7) (= falling 3))
			)
		)
		(if (& (gEgo onControl:) ctlGREY)
			(if (not falling)
				(if (not walkingDown)
					(if (> (gEgo y?) 100) ; at the bottom
						(walkingScript changeState: 5)
						(= walkingDown 2)
					else ; at the top
						(walkingScript changeState: 1)
						(= walkingDown 1)
					)
				)
			)
		)
	)
)

(instance coinShine of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0
				(coin setCycle: End self cycleSpeed: 2)
			)
			(1 (= cycles (Random 40 100)))
			(2 (self changeState: 0))
		)
	)
)

(instance pickUpScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0)
			(1      ; move to coin
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 159 168 self
					ignoreControl: ctlWHITE
				)
			)
			(2
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					loop: 0
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(3
				(= cycles 6)
				(Print
					{Huzzah! It's your lucky day, it's a gold coin...head's side up! You place it in your bag.}
					#at
					-1
					10
				)
				(coin hide:)
				(= coinVis 0)
				(++ gGold)
				(= g43Gold 1)
			)
			(4
				(alterEgo setCycle: Beg self)
			)
			(5
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(alterEgo hide:)
				(gEgo show: observeControl: ctlWHITE)
			)
		)
	)
)

(instance walkingScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(0)
			(1      ; walking down
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 216 96 self)
			)
			(2
				(gEgo setMotion: MoveTo 115 133 self setPri: 8)
			)
			(3
				(gEgo setMotion: MoveTo 160 163 self setPri: -1)
			)
			(4
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= walkingDown 0)
			)
			(5      ; walking up
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 115 133 self)
			)
			(6
				(gEgo setMotion: MoveTo 216 96 self setPri: 8)
			)
			(7
				(gEgo setMotion: MoveTo 124 72 self setPri: -1)
			)
			(8
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= walkingDown 0)
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 100)
		(Print textRes textResIndex #width 300 #at -1 10)
	else
		(Print textRes textResIndex #width 300 #at -1 130)
	)
)

(instance alterEgo of Act
	(properties
		y 130
		x 40
		view 33
	)
)

(instance coin of Prop
	(properties
		y 167
		x 175
		view 94
	)
)

(instance deathSplash of Prop
	(properties
		y 187
		x 147
		view 87
		loop 1
	)
)

(instance ripple1 of Prop
	(properties
		y 174
		x 133
		view 163
	)
)

(instance ripple2 of Prop
	(properties
		y 189
		x 150
		view 163
	)
)
(instance ripple3 of Prop
	(properties
		y 189
		x 207
		view 163
	)
)
