;;; Sierra Script 1.0 - (do not remove this comment)

(script# 801)
(include sci.sh)
(include game.sh)
(use main)
(use game)
(use menubar)
(use obj)
(use cycle)
(use user)
(use controls)
(use feature)
(use sciaudio)

(public
	TitleScreen 0
)

(local
; TITLE SCREEN
;
; titlescreen.sc
; Contains the title screen room.


	; Variables to determine what the "selector" View is highlighting
	sNewGame =  1
	sRestore =  0
	sPlayDemo =  0
	sQuitGame =  0
	myEvent
	myEventX
	myEventY
	snd
)

(instance TitleScreen of Rm
	(properties
		picture 800
	)
	
	(method (init)
		; Set up the title screen
		(ProgramControl)
		(= gProgramControl FALSE)
		(gGame setSpeed: 3)
		(SL disable:)
		(TheMenuBar hide:)
		(super init:)
		(self setScript: RoomScript)
		(gEgo init: hide:)
		(Display
			{Copyright < 2020 v.1.2}
			dsCOORD
			141
			177
			dsCOLOUR
			1          ; dark blue
			dsBACKGROUND
			clTRANSPARENT
		)
		; (profile:init()loop(1))
		; (games:init()setCycle(Fwd)cycleSpeed(2))
		(newGame init:)
		(restore init: loop: 1)
		(quitGame init: loop: 2)
		(playDemo init: loop: 3)
		(selector init: loop: 4)
		(books init:)
		(BAlogo init:)
		; (send gTheMusic:prevSignal(0)stop()number(999)play())
		(= snd aud)
		(snd
			command: {playx}
			fileName: {music\\titlescreen.mp3}
			volume: {70}
			loopCount: {0}
			init:
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 40))
			(1 (= cycles 40))
			(2 (= cycles 7))
			; (line:init()setCycle(End))
			(3 (= cycles 10))
			; (line:loop(1)posn(208 140)cel(0)setCycle(End)cycleSpeed(1))
			(4 (= seconds 12))
			(5
				(= gDemo 1)
				(gRoom newRoom: INITROOMS_SCRIPT)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evJOYSTICK)
			(if (== (pEvent message?) 1) (selectorCheckUp)) ; UP
		)
		(if (== (pEvent type?) evJOYSTICK)
			(if (== (pEvent message?) 5) (selectorCheckDown)) ; Down
		)
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_RETURN)
				(cond 
					(sNewGame (gRoom newRoom: 900))
					(sRestore
						(= snd aud)
						(snd
							command: {stop}
							fileName: {music\\intro.mp3}
							fadeOutMillisecs: {4000}
							loopCount: {0}
							init:
						)
						(gGame restore:)
					)
					(sPlayDemo (gRoom newRoom: 901))
					(sQuitGame (= gQuitGame 1))
				)
			)
		)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(cond 
				(
					(and
						(> (pEvent x?) (newGame nsLeft?))
						(< (pEvent x?) (newGame nsRight?))
						(> (pEvent y?) (newGame nsTop?))
						(< (pEvent y?) (newGame nsBottom?))
					)
					(gRoom newRoom: 900)
				)
				(
					(and
						(> (pEvent x?) (restore nsLeft?))
						(< (pEvent x?) (restore nsRight?))
						(> (pEvent y?) (restore nsTop?))
						(< (pEvent y?) (restore nsBottom?))
					)
					(gGame restore:)
				)
				(
					(and
						(> (pEvent x?) (playDemo nsLeft?))
						(< (pEvent x?) (playDemo nsRight?))
						(> (pEvent y?) (playDemo nsTop?))
						(< (pEvent y?) (playDemo nsBottom?))
					)
					; = gDemo 1
					(gRoom newRoom: 901)
				)
				(
					(and
						(> (pEvent x?) (quitGame nsLeft?))
						(< (pEvent x?) (quitGame nsRight?))
						(> (pEvent y?) (quitGame nsTop?))
						(< (pEvent y?) (quitGame nsBottom?))
					)
					(= gQuitGame 1)
				)
			)
		)
	)
	                     ; (else
	; FormatPrint("the mouse is on %u and %u" (send pEvent:x) (send pEvent:y) )
	; ) ; end method
	(method (doit)
		(super doit:)
		(= myEvent (Event new: evNULL))
		(cond 
			(
				(and
					(> (myEvent x?) (newGame nsLeft?))
					(< (myEvent x?) (newGame nsRight?))
					(> (myEvent y?) (newGame nsTop?))
					(< (myEvent y?) (newGame nsBottom?))
				)
				(selectorZero)
				(= sNewGame 1)
				(selector posn: 44 61)
				(myEvent claimed: TRUE)
			)
			(
				(and
					(> (myEvent x?) (restore nsLeft?))
					(< (myEvent x?) (restore nsRight?))
					(> (myEvent y?) (restore nsTop?))
					(< (myEvent y?) (restore nsBottom?))
				)
				(selectorZero)
				(= sRestore 1)
				(selector posn: 44 94)
				(myEvent claimed: TRUE)
			)
			(
				(and
					(> (myEvent x?) (playDemo nsLeft?))
					(< (myEvent x?) (playDemo nsRight?))
					(> (myEvent y?) (playDemo nsTop?))
					(< (myEvent y?) (playDemo nsBottom?))
				)
				(selectorZero)
				(= sPlayDemo 1)
				(selector posn: 44 127)
				(myEvent claimed: TRUE)
			)
			(
				(and
					(> (myEvent x?) (quitGame nsLeft?))
					(< (myEvent x?) (quitGame nsRight?))
					(> (myEvent y?) (quitGame nsTop?))
					(< (myEvent y?) (quitGame nsBottom?))
				)
				(selectorZero)
				(= sQuitGame 1)
				(selector posn: 44 160)
				(myEvent claimed: TRUE)
			)
		)
		(myEvent dispose:)
	)
)

