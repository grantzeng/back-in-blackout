package networking;

import managers.TransmissionManager;
import networking.File.FileStatus;

public class Connection {
    private File source;
    private File target;
    private Server server;
    private Server client;
    private int fileSize;
    private int fp = 0;
    private int upspeed = 0;
    private int downspeed = 0;

    public Connection(File source, File emptyFile, Server server, Server client) {
        this.source = source;
        target = emptyFile;
        this.server = server;
        this.client = client;

        fileSize = source.getSize();
    }

    public void setUpspeed(int upspeed) {
        this.upspeed = upspeed;
    }

    public void setDownspeed(int downspeed) {
        this.downspeed = downspeed;
    }

    public void transmit() {

    }
    
    public boolean closeIfOutOfRange(TransmissionManager transmissionManager) {
        if (!server.getOwner().canSendTo(client.getOwner())) {
            transmissionManager.closeTransmission(this);
            target.setStatus(FileStatus.TRANSIENT);
            server.unplug(this); 
            client.unplug(this, target);
            return true; 
        }
        return false; 
        
    }
    
    public void close(TransmissionManager transmissionManager) {
        transmissionManager.closeTransmission(this);
        server.unplug(this);
        client.unplug(this);
    }

}
