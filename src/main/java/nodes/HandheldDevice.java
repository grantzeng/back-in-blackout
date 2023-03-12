package nodes;

import static nodes.NetworkNode.NodeType.HandheldDevice;
import static nodes.NetworkNode.NodeType.StandardSatellite;
import static nodes.NetworkNode.NodeType.RelaySatellite;
import static nodes.NetworkNode.NodeType.TeleportingSatellite;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;
import unsw.response.models.FileInfoResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import networking.Server;
import unsw.utils.Angle;

public class HandheldDevice extends NetworkNode {
    private static final double HANDHELD_DEVICE_RANGE = 50000.0;
    private Server server;

    public HandheldDevice(String id, Angle radians) {
        super(id, radians, RADIUS_OF_JUPITER);
    }

    public Server setServer() {
        server = new Server(this, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
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
    public NodeType type() {
        return HandheldDevice;
    }

    @Override
    public double range() {
        return HANDHELD_DEVICE_RANGE;
    }
    
    @Override
    protected Map<String, FileInfoResponse> getServerInfoResponse() {
        return server.getServerInfoResponse();
    }
}
