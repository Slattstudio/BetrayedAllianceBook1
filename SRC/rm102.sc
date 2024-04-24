;;; Sierra Script 1.0 - (do not remove this comment)
; +3 SCORE // +2 gINT //
(script# 102)
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
	rm102 0
)

(local

; SLIDE BLOCK PUZZLE
; By RYAN SLATTERY




	block1posn =  4

	block2posn =  6
; Visual represetation of positions
	block3posn =  7
; No block at 11: emptyBlock 11X
	block4posn =  8
; |----------------------------- |
	block5posn =  9
; |   1   |   2   |  3   |  4    |
	block6posn =  5
; |------------------------------|
	block8posn =  10
; |   5   |   6   |  7   |  8    |
	block9posn =  12
; |------------------------------|
	block10posn =  1
; |   9   |  10   | 11X  |  12   |
	block11posn =  3
; |------------------------------|
	block12posn =  2

	; Block 7 is not used on the puzzle board and as such, doesn't have a position variable
	emptyBlock =  11  ; Represents which block position is without a block (range of value: 1 - 12)
	oldEmpty =  11
	clicked =  0      ; If TRUE (1) the player cannot move another block. This destroys timing and movement bugs
	; Example bug: any block that is moved will always be a possible block to move after it itself
	; moves. Hence, the player can double click a block and it will move 30 paces one direction
	; without having completed it 30 paces in the first direction, which will place it outside
	; any square space.
	edgeMove          ; This switch variable (used in procedure edgeCheck at 650) is used to disallow edge pieces to
	; move left or right when they are on the left or right edges respectively. Since a block is
	; told to move right if the empty space is greater than the block position +1, a block on space
	; four (4) would be told to move right if the empty square is at five (5). However, this
	; results in a movement placing the block outside the puzzle board. This variable is used to
	; prevent that nasty bug.
	topRow =  0       ; Will equal TRUE (1) when blocks 1,2,3,4 are on block positions 1,2,3,4 respectively
	bottomRow =  0    ; Will equal TRUE (1) when blocks 9,10,11,12 are on block positions 9,10,11,12 respectively
	leftRow =  0      ; Will equal TRUE (1) when blocks 1,5,9 are on block positions 1,5,9 respectively
	rightRow =  0     ; Will equal TRUE (1) when blocks 4,8,12 are on block positions 4,8,12 respectively
	allRows =  0      ; Will equal TRUE (1) when ^^those four are all TRUE (1)
	oldSpeed          ; Will equal gSpeed, so game can be put back to normal speed after puzzle solved
	solved =  0
	clickedBlock =  0 ; Used to determind which block was clicked for the alternate enter
	blockFailNum =  0
)                     ; Adds by 1 whenever unique block clicked but not moved. When 13, alternate win con unveiled

(instance rm102 of Rm
	(properties
		picture scriptNumber
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(= oldSpeed gSpeed)
		(gGame setSpeed: 3)
		(grid init: setScript: emptyChange)
		(block1 init: ignoreActors: setPri: 12)

		(block2 init: ignoreActors: setPri: 12)
; Starting Positions on the Board
		(block3 init: ignoreActors: setPri: 12)
; (X) is the open block
		(block4 init: ignoreActors: setPri: 12)
; |-------------------------------|
		(block5 init: ignoreActors: setPri: 12)
; |   10  |   12  |   11  |   1   |
		(block6 init: ignoreActors: setPri: 12)
; |-------------------------------|
		(block8 init: ignoreActors: setPri: 12)
; |   6   |   2   |   3   |   4   |
		(block9 init: ignoreActors: setPri: 12)
; |-------------------------------|
		(block10 init: ignoreActors: setPri: 12)
; |   5   |   8   |   X   |   9   |
		(block11 init: ignoreActors: setPri: 12)
; |-------------------------------|
		(block12 init: ignoreActors: setPri: 12)

		(block7 init: hide:)      ; Blcok 7 is the immovable block that will be added to complete the puzzle
		(leave init:)       ; View which will let the player quit the puzzle if they want
		(topBar init:)      ; Visible representation that the top row are all in order
		(bottomBar init:)   ; Visible representation that the bottom row are all in order
		(leftBar init:)     ; Visible representation that the left column are all in order
		(rightBar init:)    ; Visible representation that the right column are all in order
		; (instructions:init())
		; (infoButton:init())
		(skip init: hide: setPri: 15)
		(= gMap 0)
	)
)

; SetUpEgo()
; (send gEgo:init()hide()put(INV_BLOCK 102))

