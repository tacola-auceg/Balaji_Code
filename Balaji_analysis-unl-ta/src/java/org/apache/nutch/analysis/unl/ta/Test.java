package org.apache.nutch.analysis.unl.ta;

/**
 * version history**
 *version number:1.1
@author subalalitha
 */
import java.io.*;
import java.util.*;

/**
This class is used mainly to print the c,cr and crc AVL and binary search trees after indices are built

 */
public class Test {

    public static void main(String args[]) {
        long starttime = System.currentTimeMillis();

        Hashtable fileList = new Hashtable();
        try {
            FileInputStream fos = new FileInputStream("./crawl-unl/filelist.txt");
            ObjectInputStream oos = new ObjectInputStream(fos);
            fileList = (Hashtable) oos.readObject();

            oos.close();
        } catch (Exception e) {
        }
        System.out.println(fileList.size());

        BinarySearchTree binaryTree = new BinarySearchTree();

        try {

            FileInputStream fis2 = new FileInputStream("./crawl-unl/" + args[0]);
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            String str = "";
            System.out.println("i am here");
            binaryTree.root = binaryTree.tree_FileRead(binaryTree.root, ois2, str);
            System.out.println("binarytree" + binaryTree);

            binaryTree.display_count1();
//binaryTree.printTree();    
            ois2.close();

        } catch (Exception e) {
        }
        long endtime = System.currentTimeMillis();
        double duration = (double) (endtime - starttime) / 1000.00;
        System.out.println("time taken" + duration);

    /*AvlTree avlTree=new AvlTree();
    try
    {

    FileInputStream fis=new FileInputStream("./crawl-unl/Avl_crc.txt");
    ObjectInputStream ois=new ObjectInputStream(fis);
    String str="";
    avlTree.root=avlTree.tree_FileRead(avlTree.root, ois,str);
    avlTree.printTree();
    ois.close();
    }catch(Exception e)
    {
    } */

    }
}
