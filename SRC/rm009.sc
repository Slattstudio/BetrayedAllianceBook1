;;; Sierra Script 1.0 - (do not remove this comment)

(script# 9)
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
	rm009 0
)

(local


	htext
	htext2
	snd
)

(instance rm009 of Rm
	(properties
		picture 900
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
		(cP init: setPri: 10 setScript: cardFlourish)
		(cR init: setPri: 10)
		(cO init: setPri: 10)
		(cF init: setPri: 10)
		(cI init: setPri: 10)
		(cL init: setPri: 10)
		(cE init: setPri: 10)
		(cG init: setPri: 10)
		(cA init: setPri: 10)
		(cM init: setPri: 10)
		(cE2 init: setPri: 10)
		(cS init: setPri: 10)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0
				(= cycles 90)
				(= gWndColor 5)
				(= gWndBack 13)
				(Print
					{This update for Betrayed Alliance Book 1 is not complete. This is a limited release to patrons and backers.\n\nIf you have received this game in any other way, you will be prosecuted for breaking copyright.}
					#font
					0
					#title
					{NOTICE}
				)
				(Print
					{Just kidding. Have fun! Just know that it's not finished, and doesn't represent the completed game.}
					#font
					0
					#title
					{NOTICE}
				)
				(= gWndColor 0) ; clBLACK
				(= gWndBack 15)
			)                   ; clWHITE
			(1 (= cycles 90))
			(2
				(cardFlourish changeState: 1)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_RETURN)
				(gRoom newRoom: 803)
			)
		)
	)
)

(instance cardFlourish of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 10))
			(1
				(= cycles 4)
				(= snd aud)
				(snd
					command: {playx}
					fileName: {music\\profilegames.mp3}
					volume: {70}
					loopCount: {0}
					init:
				)
				; (send gTheMusic:prevSignal(0)stop()number(801)play())
				(cP setCycle: End)
			)
			(2
				(= cycles 4)
				(cR setCycle: End)
			)
			(3
				(= cycles 4)
				(cO setCycle: End)
			)
			(4
				(= cycles 4)
				(cF setCycle: End)
			)
			(5
				(= cycles 4)
				(cI setCycle: End)
			)
			(6
				(= cycles 4)
				(cL setCycle: End)
			)
			(7
				(= cycles 4)
				(cE setCycle: End)
			)
			(8
				(= cycles 4)
				(cG setCycle: End)
			)
			(9
				(= cycles 4)
				(cA setCycle: End)
			)
			(10
				(= cycles 4)
				(cM setCycle: End)
			)
			(11
				(= cycles 4)
				(cE2 setCycle: End)
			)
			(12
				(= cycles 4)
				(cS setCycle: End)
			)
			(13
				(= cycles 13)
				(= htext
					(Display
						{Presents}
						dsCOORD
						141
						85
						dsCOLOUR
						11      ; sky blue
						dsBACKGROUND
						clTRANSPARENT
						dsSAVEPIXELS
						dsFONT
						3
					)
				)
			)
			(14
				(= cycles 37)
				(= htext2
					(Display
						{A Game by Ryan Slattery}
						dsCOORD
						141
						110
						dsCOLOUR
						9
						dsBACKGROUND
						clTRANSPARENT
						dsSAVEPIXELS
						dsFONT
						3
					)
				)
			)
			(15
				(= cycles 15)
				(cP setCycle: Beg)
				(cR setCycle: Beg)
				(cO setCycle: Beg)
				(cF setCycle: Beg)
				(cI setCycle: Beg)
				(cL setCycle: Beg)
				(cE setCycle: Beg)
				(cG setCycle: Beg)
				(cA setCycle: Beg)
				(cM setCycle: Beg)
				(cE2 setCycle: Beg)
				(cS setCycle: Beg)
				(Display {} dsRESTOREPIXELS htext)
				(Display {} dsRESTOREPIXELS htext2)
			)
			(16 (gRoom newRoom: 803))
		)
	)
)

(instance cP of Prop
	(properties
		y 70
		x 30
		view 19
		loop 10
	)
)

(instance cR of Prop
	(properties
		y 70
		x 50
		view 19
		loop 5
	)
)

(instance cO of Prop
	(properties
		y 70
		x 70
		view 19
		loop 6
	)
)

(instance cF of Prop
	(properties
		y 70
		x 90
		view 19
		loop 9
	)
)

(instance cI of Prop
	(properties
		y 70
		x 110
		view 19
		loop 3
	)
)

(instance cL of Prop
	(properties
		y 70
		x 130
		view 19
		loop 4
	)
)

(instance cE of Prop
	(properties
		y 70
		x 150
		view 19
		loop 7
	)
)

(instance cG of Prop
	(properties
		y 70
		x 175
		view 19
		loop 2
	)
)

(instance cA of Prop
	(properties
		y 70
		x 195
		view 19
		loop 1
	)
)

(instance cM of Prop
	(properties
		y 70
		x 215
		view 19
	)
)

(instance cE2 of Prop
	(properties
		y 70
		x 235
		view 19
		loop 7
	)
)

(instance cS of Prop
	(properties
		y 70
		x 255
		view 19
		loop 8
	)
)

(instance aud of sciAudio
	(properties)
	
	(method (init)
		(super init:)
	)
)