(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= seconds 1))
			(1
				; (if (send gEgo:has(13)) // block this out and use the one below when testing is over
				(if (== (IsOwnedBy INV_BLOCK 102) TRUE)
					(Print 102 7) ; You place the missing piece onto the board and ready your mind.
					(block7 show:)
				else
					; (send gEgo:put(13 102))
					(Print 102 8 #width 280 #at -1 10) ; There seems to be a missing piece.
					(Print 102 9 #width 280 #at -1 10) ; The blocks are immobile without the last piece.
					(= gTimeCh 0)
					(gGame setSpeed: oldSpeed)
					(gRoom newRoom: 41)
				)
			)
		)
	)
	
	(method (handleEvent pEvent &tmp button)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if
				(and
					(> (pEvent x?) 233)
					(< (pEvent x?) 259)
					(> (pEvent y?) 41)
					(< (pEvent y?) 64)
				)                         ; If clicked on part of Pic
				(Print 102 5)
			)
			(if
				(and
					(> (pEvent x?) (leave nsLeft?))
					(< (pEvent x?) (leave nsRight?))
					(> (pEvent y?) (leave nsTop?))
					(< (pEvent y?) (leave nsBottom?))
				)                                       ; If clicked on leave view
				(= gTimeCh 0)
				(gEgo get: INV_BLOCK)
				(gGame setSpeed: oldSpeed)
				(gRoom newRoom: 41)
			)
			(if
				(and
					(> (pEvent x?) (skip nsLeft?))
					(< (pEvent x?) (skip nsRight?))
					(> (pEvent y?) (skip nsTop?))
					(< (pEvent y?) (skip nsBottom?))
				)                                      ; If clicked on skip view
				(if (or (not gHardMode) (> blockFailNum 12))
					(= button
						(Print
							{You found a secret button. Would you like to press it?}
							#button
							{ Yes_}
							1
							#button
							{ No_}
							0
							#font
							4
						)
					)
					(if (== button 1)
						(= solved 1)
						(emptyChange changeState: 6)    ; Send game to the winning section
						(= allRows 1)
					)
				)
			)
			; skip the game
			(if
				(and
					(> (pEvent x?) (block7 nsLeft?))
					(< (pEvent x?) (block7 nsRight?))
					(> (pEvent y?) (block7 nsTop?))
					(< (pEvent y?) (block7 nsBottom?))
				)                                       ; If clicked on block7 view (the unmovable block)
				(Print 102 1)
			)                   ; Let's player know that this block is sealed off from the puzzle board
; (if(not(clicked))
;                    (if(== (emptyBlock) (+(block1posn)1)) // Check to see if the empty block is to the right
;                        (edgeCheckRight)    // Checks to see if you clicked on a block on the RIGHT edge
;                        (if(edgeMove)   // if you are not on an edge like 4 or 8
;                            (block1:xStep(4)setMotion(MoveTo (+(block1:x)30) (block1:y) )) // move right
;                            = block1posn (+(block1posn)1) // if a block moves right, its position on the grid is +1
;                            (changeEmpty)   // Makes clicked TRUE (1) and sets 10 cycles before next move is possible
;                            = emptyBlock (-(emptyBlock)1) // the empty block, if a block moves right, lessens by 1
;                        )(else // edgeMove is FALSE meaning you canNOT make that move.
;                            = edgeMove 1 // You were on the edge, and the variable is reset for next time.
;                        )
;                    )(else  // That is, if the empty block is NOT to the right of the block you clicked...
;                        (if(== (emptyBlock) (-(block1posn)1))   // Maybe it is on the left?
;                            (edgeCheckLeft) // Checks to see if you clicked on a block on the LEFT edge
;                            (if(edgeMove)   // If your block is OK to move left...
;                                (block1:xStep(4)setMotion(MoveTo (-(block1:x)30) (block1:y) )) // move left
;                                = block1posn (-(block1posn)1)   // A block moving Left will always = itself - 1
;                                (changeEmpty)
;                                = emptyBlock (+(emptyBlock)1)   // An empty block position will increase by 1 by a LEFT move
;                            )(else
;                                = edgeMove 1
;                            )
;                        )(else // If the empty block space is not LEFT or RIGHT...
;                            (if(== (emptyBlock) (+(block1posn)4)) // Maybe it is down (which has a difference of 4)
;                                (block1:yStep(4)setMotion(MoveTo (block1:x) (+(block1:y)30) )) // move down
;                                = block1posn (+(block1posn)4)   // A block moving down increases by 4
;                                (changeEmpty)
;                                = emptyBlock (-(emptyBlock)4)   // The blank space is subtracted by 4
;                            )(else  // If the empty block is not RIGHT, LEFT, or DOWN...
;                                (if(== (emptyBlock) (-(block1posn)4))   // Is it UP?
;                                    (block1:yStep(4)setMotion(MoveTo (block1:x) (-(block1:y)30) )) // move up
;                                    = block1posn (-(block1posn)4) //similar to down, only reversed in algebra
;                                    (changeEmpty)
;                                    = emptyBlock (+(emptyBlock)4)
;                                )
;                            )
;                        )
;                    )
;                )
			(if
				(checkEvent
					pEvent
					(block1 nsLeft?)
					(block1 nsRight?)
					(block1 nsTop?)
					(block1 nsBottom?)
				)
				(moveBlock block1 block1posn)
				(if (== oldEmpty emptyBlock)
				else
					(= block1posn oldEmpty)
					(= oldEmpty emptyBlock)
				)
			)
			; All the rest of the blocks follow the same logic as the first, requiring no commentary.
			(if
				(checkEvent
					pEvent
					(block2 nsLeft?)
					(block2 nsRight?)
					(block2 nsTop?)
					(block2 nsBottom?)
				)
				(moveBlock block2 block2posn)
				(if (== oldEmpty emptyBlock)
				else
					(= block2posn oldEmpty)
					(= oldEmpty emptyBlock)
				)
			)
			(if
				(checkEvent
					pEvent
					(block3 nsLeft?)
					(block3 nsRight?)
					(block3 nsTop?)
					(block3 nsBottom?)
				)
				(moveBlock block3 block3posn)
				(if (== oldEmpty emptyBlock)
				else
					(= block3posn oldEmpty)
					(= oldEmpty emptyBlock)
				)
			)
			(if
				(checkEvent
					pEvent
					(block4 nsLeft?)
					(block4 nsRight?)
					(block4 nsTop?)
					(block4 nsBottom?)
				)
				(moveBlock block4 block4posn)
				(if (== oldEmpty emptyBlock)
				else
					(= block4posn oldEmpty)
					(= oldEmpty emptyBlock)
				)
			)
			(if
				(checkEvent
					pEvent
					(block5 nsLeft?)
					(block5 nsRight?)
					(block5 nsTop?)
					(block5 nsBottom?)
				)
				(moveBlock block5 block5posn)
				(if (== oldEmpty emptyBlock)
				else
					(= block5posn oldEmpty)
					(= oldEmpty emptyBlock)
				)
			)
			(if
				(checkEvent
					pEvent
					(block6 nsLeft?)
					(block6 nsRight?)
					(block6 nsTop?)
					(block6 nsBottom?)
				)
				(moveBlock block6 block6posn)
				(if (== oldEmpty emptyBlock)
				else
					(= block6posn oldEmpty)
					(= oldEmpty emptyBlock)
				)
			)
			(if
				(checkEvent
					pEvent
					(block8 nsLeft?)
					(block8 nsRight?)
					(block8 nsTop?)
					(block8 nsBottom?)
				)
				(moveBlock block8 block8posn)
				(if (== oldEmpty emptyBlock)
				else
					(= block8posn oldEmpty)
					(= oldEmpty emptyBlock)
				)
			)
			(if
				(checkEvent
					pEvent
					(block9 nsLeft?)
					(block9 nsRight?)
					(block9 nsTop?)
					(block9 nsBottom?)
				)
				(moveBlock block9 block9posn)
				(if (== oldEmpty emptyBlock)
				else
					(= block9posn oldEmpty)
					(= oldEmpty emptyBlock)
				)
			)
			(if
				(checkEvent
					pEvent
					(block10 nsLeft?)
					(block10 nsRight?)
					(block10 nsTop?)
					(block10 nsBottom?)
				)
				(moveBlock block10 block10posn)
				(if (== oldEmpty emptyBlock)
				else
					(= block10posn oldEmpty)
					(= oldEmpty emptyBlock)
				)
			)
			(if
				(checkEvent
					pEvent
					(block11 nsLeft?)
					(block11 nsRight?)
					(block11 nsTop?)
					(block11 nsBottom?)
				)
				(moveBlock block11 block11posn)
				(if (== oldEmpty emptyBlock)
				else
					(= block11posn oldEmpty)
					(= oldEmpty emptyBlock)
				)
			)
			(if
				(checkEvent
					pEvent
					(block12 nsLeft?)
					(block12 nsRight?)
					(block12 nsTop?)
					(block12 nsBottom?)
				)
				(moveBlock block12 block12posn)
				(if (== oldEmpty emptyBlock)
				else
					(= block12posn oldEmpty)
					(= oldEmpty emptyBlock)
				)
			)
		)
	)
	
	(method (doit &tmp button)
		(super doit:)
		(if gHardMode
			(if (< blockFailNum 13) (skip hide:) else (skip show:))
		else
			(skip show:)
			(if (== blockFailNum 13)	; if click 13 or more times.
				
				(++ blockFailNum)
				(= button
					(Print
						{You found a secret button. Would you like to press it?}
						#button
						{ Yes_}
						1
						#button
						{ No_}
						0
						#font
						4
					)
				)
				(if (== button 1)
					(= solved 1)
					(emptyChange changeState: 6)    ; Send game to the winning section
					(= allRows 1)
				)
			)
		)
		(if (not solved)
			(if
				(and
					(== block1posn 1)
					(== block5posn 5)
					(== block9posn 9)
				)                                                      ; If LEFT side is in place
				(leftBar cel: 1) ; Visually shows that the left column is in order
				(= leftRow 1)
			else                ; Sets the varible to TRUE (1) ; If the blocks are out of order...
				(leftBar cel: 0) ; Visually show that the column is NOT in order (as if they couldn't tell!)
				(= leftRow 0)
			)                   ; Sets the variable to FALSE (0)
			(if
				(and
					(== block4posn 4)
					(== block8posn 8)
					(== block12posn 12)
				)                                                        ; If right side is in place
				(rightBar cel: 1)
				(= rightRow 1)
			else
				(rightBar cel: 0)
				(= rightRow 0)
			)
			(if
				(and
					(== block1posn 1)
					(== block2posn 2)
					(== block3posn 3)
					(== block4posn 4)
				)                                                                          ; If top side is in place
				(topBar cel: 1)
				(= topRow 1)
			else
				(topBar cel: 0)
				(= topRow 0)
			)
			(if
				(and
					(== block9posn 9)
					(== block10posn 10)
					(== block11posn 11)
					(== block12posn 12)
				)                                                                                ; If bottom side is in place
				(bottomBar cel: 1)
				(= bottomRow 1)
			else
				(bottomBar cel: 0)
				(= bottomRow 0)
			)
			(if (and leftRow bottomRow topRow rightRow)         ; If all rows are in place...
				(if (not allRows)
					(emptyChange changeState: 6) ; Send game to the winning section
					(= allRows 1)
					(= solved 1)
				)
			)
		)
	)
)

