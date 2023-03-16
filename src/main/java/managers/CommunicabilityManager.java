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
        System.out.println("Do BFS from: " + node.getId());

        List<NetworkNode> visited = new ArrayList<>();
        Queue<NetworkNode> queue = new ArrayDeque<>();
        queue.add(node);
        visited.add(node);

        while (!queue.isEmpty()) {
            NetworkNode curr = queue.poll();
            /*
             * System.out.println("bfs looking at neighbours of: " + curr.getId());
             * System.out.println("immediate neighbours: " +
             * curr.getVisible().stream().map(n -> n.getId()).collect(Collectors.toList()));
             */

            for (NetworkNode next : curr.getVisible()) {
                // System.out.println(curr.getVisible().stream().map(e ->
                // e.getId()).collect(Collectors.toList()));
                if (!visited.contains(next)) {
                    queue.add(next);
                    visited.add(next);
                }
            }
        }

        node.setCommunicable(visited);
        System.out.println(node.getCommunicable().stream().map(n -> n.getId()).collect(Collectors.toList()));

        // List<NetworkNode> communicable = visited.stream().filter(n -> n !=
        // node).collect(Collectors.toList());
        // System.out
        // .println(node.getId() + ": " + communicable.stream().map(n ->
        // n.getId()).collect(Collectors.toList()));
        // node.setCommunicable(communicable);
        return visited;
    }

    public static void update(Map<String, NetworkNode> nodes) {

        // Distribute visibility graph adjaceny list among nodes
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
            // System.out.println("set visible nodes of " + server.getId() + " can see: "
            // + server.getVisible().stream().map(e ->
            // e.getId()).collect(Collectors.toList()));
            visible = new ArrayList<>();
        }

        /*
         * for (NetworkNode node : nodes.values()) { System.out.println(node.getId() +
         * ": visible neighbours -> " + node.getVisible().stream().map(e ->
         * e.getId()).collect(Collectors.toList())); }
         */

        // Run modified BFS across visibility graph to update communicability
        for (NetworkNode server : nodes.values()) {
            getAndUpdateCommunicableEntitiesInRange(server);
            /*
             * System.out.println(server.getId() + ": communicable with -> " +
             * server.getCommunicable().stream().map(e ->
             * e.getId()).collect(Collectors.toList()));
             */
        }

    }

}
