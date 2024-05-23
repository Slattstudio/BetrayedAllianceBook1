;;; Sierra Script 1.0 - (do not remove this comment)
; Score +5
(script# 30)
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

(public
	rm030 0
)

(local

; Inside Hermit Cave



	; (use "sciaudio")
	comingIn =  1
	printTime
	message =  0
	repeat_ =  0
	; snd
	winning =  0
	erasmusLooking =  0 ; 0 = left, 1 = right
	azizaLooking =  1
)                  ; 0 = left, 1 = right

(instance rm030 of Rm
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
		(laura
			init:
			ignoreControl: ctlWHITE
			ignoreActors:
			setScript: winScript
		)
		(switch gPreviousRoomNumber
			(26 (gEgo posn: 163 40 loop: 2))
			; = comingIn 1
			; (RoomScript:changeState(1))
			(107
				(gEgo posn: 151 53 loop: 1)
				(= comingIn 0)
				(if [g107Solved 1]
					(cond 
						((not [g107Solved 2]) (winScript changeState: 1) (= winning 1))
						((not g30Flask) (winScript changeState: 5) (= winning 1))
					)
				)
			)
			(else 
				(gEgo posn: 163 40 loop: 2)
			)
		)
		; = comingIn 1
		; (RoomScript:changeState(1))
		(SetUpEgo)
		;(send gTheMusic:prevSignal(0)stop())
		
		(gTheMusic number: 30 loop: -1 play:)
		
		(gEgo init:)
		(= gEgoRunning 0)
		(RunningCheck)
		
		(alterEgo init: hide: ignoreActors: setScript: proposeScript)
		(hermit init: setScript: DialScript)
		(arcade init: setCycle: Fwd)
		(magnet init: ignoreActors: setPri: 15)
		(guestBook
			init:
			setPri: 5
			ignoreActors:
			setScript: hubrisScript
			hide:
		)
		(heroEyes init: cycleSpeed: 3 setScript: blinkScript)
		(erasmusEyes init: cycleSpeed: 3)
		(larryEyes init: cycleSpeed: 3)
		(wilcoEyes init: cycleSpeed: 3)
		(rosellaEyes init: cycleSpeed: 3)
		(azizaEyes init: cycleSpeed: 3)
		(heroSmile init: setScript: smileScript)
		(larryBrow init:)
		(wilcoSmile init:)
		(rosellaSmile init:)
		(flower init: hide: setPri: 5)
		(if [gFlowerGiven 3] (flower show:))
	)
)

