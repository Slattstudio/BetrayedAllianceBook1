;;; Sierra Script 1.0 - (do not remove this comment)

(script# 125)
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
	rm125 0
)

(local
; Ending - Cave @ night



	; (use "sciaudio")
	throwing =  0
	movingRight =  0
	horseOnScreen =  0
	hitWithArrow =  0
)
; snd

(instance rm125 of Rm
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
				(gEgo posn: 42 160 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: ignoreControl: ctlWHITE)
		(julyn
			init:
			setCycle: Walk
			ignoreControl: ctlWHITE
			ignoreActors:
		)
		(horse
			init:
			hide:
			setCycle: Walk
			ignoreControl: ctlWHITE
			ignoreActors:
		)
		(arrow init: hide: ignoreControl: ctlWHITE ignoreActors:)
		(wizard init: ignoreActors: setScript: wizardScript)
		(soldier init: ignoreActors: ignoreControl: ctlWHITE)
		(soldier2 init: ignoreActors: ignoreControl: ctlWHITE)
		(alterEgo
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
		)
		(throwEgo init: hide: ignoreActors:)
		(strawTargets init:)
		(= gEgoRunning 1)
		(RunningCheck)
		(= gArcStl 1)
		(= gMap 1)
		(SetCursor 998 (HaveMouse))
		(= gCurrentCursor 998)
		(gTheMusic number: 10 loop: -1 play:)
		
	)
)

; (TheMenuBar:state(DISABLED))

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 1))
			(1
				(gEgo setMotion: MoveTo 92 156 RoomScript)
				(julyn setMotion: MoveTo 86 160)
			)
; = snd aud (send snd:
;                    command("playx")
;                    fileName("music\\zombiechase.mp3")
;                    volume("70")
;                    loopCount("-1")
;                    init()
;                )
			(2
				(gEgo setMotion: MoveTo 130 156 RoomScript)
				(Print
					{Look Julyn! There is the Great Wizard who sent me to find you!}
					#at
					-1
					20
					#title
					{You say:}
				)
			)
			(3
				(= cycles 20)
				(Print
					{Wait a minute! What are you doing here with these Ishvi soldiers?}
					#at
					-1
					20
					#title
					{You ask:}
				)
			)
			(4
				(= cycles 2)
				(PrintWiz 125 0)
				(PrintWiz 125 1)
			)
			; Print(125 1 #at -1 20 #title "Wizard:")
			(5
				(soldier
					setCycle: Walk
					setMotion: MoveTo (+ (julyn x?) 3) (+ (julyn y?) 3)
				)
				(soldier2
					setCycle: Walk
					setMotion: MoveTo (- (julyn x?) 3) (- (julyn y?) 5) RoomScript
				)
			)
			(6
				(julyn setMotion: MoveTo 86 190)
				(soldier setMotion: MoveTo (+ (julyn x?) 3) 190)
				(soldier2
					setMotion: MoveTo (- (julyn x?) 3) 190 RoomScript
				)
			)
			(7
				(= cycles 2)
				(julyn hide:)
				(soldier hide:)
				(soldier2 hide:)
			)
			(8
				(wizardScript changeState: 1)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		
		(SetCursor 998 (HaveMouse))
		(= gCurrentCursor 998)
		
		(if throwing
			(throwEgo posn: (+ (throwEgo x?) 5) (throwEgo y?))
		)
		(if hitWithArrow
			(wizard posn: (- (wizard x?) 3) (wizard y?))
		)
		(if horseOnScreen
			(if movingRight
				(leah
					init:
					show:
					loop: 5
					posn: (+ (horse x?) 3) (- (horse y?) 17)
					setPri: 14
				)
			else
				(leah
					init:
					show:
					loop: 4
					posn: (- (horse x?) 2) (- (horse y?) 17)
					setPri: 14
				)
			)
		)
	)
)

