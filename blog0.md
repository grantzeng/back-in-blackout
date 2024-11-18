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

