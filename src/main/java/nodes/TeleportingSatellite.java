package nodes;

import static nodes.NetworkNode.NodeType.DesktopDevice;
import static nodes.NetworkNode.NodeType.HandheldDevice;
import static nodes.NetworkNode.NodeType.LaptopDevice;
import static nodes.NetworkNode.NodeType.StandardSatellite;
import static nodes.NetworkNode.NodeType.RelaySatellite;
import static nodes.NetworkNode.NodeType.TeleportingSatellite;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import unsw.response.models.FileInfoResponse;
import networking.Server;
import nodes.NetworkNode.NodeType;
import unsw.utils.Angle;

public class TeleportingSatellite extends NetworkNode {
    private static final double TELEPORTING_SATELLITE_RANGE = 150000.0;
    private double linearVelocity;
    private Server server;

    public TeleportingSatellite(String id, double height, Angle position) {
        super(id, position, height);
        linearVelocity = 1000.0;
    }

    public Server setServer() {
        server = new Server(this, 200, Integer.MAX_VALUE, 10, 15);
        return server;
    }

    public Server getServer() {
        return server;
    }

    public double range() {
        return 200000.0;
    }

    public List<NodeType> supports() {
        return Arrays.asList(HandheldDevice, LaptopDevice, DesktopDevice, StandardSatellite, RelaySatellite,
                TeleportingSatellite);

    }

    public NodeType type() {
        return TeleportingSatellite;
    }

    @Override
    public void move() {
        // Check if need to teleport
        Angle delta = Angle.fromRadians(-1.0 * linearVelocity / getHeight());

        double newPosition = getPosition().toDegrees() - delta.toDegrees();
        if ((linearVelocity < 0 && newPosition > 180.0) || (linearVelocity > 0 && newPosition < 180.0)) {
            teleport();
            return;
        }

        // Otherwise just normal motion
        setPosition(getPosition().subtract(delta));
    }

    private void teleport() {
        setPosition(Angle.fromDegrees(0));
        linearVelocity = -1.0 * linearVelocity;
    }

    @Override
    protected Map<String, FileInfoResponse> getServerInfoResponse() {
        return server.getServerInfoResponse();
    }

}
