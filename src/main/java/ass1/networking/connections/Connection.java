package ass1.networking.connections;

import ass1.file.File;

/*
 * Interfaces with a file object to separate transmission from a resource or
 * transmission
 */
public class Connection {
    private File file;
    private int fp = 0; // file pointer
    private String buffer;

    public Connection() {
    }

    public String yield() {
        fp++;
        return null;
    }

}
