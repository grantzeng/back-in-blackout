package ass1.nodes.satellites;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.RelaySatellite;
import unsw.utils.Angle;
import unsw.utils.Orientation;
import static unsw.utils.Orientation.ANTICLOCKWISE;
import static unsw.utils.Orientation.CLOCKWISE;

public class RelaySatellite extends Satellite {
    public RelaySatellite(String id, double height, Angle radians) {
        super(id, height, radians);
        setInitialLinearVelocity(radians);
    }

    private void setInitialLinearVelocity(Angle radians) {

        if (radians.toDegrees() < 345.0 && radians.toDegrees() > 190.0) {
            setLinearVelocity(-1.0 * 1500);
            return;
        }

        // Satellite in (140°, 190°) or (345°, 360°] or [0°, 140°)
        setLinearVelocity(1500);
    }

    public NetworkNodeType type() {
        return RelaySatellite;
    }

    @Override
    public void move() {

        /*
         * "This means it can briefly exceed the boundary" i.e. set position, then do
         * the correction.
         * 
         * My idea: if not in (140, 190), then just keep going whatever you've been told
         * to do
         */

        Angle newPosition = getPosition().add(delta());

        if (!(getPosition().toDegrees() > 140.0 && getPosition().toDegrees() < 190.0)) {
            // Not in relay region
            setPosition(newPosition);
            return;
        }

        // In edge case, need to reverse
        if ((orientation() == CLOCKWISE && newPosition.toDegrees() < 140.0)
                || (orientation() == ANTICLOCKWISE && newPosition.toDegrees() > 190.0)) {
            reverse();
        }

        // In relay region, but not edge case
        setPosition(newPosition);
    }

    private void reverse() {
        setLinearVelocity(-1.0 * getLinearVelocity());
    }
}
