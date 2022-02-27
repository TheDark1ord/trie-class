package Trie;

import java.util.*;

class Trie
{
    Trie() {};

    Trie(String[] args)
    {
        for (String str: args) this.addString(str);
    }

    Trie(Collection<String> args)
    {
        for (String str: args) this.addString(str);
    }

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

    public boolean containsString(String str)
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
            for (Node node: root.children.values())
                retArr.addAll(getLeafs(node, new StringBuilder()));
            return retArr; 
        }

        Tuple<Node, Integer> lastNode = searchDown(pref);
        // Given prefix was not found
        if (lastNode.second != pref.length())
            return new ArrayList<>();
        return getLeafs(lastNode.first, new StringBuilder(pref.substring(0, pref.length() - 1)));
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

        while (current.children.size() == 0 && !current.isLeaf && current != root)
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
    private List<String> getLeafs(Node node, StringBuilder prefix)
    {
        List<String> ret_list = new ArrayList<>();
        if (node.isLeaf)
            ret_list.add(prefix.toString() + node.symb);

        prefix.append(node.symb);
        for (Node current: node.children.values())
            ret_list.addAll(getLeafs(current, prefix));
        prefix.deleteCharAt(prefix.length() - 1);

        return ret_list;
    }

    private final Node root = new Node();
}
