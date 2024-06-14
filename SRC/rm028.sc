;;; Sierra Script 1.0 - (do not remove this comment)
; +9 SCORE // + gDef 20 //
(script# 28)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use door)
(use feature)
(use game)
(use inv)
(use main)
(use obj)

(public
	rm028 0
)

(local

; Tavern Entrance



	message =  0
	repeat_ =  0
	marbleDown =  0
	disguiseFailed =  0
	usingRuler =  0
	
	armorNum = 0
	armorI 
)

(instance rm028 of Rm
	(properties
		picture scriptNumber
		north 0
		east 45
		south 43
		west 40
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript setRegions: 204)
		(SetUpEgo)
		(gEgo init:)
		(RunningCheck)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 187 183 loop: 1)
			)
			(40 (gEgo posn: 30 183 loop: 0))
			(43
				(gEgo posn: 187 183 loop: 3)
			)
			(44
				(gEgo posn: 187 156 loop: 2 setMotion: MoveTo 187 173)
				(gTheMusic prevSignal: 0 number: 25 loop: -1 play:)
			)
			(45
				(gEgo posn: 300 181 loop: 1)
			)
		)
		; (send gTheMusic:prevSignal(0)fade())
		(seller init: setScript: BerateScript)
		(meatEater init: setScript: eatScript)
		(if [gArmor 2]
			(meatEater loop: 0)
		else
			(meatEater loop: 3)
		)
				
		(alterEgo init: hide: setScript: letterScript ignoreActors:)
		(flower init: hide: setScript: proposeScript)
		(if [gFlowerGiven 2] (flower show:))
		(eatScript changeState: 4)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(2
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 10 166 self)
			)
			(3
				(if gDisguised
					(PrintOther 0 84)
					(= gDisguised 0)
					(RunningCheck)
				)
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					posn: (gEgo x?) (gEgo y?)
					setCycle: End RoomScript
					cycleSpeed: 2
				)
			)
			(4
				(= cycles 10)
				(if marbleDown (PrintOther 28 82))
				(if usingRuler (PrintOther 28 136))
			)
			(5
				(alterEgo setCycle: Beg RoomScript)
				(if (not gVen)
					(cond 
						(marbleDown
							(PrintOther 28 83)
							(PrintOther 28 84)
							(= gVen 1)
							(++ gMarbleNum)
							((gInv at: 9) count: gMarbleNum)
							(gGame changeScore: 1)
							(= marbleDown 0)
						)
						(usingRuler
							(PrintOther 28 137)
							(= gVen 1)
							(++ gMarbleNum)
							((gInv at: 9) count: gMarbleNum)
							(gGame changeScore: 1)
							(= usingRuler 0)
						)
						((not gVen) (PrintOther 28 95) (PrintOther 28 89) (PrintOther 28 88)) ; #width 280 #at -1 8) // You look inside and see something shiny. ; #width 280 #at -1 8) // You put your hand into the grassy pot and feel around.
						(else (Print 28 96 #width 280 #at -1 8)) ; #width 280 #at -1 8) // Unfortunately, you your arm isn't long enough.
					)
				else                                             ; It appears to be empty now.
					(Print 28 92) ; There's no reason to do that now.
					(= marbleDown 0)
				)
			)
			(6
				(alterEgo hide:)
				(gEgo show: setMotion: MoveTo 34 176 self)
			)
			(7
				(gEgo loop: 2 observeControl: ctlWHITE)
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
					(checkEvent
						pEvent
						(seller nsLeft?)
						(seller nsRight?)
						(seller nsTop?)
						(seller nsBottom?)
					)
					(PrintOther 28 21) ; #width 270 #at -1 14)
					(PrintOther 28 22)
				)                     ; #width 270 #at -1 14)
				(if
					(checkEvent
						pEvent
						(meatEater nsLeft?)
						(meatEater nsRight?)
						(meatEater nsTop?)
						(meatEater nsBottom?)
					)
					(PrintOther 28 23)
				)                     ; #width 270 #at -1 14)
				(if
					(==
						ctlRED
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                 ; pot
					(if (< (pEvent x?) 37)
						(PrintOther 28 24)
					else                 ; #width 270 #at -1 14)
						(PrintOther 28 25)
					)
				)                        ; #width 270 #at -1 14)
				(if (checkEvent pEvent 107 130 1 33) (PrintOther 28 26)) ; chimney ; #width 270 #at -1 14)
				(if (checkEvent pEvent 269 289 122 148)  ; barrel
					(PrintOther 28 27)
				)
			)
		)                            ; #width 270 #at -1 14)
		(if (Said '(tell<about),show,give/letter,note,message')
			(cond 
				((<= (gEgo distanceTo: seller) 35)
					(if (gEgo has: INV_LETTER)
						(letterScript changeState: 1)
					else
						(Print 28 38)
					)
				)
				((<= (gEgo distanceTo: meatEater) 35)
					(if (== g62Letter 1)
						(PrintSammy 28 72) ; #width 290 #at -1 40 #title "He says:")
						(Print 28 138 #title {Armor Bonus:} #icon 277 2)
						(= g62Letter 2)
						(= [gArmor 2] 1)
						(= gDef (+ gDef 10))
						
						(if (not (gEgo has: 27))
							(gEgo get: 27)
						)
						(for ( (= armorI 0)) (< armorI 4)  ( (++ armorI)) (if (> [gArmor armorI] 0) (++ armorNum))) ; calculate how many armor piece
						((gInv at: 27) count: armorNum)	
						(= gArmorLoop 2)
						
						(gGame changeScore: 3)
						(gEgo put: 24)
						(eatScript changeState: 4)
					else
						(Print 28 38)
					)
				)
				(else (PrintNCE))
			)
		)
		(if (Said 'buy>')
			(if (Said '/meat,chicken,pork,food')
				(if (not (gEgo has: INV_MEAT))
					(if (<= (gEgo distanceTo: seller) 35)
						(if (== (IsOwnedBy INV_LETTER 28) TRUE)
							(if [gFlowerGiven 2]
								(PrintDebby 28 120) ; #width 290 #at -1 -1 #title "She says:")
								(gEgo get: INV_MEAT)
								(Print 0 5 #icon 257)
								(gGame changeScore: 1)
							else
								(= button
									(Print 28 139 #button { Yes_} 1 #button { No_} 0)
								)
								(if (== button 0) (PrintDebby 28 39)) ; #width 290 #at -1 -1 #title "She says:")
								(if (== button 1)
									(if (not (gEgo has: INV_MEAT))
										(if (> gGold 4)
											(= gGold (- gGold 5))
											(PrintDebby 28 40) ; #width 290 #at -1 -1 #title "She says:")
											(gEgo get: INV_MEAT)
											(Print 0 5 #icon 257)
											(gGame changeScore: 1)
										else
											(PrintDebby 28 43)
										)
									else                      ; #width 290 #at -1 -1 #title "She says:")
										(Print 28 44)
									)
								)
							)
						else
							(PrintOther 28 42)
						)
					else
						(PrintNCE)
					)
				else
					(PrintOther 28 121)
				)
			)
		)
		(if
			(or
				(Said 'give/ring[/woman,debby]')
				(Said 'give/woman,debby/ring')
				(Said 'propose[/marriage,woman,debby]')
			)
			(if (gEgo has: 18)
				(proposeScript changeState: 1)
			else
				(PrintDHI)
			)
		)
		(if (or (Said 'look,use,read,open/portal,map')
				(Said 'map'))
			(Print 28 102)
		)
		(if (Said 'smell[/!*]') (PrintOther 28 107))
		(if (Said '(ask<about)>')
			(cond 
				((<= (gEgo distanceTo: seller) 35)
					(if message (if gPrintDlg (gPrintDlg dispose:)))
					(= message 0)
					(= repeat_ 1)
					(cond 
						((== (IsOwnedBy INV_LETTER 28) TRUE) ; Letter is owned by this room when gEgo gives letter to Deborah
							(= gWndColor 14)
							(= gWndBack 8)
							(if (Said '/meat,food')
								(PrintDebby 28 29) ; #width 290 #at -1 -1 #title "She says:")
								(PrintDebby 28 30)
							)
							(if (Said '/healing,potion') 
								(PrintDebby 28 164)
							)
							(if (Said '/pork') (PrintDebby 28 31)) ; #width 290 #at -1 -1 #title "She says:")
							(if (Said '/chicken') (PrintDebby 28 32)) ; #width 290 #at -1 -1 #title "She says:")
							(if (Said '/princess')
								(PrintDebby 28 33) ; #width 290 #at -1 -1 #title "She says:")
								(PrintDebby 28 34)
							)             ; #width 290 #at -1 -1 #title "She says:")
							(if (Said '/tristan') (PrintDebby 28 140))
							(if (Said '/cave') (PrintDebby 28 35)) ; #width 290 #at -1 -1 #title "She says:")
							(if (Said '/daughter,family')
								(PrintDebby 28 36) ; #width 290 #at -1 -1 #title "She says:")
								(PrintDebby 28 37)
							)             ; #width 290 #at -1 -1 #title "She says:")
							(if (Said '/jasper,husband') (PrintDebby 28 124)) ; #width 290 #at -1 -1 #title "She says:")
							(if (Said '/castle') (PrintDebby 28 45)) ; #width 290 #at -1 -1 #title "She says:")
							(if (Said '/key,chest') (PrintDebby 28 150))
							(if (Said '/husband') (PrintDebby 28 46)) ; #width 290 #at -1 -1 #title "She says:")
							(if (Said '/wizard') (PrintDebby 28 47)) ; #width 290 #at -1 -1 #title "She says:")
							(if (Said '/king') (PrintDebby 28 151))
							(if (Said '/carmyle') (PrintDebby 28 48)) ; #width 290 #at -1 -1 #title "She says:")
							(if (Said '/weather') (PrintDebby 28 49)) ; #width 290 #at -1 -1 #title "She says:")
							(if (Said '/ogre,monster') (PrintDebby 28 105)) ; #width 290 #at -1 -1 #title "She says:")
							; peoples of shelah
							(if (Said '/man, fisher,bobby') (PrintDebby 28 142))
							(if (Said '/leah') (PrintDebby 28 142))
							(if (Said '/sammy')
								(PrintDebby 28 74) ; #width 290 #at -1 -1 #title "She says:")
								(PrintDebby 28 75)
							)             ; #width 290 #at -1 -1 #title "She says:")
							(if (Said '/kingdom,ishvi') (PrintDebby 28 154))
							(if (Said '/ishvi') (PrintDebby 28 153))
							(if (Said '/deborah, woman') (PrintDebby 28 122))
							(if (Said '/rose') (PrintDebby 28 123))
							(if (Said '/sarah') (PrintDebby 28 142))
							(if (Said '/hans') (PrintDebby 28 125))
							(if (Said '/lex') (PrintDebby 28 142))
							(if (Said '/colin') (PrintDebby 28 142))
							(if (Said '/longeau') (PrintDebby 28 126))
							(if (Said '/moon') (PrintDebby 0 01))
							(if (Said '/gyre') (PrintDebby 0 91))
							(if (Said '/name') (PrintDebby 28 143))
							(if (Said '/*') (PrintDebby 28 50))
						)
						((Said '/*') (Print 28 42 #at -1 -1)) ; #width 290 #at -1 -1 #title "She says:")
					)
					(= gWndColor 0) ; clBLACK
					(= gWndBack 15)
				)
				((<= (gEgo distanceTo: meatEater) 35) ; clWHITE
					(if (Said '/wizard') (PrintSammy 28 51))
					(if (Said '/carmyle') (PrintSammy 28 52)) ; #width 290 #at -1 40 #title "He says:")
					(if (Said '/princess') (PrintSammy 28 109)) ; #width 290 #at -1 40 #title "He says:")
					(if (Said '/tristan') (PrintSammy 28 141))
					(if (Said '/meat,chicken,pork,food,debby')
						(PrintSammy 28 53)
						(PrintSammy 28 134)
					)
					(if (Said '/cooking')
						(PrintSammy 28 145)	
					)
					(if (Said '/retirement,clothes,army,job,soldier,man')
						(PrintSammy 28 54)
					)
					(if (Said '/ogre,monster,troll')
						(PrintSammy 28 106)
						(PrintSammy 28 135)
					)
					(if (Said '/cave')
						(PrintSammy 28 57)
						(PrintSammy 28 58)
						(PrintSammy 28 59)
					)
					(if (Said '/king') (PrintSammy 28 60))
					(if (Said '/castle,tower,duty,guard')
						(PrintSammy 28 61)
						(PrintSammy 28 62)
					)
					(if (Said '/entrance,passage,secret')
						(PrintSammy 28 63)
						(PrintSammy 28 64)
						(PrintSammy 28 65)
					)
					(if (Said '/trick,pit')
						(PrintSammy 28 66)
						(PrintSammy 28 67)
					)
					(if (Said '/armor,breastplate,coat,plate')
						(if [gArmor 2]
							(PrintSammy 28 157)
						else
							(PrintSammy 28 158)
							(PrintSammy 28 71)
						)
					)
					(if (Said '/gallagos,friend,letter')
						(if [gArmor 2]
							(PrintSammy 28 160)	
						else
							(PrintSammy 28 70)
							(PrintSammy 28 71)
						)
					)
					(if (Said '/healing,potion') 
						(PrintSammy 28 162)
						(PrintSammy 28 163)
					)
					(if (Said '/kingdom,ishvi') (PrintSammy 28 155))
					(if (Said '/ishvi') (PrintSammy 28 152))
					(if (Said '/gyre') (PrintSammy 28 0))
					(if (Said '/story') (PrintSammy 28 108))
					; peoples of shelah
					(if (Said '/fisher,bobby') (PrintSammy 28 127))
					(if (Said '/leah') (PrintSammy 28 129))
					(if (Said '/sammy, man') (PrintSammy 28 73))
					(if (Said '/deborah') (PrintSammy 28 130))
					(if (Said '/rose') (PrintSammy 28 131))
					(if (Said '/sarah') (PrintSammy 28 129))
					(if (Said '/hans') (PrintSammy 28 132))
					(if (Said '/lex') (PrintSammy 28 128))
					(if (Said '/colin') (PrintSammy 28 128))
					(if (Said '/longeau') (PrintSammy 28 133))
					(if (Said '/moon,tor') (PrintSammy 28 128))
					(if (Said '/name') (PrintSammy 28 144))
					(if (Said '/*') (PrintSammy 28 68))
				)
				((Said '/*') (Print 28 69))
			)
		)
		(if (Said 'give/flower')
			(if (gEgo has: 21)
				(cond 
					((<= (gEgo distanceTo: meatEater) 30) (PrintSammy 28 117))
					((<= (gEgo distanceTo: seller) 35)
						(cond 
							([gFlowerGiven 2] (PrintOther 28 116))
							((== (IsOwnedBy INV_LETTER 28) FALSE) (PrintDebby 28 118))
							(else                  ; #title "She says:" #width 270 #at -1 40)
								(PrintDebby 28 119) ; #title "She says:" #width 270 #at -1 40)
								(= [gFlowerGiven 2] 1)
								(-- gFlowers)
								(if (== gFlowers 0) (gEgo put: 21))
								((gInv at: 21) count: gFlowers)
								(flower show:)
								(gGame changeScore: 1)
							)
						)
					)
					(else (PrintOther 28 115))
				)
			else
				(PrintDHI)
			)
		)
		(if (Said 'wear,use,(put<on)/goggles')
			(if (gEgo has: INV_GOGGLES)
				(if gDisguised
					(Print 0 53)
				else
					(PrintOther 0 80)
					(= gDisguised 1)
					(= gEgoRunning 0)
					(RunningCheck)
					(gEgo setMotion: NULL)
				)
			else
				(PrintDHI)
			)
		)
		(if (Said 'talk/woman,deborah')
			(if (<= (gEgo distanceTo: seller) 35)
				(if message
					(if gPrintDlg
						(gPrintDlg dispose:)
						(= message 0)
						(= repeat_ 1)
					)
				)
				(if (== (IsOwnedBy INV_LETTER 28) TRUE) ; Letter is owned by this room when gEgo gives letter to Deborah
					(if gDisguised
						(PrintDebby 28 111)
					else                   ; #title "She says:" #width 270 #at -1 -1)
						(PrintDebby 28 76)
					)
				else                      ; #title "She says:" #width 270 #at -1 -1)
					(if gDisguised
						(PrintDebby 28 112) ; #title "She says:" #width 270 #at -1 -1)
						(= disguiseFailed 1)
					else
						(PrintDebby 28 77)
					)                     ; #title "She says:" #width 270 #at -1 -1)
					(PrintDebby 28 78)
				)
			else                      ; #title "She says:" #width 270 #at -1 -1)
				(PrintNCE)
			)
		)
		(if (Said 'talk/man,sammy,guard')
			(if (<= (gEgo distanceTo: meatEater) 30)
				(if gDisguised
					(if (== g62Letter 2)
						(PrintSammy 28 156)
					else
						(PrintSammy 28 113)
						(PrintSammy 28 114)	
					)
				else
					(PrintSammy 28 73)
				)
			else
				(PrintNCE)
			)
		)
		(if (Said 'hit/woman') (PrintOther 28 79)) ; #at -1 20)
		(if (Said 'take/barrel') (PrintOther 28 80)) ; #at -1 20)
; (if(Said('smell/barrel'))
;            Print(28 81 #at -1 20)
;        )
		(if (Said 'rub,feel,hold/marble,sun,shooter')
			(if (gEgo has: 9)
				(if (not gVen) (Print 0 95) else (Print 0 94))
			else
				(PrintDHI)
			)
		)
		(if (or (Said 'use,put/ruler')
				(Said 'reach/ruler')
				(Said 'reach/pot/ruler'))
			(if (gEgo has: INV_RULER)
				(if (& (gEgo onControl:) ctlSILVER)
					(= usingRuler 1)
					(RoomScript changeState: 2)
				else
					(Print 28 93)
				)
			else                 ; There's no reason to do that here.
				(PrintDHI)
			)
		)
		(if (Said 'use,drop,put,shoot/marble')
			(if (gEgo has: 9)
				(if (& (gEgo onControl:) ctlSILVER)
					(= marbleDown 1)
					(RoomScript changeState: 2)
				else
					(Print 28 93)
				)
			else                 ; There's no reason to do that here.
				(Print 28 90)
				(Print 28 91)
			)
		)
		(if (Said 'break,smash/pot,urn') (PrintOther 28 103)) ; #at -1 20) // Destuction of property is not in the General's guide for proper treatment of the King's subjects.
		(if (Said 'open/pot,urn') (PrintOther 28 104)) ; #at -1 20) // The pots are already open.
		(if (Said '(dig<in)/pot,urn')
			(if (& (gEgo onControl:) ctlSILVER)
				(RoomScript changeState: 2)
			else
				(PrintNCE)
			)
		)
		(if (Said 'give,use/ring')
			(if (gEgo has: 18) (Print 28 94) else (PrintDHI))
		)
		(if (not [gArmor 2]) ; you don't have the armor
			(if (Said 'look/armor')
				(PrintOther 28 161)
			)
		)			
		(if (Said 'look,search>')
			(if (Said '/flower')
				(if [gFlowerGiven 2]
					(PrintOther 0 98)
				else
					(PrintOther 0 97)
				)
			)
			(if (Said '/urn,pot')
				(if (& (gEgo onControl:) ctlSILVER)
					(RoomScript changeState: 2)
				else
					(PrintOther 28 87)
				)
			)                         ; #width 280 #at -1 8) // These flowerless pots seem to have been neglected for many years.
			(if (Said '/woman')
				(PrintOther 28 21) ; #width 270 #at -1 14)
				(PrintOther 28 22)
			)                     ; #width 270 #at -1 14)
			(if (Said '/man') 
				(if [gArmor 2]
					(PrintOther 28 159)		
				else
					(PrintOther 28 23) ; #width 270 #at -1 14)
				)
			)
			(if (Said '/tavern,building,house') (PrintOther 28 86)) ; #width 270 #at -1 14) // This cozy hut looks like it's been here for a long time. And so does its owner.
			(if (Said '/barrel') (PrintOther 28 27))
			(if (Said '[/!*]') (PrintOther 28 85))
			; this will handle just "look" by itself ; #width 280 #at -1 8)
		)
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_RETURN)
				(if message (DisposeTextScript changeState: 5))
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 44)
		)
		(if (<= (gEgo distanceTo: seller) 40)
			(if (== (IsOwnedBy INV_LETTER 28) FALSE)
				(if (not repeat_)
					(if (not message)
						(if (or (== gDisguised 0) disguiseFailed)
							(BerateScript changeState: 2)
							(= message 1)
						)
					)
				)
			)
		else
			(= repeat_ 0)
			(if message
				(if gPrintDlg (gPrintDlg dispose:))
				(= message 0)
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
	(Print textRes textResIndex #width 280 #at -1 10)
)

(procedure (PrintSammy textRes textResIndex)
	(= gWndColor 7)
	(= gWndBack 1)
	(Print
		textRes
		textResIndex
		#width
		280
		#at
		-1
		20
		#title
		{He says:}
	)
	(= gWndColor 0) ; clBLACK
	(= gWndBack 15)
)
                    ; clWHITE
(procedure (PrintDebby textRes textResIndex)
	(= gWndColor 14) ; clYELLOW
	(= gWndBack 8)  ; clGREY
	(Print
		textRes
		textResIndex
		#width
		280
		#at
		-1
		20
		#title
		{She says:}
	)
	(= gWndColor 0) ; clBLACK
	(= gWndBack 15)
)
                    ; clWHITE
(instance BerateScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(2
				(seller
					loop: 0
					cel: 0
					setCycle: End BerateScript
					cycleSpeed: 1
				)
				(= gWndColor 14) ; clYELLOW
				(= gWndBack 8)  ; clGREY
				(Print 28 (Random 1 19) #dispose #at 100 90)
				(= gWndColor 0) ; clBLACK
				(= gWndBack 15)
			)                   ; clWHITE
			(3
				(= cycles 12)
				(seller cel: 0 loop: 1 setCycle: Fwd)
			)
			(4
				(= seconds 2)
				(seller cel: 0 loop: 2 setCycle: End)
			)
			(5
				(if message
					(if (not repeat_)
						(if gPrintDlg
							(gPrintDlg dispose:)
							(= message 0)
							(= repeat_ 1)
						)
					)
				)
			)
		)
	)
)

(instance letterScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(= cycles 10)
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo put: INV_LETTER 28)
			)
			(2
				(= cycles 14)
				(if message
					(if (not repeat_)
						(if gPrintDlg
							(gPrintDlg dispose:)
							(= message 0)
							(= repeat_ 1)
						)
					)
				)
				(= message 0)
				(= repeat_ 1)
				(PrintDebby 28 101)
			)                      ; #width 290 #at -1 40 #title "She says:") // What do you have there?
			(3
				(= cycles 7)
				(PrintDebby 28 97) ; #width 290 #at -1 40 #title "She says:") // It's a letter from my husband! He wrote this over 10 years ago!
				(PrintDebby 28 98) ; #width 290 #at -1 40 #title "She says:") // Maybe you're not as bad as they say. As far as I'm concerned your a fine gentleman!
				(gGame changeScore: 3)
			)
			(4
				(= cycles 1)
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
			)
			(5
				(PrintDebby 28 99) ; #width 290 #at -1 40 #title "She says:") // Would you like to buy some meat?
				(Print 28 100 #width 280 #at -1 40 #title {You think:})
			)
		)
	)
)
                                                                       ; The smell certainly is enticing.
(instance eatScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(if [gArmor 2]
					(meatEater loop: 0)
				else
					(meatEater loop: 3)
				)
				(meatEater cel: 0 setCycle: End self cycleSpeed: 3)
			)
			(2
				(if [gArmor 2]
					(meatEater loop: 1)
				else
					(meatEater loop: 4)
				)
				(meatEater cel: 0 setCycle: End self)
			)
			(3
				(= cycles 24)
				(if [gArmor 2]
					(meatEater loop: 2)
				else
					(meatEater loop: 5)
				)
				(meatEater cel: 0 setCycle: Fwd)
			)
			(4
				(= cycles (Random 20 60))
				(if [gArmor 2]
					(meatEater loop: 0)
				else
					(meatEater loop: 3)
				)
				(meatEater setCycle: CT)
			)
			(5 (eatScript changeState: 1))
		)
	)
)

