public interface Movement {
    // 
    move()
}


public class Entity implements Movement {
    Angle position;
    int height; 
}

/*

    Registering stuff. 
        - source file store has to okay
        - dest file store has to okay
        - source transmission manager has to okay
        - dest transmission manager has to okay
        - create files + create connection 
    
    Ticking over
        - check if connection needs to be closed. 
        - source transmission manager gives resources to connection 
        - dest transmission manager gives resources to connection 
        - connection.transmit(); 
        
    closing
        - cleaning up files. 
        

*/


public class FileStore {

    NetworkNode owner; 
    
    int maxFiles; 
    int maxStorage; 
    
    Map<String, File> files; 
    
    void add(File file);
    void remove(File file);
    
    File store(File file);
        
    boolean exists(String filename); 
    
}

public class TransmissionServer {

    NetworkNode owner;

    int maxUpSpeed; 
    int maxDownSpeed; 
    
    List<Connection> uploads;
    List<Connection> downloads;
    
    List<?> compatibilities;
    
    
    
}


public class Connection {

    File src;
    File dest;
    int fp; // where transmission is up to 
    
    int rate; 
    
    
    
    void reset(); 
    void allocate(); 
}