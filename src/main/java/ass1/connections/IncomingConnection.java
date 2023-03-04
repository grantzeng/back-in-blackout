package ass1.connections;

import ass1.file.File;

public class IncomingConnection extends Connection {

    private OutgoingConnection sourcepoint;
    private String buffer;

    public IncomingConnection(Connection endpoint, File resource, String origin) {
        super(endpoint, resource, origin);
        buffer = "";
    }

    public void receive(String ch) {
        if (ch.length() > 1) {
            throw new IllegalArgumentException("Can only receive one byte at a time");
        }

        if (bytesRemaining() == 0) {
            System.out.println("Rate limited by " + getOrigin());
            return;
        }

        buffer += ch;
        logByteUsed();
    }

    @Override
    protected void setConnection(Connection sourcepoint) {
        this.sourcepoint = (OutgoingConnection) sourcepoint;
    }
}
