package ass1.nodes.satellites;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.TeleportingSatellite;
import unsw.utils.Angle;

public class TeleportingSatellite extends Satellite {
    public TeleportingSatellite(String id, double height, Angle degrees) {
        super(id, height, degrees);
        setLinearVelocity(1000.0);
    }

    public NetworkNodeType type() {
        return TeleportingSatellite;
    }

    @Override
    public void move() {
    }

    private void teleport() {
    }
}
