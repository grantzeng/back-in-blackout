package nodes;

import static nodes.NetworkNode.NodeType.Device;
import static nodes.NetworkNode.NodeType.StandardSatellite;
import static nodes.NetworkNode.NodeType.RelaySatellite;
import static nodes.NetworkNode.NodeType.TeleportingSatellite;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;
import java.util.List;

import networking.Server;
import unsw.utils.Angle;

public class LaptopDevice extends NetworkNode {
    private static final double LAPTOP_DEVICE_RANGE = 100000.0;
    private Server server;

    public LaptopDevice(String id, Angle radians) {
        super(id, radians, RADIUS_OF_JUPITER);
        server = new Server(this, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);

    }

    public Server getServer() {
        return server;
    }

    @Override
    public List<NodeType> supports() {
        return Arrays.asList(StandardSatellite, RelaySatellite, TeleportingSatellite);
    }

    @Override
    public void move() {
        return;
    }

    @Override
    public NodeType subtype() {
        return NodeType.LaptopDevice;
    }
    @Override
    public NodeType type() {
        return Device;
    }

    @Override
    public double range() {
        return LAPTOP_DEVICE_RANGE;
    }

}