; (send gGame:setSpeed(5))

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 1))
			(1
				; (send gTheMusic:prevSignal(0)stop()number(30)loop(-1)play())
; = snd aud (send snd:
;                    command("playx")
;                    fileName("music\\henry.mp3")
;                    volume("70")
;                    loopCount("-1")
;                    init()
;                )
				(laura setCycle: Walk setMotion: MoveTo 30 70)
				(if comingIn
					(ProgramControl)
					(SetCursor 997 (HaveMouse))
					(= gCurrentCursor 997)
					(gEgo
						setMotion: MoveTo 163 54 RoomScript
						ignoreControl: ctlWHITE
					)
				)
			)
			(2
				(= seconds 4)
				(PlayerControl)
				(gEgo observeControl: ctlWHITE)
				; = comingIn 0
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(if comingIn
					(= gWndColor 14)
					(= gWndBack 3)
					(Print
						30
						20
						#dispose
						#mode
						alCENTER
						#width
						280
						#title
						{Henry:}
					)
; Ello. 'Ow are you? 'Ave we met before?" #dispose #mode alCENTER #width 300 #title "Henry:
					(= gWndColor 0)
					(= gWndBack 15)
					(hermit setCycle: Fwd cycleSpeed: 1)
					(= message 1)
				)
			)
			(3
				(= seconds 6)
				(if comingIn
					(if message
						(if gPrintDlg
							(gPrintDlg dispose:)
							(hermit setCycle: CT)
							(= message 0)
						)
					)
					(= gWndColor 14)
					(= gWndBack 3)
					(Print 30 21 #dispose #width 280 #title {Henry:})
; I'm 'Enry the 'ermit, that's me. Me farther was an 'ermit and me murther was as 'ermit sos I come by me job rightly." #dispose #width 306 #title "Henry:
					(= gWndColor 0)
					(= gWndBack 15)
					(hermit setCycle: Fwd cycleSpeed: 1)
					(= message 1)
				)
				(= comingIn 0)
			)
			(4
				(if message
					(if gPrintDlg
						(gPrintDlg dispose:)
						(hermit setCycle: CT)
						(= message 0)
					)
				)
			)
		)
	)
	
	(method (handleEvent pEvent &tmp button [buffer 300])
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (not message)
					(if
						(==
							ctlSILVER
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                                                    ; erasmus
						(PrintLeft 30 27)
					)
					(if
						(==
							ctlRED
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                                                 ; hero
						(PrintLeft 30 26)
					)
					(if
						(==
							ctlBLUE
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                                                  ; larry
						(PrintLeft 30 28)
					)
					(if
						(==
							ctlNAVY
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                                                  ; wilco
						(PrintRight 30 24)
					)
					(if
						(==
							ctlBROWN
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                                                   ; rosella
						(PrintRight 30 29)
					)
					(if
						(==
							ctlGREY
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                                                  ; aziza
						(PrintRight 30 25)
					)
					(if
						(==
							ctlPURPLE
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)                                                                     ; sign
						(PrintRight 30 30)
					)
	; Shouldn't somebody warn Laura about taking a shower?
					(if
						(checkEvent
							pEvent
							(arcade nsLeft?)
							(arcade nsRight?)
							(arcade nsTop?)
							(arcade nsBottom?)
						)
						(PrintOther 30 22)
					)
	; Wow! The latest version of Sail Boat Xtreme. Looks like it only cost one gold to play.
					(if
						(checkEvent
							pEvent
							(hermit nsLeft?)
							(hermit nsRight?)
							(hermit nsTop?)
							(hermit nsBottom?)
						)
						(PrintOther 30 15)
					)
					(if
						(checkEvent
							pEvent
							(- (guestBook nsLeft?) 20)
							(guestBook nsRight?)
							(guestBook nsTop?)
							(guestBook nsBottom?)
						)
						(PrintOther 30 23)
					)
				)
			)
		)
; There lies before you a guestbook recording all 'Enry's visitors.
		(if (== (pEvent type?) evKEYBOARD)
			(if (== (pEvent message?) KEY_RETURN)
				(DialScript changeState: 1)
				(if winning (winScript cue:))
			)
		)                            ; (DialScript2:changeState(1))
		(if (Said 'listen[/!*]')
			(PrintOther 30 60)
			(PrintOther 30 61)	
		)
		(if (Said 'run') (Print 0 88))
		(if (or (Said 'look,use,read,open/portal,map')
				(Said 'map'))
			(Print 0 88)
		)
		(if (Said 'give/flower/rosella,hero,roger,larry,erasmus,aziza')
			(if (gEgo has: 21)
				(PrintHenry 30 66)
			else
				(PrintDHI)
			)	
		)
		(if (Said 'give/flower')
			(if (gEgo has: 21)
				(if [gFlowerGiven 3]
					(PrintOther 30 48)
				else
					(PrintHenry 30 49)
					(= [gFlowerGiven 3] 1)
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
		(if
			(Said
				'play,insert,use,pay/coin,gold,game,arcade,machine,sailboat'
			)
			(PrintOther 30 38)
			(gRoom newRoom: 107)
		)
		(if
			(or
				(Said 'give/ring[/man,henry]')
				(Said 'give/man,henry/ring')
				(Said 'propose[/marriage,henry,man]')
			)
			(if (gEgo has: 18)
				; kneeling animation
				(proposeScript changeState: 1)	
			else
				(PrintDHI)
			)
		)
		(if (or (Said 'sign/name,book')
				(Said 'use/pen'))	
			(if (not g30Signed)
				(PrintOther 30 35)
				(PrintOther 30 36)
				(gGame changeScore: 1)
				(hubrisScript changeState: 2)
				(= g30Signed 1)
			else
				(PrintOther 30 37)
			)
		)
		(if (or (Said 'use,put,stick/magnet')
				(Said 'cheat'))
			(if (gEgo has: 11)     ; magnet
				(PrintOther 30 39)
				(gEgo put: 11 30)
				(hubrisScript changeState: 5)
			else                              ; shake screen after 2 cycles
				(Print 0 86)
			)
		)
		(if (Said 'take/magnet')
			(if (== (IsOwnedBy 11 30) TRUE)
				(PrintOther 30 40)
				(gEgo get: 11)
			else
				(PrintOther 0 87)
			)
		)
		(if (Said 'hello/adventurer<fellow') (PrintOther 30 50))
		(if (Said 'talk/erasmus,roger,larry,hero')
			(PrintOther 30 54)
		)
		(if (Said 'talk/rosella,aziza') (PrintOther 30 55))
		(if (Said 'talk/adventurer')
			(PrintOther 30 65)
		)
		(if (Said 'talk/man,henry,hermit')
			(if gDisguised
				(PrintHenry 30 17)
				(PrintHenry 30 18)
			else
				(PrintHenry 30 0) ; An 'ermit's life used to be rough. But now all'eh adventurers are retired it's a great market.
				(if (gEgo has: INV_MARBLES)
					(if [g107Solved 1]
						(PrintHenry 30 19)		
					else	
						(PrintHenry 30 62)
					)
				)
			)
		)                         ; marble hint	
		(if (Said 'read/sign') (PrintOther 30 41))
		(if (Said 'read,look/book,guestbook')
			(PrintOther 30 42)
			(if (not g30Signed)
				(PrintOther 30 57)	
			)
		)
		(if (Said '(pick<up),take/book,guestbook')
			(PrintOther 18 41)
		)
		(if (Said '(ask<about)>')
			; = gWndColor 14 = gWndBack 3
			(if (Said '/cave') (PrintHenry 30 1)) ; Nothing to it really. An 'ermit's 'ome is an 'omely place.
			(if (Said '/game,poker,cribbage')
				(PrintHenry 30 2) ; No one really likes cribbage, but poker and Sail Boat Extreme(tm) are popular 'ere.
				(PrintHenry 30 16)
			)                     ; Erasmus even beat my score at Sail Boat Extreme(tm). Some say there's a way to cause a glitch, but I've never seen it.
			(if (Said '/hero,aziza,larry,roger,erasmus')
				(PrintHenry 30 3)
			)                    ; They've been here for decades now. I guess their time of need has ended.                       ; Erasmus'll tell ya I like a good game though. If ya beat my Sail Boat Extreme score I'll tell ya all 'bout it.
			(if (Said '/weather')
				(PrintHenry 30 8) ; What's the difference between an 'orse and the weather?
				(PrintHenry 30 9)
			)
			(if (Said '/glitch,cheat') (PrintHenry 30 64))                    
			(if (Said '/marble')
				(if (gEgo has: INV_MARBLES)	 
					(if [g107Solved 1]
						(PrintHenry 30 47)		
					else	
						(PrintHenry 30 62)
					)
				else
					(PrintHenry 30 63)
				)
			)
			(if (Said '/princess,carmyle') (PrintHenry 30 13))
			(if (Said '/wizard') (PrintHenry 30 14))
			(if (Said '/hermit') (PrintHenry 30 10)) ; An 'ermit's life used to be rough. But now all'eh adventurers are retired it's a great market.
			(if (Said '/fenrus') (PrintHenry 30 11)) ; Erasmus' old buddy left him to start a string of pizza-party-places. Very popular with the kiddies.
			(if (Said '/name') (PrintHenry 30 56))
			(if (Said '/*') (PrintHenry 30 12))
		)                         ; Are ya sure ya don't wanna talk about something interesting?
		; = gWndColor 0 = gWndBack 15
		(if (Said 'look>')
			(if (Said '/flower')
				(if [gFlowerGiven 3]
					(PrintOther 0 98)
				else
					(PrintOther 0 97)
				)
			)
			(if (Said '/table,desk') (PrintOther 30 58))
			(if (Said '/game,arcade') (PrintOther 30 22))
			(if (Said '/henry,hermit,man') (PrintOther 30 15))
			(if (Said '/laura,bow,shower') (PrintOther 30 30))
			(if (Said '/sign') (PrintOther 30 41))
			(if (Said '/erasmus') (PrintLeft 30 27))
			(if (Said '/hero') (PrintLeft 30 26))
			(if (Said '/larry') (PrintLeft 30 28))
			(if (Said '/roger') (PrintRight 30 24))
			(if (Said '/rosella,princess') (PrintRight 30 29))
			(if (Said '/aziza') (PrintRight 30 25))
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(PrintOther 30 43)
				(PrintOther 30 44)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (not comingIn)
			(if (== (gEgo onControl:) ctlMAROON)
				(gRoom newRoom: 26)
			)
		)
		(if (== (IsOwnedBy 11 30) TRUE)
			(magnet show:)
		else
			(magnet hide:)
		)
		
		(if message
			(= gMap 1)
			(= gArcStl 1)	
		else
			(= gMap 0)
			(= gArcStl 0)
		)
		
	)
)

(instance blinkScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (self changeState: 7))
			(1
				(heroEyes setCycle: End)
				(self changeState: 7)
			)
			(2
				(if azizaLooking   ; looking right
					(azizaEyes setCycle: End)
					(= azizaLooking 0)
				else
					(azizaEyes setCycle: Beg)
					(= azizaLooking 1)
				)
				(self changeState: 7)
			)
			(3
				(wilcoEyes setCycle: End)
				(self changeState: 7)
			)
			(4
				(if erasmusLooking   ; looking right
					(erasmusEyes setCycle: Beg)
					(= erasmusLooking 0)
				else
					(erasmusEyes setCycle: End)
					(= erasmusLooking 1)
				)
				(self changeState: 7)
			)
			(5
				(larryEyes setCycle: End)
				(self changeState: 7)
			)
			(6
				(rosellaEyes setCycle: End)
				(self changeState: 7)
			)
			(7 (= cycles (Random 3 15)))
			(8
				(self changeState: (Random 1 6))
			)
		)
	)
)

; CHARACTERS RANDOMLY SMILING //////////////////
(instance smileScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (self changeState: 1))
			(1
				(if (heroSmile cel?)
					(heroSmile cel: 0)
				else
					(heroSmile cel: 1)
				)
				(self changeState: 5)
			)
			(2
				(if (rosellaSmile cel?)
					(rosellaSmile cel: 0)
				else
					(rosellaSmile cel: 1)
				)
				(self changeState: 5)
			)
			(3
				(if (wilcoSmile cel?)
					(wilcoSmile cel: 0)
				else
					(wilcoSmile cel: 1)
				)
				(self changeState: 5)
			)
			(4
				(if (larryBrow cel?)
					(larryBrow cel: 0)
				else
					(larryBrow cel: 1)
				)
				(self changeState: 5)
			)
			(5 (= cycles (Random 15 25)))
			(6
				(self changeState: (Random 1 4))
			)
		)
	)
)

