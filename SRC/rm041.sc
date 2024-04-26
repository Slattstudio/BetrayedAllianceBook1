;;; Sierra Script 1.0 - (do not remove this comment)
; + 2 SCORE //
(script# 41)
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
	rm041 0
)


; Mausoleum Closeup



; (use "sciaudio")
; snd

(instance rm041 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 42
		west 0
	)
	
	(method (init)
		(super init:)
		(self
			setScript: RoomScript
			setRegions: 200
			setRegions: 204
		)
		(switch gPreviousRoomNumber
			(102
				(gEgo posn: 167 146 loop: 3)
				(if g102Solved
					(RoomScript changeState: 9)
				else
					(gEgo posn: 167 146 loop: 3)
				)
			)
			; (send gEgo:get(INV_BLOCK))
			(67
				(gEgo posn: 167 146 loop: 2)
				(gTheMusic number: 25 loop: -1 play:)
			)
			(else 
				(gEgo posn: 166 183 loop: 3)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(= gEgoRunning 0)
		(RunningCheck)
		(lid init: setPri: 3)
		(lidBehind
			init:
			hide:
			posn: (- (lid x?) 5) (+ (lid y?) 8)
			setPri: 1
			loop: 2
		)
		(if g41Coffin (lid loop: 1) (lidBehind show:)) ; if used shovel ; blocks
		(if g102Solved   ; lid open
			(lid
				posn: (- (lid x?) 5) (+ (lid y?) 8)
				setPri: 1
				loop: 2
			)
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState &tmp button)
		(= state mainState)
		(switch state
			(0)
			(1       ; move to sarcophagus
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(gEgo
					ignoreControl: ctlWHITE
					setMotion: MoveTo 166 145 RoomScript
				)
			)
			(2 (= cycles 5) (gEgo loop: 3))
			(3
				(openGrave)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(4      ; move to urn
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(if (> (gEgo x?) 166)     ; ego on right
					(gEgo
						ignoreControl: ctlWHITE
						setMotion: MoveTo 244 145 self
					)
				else
					(gEgo
						ignoreControl: ctlWHITE
						setMotion: MoveTo 95 145 self
					)
				)
			)
			(5 (= cycles 5) (gEgo loop: 3))
			(6
				(if (> (gEgo x?) 166)     ; ego on right
					(PrintOther 41 27)
					(PrintOther 41 28)
				else     ; ego on left
					(PrintOther 41 25)
					(PrintOther 41 26)
				)
				(PlayerControl)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)
			(9 (= cycles 10))
			(10
				(= cycles 7)
				(PrintOther 41 5) ; The sarcophagus opens slowly as you think maybe this wasn't such a good idea.
				(PrintOther 41 4)
			)                    ; To your surprise there is no dead body or horrible odor, but only a dark pit with a ladder.
			(11
				(Print 41 6 #title {You think:} #at -1 20 #width 280) ; Let's find out what's down there.
				(gRoom newRoom: 67)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evJOYSTICK))
; (if(== (send pEvent:message) 3) // If pressed the Right arrow
;                (if(atTop)
;                    (RoomScript:changeState(6))
;                    = gMap 0
;                )
;            )
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if (checkEvent pEvent 87 103 94 121)   ; Left Urn
					(PrintOther 41 29)
				)
				(if (checkEvent pEvent 234 253 94 121)   ; Right Urn
					(PrintOther 41 29)
				)
				(if (checkEvent pEvent 90 99 127 133)  ; Left placard
					(if
					(and (& (gEgo onControl:) ctlGREY) (< (gEgo x?) 160))
						(PrintOther 41 25)
					else
						(PrintOther 41 30)
					)
				)
				(if (checkEvent pEvent 239 249 127 133)  ; Right placard
					(if
					(and (& (gEgo onControl:) ctlGREY) (> (gEgo x?) 160))
						(PrintOther 41 27)
					else
						(PrintOther 41 30)
					)
				)
				(if (checkEvent pEvent 140 196 117 142)  ; Sarcophagus
					(PrintOther 41 7)
				)                    ; The sarcophagus of Longeau Carmyle. He was well known for his wealth as well as his cruelty and ambition.
				(if (checkEvent pEvent 203 232 162 178)  ; Stone Sign
					(if (& (gEgo onControl:) ctlSILVER)
						(= gWndColor 0) ; clYELLOW
						(= gWndBack 7) ; clDARKBLUE
						(Print 41 8 #title {It reads:} #font 4 #width 200) ; Here lies Longeau Carmyle. His quest for eternal life may have eluded him, but his spirit lives on in his children.
						(= gWndColor 0) ; clYELLOW
						(= gWndBack 15) ; clDARKBLUE
						(Print 41 9 #title {You think:} #at -1 20)
					else
						(PrintOther 41 10)
					)
				)                         ; There is something written on that stone, but you can't make it out from there.
				(if (checkEvent pEvent 10 24 141 162)  ; Gravestone
					(if (& (gEgo onControl:) ctlRED)
						(= gWndColor 0) ; clYELLOW
						(= gWndBack 7) ; clDARKBLUE
						(Print 41 31 #title {It reads:} #font 4 #width 120)
						(= gWndColor 0) ; clYELLOW
						(= gWndBack 15)
					else              ; clDARKBLUE
						(PrintOther 41 10)
					)
				)
			)
		)                                 ; There is something written on that stone, but you can't make it out from there.
		(if
			(or
				(Said '[pry,force<open]/grave,coffin,slab,gravestone,lid/shovel')
				(Said 'use/shovel/grave,coffin,slab,gravestone')
				(Said 'open/(coffin,grave,slab,gravestone)/shovel')
				(Said 'pry<off/lid,slab/shovel')
			)
			(if (& (gEgo onControl:) ctlMAROON)
				; (if(atTop)
				; (if(not(gHardMode))
				(self changeState: 1)
			else
				; )
				(PrintOther 41 12)
			)
		)                          ; This isn't a good place to do that.
		(if (Said '[pry,force<open]//shovel') ; This should allow "pry open with shovel"
			(if (& (gEgo onControl:) ctlMAROON)
				; (if(not(gHardMode))
				(self changeState: 1)
			else
				; )
				(PrintOther 41 12)
			)
		)                          ; This isn't a good place to do that.
		(if
			(or
				(Said 'use/shovel')
				(Said '[pry,force<open]/shovel')
			)
			(if (& (gEgo onControl:) ctlMAROON)
				(if (not gHardMode)
					(self changeState: 1)
				else
					(PrintOther 41 11)
				)
			else                      ; What are you trying to use the shovel on?
				(PrintOther 41 12)
			)
		)                          ; This isn't a good place to do that.
		(if
		(Said '[pry,force<open]/grave,coffin,slab,gravestone')
			(if (& (gEgo onControl:) ctlMAROON)
				(PrintOther 41 13)
			else                  ; You will need an implement of some kind to do that.
				(PrintOther 41 12)
			)
		)                          ; This isn't a good place to do that.
		(if
		(or (Said 'use/sword') (Said '[pry,force<open]/sword'))
			(PrintOther 41 14)
		)                     ; That will only damage your sword.
		(if (Said '[pry,force<open]') (PrintOther 41 15)) ; How would you like to do that?

		(if (Said 'take,(pick<up)>')
			(if (Said '/ashes,vase')
				(Print 41 36 #at -1 10)	
			)		
		)
		(if (Said 'listen')
			(PrintOther 42 18)	
			(Print 42 19)
		)
		(if (Said '(look,read)>')
			(if (Said '/sign,message')
				(cond 
					((& (gEgo onControl:) ctlSILVER)      ; Longeau sign
						(= gWndColor 0) ; clYELLOW
						(= gWndBack 7) ; clDARKBLUE
						(Print 41 8 #title {It reads:} #font 4 #width 200) ; Here lies Longeau Carmyle. His quest for eternal life may have eluded him, but his spirit lives on in his children.
						(= gWndColor 0) ; clYELLOW
						(= gWndBack 15) ; clDARKBLUE
						(Print 41 9 #title {You think:} #at -1 20)
					)
					((& (gEgo onControl:) ctlGREY) (self changeState: 4)) ; urn signs
					((& (gEgo onControl:) ctlRED)                 ; gravestone
						(= gWndColor 0)   ; clYELLOW
						(= gWndBack 7)   ; clDARKBLUE
						(Print 41 31 #title {It reads:} #font 4 #width 140)
						(= gWndColor 0)   ; clYELLOW
						(= gWndBack 15)
					)
					(else (PrintNCE))     ; clDARKBLUE
				)
			)
			(if (Said '/sarcophagus,grave,slab')
				(cond 
					((& (gEgo onControl:) ctlMAROON)
						(if g41Coffin
							(if g70GotMap
								(PrintOther 41 3)
							else
								(PrintOther 41 1)
								(if (gEgo has: INV_BLOCK) (gEgo put: INV_BLOCK 102))
								(gRoom newRoom: 102)
							)
						else
							(PrintOther 41 2)
						)
					)
					((& (gEgo onControl:) ctlSILVER)
						(= gWndColor 0) ; clYELLOW
						(= gWndBack 7) ; clDARKBLUE
						(Print 41 8 #title {It reads:} #font 4 #width 200) ; Here lies Longeau Carmyle. His quest for eternal life may have eluded him, but his spirit lives on in his children.
						(= gWndColor 0) ; clYELLOW
						(= gWndBack 15) ; clDARKBLUE
						(Print 41 9 #title {You think:} #at -1 20)
					)
					((& (gEgo onControl:) ctlRED)
						(= gWndColor 0)   ; clYELLOW
						(= gWndBack 7)   ; clDARKBLUE
						(Print 41 31 #title {It reads:} #font 4 #width 140)
						(= gWndColor 0)   ; clYELLOW
						(= gWndBack 15)
					)
					(else (PrintOther 41 16)) ; clDARKBLUE
				)
			)
			(if (Said '/mausoleum') (PrintOther 41 7))
			(if (Said '/gravestone,epitaph,stone')
				(if (& (gEgo onControl:) ctlSILVER)
					(PrintOther 41 8)
					(Print 41 9 #title {You think:} #at -1 10)
				else
					(PrintOther 41 10)
				)
			)
			(if (Said '/urn,vase,placard')
				(if (& (gEgo onControl:) ctlGREY)
					(self changeState: 4)
				else
					(PrintNCE)
				)
			)
			(if (Said '[/!*]') (PrintOther 41 17))
		; this will handle just "look" by itself
		)
		(if (Said 'open/coffin,grave,slab')
			(cond 
				((not g41Coffin)
					(if (& (gEgo onControl:) ctlMAROON)
						(PrintOther 41 18)
					else               ; It appears to be sealed quite tightly. With a spade or shovel you think you could pry it loose.
						(PrintNCE)
					)
				)
				((not g70GotMap)
					(PrintOther 41 19) ; This layer has some strange blocks on it.
					(if (gEgo has: INV_BLOCK) (gEgo put: INV_BLOCK 102))
					(gRoom newRoom: 102)
				)
				(else (PrintOther 41 3))
			)
		)
		(if (Said 'climb,enter,(go<in)/grave,coffin,cave,hole,ladder')
			(if g67TableMoved	; variable denotes whether this cave has been completed.
				(if (not [gMissingBooks 4])
					(PrintOther 41 34)
					(gRoom newRoom: 67)
				else
					(PrintOther 41 35)	
				)
			else
				(PrintOther 41 33)
			)
		)
		(if (Said 'climb/wall,vine')
			(if (& (gEgo onControl:) ctlNAVY)
				(PrintOther 41 24)
			else
				(PrintNCE)
			)
		)
	)
	
	(method (doit)
		(super doit:)
	)
)

(procedure (openGrave)
	(if (gEgo has: INV_SHOVEL)
		(if (& (gEgo onControl:) ctlMAROON)
			(if (not g102Solved)
				(if (not g41Coffin) ; whether or not the slab has been removed
					(PrintOther 41 20) ; You pry the concrete slab off the sarcophagus only to find...
					(Print 41 21) ; There is another stone layer underneath. But this one is different.
					(PrintOther 41 37)
					(= g41Coffin 1)
					(gGame changeScore: 2)
					(PlayerControl)
					(if (gEgo has: INV_BLOCK) (gEgo put: INV_BLOCK 102))
					(gRoom newRoom: 102)
				else
					(PrintOther 41 32)
					(gRoom newRoom: 102)
				)
			else
				(PrintOther 41 22)
			)
		else
			(PrintNCE)
		)
	else
		(PrintOther 41 23)
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

; (instance aud of sciAudio
;   (properties)
;   (method (init)
;      (super:init())
;   )
; )
(instance lid of Prop
	(properties
		y 123
		x 168
		view 101
	)
)

(instance lidBehind of Prop
	(properties
		y 123
		x 168
		view 101
	)
)
