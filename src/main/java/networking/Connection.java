package networking;

import networking.File.FileStatus;
import nodes.NetworkNode;

public class Connection {
    private NetworkNode from;
    private NetworkNode to;

    private Server server;
    private Server client;

    private File source;
    private File target;

    private int filesize;
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
        isActive = false;
        upspeed = 0;
        downspeed = 0;
    }

    public void addSource(File source) {
        this.source = source;
        filesize = source.getSize();
        filename = source.getFilename();
        fp = 0;
        if (target != null) {
            isActive = true;
        }
    }

    public void addTarget(File target) {
        this.target = target;
        if (source != null) {
            isActive = true;
        }
    }

    public void setUpspeed(int upspeed) {
        this.upspeed = upspeed;
    }

    public void setDownspeed(int downspeed) {
        this.downspeed = downspeed;
    }

    public void transmit() {

        if (!isActive()) {
            return;
        }

        server.giveUpBandwidth(this);
        client.giveDownBandwidth(this);

        if (!from.canCommunicateWith(to)) {
            // Out of range, close transmission
            server.removeUploadConnection(this);
            client.removeDownloadConnection(this, filename);
            isActive = false;
            return;
        }

        // Transmit bytes for the tick
        int bytes = Math.min(upspeed, downspeed);
        while (fp < filesize && bytes > 0) {
            String letter = source.read(fp);
            target.append(letter);
            fp++;
            bytes--;
        }

        if (fp == filesize) {
            // Transmission complete, free resources
            target.setStatus(FileStatus.COMPLETE);
            server.removeUploadConnection(this);
            client.removeDownloadConnection(this);
            isActive = false;
            return;
        }
    }

    public boolean isActive() {
        return isActive;
    }

}
