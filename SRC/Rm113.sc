;;; Sierra Script 1.0 - (do not remove this comment)
; ******************************************************************************
(script# 113)
(include sci.sh)
(include game.sh)
(use main)
(use controls)
(use cycle)
(use game)
(use feature)
(use obj)
(use Inv)

(public
	rm113 0
)

(local
	; Doan Mini Game
	; ******************************************************************************
	; *****************************************************************************
	; *****************************************************************************
	thisPiece =  0 ; just to make the copy and pasting of code easier
	goToRoom =  0 ; use this variable to trigger room change.
	; I used a seperate instance for the won game changeState.
	; I have seen enough times where the game does not like it when you try to change rooms via
	; a secondary instance rather than the roomscript, so I don't even bother... the variable is
	; changed in the winGame script, but the room change occurs in the roomScript doit method.
	nextRoom =  2 ; Set variable to whatever room happens upon completion... you could hard code it later, I just thought this would be easier
	winGame =  FALSE ; triggers the ending animations
	startUp =  FALSE ; used by doit method, simply to hide the cursor and display opening print statements
	pcFollowing =  0 ; piece currently selected... Corresponds to cel number
	place1 =  0 ; piece located in position 1...
	place2 =  0
	place3 =  0
	place4 =  0
	place5 =  0
	place6 =  0
	place7 =  0
	place8 =  0
	place9 =  0
)
;
; place1energy = FALSE // attempting to handle the lightning in real time
; place2energy = FALSE
; place3energy = FALSE
; place4energy = FALSE
; place5energy = FALSE
; place6energy = FALSE
; place7energy = FALSE
; place8energy = FALSE
; place9energy = FALSE
; *****************************************************************************
(instance rm113 of Rm
	(properties
		picture 110
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(pc1 init:) ; the views instances that are used as cursors
		(pc2 init:)
		(pc3 init:)
		(pc4 init:)
		(pc5 init:)
		(pc6 init:)
		(pc7 init:)
		(pc8 init:)
		(pc9 init:)
		(side1 init: setPri: 10) ; the views instances that are used to check for click ons
		(side2 init: setPri: 10) ; I had to use a second set here. With the views acting like
		(side3 init: setPri: 10) ; a cursor, I was having a really hard time seperating when
		(side4 init: setPri: 10) ; there was a click on the static piece instead of the instance
		(side5 init: setPri: 10) ; that was being dragged around like it was the cursor
		(side6 init: setPri: 10)
		(side7 init: setPri: 10)
		(side8 init: setPri: 10)
		(side9 init: setPri: 10)
		(pc1lightning init: setPri: 4 setCycle: Fwd)
		(pc2lightning init: setPri: 4 setCycle: Fwd)
		(pc3lightning init: setPri: 4 setCycle: Fwd)
		(pc4lightning init: setPri: 4 setCycle: Fwd)
		(pc5lightning init: setPri: 4 setCycle: Fwd)
		(pc6lightning init: setPri: 4 setCycle: Fwd)
		(pc7lightning init: setPri: 4 setCycle: Fwd)
		(pc8lightning init: setPri: 4 setCycle: Fwd)
		(pc9lightning init: setPri: 4 setCycle: Fwd)
		(fkCursor init:) ; the instance that is used as the hand cursor when no piece is selected
		(gGame setCursor: 1 SET_CURSOR_VISIBLE)
	)
)
                                                 ; Declaring here has no effect, duplicated in Doit Method
; ******************************************************
(instance fkCursor of Prop
	(properties
		x 400
		y 400
		view 581
		loop 0
		cel 0
	)
)

(instance pc1 of Prop
	(properties
		x 400
		y 45
		view 581
		loop 0
		cel 1
	)
)

(instance pc2 of Prop
	(properties
		x 400
		y 45
		view 581
		loop 0
		cel 2
	)
)

(instance pc3 of Prop
	(properties
		x 400
		y 79
		view 581
		loop 0
		cel 3
	)
)

(instance pc4 of Prop
	(properties
		x 400
		y 79
		view 581
		loop 0
		cel 4
	)
)

(instance pc5 of Prop
	(properties
		x 400
		y 110
		view 581
		loop 0
		cel 5
	)
)

(instance pc6 of Prop
	(properties
		x 400
		y 110
		view 581
		loop 0
		cel 6
	)
)

(instance pc7 of Prop
	(properties
		x 400
		y 138
		view 581
		loop 0
		cel 7
	)
)

(instance pc8 of Prop
	(properties
		x 400
		y 138
		view 581
		loop 0
		cel 8
	)
)

(instance pc9 of Prop
	(properties
		x 400
		y 169
		view 581
		loop 0
		cel 9
	)
)

(instance side1 of Prop
	(properties
		x 261
		y 45
		view 581
		loop 0
		cel 1
	)
)

(instance side2 of Prop
	(properties
		x 296
		y 45
		view 581
		loop 0
		cel 2
	)
)

(instance side3 of Prop
	(properties
		x 261
		y 79
		view 581
		loop 0
		cel 3
	)
)

(instance side4 of Prop
	(properties
		x 296
		y 79
		view 581
		loop 0
		cel 4
	)
)

(instance side5 of Prop
	(properties
		x 261
		y 110
		view 581
		loop 0
		cel 5
	)
)

(instance side6 of Prop
	(properties
		x 296
		y 110
		view 581
		loop 0
		cel 6
	)
)

(instance side7 of Prop
	(properties
		x 261
		y 138
		view 581
		loop 0
		cel 7
	)
)

(instance side8 of Prop
	(properties
		x 296
		y 138
		view 581
		loop 0
		cel 8
	)
)

(instance side9 of Prop
	(properties
		x 261
		y 169
		view 581
		loop 0
		cel 9
	)
)

(instance pc1lightning of Prop
	(properties
		x 400
		y 400
		view 581
		loop 2
		cel 0
	)
)

(instance pc2lightning of Prop
	(properties
		x 400
		y 400
		view 581
		loop 3
		cel 0
	)
)

(instance pc3lightning of Prop
	(properties
		x 400
		y 400
		view 581
		loop 4
		cel 0
	)
)

(instance pc4lightning of Prop
	(properties
		x 400
		y 400
		view 581
		loop 4
		cel 0
	)
)

(instance pc5lightning of Prop
	(properties
		x 400
		y 400
		view 581
		loop 5
		cel 0
	)
)

(instance pc6lightning of Prop
	(properties
		x 400
		y 400
		view 581
		loop 6
		cel 0
	)
)

(instance pc7lightning of Prop
	(properties
		x 400
		y 400
		view 581
		loop 5
		cel 0
	)
)

(instance pc8lightning of Prop
	(properties
		x 400
		y 400
		view 581
		loop 6
		cel 0
	)
)

(instance pc9lightning of Prop
	(properties
		x 400
		y 400
		view 581
		loop 2
		cel 0

	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit &tmp myEvent)
		(super doit:)
		; **********************************************
		; Only happens at room startup, hide cursor and print opening messages
		; **********************************************
		(if (== startUp FALSE)
			(gGame setCursor: 1 SET_CURSOR_VISIBLE) ; actually a cursor resource that is all transparency...
			(Print {Solve this puzzle})
			(= startUp TRUE)
		)
		; **********************************************
		; capture cursor position with myEvent variable
		; **********************************************
		(= myEvent (Event new: evNULL))
		; **********************************************
		; position the puzzle piece that is currently in use
		; **********************************************
		(switch pcFollowing
			(0
				(fkCursor setPri: 15 posn: (myEvent x?) (myEvent y?))
			)
			(1
				(pc1 setPri: 15 posn: (myEvent x?) (myEvent y?))
			)
			(2
				(pc2 setPri: 15 posn: (myEvent x?) (myEvent y?))
			)
			(3
				(pc3 setPri: 15 posn: (myEvent x?) (myEvent y?))
			)
			(4
				(pc4 setPri: 15 posn: (myEvent x?) (myEvent y?))
			)
			(5
				(pc5 setPri: 15 posn: (myEvent x?) (myEvent y?))
			)
			(6
				(pc6 setPri: 15 posn: (myEvent x?) (myEvent y?))
			)
			(7
				(pc7 setPri: 15 posn: (myEvent x?) (myEvent y?))
			)
			(8
				(pc8 setPri: 15 posn: (myEvent x?) (myEvent y?))
			)
			(9
				(pc9 setPri: 15 posn: (myEvent x?) (myEvent y?))
			)
		) ; end switch
		; **********************************************
		; Check to see if the Game has been won
		; **********************************************
		(if
			(and
				(== winGame FALSE)
				(== place1 5)
				(== place2 2)
				(== place3 6)
				(== place4 4)
				(== place5 1)
				(== place6 3)
				(== place7 7)
				(== place8 9)
				(== place9 8)
			)
			(= winGame TRUE) ; insures this is only fired off once
			(fkCursor setScript: doWin)
		)                        ; do win game, changestate at end of script
		; **********************************************
		; delete myEvent for this cycle
		; **********************************************
		(myEvent dispose:)
		; **********************************************
		; trigger the room change
		; **********************************************
		(if (!= goToRoom 0)
			(UnLoad rsPIC 110)
			(UnLoad rsSCRIPT scriptNumber)
			; UnLoad(rsTEXT scriptNumber)
			(UnLoad rsVIEW 581)
			(gRoom newRoom: goToRoom)
		)
	)
	 ; end doit method
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (not (& (pEvent modifiers?) emRIGHT_BUTTON)) ; left clicks
				(if (== gProgramControl FALSE)
					; ****************************************************************************
					; PLAYABLE AREA
					; ****************************************************************************
					; Now for the hard part. First let's run through the 9 positions of the
					; game board. Each position is colored with it's own control color so check
					; for a click on that.
					; Then we need to figure out if the user is carrying around a puzzle piece
					; that they are trying to place there. Use a switch(pcFollowing).
					; Next we need to determine if the space is empty or have they already placed
					; a piece in that spot previously. If empty, place piece and bring back fake cursor.
					; If previously filled, place piece and replace puzzle piece in tow.
					; ****************************************************************************
					(if
						(==
							$0002
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                         ; Navy
						(switch pcFollowing
							(0
								(= thisPiece 0)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place1 0)
									(= pcFollowing 0)
									(= place1 0)
								else
									(= pcFollowing place1)
									(= place1 0)
									(fkCursor posn: 400 400 setPri: 14)
								)
							)
							(1
								(= thisPiece 1)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place1 0)
									(= pcFollowing 0)
									(= place1 thisPiece)
								else
									(= pcFollowing place1)
									(= place1 thisPiece)
								)
								(pc1 posn: 111 56 setPri: 10)
							)
							(2
								(= thisPiece 2)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place1 0)
									(= pcFollowing 0)
									(= place1 thisPiece)
								else
									(= pcFollowing place1)
									(= place1 thisPiece)
								)
								(pc2 posn: 111 56 setPri: 10)
							)
							(3
								(= thisPiece 3)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place1 0)
									(= pcFollowing 0)
									(= place1 thisPiece)
								else
									(= pcFollowing place1)
									(= place1 thisPiece)
								)
								(pc3 posn: 111 56 setPri: 10)
							)
							(4
								(= thisPiece 4)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place1 0)
									(= pcFollowing 0)
									(= place1 thisPiece)
								else
									(= pcFollowing place1)
									(= place1 thisPiece)
								)
								(pc4 posn: 111 56 setPri: 10)
							)
							(5
								(= thisPiece 5)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place1 0)
									(= pcFollowing 0)
									(= place1 thisPiece)
								else
									(= pcFollowing place1)
									(= place1 thisPiece)
								)
								(pc5 posn: 111 56 setPri: 10)
							)
							(6
								(= thisPiece 6)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place1 0)
									(= pcFollowing 0)
									(= place1 thisPiece)
								else
									(= pcFollowing place1)
									(= place1 thisPiece)
								)
								(pc6 posn: 111 56 setPri: 10)
							)
							(7
								(= thisPiece 7)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place1 0)
									(= pcFollowing 0)
									(= place1 thisPiece)
								else
									(= pcFollowing place1)
									(= place1 thisPiece)
								)
								(pc7 posn: 111 56 setPri: 10)
							)
							(8
								(= thisPiece 8)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place1 0)
									(= pcFollowing 0)
									(= place1 thisPiece)
								else
									(= pcFollowing place1)
									(= place1 thisPiece)
								)
								(pc8 posn: 111 56 setPri: 10)
							)
							(9
								(= thisPiece 9)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place1 0)
									(= pcFollowing 0)
									(= place1 thisPiece)
								else
									(= pcFollowing place1)
									(= place1 thisPiece)
								)
								(pc9 posn: 111 56 setPri: 10)
							)
						)
					) ; end current cursor switch ; end if Control
					(if
						(==
							$0004
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                         ; Green
						(switch pcFollowing
							(0
								(= thisPiece 0)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place2 0)
									(= pcFollowing 0)
									(= place2 0)
								else
									(= pcFollowing place2)
									(= place2 0)
									(fkCursor posn: 400 400 setPri: 14)
								)
							)
							(1
								(= thisPiece 1)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place2 0)
									(= pcFollowing 0)
									(= place2 thisPiece)
								else
									(= pcFollowing place2)
									(= place2 thisPiece)
								)
								(pc1 posn: 159 56 setPri: 10)
							)
							(2
								(= thisPiece 2)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place2 0)
									(= pcFollowing 0)
									(= place2 thisPiece)
								else
									(= pcFollowing place2)
									(= place2 thisPiece)
								)
								(pc2 posn: 159 56 setPri: 10)
							)
							(3
								(= thisPiece 3)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place2 0)
									(= pcFollowing 0)
									(= place2 thisPiece)
								else
									(= pcFollowing place2)
									(= place2 thisPiece)
								)
								(pc3 posn: 159 56 setPri: 10)
							)
							(4
								(= thisPiece 4)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place2 0)
									(= pcFollowing 0)
									(= place2 thisPiece)
								else
									(= pcFollowing place2)
									(= place2 thisPiece)
								)
								(pc4 posn: 159 56 setPri: 10)
							)
							(5
								(= thisPiece 5)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place2 0)
									(= pcFollowing 0)
									(= place2 thisPiece)
								else
									(= pcFollowing place2)
									(= place2 thisPiece)
								)
								(pc5 posn: 159 56 setPri: 10)
							)
							(6
								(= thisPiece 6)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place2 0)
									(= pcFollowing 0)
									(= place2 thisPiece)
								else
									(= pcFollowing place2)
									(= place2 thisPiece)
								)
								(pc6 posn: 159 56 setPri: 10)
							)
							(7
								(= thisPiece 7)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place2 0)
									(= pcFollowing 0)
									(= place2 thisPiece)
								else
									(= pcFollowing place2)
									(= place2 thisPiece)
								)
								(pc7 posn: 159 56 setPri: 10)
							)
							(8
								(= thisPiece 8)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place2 0)
									(= pcFollowing 0)
									(= place2 thisPiece)
								else
									(= pcFollowing place2)
									(= place2 thisPiece)
								)
								(pc8 posn: 159 56 setPri: 10)
							)
							(9
								(= thisPiece 9)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place2 0)
									(= pcFollowing 0)
									(= place2 thisPiece)
								else
									(= pcFollowing place2)
									(= place2 thisPiece)
								)
								(pc9 posn: 159 56 setPri: 10)
							)
						)
					) ; end current cursor switch ; end if Control
					(if
						(==
							$0008
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                         ; Teal
						(switch pcFollowing
							(0
								(= thisPiece 0)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place3 0)
									(= pcFollowing 0)
									(= place3 0)
								else
									(= pcFollowing place3)
									(= place3 0)
									(fkCursor posn: 400 400 setPri: 14)
								)
							)
							(1
								(= thisPiece 1)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place3 0)
									(= pcFollowing 0)
									(= place3 thisPiece)
								else
									(= pcFollowing place3)
									(= place3 thisPiece)
								)
								(pc1 posn: 207 56 setPri: 10)
							)
							(2
								(= thisPiece 2)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place3 0)
									(= pcFollowing 0)
									(= place3 thisPiece)
								else
									(= pcFollowing place3)
									(= place3 thisPiece)
								)
								(pc2 posn: 207 56 setPri: 10)
							)
							(3
								(= thisPiece 3)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place3 0)
									(= pcFollowing 0)
									(= place3 thisPiece)
								else
									(= pcFollowing place3)
									(= place3 thisPiece)
								)
								(pc3 posn: 207 56 setPri: 10)
							)
							(4
								(= thisPiece 4)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place3 0)
									(= pcFollowing 0)
									(= place3 thisPiece)
								else
									(= pcFollowing place3)
									(= place3 thisPiece)
								)
								(pc4 posn: 207 56 setPri: 10)
							)
							(5
								(= thisPiece 5)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place3 0)
									(= pcFollowing 0)
									(= place3 thisPiece)
								else
									(= pcFollowing place3)
									(= place3 thisPiece)
								)
								(pc5 posn: 207 56 setPri: 10)
							)
							(6
								(= thisPiece 6)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place3 0)
									(= pcFollowing 0)
									(= place3 thisPiece)
								else
									(= pcFollowing place3)
									(= place3 thisPiece)
								)
								(pc6 posn: 207 56 setPri: 10)
							)
							(7
								(= thisPiece 7)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place3 0)
									(= pcFollowing 0)
									(= place3 thisPiece)
								else
									(= pcFollowing place3)
									(= place3 thisPiece)
								)
								(pc7 posn: 207 56 setPri: 10)
							)
							(8
								(= thisPiece 8)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place3 0)
									(= pcFollowing 0)
									(= place3 thisPiece)
								else
									(= pcFollowing place3)
									(= place3 thisPiece)
								)
								(pc8 posn: 207 56 setPri: 10)
							)
							(9
								(= thisPiece 9)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place3 0)
									(= pcFollowing 0)
									(= place3 thisPiece)
								else
									(= pcFollowing place3)
									(= place3 thisPiece)
								)
								(pc9 posn: 207 56 setPri: 10)
							)
						)
					) ; end current cursor switch ; end if Control
					(if
						(==
							$0010
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                         ; Maroon
						(switch pcFollowing
							(0
								(= thisPiece 0)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place4 0)
									(= pcFollowing 0)
									(= place4 0)
								else
									(= pcFollowing place4)
									(= place4 0)
									(fkCursor posn: 400 400 setPri: 14)
								)
							)
							(1
								(= thisPiece 1)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place4 0)
									(= pcFollowing 0)
									(= place4 thisPiece)
								else
									(= pcFollowing place4)
									(= place4 thisPiece)
								)
								(pc1 posn: 111 107 setPri: 10)
							)
							(2
								(= thisPiece 2)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place4 0)
									(= pcFollowing 0)
									(= place4 thisPiece)
								else
									(= pcFollowing place4)
									(= place4 thisPiece)
								)
								(pc2 posn: 111 107 setPri: 10)
							)
							(3
								(= thisPiece 3)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place4 0)
									(= pcFollowing 0)
									(= place4 thisPiece)
								else
									(= pcFollowing place4)
									(= place4 thisPiece)
								)
								(pc3 posn: 111 107 setPri: 10)
							)
							(4
								(= thisPiece 4)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place4 0)
									(= pcFollowing 0)
									(= place4 thisPiece)
								else
									(= pcFollowing place4)
									(= place4 thisPiece)
								)
								(pc4 posn: 111 107 setPri: 10)
							)
							(5
								(= thisPiece 5)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place4 0)
									(= pcFollowing 0)
									(= place4 thisPiece)
								else
									(= pcFollowing place4)
									(= place4 thisPiece)
								)
								(pc5 posn: 111 107 setPri: 10)
							)
							(6
								(= thisPiece 6)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place4 0)
									(= pcFollowing 0)
									(= place4 thisPiece)
								else
									(= pcFollowing place4)
									(= place4 thisPiece)
								)
								(pc6 posn: 111 107 setPri: 10)
							)
							(7
								(= thisPiece 7)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place4 0)
									(= pcFollowing 0)
									(= place4 thisPiece)
								else
									(= pcFollowing place4)
									(= place4 thisPiece)
								)
								(pc7 posn: 111 107 setPri: 10)
							)
							(8
								(= thisPiece 8)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place4 0)
									(= pcFollowing 0)
									(= place4 thisPiece)
								else
									(= pcFollowing place4)
									(= place4 thisPiece)
								)
								(pc8 posn: 111 56 setPri: 10)
							)
							(9
								(= thisPiece 9)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place4 0)
									(= pcFollowing 0)
									(= place4 thisPiece)
								else
									(= pcFollowing place4)
									(= place4 thisPiece)
								)
								(pc9 posn: 111 107 setPri: 10)
							)
						)
					) ; end current cursor switch ; end if Control
					(if
						(==
							$0020
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                         ; Purple
						(switch pcFollowing
							(0
								(= thisPiece 0)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place5 0)
									(= pcFollowing 0)
									(= place5 0)
								else
									(= pcFollowing place5)
									(= place5 0)
									(fkCursor posn: 400 400 setPri: 14)
								)
							)
							(1
								(= thisPiece 1)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place5 0)
									(= pcFollowing 0)
									(= place5 thisPiece)
								else
									(= pcFollowing place5)
									(= place5 thisPiece)
								)
								(pc1 posn: 159 107 setPri: 10)
							)
							(2
								(= thisPiece 2)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place5 0)
									(= pcFollowing 0)
									(= place5 thisPiece)
								else
									(= pcFollowing place5)
									(= place5 thisPiece)
								)
								(pc2 posn: 159 107 setPri: 10)
							)
							(3
								(= thisPiece 3)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place5 0)
									(= pcFollowing 0)
									(= place5 thisPiece)
								else
									(= pcFollowing place5)
									(= place5 thisPiece)
								)
								(pc3 posn: 159 107 setPri: 10)
							)
							(4
								(= thisPiece 4)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place5 0)
									(= pcFollowing 0)
									(= place5 thisPiece)
								else
									(= pcFollowing place5)
									(= place5 thisPiece)
								)
								(pc4 posn: 159 107 setPri: 10)
							)
							(5
								(= thisPiece 5)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place5 0)
									(= pcFollowing 0)
									(= place5 thisPiece)
								else
									(= pcFollowing place5)
									(= place5 thisPiece)
								)
								(pc5 posn: 159 107 setPri: 10)
							)
							(6
								(= thisPiece 6)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place5 0)
									(= pcFollowing 0)
									(= place5 thisPiece)
								else
									(= pcFollowing place5)
									(= place5 thisPiece)
								)
								(pc6 posn: 159 107 setPri: 10)
							)
							(7
								(= thisPiece 7)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place5 0)
									(= pcFollowing 0)
									(= place5 thisPiece)
								else
									(= pcFollowing place5)
									(= place5 thisPiece)
								)
								(pc7 posn: 159 107 setPri: 10)
							)
							(8
								(= thisPiece 8)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place5 0)
									(= pcFollowing 0)
									(= place5 thisPiece)
								else
									(= pcFollowing place5)
									(= place5 thisPiece)
								)
								(pc8 posn: 159 56 setPri: 10)
							)
							(9
								(= thisPiece 9)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place5 0)
									(= pcFollowing 0)
									(= place5 thisPiece)
								else
									(= pcFollowing place5)
									(= place5 thisPiece)
								)
								(pc9 posn: 159 107 setPri: 10)
							)
						)
					) ; end current cursor switch ; end if Control
					(if
						(==
							$0040
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                         ; Brown
						(switch pcFollowing
							(0
								(= thisPiece 0)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place6 0)
									(= pcFollowing 0)
									(= place6 0)
								else
									(= pcFollowing place6)
									(= place6 0)
									(fkCursor posn: 400 400 setPri: 14)
								)
							)
							(1
								(= thisPiece 1)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place6 0)
									(= pcFollowing 0)
									(= place6 thisPiece)
								else
									(= pcFollowing place6)
									(= place6 thisPiece)
								)
								(pc1 posn: 207 107 setPri: 10)
							)
							(2
								(= thisPiece 2)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place6 0)
									(= pcFollowing 0)
									(= place6 thisPiece)
								else
									(= pcFollowing place6)
									(= place6 thisPiece)
								)
								(pc2 posn: 207 107 setPri: 10)
							)
							(3
								(= thisPiece 3)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place6 0)
									(= pcFollowing 0)
									(= place6 thisPiece)
								else
									(= pcFollowing place6)
									(= place6 thisPiece)
								)
								(pc3 posn: 207 107 setPri: 10)
							)
							(4
								(= thisPiece 4)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place6 0)
									(= pcFollowing 0)
									(= place6 thisPiece)
								else
									(= pcFollowing place6)
									(= place6 thisPiece)
								)
								(pc4 posn: 207 107 setPri: 10)
							)
							(5
								(= thisPiece 5)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place6 0)
									(= pcFollowing 0)
									(= place6 thisPiece)
								else
									(= pcFollowing place6)
									(= place6 thisPiece)
								)
								(pc5 posn: 207 107 setPri: 10)
							)
							(6
								(= thisPiece 6)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place6 0)
									(= pcFollowing 0)
									(= place6 thisPiece)
								else
									(= pcFollowing place6)
									(= place6 thisPiece)
								)
								(pc6 posn: 207 107 setPri: 10)
							)
							(7
								(= thisPiece 7)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place6 0)
									(= pcFollowing 0)
									(= place6 thisPiece)
								else
									(= pcFollowing place6)
									(= place6 thisPiece)
								)
								(pc7 posn: 207 107 setPri: 10)
							)
							(8
								(= thisPiece 8)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place6 0)
									(= pcFollowing 0)
									(= place6 thisPiece)
								else
									(= pcFollowing place6)
									(= place6 thisPiece)
								)
								(pc8 posn: 207 56 setPri: 10)
							)
							(9
								(= thisPiece 9)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place6 0)
									(= pcFollowing 0)
									(= place6 thisPiece)
								else
									(= pcFollowing place6)
									(= place6 thisPiece)
								)
								(pc9 posn: 207 107 setPri: 10)
							)
						)
					) ; end current cursor switch ; end if Control
					(if
						(==
							$0080
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                         ; Silver
						(switch pcFollowing
							(0
								(= thisPiece 0)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place7 0)
									(= pcFollowing 0)
									(= place7 0)
								else
									(= pcFollowing place7)
									(= place7 0)
									(fkCursor posn: 400 400 setPri: 14)
								)
							)
							(1
								(= thisPiece 1)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place7 0)
									(= pcFollowing 0)
									(= place7 thisPiece)
								else
									(= pcFollowing place7)
									(= place7 thisPiece)
								)
								(pc1 posn: 111 157 setPri: 10)
							)
							(2
								(= thisPiece 2)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place7 0)
									(= pcFollowing 0)
									(= place7 thisPiece)
								else
									(= pcFollowing place7)
									(= place7 thisPiece)
								)
								(pc2 posn: 111 157 setPri: 10)
							)
							(3
								(= thisPiece 3)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place7 0)
									(= pcFollowing 0)
									(= place7 thisPiece)
								else
									(= pcFollowing place7)
									(= place7 thisPiece)
								)
								(pc3 posn: 111 157 setPri: 10)
							)
							(4
								(= thisPiece 4)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place7 0)
									(= pcFollowing 0)
									(= place7 thisPiece)
								else
									(= pcFollowing place7)
									(= place7 thisPiece)
								)
								(pc4 posn: 111 157 setPri: 10)
							)
							(5
								(= thisPiece 5)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place7 0)
									(= pcFollowing 0)
									(= place7 thisPiece)
								else
									(= pcFollowing place7)
									(= place7 thisPiece)
								)
								(pc5 posn: 111 157 setPri: 10)
							)
							(6
								(= thisPiece 6)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place7 0)
									(= pcFollowing 0)
									(= place7 thisPiece)
								else
									(= pcFollowing place7)
									(= place7 thisPiece)
								)
								(pc6 posn: 111 157 setPri: 10)
							)
							(7
								(= thisPiece 7)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place7 0)
									(= pcFollowing 0)
									(= place7 thisPiece)
								else
									(= pcFollowing place7)
									(= place7 thisPiece)
								)
								(pc7 posn: 111 157 setPri: 10)
							)
							(8
								(= thisPiece 8)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place7 0)
									(= pcFollowing 0)
									(= place7 thisPiece)
								else
									(= pcFollowing place7)
									(= place7 thisPiece)
								)
								(pc8 posn: 111 157 setPri: 10)
							)
							(9
								(= thisPiece 9)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place7 0)
									(= pcFollowing 0)
									(= place7 thisPiece)
								else
									(= pcFollowing place7)
									(= place7 thisPiece)
								)
								(pc9 posn: 111 157 setPri: 10)
							)
						)
					) ; end current cursor switch ; end if Control
					(if
						(==
							$0100
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                         ; Gray
						(switch pcFollowing
							(0
								(= thisPiece 0)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place8 0)
									(= pcFollowing 0)
									(= place8 0)
								else
									(= pcFollowing place8)
									(= place8 0)
									(fkCursor posn: 400 400 setPri: 14)
								)
							)
							(1
								(= thisPiece 1)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place8 0)
									(= pcFollowing 0)
									(= place8 thisPiece)
								else
									(= pcFollowing place8)
									(= place8 thisPiece)
								)
								(pc1 posn: 159 157 setPri: 10)
							)
							(2
								(= thisPiece 2)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place8 0)
									(= pcFollowing 0)
									(= place8 thisPiece)
								else
									(= pcFollowing place8)
									(= place8 thisPiece)
								)
								(pc2 posn: 159 157 setPri: 10)
							)
							(3
								(= thisPiece 3)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place8 0)
									(= pcFollowing 0)
									(= place8 thisPiece)
								else
									(= pcFollowing place8)
									(= place8 thisPiece)
								)
								(pc3 posn: 159 157 setPri: 10)
							)
							(4
								(= thisPiece 4)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place8 0)
									(= pcFollowing 0)
									(= place8 thisPiece)
								else
									(= pcFollowing place8)
									(= place8 thisPiece)
								)
								(pc4 posn: 159 157 setPri: 10)
							)
							(5
								(= thisPiece 5)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place8 0)
									(= pcFollowing 0)
									(= place8 thisPiece)
								else
									(= pcFollowing place8)
									(= place8 thisPiece)
								)
								(pc5 posn: 159 157 setPri: 10)
							)
							(6
								(= thisPiece 6)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place8 0)
									(= pcFollowing 0)
									(= place8 thisPiece)
								else
									(= pcFollowing place8)
									(= place8 thisPiece)
								)
								(pc6 posn: 159 157 setPri: 10)
							)
							(7
								(= thisPiece 7)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place8 0)
									(= pcFollowing 0)
									(= place8 thisPiece)
								else
									(= pcFollowing place8)
									(= place8 thisPiece)
								)
								(pc7 posn: 159 157 setPri: 10)
							)
							(8
								(= thisPiece 8)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place8 0)
									(= pcFollowing 0)
									(= place8 thisPiece)
								else
									(= pcFollowing place8)
									(= place8 thisPiece)
								)
								(pc8 posn: 159 157 setPri: 10)
							)
							(9
								(= thisPiece 9)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place8 0)
									(= pcFollowing 0)
									(= place8 thisPiece)
								else
									(= pcFollowing place8)
									(= place8 thisPiece)
								)
								(pc9 posn: 159 157 setPri: 10)
							)
						)
					) ; end current cursor switch ; end if Control
					(if
						(==
							$0200
							(OnControl ocSPECIAL (pEvent x?) (pEvent y?))
						)                                         ; Blue
						(switch pcFollowing
							(0
								(= thisPiece 0)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place9 0)
									(= pcFollowing 0)
									(= place9 0)
								else
									(= pcFollowing place9)
									(= place9 0)
									(fkCursor posn: 400 400 setPri: 14)
								)
							)
							(1
								(= thisPiece 1)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place9 0)
									(= pcFollowing 0)
									(= place9 thisPiece)
								else
									(= pcFollowing place9)
									(= place9 thisPiece)
								)
								(pc1 posn: 207 157 setPri: 10)
							)
							(2
								(= thisPiece 2)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place9 0)
									(= pcFollowing 0)
									(= place9 thisPiece)
								else
									(= pcFollowing place9)
									(= place9 thisPiece)
								)
								(pc2 posn: 207 157 setPri: 10)
							)
							(3
								(= thisPiece 3)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place9 0)
									(= pcFollowing 0)
									(= place9 thisPiece)
								else
									(= pcFollowing place9)
									(= place9 thisPiece)
								)
								(pc3 posn: 207 157 setPri: 10)
							)
							(4
								(= thisPiece 4)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place9 0)
									(= pcFollowing 0)
									(= place9 thisPiece)
								else
									(= pcFollowing place9)
									(= place9 thisPiece)
								)
								(pc4 posn: 207 157 setPri: 10)
							)
							(5
								(= thisPiece 5)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place9 0)
									(= pcFollowing 0)
									(= place9 thisPiece)
								else
									(= pcFollowing place9)
									(= place9 thisPiece)
								)
								(pc5 posn: 207 157 setPri: 10)
							)
							(6
								(= thisPiece 6)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place9 0)
									(= pcFollowing 0)
									(= place9 thisPiece)
								else
									(= pcFollowing place9)
									(= place9 thisPiece)
								)
								(pc6 posn: 207 157 setPri: 10)
							)
							(7
								(= thisPiece 7)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place9 0)
									(= pcFollowing 0)
									(= place9 thisPiece)
								else
									(= pcFollowing place9)
									(= place9 thisPiece)
								)
								(pc7 posn: 207 157 setPri: 10)
							)
							(8
								(= thisPiece 8)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place9 0)
									(= pcFollowing 0)
									(= place9 thisPiece)
								else
									(= pcFollowing place9)
									(= place9 thisPiece)
								)
								(pc8 posn: 207 157 setPri: 10)
							)
							(9
								(= thisPiece 9)
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(if (== place9 0)
									(= pcFollowing 0)
									(= place9 thisPiece)
								else
									(= pcFollowing place9)
									(= place9 thisPiece)
								)
								(pc9 posn: 207 157 setPri: 10)
							)
						)
					) ; end current cursor switch
					; FormatPrint("%d %d %d %d %d %d %d %d %d" place1 place2 place3 place4 place5 place6 place7 place8 place9) ; end if Control
					; ****************************************************************************
					; So that should cover the clicks on the game board. Now we need to handle
					; the clicks on the pieces that are sitting in the tray. Had to create a second
					; instance of the piece in order to handle this as easily as possible.
					; ****************************************************************************
					(if
						(and
							(> (pEvent x?) (side1 nsLeft?))
							(< (pEvent x?) (side1 nsRight?))
							(> (pEvent y?) (side1 nsTop?))
							(< (pEvent y?) (side1 nsBottom?))
						)
						(switch pcFollowing
							(0
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(fkCursor posn: 400 400)
								(side1 posn: 400 400)
							)
							(1
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
							)
							; (side1:posn((side1:x) (side1:y)))
							; (pc1:posn(400 400))
							; (side1:posn(400 400))
							(2
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side2 posn: (side1 x?) (side1 y?))
								(pc2 posn: 400 400)
								(side1 posn: 400 400)
							)
							(3
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side3 posn: (side1 x?) (side1 y?))
								(pc3 posn: 400 400)
								(side1 posn: 400 400)
							)
							(4
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side4 posn: (side1 x?) (side1 y?))
								(pc4 posn: 400 400)
								(side1 posn: 400 400)
							)
							(5
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side5 posn: (side1 x?) (side1 y?))
								(pc5 posn: 400 400)
								(side1 posn: 400 400)
							)
							(6
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side6 posn: (side1 x?) (side1 y?))
								(pc6 posn: 400 400)
								(side1 posn: 400 400)
							)
							(7
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side7 posn: (side1 x?) (side1 y?))
								(pc7 posn: 400 400)
								(side1 posn: 400 400)
							)
							(8
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side8 posn: (side1 x?) (side1 y?))
								(pc8 posn: 400 400)
								(side1 posn: 400 400)
							)
							(9
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side9 posn: (side1 x?) (side1 y?))
								(pc9 posn: 400 400)
								(side1 posn: 400 400)
							)
						) ; end current cursor switch
						(= pcFollowing 1)
					) ; end view
					(if
						(and
							(> (pEvent x?) (side2 nsLeft?))
							(< (pEvent x?) (side2 nsRight?))
							(> (pEvent y?) (side2 nsTop?))
							(< (pEvent y?) (side2 nsBottom?))
						)
						(switch pcFollowing
							(0
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(fkCursor posn: 400 400)
								(side2 posn: 400 400)
							)
							(1
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side1 posn: (side2 x?) (side2 y?))
								(pc1 posn: 400 400)
								(side2 posn: 400 400)
							)
							(2
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
							)
							; (side2:posn((side1:x) (side1:y)))
							; (pc2:posn(400 400))
							; (side2:posn(400 400))
							(3
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side3 posn: (side2 x?) (side2 y?))
								(pc3 posn: 400 400)
								(side2 posn: 400 400)
							)
							(4
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side4 posn: (side2 x?) (side2 y?))
								(pc4 posn: 400 400)
								(side2 posn: 400 400)
							)
							(5
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side5 posn: (side2 x?) (side2 y?))
								(pc5 posn: 400 400)
								(side2 posn: 400 400)
							)
							(6
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side6 posn: (side2 x?) (side2 y?))
								(pc6 posn: 400 400)
								(side2 posn: 400 400)
							)
							(7
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side7 posn: (side2 x?) (side2 y?))
								(pc7 posn: 400 400)
								(side2 posn: 400 400)
							)
							(8
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side8 posn: (side2 x?) (side2 y?))
								(pc8 posn: 400 400)
								(side2 posn: 400 400)
							)
							(9
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side9 posn: (side2 x?) (side2 y?))
								(pc9 posn: 400 400)
								(side2 posn: 400 400)
							)
						) ; end current cursor switch
						(= pcFollowing 2)
					) ; end view
					(if
						(and
							(> (pEvent x?) (side3 nsLeft?))
							(< (pEvent x?) (side3 nsRight?))
							(> (pEvent y?) (side3 nsTop?))
							(< (pEvent y?) (side3 nsBottom?))
						)
						(switch pcFollowing
							(0
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(fkCursor posn: 400 400)
								(side3 posn: 400 400)
							)
							(1
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side1 posn: (side3 x?) (side3 y?))
								(pc1 posn: 400 400)
								(side3 posn: 400 400)
							)
							(2
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side2 posn: (side3 x?) (side3 y?))
								(pc2 posn: 400 400)
								(side3 posn: 400 400)
							)
							(3
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
							)
							; (side3:posn((side3:x) (side3:y)))
							; (pc3:posn(400 400))
							; (side3:posn(400 400))
							(4
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side4 posn: (side3 x?) (side3 y?))
								(pc4 posn: 400 400)
								(side3 posn: 400 400)
							)
							(5
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side5 posn: (side3 x?) (side3 y?))
								(pc5 posn: 400 400)
								(side3 posn: 400 400)
							)
							(6
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side6 posn: (side3 x?) (side3 y?))
								(pc6 posn: 400 400)
								(side3 posn: 400 400)
							)
							(7
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side7 posn: (side3 x?) (side3 y?))
								(pc7 posn: 400 400)
								(side3 posn: 400 400)
							)
							(8
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side8 posn: (side3 x?) (side3 y?))
								(pc8 posn: 400 400)
								(side3 posn: 400 400)
							)
							(9
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side9 posn: (side3 x?) (side3 y?))
								(pc9 posn: 400 400)
								(side3 posn: 400 400)
							)
						) ; end current cursor switch
						(= pcFollowing 3)
					) ; end view
					(if
						(and
							(> (pEvent x?) (side4 nsLeft?))
							(< (pEvent x?) (side4 nsRight?))
							(> (pEvent y?) (side4 nsTop?))
							(< (pEvent y?) (side4 nsBottom?))
						)
						(switch pcFollowing
							(0
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(fkCursor posn: 400 400)
								(side4 posn: 400 400)
							)
							(1
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side1 posn: (side4 x?) (side4 y?))
								(pc1 posn: 400 400)
								(side4 posn: 400 400)
							)
							(2
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side2 posn: (side4 x?) (side4 y?))
								(pc2 posn: 400 400)
								(side4 posn: 400 400)
							)
							(3
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side3 posn: (side4 x?) (side4 y?))
								(pc3 posn: 400 400)
								(side4 posn: 400 400)
							)
							(4
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
							)
							; (side4:posn((side4:x) (side4:y)))
							; (pc4:posn(400 400))
							; (side4:posn(400 400))
							(5
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side5 posn: (side4 x?) (side4 y?))
								(pc5 posn: 400 400)
								(side4 posn: 400 400)
							)
							(6
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side6 posn: (side4 x?) (side4 y?))
								(pc6 posn: 400 400)
								(side4 posn: 400 400)
							)
							(7
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side7 posn: (side4 x?) (side4 y?))
								(pc7 posn: 400 400)
								(side4 posn: 400 400)
							)
							(8
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side8 posn: (side4 x?) (side4 y?))
								(pc8 posn: 400 400)
								(side4 posn: 400 400)
							)
							(9
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side9 posn: (side4 x?) (side4 y?))
								(pc9 posn: 400 400)
								(side4 posn: 400 400)
							)
						) ; end current cursor switch
						(= pcFollowing 4)
					) ; end view
					(if
						(and
							(> (pEvent x?) (side5 nsLeft?))
							(< (pEvent x?) (side5 nsRight?))
							(> (pEvent y?) (side5 nsTop?))
							(< (pEvent y?) (side5 nsBottom?))
						)
						(switch pcFollowing
							(0
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(fkCursor posn: 400 400)
								(side5 posn: 400 400)
							)
							(1
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side1 posn: (side5 x?) (side5 y?))
								(pc1 posn: 400 400)
								(side5 posn: 400 400)
							)
							(2
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side2 posn: (side5 x?) (side5 y?))
								(pc2 posn: 400 400)
								(side5 posn: 400 400)
							)
							(3
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side3 posn: (side5 x?) (side5 y?))
								(pc3 posn: 400 400)
								(side5 posn: 400 400)
							)
							(4
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side4 posn: (side5 x?) (side5 y?))
								(pc4 posn: 400 400)
								(side5 posn: 400 400)
							)
							(5
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
							)
							; (side5:posn((side5:x) (side5:y)))
							; (pc5:posn(400 400))
							; (side5:posn(400 400))
							(6
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side6 posn: (side5 x?) (side5 y?))
								(pc6 posn: 400 400)
								(side5 posn: 400 400)
							)
							(7
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side7 posn: (side5 x?) (side5 y?))
								(pc7 posn: 400 400)
								(side5 posn: 400 400)
							)
							(8
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side8 posn: (side5 x?) (side5 y?))
								(pc8 posn: 400 400)
								(side5 posn: 400 400)
							)
							(9
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side9 posn: (side5 x?) (side5 y?))
								(pc9 posn: 400 400)
								(side5 posn: 400 400)
							)
						) ; end current cursor switch
						(= pcFollowing 5)
					) ; end view
					(if
						(and
							(> (pEvent x?) (side6 nsLeft?))
							(< (pEvent x?) (side6 nsRight?))
							(> (pEvent y?) (side6 nsTop?))
							(< (pEvent y?) (side6 nsBottom?))
						)
						(switch pcFollowing
							(0
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(fkCursor posn: 400 400)
								(side6 posn: 400 400)
							)
							(1
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side1 posn: (side6 x?) (side6 y?))
								(pc1 posn: 400 400)
								(side6 posn: 400 400)
							)
							(2
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side2 posn: (side6 x?) (side6 y?))
								(pc2 posn: 400 400)
								(side6 posn: 400 400)
							)
							(3
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side3 posn: (side6 x?) (side6 y?))
								(pc3 posn: 400 400)
								(side6 posn: 400 400)
							)
							(4
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side4 posn: (side6 x?) (side6 y?))
								(pc4 posn: 400 400)
								(side6 posn: 400 400)
							)
							(5
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side5 posn: (side6 x?) (side6 y?))
								(pc5 posn: 400 400)
								(side6 posn: 400 400)
							)
							(6
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
							)
							; (side6:posn((side6:x) (side6:y)))
							; (pc6:posn(400 400))
							; (side6:posn(400 400))
							(7
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side7 posn: (side6 x?) (side6 y?))
								(pc7 posn: 400 400)
								(side6 posn: 400 400)
							)
							(8
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side8 posn: (side6 x?) (side6 y?))
								(pc8 posn: 400 400)
								(side6 posn: 400 400)
							)
							(9
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side9 posn: (side6 x?) (side6 y?))
								(pc9 posn: 400 400)
								(side6 posn: 400 400)
							)
						) ; end current cursor switch
						(= pcFollowing 6)
					) ; end view
					(if
						(and
							(> (pEvent x?) (side7 nsLeft?))
							(< (pEvent x?) (side7 nsRight?))
							(> (pEvent y?) (side7 nsTop?))
							(< (pEvent y?) (side7 nsBottom?))
						)
						(switch pcFollowing
							(0
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(fkCursor posn: 400 400)
								(side7 posn: 400 400)
							)
							(1
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side1 posn: (side7 x?) (side7 y?))
								(pc1 posn: 400 400)
								(side7 posn: 400 400)
							)
							(2
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side2 posn: (side7 x?) (side7 y?))
								(pc2 posn: 400 400)
								(side7 posn: 400 400)
							)
							(3
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side3 posn: (side7 x?) (side7 y?))
								(pc3 posn: 400 400)
								(side7 posn: 400 400)
							)
							(4
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side4 posn: (side7 x?) (side7 y?))
								(pc4 posn: 400 400)
								(side7 posn: 400 400)
							)
							(5
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side5 posn: (side7 x?) (side7 y?))
								(pc5 posn: 400 400)
								(side7 posn: 400 400)
							)
							(6
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side6 posn: (side7 x?) (side7 y?))
								(pc6 posn: 400 400)
								(side7 posn: 400 400)
							)
							(7
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
							)
							; (side7:posn((side7:x) (side7:y)))
							; (pc7:posn(400 400))
							; (side7:posn(400 400))
							(8
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side8 posn: (side7 x?) (side7 y?))
								(pc8 posn: 400 400)
								(side7 posn: 400 400)
							)
							(9
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side9 posn: (side7 x?) (side7 y?))
								(pc9 posn: 400 400)
								(side7 posn: 400 400)
							)
						) ; end current cursor switch
						(= pcFollowing 7)
					) ; end view
					(if
						(and
							(> (pEvent x?) (side8 nsLeft?))
							(< (pEvent x?) (side8 nsRight?))
							(> (pEvent y?) (side8 nsTop?))
							(< (pEvent y?) (side8 nsBottom?))
						)
						(switch pcFollowing
							(0
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(fkCursor posn: 400 400)
								(side8 posn: 400 400)
							)
							(1
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side1 posn: (side8 x?) (side8 y?))
								(pc1 posn: 400 400)
								(side8 posn: 400 400)
							)
							(2
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side2 posn: (side8 x?) (side8 y?))
								(pc2 posn: 400 400)
								(side8 posn: 400 400)
							)
							(3
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side3 posn: (side8 x?) (side8 y?))
								(pc3 posn: 400 400)
								(side8 posn: 400 400)
							)
							(4
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side4 posn: (side8 x?) (side8 y?))
								(pc4 posn: 400 400)
								(side8 posn: 400 400)
							)
							(5
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side5 posn: (side8 x?) (side8 y?))
								(pc5 posn: 400 400)
								(side8 posn: 400 400)
							)
							(6
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side6 posn: (side8 x?) (side8 y?))
								(pc6 posn: 400 400)
								(side8 posn: 400 400)
							)
							(7
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side7 posn: (side8 x?) (side8 y?))
								(pc7 posn: 400 400)
								(side8 posn: 400 400)
							)
							(8
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
							)
							; (side8:posn((side8:x) (side8:y)))
							; (pc8:posn(400 400))
							; (side8:posn(400 400))
							(9
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side9 posn: (side8 x?) (side8 y?))
								(pc9 posn: 400 400)
								(side8 posn: 400 400)
							)
						) ; end current cursor switch
						(= pcFollowing 8)
					) ; end view
					(if
						(and
							(> (pEvent x?) (side9 nsLeft?))
							(< (pEvent x?) (side9 nsRight?))
							(> (pEvent y?) (side9 nsTop?))
							(< (pEvent y?) (side9 nsBottom?))
						)
						(switch pcFollowing
							(0
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(fkCursor posn: 400 400)
								(side9 posn: 400 400)
							)
							(1
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side1 posn: (side9 x?) (side9 y?))
								(pc1 posn: 400 400)
								(side9 posn: 400 400)
							)
							(2
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side2 posn: (side9 x?) (side9 y?))
								(pc2 posn: 400 400)
								(side9 posn: 400 400)
							)
							(3
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side3 posn: (side9 x?) (side9 y?))
								(pc3 posn: 400 400)
								(side9 posn: 400 400)
							)
							(4
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side4 posn: (side9 x?) (side9 y?))
								(pc4 posn: 400 400)
								(side9 posn: 400 400)
							)
							(5
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side5 posn: (side9 x?) (side9 y?))
								(pc5 posn: 400 400)
								(side9 posn: 400 400)
							)
							(6
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side6 posn: (side9 x?) (side9 y?))
								(pc6 posn: 400 400)
								(side9 posn: 400 400)
							)
							(7
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side7 posn: (side9 x?) (side9 y?))
								(pc7 posn: 400 400)
								(side9 posn: 400 400)
							)
							(8
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
								(side8 posn: (side9 x?) (side9 y?))
								(pc8 posn: 400 400)
								(side9 posn: 400 400)
							)
							(9
								(pEvent type: evMOUSEBUTTON claimed: TRUE)
							)
						)
						; (side9:posn((side9:x) (side9:y)))
						; (pc9:posn(400 400))
						; (side9:posn(400 400)) ; end current cursor switch
						(= pcFollowing 9)
					)
				)
			)
		)
	)
)
 ; end view
