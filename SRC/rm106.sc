;;; Sierra Script 1.0 - (do not remove this comment)

(script# 106)
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
	rm106 0
)

(local
; 3D CHESS/KNIGHT PUZZLE
; BY RYAN SLATTERY



	faceDark =  1   ; determines how to move the board ("dark" because it has more dark squares)
	faceNumber =  1 ; numbers the sides of the board (1-4 sides of the board)
	currentFaceNumber =  1
	; rank = 1
	; file = 1
	; knightMoveLoopNumber
	knightMoveLeft =  0 ; the Knight has only two possible moves from any one point...left
	knightMoveRight =  0 ; ... and right
	moveOnce =  0
	kingFace =  4
	kingCaptured =  0
	clicked =  0
	turning =  0
	textShowing =  0
	htext
	myEvent
	knightSquare =  1
	knightSquareVar
	rBlock =  0
	gBlock =  0
	bBlock =  0
	h7 =  0
	h8 =  0
	h9 =  0
	h4 =  0
	h5 =  0
	h6 =  0
	h1 =  0
	h2 =  0
	h3 =  0
)

(instance rm106 of Rm
	(properties
		picture scriptNumber
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(board init: setScript: turnBoard)
		(knight init: setPri: 15 xStep: 4 yStep: 4 ignoreActors:)
		(king
			init:
			setPri: 15
			xStep: 8
			yStep: 8
			hide:
			ignoreActors:
		)
		(arrowUp init:)
		(arrowDown init:)
		(reset init:)
		(green init:)
		(blue init:)
		(red init:)
		(cancel init:)
		(numberSide init: cel: 1)
		(holes init: setPri: 14 setScript: downHole)
		(Exit init:)
		(Display
			{Click knight to move.}
			dsCOORD
			119
			130
			dsCOLOUR
			9
			dsALIGN
			alCENTER
			dsBACKGROUND
			clTRANSPARENT
			dsWIDTH
			60
			dsFONT
			4
		)
		(Display
			{The face of the board:}
			dsCOORD
			30
			20
			dsCOLOUR
			9
			dsALIGN
			alCENTER
			dsBACKGROUND
			clTRANSPARENT
			dsWIDTH
			60
			dsFONT
			4
		)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (doit)
		(super doit:)
		(if (not clicked)
			(green cel: 0)
			(red cel: 0)
			(blue cel: 0)
			(cancel cel: 0)
			(knightMove1 hide:)
			(knightMove2 hide:)
		)
		(if (== faceNumber 0)
			(numberSide cel: 1)
			(= faceNumber 1)
		)
		(if (== currentFaceNumber faceNumber)
			(knight show:)
		else
			(knight hide:)
		)
		(cond 
			((== faceNumber kingFace)
				(if (not kingCaptured)
					(king show: setMotion: MoveTo 164 84)
				else
					(king hide:)
				)
			)
			((== 3 faceNumber) (king show: setMotion: MoveTo 184 52))
			(else (king hide:))
		)
		(if (not turning)
			(holes cel: faceNumber show:)
		else
			(holes hide:)
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if
				(and
					(> (pEvent x?) (knight nsLeft?))
					(< (pEvent x?) (knight nsRight?))
					(> (pEvent y?) (knight nsTop?))
					(< (pEvent y?) (knight nsBottom?))
				)
				(if (== currentFaceNumber faceNumber)
					(setUpMoves)
					(= clicked 1)
					(knightMove1 init: show: setPri: 14 ignoreActors:)
					(knightMove2 init: show: setPri: 14 ignoreActors:)
				)
			)
		)
		(if
			(and
				(> (pEvent x?) (arrowUp nsLeft?))
				(< (pEvent x?) (arrowUp nsRight?))
				(> (pEvent y?) (arrowUp nsTop?))
				(< (pEvent y?) (arrowUp nsBottom?))
			)
			(cond 
				(clicked (Print 106 0))
				((> faceNumber 1)
					(if (not (== currentFaceNumber faceNumber))
						(= turning 1)
						(turnBoard changeState: 4)
					)
				)
			)
		)
		(if
			(and
				(> (pEvent x?) (reset nsLeft?))
				(< (pEvent x?) (reset nsRight?))
				(> (pEvent y?) (reset nsTop?))
				(< (pEvent y?) (reset nsBottom?))
			)
			(resetBoard)
		)
		(if
			(and
				(> (pEvent x?) (Exit nsLeft?))
				(< (pEvent x?) (Exit nsRight?))
				(> (pEvent y?) (Exit nsTop?))
				(< (pEvent y?) (Exit nsBottom?))
			)
			(Print 106 1)
		)
; Goodbye
		(if
			(and
				(> (pEvent x?) (arrowDown nsLeft?))
				(< (pEvent x?) (arrowDown nsRight?))
				(> (pEvent y?) (arrowDown nsTop?))
				(< (pEvent y?) (arrowDown nsBottom?))
			)
			(cond 
				(clicked (Print 106 0))
				((< faceNumber 4) (= turning 1) (turnBoard changeState: 2))
			)
		)
		(if
			(and
				(> (pEvent x?) (knightMove1 nsLeft?))
				(< (pEvent x?) (knightMove1 nsRight?))
				(> (pEvent y?) (knightMove1 nsTop?))
				(< (pEvent y?) (knightMove1 nsBottom?))
			)
			(if clicked
				(cond 
					((== gBlock 1) (moveToGreen))
					((== bBlock 1) (moveToBlue))
				)
			)
		)
		(if
			(and
				(> (pEvent x?) (knightMove2 nsLeft?))
				(< (pEvent x?) (knightMove2 nsRight?))
				(> (pEvent y?) (knightMove2 nsTop?))
				(< (pEvent y?) (knightMove2 nsBottom?))
			)
			(if clicked
				(cond 
					((== rBlock 1) (moveToRed))
					((== bBlock 1) (moveToBlue))
				)
			)
		)
		(if
			(and
				(> (pEvent x?) (red nsLeft?))
				(< (pEvent x?) (red nsRight?))
				(> (pEvent y?) (red nsTop?))
				(< (pEvent y?) (red nsBottom?))
			)
			(if clicked (moveToRed))
		)
		; (knightMoves:hide())
		(if
			(and
				(> (pEvent x?) (green nsLeft?))
				(< (pEvent x?) (green nsRight?))
				(> (pEvent y?) (green nsTop?))
				(< (pEvent y?) (green nsBottom?))
			)
			(if clicked (moveToGreen))
		)
		; (knightMoves:hide())
		(if
			(and
				(> (pEvent x?) (blue nsLeft?))
				(< (pEvent x?) (blue nsRight?))
				(> (pEvent y?) (blue nsTop?))
				(< (pEvent y?) (blue nsBottom?))
			)
			(if clicked (moveToBlue))
		)
		; (knightMoves:hide())
		(if
			(and
				(> (pEvent x?) (cancel nsLeft?))
				(< (pEvent x?) (cancel nsRight?))
				(> (pEvent y?) (cancel nsTop?))
				(< (pEvent y?) (cancel nsBottom?))
			)
			(if clicked (resetMoves))
		)
	)
)

