;;; Sierra Script 1.0 - (do not remove this comment)
(script# 973)
(include sci.sh)
(include game.sh)
(use main)
(use obj)
(use controls)


(local


	[msgBuf 250]
)
; snd

(class sciAudio
	(properties
		command -111
		fileName -111
		soundClass -111
		volume -111
		fadeInMillisecs -111
		fadeOutMillisecs -111
		loopFadeInMillisecs -111
		loopFadeOutMillisecs -111
		loopCount -111
		conductorFile -111
		playXFadeOutMillisecs -111  ; used only for playx command
	)
	
	(method (init &tmp hFile)
		(if (== soundClass -111) (= soundClass {noSoundClass}))
		; = volume gVolume
		(StrCpy @msgBuf {})
; if 'playx' (play exclusive), send command to stop all currently playing commands for sound class
		(if (== STRINGS_EQUAL (StrCmp command {playx}))
			(StrCat @msgBuf {(sciAudio\n})
			(StrCat @msgBuf {___command stop\n})
			(StrCat @msgBuf {___soundClass_})
			(StrCat @msgBuf soundClass)
			(StrCat @msgBuf {\n})
; fade if requested
			(if (!= playXFadeOutMillisecs -111)
				(StrCat @msgBuf {___fadeOutMillisecs_})
				(StrCat @msgBuf playXFadeOutMillisecs)
				(StrCat @msgBuf {\n})
			)
			(StrCat @msgBuf {)\n})
		)
		(StrCat @msgBuf {(sciAudio\n})
		(if
			(or
				(== STRINGS_EQUAL (StrCmp command {play}))
				(== STRINGS_EQUAL (StrCmp command {playx}))
			)
			(StrCat @msgBuf {___command play})
			(StrCat @msgBuf {\n})
			(StrCat @msgBuf {___fileName_})
			(StrCat @msgBuf {"})
			(StrCat @msgBuf fileName)
			(StrCat @msgBuf {"})
			(StrCat @msgBuf {\n})
			(StrCat @msgBuf {___soundClass_})
			(StrCat @msgBuf soundClass)
			(StrCat @msgBuf {\n})
		)
		(if
			(or
				(== STRINGS_EQUAL (StrCmp command {stop}))
				(== STRINGS_EQUAL (StrCmp command {change}))
			)
			(StrCat @msgBuf {___command stop})
			(StrCat @msgBuf {\n})
			(if (!= fileName -111)
				(StrCat @msgBuf {___fileName_})
				(StrCat @msgBuf {"})
				(StrCat @msgBuf fileName)
				(StrCat @msgBuf {"})
				(StrCat @msgBuf {\n})
			else
				(StrCat @msgBuf {___soundClass_})
				(StrCat @msgBuf soundClass)
				(StrCat @msgBuf {\n})
			)
		)
		(if (!= volume -111)
			(StrCat @msgBuf {___volume_})
			(StrCat @msgBuf volume)
			(StrCat @msgBuf {\n})
		)
		(if (!= fadeInMillisecs -111)
			(StrCat @msgBuf {___fadeInMillisecs_})
			(StrCat @msgBuf fadeInMillisecs)
			(StrCat @msgBuf {\n})
		)
		(if (!= fadeOutMillisecs -111)
			(StrCat @msgBuf {___fadeOutMillisecs_})
			(StrCat @msgBuf fadeOutMillisecs)
			(StrCat @msgBuf {\n})
		)
		(if (!= loopFadeInMillisecs -111)
			(StrCat @msgBuf {___loopFadeInMillisecs_})
			(StrCat @msgBuf loopFadeInMillisecs)
			(StrCat @msgBuf {\n})
		)
		(if (!= loopFadeOutMillisecs -111)
			(StrCat @msgBuf {___loopFadeOutMillisecs_})
			(StrCat @msgBuf loopFadeOutMillisecs)
			(StrCat @msgBuf {\n})
		)
		(if (!= loopCount -111)
			(StrCat @msgBuf {___loopCount_})
			(StrCat @msgBuf loopCount)
			(StrCat @msgBuf {\n})
		)
		(StrCat @msgBuf {)})
		(cond 
			((!= conductorFile -111)
				(if
				(!= NULL (= hFile (FOpen conductorFile fCREATE)))
					(FPuts hFile @msgBuf)
					(FClose hFile)
				)
			)
			(
				(!=
					NULL
					(= hFile (FOpen {sciAudio\\command.con} fCREATE))
				)
				(FPuts hFile @msgBuf)
				(FClose hFile)
			)
		)
	)
)
