package managers;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;

import networking.Connection;
import nodes.NetworkNode;
import networking.File;

public class TransmissionManager {
    private List<Connection> connections = new ArrayList<>();

    public void sendFile(String filename, NetworkNode from, NetworkNode to) throws VirtualFileNotFoundException,
            VirtualFileNoBandwidthException, VirtualFileAlreadyExistsException, VirtualFileNoStorageSpaceException {

        Connection conn = new Connection(from, to, from.getServer(), to.getServer());

        File source = from.getFile(filename);
        conn.addSource(source);

        from.checkUploadResourcesAvailable(filename);
        to.checkDownloadResourcesAvailable(filename, source.getSize());

        conn.addTarget(to.createEmptyFile(filename, source.getSize()));

        from.addUploadConnection(conn); // Adds connetion and updates everything
        to.addDownloadConnection(conn);

        connections.add(conn);

    }

    public void processConnections() {
        connections.stream().forEach(conn -> conn.transmit());

        connections = connections.stream().filter(conn -> conn.isActive()).collect(Collectors.toList());
    }

}
