package Trie;

import java.util.*;


// Trie interface
class Trie
{

    public void addString(String str) {
        implementation.addString(str);
    }

    public void deleteString(String str) {
        implementation.deleteString(str);
    }

    public boolean searchString(String str) {
        return implementation.searchString(str);
    }

    public List<String> searchPrefix(String pref) {
        return implementation.searchPrefix(pref);
    }

    private final TriePrivate implementation = new TriePrivate();
}

// Trie implementation
class TriePrivate
{
    private final Node root = new Node();

    public void addString(String str)
    {
        // Last matching node, found in the trie
        Tuple<Node, Integer> last_node = searchDown(str);

        // Add missing nodes to complete the string
        for ( ;last_node.second < str.length(); last_node.second++)
        {
            last_node.first.addChild(new Node(str.charAt(last_node.second), last_node.first));
            last_node.first = searchNode(last_node.first.children, str.charAt(last_node.second));
        }

        // Mark as the end of a string
        last_node.first.isLeaf = true;
    }

    public boolean searchString(String str)
    {
        // Last matching node, found in the trie
        Tuple<Node, Integer> last_node = searchDown(str);
        return (last_node.first.isLeaf && str.equals(constructString(last_node.first)));
    }

    public List<String> searchPrefix(String pref)
    {
        if (pref.length() == 0)
        {
            List<String> ret_arr = new ArrayList<String>();
            for (Node node: root.children)
                ret_arr.addAll(getLeafs(node, ""));
            return ret_arr; 
        }

        Tuple<Node, Integer> last_node = searchDown(pref);

        // Given prefix was not found
        if (last_node.second != pref.length())
            return new ArrayList<>();
        return getLeafs(last_node.first, pref.substring(0, pref.length() - 1));
    }

    public void deleteString(String str)
    {
        if (!searchString(str))
            throw new RuntimeException(
                    "ERROR: String \"" + str + "\" could not be found"
            );

        Tuple<Node, Integer> to_delete = searchDown(str);

        Node parent = to_delete.first.parent;
        Node current = to_delete.first;
        current.isLeaf = false;

        if (current.children.length != 0)
            return;

        while (!current.isLeaf && current != root)
        {
            // To avoid paired reference (parent<-->child) and memory leak
            current.parent = null;

            parent.deleteChild(current);

            current = parent;
            parent = current.parent;
        }
    }

    // Advance down the trie and search for pref while possible
    // If next char in sequence wasn't found return last found node and last char index
    private Tuple<Node, Integer> searchDown(String pref)
    {
        if (pref.length() == 0)
            return new Tuple<>(root, 0);

        int charPos = 0;
        Node current = root;
        Node next = root;

        do
        {
            current = next;
            next = searchNode(current.children, pref.charAt(charPos++));
        } while (next != null && charPos < pref.length());

        if (next != null)
            current = next;
        else charPos--;

        return new Tuple<>(current, charPos);
    }

    // Get all complete strings, following given node
    // Prefix - constructed string, preceding given node
    private List<String> getLeafs(Node node, String prefix)
    {
        List<String> ret_list = new ArrayList<>();
        if (node.isLeaf)
            ret_list.add(prefix + node.symb);

        for (Node current: node.children)
            ret_list.addAll(getLeafs(current, prefix + node.symb));

        return ret_list;
    }

    // Go up the trie and construct the string
    private String constructString(Node node)
    {
        Node current = node;
        StringBuilder returnString = new StringBuilder();

        while (current.parent != null)
        {
            returnString.insert(0, current.symb);
            current = current.parent;
        }
        return returnString.toString();
    }

    // Search for a node with a given symb in a given Node arr
    private Node searchNode(Node[] nodeArr, char sym)
    {
        for (Node node : nodeArr) if (node.symb == sym) return node;
        return null;
    }

    private static class Node
    {
        char symb;
        // If True, this node is the last symbol of some string in the Trie
        Boolean isLeaf;
        Node parent;
        Node[] children;

        Node()
        {
            symb = 0;
            isLeaf = false;
            parent = null;
            children = new Node[0];
        }
        Node(char sym, Node prev)
        {
            this.symb = sym;
            this.parent = prev;
            this.isLeaf = false;

            children = new Node[0];
        }

        void addChild(Node child)
        {
            Node[] newArr = new Node[children.length + 1];
            System.arraycopy(children, 0, newArr, 0, children.length);

            newArr[newArr.length - 1] = child;
            children = newArr;
        }

        void deleteChild(Node to_delete)
        {
            Node[] newArr = new Node[children.length - 1];

            int i = 0;
            for (Node child: children)
                if (child != to_delete)
                    newArr[i++] = child;

            children = newArr;
        }
    }
}

class Tuple<K, V>
{
    public K first;
    public V second;

    public Tuple(K first, V second)
    {
        this.first = first;
        this.second = second;
    }
}