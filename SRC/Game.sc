;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; game.sc
; Contains the general game functions, including the game handler class, 
; region/room classes and statusbar class.
(script# GAME_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use controls)
(use sound)
(use syswindow)
(use cycle)
(use inv)
(use user)
(use obj)





(instance cast of EventHandler
	(properties)
)


(instance features of EventHandler
	(properties)
)


(instance sFeatures of EventHandler
	(properties)
	
	(method (delete param1)
		(super delete: param1)
	)
)


(instance sounds of EventHandler
	(properties)
)


(instance regions of EventHandler
	(properties)
)


(instance locales of EventHandler
	(properties)
)


(instance addToPics of EventHandler
	(properties)
	
	(method (doit)
		(AddToPic elements)
	)
)


(instance _controls of Controls
	(properties)
)


(instance timers of Set
	(properties)
)


(class Game of Obj
	(properties
		script 0
	)
	
	(method (init)
		((= gCast cast) add:)
		((= gFeatures features) add:)
		((= gSFeatures sFeatures) add:)
		((= gSounds sounds) add:)
		((= gRegions regions) add:)
		((= gLocales locales) add:)
		((= gAddToPics addToPics) add:)
		((= gTimers timers) add:)
		(= gSaveDirPtr (GetSaveDir))
		(Inv init:)
		(User init:)
	)
	
	(method (doit)
		(gSounds eachElementDo: #check)
		(gTimers eachElementDo: #doit)
		(if gPrintDlg (gPrintDlg check:))
		(Animate (gCast elements?) 1)
		(if gCastMotionCue
			(= gCastMotionCue FALSE)
			(gCast eachElementDo: #motionCue)
		)
		(if script (script doit:))
		(gRegions eachElementDo: #doit)
		(if (== gRoomNumber gRoomNumberExit) (User doit:))
		(if (!= gRoomNumber gRoomNumberExit)
			(self newRoom: gRoomNumber)
		)
		(gTimers eachElementDo: #delete)
		(return (GameIsRestarting FALSE))
	)
	
	(method (showSelf)
		(gRegions showSelf:)
	)
	
	(method (play)
		(= gGame self)
		(= gSaveDirPtr (GetSaveDir))
		(if (not (GameIsRestarting)) (GetCWD gSaveDirPtr))
		(self setCursor: gLoadingCursor 1)
		(self init:)
		(self setCursor: gNormalCursor (HaveMouse))
		(while (not gQuitGame)
			(self doit:)
			(Wait gSpeed)
		)
	)
	
	(method (replay)
		(if gUserEvent (gUserEvent dispose:))
		(gSFeatures release:)
		(if gPrintDlg (gPrintDlg dispose:))
		(gCast eachElementDo: #perform RU)
		(gGame setCursor: gLoadingCursor 1)
		(DrawPic (gRoom curPic?) 100 dpCLEAR gDefaultPalette)
		(if (!= gOverlay -1)
			(DrawPic gOverlay 100 dpNO_CLEAR gDefaultPalette)
		)
		(if (gRoom controls?) ((gRoom controls?) draw:))
		(gAddToPics doit:)
		(gGame setCursor: gNormalCursor (HaveMouse))
		(SL doit:)
		(DoSound sndRESUME)
		(Sound pause: 0)
		(while (not gQuitGame)
			(self doit:)
			(Wait gSpeed)
		)
	)
	
	(method (newRoom newRoomNum &tmp oldCursor hEvent)
		(gAddToPics dispose:)
		(gFeatures eachElementDo: #dispose release:)
		(gCast eachElementDo: #dispose eachElementDo: #delete)
		(gTimers eachElementDo: #delete)
		(gRegions eachElementDo: #perform DNKR release:)
		(gLocales eachElementDo: #dispose release:)
		(Animate 0)
		(= gPreviousRoomNumber gRoomNumberExit)
		(= gRoomNumberExit newRoomNum)
		(= gRoomNumber newRoomNum)
		(FlushResources newRoomNum)
		(= oldCursor (self setCursor: gLoadingCursor 1))
		(self
			startRoom: gRoomNumberExit
			checkAni:
			setCursor: oldCursor (HaveMouse)
		)
		(SetSynonyms gRegions)
		(while ((= hEvent (Event new: evMOUSE)) type?)
			(hEvent dispose:)
		)
		(hEvent dispose:)
	)
	
	(method (startRoom roomNum)
		(if gDebugOnExit (SetDebug))
		(gRegions addToFront: (= gRoom (ScriptID roomNum)))
		(gRoom init:)
	)
	
	(method (restart)
		(if gPrintDlg (gPrintDlg dispose:))
		(RestartGame)
	)
	
	(method (restore &tmp gameNum oldCursor hSound)
		(= gameNum  -1)
		(Load rsFONT gSaveRestoreFont)
		(Load rsCURSOR gNormalCursor)
		(Load rsCURSOR gLoadingCursor)
		(= oldCursor (self setCursor: gNormalCursor))
		(= hSound (Sound pause: 1))
		(if (GetSaveDisk TRUE)
			(if gPrintDlg (gPrintDlg dispose:))
			(if gRetry
            	(= gameNum 0)
            else
				(= gameNum (Restore doit: &rest))
			)
			(if (!= gameNum -1)
				(self setCursor: gLoadingCursor)
				(if (CheckSaveGame objectName gameNum gVersion)
					(gCast eachElementDo: #dispose)
					(gCast eachElementDo: #delete)
					(RestoreGame objectName gameNum gVersion)
				else
					(Print
						{That game was saved under a different interpreter. It cannot be restored.}
						#font
						0
						#button
						{OK}
						1
					)
					(self setCursor: oldCursor (HaveMouse))
					(= gameNum -1)
				)
			)
			(GetSaveDisk FALSE)
		)
		(Sound pause: hSound)
		(return gameNum)
	)
	
	(method (save &tmp [strDescBuf 20] gameNum oldCursor hSound)
		(Load rsFONT gSaveRestoreFont)
		(Load rsCURSOR gLoadingCursor)
		(= oldCursor (self setCursor: gNormalCursor))
		(= hSound (Sound pause: 1))
		(if (GetSaveDisk TRUE)
			(if gPrintDlg (gPrintDlg dispose:))
			(= gameNum (Save doit: @strDescBuf))
			(if (!= gameNum -1)
				(= oldCursor (self setCursor: gLoadingCursor 1))
				(if
				(not (SaveGame objectName gameNum @strDescBuf gVersion))
					(Print
						{Your save game disk is full. You must either use another disk or save over an existing saved game.}
						#font
						0
						#button
						{OK}
						1
					)
				)
				(self setCursor: oldCursor (HaveMouse))
			)
			(GetSaveDisk FALSE)
		)
		(Sound pause: hSound)
	)
	
	(method (changeScore addPoints)
		(= gScore (+ gScore addPoints))
		(SL doit:)
	)
	
	(method (handleEvent pEvent)
		(if
			(or
				(gRegions handleEvent: pEvent)
				(gLocales handleEvent: pEvent)
			)
			(return (pEvent claimed?))
		)
		(if script (script handleEvent: pEvent))
		(return (pEvent claimed?))
	)
	
	(method (showMem)
		(FormatPrint
			{Free Heap: %u Bytes\nLargest ptr: %u Bytes\nFreeHunk: %u KBytes\nLargest hunk: %u Bytes}
			(MemoryInfo miFREEHEAP)
			(MemoryInfo miLARGESTPTR)
			(>> (MemoryInfo miFREEHUNK) 6)
			(MemoryInfo miLARGESTHUNK)
		)
	)
	
	(method (setSpeed newSpeed &tmp oldSpeed)
		(= oldSpeed gSpeed)
		(= gSpeed newSpeed)
		(return oldSpeed)
	)
	
	(method (setCursor csrNumber &tmp oldCursor)
		(= oldCursor gCurrentCursor)
		(= gCurrentCursor csrNumber)
		(SetCursor csrNumber &rest)
		(return oldCursor)
	)
	
	(method (checkAni &tmp hCastMember)
		(Animate (gCast elements?) 0)
		(Wait 0)
		(Animate (gCast elements?) 0)
		(while (> (Wait 0) gCheckAniWait)
			(= hCastMember (gCast firstTrue: #isExtra))
			(breakif (== hCastMember NULL))
			(hCastMember addToPic:)
			(Animate (gCast elements?) 0)
			(gCast eachElementDo: #delete)
		)
	)
	
	(method (notify)
	)
	
	(method (setScript newScript)
		(if script (script dispose:))
		(if newScript (newScript init: self &rest))
	)
	
	(method (cue)
		(if script (script cue:))
	)
	
	(method (wordFail aWord &tmp [temps 100])
		(FormatPrint {I don't understand "%s".} aWord)
		(return FALSE)
	)
	
	(method (syntaxFail)
		(Print 994 0)
	)
	
; That doesn't appear to be a proper sentence.
	(method (semanticFail)
		(Print 994 1)
	)
	
; That sentence doesn't make sense.
	(method (pragmaFail)
		(Print 994 2)
	)
)

; You've left me responseless.

(class Rgn of Obj
	(properties
		script 0
		number 0
		timer 0
		keep 0
		initialized FALSE
	)
	
	(method (init)
		(if (not initialized)
			(= initialized TRUE)
			(if (not (gRegions contains: self))
				(gRegions addToEnd: self)
			)
			(super init:)
		)
	)
	
	(method (doit)
		(if script (script doit:))
	)
	
	(method (dispose)
		(gRegions delete: self)
		(if (IsObject script) (script dispose:))
		(if (IsObject timer) (timer dispose:))
		(gSounds eachElementDo: #clean self)
		(DisposeScript number)
	)
	
	(method (handleEvent pEvent)
		(if script (script handleEvent: pEvent))
		(return (pEvent claimed?))
	)
	
	(method (setScript newScript)
		(if (IsObject script) (script dispose:))
		(if newScript (newScript init: self &rest))
	)
	
	(method (cue)
		(if script (script cue:))
	)
	
	(method (newRoom)
	)
	
	(method (notify)
	)
)


(class Rm of Rgn
	(properties
		script 0
		number 0
		timer 0
		keep 0
		initialized 0
		picture 0
		style $ffff
		horizon 0
		controls 0
		north 0
		east 0
		south 0
		west 0
		curPic 0
		picAngle 0
		vanishingX 160
		vanishingY 35536
	)
	
	(method (init)
		(= number gRoomNumberExit)
		(= controls _controls)
		(= gPicAngle picAngle)
		(if picture (self drawPic: picture))
		(switch ((User alterEgo?) edgeHit?)
			(EDGE_TOP
				((User alterEgo?) y: 185)
			)
			(EDGE_LEFT
				((User alterEgo?)
					x: (- 315 ((User alterEgo?) xStep?))
				)
			)
			(EDGE_BOTTOM
				((User alterEgo?)
					y: (+ horizon ((User alterEgo?) yStep?))
				)
			)
			(EDGE_RIGHT
				((User alterEgo?) x: 4)
			)
		)
		((User alterEgo?) edgeHit: EDGE_NONE)
	)
	
	(method (doit &tmp newRoomDir)
		(if script (script doit:))
		(switch ((User alterEgo?) edgeHit?)
			(EDGE_TOP (= newRoomDir north))
			(EDGE_RIGHT (= newRoomDir east))
			(EDGE_BOTTOM
				(= newRoomDir south)
			)
			(EDGE_LEFT (= newRoomDir west))
			(else  (return))
		)
		(if newRoomDir (self newRoom: newRoomDir))
	)
	
	(method (dispose)
		(if controls (controls dispose:))
		(super dispose:)
	)
	
	(method (handleEvent pEvent)
		(if (or (super handleEvent: pEvent) controls)
			(controls handleEvent: pEvent)
		)
		(return (pEvent claimed?))
	)
	
	(method (newRoom newRoomNum)
		(gRegions
			delete: self
			eachElementDo: #newRoom newRoomNum
			addToFront: self
		)
		(= gRoomNumber newRoomNum)
		(super newRoom: newRoomNum)
	)
	
	(method (setRegions scriptNumbers &tmp i scrNum hScript)
		(for ( (= i 0)) (< i argc)  ( (++ i)) (= scrNum [scriptNumbers i]) (= hScript (ScriptID scrNum)) (hScript number: scrNum) (gRegions add: hScript) (if (not (hScript initialized?)) (hScript init:)))
	)
	
	(method (setFeatures features &tmp i)
		(for ( (= i 0)) (< i argc)  ( (++ i)) (gFeatures add: [features i]))
	)
	
	(method (setLocales scriptNumbers &tmp i scrNum hScript)
		(for ( (= i 0)) (< i argc)  ( (++ i)) (= scrNum [scriptNumbers i]) (= hScript (ScriptID scrNum)) (hScript number: scrNum) (gLocales add: hScript) (hScript init:))
	)
	
	(method (drawPic picNum picAni)
		(addToPics dispose:)
		(= curPic picNum)
		(= gOverlay -1)
		(cond 
			((== argc 2) (DrawPic picNum picAni dpCLEAR gDefaultPalette))
			((!= style -1) (DrawPic picNum style dpCLEAR gDefaultPalette))
			(else (DrawPic picNum gDefaultPicAni dpCLEAR gDefaultPalette))
		)
	)
	
	(method (overlay picNum picAni)
		(= gOverlay picNum)
		(cond 
			((== argc 2) (DrawPic picNum picAni dpNO_CLEAR gDefaultPalette))
			((!= style -1) (DrawPic picNum style dpNO_CLEAR gDefaultPalette))
			(else (DrawPic picNum gDefaultPicAni dpNO_CLEAR gDefaultPalette))
		)
	)
)


(class Locale of Obj
	(properties
		number 0
	)
	
	(method (dispose)
		(gLocales delete: self)
		(DisposeScript number)
	)
	
	(method (handleEvent pEvent)
		(return (pEvent claimed?))
	)
)


(class SL of Obj
	(properties
		state DISABLED
		code NULL
	)
	
	(method (doit &tmp [strBuf 41])
		(if code
			(code doit: @strBuf)
			(if state (DrawStatus @strBuf) else (DrawStatus FALSE))
		)
	)
	
	(method (enable)
		(= state ENABLED)
		(self doit:)
	)
	
	(method (disable)
		(= state DISABLED)
		(self doit:)
	)
)


(instance RU of Code
	(properties)
	
	(method (doit param1 &tmp newSignal)
		(if (param1 underBits?)
			(= newSignal (param1 signal?))
			(= newSignal (| newSignal 1))
			(= newSignal (& newSignal $fffb))
			(param1 underBits: 0 signal: newSignal)
		)
	)
)


(instance DNKR of Code
	(properties)
	
	(method (doit param1)
		(if (not (param1 keep?)) (param1 dispose:))
	)
)


(procedure (GetSaveDisk fSAVEDISK &tmp btnPressed [temp1 40] [temp41 40] [temp81 40])
	(= btnPressed 1) ; btnOK
	(DeviceInfo 0 gVersion @temp1)
	(DeviceInfo @temp41)
	(if
		(and
			(DeviceInfo 2 @temp1 @temp41)
			(DeviceInfo 3 @temp41)
		)
		(if fSAVEDISK
			(Format
				@temp81
				{Please insert your SAVE GAME disk in drive %s.}
				@temp41
			)
		else
			(Format
				@temp81
				{Please insert your GAME disk in drive %s.}
				@temp41
			)
		)
		(DeviceInfo 4)
		(if fSAVEDISK
			(= btnPressed
				(Print @temp81 #font 0 #button {OK} 1 #button {Cancel} 0)
			)
		else
			; #button "Change Directory" 2
			(= btnPressed (Print @temp81 #font 0 #button {OK} 1))
		)
	)
; (if(== btnPressed 2) // btnChangeDirectory
; 			
; 			SRChangeDirectory(gVersion)
; 		)
	(return btnPressed)
)
