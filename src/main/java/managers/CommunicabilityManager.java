package managers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import nodes.NetworkNode;
import unsw.utils.MathsHelper;

public class CommunicabilityManager {

    /**
     * Updates list of other network node can send files to
     * @pre Visibility graph has been updated
     * @param node
     * @return
     */
    public static List<NetworkNode> getAndUpdateCommunicableEntitiesInRange(NetworkNode node) {

        List<NetworkNode> visited = new ArrayList<>();
        Queue<NetworkNode> queue = new ArrayDeque<>();
        queue.add(node);
        visited.add(node);

        while (!queue.isEmpty()) {
            NetworkNode curr = queue.poll();

            for (NetworkNode next : curr.getVisible()) {

                if (!visited.contains(next) && curr.canSendDirectlyTo(next)) {
                    queue.add(next);
                    visited.add(next);
                }
            }
        }

        List<NetworkNode> communicable = visited.stream().filter(n -> n != node).collect(Collectors.toList());

        node.setCommunicable(communicable);

        return communicable;
    }

    public static void update(Map<String, NetworkNode> nodes) {

        // Distribute visibility graph adjaceny list among network nodes
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


        // Run modified BFS across visibility graph to update who node can communicate with
        for (NetworkNode server : nodes.values()) {
            getAndUpdateCommunicableEntitiesInRange(server);
        }

    }

}
