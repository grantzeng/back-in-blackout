package ass1.networking;

import ass1.file.File;

/*
 * Interfaces with a file object to separate transmission from a resource or
 * transmission
 */
public class Socket {
    private File file;
    private int fp = 0; // file pointer
    private String buffer;

    public Socket() {
    }

    public String yield() {
        fp++;
        return null;
    }

}
