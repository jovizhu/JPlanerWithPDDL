;; logistics domain
;;
;; logistics-typed-length: strips + simple types
;;    based on logistics-strips-length.
;; Tue Dec  1 16:10:25 EST 1998 Henry Kautz

(define (domain logistics)
  (:requirements :strips :typing :negative-preconditions) 
  (:types 
  	package location vehicle - object
  	truck airplane - vehicle
  	city airport - location)
  
  (:predicates 	
		(at ?vehicle-or-package - (either vehicle package)  ?location - location)
		(in ?package - package ?vehicle - vehicle)
		(in-city ?loc-or-truck - (either location truck) ?citys - city))

  (:action fly-airplane
	:parameters
		(?airplane - airplane
		 ?loc-from - airport
		 ?loc-to - airport)
	:precondition
		(at ?airplane ?loc-from)
	:effect
		(and 	(not (at ?airplane ?loc-from)) 
		(at ?airplane ?loc-to)))
)
