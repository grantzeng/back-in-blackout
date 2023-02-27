package ass1.nodes;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;
import unsw.utils.AngleNormaliser;

public abstract class NetworkNode {
    private String id;
    private Angle position; // degrees
    private double height; // km
    private double linearVelocity; // km/min

    // private Server server;

    protected NetworkNode(String id, double height, Angle degrees) {
        this.id = id;
        setHeight(height);
        setPositionDegrees(degrees);
    }

    // Motion related things
    protected void setLinearVelocity(double linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    protected double getLinearVelocity() {
        return linearVelocity;
    }

    protected Angle getPositionDegrees() {
        return position;
    }

    protected void setPositionDegrees(Angle degrees) {
        this.position = AngleNormaliser.normalise(degrees);
    }

    protected double getHeight() {
        return height;
    }

    protected void setHeight(double height) {
        // Will need this for moving devices
        if (height < 0) {
            throw new IllegalArgumentException("Height cannot be negative");
        }
        this.height = height;
    }

    public abstract void move();

    // Returning network node information
    public abstract NetworkNodeType type();

    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, position, height, type().toString());
    }

}
