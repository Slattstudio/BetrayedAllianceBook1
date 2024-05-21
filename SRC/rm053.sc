;;; Sierra Script 1.0 - (do not remove this comment)
; + 5 SCORE // gInt + 3 //
(script# 53)
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
	rm053 0
)

(local

; FaceCave



	lines =  1
	soldiersVisible =  0
	walkOff =  0
	message =  0
)

(instance rm053 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 21
		west 51
	)
	
	(method (init)
		(super init:)
		(self
			setScript: RoomScript
			setRegions: 200
			setRegions: 204
		)
		(gEgo init:)
		(oldMan
			init:
			hide:
			ignoreActors:
			setScript: conversationScript
		)
		(fatMan init: hide: ignoreActors:)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
			(21
				(gEgo posn: 100 170 loop: 3)
			)
			(51 (gEgo posn: 20 130 loop: 0))
			(75
				(gEgo posn: 248 160 loop: 2 hide:)
				(emergeScript changeState: 1)
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
				(if g53OutFirstTime
					(oldMan show:)
					(fatMan show:)
					(conversationScript changeState: 1)
					(= soldiersVisible 1)
					(gGame changeScore: 5)
					(= g53OutFirstTime 0)
				)
			)
		)
		(SetUpEgo)
		(RunningCheck)
		(alterEgo
			init:
			hide:
			posn: 248 160
			ignoreActors:
			setScript: emergeScript
		)
		(nothing init: hide: ignoreActors: setScript: climbScript)
		(chain init: setPri: 6)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			(1
				(= cycles 5)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(= gWndColor 14) ; clYELLOW
				(= gWndBack 8)  ; clGREY
				(Print
					{We haven't heard anything for an hour. He's a goner. Let's get out of here.}
					#title
					{Soldier says:}
					#at
					30
					80
					#width
					200
				)
				(= gWndColor 0) ; clBLACK
				(= gWndBack 15)
			)                   ; clWHITE
			(2
				(oldMan
					view: 346
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo 1 (oldMan y?) RoomScript
				)
				(fatMan
					view: 347
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo 1 (fatMan y?)
				)
			)
			(3
				(= cycles 10)
				(oldMan hide:)
			)
			(4
				(fatMan hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= soldiersVisible 0)
			)

			(5      ; pick flowers
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 39 137 self
					ignoreControl: ctlWHITE
				)
			)
			(6
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					loop: 1
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(7
				(= cycles 6)
				(if (or gFlowers [gFlowerGiven 0])
					(PrintOther 53 34)
					(PrintOther 53 31)
				else
					(Print 53 37 #icon 272 #title {Heliopsis Splendor})
					(gEgo get: INV_FLOWER)
					(= gFlowers 8)
					((gInv at: 21) count: gFlowers)
				)
			)
			(8
				(alterEgo setCycle: Beg self)
			)
			(9
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(alterEgo hide:)
				(gEgo show: observeControl: ctlWHITE)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if
			(or
				(== (pEvent message?) KEY_RETURN)
				(if (== (pEvent message?) 3))
			)                                                                                  ; pressed right arrow
			(if message
				(if gPrintDlg
					(gPrintDlg dispose:)
					(= message 0)
					; (buttonInstructions hide:)
					(conversationScript cycles: 0 cue:)
				)
			)
		)
		; dialogTrack()
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (not message)
					(if (checkEvent pEvent 225 282 30 88)     ; skull
						(PrintOther 53 22)
						(if g53OutFirstTime
							(PrintOther 53 23)
						else
							(PrintOther 53 24)
						)
					)
					(if (checkEvent pEvent 228 272 90 116)     ; well
						(PrintOther 53 25)
					)
					(if
						(==
							ctlNAVY
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                  ; flowers
						(PrintOther 53 26)
					)
					(if
						(==
							ctlCYAN
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                  ; flowers
						(PrintOther 53 47)
						(return)
					)
					(if
						(==
							ctlGREEN
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                   ; tree for hiding
						(PrintOther 53 27)
					)
					(if
						(==
							ctlTEAL
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                  ; dead tree
						(PrintOther 53 28)
					)
				)
			)
		)         
		
		(if (or (Said 'use/well')
				(Said 'raise,lift/bucket')
				(Said 'use,turn/pulley,handle'))
			(if (& (gEgo onControl:) ctlMAROON)
				(climbScript changeState: 7)	
			else
				(PrintNCE)
			)
		)
		(if (Said 'smell[/!*]')
			(PrintOther 53 44)
			(PrintOther 53 29)	
		)
		(if (Said 'smell/flower') (PrintOther 53 29))
		(if (or (Said 'make/wish') (Said 'throw,toss,drop/coin'))
			(PrintOther 53 45)
		)
		(if (Said 'look<in/well')
			(if (& (gEgo onControl:) ctlMAROON)
				(PrintOther 60 23)
			else
				(PrintNCE)
			)
		)
		(if (Said 'look>')
			(if (Said '/bush,azalea')
				(PrintOther 53 47)	
			)
			(if (Said '/face,cave,skull')
				(PrintOther 53 22)
				(if g53OutFirstTime
					(PrintOther 53 23)
				else
					(PrintOther 53 24)
				)
			)
			(if (Said '/mouth,well,pit')
				(PrintOther 53 25)
				(PrintOther 53 41)
			)
			(if (Said '/chain,rope') (PrintOther 53 42))
			(if (Said '/(tree<dead)') (PrintOther 53 28))
; (if (Said('/grass'))
;                Print("There is a patch of grass which is discolored. It appears softer than the rest of the ground." #width 280 #at -1 8)
;            )
			(if (Said '/flower,ground')
				(PrintOther 53 26)
			)
			(if (Said '[/!*]') (PrintOther 53 30))
		; this will handle just "look" by itself
		)
		(if (Said 'take,pick/flower')
			(if (gEgo inRect: 1 75 136 188)
				(self changeState: 5)
			else
				(PrintNCE)
			)
		)
		(if (Said 'take,pick/azalea')
			(PrintOther 53 48)	
		)
		; PrintOther(53 31)
		(if (Said 'talk/face,cave,well,skull')
			(PrintOther 53 32)
		)
		(if (Said 'climb/tree') (PrintOther 53 33))
		(if
			(or
				(Said 'listen/[cave,mouth,hole,well]')
				(Said 'listen/[/!*]')
			)
			(PrintOther 53 38)
			(if (not g73Wash) (PrintOther 53 40)) ; if you haven't been in the whispering caverns yet
		)
		(if (Said 'listen/*') (PrintOther 53 39))
		(if
			(or
				(Said '(go,jump<in)/cave,mouth,hole,well,skull')
				(if (Said 'climb,enter,(crawl<in)/well,hole,mouth,cave,chain,skull')
				)
				(if (Said 'take/chain')
				)
				(if (Said '(climb<down)[/!*]')
				)
			)
			; (if(not(send gEgo:has(INV_BLOW_DART_GUN)))
			(if (& (gEgo onControl:) ctlMAROON)
				(PrintOK)
				(climbScript changeState: 1)
			else
				(PrintNCE)
			)
		)
	)
	
; )(else
;                Print("No way am I going back in there." #title "You think:")
;            )
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlSILVER)
			(if soldiersVisible
				(= gWndColor 14) ; clYELLOW
				(= gWndBack 8)  ; clGREY
				(Print
					{We haven't heard anything for an hour. He's a goner. Let's get out of here.}
					#title
					{Soldier says:}
					#at
					30
					80
					#width
					200
				)
				(= gWndColor 0) ; clBLACK
				(= gWndBack 15) ; clWHITE
				(gRoom newRoom: 21)
			else
				(gRoom newRoom: 21)
			)
		)
		(if (& (gEgo onControl:) ctlBLUE)
			(if soldiersVisible
				(if (not walkOff)
					(RoomScript changeState: 1)
					(emergeScript changeState: 4)
					(= walkOff 1)
				)
			else
				(gRoom newRoom: 51)
			)
		)
		(if (& (gEgo onControl:) ctlGREY)
			(if soldiersVisible
				(emergeScript changeState: 4)
				(= soldiersVisible 0)
			)
		)
		(if soldiersVisible (if (< lines 22) (ProgramControl)))
	)
)

(instance emergeScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(alterEgo
					show:
					posn: 99 44
					setCycle: End emergeScript
					cycleSpeed: 2
				)
			)
			(2
				(alterEgo
					view: 409
					loop: 1
					cel: 0
					setCycle: End emergeScript
				)
			)
			(3
				(alterEgo hide:)
				(gEgo show: posn: 99 44)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(4
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 54 43 self
					ignoreControl: ctlWHITE
				)
			)
			(5
				(= cycles 5)
				(gEgo loop: 3 observeControl: ctlWHITE)
			)
			; = gInt (+ gInt 3)
			(6
				(if (not walkOff)
					(PrintOther 53 35)
					(PlayerControl)
					(SetCursor 999 (HaveMouse))
					(= gCurrentCursor 999)
				)
				(= soldiersVisible 1)
			)
		)
	)
)