(instance turnBoard of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(2
				(board setCycle: End turnBoard)
				(= turning 1)
			)
			(3
				(= turning 0)
				(faceNumberPlus)
				(numberSide cel: faceNumber)
				(if faceDark
					(board loop: 1 cel: 0)
					(= faceDark 0)
				else
					(board loop: 0 cel: 0)
					(= faceDark 1)
				)
			)
			(4
				(if faceDark
					(board loop: 1 setCycle: Beg turnBoard)
				else
					(board loop: 0 setCycle: Beg turnBoard)
				)
				(= turning 1)
			)
			(5
				(= turning 0)
				(faceNumberMinus)
				(numberSide cel: faceNumber)
				(if faceDark
					(board loop: 1 cel: 0)
					(= faceDark 0)
				else
					(board loop: 0 cel: 0)
					(= faceDark 1)
				)
			)
		)
	)
)

(instance downHole of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(2
				(= seconds 3)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(ProgramControl)
			)
			(3
				(Print 106 2)
; Uh oh
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(PlayerControl)
				(resetBoard)
			)
			(5 (= seconds 2))
			(6
				(= seconds 2)
				(SetCursor 997 (HaveMouse))
				(= gCurrentCursor 997)
				(= kingCaptured 1)
			)
			(7
				(SetCursor 999 (HaveMouse))
				(= gCurrentCursor 999)
				(Print 106 3)
			)
		)
	)
)

