;;; Sierra Script 1.0 - (do not remove this comment)

(script# 803)
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

(public
	rm803 0
)

(local
; TITLE SCREEN (RING)
;
; titlescreen.sc
; Contains the title screen room.


	; (use "sciaudio")

	snd
	myEvent
	sNewGame =  1
	sRestore =  0
	bottomShimmering =  0
	topShimmering =  0
	
	overNewGame = 0	; is the cursor over the button
	overRestore = 0 ; is the cursor over the button
	
	overAutosave = 0 ; is the cursor over the button
)

(instance rm803 of Rm
	(properties
		picture 802
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(ProgramControl)
		(= gProgramControl FALSE)
		(gGame setSpeed: 3)
		(SL disable:)
		(TheMenuBar hide:)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
		)
		(Display
			{Book 1}
			dsFONT
			4
			dsCOORD
			;85
			71
			173
			dsCOLOUR
			9          ; grey
			dsBACKGROUND
			clTRANSPARENT
		)
		(Display
			{- v.1.3.3}
			dsFONT
			4
			dsCOORD
			;117
			103
			173
			dsCOLOUR
			1          ; dark blue
			dsBACKGROUND
			clTRANSPARENT
		)
		(Display
			{Copyright < 2024} 
			dsFONT
			4
			dsCOORD
			;117
			170
			173
			dsCOLOUR
			1          ; dark blue
			dsBACKGROUND
			clTRANSPARENT
		)
; MUSIC but not ready

		(gTheMusic prevSignal: 0 number: 100 play: loop: 0)
		(= gMap 1)
		(= gArcStl 1)
		(TheMenuBar state: DISABLED)
		(SetUpEgo)
		(gEgo init: hide:)
		(newGame init: setPri: 15)
		(restore init: setPri: 15)
		(betrayed init: setPri: 15)
		(alliance init: setPri: 15)
		
		(frontShimmer init: hide: cycleSpeed: 2 setPri: 10)
		(frontLeftShimmer init: hide: cycleSpeed: 2 setPri: 10)
		(frontRightShimmer init: hide: cycleSpeed: 2 setPri: 10)
		(backShimmer
			init:
			hide:
			cycleSpeed: 2
			cycleSpeed: 2
			setPri: 1
			setScript: backShimmerScript
		)
		(backLeftShimmer init: hide: cycleSpeed: 2 setPri: 1)
		(backRightShimmer init: hide: cycleSpeed: 2 setPri: 1)
		(slattstudioLogo init: setPri: 15)
		(autosave init: setPri: 15)
		(gTheMusic prevSignal: 0 stop: number: 800 play:)
				
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			(1 (= cycles 13) (frontShine))
			(2 (= bottomShimmering 2))
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent message?) KEY_RETURN)
			(if sNewGame (gRoom newRoom: 900))
			(if sRestore (gGame restore:))
		)
		(if (== (pEvent message?) KEY_ESCAPE)
			(if
				(Print
					{Do you really want to quit?}
					#title
					{Quit}
					#font
					gDefaultFont
					#button
					{ Quit_}
					1
					#button
					{ Oops_}
					0
				)
				(= gQuitGame TRUE)
			)	
		)
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) $006e) (gRoom newRoom: 900)) ; lowercase n
			(if (== (pEvent message?) $0072) (gGame restore:)) ; lowercase r
		)
		(if (== (pEvent type?) evJOYSTICK)
			(if (== (pEvent message?) 1)    ; If pressed the UP arrow
				(= sNewGame 1)
				(= sRestore 0)
				(if (not topShimmering)
					(backShimmerScript changeState: 1)
					(= topShimmering 1)
				)
			)
			(if (== (pEvent message?) 5)    ; If pressed the DOWN arrow
				(= sNewGame 0)
				(= sRestore 1)
				(if (not bottomShimmering)
					(RoomScript changeState: 1)
					(= bottomShimmering 1)
				)
			)
		)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if
				(and
					(> (pEvent x?) (slattstudioLogo nsLeft?))
					(< (pEvent x?) (slattstudioLogo nsRight?))
					(> (pEvent y?) (slattstudioLogo nsTop?))
					(< (pEvent y?) (slattstudioLogo nsBottom?))
				)
				(Print{\nThis game is brought to you by Slattstudio, devoted to bringing you freeware so good you'd pay to buy it!} #width 130 #font 4 #icon 979)
			)
			
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				
				(if
					(and
						(> (pEvent x?) (autosave nsLeft?))
						(< (pEvent x?) (autosave nsRight?))
						(> (pEvent y?) (autosave nsTop?))
						(< (pEvent y?) (autosave nsBottom?))
					)
					(= gWndColor 0) ; clYELLOW
					(= gWndBack 14) ; clDARKBLUE
					(= gVertButton 1)
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
					(return)
				)
			)
			(if overNewGame (gRoom newRoom: 900))
			(if overRestore (gGame restore:))
			(if overAutosave
				(if gAutosave
					(= gAutosave 0)
					;(autosave loop: 2)
				else
					(= gAutosave 1)
					;(autosave loop: 1)
				)	
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(= myEvent (Event new: evNULL))
		(if sNewGame (newGame cel: 1) else (newGame cel: 0))
		(if sRestore (restore cel: 1) else (restore cel: 0))
		(if gAutosave (autosave loop: 1) else (autosave loop: 2))
		
		(if (and
					(> (myEvent x?) (- (slattstudioLogo nsLeft?) 12))
					(< (myEvent x?) (+ (slattstudioLogo nsRight?) 12))
					(> (myEvent y?) (+ (slattstudioLogo nsTop?) 2))
					(< (myEvent y?) (+ (slattstudioLogo nsBottom?) 14))
			)
			(slattstudioLogo cel: 1)
		else
			(slattstudioLogo cel: 0)
		)
		
		(cond 
			
			(
				(and
					(> (myEvent x?) (- (newGame nsLeft?) 12))
					(< (myEvent x?) (+ (newGame nsRight?) 12))
					(> (myEvent y?) (+ (newGame nsTop?) 2))
					(< (myEvent y?) (+ (newGame nsBottom?) 14))
				)
				; (selectorZero())
				(= sNewGame 1)
				(= sRestore 0)
				(= overNewGame 1)
				(= overRestore 0)
				(= overAutosave 0)
				; (selector:posn(44 61))
				(if (not topShimmering)
					(backShimmerScript changeState: 1)
					(= topShimmering 1)
				)
				(myEvent claimed: TRUE)
			)
			(
				(and
					(> (myEvent x?) (- (restore nsLeft?) 12))
					(< (myEvent x?) (+ (restore nsRight?) 12))
					(> (myEvent y?) (+ (restore nsTop?) 2))
					(< (myEvent y?) (+ (restore nsBottom?) 20))
				)
				(= sRestore 1)
				(= sNewGame 0)
				(= overNewGame 0)
				(= overRestore 1)
				(= overAutosave 0)
				(if (not bottomShimmering)
					(RoomScript changeState: 1)
					(= bottomShimmering 1)
				)
				; (selector:posn(44 94))
				(myEvent claimed: TRUE)
			)
			(
				(and
					(> (myEvent x?) (- (autosave nsLeft?) 12))
					(< (myEvent x?) (+ (autosave nsRight?) 12))
					(> (myEvent y?) (+ (autosave nsTop?) 2))
					(< (myEvent y?) (+ (autosave nsBottom?) 20))
				)
				(= sRestore 0)
				(= sNewGame 0)
				(= overNewGame 0)
				(= overRestore 0)
				(= overAutosave 1)
			
				(autosave cel: 1)
				(myEvent claimed: TRUE)
			)
			(else
				(if (== bottomShimmering 2) (= bottomShimmering 0))
				(if (== topShimmering 2) (= topShimmering 0))
				(= overNewGame 0)
				(= overRestore 0)
				(= overAutosave 0)
				
				(autosave cel: 0)
			)
		)
		(myEvent dispose:)
	)
)