(instance conversationScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1 (= cycles 20))
			(2
				(fatMan loop: 1 cel: 0 setCycle: End cycleSpeed: 1)
				(cond 
					((< lines 21)
						(= gWndColor 14) ; clYELLOW
						(= gWndBack 8) ; clGREY
						(Print
							53
							lines
							#title
							{Young soldier:}
							#at
							110
							30
							#width
							200
							#dispose
						)
						(= gWndColor 0) ; clBLACK
						(= gWndBack 15) ; clWHITE
						(++ lines)
						(= message 1)
						(ProgramControl)
					)
					((== lines 21) 
						(PlayerControl)
						(++ lines)
						(= [gLetters 3] 1)
						(self changeState: 5))
				)
			)
			(3
				(oldMan loop: 1 cel: 0 setCycle: End cycleSpeed: 1)
				(cond 
					((< lines 21)
						(= gWndColor 7) ; clYELLOW
						(= gWndBack 1) ; clGREY
						(Print
							53
							lines
							#title
							{Old soldier:}
							#at
							30
							30
							#width
							200
							#dispose
						)
						(= gWndColor 0) ; clBLACK
						(= gWndBack 15) ; clWHITE
						(++ lines)
						(= message 1)
						(ProgramControl)
					)
					((== lines 21) (PlayerControl) (++ lines) (self changeState: 5))
				)
			)
			(4
				(conversationScript changeState: 2)
			)
			(5
				(fatMan loop: 1 cel: 0 setCycle: End self cycleSpeed: 1)
			)
			(6
				(oldMan loop: 1 cel: 0 setCycle: End self cycleSpeed: 1)
			)
			(7 (self changeState: 5))
		)
	)
)

