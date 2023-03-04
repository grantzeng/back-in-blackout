package ass1.nodes;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.AngleNormaliser;
import unsw.utils.Orientation;
import static unsw.utils.Orientation.CLOCKWISE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    protected Angle getPosition() {
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

    protected double getHeight() {
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

    // Remove file server clas
    private Map<String, File> files;
    private List<Connection> connections;

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
     * Motion related
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

    // Returning network node information
    public abstract NetworkNodeType type();

    private Map<String, FileInfoResponse> serverContents() {
        Map<String, FileInfoResponse> info = new HashMap<>();

        for (File file : files.values()) {
            info.put(file.getFileInfoResponse().getFilename(), file.getFileInfoResponse());
        }

        return info;
    }

    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, position, height, type().toString(), serverContents());
    }

}
