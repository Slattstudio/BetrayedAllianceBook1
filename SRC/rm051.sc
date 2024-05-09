;;; Sierra Script 1.0 - (do not remove this comment)

(script# 51)
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
	rm051 0
)

(local
; East Castle Fence



	randoFat
	randoOld
	; switchSides = 0
	[odds 4] = [1 3 5 7]
	[evens 4] = [0 2 4 6]
	rando
	[TextNumFirstLine 11] = [0 4 6 8 12 16 19 22 26 29 31]
	tauntNumber =  0
	respondNumber =  1
	numberOfLines =  1
	multRespVar =  0
	noRepeat
)

(instance rm051 of Rm
	(properties
		picture scriptNumber
		north 0
		east 53
		south 0
		west 0
	)
	
	(method (init)
		(super init:)
		(self
			setScript: RoomScript
			setRegions: 200
			setRegions: 204
		)
		(switch gPreviousRoomNumber
			(else 
				(gEgo posn: 300 140 loop: 1)
			)
			(53
				(gEgo posn: 300 140 loop: 1)
			)
		)
		(SetUpEgo)
		(gEgo init: setPri: 12)
		(RunningCheck)
		(oldMan
			init:
			ignoreActors:
			setScript: tauntScript
			ignoreControl: ctlWHITE
		)
		(fatMan
			init:
			ignoreActors:
			setScript: fatManScript
			ignoreControl: ctlWHITE
		)
		; (oldManScript:changeState(1))
		(fatManScript changeState: 1)
		(tauntScript changeState: 1)
	)
)


