package ass1.nodes.satellites;

import ass1.nodes.NetworkNodeType;
import ass1.networking.Server;

import unsw.utils.Angle;

public class StandardSatellite extends Satellite {
    public StandardSatellite(String id, double height, Angle radians) {
        super(id, height, radians);
        setLinearVelocity(-2500.0);
    }

    @Override
    protected void initialiseServer() {
        setServer(new Server(80, 3, 1, 1));
    }

    public NetworkNodeType type() {
        return NetworkNodeType.StandardSatellite;
    }

    @Override
    public void move() {
        // Defaut orientation is clockwise
        setPosition(getPosition().subtract(signedDelta()));
    };

}
