package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.index.unl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.*;
import org.apache.nutch.analysis.unl.ta.*;
import java.util.*;
import java.io.*;
/**
 * version history**
 *version number:1.1 (indicates developments done after  5th PRSG Meet)
  @author subalalitha
 */
/**
 * Implements an AVL tree.
 * The key for the AVL Tree is the document id.
 * This is designed for forward indexing.
 * The purpose of forward indexing is used for Re-ranking.
 * Note that all "matching" is based on the compareTo method.
 * Methods without comments are not currently used but used previously and they are not deleted which may be used later
 * 
 */
public class AvlTree implements Serializable {

    /**
     * Construct the tree.
     */
    public AvlTree() {
        root = null;
    }

    /**
     * Insert into the tree; duplicates are ignored.
     * @param x the item to insert.
     */
    public void insert(Comparable x, String doc) {
        root = insert(x, doc, root);
    }

    /**
     * Remove from the tree. Nothing is done if x is not found.
     * @param x the item to remove.
     */
    public void remove(Comparable x) {
        ////System.out.println( "Sorry, remove unimplemented" );
        }
//The function tree_FileRead is used to read the AVL Tree from a binary file.

    public static AvlNode tree_FileRead(AvlNode r, ObjectInputStream input, String str) throws IOException, ClassNotFoundException {
        try {

            str = (String) input.readObject();
            if (str.equalsIgnoreCase("Avl NODE EXISTS")) {
                r = new AvlNode();
                r.element = (Comparable) input.readObject();
                r.docid = (String) input.readObject();
                r.height = ((Integer) input.readObject()).intValue();
                str = (String) input.readObject();
                if (str.equalsIgnoreCase("Avl Next NODE EXISTS")) {
                    Avl_linknextNode dr = new Avl_linknextNode();
                    dr.docid = (Comparable) input.readObject();
                    dr.concept_name = (String) input.readObject();
                    dr.Reln_name = (String) input.readObject();
                    dr.Toconcept_name = (String) input.readObject();
                    dr.frequency = ((Integer) input.readObject()).intValue();
                    dr.vbcheck = (String) input.readObject();
                    dr.gn_word = (String) input.readObject();
                    dr.senid = (String) input.readObject();
                    dr.weight = ((Integer) input.readObject()).intValue();
                    dr.tamcon = (String) input.readObject();
                    dr.topos = (String) input.readObject();
                    r.next = dr;
                    str = (String) input.readObject();
                    while (str.equalsIgnoreCase("Avl Next NODE EXISTS")) {
                        Avl_linknextNode dr1 = new Avl_linknextNode();
                        dr1.docid = (Comparable) input.readObject();
                        dr1.concept_name = (String) input.readObject();
                        dr1.Reln_name = (String) input.readObject();
                        dr1.Toconcept_name = (String) input.readObject();
                        dr1.frequency = ((Integer) input.readObject()).intValue();
                        dr1.vbcheck = (String) input.readObject();
                        dr1.gn_word = (String) input.readObject();
                        dr1.senid = (String) input.readObject();
                        dr1.weight = ((Integer) input.readObject()).intValue();
                        dr1.tamcon = (String) input.readObject();
                        dr1.topos = (String) input.readObject();
                        dr.next = dr1;
                        dr = dr1;
                        str = (String) input.readObject();
                    }

                }
                str = (String) input.readObject();
                if (str.equalsIgnoreCase("LEFT BNODE")) {
                    r.left = tree_FileRead(r.left, input, str);
                }
                str = (String) input.readObject();
                if (str.equalsIgnoreCase("RIGHT BNODE")) {
                    r.right = tree_FileRead(r.right, input, str);
                }
            }
        } catch (EOFException e) {
            e.printStackTrace();
        }
        return r;
    }
//the function tree_FileWrite is used to construct the Binaryfile

