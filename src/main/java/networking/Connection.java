package networking;

import managers.TransmissionManager;
import networking.File.FileStatus;

public class Connection {
    private File source;
    private File target;
    private Server server;
    private Server client;
    private TransmissionManager transmissionManager;
    private int fileSize;
    private int fp = 0;
    private int upspeed = 0;
    private int downspeed = 0;

    public Connection(File source, File emptyFile, Server server, Server client, TransmissionManager transmissionManager) {
        this.source = source;
        target = emptyFile;
        this.server = server;
        this.client = client;
        this.transmissionManager = transmissionManager;
        fileSize = source.getSize();
    }

    public void setUpspeed(int upspeed) {
        this.upspeed = upspeed;
    }

    public void setDownspeed(int downspeed) {
        this.downspeed = downspeed;
    }

    public void transmit() {
        int bytes = Math.min(upspeed, downspeed); 
        while (fp < fileSize && bytes > 0) {
            target.append(source.read(fp));
            fp++; 
            bytes--;
        }
        
        if (fp == fileSize) {
            target.setStatus(FileStatus.COMPLETE);
            close();
        }
    }
    
    public boolean closeIfOutOfRange() {
        if (!server.getOwner().canSendTo(client.getOwner())) {
            transmissionManager.closeTransmission(this);
            target.setStatus(FileStatus.TRANSIENT);
            server.unplug(this); 
            client.unplug(this, target);
            return true; 
        }
        return false; 
        
    }
    
    public void close() {
        transmissionManager.closeTransmission(this);
        server.unplug(this);
        client.unplug(this);
    }

}
