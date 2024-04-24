;;; Sierra Script 1.0 - (do not remove this comment)

(script# 59)
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
	rm059 0
)

(local
; Cavern of Madness (Ghost)



	myEvent
	ghostAwake = 0
	awakening = 0
	
	intro =  0
	target =  0
	shot =  0
	flow1 =  0
	flow2 =  0
	flow3 =  0
	flow4 =  0
	tripped =  0
	deathAnimation =  0
)

(instance rm059 of Rm
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
				(gEgo posn: 146 170 loop: 1)
			)
			(73
				(gEgo posn: 146 170 loop: 3)
			)
		)
		(SetUpEgo)
		(gEgo init: setPri: 4)
		(water1 init: hide: setPri: 1 setScript: stream1Script)
		(water2 init: hide: setPri: 2 setScript: ghost2Script)
		(water3 init: hide: setPri: 0 setScript: stream2Script)
		(water4 init: hide: setPri: 1)
		(water5 init: hide: setPri: 0 setScript: stream3Script)
		(water6 init: hide: setPri: 2)
		(water7 init: hide: setPri: 2 setScript: stream4Script)
		(water8 init: hide: setPri: 0)
		
		(ghost init: hide: setPri: 4 cycleSpeed: 2 setScript: ghostScript xStep: 4 yStep: 2 ignoreActors:)
		
		(waterButton1 init: setPri: 4)
		(waterButton2 init: setPri: 4)
		(waterButton3 init: setPri: 4)
		(waterButton4 init: setPri: 5 ignoreActors:)
		
		(dart
			init:
			hide:
			ignoreActors:
			xStep: 15
			yStep: 15
			setPri: 4
			ignoreControl: ctlWHITE
		)
		
		(alterEgo
			init:
			ignoreActors:
			ignoreControl: ctlWHITE
			hide:
			setScript: tripScript
		)
		(ghostScript changeState: 1)
		(ghost2Script changeState: 1)
		(alterGhost init: hide: ignoreActors:)
		(skeleton init: ignoreActors:)
		
		
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState &tmp dyingScript)
		(= state mainState)
		(switch state
			(0 (= seconds 2))
			(1
				(= seconds 2)
				
			)
			(2 (= intro 1))
			(3 (= intro 0))
			(4
				(gEgo hide:)
				(alterEgo
					show:
					view: 428
					loop: 1
					cel: 0
					posn: (+ (gEgo x?) 14) (- (gEgo y?) 0)
				)
				(ghost hide:)
				(alterGhost
					show:
					posn: (ghost x?) (ghost y?)
					setCycle: End RoomScript
					cycleSpeed: 2
				)
			)
			(5
				(alterEgo setCycle: End RoomScript cycleSpeed: 2)
			)
			(6
				(= cycles 10)
				(ShakeScreen 1)
				(gTheSoundFX number: 200 play:)
			)
			(7
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 604
					register:
						{The hatred and sheer malice of this apparition fill every thought in your mind and all you can hear is a voice saying, 'now you're mine forever.'}
				)
				(gGame setScript: dyingScript)
			)
		)
	)
	
	(method (handleEvent pEvent &tmp dyingScript)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) (ghost nsLeft?))
						(< (pEvent x?) (ghost nsRight?))
						(> (pEvent y?) (ghost nsTop?))
						(< (pEvent y?) (ghost nsBottom?))
					)
					(PrintOther 59 3)
					(return)
				)
				(if
					(and
						(> (pEvent x?) (skeleton nsLeft?))
						(< (pEvent x?) (skeleton nsRight?))
						(> (pEvent y?) (skeleton nsTop?))
						(< (pEvent y?) (skeleton nsBottom?))
					)
					(PrintOther 59 13)
				)
			)
			(if (not shot)
				(if (== target 1)
					(if (> gApple 0)
						(dartScript changeState: 1)
					else
						(PrintOther 59 0)
					)
				)
				(if (== target 2)
					(if (> gApple 0)
						(dartScript changeState: 3)
					else
						(PrintOther 59 0)
					)
				)
				(if (== target 3)
					(if (> gApple 0)
						(dartScript changeState: 5)
					else
						(PrintOther 59 0)
					)
				)
				(if (== target 4)
					(if (> gApple 0)
						(dartScript changeState: 7)
					else
						(PrintOther 59 0)
					)
				)
			)
		)
		(if (Said 'look>')
			(if (Said '/ghost')
				(if ghostAwake
					(PrintOther 59 3)
				else
					(PrintOther 59 4)
				)
			)
			(if (Said '/switch,button')
				(PrintOther 59 5)
			)
			(if (Said '/skeleton,corpse,body')
				(PrintOther 59 13)
			)
			(if (Said '/floor,floor,path')
				(PrintOther 59 6)
				(PrintOther 59 7)
			)
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(PrintOther 59 8)
				(if ghostAwake
					(PrintOther 59 9)
				)
			)
		)
		
		(if (Said 'talk,ask/ghost,man')
			(PrintOther 59 10)
			
		)
		(if (Said 'attack,fight/ghost,man')
			(PrintOther 59 11)
		)
		(if
		(Said 'use,shoot,blow/dart,dartgun,gun,(dart<gun)')
			(PrintOther 59 12)
		)
		; (stream1Script:changeState(1))
		; (stream2Script:changeState(1))
		; (stream3Script:changeState(1))
		; (stream4Script:changeState(1))
		(if (Said 'use/map')
			(Print {This isn't a good place to use that.})
		)
		(if (or (Said 'listen[/!*]')
				(Said 'listen/voice'))
			(PrintWhisper)
			(if (== gListened 74)
				(PrintOther 58 75)	
			)
			(if (== gListened 75)
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 604
					register:
						{Where the voice came from, you cannot say. But you dwelt too deeply on despairing thoughts, driving you to lose your mind and your life.}
				)
				(gGame setScript: dyingScript)	
			)			
		)
	)
	
	(method (doit &tmp dyingScript)
		(super doit:)
		(= myEvent (Event new: evNULL))
		(if (gEgo has: 8)     ; INV_BLOW_DART_GUN
			(cond 
				(
					(and
						(> (myEvent x?) (waterButton1 nsLeft?))
						(< (myEvent x?) (waterButton1 nsRight?))
						(> (myEvent y?) (waterButton1 nsTop?))
						(< (myEvent y?) (waterButton1 nsBottom?))
					)
					(SetCursor 994 (HaveMouse))
					(= gCurrentCursor 994)
					(= target 1)
					(= gNoClick 1)
				)
				(
					(and
						(> (myEvent x?) (waterButton2 nsLeft?))
						(< (myEvent x?) (waterButton2 nsRight?))
						(> (myEvent y?) (waterButton2 nsTop?))
						(< (myEvent y?) (waterButton2 nsBottom?))
					)
					(SetCursor 994 (HaveMouse))
					(= gCurrentCursor 994)
					(= target 2)
					(= gNoClick 1)
				)
				(
					(and
						(> (myEvent x?) (waterButton4 nsLeft?))
						(< (myEvent x?) (waterButton4 nsRight?))
						(> (myEvent y?) (waterButton4 nsTop?))
						(< (myEvent y?) (waterButton4 nsBottom?))
					)
					(SetCursor 994 (HaveMouse))
					(= gCurrentCursor 994)
					(= target 3)
					(= gNoClick 1)
				)
				(
					(and
						(> (myEvent x?) (waterButton3 nsLeft?))
						(< (myEvent x?) (waterButton3 nsRight?))
						(> (myEvent y?) (waterButton3 nsTop?))
						(< (myEvent y?) (waterButton3 nsBottom?))
					)
					(SetCursor 994 (HaveMouse))
					(= gCurrentCursor 994)
					(= target 4)
					(= gNoClick 1)
				)
				(else
					(= target 0)
					(SetCursor 999 (HaveMouse))
					(= gCurrentCursor 999)
					(= gNoClick 0)
				)
			)
		)
		(myEvent dispose:)
		
		(if (& (gEgo onControl:) ctlGREY)
			(if (not ghostAwake)
				(if (not awakening)
					(ghost2Script changeState: 6)
					(= awakening 1)
				)
			)
		)
		
		(if (& (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 73)
		)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 75)
		)
		(if (& (gEgo onControl:) ctlBLUE)
			(if gEgoRunning
				(if (not tripped)
					(= tripped 1)
					(tripScript changeState: 1)
				)
			)
		)
