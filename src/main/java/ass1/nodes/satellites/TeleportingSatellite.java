package ass1.nodes.satellites;

import ass1.networking.Server;
import ass1.nodes.NetworkNodeType;
import unsw.utils.Angle;
import static unsw.utils.Orientation.ANTICLOCKWISE;
import static unsw.utils.Orientation.CLOCKWISE;

public class TeleportingSatellite extends Satellite {
    public TeleportingSatellite(String id, double height, Angle radians) {
        super(id, height, radians);
        setLinearVelocity(1000.0);
    }

    @Override
    protected void initialiseServer() {
        setServer(new Server(200, Integer.MAX_VALUE, 10, 15));
    }

    public NetworkNodeType type() {
        return NetworkNodeType.TeleportingSatellite;
    }

    @Override
    public void move() {

        // Check if need to teleport
        double newPosition = getPosition().toDegrees() - signedDelta().toDegrees();
        if ((orientation() == ANTICLOCKWISE && newPosition > 180.0)
                || (orientation() == CLOCKWISE && newPosition < 180.0)) {
            teleport();
            return;
        }

        // Otherwise just normal motion
        setPosition(getPosition().subtract(signedDelta()));
    }

    private void teleport() {
        setPosition(Angle.fromDegrees(0));
        setLinearVelocity(-1.0 * getLinearVelocity());
    }
}
