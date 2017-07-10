//this Index .java contains index structure that supports query expansion.The tamil word of toconcept is stored in both binary and binary next nodes
/**
 *UNL Indexer
 Index.java
 *version history

 version number:1.1 (indicates developments done after  5th PRSG Meet)
 @author subalalitha
 ----------------------------
 *june 5 09:inclusion of tamil toconcept
 *july 6 09:inclusion pos tags of from and toconcept
 *july 7 09: flags usage to build c and crc,cr tress seperately

 -----------------------------------------------------
 *previous version info: previous version did not have tamil toconcept,pos tag of from concept and pos tag of toconcept.
 *current version info: currently indexer has all the above mentioned datum and flags have been used to run the C avl and bst tree & CR,CRC avl and bst trees seperately.This has been done to avoid heap space exception.
 */
package org.apache.nutch.analysis.unl.ta;

//package org.apache.nutch.index.unl;

import java.io.*;
import java.util.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.logging.FileHandler; //import org.apache.nutch.analysis.unl.ta.*;
//import org.apache.nutch.index.unl.*;
//import org.apache.nutch.template.unl.*;
//import org.apache.nutch.enconversion.unl.ta.*;
import org.apache.nutch.unl.UNL;
import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.analysis.unl.ta.SentExtr;
import org.apache.nutch.analysis.unl.ta.FinalAppln;

/**
 * The class Index gets the documents stored in the form of graphs using
 * Multilist data structure as input. The indexing is based on how many
 * conceptual relation ship a concept possess with other concepts and frequent
 * occurrence of a concept. The methodolgy identifies three types of indices
 * namely,concept-relation-concept(source concept-connecting
 * relation-destination concept),concept-relation and concept. The identified
 * three types of indices are stored in their respective binary search trees
 * namely crc,cr and c. Modified indexing
 **/
public class Index implements Serializable, UNL {

	// ll_new array object is the input MultiList for Indexing
	public static BinarySearchTree crc = new BinarySearchTree();
	/* The object cr consist of the indices c-r */
	public static BinarySearchTree cr = new BinarySearchTree();
	/* The object c consist of the indices c */
	public static BinarySearchTree c = new BinarySearchTree();
	public static ArrayList<String> asciilist_c = new ArrayList<String>();
	public static ArrayList<String> asciilist_cr = new ArrayList<String>();
	public static ArrayList<String> asciilist_crc = new ArrayList<String>();
	ArrayList asciilist_avl = new ArrayList();
	public String index_tamil;
	public int weight_crc = 0;
	public int weight_cr = 0;
	public int weight_c = 0;
	public static int filecount;
	int sen_weight = 0;
	public static int flag = 0;
	AvlTree avl_crc = new AvlTree();
	AvlTree avl_cr = new AvlTree();
	AvlTree avl_c = new AvlTree();
	int count_flg = 0;
	int count_flgnot = 0;
	public static String did = "";
	ArrayList ar = new ArrayList();
	ArrayList ar1 = new ArrayList();
	public static ArrayList list = new ArrayList();
	public static AvlTree avlcrc = new AvlTree();
	public static AvlTree avlcr = new AvlTree();
	public static AvlTree avlc = new AvlTree();
	public static FinalLLImpl[] ll_new = new FinalLLImpl[35000];
	public static Snippet snippet = new Snippet();
	public static OfflineSummary summary = new OfflineSummary();
	// public static FinalAppln final_appln = new FinalAppln();
	public static Logger log;
	public static Hashtable alnew = new Hashtable();
	public static int counter_crc = 0;
	public static int counter_c = 0;
	public static int stfrm;
	public static Index in;
	PrintStream ps;

	public static Configuration conf = NutchConfiguration.create();
	public static String input_path = conf.get("SentenceExtraction");
	public static String path = conf.get("unl_resource_dir");
	public static String graph_path = conf.get("unl-Graph");

	// public static FileWriter fw;
	// empty constructor

	public Index() {

	}

	public Index(String str) {
		// empty function
	}

