package networking;

public class Connection {

    private File source; 
    private File target; 
    private Server server; 
    private Server client; 
    private int fileSize;
    private int fp; 
    private int upspeed; 
    private int downspeed;
    
    public void setUpspeed(int upspeed) {
        this.upspeed = upspeed;
    }
    
    public void setDownspeed(int downspeed) {
        this.downspeed = downspeed;
    }
    
    public void transmit() {
        
    }
    
    public void close() {
        server.closeConnection(this);
        client.closeConnection(this);
    }
    
}
