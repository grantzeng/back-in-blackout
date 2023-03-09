# 2023-02-26
### 3:55PM
The issue I'm running into here is: 
- "client goes out of range of server" -- what do we do? 
- "server finishes transmission" -- what do we do?

I think because the `NetworkNode` keeps track of who it can communicate with, it's each nodes' responsibility to clean up any references to other nodes they're not allowed to talk to.  It's not `BlackoutController`'s job, because it doesn't have access to compatability information, only visibility. 

Maybe `Socket`/`Session`/`Buffer` -- I haven't decided what to call this object, but basically it interfaces with the source file in the server node and the destination file in the client node -- but I think this should be what keeps track of whether things are uploading, downloading or transient etc.

We'll go do some groceries shopping and then come back and implement two things: 
- communicablity of classes
- motion 


### 5:00PM 
I have to just accept I'm not going to resolve this issue today. 


However I think I _have_ converged on what the problem is: 
1. What happens when client goes out of range of server?
    - My proposal is that it's the `Server` objects' responsibility to clean up invalid connections. I had some ugly ideas about storing it in a map, but I also don't want to give the connection object a reference to the WHOLE server. 
    - Somehow I want to tag these connection objects with what it's sending to so I can remove them all if out of range. 
2. What happens when a transmission is complete? 
    - Instruction has to be sent to client that we're done so they can clean up stuff, then we have to clean up the connection 
    
Maybe this object is best called a `Connection`. Or else two sockets make a connection?


# 2023-03-27

Spent all morning trying to figure out transmission. 

So far it's clear we need to have two connection objects that interface with each other. 

What I haven't figured out is:  1. how do I register them and 2. how do I get them to transmit 3. How do I remove the connection objects when not needed 

I've decided I'm just going to at least try to get all of task 1 done 



### 1:42PM 
The spec doesn't say anything about whether angles need ot be in degrees or radians, so I am deciding to store as radians. 

### 1:46PM 
Change of plan, it seems like in the front end and tests the angles inputted are in degrees



### 5:09PM Relay debugging

There's some buggy semantics I have to resolve. I don't think the test is wrong, I think I've mixed up clockwise and anticlockwise

-> delta is positive because it's calculated...but really it should be negative. 
Best way is to just make delta free of negative signs so we don't need to mess about.

The test is correct. 
