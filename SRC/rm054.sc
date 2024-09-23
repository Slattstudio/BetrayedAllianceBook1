;;; Sierra Script 1.0 - (do not remove this comment)

(script# 54)
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
	rm054 0
)

; Old Demo Room




(instance rm054 of Rm
	(properties
		picture scriptNumber
		north 0
		east 0
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 200 160 loop: 3)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(if (not (gEgo has: 5))
			(lantern init: loop: 6 setCycle: Fwd cycleSpeed: 2)
		else
			(lantern init: hide:)
		)
		(guard init: loop: 4)
		(prisoner init: loop: 5)
		(zzz init: setPri: 16 loop: 2 setCycle: Fwd cycleSpeed: 3)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (handleEvent pEvent &tmp dyingScript)
		(super handleEvent: pEvent)
		(if (Said 'look>')
			(if (Said '/guard,man')
				(Print 54 0)
; Sleeping on duty.
				(Print 54 1)
; Who hires these people?
				(Print 54 2)
			)
; Wait, that's Mark! I hired him!
			(if (Said '/prisoner') (Print 54 3) (Print 54 4))
; No mattress for you, huh? Guess it does pay to devote your allegiance to the King.
; At least when you get locked up you get something comfy.
			(if (Said '/moon,window,sky,outside') (Print 54 5))
; Yep, there's a moon out there.
			(if (Said 'around') (Print 54 6) (Print 54 7))
; It's a dark, cool cell with a lantern and a sleeping guard.
; I better be quiet.
			(if (Said '/anyword') (Print 54 8))
; Why look at that? It's quite boring.
			(if (Said '[ /* , !* ]') (Print 54 6) (Print 54 7))
; It's a dark, cool cell with a lantern and a sleeping guard.
		)
; I better be quiet.
		(if (Said 'talk/man,prisoner,guard')
			(= dyingScript (ScriptID DYING_SCRIPT))
			(dyingScript
				caller: 272
				register: {Sometimes silent isn't deadly. This was one of those times.}
			)
			(gGame setScript: dyingScript)
		)
		(if (Said 'fart,burp,shout')
			(= dyingScript (ScriptID DYING_SCRIPT))
			(dyingScript
				caller: 272
				register: {Sometimes silent isn't deadly. This was one of those times.}
			)
			(gGame setScript: dyingScript)
		)
		(if (Said 'open/door')
			(cond 
				((== (gEgo onControl:) ctlMAROON) (Print 54 11))
				((== (gEgo onControl:) ctlSILVER) (Print 54 12) (gRoom newRoom: 51))
; There is no reason to open this gate.
; OK
				(else (PrintNCE))
			)
		)
		(if (Said 'take/pick,lantern,lamp,light')
			(if (not (gEgo has: 5))
				(if (<= (gEgo distanceTo: lantern) 30)
					(Print 54 13)
; You silently take the lantern.
					(gEgo get: 5)
					(lantern loop: 4)
				else
					; ++gDemoScore//CHANGESCORE
					(Print 54 14)
				)
			else
; You must get a little closer.
				(Print 54 15)
			)
		)
	)
)

; That's funny. Why would you take what you already have?
(instance guard of Prop
	(properties
		y 155
		x 215
		view 310
	)
)

(instance lantern of Prop
	(properties
		y 160
		x 75
		view 101
	)
)

(instance zzz of Prop
	(properties
		y 135
		x 210
		view 100
	)
)

(instance prisoner of Prop
	(properties
		y 80
		x 140
		view 310
	)
)
