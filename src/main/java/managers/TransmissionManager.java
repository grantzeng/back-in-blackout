package managers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;

import networking.Connection;
import networking.Server;
import nodes.NetworkNode;
import networking.File;

public class TransmissionManager {
    private List<Connection> connections = new ArrayList<>();

    public void sendFile(String filename, NetworkNode from, NetworkNode to) throws VirtualFileNotFoundException,
            VirtualFileNoBandwidthException, VirtualFileAlreadyExistsException, VirtualFileNoStorageSpaceException {

        Connection conn = new Connection(from, to, from.getServer(), to.getServer());
        try {
            File source = from.getFile(filename);
            conn.addSource(source);

            from.checkUploadResourcesAvailable(filename);
            to.checkDownloadResourcesAvailable(filename, source.getSize());
            File dest = to.createEmptyFile(filename, source.getSize());
            conn.addDest(dest);

            from.addUploadConnection(conn); // Adds connetion and updates everything
            to.addDownloadConnection(conn);

            connections.add(conn);

        } catch (Exception e) {
            connections.remove(conn);
        }
    }

    /*
     * public void processTransmissions() {
     * 
     * for (int i = 0; i < connections.size(); i++) { connections.get(i).transmit();
     * }
     * 
     * // NB: Functional syntax causes ConcurrentModificationException, oops //
     * connections.stream().forEach(c -> c.transmit()); }
     * 
     * public void closeOutOfRangeTransmissions() {
     * 
     * List<Connection> toClose = connections.stream().filter(c ->
     * c.outOfRange()).collect(Collectors.toList());
     * 
     * for (Connection stale : toClose) { stale.close(); }
     * 
     * }
     * 
     * public void closeTransmission(Connection connection) {
     * connections.remove(connection); }
     */

}
