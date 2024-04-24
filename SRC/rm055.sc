;;; Sierra Script 1.0 - (do not remove this comment)

(script# 55)
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
	rm055 0
)

; Old Demo Room




(instance rm055 of Rm
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
				(gEgo posn: 200 130 loop: 1)
			)
			(51
				(gEgo posn: 260 150 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init:)
		(shelf init: loop: 1 ignoreActors:)
		(treasureBox init: loop: 1 ignoreActors:)
		(rope init: loop: 2 ignoreActors: setPri: 1)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState intro)
		(= state intro)
		(switch state
			(0 (= cycles 1))
			(1
				(= cycles 7)
				(gEgo setMotion: MoveTo 250 140)
			)
			(2 (= cycles 10))
		)
	)
	
	(method (handleEvent pEvent &tmp dyingScript)
		(super handleEvent: pEvent)
		(if (Said 'look>')
			(if (Said '/box,crate') (Print 55 2))
; Besides some cool poker chips there is nothing interesting.
			(if (Said '/vial,flask,pot') (Print 55 3))
; You aren't about to drink whatever's in these things. From the looks of it, they've been sitting around for a few years anyway.
			(if (Said '/moon,window,sky,outside') (Print 55 4))
; Sure is a beautiful night to be in prison.
			(if (Said '/book,bookshelf,shelf,bookcase,case')
				(Print 55 5)
			)
; Sorry, I'm in no mood to sit and read through these books to see if they would be interesting.
			(if (Said 'around')
				(cond 
					((not (gEgo has: 2)) (Print 55 6))
					((not (gEgo has: 6)) (Print 55 7))
; This is the storage room. There are boxes, a bookshelf, a chest, some vials, rope, and all kinds of other things. My gear should be in here somewhere.
					(else (Print 55 8))
; I got my gear, but maybe there is something else helpful here.
				)
			)
; There is nothing else here that is of interest to me.
			(if (Said '/anyword') (Print 55 9))
; Why look at that? It's quite boring.
			(if (Said '[ /* , !* ]')
				(cond 
					((not (gEgo has: 2)) (Print 55 6))
					((not (gEgo has: 6)) (Print 55 7))
; This is the storage room. There are boxes, a bookshelf, a chest, some vials, rope, and all kinds of other things. My gear should be in here somewhere.
					(else (Print 55 8))
; I got my gear, but maybe there is something else helpful here.
				)
			)
		)
; There is nothing else here that is of interest to me.
		(if (Said 'open/chest,box')
			(if (<= (gEgo distanceTo: treasureBox) 25)
				(if (not (gEgo has: 2))
					(treasureBox cel: 1)
					(Print 55 13)
; What luck, it isn't locked!
					(Print 55 14)
; You open it up and find your sword inside.
					(Print 55 15)
; You take it.
					(gEgo get: 2)
				else
					; ++gDemoScore//CHANGESCORE
					(Print 55 16)
				)
			else
; It's already open and there's nothing left inside.
				(Print 55 17)
			)
		)
; You're not close enough.
		(if (Said 'take,pick/rope')
			(if (not (gEgo has: 6))
				(if (<= (gEgo distanceTo: rope) 20)
					(gEgo get: 6)
					(Print 55 18)
; No prob
					(rope hide:)
				else
					; ++gDemoScore//CHANGESCORE
					(Print 55 17)
				)
			else
; You're not close enough.
				(Print 55 20)
			)
		)
; You already have that.
		(if (Said 'take/chips') (Print 55 21))
	)
	
; Cool though they may be, I don't really have much use for them.
	(method (doit)
		(super doit:)
		(if (== (gEgo onControl:) ctlSILVER)
			(gRoom newRoom: 51)
		)
	)
)

(instance shelf of Prop
	(properties
		y 80
		x 230
		view 102
	)
)

(instance treasureBox of Prop
	(properties
		y 85
		x 255
		view 233
	)
)

(instance rope of Prop
	(properties
		y 100
		x 210
		view 102
	)
)