    public static void tree_FileWrite(AvlNode r, ObjectOutputStream output) throws IOException {
        if (r != null) {
            output.writeObject("Avl NODE EXISTS");
            output.writeObject(r.element);
            output.writeObject(r.docid);
            output.writeObject(r.height);
            Avl_linknextNode d = new Avl_linknextNode();
            d = r.next;
            while (d != null) {
                output.writeObject("Avl Next NODE EXISTS");
                output.writeObject(d.docid);
                output.writeObject(d.concept_name);
                output.writeObject(d.Reln_name);
                output.writeObject(d.Toconcept_name);
                output.writeObject(d.frequency);
                output.writeObject(d.vbcheck);
                output.writeObject(d.gn_word);
                output.writeObject(d.senid);
                output.writeObject(d.weight);
                output.writeObject(d.tamcon);
                output.writeObject(d.topos);
                d = d.next;
            }
            output.writeObject("NO Avl Next NODE Exists");
            output.writeObject("LEFT BNODE");
            tree_FileWrite(r.left, output);
            output.writeObject("RIGHT BNODE");
            tree_FileWrite(r.right, output);
        } else {
            output.writeObject("Avl NODE DOES NOT EXISTS");
            return;
        }

    }

    /**
     * Find the smallest item in the tree.
     * @return smallest item or null if empty.
     */
    public Comparable findMin() {
        return elementAt(findMin(root));
    }

    /**
     * Find the largest item in the tree.
     * @return the largest item of null if empty.
     */
    public Comparable findMax() {
        return elementAt(findMax(root));
    }

    /**
     * Find an item in the tree.
     * @param x the item to search for.
     * @return the matching item or null if not found.
     */
    public AvlNode find(Comparable x) {
        return (find(x, root));
    }

    /**
     * Make the tree logically empty.
     */
    public void makeEmpty() {
        root = null;
    }

    /**
     * Test if the tree is logically empty.
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Print the tree contents in sorted order.
     */
    public void printTree() {
        if (isEmpty()) {
            //System.out.println("Empty tree");
        } else {
            printTree(root);
        }
    }

    public void fillWeight(int flag) throws IOException {
        if (isEmpty()) {
            //System.out.println("Empty tree");
        } else {
            fillWeight(root, flag);
        }
    }

    /**
     * Internal method to get element field.
     * @param t the node.
     * @return the element field or null if t is null.
     */
    private Comparable elementAt(AvlNode t) {
        return t == null ? null : t.element;
    }

    /**
     * Internal method to insert into a subtree.
     * @param x the item to insert.
     * @param t the node that roots the tree.
     * @param  doc document identifier
     * @return the new root.
     */
    public AvlNode insert(Comparable x, String doc, AvlNode t) {
        if (t == null) {
            t = new AvlNode();
            t.element = x;
            t.docid = doc;
        //insert_Avl_linknextNode(x,t );
        } else if (x.compareTo(t.element) < 0) {
            t.left = insert(x, doc, t.left);
            if (height(t.left) - height(t.right) == 2) {
                if (x.compareTo(t.left.element) < 0) {
                    t = rotateWithLeftChild(t);
                } else {
                    t = doubleWithLeftChild(t);
                }
            }
        } else if (x.compareTo(t.element) > 0) {
            t.right = insert(x, doc, t.right);
            if (height(t.right) - height(t.left) == 2) {
                if (x.compareTo(t.right.element) > 0) {
                    t = rotateWithRightChild(t);
                } else {
                    t = doubleWithRightChild(t);
                }
            }
        } else;  // Duplicate; do nothing
        t.height = max(height(t.left), height(t.right)) + 1;
        return t;
    }
//The function  insert_Avl_linknextNodestores all the indices in a particular unl graph

