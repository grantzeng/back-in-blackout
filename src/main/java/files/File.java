package files; 
/*
    
    It's clear there should be some kind of file object
    

*/

import unsw.response.models.FileInfoResponse;

public class File { 
    
    public static final int COMPLETE = 1;
    public static final int PARTIAL = 0; 
    public static final int TRANSIENT = -1; 

    // public for dev purposes, I hate getters and setters they're just such a shallow abstraction
    public String fname; 
    public int size;            // final size, "disk allocation"
    public int status;

    public String data;         // data.length() will give actual current size

    // We may make other constructors later but for now just...keep it obvious

    public File(String fname, int size, int status, String data) { 
        this.fname = fname; 
        this.size = size; 
        this.status = status;  
        this.data = data; 
    }

    /*
    
        Interface for accessing file data

        TODO: REMOVE THE GUARD STATEMENTS and replace with something better
    
    */

    // read file content
    // - start at what letter in the data
    public String read(int fp, int bytes) {
        if (fp >= size) {
            System.out.println("Error: fp greater than size");
            return "NO DATA"; 
        } 
        
        if (fp + bytes >= size) {
            System.out.println("Error: requesting too many bytes starting at fp"); 
            return "NO DATA"; 
        }

        return data.substring(fp, fp + bytes); 

    }

    public void append(String fragment) { 
        if (fragment.length() + data.length() > size) {
            // assert this doesn't happen
            System.out.println("Error, cannot append exceeds diskspace?"); 
            return; 
        }

        data += fragment; 
    }

    public FileInfoResponse getFileInfoResponse() {
        return new FileInfoResponse(fname, data, size, status == COMPLETE);
    }
}