	// this function is used to read the C ,CR and CRC binary search trees from
	// binary file,parameter is the binary file name
	public BinarySearchTree getBinarySearchTree(String fileName) {
		BinarySearchTree binaryTree = new BinarySearchTree();
		try {

			FileInputStream fis2 = new FileInputStream("./crawl-unl/"
					+ fileName);
			ObjectInputStream ois2 = new ObjectInputStream(fis2);
			String str = "";
			binaryTree.root = binaryTree.tree_FileRead(binaryTree.root, ois2,
					str);
			ois2.close();
			fis2.close();
		} catch (Exception e) {
			binaryTree = new BinarySearchTree();
			return binaryTree;
		}

		return binaryTree;
	}

	// this function is used to read the C ,CR and CRC avl trees from binary
	// file,parameter is the binary file name

	public AvlTree getAvlTree(String fileName) {
		AvlTree avlTree = new AvlTree();
		try {

			FileInputStream fis = new FileInputStream("./crawl-unl/" + fileName);
			ObjectInputStream ois = new ObjectInputStream(fis);
			String str = "";
			avlTree.root = avlTree.tree_FileRead(avlTree.root, ois, str);
			ois.close();
			fis.close();
		} catch (Exception e) {
			// ////System.out.println(e);//debugging
			avlTree = new AvlTree();
		}

		return avlTree;
	}

