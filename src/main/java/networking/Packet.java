package networking; 

/*
    Network packets
    - it should contain every piece of information for a network entity
    to decide whether it wants to process a packet or not 

    Basically entity gets a packet, then it has to figure out whether it wants
    to ignore it or not


*/

public class Packet { 
    private String to; 
    private String from; 
    private String sourcetype; // source node type so receivers only selectively listen
    private String content; 

    public Packet(String to, String from, String content, String sourcetype) { 
        this.to = to; 
        this.from = from; 
        this.content = content; 
        this.sourcetype = sourcetype; 
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