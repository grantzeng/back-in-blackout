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
                if (curr.canSendTo(next) && !visited.contains(next)) {
                    visited.add(next);
                    if (next.type() == RelaySatellite) {
                        queue.add(next);
                    }
                }
            }
        }

        return visited.stream().filter(n -> n != node).collect(Collectors.toList());
    }

    public static void update(Map<String, Server> servers) {

        for (Server server : servers.values()) {
            Map<String, Server> communicable = new HashMap<>();

            findCommunicableEntitiesInRange(server.getOwner()).stream().map(n -> n.getServer())
                    .forEach(s -> communicable.put(s.getId(), s));

            server.setCommunicable(communicable);
        }

    }

}
