# 2024-11-18 Mon
> Week 11, T3 2024 

Reread the assignment and see if we can rearchitect the system to work the way we want it to. 

There's three things I'm trying to learn here: 
- How to architect a non-trivial piece of software. 
- How to put together a system that is a working version every step of the way
- Tests and the role of test writing. (This is a big question mark for me) 

The point is: there is this jump between writing a glorified script (which I can do), and being able to put together a software system. You need to start small. Start with something the size of a uni assignment. Start with a webapp. Read about these things. There's all this practical know-how you need to learn which you haven't but which you can't really learn except by doing it yourself. 

I'm not going to lie, I'm full of anxiety and fear about whether I can do it but you just need to be able to sit with this - and just do it to show to yourself you can do things. 


# 2:18pm 
Coming up with a new design. 

Main behaviours to think about: 
- movement (this is encapsulateable in separate device/satellite classes) 
- detecting communicability/in-range
- file transmission

Basically `BlackoutController` should just add/remove devices and satellites and tick the system over. It shouldn't be involved in the communcations between objects. 

I think there needs to be some sort of handshake. Like the only role of the `BlackoutController` is that it should pass the reference of one node to another, and then leave the nodes to call each other in some kind of 'remote procedure call' back and forth situation/ping pong of calls. (This is what the `NetworkNode` abstraction should be for possibly - however I've read Ousterhout now and it's obvious this class is shallow) 
- Basically if goes out of range then the file and the reference to the other party in the connection should be deleted. 
- Whereas for something else it'll keep the reference but keep pinging it each time. 

I think let's start with carefully re-reading the constraints on transmission 

# Transmission constraints

Factors affecting whether a connection can be set up: 
- device/satellite compatibility
- whether they are "in range" 
    - different devices/satellites have different send/receive ranges
    - this is affected by relays
    - also affected by the planet 
    - also the satellites move, so things go in and out of range. 

> Basically `BlackoutController` is an application which is using this network and we need to provide an abstraction of the resources for it. 

Factors affecting an established connection 
- up/down down bandwidth of sender/receiver. 
- "in-rangeness" 

### What happens if "goes out of range" 

Actually I think the real issue here is: how does every node know what the whole topology of the network is. I mean the easy solution is to push this up to the `BlackoutController` but I'm saying - what if this was a real distributed system? How does every node _know_ the topology of the network? 

Basically: 
- Movement is straightforward - you can encapuslate this in different classes and just offer a `Movable` interface that BlackoutController can implement. 

Let's just try writing down all the pieces of data that affect whether a connection _can_ be set up -- we'll worry about bandwidth later. 


# Factors affecting whether a connection can be set up 
### Compatability 
> I think basically just forbid connections from happening if it doesn't meet compatability requirements
- satellite -> satellite OK
- device -> satellite OK
- satellite -> device OK 
- device -> device NOT ALLOWED

- `StandardSatellite` -> handhelds and laptops only (as well as other satellites)
- `TeleportingSatellite` -> 

### Straightline visibility
`MathsHelper.isVisible` will do the geometry for you so you don't need to figure it out. 

### Whether there is a relay
Relay satellite let you get the signal around the planet 

### Rangefulness
Different nodes have different sending/receiving ranges 

`HandheldDevice` - max range 50,000 km
`LaptopDevice` - max range 100,000 km
`DesktopDevice` - max range 200,000 km

`StandardSatellite` - max range 150,000 km
`TeleportingSatellite` - max range 200,000 km
`RelaySatellite` - max range 300,000km


> "If a `HandheldDevice` (range 50,000km) is 100,000km away from a `StandardSatellite` (range 150,000km) it can't send files _to_ the satellite, but it can receive files _from_ a satellite"

^This to me suggests that every node should know the whole topology. 

### How we could do this
Topology of the network. 

There's two possible ways: 
- We have a central node that stores the network topology - everyone just messages the central node. I.e. we have a client server architecture/a single clearing house kind of situation. This is easier to implement, but not scalable. Also it doesn't really match the spirit of the assignment, which is having a sort of P2P set up. 
- Versus we have a distributed system - everyone has their own copy of the network topology and somehow every node slowly gets updates about network topology. We simulate "cannot talk" by each node censoring themselves if certain node is not met.  I.e. we have some kind of P2P system. 


Central node gets a copy of the whole graph vs. everyone has a copy of the graph and slowly updates each other. 


### I think we'll break the problem down like this: 
- Movement is a separate issue and relatively straightforward. 
- File transmission, break this down into: 
    - how should we handle nodes detecting communicability (this is because the network topology is constantly changing - say we add a relay, or things move or whatever.)
    - how should we handle "connections"/data transmission
    - what happens if a connection is "lost" in some way. 