; *************************************/ ; end if not program control ; end left click ; end mouse button ; end method ; end instance
; ********************************************
(instance lightning1 of Prop
	(properties
		view 581
		loop 2
		cel 0
		x 157
		y 94
	)
)

(instance lightning2 of Prop
	(properties
		view 581
		loop 5
		cel 0
		x 135
		y 96
	)
)

(instance lightning3 of Prop
	(properties
		view 581
		loop 6
		cel 0
		x 187
		y 96
	)
)

(instance lightning4 of Prop
	(properties
		view 581
		loop 2
		cel 0
		x 111
		y 94
	)
)

(instance lightning5 of Prop
	(properties
		view 581
		loop 2
		cel 0
		x 111
		y 143
	)
)

(instance lightning6 of Prop
	(properties
		view 581
		loop 2
		cel 0
		x 210
		y 94
	)
)

(instance lightning7 of Prop
	(properties
		view 581
		loop 2
		cel 0
		x 210
		y 143
	)
)

(instance lightning8 of Prop
	(properties
		view 581
		loop 5
		cel 0
		x 134
		y 148
	)
)

(instance lightning9 of Prop
	(properties
		view 581
		loop 6
		cel 0
		x 184
		y 148
	)
)

(instance lightning10 of Prop
	(properties
		view 581
		loop 5
		cel 0
		x 134
		y 40
	)
)

