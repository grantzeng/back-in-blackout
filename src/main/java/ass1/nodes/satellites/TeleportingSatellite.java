package ass1.nodes.satellites;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.RelaySatellite;
import static ass1.nodes.NetworkNodeType.StandardSatellite;
import static ass1.nodes.NetworkNodeType.TeleportingSatellite;
import static ass1.nodes.NetworkNodeType.HandheldDevice;
import static ass1.nodes.NetworkNodeType.DesktopDevice;
import static ass1.nodes.NetworkNodeType.LaptopDevice;

import unsw.utils.Angle;
import static unsw.utils.Orientation.ANTICLOCKWISE;
import static unsw.utils.Orientation.CLOCKWISE;

import java.util.Arrays;
import java.util.List;

public class TeleportingSatellite extends Satellite {
    public TeleportingSatellite(String id, double height, Angle radians) {
        super(id, height, radians);
        setLinearVelocity(1000.0);
        setServer(200, Integer.MAX_VALUE, 10, 15);
    }

    public double range() {
        return 200000.0;
    }

    public List<NetworkNodeType> supports() {
        return Arrays.asList(HandheldDevice, LaptopDevice, DesktopDevice, StandardSatellite, RelaySatellite,
                TeleportingSatellite);

    }

    public NetworkNodeType type() {
        return TeleportingSatellite;
    }

    @Override
    public void move() {

        // Check if need to teleport
        double newPosition = getPosition().toDegrees() - signedDelta().toDegrees();
        if ((orientation() == ANTICLOCKWISE && newPosition > 180.0)
                || (orientation() == CLOCKWISE && newPosition < 180.0)) {
            teleport();
            return;
        }

        // Otherwise just normal motion
        setPosition(getPosition().subtract(signedDelta()));
    }

    private void teleport() {
        setPosition(Angle.fromDegrees(0));
        setLinearVelocity(-1.0 * getLinearVelocity());
    }
}
