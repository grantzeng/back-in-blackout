package ass1.connections;

public abstract class Connection {

    public abstract void setByteAllocation(int allocation);
    
    public abstract void send(); 
    
    public abstract boolean isFinished(); 
    public abstract String getDestination();
    
    public abstract void reset();    

}
