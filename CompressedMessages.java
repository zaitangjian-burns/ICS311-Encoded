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

public class CompressedMessages {

    // Compresses the message using run-length encoding
    public static String runLengthEncode(String input) {
        StringBuilder encoded = new StringBuilder();
        int n = input.length();
        
        for (int i = 0; i < n; i++) {
            int count = 1;
            
            while (i + 1 < n && input.charAt(i) == input.charAt(i + 1)) {
                i++;
                count++;
            }
            encoded.append(input.charAt(i)).append(count);
        }
        
        return encoded.toString();
    }

    public static void main(String[] args) {
        // Create the communication graph
        CommunicationGraph graph = new CommunicationGraph();

        // Add people (nodes) to the graph  
        Node alice = graph.addNode("Alice");
        Node bob = graph.addNode("Bob");

        // Assign metadata to nodes
        alice.setMetadata("email", "alice@example.com");
        bob.setMetadata("email", "bob@example.com");

        // Create a message body and apply RLE compression
        String messageBody = "AAAABBBCCDAA";
        String compressedBody = runLengthEncode(messageBody);

        // Create and send a compressed message
        Message message = new Message("Alice", "Bob", "RLE compressed", compressedBody);
        graph.sendMessage(message);

        // Another example of message
        String messageBody2 = "PPPPQQQQQ";
        String compressedBody2 = runLengthEncode(messageBody2);
        Message message2 = new Message("Bob", "Alice", "RLE compressed", compressedBody2);
        graph.sendMessage(message2);
    }
}
