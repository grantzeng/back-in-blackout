package nodes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import helpers.AngleNormaliser;
import networking.Connection;
import networking.File;
import networking.Server;
import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;
import unsw.utils.MathsHelper;

public abstract class NetworkNode {
    private String id;
    private Angle position;
    private double height;
    private Server server;
    private List<NetworkNode> visible;
    private List<NetworkNode> communicable;

    public enum NodeType {
        DesktopDevice, HandheldDevice, LaptopDevice, RelaySatellite, StandardSatellite, TeleportingSatellite
    }

    protected NetworkNode(String id, Angle position, double height) {
        this.id = id;
        this.position = position;
        this.height = height;
        this.server = new Server(this, 0, 0, 0, 0);
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
    protected void setServer(Server server) {
        this.server = server;
    }

    public abstract Server getServer();

    public File getFile(String filename) throws VirtualFileNotFoundException {
        return server.getFile(filename);
    }

    public void removePartialFile(String filename) {
        server.removeFile(filename);
    }

    public File createEmptyFile(String filename, int size)
            throws VirtualFileAlreadyExistsException, VirtualFileNoStorageSpaceException {
        return server.createEmptyFile(filename, size);
    }

    public void addUploadConnection(Connection conn) {
        server.addUploadConnection(conn);
    }

    public void addDownloadConnection(Connection conn) {
        server.addDownloadConnection(conn);
    }

    public void removeDownloadConnection(Connection conn) {
        server.removeDownloadConnection(conn);
    }

    public void removeDownloadConnection(Connection conn, String filename) {
        server.removeDownloadConnection(conn, filename);
    }

    public void removeUploadConnection(Connection conn) {
        server.removeUploadConnection(conn);
    }

    public void checkUploadResourcesAvailable(String filename)
            throws VirtualFileNotFoundException, VirtualFileNoBandwidthException {
        server.checkUploadResourcesAvailable(filename);
    }

    public void checkDownloadResourcesAvailable(String filename, int filesize) throws VirtualFileAlreadyExistsException,
            VirtualFileNoBandwidthException, VirtualFileNoStorageSpaceException {
        server.checkDownloadResourcesAvailable(filename, filesize);
    }

    /*
     * Communicability
     */

    public void setCommunicable(List<NetworkNode> communicable) {
        this.communicable = communicable;
    }

    public boolean canSendDirectlyTo(NetworkNode client) {
        return supports().contains(client.type())
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

    /*
     * What kind of object am I?
     */
    public abstract NodeType type();

    protected abstract List<NodeType> supports();

    protected abstract double range();

    /*
     * Entity info response
     */

    public EntityInfoResponse getInfo() {
        return new EntityInfoResponse(id, position, height, type().toString(), server.getServerInfoResponse());
    }

}
