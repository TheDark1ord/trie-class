package Trie;

import java.util.*;

class Node
    {
        char symb;
        // If True, this node is the last symbol of some string in the Trie
        boolean isLeaf;
        Node parent;

        HashMap<Character, Node> children = new HashMap<>();

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

        void addChild(Node newNode)
        {
            children.put(newNode.symb, newNode);
        }

        void deleteChild(Node to_delete)
        {
            children.remove(to_delete.symb);
        }

        Node searchChild(char symb)
        {
            return children.get(symb);
        }
    }