; You did it! Are you ready for a tougher challenge?
(procedure (resetBoard)
	(if (> faceNumber 1)
		(board loop: 0 setCycle: Beg turnBoard)
	)
	(= clicked 0)
	(= faceNumber 1)
	(= currentFaceNumber 1)
	(= knightSquare 1)
	(knight posn: 132 116 show:)
	(numberSide cel: 1)
	(resetMoves)
)

(procedure (faceNumberPlus)
	(++ faceNumber)
	(if (== faceNumber 5) (= faceNumber 1))
)

(procedure (faceNumberMinus)
	(-- faceNumber)
	(if (== faceNumber 5) (= faceNumber 1))
)

; (procedure (KMLNCalc)
;    (if (== rank 1)
;        = knightMoveLoopNumber (* rank file)
;    )(else
;        (if (== rank 2)
;            = knightMoveLoopNumber (+(+ rank file)1)
;        )(else
;            (if (== rank 3)
;                = knightMoveLoopNumber (+(+ rank file)3)
;            )
;        )
;    )
; )
(procedure (setUpMoves)
	(switch knightSquare
		(1
			(knightMove1 posn: 148 85 loop: 1)
			(knightMove2 posn: 164 101 loop: 2)
			(blue cel: 1)
			(red cel: 1)
			(cancel cel: 1)
			(= bBlock 1)
			(= rBlock 1)
		)
		(2
			(knightMove1 posn: 132 85 loop: 0)
			(knightMove2 posn: 164 85 loop: 2)
			(green cel: 1)
			(red cel: 1)
			(cancel cel: 1)
			(= gBlock 1)
			(= rBlock 1)
		)
		(3
			(knightMove1 posn: 132 101 loop: 0)
			(knightMove2 posn: 148 85 loop: 1)
			(green cel: 1)
			(blue cel: 1)
			(cancel cel: 1)
			(= gBlock 1)
			(= bBlock 1)
		)
		(4
			(knightMove1 posn: 153 68 loop: 4)
			(knightMove2 posn: 164 85 loop: 2)
			(blue cel: 1)
			(red cel: 1)
			(cancel cel: 1)
			(= bBlock 1)
			(= rBlock 1)
		)
		(5
			(knightMove1 posn: 138 68 loop: 3)
			(knightMove2 posn: 168 68 loop: 5)
			(green cel: 1)
			(red cel: 1)
			(cancel cel: 1)
			(= gBlock 1)
			(= rBlock 1)
		)
		(6
			(knightMove1 posn: 132 85 loop: 0)
			(knightMove2 posn: 154 68 loop: 4)
			(green cel: 1)
			(blue cel: 1)
			(cancel cel: 1)
			(= gBlock 1)
			(= bBlock 1)
		)
		(7
			(knightMove1 posn: 162 59 loop: 7)
			(knightMove2 posn: 168 68 loop: 5)
			(blue cel: 1)
			(red cel: 1)
			(cancel cel: 1)
			(= bBlock 1)
			(= rBlock 1)
		)
		(8
			(knightMove1 posn: 148 59 loop: 6)
			(knightMove2 posn: 177 59 loop: 8)
			(green cel: 1)
			(red cel: 1)
			(cancel cel: 1)
			(= gBlock 1)
			(= rBlock 1)
		)
		(9
			(knightMove1 posn: 138 68 loop: 3)
			(knightMove2 posn: 162 59 loop: 7)
			(green cel: 1)
			(blue cel: 1)
			(cancel cel: 1)
			(= gBlock 1)
			(= bBlock 1)
		)
	)
)

