package networking;

import managers.TransmissionManager;

public class Connection {
    private File source;
    private File target;
    private TransmissionManager transmissionManager;
    private Server server;
    private Server client;
    private int fileSize;
    private int fp = 0;
    private int upspeed = 0;
    private int downspeed = 0;

    public Connection(File source, File emptyFile, Server server, Server client,
            TransmissionManager transmissionManager) {
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

    }

    public void close(TransmissionManager transmissionManager) {
        transmissionManager.closeTransmission(this);
        server.unplug(this);
        client.unplug(this);
    }

}
