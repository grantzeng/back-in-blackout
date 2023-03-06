package ass1.nodes;

import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.AngleNormaliser;
import unsw.utils.MathsHelper;
import unsw.utils.Orientation;
import static unsw.utils.Orientation.CLOCKWISE;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import ass1.connections.Connection;
import static ass1.connections.ConnectionType.RECIEVING;
import static ass1.connections.ConnectionType.SENDING;

import ass1.file.File;
import ass1.file.TransmissionStatus;

import static unsw.utils.Orientation.ANTICLOCKWISE;

public abstract class NetworkNode {
    private String id;
    private Angle position; // radians
    private double height; // km
    private double linearVelocity; // km/min

    protected NetworkNode(String id, double height, Angle radians) {
        this.id = id;
        setHeight(height);
        setPosition(radians);
    }

    // Motion related things
    protected void setLinearVelocity(double linearVelocity) {
        this.linearVelocity = linearVelocity;
    }

    protected double getLinearVelocity() {
        return linearVelocity;
    }

    /**
     * Returns position (in radians)
     *
     * @return
     */
    public Angle getPosition() {
        return position;
    }

    /**
     * Sets position (in radians)
     *
     * @param radians
     */
    protected void setPosition(Angle radians) {
        AngleNormaliser normaliser = new AngleNormaliser();
        this.position = normaliser.normalise(radians);
    }

    public double getHeight() {
        return height;
    }

    protected void setHeight(double height) {
        // Will need this for moving devices
        if (height < 0) {
            throw new IllegalArgumentException("Height cannot be negative");
        }
        this.height = height;
    }

    /*
     * Communicability related
     */
    // protected abstract List<NetworkNodeType> supports();

    /*
     * File server related
     */
    private Map<String, File> files;
    private List<Connection> connections;
    private List<NetworkNode> visible; // Updated by blackout controller at every tick
    private List<NetworkNode> communicable; // Cache communicable results every tick

    private int maxSendingBandwidth;
    private int maxReceivingBandwidth;
    private int bytesCap;
    private int filesCap;

    /**
     * Initialises variables that form a server, with server storage and
     * transmission constraints
     * 
     * @param bytesCap
     * @param fileCountCap
     * @param sendingBandwidth
     * @param receivingBandwidth
     */
    protected void setServer(int bytesCap, int fileCountCap, int sendingBandwidth, int receivingBandwidth) {
        files = new HashMap<String, File>();
        connections = new ArrayList<Connection>();
        visible = new ArrayList<NetworkNode>();
        communicable = new ArrayList<NetworkNode>();

        this.maxSendingBandwidth = sendingBandwidth;
        this.maxReceivingBandwidth = receivingBandwidth;
        this.bytesCap = bytesCap;
        this.filesCap = fileCountCap;
    }

    protected Map<String, File> getFiles() {
        return files;
    }

    protected void setFiles(Map<String, File> files) {
        this.files = files;
    }

    /*
     * Functions for determining who is in sending range of this
     */
    public String getId() {
        return id;
    }

    public List<NetworkNode> getVisible() {
        return visible;
    }

    public void setVisible(List<NetworkNode> visible) {
        this.visible = visible;
    }

    /**
     * @Pre visibility graph must be updated across whole system (i.e. Blackout
     *      controller must have done set up for tick)
     */

    private boolean supportsSendingTo(NetworkNode node) {
        return node.supports().contains(type());
    }

    private boolean hasRangeToSendTo(NetworkNode target) {
        double distance = MathsHelper.getDistance(height, position, target.getHeight(), target.getPosition());
        return distance <= range();
    }

    protected abstract double range();

    protected abstract List<NetworkNodeType> supports();

    public abstract NetworkNodeType type();

    public List<String> communicableEntitiesInRange() {
        List<NetworkNode> visited = new ArrayList<>();
        Queue<NetworkNode> queue = new ArrayDeque<>();
        queue.add(this);
        visited.add(this);

        while (!queue.isEmpty()) {
            NetworkNode curr = queue.poll();
            for (NetworkNode visible : curr.getVisible()) {
                if (curr.supportsSendingTo(visible) && curr.hasRangeToSendTo(visible) && !visited.contains(visible)) {
                    visited.add(visible);
                    if (visible.type() == NetworkNodeType.RelaySatellite) {
                        queue.add(visible);
                    }
                }
            }
        }

        // Post processing: remove us from sending files to ourselves
        communicable = visited.stream().filter(entity -> entity != this).collect(Collectors.toList());

        return communicable.stream().map(node -> node.getId()).collect(Collectors.toList());
    }

    /*
     * 
     * Dealing with transmission
     * 
     */