; (procedure(moveToSquare)
;    (switch(knightSquare)
;        (case 1
;            (if(moveOnce)
;                (if (knightMoveLeft)
;                    (knight:setMotion(MoveTo 148 84))
;                    resetMoves() = moveOnce 0 = knightSquareVar 8
;                )(else
;                    (if(knightMoveRight)
;                        (knight:setMotion(MoveTo 164 100))
;                        resetMoves() = moveOnce 0 = knightSquareVar 6
;                    )
;                )
;            )
;        )(case 2
;            (if(moveOnce)
;                (if (knightMoveLeft)
;                    (knight:setMotion(MoveTo 132 84))
;                    resetMoves() = moveOnce 0 = knightSquareVar 7
;                )(else
;                    (if(knightMoveRight)
;                        (knight:setMotion(MoveTo 164 84))
;                        resetMoves()  = moveOnce 0 = knightSquareVar 9
;                    )
;                )
;            )
;        )(case 3
;            (if(moveOnce)
;                (if (knightMoveLeft)
;                    (knight:setMotion(MoveTo 132 100))
;                    resetMoves() = moveOnce 0 = knightSquareVar 4
;                )(else
;                    (if(knightMoveRight)
;                        (knight:setMotion(MoveTo 148 84))
;                        resetMoves()  = moveOnce 0 = knightSquareVar 8
;                    )
;                )
;            )
;        )(case 4
;            (if(moveOnce)
;                (if (knightMoveLeft)
;                    (turnBoard:changeState(2)) ++ currentFaceNumber
;                    (knight:setMotion(MoveTo 148 116))
;                    resetMoves()  = moveOnce 0 = knightSquareVar 2
;                )(else
;                    (if(knightMoveRight)
;                        (knight:setMotion(MoveTo 164 84))
;                        resetMoves()  = moveOnce 0 = knightSquareVar 9
;                    )
;                )
;            )
;        )(case 5
;            (if(moveOnce)
;                (if (knightMoveLeft)
;                    (turnBoard:changeState(2)) ++ currentFaceNumber
;                    (knight:setMotion(MoveTo 132 116))
;                    resetMoves()  = moveOnce 0 = knightSquareVar 1
;                )(else
;                    (if(knightMoveRight)
;                        (turnBoard:changeState(2)) ++ currentFaceNumber
;                        (knight:setMotion(MoveTo 164 116))
;                        resetMoves()  = moveOnce 0 = knightSquareVar 3
;                    )
;                )
;            )
;        )(case 6
;            (if(moveOnce)
;                (if (knightMoveLeft)
;                    (knight:setMotion(MoveTo 132 84))
;                     resetMoves() = moveOnce 0 = knightSquareVar 7
;                )(else
;                    (if(knightMoveRight)
;                        (turnBoard:changeState(2)) ++ currentFaceNumber
;                        (knight:setMotion(MoveTo 148 116))
;                        resetMoves() = moveOnce 0  = knightSquareVar 2
;                    )
;                )
;            )
;        )(case 7
;            (if(moveOnce)
;                (if (knightMoveLeft)
;                    (turnBoard:changeState(2)) ++ currentFaceNumber
;                    (knight:setMotion(MoveTo 148 100))
;                     resetMoves() = moveOnce 0 = knightSquareVar 5
;                )(else
;                    (if(knightMoveRight)
;                        (turnBoard:changeState(2)) ++ currentFaceNumber
;                        (knight:setMotion(MoveTo 164 116))
;                        resetMoves()  = moveOnce 0 = knightSquareVar 3
;                    )
;                )
;            )
;        )(case 8
;            (if(moveOnce)
;                (if (knightMoveLeft)
;                    (turnBoard:changeState(2)) ++ currentFaceNumber
;                    (knight:setMotion(MoveTo 132 100))
;                    resetMoves() = moveOnce 0 = knightSquareVar 4
;                )(else
;                    (if(knightMoveRight)
;                        (turnBoard:changeState(2)) ++ currentFaceNumber
;                        (knight:setMotion(MoveTo 164 100))
;                         resetMoves() = moveOnce 0 = knightSquareVar 6
;                    )
;                )
;            )
;        )(case 9
;            (if(moveOnce)
;                (if (knightMoveLeft)
;                    (turnBoard:changeState(2)) ++ currentFaceNumber
;                    (knight:setMotion(MoveTo 132 116))
;                     resetMoves() = moveOnce 0 = knightSquareVar 1
;                )(else
;                    (if(knightMoveRight)
;                        (turnBoard:changeState(2)) ++ currentFaceNumber
;                        (knight:setMotion(MoveTo 148 100))
;                        resetMoves() = moveOnce 0 = knightSquareVar 5
;                    )
;                )
;            )
;        )
;    )
; )
(procedure (holeCheck)
	(switch currentFaceNumber
		(1
			(= h7 0)
			(= h8 0)
			(= h9 0)
			(= h4 4)
			(= h5 5)
			(= h6 0)
			(= h1 0)
			(= h2 2)
			(= h3 0)
			(if
				(or
					(== knightSquare h2)
					(== knightSquare h4)
					(== knightSquare h5)
				)
				(downHole changeState: 2)
			)
		)
		(2
			(= h7 7)
			(= h8 8)
			(= h9 0)
			(= h4 0)
			(= h5 5)
			(= h6 6)
			(= h1 0)
			(= h2 0)
			(= h3 3)
			(if
				(or
					(== knightSquare h3)
					(== knightSquare h5)
					(== knightSquare h6)
					(== knightSquare h7)
					(== knightSquare h8)
				)
				(downHole changeState: 2)
			)
		)
		(3
			(= h7 7)
			(= h8 0)
			(= h9 0)
			(= h4 0)
			(= h5 0)
			(= h6 6)
			(= h1 0)
			(= h2 2)
			(= h3 0)
			(if
				(or
					(== knightSquare h2)
					(== knightSquare h6)
					(== knightSquare h7)
				)
				(downHole changeState: 2)
			)
		)
		(4
			(= h7 0)
			(= h8 8)
			(= h9 9)
			(= h4 0)
			(= h5 5)
			(= h6 6)
			(= h1 0)
			(= h2 0)
			(= h3 0)
			(if
				(or
					(== knightSquare h5)
					(== knightSquare h6)
					(== knightSquare h8)
				)
				(downHole changeState: 2)
			)
			(if (== knightSquare h9) (downHole changeState: 5))
		)
	)
)

