;;; Sierra Script 1.0 - (do not remove this comment)
(script# 114)
(include sci.sh)
(include game.sh)
(use Controls)
(use Cycle)
(use Door)
(use Feature)
(use Game)
(use Inv)
(use Main)
(use Obj)

(public
	rm114 0
)

(local

	cursorOver = 0	; which block is the cursor over, if any
	blockHeld = 0 ; which block is the player currently holding
	blockInput = 0 ; This is the block number that will be inputted into the slot
					 
	boardNumberSelect = 0 ; which square is currently selected (by ego being on its respective control color)
	;handPosition = 0 ; used with keyboard controls to dictate where you can grab a block
	
	; each square will be filled with the number corresponding to the block the solution being	 1, 2, 3
																							  	;4, 5, 6
																							  	;7, 8, 9
	[squareFilled 9] = [0 0 0	; 0, 1, 2
						0 0 0	; 3, 4, 5
						0 0 0]	; 6, 7, 8
						
	solved = 0		; did you win the game yet?
	swapping = 0	; used to change what's being placed and picked up in the event there's already a piece down
	mouseControl = 1	; 0 when using the keyboard only
	
	myEvent ; used in doit to determine the coords of the cursor
	
	; use 0 for no potential, 1 for out potential, and 2 for possible in potential
	[blockPlacedPotential 6] = [0 0 0 0 0 0]	; 0 1 2
												; x x x
												; 3 4 5
											
	; used in for loop to check all potential combinations that meet with the placed cube and will trigger one of six lightning views
	[SurroundingBlockPotential 6] = [0 0 0 0 0 0]	; 5 4 3
													; x x x
													; 2 1 0	
																				
	
)


(instance rm114 of Rm
	(properties
		picture 110
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
				(gEgo posn: 150 100 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: hide:)	; Ego, while hidden, is transported around screen to trigger different control colors and set boardNumberSelect
		
		(exitButton init: setPri: 7)
		
		(block1 init: setPri: 7)
		(block2 init: setPri: 7)
		(block3 init: setPri: 7)
		(block4 init: setPri: 7)
		(block5 init: setPri: 7)
		(block6 init: setPri: 7)
		(block7 init: setPri: 7)
		(block8 init: setPri: 7)
		(block9 init: setPri: 7)
		
		(lightningComp init: hide: setPri: 1)
		
		(lightningUp init: hide: setPri: 1)
		(lightningDown init: hide: setPri: 1)
		(lightningArcUp init: hide: setPri: 1)
		(lightningArcDown init: hide: setPri: 1)
		
		; these are additional lightning flows for when the lightning must show on the top and bottom of the board at the same time
		(extraLightningDown init: hide: setPri: 1)
		(extraLightningArcUp init: hide: setPri: 1)
		(extraLightningArcDown init: hide: setPri: 1)
		
		(movingPiece init: setPri: 7) ; shows when taking a piece (and the corresponding block is hidden)
		
		(infoButton init: hide:)	; shows or hides in doit method depending on gHardMode
		
		(squareSelectorLight init: setPri: 0)	; shows when using keyboard controls to show which square is currently being selected
		
		(= gArcStl 1)	; disable typing
		(= gMap 1)
	)
)

(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		; code executed each game cycle
		(if (not gHardMode)
			(infoButton show:)
		else
			(infoButton hide:)
		)
		
		;(if mouseControl
			(= myEvent (Event new: evNULL))	; create new event to determine where the cursor is
			(cond 
				((checkEvent myEvent (block1 nsLeft?) (block1 nsRight?) (block1 nsTop?) (block1 nsBottom?) block1))
				((checkEvent myEvent (block2 nsLeft?) (block2 nsRight?) (block2 nsTop?) (block2 nsBottom?) block2))
				((checkEvent myEvent (block3 nsLeft?) (block3 nsRight?) (block3 nsTop?) (block3 nsBottom?) block3))
				((checkEvent myEvent (block4 nsLeft?) (block4 nsRight?) (block4 nsTop?) (block4 nsBottom?) block4))
				((checkEvent myEvent (block5 nsLeft?) (block5 nsRight?) (block5 nsTop?) (block5 nsBottom?) block5))
				((checkEvent myEvent (block6 nsLeft?) (block6 nsRight?) (block6 nsTop?) (block6 nsBottom?) block6))
				((checkEvent myEvent (block7 nsLeft?) (block7 nsRight?) (block7 nsTop?) (block7 nsBottom?) block7))
				((checkEvent myEvent (block8 nsLeft?) (block8 nsRight?) (block8 nsTop?) (block8 nsBottom?) block8))
				((checkEvent myEvent (block9 nsLeft?) (block9 nsRight?) (block9 nsTop?) (block9 nsBottom?) block9))
				(else
					(gGame setCursor: 999)	; Set back to default cursor when not over a block
					(= cursorOver 0)
				)
			)		
			(gEgo posn: (myEvent x?) (myEvent y?))
	
			(if blockHeld
				(movingPiece loop: blockHeld posn: (myEvent x?)(myEvent y?))	
			else
				(movingPiece loop: 0)
			)
			
			(myEvent dispose:)	; dispose the event so heap is not building up
		;)
		
		; determine which square the cursor is over
		;(if (not handPosition)
			(cond
				( (== (gEgo onControl:) ctlNAVY)
					(= boardNumberSelect 1)
					(squareSelectorLight posn: 108 62 cel: 1)
				)
				( (== (gEgo onControl:) ctlGREEN)
					(= boardNumberSelect 2)
					(squareSelectorLight posn: 160 62 cel: 1)
				)
				( (== (gEgo onControl:) ctlTEAL)
					(= boardNumberSelect 3)
					(squareSelectorLight posn: 211 62 cel: 1)
				)
				( (== (gEgo onControl:) ctlMAROON)
					(= boardNumberSelect 4)
					(squareSelectorLight posn: 108 113 cel: 1)
				)
				( (== (gEgo onControl:) ctlPURPLE)
					(= boardNumberSelect 5)
					(squareSelectorLight posn: 160 113 cel: 1)
				)
				( (== (gEgo onControl:) ctlBROWN)
					(= boardNumberSelect 6)
					(squareSelectorLight posn: 211 113 cel: 1)
				)
				( (== (gEgo onControl:) ctlSILVER)
					(= boardNumberSelect 7)
					(squareSelectorLight posn: 108 165 cel: 1)
				)
				( (== (gEgo onControl:) ctlGREY)
					(= boardNumberSelect 8)
					(squareSelectorLight posn: 160 165 cel: 1)
				)
				( (== (gEgo onControl:) ctlBLUE)
					(= boardNumberSelect 9)
					(squareSelectorLight posn: 211 165 cel: 1)
				)
				(else
					(= boardNumberSelect 0)
					(squareSelectorLight cel: 0)
				)		
			)
		;)
		
		;(if mouseControl
			(squareSelectorLight hide:)
		;else
		;	(squareSelectorLight show:)	; illustrate what you can do with keyboard controls
		;	(gGame setCursor: 998) ; invisible cursor
		;	(if blockHeld
		;		(movingPiece loop: blockHeld posn: (squareSelectorLight x?)(squareSelectorLight y?))	
		;	else
		;		(movingPiece loop: 0)
		;	)	
		;)
		;(if handPosition
		;	(squareSelectorLight view: 587 setPri: 10)	; hand icon
			;(= boardNumberSelect 0)
		;else
		;	(if (not blockHeld)
		;		(squareSelectorLight view: 588 setPri: 0)
		;	)	
		;)
		
	)
	
	(method (handleEvent pEvent &tmp i)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if mouseControl
				(if (and
						(> (pEvent x?) (exitButton nsLeft?))
						(< (pEvent x?) (exitButton nsRight?))
						(> (pEvent y?) (+ (exitButton nsTop?) 0))
						(< (pEvent y?) (+ (exitButton nsBottom?) 8))
					)
					(= gMap 0)
					(= gArcStl 0)
					(gRoom newRoom: 50)
				)
				(if blockHeld
					(placePiece)			
				else
					(if cursorOver	; if over a puzzle piece
						(= blockHeld cursorOver)	; take the piece
						(hideBlock)
						; if the player picked up a piece that was already on the board
						(for ( (= i 0)) (< i 10)  ( (++ i))
							(if (== boardNumberSelect i)
								(= [squareFilled (- i 1)] 0)
							)	
						)
					)
				)
			)
		)
	)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0 ; Handle state changes
			)
			(1
				(lightningComp show: setCycle: End RoomScript cycleSpeed: 2)
			)
			(2
				(= cycles 8)
				(lightningComp loop: 15 setCycle: Fwd)
			)
			(3
				(= cycles 10)
				(Print 110 2)
				(= g110Solved 1)
				(gGame changeScore: 4)
				(= gInt (+ gInt 3))
				(= gArcStl 0)
				(= gMap 0)
			)
			(4 (gRoom newRoom: 50)
			)
		)
	)
)

