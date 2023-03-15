package nodes;

import static nodes.NetworkNode.NodeType.HandheldDevice;
import static nodes.NetworkNode.NodeType.LaptopDevice;
import static nodes.NetworkNode.NodeType.StandardSatellite;
import static nodes.NetworkNode.NodeType.RelaySatellite;
import static nodes.NetworkNode.NodeType.TeleportingSatellite;
import static nodes.NetworkNode.NodeType.Satellite;

import java.util.Arrays;
import java.util.List;

import networking.Server;
import unsw.utils.Angle;

public class StandardSatellite extends NetworkNode {
    private static final double STANDARD_SATELLITE_RANGE = 150000.0;

    private double linearVelocity;
    private Server server;

    public StandardSatellite(String id, double height, Angle position) {
        super(id, position, height);
        linearVelocity = -2500.0;
        server = new Server(this, 80, 3, 1, 1);


    }


    public Server getServer() {
        return server;
    }

    public double range() {
        return STANDARD_SATELLITE_RANGE;
    }

    public List<NodeType> supports() {
        return Arrays.asList(HandheldDevice, LaptopDevice, StandardSatellite, RelaySatellite, TeleportingSatellite);

    }

    public NodeType subtype() {
        return StandardSatellite;
    }

    public NodeType type() {
        return Satellite;
    }

    @Override
    public void move() {
        // Defaut orientation is clockwise
        Angle delta = Angle.fromRadians(-1.0 * linearVelocity / getHeight());
        setPosition(getPosition().subtract(delta));
    };

}