(procedure (moveToGreen)
	(switch knightSquare
		(2
			(knight setMotion: MoveTo 132 84)
			(= knightSquare 7)
		)
		(3
			(knight setMotion: MoveTo 132 100)
			(= knightSquare 4)
		)
		(5
			(turnBoard changeState: 2)
			(++ currentFaceNumber)
			(knight setMotion: MoveTo 132 116)
			(= knightSquare 1)
		)
		(6
			(knight setMotion: MoveTo 132 84)
			(= knightSquare 7)
		)
		(8
			(turnBoard changeState: 2)
			(++ currentFaceNumber)
			(knight setMotion: MoveTo 132 100)
			(= knightSquare 4)
		)
		(9
			(turnBoard changeState: 2)
			(++ currentFaceNumber)
			(knight setMotion: MoveTo 132 116)
			(= knightSquare 1)
		)
	)
	(holeCheck)
	(resetMoves)
)

(procedure (moveToBlue)
	(switch knightSquare
		(1
			(knight setMotion: MoveTo 148 84)
			(= knightSquare 8)
		)
		(3
			(knight setMotion: MoveTo 148 84)
			(= knightSquare 8)
		)
		(4
			(turnBoard changeState: 2)
			(++ currentFaceNumber)
			(knight setMotion: MoveTo 148 116)
			(= knightSquare 2)
		)
		(6
			(turnBoard changeState: 2)
			(++ currentFaceNumber)
			(knight setMotion: MoveTo 148 116)
			(= knightSquare 2)
		)
		(7
			(turnBoard changeState: 2)
			(++ currentFaceNumber)
			(knight setMotion: MoveTo 148 100)
			(= knightSquare 5)
		)
		(9
			(turnBoard changeState: 2)
			(++ currentFaceNumber)
			(knight setMotion: MoveTo 148 100)
			(= knightSquare 5)
		)
	)
	(holeCheck)
	(resetMoves)
)

