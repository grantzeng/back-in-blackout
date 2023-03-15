package nodes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import networking.Server;

import static nodes.NetworkNode.NodeType.DesktopDevice;
import static nodes.NetworkNode.NodeType.HandheldDevice;
import static nodes.NetworkNode.NodeType.LaptopDevice;
import static nodes.NetworkNode.NodeType.StandardSatellite;
import static nodes.NetworkNode.NodeType.RelaySatellite;
import static nodes.NetworkNode.NodeType.TeleportingSatellite;

import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

public class RelaySatellite extends NetworkNode {
    private static final double RELAY_SATELLITE_RANGE = 300000.0;
    private double linearVelocity;
    private Server server;

    public RelaySatellite(String id, double height, Angle position) {
        super(id, position, height);
        setInitialLinearVelocity(position);
    }
    
    
    public Server setServer() {
        server = new Server(this, 200, Integer.MAX_VALUE, 10, 15);
        return server;
    }
    
    public Server getServer() {
        return server;
    }

    private void setInitialLinearVelocity(Angle radians) {

        if (radians.toDegrees() < 345.0 && radians.toDegrees() > 190.0) {
            // Relay goes clockwise, which is caused by a positive linear velocity
            linearVelocity = 1500.0;
            return;
        }
        linearVelocity = -1.0 * 1500.0;
    }

    @Override
    public void move() {

        Angle oldPosition = getPosition();
        Angle delta = Angle.fromRadians(-1.0 * linearVelocity / getHeight());

        setPosition(getPosition().subtract(delta));

        if (!(oldPosition.toDegrees() > 140.0 && oldPosition.toDegrees() < 190.0)) {
            // Not in relay region, do not do boundary checks
            return;
        }

        // In relay region, do boundary checks
        if ((linearVelocity > 0 && getPosition().toDegrees() > 180.0)
                || (linearVelocity < 0 && getPosition().toDegrees() < 140.0)) {
            reverse();
            return;
        }
    }

    private void reverse() {
        linearVelocity = -1.0 * linearVelocity;
    }

    @Override
    public List<NodeType> supports() {
        return Arrays.asList(DesktopDevice, HandheldDevice, LaptopDevice, StandardSatellite, RelaySatellite,
                TeleportingSatellite);
    }

    @Override
    public NodeType type() {
        return RelaySatellite;
    }

    @Override
    public double range() {
        return RELAY_SATELLITE_RANGE;
    }


}