; (if(& (send gEgo:onControl()) ctlGREY)
;            (if(not(intro))
;                = intro 1  // sends the ghost after you
;            )
;        )
		(if ghostAwake
			(cond 
				(flow1
					(if (< (ghost y?) 96)
						(ghost setMotion: MoveTo (gEgo x?) (ghost y?))
					else                                             ; horizontal
						(ghost setMotion: Follow gEgo)
					)
				)
				(flow2
					(if (< (ghost x?) 96)
						(ghost setMotion: MoveTo (ghost x?) (gEgo y?))
					else
						(ghost setMotion: Follow gEgo)
					)
				)
				(flow3
					(if (< (ghost y?) 115)
						(ghost setMotion: Follow gEgo)
					else
						(ghost setMotion: MoveTo (gEgo x?) (ghost y?))
					)
				)
				((>= (gEgo distanceTo: ghost) 30)
					(if intro
						(ghost setMotion: Follow gEgo)
					else
						(ghost setMotion: MoveTo (gEgo x?) (ghost y?))
					)
				)
			)
			(cond 
				((>= (gEgo distanceTo: ghost) 20))
	; /(if(intro)
	;                (ghost:setMotion(Follow gEgo))
	;            )
				((not deathAnimation) (RoomScript changeState: 4) (= deathAnimation 1))
			)
			(if (> (gEgo x?) (ghost x?))
				(ghost loop: 1)
			else
				(ghost loop: 0)
			)
		)
	)
)

