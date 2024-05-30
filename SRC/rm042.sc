;;; Sierra Script 1.0 - (do not remove this comment)
; + 3 SCORE //
(script# 42)
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
	rm042 0
)

(local

; Inside the Graveyard



	portalVis =  0
)

(instance rm042 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 40
		west 0
	)
	
	(method (init)
		(super init:)
		(self
			setScript: RoomScript
			setRegions: 200
			setRegions: 204
		)
		(SetUpEgo)
		(gEgo init:)
		(= gEgoRunning 0)
		(RunningCheck)
		(alterEgo
			init:
			hide:
			ignoreActors:
			setScript: shovelScript
		)
		(if (not gGrave)
			(portal
				init:
				ignoreActors:
				setPri: 1
				setScript: teleScript
			)
		else
			(portal
				init:
				ignoreActors:
				setPri: 1
				loop: 1
				setScript: teleScript
			)
			(= portalVis 1)
		)
		(switch gPreviousRoomNumber
			(40
				(if (not gTeleporting)
					(gEgo posn: 150 168 loop: 3)
				else
					(gEgo hide:)
					(teleScript changeState: 1)
				)
			)
			(41
				(if (not gTeleporting)
					(gEgo posn: 150 130 loop: 2)
				else
					(gEgo show: posn: 68 164 loop: 2 hide:)
					(teleScript changeState: 1)
				)
			)
			(else 
				(gEgo hide: posn: 150 130)
				(teleScript changeState: 1)
			)
		)
		(if (not g102Solved) (lid init:)) ; lid closed
		(if g41Coffin (slab init:))
		;(gTheMusic fade:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 1))
			(1)
		)
	)
	
	; (send gTheMusic:prevSignal(0)stop()number(42)loop(0)play())
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) 59)        ; grave w/ symbol
						(< (pEvent x?) 76)
						(> (pEvent y?) 127)
						(< (pEvent y?) 147)
					)
					(PrintOther 42 1)
				)
				(if
					(and
						(> (pEvent x?) 221)        ; grave w/ cross
						(< (pEvent x?) 236)
						(> (pEvent y?) 115)
						(< (pEvent y?) 147)
					)
					(PrintGrave 42 2)
				)
				(if
					(and
						(> (pEvent x?) 266)        ; grave furthest right on screen
						(< (pEvent x?) 280)
						(> (pEvent y?) 121)
						(< (pEvent y?) 146)
					)
					(PrintGrave 42 3)
				)
				(if
					(and
						(> (pEvent x?) 246)        ; grave hole
						(< (pEvent x?) 264)
						(> (pEvent y?) 114)
						(< (pEvent y?) 130)
					)
					(if (& (gEgo onControl:) ctlMAROON)
						(if (gEgo has: INV_SHOVEL)
							(PrintOther 42 4)
						else
							(PrintOther 42 5)
							(PrintOther 42 6)
						)
					else
						(PrintOther 42 11)
					)
				)
				(if
					(and
						(> (pEvent x?) 246)        ; unmarked grave w/hole
						(< (pEvent x?) 256)
						(> (pEvent y?) 103)
						(< (pEvent y?) 111)
					)
					(PrintOther 42 8)
				)
				(if
					(and
						(> (pEvent x?) 138)        ; mausoleum
						(< (pEvent x?) 179)
						(> (pEvent y?) 31)
						(< (pEvent y?) 98)
					)
					(PrintOther 42 9)
				)
				
				; six background gravestones
				(if
					(and
						(> (pEvent x?) 39)        
						(< (pEvent x?) 51)
						(> (pEvent y?) 104)
						(< (pEvent y?) 117)
					)
					(PrintGrave 42 21)
				)
				(if
					(and
						(> (pEvent x?) 66)       
						(< (pEvent x?) 75)
						(> (pEvent y?) 106)
						(< (pEvent y?) 117)
					)
					(PrintGrave 42 22)
				)
				(if
					(and
						(> (pEvent x?) 87)        ; 
						(< (pEvent x?) 97)
						(> (pEvent y?) 103)
						(< (pEvent y?) 117)
					)
					(PrintGrave 42 23)
				)
				(if
					(and
						(> (pEvent x?) 184)        ; 
						(< (pEvent x?) 193)
						(> (pEvent y?) 105)
						(< (pEvent y?) 115)
					)
					(PrintGrave 42 24)
				)
				(if
					(and
						(> (pEvent x?) 201)        ; 
						(< (pEvent x?) 209)
						(> (pEvent y?) 102)
						(< (pEvent y?) 113)
					)
					(PrintGrave 42 25)
				)
				(if
					(and
						(> (pEvent x?) 216)        ; 
						(< (pEvent x?) 223)
						(> (pEvent y?) 100)
						(< (pEvent y?) 112)
					)
					(PrintGrave 42 26)
				)
			)
		)
		(if
			(or (Said '((look<in),examine,search)/(hole,opening,grave[<open])')
				(Said 'look/grave<open'))
			(if (& (gEgo onControl:) ctlMAROON)
				(if (gEgo has: INV_SHOVEL)
					(PrintOther 42 4)
				else
					(PrintOther 42 5)
					(PrintOther 42 6)
				)
			else
				(PrintOther 42 11)
			)
		)
		(if (Said 'enter,(jump<in),(climb<in)/grave,hole')
			(if (& (gEgo onControl:) ctlMAROON)
				(PrintOther 42 10)
			else
				(PrintNCE)
			)
		)
		(if (or (Said 'listen[/!*]')
				(Said 'listen/ground')
				(Said 'put/ear/ground'))
			(PrintOther 42 18)	
			(Print 42 19)
		)
		(if (Said 'read/gravestone,epitaph,grave')
			(if (& (gEgo onControl:) ctlNAVY)
				(PrintOther 42 1)
			else
				(if (& (gEgo onControl:) ctlCYAN)
					(if (> (gEgo x?) 249)
						(PrintGrave 42 3)	
					else
						(PrintGrave 42 2)
					)				
				else
					(if (& (gEgo onControl:) ctlGREEN)
						(PrintGrave 42 21)	
					else
						(if (& (gEgo onControl:) ctlTEAL)
							(PrintGrave 42 22)	
						else
							(if (& (gEgo onControl:) ctlPURPLE)
								(PrintGrave 42 23)
							else
								(if (& (gEgo onControl:) ctlBLUE)
									(PrintGrave 42 24)
								else
									(if (& (gEgo onControl:) ctlLIME)
										(PrintGrave 42 25)
									else
										(if (& (gEgo onControl:) ctlRED)
											(PrintGrave 42 26)
										else
											(if (& (gEgo onControl:) ctlMAROON)
												(PrintOther 42 8)
											else
												(PrintNCE)
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
		(if (Said 'look>')
			(if (Said '/mausoleum') (PrintOther 42 9))
			(if (Said '/grave,epitaph,gravestone')
				(PrintOther 42 12)
			)
			(if (Said '/hole,opening')
				(if (& (gEgo onControl:) ctlMAROON)
					(if (gEgo has: INV_SHOVEL)
						(PrintOther 42 4)
					else
						(PrintOther 42 5)
						(PrintOther 42 6)
					)
				else
					(PrintOther 42 11)
				)
			)
			(if (Said '[/!*]') (PrintOther 42 0))
			; this will handle just "look" by itself
		)
		(if (Said 'touch/symbol,grave')
			(if (& (gEgo onControl:) ctlNAVY)
				(PrintOther 42 13)
			else
				(PrintNCE)
			)
		)
		(if (Said '(pick<up),take/shovel')
			(if (not (gEgo has: INV_SHOVEL))
				(if (& (gEgo onControl:) ctlMAROON)
					(shovelScript changeState: 1)
					(gEgo get: INV_SHOVEL)
					(gGame changeScore: 1)
				else
					(PrintNCE)
				)
			else
				(PrintOther 42 14)
			)
		)
		(if (or (Said 'use/shovel') (Said 'dig'))
			(if (gEgo has: INV_SHOVEL)
				(cond 
					((& (gEgo onControl:) ctlNAVY)
						(if (not portalVis)
							(shovelScript changeState: 7)
						else
							(Print 42 15)
						)
					)
					((& (gEgo onControl:) ctlCYAN) (PrintOther 42 16))
					(else (Print 42 15))
				)
			else
				(PrintOther 42 17)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 41)
		)
		(if (& (gEgo onControl:) ctlGREY) (gRoom newRoom: 40))
		(if (<= (gEgo distanceTo: portal) 20)
			(if portalVis (portal loop: 1))
		)
	)
)

; (send gGame:changeScore(1))
(instance shovelScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 240 125 shovelScript
					ignoreControl: ctlWHITE
				)
			)
			(2
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					posn: 240 125
					setCycle: End shovelScript
					cycleSpeed: 2
				)
			)
			(3
				(= cycles 8)
				(Print
					{You take the shovel from the open grave. Finder's keepers, sorry gravekeepers.}
					#icon
					208
					#title
					{Shovel}
				)
			)
			(4
				(alterEgo setCycle: Beg shovelScript)
			)
			(5
				(gEgo show: observeControl: ctlWHITE)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(7
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 79 157 shovelScript
					ignoreControl: ctlWHITE
				)
			)
			(8
				(= cycles 25)
				(gEgo hide:)
				(alterEgo
					show:
					view: 419
					loop: 1
					posn: 79 157
					setCycle: Fwd
					cycleSpeed: 2
				)
			)
			(9
				(alterEgo hide:)
				(gEgo show: loop: 1 observeControl: ctlWHITE)
				(portal setCycle: End shovelScript)  ; activate portal
				(if (not gGrave) (= gGrave 1) (gGame changeScore: 2))
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(10
				(Print
					{You have unearthed a portal. It is now added to your map!}
				)
				(= portalVis 1)
			)
		)
	)
)

(instance teleScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
				
				(alterEgo
					show:
					setCycle: Beg teleScript
					cycleSpeed: 2
					ignoreActors:
				)
				(gTheSoundFX number: 205 play:)
				(= gTeleporting 0)
			)
			(2
				(gEgo show: posn: 68 164 loop: 2)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
)

(procedure (PrintGrave textRes textResIndex)
	(= gWndColor 0)   ; clYELLOW	
	(= gWndBack 7)   ; clDARKBLUE
	
	(if (< (gEgo x?) 160)	; ego on left
		(Print textRes textResIndex #title {It reads:} #font 4 #width 100 #at 160 -1)	
	else
		(Print textRes textResIndex #title {It reads:} #font 4 #width 100 #at 40 -1)
	)
	(= gWndColor 0)   ; clYELLOW
	(= gWndBack 15)
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
		y 164
		x 68
		view 128
		cel 10
	)
)

(instance portal of Prop
	(properties
		y 164
		x 68
		view 16
	)
)

(instance lid of Prop
	(properties
		y 65
		x 151
		view 101
		loop 4
	)
)

(instance slab of Prop
	(properties
		y 62
		x 147
		view 101
		loop 5
	)
)
