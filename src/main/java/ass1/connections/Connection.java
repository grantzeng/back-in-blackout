package ass1.connections;

import ass1.file.File;
import ass1.nodes.NetworkNode;
import static ass1.connections.ResponseCode.OK;
import static ass1.connections.ResponseCode.RATE_LIMITED;
import static ass1.connections.ConnectionType.RECIEVING;
import static ass1.connections.ConnectionType.SENDING;

/*
 * Designed class s.t. setting can make it a socket you read to or a socket you
 * write to - redesign this later.
 */
public class Connection {
    private File resource;
    private Connection endpoint;
    private NetworkNode origin;

    private int bytesAllocation = 0;
    private int bytesUsed = 0;

    // Writing to a socket
    private String buffer = "";

    // Reading from a file and writing to the opposite socket
    private int fileSize;
    private int fp;

    private boolean hit = false;
    private ConnectionType type;

    public Connection(File emptyFile, NetworkNode origin, int fileSize) {
        // Set up socket as a write resource
        this.resource = emptyFile;
        this.origin = origin;
        this.fileSize = fileSize;
        this.fp = 0;
        this.type = RECIEVING;
        System.out.println("receiving: " + this);
    }

    public Connection(File sourceFile, NetworkNode origin) {
        // Set up socket as a read resource
        this.resource = sourceFile;
        this.origin = origin;
        this.fileSize = resource.getSize();
        this.fp = 0;
        this.type = SENDING;
        // System.out.println("sending: " + this);
    }

    public ConnectionType getType() {
        return type;
    }

    public NetworkNode getOrigin() {
        return origin;
    }

    public void connect(Connection endpoint) {
        System.out.println("connect()");
        System.out.println("    " + this);
        System.out.println("    " + endpoint);

        if (this.endpoint != null) {
            System.out.println("Source already been set, cannot change");
            System.out.println("    I am: " + this);
            System.out.println("    Sending to: " + this.endpoint);
            return;
        }

        this.endpoint = endpoint;
    }

    // Implement rate limiting behaviour
    protected ResponseCode logByteUsage() {
        if (bytesUsed == bytesAllocation) {
            System.out.println("Used all available bytes");
            return RATE_LIMITED;
        }
        if (!hit) {
            hit = true;
        }

        bytesUsed++;
        return OK;
    }

    /*
     * Reading into a socket
     */

    public void giveBytesAllocation(int bytesAllocation) {
        this.bytesAllocation = bytesAllocation;
    }

    /*
     * Writes until rate limited
     */
    public ResponseCode write(String content) {
        for (String asciiChar : content.split("")) {
            if (logByteUsage() == RATE_LIMITED) {
                System.out.println("Cannot write any more to buffer");
                return RATE_LIMITED;
            }
            buffer += asciiChar;
        }
        return OK;
    }

    private void flush() {
        System.out.println("Flush buffer to file");
        resource.append(buffer);
        buffer = "";

    }

    public void send() {
        if (fp > fileSize) {
            System.out.println("File is complete");
            return;
        }

        // Keep trying to write to endpoint socket until rate limited
        String letter = resource.read(fp);
        while (endpoint.write(letter) != ResponseCode.RATE_LIMITED) {
            System.out.println("fp: " + fp + " " + letter);
            fp++;
        }

    }

    public void setByteAllocation(int bytesAllocation) {
        this.bytesAllocation = bytesAllocation;
    }

    public void reset() {
        this.bytesUsed = 0;
        flush();
        hit = false;
    }
}
