package ass1.connections;

import ass1.file.File;


public class SendingConnection extends Connection {
    
    private File resource; 
    private ReceivingConnection endpoint; 
    
    private int bytesAllocation; 
    private int bytesUsed; 
    
    private int transmissionSize; 
    private int fp; 
    
    private String destination; // For server network nodes to remove when client network node goes out of range 
    
    private boolean active;     // Set as active when it acquires an endpoint
    
    public SendingConnection(File source, String clientName, int transmissionSize) {
        resource = source; 
        this.destination = clientName; 
        bytesAllocation = 0; 
        bytesUsed = 0; 
        this.transmissionSize = transmissionSize;
        fp = 0; 
        active = false; 
    }

    public void connect(ReceivingConnection endpoint) {
        if (this.endpoint != null) {
            System.out.println("Sourcepoint has already acquired endpoint reference");
            return; 
        }
    
        this.endpoint = endpoint; 
        active = true; 
    }

    
    /*
        Piping data to endpoint
    */
    public ConnectionStatus usage() {
        if (bytesUsed == bytesAllocation) {
            return ConnectionStatus.RATE_LIMITED;
        }
        bytesUsed++;
        return ConnectionStatus.OK; 
    }
    
    
    public void send() {
        
        while (fp < transmissionSize && bytesUsed <= bytesAllocation) {
            
            String letter = resource.read(fp);
            
            if (endpoint.write(letter) != ConnectionStatus.OK) { 
                break; 
            }
                        
            bytesUsed++; 
            fp++; 
        }
        
        if (bytesUsed >= bytesAllocation) {
            System.out.println("Rate limited by server");
            return;
        }
        
        if (fp == transmissionSize) {
            System.out.println("All bytes have been sent already");
            active = false; 
            return; 
        }
    
    }
    
    public boolean isFinished() {
        return active == false; 
    }
    
    public void reset() {
        this.bytesUsed = 0; 
    }

    
}