; (if(>=(send gEgo:distanceTo(ghost))30)
;            (if(intro)
;                (ghost:setMotion(Follow gEgo))
;            )
;        )(else
;            //death
;        )
(instance stream1Script of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= flow1 1)
				(ghost observeControl: $0400) ; LIGHT GREEN
				(gEgo observeControl: $0400)
				(water1 show: setCycle: End stream1Script)
				(gTheSoundFX number: 202 play:)
			)
			(2
				(water2 show: setCycle: End stream1Script)
			)
			(3
				(= seconds 8)
				(if (not intro) (= intro 1))
			; Print("Well that wasn't what you expected!")
			; Print("The dart passed right through him and hit the switch, unleashing a torrent of water.")
			)
			(4
				(ghost ignoreControl: $0400)
				(gEgo ignoreControl: $0400)
				(water1 hide:)
				(water2 hide:)
				(= flow1 0)
			)
		)
	)
)

(instance stream2Script of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= flow2 1)
				(water3 show: setCycle: End stream2Script)
				(ghost observeControl: ctlGREEN)
				(gEgo observeControl: ctlGREEN)
				(gTheSoundFX number: 202 play:)
			)
			(2
				(water4 show: setCycle: End stream2Script)
			)
			(3 (= seconds 8))
			(4
				(ghost ignoreControl: ctlGREEN)
				(gEgo ignoreControl: ctlGREEN)
				(water3 hide:)
				(water4 hide:)
				(= flow2 0)
			)
		)
	)
)

(instance stream3Script of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= flow3 1)
				(ghost observeControl: ctlTEAL)
				(gEgo observeControl: ctlTEAL)
				(water5 show: setCycle: End stream3Script)
				(gTheSoundFX number: 202 play:)
			)
			(2
				(water6 show: setCycle: End stream3Script)
			)
			(3 (= seconds 8))
			(4
				(water5 hide:)
				(water6 hide:)
				(ghost ignoreControl: ctlTEAL)
				(gEgo ignoreControl: ctlTEAL)
				(= flow3 0)
			)
		)
	)
)

(instance stream4Script of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= flow4 1)
				(ghost observeControl: ctlPURPLE)
				(gEgo observeControl: ctlPURPLE)
				(water7 show: setCycle: End stream4Script)
				(gTheSoundFX number: 202 play:)
			)
			(2
				(water8 show: setCycle: End stream4Script)
			)
			(3 (= seconds 8))
			(4
				(water7 hide:)
				(water8 hide:)
				(ghost ignoreControl: ctlPURPLE)
				(gEgo ignoreControl: ctlPURPLE)
				(= flow4 0)
			)
		)
	)
)

(instance ghostScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			; floating ghost
			(1
				(= cycles 3)
				(if ghostAwake
					(ghost posn: (ghost x?) (+ (ghost y?) 1))
				)
			)
			(2
				(= cycles 3)
				(if ghostAwake
					(ghost posn: (ghost x?) (- (ghost y?) 1))
				)
			)
			(3 (ghostScript changeState: 1))
		)
	)
)