(procedure (checkEvent pEvent x1 x2 y1 y2 view)
	(if
		(and
			(> (pEvent x?) x1)
			(< (pEvent x?) x2)
			(> (pEvent y?) (+ y1 8))
			(< (pEvent y?) (+ y2 8))
		)
		(gGame setCursor: 991)
		(= cursorOver (view loop?))
		(return TRUE)
	else
		(return FALSE)
	)
)
(procedure (SurroundingSquareCheck num)
	(if [squareFilled num]	; if the block is filled, check to see which block is in it, then assign variables to its various arrows (1 for out, 2 for in) 
		(if (== [squareFilled num] 1)
			(= [SurroundingBlockPotential 2] 1)
			(= [SurroundingBlockPotential 4] 2)				
		)
		(if (== [squareFilled num] 2)
			(= [SurroundingBlockPotential 3] 1)
			(= [SurroundingBlockPotential 4] 2)
			(= [SurroundingBlockPotential 5] 1)		
		)		
		(if (== [squareFilled num] 3)
			(= [SurroundingBlockPotential 0] 1)
			(= [SurroundingBlockPotential 4] 2)	
		)
		(if (== [squareFilled num] 4)
			(= [SurroundingBlockPotential 1] 1)
			(= [SurroundingBlockPotential 2] 2)
			(= [SurroundingBlockPotential 4] 1)	
		)
		(if (== [squareFilled num] 5)
			(= [SurroundingBlockPotential 1] 1)
			(= [SurroundingBlockPotential 3] 2)
			(= [SurroundingBlockPotential 4] 2)
			(= [SurroundingBlockPotential 5] 2)	
		)
		(if (== [squareFilled num] 6)
			(= [SurroundingBlockPotential 0] 2)
			(= [SurroundingBlockPotential 1] 1)
			(= [SurroundingBlockPotential 4] 1)	
		)
		(if (== [squareFilled num] 7)
			(= [SurroundingBlockPotential 1] 2)
			(= [SurroundingBlockPotential 2] 1)	
		)
		(if (== [squareFilled num] 8)
			(= [SurroundingBlockPotential 1] 1)
			(= [SurroundingBlockPotential 3] 2)
			(= [SurroundingBlockPotential 5] 2)	
		)	
		(if (== [squareFilled num] 9)
			(= [SurroundingBlockPotential 0] 1)
			(= [SurroundingBlockPotential 1] 2)
		)
	)
	; Check the potentials against each other before rewriting these variables (then assignment lightning)
	; LIGHTNING UP and LEFT			
	(if (== (+ [blockPlacedPotential 0] [SurroundingBlockPotential 5]) 3)	; input 1 + output 2 matching = 3
		(if (or (== (+ boardNumberSelect 5) (+ num 1)) 
				(and (== (- boardNumberSelect 4) (+ num 1)) (or (== boardNumberSelect 6) (== boardNumberSelect 9)))
			)
			(lightningArcDown show: posn: (- (squareSelectorLight x?) 20) (- (squareSelectorLight y?) 18) cel: 0 setCycle: End)
			(if
				(== (+ boardNumberSelect 5) (+ num 1))	; if jumping across from top to bottom posn lightning at bottom
				(extraLightningArcDown show: posn: 187 195 cel: 0 setCycle: End)	
			)
		)
	)
	; LIGHTNING UP
	(if (== (+ [blockPlacedPotential 1] [SurroundingBlockPotential 4]) 3)
		(if (or (== (- boardNumberSelect 3) (+ num 1))
				(== (+ boardNumberSelect 6) (+ num 1))
			)
			(lightningUp show: posn: (squareSelectorLight x?)(- (squareSelectorLight y?) 34) cel: 0 setCycle: End)
			(if (== (+ boardNumberSelect 6) (+ num 1))
				(extraLightningDown show: posn: (squareSelectorLight x?)(+ (squareSelectorLight y?) 115) cel: 0 setCycle: End)
			)
		)
	)
	; LIGHTNING UP and RIGHT	
	(if (== (+ [blockPlacedPotential 2] [SurroundingBlockPotential 3]) 3)
		(if (or (== (- boardNumberSelect 2) (+ num 1))
				(== (+ boardNumberSelect 7) (+ num 1))
				(and (== (+ boardNumberSelect 4) (+ num 1)) (== boardNumberSelect 3)) ; across diagonal
			)	
			(lightningArcUp show: posn: (+ (squareSelectorLight x?) 20) (- (squareSelectorLight y?) 18) cel: 0 setCycle: End)
			(if (== (+ boardNumberSelect 7) (+ num 1))
				(extraLightningArcUp show: posn: 135 195 cel: 0 setCycle: End)		
			)
			(if (== (+ boardNumberSelect 4) (+ num 1)) ; across diagonal
				(extraLightningArcUp show: posn: 83 195 cel: 0 setCycle: End)			
			)
		)
	)
	; LIGHTNING DOWN and LEFT	
	(if (== (+ [blockPlacedPotential 3] [SurroundingBlockPotential 2]) 3)
		(if (or (== (+ boardNumberSelect 2) (+ num 1))
				(== (- boardNumberSelect 7) (+ num 1))
				(and (== (- boardNumberSelect 4) (+ num 1)) (== boardNumberSelect 7)) ; across diagonal
			)
			(lightningArcUp show: posn: (- (squareSelectorLight x?) 20) (+ (squareSelectorLight y?) 23) cel: 0 setCycle: End)
			(if (== (- boardNumberSelect 4) (+ num 1)) ; across diagonal
				(extraLightningArcUp show: posn: 236 40 cel: 0 setCycle: End)			
			)
			(if (== (- boardNumberSelect 7) (+ num 1))
				(extraLightningArcUp show: posn: 133 40 cel: 0 setCycle: End)		
			)
		)
	)
	; LIGHTNING DOWN	
	(if (== (+ [blockPlacedPotential 4] [SurroundingBlockPotential 1]) 3)
		(if (or (== (+ boardNumberSelect 3) (+ num 1))
				(== (- boardNumberSelect 6) (+ num 1))
			)	
			(lightningDown show: posn: (squareSelectorLight x?) (+ (squareSelectorLight y?) 20) cel: 0 setCycle: End)
			(if (== (- boardNumberSelect 6) (+ num 1))	
				(extraLightningDown show: posn: (squareSelectorLight x?)(- (squareSelectorLight y?) 125) cel: 0 setCycle: End)	
			)
		)
	)
	; LIGHTNING DOWN and RIGHT	
	(if (== (+ [blockPlacedPotential 5] [SurroundingBlockPotential 0]) 3)
		(if (or (== (- boardNumberSelect 5) (+ num 1)) 
				(and (== (+ boardNumberSelect 4) (+ num 1)) (or (== boardNumberSelect 2) (== boardNumberSelect 5)))
			)
			(lightningArcDown show: posn: (+ (squareSelectorLight x?) 20) (+ (squareSelectorLight y?) 23) cel: 0 setCycle: End)
			(if (== (- boardNumberSelect 5) (+ num 1))
				(extraLightningArcDown show: posn: 185 40 cel: 0 setCycle: End)	
			)
		)
	)
	;(FormatPrint {BP %u SP %u\nBP %u SP %u\nBP %u SP %u\nBP %u SP %u\nBP %u SP %u\nBP %u SP %u\n}
	;	[blockPlacedPotential 0] [SurroundingBlockPotential 0]
	;	[blockPlacedPotential 1] [SurroundingBlockPotential 1]
	;	[blockPlacedPotential 2] [SurroundingBlockPotential 2]
	;	[blockPlacedPotential 3] [SurroundingBlockPotential 3]
	;	[blockPlacedPotential 4] [SurroundingBlockPotential 4]
	;	[blockPlacedPotential 5] [SurroundingBlockPotential 5]
	;	)
	; set all the surrounding block potential variables back to zero
	(= [SurroundingBlockPotential 0] 0)
	(= [SurroundingBlockPotential 1] 0)
	(= [SurroundingBlockPotential 2] 0)
	(= [SurroundingBlockPotential 3] 0)
	(= [SurroundingBlockPotential 4] 0)
	(= [SurroundingBlockPotential 5] 0)	
)

