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


public class StandardSatellite extends NetworkNode {

    private static final double STANDARD_SATELLITE_RANGE = 150000.0;


    private double linearVelocity;
    private Server server;
    
    public StandardSatellite(String id, double height, Angle position) {
        super(id, position, height);
        linearVelocity = -2500.0; 

    }

    public Server setServer() {
        server = new Server(this, 80, 3, 1, 1);
        return server;
    }

    public double range() {
        return STANDARD_SATELLITE_RANGE;
    }

    public List<NodeType> supports() {
        return Arrays.asList(HandheldDevice, LaptopDevice, StandardSatellite, RelaySatellite,
        TeleportingSatellite);


    }
    
    public NodeType type() {
        return StandardSatellite;
    }

    @Override
    public void move() {
        // Defaut orientation is clockwise
        Angle delta = Angle.fromRadians(-1.0 * linearVelocity / getHeight());
        setPosition(getPosition().subtract(delta));
    };
    
    @Override
    protected Map<String, FileInfoResponse> getServerInfoResponse() {
        return server.getServerInfoResponse();
    }

    
}
