;;; Sierra Script 1.0 - (do not remove this comment)

(script# 74)
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
	rm074 0
)

(local
; Filler Room for Battles



	deadMan =  0
	prepareForBattle =  0
	canAttack =  1
	bodySearched =  0
	coins =  0
)

(instance rm074 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 68
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 204 setRegions: 200)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		
		(if (not g74Poster)	
			(poster init:)
		)
		
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
				(= canAttack 0)
				(soldier posn: 1 1 hide:)
			)
			(68
				(gEgo posn: 32 113 loop: 0)
				(soldier posn: 1 113)
				(if gFollowed
					(soldierScript changeState: 5)
					(gEgo setMotion: MoveTo 319 (gEgo y?))
					(= canAttack 0)
				else
					(= canAttack 0)
				)
			)
			(103       ; Battle Room
				(if (not gRanAway)
					(deadSoldier show: posn: 195 130)
					(gEgo posn: 195 125 loop: 2 observeControl: ctlGREY)
					(= deadMan 1)
					(= gFollowed 0)
				else
					(= gFollowed 1)
					(gEgo posn: 145 133 loop: 0)
					(soldier show: posn: 185 133 loop: 0)
					(RoomScript changeState: 1)
				)
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
			)
		)
		;(if gFollowed)
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
					(gEgo setMotion: MoveTo 1 133)
					(soldier show: setCycle: Walk setMotion: Follow gEgo)
					(= gFollowed 1)
				)
			)
			(2
			)
				;removing Poster
			(3
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				
				(gEgo setMotion: MoveTo 79 84 self ignoreControl: ctlWHITE)		
			)
			(4 (= cycles 2)
				(gEgo loop: 3)	
			)
			(5
				(PrintOther 74 16)
				(PrintOther 74 17)
				(poster hide:)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)	
				(= g74Poster 1)
				
				(gEgo observeControl: ctlWHITE)	
			)
			
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if gFollowed
					(if
						(and
							(> (pEvent x?) (soldier nsLeft?))
							(< (pEvent x?) (soldier nsRight?))
							(> (pEvent y?) (soldier nsTop?))
							(< (pEvent y?) (soldier nsBottom?))
						)
						(PrintOther 74 1)
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
								(PrintOther 74 2)
							)
						)
					else
						(PrintOther 74 2)
					)
				)
				(if (checkEvent pEvent 77 89 30 46)
					(if (not g74Poster)		
						(if (& (gEgo onControl:) ctlSILVER)
							(PrintOther 74 10)
							(PrintOther 74 11)
						else
							(PrintOther 74 12)
						)
					)
				)
				(if
					(==
						ctlNAVY
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                   ; mountains
					(PrintOther 74 3)
				)
			)
		)
		(if (or canAttack gFollowed)
			(if (not deadMan)
				(if (Said 'use/map')
					(PrintOther 68 11)	
				)
			)
		)
		(if (Said 'take,remove,tear/poster,notice,paper')
			(if (not (or canAttack gFollowed))
				(if (not g74Poster)	
					(if (& (gEgo onControl:) ctlSILVER)
						(self changeState: 3)
					else
						(PrintNCE)
					)
				else
					(PrintOther 74 14)
				)
			else
				(PrintOther 74 15)
			)
		)
		(if (Said 'read/poster,notice,paper')
			(if (not g74Poster)	
				(if (& (gEgo onControl:) ctlSILVER)
					(PrintOther 74 10)
					(PrintOther 74 11)
				else
					(PrintOther 74 12)
				)
			else
				(PrintOther 74 14)
			)
		)
		(if (Said 'look>')
			(if (Said '/rock') (PrintOther 74 4))
			(if (Said '/mountain') (PrintOther 74 3))
			(if (Said '/soldier,man,body')
				(cond 
					(gFollowed (PrintOther 68 1))
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
			(if (Said '/tree,poster,notice,paper')
				(if (not g74Poster)	
					(if (& (gEgo onControl:) ctlSILVER)
						(PrintOther 74 10)
						(PrintOther 74 11)
					else
						(PrintOther 74 12)
					)
				else
					(PrintOther 74 14)
				)	
			)
			(if (Said '/face,picture,image')
				(if (& (gEgo onControl:) ctlSILVER)
					(PrintOther 74 13)
				else
					(PrintOther 74 12)
				)
			)
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(if g74Poster	; post is ripped
					(PrintOther 74 9)
				else
					(PrintOther 74 4)	
				)
				(if gFollowed 
					(PrintOther 68 5) 
				else
					(if deadMan 
						(PrintOther 68 2)
					)
				)
			)
		)
		(if (Said '(ask<about),talk/man,soldier')
			(if gFollowed
				(if (not deadMan)
					(PrintSoldier 68 10)
				else
					(PrintOther 68 9)
				)
			else
				(PrintOther 68 9)
			)
		)
		(if (Said 'climb') (PrintOther 74 7))
		(if (or (Said 'search,examine,loot/body,man,soldier') (Said 'take/spoils'))
			(if deadMan
				(if (not bodySearched)
					(searchScript changeState: 1)
				else
					(PrintOther 74 8)
				)
			else
				(PrintOther 74 0)
			)
		)
		;(if (Said 'use/map')
		;	(Print {This isn't a good place to use that.})
		;)
	)
	
	(method (doit)
		(super doit:)
		(if (< (gEgo distanceTo: soldier) 24)
			(if (not deadMan)
				(if canAttack
					(if (not prepareForBattle)
						(soldierScript changeState: 3)
					)
				)
			)
		)
		; sliding slightly on the incline
		(if (& (gEgo onControl:) ctlMAROON)
			(if (== (gEgo loop?) 1)        ; going left
				(if (gEgo isStopped:)
					(gEgo posn: (+ (gEgo x?) 1) (+ (gEgo y?) 1))
				else                                                              ; slowly slide down
					(gEgo posn: (+ (gEgo x?) 1) (- (gEgo y?) 1))
				)
			else                                                                  ; running up a hill
				(gEgo posn: (+ (gEgo x?) 1) (+ (gEgo y?) 1))
			)
		)                                                                     ; slowly slide down
		(if (& (soldier onControl:) ctlMAROON)
			(if (== (soldier loop?) 1)   ; going left
				(if (soldier isStopped:)
					(soldier posn: (+ (soldier x?) 1) (+ (soldier y?) 1))
				else                                                        ; slowly slide down
					(soldier posn: (+ (soldier x?) 1) (- (soldier y?) 1))
				)
			else                                                            ; running up a hill
				(soldier posn: (+ (soldier x?) 1) (+ (soldier y?) 1))
			)
		)
	)
)
                                                                        ; slowly slide down
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
						(Print {How unlucky, only 1 measly gold coin.} #at -1 140)
						(= gGold (+ gGold 1))
					)
					((== coins 10)
						(Print
							{Wow, this guy was loaded! 20 Gold coins!}
							#at
							-1
							140
						)
						(= gGold (+ gGold 20))
					)
					(else
						(FormatPrint
							{After examining your enemy you find %u gold pieces.}
							coins
							#at
							-1
							140
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

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 140)
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
	)
)
(instance poster of Prop
	(properties
		y 48
		x 83
		view 162
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
