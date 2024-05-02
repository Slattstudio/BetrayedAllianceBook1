;;; Sierra Script 1.0 - (do not remove this comment)
; + 2 SCORE  + 2 INT //
(script# 105)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use door)
(use feature)
(use game)
(use inv)
(use main)
(use obj)

(public
	rm105 0
)

(local

; CHESS BOARD PUZZLE
; BY RYAN SLATTERY



	myEvent
	choose =  1
	overQueen =  0
	overRook =  0
	overKing =  0
	overPawn1 =  0
	overPawn2 =  0
	overPawn3 =  0
	moveQueen =  0
	moveKing =  0
	moveRook =  0
	boardCoord =  0
	winning =  0
	mateNumber
)

(instance rm105 of Rm
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
		(= gArcStl 1)	; Adding this to remove text input if we're playing chess
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 150 130 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: hide:)
		(bKing init: setScript: variations)
		(wKing init: setScript: winningComb)
		(bQueen init:)
		(wQueen init:)
		(bRook init:)
		(wRook init:)
		(wPawn1 init:)
		; (wPawn2:init())
		(wPawn3 init:)
		(wPawn4 init:)
		(wPawn5 init:)
		(wPawn6 init:)
		(wPawn7 init:)
		(bPawn1 init:)
		(bPawn2 init:)
		(bPawn3 init:)
		(bPawn4 init:)
		(Exit init:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0 (= cycles 10) (= choose 0))
			(1 (= choose 1) (Print 105 22))
; Find the best move for black.
			(3 (= cycles 7))
			(4
				(= cycles 10)
				(wQueen setMotion: MoveTo 190 115)
			)
			(5
				(= cycles 10)
				(Print 105 23)
			)
; Checkmate!
			(6
				(= gArcStl 0)
				(gRoom newRoom: 44)
			)
		)
	)
	
	(method (handleEvent pEvent &tmp button)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if
				(and
					(> (pEvent x?) (Exit nsLeft?))
					(< (pEvent x?) (Exit nsRight?))
					(> (pEvent y?) (Exit nsTop?))
					(< (pEvent y?) (Exit nsBottom?))
				)
				(if (not winning) (= gArcStl 0) (gRoom newRoom: 44))
			)
			(cond 
				(choose
					(cond 
						(overQueen (takePiece) (= moveQueen 1))
						(overRook (takePiece) (= moveRook 1))
						(overKing (Print 105 2))
						(overPawn1 (Print 105 1))
						(overPawn2 (Print 105 1))
						(overPawn3 (Print 105 1))
					)
				)
				(moveQueen
					(switch boardCoord
						(25
							(= moveQueen 0)
							(gGame setCursor: 999)
							(bQueen setMotion: MoveTo 90 115)
							(selector hide:)
							(variations changeState: 1)
						)
						(34
							(= moveQueen 0)
							(gGame setCursor: 999)
							(bQueen setMotion: MoveTo 110 95)
							(selector hide:)
							(variations changeState: 1)
							(= mateNumber 1)
						)
						(43 (PrintOther 105 10) (wrongAnswer))
						(61 (PrintOther 105 11) (wrongAnswer))
						(59 (PrintOther 105 12) (wrongAnswer))
						(60 (PrintOther 105 13) (wrongAnswer)) ; show
						(44 (PrintOther 105 13) (wrongAnswer))
						(53 (PrintOther 105 10) (wrongAnswer))
						(45 (PrintOther 105 13) (wrongAnswer)) ; show
						(38 (PrintOther 105 14) (wrongAnswer)) ; show
						(31 (PrintOther 105 11) (wrongAnswer))
						(51 (PrintOther 105 15) (wrongAnswer))
						(0
							(= moveQueen 0)
							(= choose 1)
							(bQueen posn: 150 55)
							(gGame setCursor: 999)
						)
					)
				)
				(moveRook
					(switch boardCoord
						(33 (PrintOther 105 10) (wrongAnswer))
						(34 (PrintOther 105 16) (wrongAnswer))   ; show this one
						(35
							(= moveRook 0)
							(gGame setCursor: 999)
							(bRook setMotion: MoveTo 130 95)
							(selector hide:)
							(winningComb changeState: 1)
						)
						(37 (PrintOther 105 12) (wrongAnswer))
						(38 (PrintOther 105 10) (wrongAnswer))
						(39 (PrintOther 105 10) (wrongAnswer))
						(40 (PrintOther 105 12) (wrongAnswer))
						(44 (PrintOther 105 10) (wrongAnswer))
						(28 (PrintOther 105 12) (wrongAnswer))
						(0
							(= moveRook 0)
							(= choose 1)
							(bRook posn: 150 95)
							(gGame setCursor: 999)
						)
					)
				)
			)
		)
	)
	
	(method (doit)
		(super doit:)
		(if choose
			(= myEvent (Event new: evNULL))
			(cond 
				(
					(and
						(> (myEvent x?) (bQueen nsLeft?))
						(< (myEvent x?) (bQueen nsRight?))
						(> (myEvent y?) (+ (bQueen nsTop?) 8))
						(< (myEvent y?) (+ (bQueen nsBottom?) 8))
					)
					(selector init: show: posn: (bQueen x?) (bQueen y?))
					(= overQueen 1)
				)
				(
					(and
						(> (myEvent x?) (bRook nsLeft?))
						(< (myEvent x?) (bRook nsRight?))
						(> (myEvent y?) (+ (bRook nsTop?) 8))
						(< (myEvent y?) (+ (bRook nsBottom?) 8))
					)
					(selector init: show: posn: (bRook x?) (bRook y?))
					(= overRook 1)
				)
				(
					(and
						(> (myEvent x?) (bKing nsLeft?))
						(< (myEvent x?) (bKing nsRight?))
						(> (myEvent y?) (+ (bKing nsTop?) 8))
						(< (myEvent y?) (+ (bKing nsBottom?) 8))
					)
					(selector init: show: posn: (bKing x?) (bKing y?))
					(= overKing 1)
				)
				(
					(and
						(> (myEvent x?) (bPawn1 nsLeft?))
						(< (myEvent x?) (bPawn1 nsRight?))
						(> (myEvent y?) (+ (bPawn1 nsTop?) 8))
						(< (myEvent y?) (+ (bPawn1 nsBottom?) 8))
					)
					(selector init: show: posn: (bPawn1 x?) (bPawn1 y?))
					(= overPawn1 1)
				)
				(
					(and
						(> (myEvent x?) (bPawn2 nsLeft?))
						(< (myEvent x?) (bPawn2 nsRight?))
						(> (myEvent y?) (+ (bPawn2 nsTop?) 8))
						(< (myEvent y?) (+ (bPawn2 nsBottom?) 8))
					)
					(selector init: show: posn: (bPawn2 x?) (bPawn2 y?))
					(= overPawn2 1)
				)
				(
					(and
						(> (myEvent x?) (bPawn3 nsLeft?))
						(< (myEvent x?) (bPawn3 nsRight?))
						(> (myEvent y?) (+ (bPawn3 nsTop?) 8))
						(< (myEvent y?) (+ (bPawn3 nsBottom?) 8))
					)
					(selector init: show: posn: (bPawn3 x?) (bPawn3 y?))
					(= overPawn3 1)
				)
				(else
					(selector hide:)
					(= overQueen 0)
					(= overRook 0)
					(= overKing 0)
					(= overPawn1 0)
					(= overPawn2 0)
					(= overPawn3 0)
				)
			)
			(myEvent dispose:)
		else
			(= myEvent (Event new: evNULL))
			(if moveQueen
				(bQueen posn: (myEvent x?) (myEvent y?))
				(cond 
					(
						(and
							(> (myEvent x?) 82)
							(< (myEvent x?) 97)
							(> (myEvent y?) 105)
							(< (myEvent y?) 125)
						)
						(selector show: posn: 90 115) ; threaten mate
						(= boardCoord 25)
					)
					(
						(and
							(> (myEvent x?) 102)
							(< (myEvent x?) 117)
							(> (myEvent y?) 85)
							(< (myEvent y?) 105)
						)
						(selector show: posn: 110 95)
						(= boardCoord 34)
					)
					(
						(and
							(> (myEvent x?) 122)
							(< (myEvent x?) 137)
							(> (myEvent y?) 65)
							(< (myEvent y?) 85)
						)
						(selector show: posn: 130 75)
						(= boardCoord 43)
					)
					(
						(and
							(> (myEvent x?) 162)
							(< (myEvent x?) 177)
							(> (myEvent y?) 25)
							(< (myEvent y?) 45)
						)
						(selector show: posn: 170 35)
						(= boardCoord 61)
					)
					(
						(and
							(> (myEvent x?) 122)
							(< (myEvent x?) 137)
							(> (myEvent y?) 25)
							(< (myEvent y?) 45)
						)
						(selector show: posn: 130 35)
						(= boardCoord 59)
					)
					(
						(and
							(> (myEvent x?) 142)
							(< (myEvent x?) 157)
							(> (myEvent y?) 25)
							(< (myEvent y?) 45)
						)
						(selector show: posn: 150 35)
						(= boardCoord 60)
					)
					(
						(and
							(> (myEvent x?) 142)
							(< (myEvent x?) 157)
							(> (myEvent y?) 65)
							(< (myEvent y?) 85)
						)
						(selector show: posn: 150 75)
						(= boardCoord 44)
					)
					(
						(and
							(> (myEvent x?) 162)
							(< (myEvent x?) 177)
							(> (myEvent y?) 45)
							(< (myEvent y?) 65)
						)
						(selector show: posn: 170 55)
						(= boardCoord 53)
					)
					(
						(and
							(> (myEvent x?) 162)
							(< (myEvent x?) 177)
							(> (myEvent y?) 65)
							(< (myEvent y?) 85)
						)
						(selector show: posn: 170 75)
						(= boardCoord 45)
					)
					(
						(and
							(> (myEvent x?) 182)
							(< (myEvent x?) 197)
							(> (myEvent y?) 85)
							(< (myEvent y?) 105)
						)
						(selector show: posn: 190 95)
						(= boardCoord 38)
					)
					(
						(and
							(> (myEvent x?) 202)
							(< (myEvent x?) 217)
							(> (myEvent y?) 105)
							(< (myEvent y?) 125)
						)
						(selector show: posn: 210 115)
						(= boardCoord 31)
					)
					(
						(and
							(> (myEvent x?) 122)
							(< (myEvent x?) 137)
							(> (myEvent y?) 45)
							(< (myEvent y?) 65)
						)
						(selector show: posn: 130 55)                                         ; take rook
						(= boardCoord 51)
					)
					(else (selector hide:) (= boardCoord 0))
				)
			else
				(if moveRook (bRook posn: (myEvent x?) (myEvent y?)))
				(if moveRook
					(cond 
						(
						(and (> (myEvent y?) 85) (< (myEvent y?) 105))
							(cond 
								(
								(and (> (myEvent x?) 82) (< (myEvent x?) 97)) (selector show: posn: 90 95) (= boardCoord 33))
								(
								(and (> (myEvent x?) 102) (< (myEvent x?) 117)) (selector show: posn: 110 95) (= boardCoord 34))
								(
								(and (> (myEvent x?) 122) (< (myEvent x?) 137)) (selector show: posn: 130 95) (= boardCoord 35))
								(
								(and (> (myEvent x?) 162) (< (myEvent x?) 177)) (selector show: posn: 170 95) (= boardCoord 37))
								(
								(and (> (myEvent x?) 182) (< (myEvent x?) 197)) (selector show: posn: 190 95) (= boardCoord 38))
								(
								(and (> (myEvent x?) 202) (< (myEvent x?) 217)) (selector show: posn: 210 95) (= boardCoord 39))
								(
								(and (> (myEvent x?) 222) (< (myEvent x?) 237)) (selector show: posn: 230 95) (= boardCoord 40))
								(else (selector hide:) (= boardCoord 0))
							)
						)
						(
						(and (> (myEvent x?) 142) (< (myEvent x?) 157))
							(cond 
								(
								(and (> (myEvent y?) 65) (< (myEvent y?) 85)) (selector show: posn: 150 75) (= boardCoord 44))
								(
								(and (> (myEvent y?) 105) (< (myEvent y?) 125)) (selector show: posn: 150 115) (= boardCoord 28))
								(else (selector hide:) (= boardCoord 0))
							)
						)
					)
				)
			)
			(myEvent dispose:)
		)
	)
)