    public void insert_Avl_linknextNode(Comparable i, String concept_name, String reln, String Toconcept, String sen, int w, String vb, int freq, String tamil, String tamcon, String pos) {
	 
        // TODO Auto-generated method stub

        Avl_linknextNode next_node = new Avl_linknextNode();
        Avl_linknextNode next_node1 = new Avl_linknextNode();
        int flag = 0;
        int sen_weight = 0;
        AvlNode node = new AvlNode();
        int counter = 0;
        node = find(i);
        if ((node != null) && (node.next == null)) {
            node.next = next_node;
            next_node.concept_name = concept_name;
            next_node.Reln_name = reln;
            next_node.Toconcept_name = Toconcept;
            next_node.senid = sen;
            next_node.vbcheck = vb;
            next_node.frequency = freq;
            next_node.gn_word = tamil;
            next_node.tamcon = tamcon;
            next_node.topos = pos;
            if (sen.equals("s1")) {
                sen_weight = 100;
            } else if (sen.equals("s2")) {
                sen_weight = 75;
            } else if (sen.equals("s3") | (sen.equals("s4")) || (sen.equals("s5"))) {
                sen_weight = 50;
            }
            next_node.weight = next_node.weight + sen_weight;
            next_node.next = null;
        } else {
            Avl_linknextNode next_node2 = new Avl_linknextNode();
            Avl_linknextNode next_node3 = new Avl_linknextNode();
            next_node1 = node.next;
            while (next_node1 != null) {
                next_node3 = next_node1;
                if ((next_node1.concept_name + next_node1.Reln_name + next_node1.Toconcept_name).equalsIgnoreCase(concept_name + reln + Toconcept)) {
                    counter = 1;
                    break;
                }

                next_node1 = next_node1.next;
            }

            next_node3.weight = ++next_node3.weight;
            if (counter == 0) {
                next_node3.next = next_node2;
                next_node2.concept_name = concept_name;
                next_node2.Reln_name = reln;
                next_node2.Toconcept_name = Toconcept;
                next_node2.senid = sen;
                next_node2.vbcheck = vb;
                next_node2.frequency = freq;
                next_node2.gn_word = tamil;
                next_node2.tamcon = tamcon;
                next_node2.topos = pos;
                next_node2.next = null;
            }
        }

    ////System.out.println("the nooooooooooooo"+node.element);

    }
//The function  insert_Avl_linknextNode1 stores all the indices in a particular unl graph

    public void insert_Avl_linknextNode1(Comparable i, String concept_name, String reln, String Toconcept, String sen, int weight, String vb, int freq, String tamil, String tam, String pos) {// TODO Auto-generated method stub
        ////System.out.println("insert_Avl_linknextNode");
        Avl_linknextNode next_node = new Avl_linknextNode();
        Avl_linknextNode next_node1 = new Avl_linknextNode();
        int flag = 0;
        int sen_weight = 0;
        AvlNode node = new AvlNode();
        int counter = 0;
        node = find(i);
        if ((node != null) && (node.next == null)) {
            node.next = next_node;
            next_node.concept_name = concept_name;
            next_node.Reln_name = reln;
            next_node.Toconcept_name = Toconcept;
            next_node.senid = sen;
            next_node.vbcheck = vb;
            next_node.frequency = freq;
            next_node.gn_word = tamil;
            next_node.tamcon = tam;
            if (sen.equals("s1")) {
                sen_weight = 100;
            } else if (sen.equals("s2")) {
                sen_weight = 75;
            } else if (sen.equals("s3") || (sen.equals("s4")) || (sen.equals("s5"))) {
                sen_weight = 50;
            }
            next_node.weight = next_node.weight + sen_weight;
            next_node.next = null;
        } else {
            Avl_linknextNode next_node2 = new Avl_linknextNode();
            Avl_linknextNode next_node3 = new Avl_linknextNode();
            next_node1 = node.next;
            while (next_node1 != null) {
                next_node3 = next_node1;
                if ((next_node1.concept_name + next_node1.Reln_name).equalsIgnoreCase(concept_name + reln)) {
                    counter = 1;
                    break;
                }

                next_node1 = next_node1.next;
            }
            next_node3.weight = ++next_node3.weight;
            if (counter == 0) {
                next_node3.next = next_node2;
                next_node2.concept_name = concept_name;
                next_node2.Reln_name = reln;
                next_node2.Toconcept_name = Toconcept;
                next_node2.senid = sen;
                next_node2.vbcheck = vb;
                next_node2.frequency = freq;
                next_node2.gn_word = tamil;
                next_node2.tamcon = tam;
                next_node2.next = null;
            }
        }



    }
//The function  insert_Avl_linknextNode2 stores all the indices in a particular unl graph

