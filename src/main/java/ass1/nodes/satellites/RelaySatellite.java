package ass1.nodes.satellites;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.RelaySatellite;
import unsw.utils.Angle;

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
        

    }

    private void reverse() {
        setLinearVelocity(-1.0 * getLinearVelocity());
    }
}
