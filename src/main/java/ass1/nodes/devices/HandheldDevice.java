package ass1.nodes.devices;

import ass1.nodes.NetworkNodeType;
import static ass1.nodes.NetworkNodeType.HandheldDevice;
import unsw.utils.Angle;

public class HandheldDevice extends Device {

    public HandheldDevice(String id, Angle position) {
        super(id, position);
    }
    
    public NetworkNodeType type() {
        return HandheldDevice;
    }
}
