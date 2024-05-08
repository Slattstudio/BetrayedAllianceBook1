;;; Sierra Script 1.0 - (do not remove this comment)
; + 3 SCORE //
(script# 65)
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
	rm065 0
)

(local

; Hidden Grove



	; (use "sciaudio")
	sarahGone =  0
	; snd
	falling =  0
)

(instance rm065 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
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
		; = g65Grove 1
		(RunningCheck)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 70 144 loop: 0)
			)
			(72 (gEgo posn: 70 144 loop: 0))
		)
; = snd aud (send snd:
;            command("playx")
;            fileName("music\\discordandpeace.mp3")
;            volume("70")
;            loopCount("-1")
;            init()
;        )
		(actionEgo
			init:
			hide:
			ignoreControl: ctlWHITE
			setScript: fallScript
		)
		(waterfall init: setCycle: Fwd cycleSpeed: 3 setPri: 0)
		; (waterRipple:init()setCycle(Fwd)cycleSpeed(3)setPri(1)ignoreActors())
		; (waterRipple2:init()setCycle(Fwd)cycleSpeed(3)setPri(1)ignoreActors())
		(sarah init: setScript: sarahScript ignoreActors:)
		(sarahScript changeState: 1)
		(squirrel
			init:
			setCycle: Walk
			ignoreActors:
			setScript: squirrelScript
			ignoreControl: ctlWHITE
		)
		(flower init: hide: setPri: 12)
		(squirrelScript changeState: 1)
		(if [g65Grove 1] (sarah init: hide:) (= sarahGone 1))
		(if [gFlowerGiven 5] (flower show:))
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 1))
			(1
				(if (not (== [g65Grove 0] 2))
					(gEgo setMotion: MoveTo 90 144 self)
				else
					(gEgo setMotion: MoveTo 90 144)
				)
			)
			(2 (= cycles 2))
			(3
				(PrintOther 65 15) ; #width 280 #at -1 20)
				(= [g65Grove 0] 2)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(checkEvent
						pEvent
						(squirrel nsLeft?)
						(squirrel nsRight?)
						(squirrel nsTop?)
						(squirrel nsBottom?)
					)
					(PrintOther 65 9)
				else                 ; #width 280 #at -1 8)
					(cond 
						(
							(checkEvent
								pEvent
								(sarah nsLeft?)
								(sarah nsRight?)
								(sarah nsTop?)
								(sarah nsBottom?)
							)
							(if (not sarahGone) (PrintOther 65 14))
						)
						((checkEvent pEvent 201 208 132 138) ; #width 280 #at -1 8)
							(if sarahGone
								(PrintOther 65 11)
								(Print
									65
									0
									#title
									{It reads:}
									#width
									280
									#at
									-1
									-1
									#font
									4
								)
							)
						)
						(
							(==
								ctlCYAN
								(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
							)                                                                      ; coffin
							(PrintOther 65 21)
						)
					)
					(if
						(checkEvent
							pEvent
							(waterfall nsLeft?)
							(waterfall nsRight?)
							(waterfall nsTop?)
							(waterfall nsBottom?)
						)
						(PrintOther 65 13)
					)                     ; #width 280 #at -1 8)
					(if
						(or
							(== ctlYELLOW (OnControl ocPRIORITY (pEvent x?) (pEvent y?)))  ; tree
							(== ctlFUCHSIA (OnControl ocPRIORITY (pEvent x?) (pEvent y?))))
						(PrintOther 65 12)
					)                     ; #width 280 #at -1 8)
					(if (checkEvent pEvent 201 208 132 138)
						(if sarahGone
							(PrintOther 65 11)
							(Print
								65
								0
								#title
								{It reads:}
								#width
								280
								#at
								-1
								-1
								#font
								4
							)
						)
					)
				)
			)
		)
		(if (or (Said 'listen[/!*]')
				(Said 'listen/voice,song'))
			(if g65Grove
				(PrintOther 63 24)	
			else
				(PrintSarah 65 45)
			)
		)
		(if (Said 'open,lift/grave,lid,sarcophagus')
			(if sarahGone
				(PrintOther 65 23)
			else
				(PrintOther 65 22)
			)
		)
		(if (Said 'give,place,leave,drop/flower')
			(if (gEgo has: 21)
				(if sarahGone
					(if [gFlowerGiven 5]
						(PrintOther 65 20)
					else
						(PrintOther 65 19)
						(flower show:)
						(= [gFlowerGiven 5] 1)
						(-- gFlowers)
						(if (== gFlowers 0) (gEgo put: 21))
						((gInv at: 21) count: gFlowers)
						(gGame changeScore: 1)
					)
				else
					(PrintSarah 65 47)
				)
			else
				(PrintDHI)
			)
		)
		(if
		(Said '(pick<up),(look,read,take)/(note,letter,paper)')
			(if sarahGone
				(if (& (gEgo onControl:) ctlSILVER)
					(if (not [gLetters 0])
						(= [gLetters 0] 1)
						(gGame changeScore: 1)
					)
					(PrintOther 65 11)
					(Print
						65
						0
						#title
						{It reads:}
						#width
						280
						#at
						-1
						-1
						#font
						4
					)
				else
					(PrintNCE)
				)
			else
				(PrintOther 65 10)
			)
		)                         ; #width 280 #at -1 8)
		(if (Said 'look>')
			(if (Said '/flower')
				(if [gFlowerGiven 5]
					(PrintOther 0 99)
				else
					(PrintOther 0 97)
				)
			)
			(if (Said '/squirrel,animal,creature')
				(PrintOther 65 9)
			)
			(if (Said '/cave,tunnel')
				(PrintOther 65 26)
			)                     
			(if (Said '/woman,sarah')
				(if (not sarahGone)
					(PrintOther 65 8)
				else                 ; #width 280 #at -1 8)
					(PrintOther 65 17)
				)
			)                         ; #width 280 #at -1 8)
			(if (Said '/stone,altar,grave') (PrintOther 65 21))
			(if (Said '/tree') (PrintOther 65 7)) ; #width 280 #at -1 8)
			(if (Said '/guitar,instrument')
				(if (not sarahGone)
					(PrintOther 65 6)
				else                 ; #width 280 #at -1 8)
					(PrintOther 65 25)
				)
			)
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(if (not sarahGone)
					(PrintOther 65 5)
				else                 ; #width 280 #at -1 8)
					(PrintOther 65 24)
				)
			)
		)                      
		(if (Said 'talk/woman,muse')
			(if (not sarahGone)
				(if (<= (gEgo distanceTo: sarah) 35)
					(PrintSarah 65 28)
					(PrintSarah 65 29)
					(PrintSarah 65 30)
				else
					(PrintNCE)
				)
			else
				(PrintOther 65 2)
			)
		)                    
		(if (Said '(ask<about)>')
			(if (not sarahGone)
				(if (<= (gEgo distanceTo: sarah) 35)
					(if (Said '/woman')
						(PrintSarah 65 31)
						(PrintSarah 65 32)
					)
					(if (Said '/name')
						(PrintSarah 65 52)
					)
					(if (Said '/music,song')
						(PrintSarah 65 33)
						(PrintSarah 65 34)
					)
					(if (Said '/princess')
						(PrintSarah 65 35)
						(PrintSarah 65 36)
					)
					(if (Said '/riddle')
						(PrintSarah 65 37)
						(PrintSarah 65 38)
						(PrintSarah 65 39)
					)
					(if (Said '/gyre,husband,love') (PrintSarah 65 40))
					(if (Said '/dress,clothes,marriage')
						(PrintSarah 65 41)
						(PrintSarah 65 42)
					)
					(if (Said '/rock,soil,destiny') (PrintSarah 65 43))
					(if (Said '/tunnel,cave') (PrintSarah 65 44))
					(if (Said '/gift,ring') (PrintSarah 65 42))
					(if (Said '/vow') (PrintSarah 65 45))
					(if (Said '/squirrel') (PrintSarah 65 48))
					(if (Said '/*') (PrintSarah 65 46))
				else
					(if (Said '/*')
						(PrintNCE)
					)
				)
			else
				(if (Said '/*')
					(PrintOther 65 3)
				)
			)
		)                        ; #at -1 8)
		;(if (Said 'use/map') (Print 65 16)) ; This isn't a good place to use that.
		(if (Said 'sing') (PrintOther 65 18)) ; This isn't a good place to use that.
		(if (Said '((tell<about),show,give)/ring')
			; (if(Said('ring'))
			(if (not sarahGone)
				(if (gEgo has: INV_RING)
					(sarahScript changeState: 5)
				else
					(PrintOther 65 1)
				)
			else
				(PrintOther 65 2)
			)
		)
	)
	
	; )
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 72)
		)
		(if (& (gEgo onControl:) ctlGREY)       ; fall from  back
			(if (not falling)
				(fallScript changeState: 4)
				(= falling 1)
			)
		)
	)
)

