;;; Sierra Script 1.0 - (do not remove this comment)
; + 18 SCORE //
(script# 69)
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
	rm069 0
)

(local

; Prison cell

	nearPrisoner =  0
	[gotBlindfold 6]
	oublietteOpen =  0
	numBlindfolds =  0
	moving =  0
	countdown =  0
	julynFree =  0
	gyreHere =  0
	dartShot =  0
	gameOver =  0
)

(instance rm069 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 204)
		(SetUpEgo)
		(gEgo init:)
		(oubliette
			init:
			ignoreActors:
			setPri: 1
			setScript: oublietteScript
		)
		(oSwitch init:)
		(prisoner1 init: setScript: stepAsideScript)
		(prisoner2 init:)
		(prisoner3 init:)
		(prisoner4 init:)
		(prisoner5 init:)
		(blinderProp init: ignoreActors: setScript: pickUpScript)
		(blinderCord init: setPri: 15 hide:)
		(rope init: hide: setPri: 1)
		(julyn init: hide: setPri: 2 ignoreActors:)
		(gyre
			init:
			hide:
			ignoreControl: ctlWHITE
			setCycle: Walk
			setScript: gyreScript
		)
		(alterEgo init: hide:)
		(ring
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
			setPri: 15
			setScript: ringScript
		)
		(dart
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
			setScript: dartScript
		)
		(switch gPreviousRoomNumber
			(103
				(gEgo posn: 161 98 loop: 2)
				(gyre init: show: loop: 4 posn: 167 126 ignoreActors:)
				(julyn init: show: loop: 1 posn: 270 114)
				(prisoner1 loop: 1)
				(prisoner2 loop: 1)
				(prisoner3 loop: 1)
				(prisoner4 loop: 1)
				(prisoner5 loop: 1)
				(gGame changeScore: 5)      ; 5 points less for killing him instead of tricking him
				(RoomScript changeState: 1)
				(gTheMusic number: 109 loop: -1 play:)
			)
			(else 
				(gEgo posn: 163 159 loop: 3)
			)
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1 (= cycles 7)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)
			(2
				(gEgo setMotion: MoveTo 163 133 RoomScript)
				(julyn
					setCycle: Walk
					setMotion: MoveTo 173 133
					setPri: 13
				)
			)
			(3
				(= cycles 7)
				(PrintHero 69 48) ; #title "You say:")
				(julyn setCycle: Walk setMotion: MoveTo 173 170)
				(gEgo setMotion: MoveTo 163 170)
			)
			(4 
				(= gAutosave 0) ; don't want to autosave during ending
				(gRoom newRoom: 8)
			)
		)
	)
	
	(method (handleEvent pEvent &tmp dyingScript)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (checkEvent pEvent oubliette)
					(if oublietteOpen
						(PrintOther 69 0)
					else
						(PrintOther 69 2)
					)
				)
				(if (checkEvent pEvent prisoner1) (PrintOther 69 1))
				(if (checkEvent pEvent prisoner2) (PrintOther 69 1))
				(if (checkEvent pEvent prisoner3) (PrintOther 69 1))
				(if (checkEvent pEvent prisoner4) (PrintOther 69 1))
				(if (checkEvent pEvent prisoner5) (PrintOther 69 1))
				(if (checkEvent pEvent blinderProp)
					(PrintOther 69 3)
					(PrintOther 69 43)
				)
				(if (checkEvent pEvent oSwitch) (PrintOther 69 4))
			)
		)
		(if (Said 'smell[/!*]')
			(PrintOther 69 68)
		)
		(if (Said 'talk>')
			(if (Said '/princess,julyn,woman')
				(cond 
					(julynFree 
						(if countdown (PrintOther 69 5)
						else (PrintJulyn 69 6))
					)
					(oublietteOpen (PrintJulyn 69 7))
					(else (PrintOther 69 8))
				)
			)
			(if (Said '/prisoner') (PrintOther 69 37))
			(if (Said '/man') 
				(if gyreHere
					(PrintGyre 69 65)	
				else
					(PrintOther 69 37)
				)
			)
			(if (Said '/gyre') 
				(if gyreHere
					(PrintGyre 69 65)	
				else
					(PrintOther 69 66)
				)
			)
		)
		(if (Said '(ask<about)/*')
			(if oublietteOpen
				(PrintJulyn 69 46)
			else                  ; Sorry, there's no time to talk about that.
				(Print {No one replies.} #at -1 10)
			)
		)
		(if (Said 'wear/blindfold') (PrintOther 69 60))
		(if
			(or
				(Said 'climb,enter,jump/trapdoor,door,oubliette,hole')
				(Said '(climb,jump)<down')
			)
			(PrintOther 69 61)
		)
		(if (Said 'use/cape') (PrintOther 69 62))
		(if (Said 'search/man,prisoner') (PrintOther 69 63))
		(if (Said 'look<down')
			(if oublietteOpen
				(PrintOther 69 13)
			else
				(PrintOther 69 14)
			)
		)
		(if (Said 'look>')
			(if (Said '/prisoner,man') (PrintOther 69 10))
			(if (Said '/princess,julyn')
				(cond 
					(julynFree
						(PrintOther 69 11)
						(Print 69 12 #title {You think:} #width 280 #at -1 8)
					)
					(oublietteOpen (PrintOther 69 13))
					(else (PrintOther 69 14))
				)
			)
			(if (Said '/trapdoor,door,oubliette,hole,grate')
				(if oublietteOpen
					(PrintOther 69 41)
				else
					(PrintOther 69 2)
				)
			)
			(if (Said '/lever,switch,wall') (PrintOther 69 4))
			(if (Said '/blindfold')
				(if (== numBlindfolds 6)
					(PrintOther 69 69)	
				else
					(PrintOther 69 15)
				)
			)
			(if (Said '/rope')
				(if (== numBlindfolds 6)
					(PrintOther 69 69)	
				else
					(PrintOther 69 70)
				)	
			)
			(if (Said '/bucket') (PrintOther 69 58))
			(if (Said '/window,sky') (PrintOther 69 64))
			(if (Said '/floor')
				(PrintOther 69 49)
				(if (not (== [gotBlindfold 5] 1)) (PrintOther 69 50))
			)
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(PrintOther 69 16)
				(PrintOther 69 17)
			)
		)
		(if (Said 'wear,use,(put<on)/goggles')
			(if gyreHere
				(PrintGyre 69 67)
				(gyreScript changeState: 6)	
			else
				(Print 0 25)
			)	
		)
		(if (Said 'use,shoot,blow/dart,dartgun,gun,(dart<gun)')
			; ++gApple // for playtest purposes
			(if (> gApple 0)
				(if gyreHere
					(-- gApple)
					(= dartShot 1)
					; Print(69 38 #width 280 #at -1 8)
					; (gyreScript:changeState(6)) // go to battle
					(dartScript changeState: 1)
				else
					(PrintOther 69 40)
				)
			else
				(PrintOther 69 39)
			)
		)
		(if (Said 'lower,drop,(put,lie,lay<down)/sword')
			(if gyreHere
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 610
					register: {Bold - yet ineffective - strategy.}
				)
				(gGame setScript: dyingScript)	
			else
				(PrintCantDoThat)
			)
		)
		(if (Said 'drop,show,use,give,(tell<about)/ring')
			(if (gEgo has: INV_RING)
				(if countdown
					(gyreScript changeState: 7)
					(ringScript changeState: 1)
				else
					(PrintOther 69 19)
				)
			else
				(PrintDHI)
			)
		)
		(if (Said 'tie/rope/*')
			(if (== numBlindfolds 6)
				(PrintOther 69 71)	
			else
				(PrintOther 69 70)
			)
		)
		(if (Said 'lower,use/rope,blindfold')
			(if oublietteOpen
				(if (& (gEgo onControl:) ctlGREY)
					(cond 
						((== numBlindfolds 6)
							(++ numBlindfolds)
							(blinderProp hide:)
							(pickUpScript changeState: 6)
						)
						((> numBlindfolds 6) (PrintOther 69 57))
						(else (PrintOther 69 20))
					)
				else
					(PrintOther 69 21)
				)
			else
				(PrintOther 69 22)
			)
		)
		(if (Said 'use,pull,flip/lever,switch')
			(if (<= (gEgo distanceTo: oSwitch) 35)
				(PrintOK)
				(if oublietteOpen
					(oublietteScript changeState: 5)
				else                                 ; closing
					(oublietteScript changeState: 1)
				)
			else                                     ; openning
				(PrintNCE)
			)
		)
		(if (Said 'run') (Print 0 88))
		(if (or (Said 'look,use,read,open/portal,map')
				(Said 'map'))
			(PrintOther 69 47)
		)
		(if (Said 'climb/down') (PrintOther 69 51))
		(if (Said 'open/door,oubliette,hole,grate')
			(cond 
				(oublietteOpen (PrintOther 69 23))
				((<= (gEgo distanceTo: oSwitch) 35) (PrintOK) (oublietteScript changeState: 1))
				(else (PrintNCE))
			)
		)
		(if (Said 'close/door,oubliette,hole,grate')
			(cond 
				((not oublietteOpen) (PrintOther 69 23))
				((<= (gEgo distanceTo: oSwitch) 35) (PrintOK) (oublietteScript changeState: 5))
				(else (PrintNCE))
			)
		)
		(if
			(or
				(Said 'free,release,save,help/man,prisoner')
				(Said 'break/chain')
			)
			(PrintOther 69 42)
		)
		(if (Said '(pick<up),take,remove,untie/blindfold')
			(switch nearPrisoner
				(0 (PrintOther 69 24))
				(1
					(if [gotBlindfold 0]
						(PrintOther 69 25)
					else
						(PrintOther 69 26)
						(prisoner1 loop: 1)
						(= [gotBlindfold 0] 1)
						(++ numBlindfolds)
						(blindfoldPtCheck)
					)
				)
				(2
					(if [gotBlindfold 1]
						(PrintOther 69 25)
					else
						(PrintOther 69 26)
						(prisoner2 loop: 2)
						(= [gotBlindfold 1] 1)
						(++ numBlindfolds)
						(blindfoldPtCheck)
					)
				)
				(3
					(if [gotBlindfold 2]
						(PrintOther 69 25)
					else
						(PrintOther 69 26)
						(prisoner3 loop: 1)
						(= [gotBlindfold 2] 1)
						(++ numBlindfolds)
						(blindfoldPtCheck)
					)
				)
				(4
					(if [gotBlindfold 3]
						(PrintOther 69 25)
					else
						(PrintOther 69 26)
						(prisoner4 loop: 1)
						(= [gotBlindfold 3] 1)
						(++ numBlindfolds)
						(blindfoldPtCheck)
					)
				)
				(5
					(if [gotBlindfold 4]
						(PrintOther 69 25)
					else
						(PrintOther 69 26)
						(prisoner5 loop: 1)
						(= [gotBlindfold 4] 1)
						(++ numBlindfolds)
						(blindfoldPtCheck)
					)
				)
				(6
					(if [gotBlindfold 5]
						(PrintOther 69 27)
					else
						(pickUpScript changeState: 1)
					)
				)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(if (not moving)
				(stepAsideScript changeState: 1)
				(= moving 1)
			)
		)
		(if oublietteOpen
			(if (not gameOver)
				(gEgo observeControl: ctlSILVER)
			else
				(gEgo ignoreControl: ctlSILVER)
			)
		else
			(gEgo ignoreControl: ctlSILVER)
		)
		(if (> numBlindfolds 0)
			(blinderCord show: cel: numBlindfolds)
		else
			(blinderCord hide:)
		)
		(cond 
			((<= (gEgo distanceTo: prisoner1) 25) (= nearPrisoner 1))
			((<= (gEgo distanceTo: prisoner2) 11) (= nearPrisoner 2))
			((<= (gEgo distanceTo: prisoner3) 25) (= nearPrisoner 3))
			((<= (gEgo distanceTo: prisoner4) 25) (= nearPrisoner 4))
			((<= (gEgo distanceTo: prisoner5) 25) (= nearPrisoner 5))
			((<= (gEgo distanceTo: blinderProp) 40) (= nearPrisoner 6))
			(else (= nearPrisoner 0))
		)
	)
)

