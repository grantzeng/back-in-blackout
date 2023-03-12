package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import managers.CommunicabilityManager;
import managers.TransmissionManager;
import managers.VisibilityManager;
import networking.Connection;
import networking.File;
import networking.Server;

import nodes.DesktopDevice;
import nodes.HandheldDevice;
import nodes.LaptopDevice;
import nodes.NetworkNode;
import nodes.RelaySatellite;
import nodes.StandardSatellite;
import nodes.TeleportingSatellite;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public class BlackoutController {
    private List<NetworkNode> nodes = new ArrayList<>();
    private List<Server> servers = new ArrayList<>();

    private int clock = 0;

    private void update() {
        VisibilityManager.update(nodes);
        CommunicabilityManager.update(servers);
    }

    public void createDevice(String deviceId, String type, Angle position) {
        switch (type) {
        case "HandheldDevice":
            nodes.put(deviceId, new HandheldDevice(deviceId, position));
            break;
        case "LaptopDevice":
            nodes.put(deviceId, new LaptopDevice(deviceId, position));
            break;
        case "DesktopDevice":
            nodes.put(deviceId, new DesktopDevice(deviceId, position));
            break;
        default:
            System.out.println("No device was added to Blackout");
            break;
        }
        update();
    }

    /**
     * @pre deviceId is a valid device id
     * @param deviceId
     */
    public void removeDevice(String deviceId) {
        nodes.remove(deviceId);
        update();
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        switch (type) {
        case "StandardSatellite":
            nodes.put(satelliteId, new StandardSatellite(satelliteId, height, position));
            break;
        case "TeleportingSatellite":
            nodes.put(satelliteId, new TeleportingSatellite(satelliteId, height, position));
            break;
        case "RelaySatellite":
            nodes.put(satelliteId, new RelaySatellite(satelliteId, height, position));
            break;
        default:
            System.out.println("No satellite was added to Blackout");
            break;
        }
        update();
    }

    /**
     * @pre satelliteId is a valid satellite id
     * @param satelliteId
     */
    public void removeSatellite(String satelliteId) {
        nodes.remove(satelliteId);
        update();
    }

    public List<String> listDeviceIds() {
        return nodes.values().stream().filter(node -> node instanceof Device)
                .map(device -> device.getInfo().getDeviceId()).collect(Collectors.toList());
    }

    public List<String> listSatelliteIds() {
        return nodes.values().stream().filter(node -> node instanceof Satellite)
                .map(satellite -> satellite.getInfo().getDeviceId()).collect(Collectors.toList());
    }

    /**
     * @pre deviceId is a valid device id
     * @param deviceId
     * @param filename
     * @param content
     */
    public void addFileToDevice(String deviceId, String filename, String content) {
        ((Device) nodes.get(deviceId)).addFile(new File(filename, content));
    }

    public EntityInfoResponse getInfo(String id) {
        return nodes.get(id).getInfo();
    }

    public void simulate() {

        System.out.println("\n" + clock + "\n");

        nodes.values().stream().forEach(n -> n.move());
        update();

        nodes.values().stream().forEach(n -> n.beforeTick());
        nodes.values().stream().forEach(n -> n.transmit());
        nodes.values().stream().forEach(n -> n.afterTick());

        clock++;
    }

    /**
     * Simulate for the specified number of minutes. You shouldn't need to modify
     * this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    /**
     * @pre: updateVisibleNeighbours() has been run, i.e. visibility graph has been
     *       updated
     * @param id
     * @return
     */
    public List<String> communicableEntitiesInRange(String id) {
        return nodes.get(id).communicableEntitiesInRange();
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        (nodes.get(fromId)).sendFile(fileName, nodes.get(toId));
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
