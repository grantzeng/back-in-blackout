package unsw.blackout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nodes.NetworkNode.NodeType.Device;
import static nodes.NetworkNode.NodeType.Satellite;

import managers.CommunicabilityManager;
import managers.TransmissionManager;
import managers.VisibilityManager;
import networking.File;

import nodes.DesktopDevice;
import nodes.HandheldDevice;
import nodes.LaptopDevice;
import nodes.NetworkNode;
import nodes.RelaySatellite;
import nodes.StandardSatellite;
import nodes.TeleportingSatellite;

import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

public class BlackoutController {
    private Map<String, NetworkNode> nodes = new HashMap<>();

    private TransmissionManager transmissionManager = new TransmissionManager();

    private int clock = 0;

    public void createDevice(String deviceId, String type, Angle position) {

        switch (type) {
        case "HandheldDevice":
            HandheldDevice handheldDevice = new HandheldDevice(deviceId, position);
            nodes.put(deviceId, handheldDevice);
            break;
        case "LaptopDevice":
            LaptopDevice laptopDevice = new LaptopDevice(deviceId, position);
            nodes.put(deviceId, laptopDevice);
            break;
        case "DesktopDevice":
            DesktopDevice desktopDevice = new DesktopDevice(deviceId, position);
            nodes.put(deviceId, desktopDevice);
            break;
        default:
            System.out.println("No device was added to Blackout");
            return;
        }
        CommunicabilityManager.update(nodes);
    }

    /**
     * @pre deviceId is a valid device id
     * @param deviceId
     */
    public void removeDevice(String deviceId) {
        NetworkNode node = nodes.remove(deviceId);
        node.free();
        CommunicabilityManager.update(nodes);
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        switch (type) {
        case "RelaySatellite":
            RelaySatellite relaySatellite = new RelaySatellite(satelliteId, height, position);
            nodes.put(satelliteId, relaySatellite);
            break;
        case "StandardSatellite":
            StandardSatellite standardSatellite = new StandardSatellite(satelliteId, height, position);
            nodes.put(satelliteId, standardSatellite);
            break;
        case "TeleportingSatellite":
            TeleportingSatellite teleportingSatellite = new TeleportingSatellite(satelliteId, height, position);
            nodes.put(satelliteId, teleportingSatellite);
            break;
        default:
            System.out.println("No device was added to Blackout");
            return;
        }
        CommunicabilityManager.update(nodes);
    }

    /**
     * @pre satelliteId is a valid satellite id
     * @param satelliteId
     */
    public void removeSatellite(String satelliteId) {
        NetworkNode node = nodes.remove(satelliteId);
        node.free();
        CommunicabilityManager.update(nodes);
    }

    public List<String> listDeviceIds() {
        return nodes.values().stream().filter(node -> node.type() == Device).map(device -> device.getId())
                .collect(Collectors.toList());
    }

    public List<String> listSatelliteIds() {
        return nodes.values().stream().filter(node -> node.type() == Satellite).map(satellite -> satellite.getId())
                .collect(Collectors.toList());
    }

    /**
     * @pre deviceId is a valid device id
     * @param deviceId
     * @param filename
     * @param content
     */

    public void addFileToDevice(String deviceId, String filename, String content) {
        File complete = new File(filename, content);
        System.out.println(complete);

        (nodes.get(deviceId)).addFile(complete);
    }

    public EntityInfoResponse getInfo(String id) {
        return nodes.get(id).getInfo();
    }

    public void simulate() {

        System.out.println("\n" + clock + "\n");

        nodes.values().stream().forEach(node -> node.move());
        CommunicabilityManager.update(nodes);

        /*
         * nodes.values().stream().forEach(n -> n.move()); update();
         * 
         * transmissionManager.closeOutOfRangeTransmissions();
         * 
         * for (Server server : servers.values()) {
         * server.allocateBandwidthToConnections(); }
         * 
         * transmissionManager.processTransmissions();
         * 
         * clock++;
         */
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
        // return nodes.get(id).communicableEntitiesInRange();
        return CommunicabilityManager.getAndUpdateCommunicableEntitiesInRange(nodes.get(id)).stream()
                .map(n -> n.getId()).collect(Collectors.toList());
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        transmissionManager.sendFile(fileName, nodes.get(fromId), nodes.get(toId));
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