(procedure (takePiece)
	(= choose 0)
	(selector hide:)
	(gGame setCursor: 993)
)

(instance winningComb of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1 (= cycles 12))
			(2
				(= cycles 7)
				(= winning 1)
				(PrintOther 105 20)
				(PrintOther 105 21)
			)
			(3
				(= cycles 5)
				(PrintOther 105 24)
; If White takes your Rook with his pawn...
				(wPawn3
					xStep: 4
					yStep: 4
					ignoreActors:
					setMotion: MoveTo 130 95
				)
			)
			(4 (= cycles 7) (bRook hide:))
			(5
				(= cycles 20)
				(bQueen yStep: 7 ignoreActors: setMotion: MoveTo 150 175)
			)
			(6
				(= cycles 20)
				(PrintOther 105 23)
			)
; Checkmate!
			(7
				(= cycles 7)
				(PrintOther 105 26)
; Or...
				(bRook show:)
				(wPawn3 posn: 150 115)
				(bQueen posn: 150 55)
			)
			(8
				(= cycles 5)
				(PrintOther 105 27)
; If White takes your Queen...
				(wRook xStep: 4 ignoreActors: setMotion: MoveTo 150 55)
			)
			(9
				(= cycles 16)
				(bQueen hide:)
				(bRook yStep: 6 ignoreActors: setMotion: MoveTo 130 175)
			)
			(10
				(= cycles 20)
				(PrintOther 105 23)
			)
