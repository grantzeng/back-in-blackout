package ass1.connections;

import ass1.file.File;

public class ReceivingConnection extends Connection {

    private File resource; 
    
    private int bytesAllocation; 
    private int bytesUsed; 
    
    private int transmissionSize; 
    private int fp; 
    
    private String destination;
    
    private boolean hit; 
    private boolean active;
    
    public ReceivingConnection(File target, String clientName, int transmissionSize) {
        this.resource = target; 
        this.destination = clientName;
        bytesAllocation = 0; 
        bytesUsed = 0; 
        this.transmissionSize = transmissionSize;
        fp = 0; 
        active = true; 
    }
    
    public ConnectionStatus write(String character) {
    
        if (!active) {
            System.out.println("Socket is inactive");
            return ConnectionStatus.OK;
        }
        
        if (bytesUsed >= bytesAllocation) {
            System.out.println("Rate limited by client");
            return ConnectionStatus.RATE_LIMITED;  
        }
            
        resource.append(character);
            
        hit = true; 
        bytesUsed++; 
        fp++; 
    
        if (fp == transmissionSize) {
            active = false; 
            resource.setStatusComplete();
            return ConnectionStatus.COMPLETE;
        }
        
        return ConnectionStatus.OK;
    }
    
    public boolean isFinished() {
        return (active == false) || (hit == false && active == true);
    }
    
    public void reset() {
        this.bytesUsed = 0; 
        hit = false; 
        
    }
    
    
    
}