(procedure (currentCheck)	; checks all surrounding blocks for variables meeting specific requirements for lightning
	(switch boardNumberSelect
		(1
			(SurroundingSquareCheck 3)
			(SurroundingSquareCheck 6)
			(SurroundingSquareCheck 7)
		)
		(2
			(SurroundingSquareCheck 3)
			(SurroundingSquareCheck 4)
			(SurroundingSquareCheck 5)		
		)
		(3
			(SurroundingSquareCheck 5)
			(SurroundingSquareCheck 6)
			(SurroundingSquareCheck 7)
			(SurroundingSquareCheck 8)	
		)
		(4
			(SurroundingSquareCheck 0)
			(SurroundingSquareCheck 1)
			(SurroundingSquareCheck 6)	
		)
		(5
			(SurroundingSquareCheck 1)
			(SurroundingSquareCheck 6)
			(SurroundingSquareCheck 7)
			(SurroundingSquareCheck 8)		
		)
		(6
			(SurroundingSquareCheck 1)
			(SurroundingSquareCheck 2)
			(SurroundingSquareCheck 8)		
		)
		(7
			(SurroundingSquareCheck 0)
			(SurroundingSquareCheck 2)
			(SurroundingSquareCheck 3)
			(SurroundingSquareCheck 4)		
		)
		(8
			(SurroundingSquareCheck 0)
			(SurroundingSquareCheck 2)
			(SurroundingSquareCheck 4)	
		)
		(9
			(SurroundingSquareCheck 2)
			(SurroundingSquareCheck 4)
			(SurroundingSquareCheck 5)	
		)	
	)
)
(procedure (placePiece &tmp i ii x y)
	(for ( (= i 0)) (< i 10)  ( (++ i))
		(if (== boardNumberSelect i)	; determine where on the board we're placing the block
			(switch boardNumberSelect	; set the x and y value of the where the block will eventually go
				(1 
					(= x 110)(= y 55)
					(squareReplacementCheck 0)
				)
				(2 
					(= x 160)(= y 55)
					(squareReplacementCheck 1)
				)
				(3 
					(= x 210)(= y 55)
					(squareReplacementCheck 2)
				)
				(4 
					(= x 110)(= y 105)
					(squareReplacementCheck 3)
				)
				(5 
					(= x 160)(= y 105)
					(squareReplacementCheck 4)
				)
				(6 
					(= x 210)(= y 105)
					(squareReplacementCheck 5)
				)
				(7 
					(= x 110)(= y 155)
					(squareReplacementCheck 6)
				)
				(8 
					(= x 160)(= y 155)
					(squareReplacementCheck 7)
				)
				(9 
					(= x 210)(= y 155)
					(squareReplacementCheck 8)
				)
				(0	; if not on the board, place the piece back
					(for ( (= ii 0)) (< ii 10)  ( (++ ii))
						(if (== blockHeld ii)	; determine which block we're placing
							(switch blockHeld	; put the block back to their original puzzle starting points
								(1
									(block1 show: posn: 260 105)									
								)
								(2
									(block2 show: posn: 300 105)
								)
								(3
									(block3 show: posn: 260 75)		
								)
								(4
									(block4 show: posn: 260 135)	
								)
								(5
									(block5 show: posn: 300 135)		
								)
								(6
									(block6 show: posn: 260 165)
								)
								(7
									(block7 show: posn: 300 45)	
								)
								(8
									(block8 show: posn: 300 75)
								)
								(9
									(block9 show: posn: 260 45)	
								)
							)
						)
					)	
					(= blockHeld 0)	; block held is set to 0 as it's either placed or replaced
					(return) 	
				)
	
			)
			(if (not swapping)	; if not swapping two blocks, this sets the blockSwapped variable to that of the currently held block to be placed.
				(= blockInput blockHeld)	
			)
			(for ( (= ii 0)) (< ii 10)  ( (++ ii))
				(if (== blockInput ii)	; determine which block we're placing
					(switch blockInput
						(1
							(block1 show: posn: x y)
							(= [blockPlacedPotential 2] 1)
							(= [blockPlacedPotential 4] 2)	
						)
						(2
							(block2 show: posn: x y)
							(= [blockPlacedPotential 3] 1)
							(= [blockPlacedPotential 4] 2)
							(= [blockPlacedPotential 5] 1)		
						)
						(3
							(block3 show: posn: x y)
							(= [blockPlacedPotential 0] 1)
							(= [blockPlacedPotential 4] 2)	
						)
						(4
							(block4 show: posn: x y)
							(= [blockPlacedPotential 1] 1)
							(= [blockPlacedPotential 2] 2)
							(= [blockPlacedPotential 4] 1)	
						)
						(5
							(block5 show: posn: x y)
							(= [blockPlacedPotential 1] 1)
							(= [blockPlacedPotential 3] 2)
							(= [blockPlacedPotential 4] 2)
							(= [blockPlacedPotential 5] 2)	
						)
						(6
							(block6 show: posn: x y)
							(= [blockPlacedPotential 0] 2)
							(= [blockPlacedPotential 1] 1)
							(= [blockPlacedPotential 4] 1)		
						)
						(7
							(block7 show: posn: x y)
							(= [blockPlacedPotential 1] 2)
							(= [blockPlacedPotential 2] 1)		
						)
						(8
							(block8 show: posn: x y)
							(= [blockPlacedPotential 1] 1)
							(= [blockPlacedPotential 3] 2)
							(= [blockPlacedPotential 5] 2)		
						)
						(9
							(block9 show: posn: x y)
							(= [blockPlacedPotential 0] 1)
							(= [blockPlacedPotential 1] 2)		
						)						
					)
					(if swapping
						(= blockInput 0)
						(= swapping 0)	
					else
						(= blockHeld 0)
						(= blockInput 0)
					)	
				)						
			)
		)
	)
	(solutionCheck)
	(if (not solved)
		(currentCheck)	
	)
	; return all variables to zero
	(= [blockPlacedPotential 0] 0)
	(= [blockPlacedPotential 1] 0)
	(= [blockPlacedPotential 2] 0)
	(= [blockPlacedPotential 3] 0)
	(= [blockPlacedPotential 4] 0)
	(= [blockPlacedPotential 5] 0)	
)

