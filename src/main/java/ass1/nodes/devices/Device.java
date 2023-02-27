package ass1.nodes.devices;

import ass1.nodes.NetworkNode;
import unsw.utils.Angle;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

public abstract class Device extends NetworkNode {

    protected Device(String id, Angle degrees) {
        super(id, RADIUS_OF_JUPITER, degrees);
        setLinearVelocity(0.0);
    }

    @Override
    public void move() {
        System.out.println("Devices don't move");
        return;
    }

}
