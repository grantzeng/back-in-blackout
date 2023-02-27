package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ass1.nodes.NetworkNode;
import ass1.nodes.devices.DesktopDevice;
import ass1.nodes.devices.HandheldDevice;
import ass1.nodes.devices.LaptopDevice;
import ass1.nodes.satellites.RelaySatellite;
import ass1.nodes.satellites.StandardSatellite;
import ass1.nodes.satellites.TeleportingSatellite;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public class BlackoutController {

    private Map<String, NetworkNode> nodes = new HashMap<>();

    public void createDevice(String deviceId, String type, Angle position) {
        switch (type) {
            case "HandheldDevice":
                nodes.put(deviceId, new HandheldDevice(deviceId, position)); break;
            case "LaptopDevice":
                nodes.put(deviceId, new LaptopDevice(deviceId, position)); break;
            case "DesktopDevice":
                nodes.put(deviceId, new DesktopDevice(deviceId, position)); break;
            default:
                System.out.println("No device was added to Blackout"); break;
        }
    }

    /**
     * @pre deviceId is a valid device id
     * @param deviceId
     */
    public void removeDevice(String deviceId) {
        nodes.remove(deviceId);
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        switch (type) {
            case "StandardSatellite":
                nodes.put(satelliteId, new StandardSatellite(satelliteId, height, position)); break;
            case "TeleportingSatellite": 
                nodes.put(satelliteId, new TeleportingSatellite(satelliteId, height, position)); break;
            case "RelaySatellite":
                nodes.put(satelliteId, new RelaySatellite(satelliteId, height, position)); break;
            default: 
                System.out.println("No satellite was added to Blackout"); break;
        }
    }
    
    /**
     * @pre satelliteId is a valid satellite id
     * @param satelliteId
     */
    public void removeSatellite(String satelliteId) {
        nodes.remove(satelliteId);
    }

    public List<String> listDeviceIds() {
        return nodes.values()
                    .stream()
                    .filter(node -> node instanceof Device)
                    .map(device -> device.getInfo().getDeviceId())
                    .collect(Collectors.toList());
    }

    public List<String> listSatelliteIds() {
        return nodes.values()
                    .stream()
                    .filter(node -> node instanceof Satellite)
                    .map(satellite -> satellite.getInfo().getDeviceId())
                    .collect(Collectors.toList());
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        // TODO: Task 1g)
    }

    public EntityInfoResponse getInfo(String id) {
        return nodes.get(id).getInfo();
    }

    public void simulate() {
        // TODO: Task 2a)
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public List<String> communicableEntitiesInRange(String id) {
        // TODO: Task 2 b)
        return new ArrayList<>();
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        // TODO: Task 2 c)
    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }
}
