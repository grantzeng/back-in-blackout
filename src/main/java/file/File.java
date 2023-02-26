package file;

import unsw.response.models.FileInfoResponse;

public class File {

    private String filename; 
    private String data = ""; 
    private int size = 0;
    
    // Complete file constructor
    public File(String filename, String data, int size) {
        this.filename = filename; 
        this.data = data; 
        this.size = size; 
    }
    
    // Empty file constructor for client receiving data
    public File(String filename, int size) {
        this.filename = filename; 
        this.size = size;
    
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
        if (data.length() + string.length() < size) {
            data += string; 
        }
        System.out.println("Cannot append, overflows file size");
    }
    
    public FileInfoResponse getFileInfoResponse() {
        return null;
    }
}
