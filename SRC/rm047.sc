;;; Sierra Script 1.0 - (do not remove this comment)
; + 3 SCORE // gInt + 1 //
(script# 47)
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
	rm047 0
)

(local
;
; *                                Attic of Bar                                  *


	chestOpen =  0
	exiting =  0
	lookingForMarble =  0
	gettingBook =  0
)

(instance rm047 of Rm
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
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 155 115 loop: 2)
				(RoomScript changeState: 6)
			)
		)
		(bookshelf init: hide: ignoreActors:)
		(chest
			init:
			ignoreActors:
			setPri: 8
			setScript: chestScript
		)
		(alterEgo init: hide: ignoreActors: setScript: lookUnder)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		(if (== (gEgo onControl:) ctlMAROON)
			(if (not exiting) (self changeState: 1) (= exiting 1))
		)
		; (send gRoom:newRoom(44))
		(if chestOpen
			(if (>= (gEgo distanceTo: chest) 45)
				(chest setCycle: Beg)
				(= chestOpen 0)
			)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(==
						ctlNAVY
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                   ; bed
					(PrintOther 47 2)
				)
; (if((> (send pEvent:x) (bookshelf:nsLeft))and
;                    (< (send pEvent:x) (bookshelf:nsRight))and
;                    (> (send pEvent:y) (bookshelf:nsTop))and
;                    (< (send pEvent:y) (bookshelf:nsBottom)))
;                    (if(== gMissingBooks[0] 0)
;                        PrintOther(47 0)
;                    )(else
;                        PrintOther(47 1) //There are a few personal items here, a mirror, some drawings, a doll, but nothing of much interest.
;                    )
;                )(else
;                    (if((>  (send pEvent:x) 141)and   // beds
;                        (< (send pEvent:x) 182)and
;                        (> (send pEvent:y) 118)and
;                        (< (send pEvent:y) 174))
;                        PrintOther(47 2)
;                    )
;                )
				(if
					(==
						ctlCYAN
						(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
					)                                                                  ; chest
					(PrintOther 47 6)
				)
				(cond 
					(
						(==
							ctlBLUE
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                                             ; picture
						(PrintOther 47 9)
						(Print 47 35 #at -1 10)
					)
					((checkEvent pEvent 108 144 67 89)     ; window
						(PrintOther 47 3)
						(Print 47 4 #title {You think:} #width 280 #at -1 20)
					)
				)
				(if (checkEvent pEvent 80 95 59 75) (PrintOther 47 10)) ; wash cloth
				(if (checkEvent pEvent 115 126 92 98)  ; bowl
					(PrintOther 47 12)
				)
				(if (checkEvent pEvent 185 191 95 108)  ; urn
					(PrintOther 47 14)
				)
				(if (checkEvent pEvent 100 139 120 140)  ; rug
					(PrintOther 47 16)
				)
				(if
					(or
						(checkEvent pEvent 59 94 156 177)
						(==
							ctlRED
							(OnControl ocPRIORITY (pEvent x?) (pEvent y?))
						)
					)                                                                                                           ; barrel
					(PrintOther 47 5)
				)
				(if
					(checkEvent
						pEvent
						(chest nsLeft?)
						(chest nsRight?)
						(chest nsTop?)
						(chest nsBottom?)
					)                                                                                  ; chest
					(if chestOpen (PrintOther 47 8) else (PrintOther 47 7))
				)
			)
		)
		(if (Said 'move,lift,search,(look<under)/rug')
			(if (gEgo inRect: 100 114 163 150)
				(lookUnder changeState: 1)
			else
				(PrintNCE)
			)
		)
		(if (Said 'look<in/chest')
			(if chestOpen
				(if (== [gMissingBooks 0] 0)
					(PrintOther 47 0)
				else
					(PrintOther 47 1)
				)
			else
				(PrintOther 47 36)
			)
		)
		(if (Said 'rub,feel,hold/marble,sun,shooter')
			(if (gEgo has: 9)
				(if (not gJup) (Print 0 95) else (Print 0 94))
			else
				(PrintDHI)
			)
		)
		(if (Said 'search,(look<in)/bed,mattress')
			(if (gEgo inRect: 100 114 163 150)
				(= lookingForMarble 1)
				(lookUnder changeState: 6)
			else
				(PrintNCE)
			)
		)
		(if (Said '((look,search)<under)>')
			(if (Said '/bed')
				(if (gEgo inRect: 100 114 163 150)
					(lookUnder changeState: 6)
				else
					(PrintNCE)
				)
			)
			(if (Said '/mattress')
				(if (gEgo inRect: 100 114 163 150)
					(= lookingForMarble 1)
					(lookUnder changeState: 6)
				else
					(PrintNCE)
				)
			)
		)
		(if (Said 'open/drawer,desk')
			(if (gEgo inRect: 100 114 163 150)
				(self changeState: 3)
			else
				(PrintNCE)
			)
		)
		(if (Said 'open,unlock/chest')
			(cond 
				((& (gEgo onControl:) ctlRED)
					(if chestOpen
						(PrintItIs)
					else
						; (chest:setCycle(End)cycleSpeed(2))
						; (= chestOpen 1)
						(chestScript changeState: 1)
					)
				)
				((& (gEgo onControl:) ctlSILVER) (chestScript changeState: 7))
				(else (PrintNCE))
			)
		)
		(if (or (Said 'wash/face,self') (Said 'use/water'))
			(PrintOther 47 39)
		)
		(if (Said 'close/chest')
			(if (& (gEgo onControl:) ctlRED)
				(if (not chestOpen)
					(PrintItIs)
				else
					(chest setCycle: Beg cycleSpeed: 2)
					(= chestOpen 0)
				)
			else
				(PrintNCE)
			)
		)
		(if (Said '(look<in)/vase') (PrintOther 47 38))
		(if (Said '(look,search)>')
			(if (Said '/window,castle')
				(PrintOther 47 3)
				(Print 47 4 #title {You think:} #width 280 #at -1 20)
			)
			(if (Said '/desk,table') (PrintOther 47 40))
			(if (Said '/man')
				(PrintOther 47 9)
				(Print 47 35 #at -1 10)
			)
			(if (Said '/bed') (PrintOther 47 2))
			(if (Said '/cloth,towel') (PrintOther 47 10))
			(if (Said '/bowl') (PrintOther 47 12))
			(if (Said '/vase') (PrintOther 47 14))
			(if (Said '/rug') (PrintOther 47 16))
			(if (Said '/barrel') (PrintOther 47 5))
			(if (Said '/chest')
				(cond 
					((& (gEgo onControl:) ctlSILVER) (PrintOther 47 6)) ; left chest
					((& (gEgo onControl:) ctlRED) (PrintOther 47 7)) ; right chest
					(else (PrintOther 47 37))
				)
			)
			(if (Said '/toy,doll')
				(if chestOpen (PrintOther 47 19) else (Print 47 20)) ; It's just an old rag doll. It doesn't even make noises.
			)
			(if (Said '/picture')
				(if chestOpen
					(PrintOther 47 21)
				else                  ; It looks like a picture from an old fairy tale. What was it? Oh yes, 'The Princess and the Pea.'
					(PrintOther 47 9)
					(Print 47 35 #at -1 10)
				)
			)
			(if (Said '/mirror')
				(if chestOpen (PrintOther 47 22) else (Print 47 20)) ; You like this mirror. There are no tricky wizards looking back at you.
			)
			(if (Said '/mattress')
				(if (gEgo inRect: 100 114 163 150)
					(= lookingForMarble 1)
					(lookUnder changeState: 6)
				else
					(PrintNCE)
				)
			)
			(if (Said '/book')
				(if (== [gMissingBooks 0] 0)
					(if chestOpen
						(PrintOther 47 26)
					else                  ; There is a book about potion-making in the chest. It has the Library's marking on it.
						(PrintOther 47 20)
					)
				else
					(Print 0 63 #font 4 #width 280)
				)
			)
; (if (Said('/shelf,bookcase,bookshelf'))
;                (if(== gMissingBooks[0] 0)
;                    PrintOther(47 0)
;                )(else
;                    PrintOther (47 1) //There are a few personal items here, a mirror, some drawings, a doll, but nothing of much interest.
;                )
;            )
			(if (Said '/item')
				(if chestOpen
					(if (== [gMissingBooks 0] 0)
						(PrintOther 47 0)
					else
						(PrintOther 47 1)
					)
				else                     ; There are a few personal items here, a mirror, some drawings, a doll, but nothing of much interest.
					(PrintOther 47 27)
				)
			)
			(if (Said '[/!*]') (PrintOther 47 28))
		; this will handle just "look" by itself
		)
		(if (Said 'run') (Print 0 88))
		(if (or (Said 'look,use,read,open/portal,map')
				(Said 'map'))
			(Print 0 88)
		)
		(if (Said '(pick<up),take>')
			(if (Said '/book')
				(if (== [gMissingBooks 0] 0)
					(if chestOpen
						(chestScript changeState: 1)
						(= gettingBook 1)
					else
						(Print {You can't get to it from there.})
					)
				else
					(PrintATI)
				)
			)
			(if (Said '/mirror')
				(if (& (gEgo onControl:) ctlRED)
					(if chestOpen
						(PrintOther 47 29)
					else
						(Print {You can't get to it from there.})
					)
				else
					(PrintNCE)
				)
			)
			(if (Said '/toy')
				(if (& (gEgo onControl:) ctlRED)
					(if chestOpen
						(PrintOther 47 30)
					else
						(Print {You can't get to it from there.})
					)
				else
					(PrintNCE)
				)
			)
			(if (Said '/picture')
				(if (& (gEgo onControl:) ctlRED)
					(if chestOpen
						(PrintOther 47 31)
					else
						(Print {You can't get to it from there.})
					)
				else
					(if (& (gEgo onControl:) ctlGREY)
						(PrintOther 47 44)	
					else
						(PrintNCE)
					)
				)
			)
			(if (Said '/towel')
				(PrintOther 47 43)	
			)
		)
		(if (Said 'sleep') (PrintOther 47 2))
		(if (Said 'take/nap') (PrintOther 47 2))
		(if (Said 'get<in/bed') (PrintOther 47 2))
	)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(1      ; exit room
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 220 162 self
					ignoreControl: ctlWHITE
					setPri: 1
				)
			)
			(2
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(gRoom newRoom: 44)
			)
			(3      ; walk to drawer
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 121 119 self
					ignoreControl: ctlWHITE
				)
			)
			(4 (= cycles 2) (gEgo loop: 3))
			(5
				(PrintOther 47 34)
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(gEgo observeControl: ctlWHITE)
			)
			(6
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo setMotion: MoveTo 145 115 self)
			)
			(7
				(gEgo loop: 2)
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
			)
		)
	)
)

(instance chestScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1         ; move to chest
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 150 132 self
					ignoreControl: ctlWHITE
				)
			)
			(2
				(gEgo hide:)
				(alterEgo
					show:
					loop: 0
					posn: (gEgo x?) (gEgo y?)
					view: 232
					setCycle: End self
					cycleSpeed: 2
				)
			)
			(3
				(if (or (gEgo has: 26) (== (IsOwnedBy 26 47) TRUE))
					(if (gEgo has: 26)
						(gEgo put: 26 47)
						(PrintOther 47 42)
					)
					
					(if chestOpen
						(if gettingBook
							(if (== [gMissingBooks 0] 0)
								(PrintOther 47 32)
								(Print 47 33 #width 160 #icon 984 1 1) ; You take the book 'New Notions on Lotions and Potions'. To view it, look in your menubar.
								(= [gMissingBooks 0] 1)
								(gGame changeScore: 1)
								(if (not (gEgo has: 20)) (gEgo get: 20))
								(self cue:)
							else
								(PrintATI)
							)
						)
					else
						(chest setCycle: End self cycleSpeed: 2)
						(= chestOpen 1)
						(gTheSoundFX number: 204 play:)
					)
				else
					(Print "It's locked.")
					(self cue:)
				)
			)
			(4
				(alterEgo setCycle: Beg self)
			)
			(5
				(= cycles 5)
				(alterEgo hide:)
				(gEgo show: loop: 0 observeControl: ctlWHITE)
			)
			(6
				(if (or (gEgo has: 26) (== (IsOwnedBy 26 47) TRUE))
					(if (== [gMissingBooks 0] 0)
						(PrintOther 47 0)
					else
						(PrintOther 47 1)
					)
				)
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
			)
			(7      ; trying to open the left chest
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 58 160 self
					ignoreControl: ctlWHITE
				)
			)
			(8
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
			(9
				(= cycles 6)
				(Print {It's locked.} #at -1 10)
			)
			(10
				(alterEgo setCycle: Beg self)
			)
			(11
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(alterEgo hide:)
				(gEgo show: observeControl: ctlWHITE)
			)
		)
	)
)

(instance lookUnder of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1      ; look under rug
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 149 134 self
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
				(= cycles 6)
				(if (or (gEgo has: 26) (== (IsOwnedBy 26 47) TRUE))
					(PrintOther 47 17)	
				else
					(Print 47 41 #icon 214)
					(gEgo get: 26)
					(gGame changeScore: 1)	
				)
			)
			(4
				(alterEgo setCycle: Beg self)
			)
			(5
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(alterEgo hide:)
				(gEgo show: observeControl: ctlWHITE)
			)
			(6      ; look under bed   -or-  mattress
				(ProgramControl)
				(SetCursor 997 1)
				(= gCurrentCursor 997)
				(gEgo
					setMotion: MoveTo 110 125 self
					ignoreControl: ctlWHITE
				)
			)
			(7
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
			(8
				(= cycles 6)
				(if lookingForMarble
					(if (not gJup)
						(PrintOther 47 23) ; You feel under the mattress to reveal a pea-like object.
						(PrintOther 47 24) ; What do you know? It is a marble. This one looks like Jupiter.
						(= gJup 1)
						(++ gMarbleNum)
						((gInv at: 9) count: gMarbleNum)
						(gGame changeScore: 1)
						(++ gInt)
					else
						(PrintOther 47 25)
					)
				else                      ; There is nothing under the mattress anymore. It's now ready to give restful sleep.
					(PrintOther 47 18)
				)                     ; Under the bed you find brooms and rags and a half-eaten apple. Unsurprisingly, you find nothing you want to take.
				(= lookingForMarble 0)
			)
			(9
				(alterEgo setCycle: Beg self)
			)
			(10
				(PlayerControl)
				(SetCursor 999 1)
				(= gCurrentCursor 999)
				(alterEgo hide:)
				(gEgo show: observeControl: ctlWHITE)
			)
		)
	)
)

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #width 290 #at -1 10)
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

(instance bookshelf of Prop
	(properties
		y 125
		x 144
		view 102
		loop 1
	)
)

(instance chest of Prop
	(properties
		y 139
		x 178
		view 82
		loop 2
	)
)

(instance alterEgo of Prop
	(properties
		y 164
		x 68
		view 232
		loop 0
	)
)
