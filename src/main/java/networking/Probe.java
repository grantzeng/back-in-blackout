package networking; 
/*

    This class exists because it's an artefact 

 */
public class Probe { 

    // The hop information
    // - i.e. lower level
    public String from; 
    public String to; 

    // The network level data
    // - e.g. like TCP 
    public String id; 
    public String type;

    // Note: id and from will differ if more than 1 hop
    // Basically we have hop level information and also network level information 

    public Probe(String from, String to, String id, String type) {
        this.from = from; 
        this.to = to;  
        this.id = id; 
        this.type = type; 
    }
}