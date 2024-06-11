;;; Sierra Script 1.0 - (do not remove this comment)

(script# 49)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use sciaudio)

(public
	rm049 0
)

(local
; Dock Building



	snd
	badJoke =  0
	doorOpen =  0
	falling =  0
	inShed = 0

)

(instance rm049 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 61
		west 48
	)
	
	(method (init)
		(super init:)
		(self
			setScript: RoomScript
			setRegions: 200
			setRegions: 204
		)
		(SetUpEgo)
		(gEgo init: observeControl: ctlCYAN)
		(RunningCheck)
		(switch gPreviousRoomNumber
			(else )
			; (send gEgo:posn(150 130)loop(1))
			(48
				(if (> (gEgo y?) 140)   ; on bridge
					(gEgo posn: 20 150 loop: 0)
				else
					(gEgo posn: 20 82 loop: 0 setMotion: MoveTo 60 90)
				)
			)
			(22 (gEgo posn: 85 75 loop: 2))
			(61
				(gEgo posn: 215 175 loop: 3)
			)
			(60
				(gEgo posn: 93 64 loop: 2 setMotion: MoveTo 93 90)
			)
			(62
				(gEgo posn: 194 160 loop: 1)
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
			)
		)
		(actionEgo init: hide: ignoreControl: ctlWHITE)
		(deathSplash
			init:
			hide:
			ignoreActors:
			setPri: 1
			setScript: fallScript
		)
	)
)

; (door:init()setPri(5)ignoreActors())
; = snd aud
;        (send snd:
;            command("play")
;            fileName("music\\sailboat.mp3")
;            volume("100")
;            loopCount("-1")
;            init()
;         )

(instance RoomScript of Script
	(properties)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(==
						ctlLIME
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)
					(if (> (pEvent x?) 118)
						(if (& (gEgo onControl:) ctlGREY)
							(PrintOther 49 1) ; The barrel has some markings of arrows on it.
							(Print {} #title {The Carving:} #icon 95)
							(if (not [gArtwork 2]) 
								(= [gArtwork 2] 1)
								(PrintOther 49 15)
							)
						else
; (if(not(badJoke))
;                            Print("It's so cryptic you'll flip your lid!")
;                            Print("I know, that joke is a barrel of laughs.")
;                            Print("Still not funny? Was the delivery too wooden?")
;                            = badJoke 1
;                        )
							(PrintOther 49 2)
						)
					)
				)                            ; A barrel stands upright near the dock house. Closer inspection shows marks on the lid, but you're too far away to make it out.
				(if
					(==
						ctlBLUE
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)
					(PrintOther 49 3)   ; All vessels must check in and out at the dock, including all cargo orders. Security measures, of course.
					(PrintOther 49 4)
				)                    ; The dock also provides all domestic mail deliveries as well as those over seas.
				(if
					(and
						(> (pEvent x?) 0)       ; Dock
						(< (pEvent x?) 80)
						(> (pEvent y?) 145)
						(< (pEvent y?) 175)
					)
					(Print 49 5)
				)
			)
		)                       ; Shelah's dock for larger vessels.
		(if (or (Said 'open/door')
				(Said 'enter/building,dock,house'))
			(if (& (gEgo onControl:) ctlRED)
				(PrintOK)
				(gRoom newRoom: 62)
			else
				(PrintNCE)
			)
		)
		(if (Said 'knock')
			(if (& (gEgo onControl:) ctlRED)
				(PrintOther 49 14)
			else
				(PrintNCE)
			)
		)
		(if (Said 'open,lift/lid,barrel')
			(if (& (gEgo onControl:) ctlGREY)
				(Print 49 6)
			else
				(PrintNCE)
			)
		)
		(if (Said 'take/barrel') (Print 49 7)) ; Do you want to store it in your pocket or your bag?
		(if (or (Said 'pants,bag') (Said 'put/pants,bag'))
			(Print 49 8)
		)                    ; Doesn't fit
		(if (Said 'look,examine>')
			(if (Said '/window,glass')
				(if (or (& (gEgo onControl:) ctlGREY)
						(& (gEgo onControl:) ctlRED))
					(PrintOther 49 18)
				else
					(PrintOther 49 17)
				)	
			)
			(if (Said '/barrel,lid')
				(if (& (gEgo onControl:) ctlGREY)
					(PrintOther 49 1) ; The barrel has some markings of arrows on it.
					(Print {} #title {The Carving:} #icon 95)
					(if (not [gArtwork 2]) 
						(= [gArtwork 2] 1)
						(PrintOther 49 15)
					)
				else
; (if(not(badJoke))
;                        Print("It's so cryptic you'll flip your lid!")
;                        Print("I know, that joke is a barrel of laughs.")
;                        Print("Still not funny? Was the delivery too wooden?")
;                        = badJoke 1
;                    )
					(PrintOther 49 2)
				)
			)
			(if (Said '/building,house,dock, sign')
				(PrintOther 49 9)
			)                    ; All traveling merchants must check their cargo here. Because of its dealings with seafarers the King has made the Royal Dock the mail delivery service as well.
			(if (Said '/mountain') (PrintOther 49 10)) ; The eastern mountains' beauty strikes you. The glory of these mountains is often obscured by the many ghost legends growing up around them. They certainly have a different feeling at night.
			(if (Said '/shed,shack')
				(if (& (gEgo onControl:) ctlGREEN)
					(PrintOther 49 12)
				else
					(PrintOther 49 13)
				)
			)
			(if (Said '/pulley,rope,hook')
				(PrintOther 49 16)	
			)
			(if (Said '[/!*]')
				(if inShed
					(PrintOther 49 12)
				else
					; this will handle just "look" by itself
					(PrintOther 49 11)
				)
			)
		)
	)

	(method (doit)
		(super doit:)
		
		(if (& (gEgo onControl:) ctlGREEN)
			(= inShed 1)
			(gEgo setPri: 10)	; make sure ego sprite doesn't show through roof (did this so map could be higher priority than roof)		
		else
			(= inShed 0)
			(gEgo setPri: -1)	; set ego priority back to being tracked by y axis
		)
		
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 60)
		)
		(if (& (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 62)
		)
		(if (& (gEgo onControl:) ctlFUCHSIA)        ; Fall into water  from "top"
			(if (not falling)
				(if (< (gEgo y?) 157)
					(fallScript changeState: 7)
				else
					(fallScript changeState: 10)
				)
				(= falling 1)
			)
		)
		(if (& (gEgo onControl:) ctlNAVY)         ; fall from bottom of pier
			(if (not falling)
				(fallScript changeState: 1)
				(= falling 1)
			)
		)
		(if (& (gEgo onControl:) ctlBLUE)       ; fall from  "top" of pier
			(if (not falling)
				(fallScript changeState: 4)
				(= falling 1)
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
			(1
				(= cycles 2)     ; falling from bottom
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
					yStep: 10
					setMotion: MoveTo (gEgo x?) 178 self
					setPri: 1
				)
			)
			(8
				(actionEgo hide:)
				(deathSplash
					show:
					posn: (gEgo x?) 178
					setCycle: End self
					setPri: 1
					cycleSpeed: 3
				)
				(gTheSoundFX number: 202 play:)
			)
			(9 (self changeState: 14))
			(10
				(= cycles 2)     ; falling from bottom
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
					setMotion: MoveTo (gEgo x?) 184
					setPri: 15
				)
			)
			(11
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
			(12 (self changeState: 14))
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

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 100)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
	)
)

(instance aud of sciAudio
	(properties)
	
	(method (init)
		(super init:)
	)
)

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
