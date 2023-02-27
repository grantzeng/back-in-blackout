package ass1.nodes;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;
import unsw.utils.AngleNormaliser;

public abstract class NetworkNode {
    private String id;
    private Angle position;        // degrees
    private double height;         // km
    private double linearVelocity; // km/min

    // private Server server;

    protected NetworkNode(String id, double height, Angle degrees) {
        this.id = id;
        this.height = height;
        setPosition(degrees);
    }

    // Motion related things
    protected void setLinearVelocity(double linearVelocity) {
        this.linearVelocity = linearVelocity;
    }
    
    protected double getLinearVelocity() {
        return linearVelocity;
    }
    
    protected Angle getPosition() {
        return position; 
    }
    
    protected void setPosition(Angle degrees) {
        this.position = AngleNormaliser.normalise(degrees);
    }

    public abstract void move();

    // Returning network node information
    public abstract NetworkNodeType type();

    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, position, height, type().toString());
    }

}
