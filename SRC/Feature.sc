;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; feature.sc
; Contains the base class, Feature, as well as it's descendants, View, Prop, 
; Act and more. This is the main script for sprites (VIEWs).
(script# FEATURE_SCRIPT)
(include sci.sh)
(include game.sh)
(use main)
(use controls)
(use cycle)
(use obj)





(class Feature of Obj
	(properties
		y 0
		x 0
		z 0
		heading 0
	)
	
	(method (handleEvent pEvent)
		(return (pEvent claimed?))
	)
)


(class PV of Feature
	(properties
		y 0
		x 0
		z 0
		heading 0
		view 0
		loop 0
		cel 0
		priority -1
		signal 0
	)
	
	(method (init)
		(gAddToPics add: self)
	)
	
	(method (showSelf)
		(Print objectName #icon view loop cel)
	)
)


(class View of Feature
	(properties
		y 0
		x 0
		z 0
		heading 0
		yStep 2
		view 0
		loop 0
		cel 0
		priority 0
		underBits 0
		signal $0101
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		lsTop 0
		lsLeft 0
		lsBottom 0
		lsRight 0
		brTop 0
		brLeft 0
		brBottom 0
		brRight 0
	)
	
	(method (init)
		(= signal (& signal $7fff))
		(if (not (gCast contains: self))
			(= lsTop 0)
			(= lsLeft 0)
			(= lsBottom 0)
			(= lsRight 0)
			(= signal (& signal $ff77))
		)
		(BaseSetter self)
		(gCast add: self)
	)
	
	(method (dispose)
		(self startUpd: hide:)
		(= signal (| signal $8000))
	)
	
	(method (showSelf)
		(Print objectName #icon view loop cel)
	)
	
	(method (posn newX newY newZ)
		(if (>= argc 1)
			(= x newX)
			(if (>= argc 2)
				(= y newY)
				(if (>= argc 3) (= z newZ))
			)
		)
		(BaseSetter self)
		(self forceUpd:)
	)
	
	(method (stopUpd)
		(= signal (| signal 1))
		(= signal (& signal $fffd))
	)
	
	(method (forceUpd)
		(= signal (| signal $0040))
	)
	
	(method (startUpd)
		(= signal (| signal 2))
		(= signal (& signal $fffe))
	)
	
	(method (setPri newPriority)
		(cond 
			((== argc 0) (= signal (| signal $0010)))
			((== newPriority -1) (= signal (& signal $ffef)))
			(else (= priority newPriority) (= signal (| signal $0010)))
		)
		(self forceUpd:)
	)
	
	(method (setLoop newLoop)
		(cond 
			((== argc 0) (= signal (| signal $0800)))
			((== newLoop -1) (= signal (& signal $f7ff)))
			(else (= loop newLoop) (= signal (| signal $0800)))
		)
		(self forceUpd:)
	)
	
	(method (setCel newCel)
		(cond 
			((== argc 0) (= signal (| signal $1000)))
			((== newCel -1) (= signal (& signal $efff)))
			(else
				(= signal (| signal $1000))
				(if (>= newCel (self lastCel:))
					(= cel (self lastCel:))
				else
					(= cel newCel)
				)
			)
		)
		(self forceUpd:)
	)
	
	(method (ignoreActors fIGNORE)
		(if (or (== 0 argc) fIGNORE)
			(= signal (| signal $4000))
		else
			(= signal (& signal $bfff))
		)
	)
	
	(method (hide)
		(= signal (| signal 8))
	)
	
	(method (show)
		(= signal (& signal $fff7))
	)
	
	(method (delete)
		(if (& signal $8000)
			(if (& signal $0020)
				(if (not gArcStl)
					(gAddToPics
						add:
							((PV new:)
								view: view
								loop: loop
								cel: cel
								x: x
								y: y
								z: z
								priority: priority
								signal: signal
								yourself:
							)
					)
				)
			)
			(= signal (& signal $7fff))
			(gCast delete: self)
			(if underBits
				(UnLoad rsMEMORY underBits)
				(= underBits NULL)
			)
			(super dispose:)
		)
	)
	
	(method (addToPic)
		(if (not (gCast contains: self)) (self init:))
		(self signal: (| signal $8021))
	)
	
	(method (lastCel)
		(return (- (NumCels self) 1))
	)
	
	(method (isExtra fMAKE_EXTRA &tmp fIS_EXTRA)
		(= fIS_EXTRA (& signal $0200))
		(if argc
			(if fMAKE_EXTRA
				(= signal (| signal $0200))
			else
				(= signal (& signal $fdff))
			)
		)
		(return fIS_EXTRA)
	)
	
	(method (motionCue)
	)
)


(class Prop of View
	(properties
		y 0
		x 0
		z 0
		heading 0
		yStep 2
		view 0
		loop 0
		cel 0
		priority 0
		underBits 0
		signal 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		lsTop 0
		lsLeft 0
		lsBottom 0
		lsRight 0
		brTop 0
		brLeft 0
		brBottom 0
		brRight 0
		cycleSpeed 0
		script 0
		cycler 0
		timer 0
	)
	
	(method (doit)
		(if (& signal $8000) (return))
		(if script (script doit:))
		(if (and (& signal 4) (not (& signal 2))) (return))
		(if cycler (cycler doit:))
	)
	
	(method (handleEvent pEvent)
		(if script (script handleEvent: pEvent))
		(return (pEvent claimed?))
	)
	
	(method (delete)
		(if (& signal $8000)
			(self setScript: 0 setCycle: 0)
			(if timer (timer dispose:))
			(super delete:)
		)
	)
	
	(method (motionCue)
		(if (and cycler (cycler completed?))
			(cycler motionCue:)
		)
	)
	
	(method (setCycle theCycler)
		(if cycler (cycler dispose:))
		(if theCycler
			(self setCel: -1)
			(self startUpd:)
			(if (& (theCycler -info-?) $8000)
				(= cycler (theCycler new:))
			else
				(= cycler theCycler)
			)
			(cycler init: self &rest)
		else
			(= cycler NULL)
		)
	)
	
	(method (setScript theScript)
		(if (IsObject script) (script dispose:))
		(if theScript (theScript init: self &rest))
	)
	
	(method (cue)
		(if script (script cue:))
	)
)


(class Act of Prop
	(properties
		y 0
		x 0
		z 0
		heading 0
		yStep 2
		view 0
		loop 0
		cel 0
		priority 0
		underBits 0
		signal 0
		nsTop 0
		nsLeft 0
		nsBottom 0
		nsRight 0
		lsTop 0
		lsLeft 0
		lsBottom 0
		lsRight 0
		brTop 0
		brLeft 0
		brBottom 0
		brRight 0
		cycleSpeed 0
		script 0
		cycler 0
		timer 0
		illegalBits $8000
		xLast 0
		yLast 0
		xStep 3
		moveSpeed 0
		blocks 0
		baseSetter 0
		mover 0
		looper 0
		viewer 0
		avoider 0
	)
	
	(method (init)
		(super init:)
		(= xLast x)
		(= yLast y)
	)
	
	(method (doit &tmp oldLeft oldRight)
		(if (& signal $8000) (return))
		(= signal (& signal $fbff))
		(if script (script doit:))
		(if (and (& signal 4) (not (& signal 2))) (return))
		(if viewer (viewer doit: self))
		(cond 
			(avoider (avoider doit:))
			(mover (mover doit:))
		)
		(if cycler
			(= oldLeft brLeft)
			(= oldRight brRight)
			(cycler doit:)
			(if baseSetter
				(baseSetter doit: self)
			else
				(BaseSetter self)
			)
			(if (or (!= oldLeft brLeft) (!= oldRight brRight))
				(if (not (self canBeHere:)) (self findPosn:))
			)
		)
		(= xLast x)
		(= yLast y)
	)
	
	(method (posn newX newY)
		(super posn: newX newY &rest)
		(= xLast newX)
		(= yLast newY)
		(if (not (self canBeHere:)) (self findPosn:))
	)
	
	(method (setLoop theLooper &tmp pLooper)
		(if (== argc 0)
			(super setLoop:)
			(= pLooper NULL)
		else
			(cond 
				((not (IsObject theLooper)) (super setLoop: theLooper &rest) (= pLooper NULL))
				((& (theLooper -info-?) $8000) (= pLooper (theLooper new:)))
				(else (= pLooper theLooper))
			)
			(if pLooper
				(if looper (looper dispose:))
				((= looper pLooper) init: self &rest)
			)
		)
	)
	
	(method (delete)
		(if (& signal $8000)
			(if (!= mover -1) (self setMotion: 0))
			(self setAvoider: 0)
			(if baseSetter
				(baseSetter dispose:)
				(= baseSetter NULL)
			)
			(if looper (looper dispose:) (= looper NULL))
			(if viewer (viewer dispose:) (= viewer NULL))
			(if blocks (blocks dispose:) (= blocks NULL))
			(super delete:)
		)
	)
	
	(method (motionCue)
		(if (and mover (mover completed?)) (mover motionCue:))
		(super motionCue:)
	)
	
	(method (setMotion theMover)
		(if (and mover (!= mover -1)) (mover dispose:))
		(if theMover
			(self startUpd:)
			(if (& (theMover -info-?) $8000)
				(= mover (theMover new:))
			else
				(= mover theMover)
			)
			(mover init: self &rest)
		else
			(= mover NULL)
		)
	)
	
	(method (setAvoider theAvoider)
		(if avoider (avoider dispose:))
		(if (IsObject theAvoider)
			(if (& (theAvoider -info-?) $8000)
				(= avoider (theAvoider new:))
			else
				(= avoider theAvoider)
			)
		)
		(if avoider (avoider init: self &rest))
	)
	
	(method (ignoreHorizon fIGNORE_HORIZON)
		(if (or (not argc) fIGNORE_HORIZON)
			(= signal (| signal $2000))
		else
			(= signal (& signal $dfff))
		)
	)
	
	(method (observeControl bits &tmp i)
		(for ( (= i 0)) (< i argc)  ( (++ i)) (= illegalBits (| illegalBits [bits i])))
	)
	
	(method (ignoreControl bits &tmp i)
		(= i 0)
		(while (< i argc)
			(= illegalBits (& illegalBits (~ [bits i])))
			(++ i)
		)
	)
	
	(method (observeBlocks)
		(if (not blocks) (= blocks (Set new:)))
		(blocks add: &rest)
	)
	
	(method (ignoreBlocks)
		(blocks delete: &rest)
		(if (blocks isEmpty:)
			(blocks dispose:)
			(= blocks NULL)
		)
	)
	
	(method (isStopped)
		(if
			(or
				(not (IsObject mover))
				(and (== x (mover xLast?)) (== y (mover yLast?)))
			)
			(return TRUE)
		)
		(return FALSE)
	)
	
	(method (isBlocked)
		(return (& signal $0400))
	)
	
	(method (findPosn &tmp i j oldX oldY fCAN_BE_HERE)
		(= oldX x)
		(= oldY y)
		(= fCAN_BE_HERE FALSE)
		(for ( (= i 1)) (not fCAN_BE_HERE)  ( (++ i)) (for ( (= j 0)) (and (not fCAN_BE_HERE) (< j 8))  ( (++ j)) (= x
			(+ oldX (* i (IsPosOrNeg (CosMult (* j 45) 100))))
		) (= y
			(- oldY (* i (IsPosOrNeg (SinMult (* j 45) 100))))
		) (if (= fCAN_BE_HERE (self canBeHere:))
			(= fCAN_BE_HERE (self onControl:))
		)))
		(self posn: x y)
	)
	
	(method (inRect x1 y1 x2 y2)
		(if (and (<= x1 x) (< x x2) (<= y1 y) (< y y2))
			(return TRUE)
		)
		(return FALSE)
	)
	
	(method (onControl fUSE_POINT)
		(if (and argc fUSE_POINT)
			(return (OnControl ocSPECIAL x y))
		else
			(return (OnControl ocSPECIAL brLeft brTop brRight brBottom))
		)
	)
	
	(method (distanceTo pObj)
		(return (GetDistance x y (pObj x?) (pObj y?) gPicAngle))
	)
	
; ---------
	(method (canBeHere)
		(if baseSetter
			(baseSetter doit: self)
		else
			(BaseSetter self)
		)
		(if
			(or
				(and (== illegalBits 0) (& signal $2000))
				(CanBeHere self (gCast elements?))
			)
			(if
				(or
					(& signal $2000)
					(not (IsObject gRoom))
					(>= y (gRoom horizon?))
				)
				(if
				(or (== blocks 0) (blocks allTrue: #doit self))
					(if
					(and (>= x 0) (<= x 320) (>= y 0) (<= y 190))
						(return TRUE)
					)
				)
			)
		)
		(return FALSE)
	)
	
; LSL2's
; 		(if(baseSetter)
; 			(send baseSetter:doit(self))
; 		)(else
; 			BaseSetter(self)
; 		)
; 		(if(CanBeHere(self (send gCast:elements)) )
; 			(if((& signal $2000) or (not IsObject(gRoom)) or 
; 			    (>= y (send gRoom:horizon)) ) 
; 				(if((== blocks 0) or (send blocks:allTrue(#doit self)) )
; 					return(TRUE)
; 				)
; 			)
; 		)
; 		return(FALSE)
	(method (setStep newX newY)
		(if (and (>= argc 1) (!= newX -1)) (= xStep newX))
		(if (and (>= argc 2) (!= newY -1)) (= yStep newY))
		(if
		(and mover (!= mover -1) (mover isMemberOf: MoveTo))
			((self mover?) init:)
		)
	)
	
	(method (setDirection direction &tmp vanX vanY newX newY angle temp5)
		(= vanX (gRoom vanishingX?))
		(= vanY (gRoom vanishingY?))
		(if (and (== xStep 0) (== yStep 0)) (return))
		(= temp5 (/ 32000 (proc999_3 xStep yStep)))
		(switch direction
			(CENTER
				(self setMotion: 0)
				(return)
			)
			(UP
				(= newX (- vanX x))
				(= newY (- vanY y))
			)
			(DOWN
				(= newX (- x vanX))
				(= newY (- y vanY))
			)
			(RIGHT
				(= newX temp5)
				(= newY 0)
			)
			(LEFT
				(= newX (- temp5))
				(= newY 0)
			)
			(else 
				(if (< 180 (= angle (GetAngle x y vanX vanY)))
					(= angle (- angle 360))
				)
				(= angle
					(+ (/ (+ angle 90) 2) (* 45 (- direction 2)))
				)
				(= newX (SinMult angle 100))
				(= newY (- (CosMult angle 100)))
			)
		)
		(= temp5 (/ temp5 5))
		(while
		(and (< (Abs newY) temp5) (< (Abs newX) temp5))
			(= newX (* newX 5))
			(= newY (* newY 5))
		)
		(self setMotion: MoveTo (+ x newX) (+ y newY))
	)
)


(class Blk of Obj
	(properties
		top 0
		left 0
		bottom 0
		right 0
	)
	
	(method (doit pObj)
		(if
			(or
				(<= (pObj brBottom?) top)
				(> (pObj brTop?) bottom)
				(< (pObj brRight?) left)
				(>= (pObj brLeft?) right)
			)
			(return TRUE)
		)
		(return FALSE)
	)
)


(class Cage of Blk
	(properties
		top 0
		left 0
		bottom 0
		right 0
	)
	
	(method (doit pObj)
		(if
			(and
				(>= (pObj brTop?) top)
				(>= (pObj brLeft?) left)
				(<= (pObj brBottom?) bottom)
				(<= (pObj brRight?) right)
			)
			(return TRUE)
		)
		(return FALSE)
	)
)
