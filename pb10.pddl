;; original name rocket_ext.a
;;

(define (problem pb1)
  (:domain logistics)
  (:requirements :strips :typing) 
  (:objects mxf - package
	    avrim - package
	    airplane1 - airplane
	    airplane2 - airplane
	    par-airport -  airport
	    jfk-airport -  airport
	    bos-airport -  airport)
  (:init
	 (at airplane1 bos-airport)
	 (at airplane2 par-airport)
	 )
  (:goal (and 
	  (at airplane2 bos-airport)
	  (at airplane1 jfk-airport)
	  )
	 )
  )