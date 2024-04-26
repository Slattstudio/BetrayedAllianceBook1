;;; Sierra Script 1.0 - (do not remove this comment)
; + 2 SCORE //
(script# 23)
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
	rm023 0
)

(local

; West Fence for N Castle



	; (use "sciaudio")
	talking =  0
	arrows =  0
	asked =  0
)
; snd

(instance rm023 of Rm
	(properties
		picture scriptNumber
		west 24
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 204)
; setRegions(200)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 10 134 loop: 0)
			)
		)
; = snd aud (send snd:
;                    command("playx")
;                    fileName("music\\leah.mp3")
;                    volume("70")
;                    loopCount("0")
;                    init()
;                )
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		
		
		(horse init: setScript: horseScript)
		(kissyFace
			init:
			hide:
			setScript: speechScript
			ignoreActors:
			ignoreControl: ctlWHITE
		)
		(grass init: setScript: dialogScript)
		(target init: setScript: archeryScript)
		(arrow1 init: hide: ignoreControl: ctlWHITE setScript: proposeScript)
		(arrow2 init: hide: ignoreControl: ctlWHITE)
		(arrow3 init: hide: ignoreControl: ctlWHITE)
		(alterEgo init: hide: ignoreActors:)
		
		(if (not g23Kissed)
			(leah
				init:
				setScript: leahScript
				setPri: 15
				ignoreControl: ctlWHITE
			)
		else
			(leah
				init:
				posn: 140 157
				setScript: leahScript
				setPri: 15
				loop: 0
				ignoreControl: ctlWHITE
			)
		)
		(if [gFlowerGiven 0] (flower init:))
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			(1          ; change leah's view when she walks to fence for the kiss
				(leah posn: 80 134 view: 406 loop: 0 cel: 0)
			)
		)
	)
	
	(method (handleEvent pEvent &tmp dyingScript button)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(checkEvent
						pEvent
						(leah nsLeft?)
						(leah nsRight?)
						(leah nsTop?)
						(leah nsBottom?)
					)
					(PrintOther 23 0)
				)
				; Print(23 0 #width 290 #at -1 12)
				(if
					(checkEvent
						pEvent
						(horse nsLeft?)
						(horse nsRight?)
						(horse nsTop?)
						(horse nsBottom?)
					)
					(PrintOther 23 1)
				)
				; Print(23 1 #at -1 20)
				(if
					(checkEvent
						pEvent
						(target nsLeft?)
						(target nsRight?)
						(target nsTop?)
						(target nsBottom?)
					)
					(PrintOther 23 25)
					(PrintOther 23 26)
				)
			)
		)
		; Print(23 25 #width 290 #at -1 12)
		; Print(23 26 #width 290 #at -1 12)
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
		(if (Said 'kiss/woman,leah,her') (PrintOther 23 31))
		; Print(23 31 #width 290  #at -1 12)
		(if (Said 'climb,jump/fence') (PrintOther 23 45))
		(if (Said 'take,ride/horse') (PrintOther 23 87))
		(if
			(or
				(Said 'give/ring[/woman,leah]')
				(Said 'give/woman,leah/ring')
				(Said 'propose[/marriage,leah]')
			)
			(if (gEgo has: 18)
				(proposeScript changeState: 1)
			else
				(PrintDHI)
			)
		)
		(if
			(or
				(Said 'swear,make/oath,love,leah')
				(Said 'elope')
				(Said '(leave<with),(runaway<with),join/woman,leah')
			)
			(= button
				(Print
					{Swear an oath of love and join with Leah?}
					#title
					{Are you sure?}
					#button
					{ Yes_}
					1
					#button
					{ No_}
					0
				)
			)
			(if (== button 1)
				(proposeScript changeState: 4)
			else
				(Print 23 76)
			)
		)
		(if (Said 'smell[/!*]')
			(if (<= (gEgo distanceTo: leah) 60)
				(PrintOther 23 90)	
			else
				(PrintOther 23 91)
			)
		)
		(if (Said 'look>')
			(if (Said '/flower')
				(if [gFlowerGiven 0]
					(PrintOther 0 98)
				else
					(PrintOther 0 97)
				)
			)
			(if (Said '/woman') (PrintOther 23 39))
			; Print(23 39 #width 280 #at -1 8)
			(if (Said '/horse') (PrintOther 23 40))
			; Print(23 40 #width 280 #at -1 8)
			(if (Said '/castle,tower') (PrintOther 23 41))
			; Print(23 41 #width 280 #at 80 30)
			(if (Said '/arrow') (PrintOther 23 42))
			; Print(23 42 #width 280 #at -1 8)
			(if (Said '/target,mount')
				(PrintOther 23 25)
				(PrintOther 23 26)
			)
			; Print(23 25 #width 280 #at -1 8)
			; Print(23 26 #width 280 #at -1 8)
			(if (Said '[/!*]') (PrintOther 23 43))
			; this will handle just "look" by itself
			; Print(23 43 #width 280 #at -1 8)
		)
		; Print(23 44 #width 280 #at -1 8)
		(cond 
			(talking
				(if (Said 'give/flower')
					(cond 
						([gFlowerGiven 0] (PrintOther 23 77))
						((gEgo has: 21)
							(PrintLeah 23 78)
							(= [gFlowerGiven 0] 1)
							(-- gFlowers)
							(if (== gFlowers 0) (gEgo put: 21))
							((gInv at: 21) count: gFlowers)
							(gGame changeScore: 1)
						)
						(else (PrintDHI))
					)
				)
				(if (Said '(ask<about)>')
					(if
					(or (Said '/carmyle,book') (Said '/family<carmyle'))
						(PrintLeah 23 24)
						; Print(23 24 #width 280 #at -1 12 #title "She says:")
						(if (== [gMissingBooks 3] 0)
							(PrintLeah 23 27)
							; Print(23 27 #width 280 #at -1 12 #title "She says:")
							(Print 23 28 #icon 984 1 0)
							(= [gMissingBooks 3] 1)
							(gGame changeScore: 1)
							(if (not (gEgo has: 20)) (gEgo get: 20))
						)
					)
					(if (Said '/hair') (PrintLeah 23 2) (PrintLeah 23 3))
					; Print(23 2 #width 290 #at -1 12 #title "She says:")
					; Print(23 3 #width 290 #at -1 12 #title "She says:")
					(if (Said '/woman, leah, name')
						(PrintLeah 23 4)
						(PrintLeah 23 35)
					)
					; Print(23 4 #width 290 #at -1 12 #title "She says:")
					(if (Said '/mother') (PrintLeah 23 65))
					; Print(23 5 #width 290 #at -1 12 #title "She says:")
					(if (Said '/father,weigel,family,speyr')
						(PrintLeah 23 5)
						(Print 23 62 #at -1 12)
					)
					; Print(23 5 #width 290 #at -1 12 #title "She says:")
					(if (Said '/lust,wanderlust,adventure')
						(PrintLeah 23 6)
					)
					(if (Said '/plan')
						(PrintLeah 23 93)
					)
					; Print(23 6 #width 290 #at -1 12 #title "She says:")
					(if (Said '/horse') (PrintLeah 23 7))
					; Print(23 7 #width 290 #at -1 12 #title "She says:")
					(if (Said '/land,wonder,travel') (PrintLeah 23 8))
					; Print(23 8 #width 290 #at -1 12 #title "She says:")
					(if (Said '/skull,desert') (PrintLeah 23 9))
					; Print(23 9 #width 290 #at -1 12 #title "She says:")
					(if (Said '/marauder,thief,battle,fighting,sword')
						(PrintLeah 23 10)
					)
					; Print(23 10 #width 290 #at -1 12 #title "She says:")
					(if (Said '/fire,blaze,passion') (PrintLeah 23 11))
					; Print(23 11 #width 290 #at -1 12 #title "She says:")
					(if (Said '/marriage,wedding,oath,date')
						(PrintLeah 23 12)
					)
					; Print(23 12 #width 290 #at -1 12 #title "She says:")
					(if (Said '/love,feeling,kiss') (PrintLeah 23 13))
					; Print(23 13 #width 290 #at -1 12 #title "She says:")
					(if (Said '/castle') (PrintLeah 23 14))
					; Print(23 14 #width 290 #at -1 12 #title "She says:")
					(if (Said '/library')
						(PrintLeah 23 57)
						(if (== [gMissingBooks 3] 0) (PrintLeah 23 61))
					)
					; Print(23 14 #width 290 #at -1 12 #title "She says:")
					(if (Said '/weather') (PrintLeah 23 15))
					; Print(23 15 #width 290 #at -1 12 #title "She says:")
					(if (Said '/princess')
						(PrintLeah 23 47)
						(PrintLeah 23 16)
					)
					; Print(23 16 #width 290 #at -1 12 #title "She says:")
					(if (Said '/tristan') (PrintLeah 23 88))
					(if (Said '/king,kingdom') (PrintLeah 23 17))
					; Print(23 17 #width 290 #at -1 12 #title "She says:")
					(if (Said '/diamond,necklace') (PrintLeah 23 18))
					; Print(23 18 #width 290 #at -1 12 #title "She says:")
					(if (Said '/journey,mountain') (PrintLeah 23 19))
					; Print(23 19 #width 290 #at -1 12 #title "She says:")
					(if (Said '/wizard')
						(PrintLeah 23 21)
						(PrintLeah 23 22)
					)
					; Print(23 21 #width 290 #at -1 12 #title "She says:")
					; Print(23 22 #width 290 #at -1 12 #title "She says:")
					(if (Said '/king') (PrintLeah 23 23))
					(if (Said '/cave') (PrintLeah 23 49))
					(if (Said '/(life<new)') (PrintLeah 23 79))
					(if (Said '/ogre') (PrintLeah 23 50) (PrintLeah 23 51))
					(if (Said '/graveyard,sarcophagus,grave')
						(PrintLeah 23 69)
						(PrintLeah 23 70)
					)
					(if (Said '/trick,puzzle')
						(PrintLeah 23 71)
						(PrintLeah 23 72)
					)
					(if (Said '/tunnel') (PrintLeah 23 72))
					(if (Said '/gyre') (PrintLeah 23 46))
					(if (Said '/target,archery,arrow,bow') (PrintLeah 23 66))
					(if (Said '/mustache')
						(if gDisguised (PrintLeah 23 75) else (PrintLeah 23 74))
					)
					(if (Said '/ishvi,shelah,kingdom') (PrintLeah 23 92))
					(if (Said '/fisher,bobby') (PrintLeah 23 20))
					(if (Said '/leah') (PrintLeah 23 20))
					(if (Said '/sammy') (PrintLeah 23 81))
					(if (Said '/deborah') (PrintLeah 23 82))
					(if (Said '/rose') (PrintLeah 23 82))
					(if (Said '/sarah') (PrintLeah 23 83))
					(if (Said '/hans') (PrintLeah 23 86))
					(if (Said '/lex') (PrintLeah 23 84))
					(if (Said '/colin') (PrintLeah 0 91))
					(if (Said '/longeau') (PrintLeah 23 29))
					(if (Said '/moon') (PrintLeah 23 85))
					(if (Said '/tor') (PrintLeah 23 89))
					(if (Said '/gyre') (PrintLeah 23 46))
					(if (Said '/*') (PrintLeah 23 30))
				)
				(dialogScript cycles: 0)
				(dialogScript changeState: 2)
			)
			((Said '*')
				(PrintLeah 23 36)
; Just a moment my love." #at -1 12 #title "She says:
				(= asked 1)
				(archeryScript changeState: 9)
			)
		)
		(if (Said 'talk/woman,her,leah')
			(if gDisguised (PrintLeah 23 73) else (PrintLeah 23 4)(PrintLeah 23 37))
		)
		; Print(23 37) /* Ask me anything you wish."#width 150 #at -1 12 #title "She says: */
		(if (Said 'talk/horse,mircea') (Print 23 48)) ; Stop horsing around.
		(if (Said 'use/map')
			(Print
				{You're afraid of frightening Leah by disappearing before her eyes.}
			)
		)
	)
)
                                                                                        ; Stop horsing around.
(instance speechScript of Script
	(properties)
	
	(method (changeState newState &tmp [buffer 500])
		(= state newState)
		(switch state
			(0
				(= cycles 7)
				(if (not g23Kissed)
					(ProgramControl)
					(= gEgoRunning 0)
					(RunningCheck)
				)
			)
			(1
				(= seconds 3)
				(if (not g23Kissed)
					(= gWndColor 14) ; clYELLOW
					(= gWndBack 1) ; clDARKBLUE
					(Format
						@buffer
						{%s! Come quickly!\n\nI have something very important for you.}
						@gName
					)
					(Print
						@buffer
						#dispose
						#at
						-1
						12
						#width
						240
						#mode
						alCENTER
						#title
						{Woman:}
					)
					(= gWndColor 0) ; clBLACK
					(= gWndBack 15) ; clWHITE
					(SetCursor 997 (HaveMouse))
					(= gCurrentCursor 997)
				)
			)
			(2
				(= seconds 3)
				(if (not g23Kissed)
					(gEgo setMotion: MoveTo 60 134 speechScript)
				)
			)
			(3
				(= cycles 5)
				(if (not g23Kissed) (gPrintDlg dispose:))
			)
			(4
				(if (not g23Kissed) (leahScript changeState: 2))
			)
		)
	)
)

(instance leahScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1))
			(1
				(if (not g23Kissed)
					(leah
						setCycle: Walk
						ignoreControl: ctlWHITE
						ignoreActors:
						setMotion: MoveTo 88 (gEgo y?) RoomScript
					)
				else
					(leah
						setCycle: Walk
						setMotion: MoveTo 170 177 archeryScript
					)
				)
			)
			(2
				(leah
					posn: 80 134
					view: 406
					loop: 0
					cel: 0
					setCycle: End leahScript
				)
				(kissyFace show: posn: 62 134 setCycle: End cycleSpeed: 2)
				(gEgo hide:)
				(gTheSoundFX number: 24 play:)
			)
			(3 (= seconds 3))
			(4
				(leah setCycle: Beg)
				(kissyFace
					loop: 1
					cel: 0
					setCycle: End leahScript
					cycleSpeed: 5
				)
			)
			; (princessScript:changeState(1))
			(5
				(= cycles 20)
				(kissyFace loop: 2 setCycle: Fwd cycleSpeed: 2)
			)
			(6
				(= cycles 1)
				(gEgo show:)
				(kissyFace hide:)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= g23Kissed 1)
			)
			(7
				(= cycles 12)
				(PrintLeah 23 32) ; #width 290 #at -1 12 #title "She says:")
				(PrintLeah 23 33)
			)                    ; #width 290 #at -1 12 #title "She says:")
			(8
				(= cycles 1)
				(PrintLeah 23 34) ; #width 150 #at -1 12 #title "She says:")
				(PlayerControl)
				(= gWndColor 0) ; clYELLOW
				(= gWndBack 14) ; clDARKBLUE
				(Print 23 80 #font 4 #title {Asking questions:} #at 140 -1 #width 120 #button {Ok} )
				(= gWndColor 0) ; clBLACK
				(= gWndBack 15)
			)                     ; clWHITE
			(9
				(= talking 1)
				(dialogScript changeState: 1)
			)
		)
	)
)

(instance horseScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1))
			(1
				(horse
					loop: 2
					cel: 0
					setCycle: End horseScript
					cycleSpeed: 2
				)
			)
			(2
				(= cycles (Random 15 100))
				(horse loop: 3 cel: 0 setCycle: Fwd cycleSpeed: 4)
				(grass setCycle: Fwd cycleSpeed: 4)
			)
			(3
				(= cycles (Random 30 100))
				(horse loop: 2 cel: 3 setCycle: Beg cycleSpeed: 2)
				(grass setCycle: CT)
			)
			(4 (horseScript changeState: 1))
		)
	)
)

(instance archeryScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles 14)
				(leah view: 406 loop: 1 cel: 0)
			)
			(2
				(leah setCycle: End archeryScript cycleSpeed: 2)
			)
			(3 (= cycles 8))
			(4
				(leah loop: 2)
				(if (< arrows 2)
					(if (< arrows 1)
						(arrow1
							init:
							show:
							posn: 177 157
							yStep: 13
							setMotion: MoveTo (Random 200 211) (Random 107 123) archeryScript
							ignoreActors:
							setPri: 15
						)
						(++ arrows)
					else
						(arrow2
							init:
							show:
							posn: 177 157
							yStep: 13
							setMotion: MoveTo (Random 200 211) (Random 107 123) archeryScript
							ignoreActors:
							setPri: 15
						)
						(++ arrows)
					)
				else
					(arrow3
						init:
						show:
						posn: 177 157
						yStep: 13
						setMotion: MoveTo (Random 200 211) (Random 107 123) archeryScript
						ignoreActors:
						setPri: 15
					)
					(++ arrows)
				)
			)
			(5 (= cycles 20))
			(6
				(if (== arrows 3)
					(archeryScript changeState: 9)
				else
					(archeryScript changeState: 1)
				)
			)
			(9
				(self cycles: 0)
				(leah
					view: 318
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo 92 135 archeryScript
				)
			)
			(10 (= cycles 2) (leah cel: 4))
			(11
				(= talking 1)
				(dialogScript changeState: 1)
			)
		)
	)
)

(instance dialogScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(if talking (dialogScript changeState: 2))
			)
			(2
				(= cycles 60)
				(if asked (PrintLeah 23 38) (= asked 0))
; What was that my dear?" #at -1 12 #title "She says:
			)
			(3
				(= talking 0)
				(if (< arrows 3)
					(leah
						view: 318
						setCycle: Walk
						setMotion: MoveTo 170 177 dialogScript
					)
				else
					(leah
						view: 318
						setCycle: Walk
						setMotion: MoveTo 190 137 dialogScript
						ignoreActors:
					)
				)
			)
			(4
				(if (< arrows 3)
					(archeryScript changeState: 1)
				else
					(dialogScript changeState: 5)
				)
			)
			(5 (= cycles 12))
			(6
				(= cycles 12)
				(arrow1 hide:)
				(arrow2 hide:)
				(arrow3 hide:)
				(= arrows 0)
			)
			(7
				(self cycles: 0)
				(leah
					view: 318
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo 92 135 dialogScript
				)
			)
			(8
				(leah cel: 4)
				(= talking 1)
				(dialogScript changeState: 1)
			)
		)
	)
)
(instance proposeScript of Script
	(properties)
	
	(method (changeState newState dyingScript)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				
				(alterEgo show: posn: (gEgo x?)(gEgo y?) view: 233 cel: 0 setCycle: End self cycleSpeed: 3)
				(if (> (gEgo x?) (leah x?))
					(alterEgo loop: 1)	
				else
					(alterEgo loop: 0)
				)
			)
			(2 (= cycles 10)
				
			)
			(3
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 602
					register:
						{With another woman's ring, you pledge your love and ride off into the wilderness with Leah (the woman you just met). Not very heroic of you! As for Julyn, no one seems to know what became of her.}
				)
				(gGame setScript: dyingScript)
			)
			; swearing an oath
			(4
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				;(gTheSoundFX number: 24 play:)
				
				(alterEgo show: posn: (gEgo x?)(gEgo y?) view: 233 loop: 2 cel: 0 setCycle: End self cycleSpeed: 3)
			)
			(5 (= cycles 10)
							
			)
			(6
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 602
					register:
						{You pledge your love and ride off into the wilderness with Leah (the woman you just met). Not very heroic of you! As for Julyn, no one seems to know what became of her.}
				)
				(gGame setScript: dyingScript)	
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

(procedure (PrintLeah textRes textResIndex)
	(= gWndColor 14) ; clYELLOW
	(= gWndBack 1) ; clDARKBLUE
	(Print
		textRes
		textResIndex
		#title
		{She says:}
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
(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 10)
)

(instance alterEgo of Prop
	(properties
		y 104
		x 150
		view 128
		cel 10
	)
)
(instance horse of Prop
	(properties
		y 125
		x 130
		view 50
		loop 2
	)
)

(instance target of Prop
	(properties
		y 137
		x 214
		view 50
		loop 5
	)
)

(instance grass of Prop
	(properties
		y 129
		x 117
		view 50
		loop 4
	)
)

(instance leah of Act
	(properties
		y 134
		x 200
		view 318
		loop 1
	)
)

(instance kissyFace of Act
	(properties
		y 132
		x 62
		view 407
	)
)

(instance arrow1 of Act
	(properties
		y 157
		x 175
		view 118
	)
)

(instance arrow2 of Act
	(properties
		y 157
		x 175
		view 118
	)
)

(instance arrow3 of Act
	(properties
		y 157
		x 175
		view 118
	)
)

(instance flower of Prop
	(properties
		y 107
		x 247
		view 97
		loop 2
	)
)
