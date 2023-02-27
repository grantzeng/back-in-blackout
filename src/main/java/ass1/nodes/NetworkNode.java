package ass1.nodes;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public abstract class NetworkNode {
    private String id;
    private Angle position;
    private double height;
    private double linearVelocity;

    // private Server server;

    protected NetworkNode(String id, double height, Angle position) {
        this.id = id;
        this.height = height;
        this.position = position;
    }

    protected void setLinearVelocity(double linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    public abstract void move();

    public abstract NetworkNodeType type();

    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, position, height, type().toString());
    }

}
