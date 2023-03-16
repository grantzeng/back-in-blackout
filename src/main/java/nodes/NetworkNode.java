package nodes;

import java.util.List;

import helpers.AngleNormaliser;
import networking.Connection;
import networking.File;
import networking.Server;
import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public abstract class NetworkNode {
    private String id;
    private Angle position;
    private double height;
    private List<NetworkNode> visible;
    private List<NetworkNode> communicable;

    public enum NodeType {
        DesktopDevice, HandheldDevice, LaptopDevice, RelaySatellite, StandardSatellite, TeleportingSatellite, Satellite,
        Device
    }

    protected NetworkNode(String id, Angle position, double height) {
        this.id = id;
        this.position = position;
        this.height = height;
    }

    /*
     * Motion and position
     */
    public String getId() {
        return id;
    }

    public double getHeight() {
        return height;
    }

    protected void setHeight(double height) {
        this.height = height;
    }

    public Angle getPosition() {
        return position;
    }

    protected void setPosition(Angle position) {
        AngleNormaliser normaliser = new AngleNormaliser();
        this.position = normaliser.normalise(position);

    }

    public abstract void move();

    /*
     * File server behaviour is delegated to server instance.
     */

    public abstract Server getServer();

    public File getFile(String filename) throws VirtualFileNotFoundException {
        return getServer().getFile(filename);
    }

    public void removePartialFile(String filename) {
        getServer().removeFile(filename);
    }

    public File createEmptyFile(String filename, int size)
            throws VirtualFileAlreadyExistsException, VirtualFileNoStorageSpaceException {
        return getServer().createEmptyFile(filename, size);
    }

    public void addFile(File file) {
        getServer().addFile(file);
    }

    public void addUploadConnection(Connection conn) {
        getServer().addUploadConnection(conn);
    }

    public void addDownloadConnection(Connection conn) {
        getServer().addDownloadConnection(conn);
    }

    public void removeDownloadConnection(Connection conn) {
        getServer().removeDownloadConnection(conn);
    }

    public void removeDownloadConnection(Connection conn, String filename) {
        getServer().removeDownloadConnection(conn, filename);
    }

    public void removeUploadConnection(Connection conn) {
        getServer().removeUploadConnection(conn);
    }

    public void checkUploadResourcesAvailable(String filename)
            throws VirtualFileNotFoundException, VirtualFileNoBandwidthException {
        getServer().checkUploadResourcesAvailable(filename);
    }

    public void checkDownloadResourcesAvailable(String filename, int filesize) throws VirtualFileAlreadyExistsException,
            VirtualFileNoBandwidthException, VirtualFileNoStorageSpaceException {
        getServer().checkDownloadResourcesAvailable(filename, filesize);
    }

    /*
     * Communicability
     */

    public void setCommunicable(List<NetworkNode> communicable) {
        this.communicable = communicable;
    }

    public boolean canSendDirectlyTo(NetworkNode client) {
        return supports().contains(client.subtype())
                && MathsHelper.getDistance(height, position, client.getHeight(), client.getPosition()) <= range();
    }

    public boolean canCommunicateWith(NetworkNode client) {
        return communicable.contains(client);
    }

    public void setVisible(List<NetworkNode> visible) {
        this.visible = visible;
    }

    public List<NetworkNode> getVisible() {
        return visible;
    }

    public List<NetworkNode> getCommunicable() {
        return communicable;
    }

    /*
     * What kind of object am I?
     */
    public abstract NodeType type();

    public abstract NodeType subtype();

    protected abstract List<NodeType> supports();

    protected abstract double range();

    /*
     * Free resources tagged with if out of
     */
    public void free() {
        getServer().free();
    }

    /*
     * Entity info response
     */

    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, position, height, subtype().toString(), getServer().getServerInfoResponse());
    }

}