; Procedures for the Title Menu
(procedure (selectorCheckUp)
	(RoomScript changeState: 3)
	(cond 
		(sNewGame (selectorZero) (= sQuitGame 1) (selector posn: 44 160))
		(sRestore (selectorZero) (= sNewGame 1) (selector posn: 44 61))
		(sPlayDemo (selectorZero) (= sRestore 1) (selector posn: 44 94))
		(sQuitGame (selectorZero) (= sPlayDemo 1) (selector posn: 44 127))
	)
)

(procedure (selectorCheckDown)
	(RoomScript changeState: 3)
	(cond 
		(sNewGame (selectorZero) (= sRestore 1) (selector posn: 44 94))
		(sRestore (selectorZero) (= sPlayDemo 1) (selector posn: 44 127))
		(sPlayDemo (selectorZero) (= sQuitGame 1) (selector posn: 44 160))
		(sQuitGame (selectorZero) (= sNewGame 1) (selector posn: 44 61))
	)
)

(procedure (selectorZero)
	(= sNewGame 0)
	(= sRestore 0)
	(= sPlayDemo 0)
	(= sQuitGame 0)
)

(instance BAlogo of Prop
	(properties
		y 52
		x 208
		view 995
	)
)

(instance books of Prop
	(properties
		y 173
		x 200
		view 984
	)
)

(instance newGame of Prop
	(properties
		y 57
		x 44
		view 998
	)
)

(instance restore of Prop
	(properties
		y 90
		x 44
		view 998
	)
)

(instance playDemo of Prop
	(properties
		y 123
		x 44
		view 998
	)
)

(instance quitGame of Prop
	(properties
		y 156
		x 44
		view 998
	)
)

(instance selector of Prop
	(properties
		y 61
		x 44
		view 998
	)
)

(instance line of Prop
	(properties
		y 112
		x 208
		view 997
	)
)

(instance aud of sciAudio
	(properties)
	
	(method (init)
		(super init:)
	)
)
