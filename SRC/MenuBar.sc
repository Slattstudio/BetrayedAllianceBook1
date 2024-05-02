;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; menubar.sc
; Contains the customized Menubar class. This is the script you modify if you
; want to customize the menu.
(script# MENUBAR_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use controls)
(use gauge)
(use sound)
(use user)

(public
	ToggleSound 0
)

(local


	armor =  0
	books =  0
	i
)

(class TheMenuBar of MenuBar
	(properties
		state 0
	)
	
	(method (init)
		(AddMenu { _} {About Game`^g:Help`#1:Settings`#4})
		(AddMenu
			{ File_}
			{Restart Game`#9:Save Game`#5:Restore Game`#7:--! :Quit`^q}
		)
		(AddMenu
			{ Character -}
			{Ask About`^a:Retype`#3:--!:Inventory`^I:Character Stats`^c:Notes`^n}
		)
		(AddMenu
			{ Speed_}
			{Change...`^s:--!:Faster`+:Normal`=:Slower`-Music}
		)
		(if (DoSound sndSET_SOUND)
			(AddMenu { %_} {Volume...`^v:Turn Off`#2=1})
		else
			(AddMenu { Sound_} {Volume...`^v:Turn On`#2=1})
		)
; (if(< Graph(grGET_COLOURS) 9)
; 			SetMenu(MENU_CHARACTER 32 0)
; 		)(else
; 			SetMenu(MENU_CHARACTER smMENU_SAID '/color')
; 		)
		(SetMenu MENU_DIFFICULTY smMENU_SAID 'difficulty,settings')
		(SetMenu MENU_CHARACTER smMENU_SAID 'character')
		(SetMenu MENU_SAVE smMENU_SAID 'save[/game]')
		(SetMenu MENU_RESTORE smMENU_SAID 'restore[/game]')
		(SetMenu MENU_RESTART smMENU_SAID 'restart[/game]')
		(SetMenu MENU_QUIT smMENU_SAID 'done[/game]')
		(SetMenu MENU_INVENTORY smMENU_SAID 'all')
	)
	
	(method (handleEvent pEvent &tmp menuItem hGauge newSpeed newVolume wndCol wndBack hPause [buffer 500] button)
		(= menuItem (super handleEvent: pEvent))
		(switch menuItem
			(MENU_ABOUT
				(= gWndColor 11)    ; clCYAN
				(= gWndBack 0)      ; clBLACK
				(Print 997 0 #font 4 #title {About} #icon 999 2 0 #width 160)
				(Print 997 1 #font 4 #title {Art and Story} #icon 952)
				(Print 997 3 #font 4 #title {Programming} #icon 951)
				(Print 997 2 #font 4 #title {Music} #icon 950)
				(Print 997 22 #font 4 #title {Playtesting} #width 220)
				(Print 997 20 #title {Kickstarter Credits:} #font 4 #width 320)
				(Print 997 21 #title {Kickstarter Credits:} #font 4 #width 320)
				(= gWndColor 0)     ; clBLACK
				(= gWndBack 15)
			)                       ; clWHITE
			(MENU_HELP
				(= gWndColor 1)    ; clBLACK
				(= gWndBack 11)      ; clCYAN
				(Print 997 15 #title {Help} #font 4 #width 280)
				(= gWndColor 0)     ; clBLACK
				(= gWndBack 15)
			)                       ; clWHITE
			(MENU_DIFFICULTY
				(= gVertButton 1)
				(= gWndColor 0)
				(= gWndBack 13)
				(= gHardMode
					(Print
						10
						0
						#title
						{Puzzle Difficulty:}
						#button
						{ Veteran_}
						1
						#button
						{ Novice_}
						0
						#font
						4
					)
				)
				(= gWndColor 0) ; clYELLOW
				(= gWndBack 14) ; clDARKBLUE
				(= gAutosave
					(Print 997 12
						#title
						{Autosave and Retry:}
						#button
						{ On_}
						1
						#button
						{ Off_}
						0
						#font
						4
					)
				)
				;(= gHardMode button)
				
				(= gVertButton 0)
				(= gWndColor 0)     ; clBLACK
				(= gWndBack 15)
			)                       ; clWHITE
			(MENU_RESTART
				(if
					(Print
						997
						7
						#title
						{Restart}
						#font
						gDefaultFont
						#button
						{Restart}
						1
						#button
						{ Game on_}
						0
					)
					(gGame restart:)
				)
			)
			(MENU_RESTORE (gGame restore:))
			(MENU_SAVE (gGame save:))
			(MENU_QUIT
				(if
					(Print
						997
						8
						#title
						{Quit}
						#font
						gDefaultFont
						#button
						{ Quit_}
						1
						#button
						{ Play_}
						0
					)
					(= gQuitGame TRUE)
				)
			)
			(MENU_ASKABOUT
				(StrCpy (User inputLineAddr?) {ask about_})
				(pEvent
					claimed: FALSE
					type: evKEYBOARD
					message: (User echo?)
				)
			)
; (case MENU_TELLABOUT
;                StrCpy((User:inputLineAddr) "tell about ")
;                (send pEvent:
; 					claimed(FALSE)
; 					type(evKEYBOARD)
; 					message((User:echo))
; 				)
; 			)
			(MENU_RETYPE
				(pEvent
					claimed: FALSE
					type: evKEYBOARD
					message: (User echo?)
				)
			)
			(MENU_INVENTORY
				; (if(PrintCantDoThat($400))
				(gInv showSelf: gEgo)
			)
			; )
			(MENU_CHARACTER
				(for ( (= i 0)) (< i 5)  ( (++ i)) (if (> [gMissingBooks i] 0) (++ books)))
				(for ( (= i 0)) (< i 4)  ( (++ i)) (if (> [gArmor i] 0) (++ armor)))
				(Format
					@buffer
					{%s__________\nHealth__%u/%u\n>__%u__Strength\n>__%u__Agility\n>__%u__Luck\n>__%u__Intelligence\n>__%u__Defense\n\n%u Gold Piece(s)\n\n%u/%u Potion(s)\n%u Dart(s)\n%u/4 Armor Pieces\n%u/5 Missing Books\n%u/8 Planetary Marbles\n\nExperience____%u\nPuzzle Pts___%u/%u}
					@gName
					gHlth
					gMaxHlth
					gStr
					gAg
					gLuk
					gInt
					gDef
					gGold
					gFullFlask
					gFlask
					gApple
					armor
					books
					gMarbleNum
					gExp
					gScore
					gMaxScore
				)
				(= gWndColor 14) ; clYELLOW
				(= gWndBack 8) ; clGREY
				(if (> (gEgo x?) 160)
					(Print @buffer #at 8 5 #icon 904 0 0 #font 4)
				else
					(Print @buffer #at 170 5 #icon 904 0 0 #font 4)
				)
				(= gWndColor 0) ; clBLACK
				(= gWndBack 15) ; clWHITE
				(= books 0)
				(= armor 0)
			)
			(MENU_NOTES
				(= gVertButton 1)
				(while (> button 0)
					(= button
						(Print
							997
							11
							#title
							{Your Notes:}
							#button
							{ Images_}
							3
							#button
							{ Lost Books_}
							1
							#button
							{ Letters_}
							2
							#button
							{ Close_}
							0
							#icon
							271
						)
					)
					(switch button
						(3       ; ARTWORK
							(if [gArtwork 0]
								(Print {} #icon 550 4 0 #title {The Image:})
							)
							(if [gArtwork 1]
								(Print 70 19 #font 4 #icon 82 1 0 #title {The Image:})
							)
							(cond 
								([gArtwork 2] (Print {} #title {The Carving:} #icon 95))
								((not [gArtwork 0]) (Print 997 19))
							)
						)                         ; You have no artwork at this time.
						(1 (viewBooks)) ; BOOKS
						(2       ; LETTERS
							(if [gLetters 0]
								(Print 65 0 #title {It reads:} #width 280 #at -1 -1 #font 4)
							)
							(if [gArtwork 1]
								(Print 70 19 #font 4 #icon 82 1 0 #title {The Image:})
							)
							(if (and [g70Notes 0][g70Notes 1][g70Notes 2])
								(Print 70 50 #font 4)			
							else
								(if	[g70Notes 0] (Print 70 51 #font 4))
								(if	[g70Notes 1] (Print 70 52 #font 4))
								(if	[g70Notes 2] (Print 70 53 #font 4))
							)
							(if [gLetters 1] (Print 997 16 #font 4)) ; You have a sealed letter for Deborah from Jasper.
							(if [gLetters 2] (Print 997 17 #font 4)) ; You have a sealed letter for Sammy from Gallegos.
							(if
								(and
									(not [gLetters 0])
									(not [gLetters 1])
									(not [gLetters 2])
									(not [gArtwork 1])
									(not [g70Notes 0])
									(not [g70Notes 1])
									(not [g70Notes 2])
								)
								(Print 997 18)
							)
						)
					)
				)                             ; You have no letters at this time.
				(= gVertButton 0)
				(= button 4)
			)               ; set button to any number over 0 so that the while won't immediate close when opened a second time
			(MENU_CHANGESPEED
				(if (not gTimeCh)
					(= hGauge (Gauge new:))
					(= newSpeed
						(hGauge
							text: {Game Speed}
							description:
								{Use the mouse or the left and right arrow keys to select the game speed.}
							higher: {Faster}
							lower: {Slower}
							normal: NORMAL_SPEED
							doit: (- 15 gSpeed)
						)
					)
					(gGame setSpeed: (- 15 newSpeed))
					(DisposeScript GAUGE_SCRIPT)
				else
					(Print 997 10)
				)
			)
			(MENU_FASTERSPEED
				(if (not gTimeCh)
					(if gSpeed (gGame setSpeed: (-- gSpeed)))
				else
					(Print 997 10)
				)
			)
			(MENU_NORMALSPEED
				(if (not gTimeCh)
					(if gSpeed (gGame setSpeed: 12))
				else
					(Print 997 10)
				)
			)
			(MENU_SLOWERSPEED
				(if (not gTimeCh)
					(if (< gSpeed 15) (gGame setSpeed: (++ gSpeed)))
				else
					(Print 997 10)
				)
			)
			(MENU_VOLUME
				(= hGauge (Gauge new:))
				(= newVolume
					(hGauge
						text: {Sound Volume}
						description:
							{Use the mouse or the left and right arrow keys to adjust the volume.}
						higher: {Louder}
						lower: {Softer}
						normal: 15
						doit: (DoSound sndVOLUME newVolume)
					)
				)
				(DoSound sndVOLUME newVolume)
				(DisposeScript GAUGE_SCRIPT)
			)
			(MENU_TOGGLESOUND (ToggleSound))
		)
	)
)

(procedure (ToggleSound &tmp SOUND_OFF)
	(= SOUND_OFF (DoSound sndSET_SOUND))
	(= SOUND_OFF (DoSound sndSET_SOUND (not SOUND_OFF)))
	(if SOUND_OFF
		(SetMenu MENU_TOGGLESOUND smMENU_TEXT {Turn On})
	else
		(SetMenu MENU_TOGGLESOUND smMENU_TEXT {Turn Off})
	)
)