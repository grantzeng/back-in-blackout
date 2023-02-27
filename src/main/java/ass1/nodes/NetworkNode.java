package ass1.nodes;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;
import unsw.utils.AngleNormaliser;
import unsw.utils.Orientation;
import static unsw.utils.Orientation.CLOCKWISE;
import static unsw.utils.Orientation.ANTICLOCKWISE;

public abstract class NetworkNode {
    private String id;
    private Angle position; // radians
    private double height; // km
    private double linearVelocity; // km/min

    // private Server server;

    protected NetworkNode(String id, double height, Angle radians) {
        this.id = id;
        setHeight(height);
        setPosition(radians);
    }

    // Motion related things
    protected void setLinearVelocity(double linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    protected double getLinearVelocity() {
        return linearVelocity;
    }

    /**
     * Returns position (in radians)
     * 
     * @return
     */
    protected Angle getPosition() {
        return position;
    }

    /**
     * Sets position (in radians)
     * 
     * @param radians
     */
    protected void setPosition(Angle radians) {
        this.position = AngleNormaliser.normalise(radians);
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

    protected Angle delta() {
        return Angle.fromRadians(Math.abs(getLinearVelocity()) / getHeight());
    }

    protected Orientation orientation() {
        return getLinearVelocity() < 0 ? CLOCKWISE : ANTICLOCKWISE;
    }

    public abstract void move();

    // Returning network node information
    public abstract NetworkNodeType type();

    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, position, height, type().toString());
    }

}
