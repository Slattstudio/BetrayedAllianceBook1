;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; controls.sc
; Contains the base menubar class, dialog window class, control classes
; (button,edit,icon,etc.), as well as the Print methods. These allow you to
; have a GUI in your game, as well as print messages.
(script# CONTROLS_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use obj)

(public
	Print 0
	IconPrint 1
	EditPrint 2
	GetNumber 3
	FormatPrint 4
)




(class MenuBar of Obj
	(properties
		state DISABLED
	)
	
	(method (draw)
		(= state ENABLED)
		(DrawMenuBar TRUE)
	)
	
	(method (hide)
		(DrawMenuBar FALSE)
	)
	
	(method (handleEvent pEvent &tmp menuItem joyInfo)
		(= menuItem NULL)
		(if state
			(= joyInfo (Joystick 12 30))
			(= menuItem (MenuSelect pEvent &rest))
			(Joystick 12 joyInfo)
		)
		(return menuItem)
	)
	
	(method (add)
		(AddMenu &rest)
	)
)


(class Control of Obj
	(properties
		type 0
		state 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		key 0
		said 0
		value 0
	)
	
	(method (doit)
		(return value)
	)
	
	(method (enable fENABLE)
		(if fENABLE
			(= state (| state 1))
		else
			(= state (& state $fffe))
		)
	)
	
	(method (select fSELECT)
		(if fSELECT
			(= state (| state 8))
		else
			(= state (& state $fff7))
		)
		(self draw:)
	)
	
	(method (handleEvent pEvent &tmp tracked evType)
		(if (pEvent claimed?) (return FALSE))
		(= tracked FALSE)
		(if (& state 1)
			(= evType (pEvent type?))
			(if
				(or
					(and (== evType evSAID) (Said said))
					(and (== evType evKEYBOARD) (== (pEvent message?) key))
					(and (== evType evMOUSEBUTTON) (self check: pEvent))
				)
				(pEvent claimed: TRUE)
				(= tracked (self track: pEvent))
			)
		)
		(return tracked)
	)
	
	(method (check pEvent)
		(if
			(and
				(>= (pEvent x?) nsLeft)
				(>= (pEvent y?) nsTop)
				(< (pEvent x?) nsRight)
				(< (pEvent y?) nsBottom)
			)
			(return TRUE)
		)
		(return FALSE)
	)
	
	(method (track pEvent &tmp mouseInArea prevMouseInArea)
		(if (== evMOUSEBUTTON (pEvent type?))
			(= prevMouseInArea FALSE)
			(repeat
				(= pEvent (Event new: evNOJOYSTICK))
				(GlobalToLocal pEvent)
				(= mouseInArea (self check: pEvent))
				(if (!= mouseInArea prevMouseInArea)
					(HiliteControl self)
					(= prevMouseInArea mouseInArea)
				)
				(pEvent dispose:)
				(breakif(not (GetMouseRelease)))
			)
			(if mouseInArea (HiliteControl self))
			(return mouseInArea)
		else
			(return self)
		)
	)
	
	(method (setSize)
	)
	
	(method (move theX theY)
		(= nsRight (+ nsRight theX))
		(= nsLeft (+ nsLeft theX))
		(= nsTop (+ nsTop theY))
		(= nsBottom (+ nsBottom theY))
	)
	
	(method (moveTo newX newY)
		(self move: (- newX nsLeft) (- newY nsTop))
	)
	
	(method (draw)
		(DrawControl self)
	)
	
	(method (isType aType)
		(return (== type aType))
	)
	
	(method (checkState aState)
		(return (& state aState))
	)
	
	(method (cycle)
	)
)


(class DText of Control
	(properties
		type ctlTEXT
		state 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		key 0
		said 0
		value 0
		text 0
		font 1
		mode 0
	)
	
	(method (new)
		(self font: gDefaultFont)
		(super yourself: new:)
	)
	
	(method (setSize maxWidth &tmp [rect 4])
		(if argc
			(TextSize @rect text font maxWidth)
		else
			(TextSize @rect text font 0)
		)
		(= nsBottom (+ nsTop [rect rtBOTTOM]))
		(= nsRight (+ nsLeft [rect rtRIGHT]))
	)
)


(class DIcon of Control
	(properties
		type ctlICON
		state 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		key 0
		said 0
		value 0
		view 0
		loop 0
		cel 0
	)
	
	(method (setSize)
		(= nsRight (+ nsLeft (CelWide view loop cel)))
		(= nsBottom (+ nsTop (CelHigh view loop cel)))
	)
)


(class DButton of Control
	(properties
		type ctlBUTTON
		state 3
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		key 0
		said 0
		value 0
		text 0
		font 0
	)
	
	(method (setSize &tmp [rect 4])
		(TextSize @rect text font)
		(= [rect rtBOTTOM] (+ [rect rtBOTTOM] 2))
		(= [rect rtRIGHT] (+ [rect rtRIGHT] 2))
		(= nsBottom (+ nsTop [rect rtBOTTOM]))
		(= [rect rtRIGHT] (* (/ (+ [rect rtRIGHT] 15) 16) 16))
		(= nsRight (+ [rect rtRIGHT] nsLeft))
	)
)


(class DEdit of Control
	(properties
		type ctlEDIT
		state 1
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		key 0
		said 0
		value 0
		text 0
		font 0
		max 0
		cursor 0
	)
	
	(method (track pEvent)
		(EditControl self pEvent)
		(return self)
	)
	
	(method (setSize &tmp [rect 4])
		(TextSize @rect {M} font)
		(= nsBottom (+ nsTop [rect rtBOTTOM]))
		(= nsRight (+ nsLeft (/ (* [rect rtRIGHT] max 3) 4)))
		(= cursor (StrLen text))
	)
)


(class DSelector of Control
	(properties
		type ctlSELECTOR
		state 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		key 0
		said 0
		value 0
		font 0
		x 20
		y 6
		text 0
		cursor 0
		lsTop 0
		mark 0
	)
	
	(method (handleEvent pEvent &tmp theMark [rect 4])
		(if (pEvent claimed?) (return FALSE))
		; if the joystick was used, set the
		; events to the keyboard equivilants
		(if (== evJOYSTICK (pEvent type?))
			(pEvent type: evKEYBOARD)
			(switch (pEvent message?)
				(DOWN
					(pEvent message: KEY_DOWN)
				)
				(UP (pEvent message: KEY_UP))
				(else 
					(pEvent type: evJOYSTICK)
				)
			)
		)
		(switch (pEvent type?)
			(evKEYBOARD
				(pEvent claimed: TRUE)
				(switch (pEvent message?)
					(KEY_HOME (self retreat: 50))
					(KEY_END (self advance: 50))
					(KEY_PAGEUP
						(self advance: (- y 1))
					)
					(KEY_PAGEDOWN
						(self retreat: (- y 1))
					)
					(KEY_DOWN (self advance: 1))
					(KEY_UP (self retreat: 1))
					(else  (pEvent claimed: FALSE))
				)
			)
			(evMOUSEBUTTON
				(if (self check: pEvent)
					(pEvent claimed: TRUE)
					(if (< (pEvent y?) (+ nsTop 10))
						(repeat
							(self retreat: 1)
							(if (not (GetMouseRelease))
								(if (and (pEvent claimed?) (& state 2)) (return self))
								(return 0)
							)
						)
					)
					(if (> (pEvent y?) (- nsBottom 10))
						(repeat
							(self advance: 1)
							(if (not (GetMouseRelease))
								(if (and (pEvent claimed?) (& state 2)) (return self))
								(return 0)
							)
						)
					)
					(TextSize @rect {M} font)
					(= theMark
						(/ (- (pEvent y?) (+ nsTop 10)) [rect rtBOTTOM])
					)
					(if (> theMark mark)
						(self advance: (- theMark mark))
					else
						(self retreat: (- mark theMark))
					)
				)
			)
		)
		(if (and (pEvent claimed?) (& state 2)) (return self))
		(return 0)
	)
	
	(method (setSize &tmp [rect 4])
		(TextSize @rect {M} font)
		(= nsBottom (+ nsTop 20 (* [rect rtBOTTOM] y)))
		(= nsRight (+ nsLeft (/ (* [rect rtRIGHT] x 3) 4)))
		(= cursor text)
		(= lsTop text)
		(= mark 0)
	)
	
	(method (indexOf aString &tmp pText i)
		(= pText text)
		(for ( (= i 0)) (< i 300)  ( (++ i)) (if (== 0 (StrLen pText)) (return -1)) (if (not (StrCmp aString pText)) (return i)) (= pText (+ pText x)))
	)
	
	(method (at position)
		(return (+ text (* x position)))
	)
	
	(method (advance amount &tmp DO_DRAW)
		(= DO_DRAW FALSE)
		(while (and amount (StrAt cursor x))
			(= DO_DRAW TRUE)
			(= cursor (+ cursor x))
			(if (< (+ mark 1) y)
				(++ mark)
			else
				(= lsTop (+ lsTop x))
			)
			(-- amount)
		)
		(if DO_DRAW (self draw:))
	)
	
	(method (retreat amount &tmp DO_DRAW)
		(= DO_DRAW FALSE)
		(while (and amount (!= cursor text))
			(= DO_DRAW TRUE)
			(= cursor (- cursor x))
			(if mark (-- mark) else (= lsTop (- lsTop x)))
			(-- amount)
		)
		(if DO_DRAW (self draw:))
	)
)


(class Dialog of List
	(properties
		elements 0
		size 0
		text 0
		window 0
		theItem 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		time 0
		busy 0
		seconds 0
		lastSeconds 0
	)
	
	(method (doit pItem &tmp hEvent isClaimed)
		(= busy TRUE)
		(self eachElementDo: #init)
		(if theItem (theItem select: FALSE))
		(if (and argc pItem)
			(= theItem pItem)
		else
			(= theItem (self firstTrue: #checkState TRUE))
		)
		(if theItem (theItem select: TRUE))
		(= isClaimed FALSE)
		(while (not isClaimed)
			(self eachElementDo: #cycle)
			(= hEvent (Event new:))
			(GlobalToLocal hEvent)
			(= isClaimed (self handleEvent: hEvent))
			(hEvent dispose:)
			(self check:)
			(if (or (== isClaimed -1) (not busy))
				(= isClaimed FALSE)
				(EditControl theItem 0)
				(break)
			)
			(Wait 1)
		)
		(= busy FALSE)
		(return isClaimed)
	)
	
	(method (dispose)
		(if (== self gPrintDlg)
			(SetPort gOldPort)
			(= gPrintDlg NULL)
			(= gOldPort NULL)
		)
		(if window (window dispose:))
		(= window NULL)
		(= theItem NULL)
		(super dispose:)
	)
	
	(method (open theType thePriority)
		(if (and (PicNotValid) gCast)
			(Animate (gCast elements?) 0)
		)
		(= window (window new:))
		(window
			top: nsTop
			left: nsLeft
			bottom: nsBottom
			right: nsRight
			title: text
			type: theType
			priority: thePriority
			color: gWndColor
			back: gWndBack
			open:
		)
		(= seconds time)
		(self draw:)
	)
	
	(method (draw)
		(self eachElementDo: #draw)
	)
	
	(method (cue)
		(if (not busy) (self dispose:) else (= busy FALSE))
	)
	
	(method (advance &tmp hItem)
		(if theItem
			(theItem select: 0)
			(= hItem (self contains: theItem))
			(repeat
				(= hItem (self next: hItem))
				(if (not hItem) (= hItem (self first:)))
				(= theItem (NodeValue hItem))
				(breakif (& (theItem state?) 1))
			)
			(theItem select: 1)
		)
	)
	
	(method (retreat &tmp hItem)
		(if theItem
			(theItem select: 0)
			(= hItem (self contains: theItem))
			(repeat
				(= hItem (self prev: hItem))
				(if (not hItem) (= hItem (self last:)))
				(= theItem (NodeValue hItem))
				(breakif (& (theItem state?) 1))
			)
			(theItem select: 1)
		)
	)
	
	(method (move theX theY)
		(= nsRight (+ nsRight theX))
		(= nsLeft (+ nsLeft theX))
		(= nsTop (+ nsTop theY))
		(= nsBottom (+ nsBottom theY))
	)
	
	(method (moveTo newX newY)
		(self move: (- newX nsLeft) (- newY nsTop))
	)
	
	(method (center)
		(self
			moveTo:
				(+
					(window brLeft?)
					(/
						(-
							(- (window brRight?) (window brLeft?))
							(- nsRight nsLeft)
						)
						2
					)
				)
				(+
					(window brTop?)
					(/
						(-
							(- (window brBottom?) (window brTop?))
							(- nsBottom nsTop)
						)
						2
					)
				)
		)
	)
	
	(method (setSize &tmp i hControl [rect 4])
		(if text
			(TextSize @rect text 0 -1)
			(= nsTop [rect rtTOP])
			(= nsLeft [rect rtLEFT])
			(= nsBottom [rect rtBOTTOM])
			(= nsRight [rect rtRIGHT])
		else
			(= nsTop 0)
			(= nsLeft 0)
			(= nsBottom 0)
			(= nsRight 0)
		)
		(= i (self first:))
		(while i
			(= hControl (NodeValue i))
			(if (< (hControl nsLeft?) nsLeft)
				(= nsLeft (hControl nsLeft?))
			)
			(if (< (hControl nsTop?) nsTop)
				(= nsTop (hControl nsTop?))
			)
			(if (> (hControl nsRight?) nsRight)
				(= nsRight (hControl nsRight?))
			)
			(if (> (hControl nsBottom?) nsBottom)
				(= nsBottom (hControl nsBottom?))
			)
			(= i (self next: i))
		)
		(= nsRight (+ nsRight 4))
		(= nsBottom (+ nsBottom 4))
		(self moveTo: 0 0)
	)
	
	(method (handleEvent pEvent &tmp hItem)
		(if
			(or
				(pEvent claimed?)
				(== (pEvent type?) 0)
				(and
					(!= evMOUSEBUTTON (pEvent type?))
					(!= evKEYBOARD (pEvent type?))
					(!= evJOYSTICK (pEvent type?))
					(!= $0100 (pEvent type?))
				)
			)
			(EditControl theItem pEvent)
			(return 0)
		)
		(if (= hItem (self firstTrue: #handleEvent pEvent))
			(EditControl theItem 0)
			(if (not (hItem checkState: 2))
				(if theItem (theItem select: 0))
				(= theItem hItem)
				(theItem select: 1)
				(hItem doit:)
				(return NULL)
			)
		)
		(if
			(or
				(== (pEvent type?) $0100)
				(and
					(== evKEYBOARD (pEvent type?))
					(== $000d (pEvent message?))
				)
			)
			(if (and theItem (theItem checkState: 1))
				(= hItem theItem)
				(EditControl theItem 0)
				(pEvent claimed: TRUE)
				(return theItem)
			)
		)
		(if
			(and
				(== evKEYBOARD (pEvent type?))
				(== KEY_ESC (pEvent message?))
			)
			(pEvent claimed: TRUE)
			(return -1)
		)
		(if (not (self firstTrue: #checkState 1))
			(if
				(or
					(and
						(== evKEYBOARD (pEvent type?))
						(== KEY_RETURN (pEvent message?))
					)
					(== evMOUSEBUTTON (pEvent type?))
					(== $0100 (pEvent type?))
				)
				(pEvent claimed: TRUE)
				(return -1)
			)
		)
		(cond 
			(
				(and
					(== evKEYBOARD (pEvent type?))
					(== KEY_TAB (pEvent message?))
				)
				(pEvent claimed: TRUE)
				(self advance:)
			)
			(
				(and
					(== evKEYBOARD (pEvent type?))
					(== KEY_SHIFTTAB (pEvent message?))
				)
				(pEvent claimed: TRUE)
				(self retreat:)
			)
			(else (EditControl theItem pEvent))
		)
		(return hItem)
	)
	
	(method (check &tmp theTime)
		(if seconds
			(= theTime (GetTime gtTIME_OF_DAY))
			(if (!= lastSeconds theTime)
				(= lastSeconds theTime)
				(if (not (-- seconds)) (self cue:))
			)
		)
	)
)


(class Controls of List
	(properties
		elements 0
		size 0
	)
	
	(method (draw)
		(self eachElementDo: #setSize)
		(self eachElementDo: #draw)
	)
	
	(method (handleEvent pEvent &tmp hCtl)
		(if (pEvent claimed?) (return NULL))
		(= hCtl (self firstTrue: #handleEvent pEvent))
		(if (and hCtl (not (hCtl checkState: 2)))
			(hCtl doit:)
			(= hCtl NULL)
		)
		(return hCtl)
	)
)


(procedure (GetMouseRelease &tmp hEvent retVal)
	(= hEvent (Event new:))
	(= retVal (!= (hEvent type?) evMOUSERELEASE))
	(hEvent dispose:)
	(return retVal)
)


(procedure (Print params &tmp hDialog hDText hIcon hEdit btnPressed paramCnt diagX diagY maxWidth newPrintDlg hFirstEnabled oldPort [hButtons 6] btnsWidth buttonCnt btnX [msgBuf 1013] moveToX moveToY btnY)
	(= diagY -1)
	(= diagX -1)
	(= buttonCnt 0)
	(= hEdit NULL)
	(= hIcon NULL)
	(= btnsWidth 0)
	(= maxWidth 0)
	(= newPrintDlg NULL)
	(= hDialog (Dialog new:))
	(hDialog window: gTheWindow name: {PrintD})
	(= hDText (DText new:))
	(cond 
		((u< [params 0] 1000) (GetFarText [params 0] [params 1] @msgBuf) (= paramCnt 2))
		([params 0] (StrCpy @msgBuf [params 0]) (= paramCnt 1))
		(else (= msgBuf 0) (= paramCnt 0))
	)
	(hDText
		text: @msgBuf
		moveTo: 4 4
		font: gDefaultFont
		setSize:
	)
	(hDialog add: hDText)
	(while (< paramCnt argc)
		(switch [params paramCnt]
			(#mode
				(++ paramCnt)
				(hDText mode: [params paramCnt])
			)
			(#font
				(++ paramCnt)
				(hDText font: [params paramCnt] setSize: maxWidth)
			)
			(#width
				(= maxWidth [params (++ paramCnt)])
				(hDText setSize: maxWidth)
			)
			(#time
				(++ paramCnt)
				(hDialog time: [params paramCnt])
			)
			(#title
				(++ paramCnt)
				(hDialog text: [params paramCnt])
			)
			(#at
				(= diagX [params (++ paramCnt)])
				(= diagY [params (++ paramCnt)])
			)
			(#draw
				(Animate (gCast elements?) 0)
			)
			(#edit
				(++ paramCnt)
				(= hEdit (DEdit new:))
				(hEdit text: [params paramCnt])
				(++ paramCnt)
				(hEdit max: [params paramCnt] setSize:)
			)
			(#button
				(= [hButtons buttonCnt] (DButton new:))
				([hButtons buttonCnt]
					text: [params (++ paramCnt)]
					value: [params (++ paramCnt)]
					setSize:
				)
				(= btnsWidth
					(+ btnsWidth ([hButtons buttonCnt] nsRight?) 4)
				)
				(++ buttonCnt)
			)
			(#icon
				(if (IsObject [params (+ paramCnt 1)])
					(= hIcon ([params (+ paramCnt 1)] new:))
					(hIcon setSize:)
					(= paramCnt (+ paramCnt 1))
				else
					(= hIcon (DIcon new:))
					(hIcon
						view: [params (+ paramCnt 1)]
						loop: [params (+ paramCnt 2)]
						cel: [params (+ paramCnt 3)]
						setSize:
					)
					(= paramCnt (+ paramCnt 3))
				)
			)
			(#dispose
				(if gPrintDlg (gPrintDlg dispose:))
				(= newPrintDlg hDialog)
			)
			(#window
				(++ paramCnt)
				(hDialog window: [params paramCnt])
			)
		)
		(++ paramCnt)
	)
	(if hIcon
		(hIcon moveTo: 4 4)
		(hDText moveTo: (+ 4 (hIcon nsRight?)) (hIcon nsTop?))
		(hDialog add: hIcon)
	)
	(hDialog setSize:)
	(if hEdit
		(hEdit
			moveTo: (hDText nsLeft?) (+ 4 (hDText nsBottom?))
		)
		(hDialog add: hEdit setSize:)
	)
	(if (> btnsWidth (hDialog nsRight?))
		(= btnX 4)
	else
		(= btnX (- (hDialog nsRight?) btnsWidth))
	)
	; If you wanted the button centered...
	; = btnX / ( - (send hDialog:nsRight) btnsWidth ) 2
	(= paramCnt 0)
	(if gVertButton
		(= btnY (hDialog nsBottom?))
		(while (< paramCnt buttonCnt)
			([hButtons paramCnt]
				moveTo: (+ (hDialog nsLeft?) 4) btnY
			)
			(hDialog add: [hButtons paramCnt])
			(= btnY (+ 4 ([hButtons paramCnt] nsBottom?)))
			(++ paramCnt)
		)
	else
		(while (< paramCnt buttonCnt)
			([hButtons paramCnt] moveTo: btnX (hDialog nsBottom?))
			(hDialog add: [hButtons paramCnt])
			(= btnX (+ 4 ([hButtons paramCnt] nsRight?)))
			(++ paramCnt)
		)
	)
	(hDialog setSize: center:)
	(if (and hIcon (not (StrLen @msgBuf)))
		(hIcon
			moveTo:
				(/
					(-
						(- (hDialog nsRight?) (hDialog nsLeft?))
						(- (hIcon nsRight?) (hIcon nsLeft?))
					)
					2
				)
				4
		)
	)
	(if (== -1 diagX)
		(= moveToX (hDialog nsLeft?))
	else
		(= moveToX diagX)
	)
	(if (== -1 diagY)
		(= moveToY (hDialog nsTop?))
	else
		(= moveToY diagY)
	)
	(hDialog moveTo: moveToX moveToY)
	(= oldPort (GetPort))
	(if (hDialog text?)
		(= moveToX nwTITLE)
	else
		(= moveToX nwNORMAL)
	)
	(if newPrintDlg (= moveToY 15) else (= moveToY -1))
	(hDialog open: moveToX moveToY)
	(if newPrintDlg
		(= gOldPort (GetPort))
		(SetPort oldPort)
		(return (= gPrintDlg newPrintDlg))
	)
	(if
		(and
			(= hFirstEnabled (hDialog firstTrue: #checkState 1))
			(not (hDialog firstTrue: #checkState 2))
		)
		(hFirstEnabled state: (| (hFirstEnabled state?) 2))
	)
	(= btnPressed (hDialog doit: hFirstEnabled))
	(if (== btnPressed -1) (= btnPressed 0))
	(for ( (= paramCnt 0)) (< paramCnt buttonCnt)  ( (++ paramCnt)) (if (== btnPressed [hButtons paramCnt])
		(= btnPressed (btnPressed value?))
		(break)
	))
	(if (not (hDialog theItem?)) (= btnPressed 1))
	(hDialog dispose:)
	(return btnPressed)
)


(procedure (IconPrint message view loop cel)
	(Print message #icon view loop cel &rest)
)


(procedure (EditPrint theText theMax aMessage)
	(cond 
		((>= argc 3)
			(if (Print aMessage #edit theText theMax &rest)
				(return (StrLen theText))
			)
		)
		((Print {} #edit theText theMax &rest) (return (StrLen theText)))
	)
)


(procedure (GetNumber aMessage defaultNumber &tmp [temps 40])
	(= temps 0)
	(if (> argc 1) (Format @temps {%d} defaultNumber))
	(if (EditPrint @temps 5 aMessage)
		(return (ReadNumber @temps))
	else
		(return -1)
	)
)


(procedure (FormatPrint textorstring textid &tmp [temps 500])
	(if (u< textorstring 1000)
		(Format @temps textorstring textid &rest)
	else
		(Format @temps &rest textorstring)
	)
	(Print @temps)
)
