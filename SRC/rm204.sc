;;; Sierra Script 1.0 - (do not remove this comment)

(script# 204)
(include sci.sh)
(include game.sh)
(use controls)
(use cycle)
(use feature)
(use game)
(use main)
(use obj)

(public
	invRegion 0
)

; USE INVENTORY ITEMS IN ROOMS REGION
; BY RYAN SLATTERY

(instance invRegion of Rgn
	(properties)
	
	(method (handleEvent pEvent &tmp i)
		(super handleEvent: pEvent)
		(if (Said 'open/kit') (PrintItem 1))
		(if (Said 'give/flower')
			(if (gEgo has: 21) (Print 204 24) else (PrintDHI))
		)
		(if (Said 'use,open/package')
			(if (gEgo has: 19)
				(Print 204 23 #width 280 #at -1 8)
			else
				(PrintDHI)
			)
		)
		(if
			(or
				(Said 'use,combine,tie/kite/bar,handle')
				(Said 'use,combine,tie/bar,handle/kite')
			)
			(if (and (gEgo has: 3) (gEgo has: 4))
				(Print 204 26)
				(gEgo get: 25 put: 3 50  put: 4 50)
			else
				(Print 204 22)
			)
		)
		(if (Said 'use>')
			(if (Said '/dart,dartgun') (PrintItem 8)) ; 8
			(if (Said '/marble') (PrintItem 9)) ; 9
			(if (Said '/meat') (PrintItem 5)) ; 5
			(if (Said '/letter') (PrintItem 6)) ; 6
			(if (Said '/magnet') (PrintItem 11)) ; 11
			(if (Said '/block') (PrintItem 13)) ; 13
			(if (Said '/acorn') (PrintItem 17)) ; 17
			(if (Said '/goggles') (PrintItem 10)) ; 10
			(if (Said '/ring') (PrintItem 18)) ; 18
			(if (Said '/glider') (PrintItem 25))
			(if (Said '/kite') (PrintItem 4))
			(if (Said '/shovel') (PrintItem 2)) ; 2
			(if (Said '/bar') (PrintItem 3)) ; 3
			(if (Said '/ruler') (PrintItem 14)) ; 14
		)
		(if (Said 'eat/meat') (PrintItem 5)) ; 5
		(if (Said 'shoot,drop>')
			(if (Said '/dart,dartgun') (PrintItem 8)) ; 8
			(if (Said '/marble') (PrintItem 9)) ; 9
		)
		(if (Said 'wear/ring')
			(if (gEgo has: INV_RING) (Print 204 19 #at -1 10) else (PrintCantDoThat))
		)
		(if (Said 'measure/dick')
			(if (gEgo has: INV_RULER) (Print 204 27 #at -1 10) else (PrintCantDoThat))
		)
		(if (Said 'measure/mustache')
			(if (gEgo has: INV_RULER) (Print 204 28 #at -1 10) else (PrintCantDoThat))
		)
		(if (Said 'wear,give>')
			(if (Said '/meat') (PrintItem 5)) ; 5
			(if (Said '/letter') (PrintItem 6)) ; 6
			(if (Said '/block') (PrintItem 13)) ; 13
			(if (Said '/acorn') (PrintItem 17)) ; 17
			(if (Said '/goggles') (PrintItem 10)) ; 10
			(if (Said '/ring') (PrintItem 18)) ; 18
		)
		(if (Said 'fly/kite') (PrintItem 4)) ; 4
		(if (Said 'dig') ; 2
			(if (gEgo has: 2)
				(Print 204 2 #width 280 #at -1 10)
			else
				(Print 204 20)
			)
		)
	)
)

(procedure (PrintItem itemNumber)
	(if (gEgo has: itemNumber)
		(Print 204 itemNumber #width 280 #at -1 10)
	else
		(PrintDHI)
	)
)