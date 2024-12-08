package networking; 

/*
    Network packets
    - it should contain every piece of information for a network entity
    to decide whether it wants to process a packet or not 

    Basically entity gets a packet, then it has to figure out whether it wants
    to ignore it or not


    UPDATE: 2024-12-08
    - remove `sourcetype` field. It should simply be the case the system forbids illegal connections due to incompatbile types
      so I will just focus this class on doing data transmission


*/

public class Packet { 

    // Communication class

    String to;      // What the communication is between 
    String from; 

    String url;     // Name of resource that we're sending from
    
    int seq;        //  the sequence number 
                    // (if ack is false, then this is the start of the data paylaod
                    // (if ack is true, then this the next position we expect from )

    boolean ack;     // whether or not the packet is a data send or whether it
                    // it's an acknowledgement we got data

    String data; // the actual payload (only used in data transmission)


    public Packet(String to, String from, String fname, int seq, boolean ack, String data) {
        this.to = to; 
        this.from = from 
        this.fname = fname; 
        this.seq = seq; 
        this.ack = ack; 
        this.data = data; 
    }
   
    public int size() { 
        return content.length();
    }

    public String toString() {
        return "Packet { " +
               "to: '" + to + "', " +
               "from: '" + from + "', " +
               "content: '" + content + "', " +
               "size: " + size() +
               " }";
    }
}