(instance climbScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			; Moving to climb well
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 250 140 climbScript
					ignoreControl: ctlWHITE
				)
			)
			(2          ; climbing
				(gEgo hide:)
				(alterEgo
					view: 245
					show:
					posn: 255 140
					setCycle: Walk
					setMotion: MoveTo 255 130 climbScript
					ignoreControl: ctlWHITE
					setPri: 7
					cycleSpeed: 3
				)
			)
			(3          ; climbing down the chain
				(alterEgo
					view: 129
					posn: (chain x?) (alterEgo y?)
					yStep: 2
					setMotion: MoveTo (chain x?) 170 climbScript
					setPri: 6
					cycleSpeed: 2
				)
			)
			(4
				(= cycles 5)
				; ShakeScreen(3)
				(alterEgo hide:)
			)
			(5
				(= cycles 5)
				(if (not (gEgo has: INV_BLOW_DART_GUN))
					(PrintOther 53 36)
				else                  ; down the pit
					(PrintOther 53 43)
				)
			)                         ; again?!	
			(6
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gRoom newRoom: 58)
			)
			; Moving to use well/turn crank
			(7
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 250 130 self
					ignoreControl: ctlWHITE
				)
			)
			(8	(= cycles 2) ; face up
				(gEgo loop: 3)	
			)
			(9
				(PrintOther 53 46)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)	
			)
		)
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

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 100)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
	)
)

; (procedure (dialogTrack)
; 	(= messagePrint (RoomScript:state()))
; )
(instance alterEgo of Act
	(properties
		view 414
		x 142
		y 155
	)
)

(instance chain of Prop
	(properties
		view 156
		x 249
		y 128
	)
)

(instance nothing of Act
	(properties          ; Using this view to set climbScript...might remove it when I have more Props in the script
		view 414
		x 142
		y 155
	)
)

(instance oldMan of Act
	(properties
		y 136
		x 225
		view 336
	)
)

(instance fatMan of Act
	(properties
		y 138
		x 260
		view 337
	)
)
