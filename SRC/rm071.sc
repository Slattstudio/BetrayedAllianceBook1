;;; Sierra Script 1.0 - (do not remove this comment)
; + 3 Score + 3 INT - +15 Defense Treasure - helmet
(script# 71)
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
	rm071 0
)

(local

; Cave Maze



	; (use "sciaudio")
	passage =  0       ; True while ego is walking from spot to spot
	countdown =  0     ; Used to give the illusion of moving off screen
	beenHereBefore =  0 ; Ticks up with each new room for print commands
	roomEntrance =  0
	sendTo37 =  0
	; It's likely I only need one variable for onTheWay and can switch it between 0,1,2 for each path (wrong,tower,treasure...)
	onTheWay =  0     ; If = 0,wrong; if = 1, tower; if = 2 treasure
	[toTower 5] = [0 0 0 0 0]     ; Variables set to 3,6,9,or 12 (like a clock) When all correct, send to new room
	towerCounter =  0

; T <--^
; |
; ^-->
; |
; <--- S

	[toTreasure 5] = [0 0 0 0 0]  ; Variables set to 3,6,9,or 12 (like a clock) When all correct, find treasure
	treasureCounter =  0

; T <--^
; |
; ^-->
; |
; ^
; |
; S

	treasureVisible =  0
	chestOpen =  0
	animation =  0
	
	armorNum = 0
	armorI
	chestNearby = 0	; equal 1 if you went up, 2 for right, 3 for down, 4 for left
	
)
; snd

(instance rm071 of Rm
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
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
			(31
				(= passage 1)
				(= roomEntrance 31)
				(gEgo posn: 162 189 loop: 3)
				(gTheMusic prevSignal: 0 number: 67 loop: -1 play:)
			)
			(37
				(gEgo posn: 30 155 loop: 0)
				(= sendTo37 1)
			)
		)
		(SetUpEgo)
		(gEgo init: observeControl: ctlBLUE)
		
		
		
		(alterEgo init: hide: setScript: chestScript)
		(leaveButton init: setPri: 15)
		(if (not [gArmor 0])
			(treasure init: hide: cel: 0 ignoreActors:)
		else
			(treasure init: hide: cel: 1 ignoreActors:)
		)
		(if g70GotMap
			(mapButton init: setPri: 15 cel: 0 hide:)
		else
			(mapButton init: setPri: 15 cel: 1 hide:)
		)
		(if (== roomEntrance 31)
			(ProgramControl)
			(SetCursor 997 (HaveMouse))
			(= gCurrentCursor 997)
			(RoomScript changeState: 12)
		)
	)
)

