# Trying to figure out transmission mechanism 
Not obvious at the moment where I should distribute all this data throughout the system. 

This is a _separate_ issue to whether a transmission can be registered at all. Here we're supposing client/server already know a transmission is going to happen. 

It's not clear how I am meant to "mark a file" to be this or that. 

### Data and operations
- `inRange`: client/target is in range of server/sender 
    - _"If device goes out of range of a satellite during transfer the partially downloaded file should be removed from the recipient"_
    
- `(server) sending speed`, `(client) receiving speed`: "`transfer rate`" 
    - This information is used for bottlenecking the transmission in each tick. 
    - Don't want to share this information between network nodes so have to implement some kind of rate limiting behaviour
    
- The data to be sent in a single tick 
    - Who is responsible for figuring out what's needed...and how do they request this data?

### Potential objects

`Socket` objects, `Packet` objects -- but it's not obvious what this should look like. 

`Session` objects

Some kind of `FileWrapper` object to manage file I/O (contains a buffer, yields data that needs to be sent)
- `yield()`: yields a byte each time pressed (I like this solution, because we don't need to know beforehand how many bytes we can send/receive but somehow it keeps track of internal state of "where we're up to".) 
- need some way to set where file pointer should start (I think this is how elephant satellite could work)


### Enums

Enums for status
Enums for server responses
enums for network node type

### Questions

Can we upload the same file to different devices/satellites at the same time, or is it "locked"?
- We'll go take a break then come back to this question 
- Back from break: the answer is YES. 


# Conclusions: 
We need some kind of transmission object that abstracts away the transmission directly interacting with a source file. 

It's not entirely clear what it should look like. 



# 2023-02-26 2:52PM 
Post lunch musings. 

How do I cope with "goes out of range" 