(instance dartScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(= cycles 5)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(alterEgo
					show:
					view: 223
					loop: 2
					cel: 1
					posn: (gEgo x?) (gEgo y?)
				)
			)
			(2
				(dart
					show:
					posn: (gEgo x?) (gEgo y?)
					yStep: 5
					setMotion: MoveTo (gEgo x?) (- (gyre y?) 10) dartScript
				)
			)
			(3
				(= cycles 15)
				(gyre loop: 5 cel: 0 setCycle: End)
				(dart
					view: 83
					xStep: 5
					setCycle: Walk
					setMotion: MoveTo (- (gyre x?) 45) (+ (gyre y?) 45)
				)
				(alterEgo cel: 0)
			)
			(4
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(dart hide:)
				(gyre loop: 3 cel: 5)
				(gyreScript changeState: 6)
			)
		)
	)
)
                                            ; go to battle
(instance ringScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
; OPENNING THE OUBLIETTE
			(1
				(ring
					show:
					posn: (- (gEgo x?) 10) (gEgo y?)
					yStep: 8
					setMotion: MoveTo 151 118 ringScript
				)
				(gEgo put: 18 69)
			)
			(2
				(= cycles 5)
				(PrintGyre 69 53)
				(PrintGyre 69 52)
			)                    ; ("Where did you get that? That belongs to my wife." #title "He says:"#at -1 140 #width 280)
			(3
				(gyre setMotion: MoveTo (gyre x?) 124 ringScript)
			)
			(4
				(= cycles 5)
				(Print {NOW!} #title {You say:} #at -1 20)
				(julyn loop: 3)
				(oublietteScript changeState: 1)
			)
			(5
				(= cycles 10)
				(gyre
					view: 339
					yStep: 5
					setMotion: MoveTo 166 190
					setPri: 2
					ignoreActors:
				)
				(ring hide:)
			)
			(6
				(oublietteScript changeState: 5) ; close the oubliette
				(gGame changeScore: 10)
				(= gMap 0)
				(RoomScript changeState: 1)
				(= gameOver 1)
			)
		)
	)
)

