package ass1.nodes.satellites;

import ass1.nodes.NetworkNode;
import unsw.utils.Angle;
import unsw.utils.Orientation;
import static unsw.utils.Orientation.CLOCKWISE;
import static unsw.utils.Orientation.ANTICLOCKWISE;

public abstract class Satellite extends NetworkNode {
    protected Satellite(String id, double height, Angle radians) {
        super(id, height, radians);
    }

}
