package managers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import static nodes.NetworkNode.NodeType.RelaySatellite;
import networking.Server;
import nodes.NetworkNode;

public class CommunicabilityManager {
    public static List<NetworkNode> findCommunicableEntitiesInRange(NetworkNode node) {

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

    /*
     * public static void update(Map<String, Server> servers) {
     * 
     * for (Server server : servers.values()) {
     * 
     * Map<String, Server> communicable = new HashMap<>();
     * 
     * for (NetworkNode node : findCommunicableEntitiesInRange(server.getOwner())) {
     * communicable.put(node.getId(), node.getServer()); }
     * 
     * server.setCommunicable(communicable); }
     * 
     * }
     */

}