; Checkmate!
			(11
				(= cycles 7)
				(PrintOther 105 26)
; Or...
				(bRook posn: 130 95)
				(wRook posn: 130 55)
				(bQueen show:)
			)
			(12
				(= cycles 9)
				(PrintOther 105 30)
; If White takes your Rook to prevent checkmate...
				(wRook yStep: 4 setMotion: MoveTo 130 95)
			)
			(13
				(= cycles 9)
				(bRook hide:)
				(bQueen xStep: 5 setMotion: MoveTo 110 55)
			)
			(14
				(= cycles 10)
				(wQueen hide:)
			)
			(15
				(= cycles 15)
				(PrintOther 105 31)
			)
; You win a Queen for a Rook!
			(16
				(Print {Excellent job!})
; You can look at other moves to make sure.
				(= g105Solved 1)
				(= gInt (+ gInt 2))
				(gGame changeScore: 2)
				(= gArcStl 0)
				(gRoom newRoom: 44)
			)
		)
	)
)

(instance variations of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(1 (= cycles 12))
				(wrongAnswer)
			(2
				(= cycles 7)
				(if mateNumber
					(Print 105 8 #title "Old man says" #at -1 140)
				else                  ; show this one
					(Print 105 7 #title "Old man says" #at -1 140)
				)
			)                         ; show this one
			(3
				(= cycles 10)
				(wRook xStep: 5 ignoreActors: setMotion: MoveTo 190 55)
			)
			(4 (= cycles 4) (bPawn2 hide:))
			(5
				(= cycles 9)
				(PrintOther 105 33)
; Check
				(bKing yStep: 4 ignoreActors: setMotion: MoveTo 230 75)
			)
			(6
				(= cycles 9)
				(wRook setMotion: MoveTo 230 55)
			)
			(7
				(= cycles 9)
				(PrintOther 105 33)
; Check
				(bKing yStep: 4 xStep: 4 setMotion: MoveTo 210 95)
			)
			(8
				(= cycles 9)
				(if mateNumber
					(variations changeState: 13)
				else
					(wQueen
						xStep: 4
						yStep: 4
						ignoreActors:
						setMotion: MoveTo 150 95
					)
				)
			)
			(9 (= cycles 4) (bRook hide:))
			(10
				(= cycles 9)
				(PrintOther 105 33)
; Check
				(bKing setMotion: MoveTo 190 75)
			)
			(11
				(= cycles 12)
				(wRook setMotion: MoveTo 190 55)
			)
			(12 (RoomScript changeState: 5))
			(13
				(= cycles 14)
				(wQueen
					xStep: 5
					yStep: 5
					ignoreActors:
					setMotion: MoveTo 170 55
				)
			)
			(14
				(= cycles 9)
				(PrintOther 105 33)
; Check
				(bKing yStep: 4 xStep: 4 setMotion: MoveTo 210 115)
			)
			(15
				(= cycles 14)
				(wQueen setMotion: MoveTo 170 115)
			)
			(16
				(= cycles 9)
				(PrintOther 105 33)
; Check
				(bKing setMotion: MoveTo 210 95)
				(RoomScript changeState: 3)
			)
		)
	)
)