(procedure (changeEmpty)
	(emptyChange changeState: 2)    ; Allows 10 cycles before player can use mouse again
	(= clicked 1)
)
                    ; When True (1) player cannot use mouse on blocks
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

(procedure (edgeCheckRight)
	(if (or (== emptyBlock 5) (== emptyBlock 9))            ; 5 and 9 are the two LEFT positions at risk of the bug
		(= edgeMove 0)
	)
)

(procedure (edgeCheckLeft)
	(if (or (== emptyBlock 4) (== emptyBlock 8))            ; 4 and 8 are the two RIGHT positions at risk of the bug
		(= edgeMove 0)
	)
)

(procedure (moveBlock view whichBlock)
	(if (not clicked)
		; FormatPrint("old empty: %u /n empty: %u /n blockNum: %u" oldEmpty emptyBlock whichBlock)
		(cond 
			((== emptyBlock (+ whichBlock 1))
				(edgeCheckRight)
				(if edgeMove
					(view
						xStep: 4
						setMotion: MoveTo (+ (view x?) 30) (view y?)
					)                                                                               ; move right
					(= oldEmpty emptyBlock)
					(changeEmpty)
					(= emptyBlock (- emptyBlock 1))
				else
					(= edgeMove 1)
				)
				(return)
			)
			((== emptyBlock (- whichBlock 1))
				(edgeCheckLeft)
				(if edgeMove
					(view
						xStep: 4
						setMotion: MoveTo (- (view x?) 30) (view y?)
					)                                                                                   ; move left
					(= oldEmpty emptyBlock)
					(changeEmpty)
					(= emptyBlock (+ emptyBlock 1))
				else
					(= edgeMove 1)
				)
				(return)
			)
			((== emptyBlock (+ whichBlock 4))
				(view
					yStep: 4
					setMotion: MoveTo (view x?) (+ (view y?) 30)
				)                                                                                       ; move down
				(= oldEmpty emptyBlock)
				(= emptyBlock (- emptyBlock 4))
				(changeEmpty)
				(return)
			)
			((== emptyBlock (- whichBlock 4))
				(view
					yStep: 4
					setMotion: MoveTo (view x?) (- (view y?) 30)
				)                                                                                           ; move up
				(= oldEmpty emptyBlock)
				(= emptyBlock (+ emptyBlock 4))
				(changeEmpty)
				(return)
			)
			(else
				(= oldEmpty emptyBlock)
				(= clickedBlock whichBlock)
				(if (not (== clickedBlock emptyBlock))
					; PrintOK()
					(++ blockFailNum)
				)
			)
		)
	)
)

