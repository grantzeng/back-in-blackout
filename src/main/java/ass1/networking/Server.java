package ass1.networking;

import java.util.List;
import java.util.Map;

import ass1.file.File;
import unsw.response.models.FileInfoResponse;

import org.json.simple.JSONObject;

public class Server {
    private Map<String, File> files;
    private List<Connection> connections;

    private int maxSendingBandwidth;
    private int maxReceivingBandwidth;

    public void addFile(File file) {
        files.put(file.getFilename(), file);
    }

    public void connect(JSONObject fileToBeTransmitted, Server destination) {};

    public void connect(JSONObject fileToBeTransmittedMetadata, Connection endpoint) {};


    public Map<String, FileInfoResponse> contentsToString() {
        Map<String, FileInfoResponse> info = new HashMap<>();

        for (File file: files.values()) {
            info.put(
                file.getFileInfoResponse().getFilename(),
                file.getFileInfoResponse()
            );
        }

        return info;
    }


}
