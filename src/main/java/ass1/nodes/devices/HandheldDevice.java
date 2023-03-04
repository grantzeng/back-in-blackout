package ass1.nodes.devices;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.HandheldDevice;
import static ass1.nodes.NetworkNodeType.StandardSatellite;
import static ass1.nodes.NetworkNodeType.RelaySatellite;
import static ass1.nodes.NetworkNodeType.TeleportingSatellite;

import java.util.Arrays;
import java.util.List;

import unsw.utils.Angle;

public class HandheldDevice extends Device {
    public HandheldDevice(String id, Angle radians) {
        super(id, radians);
    }

    public NetworkNodeType type() {
        return HandheldDevice;
    }

    @Override
    public List<NetworkNodeType> supports() {
        return Arrays.asList(StandardSatellite, RelaySatellite, TeleportingSatellite);
    }

    public double range() {
        return 50000.0;
    }
}
