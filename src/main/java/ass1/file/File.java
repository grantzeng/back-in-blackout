package ass1.file;

import unsw.response.models.FileInfoResponse;
import static ass1.file.TransmissionStatus.COMPLETE;
import static ass1.file.TransmissionStatus.DOWNLOADING;

public class File {
    private String filename;
    private String data = "";
    private int size = 0;
    private TransmissionStatus transmissionStatus;

    // Complete file constructor
    public File(String filename, String data, int size) {
        this.filename = filename;
        this.data = data;
        this.size = size;
        this.transmissionStatus = COMPLETE;
    }

    // Empty file constructor for client receiving data
    public File(String filename, int size) {
        this.filename = filename;
        this.size = size;
        this.transmissionStatus = DOWNLOADING;
    }

    public String getFilename() {
        return filename;
    }

    public String read(int position) {
        return "";
    }

    public String read() {
        return data;
    }

    public String read(int start, int end) {
        return "";
    }

    public void append(String string) {
        if (data.length() + string.length() > size) {
            System.out.println("Cannot append, overflows file size");
            return;
        }
        data += string;
    }

    public FileInfoResponse getFileInfoResponse() {
        return new FileInfoResponse(filename, data, size, transmissionStatus == COMPLETE);
    }
}