; = snd aud (send snd:
;            command("playx")
;            fileName("music\\exploration.mp3")
;            volume("70")
;            loopCount("-1")
;            init()
;        )

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			(1	; walkding left
				(= passage 1)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 0 (gEgo y?) RoomScript)
				(if treasureVisible
					(= chestNearby 4)	
				else
					(if (not (== chestNearby 2))
						(= chestNearby 0)
					)	
				)
			)
			(2
				(if (== [toTower 4] 9)
					(PlayerControl)
					(SetCursor 999 (HaveMouse))
					(= gCurrentCursor 999)
					(gRoom newRoom: 37)
				)
				; if you were coming from the room where the treasure was already visible
				(if treasureVisible
					(treasureToZero)	
				)
				(if (== treasureCounter 5)
					(= treasureVisible 1)
				)
				(= [toTreasure 4] 9)
				(gEgo
					posn: 315 (gEgo y?)
					setMotion: MoveTo 280 (gEgo y?) RoomScript
				)
				(if (== chestNearby 2)	; if returning from the room with the chest
					(= treasureCounter 5)
					(= chestNearby 0)
					(= treasureVisible 1)	
				)
			)
			(3
				(= passage 0)
				(RoomScript changeState: 14)
			)
			(4	; waking right
				(= passage 1)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 319 (gEgo y?) RoomScript)
				(if treasureVisible
					(= chestNearby 2)
				else
					(if (not (== chestNearby 4))
						(= chestNearby 0)
					)	
				)
			)
			(5
				(gEgo
					posn: 10 (gEgo y?)
					setMotion: MoveTo 50 (gEgo y?) RoomScript
				)
				(if treasureVisible
					(treasureToZero)	
				)
				(if (== chestNearby 4)	; if returning from the room with the chest
					(= treasureCounter 5)
					(= chestNearby 0)
					(= treasureVisible 1)	
				)
			)
			(6
				(= passage 0)
				(RoomScript changeState: 14)
			)
			(7	; walking down
				(= cycles 1)
				(= passage 1)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				(elevation)
				(if treasureVisible
					(= chestNearby 3)
				else
					(if (not (== chestNearby 1))
						(= chestNearby 0)
					)	
				)
				
			)
			(8
				(elevation)
				(if (not (== countdown -40))
					(RoomScript changeState: 7)
				else
					(RoomScript changeState: 9)
				)
				
			)
			(9
				(alterEgo hide:)
				(gEgo
					show:
					posn: 160 94
					setMotion: MoveTo 160 130 RoomScript
					ignoreControl: ctlWHITE
				)
				(if treasureVisible
					(treasureToZero)	
				)
				(if (== chestNearby 1)	; if returning from the room with the chest
					(= treasureCounter 5)
					;(= chestNearby 0)
					(= treasureVisible 1)	
				)
			)
			(10
				(= countdown 0)
				(= passage 0)
				(gEgo observeControl: ctlWHITE)
				(RoomScript changeState: 14)
			)

			(11	; walking up
				(= passage 1)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 160 88 RoomScript
					ignoreControl: ctlWHITE
				)
				(if treasureVisible
					(= chestNearby 1)
				else
					(if (not (== chestNearby 3))
						(= chestNearby 0)
					)	
				)
			)
			(12
				(gEgo
					posn: (gEgo x?) 189
					setMotion: MoveTo (gEgo x?) 158 RoomScript
				)
				(if treasureVisible
					(treasureToZero)
					(++ treasureCounter)	
				)
				(if (== chestNearby 3)	; if returning from the room with the chest
					(= treasureCounter 5)
					(= chestNearby 0)
					(= treasureVisible 1)	
				)
			)
			(13
				(gEgo observeControl: ctlWHITE)
				; PlayerControl()(SetCursor(999 HaveMouse()) = gCurrentCursor 999)
				(= passage 0)
				(RoomScript changeState: 14)
			)
			(14
				(if (not roomEntrance)
					(if beenHereBefore
						(if (== beenHereBefore 7))
						; Print("Am I lost?" #title "You think:")
						(++ beenHereBefore)
					else
						(PrintOther 71 0)
						(++ beenHereBefore)
					)
				)
				(= roomEntrance 0)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
	
	(method (handleEvent pEvent &tmp button)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) (treasure nsLeft?))
						(< (pEvent x?) (treasure nsRight?))
						(> (pEvent y?) (treasure nsTop?))
						(< (pEvent y?) (treasure nsBottom?))
					)
					(if treasureVisible
						(if (== [gArmor 0] 1)
							(PrintOther 71 1)
						else                 ; The treasure chest is empty, which will be a real bummer for the next guy who goes to all the trouble you did to find it.
							(PrintOther 71 2) ; A closed treature chest sits in the middle of the room.
							(PrintOther 71 3)
						)
					)
				)
			)                                ; Your mind is abuzz with wonder and expectation. You try not to get your hopes up for that new car or expensive vacation.
			(if
				(and
					(> (pEvent x?) (mapButton nsLeft?))
					(< (pEvent x?) (mapButton nsRight?))
					(> (pEvent y?) (mapButton nsTop?))
					(< (pEvent y?) (mapButton nsBottom?))
				)
				(if (not gHardMode)
					(if g70GotMap
						(Print {} #icon 82 1 0)
					else                          ; Map picture
						(PrintOther 71 4)
					)
				)
			)                                ; You don't have a map to guide you.
			(if
				(and
					(> (pEvent x?) (leaveButton nsLeft?))
					(< (pEvent x?) (leaveButton nsRight?))
					(> (pEvent y?) (leaveButton nsTop?))
					(< (pEvent y?) (leaveButton nsBottom?))
				)
				(= button
					(Print
						{Are you sure you want to go back to the entrance?}
						#title
						{Retrace your steps?}
						#at
						-1
						20
						#button
						{ Yes_}
						1
						#button
						{ No_}
						0
					)
				)
				(if button (gRoom newRoom: 31))
			)
		)
		(if (Said 'hi')
			(FormatPrint {%u %u} chestNearby treasureVisible)
		)
		(if (Said 'run') (Print 0 88))
		(if (Said 'look>')
			(if (Said '/door,cave') (PrintOther 71 5)) ; Many directions are open to you, but it's hard to tell where they will lead.
			(if (Said '/treasure,chest,box')
				(if treasureVisible
					(if (== [gArmor 0] 1)
						(PrintOther 71 6)
					else                     ; The treasure chest is empty, which will be a real bummer for the next guy who goes to all the trouble you did to find it.
						(PrintOther 71 2)    ; A closed treature chest sits in the middle of the room.
						(PrintOther 71 3)
					)
				else                         ; Your mind is abuzz with wonder and expectation. You try not to get your hopes up for that new car or expensive vacation.
					(PrintOther 71 7)
				)
			)                        ; Where do you see that?
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(PrintOther 71 5)
				(if treasureVisible (PrintOther 71 16))
			)
		)  
		(if (Said '(pick<up),take,open/treasure,chest,box')
			(if treasureVisible
				(if (not chestOpen)
					; (if(<=(send gEgo:distanceTo(treasure))35)
					(if (> (gEgo y?) (treasure y?))
						(= animation 1)
						(chestScript changeState: 1)
					else
						(PrintOther 71 9)
					)
				else                       ; Move to the front of the chest.
					(PrintOther 71 10)
				)
			else
				(Print {You don't see one.})
			)
		)
		(if (Said 'close/treasure,chest,box')
			(if treasureVisible
				(if (> (gEgo y?) (treasure y?))
					(if chestOpen
						(= animation 1)
						(chestScript changeState: 1)
					else
						(PrintOther 71 11)
					)
				else                      ; It's already closed.
					(PrintOther 71 9)
				)
			else
				(Print {You don't see one.})
			)
		)
		(if (or (Said 'look,use,read,open/portal,map')
				(Said 'map'))
			(Print 0 88)
		)
		(if (Said '(put<on),wear/helmet')
			(if (== [gArmor 0] 1)
				(PrintOther 71 13)
			else                  ; You're wearing it now! We know you can't see it, but trust us, it's there.
				(PrintDHI)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (and passage sendTo37) (= sendTo37 0))
		(if (not gHardMode)
			(mapButton show:)
		else
			(mapButton hide:)
		)
		(if treasureVisible
			(if (not animation)
				(gEgo observeControl: ctlBLUE)
			else
				(gEgo ignoreControl: ctlBLUE)
			)
			(treasure show:)
		else
			(gEgo ignoreControl: ctlBLUE)
			(treasure hide:)
		)
		(if (& (gEgo onControl:) ctlBROWN))       ; Unnecessary control
		; (send gRoom:newRoom(67))
; GOING UP
		(if (& (gEgo onControl:) ctlMAROON)
			(if (not passage)
				(= onTheWay 2) ; First move towards the treasure.
				(= [toTreasure 0] 12)
				; Setting toTower //
				(cond 
					((== towerCounter 1)
						(if (== [toTower 0] 9)
							(= [toTower 1] 12)
							(++ towerCounter)
						)
					)
					((== towerCounter 3)
						(if (== [toTower 2] 3)
							(= [toTower 3] 12)
							(++ towerCounter)
						)
					)
					(else (towerToZero))
				)
				; Setting toTreasure //
				(cond 
					((not treasureCounter) (= treasureCounter 1))
					((== treasureCounter 1)
						(if (== [toTreasure 0] 12)
							(= [toTreasure 1] 12)
							(++ treasureCounter)
						)
					)
					((== treasureCounter 3)
						(if (== [toTreasure 2] 3)
							(= [toTreasure 3] 12)
							(++ treasureCounter)
						)
					)
					(else
						(if (not (== treasureCounter 5))
							(treasureToZero)
						) 
						(++ treasureCounter)
					)
				)
				(RoomScript changeState: 11)
			)
		)
; GOING DOWN
		(if (& (gEgo onControl:) ctlRED)
			(if (not passage)
				(= onTheWay 0) ; First move towards nothing
				(towerToZero)
				(if (not (== treasureCounter 5))
					(treasureToZero)
				)
				(RoomScript changeState: 7)
			)
		)
; GOING RIGHT
		(if (& (gEgo onControl:) ctlGREY)
			(if (not passage)
				(= onTheWay 0)
				; Setting toTower //
				(if (== towerCounter 2)
					(if (== [toTower 1] 12)
						(= [toTower 2] 3)
						(++ towerCounter)
					)
				else
					(towerToZero)
				)
				; Setting toTreasure //
				(if (== treasureCounter 2)
					(if (== [toTreasure 1] 12)
						(if (not (== [toTreasure 2] 3))
							(= [toTreasure 2] 3)
							(++ treasureCounter)
						)
					)
				else
					(if (not (== treasureCounter 5))
						(treasureToZero)
					)
				)
				(RoomScript changeState: 4)
			)
		)
; GOING LEFT
		(if (& (gEgo onControl:) ctlSILVER)
			(if sendTo37 (gRoom newRoom: 37)
				(return)	
			)
			(if (not passage)
				(= onTheWay 1) ; First move towards the tower.
				(= [toTower 0] 9)
				; Setting toTower //
				(cond 
					((not towerCounter) (= towerCounter 1))
					((== towerCounter 4)
						(if (== [toTower 3] 12)
							(= [toTower 4] 9) ; This is the Final Move to get to the tower
							(++ towerCounter)
						)
					)
					(else (towerToZero) (= towerCounter 1))
				)
				; Setting toTreasure //
				(if (== treasureCounter 4)
					(if (== [toTreasure 3] 12)
						(if (not (== [toTreasure 4] 9))
							(= [toTreasure 4] 9)
							(++ treasureCounter)
						)
					)
				else
					(if (not (== treasureCounter 5))
						(treasureToZero)
					)
				)
				(RoomScript changeState: 1)
			)
		)
	)
)

; (send gRoom:newRoom(67))
(instance chestScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1       ; moving to open chest
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo (+ (treasure x?) 10) 157 self
					ignoreControl: ctlWHITE
				)
			)
			(2
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					loop: 1
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(3
				(= cycles 5)
				(if chestOpen
					(treasure cel: 0)
					(= chestOpen 0)
				else
					(treasure cel: 1)
					(= chestOpen 1)
				)
				(gTheSoundFX number: 204 play:)
			)
			(4
				(alterEgo setCycle: Beg self)
			)
			(5
				(if chestOpen
					(if (not [gArmor 0])
						(PrintOther 71 14) ; The chest creaks open. You pull a helmet into your hands. It is gold and silver trimmed because, let's face it, fashion counts!
						(Print
							{A helmet found hidden in a passageway under the castle.\n\n+3 Intelligence\n\n+5 Defense}
							#title
							{Armor Bonus:}
							#icon
							277
							0
						)
						(= gInt (+ gInt 3))
						(= gDef (+ gDef 5))
						(= [gArmor 0] 1)
						
						(if (not (gEgo has: 27))
							(gEgo get: 27)
						)
						(for ( (= armorI 0)) (< armorI 4)  ( (++ armorI)) (if (> [gArmor armorI] 0) (++ armorNum))) ; calculate how many armor piece
						((gInv at: 27) count: armorNum)	
						(= gArmorLoop 0)
						
						(gGame changeScore: 3)
					else
						(PrintOther 71 1)
					)
				else                     ; "The treasure chest is empty, which will be a real bummer for the next guy who goes to all the trouble you did to find it." #at -1 10)
					(PrintOther 71 15)
				)                     ; You close the empty chest, feeling a bit sorry for the next guy to find it.
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(alterEgo hide:)
				(gEgo show: observeControl: ctlWHITE observeControl: ctlBLUE)
				(= animation 0)
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 290 #at -1 10)
)

(procedure (elevation)
	(-- countdown)
	(alterEgo
		show:
		view: 0
		loop: 2
		posn: (gEgo x?) (gEgo y?)
		setCycle: Fwd
		cycleSpeed: 0
		z: countdown
	)
	(if (== countdown -40) (RoomScript changeState: 9))
)

(procedure (towerToZero)
	(= [toTower 1] 0)
	(= [toTower 1] 0)
	(= [toTower 2] 0)
	(= [toTower 3] 0)
	(= [toTower 4] 0)
	(= towerCounter 0)
)

(procedure (treasureToZero)
	(= [toTreasure 1] 0)
	(= [toTreasure 1] 0)
	(= [toTreasure 2] 0)
	(= [toTreasure 3] 0)
	(= [toTreasure 4] 0)
	(= treasureVisible 0)
	(= treasureCounter 0)
)

(instance alterEgo of Prop
	(properties
		y 65
		x 150
		view 0
	)
)

(instance mapButton of Prop
	(properties
		y 25
		x 25
		view 998
		loop 9
	)
)

(instance leaveButton of Prop
	(properties
		y 25
		x 291
		view 998
		loop 10
	)
)

(instance treasure of Prop
	(properties
		y 154
		x 159
		view 82
	)
)
