package interfaces; 

import networking.Packet; 
import unsw.response.models.EntityInfoResponse;



/*
    Represents that an entity is able to transmit and receive packets
    - No real protocol other than send and acknowledge (if no ack assume link is dead)


 */

/*

    This interface is at a slightly lower abstraction than the others
    - but we need this because BlackoutController is responsible for ticking
    over the system -- I haven't figured out how to make the entities tick themselves
    over

 */

public interface Communicable {
    
    // This function causes network entity to flood all neighbours with file transfers
    public void broadcast(); 

    // This is the "port" that lets a network entity take in packets
    // - and then does processing with the packets (which is different depending on type
    //   e.g. relay satellite will forward it immediately)
    // All communication is by packet
    public void listen(Packet p); 

    // This function causes network entity to process and respond to all packets
    // it's received
    // - only call after broadcast (we may change it later where list)
    // - basically things
    // EDIT: Removed this function, because we can just broadcast an acknowledgement packet
    // public void acknowledge(); 

    // Unsure how this will work 
    // - but basically this should allow blackout controller to sync the node's 
    //   view of network topology with what blackout controller has 
    // - we should be able to modify this later so that blackout controller minimises
    //   the number of unnecessary packets sent by restricting how much informaiton each 
    //   node gets about the topology
    // - not sure about `create` - it could be delete instead
    // 
    public void sync(boolean add, Communicable node); 

    // This stupid getter exists because abstract class needs access to info from the concrete class
    // - we're working on a solution to try to get rid of it. 
    // UPDATE 2024-12-08: got rid of this by pushing it into the AbstractNode via the constructor
    // public String getId(); 

    public EntityInfoResponse getInfo(); 

}