package nodes;

import static nodes.NetworkNode.NodeType.HandheldDevice;
import static nodes.NetworkNode.NodeType.StandardSatellite;
import static nodes.NetworkNode.NodeType.RelaySatellite;
import static nodes.NetworkNode.NodeType.TeleportingSatellite;
import static nodes.NetworkNode.NodeType.Device;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;
import java.util.List;

import networking.Server;
import unsw.utils.Angle;

public class HandheldDevice extends NetworkNode {
    private static final double HANDHELD_DEVICE_RANGE = 50000.0;
    private Server server;

    public HandheldDevice(String id, Angle radians) {
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
        return HandheldDevice;
    }
    
    @Override
    public NodeType type() {
        return Device;
    }

    @Override
    public double range() {
        return HANDHELD_DEVICE_RANGE;
    }
    
  
}
