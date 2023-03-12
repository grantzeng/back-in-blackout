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
    private int fp;
    private int upspeed;
    private int downspeed;

    public Connection(File source, File emptyFile, Server server, Server client,
            TransmissionManager transmissionManager) {
        this.source = source;
        target = emptyFile;
        this.server = server;
        this.client = client;
        this.transmissionManager = transmissionManager;
        fp = 0;
        upspeed = 0;
        downspeed = 0;
        fileSize = source.getSize();
    }

    public void setUpspeed(int upspeed) {
        this.upspeed = upspeed;
    }

    public void setDownspeed(int downspeed) {
        this.downspeed = downspeed;
    }

    public void transmit() {
        System.out.println(this + " is now transmitting");
        int bytes = Math.min(upspeed, downspeed);
        while (fp < fileSize && bytes > 0) {
            String letter = source.read(fp);
            System.out.println(this + " sending: " + letter);
            target.append(letter);
            fp++;
            bytes--;
        }

        if (fp == fileSize) {
            target.setStatus(FileStatus.COMPLETE);
            close();
        }
    }

    public void close() {
        transmissionManager.closeTransmission(this);
        server.unplug(this);
        client.unplug(this);
    }

}
