package interfaces; 


/*

    Represents that it's possible to upload a file direct to an entity

*/


public interface Uploadable {
    /*
        Lets you upload a file 
        - So for example, only devices would implement this interface
    */
    public String upload(String fname, String data);  

}