(instance oublietteScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
; OPENING THE OUBLIETTE
			(1
				(oSwitch setCycle: End oublietteScript)
			)
			(2 (= cycles 4))
			(3
				(oubliette setCycle: End self cycleSpeed: 2)
				(= oublietteOpen 1)
				(gTheSoundFX number: 204 play:)	
			)
			(4 (ShakeScreen 1)
				(gTheSoundFX number: 200 play:)	
			)
; CLOSING THE OUBLIETTE
			(5
				(oSwitch setCycle: Beg oublietteScript)
			)
			(6 (= cycles 4))
			(7
				; ShakeScreen(1)
				(oubliette setCycle: Beg self)
				(= oublietteOpen 0)
				(gTheSoundFX number: 204 play:)
			)
			(8 (ShakeScreen 1)
				(gTheSoundFX number: 200 play:)	
			)
		)
	)
)

(instance gyreScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(gyre show: setMotion: MoveTo 163 150 gyreScript)
			)
			(2
				(= seconds 3)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= gyreHere 1)
				(= gMap 1) ; cannot move ego
				(PrintGyre 69 30) ; #title "He says:"#at -1 140 #width 280)
				(PrintGyre 69 31) ; #title "He says:"#at -1 140 #width 280)
				(PrintGyre 69 32) ; #title "He says:"#at -1 140 #width 280)
				(= countdown 1)
			)
			(3
				(= seconds 3)
				(if (not dartShot) (PrintGyre 69 54))
			)
			(4
				(= seconds 3)
				(if (not dartShot) (PrintGyre 69 55))
			)
			(5
				(= seconds 3)
				(if (not dartShot) (PrintGyre 69 56))
			)
			(6
				(PrintGyre 69 33)

				(= gBatNum 200)
				(= gSpdDmn 1)
				(= gNoRun 1)
				(= gMap 0)
				(= gOpHealth gBatNum)
				(gRoom newRoom: 103)
			)
			(7)
		)
	)
)