(instance RoomScript of Script
	(properties)
	
	(method (handleEvent pEvent)
		(super handleEvent: pEvent)
		(if (== (pEvent type?) evMOUSEBUTTON)
			(if (& (pEvent modifiers?) emRIGHT_BUTTON)
				(if
					(and
						(> (pEvent x?) (oldMan nsLeft?))
						(< (pEvent x?) (oldMan nsRight?))
						(> (pEvent y?) (oldMan nsTop?))
						(< (pEvent y?) (oldMan nsBottom?))
					)
					(Print
						{There is an older man engaged in sword practice. His form is poor, but he shows great stamina for a man his age.}
						#at
						-1
						28
					)
				)
				(if
					(and
						(> (pEvent x?) (fatMan nsLeft?))
						(< (pEvent x?) (fatMan nsRight?))
						(> (pEvent y?) (fatMan nsTop?))
						(< (pEvent y?) (fatMan nsBottom?))
					)
					(Print
						{For a generously sized man, his agility is actually quite remarkable. Too bad his sword-handling isn't equally good.}
						#at
						-1
						28
					)
				)
			)
		)
		(if (Said 'listen[/!*]')
			(Print{The sword technique of these men aren't the only thing needing work. The insults and banter you hear between them is low rate as well!} #at -1 10)	
		)
		(if (Said 'talk,ask/man,soldier')
			(Print
				{Best not to distract them. They may seek to arrest you.}
				#at
				-1
				10
			)
		)
		(if (Said 'look>')
			(if (Said '/(man<fat,big)')
				(Print
					{There are two soldiers practising swordplay. The large one is actually much faster than one would expect and the older man has great energy.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '/(man<old)')
				(Print
					{There is an older man engaged in sword practice. His form is poor, but he shows great stamina for a man his age.}
					#at
					-1
					28
				)
			)
			(if (Said '/man')
				(Print
					{There are two soldiers practising swordplay. The large one is actually much faster than one would expect and the older man has great energy.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '/soldier')
				(Print
					{There are two soldiers practising swordplay. The large one is actually much faster than one would expect and the older man has great energy.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '/town,valley')
				(Print
					{There in the valley is the town of Shelah. For centuries it has enjoyed great peace, but it may need to be evacuated if Ishvi attacks full force.}
					#width
					280
					#at
					-1
					8
				)
				(Print
					{Finding Julyn would be a great way to return to the peace treaty, but perhaps she doesn't even wish to be found.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '/fence')
				(Print
					{Not to be offensive, but this is for defense.}
					#width
					280
					#at
					-1
					8
				)
			)
			(if (Said '[/!*]')
				; this will handle just "look" by itself
				(Print
					{True to form, this training ground is getting extra use lately. These two for instance do not appear to be the conventional soldiers. Perhaps they have been conscipted or are volunteers from the village.}
					#width
					280
					#at
					-1
					8
				)
			)
		)
	)
)

(instance fatManScript of Script
	(properties)
	
	(method (changeState newState &tmp dyingScript)
		(= state newState)
		(switch state
			(1
				(fatMan setCycle: End fatManScript cycleSpeed: 4)
				(oldMan setCycle: End cycleSpeed: 4)
			)
			(2
				; (if(switchSides)
				(= randoOld (Random 0 3))
				(oldMan loop: [odds randoOld] cel: 0 setCycle: End)
				(= randoFat (Random 0 3))
				(fatMan
					loop: [evens randoFat]
					cel: 0
					setCycle: End fatManScript
				)
			)
			(3
				(= randoOld (Random 0 3))
				(oldMan loop: [odds randoOld] cel: 0 setCycle: End)
				(= randoFat (Random 0 3))
				(fatMan
					loop: [evens randoFat]
					cel: 0
					setCycle: End fatManScript
				)
			)
; (if(switchSides)
;                    = randoFat Random (1 5)
;                    (fatMan:loop(evens[randoFat])cel(0)setCycle(End fatManScript))
;                    (if (== randoFat 4)
;                        (oldMan:loop(7)cel(0)setCycle(End))
;                        = switchSides 0
;                    )(else
;                        = randoOld Random (1 5)
;                        (if (== randoOld 4)
;                            (++ randoOld)
;                        )
;                        (oldMan:loop(odds[randoOld])cel(0)setCycle(End))
;                    )
;                )(else
;                    = randoFat Random (1 5)
;                    (fatMan:loop(odds[randoFat])cel(0)setCycle(End fatManScript))
;                    (if (== randoFat 4)
;                        (oldMan:loop(8)cel(0)setCycle(End))
;                        = switchSides 1
;                    )(else
;                        = randoOld Random (1 5)
;                        (if (== randoOld 4)
;                            (++ randoOld)
;                        )
;                        (oldMan:loop(evens[randoOld])cel(0)setCycle(End))
;                    )
;                )
			(4
				(fatManScript changeState: 2)
			)
		)
	)
)

; (instance oldManScript of Script
;    (properties)
;    (method (changeState newState)
;        (var dyingScript)
;        = state newState
;        (switch (state)
;            (case 1
;                (oldMan:setCycle(End oldManScript)cycleSpeed(3))
;            )(case 2
;                = randoOld Random (1 5)
;                (oldMan:loop(evens[randoOld])cel(0)setCycle(End oldManScript))
;            )(case 3
;                = randoOld Random (1 5)
;                (oldMan:loop(evens[randoOld])cel(0)setCycle(End oldManScript))
;            )(case 4
;                (oldManScript:changeState(2))
;            )
;        )
;    )
; )
; Text Numbers that begin the taunts   *
; *      0, 4, 8, 12, 16, 19, 22 26                  *
; *
(instance tauntScript of Script
	(properties)
	
	(method (changeState newState)
		(= state newState)
		(switch state
			(0)
			(1 (= seconds 1))
			(2
				(= seconds 2)
				(= tauntNumber [TextNumFirstLine (Random 0 10)])
				(if (== tauntNumber noRepeat)
					(= tauntNumber [TextNumFirstLine (Random 0 10)])
				)
				(= noRepeat tauntNumber)
				(= respondNumber tauntNumber)
				(++ respondNumber)
				(linesCheck)  ; Checks to see how many lines in the dialog
				(branchCheck) ; Branch checks checks to see if a line of dialogue has different responses.
				(= rando (Random 0 1)) ; This determines who speaks the first line of dialog
				(switch rando
					(0
						(= gWndColor 7) ; clYELLOW
						(= gWndBack 1) ; clGREY
						(Print
							51
							tauntNumber
							#dispose
							#width
							140
							#at
							(- (oldMan x?) 30)
							70
						)
						(= gWndColor 0) ; clBLACK
						(= gWndBack 15)
					)               ; clWHITE
					(1
						(= gWndColor 14) ; clYELLOW
						(= gWndBack 8) ; clGREY
						(Print
							51
							tauntNumber
							#dispose
							#width
							140
							#at
							(- (fatMan x?) 40)
							70
						)
						(= gWndColor 0) ; clBLACK
						(= gWndBack 15)
					)
				)
			)                       ; clWHITE
			(3
				(= cycles 1)
				(if gPrintDlg (gPrintDlg dispose:))
			)
			(4
				(= seconds 2)
				(= respondNumber (+ respondNumber multRespVar))
				(switch rando
					(0
						(= gWndColor 14) ; clYELLOW
						(= gWndBack 8)  ; clGREY
						(Print
							51
							respondNumber
							#dispose
							#width
							140
							#at
							(- (fatMan x?) 40)
							60
						)
						(= gWndColor 0) ; clBLACK
						(= gWndBack 15)
					)                   ; clWHITE
					(1
						(= gWndColor 7) ; clYELLOW
						(= gWndBack 1)  ; clGREY
						(Print
							51
							respondNumber
							#dispose
							#width
							140
							#at
							(- (oldMan x?) 30)
							60
						)
						(= gWndColor 0) ; clBLACK
						(= gWndBack 15)
					)
				)
			)                           ; clWHITE
			(5
				(if gPrintDlg (gPrintDlg dispose:))
				(if (== numberOfLines 2)
					(tauntScript changeState: 1)
				else
					(= tauntNumber (+ tauntNumber 2))
					(tauntScript changeState: 6)
				)
			)
			(6
				(= seconds 2)
				(switch rando
					(0
						(= gWndColor 7) ; clYELLOW
						(= gWndBack 1) ; clGREY
						(Print
							51
							tauntNumber
							#dispose
							#width
							140
							#at
							(- (oldMan x?) 30)
							70
						)
						(= gWndColor 0) ; clBLACK
						(= gWndBack 15)
					)               ; clWHITE
					(1
						(= gWndColor 14) ; clYELLOW
						(= gWndBack 8) ; clGREY
						(Print
							51
							tauntNumber
							#dispose
							#width
							140
							#at
							(- (fatMan x?) 40)
							70
						)
						(= gWndColor 0) ; clBLACK
						(= gWndBack 15)
					)
				)
			)                       ; clWHITE
			(7
				(= seconds 3)
				(if gPrintDlg (gPrintDlg dispose:))
				(if (== numberOfLines 4)
					(= respondNumber (+ respondNumber 2))
					(switch rando
						(0
							(= gWndColor 14) ; clYELLOW
							(= gWndBack 8)  ; clGREY
							(Print
								51
								respondNumber
								#dispose
								#width
								140
								#at
								(- (fatMan x?) 40)
								60
							)
							(= gWndColor 0) ; clBLACK
							(= gWndBack 15)
						)                   ; clWHITE
						(1
							(= gWndColor 7) ; clYELLOW
							(= gWndBack 1)  ; clGREY
							(Print
								51
								respondNumber
								#dispose
								#width
								140
								#at
								(- (oldMan x?) 30)
								60
							)
							(= gWndColor 0) ; clBLACK
							(= gWndBack 15)
						)
					)
				else                        ; clWHITE
					(= numberOfLines 0) ; This needs to be removed when linesCheck Proc is finished
					(tauntScript changeState: 1)
				)
			)
			(8
				(if gPrintDlg
					(gPrintDlg dispose:)
					(tauntScript changeState: 1)
				)
			)
		)
	)
)

(procedure (linesCheck)
	(= numberOfLines 2)
	(cond 
		(
			(or
				(== tauntNumber 0)
				(== tauntNumber 16)
				(== tauntNumber 26)
			)
			(= numberOfLines 3)
		)
		((== tauntNumber 22) (= numberOfLines 4))
	)
)

(procedure (branchCheck)
	(cond 
		((== tauntNumber 12) (= multRespVar (Random 0 1)))
		((== tauntNumber 19) (= multRespVar (Random 0 1)))
		(else (= multRespVar 0))
	)
)

(instance oldMan of Act
	(properties
		y 165
		x 134
		view 229
		loop 1
	)
)

(instance fatMan of Act
	(properties
		y 165
		x 64
		view 228
	)
)
