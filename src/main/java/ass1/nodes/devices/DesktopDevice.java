package ass1.nodes.devices;

import java.util.Arrays;
import java.util.List;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.DesktopDevice;
import static ass1.nodes.NetworkNodeType.StandardSatellite;
import static ass1.nodes.NetworkNodeType.RelaySatellite;
import static ass1.nodes.NetworkNodeType.TeleportingSatellite;

import unsw.utils.Angle;

public class DesktopDevice extends Device {
    public DesktopDevice(String id, Angle radians) {
        super(id, radians);
    }

    public List<NetworkNodeType> supports() {
        return Arrays.asList(StandardSatellite, RelaySatellite, TeleportingSatellite);
    }

    public NetworkNodeType type() {
        return DesktopDevice;
    }

    public double range() {
        return 200000.0;
    }

}