; FormatPrint("num fail: %u" blockFailNum)
(instance emptyChange of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 (= gTimeCh 1)) ; Disables change in Speed (see Menubar)
			(2
				(= cycles 9)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)                                                       ; Visually show that the player must wait
			(3
				(= cycles 1)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
			)                                                       ; Visually show that the player can use the mouse
			(4 (= clicked 0))   ; Allows player to use mouse again
			(6
				(= cycles 15)
				(ProgramControl)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
			)                                                                          ; Visually show that the player must wait
			(7
				(= cycles 12)
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(if (== block6posn 7)
					(Print 102 0)
					(block6
						xStep: 4
						setMotion: MoveTo (- (block6 x?) 30) (block6 y?)
					)
				)
			)                                                                      ; move left
			(8
				(= cycles 15)
				(Print 102 2)
				(Print 102 3)
			)
			(9
				(= cycles 15)
				(Print 102 6)
				(block7 posn: 170 100 loop: 1 setPri: 12)
				(= gTimeCh 0)
				(= g102Solved 1)
				(gGame setSpeed: oldSpeed)
				(gGame changeScore: 3)
				(++ gInt)
				(++ gInt)
			)
			(10 (gRoom newRoom: 41))
		)
	)
)

; (case 12 = cycles 3 // If player clicked "instructions" button
;                (infoButton:cel(1)) // Visually show the button press...this is all just for aesthetic appeal ;)
;                (SetCursor(997 HaveMouse()) = gCurrentCursor 997) // set cursor to wait for ONE reason ^^
;            )(case 13 = cycles 1
;                (infoButton:cel(0)) // Show button "pop up" after the 3 cycle push
;            )(case 14
;                Print(102 4 ) // Now actually show the player the instructions
;                Print("" #icon 550 4 0)
;                (SetCursor(999 HaveMouse()) = gCurrentCursor 999) // Set cursor back to normal
;            )

(instance grid of Prop
	(properties
		y 133
		x 155
		view 550
	)
)

(instance leave of Prop
	(properties
		y 100
		x 275
		view 550
		loop 1
	)
)

(instance topBar of Prop
	(properties
		y 38
		x 155
		view 550
		loop 3
	)
)

(instance bottomBar of Prop
	(properties
		y 136
		x 155
		view 550
		loop 3
	)
)

(instance leftBar of Prop
	(properties
		y 131
		x 91
		view 550
		loop 2
	)
)

(instance rightBar of Prop
	(properties
		y 131
		x 219
		view 550
		loop 2
	)
)

(instance block1 of Act
	(properties
		y 70
		x 200
		view 551                     ; posn 4
	)
)

(instance block2 of Act
	(properties
		y 100
		x 140
		view 552                      ; posn 6
	)
)

(instance block3 of Act
	(properties
		y 100
		x 170
		view 553                      ; posn 7
	)
)

(instance block4 of Act
	(properties
		y 100
		x 200
		view 554                      ; posn 8
	)
)

(instance block5 of Act
	(properties
		y 130
		x 110
		view 555                      ; posn 9
	)
)

(instance block6 of Act
	(properties
		y 100
		x 110
		view 556                      ; posn 5
	)
)

(instance block8 of Act
	(properties
		y 130
		x 140
		view 558                      ; posn 10
	)
)

(instance block9 of Act
	(properties
		y 130
		x 200
		view 559                      ; posn 12
	)
)

(instance block10 of Act
	(properties
		y 70
		x 110
		view 560                     ; posn 1
	)
)

(instance block11 of Act
	(properties
		y 70
		x 170
		view 561                     ; posn 3
	)
)

(instance block12 of Act
	(properties
		y 70
		x 140
		view 562                     ; posn 2
	)
)

(instance block7 of Prop
	(properties
		y 100
		x 50
		view 557
	)
)

; (instance instructions of Prop
;    (properties y 22 x 60 view 998 loop 8)
; )
; (instance infoButton of Prop
;    (properties y 22 x 50 view 998 loop 9)
; )
(instance skip of Prop
	(properties
		y 120
		x 236
		view 998
		loop 11
	)
)