(instance hubrisScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(2
				(= seconds 2)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)
			(3
				(= cycles 1)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(4
				(PrintOther 30 32)
; All eyes are on you and you hear some snickering and snide remarks. It appears the guests consider you a bit pretentious to add your name to theirs.
				(PlayerControl)
			)
			(5 (= cycles 2))
			(6
				(= cycles 4)
				(ShakeScreen 2)
			)
			(7 (Print {BZZZT!} #font 9))
		)
	)
)

(instance DialScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(if message
					(if gPrintDlg
						(gPrintDlg dispose:)
						(= message 0)
						(hermit setCycle: CT)
						(RoomScript cue:)
					)
				)
			)
		)
	)
)

(instance winScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1
				(= cycles 4)
				(= [g107Solved 2] 1)
				(= winning 1)
			)
			(2
				(= seconds 5)
				(if message
					(if gPrintDlg
						(gPrintDlg dispose:)
						(hermit setCycle: CT)
						(= message 0)
					)
				)
				(= gWndColor 14)
				(= gWndBack 3)
				(Print 30 51 #dispose #width 280 #title {Henry:})
				(= gWndColor 0)
				(= gWndBack 15)
				(hermit setCycle: Fwd cycleSpeed: 1)
				(= message 1)
			)
			(3
				(= seconds 8)
				(if message
					(if gPrintDlg
						(gPrintDlg dispose:)
						(hermit setCycle: CT)
						(= message 0)
					)
				)
				(= gWndColor 14)
				(= gWndBack 3)
				(Print 30 52 #dispose #width 280 #title {Henry:})
				(= gWndColor 0)
				(= gWndBack 15)
				(hermit setCycle: Fwd cycleSpeed: 1)
				(= message 1)
				(= [g107Solved 2] 1)
				(gGame changeScore: 1)
			)
			(4
				(= cycles 1)
				(if (not [g107Solved 0])
					(winScript changeState: 8)
					(= winning 0)
				else
					(hermit setCycle: Fwd cycleSpeed: 1)
					(= winning 1)
				)
			)
			(5
				(= seconds 8)
				(if message
					(if gPrintDlg
						(gPrintDlg dispose:)
						(hermit setCycle: CT)
						(= message 0)
					)
				)
				(if [g107Solved 0]
					(if (not g30Flask)
						(= gWndColor 14)
						(= gWndBack 3)
						(Print 30 53 #dispose #width 280 #title {Henry:})
						(= gWndColor 0)
						(= gWndBack 15)
						(= gFlask (+ gFlask 1))
						(= gFullFlask (+ gFullFlask 1))
						(= message 1)
						(gGame changeScore: 2)
						(= g30Flask 1)
						(gEgo get: 22)
					)
				)
			)
			(6
				(= cycles 2)
				(if message
					(if gPrintDlg
						(gPrintDlg dispose:)
						(hermit setCycle: CT)
						(= message 0)
					)
				)
				(if [g107Solved 0]
					(Print 30 33 #icon 274 #title {Titanite Flask:})
				)
			)
			; = g107Solved[0] 1
			(7
				(if (== (IsOwnedBy 11 30) TRUE)
					(gEgo get: 11)
					(PrintOther 30 46)
				)
			)
			(8
				(if message
					(if gPrintDlg
						(gPrintDlg dispose:)
						(hermit setCycle: CT)
						(= message 0)
						(= winning 0)
					)
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
				(if (> (gEgo x?) (hermit x?))
					(alterEgo loop: 1)	
				else
					(alterEgo loop: 0)
				)
			)
			(2 (= cycles 10)
				
			)
			(3
				(PrintHenry 30 59)
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

(procedure (PrintHenry textRes textResIndex)
	(= gWndColor 14)
	(= gWndBack 3)
	(Print
		textRes
		textResIndex
		#width
		280
		#at
		-1
		140
		#title
		{He says:}
	)
	(= gWndColor 0)
	(= gWndBack 15)
)

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 280 #at -1 140)
)

(procedure (PrintLeft textRes textResIndex)
	(Print textRes textResIndex #width 140 #at 20 -1)
)

(procedure (PrintRight textRes textResIndex)
	(Print textRes textResIndex #width 140 #at 150 -1)
)

(instance laura of Act
	(properties
		y 70
		x 100
		view 314
		loop 1
	)
)

(instance alterEgo of Prop
	(properties
		y 104
		x 150
		view 128
		cel 10
	)
)
(instance hermit of Prop
	(properties
		y 59
		x 200
		view 25
		loop 3
	)
)

(instance arcade of Prop
	(properties
		y 62
		x 127
		view 26
	)
)

(instance guestBook of Prop
	(properties
		y 62
		x 163
		view 26
		loop 1
	)
)

(instance magnet of Prop
	(properties
		y 46
		x 140
		view 26
		loop 2
	)
)

(instance flower of Prop
	(properties
		y 61
		x 187
		view 97
	)
)

; EYES ///////////////////////////////
(instance heroEyes of Prop
	(properties
		y 91
		x 252
		view 116
		loop 1
	)
)

(instance erasmusEyes of Prop
	(properties
		y 83
		x 191
		view 116
		loop 0
	)
)

(instance larryEyes of Prop
	(properties
		y 134
		x 207
		view 116
		loop 2
	)
)

(instance wilcoEyes of Prop
	(properties
		y 122
		x 116
		view 116
		loop 3
	)
)

(instance rosellaEyes of Prop
	(properties
		y 94
		x 73
		view 116
		loop 4
	)
)

(instance azizaEyes of Prop
	(properties
		y 85
		x 131
		view 116
		loop 5
	)
)

; SMILES AND BROWS //////////////////////
(instance heroSmile of Prop
	(properties
		y 102
		x 254
		view 117
		loop 3
	)
)

(instance larryBrow of Prop
	(properties
		y 129
		x 207
		view 117
	)
)

(instance wilcoSmile of Prop
	(properties
		y 138
		x 116
		view 117
		loop 1
	)
)

(instance rosellaSmile of Prop
	(properties
		y 108
		x 71
		view 117
		loop 2
	)
)
