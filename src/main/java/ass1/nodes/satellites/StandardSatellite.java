package ass1.nodes.satellites;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.StandardSatellite;
import unsw.utils.Angle;

public class StandardSatellite extends Satellite {

    public StandardSatellite(String id, double height, Angle position) {
        super(id, height, position);
    }
    
    public NetworkNodeType type() {
        return StandardSatellite;
    }

    @Override
    public void move() {
    };

}