(procedure (wrongAnswer)
	(if (== g105Solved 0)
		(= g105Solved -1)
		(Print 105 3 #title "Old man says" #at -1 140)
	else
		(= g105Solved -2)
		(Print 105 4 #title "Old man says" #at -1 140)
		(= gArcStl 0)
		(gRoom newRoom: 44)
	)	
)

(procedure (PrintOther textRes textResIndex)
	(Print textRes textResIndex #at -1 150)
)

(instance wKing of Prop
	(properties
		y 175
		x 210
		view 565
	)
)

(instance bKing of Act
	(properties
		y 55
		x 230
		view 565
		loop 1
	)
)

(instance wQueen of Act
	(properties
		y 55
		x 110
		view 565
		cel 1
	)
)

(instance bQueen of Act
	(properties
		y 55
		x 150
		view 567
	)
)

(instance wRook of Act
	(properties
		y 55
		x 130
		view 566
	)
)

(instance bRook of Act
	(properties
		y 95
		x 150
		view 568
	)
)

(instance wPawn1 of Prop
	(properties
		y 155
		x 90
		view 565
		cel 3
	)
)

(instance wPawn2 of Act
	(properties
		y 135
		x 130
		view 569
	)
)

(instance wPawn3 of Act
	(properties
		y 115
		x 150
		view 569
	)
)

(instance wPawn4 of Prop
	(properties
		y 135
		x 170
		view 565
		cel 3
	)
)

(instance wPawn5 of Prop
	(properties
		y 155
		x 190
		view 565
		cel 3
	)
)

(instance wPawn6 of Prop
	(properties
		y 135
		x 210
		view 565
		cel 3
	)
)

(instance wPawn7 of Prop
	(properties
		y 155
		x 230
		view 565
		cel 3
	)
)

(instance bPawn1 of Prop
	(properties
		y 75
		x 90
		view 565
		loop 1
		cel 3
	)
)

(instance bPawn2 of Prop
	(properties
		y 55
		x 190
		view 565
		loop 1
		cel 3
	)
)

(instance bPawn3 of Prop
	(properties
		y 75
		x 210
		view 565
		loop 1
		cel 3
	)
)

(instance bPawn4 of Prop
	(properties
		y 135
		x 230
		view 565
		loop 1
		cel 3
	)
)

(instance Exit of Prop
	(properties
		y 182
		x 280
		view 572
		loop 15
	)
)

(instance selector of Prop
	(properties
		y 135
		x 230
		view 565
		loop 2
	)
)
