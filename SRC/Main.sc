;;; Sierra Script 1.0 - (do not remove this comment)
;
; Betrayed Alliance Main Script
; By Ryan Slattery
; ******************************************************************************
; main.sc
; Contains the game's main instance and inventory items.
(script# MAIN_SCRIPT)
(include sci.sh)
(include game.sh)
(use controls)
(use sound)
(use syswindow)
(use cycle)
(use game)
(use inv)
(use user)
(use menubar)
(use feature)
(use obj)
(use disposeload)

(public
	BA 0
	SetUpEgo 1
	SetUpActor 2
	ProgramControl 3
	PlayerControl 4
	DisposePrintDlg 5
	runProc 6
	PrintOK 7
	PrintItIs 8
	PrintGI 9
	PrintNCE 10
	PrintATI 11
	PrintDHI 12
	PrintCantDoThat 13
	AddViewToPic 14
	SetInvIOwner 15
	RunningCheck 16
	IsOwnedBy 17
	viewBooks 18
)

(local



; These are the global variables. You can access them from any script as long
; as it "use"es this script

; some variable of my own
; gDemo variables
; dIntFin = 0
;    dCasObj = 0
;    dDkRm = 1
; BETRAYED ALLIANCE VARIABLES
; + Player Character Stats +
	[gName 15] ; Once the name is determined by the player it can be used in texts using %s
	gAg =  15
	gStr =  15
	gLuk =  15
	gDef =  15
	gInt =  15
	gHlth =  30 ; 30 is start
	gMaxHlth =  30
	gExp =  60
; Collectables
	gGold =  10
	gApple =  0 ; Actually is for darts (haven't changed the name)
	gFlask =  0
	gFlowers =  0
	gFullFlask =  0
	[gArmor 4] = [0 0 0 0]         ; 4 pieces of Armor
	; [0] (rm 71  = helmet)     + 3 Int + 5 def
	; [1] (rm 135)= greaves     + 3 Ag  + 5 def
	; [2] (rm 28) = breastplate         + 10 Def
	; [3] (rm 46) = gauntlets   + 3 Str + 5 Def
	gArmorLoop = 277
; gMissingBooks[5] 0 when book is missing, 1 when found, 2 when given to library
	[gMissingBooks 5] = [0 0 0 0 0]
; [0] is New Notions of Lotions and Potions in rm 47
; [1] is The History of King Herman's Mustache rm 24
; [2] is Myths and Legends of Shelah           rm 18
; [3] is The Carmyle Family History            rm 23
; [4] is The Curse of the Relic                rm 70
	[gArtwork 3] = [0 0 0]
; [0] is the block-puzzle picture found in     rm 58
; [1] is the directions of the cave maze       rm 70
; [2] is the barrel directions				 rm 49
	[gLetters 3] = [0 0 0]
; [0] Letter to LT GYRE                        rm 65
; [1] Letter to Deborah                        rm 62
; [2] Letter to Gallegos                       rm 62
	gSun =  0   ; rm 18 wizard's room
	gMer =  0   ; rm 34 rocky path
	gVen =  0   ; rm 28 tavern
	gErth =  0  ; rm 18 wizard's room
	gMars =  0
	gJup =  0   ; rm 47 attic of bar
	gMarbleNum =  0 ; Since Saturn not in game, this is the total number of marbles
	gUra =  0   ; rm 62 dockhouse
	gNep =  0   ; rm 60 well
	gPlu =  0   ; rm 58 cavner of whispers
; Way Point Variables
	gWizRm =  0
	gFrst =  0  ; being randomly used if you wore a disguise around Lex
	gRuin =  0
	gGrave =  0
	gPond =  0
; Arcade and Puzzle Variables
	gTimeCh =  0    ; Used in the Menubar Script to disable player's speed customization
	gArcStl =  0    ; Used in the User Script to disable the Spacebar from retyping, allowing for other functions
	gMap =  0       ; Used in the User Script to disable gEgo movement when map is up
	gNoClick =  0   ; Used in the User Script to disable gEgo movement with click (only direction pad allowed.)
; BATTLE SCREEN VARIABLES
	gBatNum =  100  ; Changes to act as the view variable for the battle Script
	gOpHealth =  30 ; Opponent's health
	gEnNum          ; How many opponents will you fight?
	gSpdDmn =  0    ; When TRUE, opponent is twice as fast as normal
	gBruteStr =  0  ; When TRUE, opponent is twice as powerful
	gLifeDoub =  0  ; When TRUE, opponent has twice the life
	gDeadWeight =  0 ; When TRUE, opponent cannot attack
	gNoRun =  0     ; When TRUE, player cannot run away
	gRanAway =  0   ; Used to tell whether the Battle was won or just ran away from
	gDQ =  100      ; Used in Battle Script (103) as the incremental number of experience for "level up"
	gTrollHealth =  300

	gScore1 =  150  ; Variables used for the arcade high score board (script 107)
	gScore2 =  70
	gScore3 =  55
	gScore4 =  45
	gScore5 =  35
	gScoreName1
	gScoreName2
	gScoreName3
	gScoreName4
	gScoreName5
	;gNewScoreName
	
	[gArray 6]
	[gStepArray 9] ; filled with numbers in room 67
	[dirMap 4] = [3 7 5 1]
; OTHER VARIABLES OF MINE
	gDeathIconEnd =  0
	gHardMode =  0
	gRetry = 0
	gAutosave = 1
; If TRUE, harder parser commands and less hints
	gAnotherEgo =  0
	gTeleporting =  0
	gTimeOfDay =  1
	gTorchOut =  2500

	gVertButton =  0
	; gMatt = 0     /* If TRUE, Matt character will give hints in different areas */
	gFollowed =  0
; If TRUE, an emeny is following gEgo from across rooms
	gDartsWon =  0
; If greater than 3, sailor will not play darts with you anymore
	gWB =  900
; Specifies the View used for Window Buttons in the Control Script
	gWF =  901
; Specifies the View used for Window Frame in the Control Script
	[gFlowerGiven 8] = [0 0 0 0 0 0 0 0]
;
;    0 - Leah     !
;    1 - Bobby    !
;    2 - Debby    !
;    3 - Henry    !
;    4 - Hans
;    5 - Resting place of Sarah   !
;    6 - Colin
;    7 - Rose     !
	gListened = 71	; when 74 (listened to ghost) three times, death

	g20Entered =  0
	g23Kissed =  0
	g26Ladder =  0
	g26Intro =  0
	g27Passage =  0
	g30Signed =  0
	g30Flask =  0
	g31TrollGone =  0
	g31Guess =  1
	g31Gold =  0
	g37Statue =  0
	g37Complete =  0
	g38Solved =  0
	g40Sword =  0
	g41Coffin =  0
	g43Gold =  0
	g46Flask =  0
	g53OutFirstTime =  1
	g60Well =  0
	g62Letter =  0
	g62Package =  0
	g63PuzSol =  0
	[g65Grove 2] = [0 0] ; First 0 is for entering the grove message, the second is if Sarah is vanished
	g67_70dark =  0
	g67TableMoved = 0
	g70GotMap =  0
	g70Safe =  0
	[g70Notes 3] = [0 0 0]	; three notes found in crypt 
	g73Wash =  0
	g74Poster = 0
	g102Solved =  0
	g105Solved =  0
	[g107Solved 4] = [0 0 0 0] ; First 0 is for highest score, second is for second high score, third is for message after game, fourth is for ladder being shown in rm 26
	g110Solved =  0
	g135Darts =  0

	gEgo
; points to the ego's class
	gGame
; points to the game instance
	gRoom
; points to the current room instance
	gSpeed
; the game speed (delay each interpreter cycle)
	gQuitGame =  FALSE
; if set to TRUE, the game will exit
	gDemo =  0
; allows player to play the Demo Version
	gCast
; points to the cast class (list of actors)
	gRegions
; points to the regions class (list of regions)
	gLocales
; points to the locales class (list of locales)
	gTimers
; points to the timers class (list of timers)
	gSounds
; points to the sounds class (list of sounds)
	gInv
; points to the inventory class
	gAddToPics
; points to the add to pics class
	gFeatures
; points to the add to features class
	gSFeatures
; points to the add to sfeatures class
	gRoomNumberExit
; room number exit
	gPreviousRoomNumber
; the number of the previous room
	gRoomNumber
; the number of the current room
	gDebugOnExit =  FALSE
; enter debug mode on room exit
	gScore =  0
; the game score
	gMaxScore =  150
; the maximum game score
	gOldScore
; previous score
; score vaiables that take effect if gDemo is TRUE
	; gDemoScore = 0
	; gMaxDemoScore = 10
	; gOldDemoScore
	
	gCurrentCursor
; the number of the current cursor
	gNormalCursor =  999
; the number of the normal cursor (ie. arrow)
	gLoadingCursor =  997
; the number of the loading cursor (ie. hand)
	gTargetCursor =  995
; target cursor with 8,8 hotspot
	gDefaultFont =  6
; the number of the default font
	gSaveRestoreFont =  0
; the number of the font for the save/restore dialogs
	gDeadFont =  0
; the number of the font for the dialog when ego dies
	gUserEvent
; points to the user's event object
	gPrintDlg
; points to the current Print() window
	gVolume =  100
; the sound volume
	gVersion
; the version string
	gSaveDirPtr
; points to the save directory string
	gCheckAniWait
; the checkAni delay
	gSetRegions
; a flag -- see User:doit()
	gPicAngle
; the room's pic angle
	gOverlay =  -1
; whether to overlay the picture when drawing
	gDefaultPicAni
; the default pic animation
	gDefaultPalette
; the default palette to use for the pictures (0-3)
	gCastMotionCue
; if set, the cast's motionCue() is called
	gTheWindow
; points to the window class
	gWndColor
; the colour of the game's windows foreground (ie. text)
	gWndBack
; the colour of the game's windows background
	gOldPort
; the previous port
	gEgoView
; ego's current view number
; 85 for goggles view
	gEgoStoppedView =  903
; 907 for goggles view
	gDisguised =  0
	gEgoRunning =  0
	gRunClick =  0
; hh:mm:ss | gTimeHours:gTimeMinutes:gTimeSeconds
; the time elapsed since the game started
	gTimeSeconds
; the seconds
	gTimeMinutes
; the minutes
	gTimeHours
; the hours
	gCurrentTime
; the current time in seconds
	gTheMusic
; points to the music class
	gTheSoundFX
; points to the sound fx class
	gProgramControl
)
; states whether the program has control or the user

(instance BA of Game
	(properties)
	
	(method (init)
		; Set up the base window
		(= gTheWindow theWindow)
		(= gWndColor clBLACK)
		(= gWndBack clWHITE)
		(gTheWindow color: gWndColor back: gWndBack)
		; Initialize
		(super init:)
;
;         * Set your game version here *
		(= gVersion {1.0})
		(= gScoreName1 {Erasmus})
		(= gScoreName2 {Henry the Hermit})
		(= gScoreName3 {Roger Wilco})
		(= gScoreName4 {Rosella})
		(= gScoreName5 {Fenrus})
		
		; General initialization stuff
		(= gVolume 15)
		(DoSound sndVOLUME gVolume)
		(SL code: statusCode)
		(TheMenuBar init:)
		(scoreSound owner: self init:)
		(= gTheMusic theMusic)
		(gTheMusic owner: self init:)
		(= gTheSoundFX theSoundFX)
		(gTheSoundFX owner: self init:)
		(= gEgo ego)
		(User alterEgo: gEgo blocks: 0 y: 150)
		(Load rsFONT gDeadFont)
		(Load rsFONT gDefaultFont)
		(Load rsFONT gSaveRestoreFont)
		(Load rsCURSOR gNormalCursor)
		(Load rsCURSOR gLoadingCursor)
		(Load rsCURSOR gTargetCursor)
		(if (HaveMouse)
			(gGame setCursor: gNormalCursor SET_CURSOR_VISIBLE)
		else
			(gGame
				setCursor: gNormalCursor SET_CURSOR_VISIBLE 304 174
			)
		)
;
;         * Initialize the inventory with it's items here *
		(Inv
			add:
				Nothing
				survival_kit    ; 1
				shovel          ; 2
				metal_bar       ; 3
				kite            ; 4
				meat            ; 5
				letter          ; 6 ends up in room 28
				map             ; 7
				blow_dart_gun   ; 8
				marbles         ; 9
				goggles         ; 10
				magnet          ; 11
				explosive       ; 12 ends up in Room 36
				block           ; 13 ends up in Room 102
				ruler           ; 14
				bow             ; 15
				arrow           ; 16
				acorn           ; 17
				ring            ; 18
				package         ; 19
				books           ; 20
				flower          ; 21
				potion          ; 22
				titanite        ; 23
				message         ; 24
				glider			; 25
				key				; 26
				armor 			; 27
		)                       
		; Start the room
		(if (GameIsRestarting)
			(self newRoom: TITLESCREEN_SCRIPT)
		else
			(self newRoom: TITLESCREEN_SCRIPT)
		)
	)
	
	(method (doit)
		(super doit:)
		(cond 
			((gEgo isStopped:)            ; changes ego to standing still view
				(if (not gAnotherEgo)
					(gEgo
						view: gEgoStoppedView
						loop: (gEgo loop?)
						cel: 0
						setMotion: NULL
					)
				)
			)
			((not gAnotherEgo)
				(if gEgoRunning
					(gEgo view: 230)
				else
					(gEgo view: gEgoView)
				)
			)
		)                                     ; changes ego back to walking view
		(if gProgramControl
			(User canControl: FALSE canInput: FALSE)
		)
		; (send gEgo:ignoreControl(ctlWHITE))
		(if
			(!=
				gCurrentTime
				(= gCurrentTime (GetTime gtTIME_OF_DAY))
			)
			(if (>= (++ gTimeSeconds) 60)
				(= gTimeSeconds 0)
				(++ gTimeMinutes)
				(if (>= gTimeMinutes 60)
					(= gTimeMinutes 0)
					(++ gTimeHours)
				)
			)
		)
		(if (> gOldScore gScore)
			(= gOldScore gScore)
			(SL doit:)
		)
		(if (< gOldScore gScore)
			(= gOldScore gScore)
			(SL doit:)
		)
	)
	
	(method (replay)
		(TheMenuBar draw:)
		(SL enable:)
		; (if(DoSound(sndSET_SOUND))
; 		    SetMenu(MENU_TOGGLESOUND #text "Turn Off")
; 		)(else
; 		    SetMenu(MENU_TOGGLESOUND #text "Turn On")
; 		)
		(super replay:)
	)
	
	(method (newRoom roomNum picAni)
		(DisposePrintDlg)
		(Load rsFONT gDeadFont)
		(Load rsFONT gDefaultFont)
		(Load rsFONT gSaveRestoreFont)
		(Load rsCURSOR gNormalCursor)
		(Load rsCURSOR gLoadingCursor)
		(Load rsCURSOR gTargetCursor)
		(super newRoom: roomNum)
		(if (< argc 2)
			(= gDefaultPicAni (Random 0 5))
		else
			(= gDefaultPicAni picAni)
		)
		; autosave
		(if gAutosave
			(if
				(and
					(< gRoomNumber 136)
					(> gRoomNumber 10)
					(not (== gRoomNumber 103))
				)
				(SaveGame (gGame name?) 0 {Autosave} gVersion)
			)
		)
	)
	
	(method (startRoom roomNum)
		(DisposeLoad
			NULL
			FILEIO_SCRIPT
			JUMP_SCRIPT
			EXTRA_SCRIPT
			WINDOW_SCRIPT
			TIMER_SCRIPT
			FOLLOW_SCRIPT
			REV_SCRIPT
			DCICON_SCRIPT
			DOOR_SCRIPT
			AUTODOOR_SCRIPT
			WANDER_SCRIPT
			973
		)
		(DisposeScript DISPOSELOAD_SCRIPT)
		(if gDebugOnExit (= gDebugOnExit FALSE) (SetDebug))
		(gTheSoundFX stop: number: 1)
		(super startRoom: roomNum)
		(if (== gTheSoundFX 11) (gEgo baseSetter: NormalBase))
		
	)
	
	(method (changeScore addScore)
		(= gScore (+ gScore addScore))
	)
	
	(method (handleEvent pEvent &tmp i)
		; troflip debugging addition, For use in combination with the ALT key
		(if (== evKEYBOARD (pEvent type?))
			(switch (pEvent message?)
				($2f00 (Show 1))     ; alt-v -> Show visual screen
				($2e00 (Show 4))     ; alt-c -> Show control screen
				($1900 (Show 2))     ; alt-p -> Show priority screen
				($1400
					(gRoom newRoom: (GetNumber {Room Number?}))
				)                                                            ; alt-t -> teleport to room
				($1700
					(gEgo get: (GetNumber {Which Item?}))
				)                                                      ; alt-i -> get inventory
				($1f00
					(gCast eachElementDo: #showSelf)
				)                                                  ; alt-s -> Show cast
				($3200
					(ShowFree)         ; alt-m -> Show memory usage
					(FormatPrint
						{Free Heap: %u Bytes\nLargest ptr: %u Bytes\nFreeHunk: %u KBytes\nLargest hunk: %u Bytes}
						(MemoryInfo miFREEHEAP)
						(MemoryInfo miLARGESTPTR)
						(>> (MemoryInfo miFREEHUNK) 6)
						(MemoryInfo miLARGESTHUNK)
					)
				)
			)
		)             ; end formatprint ; end case $3200 ; end switch ; end if keyboard event
		; and now back to the normal script, You may want to delete all this bit upon release!*/
		(super handleEvent: pEvent)
		(if
		(or (!= (pEvent type?) evSAID) (pEvent claimed?))
			(return TRUE)
		)
		;(if (Said 'look/kit') (PrintOK))
		(if (Said 'look>')
			(cond
				((= i (gInv saidMe:))
					(if (i ownedBy: gEgo)
					(i showSelf:)
					else
						(PrintDHI)
					)
				)
				
			)
		)
		
		(if (Said 'give>')
			(cond
				((= i (gInv saidMe:))
					(if (i ownedBy: gEgo)
						(Print 0 103)
					else
						(PrintDHI)
					)
				)
			)
		)
		
		(if (Said 'hi') (Print 0 40))
		(if (Said 'bye') (Print 0 34))
		(if (Said 'drink/water') (Print 0 33))
		(if (Said 'use,drink/potion[<healing]')
			(if (> gFullFlask 0)
				(if (< gHlth gMaxHlth)
					(-- gFullFlask)
					(= gHlth gMaxHlth)
					(Print 0 22)
				else
					(Print 0 24)
				)
			else
				(Print 0 23)
			)
		)
		(if (Said 'run')
			(if (not gEgoRunning)
				(if gDisguised
					(Print 0 81)
					(= gDisguised 0)
					(RunningCheck)
				)
				(gEgo view: 230 xStep: 5 yStep: 4)
				(= gEgoRunning 1)
				(= [gArray 0] (pEvent x?))
				(= [gArray 1] (pEvent y?))
				(runProc)
			else
				(Print 0 39)
			)
		)
		(if (Said 'walk')
			(if (not gEgoRunning)
				(Print 0 39)
			else
				(gEgo view: 0 xStep: 3 yStep: 2)
				(= gEgoRunning 0)
				(= [gArray 0] (pEvent x?))
				(= [gArray 1] (pEvent y?))
				(runProc)
			)
		)
		(if (Said '(take<off),remove/goggles,costume')
			(if gDisguised
				(Print 0 82)
				(= gDisguised 0)
				(RunningCheck)
			else
				(Print 0 83)
			)
		)
		(if (Said 'sneak') (Print 0 72))
		(if (Said 'rest') (Print 0 73))
		(if (Said 'knock') (Print 0 109))
		(if (Said 'climb') (Print 0 76))
		(if
			(or
				(Said 'search/body')
				(Said 'take,(pick<up)/rock')
				(Said 'fight')
			)
			(Print 0 77)
		)
		(if (Said 'open/barrel,door') (Print 0 77))
		(if (Said 'thank') (Print 0 38))
		(if (Said 'eat') (Print 0 37))
		(if (Said 'drink') (Print 0 106))
		(if (Said 'listen') (Print 0 89))
		(if (Said 'rub,feel,hold/marble,sun,shooter')
			(if (gEgo has: 9) (Print 0 94) else (PrintDHI))
		)
		(if (Said 'inventory') (gInv showSelf: gEgo))
		(if (Said 'talk[/!*]') (Print 0 107))
		(if (Said 'talk/*') (Print 0 108))
		(if (Said 'take/*') (Print 0 19))
		(if (Said 'smell/*') (Print 0 101))
		(if (Said 'feel/[*]') (Print 0 105))
		(if (Said 'kiss/*') (Print 0 112))
		(if (Said 'throw/*') (PrintCantDoThat))
		(if (Said 'move,push/*') (Print 0 113))
		(if (Said 'smell') (Print 0 102))
		(if (Said '(put<on),wear/armor,greaves,helmet,breastplate,gauntlet') (Print {You are.}))
		(if (Said 'razzle/beer<root') (Print 0 111))
		(if (Said 'use/twine,waterskin,knife,metal')
			(Print 204 1)
		)
		(if (Said 'view,read>')
			(if (Said '/letter')
				(if (or (gEgo has: 6) (if g62Letter))
					(Print 0 69)
				else
					(Print 0 70)
				)
			)
			(if (Said '/book') (Print 0 71))
		)
		
		(if (Said 'look/*') (Print 0 74))
		(if (Said 'search/*') (Print 0 114))
		(return FALSE)
	)
	
	(method (wordFail)
		(Print 0 54)
	)
	
	(method (syntaxFail)
		(Print 0 (Random 55 57))
	)
	
	(method (pragmaFail)
		(Print 0 (Random 57 60))
	)
)


(class Iitem of InvI
	(properties
		said 0
		description 0
		owner 0
		view 0
		loop 0
		cel 0
		script 0
		count 0
	)
	
	(method (showSelf &tmp [buffer 50])
		(if (> count 0)
			(if (== view 277) (= loop gFullFlask)
				(Format @buffer description count)
				(Print @buffer #title objectName #icon view gArmorLoop cel)
			else
				(Format @buffer description count)
				(Print @buffer #title objectName #icon view loop cel)
			)
		else
			(if (== view 274) (= loop gFullFlask)) ; if flask
			(Print
				0
				description
				#title
				objectName
				#icon
				view
				loop
				cel
			)
		)
	)
)

; (SetCursor(description HaveMouse()))

(instance statusCode of Code
	(properties)
	
	(method (doit param1)
		(Format
			param1
			{___$etrayed #lliance: $ook I____[score: %d of %-3d]}
			gScore
			gMaxScore
		)
	)
)


(instance ego of Ego
	(properties
		y 1111
		x 0
		z 0
		heading 0
		yStep 2
		view 0
		loop 0
		cel 0
		priority 0
		underBits 0
		signal $2000
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		lsTop 0
		lsLeft 0
		lsBottom 0
		lsRight 0
		brTop 0
		brLeft 0
		brBottom 0
		brRight 0
		cycleSpeed 0
		script 0
		cycler 0
		timer 0
		illegalBits $8000
		xLast 0
		yLast 0
		xStep 3
		moveSpeed 0
		blocks 0
		baseSetter 0
		mover 0
		looper 0
		viewer 0
		avoider 0
		edgeHit 0
	)
)


(instance scoreSound of Sound
	(properties
		state 0
		number SCORE_SOUND
		priority 10
		loop 1
		handle 0
		signal 0
		prevSignal 0
		client 0
		owner 0
	)
)


(instance theMusic of Sound
	(properties
		state 0
		number 1
		priority 0
		loop 1
		handle 0
		signal 0
		prevSignal 0
		client 0
		owner 0
	)
)


(instance theSoundFX of Sound
	(properties
		state 0
		number 1
		priority 5
		loop 1
		handle 0
		signal 0
		prevSignal 0
		client 0
		owner 0
	)
)


(instance theWindow of SysWindow
	(properties)
	
	(method (open)
		(if (< (Graph grGET_COLOURS) 9)
			(if (or (< color 7) (== color 8))
				(= color 0)
				(= back 15)
			else
				(= color 15)
				(= back 0)
			)
		)
		(super open:)
	)
)


(instance NormalBase of Code
	(properties)
	
	(method (doit &tmp temp0)
		(if (== gRoomNumberExit 253)
			(= temp0 22)
		else
			(= temp0 10)
		)
		(gEgo brBottom: (+ (gEgo y?) 1))
		(gEgo brTop: (- (gEgo brBottom?) (gEgo yStep?)))
		(gEgo brLeft: (- (gEgo x?) temp0))
		(gEgo brRight: (+ (gEgo x?) temp0))
	)
)

;
; * THE PUBLIC PROCEDURES
(procedure (SetUpEgo theLoop theView)
	(PlayerControl)
	(= gDisguised 0)
	(gEgo edgeHit: EDGE_NONE)
	(switch argc
		(0
			(SetUpActor gEgo (gEgo loop?) gEgoView)
		)
		(1
			(SetUpActor gEgo theLoop gEgoView)
		)
		(2
			(SetUpActor gEgo theLoop theView)
		)
	)
)


(procedure (SetUpActor pActor theLoop theView)
	(if (> argc 1) (pActor loop: theLoop))
	(if (> argc 2) (pActor view: theView))
	(pActor
		setLoop: -1
		setPri: -1
		setStep: 3 2
		setCycle: Walk
		illegalBits: $8000
		cycleSpeed: 0
		moveSpeed: 0
		ignoreActors: 0
	)
)


(procedure (ProgramControl)
	(User canControl: FALSE canInput: FALSE)
	(= gProgramControl 1)
	(gEgo setMotion: NULL ignoreControl: ctlWHITE)
)


(procedure (PlayerControl)
	(User canControl: TRUE canInput: TRUE)
	(= gProgramControl 0)
	(gEgo setMotion: NULL observeControl: ctlWHITE)
)


(procedure (DisposePrintDlg)
	(if gPrintDlg (gPrintDlg dispose:))
)

(procedure (runProc)
	(if (not (gEgo isStopped:))
		(if gRunClick
			(gEgo setMotion: MoveTo [gArray 0] [gArray 1])
		else
			(gEgo setDirection: [dirMap (gEgo loop?)])
		)
		(= [gArray 0] 0)
		(= [gArray 1] 0)
	)
)


(procedure (PrintOK)
	(Print 0 61)
)

; O.K.

(procedure (PrintItIs)
	(Print 0 62)
)

; It is.

(procedure (PrintGI)        ; good idea
	(Print 0 25)
)


(procedure (PrintNCE)        ; not close enough
	(Print 0 26)
)


(procedure (PrintATI)        ; already took it
	(Print 0 27)
)


(procedure (PrintDHI)        ; don't have it
	(Print 0 28)
)


(procedure (PrintCantDoThat mem)
	(if (> (MemoryInfo miLARGESTPTR) mem)
		(return TRUE)
	else
		(Print 0 29)
		(return FALSE)
	)
)


(procedure (AddViewToPic pView &tmp hView)
	(if pView
		(= hView (View new:))
		(hView
			view: (pView view?)
			loop: (pView loop?)
			cel: (pView cel?)
			priority: (pView priority?)
			posn: (pView x?) (pView y?)
			addToPic:
		)
		(pView posn: (pView x?) (+ 1000 (pView y?)))
	)
)


(procedure (SetInvIOwner index owner &tmp hInvI)
	(= hInvI (gInv at: index))
	(if (< argc 2)
		(hInvI owner: gRoomNumberExit)
	else
		(hInvI owner: owner)
	)
)

(procedure (RunningCheck)
	(if gEgoRunning
		(gEgo view: 230 xStep: 5 yStep: 4)
	else
		(if gDisguised
			(= gEgoView 85)
			(= gEgoStoppedView 907)
		else
			(= gEgoView 0)
			(= gEgoStoppedView 903)
		)
		(gEgo view: gEgoView xStep: 3 yStep: 2)
	)
)

(procedure (IsOwnedBy invItem roomOrActor &tmp checkObject)
	(= checkObject (gInv at: invItem))
	(if (IsObject checkObject)
		(return (checkObject ownedBy: roomOrActor))
	else
		(return 0)
	)
)

(procedure (viewBooks &tmp i)
	(Print 0 31 #at -1 20)
	(= gDefaultFont 4)
	(= gWF 911)
	(for ( (= i 0)) (< i 5)  ( (++ i)) (if (> [gMissingBooks i] 0)
		(switch i
			(0 (Print 0 63 #width 280))
			(1 (Print 0 64 #width 280))
			(2
				(Print 0 65 #width 280)
				(Print 0 66 #width 280)
			)
			(3
				(Print 0 67 #width 280)
				(Print 0 100 #width 280)
			)
			(4 (Print 0 68 #width 280))
		)
	))
	(= gWF 901)
	(= gDefaultFont 6)
)

;
; * THE INVENTORY ITEMS                                                        *
(instance Nothing of Iitem
	(properties)
)


(instance survival_kit of Iitem
	(properties
		said '/kit,knife,twine,metal,waterskin'
		description 1
		owner 0
		view 206
		script 0
		name "survival kit"
	)
)

(instance shovel of Iitem
	(properties
		said '/shovel'
		description 2
		owner 0
		view 208
		script 0
	)
)

(instance metal_bar of Iitem
	(properties
		said '/metal bar'
		description 3
		owner 0
		view 252
		script 0
		name "metal bar"
	)
)

(instance kite of Iitem
	(properties
		said '/kite'
		description 4
		owner 0
		view 255
		script 0
	)
)

(instance meat of Iitem
	(properties
		said '/meat'
		description 5
		owner 0
		view 257
		script 0
	)
)

(instance letter of Iitem
	(properties
		said '/letter'
		description 6
		owner 0
		view 258
		script 0
	)
)

(instance map of Iitem
	(properties
		said '/map'
		description 7
		owner 0
		view 259
		script 0
	)
)

(instance blow_dart_gun of Iitem
	(properties
		said '/gun'
		description 8
		owner 0
		view 260
		script 0
		name "blow dart gun"
	)
)

(instance marbles of Iitem
	(properties
		said '/marbles'
		description {Marbles that act upon each other with solar system-like gravitational forces. You have %u.} ; 9
		owner 0
		view 261
		script 0
		count 2
	)
)

(instance goggles of Iitem
	(properties
		said '/goggles'
		description 10
		owner 0
		view 262
		script 0
	)
)

(instance magnet of Iitem
	(properties
		said '/cage'
		description 11
		owner 0
		view 263
		script 0
	)
)

(instance explosive of Iitem
	(properties
		said '/explosive'
		description 12
		owner 0
		view 264
		script 0
	)
)

(instance block of Iitem
	(properties
		said '/block'
		description 13
		owner 0
		view 265
		script 0
	)
)

(instance ruler of Iitem
	(properties
		said '/ruler'
		description 14
		owner 0
		view 266
		script 0
	)
)

(instance bow of Iitem
	(properties
		said '/bow'
		description 15
		owner 0
		view 267
		script 0
	)
)

(instance arrow of Iitem
	(properties
		said '/arrow'
		description 16
		owner 0
		view 268
		script 0
	)
)

(instance acorn of Iitem
	(properties
		said '/acorn'
		description 17
		owner 0
		view 269
		script 0
	)
)

(instance ring of Iitem
	(properties
		said '/ring'
		description 18
		owner 0
		view 209
		script 0
	)
)

(instance package of Iitem
	(properties
		said '/package'
		description 35
		owner 0
		view 270
		script 0
	)
)

(instance books of Iitem
	(properties
		said '/book'
		description 36
		owner 0
		view 271
		script 0
	)
)

(instance flower of Iitem
	(properties
		said '/flower'
		description {Heliopsis Splendor, a fragrant flower you found near a skull-shaped well. You have %u.} ; 75
		owner 0
		view 272
		script 0
		count 2
	)
)

(instance potion of Iitem
	(properties
		said '/potion,flask'
		description 78
		owner 0
		view 274
		script 0
	)
)

(instance titanite of Iitem
	(properties
		said '/titanite'
		description 90
		owner 0
		view 210
		script 0
	)
)

(instance message of Iitem
	(properties
		said '/letter'
		description 93
		owner 0
		view 276
		script 0
	)
)

(instance glider of Iitem
	(properties
		said '/glider'
		description 96
		owner 0
		view 278
		script 0
	)
)
(instance key of Iitem
	(properties
		said '/key'
		description 104
		owner 0
		view 214
		script 0
	)
)
(instance armor of Iitem
	(properties
		said '/armor'
		description {Pieces of armor that increase your combat defense.\n\nYou have %u of 4.} ; 75
		owner 0
		view 277
		script 0
		count 0
	)
)