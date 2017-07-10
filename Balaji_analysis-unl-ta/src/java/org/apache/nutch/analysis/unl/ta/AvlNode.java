package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.index.unl;

import java.io.*;

/**

/**
 * version history**
 *version number:1.1
@author subalalitha
 *
 * This class is used to store the document identifiers of all UNL graphs.
 * The key for the AVL Tree is the document id.
 * This is designed for forward indexing.
 * The purpose of forward indexing is used for Re-ranking.
 */
public class AvlNode implements Serializable {
    // Constructors

    public AvlNode() {
        element = null;
        left = null;
        right = null;
        height = 0;
        next = null;
    }

    AvlNode(Comparable theElement) {
        this(theElement, null, null, null);
    }

    AvlNode(Comparable theElement, AvlNode lt, AvlNode rt, String i) {
        element = theElement;

        left = lt;
        right = rt;
        height = 0;
        next = null;
        docid = i;
    }

    // Friendly data; accessible by other package routines
    public Comparable element;   //key which is the document identifier.
    public AvlNode left;         // Left child
    public AvlNode right;        // Right child
    public String docid;         //document id 
    public int height;       // Height
    public Avl_linknextNode next;//pointer to the next Avl_linknextNode.
}
    
