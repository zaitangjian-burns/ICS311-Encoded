/**
IMPORTANT:
THIS FILE IS TO BE COPIED AND PASTED FOR BOTH IMPLEMENTATIONS. THIS IS A BASE TO WORK FROM, MEETING THE GENERAL STRUCTURE REQUIREMENTS
WITHIN BOTH OTHER FILES ARE THIS ON TOP OF THE REST OF THE IMPLEMENATION
 */


import java.util.*;

class Node {
    private final String id; // Unique identifier for the person
    private final Map<String, String> metadata; // Additional data for the person

    public Node(String id) {
        this.id = id;
        this.metadata = new HashMap<>();
    }

    public String getId() {
        return id;
    }

    public void setMetadata(String key, String value) {
        metadata.put(key, value);
    }

    public String getMetadata(String key) {
        return metadata.getOrDefault(key, null);
    }
}

class Message {
    private final String sender; // ID of the sender
    private final String receiver; // ID of the receiver
    private final String metadata; // Information about the message (e.g., compression type)
    private final String body; // The actual message content

    public Message(String sender, String receiver, String metadata, String body) {
        this.sender = sender;
        this.receiver = receiver;
        this.metadata = metadata;
        this.body = body;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMetadata() {
        return metadata;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "Message from " + sender + " to " + receiver + "\n" +
               "Metadata: " + metadata + "\n" +
               "Body: " + body;
    }
}

class CommunicationGraph {
    private final Map<String, Node> nodes; // All people in the graph

    public CommunicationGraph() {
        this.nodes = new HashMap<>();
    }

    public Node addNode(String id) {
        if (!nodes.containsKey(id)) {
            Node node = new Node(id);
            nodes.put(id, node);
            return node;
        }
        return nodes.get(id);
    }

    public Node getNode(String id) {
        return nodes.getOrDefault(id, null);
    }

    public void sendMessage(Message message) {
        Node sender = nodes.get(message.getSender());
        Node receiver = nodes.get(message.getReceiver());

        if (sender == null || receiver == null) {
            System.out.println("Error: Sender or receiver not found in the graph.");
            return;
        }

        // Simulate message sending
        System.out.println("Message sent successfully:\n" + message);
    }
}

public class SecureCommunication {
    public static void main(String[] args) {
        // Create the communication graph
        CommunicationGraph graph = new CommunicationGraph();

        // Add people (nodes) to the graph  
        Node alice = graph.addNode("Alice");
        Node bob = graph.addNode("Bob");

        // Assign metadata to nodes
        alice.setMetadata("email", "alice@example.com");
        bob.setMetadata("email", "bob@example.com");

        // Create and send a message
        Message message = new Message("Alice", "Bob", "Basic message", "Hello Bob!");
        graph.sendMessage(message);

        // Create and send another message
        Message message2 = new Message("Charlie", "Alice", "Basic message", "Hey Alice, how are you?");
        graph.sendMessage(message2);
    }
}