(procedure (solutionCheck &tmp i scoreUp)
	(for ( (= i 0)) (< i 10)  ( (++ i))
		(if (== [squareFilled i] (+ i 1))
			(++ scoreUp)
		)
		(if (== scoreUp 9)
			;(Print {You win!})
			(RoomScript changeState: 1)
			; make sure all lightning images are gone as well to show final circuit working
			(lightningUp hide:)
			(lightningDown hide:)
			(lightningArcUp hide:)
			(lightningArcDown hide:)
			(extraLightningDown hide:)
			(extraLightningArcDown hide:)
			(extraLightningArcUp hide:)
			(return)
		)		
	)
	(= scoreUp 0)		
)
(procedure (squareReplacementCheck num)
	(if [squareFilled num]
		(= swapping 1)
		(= blockInput blockHeld)
		(= blockHeld [squareFilled num])
		(= [squareFilled num] blockInput)
		(switch blockHeld
			(1
				(block1 hide:)
			)
			(2
				(block2 hide:)
			)
			(3
				(block3 hide:)
			)
			(4
				(block4 hide:)
			)
			(5
				(block5 hide:)
			)
			(6
				(block6 hide:)
			)
			(7
				(block7 hide:)
			)
			(8
				(block8 hide:)
			)
			(9
				(block9 hide:)
			)
			
		)
	else
		(= [squareFilled num] blockHeld)
	)
)
(procedure (hideBlock)
	(switch blockHeld
		(1
			(block1 hide:)
		)
		(2
			(block2 hide:)
		)
		(3
			(block3 hide:)
		)
		(4
			(block4 hide:)
		)
		(5
			(block5 hide:)
		)
		(6
			(block6 hide:)
		)
		(7
			(block7 hide:)
		)
		(8
			(block8 hide:)
		)
		(9
			(block9 hide:)
		)
	)	
)
(instance exitButton of Prop
	(properties         
		y 174
		x 26
		view 590
	)
)
(instance block1 of Prop
	(properties     ; "Block One"
		y 105
		x 260
		view 588
		loop 1
	)
)

