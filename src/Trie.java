import java.util.HashMap;
import java.util.Map;

class Trie
{
    // true when the node is a leaf node
    boolean isLeaf;

    Map<Character, Trie> character = new HashMap<>();

    // Constructor
    Trie() {
        isLeaf = false;
    }
}
