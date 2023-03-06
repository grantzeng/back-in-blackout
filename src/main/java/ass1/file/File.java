package ass1.file;

import unsw.response.models.FileInfoResponse;
import static ass1.file.TransmissionStatus.COMPLETE;
import static ass1.file.TransmissionStatus.PARTIAL;

public class File {
    private String filename;
    private String data = "";
    private int size = 0;
    private TransmissionStatus status;

    // Complete file constructor
    public File(String filename, String content) {
        this.filename = filename;
        this.data = content;
        this.size = content.length();
        this.status = COMPLETE;
    }

    // Empty file constructor for client receiving data
    public File(String filename, int size) {
        this.filename = filename;
        this.size = size;
        this.status = PARTIAL;
    }

    public String getFilename() {
        return filename;
    }

    public TransmissionStatus getStatus() {
        return status;
    }

    public int getSize() {
        return size;
    }

    public String read(int position) {
        return "";
    }

    public String read() {
        return data;
    }

    public void append(String string) {
        if (data.length() + string.length() > size) {
            System.out.println("Cannot append, overflows file size");
            return;
        }
        data += string;
    }

    public void setStatusComplete() {
        if (status == PARTIAL) {
            status = COMPLETE;
        }
        System.out.println("File is complete");
    }

    public FileInfoResponse getFileInfoResponse() {
        return new FileInfoResponse(filename, data, size, status == COMPLETE);
    }
}
