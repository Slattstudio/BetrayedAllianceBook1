;;; Sierra Script 1.0 - (do not remove this comment)

(script# 68)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use inv)
(use main)
(use obj)
(use follow)

(public
	rm068 0
)

(local
; South of Ogre Cave



	enemyApproach
	deadMan =  0
	prepareForBattle =  0
	canAttack =  1
	bodySearched =  0
	coins =  0
)

(instance rm068 of Rm
	(properties
		picture scriptNumber
		north 0
		east 74
		south 26
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 204 setRegions: 200)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		(soldier
			init:
			hide:
			ignoreActors:
			setScript: soldierScript
		)
		(deadSoldier init: hide: ignoreActors:)
		(alterEgo
			init:
			hide:
			ignoreActors:
			setScript: searchScript
		)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
			(25       ; From the north
				(gEgo posn: 177 64 loop: 2)
				(soldier posn: 319 115 loop: 1)
			)
			(26       ; From the south
				(gEgo posn: 177 164 loop: 3)
				(soldier posn: 319 115 loop: 1)
			)
			(74       ; From the east
				(gEgo posn: 288 105 loop: 1)
				(if gFollowed
					(soldierScript changeState: 5)
					(gEgo setMotion: MoveTo 1 (gEgo y?))
					(soldier posn: 319 105 loop: 2)
					(= canAttack 0)
				else
					(= canAttack 0)
					; (soldier:posn(144 58)loop(2))
					(soldier posn: 1 1 hide:)
				)
			)
			(103       ; Battle room
				(if (not gRanAway)
					(deadSoldier show: posn: 145 135 ignoreActors:)
					(gEgo posn: 145 130 loop: 2 observeControl: ctlGREY)
					(= deadMan 1)
					(= gFollowed 0)
					(= canAttack 0)
				else
					(= gFollowed 1)
					(gEgo posn: 145 105 loop: 0)
					(soldier show: posn: 115 110 loop: 1)
					(RoomScript changeState: 1)
				)
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
			)
		)
		(if (not gFollowed)
			(if (not deadMan)
				(= enemyApproach (Random 0 100))
				(if (< enemyApproach 65)
					(if (== gPreviousRoomNumber 74) (soldier posn: 146 58))
					(soldier show: setCycle: Walk setMotion: Follow gEgo)
					; = gFollowed 1
					(= canAttack 1)
				else
					(= canAttack 0)
				)
			else
			; (RoomScript:changeState(1))
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
				(= cycles 15)
				(if gRanAway
					(= gEgoRunning 1)
					(RunningCheck)
					(gEgo setMotion: MoveTo 319 105)
					(soldier show: setCycle: Walk setMotion: Follow gEgo)
				)
			)
			; = gFollowed 1
			(2 (if gRanAway))
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
; (if((>  (send pEvent:x) 220)and   // face cave
;                    (< (send pEvent:x) 265)and
;                    (> (send pEvent:y) 0)and
;                    (< (send pEvent:y) 72))
;                    Print("As you look at the face in the rocks you can hear a distant voice pulling you nearer." #width 280 #at -1 20)
;                    Print("'Can you here me?' the voice softly echoes in your mind. Could it be Julyn?" #width 280 #at -1 20)
;                )
				(if gFollowed
					(if
						(and
							(> (pEvent x?) (soldier nsLeft?))
							(< (pEvent x?) (soldier nsRight?))
							(> (pEvent y?) (soldier nsTop?))
							(< (pEvent y?) (soldier nsBottom?))
						)
						(PrintOther 68 1)
					)
				)
				(if
					(and
						(> (pEvent x?) (deadSoldier nsLeft?))
						(< (pEvent x?) (deadSoldier nsRight?))
						(> (pEvent y?) (deadSoldier nsTop?))
						(< (pEvent y?) (deadSoldier nsBottom?))
					)
					(if (<= (gEgo distanceTo: deadSoldier) 45)
						(if deadMan
							(if (not bodySearched)
								(searchScript changeState: 1)
							else
								(PrintOther 68 2)
							)
						)
					else
						(PrintOther 68 2)
					)
				)
			)
		)
		(if (or canAttack gFollowed)
			(if (not deadMan)
				(if (or (Said 'look,use,read,open/portal,map')
						(Said 'map'))
					(Print 0 88)
				)
			)
		)
		(if (Said '(ask<about),talk/man,soldier')
			(if (or canAttack gFollowed)
				(if (not deadMan)
					(PrintSoldier 68 10)
				else
					(PrintOther 68 9)
				)
			else
				(PrintOther 68 9)
			)
		)
		(if (Said 'look>')
			(if (Said '/soldier,man,body')
				(cond 
					(canAttack (PrintOther 68 1))
					(deadMan 
						(if (< (gEgo distanceTo: soldier) 34)
							(if (not bodySearched)
								(searchScript changeState: 1)
							else
								(PrintOther 68 2)
							)
						else
							(PrintOther 68 2)
						)
					)
					(else (PrintOther 68 3))
				)
			)
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(PrintOther 68 4)
				(if canAttack
					(if deadMan (PrintOther 68 2) else (PrintOther 68 5))
				)
			)
		)
		(if (or (Said 'search,examine,loot/body,man,soldier') (Said 'take/spoils'))
			(if deadMan
				(if (not bodySearched)
					(searchScript changeState: 1)
				else
					(PrintOther 68 6)
				)
			else
				(Print {You can't do that now.})
			)
		)
		;(if (Said 'use/map')
		;	(Print {This isn't a good place to use that.})
		;)
	)
	
	(method (doit)
		(super doit:)
		(if (== (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 25)
		)
		(if (< (gEgo distanceTo: soldier) 24)
			(if (not deadMan)
				(if canAttack
					(if (not prepareForBattle)
						(soldierScript changeState: 3)
					)
				)
			)
		)
	)
)

(instance soldierScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1 (= cycles 1))
			(2
				(soldier setCycle: Walk setMotion: Follow gEgo)
			)
			(3
				(= cycles 5)
				(gEgo setMotion: NULL)
				(= prepareForBattle 1)
				(PrintOther 20 0) ; #at -1 20)
				(= gBatNum 100)
				(if gFollowed else (= gOpHealth gBatNum))
			)
			(4 (gRoom newRoom: 103))
			(5 (= cycles 10))
			(6
				(soldier show: setCycle: Walk setMotion: Follow gEgo)
				(= canAttack 1)
			)
		)
	)
)

(instance searchScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					setMotion:
						MoveTo
						(+ (deadSoldier x?) 10)
						(- (deadSoldier y?) 5)
						searchScript
					ignoreControl: ctlWHITE
					ignoreControl: ctlGREY
				)
			)
			(2
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					loop: 1
					posn: (gEgo x?) (gEgo y?)
					setCycle: End searchScript
					cycleSpeed: 2
				)
			)
			(3
				(= cycles 8)
				(= coins (Random 1 10))
				(cond 
					((== coins 1)
						(Print {How unlucky, only 1 measly gold coin.} #at -1 10)
						(= gGold (+ gGold 1))
					)
					((== coins 10)
						(Print
							{Wow, this guy was loaded! 20 Gold coins!}
							#at
							-1
							10
						)
						(= gGold (+ gGold 20))
					)
					(else
						(FormatPrint
							{After examining your enemy you find %u gold pieces.}
							coins
							#at
							-1
							10
						)
						(= gGold (+ gGold coins))
					)
				)
				(= bodySearched 1)
			)
			(4
				(alterEgo setCycle: Beg searchScript)
			)
			(5
				(gEgo
					show:
					observeControl: ctlWHITE
					observeControl: ctlGREY
				)
				(alterEgo hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
		)
	)
)

(procedure (PrintSoldier textRes textResIndex)
	(= gWndColor 15)
	(= gWndBack 8)
	(if (> (gEgo y?) 100)
		(Print
			textRes
			textResIndex
			#width
			280
			#at
			-1
			20
			#title
			{Soldier:}
		)
	else
		(Print
			textRes
			textResIndex
			#width
			280
			#at
			-1
			140
			#title
			{Soldier:}
		)
	)
	(= gWndColor 0)
	(= gWndBack 15)
)

(procedure (PrintOther textRes textResIndex)
	(if (> (gEgo y?) 100)
		(Print textRes textResIndex #width 280 #at -1 10)
	else
		(Print textRes textResIndex #width 280 #at -1 140)
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

(instance soldier of Act
	(properties
		y 130
		x 180
		view 322
		loop 2
	)
)

(instance deadSoldier of Prop
	(properties
		y 127
		x 135
		view 322
		loop 4
		cel 1
	)
)

(instance alterEgo of Prop
	(properties
		y 164
		x 68
		view 232
		loop 1
	)
)