(instance lightning11 of Prop
	(properties
		view 581
		loop 6
		cel 0
		x 184
		y 40
	)
)

(instance lightning12 of Prop
	(properties
		view 581
		loop 5
		cel 0
		x 138
		y 189
	)
)

(instance lightning13 of Prop
	(properties
		view 581
		loop 6
		cel 0
		x 181
		y 189
	)
)

(instance lightning14 of Prop
	(properties
		view 581
		loop 2
		cel 0
		x 160
		y 145
	)
)

(instance portal of Prop
	(properties
		view 581
		loop 1
		cel 0
		x 160
		y 119
	)
)

(instance doWin of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch newState
			(0
				(Print {Suddenly the ...})
				(ShakeScreen 4 ssFULL_SHAKE)
				(= cycles 5)
			)
			(1
				(lightning1 init: setPri: 5 setCycle: Fwd)
				(= cycles 5)
			)
			(2
				(lightning2 init: setPri: 5 setCycle: Fwd)
				(lightning3 init: setPri: 5 setCycle: Fwd)
				(= cycles 5)
			)
			(3
				(lightning4 init: setPri: 5 setCycle: Fwd)
				(lightning5 init: setPri: 5 setCycle: Fwd)
				(lightning6 init: setPri: 5 setCycle: Fwd)
				(lightning7 init: setPri: 5 setCycle: Fwd)
				(= cycles 5)
			)
			(4
				(lightning8 init: setPri: 5 setCycle: Fwd)
				(lightning9 init: setPri: 5 setCycle: Fwd)
				(lightning10 init: setPri: 5 setCycle: Fwd)
				(lightning11 init: setPri: 5 setCycle: Fwd)
				(lightning12 init: setPri: 5 setCycle: Fwd)
				(lightning13 init: setPri: 5 setCycle: Fwd)
				(= cycles 5)
			)
			(5
				(lightning14 init: setPri: 5 setCycle: Fwd)
				(= cycles 15)
			)
			(6
				(portal init: setPri: 15 setCycle: Fwd)
				(= cycles 25)
			)
			(7
				(Print {Next room})
				(= goToRoom nextRoom)
			)
		)
	)
)
