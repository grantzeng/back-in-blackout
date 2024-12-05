
<!--
You can view the assignment specification here: https://unswcse.atlassian.net/wiki/spaces/cs251123t1/pages/82903559/Assignment+I+Back+in+Blackout
---> 

### What's happening here? 
Revisiting an old assignment. 

The trouble with COMP2511 was that it was an OOP cargo cult and I learnt zip about how to slap a nice system together (it was clearly a course you either just had enough practical experience putting things together to know how to do it, or you just drowned. Guess what happened to me?)  

So, I'm reading Ousterhout's "A Philosophy of Software Design" and trying to put it into practice by thinking about modularising things from first principles. 

Also the assignment had a vaguely distributed systems flavour which is why I wanted to touch it again. 

### How to run it
> I'm still working on it
Download Gradle and `gradle run`, and the frontend should load. 


### Conclusions I've come to so far thinking about system design

The problem with software engineering is that because you have zero physical constraints the biggest constraint is understanding and fighting the complexity of the systems you build. 
- Fred Brooks: "conceptual integrity" 
- Software systems suffer badly from entropy, it's a house of cards if you're not careful and things go bad exponentially. Once it's spaghetti you can't fix it. 

Bad abstractions are worse than code repetition. 
- The whole naive "model domain objects with classes" thing is just absolute dogturds; a program is a data transformation not a taxonomy. 

Implementation inheritance is a recipe for sadness. Depend on interfaces. Interfaces are fantastic.  
- I really like the lightweight typeclasses.   
- All your resources should have nice interfaces so you can access them nicely. 

Sometimes the domain specialist's classifying of things is an accidental quality not an essential quality of a system. 

The only unquestionably useful thing about OOP is talking about encapsulation. However, because you don't know the design from scratch at the start it makes more sense to oversupply modules with data because you can always restrict it at a later date. 