package networking;

import unsw.response.models.FileInfoResponse;

public class File {

    private String filename;
    private String data = "";
    private int size = 0;
    private FileStatus status;

    public enum FileStatus {
        COMPLETE, PARTIAL, TRANSIENT
    }

    public File(String filename, String content) {
        this.filename = filename;
        this.data = content;
        this.size = content.length();
        this.status = FileStatus.COMPLETE;
    }

    public File(String filename, int size) {
        this.filename = filename;
        this.size = size;
        this.status = FileStatus.PARTIAL;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isComplete() {
        return status == FileStatus.COMPLETE;
    }

    public int getSize() {
        return size;
    }

    /**
     * @pre position is a valid data index
     * @param position
     * @return
     */
    public String read(int position) {
        return data.substring(position, position + 1);
    }

    public String read(int start, int end) {
        return data.substring(start, end);
    }

    public void truncate() {
        data = "";
    }

    public void append(String string) {
        if (data.length() + string.length() > size) {
            System.out.println("Cannot append, overflows file size");
            return;
        }
        data += string;
    }
    
    public void setStatus(FileStatus status) {
        this.status = status; 
    }

    public FileInfoResponse getFileInfoResponse() {
        return new FileInfoResponse(filename, data, size, status == FileStatus.COMPLETE);
    }

}