(instance pickUpScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo (- (blinderProp x?) 15) (blinderProp y?) pickUpScript
					ignoreControl: ctlWHITE
				)
			)
			(2
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					posn: (gEgo x?) (gEgo y?)
					setCycle: End pickUpScript
					cycleSpeed: 2
				)
			)
			(3
				(= cycles 8)
				(Print 69 28 #width 280 #at -1 20)
				(blinderProp hide:)
				(= [gotBlindfold 5] 1)
				(++ numBlindfolds)
			)
			(4
				(alterEgo setCycle: Beg pickUpScript)
			)
			(5
				(gEgo show: observeControl: ctlWHITE)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(blindfoldPtCheck)
			)
			(6       ; droping the line to Julyn
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 181 100 pickUpScript)
			)
			(7
				(gEgo hide:)
				(alterEgo
					init:
					show:
					view: 232
					loop: 1
					posn: (gEgo x?) (gEgo y?)
					setCycle: End pickUpScript
					cycleSpeed: 1
				)
			)
			(8
				(= cycles 15)
				(PrintHero 69 29) ; #title "You say:" #width 280 #at -1 20)
				(rope show:)
				(= julynFree 1)
			)
			(9
				(julyn
					show:
					view: 334
					posn: 160 170
					setMotion: MoveTo 160 130 pickUpScript
					setCycle: Walk
				)
			)
			(10
				(julyn view: 308 setMotion: MoveTo 165 98 pickUpScript)
			)
			(11
				(julyn loop: 0)
				(alterEgo setCycle: Beg pickUpScript)
			)
			(12
				; (send gEgo:show()loop(1)observeControl(ctlWHITE))
				; (alterEgo:hide())
				(rope hide:)
				(stepAsideScript changeState: 6)
			)
		)
	)
)

