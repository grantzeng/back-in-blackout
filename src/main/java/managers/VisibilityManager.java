package managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import nodes.NetworkNode;
import unsw.utils.MathsHelper;

public class VisibilityManager {
    public static void update(Map<String, NetworkNode> nodes) {

        List<NetworkNode> visible = new ArrayList<>();

        for (NetworkNode server : nodes.values()) {
            for (NetworkNode client : nodes.values()) {

                if (client == server) {
                    continue;
                }

                if (MathsHelper.isVisible(server.getHeight(), server.getPosition(), client.getHeight(),
                        client.getPosition())) {
                    visible.add(client);
                }
            }
            server.setVisible(visible);
            visible = new ArrayList<>();
        }

    }

}
