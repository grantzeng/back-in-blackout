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