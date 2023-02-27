package ass1.nodes.satellites;

import ass1.nodes.NetworkNode;
import unsw.utils.Angle;

public abstract class Satellite extends NetworkNode {

    protected Satellite(String id, double height, Angle degrees) {
        super(id, height, degrees);
    }

}
