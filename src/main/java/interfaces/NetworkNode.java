package interfaces; 


/*
    Interface that represent the type a network node is. 
    - Conceptually I don't really see much difference between satellites and devices and
    attempting to use implementation inheritance just causes endless suffering. It is just
    an irrational and arbitrary decision whether something is a satellite or a device
    so rather than making them separate types it would be better to just capture 
    this in the interface

    The distinction between satellites is wholly stupid and arbitrary so only just capture it with this interface
 */

public interface NetworkNode {
    // Returns the informal type of the network node
    // - is a lightweight abstraction instead of my attempt at using classes
    // public String type(); 

    public String getId(); 
}