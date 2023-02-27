package ass1.nodes.satellites;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.StandardSatellite;
import unsw.utils.Angle;

public class StandardSatellite extends Satellite {

    public StandardSatellite(String id, double height, Angle degrees) {
        super(id, height, degrees);
        setLinearVelocity(-2500.0);
    }
    
    public NetworkNodeType type() {
        return StandardSatellite;
    }

    @Override
    public void move() {
    };

}
