# Tasks for today
0. _What are the data, what are the operations?_ Try to figure out where the most logical place to put information is.
1. Identify all the entities + relationships
2. Do a rough UML of your whole system 

### Log
```
2023-02-26 12:56PM Am getting distracted. Focus on the entities first, investigate the details of transmission later. 

2023-02-26 01:13PM I think I've figured out what my issue is. The entities in the transmission stuff aren't 100% clear. 

Let's say today we aim for: 
- Figure out this on paper
- At least implement the motion and all the basic satellite stuff.
```
# Deconstructing the spec

## "Network nodes"


`Satellites, Devices`
- Have decided that they should be "network nodes" and as far as possible treated as "the same" to reduce code repetition.
```java
Device|Satellite
- position: Angle           // i.e. store as polar coordinates
- height: int 
- range: int                // is actually the _sending_ range. There's no limit to receiving range other than we have to be in the sending range of some server. 


Satellite:
// Motion constraints
// - also operates on position only
- linearVelocity: double   // store as double to avoid precision going to crap
+ move() 

// Transmission constraints
- uploadBandwidth: int 
- downloadBandwidth: int

// Server constraints
- maxStorage: int
- maxFiles: int 
```

- Current design idea is that I set `linearVelocity` of devices to `0.0` and the max up and max down bandwidths to  `Integer.MAX_VALUE` (to model " no restriction"), this is how I don't have to make devices and satellites conceptually different. (But check with the tutor if this is a "good design" idea)

- I wanted to use a template pattern and stuff lots of `protected` variables at the top but `protected` is...best avoided. Need to find alternative solution. 

- Obvious candidates for network nodes to involve themselves in `has-a` relationships e.g. a `Motion`, a `Server` and a `Transmitter` objects -- but I don't want to put up walls yet. 


## Files 
`Files` 
Forget about the unusual teleporting behaviour for now, just focus on the basic operations we should offer on a file 
```java
File
- filename: String          // unique across files -> use as primary key?
- data: String              // Basically: `[a-zA-Z0-9_]*` (alphanumeric plus spaces) 
- size: Integer             // bytes of complete content
+ File()                
+ append(String data)       // 
+ read(int start, int end)  // End is exclusive
//+ read()                  // Gives you whole contents
- corrupt()                 // should only be available to devices somehow? removes all the t's
+ getFileInfoResponse()     
```
- No other obivous operations/data that we'd want to track within a file object, but option is here to implement a proper file with all the opts stuff you learnt in 1521. I'm just not sure if there's any advantage in that

Added 2023-02-26 1:36PM: File is not really a "file" in the conventional sense, we just want a container that manages some string data in this system. 


`BlackoutController`: the god of this subsystem
- _Idea_: At each tick, controller pushes all the relevant global information into each network node

```java
BlackoutController
- Map<String, NetworkNode> nodes    // All the nodes in our system
- clock: int                        // System clock -- mostly just for my debugging
```
### File transmission 
```
```

## Transmission 
Not quite sure where to put these data, but some transmission characteristics were
```java
?
// basically bandwidths are floor divisioned across files being processed
- upspeed: Integer        
- downspeed: Integer      
```

## The transmission mechanism 
Two stages: 
- Resource checks
- Initiate a session 


### How will transmission work?
It seems like it should be a push system. Each tick, server _pushes_ data to client because `BlackoutController` has pushed that information to it. 
- If server doesn't have reference, then can't push. Client can tell if "out of range" because it'll realise it didn't 

### "If a device goes out of range during the transfer of a file..."
Client can determine this because they won't have received a transmission in a tick. 

# Other musings
### Some things that came up 


### Reminder re: small tasks
- Need to write an angle normalier. 
- Make an `enum` for satellite + device classes.


### Open questions 
- Is it bad design 