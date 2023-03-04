package ass1.nodes;

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
import ass1.file.File;

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

    private int sendingBandwidth;
    private int receivingBandwidth;
    private int bytesCap;
    private int fileCountCap;

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

        this.sendingBandwidth = sendingBandwidth;
        this.receivingBandwidth = receivingBandwidth;
        this.bytesCap = bytesCap;
        this.fileCountCap = fileCountCap;
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

    /**
     * Basically BFS on visibility graph with filtering for communicablity
     */
    private void findCommunicableEntitiesInRange() {
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
    }

    public List<String> communicableEntitiesInRange() {
        findCommunicableEntitiesInRange(); // We can optimise this later, just make it run every iteration this time
        return communicable.stream().map(node -> node.getId()).collect(Collectors.toList());
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
        Map<String, FileInfoResponse> info = new HashMap<>();

        for (File file : files.values()) {
            info.put(file.getFileInfoResponse().getFilename(), file.getFileInfoResponse());
        }

        return new EntityInfoResponse(id, position, height, type().toString(), info);
    }

}
