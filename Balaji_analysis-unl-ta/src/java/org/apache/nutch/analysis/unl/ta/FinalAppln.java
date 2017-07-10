package org.apache.nutch.analysis.unl.ta;

//package org.apache.nutch.enconversion.unl.ta;

import org.apache.nutch.unl.UNL;
import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;

import java.lang.*;
import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.IOException;
import java.util.logging.FileHandler;

public class FinalAppln implements Serializable, UNL {
	public static int docid = 0;
	public static int dociditer = 0;
	public static int initialdocid = 0;
	public static int finaldociditer = 0;
	int sentid = 0;
	int no_doc = 0;
	public static String did = "";
	public static String docid1 = "";

	int ctrs = 0;
	String encon_out = "";
	public static int filecount;
	public Hashtable get_count = new Hashtable();
	public Hashtable get_concount = new Hashtable();

	org.apache.nutch.analysis.unl.ta.Rules r = new Rules();
	// org.apache.nutch.enconversion.unl.ta.SemiRules r=new SemiRules();
	// org.apache.nutch.enconversion.unl.ta.healthRules r=new healthRules();
	public static FinalLLImpl[] ll_new = new FinalLLImpl[50000];
	// public static OfflineSummary summary = new OfflineSummary();

	public static Hashtable fileList = new Hashtable();

	public static Hashtable new_freq = new Hashtable();
	public static Hashtable al = new Hashtable();
	public static Hashtable con_count = new Hashtable();

	public static ArrayList<String> rlist = new ArrayList<String>();
	public static ArrayList<String> flist = new ArrayList<String>();

	public static Hashtable getcount = new Hashtable();
	public static FileHandler hand; // = new FileHandler("Exception.log");
	public static Logger log;
	public static PrintWriter logwriter;
	public static ArrayList list = new ArrayList();
	// public static String[] filename={"unlgraph1.txt","unlgraph2.txt"};
	public static boolean flag = true;
	public static int stfrm;
	// int l = 0;
	public static Configuration conf = NutchConfiguration.create();
	public static String input_path = conf.get("SentenceExtraction");
	public static String path = conf.get("unl_resource_dir");
	public static String graph_path = conf.get("unl-Graph");

	public FinalAppln() {
		// ll_new = new FinalLLImpl[list.size()];
	}

	public void start(FinalLLImpl[] ll_new) throws IOException,
			ClassNotFoundException {
		encon_out = "";
		String s = "";
		String s1 = "";
		String docid1 = "";
		ArrayList<String> alnew = new ArrayList<String>();

		BufferedReader in = new BufferedReader(new FileReader(input_path
				+ "Input/" + "enconinput.txt"));

		r.loadequ();

		readFilelist();
		readDocid();

		String str = "";
		String sent = "", recv = "", token = "", fn = "", temp = "";
		int temp1 = 0, j = 0;
		FileReader fr = null;
		docid = no_doc;
		initialdocid = no_doc;
		int incrementalIter = 0;

		while ((s = in.readLine()) != null) {
			// {
			fn = input_path + "Output/" + s;

			File empFile = new File(fn);

			fileList.put(no_doc + 1, fn);
			no_doc++;
			incrementalIter++;
			System.out.println("fn:" + s);
			// if(empFile.length()>25){

			StringBuffer docbuff = new StringBuffer();
			encon_out += "[d]#";

			String splitid[] = s.split("/");
			docid1 = splitid[1].replace(".txt", "").toString();
			r.getDocid(docid1);
			// //System.out.println("split docid1:"+docid1);
			alnew.add(docid1);
			// read sentences from file

			fr = new FileReader(fn);
			StreamTokenizer st = new StreamTokenizer(fr);

			while (st.nextToken() != st.TT_EOF) {
				temp = "";
				temp1 = 0;
				if (st.ttype == st.TT_WORD)
					temp = st.sval;
				else if (st.ttype == st.TT_NUMBER) {
					temp1 = (int) st.nval;
					temp = Integer.toString(temp1);
				}
				if (temp != null)
					docbuff.append(temp + " ");
			}
			StringTokenizer sentToken1 = new StringTokenizer(docbuff.toString()
					.trim(), ".", false);
			j = 0;
			while (sentToken1.hasMoreTokens()) {

				sent = sentToken1.nextToken();
				recv = r.enconvert(sent);
				encon_out += recv;
			}
			encon_out += "[/d]#";
			r.clearList();

			fr.close();

			// //System.out.println("new_Freq:"+al);
			// //System.out.println("encon_out:" +encon_out);
			// //System.out.println("Docid:" + no_doc);
			/**
			 * }else{ //System.out.println("EmptyDocs:"+no_doc); // l++; }
			 */
		}
		in.close();
		// //System.out.println("alnew:"+alnew);
		freq_count(alnew);
		// al.put(docid1, new_freq);

		graphconstruct(ll_new, al, con_count);
		alnew.clear();
		al.clear();
		writeFilelist();

	}