# 2024-12-02 Mon 

System redesign: 

The general advice is avoiding implementation inheritance.

I think I will have separate classes for each entity. 

Basically: 
- `BlackoutController` only ticks everything over. I.e. it interacts with the network only with various interfaces
- I think we need a packet object. This packet object should contain enough information for an entity to decide whether to process it or not. 

Basically I want lightweight abstractions, so to me it make more sense to have lots of interfaces (analogus to having things implementing different type classes in Haskell)

To get around the issue of there not being _enough_ information to do what operations a component needs, it makes more sense to me to oversupply data and then worry about how you can optimise this out later. For example, it's clear network nodes don't really need to know the whole network topology. But it's not clear to me _how_ I would implement this. 

I would also say that bad abstractions are criminally worse than code repetition. 

### 5:28pm 
This is also motivated to keep the design as flat as possible and with the least hierachy as possible. Because bad abstraction is just suffering and we need to keep the design as flexible enough to accomodate unexpected changes. 


It's clear to me that for example the movement and teleporting behaviour in the teleporting satellite should be encapsulated in a TeleportingSatellite class\

Not apparent to me whether some kind of file abstraction is useful in this case. 


### 5:52pm 
I think you should produce multiple designs for things on paper first before attempting to code anything up. I don't think I've yet established what the right abstractions of different resources are yet. This is a pen and paper task. Do it tomorrow morning. 


# 2024-12-05

### Rethinking
The issue is the communication between packets. 

We don't want the whole network topology in `BlackoutController` so somehow it has to be distributed among all the network nodes. Then the nodes have to have some way of communicating with each other 

```java

class Node: 
- Map<String, Node> topology;  // In current design pass in the whole network but later system should only pass in neighbours
- broadcast(); 
- accept(Packet p); 
- acknowledge();

```

Basically the issue is,  we don't want to store the whole network topology in `BlackoutController` - so it has to be distributed among network entities. I'm not sure how to do this encapsulation yet, hence we just give everyone a copy of the whole topology


### 1:51pm 
Seems like the frontend no longer works, but the backend is updating fine. I will debug the backend later but the point of today was to try to implement communication. It was supposed to be the case that having the frontend work is not essential. But otherwise everything is set up. 


### 2:36pm 

