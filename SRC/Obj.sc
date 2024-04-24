;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; obj.sc
; The base script for all the classes. This contains many of the important 
; classes, including Obj and Event.
(script# OBJ_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use controls)

(public
	IsPosOrNeg 0
	proc999_2 1
	proc999_3 2
	EqualsAny 3
)




(class Obj
	(properties)
	
	(method (new)
		(return (Clone self))
	)
	
	(method (init)
	)
	
	(method (doit)
		(return self)
	)
	
	(method (dispose)
		(DisposeClone self)
	)
	
	(method (showStr strBuf)
		(StrCpy strBuf objectName)
	)
	
	(method (showSelf &tmp [strBuf 200])
		(Print (self showStr: @strBuf))
	)
	
	(method (perform pObj)
		(pObj doit: self &rest)
	)
	
	(method (isKindOf pObj &tmp hSuperClass)
		(= hSuperClass (self superClass?))
		(if (!= objectSpecies (pObj species?))
			(if (IsObject hSuperClass)
				(return (hSuperClass isKindOf: pObj))
			)
			(return FALSE)
		)
		(return TRUE)
	)
	
	(method (isMemberOf pObj)
		; Check if it's an instance
		(if (& (pObj -info-?) $8000)
			(if (not (& objectInfo $8000))
				(return (== objectSpecies (pObj species?)))
			)
		)
	)
	
	(method (respondsTo aSelector)
		(return (RespondsTo self aSelector))
	)
	
	(method (yourself)
		(return self)
	)
)


(class Code of Obj
	(properties)
	
	(method (doit)
	)
)


(class Collect of Obj
	(properties
		elements 0
		size 0
	)
	
	(method (dispose)
		(if elements
			(self eachElementDo: #dispose)
			(DisposeList elements)
		)
		(= elements 0)
		(= size 0)
		(super dispose:)
	)
	
	(method (showStr strBuf)
		(Format
			strBuf
			{%s\n[Collection of size %d]}
			objectName
			size
		)
	)
	
	(method (showSelf &tmp [temps 40])
		(Print (self showStr: @temps))
		(self eachElementDo: #showSelf)
	)
	
	(method (add nodes &tmp i)
		(if (not elements) (= elements (NewList)))
		(for ( (= i 0)) (< i argc)  ( (++ i)) (AddToEnd elements (NewNode [nodes i] [nodes i])) (++ size))
		(return self)
	)
	
	(method (delete nodes &tmp i)
		(for ( (= i 0)) (< i argc)  ( (++ i)) (if (DeleteKey elements [nodes i]) (-- size)))
		(return self)
	)
	
	(method (eachElementDo aSelector &tmp hNode nextNode nodeVal)
		(= hNode (FirstNode elements))
		(while hNode
			(= nextNode (NextNode hNode))
			(= nodeVal (NodeValue hNode))
			(if (not (IsObject nodeVal)) (return))
			(nodeVal aSelector: &rest)
			(= hNode nextNode)
		)
	)
	
	(method (firstTrue aSelector &tmp hNode nextNode nodeVal)
		(= hNode (FirstNode elements))
		(while hNode
			(= nextNode (NextNode hNode))
			(= nodeVal (NodeValue hNode))
			(if (nodeVal aSelector: &rest) (return nodeVal))
			(= hNode nextNode)
		)
		(return NULL)
	)
	
	(method (allTrue aSelector &tmp hNode nextNode nodeVal)
		(= hNode (FirstNode elements))
		(while hNode
			(= nextNode (NextNode hNode))
			(= nodeVal (NodeValue hNode))
			(if (not (nodeVal aSelector: &rest)) (return FALSE))
			(= hNode nextNode)
		)
		(return TRUE)
	)
	
	(method (contains aNode)
		(return (FindKey elements aNode))
	)
	
	(method (isEmpty)
		(if (== elements 0) (return TRUE))
		(return (EmptyList elements))
	)
	
	(method (first)
		(return (FirstNode elements))
	)
	
	(method (next pNode)
		(return (NextNode pNode))
	)
	
	(method (release &tmp hNode nextNode)
		(= hNode (FirstNode elements))
		(while hNode
			(= nextNode (NextNode hNode))
			(self delete: (NodeValue hNode))
			(= hNode nextNode)
		)
	)
)


(class List of Collect
	(properties
		elements 0
		size 0
	)
	
	(method (showStr strBuf)
		(Format strBuf {%s\n[List of size %d]} objectName size)
	)
	
	(method (at position &tmp hNode)
		(= hNode (FirstNode elements))
		(while (and position hNode)
			(-- position)
			(= hNode (NextNode hNode))
		)
		(NodeValue hNode)
	)
	
	(method (last)
		(return (LastNode elements))
	)
	
	(method (prev pNode)
		(return (PrevNode pNode))
	)
	
	(method (addToFront nodes &tmp lastParam)
		(if (not elements) (= elements (NewList)))
		(= lastParam (- argc 1))
		(while (<= 0 lastParam)
			(AddToFront
				elements
				(NewNode [nodes lastParam] [nodes lastParam])
			)
			(++ size)
			(-- lastParam)
		)
		(return self)
	)
	
	(method (addToEnd nodes &tmp paramCounter)
		(if (not elements) (= elements (NewList)))
		(= paramCounter 0)
		(while (< paramCounter argc)
			(AddToEnd
				elements
				(NewNode [nodes paramCounter] [nodes paramCounter])
			)
			(++ size)
			(++ paramCounter)
		)
		(return self)
	)
	
	(method (addAfter pNode nodes &tmp i hNode)
		(= hNode (FindKey elements pNode))
		(if hNode
			(-- argc)
			(for ( (= i 0)) (< i argc)  ( (++ i)) (= hNode
				(AddAfter elements hNode (NewNode [nodes i] [nodes i]))
			) (++ size))
		)
		(return self)
	)
	
	(method (indexOf pNode &tmp i hNode)
		(= i 0)
		(= hNode (FirstNode elements))
		(while hNode
			(if (== pNode (NodeValue hNode)) (return i))
			(++ i)
			(= hNode (NextNode hNode))
		)
		(return -1)
	)
)


(class Set of List
	(properties
		elements 0
		size 0
	)
	
	(method (showStr strBuf)
		(Format strBuf {%s\n[Set of size %d]} objectName size)
	)
	
	(method (add nodes &tmp i hNode)
		(if (not elements) (= elements (NewList)))
		(for ( (= i 0)) (< i argc)  ( (++ i)) (= hNode [nodes i]) (if (not (self contains: hNode))
			(AddToEnd elements (NewNode hNode hNode))
			(++ size)
		))
	)
)


(class EventHandler of Set
	(properties
		elements 0
		size 0
	)
	
	(method (handleEvent pEvent &tmp hNode nextNode nodeValue)
		(= hNode (FirstNode elements))
		(while (and hNode (not (pEvent claimed?)))
			(= nextNode (NextNode hNode))
			(breakif
				(not (IsObject (= nodeValue (NodeValue hNode))))
			)
			(nodeValue handleEvent: pEvent)
			(= hNode nextNode)
		)
		(pEvent claimed?)
	)
)


(class Script of Obj
	(properties
		client 0
		state -1
		start 0
		timer 0
		cycles 0
		seconds 0
		lastSeconds 0
		register 0
		script 0
		caller 0
		next 0
	)
	
	(method (init theClient theCaller theRegister)
		(if (>= argc 1)
			(= client theClient)
			(client script: self)
			(if (>= argc 2)
				(= caller theCaller)
				(if (>= argc 3) (= register theRegister))
			)
		)
		(self changeState: start)
	)
	
	(method (doit &tmp theTime)
		(if script (script doit:))
		(cond 
			(cycles (if (not (-- cycles)) (self cue:)))
			(seconds
				(= theTime (GetTime gtTIME_OF_DAY))
				(if (!= theTime lastSeconds)
					(= lastSeconds theTime)
					(if (not (-- seconds)) (self cue:))
				)
			)
		)
	)
	
	(method (dispose &tmp temp0)
		(if (IsObject script) (script dispose:))
		(if (IsObject timer) (timer dispose:))
		(if (IsObject client)
			(cond 
				((IsObject next) (= temp0 next))
				(next (= temp0 (ScriptID next)))
				(else (= temp0 0))
			)
			(client script: temp0)
			(cond 
				((not temp0))
; do nothing
				((== gRoomNumber gRoomNumberExit) (temp0 init: client))
				(else (temp0 dispose:))
			)
		)
		(if
		(and (IsObject caller) (== gRoomNumber gRoomNumberExit))
			(caller cue: register)
		)
		(= caller NULL)
		(= next NULL)
		(= client NULL)
		(= timer NULL)
		(= script NULL)
		(super dispose:)
	)
	
	(method (changeState newState)
		(= state newState)
	)
	
	(method (cue)
		(self changeState: (+ state 1) &rest)
	)
	
	(method (handleEvent pEvent)
		(if script (script handleEvent: pEvent))
		(return (pEvent claimed?))
	)
	
	(method (setScript scriptObj)
		(if (IsObject script) (script dispose:))
		(if scriptObj (scriptObj init: self &rest))
	)
)


(class Event of Obj
	(properties
		type 0
		message 0
		modifiers 0
		y 0
		x 0
		claimed 0
	)
	
	(method (new eventType &tmp hEvent)
		(= hEvent (super new:))
		(if argc
			(GetEvent eventType hEvent)
		else
			(GetEvent $7fff hEvent)
		)
		(return hEvent)
	)
)


(procedure (IsPosOrNeg aNumber)
	(if (< aNumber 0) (return -1))
	(return (> aNumber 0))
)


(procedure (proc999_2 param1 &tmp temp0)
	(if (!= argc 1)
		(if (< param1 (= temp0 (proc999_2 &rest)))
			(return temp0)
		)
	)
	(return param1)
)


(procedure (proc999_3 param1 &tmp temp0)
	(if
		(or
			(== argc 1)
			(< param1 (= temp0 (proc999_2 &rest)))
		)
		(return param1)
	else
		(return temp0)
	)
)


(procedure (EqualsAny aNumber numbers &tmp i)
	(for ( (= i 0)) (< i (- argc 1))  ( (++ i)) (if (== aNumber [numbers i])
		(if aNumber (return aNumber))
		(return TRUE)
	))
	(return FALSE)
)
