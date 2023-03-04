package ass1.nodes.satellites;

import ass1.nodes.NetworkNodeType;

import unsw.utils.Angle;
import static unsw.utils.Orientation.ANTICLOCKWISE;
import static unsw.utils.Orientation.CLOCKWISE;


public class RelaySatellite extends Satellite {
    public RelaySatellite(String id, double height, Angle radians) {
        super(id, height, radians);
        setInitialLinearVelocity(radians);
        setServer(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
    }


    private void setInitialLinearVelocity(Angle radians) {

        if (radians.toDegrees() < 345.0 && radians.toDegrees() > 190.0) {
            // Relay goes clockwise, which is caused by a positive linear velocity
            setLinearVelocity(1500);
            return;
        }

        // Start at in (140°, 190°) or (345°, 360°] or [0°, 140°)
        // Relay goes anticlockwise, which is caused by a negative linear velocity
        setLinearVelocity(-1.0 * 1500);
    }

    ///private static List<NetworkNode> supports;
    //public List<NetworkNode> supports() {
    //    return ArrayList.
    //}

    public NetworkNodeType type() {
        return NetworkNodeType.RelaySatellite;
    }

    @Override
    public void move() {

        Angle oldPosition = getPosition();

        setPosition(getPosition().subtract(signedDelta()));

        if (!(oldPosition.toDegrees() > 140.0 && oldPosition.toDegrees() < 190.0)) {
            // Not in relay region, do not do boundary checks
            return;
        }

        // In relay region, do boundary checks
        if ((orientation() == ANTICLOCKWISE && getPosition().toDegrees() > 180.0)
                || (orientation() == CLOCKWISE && getPosition().toDegrees() < 140.0)) {
            reverse();
            return;
        }
    }

    private void reverse() {
        setLinearVelocity(-1.0 * getLinearVelocity());
    }
}