    private int countReceiving() {
        return Math.toIntExact(connections.stream().filter(connection -> connection.getType() == RECIEVING).count());
    }

    private int countSending() {
        return Math.toIntExact(connections.stream().filter(connection -> connection.getType() == SENDING).count());
    }

    // Might be able to turn these into functions within functions to make it look
    // smaller
    private int receivingChannelWidth() {
        return maxReceivingBandwidth / (countReceiving() + 1);
    }

    public int sendingChannelWidth() {
        return maxSendingBandwidth / (countSending() + 1);
    }

    public void sendFile(String filename, NetworkNode client)
            throws VirtualFileNotFoundException, VirtualFileNoBandwidthException {

        System.out.println("sendFile()");

        File source = files.get(filename);

        if (source == null || source.getStatus() == TransmissionStatus.PARTIAL) {
            throw new VirtualFileNotFoundException(filename);
        }

        if (sendingChannelWidth() == 0) {
            throw new VirtualFileNoBandwidthException(id);
        }

        // Create connection object, and try to give to client, otherwise clean it up
        Connection sourcepoint = new Connection(files.get(filename), this);
        System.out.println("    Created sourcepoint: " + sourcepoint);
        connections.add(sourcepoint);
        setBandwidths();

        try {
            client.acceptDataConnection(sourcepoint, filename, files.get(filename).getSize());
        } catch (Exception e) {
            System.out.println("    Client rejected connection");
            connections.remove(sourcepoint);
            setBandwidths();
        }

    }

    private int memoryUsage() {
        return files.values().stream().map(file -> file.getSize()).reduce(0, Integer::sum);
    }

    public void acceptDataConnection(Connection sourcepoint, String filename, int memoryRequired)
            throws VirtualFileNoBandwidthException, VirtualFileAlreadyExistsException,
            VirtualFileNoStorageSpaceException {
        System.out.println("acceptDataConnection()");
        System.out.println(sourcepoint);

        // recject if no down bandwidth
        if (receivingChannelWidth() == 0) {
            throw new VirtualFileNoBandwidthException(id);
        }

        // Reject connection if file already exists
        if (files.get(filename) != null) {
            throw new VirtualFileAlreadyExistsException(id);
        }

        // Reject connection if reached file cap
        if (files.size() + 1 > filesCap) {
            throw new VirtualFileNoStorageSpaceException("Max Files Reached");
        }

        // Reject connection if reached bytes cap
        if (memoryUsage() + memoryRequired > bytesCap) {
            throw new VirtualFileNoStorageSpaceException("Max Bytes Reached");
        }

        // Create connection object
        File emptyFile = new File(filename, memoryRequired);
        files.put(filename, emptyFile);

        Connection endpoint = new Connection(emptyFile, this, memoryRequired);
        System.out.println("    Created endpoint: " + endpoint);
        System.out.println("    Give endpoint to sourcepoint");

        sourcepoint.connect(endpoint);
        // endpoint.connect(sourcepoint);

        connections.add(endpoint);
        setBandwidths();
    }

    public void transmitData() {
        // Server hammers each connection object until rate limited
        for (Connection connection : connections) {
            if (connection.getType() == SENDING) {
                connection.send();
            }
        }
    }

    public void setBandwidths() {
        // Set up bandwidth
        int receivingAllocation = receivingChannelWidth();
        int sendingAllocation = sendingChannelWidth();

        for (Connection connection : connections) {
            if (connection.getType() == RECIEVING) {
                connection.reset(receivingAllocation);
            } else {
                connection.reset(sendingAllocation);
            }
        }

    }

    public void tickCleanUp() {
        // Clean up unused connection objects

        // Clean up any completed connection objects

        // Reset everything else
    }

    /**
     *
     * MOTION RELATED FUNCTIONALITY
     * 
     * 
     * 
     * @return
     */
    protected Angle signedDelta() {
        // -1.0 is because a +ve linear velocity causes a clockwise rotation, which is
        // defined as -ve under MathsHelper
        return Angle.fromRadians(-1.0 * getLinearVelocity() / getHeight());
    }

    protected Orientation orientation() {
        return getLinearVelocity() < 0 ? CLOCKWISE : ANTICLOCKWISE;
    }

    public abstract void move();

    public EntityInfoResponse getInfo() {
        System.out.println(this);
        Map<String, FileInfoResponse> info = new HashMap<>();

        for (File file : files.values()) {
            info.put(file.getFileInfoResponse().getFilename(), file.getFileInfoResponse());
        }

        System.out.println(info);

        return new EntityInfoResponse(id, position, height, type().toString(), info);
    }

}
