package managers;

import java.util.ArrayList;
import java.util.List;

import unsw.blackout.FileTransferException.VirtualFileNotFoundException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;

import networking.Connection;
import networking.Server;
import networking.File;

public class TransmissionManager {
    private List<Connection> connections = new ArrayList<>();

    public void registerTransmission(String filename, Server server, Server client) throws VirtualFileNotFoundException,
            VirtualFileNoBandwidthException, VirtualFileAlreadyExistsException, VirtualFileNoStorageSpaceException {
        
        File source = server.getFile(filename);
        server.checkUploadingBandwidthAvailable();
        client.checkDownloadingBandwidthAvailable();
        client.checkFileNotAlreadyExists(filename);
        client.checkStorageSpaceAvailable(source.getSize());

        File target = client.createFile(source.getFilename(), source.getSize());

        Connection connection = new Connection(source, target, server, client, this);
        connections.add(connection);

        server.addUploadingConnection(connection);
        client.addDownloadingConnection(connection);

    }

    public void processTransmissions() {

        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).transmit();
        }

        // Functional syntax was causing ConcurrentModificationException?
        // connections.stream().forEach(c -> c.transmit());
    }

    public void closeOutOfRangeTransmissions() {
        for (Connection connection : connections) {
            if (connection.closeIfOutOfRange()) {
                closeTransmission(connection);
            }
        }
    }

    public void closeTransmission(Connection connection) {
        connections.remove(connection);
    }

}