; (instance horseScript of Script
;    (properties)
;    (method (changeState newState)
;        = state newState
;        (switch (state)
;            (case 0 = cycles 1
;            )(case 1
;                = movingRight 1
;                (if(running)
;                    (horse:view(325)setMotion(MoveTo 300 170 horseScript)cycleSpeed(1))
;                )(else
;                    (horse:view(326)setMotion(MoveTo 300 170 horseScript)cycleSpeed(1))
;                )
;            )(case 2
;                = movingRight 0
;                (if(running)
;                    (horse:view(325)setMotion(MoveTo 70 170 horseScript))
;                )(else
;                    (horse:view(326)setMotion(MoveTo 70 170 horseScript))
;                )
;            )(case 3
;                (horseScript:changeState(1))
;            )
;        )
;    )
; )
(instance wizardScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(PrintWiz 125 2)
				; Print("You may be good with a sword, but can you stop my binding magic?" #at -1 20 #title "Wizard:")
				(wizard
					view: 340
					loop: 1
					cel: 0
					setCycle: End wizardScript
					cycleSpeed: 2
				)
			)
			(2
				(= cycles 15)
				(wizard loop: 3 setCycle: Fwd)
				(gEgo hide:)
				(alterEgo
					show:
					loop: 1
					posn: (gEgo x?) (gEgo y?)
					setMotion: MoveTo 263 167
				)
			)
			(3
				(wizard loop: 2 setCycle: Fwd)
				(alterEgo hide:)
				(throwEgo
					show:
					posn: (alterEgo x?) (alterEgo y?)
					setCycle: End wizardScript
					cycleSpeed: 2
				)
				(= throwing 1)
			)
			(4
				(= cycles 16)
				(= throwing 0)
				(throwEgo
					posn: (+ (throwEgo x?) 5) (throwEgo y?)
					loop: 3
					setCycle: Fwd
					cycleSpeed: 3
				)
			)
			(5
				(PrintWiz 125 3)
				(PrintWiz 125 4)
				; Print("Now what should I do with you?" #at -1 20 #title "Wizard:")
				; Print("I know! I should change you into a pig. That seems fitting." #at -1 20 #title "Wizard:")
				(wizard loop: 4 cel: 0 setCycle: End wizardScript)
			)
			(6
				(= cycles 10)
				(wizard loop: 5 setCycle: Fwd)
			)
			(7
				(= cycles 11)
				(PrintWiz 125 5)
			)
			; Print("Say hello to the 'Great Wizard' for me! Ha ha ha!" #at -1 20 #title "Wizard:")
			(8
				(arrow
					show:
					xStep: 45
					setMotion: MoveTo (wizard x?) (- (wizard y?) 20) wizardScript
				)
			)
			(9
				(= hitWithArrow 1)
				(= horseOnScreen 1)
				(arrow hide:)
				(wizard loop: 6 cel: 0 setCycle: End wizardScript)
				(horse
					show:
					posn: 319 153
					setCycle: Walk
					setMotion: MoveTo 270 153
				)
			)
			(10
				(= cycles 10)
				(= hitWithArrow 0)
				(wizard loop: 7)
			)
			(11
				(= cycles 15)
				(= gWndColor 14) ; clYELLOW
				(= gWndBack 1) ; clDARKBLUE
				(Print
					{Let's get out of here while we still have time!}
					#at
					-1
					20
					#title
					{Leah:}
				)
				(= gWndColor 0) ; clBLACK
				(= gWndBack 15) ; clWHITE
				(throwEgo view: 409 loop: 1 cel: 1 setCycle: End)
			)
			(12
				(= cycles 5)
				(PrintWiz 125 6)
				(PrintWiz 125 7)
			)
			; Print("You won't make it far, I have soldiers of Ishvi everywhere, and I will personally see to your death." #at -1 20 #title "Wizard:")
			; Print("When I'm finished with you, you're going to wish I turned you into something as pleasant as a pig!" #at -1 20 #title "Wizard:")
			(13 (gRoom newRoom: 76))
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
(instance julyn of Act
	(properties
		y 160
		x 6
		view 308
	)
)

(instance alterEgo of Act
	(properties
		y 1
		x 1
		view 341
	)
)

(instance strawTargets of Prop
	(properties
		y 53
		x 250
		view 110
	)
)

(instance throwEgo of Prop
	(properties
		y 1
		x 1
		view 341
		loop 1
	)
)

(instance wizard of Act
	(properties
		y 155
		x 187
		view 313
		loop 1
	)
)

(instance horse of Act
	(properties
		y 170
		x 38
		view 326
	)
)

(instance leah of Prop
	(properties
		y 150
		x 38
		view 318
		loop 4
	)
)

(instance soldier of Act
	(properties
		y 135
		x 120
		view 323
		loop 2
	)
)

(instance soldier2 of Act
	(properties
		y 185
		x 72
		view 333
		loop 3
	)
)

(instance arrow of Act
	(properties
		y 115
		x 319
		view 152
	)
)