	/**
	 * index process gets the array of unl graphs stored in multi list as input
	 * . flag 0 for inoking C avl and binary search trees. flag 1 for invoking
	 * CR &CRC avl and binary search trees
	 */
	public void indexProcess(FinalLLImpl[] ll_new, int flag_cavl) {

		try {
			indx(ll_new, flag_cavl);// invokes inverted indexing
			indx1(ll_new, flag_cavl);// invokes forward indexing

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * indx1 invokes forward indexing . inputs are array of multi lists and flag
	 * value
	 */
	public void indx1(FinalLLImpl[] ll_new, int flagavl) throws Exception {

		int count = 0;
		for (int i = 0; i < ll_new.length; i++) {
			if (ll_new[i] != null) {
				count++;
			}
		}

		for (int i = 0, j = 0; j < count; i++, j++) {

			// System.out.println("forward indexing started");//debugging

			// System.out.println("document id to be forward indexed is :" + j);

			int flg = 0;
			HeadNode temp1 = new HeadNode();
			temp1 = ll_new[i].head;
			ConceptNode c1 = new ConceptNode();
			ConceptToNode current1 = new ConceptToNode();
			c1 = ll_new[i].concept;
			current1 = ll_new[i].destination;
			c1 = temp1.colnext;
			if (c1 != null) {
				String str = c1.docid;
				str = str.replaceAll("d", "");
				int ki = Integer.parseInt(str.trim());
				avl_crc.insert(new Integer(ki), c1.docid);
				avl_cr.insert(new Integer(ki), c1.docid);
				avl_c.insert(new Integer(ki), c1.docid);
			}
			while (c1 != null) {
				System.out.println("uwconcept in index" + c1.uwconcept);// debugging
				ConceptNode c = new ConceptNode();
				HeadNode h = new HeadNode();
				c = c1;
				h = temp1;
				int flag = 2;
				String Universalword = new String(c1.uwconcept);
				current1 = c1.rownext;
				int begin = 0, end = 0;
				String str = c1.docid;
				str = str.replaceAll("d", "");
				int ki = Integer.parseInt(str.trim());
				ar.add(c1.uwconcept);
				ArrayList ar = new ArrayList();
				forword_indexing(ki, c1, ll_new[i], flagavl);
				c1 = c1.getColNext();
			}

		}
	}

	/**
	 * This method calls inverted indexing
	 * 
	 * @param FinalLLImpl
	 *            [] ll_new array of multi list @ param int flggbst
	 */
	public void indx(FinalLLImpl[] ll_new, int flagbst) throws Exception {
		int count = 0;

		for (int i = 0; i < ll_new.length; i++) {
			if (ll_new[i] != null) {
				count++;
				System.out.println("Inside For IF not null LL");
			} else {
				// System.out.println("Inside For else null LL");
			}
		}
		for (int i = 0, j = 0; j < count; i++, j++) {

			System.out.println("document id to be inverted indexed is :" + j
					+ "flag passed:" + flagbst);

			try {
				HeadNode temp1 = new HeadNode();
				temp1 = ll_new[i].head;
				ConceptNode c1 = new ConceptNode();
				ConceptToNode current1 = new ConceptToNode();
				c1 = temp1.colnext;

				if (c1 != null) {
					String str = c1.docid;
					str = str.replaceAll("d", "");
					int ki = Integer.parseInt(str.trim());

				}
				while ((c1 != null)) {
					System.out.println("inside main while" + c1.uwconcept);
					String Universalword = new String(c1.uwconcept);
					current1 = c1.rownext;
					int begin = 0, end = 0;
					// if(c1.uwconcept.equals("sivan(iof>god)"))//debugging
					if (flagbst == 0) {
						frequent_Indices(c1);// method that builds C BST tree
						// System.out.println("have i entered");//debugging
					} else {
						String str = c1.docid;
						str = str.replaceAll("d", "");
						int ki = Integer.parseInt(str.trim());
						System.out.println("inside else");
						indices_in_tree(c1, ki, ll_new[i]);// method that builds
															// C and CRC &CR BST
															// trees
					}
					c1 = c1.getColNext();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *This method constructs an AVL tree based on forward indexing for a
	 * Document
	 * 
	 * @param i
	 *            int Document ID
	 *@param c1
	 *            ConceptNode contains the concepts name, Tamil Word, POS Tag,
	 *            Frequency, Sentence ID & link to next Concept & Relation Nodes
	 *@param ll_new
	 *            FinalLLImpl an array of multi list from enconverter
	 * 
	 */
	public void forword_indexing(int i, ConceptNode c1, FinalLLImpl ll_new,
			int flagavl) {
		try {

			int weight = 0;

			ConceptToNode tocon = new ConceptToNode();
			tocon = c1.rownext;
			String concept_name = c1.uwconcept;

			int flg = 0;

			int freqcount1 = 0;
			String str11 = c1.freq_count.trim();
			if ((str11.equals("null")) || (str11.equals(""))) {
				freqcount1 = Integer.parseInt("1");
			} else {
				freqcount1 = Integer.parseInt(str11);
			}
			if (flagavl == 0) {
				avl_c.insert_Avl_linknextNode2(new Integer(i), concept_name,
						null, null, c1.sentid, weight, c1.poscheck, freqcount1,
						c1.gn_word, null, null);
			} else {
				if (tocon != null) {
					while (tocon != null) {
						String Toconcept;
						String tamcon;
						String param;
						String pos;
						String index_tamil = c1.gn_word;
						String reln = tocon.relnlabel;
						param = getconcept_vs_ToCon(tocon.uwtoconcept,
								c1.sentid, ll_new, i);
						StringTokenizer st = new StringTokenizer(param);
						Toconcept = st.nextToken();
						tamcon = st.nextToken();
						pos = st.nextToken();
						count_flgnot++;
						int freqcount = 0;
						String str1 = c1.freq_count.trim();
						if ((str1.equals("null")) || (str1.equals(""))) {
							freqcount = Integer.parseInt("1");
						} else {
							freqcount = Integer.parseInt(str1);
						}

						avl_crc.insert_Avl_linknextNode(new Integer(i),
								concept_name, reln, Toconcept, tocon.sentid,
								weight, c1.poscheck, freqcount, c1.gn_word,
								tamcon, pos);
						avl_cr.insert_Avl_linknextNode1(new Integer(i),
								concept_name, reln, null, tocon.sentid, weight,
								c1.poscheck, freqcount, c1.gn_word, null, null);

						flg++;
						tocon = tocon.rownext;
					}// while end tocon
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * The method indices_in_tree stores the three types of indices namely
	 * c-r-c,c-r and c in their respective Binary Search Trees inputs are
	 * concept node address,docid,array of multi lists
	 */
	public void indices_in_tree(ConceptNode c1, int i, FinalLLImpl ll_new)
			throws IOException {
		try {

			index_tamil = c1.gn_word;
			ConceptToNode ct = new ConceptToNode();
			ct = c1.rownext;
			String Toconcept;
			String tamcon;
			String pos;
			String concept_name = c1.uwconcept;
			String index_tamil = c1.gn_word;
			int val = concept_name.hashCode();
			// Object b = (new Integer(val));
			String b = String.valueOf(val);
			System.out
					.println("the concept in indices in tree:" + concept_name);
			String param;
			if ((asciilist_cr.contains(b))) {

				while (ct != null) {
					System.out.println("Inside while");
					param = getconcept_vs_ToCon(ct.uwtoconcept, c1.sentid,
							ll_new, i);// method that returns all the toconcept
										// info which is not present in concept
										// node
					StringTokenizer st = new StringTokenizer(param);
					Toconcept = st.nextToken();
					tamcon = st.nextToken();
					pos = st.nextToken();
					String reln = ct.relnlabel;
					int weight_crc = avl_crc.find_count(new Integer(i),
							concept_name, reln, Toconcept);
					int weight_cr = avl_cr.find_count1(new Integer(i),
							concept_name, reln);
					int freqcount = 0;
					String str1 = c1.freq_count.trim();
					if ((str1.equals("null")) || (str1.equals(""))) {
						freqcount = Integer.parseInt("1");
					} else {
						freqcount = Integer.parseInt(str1);
					}
					if (asciilist_crc.contains(b)) {
						counter_crc++;
						crc
								.insert_BinaryToNode(new Integer(val),
										concept_name, reln, Toconcept,
										index_tamil, i, ct.sentid, weight_crc,
										c1.poscheck, freqcount, tamcon, pos);
					}
					if (asciilist_cr.contains(b)) {

						cr.insert_BinaryToNode1(new Integer(val), concept_name,
								reln, null, index_tamil, i, ct.sentid,
								weight_cr, c1.poscheck, freqcount, null, null);
					}

					ct = ct.getRowNext();
				}
			} else {
				int fl = 0;
				while (ct != null) {

					param = getconcept_vs_ToCon(ct.uwtoconcept, c1.sentid,
							ll_new, i);
					StringTokenizer st = new StringTokenizer(param);
					Toconcept = st.nextToken();
					tamcon = st.nextToken();
					pos = st.nextToken();
					String reln = ct.relnlabel;
					int weight_crc = avl_crc.find_count(new Integer(i),
							concept_name, reln, Toconcept);
					int weight_cr = avl_cr.find_count1(new Integer(i),
							concept_name, reln);
					if (fl >= 1) {
						int freqcount = 0;
						String str1 = c1.freq_count.trim();
						if ((str1.equals("null")) || (str1.equals(""))) {
							freqcount = Integer.parseInt("1");
						} else {
							freqcount = Integer.parseInt(str1);
						}
						counter_crc++;
						crc
								.insert_BinaryToNode(new Integer(val),
										concept_name, reln, Toconcept,
										index_tamil, i, ct.sentid, weight_crc,
										c1.poscheck, freqcount, tamcon, pos);

						cr.insert_BinaryToNode1(new Integer(val), concept_name,
								reln, null, index_tamil, i, ct.sentid,
								weight_cr, c1.poscheck, freqcount, null, null);

					} else {
						int freqcount = 0;
						String str1 = c1.freq_count.trim();
						if ((str1.equals("null")) || (str1.equals(""))) {
							freqcount = Integer.parseInt("1");
						} else {
							freqcount = Integer.parseInt(str1);
						}
						counter_crc++;

						crc
								.insert(new Integer(val), concept_name, reln,
										Toconcept, index_tamil, i, c1.sentid,
										weight_crc, c1.poscheck, freqcount,
										tamcon, pos);
						cr.insert(new Integer(val), concept_name, reln, null,
								index_tamil, i, c1.sentid, weight_cr,
								c1.poscheck, freqcount, null, null);
						asciilist_cr.add(b);
						asciilist_crc.add(b);

					}
					fl++;
					ct = ct.getRowNext();
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * constructs C bst tree
	 * 
	 * input is concept node address
	 */
	public void frequent_Indices(ConceptNode c1) throws IOException {

		int count_cr = 0;
		int count_crc = 0;
		int count_c1 = 0;

		try {

			String var = c1.uwconcept;
			String index_tamil = c1.gn_word;

			int flag = 2;
			String Universalword = new String(c1.uwconcept);

			int begin = 0, end = 0;
			StringTokenizer strToken = new StringTokenizer(Universalword, " ");

			String freq = c1.uwconcept;
			String str = c1.docid;

			str = str.replaceAll("d", "");
			int ki = Integer.parseInt(str.trim());
			int val2 = c1.uwconcept.hashCode();
			// Object b = (new Integer(val2));
			String b = String.valueOf(val2);
			int freqcount = 0;
			String str1 = c1.freq_count.trim();
			if ((str1.equals("null")) || (str1.equals(""))) {
				freqcount = Integer.parseInt("1");
			} else {
				freqcount = Integer.parseInt(str1);
			}

			int weight_c = avl_c.find_count2(new Integer(ki), c1.uwconcept);

			if (asciilist_c.contains(b)) {
				// fw=new FileWriter("/root/index1.txt",true);
				// fw.write("Next Node->"+c1.uwconcept+"\t"+c1.gn_word+"\t"+val2+"\n");
				// fw.close();
				counter_c++;
				c.insert_BinaryToNode2(new Integer(val2), c1.uwconcept, null,
						null, c1.gn_word, ki, c1.sentid, weight_c, c1.poscheck,
						freqcount, null, null);
			} else {

				// fw=new FileWriter("/root/index1.txt",true);
				// fw.write("Root->"+c1.uwconcept+"\t"+c1.gn_word+"\t"+val2+"\n");
				// fw.close();
				counter_c++;
				c.insert(new Integer(val2), c1.uwconcept, null, null,
						c1.gn_word, ki, c1.sentid, weight_c, c1.poscheck,
						freqcount, null, null);
				asciilist_c.add(b);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// returns crc avl tree

	public AvlTree get_crcIndices_avl() {
		return avl_crc;

	}

	// returns cr avl tree

	public AvlTree get_crIndices_avl() {
		return avl_cr;

	}

	// returns c avl tree

	public AvlTree get_cIndices_avl() {
		return avl_c;

	}

	public int get_counter() {

		return counter_crc;
	}

	/* *
	 * The method get_crc returns the Binary Search tree object crc
	 */

	public static BinarySearchTree get_crcIndices() {
		System.out.println("in get crc indices");
		crc.display_count1();
		return crc;
	}

	/**
	 * The method get_cr returns the Binary Search tree object cr
	 */
	public static BinarySearchTree get_crIndices() {
		return cr;
	}

	/**
	 * the method returns the toconcept UW word,tamil word of toconcept and pos
	 * tag of toconcept as string. inputs are concept id,setence id ,docid,unl
	 * graph ll_new
	 */
	public String getconcept_vs_ToCon(String cs, String se, FinalLLImpl ll_new,
			int i) {
		ConceptNode temp1 = new ConceptNode();
		ConceptNode temp2 = new ConceptNode();

		temp1 = ll_new.head.colnext;
		String needed;
		while (temp1 != null) {
			temp2 = temp1;
			// System.out.println("entered into while wwwwwwwwwwwwwwwwww");//debugging
			if ((cs.equals(temp1.conceptid)) && (se.equals(temp1.sentid))) {
				// System.out.println("entered into if ffffffffffffffff");//debugging
				// System.out.println("Both Concept id and sentance id are same"+cs+"\t"+temp1.gn_word+"\t"+se+"\t"+temp1.sentid);
				break;
			}
			temp1 = temp1.getColNext();
		}
		needed = temp2.uwconcept + " " + temp2.gn_word + " " + temp2.poscheck;
		return needed;

	}

	/**
	 * The method get_c returns the Binary Search tree object c
	 */
	public static BinarySearchTree get_cIndices() {
		return c;
	}

	// public static void main(String args[]) throws Exception {

	public static void main(String args[]) {

		String str = args[0];
		Hashtable ht = new Hashtable();
		int docid_se = 0;

		// int total_pos=0;

		String s = "";
		Hashtable ht1 = new Hashtable();
		File f_n = new File(
				"./resource/unl/ta/SentenceExtraction/Output/content/");
		File[] file_id = f_n.listFiles();

		/*
		 * try{ fw=new FileWriter("/root/indexTest1.txt",true); }catch(Exception
		 * e){}
		 */
		// FinalAppln final_appln = new FinalAppln();
	/**	try {
			BufferedReader bufred = new BufferedReader(
					new FileReader(
							"./resource/unl/ta/SentenceExtraction/Input/nonemptyfiles.txt"));
			while ((s = bufred.readLine()) != null) {			
				docid_se++;

				String did_se = Integer.toString(docid_se);

				SentExtr se = new SentExtr();

				ht1 = se.impSentExtr(s, did_se);
				// }
				// }

			}
		} catch (Exception e) {
		}	*/
		int flagsummary = 1;
		if (str.equalsIgnoreCase("false")) {
			Index indx = new Index("");

		} else {
			try {
				//                
				FinalAppln docp = new FinalAppln();
				docp.run_enconversion();

			/*	BufferedWriter buffer = new BufferedWriter(new FileWriter(
						"./resource/unl/Display_Count.txt", true));

				File f = new File("./resource/unl/Enconversion/unl-graph/");
				File filename[] = f.listFiles();
				System.out.println("size is " + filename.length);

				int File_cntr = 0;
				for (Object x : filename) {

					if (x.toString().contains("unlgraph")) {
						++File_cntr;
					}
				}
				int unlgraph_size = File_cntr * 1;

				for (int flag = 0; flag < 3; flag++) {
					for (int i = 0; i < filename.length; i++) {

						FinalLLImpl[] ll = new FinalLLImpl[unlgraph_size];

						FinalLLImpl[] ll_new1 = new FinalLLImpl[unlgraph_size];

						int count1 = 0;
						String name = "unlgraph" + i + ".txt";
						try {

							FileInputStream fis = new FileInputStream(
									"./resource/unl/Enconversion/unl-graph/"
											+ name);
							// System.out.println("file name is " + name);
							ObjectInputStream ois = new ObjectInputStream(fis);
							System.out.println("reading started....");// testing
							ll_new1 = (FinalLLImpl[]) ois.readObject();
							System.out.println("reading end...." + ll_new1[i] + "" + ll_new1.length);// testing
							
					//		summary.Traverselnode(ll_new1);
					//		summary.callFunctions();
							for (int j = 0; j < ll_new1.length; j++) {								
								if (ll_new1[j] != null) {
									ll[count1] = ll_new1[j];
									count1++;
									
								}
							}
						//	summary.callFunctions();
							ois.close();
						} catch (Exception e) {
							System.out.println(e.getMessage());
						}

						in = new Index();
						long starttime = System.currentTimeMillis();
						// System.out.println("flag value" + flag);//debugging
						// Thread.sleep(1000);//debugging
						if (flag < 2) {
							System.out.println("Inside Flag:" + ll[0]);
							in.indexProcess(ll, flag);
							// in.indx(ll,flag);//method that calls indexing
							Index.writeCrc();
							ll = null;
							System.gc();
						}
						if (flag == 2) {
							// d.filltemplateone(ll);//method that calls summary
							// ll = null;

							System.gc();
						}

						long endtime = System.currentTimeMillis();
						double duration = (double) (endtime - starttime) / 1000.00;

						System.out.println("time taken for indexing "
								+ duration);

						// System.gc();
					}// inner for loop ends
					if (flag == 0) {
						Index.writeC();// method being called to write the C BST
										// tree into a binary file

						try {
							// FileWriter fw = new
							// FileWriter("/root/indices_Cnt.txt",true);
							// fw.write("C Indices Count"+counter_c + "\n");
							System.out.println("C Indices Count" + counter_c
									+ "\n");
						} catch (Exception e) {
						}

					}
					System.gc();
					if (flag == 1) {
						// Index.writeCrc();//method being called to write the
						// Cr and CRC BST trees into a binary file

						try {
							// FileWriter fw = new
							// FileWriter("/root/indices_Cnt.txt",true);
							// fw.write("CRC Indices Count"+counter_crc + "\n");
							System.out.println("CRC Indices Count"
									+ counter_crc + "\n");
						} catch (Exception e) {
						}

						toconcepttree q = new toconcepttree();
						q.expandQuery();
						GenerateICLTree icl = new GenerateICLTree();
						icl.processSuper();
						GenerateIOFTree iof = new GenerateIOFTree();
						iof.processSuper();

					}
					System.gc();
					if (flag == 2) {
						// Index.writeSummary();//method being called to write
						// summary into a binary file

					}

					System.gc();
				}// outer for loop
				summary.callFunctions();
				snippet.callFunctions(); */

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	// main function invokes indexing and summary generation.finally writes in a
	// binary file

	/**
	 * method that writes the C BST Tree and AVL tree into binary file(binary
	 * file helps writing any data structure into a file maintaining the
	 * structure) all the binary files are written and read from the ./crawl unl
	 * folder
	 */
	public static void writeC() {
		int count = 0;
		try {

			FileOutputStream fosavlc = new FileOutputStream(
					"./crawl-unl/Avl_c.txt");
			ObjectOutputStream oosavlc = new ObjectOutputStream(fosavlc);
			avlc = in.get_cIndices_avl();
			AvlTree avl = new AvlTree();
			avl.tree_FileWrite(avlc.root, oosavlc);
			oosavlc.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FileOutputStream fos3 = new FileOutputStream("./crawl-unl/c.txt");
			ObjectOutputStream oos3 = new ObjectOutputStream(fos3);
			c = in.get_cIndices();
			BinarySearchTree bst = new BinarySearchTree();
			bst.tree_FileWrite(c.root, oos3);
			oos3.close();
			fos3.close();
			c = null;
			avlc = null;
			System.gc();

		} catch (Exception h) {
			// //System.out.println(e);
		}
	}

	// method that writes the Cr& CRC BST and AVL trees into binary file

	public static void writeCrc() {
		int count = 0;
		try {

			FileOutputStream fosavlcr = new FileOutputStream(
					"./crawl-unl/Avl_cr.txt");
			ObjectOutputStream oosavlcr = new ObjectOutputStream(fosavlcr);
			avlcr = in.get_crIndices_avl();
			AvlTree avl = new AvlTree();
			avl.tree_FileWrite(avlcr.root, oosavlcr);
			oosavlcr.close();

		} catch (Exception e) {
			// //System.out.println(e);
		}
		try {

			FileOutputStream fosavlcrc = new FileOutputStream(
					"./crawl-unl/Avl_crc.txt");
			ObjectOutputStream oosavlcrc = new ObjectOutputStream(fosavlcrc);
			avlcrc = in.get_crcIndices_avl();
			AvlTree avl = new AvlTree();
			avl.tree_FileWrite(avlcrc.root, oosavlcrc);
			oosavlcrc.close();

		} catch (Exception e) {
			// //System.out.println(e);
		}
		try {
			FileOutputStream fos3 = new FileOutputStream("./crawl-unl/cr.txt");
			ObjectOutputStream oos3 = new ObjectOutputStream(fos3);

			cr = in.get_crIndices();

			BinarySearchTree bst = new BinarySearchTree();
			bst.tree_FileWrite(cr.root, oos3);
			oos3.close();
			fos3.close();
			cr = null;
			avlcr = null;
			System.gc();
		} catch (Exception e) {
			// //System.out.println(e);
		}

		try {
			FileOutputStream fos3 = new FileOutputStream("./crawl-unl/crc.txt");
			ObjectOutputStream oos3 = new ObjectOutputStream(fos3);
			crc = in.get_crcIndices();
			System.out.println("CRC IN WRITE CRC:" + crc);
			crc.display_count1();
			System.out.println("before writing");

			BinarySearchTree bst = new BinarySearchTree();
			bst.tree_FileWrite(crc.root, oos3);
			bst.display_count1();

			oos3.close();
			fos3.close();
			crc = null;
			avlcrc = null;
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ArrayList process(String queryString) {
		ArrayList arr = null;
		return arr;
	}

	public String debugQueryExpansionOutput(boolean debugQE,
			boolean singleWdwithanalyser, boolean singleWdwithoutanalyser,
			boolean multiWd) {
		return null;
	}

	public String debugQueryTranslationOutput(boolean translation,
			boolean ActualQW, boolean expansion, boolean icliofCombination) {
		return null;
	}

	public String debugSearchOutput(boolean Search, boolean Rank) {
		return null;
	}

	public Hashtable<String, Hashtable<String, String>> getOutput() {
		return null;
	}

	public Hashtable<String, Hashtable<String, Double>> getTime() {
		return null;
	}

	public Hashtable<String, Hashtable<String, String>> getCount() {
		return null;
	}

	public Hashtable<String, Hashtable<String, String>> getRank() {
		return null;
	}

	public Hashtable<String, String> getVersion() {
		return null;
	}

	public void finishedDebugTool() {
	}

}// class
