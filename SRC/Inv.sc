;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; inv.sc
; Contains the main classes for your game's inventory, one of the most essential 
; parts of an adventure game.
(script# INVENTORY_SCRIPT)
(include sci.sh)
(include game.sh)
(use obj)
(use main)
(use controls)
(use syswindow)


(local



	btnHandle =  0
)

(class InvI of Obj
	(properties
		said 0
		description 0
		owner 0
		view 0
		loop 0
		cel 0
		script 0
	)
	
	(method (showSelf)
		(if description
			(IconPrint description view loop cel)
		else
			(IconPrint objectName view loop cel)
		)
	)
	
	(method (saidMe)
		(return (Said said))
	)
	
	(method (ownedBy anObject)
		(return (== owner anObject))
	)
	
	(method (moveTo newOwner)
		(= owner newOwner)
		(return self)
	)
	
	(method (changeState newState)
		(if script (script changeState: newState))
	)
)


(class Inv of Set
	(properties
		elements 0
		size 0
		carrying {You are carrying:}
		empty {You are carrying nothing!}
	)
	
	(method (init)
		(= gInv self)
	)
	
	(method (showSelf theOwner)
		(invD text: carrying doit: theOwner)
	)
	
	(method (saidMe)
		(return (self firstTrue: #saidMe))
	)
	
	(method (ownedBy anObject)
		(return (self firstTrue: #ownedBy anObject))
	)
)


(instance invD of Dialog
	(properties)
	
	(method (init theOwner &tmp temp0 temp1 temp2 temp3 temp4 temp5 temp6)
		(= temp1 4)
		(= temp0 4)
		(= temp2 4)
		(= temp3 0)
		(= temp5 (gInv first:))
		(while temp5
			(= temp6 (NodeValue temp5))
			(if (temp6 ownedBy: theOwner)
				(++ temp3)
				(= temp4 (DText new:))
				(self
					add:
						(temp4
							value: temp6
							text: (temp6 name?)
							nsLeft: temp0
							nsTop: temp1
							state: 3
							font: gDefaultFont
							setSize:
							yourself:
						)
				)
				(if
				(< temp2 (- (temp4 nsRight?) (temp4 nsLeft?)))
					(= temp2 (- (temp4 nsRight?) (temp4 nsLeft?)))
				)
				(= temp1
					(+ temp1 (- (temp4 nsBottom?) (temp4 nsTop?)) 1)
				)
				(if (> temp1 140)
					(= temp1 4)
					(= temp0 (+ temp0 temp2 10))
					(= temp2 0)
				)
			)
			(= temp5 (gInv next: temp5))
		)
		(if (not temp3) (self dispose:) (return 0))
		(= window SysWindow)
		(self setSize:)
		(= btnHandle (DButton new:))
		(btnHandle
			text: {OK}
			setSize:
			moveTo: (- nsRight (+ 4 (btnHandle nsRight?))) nsBottom
		)
		(btnHandle
			move: (- (btnHandle nsLeft?) (btnHandle nsRight?)) 0
		)
		(self add: btnHandle setSize: center:)
		(return temp3)
	)
	
	(method (doit theOwner &tmp temp0 temp1 [ts 40])
		(if (not (self init: theOwner))
			(Print (gInv empty?))
			(return)
		)
		(self open: nwTITLE 15)
		(= temp0 btnHandle)
		(repeat
			(breakif
				(or
					(not (= temp0 (super doit: temp0)))
					(== temp0 -1)
					(== temp0 btnHandle)
				)
			)
			((temp0 value?) showSelf:)
		)
		(self dispose:)
	)
	
	(method (handleEvent pEvent &tmp temp0 temp1)
		(= temp0 (pEvent message?))
		(= temp1 (pEvent type?))
		(switch temp1
			(4
				(switch temp0
					($4800 (= temp0 $0f00))
					($5000 (= temp0 9))
				)
			)
			($0040
				(switch temp0
					(1 (= temp0 $0f00) (= temp1 4))
					(5 (= temp0 9) (= temp1 4))
				)
			)
		)
		(pEvent type: temp1 message: temp0)
		(super handleEvent: pEvent)
	)
)
