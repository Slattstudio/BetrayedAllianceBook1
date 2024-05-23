;;; Sierra Script 1.0 - (do not remove this comment)
; + 3 SCORE // + 3 Agility // + 10 Defence //
(script# 135)
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
	rm135 0
)

(local

; Castle Store Room (Night)



	; (use "sciaudio")
	goingUpSteps =  0
	hitGreaves =  0
	
	armorNum = 0
	armorI
)
; snd

(instance rm135 of Rm
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
		(trapDoor
			init:
			ignoreActors:
			setScript: climbScript
			setPri: 0
		)
		(alterEgo init: hide: ignoreActors:)
		(shimmer
			init:
			ignoreActors:
			setPri: 15
			setScript: shimmerScript
		)
		(switch gPreviousRoomNumber
			(109
				(gEgo posn: 279 77 loop: 1)
				(RoomScript changeState: 5)
				
			)

			(else 
				(gEgo hide: posn: 150 130 loop: 1)
				(climbScript changeState: 1)
				(gTheMusic number: 109 loop: -1 play:)
				;(gTheMusic prevSignal: 0 fade:)
			)
		)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			(1
				(ProgramControl)
				(gEgo setMotion: MoveTo 294 128 RoomScript)
			)
			(2
				(gEgo setMotion: MoveTo 273 77 RoomScript)
			)
			(3
				(gEgo setMotion: MoveTo 279 77 RoomScript)
			)
			(4 (gRoom newRoom: 109))
			(5
				(= goingUpSteps 1) ; technically this doesn't make sense, but this is just a trigger to ignore ctlGREY
				(ProgramControl)
				(gEgo setMotion: MoveTo 273 77 RoomScript)
			)
			(6
				(gEgo setMotion: MoveTo 294 128 RoomScript)
			)
			(7
				(gEgo setMotion: MoveTo 294 136 RoomScript)
			)
			(8
				(PlayerControl)
				(= goingUpSteps 0)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(cond 
					((checkEvent pEvent 2 20 105 124)   ; sack
						(Print
							{Smells like a sack of coffee beans.}
							#width
							280
							#at
							-1
							8
						)
					)
					(
						(or
							(checkEvent pEvent 14 42 89 118)
							(checkEvent pEvent 115 218 129 179)
						)                                                                            ; barrels
						(Print
							{There are many barrels here. Some are marked as flour, raisins, and even wine, while others have no marking at all.}
							#width
							280
							#at
							-1
							8
						)
					)
				)
				(if (checkEvent pEvent 88 114 95 118)    ; box
					(Print
						{This box is unmarked and quite heavy. It doesn't hold you interest long.}
						#width
						280
						#at
						-1
						8
					)
				)
				(if (checkEvent pEvent 157 190 80 119)    ; shelf
					(if (& (gEgo onControl:) ctlSILVER)
						(Print
							{The shelves hold gears, peices of wood and metal, and other items of little use.}
							#width
							280
							#at
							-1
							8
						)
						(if (not g135Darts)
							(Print
								{As you brush objects to the side a new set of darts catch your eye. Laced with sleeping agent these darts are great for knocking out small animals (or humans, you think).}
								#width
								280
								#at
								-1
								8
							)
						)
					else
						(Print
							{The distant shelves look to hold all kinds of tiny trinkets. Are any of them useful?}
							#width
							280
							#at
							-1
							8
						)
					)
				)
				(if (checkEvent pEvent 264 290 28 76)    ; door
					(Print
						{The doorway leads to the tower stairs and up to the prison.}
						#width
						280
						#at
						-1
						8
					)
				)
			)
		)
		(if (or (Said 'look,use,read,open/portal,map')
				(Said 'map'))
			(PrintOther 69 47)
		)
		(if (Said '(open,examine,search,lift)>')
			(if (Said '/door,trapdoor,floorboard,passage')
				(Print
					{You've come very far now. You're not going to leave without Julyn.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '/barrel')
				(Print
					{You find nothing useful in this barrel.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '/box')
				(Print
					{You could pry the box open, but you know there is nothing of use inside.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '/shelf,cabinet,trinket')
				(if (& (gEgo onControl:) ctlSILVER)
					(Print
						{The shelves hold gears, peices of wood and metal, and other items of little use.}
						#width
						280
						#at
						-1
						8
					)
					(if (not g135Darts)
						(Print
							{As you brush objects to the side a new set of darts catch your eye. Laced with sleeping agent these darts are great for knocking out small animals (or humans, you think).}
							#width
							280
							#at
							-1
							8
						)
						(Print
							{You take all five darts and carefully put them in your bag.}
						)
						(= gApple (+ gApple 5)) ; gApple was changed to count darts...Maybe I'm too lazy to change it, but you have no proof.
						(= g135Darts 1)
						(gGame changeScore: 2)
					)
				else
					(Print
						{The distant shelves look to hold all kinds of tiny trinkets. Are any of them useful?}
						#width
						280
						#at
						-1
						8
					)
				)
			)
		)
		(if (Said 'search/floor,ground') 
			(if (== (gEgo onControl:) ctlMAROON)
				(if (not [gArmor 1])
					(getGreaves)
				else
					(PrintOther 135 8)
				)	
			else
				(PrintOther 135 9)
			)
		)
		(if (Said 'look>')
			(if (Said '/glimmer,glint')
				(if  (not g135Darts) 
					(PrintOther 135 0)
				)
				(if (not [gArmor 1]) 
					(PrintOther 135 1)
				)
				(if (and g135Darts [gArmor 1])
					(PrintOther 135 2)
				)
			)
			(if (Said '/barrel')
				(Print
					{There are many barrels here. Some are marked as flour, raisins, and even wine, while others have no marking at all.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '/box')
				(Print
					{Many of the wooden boxes are unlabled. It is impossible to tell what's inside without cracking them open, but there's really not time for that anyway.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '/window,sky')
				(Print
					{The stars are shining beautifully in the clear night sky. Hopefully the cloak of night will allow you to sneak in and out undetected.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '/door')
				(Print
					{The doorway leads to the tower stairs and up to the prison.}
					#width
					280
					#at
					-1
					8
				)
				(Print
					{There is also a floor passage through which you came.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '/floor,ground')
				(if hitGreaves
					(if (== (gEgo onControl:) ctlMAROON)
						(if (not [gArmor 1])
							(getGreaves)
						else
							(Print
								{The floor is constructed of wooden boards. There is a secret door through which you came, but it's really too dark to see much of anything.}
								#width
								280
								#at
								-1
								8
							)
						)
					else
						(Print
							{The floor is constructed of wooden boards. There is a secret door through which you came, but it's really too dark to see much of anything.}
							#width
							280
							#at
							-1
							8
						)
					)
				else
					(Print
						{The floor is constructed of wooden boards. There is a secret door through which you came, but it's really too dark to see much of anything.}
						#width
						280
						#at
						-1
						8
					)
					(if (not [gArmor 1]) 
						(PrintOther 135 3)
					)
				)
			)
			(if (Said '/shelf,cabinet,trinket,drawer')
				(if (& (gEgo onControl:) ctlSILVER)
					(Print
						{The shelves hold gears, pieces of wood and metal, and other items of little use.}
						#width
						280
						#at
						-1
						8
					)
					(if (not g135Darts)
						(Print
							{As you brush objects to the side a new set of darts catch your eye. Laced with sleeping agent these darts are great for knocking out small animals (or humans, you think).}
							#width
							280
							#at
							-1
							8
						)
						(Print
							{You take all five darts and carefully put them in your bag.}
						)
						(= gApple (+ gApple 5)) ; gApple was changed to count darts...Maybe I'm too lazy to change it, but you have no proof.
						(= g135Darts 1)
						(gGame changeScore: 1)
					)
				else
					(Print
						{The distant shelves look to hold all kinds of tiny trinkets. Are any of them useful?}
						#width
						280
						#at
						-1
						8
					)
				)
			)
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(if hitGreaves
					(if (not [gArmor 1])
						(getGreaves)
					else
						(PrintOther 135 5)
					
					)
				else
					(PrintOther 135 5)
					(if (or (not g135Darts) (not [gArmor 1]))
						(PrintOther 135 4)	
					)
				)
			)
			(if (Said '/*')
				; this will handle "look anyword"
				(Print
					{There's nothing particularly interesting about it.}
					#width
					280
					#at
					-1
					8
				)
			)
		)
		(if (Said '(pick<up),take>')
			(if (Said '/dart,trinket')
				(if (not g135Darts)
					(if (== (gEgo onControl:) ctlSILVER)
						(Print
							{You take all five darts and carefully put them in your bag.}
						)
						(= gApple (+ gApple 5)) ; gApple was changed to count darts...Maybe I'm too lazy to change it, but you have no proof.
						(= g135Darts 1)
						(gGame changeScore: 1)
					else
						(PrintNCE)
					)
				else
					(PrintATI)
				)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (== (gEgo onControl:) ctlGREY)
			(if (not goingUpSteps)
				(RoomScript changeState: 1)
				(= goingUpSteps 1)
			)
		)
		(if (not [gArmor 1])
			(if (== (gEgo onControl:) ctlMAROON)
				(if (not hitGreaves)
					(Print {Ouch! You stubbed your toes on something hard!})
					(= hitGreaves 1)
					(gEgo setMotion: NULL)
				)
			else
				(= hitGreaves 0)
			)
		)
		(if goingUpSteps
			(SetCursor 997 (HaveMouse))
			(= gCurrentCursor 997)
		else
			(SetCursor 999 (HaveMouse))
			(= gCurrentCursor 999)
		)
	)
)

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

(procedure (getGreaves)
	(Print
		{You look to the ground and see something shining. Closer inspection reveals greaves, which you take.}
	)
	(Print
		{An extra pair of greaves found in a storage room of the castle.\n\n+3 Agility\n\n+5 Defense}
		#title
		{Armor Bonus:}
		#icon
		277
		1
	)
	(= [gArmor 1] 1)
	(= gAg (+ gAg 3))
	(= gDef (+ gDef 5))
	
	(if (not (gEgo has: 27))
		(gEgo get: 27)
	)
	(for ( (= armorI 0)) (< armorI 4)  ( (++ armorI)) (if (> [gArmor armorI] 0) (++ armorNum))) ; calculate how many armor piece
	((gInv at: 27) count: armorNum)	
	(= gArmorLoop 1)
	
	(gGame changeScore: 2)
)

(instance shimmerScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= cycles 1))
			(1
				(if (not [gArmor 1])
					(shimmer posn: 66 160 cel: 0 setCycle: End self setPri: 15)
				else
					(self cue:)
				)
			)
			(2 (= cycles (Random 5 40))
			)
			(3
				(if (not gHardMode)	; darts glimmer on easy mode
					(if (not g135Darts)
						(shimmer posn: 174 93 cel: 0 setCycle: End self setPri: 1)
					else
						(self cue:)
					)
				else
					(self cue:)		
				)
			)
			(4 (= cycles (Random 100 200)))
			(5 (self changeState: 1))
		)
	)
)

(instance climbScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles 15)
				(ProgramControl)
				(= goingUpSteps 1) ; technically this doesn't make sense, but this is just a trigger to ignore ctlGREY
				(trapDoor setCycle: End cycleSpeed: 2)
			)
			(2
				(alterEgo show: setCycle: End climbScript cycleSpeed: 2)
			)
			(3	
				(alterEgo hide:)
				(gEgo show: posn: (alterEgo x?) (alterEgo y?) loop: 2)
				(trapDoor setCycle: Beg self)
				
			)
			(4
				(PlayerControl)
				(= goingUpSteps 0)
				(PrintOther 135 6)	
				(PrintOther 135 7)	
			)
		)
	)
)
                                 ; technically this doesn't make sense, but this is just a trigger to ignore ctlGREY
(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 10)
)

(instance alterEgo of Act
	(properties
		y 140
		x 66
		view 409
		loop 2
	)
)

(instance trapDoor of Act
	(properties
		y 141
		x 66
		view 409
		loop 3
	)
)

(instance shimmer of Prop
	(properties
		y 160
		x 66
		view 86
	)
)
