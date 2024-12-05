import java.security.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;

class Node {
    private final String id; // Unique identifier for the person
    private final Map<String, String> metadata; // Additional data for the person
    private final KeyPair rsaKeyPair; // Public and private keys for encryption

    public Node(String id) throws NoSuchAlgorithmException {
        this.id = id;
        this.metadata = new HashMap<>();
        this.rsaKeyPair = generateRSAKeyPair();
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

    public PublicKey getPublicKey() {
        return rsaKeyPair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return rsaKeyPair.getPrivate();
    }

    private KeyPair generateRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        return keyGen.generateKeyPair();
    }
}

class Message {
    private final String sender;
    private final String receiver;
    private final String metadata;
    private final String body;

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
    private final Map<String, Node> nodes;

    public CommunicationGraph() {
        this.nodes = new HashMap<>();
    }

    public Node addNode(String id) throws NoSuchAlgorithmException {
        Node node = new Node(id);
        nodes.put(id, node);
        return node;
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

        System.out.println("Message sent successfully:\n" + message);
    }
}

class RSAEncryption {

    public static String encrypt(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(message.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedMessage, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedMessage));
        return new String(decryptedBytes);
    }
}
/**
Demonstration for the messaging system with RSA encryption
 */
public class RSAMessage {
    public static void main(String[] args) {
        try {
            // Create the communication graph
            CommunicationGraph graph = new CommunicationGraph();

            // Add nodes (users) to the graph
            Node alice = graph.addNode("Alice");
            Node bob = graph.addNode("Bob");

            // Alice sends an encrypted message to Bob
            String aliceMessage = "Hello, Bob! This is a secure message.";
            String aliceEncryptedMessage = RSAEncryption.encrypt(aliceMessage, bob.getPublicKey());

            // Create the message with metadata (indicating RSA encryption)
            Message aliceToBobMessage = new Message("Alice", "Bob", "RSA Encrypted", aliceEncryptedMessage);
            graph.sendMessage(aliceToBobMessage);

            // Bob decrypts the message
            String bobDecryptedMessage = RSAEncryption.decrypt(aliceEncryptedMessage, bob.getPrivateKey());
            System.out.println("\nBob decrypted the message: " + bobDecryptedMessage);

            // Bob sends an encrypted response to Alice
            String bobMessage = "Hi Alice! Got your secure message. Here's my reply.";
            String bobEncryptedMessage = RSAEncryption.encrypt(bobMessage, alice.getPublicKey());

            // Create the response message with metadata
            Message bobToAliceMessage = new Message("Bob", "Alice", "RSA Encrypted", bobEncryptedMessage);
            graph.sendMessage(bobToAliceMessage);

            // Alice decrypts Bob's response
            String aliceDecryptedMessage = RSAEncryption.decrypt(bobEncryptedMessage, alice.getPrivateKey());
            System.out.println("\nAlice decrypted the message: " + aliceDecryptedMessage);

        } catch (Exception e) {
        }
    }
}
