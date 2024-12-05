package interfaces; 


/*

    Represents that it's possible to upload a file direct to an entity

*/


public interface Uploadable {
    // Returns the informal type of the network node
    // - is a lightweight abstraction instead of my attempt at using classes
    // Not 100% sure about the interface
    public String upload(String fname, String data);  
}