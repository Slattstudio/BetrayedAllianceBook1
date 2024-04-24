;;; Sierra Script 1.0 - (do not remove this comment)

(script# 99)
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
	rm099 0
)

; ENDING - Skull Entrance



; (use "sciaudio")
; snd

(instance rm099 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: hide:)
		(wizard init: ignoreActors:)
		(julyn init: ignoreActors:)
		
	)
)

; (TheMenuBar:state(DISABLED))
; = snd aud (send snd:
;                    command("playx")
;                    fileName("music\\towardstheeast.mp3")
;                    volume("70")
;                    loopCount("-1")
;                    init()
;        )

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
	
		(SetCursor 998 (HaveMouse))
		(= gCurrentCursor 998)
	)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 1))
			(1
				(= cycles 15)
				(julyn setCycle: Walk setMotion: MoveTo 190 170)
				(wizard setCycle: Walk setMotion: MoveTo 183 175)
			)
			(2
				(= cycles 11)
				(PrintWiz 99 1)
				(PrintWiz 99 2)
				(julyn loop: 2)
			)
			(3
				(= cycles 11)
				(PrintWiz 99 3)
				(PrintWiz 99 4)
			)
			(4 (= cycles 1))
			; (julyn:loop(1))
			(5
				(= cycles 12)
				(PrintJulyn 99 5)
			)
			(6
				(= cycles 5)
				(PrintWiz 99 6)
			)
			(7
				(= cycles 9)
				(DrawPic 900)
				(julyn hide:)
				(wizard hide:)
			)
			(8
				(= gWndColor 14) ; clYELLOW
				(= gWndBack 1) ; clDARKBLUE
				(Print
					{Congratulations on beating the game! We hope you enjoyed the experience and maybe even felt a little nostalgia.\n\nBetrayed Alliance: Book One is now complete. We hope you look forward to Betrayed Alliance: Book Two. If you would like to help, please leave some feedback!\n\nREMEMBER TO SHARE THIS GAME WITH EVERYONE (it is free after all)!}
					#mode
					alCENTER
					#width
					280
				)
				;(Print 997 22 #font 4 #title {Playtesting Credits:} #width 220)
				(= gWndColor 0) ; clBlack
				(= gWndBack 15) ; clWhite
				(FormatPrint
					{Thank you for playing Book 1 of Betrayed Alliance.\n\nYour final score was %u of %u.}
					gScore
					gMaxScore
				)
				(if (< gScore gMaxScore)
					(Print
						{Did you find all the secrets and explore your surroundings thoroughly?\n\nDid you collect all the marbles and other collectibles?}
						#font
						4
					)
				else
					(Print
						{Wow! Maybe you are the Great Wizard with a score like that! You are either a adventure game pro or a Razzle Dazzle Root Beer cheater! Either way, we salute you!}
						#font
						4
					)
				)
				(Print
					{A free Demo for Betrayed Alliance Book 2 is currently available. We're working hard to complete it.\n\nGoodbye for now and thanks for your support!}
					#title
					{Is Book 2 Ready?}
					#width
					220
				)
				(gGame restart:)
				;(= gQuitGame TRUE)
			)
		)
	)
)

(procedure (PrintWiz textRes textResIndex)
	(= gWndColor 14) ; clYELLOW
	(= gWndBack 2) ; clGREEN
	(Print
		textRes
		textResIndex
		#title
		{Wizard:}
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
(procedure (PrintJulyn textRes textResIndex)
	(= gWndColor 13) ; clYELLOW
	(= gWndBack 5) ; clDARKBLUE
	(Print
		textRes
		textResIndex
		#title
		{Julyn:}
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
(instance julyn of Act
	(properties
		y 184
		x 185
		view 308
		loop 3
	)
)

(instance wizard of Act
	(properties
		y 189
		x 177
		view 313
		loop 3
	)
)
