package ass1.nodes.satellites;


import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.RelaySatellite;
import static ass1.nodes.NetworkNodeType.StandardSatellite;
import static ass1.nodes.NetworkNodeType.TeleportingSatellite;
import static ass1.nodes.NetworkNodeType.HandheldDevice;
import static ass1.nodes.NetworkNodeType.LaptopDevice;

import unsw.utils.Angle;

import java.util.Arrays;
import java.util.List;


public class StandardSatellite extends Satellite {
    public StandardSatellite(String id, double height, Angle radians) {
        super(id, height, radians);
        setLinearVelocity(-2500.0);
        setServer(80, 3, 1, 1);
    }
    
    public double range() {
        return 150000.0;
    }

    public List<NetworkNodeType> supports() {
        return Arrays.asList(HandheldDevice, LaptopDevice, StandardSatellite, RelaySatellite,
                TeleportingSatellite);

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