(instance block2 of Prop
	(properties       ; ...etc
		y 105
		x 300
		view 588
		loop 2
	)
)

(instance block3 of Prop
	(properties
		y 75
		x 260
		view 588
		loop 3
	)
)

(instance block4 of Prop
	(properties
		y 135
		x 260
		view 588
		loop 4
	)
)

(instance block5 of Prop
	(properties
		y 135
		x 300
		view 588
		loop 5
	)
)

(instance block6 of Prop
	(properties
		y 165
		x 260
		view 588
		loop 6
	)
)

(instance block7 of Prop
	(properties
		y 45
		x 300
		view 588
		loop 7
	)
)

(instance block8 of Prop
	(properties
		y 75
		x 300
		view 588
		loop 8
	)
)

(instance block9 of Prop
	(properties
		y 45
		x 260
		view 588
		loop 9
	)
)

(instance movingPiece of Prop	; block used for moving that can be all different blocks
	(properties
		y 45
		x 260
		view 588
	)
)

(instance infoButton of Prop
	(properties
		y 162
		x 299
		view 998
		loop 7                          
	)
)
(instance squareSelectorLight of Act
	(properties
		y 55
		x 110
		view 588
		loop 0                        
	)
)
(instance lightningComp of Act
	(properties     
		y 175
		x 160
		view 581
		loop 14
	)
)
(instance lightningUp of Prop
	(properties         
		y 175
		x 160
		view 588
		loop 10
	)
)
(instance lightningDown of Prop
	(properties         
		y 175
		x 160
		view 588
		loop 10
	)
)
(instance lightningArcUp of Prop
	(properties         
		y 175
		x 160
		view 588
		loop 11
	)
)
(instance lightningArcDown of Prop
	(properties         
		y 175
		x 160
		view 588
		loop 12
	)
)
(instance extraLightningDown of Prop
	(properties         
		y 175
		x 160
		view 588
		loop 10
	)
)
(instance extraLightningArcUp of Prop
	(properties         
		y 175
		x 160
		view 588
		loop 11
	)
)
(instance extraLightningArcDown of Prop
	(properties         
		y 175
		x 160
		view 588
		loop 12
	)
)
