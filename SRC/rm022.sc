;;; Sierra Script 1.0 - (do not remove this comment)

(script# 22)
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
	rm022 0
)

(local

	goingUp =  0
)
; snd

(instance rm022 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 60
		west 50
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
		(sign init: setPri: 7)
		(whiteDoor init:)
		(wind init: hide: ignoreActors: setScript: windScript)
		(if g63PuzSol (whiteDoor hide:))
		(switch gPreviousRoomNumber
			(21
				(gEgo posn: 125 110 loop: 2)
			)
; = snd aud (send snd:
;                    command("playx")
;                    fileName("music\\overworld.mp3")
;                    volume("70")
;                    loopCount("0")
;                    init()
;                )
			(60
				(gEgo posn: 100 175 loop: 3)
			)
			(50 (gEgo posn: 15 138 loop: 0))
; = snd aud (send snd:
;                    command("playx")
;                    fileName("music\\overworld.mp3")
;                    volume("70")
;                    loopCount("0")
;                    init()
;                )
			(57
				(if (< (gEgo y?) 120)   ; glider
					(gEgo init: hide: ignoreControl: ctlWHITE posn: 50 20)
					(alterEgo
						init:
						view: 60
						ignoreControl: ctlWHITE
						posn: 262 10
						xStep: 7
						setMotion: MoveTo 1 15 RoomScript
					)
				else
					(= goingUp 2)
					(gEgo posn: 288 82 loop: 1)
					(RoomScript changeState: 5)
				)
			)
			(else 
				(gEgo posn: 120 140 loop: 3)
			)
		)
	)
)

; (if(gMatt)
;            (matt:init()setCycle(Walk))
;        )

(instance RoomScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1 (gRoom newRoom: 50))
			(2
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(= gEgoRunning 0)
				(RunningCheck)
				(gEgo setMotion: MoveTo 287 81 RoomScript)
			)
			(3
				(gEgo setMotion: MoveTo 314 64 RoomScript)
			)
			(4
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(gRoom newRoom: 57)
			)
			; Moving back down the stair from the East
			(5
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 253 111 self)
			)
			(6
				(gEgo setMotion: MoveTo 221 127 self)
			)
			(7
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= goingUp 0)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evJOYSTICK)
			(if (or (== (pEvent message?) 7) (== (pEvent message?) 6) (== (pEvent message?) 5)) ; If pressed the LEFT arrow ; LEFT/DOWN diagonal button ; DOWN
				(if (== goingUp 1)
					(++ goingUp)	; set goingUp to 2 (so you cannot continue sending the same signal)
					(self changeState: 6)			
				)
			)
		)
		(if (or (== (pEvent message?) 1) (== (pEvent message?) 2) (== (pEvent message?) 3)) ; Up, right, or right/up
			(if (== goingUp 2)
				(-- goingUp)	; set goingUp to 1 (so you cannot continue sending the same signal)
				(self changeState: 2)					
			)
		)
	

		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(==
						ctlSILVER
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                       ; Fence to Scientist's house
					(PrintOther 22 21)
					(PrintOther 22 25)
				)
				(if
					(==
						ctlRED
						(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
					)                                                                 ; ruins
					(PrintOther 22 22)
				)
				(if
					(or
						(==
							ctlSILVER
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                                                   ; Stairway
						(==
							ctlNAVY
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)
						(==
							ctlGREY
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)
					)
					(PrintOther 22 24)
				)
				(if (checkEvent pEvent 74 86 13 27) (PrintOther 22 23)) ; skull well
			)
		)
		(if (Said 'look>')
			(if (Said '/mountain,stair') (PrintOther 22 24))
			(if (Said '/face,skull,well') (PrintOther 22 23))
			(if (Said '/ruin,pillar') (PrintOther 22 22))
			(if (Said '/fence') (PrintOther 22 21))
			(if (Said '/sign') (PrintOther 22 25))
			(if (Said '[/!*]') (PrintOther 22 20))
			; this will handle just "look" by itself
		)
		(if (Said 'climb') (Print {Go ahead and do it.}))
		
		(if (Said 'feel[/wind]') (PrintOther 22 26))
		
		(if (Said 'use,fly/kite,glider')
			(if (or (gEgo has: INV_KITE) (gEgo has:INV_GLIDER))
				(PrintOther 22 26)	
			else
				(PrintDHI)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 21)
		)
		(if (& (gEgo onControl:) ctlGREY)
			(if (not goingUp)
				(RoomScript changeState: 2)
				(= goingUp 1)
			)
		)
		(if (& (gEgo onControl:) ctlNAVY)
			(if (not goingUp)
				(RoomScript changeState: 5)
				(= goingUp 1)
			)
		)
	)
)

(instance windScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles (Random 1 20)))
			(1
				(wind
					show:
					cel: 0
					posn: (Random 220 278) (Random 13 30)
					setCycle: End self
					cycleSpeed: 1
				)
			)
			(2 (self changeState: 0))
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

(instance alterEgo of Act
	(properties
		y 141
		x 262
		view 60
	)
)

(instance whiteDoor of Act
	(properties
		y 56
		x 194
		view 89
	)
)

(instance wind of Prop
	(properties
		y 150
		x 181
		view 58
		loop 3
	)
)

(instance sign of Prop
	(properties
		y 87
		x 28
		view 132
	)
)
