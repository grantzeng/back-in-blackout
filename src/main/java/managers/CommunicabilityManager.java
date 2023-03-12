package managers;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import static nodes.NetworkNode.NodeType.RelaySatellite;
import networking.Server;
import nodes.NetworkNode;

public class CommunicabilityManager {
    public static List<String> communicableEntitiesInRange(NetworkNode node) {

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

        return visited.stream().filter(n -> n != node).map(n -> n.getId()).collect(Collectors.toList());
    }

    public static void update(Map<String, Server> servers) {

    }

}