(instance ghost2Script of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1 (= cycles 2) 
				(if ghostAwake
					(ghost cel: 0)
				)
			)
			(2 (= cycles 2) 
				(if ghostAwake
					(ghost cel: 1)
				)
			)
			(3 (= cycles 2) 
				(if ghostAwake
					(ghost cel: 2)
				)
			)
			(4 (= cycles 2) 
				(if ghostAwake
					(ghost cel: 3)
				)
			)
			(5
				(ghost2Script changeState: 1)
			)
			; ghost "awakening
			(6	
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				
				(self cycles: 0)
				(ghost show: view: 428 loop: 5 cel: 0 setCycle: End self cycleSpeed: 3)
				(gTheMusic number: 59 loop: -1 play:)
				
			)
			(7	(= cycles 5)
				(PrintOther 59 1
				)
				(PrintOther 59 2
				)
			)
			(8
				(ghost view: 332)
				(= ghostAwake 1)
				(self changeState: 1)
				
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)	
			)
		)
	)
)

(instance dartScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(dart
					show:
					view: 57
					posn: (gEgo x?) (- (gEgo y?) 30)
					setMotion: MoveTo 218 19 dartScript
				)
				(= shot 1)
				(-- gApple)
				(gTheSoundFX number: 204 play:)
			)
			(2
				(dart hide:)
				(stream1Script changeState: 1)
				(= shot 0)
			)
			(3
				(dart
					show:
					view: 157
					posn: (gEgo x?) (- (gEgo y?) 30)
					setMotion: MoveTo 20 105 dartScript
				)
				(= shot 1)
				(-- gApple)
			)
			(4
				(dart hide:)
				(stream2Script changeState: 1)
				(= shot 0)
			)
			(5
				(dart
					show:
					view: 57
					posn: (gEgo x?) (- (gEgo y?) 30)
					setMotion: MoveTo 185 152 dartScript
				)
				(= shot 1)
				(-- gApple)
			)
			(6
				(dart hide:)
				(stream3Script changeState: 1)
				(= shot 0)
			)
			(7
				(dart
					show:
					view: 157
					posn: (gEgo x?) (- (gEgo y?) 30)
					setMotion: MoveTo 289 84 dartScript
				)
				(= shot 1)
				(-- gApple)
			)
			(8
				(dart hide:)
				(stream4Script changeState: 1)
				(= shot 0)
			)
		)
	)
)

(instance tripScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(alterEgo
					show:
					view: 410
					loop: 1
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					setCycle: End tripScript
					cycleSpeed: 1
					ignoreActors:
				)
			)
			(2
				(Print
					{Ouch! The watery flood is too slippery to run on.}
				)
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 140)
)
(procedure (PrintWhisper)
	(= gWndColor 9)
	(= gWndBack 0)
	(Print 58 gListened #width 280 #at -1 20 #title {A voice within your mind:})
	(= gWndColor 0) ; clBLACK
	(= gWndBack 15)
	(++ gListened)
)

(instance alterEgo of Act
	(properties
		y 180
		x 27
		view 410
		loop 1
	)
)

(instance dart of Act
	(properties
		y 135
		x 161
		view 57                       ; 157 for horizontal
	)
)

(instance ghost of Act
	(properties
		y 60
		x 171
		view 332
		loop 1
	)
)

(instance alterGhost of Act
	(properties
		y 1
		x 1
		view 428
		loop 4
	)
)
(instance skeleton of Prop
	(properties
		y 70
		x 171
		view 411
		loop 1
		cel 1
	)
)

(instance water1 of Prop
	(properties
		y 97
		x 227
		view 69
	)
)

(instance water2 of Prop
	(properties
		y 97
		x 135
		view 69
		loop 1
	)
)

(instance water3 of Prop
	(properties
		y 141
		x 59
		view 69
		loop 2
	)
)

(instance water4 of Prop
	(properties
		y 135
		x 110
		view 69
		loop 3
	)
)

(instance water5 of Prop
	(properties
		y 187
		x 186
		view 69
		loop 4
	)
)

(instance water6 of Prop
	(properties
		y 116
		x 107
		view 69
		loop 5
	)
)

(instance water7 of Prop
	(properties
		y 118
		x 246
		view 69
		loop 6
	)
)

(instance water8 of Prop
	(properties
		y 181
		x 214
		view 69
		loop 7
	)
)

(instance waterButton1 of Prop
	(properties
		y 31
		x 218
		view 331
		loop 2
	)
)

(instance waterButton2 of Prop
	(properties
		y 122
		x 20
		view 331
		loop 3
	)
)

(instance waterButton3 of Prop
	(properties
		y 98
		x 288
		view 331
		loop 3
	)
)

(instance waterButton4 of Prop
	(properties
		y 168
		x 187
		view 331
		loop 4
	)
)
