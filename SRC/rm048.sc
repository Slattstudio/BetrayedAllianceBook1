;;; Sierra Script 1.0 - (do not remove this comment)

(script# 48)
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
	rm048 0
)

(local
; Dock



	earShot =  0
	manWalking =  1
	falling =  0
)

(instance rm048 of Rm
	(properties
		picture scriptNumber
		north 0
		east 49
		south 0
		west 43
	)
	
	(method (init)
		(super init:)
		(self
			setScript: RoomScript
			setRegions: 200
			setRegions: 204
		)
		(switch gPreviousRoomNumber
			(else )
			; (send gEgo:posn(150 130)loop(1))
			(49
				(if (> (gEgo y?) 142)   ; on bridge
					(gEgo posn: 300 155 loop: 1)
				else
					(gEgo posn: 300 75 loop: 1)
				)
			)
			(43 (gEgo posn: 20 65))
			(45 (gEgo posn: 100 65 loop: 2))
		)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		; (waves1:init()setCycle(Fwd)cycleSpeed(3)setPri(0)ignoreActors())
		; (waves2:init()setCycle(Fwd)cycleSpeed(3)setPri(0)ignoreActors())
		(actionEgo init: hide: ignoreControl: ctlWHITE setScript: proposeScript)
		(man init: setCycle: Walk ignoreControl: ctlWHITE)
		(ripple1
			init:
			setCycle: Fwd
			cycleSpeed: 4
			setPri: 11
			ignoreActors:
		)
		(ripple2
			init:
			setCycle: Fwd
			cycleSpeed: 4
			setPri: 11
			ignoreActors:
		)
		(ripple3
			init:
			setCycle: Fwd
			cycleSpeed: 4
			setPri: 11
			ignoreActors:
		)
		(deathSplash
			init:
			hide:
			ignoreActors:
			setPri: 1
			setScript: fallScript
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0
				(if gFrst   ; if Lex is disguised
					(= manWalking 0)
					(man view: 155 posn: 225 (man y?) cel: 0)
				else
					(man setMotion: MoveTo 225 (man y?) RoomScript)
				)
			)
			(1
				(man loop: 4 cel: 0)
				(= manWalking 0)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(cond 
					(
						(checkEvent
							pEvent
							(man nsLeft?)
							(man nsRight?)
							(man nsTop?)
							(man nsBottom?)
						)
						(PrintOther 48 14)
					)
					(
						(==
							ctlGREEN
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                   ; dock
						(PrintOther 48 23)
					)
				)
				(if (checkEvent pEvent 32 104 0 45)    ; library
					(PrintOther 48 21)
					(PrintOther 48 22)
				)
				(if
					(==
						ctlNAVY
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                   ; mountains
					(PrintOther 48 24)
				)
			)
		)
		(if
			(or
				(Said 'give/ring[/man,lex]')
				(Said 'give/man,lex/ring')
				(Said 'propose[/marriage,man,lex]')
			)
			(if (gEgo has: 18)
				; kneeling animation
				(if earShot
					(proposeScript changeState: 1)
				else
					(PrintNCE)
				)	
			else
				(PrintDHI)
			)
		)
		(if (Said 'wear,use,(put<on)/goggles')
			(if (gEgo has: INV_GOGGLES)
				(if gDisguised
					(Print 0 53)
				else
					(PrintOther 0 80)
					(= gDisguised 1)
					(= gEgoRunning 0)
					(RunningCheck)
					(gEgo setMotion: NULL)
				)
			else
				(PrintDHI)
			)
		)
		(if (Said '(ask<about)>')
			(cond 
				(earShot
					; (if(<=(send gEgo:distanceTo(man))50)
					(if (Said '/boat,ship') (PrintMan 48 4))
					
					(if (Said '/plot,discussion') ; gossip
						(PrintMan 48 5)
						(PrintMan 48 6)
					)
					(if (Said '/shelah,kingdom') (PrintMan 48 34))
					(if (Said '/ishvi,war') (PrintMan 48 33))
					(if (Said '/king') (PrintMan 48 12))
					(if (Said '/princess,julyn') (PrintMan 48 11))
					(if (Said '/tristan')
						(PrintMan 48 30)
						(PrintMan 48 31)
					)
					(if (Said '/cave') (PrintMan 48 15))
					; peoples of shelah
					(if (Said '/bobby') (PrintMan 0 91))
					(if (Said '/leah') (PrintMan 48 16))
					(if (Said '/sammy') (PrintMan 0 91))
					(if (Said '/deborah, woman') (PrintMan 0 92))
					(if (Said '/rose') (PrintMan 0 95))
					(if (Said '/sarah') (PrintMan 48 17))
					(if (Said '/hans') (PrintMan 0 91))
					(if (Said '/man,lex')
						(PrintMan 48 18)
						(PrintMan 48 2)
						(PrintMan 48 3)
					)
					(if (Said '/castle,job') 
						(PrintMan 48 2)
						(PrintMan 48 3)
					)
					(if (Said '/name') (PrintMan 48 18))
					(if (Said '/colin') (PrintMan 0 91))
					(if (Said '/longeau') (PrintMan 48 19))
					(if (Said '/moon,carmyle')
						(PrintMan 48 9)
						(PrintMan 48 10)
					)
					(if (Said '/danger,gyre')
						(PrintMan 48 13)
						(PrintMan 48 7)
						(PrintMan 48 8)
					)
					(if (Said '/disguise,goggles') ; gossip
						(if gFrst   ; if Lex is disguised
							(PrintMan 48 37)	
						else
							(PrintMan 48 20)
						)
					)
					
					(if (Said '/*') (PrintMan 48 20))
				)
				((Said '/*') (PrintNCE))
			)
		)
		(if (Said 'push,kick,kill/man,lex')
			(PrintOther 48 35)
			(PrintOther 48 36)		
		)
		(if (Said 'talk/man,lex')
			(if earShot
				(if gDisguised
					(if gFrst
						(PrintMan 48 38)		
					else
						(PrintMan 48 28)
						(= gFrst 1)
					)
				else
					(PrintMan 48 0)
					(PrintMan 48 1)
				)
			else
				(PrintNCE)
			)
		)
		(if (Said 'look>')
			(if (Said '/building,library')
				(PrintOther 48 21)
				(PrintOther 48 22)
			)
			(if (Said '/dock') (PrintOther 48 23))
			(if (Said '/water,river,sea') (PrintOther 48 25))
			(if (Said '/man')
				(if gFrst (PrintOther 48 29) else (PrintOther 48 14)) ; lex is disguised
			)
			(if (Said '[/!*]') (PrintOther 48 26))
			; this will handle just "look" by itself
		)
		(if (Said 'dive,swim,(get<in),(jump<in)/pond,water')
			(PrintOther 24 27)
		)
		(if (Said 'remove,(take<off)/armor') (PrintOther 24 32))
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 45)
		)
		(if (& (gEgo onControl:) ctlRED)        ; Fall into water
			(if (not falling)
				(fallScript changeState: 7)
				(= falling 1)
			)
		)
		(if (& (gEgo onControl:) ctlSILVER)         ; fall from bottom of pier
			(if (not falling)
				(fallScript changeState: 1)
				(= falling 1)
			)
		)
		(if (& (gEgo onControl:) ctlGREY)       ; fall from  back
			(if (not falling)
				(fallScript changeState: 4)
				(= falling 1)
			)
		)
		(if (<= (gEgo distanceTo: man) 45)
			(= earShot 1)
			(if (not manWalking) (man cel: 1))
		; (if(>(send gEgo:x)(man:x))
		else
			; )
			(= earShot 0)
			(if (not manWalking) (man cel: 0))
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
				(= cycles 2)    ; falling from bottom
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(actionEgo
					show:
					view: 23
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					yStep: 5
					setMotion: MoveTo (actionEgo x?) 174
					setPri: 15
				)
			)
			(2
				(actionEgo hide:)
				(deathSplash
					show:
					posn: (actionEgo x?) 200
					setCycle: End self
					setPri: 14
					cycleSpeed: 3
				)
				(gTheSoundFX number: 202 play:)
			)
			(3 (self changeState: 14))
			(4       ; falling from top
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(if gDisguised
					(actionEgo view: 154)
				else
					(actionEgo view: 88)
				)
				(actionEgo
					show:
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					yStep: 9
					setMotion: MoveTo (gEgo x?) 186 self
					setPri: 1
				)
			)
			(5
				(actionEgo hide:)
				(deathSplash
					show:
					posn: (actionEgo x?) (actionEgo y?)
					setCycle: End self
					setPri: 1
					cycleSpeed: 3
				)
				(gTheSoundFX number: 202 play:)
			)
			(6 (self changeState: 14))
			(7
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(actionEgo
					show:
					view: 23
					loop: 2
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					yStep: 9
					setMotion: MoveTo (gEgo x?) 140 self
					setPri: 1
				)
			)
			(8
				(actionEgo hide:)
				(deathSplash
					show:
					posn: (gEgo x?) 140
					setCycle: End self
					setPri: 1
					cycleSpeed: 3
				)
				(gTheSoundFX number: 202 play:)
			)
			(9 (self changeState: 14))
			(14
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 603
					register:
						{Going for a swim in heavy armor? Didn't you have a sinking feeling about doing that?}
				)
				(gGame setScript: dyingScript)
			)
		)
	)
)
(instance proposeScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				
				(actionEgo show: posn: (gEgo x?)(gEgo y?) view: 233 cel: 0 setCycle: End self cycleSpeed: 2)
				(if (> (gEgo x?) (man x?))
					(actionEgo loop: 1)	
				else
					(actionEgo loop: 0)
				)
			)
			(2 (= cycles 10)
				
			)
			(3
				(PrintMan 48 32)
				(self cue:)	
			)
			(4
				(actionEgo setCycle: Beg self)	
			)
			(5
				(gEgo show:)
				(actionEgo hide:)
				
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

(procedure (PrintMan textRes textResIndex)
	(= gWndColor 11) ; clYELLOW
	(= gWndBack 8) ; clDARKBLUE
	(Print
		textRes
		textResIndex
		#title
		{Man says:}
		#width
		280
		#at
		-1
		20
	)
	(= gWndColor 0) ; clBLACK
	(= gWndBack 15)
)
                  ; clWHITE
(instance actionEgo of Act
	(properties
		y 1
		x 1
		view 408
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

(instance man of Act
	(properties
		y 144
		x 260
		view 343
		loop 0
	)
)

(instance waves1 of Prop
	(properties
		y 138
		x 66
		view 65
		loop 2
	)
)

(instance waves2 of Prop
	(properties
		y 158
		x 194
		view 65
		loop 2
	)
)

(instance ripple1 of Prop
	(properties
		y 187
		x 147
		view 87
	)
)

(instance ripple2 of Prop
	(properties
		y 187
		x 235
		view 87
	)
)

(instance ripple3 of Prop
	(properties
		y 187
		x 310
		view 87
	)
)
