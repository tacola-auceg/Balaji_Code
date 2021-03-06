package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.snippetgeneration.unl.ta;

import java.io.*;
//import org.apache.nutch.index.unl.*;
import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;

public class offline {

    public static BinarySearchTree bstcrc = new BinarySearchTree();//Indexed documents from the BST input
    public static BinarySearchTree bstcr = new BinarySearchTree();
    public static BinarySearchTree bstc = new BinarySearchTree();
    public static AvlTree avlcrc = new AvlTree();//Indexed documents from the AvlTree input
    public static AvlTree avlcr = new AvlTree();
    public static AvlTree avlc = new AvlTree();
    public static Configuration configuration = NutchConfiguration.create();
    public static String IndexHome = configuration.get("UNLCrawl");

    public static BinarySearchTree getcrc() {
        try {
            FileInputStream fis = new FileInputStream(IndexHome+"crc.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            String str = "";
            bstcrc.root = bstcrc.tree_FileRead(bstcrc.root, ois, str);
            ois.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bstcrc;
    }

    public static BinarySearchTree getcr() {
        try {
            FileInputStream fis1 = new FileInputStream(IndexHome+"cr.txt");
            ObjectInputStream ois1 = new ObjectInputStream(fis1);
            String str = "";
            bstcr.root = bstcr.tree_FileRead(bstcr.root, ois1, str);
            ois1.close();
            fis1.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bstcr;
    }

    public static BinarySearchTree getc() {
        try {
            FileInputStream fis2 = new FileInputStream(IndexHome+"c.txt");
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            String str = "";
            bstc.root = bstc.tree_FileRead(bstc.root, ois2, str);
            ois2.close();
            fis2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bstc;
    }

    public static AvlTree getavlc() {
        try {
            FileInputStream fis2 = new FileInputStream(IndexHome+"Avl_c.txt");
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            String str = "";
            avlc.root = avlc.tree_FileRead(avlc.root, ois2, str);
            ois2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return avlc;
    }

    public static AvlTree getavlcr() {
        try {
            FileInputStream fis2 = new FileInputStream(IndexHome+"Avl_cr.txt");
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            String str = "";
            avlcr.root = avlcr.tree_FileRead(avlcr.root, ois2, str);
            ois2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return avlcr;
    }

    public static AvlTree getavlcrc() {
        try {
            FileInputStream fis2 = new FileInputStream(IndexHome+"Avl_crc.txt");
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            String str = "";
            avlcrc.root = avlcrc.tree_FileRead(avlcrc.root, ois2, str);
            ois2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return avlcrc;
    }
}