(instance backShimmerScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1 (= cycles 13) (backShine))
			(2 (= topShimmering 2))
		)
	)
)

(procedure (frontShine)
	(frontShimmer show: cel: 0 setCycle: End)
	(frontLeftShimmer show: cel: 0 setCycle: End)
	(frontRightShimmer show: cel: 0 setCycle: End)
)

; = bottomShimmering 0
(procedure (backShine)
	(backShimmer show: cel: 0 setCycle: End)
	(backLeftShimmer show: cel: 0 setCycle: End)
	(backRightShimmer show: cel: 0 setCycle: End)
)

; = bottomShimmering 0
(instance betrayed of Prop
	(properties
		y 72
		x 160
		view 982
		loop 0
	)
)

(instance alliance of Prop
	(properties
		y 160
		x 160
		view 982
		loop 1
	)
)
(instance slattstudioLogo of Prop
	(properties
		y 181
		x 158
		view 999
		loop 3
		cel 0
	)
)
(instance autosave of Prop
	(properties
		y 181
		x 291
		view 980
		loop 1
		cel 0
	)
)
(instance newGame of Prop
	(properties
		y 83
		x 160
		view 995
		loop 2
	)
)

(instance restore of Prop
	(properties
		y 125
		x 160
		view 995
		loop 3
	)
)

(instance frontShimmer of Prop
	(properties
		y 160
		x 160
		view 995
		loop 10
	)
)

(instance frontLeftShimmer of Prop
	(properties
		y 160
		x 90
		view 995
		loop 5
	)
)

(instance frontRightShimmer of Prop
	(properties
		y 160
		x 232
		view 995
		loop 6
	)
)

(instance backShimmer of Prop
	(properties
		y 63
		x 160
		view 995
		loop 7
	)
)

(instance backLeftShimmer of Prop
	(properties
		y 94
		x 87
		view 995
		loop 8
	)
)

(instance backRightShimmer of Prop
	(properties
		y 92
		x 235
		view 995
		loop 9
	)
)
