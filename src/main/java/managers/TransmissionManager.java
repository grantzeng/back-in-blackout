package managers;

import java.util.List;

import networking.Connection;
import networking.Server;
import networking.File;

public class TransmissionManager {
    List<Connection> connections;

    public void registerTransmission(String filename, Server server, Server client) {

        try {
            File source = server.getFile(filename);
            server.checkUploadingBandwidthAvailable();
            client.checkDownloadingBandwidthAvailable();    
            client.checkFileNotAlreadyExists(filename);
            client.checkStorageSpaceAvailable(source.getSize()); 

            File target = client.createFile(source.getFilename(), source.getSize());
            
            Connection connection = new Connection(source, target, server, client, this);
            connections.add(connection);
            
        } catch (Exception e) {
            System.out.println("Failed to register transmission");
        }

    }
    
    public void closeTransmission(Connection connection) {
        connections.remove(connection);
    }

}
