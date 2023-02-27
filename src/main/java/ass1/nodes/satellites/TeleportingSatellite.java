package ass1.nodes.satellites;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.TeleportingSatellite;
import unsw.utils.Angle;

public class TeleportingSatellite extends Satellite {
    public TeleportingSatellite(String id, double height, Angle radians) {
        super(id, height, radians);
        setLinearVelocity(1000.0);
    }

    public NetworkNodeType type() {
        return TeleportingSatellite;
    }

    @Override
    public void move() {
        Angle delta = Angle.fromRadians(getLinearVelocity() / getHeight());

        // Check if need to teleport
        boolean anticlockwise = delta.toDegrees() > 0;
        double newPosition = getPosition().toDegrees() + delta.toDegrees();

        if ((anticlockwise && newPosition > 180.0) || (!anticlockwise && newPosition < 180.0)) {
            teleport();
            return;
        }

        // Otherwise just normal motion
        setPosition(getPosition().add(delta));
    }

    private void teleport() {
        setPosition(Angle.fromDegrees(0));
        setLinearVelocity(-1.0 * getLinearVelocity());
    }
}
