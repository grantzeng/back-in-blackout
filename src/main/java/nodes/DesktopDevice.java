package nodes;

import static nodes.NetworkNode.NodeType.DesktopDevice;
import static nodes.NetworkNode.NodeType.RelaySatellite;
import static nodes.NetworkNode.NodeType.TeleportingSatellite;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import networking.Server;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public class DesktopDevice extends NetworkNode {
    private static final double DESKTOP_DEVICE_RANGE = 20000.0;
    private Server server;

    public DesktopDevice(String id, Angle radians) {
        super(id, radians, RADIUS_OF_JUPITER);
    }

    public Server setServer() {
        server = new Server(this, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
        return server;
    }

    @Override
    public List<NodeType> supports() {
        return Arrays.asList(RelaySatellite, TeleportingSatellite);
    }

    @Override
    public void move() {
        return;
    }

    @Override
    public NodeType type() {
        return DesktopDevice;
    }

    @Override
    public double range() {
        return DESKTOP_DEVICE_RANGE;
    }
    
    protected Map<String, FileInfoResponse> getServerInfoResponse() {
        return server.getServerInfoResponse();
    }
}
