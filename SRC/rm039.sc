;;; Sierra Script 1.0 - (do not remove this comment)
; DEMO ROOM
(script# 39)
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
	rm039 0
)





(instance rm039 of Rm
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
				(gEgo posn: 265 80 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: view: 1)
		(doorGate init: loop: 6)
		(gate init: loop: 1 setCycle: Fwd cycleSpeed: 2)
		(rope init: setPri: -1 hide:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState intro)
		(= state intro)
		(switch state
			(0 (= cycles 1))
			(10 (= cycles 1))
			(11
				(= cycles 25)
				(ProgramControl)
				(gEgo setMotion: MoveTo 200 82)
			)
			(12
				(= cycles 5)
				(gEgo loop: 2)
			)
			(13
				(= cycles 10)
				(Print 39 0)
; You tie the rope and throw it over the side of the castle wall.
				(rope show:)
			)
			(14
				(= cycles 10)
				(Print 39 1)
; Congratulations! You've beat the demo.
				(Print 39 2)
; Hopefully you had fun, and please send me feedback.
				(Print 39 3)
; @ doansephim@yahoo.com
				(PlayerControl)
			)
		)
	)
	
	(method (handleEvent pEvent &tmp dyingScript)
		(super handleEvent: pEvent)
		(if (Said 'look>')
			(if (Said '/castle') (Print 39 4))
; Yep, it's a castle.
			(if (Said '/parapet,wall,ground') (Print 39 5))
; I bet if there were something to climb down, I could get to the ground and outa here.
			(if (Said '/star,sky') (Print 39 6))
; Sorry, I really shouldn't be star-gazing right now.
			(if (Said 'around') (Print 39 7))
; I'm on the parapet of the castle. The ground seems too far down to jump to safely. I need a way to climb down,
			(if (Said '/anyword') (Print 39 8))
; Why look at that? It's quite boring.
			(if (Said '[ /* , !* ]') (Print 39 7))
		)
; I'm on the parapet of the castle. The ground seems too far down to jump to safely. I need a way to climb down,
		(if (Said 'use/rope')
			(if (gEgo has: 6) (Print 39 10) else (Print 39 11))
; How exactly do you want to use it?
		)
; You don't have any rope, stupid.

; You don't have any rope, stupid.
		(if (Said 'jump')
			(= dyingScript (ScriptID DYING_SCRIPT))
			(dyingScript
				caller: 273
				register:
					{Despite your heroic attempt to defy gravity's power, you ended up the worse for it.}
			)
			(gGame setScript: dyingScript)
		)
	)
	
	(method (doit)
		(super doit:)
		(if (== (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 50)
		)
	)
)

(instance gate of Prop
	(properties
		y 175
		x 301
		view 104
	)
)

(instance rope of Prop
	(properties
		y 157
		x 200
		view 106
	)
)

(instance doorGate of Prop
	(properties
		y 166
		x 159
		view 37
	)
)
