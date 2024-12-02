/*
    Represents that an entity is able to transmit and receive packets
    - No real protocol other than send and acknowledge (if no ack assume link is dead)

 */

public interface Communicable {
    
    // This will cause an entity to send packets to everyone it has access to
    // - basically this is sending all the SYN packets
    public void broadcast(); 

    // This should cause an entity to respond to all the packets it's received
    // - basically this will send all the ACK packets
    public void acknowledge(); 

}