(procedure (moveToRed)
	(switch knightSquare
		(1
			(knight setMotion: MoveTo 164 100)
			(= knightSquare 6)
		)
		(2
			(knight setMotion: MoveTo 164 84)
			(= knightSquare 9)
		)
		(4
			(knight setMotion: MoveTo 164 84)
			(= knightSquare 9)
		)
		(5
			(turnBoard changeState: 2)
			(++ currentFaceNumber)
			(knight setMotion: MoveTo 164 116)
			(= knightSquare 3)
		)
		(7
			(turnBoard changeState: 2)
			(++ currentFaceNumber)
			(knight setMotion: MoveTo 164 116)
			(= knightSquare 3)
		)
		(8
			(turnBoard changeState: 2)
			(++ currentFaceNumber)
			(knight setMotion: MoveTo 164 100)
			(= knightSquare 6)
		)
	)
	(holeCheck)
	(resetMoves)
)

(procedure (resetMoves)
	(= clicked 0)
	(green cel: 0)
	(red cel: 0)
	(blue cel: 0)
	(cancel cel: 0)
	(knightMove1 hide:)
	(knightMove2 hide:)
	(= gBlock 0)
	(= bBlock 0)
	(= rBlock 0)
	(= knightMoveLeft 0)
	(= knightMoveRight 0)
	(setUpMoves)
)

; = knightSquare knightSquareVar
; (switch(knightSquare)
;        (case 1
;            = rank 1 = file 1
;        )(case 2
;            = rank 1 = file 2
;        )(case 3
;            = rank 1 = file 3
;        )(case 4
;            = rank 2 = file 1
;        )(case 5
;            = rank 2 = file 2
;        )(case 6
;            = rank 2 = file 3
;        )(case 7
;            = rank 3 = file 1
;        )(case 8
;            = rank 3 = file 2
;        )(case 9
;            = rank 3 = file 3
;        )
;    )
(instance board of Prop
	(properties
		y 120
		x 160
		view 570
	)
)

(instance arrowUp of Prop
	(properties
		y 80
		x 257
		view 570
		loop 2
	)
)

(instance arrowDown of Prop
	(properties
		y 120
		x 257
		view 570
		loop 2
		cel 1
	)
)

(instance reset of Prop
	(properties
		y 142
		x 276
		view 570
		loop 5
	)
)

(instance knight of Act
	(properties
		y 116
		x 132
		view 571
	)
)

(instance king of Act
	(properties
		y 52
		x 184
		view 574
	)
)

(instance knightMove1 of Prop
	(properties
		y 101
		x 156
		view 575
	)
)

(instance knightMove2 of Prop
	(properties
		y 101
		x 196
		view 575
	)
)

(instance Exit of Prop
	(properties
		y 182
		x 218
		view 572
		loop 15
	)
)

(instance text of Prop
	(properties
		y 130
		x 156
		view 572
		loop 10
	)
)

(instance green of Prop
	(properties
		y 90
		x 50
		view 572
		loop 11
	)
)

(instance blue of Prop
	(properties
		y 110
		x 50
		view 572
		loop 12
	)
)

(instance red of Prop
	(properties
		y 130
		x 50
		view 572
		loop 13
	)
)

(instance cancel of Prop
	(properties
		y 150
		x 50
		view 572
		loop 14
	)
)

(instance numberSide of Prop
	(properties
		y 33
		x 96
		view 955
	)
)

(instance holes of Prop
	(properties
		y 120
		x 160
		view 570
		loop 3
	)
)
