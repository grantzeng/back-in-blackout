package unsw.blackout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nodes.NetworkNode.NodeType.DesktopDevice;
import static nodes.NetworkNode.NodeType.HandheldDevice;
import static nodes.NetworkNode.NodeType.LaptopDevice;
import static nodes.NetworkNode.NodeType.StandardSatellite;
import static nodes.NetworkNode.NodeType.RelaySatellite;
import static nodes.NetworkNode.NodeType.TeleportingSatellite;

import managers.CommunicabilityManager;
import managers.TransmissionManager;
import managers.VisibilityManager;
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

public class BlackoutController {
    private Map<String, NetworkNode> nodes = new HashMap<>();
    
    private TransmissionManager transmissionManager = new TransmissionManager();

    private int clock = 0;

    private void update() {
        VisibilityManager.update(nodes);
        CommunicabilityManager.update(nodes.values());
    }

    public void createDevice(String deviceId, String type, Angle position) {

        switch (type) {
        case "HandheldDevice":
            HandheldDevice handheldDevice = new HandheldDevice(deviceId, position);
            nodes.put(deviceId, handheldDevice);
            servers.put(deviceId, handheldDevice.setServer());
            break;
        case "LaptopDevice":
            LaptopDevice laptopDevice = new LaptopDevice(deviceId, position);
            nodes.put(deviceId, laptopDevice);
            servers.put(deviceId, laptopDevice.setServer());
            break;
        case "DesktopDevice":
            DesktopDevice desktopDevice = new DesktopDevice(deviceId, position);
            nodes.put(deviceId, desktopDevice);
            servers.put(deviceId, desktopDevice.setServer());
            break;
        default:
            System.out.println("No device was added to Blackout");
            return;
        }
        update();
    }

    /**
     * @pre deviceId is a valid device id
     * @param deviceId
     */
    public void removeDevice(String deviceId) {
        nodes.remove(deviceId);
        servers.remove(deviceId);
        update();
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
            servers.put(satelliteId, standardSatellite.setServer());
            break;
        case "TeleportingSatellite":
            TeleportingSatellite teleportingSatellite = new TeleportingSatellite(satelliteId, height, position);
            nodes.put(satelliteId, teleportingSatellite);
            servers.put(satelliteId, teleportingSatellite.setServer());
            break;
        default:
            System.out.println("No device was added to Blackout");
            return;
        }
        update();
    }

    /**
     * @pre satelliteId is a valid satellite id
     * @param satelliteId
     */
    public void removeSatellite(String satelliteId) {
        nodes.remove(satelliteId);
        servers.remove(satelliteId);
        update();
    }

    public List<String> listDeviceIds() {
        return nodes.values().stream()
                .filter(node -> Arrays.asList(DesktopDevice, LaptopDevice, HandheldDevice).contains(node.type()))
                .map(device -> device.getId()).collect(Collectors.toList());
    }

    public List<String> listSatelliteIds() {
        return nodes.values().stream().filter(
                node -> Arrays.asList(RelaySatellite, StandardSatellite, TeleportingSatellite).contains(node.type()))
                .map(device -> device.getId()).collect(Collectors.toList());
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

        (servers.get(deviceId)).addFile(complete);
    }

    public EntityInfoResponse getInfo(String id) {
        return nodes.get(id).getInfo();
    }

    public void simulate() {

        System.out.println("\n" + clock + "\n");

        nodes.values().stream().forEach(n -> n.move());
        update();

        transmissionManager.closeOutOfRangeTransmissions();

        for (Server server : servers.values()) {
            server.allocateBandwidthToConnections();
        }

        transmissionManager.processTransmissions();

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
