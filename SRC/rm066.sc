;;; Sierra Script 1.0 - (do not remove this comment)
; + 4 SCORE  //
(script# 66)
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
	rm066 0
)

(local

; Scientist's House (Inside)



	rando
	chestOpen =  0
	goingToChest =  0
	lensInHand =  0
	
	burned = 0
)

(instance rm066 of Rm
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
				(gEgo posn: 115 154 loop: 3)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(scientist
			init:
			setScript: scientistScript
			cycleSpeed: 2
			ignoreActors:
			ignoreControl: ctlWHITE
		)
		(chest init: setScript: chestScript)
		(fire init: setCycle: Fwd cycleSpeed: 3 setScript: proposeScript)
		(lens init: setScript: otherScientistScript)
		(alterEgo init: hide: ignoreActors:)
		(scientistScript changeState: 12)
		(flower init: hide:)
		(if [gFlowerGiven 6] (flower show:))
		
		(gTheMusic number: 66 loop: -1 play:)
		
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState &tmp dyingScript)
		(= state mainState)
		(switch state
			(0)
			(1
				(gEgo
					setMotion: MoveTo 161 146 self
					ignoreControl: ctlWHITE
				)
			)
			(2
				(gEgo loop: 1 observeControl: ctlWHITE)
			)
			; touching the fire
			(3
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				
				(gEgo setMotion: MoveTo 156 129 self ignoreControl: ctlWHITE)	
			)
			(4 
				(if gDisguised
					(PrintOther 0 84)
					(= gDisguised 0)
					(RunningCheck)
				)
				(gEgo loop: 3 hide:)
				(alterEgo show: posn: (gEgo x?)(gEgo y?) view: 236 loop: 0 cel: 0 setCycle: End self cycleSpeed: 2)
			)
			(5	(= cycles 8)
				(if burned
					(PrintOther 66 80)	
				else
					(PrintOther 66 78)
				)
			)
			(6	(= cycles 2)
				(gTheSoundFX number: 202 play:)	
				(DrawPic 166 0 1 0)
				(alterEgo loop: 1)
					
			)
			(7	(= cycles 2)
				(DrawPic 66 0 1 0)
			)
			(8	(= cycles 2)
				(DrawPic 166 0 1 0)	
			)
			(9	(= cycles 2)
				(DrawPic 66 0 1 0)
			)
			(10
				(if burned
					(PrintOther 66 81)	
				else
					(PrintOther 66 79)
					(++ burned)	
				)
				(= gHlth (- gHlth 4))
				
				(gEgo show:)
				(alterEgo hide:)
				
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				
				(if (< gHlth 1)
					(= gDeathIconEnd 1)
					(= dyingScript (ScriptID DYING_SCRIPT))
					(dyingScript
						caller: 273
						register:
							{Ouch! That hurt just a bit too much. Be wary of your health when deciding to test the limits of your body.}
					)
					(gGame setScript: dyingScript)
				)	
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if
				(and
					(& (pEvent modifiers?) emRIGHT_BUTTON)
					(not gProgramControl)
				)
				(if
					(checkEvent
						pEvent
						(scientist nsLeft?)
						(scientist nsRight?)
						(scientist nsTop?)
						(scientist nsBottom?)
					)
					(Print 66 0 #width 280 #at -1 140)
				else                                  ; The man has a wild unkempt manner about him. His goggles stick out as quite odd. He is clearly brilliant, but you're not sure just how crazy he might be.
					(if
						(==
							ctlNAVY
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                  ; Bellows
						(PrintOther 66 50)
					)
					(if
						(==
							ctlGREEN
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                               ; Ladder
						(PrintOther 66 57)
					)
					(if
						(or
							(==
								ctlYELLOW
								(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
							)                                                             ; Flasks
							(checkEvent pEvent 97 126 71 81)
							(checkEvent pEvent 215 247 72 86)
							(checkEvent pEvent 37 57 106 122)
						)
						(PrintOther 66 58)
					)
					(if
						(checkEvent
							pEvent
							(chest nsLeft?)
							(chest nsRight?)
							(+ (chest nsTop?) 14)
							(chest nsBottom?)
						)
						(Print 66 60 #width 280 #at -1 140)
					)                                  ; The man has a wild unkempt manner about him. His goggles stick out as quite odd. He is clearly brilliant, but you're not sure just how crazy he might be.
					(if (checkEvent pEvent 88 99 107 120) ; bucket
						(PrintOther 66 51)
					)
					(if
						(or
							(checkEvent pEvent 93 133 62 68) ; pipe
							(checkEvent pEvent 172 222 62 68)
						)
						(PrintOther 66 52)
					)
					(if (checkEvent pEvent 137 172 59 125) ; fireplace
						(PrintOther 66 53)
					)
					(if (checkEvent pEvent 178 193 73 93) ; tools
						(PrintOther 66 54)
					)
					(if (checkEvent pEvent 173 187 100 123) ; firewood
						(PrintOther 66 55)
					)
					(if (checkEvent pEvent 153 174 144 160) ; barrel
						(PrintOther 66 59)
					)
				)
			)
		)         ; end else
		(if (Said 'smell[/!*]')
			(PrintOther 66 82)	
		)
		
		(if (Said 'give/flower')
			(if (gEgo has: 21)
				(if [gFlowerGiven 6]
					(PrintOther 30 48)
				else
					(PrintColin 66 75)
					(= [gFlowerGiven 6] 1)
					(-- gFlowers)
					(if (== gFlowers 0) (gEgo put: 21))
					((gInv at: 21) count: gFlowers)
					(flower show:)
					(gGame changeScore: 1)
				)
			else
				(PrintDHI)
			)
		)
		(if
			(or
				(Said 'give/ring[/man,colin]')
				(Said 'give/man,colin/ring')
				(Said 'propose[/marriage,colin,man]')
			)
			(if (gEgo has: 18)
				; kneeling animation
				(proposeScript changeState: 1)	
			else
				(PrintDHI)
			)
		)
		(if (Said 'wear,use,(put<on)/goggles')
			(if (gEgo has: INV_GOGGLES)
				(if gDisguised
					(PrintOther 0 53)
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
		(if (or (Said 'touch,feel/fire')
				(Said 'put/hand/fire'))
			(if (<= (gEgo distanceTo: fire) 50)
				(PrintOK)
				(self changeState: 3)	
			else
				(PrintNCE)
			)
		)
		
		(if (Said 'run') (Print 0 88))
		(if (Said 'use/map') (Print 0 88))
		(if (Said 'press/button') (PrintOther 60 32))
		(if (Said 'get/(flask<ye)') (Print 18 75))
		(if (Said '(pick<up),take>')
			(if (Said '/goggles')
				(if (gEgo has: INV_GOGGLES)
					(PrintATI)
				else
					(PrintOther 66 30)
				)
			)
			(if (Said '/bellows') (PrintOther 66 47))
			(if (Said '/tool') (PrintOther 66 47))
			(if (Said '/wood') (PrintOther 66 47)) ; firewood
			(if (Said '/ladder') (PrintOther 66 47))
			(if (Said '/flask,container,glass,vial')
				(PrintOther 66 48)
			)
			(if (Said '/*')
				; this will handle "look anyword"
				(Print {You don't need that.} #width 280 #at -1 10)
			)
		)
		(if (Said 'look>')
			(if (Said '/flower')
				(if [gFlowerGiven 6]
					(PrintOther 0 98)
				else
					(PrintOther 0 97)
				)
			)
			(if (Said '/man,scientist,colin') (PrintOther 66 11)) ; The man may be old, but behind the strange goggles there is an intelligence in his eyes. He appears to be a lonely person, but one with great passion.
			(if (Said '/goggles') (PrintOther 66 29))
			(if (Said '/bellows') (PrintOther 66 50))
			(if (Said '/bucket,water') (PrintOther 66 51))
			(if (Said '/pipe') (PrintOther 66 52))
			(if (Said '/chimney,fireplace,fire') (PrintOther 66 53))
			(if (Said '/tool') (PrintOther 66 54))
			(if (Said '/wood') (PrintOther 66 55)) ; firewood
			(if (Said '/bench,counter') (PrintOther 66 56))
			(if (Said '/ladder') (PrintOther 66 57))
			(if (Said '/flask,container,glass,vial')
				(PrintOther 66 58)
			)
			(if (Said '/barrel') (PrintOther 66 59))
			(if (Said '/chest') (PrintOther 66 60))
			(if (Said '[/!*]') (PrintOther 66 12))
			; this will handle just "look" by itself ; This place is quite unlike the magician's room you awoke in earlier. It's considerably more grey and silver. You wonder whose magic is stronger.
		)
		(if (Said 'open>')
			(if (Said '/chest')
				(if (<= (gEgo distanceTo: chest) 50)
					(chestScript changeState: 10)
				else
					(PrintNCE)
				)
			)
			(if (Said '/*') (Print 66 49))
		)
		(if (Said 'talk/man,scientist')
			(if (<= (gEgo distanceTo: scientist) 90)
				(cond 
					((== g62Package 1) (givePackage))
					(gDisguised (PrintColin 66 33))
					(else (PrintColin 66 15))
				)
			else                            ; You've got to see my new inventions. Lasers and goggles are my speciality. I've made all kinds of advances in the scientific viewing of the world.
				(PrintNCE)
			)
		)
		(if (Said 'give>')
			(if (Said '/package') (givePackage))
			(if (Said '/titanite')
				(if (gEgo has: 23) (giveTitanite) else (PrintDHI))
			)
		)
		(if (Said '(ask<about),(ask<for)>')
			(cond 
				((<= (gEgo distanceTo: scientist) 90)
					(if (Said '/man,scientist,colin,name') (PrintColin 66 44))
					(if (Said '/package') (givePackage))
					(if (Said '/science') (PrintColin 66 18)) ; What would you like to know? It will find an answer for everything.
					(if (Said '/magnet') (PrintColin 66 19)) ; Magnets can mess up electronics, causing all kinds of weird things. I even heard of a janitor using one to get a better score on a gambling machine!
					(if (Said '/wizard') (PrintColin 66 20)) ; I don't have any knowledge of that.
					(if (Said '/ghost') (PrintColin 66 20))
					(if (Said '/carmyle') (PrintColin 66 21)) ; From what I hear, they are a superstitious bunch.
					(if (Said '/cave') (PrintColin 66 22)) ; Sorry, I don't get out much.
					(if (Said '/troll,monster') (PrintColin 66 23)) ; Sorry, I'm more of a chemistry-type person. Carbon-based life critters are not my thing.
					(if (Said '/laser') (PrintColin 66 24)) ; My defense system is made harnessing the power of electricity. I'm working on a portable model also.
					(if (Said '/model') (PrintColin 66 25)) ; It's not quite ready yet. It takes a lot of energy, and making a portable generator is no small task.
					(if (Said '/princess') (PrintColin 66 26)) ; Yes, I hear she is lost. I know how sad it can be to have a lost child.
					(if (Said '/son,family')
						(PrintColin 66 27) ; Soon I will see my son again. We got separated when his transmitter malfunctioned.
						(PrintColin 66 28)
					)                  ; You should play JummyBummy Space Adventure 2 to find out what happened to him.
					(if (Said '/transmitter,jummybummy')
						(PrintColin 66 34)
						(PrintColin 66 35)
					)
					(if (Said '/goggles')
						(PrintColin 66 36)
						(if (not (gEgo has: INV_GOGGLES))
							(PrintColin 66 37)
							(ProgramControl)
							(SetCursor 997 (HaveMouse))
							(= gCurrentCursor 997)
							(= goingToChest 1)
						else
							(PrintColin 66 43)
						)
					)
					(if (Said '/bellows') (PrintColin 66 61))
					(if (Said '/bucket,water') (PrintColin 66 62))
					(if (Said '/pipe') (PrintColin 66 63))
					(if (Said '/chimney,fireplace,fire') (PrintColin 66 64))
					(if (Said '/tool') (PrintColin 66 65))
					(if (Said '/wood') (PrintColin 66 66)) ; firewood
					(if (Said '/bench,counter') (PrintColin 66 67))
					(if (Said '/ladder') (PrintColin 66 68))
					(if (Said '/potion') (PrintColin 66 76))
					(if (Said '/flask,container,glass,vial,titanite')
						(if (gEgo has: 23)
							(giveTitanite)
						else
							(PrintColin 66 69)
						)
					)
					(if (Said '/barrel') (PrintColin 66 70))
					(if (Said '/chest') (PrintColin 66 71))
					; peoples of shelah
					(if (Said '/bobby') (PrintColin 0 91))
					(if (Said '/leah') (PrintColin 48 16))
					(if (Said '/sammy') (PrintColin 0 91))
					(if (Said '/deborah, woman') (PrintColin 0 92))
					(if (Said '/rose') (PrintColin 0 95))
					(if (Said '/sarah') (PrintColin 48 17))
					(if (Said '/hans') (PrintColin 0 91))
					(if (Said '/lex') (PrintColin 48 18))
					(if (Said '/colin, man') (PrintColin 0 91))
					(if (Said '/longeau') (PrintColin 48 19))
					(if (Said '/moon,carmyle') (PrintColin 48 9))
					(if (Said '/gyre') (PrintColin 48 13))
					(if (Said '/*') (PrintColin 66 17))
				)
				((Said '/*') (PrintNCE))
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (& (gEgo onControl:) ctlMAROON)
			(gRoom newRoom: 50)
		)
	)
)

(instance chestScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1      ; move to chest
				(scientist
					view: 309
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo 104 150 self
				)
				(RoomScript changeState: 1)
			)
			(2      ; move to chest
				(scientist
					setMotion: MoveTo (+ (chest x?) 20) 157 self
					setPri: 14
				)
			)
			(3      ; open chest
				(scientist
					view: 402
					loop: 10
					cel: 0
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(4
				(chest setCycle: End self cycleSpeed: 3)
				(gTheSoundFX number: 204 play:)
			)                                           ; chest opens
			(5       ; bending down
				(scientist loop: 11 cel: 0 setCycle: End self)
			)
			(6
				(PrintColin 66 45)
				(scientist loop: 11 cel: 2 setCycle: Beg self)
			)
			(7
				(scientist loop: 10 cel: 2 setCycle: Beg self)
				(chest setCycle: Beg)
			)
			(8
				(scientist
					view: 309
					setCycle: Walk
					cycleSpeed: 0
					setMotion: MoveTo (- (gEgo x?) 20) (+ (gEgo y?) 2) self
					setPri: -1
				)
			)
			(9
				(PrintColin 66 38)
				(PrintColin 66 39)
				(if (not (gEgo has: INV_GOGGLES))
					(Print
						{\nWord-finding Goggles added to your Inventory.}
						#title
						{Goggles}
						#icon
						262
					)
					; PrintColin(66 40)
					(gEgo get: INV_GOGGLES)
					(gGame changeScore: 1)
					(PrintColin 66 41)
					(PrintColin 66 42)
				else
					(PrintColin 66 43)
				)
				(= goingToChest 0)
				(scientistScript changeState: 12)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			; ego tries and fails to open chest
			(10
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo (+ (chest x?) 30) 157 self
					ignoreControl: ctlWHITE
					setPri: 14
				)
			)
			(11
				(gEgo hide:)
				(alterEgo
					show:
					view: 232
					loop: 1
					cel: 0
					posn: (gEgo x?) (gEgo y?)
					setCycle: End self
					cycleSpeed: 2
					setPri: 14
				)
			)
			(12
				(= cycles 6)
				(Print {It's locked.} #at -1 10)
			)
			(13
				(alterEgo setCycle: Beg self)
			)
			(14
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(alterEgo hide: setPri: -1)
				(gEgo show: observeControl: ctlWHITE setPri: -1)
			)
		)
	)
)

(instance scientistScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1      ; move to chair
				(if goingToChest
					(chestScript changeState: 1)
				else
					(scientist
						view: 309
						setCycle: Walk
						cycleSpeed: 0
						setMotion: MoveTo 104 143 self
					)
				)
			)
			(2
				(= cycles 10)
				(scientist view: 402 loop: 4 cel: 0)
			)
			(3      ; sit down
				(scientist
					view: 402
					loop: 4
					cel: 0
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(4 (= cycles 15))    ; sitting for a time
			(5      ; lifting lens to face
				(scientist
					view: 402
					loop: 5
					cel: 0
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(6      ; lower head to look
				(scientist
					view: 402
					loop: 6
					cel: 0
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(7 (= cycles 30))     ; lookiing through
			(8      ; lower head to look
				(scientist
					view: 402
					loop: 6
					cel: 2
					setCycle: Beg self
					cycleSpeed: 2
				)
			)
			(9      ; lower head to look
				(scientist
					view: 402
					loop: 5
					cel: 2
					setCycle: Beg self
					cycleSpeed: 2
				)
			)
			(10      ; get up
				(scientist
					view: 402
					loop: 4
					cel: 3
					setCycle: Beg self
					cycleSpeed: 2
				)
			)
			(11 (= cycles 15))     ; stand a moment
			(12
				(if goingToChest
					(chestScript changeState: 1)
				else
					(scientist
						view: 309
						setCycle: Walk
						cycleSpeed: 0
						setMotion: MoveTo 190 130 self
					)
				)
			)
			(13 (= cycles 13))     ; tinker with items
			(14
				(otherScientistScript changeState: 1)
			)
		)
	)
)

(instance otherScientistScript of Script
	(properties)
	                                     ; used becaues after 16 or 17 cases in the STUDIO template, things don't work right
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1      ; putting hand out to take lens
				(scientist
					view: 402
					loop: 7
					cel: 0
					setCycle: End self
					cycleSpeed: 3
				)
			)
			(2      ; pulls out hammer
				(scientist
					x: (+ (scientist x?) 2)
					loop: 8
					cel: 0
					setCycle: End self
				)
				(lens show:)
				(= lensInHand 0)
			)
			(3
				(= cycles 20)     ; hammers
				(scientist loop: 9 cel: 0 setCycle: Fwd cycleSpeed: 3)
			)
			(4      ; puts hammer away
				(scientist loop: 8 cel: 2 setCycle: Beg self)
			)
			(5      ; takes lens
				(scientist
					x: (- (scientist x?) 2)
					loop: 7
					cel: 2
					setCycle: Beg self
				)
				(lens hide:)
				(= lensInHand 1)
			)
			(6
				(scientistScript changeState: 1)
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
				(if (> (gEgo x?) (scientist x?))
					(alterEgo loop: 1)	
				else
					(alterEgo loop: 0)
				)
			)
			(2 (= cycles 10)
				
			)
			(3
				(PrintColin 66 77)
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

(procedure (givePackage)
	(cond 
		((== g62Package 1)
			(= gWndColor 8) ; clGREY
			(= gWndBack 11) ; clCYAN
			(PrintColin 66 13)
			(PrintColin 66 14)
			(gGame changeScore: 1)
			(++ g62Package)
			(if (gEgo has: 19) (gEgo put: 19))
			(if (not gHardMode) (PrintColin 66 16)) ; Perhaps you can use the goggles to help out someone else, too.
			(= gWndColor 0) ; clBLACK
			(= gWndBack 15)
		)
		((== g62Package 2) (Print {You've already given the package, genius!})) ; clWHITE
		(else (PrintColin 66 46))
	)
)

(procedure (giveTitanite)
	(PrintColin 66 72)
	(PrintColin 66 73)
	(Print 66 74 #title {Titanite Flask:} #icon 275)
	(++ gFlask)
	; ++gFullFlask
	(= g46Flask 1)
	(gGame changeScore: 2)
	(gEgo get: 22 put: 23)
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

(procedure (PrintColin textRes textResIndex)
	(= gWndColor 8)
	(= gWndBack 11)             ; clDARKBLUE
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
	(= gWndColor 0)
	(= gWndBack 15)
)

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 10)
)

(instance alterEgo of Prop
	(properties
		y 164
		x 68
		view 232
		loop 0
	)
)

(instance flower of Prop
	(properties
		y 116
		x 226
		view 97
	)
)

(instance scientist of Act
	(properties
		y 129
		x 141
		view 402
	)
)

(instance fire of Prop
	(properties
		y 105
		x 156
		view 114
	)
)

(instance lens of Prop
	(properties
		y 115
		x 205
		view 115
	)
)

(instance chest of Prop
	(properties
		y 166
		x 58
		view 115
		loop 2
	)
)