    public void insert_Avl_linknextNode2(Comparable i, String concept_name, String reln, String Toconcept, String sen, int weight, String vb, int freq, String tamil, String tam, String pos) {

        Avl_linknextNode next_node = new Avl_linknextNode();
        Avl_linknextNode next_node1 = new Avl_linknextNode();
        int flag = 0;
        int sen_weight = 0;
        AvlNode node = new AvlNode();
        int counter = 0;
        node = find(i);
        if ((node != null) && (node.next == null)) {
            node.next = next_node;
            next_node.concept_name = concept_name;
            next_node.Reln_name = reln;
            next_node.Toconcept_name = Toconcept;
            next_node.senid = sen;
            next_node.vbcheck = vb;
            next_node.frequency = freq;
            next_node.gn_word = tamil;
            next_node.tamcon = tam;
            if (sen.equals("s1")) {
                sen_weight = 100;
            } else if (sen.equals("s2")) {
                sen_weight = 75;
            } else if (sen.equals("s3") | (sen.equals("s4")) || (sen.equals("s5"))) {
                sen_weight = 50;
            }
            next_node.weight = next_node.weight + sen_weight;
            next_node.next = null;
        } else {
            Avl_linknextNode next_node2 = new Avl_linknextNode();
            Avl_linknextNode next_node3 = new Avl_linknextNode();
            //node=find(i);
            next_node1 = node.next;
            while (next_node1 != null) {
                next_node3 = next_node1;
                if (next_node1.concept_name.equalsIgnoreCase(concept_name)) {

		    //System.out.println("Concept name-->"+concept_name+" "+sen);
		    next_node3.senid=next_node3.senid+sen;
                    counter = 1;
                    break;
                }
                next_node1 = next_node1.next;
            }
            next_node3.weight = ++next_node3.weight;
	    //System.out.println("Sentence id-->"+next_node3.gn_word+"--->"+next_node3.senid);		
            if (counter == 0) {
                next_node3.next = next_node2;
                next_node2.concept_name = concept_name;
                next_node2.Reln_name = reln;
                next_node2.Toconcept_name = Toconcept;
                next_node2.senid = sen;
                next_node2.vbcheck = vb;
                next_node2.frequency = freq;
                next_node2.gn_word = tamil;
                next_node2.tamcon = tam;
                next_node2.next = null;
            }
        }



    }

    /**
     * Internal method to find the smallest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the smallest item.
     */
    private AvlNode findMin(AvlNode t) {
        if (t == null) {
            return t;
        }

        while (t.left != null) {
            t = t.left;
        }
        return t;
    }

    /**
     * Internal method to find the largest item in a subtree.
     * @param t the node that roots the tree.
     * @return node containing the largest item.
     */
    private AvlNode findMax(AvlNode t) {
        if (t == null) {
            return t;
        }
        while (t.right != null) {
            t = t.right;
        }
        return t;
    }

    /**
     * Internal method to find an item in a subtree.
     * @param x is item to search for.
     * @param t the node that roots the tree.
     * @return node containing the matched item.
     */
    private AvlNode find(Comparable x, AvlNode t) {
        while (t != null) {
            if (x.compareTo(t.element) < 0) {
                t = t.left;
            } else if (x.compareTo(t.element) > 0) {
                t = t.right;
            } else {
                return t;    // Match
            }
        }
        return null;   // No match
    }

    /**
     * Internal method to print a subtree in sorted order.
     * @param t the node that roots the tree.
     */
    private void printTree(AvlNode t) {
        if (t != null) {	//////System.out.println("\n");
            printTree(t.left);
            //////System.out.println( t.element );
            display_connected_nodes(t);
            printTree(t.right);
        }
    }

    private void fillWeight(AvlNode t, int flg) throws IOException {//insert
        if (t != null) {	////System.out.println("\n");
            fillWeight(t.left, flg);
            //////System.out.println( t.element );
            fill_connected_nodes(t, flg);
            fillWeight(t.right, flg);
        }
    }
// The function  display_connected_nodesis used to display the AVL Tree in an inorder manner using buffer writer

