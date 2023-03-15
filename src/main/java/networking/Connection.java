package networking;

import managers.TransmissionManager;
import networking.File.FileStatus;
import nodes.NetworkNode;

public class Connection {
    private NetworkNode from;
    private NetworkNode to;

    private Server server;
    private Server client;

    private File source;
    private File target;

    private int fileSize;
    private String filename;
    private int fp;
    private boolean isActive;

    private int upspeed;
    private int downspeed;

    public Connection(NetworkNode from, NetworkNode to, Server server, Server client) {
        this.from = from;
        this.to = to;
        this.server = server;
        this.client = client;
        fileSize = source.getSize();
        filename = source.getFilename();
        fp = 0;
        isActive = true;
        upspeed = 0;
        downspeed = 0;
    }

    public void addSource(File source) {
        this.source = source;
    }

    public void addTarget(File target) {
        this.target = target;
    }

    public void setUpspeed(int upspeed) {
        this.upspeed = upspeed;
    }

    public void setDownspeed(int downspeed) {
        this.downspeed = downspeed;
    }

    public void transmit() {
        System.out.println(this + " is now transmitting");

        if (!from.canCommunicateWith(to)) {
            // Out of range, close transmission
            server.removeUploadConnection(this);
            client.removeDownloadConnection(this, filename);
        }

        int bytes = Math.min(upspeed, downspeed);
        // Tranmit bytes
        while (fp < fileSize && bytes > 0) {
            String letter = source.read(fp);
            System.out.println(this + " sending: " + letter);
            target.append(letter);
            fp++;
            bytes--;
        }

        if (fp == fileSize) {
            // Transmission complete, free resources
            target.setStatus(FileStatus.COMPLETE);
            server.removeUploadConnection(this);
            client.removeDownloadConnection(this);
        }
    }

    public void reset() {
        setDownspeed(0);
        setUpspeed(0);
    }

}
