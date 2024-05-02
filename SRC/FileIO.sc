;;; Sierra Script 1.0 - (do not remove this comment)
;
; SCI Template Game
; By Brian Provinciano
; ******************************************************************************
; fileio.sc
; Contains a File I/O class to simplify the use of files and rid the need to 
; use kernel functions.
(script# FILEIO_SCRIPT)
(include sci.sh)
(include game.sh)
(use obj)





(class File of Obj
	(properties
		handle 0
	)
	
	(method (dispose)
		(self close:)
		(super dispose:)
	)
	
	(method (showStr strBuf)
		(Format strBuf {File: %s} objectName)
	)
	
	(method (open mode)
		(switch argc
			(0
				(= handle (FOpen objectName fOPENFAIL))
			)
			(1
				(= handle (FOpen objectName mode))
			)
			(else  (= handle NULL))
		)
		(if (== handle -1) (= handle NULL))
		(if handle (return self))
		(return NULL)
	)
	
	(method (write putStrings &tmp i)
		(if (not handle) (= handle (self open:)))
		(if handle
			(for ( (= i 0)) (< i argc)  ( (++ i)) (FPuts handle [putStrings i]))
		)
	)
	
	(method (read strBuf size)
		(if (!= argc 2) (return 0))
		(if (not handle) (= handle (self open: fOPENCREATE)))
		(if handle (return (FGets strBuf size)))
		(return 0)
	)
	
	(method (close)
		(if handle (FClose handle))
		(= handle NULL)
	)
)
