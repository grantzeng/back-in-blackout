package ass1.nodes;

import java.util.List;

import ass1.networking.Server;
import unsw.utils.Angle;


public abstract class NetworkNode {

    private Angle position; 
    private int height; 
    
    private Server server;
    private List<NetworkNode> communicable;

    /**
     * Enregister all other nodes that this network node can send files to given a
     * list of nodes that are in its direct line of sight
     * 
     * @param visible
     */
    public void setCommunicable(List<NetworkNode> visible) {
        // Blackout passes in visible satellites
        // Then do checks for which things are allocable 
    }
    
    // Implement communicable classes

    // protected abstract List<NetworkNodeTypes> communicableClasses();
    
    // Getters and setters for position
    public abstract void move(); 

    protected Angle getPosition() {
        return position; 
    } 
    protected void setPosition(Angle angle) {
        position = angle;
    } 
    

}