(instance stepAsideScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo (gEgo x?) (- (gEgo y?) 10) stepAsideScript
				)
			)
			(2 (= cycles 3))
			(3
				(= cycles 4)
				(Print 69 34 #title {You think:} #at -1 10)
			)
			(4
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(= moving 0)
			)
			(6
				(= seconds 3)    ; kiss
				(julyn view: 2 loop: 5 cel: 0 setCycle: End cycleSpeed: 1)
				(alterEgo
					view: 407
					loop: 9
					cel: 0
					setCycle: End
					cycleSpeed: 3
				)
			)
			(7
				(julyn setCycle: Beg)
				(alterEgo
					loop: 10
					cel: 0
					setCycle: End stepAsideScript
					cycleSpeed: 5
				)
				(gGame changeScore: 5)
				(if (not gHardMode)
					(= gHlth gMaxHlth)	
				)
			)
			(8
				(= cycles 8)
				(PrintJulyn 69 36)
				(gEgo show: loop: 1 observeControl: ctlWHITE)
				(alterEgo hide:)
			)
			(9
				(Print 69 35 #title "You say:" #at -1 130)
				(PrintJulyn 69 44)
				(if (gEgo has: 18)
					(PrintHero 69 45)
				else                 ; #title "You say:")
					(Print {I'm not sure...} #title {You say:} #at -1 20)
				)
				(julyn
					view: 308
					cycleSpeed: 0
					setCycle: Walk
					setMotion: MoveTo 239 124 stepAsideScript
					ignoreActors:
					ignoreControl: ctlWHITE
					setPri: 12
				)
				(gEgo loop: 0)
			)
			(10
				(oublietteScript changeState: 5)
				(julyn setMotion: MoveTo 240 124)
				(gEgo setMotion: MoveTo 163 101 stepAsideScript)
			)
			(11
				(= cycles 30)
				(julyn loop: 1)
				(gEgo loop: 2)
			)
			(12
				(= cycles 20)
				(= gWndColor 13) ; clYELLOW
				(= gWndBack 5) ; clDARKBLUE
				(PrintJulyn 69 59) ; #title "Julyn whispers:")
				(= gWndColor 0) ; clBLACK
				(= gWndBack 15)
			)                 ; clWHITE
			(13 (gyreScript changeState: 1))
		)
	)
)

(procedure (blindfoldPtCheck)
	(if (== numBlindfolds 6) (gGame changeScore: 3))
)

(procedure (checkEvent event view)
	(if
		(and
			(> (event x?) (view nsLeft?))
			(< (event x?) (view nsRight?))
			(> (event y?) (view nsTop?))
			(< (event y?) (view nsBottom?))
		)
	)
)

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
		120
	)
	(= gWndColor 0) ; clBLACK
	(= gWndBack 15)
)
                  ; clWHITE
(procedure (PrintGyre textRes textResIndex)
	(= gWndColor 0)
; clBLACK
	(= gWndBack 12)                             ; clMAROON
	(Print textRes textResIndex #at -1 20 #title {He says:})
	(= gWndColor 0)
; clBLACK
	(= gWndBack 15)
)
                                                ; clMAROON
(procedure (PrintHero textRes textResIndex)
	(Print textRes textResIndex #at -1 20 #title {You say:})
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 100)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
	)
)

(instance julyn of Act
	(properties
		y 149
		x 166
		view 308
	)
)

(instance gyre of Act
	(properties
		y 189
		x 161
		view 338
	)
)

(instance ring of Act
	(properties
		y 189
		x 161
		view 153
	)
)

(instance oubliette of Prop
	(properties
		y 131
		x 161
		view 72
		loop 3
	)
)

(instance rope of Prop
	(properties
		y 186
		x 161
		view 81
	)
)

(instance oSwitch of Prop
	(properties
		y 110
		x 250
		view 72
		loop 2
	)
)

(instance prisoner1 of Prop
	(properties
		y 98
		x 125
		view 422
	)
)

(instance prisoner2 of Prop
	(properties
		y 110
		x 226
		view 423
		loop 1
	)
)

(instance prisoner3 of Prop
	(properties
		y 118
		x 88
		view 424
	)
)

(instance prisoner4 of Prop
	(properties
		y 154
		x 266
		view 425
	)
)

(instance prisoner5 of Prop
	(properties
		y 158
		x 50
		view 426
	)
)

(instance blinderProp of Prop
	(properties
		y 134
		x 220
		view 77
	)
)

(instance blinderCord of Prop
	(properties
		y 190
		x 160
		view 77
		loop 4
	)
)

(instance dart of Act
	(properties
		y 135
		x 161
		view 57
	)
)

(instance alterEgo of Prop
	(properties
		y 164
		x 68
		view 232
	)
)
