;;; Sierra Script 1.0 - (do not remove this comment)

(script# 25)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use door)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use DCIcon)
(use sciaudio)

(public
	rm025 0
)

(local
; Entrance to Cave to Castle Passage



	number =  0
	[array 4] = [2 4 6 8]
	; bloodChance
	; bloodVisible = 0
	snd
)

(instance rm025 of Rm
	(properties
		picture scriptNumber
		north 0
		east 20
		south 68
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
		(RunningCheck)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
			; = bloodChance 50
			(20
				(gEgo posn: 300 150 loop: 1)
			)
			; = bloodChance 50
			(24
				(gEgo posn: 130 115 loop: 2 setMotion: MoveTo 130 125)
			)
			; (send gTheMusic:prevSignal(0)stop())
			; = bloodChance 95
			(68
				(gEgo posn: 130 175 loop: 3)
			)
			; = bloodChance 50
			(31
				(gEgo posn: 80 150 loop: 0)
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
			)
		)
		(= gFollowed 0) ; No emeny is following you
		(= gRanAway 0)
		(= gEnNum 0) ; Resets amount of enemies to normal
		(horse init: setCycle: Fwd cycleSpeed: 6)
	)
)

; (send gTheMusic:prevSignal(0)stop()number(25)loop(0)play())

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
		)
	)
	
	(method (handleEvent pEvent &tmp message)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(checkEvent
						pEvent
						(horse nsLeft?)
						(horse nsRight?)
						(horse nsTop?)
						(horse nsBottom?)
					)
					(PrintOther 25 20)
				)
				(if (checkEvent pEvent 40 109 54 70) (PrintOther 25 22)) ; Pond
				(if (checkEvent pEvent 293 319 0 79) (PrintOther 25 23)) ; Tower
				(if (checkEvent pEvent 51 71 80 149) (PrintOther 25 24)) ; Cave
			)
		)
		(if (Said 'smell[/!*]')
			(PrintOther 25 30)
		)
		(if (Said 'look>')
			(if (Said '/pond') (PrintOther 25 25))
			(if (Said '/cave') (PrintOther 25 24))
			(if (Said '/woman,leah,horse') (PrintOther 25 20))
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(PrintOther 25 26)
				(PrintOther 25 27)
			)
		)
		(if (Said 'jump,climb/fence') (PrintOther 25 29))
	)
	
	(method (doit)
		(super doit:)
		(if (== (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 24)
		)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 31)
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

(instance testIcon of DCIcon
	(properties)
	
	(method (init)
		(super init:)
		(if (== gRoomNumberExit 540)
			(= cycler (End new:))
			(cycler init: self)
		)
	)
)

(instance aud of sciAudio
	(properties)
	
	(method (init)
		(super init:)
	)
)

(instance horse of Prop
	(properties
		y 50
		x 207
		view 50
		loop 6
	)
)
