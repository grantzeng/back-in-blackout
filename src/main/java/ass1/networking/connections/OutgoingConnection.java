package ass1.networking.connections;

import ass1.file.File;

public class OutgoingConnection extends Connection {

    private IncomingConnection endpoint;
    private int fp;
    private int limit;

    public OutgoingConnection(Connection endpoint, File resource, String origin, int limit) {
        super(endpoint, resource, origin);
        fp = 0;
        this.limit = limit;
        setConnection(endpoint);

    }

    public void send() {

        if (fp == limit) {
            System.out.println("File has completed sending");
            getResource().setTransmissionStatus();
            return;
        }

        endpoint.receive(getResource().read(fp));
        logByteUsed();
        fp++;
    }

    @Override
    protected void setConnection(Connection endpoint) {
        this.endpoint = (IncomingConnection) endpoint;
    }

}