    public void display_connected_nodes(AvlNode t) {
        try {
            BufferedWriter buff = new BufferedWriter(new FileWriter("AvlTree.txt", true));
            buff.append("the Doc id" + t.element);
            buff.append("\n");
            Avl_linknextNode next_node = new Avl_linknextNode();
            next_node = t.next;
            while (next_node != null) {
                buff.append("the concept" + next_node.concept_name + "\n");
                buff.append("the relation" + next_node.Reln_name + "\n");
                buff.append("the connected concept" + next_node.Toconcept_name + "\n");
                buff.append("the sentence id:" + next_node.senid + "\n");
                buff.append("the weight" + next_node.weight + "\n");
                buff.append("\n");
                next_node = next_node.next;
            }
            buff.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void fill_connected_nodes(AvlNode t, int flag) throws IOException {
        
        Avl_linknextNode next_node = new Avl_linknextNode();
        next_node = t.next;
        while (next_node != null) {
            int weight = 0;
            
            if (flag == 1) {
                weight = computeWeight(next_node.concept_name, next_node.senid, t.element);
            } else if (flag == 2) {
                weight = computeWeight(next_node.concept_name, next_node.Reln_name, next_node.senid, t.element);
            } else if (flag == 3) {
                weight = computeWeight(next_node.concept_name, next_node.Reln_name, next_node.Toconcept_name, next_node.senid, t.element);
            }
          
            next_node.weight = weight;
            next_node = next_node.next;
        }
    }

    public int computeWeight(String concept_name, String reln, String senid, Comparable i) throws IOException {

        int sen_weight = 0;

        int count_c = find_count1(i, concept_name, reln);
        if (senid.equals("s1")) {
            sen_weight = 100;
        } else if (senid.equals("s2")) {
            sen_weight = 75;
        } else if (senid.equals("s3") || (senid.equals("s4")) || (senid.equals("s5"))) {
            sen_weight = 50;
        }
        int weight_c = (count_c * 5) + sen_weight;
        return weight_c;
    }

    public int computeWeight(String concept_name, String reln, String toconcept, String senid, Comparable i) throws IOException {

        int sen_weight = 0;

        int count_c = find_count(i, concept_name, reln, toconcept);
        if (senid.equals("s1")) {
            sen_weight = 100;
        } else if (senid.equals("s2")) {
            sen_weight = 75;
        } else if (senid.equals("s3") || (senid.equals("s4")) || (senid.equals("s5"))) {
            sen_weight = 50;
        }
        int weight_c = (count_c * 5) + sen_weight;
        return weight_c;
    }

    public int computeWeight(String concept_name, String senid, Comparable i) throws IOException {

        int sen_weight = 0;

        int count_c = find_count2(i, concept_name);
        if (senid.equals("s1")) {
            sen_weight = 100;
        } else if (senid.equals("s2")) {
            sen_weight = 75;
        } else if (senid.equals("s3") || (senid.equals("s4")) || (senid.equals("s5"))) {
            sen_weight = 50;
        }
        int weight_c = (count_c * 5) + sen_weight;
        return weight_c;
    }

  /*  public int find_count(Comparable x, String con, String rel, String tocon) {
        AvlNode an = new AvlNode();
        Avl_linknextNode anl = new Avl_linknextNode();
        an = find(x);
        int count = 0;
        if (an != null) {
            anl = an.next;

            while (anl != null) {
                if ((anl.concept_name + anl.Reln_name + anl.Toconcept_name).equals(con + rel + tocon)) {
                    //count++;
                    break;
                }
                anl = anl.next;

            }
        }
        return anl.weight;
    }

   public int find_count1(Comparable x, String con, String rel) {
        AvlNode an = new AvlNode();
        Avl_linknextNode anl = new Avl_linknextNode();
        an = find(x);
        int count = 0;
        if (an != null) {
            anl = an.next;

            while (anl != null) {
                if ((anl.concept_name + anl.Reln_name).equals(con + rel)) {
                    //count++;
                    break;
                }
                anl = anl.next;

            }
        }
        return anl.weight;
    }

    public int find_count2(Comparable x, String con) {
        AvlNode an = new AvlNode();
        Avl_linknextNode anl = new Avl_linknextNode();
        an = find(x);

        int count = 0;
        if (an != null) {
            anl = an.next;
            while (anl != null) {
                if (anl.concept_name.equals(con)) {
                    //count++;
                    break;
                }
                anl = anl.next;

            }
		return anl.weight;
        }
	
        
    }
*/
public int find_count(Comparable x,String con,String rel,String tocon)
{
	AvlNode an=new AvlNode ();
	Avl_linknextNode anl=new Avl_linknextNode();
	an=find(x);
	int count =0;
	if(an!=null)
	{
	anl=an.next;
	
	while(anl!=null)
	{
		if((anl.concept_name+anl.Reln_name+anl.Toconcept_name).equals(con+rel+tocon))
		{
			count++;
		}
		anl=anl.next;
		
	}
	}
	return count;
}
public int find_count1(Comparable x,String con,String rel)
{
	AvlNode an=new AvlNode ();
	Avl_linknextNode anl=new Avl_linknextNode();
	an=find(x);
	int count =0;
	if(an!=null)
	{
	anl=an.next;
	
	while(anl!=null)
	{
		if((anl.concept_name+anl.Reln_name).equals(con+rel))
		{
			count++;
		}
		anl=anl.next;
		
	}
	}
	return count;
}
public int find_count2(Comparable x,String con)
{
	AvlNode an=new AvlNode ();
	Avl_linknextNode anl=new Avl_linknextNode();
	an=find(x);
	
	int count =0;
	if(an!=null){
	anl=an.next;
	while(anl!=null)
	{
		if(anl.concept_name.equals(con))
		{
			count++;
		}
		anl=anl.next;
		
	}
	}
	return count;
}
    /**
     * Return the height of node t, or -1, if null.
     */
    private static int height(AvlNode t) {
        return t == null ? -1 : t.height;
    }

    /**
     * Return maximum of lhs and rhs.
     */
    private static int max(int lhs, int rhs) {
        return lhs > rhs ? lhs : rhs;
    }

    /**
     * Rotate binary tree node with left child.
     * For AVL trees, this is a single rotation for case 1.
     * Update heights, then return new root.
     */
    private static AvlNode rotateWithLeftChild(AvlNode k2) {
        AvlNode k1 = k2.left;
        k2.left = k1.right;
        k1.right = k2;
        k2.height = max(height(k2.left), height(k2.right)) + 1;
        k1.height = max(height(k1.left), k2.height) + 1;
        return k1;
    }

    /**
     * Rotate binary tree node with right child.
     * For AVL trees, this is a single rotation for case 4.
     * Update heights, then return new root.
     */
    private static AvlNode rotateWithRightChild(AvlNode k1) {
        AvlNode k2 = k1.right;
        k1.right = k2.left;
        k2.left = k1;
        k1.height = max(height(k1.left), height(k1.right)) + 1;
        k2.height = max(height(k2.right), k1.height) + 1;
        return k2;
    }

    /**
     * Double rotate binary tree node: first left child
     * with its right child; then node k3 with new left child.
     * For AVL trees, this is a double rotation for case 2.
     * Update heights, then return new root.
     */
    private static AvlNode doubleWithLeftChild(AvlNode k3) {
        k3.left = rotateWithRightChild(k3.left);
        return rotateWithLeftChild(k3);
    }

    /**
     * Double rotate binary tree node: first right child
     * with its left child; then node k1 with new right child.
     * For AVL trees, this is a double rotation for case 3.
     * Update heights, then return new root.
     */
    private static AvlNode doubleWithRightChild(AvlNode k1) {
        k1.right = rotateWithLeftChild(k1.right);
        return rotateWithRightChild(k1);
    }
    /** The tree root. */
    public AvlNode root;
}
    

