;;; Sierra Script 1.0 - (do not remove this comment)
; + 4 SCORE // + 3 gINT //
(script# 110)
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
	rm110 0
)

(local

; Entrance Puzzle for Scientist's Door



	; "Over Block One...Two...Three...Nine
	oBO =  0
	oBTw =  0
	oBTh =  0
	oBF =  0
	oBFi =  0
	oBSi =  0
	oBSe =  0
	oBE =  0
	oBN =  0
	; "Square # ON" i.e. Has a piece on it
	sOO =  0
	sTwO =  0
	sThO =  0
	sFoO =  0
	sFiO =  0
	sSiO =  0
	sSeO =  0
	sEO =  0
	sNO =  0
	myEvent
	pT =  0 ; "Piece Taken"
	mP =  0 ; "Moving Piece"
	tO =  0 ; "Taken Off"
	solved =  0
	failed =  0
)

(instance rm110 of Rm
	(properties
		picture scriptNumber
	)
	
	(method (init)
		(super init:)
		(self setScript: RoomScript)
		(SetUpEgo)
		(gEgo init: hide:)
		(bO init:)
		(bTw init:)
		(bTh init:)
		(bFo init:)
		(bFi init:)
		(bSi init:)
		(bSe init:)
		(bE init:)
		(bN init:)
		(invH init: hide: ignoreActors:)
		(lightning init: hide: setPri: 1)
		(= gArcStl 1)
		(infoButton init: hide:)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (changeState mainState)
		(= state mainState)
		(switch state
			(0)
			(1
				(lightning show: setCycle: End RoomScript cycleSpeed: 2)
			)
			(2
				(= cycles 8)
				(lightning loop: 15 setCycle: Fwd)
			)
			(3
				(= cycles 10)
				(Print 110 2)
				(= g110Solved 1)
				(gGame changeScore: 4)
				(= gInt (+ gInt 3))
				(= gArcStl 0)
			)
			(4 (gRoom newRoom: 50))
		)
	)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(cond 
			((not pT)
				(if (== (pEvent type?) evMOUSEBUTTON)
					(gEgo posn: (pEvent x?) (pEvent y?))
					(if (not gHardMode)
						(if
							(and
								(> (pEvent x?) (infoButton nsLeft?))
								(< (pEvent x?) (infoButton nsRight?))
								(> (pEvent y?) (infoButton nsTop?))
								(< (pEvent y?) (infoButton nsBottom?))
							)
							(Print 110 3 #title {Hint:})
						)
					)
					(if (== (gEgo onControl:) ctlNAVY)
						(if sOO (= tO 1) (= sOO 0))
					)
					(if (== (gEgo onControl:) ctlGREEN)
						(if sTwO (= tO 1) (= sTwO 0))
					)
					(if (== (gEgo onControl:) ctlTEAL)
						(if sThO (= tO 1) (= sThO 0))
					)
					(if (== (gEgo onControl:) ctlMAROON)
						(if sFoO (= tO 1) (= sFoO 0))
					)
					(if (== (gEgo onControl:) ctlPURPLE)
						(if sFiO (= tO 1) (= sFiO 0))
					)
					(if (== (gEgo onControl:) ctlBROWN)
						(if sSiO (= tO 1) (= sSiO 0))
					)
					(if (== (gEgo onControl:) ctlSILVER)
						(if sSeO (= tO 1) (= sSeO 0))
					)
					(if (== (gEgo onControl:) ctlGREY)
						(if sEO (= tO 1) (= sEO 0))
					)
					(if (== (gEgo onControl:) ctlBLUE)
						(if sNO (= tO 1) (= sNO 0))
					)
					(if oBO (= mP 1) (= pT 1))
					(if oBTw (= mP 2) (= pT 1))
					(if oBTh (= mP 3) (= pT 1))
					(if oBF (= mP 4) (= pT 1))
					(if oBFi (= mP 5) (= pT 1))
					(if oBSi (= mP 6) (= pT 1))
					(if oBSe (= mP 7) (= pT 1))
					(if oBE (= mP 8) (= pT 1))
					(if oBN (= mP 9) (= pT 1))
				)
			)
			((== (pEvent type?) evMOUSEBUTTON)
				(gEgo posn: (pEvent x?) (pEvent y?))
				(if (== (gEgo onControl:) ctlNAVY)
					(if sOO
						(Print 110 1)
					else
						(= pT 0)
						(switch mP
							(1 (movePiece bO 110 55))    ; (bO:show()posn(110 55))  (invH:hide())
							(2 (movePiece bTw 110 55))    ; (bTw:show()posn(110 55)) (invH:hide())
							(3 (movePiece bTh 110 55))    ; (bTh:show()posn(110 55)) (invH:hide())
							(4 (movePiece bFo 110 55))    ; (bFo:show()posn(110 55))  (invH:hide())
							(5 (movePiece bFi 110 55))    ; (bFi:show()posn(110 55))  (invH:hide())
							(6 (movePiece bSi 110 55))    ; (bSi:show()posn(110 55))   (invH:hide())
							(7 (movePiece bSe 110 55))    ; (bSe:show()posn(110 55))  (invH:hide())
							(8 (movePiece bE 110 55))    ; (bE:show()posn(110 55))  (invH:hide())
							(9 (movePiece bN 110 55))
						)
					)
				)                                        ; (bN:show()posn(110 55))   (invH:hide())
				(if (== (gEgo onControl:) ctlGREEN)
					(if sTwO
						(Print 110 1)
					else
						(= pT 0)
						(switch mP
							(1 (movePiece bO 160 55))   ; (bO:show()posn(160 55)) (invH:hide())
							(2 (movePiece bTw 160 55))    ; (bTw:show()posn(160 55)) (invH:hide())
							(3 (movePiece bTh 160 55))    ; (bTh:show()posn(160 55)) (invH:hide())
							(4 (movePiece bFo 160 55))    ; (bFo:show()posn(160 55)) (invH:hide())
							(5 (movePiece bFi 160 55))    ; (bFi:show()posn(160 55)) (invH:hide())
							(6 (movePiece bSi 160 55))    ; (bSi:show()posn(160 55)) (invH:hide())
							(7 (movePiece bSe 160 55))    ; (bSe:show()posn(160 55)) (invH:hide())
							(8 (movePiece bE 160 55))    ; (bE:show()posn(160 55)) (invH:hide())
							(9 (movePiece bN 160 55))
						)
					)
				)                                        ; (bN:show()posn(160 55)) (invH:hide())
				(if (== (gEgo onControl:) ctlTEAL)
					(if sThO
						(Print 110 1)
					else
						(= pT 0)
						(switch mP
							(1 (movePiece bO 210 55))    ; (bO:show()posn(210 55))  (invH:hide())
							(2 (movePiece bTw 210 55))    ; (bTw:show()posn(210 55))  (invH:hide())
							(3 (movePiece bTh 210 55))    ; (bTh:show()posn(210 55))  (invH:hide())
							(4 (movePiece bFo 210 55))    ; (bFo:show()posn(210 55))  (invH:hide())
							(5 (movePiece bFi 210 55))     ; (bFi:show()posn(210 55))   (invH:hide())
							(6 (movePiece bSi 210 55))    ; (bSi:show()posn(210 55))    (invH:hide())
							(7 (movePiece bSe 210 55))    ; (bSe:show()posn(210 55))  (invH:hide())
							(8 (movePiece bE 210 55))    ; (bE:show()posn(210 55))  (invH:hide())
							(9 (movePiece bN 210 55))
						)
					)
				)                                        ; (bN:show()posn(210 55))   (invH:hide())
				(if (== (gEgo onControl:) ctlMAROON)
					(if sFoO
						(Print 110 1)
					else
						(= pT 0)
						(switch mP
							(1 (movePiece bO 110 105))   ; (bO:show()posn(110 105)) (invH:hide())
							(2 (movePiece bTw 110 105))    ; (bTw:show()posn(110 105))  (invH:hide())
							(3 (movePiece bTh 110 105))    ; (bTh:show()posn(110 105)) (invH:hide())
							(4 (movePiece bFo 110 105))    ; (bFo:show()posn(110 105))  (invH:hide())
							(5 (movePiece bFi 110 105))    ; (bFi:show()posn(110 105))  (invH:hide())
							(6 (movePiece bSi 110 105))    ; (bSi:show()posn(110 105))    (invH:hide())
							(7 (movePiece bSe 110 105))    ; (bSe:show()posn(110 105))  (invH:hide())
							(8 (movePiece bE 110 105))    ; (bE:show()posn(110 105))   (invH:hide())
							(9 (movePiece bN 110 105))
						)
					)
				)                                         ; (bN:show()posn(110 105))    (invH:hide())
				(if (== (gEgo onControl:) ctlPURPLE)
					(if sFiO
						(Print 110 1)
					else
						(= pT 0)
						(switch mP
							(1 (movePiece bO 160 105))   ; (bO:show()posn(160 105))  (invH:hide())
							(2 (movePiece bTw 160 105))    ; (bTw:show()posn(160 105))   (invH:hide())
							(3 (movePiece bTh 160 105))     ; (bTh:show()posn(160 105))  (invH:hide())
							(4 (movePiece bFo 160 105))    ; (bFo:show()posn(160 105))  (invH:hide())
							(5 (movePiece bFi 160 105))    ; (bFi:show()posn(160 105))  (invH:hide())
							(6 (movePiece bSi 160 105))    ; (bSi:show()posn(160 105))   (invH:hide())
							(7 (movePiece bSe 160 105))    ; (bSe:show()posn(160 105))  (invH:hide())
							(8 (movePiece bE 160 105))    ; (bE:show()posn(160 105))  (invH:hide())
							(9 (movePiece bN 160 105))
						)
					)
				)                                         ; (bN:show()posn(160 105))   (invH:hide())
				(if (== (gEgo onControl:) ctlBROWN)
					(if sSiO
						(Print 110 1)
					else
						(= pT 0)
						(switch mP
							(1 (movePiece bO 210 105))   ; (bO:show()posn(210 105))  (invH:hide())
							(2 (movePiece bTw 210 105))    ; (bTw:show()posn(210 105))  (invH:hide())
							(3 (movePiece bTh 210 105))    ; (bTh:show()posn(210 105)) (invH:hide())
							(4 (movePiece bFo 210 105))    ; (bFo:show()posn(210 105))  (invH:hide())
							(5 (movePiece bFi 210 105))    ; (bFi:show()posn(210 105))  (invH:hide())
							(6 (movePiece bSi 210 105))    ; (bSi:show()posn(210 105))   (invH:hide())
							(7 (movePiece bSe 210 105))    ; (bSe:show()posn(210 105))  (invH:hide())
							(8 (movePiece bE 210 105))    ; (bE:show()posn(210 105))  (invH:hide())
							(9 (movePiece bN 210 105))
						)
					)
				)                                         ; (bN:show()posn(210 105))   (invH:hide())
				(if (== (gEgo onControl:) ctlSILVER)
					(if sSeO
						(Print 110 1)
					else
						(= pT 0)
						(switch mP
							(1 (movePiece bO 110 155))    ; (bO:show()posn(110 155))   (invH:hide())
							(2 (movePiece bTw 110 155))    ; (bTw:show()posn(110 155))   (invH:hide())
							(3 (movePiece bTh 110 155))    ; (bTh:show()posn(110 155)) (invH:hide())
							(4 (movePiece bFo 110 155))    ; (bFo:show()posn(110 155))   (invH:hide())
							(5 (movePiece bFi 110 155))    ; (bFi:show()posn(110 155))   (invH:hide())
							(6 (movePiece bSi 110 155))    ; (bSi:show()posn(110 155))    (invH:hide())
							(7 (movePiece bSe 110 155))    ; (bSe:show()posn(110 155))  (invH:hide())
							(8 (movePiece bE 110 155))    ; (bE:show()posn(110 155))   (invH:hide())
							(9 (movePiece bN 110 155))
						)
					)
				)                                         ; (bN:show()posn(110 155))   (invH:hide())
				(if (== (gEgo onControl:) ctlGREY)
					(if sEO
						(Print 110 1)
					else
						(= pT 0)
						(switch mP
							(1 (movePiece bO 160 155))   ; (bO:show()posn(160 155))   (invH:hide())
							(2 (movePiece bTw 160 155))    ; (bTw:show()posn(160 155))  (invH:hide())
							(3 (movePiece bTh 160 155))    ; (bTh:show()posn(160 155))   (invH:hide())
							(4 (movePiece bFo 160 155))    ; (bFo:show()posn(160 155))  (invH:hide())
							(5 (movePiece bFi 160 155))    ; (bFi:show()posn(160 155))   (invH:hide())
							(6 (movePiece bSi 160 155))    ; (bSi:show()posn(160 155))    (invH:hide())
							(7 (movePiece bSe 160 155))    ; (bSe:show()posn(160 155))    (invH:hide())
							(8 (movePiece bE 160 155))    ; (bE:show()posn(160 155))   (invH:hide())
							(9 (movePiece bN 160 155))
						)
					)
				)                                         ; (bN:show()posn(160 155))  (invH:hide())
				(if (== (gEgo onControl:) ctlBLUE)
					(if sNO
						(Print 110 1)
					else
						(= pT 0)
						(switch mP
							(1 (movePiece bO 210 155))   ; (bO:show()posn(210 155))  (invH:hide())
							(2 (movePiece bTw 210 155))    ; (bTw:show()posn(210 155))  (invH:hide())
							(3 (movePiece bTh 210 155))    ; (bTh:show()posn(210 155)) (invH:hide())
							(4 (movePiece bFo 210 155))    ; (bFo:show()posn(210 155)) (invH:hide())
							(5 (movePiece bFi 210 155))    ; (bFi:show()posn(210 155)) (invH:hide())
							(6 (movePiece bSi 210 155))    ; (bSi:show()posn(210 155))  (invH:hide())
							(7 (movePiece bSe 210 155))    ; (bSe:show()posn(210 155))   (invH:hide())
							(8 (movePiece bE 210 155))    ; (bE:show()posn(210 155))  (invH:hide())
							(9 (movePiece bN 210 155))
						)
					)
				)                                         ; (bN:show()posn(210 155))  (invH:hide())
				(if (== (gEgo onControl:) ctlBLACK)
					(= pT 0)
					(switch mP
						(1 (movePiece bO 260 105))   ; (bO:show()posn(260 105))  (invH:hide())
						(2 (movePiece bTw 300 105))    ; (bTw:show()posn(300 105))  (invH:hide())
						(3 (movePiece bTh 260 75))    ; (bTh:show()posn(260 75))  (invH:hide())
						(4 (movePiece bFo 260 135))    ; (bFo:show()posn(260 135))  (invH:hide())
						(5 (movePiece bFi 300 135))    ; (bFi:show()posn(300 135))  (invH:hide())
						(6 (movePiece bSi 260 165))    ; (bSi:show()posn(260 165))    (invH:hide())
						(7 (movePiece bSe 300 45))    ; (bSe:show()posn(300 45))  (invH:hide())
						(8 (movePiece bE 300 75))    ; (bE:show()posn(300 75))  (invH:hide())
						(9 (movePiece bN 260 45))
					)
				)
			)
		)
	)
	                                                 ; (bN:show()posn(260 45))   (invH:hide())
	(method (doit)
		(super doit:)
		(if (== sOO 1)
			(if (== sTwO 2)
				(if (== sThO 3)
					(if (== sFoO 4)
						(if (== sFiO 5)
							(if (== sSiO 6)
								(if (== sSeO 7)
									(if (== sEO 8)
										(if (== sNO 9)
											(if (not solved)
												(RoomScript changeState: 1)
												(= solved 1)
											)
										)
									)
								)
							)
						)
					)
				)
			)
		)
		(if (not gHardMode)
			(infoButton show:)
		else
			(infoButton hide:)
		)
		(allDown)
		(= myEvent (Event new: evNULL))
		(if (not tO)
			(if (== (bO onControl:) ctlNAVY) (= sOO 1))
			(if (== (bO onControl:) ctlGREEN) (= sTwO 1))
			(if (== (bO onControl:) ctlTEAL) (= sThO 1))
			(if (== (bO onControl:) ctlMAROON) (= sFoO 1))
			(if (== (bO onControl:) ctlPURPLE) (= sFiO 1))
			(if (== (bO onControl:) ctlBROWN) (= sSiO 1))
			(if (== (bO onControl:) ctlSILVER) (= sSeO 1))
			(if (== (bO onControl:) ctlGREY) (= sEO 1))
			(if (== (bO onControl:) ctlBLUE) (= sNO 1))
			(if (== (bTw onControl:) ctlNAVY) (= sOO 2))
			(if (== (bTw onControl:) ctlGREEN) (= sTwO 2))
			(if (== (bTw onControl:) ctlTEAL) (= sThO 2))
			(if (== (bTw onControl:) ctlMAROON) (= sFoO 2))
			(if (== (bTw onControl:) ctlPURPLE) (= sFiO 2))
			(if (== (bTw onControl:) ctlBROWN) (= sSiO 2))
			(if (== (bTw onControl:) ctlSILVER) (= sSeO 2))
			(if (== (bTw onControl:) ctlGREY) (= sEO 2))
			(if (== (bTw onControl:) ctlBLUE) (= sNO 2))
			(if (== (bTh onControl:) ctlNAVY) (= sOO 3))
			(if (== (bTh onControl:) ctlGREEN) (= sTwO 3))
			(if (== (bTh onControl:) ctlTEAL) (= sThO 3))
			(if (== (bTh onControl:) ctlMAROON) (= sFoO 3))
			(if (== (bTh onControl:) ctlPURPLE) (= sFiO 3))
			(if (== (bTh onControl:) ctlBROWN) (= sSiO 3))
			(if (== (bTh onControl:) ctlSILVER) (= sSeO 3))
			(if (== (bTh onControl:) ctlGREY) (= sEO 3))
			(if (== (bTh onControl:) ctlBLUE) (= sNO 3))
			(if (== (bFo onControl:) ctlNAVY) (= sOO 4))
			(if (== (bFo onControl:) ctlGREEN) (= sTwO 4))
			(if (== (bFo onControl:) ctlTEAL) (= sThO 4))
			(if (== (bFo onControl:) ctlMAROON) (= sFoO 4))
			(if (== (bFo onControl:) ctlPURPLE) (= sFiO 4))
			(if (== (bFo onControl:) ctlBROWN) (= sSiO 4))
			(if (== (bFo onControl:) ctlSILVER) (= sSeO 4))
			(if (== (bFo onControl:) ctlGREY) (= sEO 4))
			(if (== (bFo onControl:) ctlBLUE) (= sNO 4))
			(if (== (bFi onControl:) ctlNAVY) (= sOO 5))
			(if (== (bFi onControl:) ctlGREEN) (= sTwO 5))
			(if (== (bFi onControl:) ctlTEAL) (= sThO 5))
			(if (== (bFi onControl:) ctlMAROON) (= sFoO 5))
			(if (== (bFi onControl:) ctlPURPLE) (= sFiO 5))
			(if (== (bFi onControl:) ctlBROWN) (= sSiO 5))
			(if (== (bFi onControl:) ctlSILVER) (= sSeO 5))
			(if (== (bFi onControl:) ctlGREY) (= sEO 5))
			(if (== (bFi onControl:) ctlBLUE) (= sNO 5))
			(if (== (bSi onControl:) ctlNAVY) (= sOO 6))
			(if (== (bSi onControl:) ctlGREEN) (= sTwO 6))
			(if (== (bSi onControl:) ctlTEAL) (= sThO 6))
			(if (== (bSi onControl:) ctlMAROON) (= sFoO 6))
			(if (== (bSi onControl:) ctlPURPLE) (= sFiO 6))
			(if (== (bSi onControl:) ctlBROWN) (= sSiO 6))
			(if (== (bSi onControl:) ctlSILVER) (= sSeO 6))
			(if (== (bSi onControl:) ctlGREY) (= sEO 6))
			(if (== (bSi onControl:) ctlBLUE) (= sNO 6))
			(if (== (bSe onControl:) ctlNAVY) (= sOO 7))
			(if (== (bSe onControl:) ctlGREEN) (= sTwO 7))
			(if (== (bSe onControl:) ctlTEAL) (= sThO 7))
			(if (== (bSe onControl:) ctlMAROON) (= sFoO 7))
			(if (== (bSe onControl:) ctlPURPLE) (= sFiO 7))
			(if (== (bSe onControl:) ctlBROWN) (= sSiO 7))
			(if (== (bSe onControl:) ctlSILVER) (= sSeO 7))
			(if (== (bSe onControl:) ctlGREY) (= sEO 7))
			(if (== (bSe onControl:) ctlBLUE) (= sNO 7))
			(if (== (bE onControl:) ctlNAVY) (= sOO 8))
			(if (== (bE onControl:) ctlGREEN) (= sTwO 8))
			(if (== (bE onControl:) ctlTEAL) (= sThO 8))
			(if (== (bE onControl:) ctlMAROON) (= sFoO 8))
			(if (== (bE onControl:) ctlPURPLE) (= sFiO 8))
			(if (== (bE onControl:) ctlBROWN) (= sSiO 8))
			(if (== (bE onControl:) ctlSILVER) (= sSeO 8))
			(if (== (bE onControl:) ctlGREY) (= sEO 8))
			(if (== (bE onControl:) ctlBLUE) (= sNO 8))
			(if (== (bN onControl:) ctlNAVY) (= sOO 9))
			(if (== (bN onControl:) ctlGREEN) (= sTwO 9))
			(if (== (bN onControl:) ctlTEAL) (= sThO 9))
			(if (== (bN onControl:) ctlMAROON) (= sFoO 9))
			(if (== (bN onControl:) ctlPURPLE) (= sFiO 9))
			(if (== (bN onControl:) ctlBROWN) (= sSiO 9))
			(if (== (bN onControl:) ctlSILVER) (= sSeO 9))
			(if (== (bN onControl:) ctlGREY) (= sEO 9))
			(if (== (bN onControl:) ctlBLUE) (= sNO 9))
		)
		(if pT
			(= tO 0)
			(switch mP
				(1
					(bO posn: 260 105 hide:)
					(invHProc 4)
				)
				(2
					(bTw posn: 300 105 hide:)
					(invHProc 1)
				)
				(3
					(bTh posn: 260 75 hide:)
					(invHProc 5)
				)
				(4
					(bFo posn: 260 135 hide:)
					(invHProc 3)
				)
				(5
					(bFi posn: 300 135 hide:)
					(invHProc 0)
				)
				(6
					(bSi posn: 260 165 hide:)
					(invHProc 2)
				)
				(7
					(bSe posn: 300 45 hide:)
					(invHProc 6)
				)
				(8
					(bE posn: 300 75 hide:)
					(invHProc 8)
				)
				(9
					(bN posn: 260 45 hide:)
					(invHProc 7)
				)
			)
		)
		(myEvent dispose:)
		(= myEvent (Event new: evNULL))
		(if (not pT)
			(cond 
				(
					(checkEventPT
						myEvent
						(bO nsLeft?)
						(bO nsRight?)
						(bO nsTop?)
						(bO nsBottom?)
					)
					(= oBO 1)
				)
				(
					(checkEventPT
						myEvent
						(bTw nsLeft?)
						(bTw nsRight?)
						(bTw nsTop?)
						(bTw nsBottom?)
					)
					(= oBTw 1)
				)
				(
					(checkEventPT
						myEvent
						(bTh nsLeft?)
						(bTh nsRight?)
						(bTh nsTop?)
						(bTh nsBottom?)
					)
					(= oBTh 1)
				)
				(
					(checkEventPT
						myEvent
						(bFo nsLeft?)
						(bFo nsRight?)
						(bFo nsTop?)
						(bFo nsBottom?)
					)
					(= oBF 1)
				)
				(
					(checkEventPT
						myEvent
						(bFi nsLeft?)
						(bFi nsRight?)
						(bFi nsTop?)
						(bFi nsBottom?)
					)
					(= oBFi 1)
				)
				(
					(checkEventPT
						myEvent
						(bSi nsLeft?)
						(bSi nsRight?)
						(bSi nsTop?)
						(bSi nsBottom?)
					)
					(= oBSi 1)
				)
				(
					(checkEventPT
						myEvent
						(bSe nsLeft?)
						(bSe nsRight?)
						(bSe nsTop?)
						(bSe nsBottom?)
					)
					(= oBSe 1)
				)
				(
					(checkEventPT
						myEvent
						(bE nsLeft?)
						(bE nsRight?)
						(bE nsTop?)
						(bE nsBottom?)
					)
					(= oBE 1)
				)
				(
					(checkEventPT
						myEvent
						(bN nsLeft?)
						(bN nsRight?)
						(bN nsTop?)
						(bN nsBottom?)
					)
					(= oBN 1)
				)
				(else
					(gGame setCursor: 999)
					(= oBO 0)
					(= oBTw 0)
					(= oBTh 0)
					(= oBF 0)
					(= oBFi 0)
					(= oBSi 0)
					(= oBSe 0)
					(= oBE 0)
					(= oBN 0)
				)
			)
		)
		(myEvent dispose:)
	)
)

