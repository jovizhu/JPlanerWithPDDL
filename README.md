JPlan with PDDL input

I try to make it to support PDDL as an input. 
Now it supports. but when I test it, i found that the
Java GraphPlan is not a well finished planner.
There is some errors, in the leveloff funciton, duplicate checking for plan.
I believe there is some error in the search plan funcion.
   wei zhu


JPlan: Java GraphPlan Implementation

JPlan is a java implementation of the GraphPlan planning algorithm.
First release: April 2004.
JPlan is distributed under LGPL (see file LICENSE).

Running JPlan: 
1. add jplan.jar to your classpath
2. type:
   java JPlan operators_file facts_file [max_level]
  
 operatos_file: is a text file containing the STRIPS-like definitions of operators
 facts_file: is a text file containing the planning problem (set of objects, initial state, 
   and goal state).
 max_level: maximum number of planning graph level (default is 10)
3. If a valid plan could be generated then you should see it in the file "output.pln"
   also the created planning graph information is written in the file "output.txt".

for more information please check the examples directory.


Thank you for your interest in JPlan!

Contact The Author:
Yasser EL-Manzalawy
ymelmanz@yahoo.com
