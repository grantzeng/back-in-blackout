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