(procedure (allDown)
	(if (and sOO sTwO sThO sFoO sFiO sSiO sSeO sEO sNO)
		(if (not solved)
			(if (not failed)
				(Print {That doesn't seem to work.})
				(= failed 1)
			)
		)
	else
		(= failed 0)
	)
)

(procedure (takePiece)
	(= pT 1)
	(gGame setCursor: 993)
)

(procedure (movePiece view x y)
	(view show: posn: x y)
	(invH hide:)
)

(procedure (invHProc loopNum)
	(invH
		show:
		loop: loopNum
		posn: (myEvent x?) (myEvent y?)
	)
)

(procedure (checkEventPT pEvent x1 x2 y1 y2)
	(if
		(and
			(> (pEvent x?) x1)
			(< (pEvent x?) x2)
			(> (pEvent y?) (+ y1 8))
			(< (pEvent y?) (+ y2 8))
		)
		(gGame setCursor: 991)
		(return TRUE)
	else
		(return FALSE)
	)
)

(instance lightning of Act
	(properties            ; "Block One"
		y 175
		x 160
		view 581
		loop 14
	)
)

(instance bO of Act
	(properties     ; "Block One"
		y 105
		x 260
		view 581
		loop 4
	)
)

(instance bTw of Act
	(properties       ; ...etc
		y 105
		x 300
		view 581
		loop 1
	)
)

(instance bTh of Act
	(properties
		y 75
		x 260
		view 581
		loop 5
	)
)

(instance bFo of Act
	(properties
		y 135
		x 260
		view 581
		loop 3
	)
)

(instance bFi of Act
	(properties
		y 135
		x 300
		view 581
		loop 0
	)
)

(instance bSi of Act
	(properties
		y 165
		x 260
		view 581
		loop 2
	)
)

(instance bSe of Act
	(properties
		y 45
		x 300
		view 581
		loop 6
	)
)

(instance bE of Act
	(properties
		y 75
		x 300
		view 581
		loop 8
	)
)

(instance bN of Act
	(properties
		y 45
		x 260
		view 581
		loop 7
	)
)

(instance invH of Act
	(properties
		y 45
		x 260
		view 581
	)
)

(instance infoButton of Prop
	(properties
		y 162
		x 299
		view 998
		loop 7                               ; 160 46
	)
)
