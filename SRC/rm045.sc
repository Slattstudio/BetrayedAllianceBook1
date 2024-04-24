;;; Sierra Script 1.0 - (do not remove this comment)
;
; *                      Outside the Library                                     *
(script# 45)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use dpath)
(use sciaudio)

(public
	rm045 0
)

(local


	movingRight =  0
	running =  0
)
; snd

(instance rm045 of Rm
	(properties
		picture scriptNumber
		north 0
		east 60
		south 48
		west 28
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
				(gEgo posn: 285 95 loop: 2)
			)
			; (send gTheMusic:prevSignal(0)stop())
			(28 (gEgo posn: 30 175 loop: 0))
			; (send gTheMusic:prevSignal(0)stop())
			(46
				(gEgo posn: 248 103 loop: 2)
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
			)
			; (send gTheMusic:prevSignal(0)stop())
; = snd aud (send snd:
;                    command("playx")
;                    fileName("music\\overworld.mp3")
;                    volume("70")
;                    loopCount("0")
;                    init()
;                )
			(48
				(gEgo posn: 260 175 loop: 3)
			)
			; (send gTheMusic:prevSignal(0)stop())
			(60
				(gEgo posn: 300 140 loop: 1)
			)
		)
		; (send gTheMusic:prevSignal(1)stop())
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		(statue init: hide:)
	)
)

; (horse:init()setCycle(Walk)ignoreControl(ctlWHITE)setScript(horseScript))
(instance RoomScript of Script
	(properties)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
; (if((> (send pEvent:x) 1)and // fence
;                    (< (send pEvent:x) 87)and
;                    (> (send pEvent:y) 85)and
;                    (< (send pEvent:y) 148))
;                    Print("The fence keeps the crops safe from potential herbivores, both animal and human." #width 290 #at -1 8)
;                )
;                (if((> (send pEvent:x) (statue:nsLeft))and
;                    (< (send pEvent:x) (statue:nsRight))and
;                    (> (send pEvent:y) (statue:nsTop))and
;                    (< (send pEvent:y) (statue:nsBottom)))
;                    Print("A statue of the Great Wizard. Looks different than the wizard you met earlier." #width 280 #at -1 8)
;                )(else
;                    (if((> (send pEvent:x) 88)and // bush
;                        (< (send pEvent:x) 163)and
;                        (> (send pEvent:y) 92)and
;                        (< (send pEvent:y) 105))
;                        Print("There are lovely looking berries in the bush by the library." #width 290 #at -1 8)
;                    )
;                )
				(if
					(and
						(> (pEvent x?) 117)    ; library
						(< (pEvent x?) 319)
						(> (pEvent y?) 1)
						(< (pEvent y?) 94)
					)
					(PrintOther 45 0)
				)
			)
		)                            ; #width 290 #at -1 8)
; (if (Said('talk>'))
;            (if (Said('/wizard,statue'))
;                Print("It may be a wizard, but it's still a statue. It doesn't talk back."#width 280 #at -1 8)
;            )
;            (if (Said('[/!*]'))
;                // this will handle just "talk" by itself
;                Print("Who are you talking to?" #width 280 #at -1 8)
;            )
;            (if (Said('/*'))
;                // this will handle "talk anyword"
;                Print("No one talks back." #width 280 #at -1 8)
;            )
;        )
		(if (Said 'look>')
			(if (Said '/library,building') (PrintOther 45 0))

			(if (Said '[/!*]') (PrintOther 45 1))
		; this will handle just "look" by itself
		)
		(if (Said 'pick,take,eat/berry,berries')
			(if (& (gEgo onControl:) ctlSILVER)
				(Print
					{That's not a berry good idea. What if they're poisonous? You wouldn't want to be berried so young would you?}
					#width
					280
					#at
					-1
					8
				)
			else
				(PrintNCE)
			)
		)
; (if(Said('read/book'))
;            (if(<=(send gEgo:distanceTo(statue))45)
;                Print("The characters in the book do not appear to be in a language you can understand.")
;            )(else
;                PrintNCE()
;            )
;        )
		(if (Said 'open/door')
			(if (& (gEgo onControl:) ctlMAROON)
				(gRoom newRoom: 46)
			else
				(PrintNCE)
			)
		)
		(if (Said 'knock')
			(if (& (gEgo onControl:) ctlMAROON)
				(PrintOther 45 2)
			else
				(PrintNCE)
			)
		)
	)
	
	(method (doit)
		(super doit:)
	)
)

; (if(movingRight)
;            (girl:init()loop(5)posn((+(horse:x)3) (-(horse:y)17))setPri(14))
;        )(else
;            (girl:init()loop(4)posn((-(horse:x)2) (-(horse:y)17))setPri(14))
;        )
; (instance horseScript of Script
;    (properties)
;    (method (changeState newState)
;        = state newState
;        (switch (state)
;            (case 0 = cycles 1
;            )(case 1
;                = movingRight 1
;                (if(running)
;                    (horse:view(325)setMotion(MoveTo 300 170 horseScript)cycleSpeed(1))
;                )(else
;                    (horse:view(326)setMotion(MoveTo 300 170 horseScript)cycleSpeed(1))
;                )
;            )(case 2
;                = movingRight 0
;                (if(running)
;                    (horse:view(325)setMotion(MoveTo 70 170 horseScript))
;                )(else
;                    (horse:view(326)setMotion(MoveTo 70 170 horseScript))
;                )
;            )(case 3
;                (horseScript:changeState(1))
;            )
;        )
;    )
; )
; (instance aud of sciAudio
;   (properties)
;   (method (init)
;      (super:init())
;   )
; )
(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 100)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
	)
)

; (instance horse of Act
;    (properties y 170 x 38 view 326)
; )
; (instance girl of Prop
;    (properties y 150 x 38 view 318 loop 4)
; )
(instance statue of Prop
	(properties
		y 130
		x 170
		view 84
	)
)
