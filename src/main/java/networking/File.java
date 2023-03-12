package networking;

import unsw.response.models.FileInfoResponse;

public class File {
    private String filename;
    private String data = "";
    private int size = 0;
    private boolean isComplete;

    public File(String filename, String content) {
        this.filename = filename;
        this.data = content;
        this.size = content.length();
        this.isComplete = true;
    }

    public File(String filename, int size) {
        this.filename = filename;
        this.size = size;
        this.isComplete = false;
    }

    public String getFilename() {
        return filename;
    }

    public boolean isComplete() {
        return isComplete;
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

    public void setStatusComplete() {

        if (!isComplete) {
            System.out.println(this + " is now marked as complete");
            isComplete = true;
        }

    }

    public FileInfoResponse getFileInfoResponse() {
        return new FileInfoResponse(filename, data, size, isComplete);
    }

}
