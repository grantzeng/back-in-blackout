package ass1.nodes.satellites;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.RelaySatellite;
import unsw.utils.Angle;

public class RelaySatellite extends Satellite {
    public RelaySatellite(String id, double height, Angle position) {
        super(id, height, position);
        setLinearVelocity(1500.0);
    }

    public NetworkNodeType type() {
        return RelaySatellite;
    }

    @Override
    public void move() {
    }

    private void reverse() {
    }
}
