package ass1.networking.connections;

import ass1.file.File;

/*
 * Interfaces with a file object to separate transmission from a resource or
 * transmission
 */
public abstract class Connection {
    private File resource;
    private String origin;
    private int bytesRemaining = 0;


    public Connection(Connection connection, File resource, String origin) {
        this.resource = resource;
        this.origin = origin;
        setConnection(connection);
    }

    protected void logByteUsed() {
        if (bytesRemaining == 0) {
            throw new IllegalCallerException("No more bytes remaining this tick");
        }
        bytesRemaining--;
    }

    protected int bytesRemaining() {
        return bytesRemaining;
    }

    protected String getOrigin() {
        return origin;
    }

    protected File getResource() {
        return resource;
    }

    protected abstract void setConnection(Connection connection);
}


