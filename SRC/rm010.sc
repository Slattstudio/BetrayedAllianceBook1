;;; Sierra Script 1.0 - (do not remove this comment)
; +1 Score
(script# 10)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use menubar)

(public
	rm010 0
)

(local
; *                          Screen to Name our Hero!                            *

	snd
)

(instance rm010 of Rm
	(properties
		picture 900
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
		)
		(SetUpEgo)
		(TheMenuBar draw:)
		(SL enable:)
		(TheMenuBar state: ENABLED)
		(gGame setSpeed: 4)
		(= gMap 1)
		(= gArcStl 1)
	)
)

; = snd aud (send snd:
;            command("stop")
;            fileName("music\\intro.mp3")
;            fadeOutMillisecs("4000")
;            loopCount("0")
;            init()
;        )

(instance RoomScript of Script
	(properties)
	
	(method (changeState intro &tmp button [buffer 300])
		(= state intro)
		(switch state
			(0 (= cycles 5))
			(1 (= cycles 1))
			(2 (= cycles 1))
			(3
				(= cycles 10)
				(EditPrint
					@gName
					14
					{Welcome Hero! Please do us the honor of knowing your name:}
					#at
					70
					40
				)
				(if (== gName 0)
					(Print {Come on you can do better!})
					(RoomScript changeState: 2)
				else
					(Format
						@buffer
						{Excellent! Good luck in your quest, %s.}
						@gName
					)
					(Print @buffer #at -1 40 #mode alCENTER #width 250)
				)
			)
			; (send gGame:changeScore(1))
			(4 (= cycles 6))
			(5
				(= cycles 7)
				(= gWndColor 11) ; clCYAN
				(= gWndBack 1)   ; clDARKBLUE
				(Print
					{You awaken hazily in an unknown place. Upon escaping the castle, falsely charged of kidnapping the princess on the eve of the peace treaty, you remember finding respite near a pond, but after that it's all gone.}
					#at
					-1
					20
					#width
					250
					#font
					4
				)
			)
			(6
				(= cycles 4)
				(Print
					{Well, better figure out what this place is, and even more importantly, a way to get out.}
					#at
					-1
					40
					#width
					220
					#font
					4
				)
				(= gWndColor 0) ; clBLACK
				(= gWndBack 15)
			)                   ; clWHITE
			(7
				(gEgo get: 1)
; = snd aud (send snd:
;                    command("playx")
;                    fileName("music\\wizardsroom.mp3")
;                    volume("70")
;                    loopCount("-1")
;                    init()
;                )
				(gRoom newRoom: 18)
			)
		)
	)
)