(instance DisposeTextScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(5
				(if gPrintDlg
					(gPrintDlg dispose:)
					(= message 0)
					(= repeat_ 1)
				)
			)
		)
	)
)
(instance proposeScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo hide:)
				
				(alterEgo show: posn: (gEgo x?)(gEgo y?) view: 233 cel: 0 setCycle: End self cycleSpeed: 2)
				(if (> (gEgo x?) (seller x?))
					(alterEgo loop: 1)	
				else
					(alterEgo loop: 0)
				)
			)
			(2 (= cycles 10)
				
			)
			(3
				(if (== (IsOwnedBy INV_LETTER 28) TRUE)
					(PrintDebby 28 147)
					(PrintSammy 28 148)
					(PrintDebby 28 149)
				else
					(PrintOther 28 146)
				)
				(self cue:)	
			)
			(4
				(alterEgo setCycle: Beg self)	
			)
			(5
				(gEgo show:)
				(alterEgo hide:)
				
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)	
			)
		)
	)
)

(instance seller of Prop
	(properties
		y 145
		x 131
		view 405
	)
)

(instance meatEater of Prop
	(properties
		y 154
		x 260
		view 71
	)
)

(instance alterEgo of Prop
	(properties
		y 1
		x 1
		view 1
	)
)

(instance flower of Prop
	(properties
		y 141
		x 145
		view 97
	)
)
