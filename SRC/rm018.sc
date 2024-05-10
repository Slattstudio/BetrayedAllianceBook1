;;; Sierra Script 1.0 - (do not remove this comment)
; Score +5 //
(script# 18)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use window)
(use menubar)

(public
	rm018 0
)

(local

; Wizard's Magic Room

	portVis =  0 ; portal visisble
	rando
	kik =  0 ; kicked the ball
	mirLk =  0 ; looked at mirror
	wtm =  0 ; walk through mirror
	
	book =  30
	doorOpen =  0
	onRed =  0
	waiting =  0
	pickUp =  0
	takingMarble =  0
	
	teleportAnimation = 0
; snd
	[planetMoveX 12] = [1 -2 -6 -10 -6 -2 1 4 8 12 8 4] ; (2 -4 -12 -20 -12  -4   2   8 16 24 16 8)
	[planetMoveY 12] = [4 3 2 -1 -4 -5 -6 -5 -4 -1 2 3]
)                                                 ; (8  7   4  -2  -8 -11 -12 -11 -8 -2  4 7)

(instance rm018 of Rm
	(properties
		picture scriptNumber
	)
	
	(method (init)
		(super init:)
		(self
			setScript: RoomScript
			setRegions: 200
			setRegions: 204
		)
		(switch gPreviousRoomNumber
			(10
				(gEgo init: posn: 112 130 loop: 1)
				(RoomScript changeState: 1)
			)
			(else 
				(gEgo init: posn: 115 135 loop: 1)
				(mapItem init: hide:)
				(portal init: loop: 1)
				(RoomScript changeState: 5)
			)
		)
		(SetUpEgo)
		(= gEgoRunning 0)
		(RunningCheck)
		(gEgo init: observeControl: ctlMAROON)
		(portal init: ignoreActors: setPri: 1 setScript: WizWalk)
		(mapItem init: setPri: 9)
		(mirrorEgo
			init:
			hide:
			ignoreActors:
			ignoreControl: ctlWHITE
			setPri: 7
		)
		(mirrorShine init: hide: ignoreActors:)

		(if (not (gEgo has: INV_MARBLES))
			(planet
				init:
				ignoreActors:
				setPri: -1
				xStep: 2
				setCycle: Fwd
				setScript: orbit
			)
			(sun init: ignoreActors: setPri: -1 setScript: sunShot)
		)
		
		(ProgramControl)
		(SetCursor 997 (HaveMouse))
		(= gCurrentCursor 997)
		(gTheMusic number: 18 loop: -1 play:)
		(= gMap 0)
		(= gArcStl 0)
		(TheMenuBar state: ENABLED)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState &tmp button dyingScript)
		(= state mainState)
		(switch state
			(0)
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(alterEgo
					init:
					view: 409
					loop: 1
					posn: 112 130
					setCycle: End self
					cycleSpeed: 2
					ignoreActors:
				)
			)
			(2
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo show: loop: 2 setMotion: MoveTo (gEgo x?) 145 self)
				(alterEgo hide:)
			)
			(3
				(= cycles 2)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(4
				; = gVertButton 1
				(= gWndColor 0)
				(= gWndBack 14)
				(= button
					(Print
						10
						1
						#button
						{ Veteran_}
						1
						#button
						{ Novice_}
						0
						#font
						4
						#at
						140
						-1
						#width
						160
					)
				)
				(= gHardMode button)
				(= gVertButton 0)
				(= gWndColor 0) ; clBLACK
				(= gWndBack 15) ; clWHITE
				(if (not gHardMode)
					(Print 997 15 #title {How to play:} #font 4 #width 280)
				)
			)
			; teleporting in animation
			(5
				(gEgo hide:)
				(alterEgo
					init:
					view: 128
					loop: 0
					cel: 10
					posn: 115 135
					setCycle: Beg self
					cycleSpeed: 2
					ignoreActors:
				)
				(gTheSoundFX number: 205 play:)
				(= gTeleporting 0)
			)
			(6
				(gEgo show: loop: 2)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(8
				(if (not kik)
					(ProgramControl)
					(SetCursor 997 (HaveMouse))
					(= gCurrentCursor 997)
					(if (< (sun x?) (gEgo x?))
						(gEgo
							setMotion: MoveTo (+ (sun x?) 20) (sun y?) RoomScript
							ignoreControl: ctlWHITE
						)
						(alterEgo view: 232 loop: 1)
					else
						(gEgo
							setMotion: MoveTo (- (sun x?) 13) (sun y?) RoomScript
							ignoreControl: ctlWHITE
						)
						(alterEgo view: 232 loop: 0)
					)
					(= kik 1)
					(= pickUp 0)
				else
					(ProgramControl)
					(= kik 2)
					(= waiting 1)
					(= pickUp 1)
				)
			)
			; Print("infinite?")
			(9       ; bend down to take marble
				(= takingMarble 1)
				(gEgo hide:)
				(alterEgo
					show:
					posn: (gEgo x?) (gEgo y?)
					setCycle: End self
					cycleSpeed: 2
				)
				(if (& (gEgo onControl:) ctlSILVER)
					(if (and (not gWizRm) (gEgo has: 7))
						(mirrorEgo view: 137)
					else						
						(mirrorEgo view: 232)
					)
					(mirrorEgo
						show:						
						posn: (gEgo x?) 110
						setCycle: End
						cycleSpeed: 2
						loop: (alterEgo loop?)
					)
				)
			)
			(10
				(alterEgo setCycle: Beg self)
				(mirrorEgo setCycle: Beg)
				; retrive the marbles
				(PrintOK)
				(Print 18 3 #icon 261 #title {Cosmic Marbles})
				; Print(18 3 #width 280 #at -1 8)
				(sun hide:)
				(planet hide:)
				(gEgo get: 9)
				(gGame changeScore: 1)
				(= gErth 1)
				(= gSun 1)
				(= gMarbleNum 2)
				((gInv at: 9) count: gMarbleNum)
			)
			(11
				(= takingMarble 0)
				(gEgo
					show:
					observeControl: ctlWHITE
					loop: (alterEgo loop?)
				)
				(alterEgo hide:)
				(mirrorEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			; falling over dead
			(12	(= cycles 5)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(alterEgo init: show: view: 105 loop: 0 cel: 0 posn: (+ (gEgo x?) 15) (gEgo y?))
				(PrintOther 18 88)	
				(PrintOther 18 89)
					
			)
			(13
				(alterEgo setCycle: End self cycleSpeed: 2)	
			)
			(14	(= cycles 10)
				; thud
				(gTheSoundFX number: 200 play:)
				(gTheMusic stop:)
				(ShakeScreen 1)
			)
			(15
				(= dyingScript (ScriptID DYING_SCRIPT))
				(dyingScript
					caller: 607
					register:
						{That's one way to start an adventure. Unfortunately, it's also a way to end an adventure.}
				)
				(gGame setScript: dyingScript)		
			)
			; walked into closet
			(16 (= cycles 10)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(ShakeScreen 1)
				
				(gTheSoundFX number: 200 play:)
				(= onRed 1)	
			)
			(17
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)	
				(Print 18 42 #width 280 #at -1 8)
			)
			(18	; teleporting Out animation in mirror
				(mirrorEgo
					show:
					view: 138
					loop: 0	
					cel: 0					
					posn: (gEgo x?) 110
					setCycle: End
					cycleSpeed: 3						
				)	
			)
			; wizard walk through mirror
			(19	(= cycles 4)
				(gTheSoundFX number: 205 play:)
				(mirrorShine show: setCycle: Fwd)	
			)
			(20
				(mirrorShine hide:)	
			)
		)
	)
	
	(method (handleEvent pEvent &tmp i [buffer 50] dyingScript)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (checkEvent pEvent 92 123 65 117) ; tapestry 
					(PrintOther 18 6)
				) 
				(if
					(checkEvent pEvent (mapItem nsLeft?) (mapItem nsRight?) (mapItem nsTop?) (mapItem nsBottom?))
					(if (gEgo has: 7) 
					else 
						(PrintOther 18 24)
					)
				)                 
				(if
					(checkEvent pEvent (sun nsLeft?)
						(sun nsRight?)
						(sun nsTop?)
						(sun nsBottom?)
					)
					(if (not (gEgo has: 9)) (PrintOther 18 5))
				)                        ; #width 280 #at -1 8)
				
				(if (checkEvent pEvent 181 199 101 110)       ; cups of powders
					(PrintOther 18 76)
				)                     
				(if (checkEvent pEvent 153 173 146 155)     ; note
					(if (& (gEgo onControl:) ctlSILVER)
						(PrintOther 18 37) ; #width 280 #at -1 8)
						(Print 18 38 #width 100 #at 20 -1 #font 4)
					else
						(PrintOther 18 46)
					)
				)                      
				(cond 
					(
						(== ctlMAROON
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)
						(PrintOther 18 80)
					)
					((checkEvent pEvent 29 51 90 138)        ; books
						(if (< book 35)
							(Print 18 book #width 280 #at -1 8)
							(++ book)
						else
							(= book 30)
							(Print 18 book #width 280 #at -1 8)
							(++ book)
						)
					)
				)
				; (if ( checkEvent(pEvent 148 183 68 121) )   // Mirror
				(if
					(== ctlGREY (OnControl ocSPECIAL (pEvent x?) (pEvent y?)))
					(PrintOther 18 77)
				)
				(if
					(== ctlYELLOW (OnControl ocPRIORITY (pEvent x?) (pEvent y?)))
					(PrintOther 18 78)
				)
				; Print(18 79 #title "Cover:" #font 8 #width 120)
				(if
					(==
						ctlNAVY
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)
					(WizTalk)
				)
			)
		)
		
		(if (Said 'read,open/book')
			(if (and (& (gEgo onControl:) ctlSILVER) (== (gEgo loop?) 2)) ; in front of blue book
				(PrintOther 18 91)		
			else
				(if (& (gEgo onControl:) ctlNAVY) ; in front of book shelf
					(PrintOther 18 49)	
				else
					(if [gMissingBooks 2]	; if has missing book
						(viewBooks)	
					else
						(PrintOther 18 49)
					)
				)
			)
		)
		
		(if (Said 'drink/(flask[<ye]),potion,liquid')
			(if (or (& (gEgo onControl:) ctlGREY) (& (gEgo onControl:) ctlCYAN))
				(RoomScript changeState: 12)
			else
				(PrintNCE)
			)	
		)
		(if (Said 'get/(flask<ye)') (Print 18 75))
		(if (Said '(hide,get)<behind/tapestry')
			(if gPond
				(PrintOther 18 86)	
			else
				(PrintOther 18 85)
			)
		)
		(if (Said 'open,(look<in)/(drawer,desk)')
			(if (<= (gEgo distanceTo: mapItem) 60)
				(PrintOther 18 39)
				(PrintOther 18 40)
				(PrintOther 18 45)
			else
				(PrintNCE)
			)
		)
		(if (Said 'talk/(man,wizard)') (WizTalk))
		(if (Said '(walk<through,into)/mirror')
			(PrintOther 18 8)
		)
		(if (Said 'run') (PrintOther 18 66))

		(if (Said 'break/mirror') (PrintOther 18 71))
		(if (Said 'open,push,move,(look<behind)>')
			(if (Said '/mirror') (PrintOther 18 72))
			(if (Said '/tapestry') (PrintOther 18 74))
			(if (Said '/bookcase') (PrintOther 18 90))
		)
; (if(Said('use,ball<crystal'))
;            (if(<=(send gEgo:distanceTo(orb))45)
;                PrintOther(18 25)
;            )(else
;                PrintNCE()
;            )
;        )
		(if (not (gEgo has: 7)) ; map
			(if (Said 'look/map,picture')
				(PrintOther 18 24)
			)			
		)	
		(if (Said '(look,search,read)>')
			(if (Said '/flask,potion')
				(PrintOther 18 77)	
			)
			(if (Said '/marble,ball,sun')
				(if (gEgo has: 9)
					(Format @buffer {Marbles that act upon each other with solar system-like gravitational forces. You have %u.} gMarbleNum)
					(Print @buffer #title "marbles" #icon 261)
				else
					; Print(18 29 #width 280 #at -1 8)
					(PrintOther 18 5)
				)
			)
			; Print(18 5 #width 280 #at -1 8)
			(if (Said '/wall')
				(PrintOther 18 6)
				; Print(18 6 #width 280 #at -1 8)
				(if (not (gEgo has: INV_MAP)) (PrintOther 18 0))
			)
			(if (Said '/rug,tapestry') (PrintOther 18 6))
			(if (Said '/door') (Print 18 27 #at -1 8))
			
			
			; Print(18 24 #width 280 #at -1 8)
; (if (Said('/mask'))
;                PrintOther(18 26)
;                //Print(18 26 #width 280 #at -1 8)
;            )
			(if (Said '/reflection')
				(if (not gWizRm)
					(if (gEgo has: 7)
						(if (& (gEgo onControl:) ctlSILVER)      ; SILVER
							(Print 18 22 #width 280 #at -1 8)
							(WizWalk changeState: 2)
						else
							(PrintNCE)
						)
					else
						(PrintOther 18 73)
					)
				else
					(PrintOther 18 73)
				)
			)
			; Print(18 26 #width 280 #at -1 8)
; (if (Said('/(chess,board)'))
;                PrintOther(18 28)
;                //Print(18 28 #width 280 #at -1 8)
;            )
			(if (Said '/book')
				(if onRed
					(if (> [gMissingBooks 2] 0)
						(PrintOther 18 65)
					else
						(PrintOther 18 70)
					)
				else
					(if (and (& (gEgo onControl:) ctlSILVER) (== (gEgo loop?) 2)) ; in front of blue book
						(PrintOther 18 78)	
					else  
						(PrintOther 18 51)
					)
				)
			)
			(if (Said '/bookshelf')
				(PrintOther 18 51)
			)
			; Print(18 51 #width 280 #at -1 8)
			(if (Said '/portal') (PrintOther 18 48))
			; Print(18 48 #width 280 #at -1 8)
			(if (Said '/orb') (PrintOther 18 25))
			; Print(18 25 #width 280 #at -1 8)
			(if (Said '/(mirror,man,wizard)') (WizTalk))
			(if (Said '/reflection')
				(if (not gWizRm) (PrintOther 18 73) else (WizTalk))
			; Print(18 26 #width 280 #at -1 8)
			)
			(if (Said '/floor') 
				(PrintOther 18 12)
				(if (not (gEgo has:INV_MARBLES))
					(PrintOther 18 83)
				)
			)
			
			(if (Said '/pot,container') (PrintOther 18 82))
			(if (Said '/closet')
				(if onRed 
					(if [gMissingBooks 2]
						(PrintOther 18 57)
					else
						(PrintOther 18 43)
					) 
				else 
					(PrintOther 18 44)
				)
			)
			; Print(18 44 #width 280 #at -1 8)
			(if (Said '/(desk,paper,note,letter)')
				(PrintOther 18 37)
				(if (& (gEgo onControl:) ctlSILVER)
					(Print 18 38 #width 100 #at 20 -1 #font 4)
				else
					(PrintOther 18 81)
				)
			)
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(if onRed
					(if [gMissingBooks 2]
						(PrintOther 18 57)
					else
						(PrintOther 18 43)
					)
				else
					; Print(18 43 #width 280 #at -1 8)
					(PrintOther 18 11)
				)
			)
		)

		(if (Said '(pick<up),take>')
			(if (Said '/map,picture')
				(cond 
					((gEgo has: 7) (PrintOther 18 2))
					((<= (gEgo distanceTo: mapItem) 45)
						; Print(18 2 #width 280 #at -1 8)
						(mapItem hide:)
						(Print 18 4 #icon 259 0 0 #title {Map})
						; Print(18 4 #width 280 #at -1 8)
						(gEgo get: 7)
						(gGame changeScore: 1)
					)
					(else (PrintNCE))
				)
			)
			(if (Said '/flask') (PrintOther 18 87))
			(if (Said '/ball')
; (if(<=(send gEgo:distanceTo(orb))45)
;                    PrintOther(18 36)
;                    return
;                )(else
				(cond 
					((gEgo has: 9) (PrintOther 18 2))
					((<= (gEgo distanceTo: sun) 45) (RoomScript changeState: 8))
					; Print(18 2 #width 280 #at -1 8)
					(else (PrintNCE))
				; pick up marble animation
				)
			)
			; )
			(if (Said '/(marble,shooter)')
				(cond 
					((gEgo has: 9) (PrintOther 18 2))
					((<= (gEgo distanceTo: sun) 35) (RoomScript changeState: 8))
					; Print(18 2 #width 280 #at -1 8)
					(else (PrintNCE))
				; pick up marble animation
				)
			)
			(if (Said '/book')
				(if onRed
					(if (== [gMissingBooks 2] 0)
						(Print 18 47 #icon 984 1 1)
						(= [gMissingBooks 2] 1)
						(gGame changeScore: 2)
						(if (not (gEgo has: 20)) (gEgo get: 20))
					else
						(PrintATI)
					)
				else
					(if (& (gEgo onControl:) ctlSILVER)
						(PrintOther 18 84)	
					else
						(PrintOther 18 41)
					)
				)
			)
			; Print(18 41 #width 280 #at -1 8)
			(if (Said '/candle') (PrintOther 18 62))
			(if (Said '/*') (PrintOther 18 36))
		; this will handle "look anyword"
		)
		; Print(18 36 #width 280 #at -1 8)
		(if (Said 'open/door') (Print {There is no door.}))
	)
	
; USE ITEMS
; END USE ITEMS

	(method (doit)
		(super doit:)
		(if pickUp
			(if (not waiting) (RoomScript changeState: 8))
		; = kik 0
		)
		(if (& (gEgo onControl:) ctlSILVER)
			(if (or takingMarble gTeleporting)
				(gEgo mirrorEgo: 0)
			else
				(gEgo mirrorEgo: 123)
			)
			(if gTeleporting
				(if (not teleportAnimation)
					(self changeState: 18)	
					(= teleportAnimation 1)
				)
			)
		)
		(if (not (& (gEgo onControl:) ctlSILVER))
			(gEgo mirrorEgoDispose:)
		)

		(if (== (gEgo onControl:) ctlRED)      ; RED
			(if onRed
			else
				(self changeState: 16)
			)
		else
			(= onRed 0)
		)
		(if (<= (gEgo distanceTo: portal) 2)
			(if (not gWizRm)
				(if portVis
					(portal loop: 1)
					; (cardFlourish:changeState(2))
					(gGame changeScore: 1)
					(= gWizRm 1)
					(= gPond 1)
				)
			)
		)
		
		(if (& (Wizard onControl:) ctlBLUE)
			(self changeState: 19)
		)

		(if (not kik)
			(if (not waiting)
				(if (<= (gEgo distanceTo: sun) 9)
					(sunShot changeState: 2)
				)
			)
		)
	)
)

(instance orbit of Script
	(properties)
	
	(method (planetMove state)
		(planet
			setMotion:
				MoveTo
				(+ (sun x?) [planetMoveX state])
				(+ (sun y?) [planetMoveY state])
				orbit
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(12
				(= cycles 15)
				(orbit changeState: 0)
			)
			(else 
				(= cycles 15)
				(self planetMove: state)
			)
		)
	)
)

(instance sunShot of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(2
				(= cycles 10)
				(= kik 1)
				(= rando (Random 30 35))
				(cond 
					((> (gEgo x?) (sun x?))    ; Ego on the Right
						(if (> (sun x?) 95)
							(sun
								setCycle: Walk
								xStep: 5
								setMotion: MoveTo (- (sun x?) rando) (sun y?) sunShot
							)
						else
							(sun
								setCycle: Walk
								xStep: 5
								setMotion: MoveTo (+ (sun x?) rando) (sun y?) sunShot
							)
						)
					)
					((> (sun x?) 215) ; Ego on the Left
						(sun
							setCycle: Walk
							xStep: 5
							setMotion: MoveTo (- (sun x?) rando) (sun y?) sunShot
						)
					)
					(else
						(sun
							setCycle: Walk
							xStep: 5
							setMotion: MoveTo (+ (sun x?) rando) (sun y?) sunShot
						)
					)
				)
			)
			(3
				(sun cel: 0)
				(= kik 0)
				(= waiting 0)
			)
		)
	)
)

(instance WizWalk of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(2
				(= cycles 9)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)
			(3
				(PrintWiz 18 13)
				(gEgo setMotion: MoveTo 106 148 WizWalk)
			)
			(4	(= cycles 2)
				(gEgo loop: 0)
				
			)
			(5
				(Print 18 9 #at -1 20)
				(Wizard
					init:
					posn: 151 70
					setCycle: Walk
					setMotion: MoveTo 151 124 WizWalk
					ignoreControl: ctlWHITE
					setPri: 7
				)
			)
			(6 (= cycles 8)
			)
		
			(7
				(PrintWiz 18 14)
				(Wizard setMotion: MoveTo 151 148 WizWalk setPri: 10)
			)
			(8
				(= cycles 5)
				(Wizard loop: 1)
			)
			(9
				(= cycles 7)
				(PrintWiz 18 15)
				(PrintWiz 18 16)
				(PrintWiz 18 17)
			)
			(10
				(= cycles 15)
				(Wizard
					posn: (- (Wizard x?) 5) (Wizard y?)
					view: 340
					loop: 1
					cel: 0
					setCycle: End
					cycleSpeed: 2
				)
				(PrintWiz 18 18)
				(PrintWiz 18 19)
				(portal setCycle: End)
				(= portVis 1)
			)
			(11
				(Wizard setCycle: Beg)
				(PrintWiz 18 20)
				(gEgo setMotion: MoveTo 106 134 WizWalk)
			)
			(12
				(= cycles 7)
				(gEgo loop: 0)
			)
			(13
				(PrintWiz 18 21)
				(Wizard
					view: 313
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo 151 124 WizWalk
				)
			)
			(14
				(Wizard setMotion: MoveTo 151 66 WizWalk setPri: 7)
			)
			(15
				(= cycles 6)
				
			)
			(16 (PrintOther 18 23)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
)

(procedure (WizTalk)
	(if (not gWizRm)
		(if (gEgo has: 7)
			(if (& (gEgo onControl:) ctlSILVER)      ; SILVER
				(Print 18 22 #width 280 #at -1 8)
				(WizWalk changeState: 2)
			else
				(PrintNCE)
			)
		else
			(Print 18 7 #width 280 #at -1 8)
			(++ mirLk)
		)
	else
		(Print 18 7 #width 280 #at -1 8)
	)
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 100)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
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
(procedure (checkEvent pEvent x1 x2 y1 y2)
	(if
		(and
			(> (pEvent x?) x1)
			(< (pEvent x?) x2)
			(> (pEvent y?) y1)
			(< (pEvent y?) y2)
		)
		(return TRUE)
	else
		(return FALSE)
	)
)

(instance alterEgo of Act
	(properties
		y 66
		x 166
		view 313
	)
)

(instance mirrorEgo of Act
	(properties            ; used only when picking up marble while ego in mirror
		y 66
		x 166
		view 232
	)
)

(instance Wizard of Act
	(properties
		y 66
		x 166
		view 313
	)
)

(instance mapItem of Prop
	(properties
		y 99
		x 195
		view 259
		cel 1
	)
)
(instance mirrorShine of Prop
	(properties
		y 125
		x 152
		view 161
	)
)

(instance planet of Act
	(properties
		y 150
		x 225
		view 20
	)
)

(instance sun of Act
	(properties
		y 142
		x 220
		view 21
	)
)

(instance portal of Prop
	(properties
		y 142
		x 108
		view 16
	)
)