	public void freq_count(ArrayList<String> alnew) {
		String word = "";
		String g_word = "";
		String conentry = "";
		String relnentry = "";
		String docid1 = "";
		Hashtable freq_table = null;
		Hashtable concept_table = null;
		int l = 0;
		try {
			StringTokenizer strToken = new StringTokenizer(encon_out, "#");
			while (strToken.hasMoreTokens()) {
				try {
					word = strToken.nextToken().trim();
					if (word.equals("[d]")) {
						freq_table = new Hashtable();
						concept_table = new Hashtable();
						docid1 = alnew.get(l).toString();
						// //System.out.println("Inside Tag [d]:"+docid1+":"+l);
						l++;
					} else if (word.equals("[s]")) {

					} else if (word.equals("[/d]")) {
						al.put(docid1, freq_table);
						con_count.put(docid1, concept_table);
						// //System.out.println("new_Freq:"+al);
						// freq_table.clear();
					} else if (word.equals("[w]")) {

						conentry = strToken.nextToken().trim();

						while (!(conentry.equals("[/w]"))) {
							System.out.println("conentry:" + conentry);
							StringTokenizer strToken1 = new StringTokenizer(
									conentry, ";");
							if (strToken1.hasMoreElements()) {
								g_word = strToken1.nextToken().trim();
								System.out.println("g_word:"+g_word);
								int count = 1;
								String frequency = "";
								if (freq_table.isEmpty()) {
									frequency = Integer.toString(count)
											.toString();
									freq_table.put(g_word, frequency);
								} else if (freq_table.containsKey(g_word)) {
									String freq_count = freq_table.get(g_word)
											.toString();
									int f_cnt = Integer.parseInt(freq_count);
									f_cnt++;
									frequency = Integer.toString(f_cnt)
											.toString();
									freq_table.remove(g_word);
									freq_table.put(g_word, frequency);
								} else {
									frequency = Integer.toString(count)
											.toString();
									freq_table.put(g_word, frequency);
								}
							}
							if (strToken1.hasMoreElements()) {
								String uwconcept = strToken1.nextToken().trim()
										+ '(' + strToken1.nextToken().trim()
										+ ')';
							//	 System.out.println("uwconcept:"+uwconcept);
								int count = 1;
								String con_frequency = "";
								if (concept_table.isEmpty()) {
									con_frequency = Integer.toString(count)
											.toString();
									concept_table.put(uwconcept, con_frequency);
								} else if (concept_table.containsKey(uwconcept)) {
									String freq_count = concept_table.get(
											uwconcept).toString();
									int con_cnt = Integer.parseInt(freq_count);
									con_cnt++;
									con_frequency = Integer.toString(con_cnt)
											.toString();
									concept_table.remove(uwconcept);
									concept_table.put(uwconcept, con_frequency);
								} else {
									con_frequency = Integer.toString(count)
											.toString();
									concept_table.put(uwconcept, con_frequency);
								}
							}
							if (strToken1.hasMoreElements()) {

							}
							if (strToken1.hasMoreElements()) {

							}
							if (strToken1.hasMoreElements()) {

							}
							if (strToken1.hasMoreElements()) {

							}
							conentry = strToken.nextToken().trim();
						}
					}
					/**
					 * else if (word.equals("[r]")) { while
					 * (!(relnentry.equals("[/r]"))) { } }else{ }
					 */
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//	
		} catch (Exception e) {
			e.getStackTrace();
		}
	//	System.out.println("new_Freq:"+freq_table);
	//	System.out.println("Concept_Freq:"+concept_table);
	}

	public void graphconstruct(FinalLLImpl[] ll_new, Hashtable al,
			Hashtable con_count) {
		String g_word = "", concept = "", conceptto = "", sid = "", did = "", relnid = "", frmscpid = "", toscpid = "", conceptfrm = "", ConceptToNode = "";
		String verbword = "";
		String conentry = "";
		String relnentry = "";
		String attribute = "";
		String frmscopeid = "";
		String toscopeid = "";
		String subgraphid = "", frmflag = "", toflag = "", sgid = "", frmflg = "", toflg = "", dummy_cid = "";
		String word = "";
		int totcon_sent = 0;
		int totrel_sent = 0;

		String fd = "";
		String fd1 = "";
		String fldoc = "";

		try {
			// //System.out.println("Encon_Out:"+encon_out);
			StringTokenizer strToken = new StringTokenizer(encon_out, "#");
			String conceptid = " ";
			String scopeid = "";
			int iter = 1;
			while (strToken.hasMoreTokens()) {
				try {
					word = strToken.nextToken().trim();
					if (word.equals("[d]")) {

						ll_new[docid] = new FinalLLImpl();
						fldoc = list.get(docid).toString();
						StringTokenizer strTok = new StringTokenizer(fldoc, "/");
						fd1 = strTok.nextToken();
						fd = strTok.nextToken();
						fd = fd.replace(".txt", "");
						// System.out.println("fd:" + fd);
						docid++;
						get_count = (Hashtable) al.get(fd);
						get_concount = (Hashtable) con_count.get(fd);
						// //System.out.println("enconout Inside Graph Construct:"+fd+"\n"+encon_out);

					} else if (word.equals("[s]")) {
						totcon_sent = 0;
						totrel_sent = 0;
						sentid++;

					} else if (word.equals("[/d]")) {
						sentid = 0;
						iter++;
					} else if (word.equals("[w]")) {

						conentry = strToken.nextToken().trim();
						while (!(conentry.equals("[/w]"))) {
							StringTokenizer strToken1 = new StringTokenizer(
									conentry, ";");
							if (strToken1.hasMoreElements()) {
								g_word = strToken1.nextToken().trim();
							}
							if (strToken1.hasMoreElements()) {
								concept = strToken1.nextToken().trim() + '('
										+ strToken1.nextToken().trim() + ')';
							}
							if (strToken1.hasMoreElements()) {
								verbword = strToken1.nextToken().trim();
							}
							totcon_sent++;
							if (strToken1.hasMoreElements()) {
								attribute = strToken1.nextToken().trim();
							}
							if (strToken1.hasMoreElements()) {
								conceptid = strToken1.nextToken().trim();
							}
							if (strToken1.hasMoreElements()) {
								scopeid = strToken1.nextToken().trim();
							}
							sid = ("s" + sentid);

							// did=("d"+docid);
							Object str = null;
							Object con_freq = null;
							did = fd.trim();
							if (g_word != null) {
								str = get_count.get(g_word);
							}
							if (concept != null) {
								con_freq = get_concount.get(concept);
							}
							// //System.out.println("G_WORD:"+g_word+":"+sid+":"+conceptid+":"+did);
							// ll[dociditer-1].addConcept(g_word,concept,conceptid,did,sid,verbword);
							if (str != null) {
								ll_new[docid - 1].addConcept(g_word, concept,
										conceptid, did, sid, verbword, str
												.toString(), con_freq
												.toString(), "", "");
							} else {
								ll_new[docid - 1].addConcept(g_word, concept,
										conceptid, did, sid, verbword, "", "",
										"", "");
							}

							conentry = strToken.nextToken().trim();

						}
					//	if(!conceptid.isEmpty()){
						int cid = Integer.parseInt(conceptid);
						cid++;
						dummy_cid = Integer.toString(cid);
					//	System.out.println("Dummy_conceptid:"+dummy_cid);
						ll_new[docid - 1].addConcept("Dummy", "",
								dummy_cid, did, sid, "", "", "", "", "");
					//	}
					}

					else if (word.equals("[r]")) {
						relnentry = strToken.nextToken().trim();
						// //System.out.println("RELN:"+relnentry);
						while (!(relnentry.equals("[/r]"))) {

							StringTokenizer strToken2 = new StringTokenizer(
									relnentry);
							if(strToken2.hasMoreElements()){
								subgraphid = strToken2.nextToken().trim();
								if(!subgraphid.equals("sgflg")){
									
								}else{
									sgid = "None";
								}
							}
							if(strToken2.hasMoreElements()){
								frmflag = strToken2.nextToken().trim();
								if(!frmflag.equals("frmscp")){
									frmflg = frmflag;
								}else{
									frmflg = "None";
								}
							}
							if (strToken2.hasMoreElements()) {

								frmscopeid = strToken2.nextToken().trim();
								// //System.out.println("frmSCP:"+frmscopeid);
								if (!(frmscopeid.equals("scp1"))) {
									frmscpid = frmscopeid;
								} else {
									frmscpid = "None";
								}
							}
							if (strToken2.hasMoreElements()) {

								conceptfrm = strToken2.nextToken().trim();
								// //System.out.println("frmconceptid:"+conceptfrm);
							}
							if (strToken2.hasMoreElements()) {

								relnid = strToken2.nextToken().trim();
								// //System.out.println("relnid:"+relnid);
							}
							if (strToken2.hasMoreElements()) {
								conceptto = strToken2.nextToken().trim();
								// //System.out.println("toconceptid:"+conceptto);
							}
							if (strToken2.hasMoreElements()) {

								toscopeid = strToken2.nextToken().trim();
								// //System.out.println("toscpid:"+toscopeid);
								if (!(toscopeid.equals("scp2"))) {
									toscpid = toscopeid;
								} else {
									toscpid = "None";
								}
							}
							if(strToken2.hasMoreElements()){
								toflag = strToken2.nextToken().trim();
								if(!toflag.equals("toscp")){
									toflg = toflag;
								}else{
									toflg = "None";
								}
							}

							sid = ("s" + sentid);
							// did=("d"+(FinalAppln.initialdocid+iter));
							did = fd.trim();

							totrel_sent++;

							if ((!(conceptfrm.equals("None")))
									&& (!(conceptto.equals("None")))) {

								ll_new[docid - 1].addRelation(relnid);
								ConceptToNode cn = ll_new[docid - 1]
										.addCT_Concept(sgid, frmflg, frmscpid, conceptfrm, relnid, conceptto, toscpid, toflg, did, sid);
								ll_new[docid - 1].addCT_Relation(cn);
							}
							if(conceptfrm.equals("None")){
								conceptfrm = dummy_cid;
								ll_new[docid - 1].addRelation(relnid);
								ConceptToNode cn = ll_new[docid - 1]
										.addCT_Concept(sgid, frmflg, frmscpid, conceptfrm, relnid, conceptto, toscpid, toflg, did, sid);
								ll_new[docid - 1].addCT_Relation(cn);
							}
							if (conceptto.equals("None")) {

							}
							relnentry = strToken.nextToken().trim();
						}
					}
					else {

					}
				} catch (Exception e) {
					e.printStackTrace();
					// System.out.println("Document is Empty");
					no_doc++;
				}
			}

		} catch (Exception e) {
			e.getStackTrace();
		}
	}

	public void readFilelist() {
		try {
			File f = new File(graph_path + "filelist.txt");
			/** to check whether the file exist or not **/

			boolean fileexist = f.exists();
			if (fileexist) {
				FileInputStream fis = new FileInputStream(graph_path
						+ "filelist.txt");
				ObjectInputStream ois = new ObjectInputStream(fis);
				fileList = (Hashtable) ois.readObject();
				ois.close();
				fis.close();
			} else {
				f.createNewFile();
			}
		} catch (Exception e) {
			fileList = new Hashtable();

		}
	}

	public void writeFilelist() {
		try {
			FileOutputStream fos = new FileOutputStream(graph_path
					+ "filelist.txt");
			ObjectOutput oos = new ObjectOutputStream(fos);
			oos.writeObject(fileList);
			oos.flush();
			oos.close();
		} catch (Exception e) {

		}
	}

	public void readDocid() {
		try {
			BufferedReader in2 = new BufferedReader(new FileReader(path
					+ "Docid.txt"));
			String str1 = in2.readLine();
			if (str1.trim() != null)
				no_doc = Integer.parseInt(str1) + fileList.size();
			else
				no_doc = 0;
			in2.close();
			// graphconstruct(ll_new,al);
		} catch (Exception e) {
			no_doc = 0;
		}
	}

	public static void run_enconversion() {
//	public static void main(String[] args){
		// String str = args[0];
		int sen = 0;
		// FinalAppln ap=new FinalAppln();
		int docid = stfrm;
		String did = "";
		String fn1 = null;
		// Index in=new Index();
		// ArrayList<String> docnew = new ArrayList<String>();
		BufferedWriter buf = null;
		BufferedWriter out = null;
		BufferedWriter buffer = null;
		BufferedWriter inbuf = null;
		BufferedWriter emptybuf = null;

		BufferedReader inbr = null;
		BufferedReader in_enc = null;
		BufferedReader reademptybuf = null;
		// int filecount=4;
		int total_pos = 0;
		int counter = 0;
		String sendoc = null;
		String strsen1 = null;
		String strSent = null;
		String getfName = null;

		try {

			BufferedReader bufred = new BufferedReader(new FileReader(
					input_path + "Input/" + "nonemptyfiles.txt"));
			inbr = new BufferedReader(new FileReader(path + "lastfile.txt"));
			in_enc = new BufferedReader(new FileReader(path + "lastcount.txt"));
			reademptybuf = new BufferedReader(new FileReader(path
					+ "emptyfiles.txt"));
			out = new BufferedWriter(new FileWriter(input_path + "Input/"
					+ "enconinput.txt"));
			String s = "";
			String sentStr = "";
			String prefix = "";
			String suffix = "";
			String st = "";
			String stg = "";
			int incrementalIter = 0;

			FinalAppln docp = new FinalAppln();
			int count = 0;
			while ((sentStr = bufred.readLine()) != null) {
				String func = input_path + "Output/" + sentStr;
				// //System.out.println("FUNC:"+func);
				File newFile = new File(func);
				if (newFile.length() > 50) {
					list.add(sentStr);
					// //System.out.println(sentStr);
				} else {
					// //System.out.println("EmptyFiles:"+sentStr);
				}
			}
			// System.out.println("LIST:"+list+":"+list.size());

			int totalsize = list.size();
			if ((st = inbr.readLine()) != null) {
				int sz = list.indexOf(st);
				stfrm = sz + 1;
			} else {
				stfrm = 0;
			}
			// String getfName = list.get(list.size() - 1).toString();
			if ((stg = in_enc.readLine()) != null) {
				int fcnt = Integer.parseInt(stg);
				filecount = fcnt;
			} else {
				filecount = 0;
			}
			for (int i = stfrm; i < list.size(); i++) {
				try {
					sentStr = (String) list.get(i);
				} catch (Exception e) {
					e.printStackTrace();
				}
				out.write(sentStr + "\n");
				incrementalIter++;
				if (incrementalIter % 1 == 0) {
					getfName = list.get(i).toString();
					out.close();
					// System.out.println("FileName"+getfName);
					try {
						docp.start(ll_new);
						writeinObject("unlgraph" + filecount + ".txt");
						// in = new Index();
						// in.indexProcess(ll_new);
						// alnew.clear();
						if (incrementalIter % 1 == 0) {

							filecount++;
							ll_new = null;
							ll_new = new FinalLLImpl[totalsize];
						}
						// System.gc();
					} catch (Throwable ex) {
					}
					// System.gc();
					out = new BufferedWriter(new FileWriter(input_path
							+ "Input/" + "enconinput.txt"));
					// buffer = new BufferedWriter(new FileWriter(path +
					// "lastfile.txt"));
					// inbuf = new BufferedWriter(new FileWriter(path +
					// "lastcount.txt"));
				}
				// buffer.close();
				// summary.callFunctions();
			}
			// run_Index();

			// buffer.write(getfName);
			// buffer.close();
			// inbuf.write(Integer.toString(filecount));
			// inbuf.close();
			// emptybuf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}// main

	public static void writeinObject(String filename) {
		try {
			System.out.println("Entered in to writing block");
			FileOutputStream fostream = new FileOutputStream(graph_path
					+ filename);
			ObjectOutput oostream = new ObjectOutputStream(fostream);
			oostream.writeObject(ll_new);
			System.out.println("Writing Completed");
			oostream.close();
			fostream.close();
		} catch (Exception e) {
			e.printStackTrace();
			// log.log(Level.SEVERE, "Uncaught exception", e);
		}
	}

	public ArrayList process(String queryString) {
		ArrayList arr = null;
		return arr;
	}

}