(instance squirrelScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(squirrel
					setMotion: MoveTo 107 (+ (squirrel y?) 10) squirrelScript
				)
			)
			(2
				(squirrel setMotion: MoveTo 258 175 squirrelScript)
			)
			(3 (squirrel loop: 1 cel: 0))
		)
	)
)

(instance sarahScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles (Random 30 60))
				(sarah loop: 2 setCycle: Fwd cycleSpeed: 2)
			)
			(2
				(sarah loop: 0 cel: 0 setCycle: End sarahScript)
			)
			(3 (sarahScript changeState: 1))
			(5
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(self cycles: 0)
				(gEgo
					ignoreControl: ctlWHITE
					setMotion: MoveTo 186 166 sarahScript
				)
				
			)
			(6
				;(= cycles 5)
				(gEgo loop: 3 observeControl: ctlWHITE)
				(sarah
					loop: 1
					cel: 0
					setCycle: End sarahScript
					cycleSpeed: 2
				)
				(gTheSoundFX number: 205 play:)
				
			)
			(7
				(= cycles 1)
				(PrintSarah 65 49)
				(PrintSarah 65 50)
				(PrintSarah 65 51)
			)
			(8
				(= sarahGone 1)
				(= [g65Grove 1] 1)
				(gGame changeScore: 1)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				
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
			(4       ; falling from top
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(actionEgo
					show:
					view: 88
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					yStep: 7
					setMotion: MoveTo (gEgo x?) 186 self
					setPri: 1
				)
			)
			(5
				(= cycles 13)
				(actionEgo hide:)
				(gTheSoundFX number: 202 play:)
			)
			; (deathSplash:show()posn((actionEgo:x)(actionEgo:y))setCycle(End self) setPri(1)cycleSpeed(2))
			(6
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

(procedure (PrintSarah textRes textResIndex)
	(= gWndColor 15) ; clWHITE
	(= gWndBack 3) ; clDARKBLUE
	(Print textRes textResIndex #at -1 20 #title {She says:})
	(= gWndColor 0) ; clWHITE
	(= gWndBack 15)
)
                  ; clDARKBLUE
(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #at -1 10)
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

(instance actionEgo of Act
	(properties
		y 1
		x 1
		view 408
	)
)

(instance sarah of Prop
	(properties
		y 154
		x 206
		view 404
		loop 2
	)
)

(instance waterfall of Prop
	(properties
		y 101
		x 210
		view 65
		loop 3
	)
)

(instance waterRipple of Prop
	(properties
		y 120
		x 175
		view 65
		loop 1
	)
)

(instance waterRipple2 of Prop
	(properties
		y 145
		x 260
		view 65
		loop 1
	)
)

(instance squirrel of Act
	(properties
		y 147
		x 45
		view 61
	)
)

(instance flower of Prop
	(properties
		y 138
		x 206
		view 97
		loop 1
	)
)