Packet sending seems to work. Basically write the code pretending the network was a complete graph and then we fiddle with the available nodes for each node so that each network node only sees neighbours. 
> Idea is entity reads packet header and then decides what to do with it. (makes sense to just throw away packets you're not interested in)

I think we'll have to push up all the packet sending/receiving to a class they all inherit from, but then override for any specific behaviours (e.g. like how teleporting interferes with packets sending)

Basically the main behaviours you have to have in this system are: 
- Network entities move 
- Network entities send packets to each other
- Network entities can have files uploaded onto them
- Network entities have to manage what packets they send out. 

> Re: elephant satellite - this means you can just pause sending packets out when out of range. For everything else, if you don't get an acknowledgement then assume the connection is broken and stop sending. I think the design works. 

I'll worry about the issue of testing later, what you're trying to learn at the moment is system design. 

# 2024-12-06
### 10:41am
Let's say we try to get this done by the end of next week. 

Things to implement today:
- movement
- file uploading
- packet sending 

My plan is: 
- try to push up the packet sending and processing to an abstract class because it's clear this function will basically be the same for every network node. 


### 11:06
I've introduced an abstract class. Good luck. 
- `protected` will let you pass abstract class state to a derived class state
- but there doesn't seem to be a good way of going the other way. 

Honestly, there's got to be a better way of doing this because you've introduced one level of hierachy which you don't 100% know will work or not. 

> For now this design works because I wanted to focus on packet sending but be prepared to burn it down. 

> Some judicious caution seems very necessary. Fools rush where angels fear to tread. 


### TODO: Find a better way that doesn't involve implementation inheritance

You just introduce a stupid artifical problem of how to pass information between the abstract class and subclass. And a whole load of pointless boiler plate. 

There has GOT to be a better way of modularising the code. 


### 11:14am 

Actually no: scrap this. 

The point is how do you give shared functionality to different classes. My conceptual model is we have six different entities and some of them we give different additional objects. 

I think it would be illuminating to go do some research about ways of walking around this problem. 

> For example I like how Haskell has light weight typeclasses so you can just sort of "put a piece of cellphane" over each object depending on how you want to access it. 

I'm sure as hell gonna try to walk around this issue. I refuse to write stupid boilerplate. 


### 11:25am 

> Ousterhout: design it twice. 

Draw out your current abstract class design 

I'm going to read the Parnas paper to see what he says. 

The problem is that if you draw the lines of the module wrong then you're fucked because the object might be missing some state it needs


movement
file transmission 
file storage

### 12:56pm 

I'm not really sure how to do this. What is apparent is the design of files, file storage etc. isn't clear. 

I think re transport - just notify by sending a packet. Basically network entities just have a stream of tokens they process which may change their internal state. 

For now just implement movement because this is all fairly self contained. 

### 1:00pm 

Getters and setters are not useful abstractions of a module. They're very shallow. Modules should be deep. 

### 1:45pm 
The implementation of movement is broken, but I'm trying to focus more on the issue of modularising code, not specific algorithmic things. 

Some of the angle code should be replaced by just using `Angle`. 


### 2:16pm 

The issue is how do you do this without an abstract class? It's clear you want to reuse the transmission code. 

If you naively use any of the design patterns, they draw too many walls around so you cause yourself problems later on. 

The problem is just unsolved for now. We'll come back to it later. 

# 2024-12-08
### Trying to redesign without inheritance

"The Flaws of Inheritance"
> https://www.youtube.com/watch?v=hxGOiiR9ZKg 

"Dependency Injection, the best pattern" 
> https://www.youtube.com/watch?v=J1f5b4vcxCQ

Can we somehow get rid of the abstract class and make modules that can be composed for file management, file transmission etc. Basically the alternative to implementation inheritance is composition + interfaces. I don't think I have the right abstractions. 



## Still having trouble trying to come up with a reasonabel 
Let's just move back to files so we can add files to devices

Regarding issue of things teleporting:
- This is a complication you can account for later.



### 5:24pm 
Let's just - instead of moving everything into a separate file, let's just write everything in abstract node and figure out how to refactor out the inheritance later. 

> The issue is basically I don't know how to make the composition work and because all these features need sharing of state... so I'm just going to put it all into one thing. 


Let's just used `protected` and the simple class hierachy I have now. I really intensely dislike inheritance but let's just see if we can make it work



### 6:35pm 

System now takes in files. 

Honestly for the sake of making life easier I've just pushed everything up into the `AbstractNode` class. This is probably the easiest solution when I don't know how I should draw the lines between these modules and I remember when I originally did this assignment that was the issue, it seemed like people were more concerned about some stupid aesthetic issue of a class being "too big" over what Ousterhout says that "modules should be deep".  The problem with drawing lines is that you end up wasting time writing pointless boilerplate to just pass information between two modules

Currently abstract node offers this interface
```
AbstractNode: 
- broadcast // send out packets
- listen(p) // receive packets into a buffer
- sync      // update current info re network topology
- upload    // add file to a node (Issue: currently does not forbid satellites from having files added to them )

```

Things I'm going to do: 
- I don't know _how_ to draw the walls, so I'm not going to draw them at all. If it's apparent walls should be drawn between the six types of network nodes, then I will draw them. But drawing them upfront is just a recipe for stupidity. Just get something working

> It's why when I was doing a 6080 assignment I decided NOT to use some sort of intermediate state component and just use Redux because then you walk around the issue of having the blast holes through bad abstractions. 

> If it's likely to have to share state, just put it in the same module. 

> HOWEVER, the problem with deep modules is they're going to be an absolute pain to test. So while you may get working code for this mini project YOU should look at ways of partitioning it to make the bloody thing testable. This is a tradeoff. 

> The old design was populated like 75% with getters and setters and other such useless boilerplate. Which is probably an indication that you drew the walls wrong when trying to partition state. ***But the _REASON_ the walls were drawn all stupidly was because you were TOLD that LOTS OF WALLS were GOOD!***

- Packet idea with a seq number and ack flag saves you the trouble of manually computing the minimum - which is what the `Connection` object was doing in the previous design. (but at the time of doing this course you hadn't done networks yet)

### Summary of my current strategy

Basically, keep working on it the obvious way to you (which is `AbstractNode` should do all the operations we need.)
> at this point the inheritance could probably just be replaced with a strategy pattern since the only situations where the type of abstract node matter is (1)movement (2) whether file sending is permitted whatsoever

But then you ought to think about what the costs are of doing it with this design

### Things to do
Current solution seems like it should work but
- get rid of the inheritance, is there a good way of replacing `AbstractNode` with something like a composition of a server, a motor class etc. 
- is there a way of replacing my current solution of just pushing all the state upwards into one class, with building individual modules that can somehow share state? (i.e. is there a way of rewriting my current design in terms of composition without having this combinatorial number of stupid boilerplate functions that just pass data around)
- also need to redesign it to make it testable

> Walking around the problem of blasting walls between modules that need to share state by not putting the friggin walls there in the first place 



# 2024-12-04 

### Survey what's left to do
> Basically the point of this exercise is thinking about how to modularise code. After I've learnt that skill, then I want to think about testing and the purpose and role of testing 

(Testing seems kind of pointless unless (a) you're refactoring/modifying existing code (so you need to make sure it won't break) and (b) you have a conceptual clarity about how some component _must_ behave) 

(Also like that random comment I read on a Primagen video reacting to the Youtube video above: the problem is that when you start, you don't know all the possible details of your system so you are sort of forced to just build bottom up; in theory with inheritance you could do it if you did - but it's very top down, so you're stuck when you have to add something to the system) 

(I do need some way of replacing the `AbstractNode` class with composition - like is there a way of building a new class from modules and getting them to share state nicely without ending up having to blow a combinatorial C(n, 2) number of holes to make it work?)

Basically the remaining features are: 
- `communicableEntitiesInRange` (Task 2b) 
- `sendFile` (Task 2c) 

The problem you're trying to solve how do you modularise it such that the data is available for all the operations. Like ideally you want it modular as possible i.e. push things down and isolate them rather than having shared state because this makes it more component wise and easier to test. Again the problem of drawing walls wrong. 

# `communicableEntitiesInRange`
Where should this behaviour go? 

I can see two possible solutions:
(1) we just do a graph search in `BlackoutController` because it has access to the whole network topology. 
    - This was my original solution, basically I had a `CommunicabilityManager` class that just populated the nodes with visible information. But the thing I don't like is that essentially this is giving the job to `BlackoutController` rather than forcing the network to just do this itself. 
(2) somehow you do it with packets in a distributed way. (how you'd prefer to do it)

There should be some way of subscribing/unsubscribing entities from each other. Currently `BlackoutController` gives the whole network topology, but really a node only should have access to its immediate neighbour 

> I think it's reasonable that when an entity is first added that `BlackoutController` because it has access to the whole graph figures out who's its neighbours will be; but after that the system should just tick itself over.

First I'm going to write out the restrictions on communications 

### Restrictions/allowances on communication 

Permissions
```
Target needs to be within range of the source (but source does not need to be within range of target)

All satellites can send files to other satellites
```
Restrictions
```
Devices cannot send files to other devices 

StandardSatellites cannot communicate with DesktopDevices (can only communicate with HandheldDevices and LaptopDevices)

(Teleporting satellite has no restrictions on devices it can communicate with)


```

Sending ranges
```
HandheldDevice - 50_000km 
LaptopDevice - 100_000km
DesktopDevice - 200_000km 
StandardSatellite - 150_000km 
TeleportingSatellite - 200_000km
RelaySatellite - 300_000km //basically only forwards packets 
```

Bandwidths



### Do we need a distributed hash table? 

The problem with giving everyone a copy of the whole topology is that it's not going to be scalable. 


This being said, if we're supposed to avoid putting logic in `BlackoutController`, then it's the manager's job to push the whole graph into the nodes. 

I think the easiest solution is that every node gets the whole topology, and then each node is responsible for censoring itself. 


### No we don't.

The simplest solution is - to avoid logic in `BlackoutController`, `BlackoutController` just passes in the whole network topology and the abstract node itself can figure out what to do with that. Oversupplying data when the design is unclear is probably a good idea, we can figure out how to minmimise the amount of logic in `BlackoutController` and minimise how much each node knows about the topology later. 

I think we may be able to just get rid of the `Communicable` interface? Not 100% sure. 



### 11:04am how to maintain encapsulation of topology. 

Basically I don't want a node to have references to objects it has no need to know about. The problem is this dynamically changes at every tick and the issue is that only `BlackoutController` knows the global state

So my solution is: 
- `BlackoutController` passes in the entire topology
- Every node is itself responsible for filtering out references it has no need to know about. So the idea is we only maintain local topology in the node and don't have references to things we do not need (because it will be just like a Chekov's gun at some point). 

We can always update the `sync` function later if we want to turn the faucet on more re: how much topology a node needs to know about. But it _ought_ to be as minimal as possible but flexible to change later. 

Basically the point here is (1) how to maintain encapsulation but (2) without shooting yourself in the foot because you don't have all the data you need to do what you need to do. 


