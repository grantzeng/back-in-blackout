package managers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import static nodes.NetworkNode.NodeType.RelaySatellite;
import nodes.NetworkNode;
import unsw.utils.MathsHelper;

public class CommunicabilityManager {
    public static List<NetworkNode> getAndUpdateCommunicableEntitiesInRange(NetworkNode node) {
        List<NetworkNode> visited = new ArrayList<>();
        Queue<NetworkNode> queue = new ArrayDeque<>();
        queue.add(node);
        visited.add(node);

        while (!queue.isEmpty()) {
            NetworkNode curr = queue.poll();
            for (NetworkNode next : curr.getVisible()) {
                if (curr.canSendDirectlyTo(next) && !visited.contains(next)) {
                    visited.add(next);
                    if (next.type() == RelaySatellite) {
                        queue.add(next);
                    }
                }
            }
        }

        List<NetworkNode> communicable = visited.stream().filter(n -> n != node).collect(Collectors.toList());
        node.setCommunicable(communicable);
        return communicable;
    }

    public static void update(Map<String, NetworkNode> nodes) {
    
        // Distribute visibility graph adjaceny list among nodes
        List<NetworkNode> visible = new ArrayList<>();
        for (NetworkNode server : nodes.values()) {
            for (NetworkNode client : nodes.values()) {

                if (client == server) { continue;}

                if (MathsHelper.isVisible(server.getHeight(), server.getPosition(), client.getHeight(),
                        client.getPosition())) {
                    visible.add(client);
                }
            }
            server.setVisible(visible);
            visible = new ArrayList<>();
        }
        
        // Run modified BFS across visibility graph to update communicability
        for (NetworkNode server : nodes.values()) {
            getAndUpdateCommunicableEntitiesInRange(server);
        }
    }

}
