package Trie;

import java.util.*;

class Trie
{
    private final Node root = new Node();

    public void addString(String str)
    {
        // Last matching node, found in the trie
        Tuple<Node, Integer> lastNode = searchDown(str);

        // Add missing nodes to complete the string
        for ( ;lastNode.second < str.length(); lastNode.second++)
        {
            Node newNode = new Node(str.charAt(lastNode.second), lastNode.first);
            lastNode.first.addChild(newNode);
            lastNode.first = newNode;
        }

        // Mark as the end of a string
        lastNode.first.isLeaf = true;
    }

    public boolean searchString(String str)
    {
        // Last matching node, found in the trie
        Tuple<Node, Integer> lastNode = searchDown(str);
        return (lastNode.first.isLeaf && lastNode.second == str.length());
    }

    public List<String> searchPrefix(String pref)
    {
        if (pref.length() == 0)
        {
            List<String> retArr = new ArrayList<String>();
            for (Node node: root.children)
                retArr.addAll(getLeafs(node, ""));
            return retArr; 
        }

        Tuple<Node, Integer> lastNode = searchDown(pref);
        // Given prefix was not found
        if (lastNode.second != pref.length())
            return new ArrayList<>();
        return getLeafs(lastNode.first, pref.substring(0, pref.length() - 1));
    }

    public void deleteString(String str)
    {
        Tuple<Node, Integer> toDelete = searchDown(str);

        // Check if toDelete is the string we are looking for
        if(!toDelete.first.isLeaf || toDelete.second != str.length())
            throw new RuntimeException(
                        "ERROR: String \"" + str + "\" could not be found"
                );

        Node parent = toDelete.first.parent;
        Node current = toDelete.first;
        current.isLeaf = false;

        if (current.children.size() != 0)
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
            next = current.searchChild(pref.charAt(charPos++));
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
        boolean isLeaf;
        Node parent;

        TreeSet<Node> children = new TreeSet<Node>((o1, o2) -> o1.compareTo(o2));

        Node()
        {
            symb = 0;
            isLeaf = false;
            parent = null;
        }
        Node(char sym, Node prev)
        {
            this.symb = sym;
            this.parent = prev;
            this.isLeaf = false;
        }

        int compareTo(Node other)
        {
            if (this.symb == other.symb)
                return 0;
            return (this.symb > other.symb) ? 1 : -1;
        }

        void addChild(Node child)
        {
            children.add(child);
        }

        void deleteChild(Node to_delete)
        {
            children.remove(to_delete);
        }

        Node searchChild(char symb)
        {
            if (children.size() == 0)
                return null;

            Node retNode = null;
            Node[] childrenArr = children.toArray(new Node[0]);

            int low = 0, high = children.size() - 1;
            while (low <= high)
            {
                int mid = (low + high) / 2; 
                if (childrenArr[mid].symb < symb)
                    low = mid + 1;
                else if (childrenArr[mid].symb > symb)
                    high = mid - 1;
                else
                {
                    retNode = childrenArr[mid];
                    break;
                }
            }

            return retNode;
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
