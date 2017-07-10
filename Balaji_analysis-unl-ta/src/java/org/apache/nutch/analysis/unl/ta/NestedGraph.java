package org.apache.nutch.analysis.unl.ta;

//package org.apache.nutch.enconversion.unl.ta;

import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;

import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.regex.Pattern;

/**
 * 
 */

/**
 * @author Balaji Jagan
 * 
 */
public class NestedGraph {

	ArrayList<String> nRoot;
	ArrayList<String> nAnal;
	ArrayList<String> nReln;
	ArrayList<String> nCRC;
	ArrayList<String> nSubGraph;
	ArrayList nNum;
	ArrayList subgraph;
	ArrayList fromgraph;
	ArrayList fromscope;
	ArrayList fromcid;
	ArrayList relation;
	ArrayList tocid;
	ArrayList toscope;
	ArrayList tograph;

	// String getCRC="";
	String nResult;
	String concat;
	String nestresult;

	// String id1;
	// String id2;

	/**
	 * 
	 */
	public NestedGraph() {
		// TODO Auto-generated constructor stub
	}

	public void init() {
		nRoot = new ArrayList<String>();
		nAnal = new ArrayList<String>();
		nReln = new ArrayList<String>();
		nNum = new ArrayList();
		nCRC = new ArrayList<String>();
		nSubGraph = new ArrayList<String>();

		subgraph = new ArrayList<String>();
		fromgraph = new ArrayList<String>();
		fromscope = new ArrayList<String>();
		fromcid = new ArrayList<String>();
		relation = new ArrayList<String>();
		tocid = new ArrayList<String>();
		toscope = new ArrayList<String>();
		tograph = new ArrayList<String>();

		// getCRC = "";
		nResult = "";
		concat = "";
		nestresult = "";
		// id1 = "";
		// id2 = "";

	}

	public String processGraph(String result) {
		// System.out.println("Inside Nested UNL");
		init();
		// nRoot.addAll(root);
		// nAnal.addAll(anal);
		// nReln.addAll(reln);
		// System.out.println("Nested UNL:"+nRoot+"\n"+nReln+"\n"+num+"\n"+result+"\n");
		System.out.println("result:" + result);
		StringTokenizer strTok = new StringTokenizer(result, "#");
		// int totcnt = strTok.countTokens();
		String crcEntry = "";
		String conEntry = "";
		String getCRC = "";
		// int cnt = 0;
		while (strTok.hasMoreElements()) {
			try {
				// System.out.println("Inside While:");
				getCRC = strTok.nextToken();
				// System.out.println("getCRC:"+getCRC);
				if (getCRC.equals("[s]")) {

				} else if (getCRC.equals("[w]")) {
					conEntry = strTok.nextToken().trim();
					while (!(conEntry.equals("[/w]"))) {
						// System.out.println(conEntry);
						conEntry = strTok.nextToken().trim();
					}

				} else if (getCRC.equals("[r]")) {
					crcEntry = strTok.nextToken().trim();
					while (!(crcEntry.equals("[/r]"))) {
						nCRC.add(crcEntry);
						crcEntry = strTok.nextToken().trim();
					}
				}
			} catch (Exception e) {

			}
			// cnt++;
			// System.out.println("cnt:"+cnt);
		}
		addTags();
		// nResult = result;
		compoundUWs();
		addToResult(result);
		// System.out.println("nCRC:"+nCRC);
		return nestresult;
	}

	public void addTags() {
		int i = 0, totsize = nCRC.size();
		while (i < totsize) {
			String getWord = nCRC.get(i).toString();
			int index = nCRC.indexOf(getWord);
			getWord = "sgflg" + " " + "frmscp" + " " + "scp1" + " " + getWord
					+ " " + "scp2" + " " + "toscp";
			System.out.println("GetWord:" + getWord);
			nCRC.remove(index);
			nCRC.add(index, getWord);
			i++;
		}
	}

	public void compoundUWs() {
		System.out.println("CompoundUWs:");
		int i = 0, totsize = nCRC.size();
		String scopeid = "";
		String id1 = "", id2 = "", id3 = "", id4 = "", id5 = "";
		int id = 1, sid;
		while (i < totsize) {
			nResult = nCRC.get(i).toString();
			int index = nCRC.indexOf(nResult);
			// if(nResult.contains("mod")){
			// System.out.println("nResult:"+nResult);

			// System.out.println("nResult inside Else:"+nResult);
			String pattern = "[,\\s]+";
			Pattern splitter = Pattern.compile(pattern);
			String[] getid = splitter.split(nResult);
			id1 = getid[0].toString();
			// System.out.println("id1:"+id1);
			id2 = getid[1].toString();
			// System.out.println("id2:"+id2);
			id3 = getid[2].toString();
			id4 = getid[3].toString();
			id5 = getid[4].toString();
			System.out.println("id:" + id1 + " " + id2 + " " + id3 + " " + id4
					+ " " + id5);
			if ((id5.contains("mod")) || (id5.contains("pos"))
					|| (id5.contains("and")) || (id5.contains("or"))) {
				// System.out.println("Inside MOD:");
				// sid = id++;
				sid = Integer.parseInt(id4);
				scopeid = ":0" + Integer.toString(sid);
				// System.out.println("SCOPEID:"+scopeid);
				nResult = nResult.replace("scp1", scopeid);
				nCRC.remove(index);
				nCRC.add(index, nResult);
				System.out.println("nResult FFFFFFFFFFFF:" + nResult);
				// getScope(id1,scopeid);
			} else {
				/*
				 * nResult = nResult.replace("scp1", "None");
				 * nCRC.remove(index); nCRC.add(index, nResult);
				 */
				// System.out.println("Else NRESULT:"+nResult);
			}

			// }
			// }
			// }
			// }
			i++;
		}
		getScope();
		// System.out.println("Final NCRC:"+nCRC);
	}

	public void getScope() {
		System.out.println("Inside getScope:");
		String id1 = "", id2 = "", id3 = "", id4 = "", id5 = "";
		String nid1 = "", nid2 = "", nid3 = "", nid4 = "", nid5 = "", nid6 = "", nid7 = "", nid8 = "";
		int totsize = nCRC.size();
		try {
			for (int i = 0; i < totsize; i++) {
				String sResult = nCRC.get(i).toString();
				String pattern = "[,\\s]+";
				Pattern splitter = Pattern.compile(pattern);
				String[] getid = splitter.split(sResult);
				id3 = getid[2].toString();
				System.out.println("id1:" + id1);
				id4 = getid[3].toString();
				for (int j = i + 1; j < totsize; j++) {
					String nextCRC = nCRC.get(j).toString();
					int index = nCRC.indexOf(nextCRC);
					String pattern1 = "[,\\s]+";
					Pattern splitter1 = Pattern.compile(pattern1);
					String[] getid1 = splitter1.split(nextCRC);
					nid1 = getid1[0].toString();
					// System.out.println("nid1:"+nid1);
					nid2 = getid1[1].toString();
					// System.out.println("nid2:"+nid2);
					nid3 = getid1[2].toString();
					// System.out.println("nid3:"+nid3);
					nid4 = getid1[3].toString();
					// System.out.println("nid4:"+nid4);
					nid5 = getid1[4].toString();
					// System.out.println("nid5:"+nid5);
					nid6 = getid1[5].toString();
					nid7 = getid1[6].toString();
					nid8 = getid1[7].toString();
					System.out.println("nid5:" + nid1 + " " + nid2 + " " + nid3
							+ " " + nid4 + " " + nid5 + " " + nid6 + " " + nid7
							+ " " + nid8);
					if (!(id3.equals("scp1"))) {
						System.out.println("Inside not scp1:");
						System.out.println("nid4==============>:" + id4 + ":"
								+ nid6);
						if (id4.equals(nid6)) {
							System.out.println("nid4:" + id4 + ":" + nid6);
							// nid5.replace(nid5, id1);
							String finalCRC = nid1 + "\t" + nid2 + "\t" + nid3
									+ "\t" + nid4 + "\t" + nid5 + "\t" + nid6
									+ "\t" + id3 + "\t" + nid8;
							System.out.println("finalCRC:" + finalCRC);
							nCRC.remove(index);
							nCRC.add(index, finalCRC);

						}
					}
				}
			}
			System.out.println("List of nested CRCs:" + nCRC);
			get_Subgraphs();

		} catch (Exception e) {

		}
	}

	public void get_Subgraphs() {
		try {
			for (String get_crc : nCRC) {
				String pattern1 = "[,\\s]+";
				Pattern splitter2 = Pattern.compile(pattern1);
				String[] process_CRC = splitter2.split(get_crc);
				subgraph.add(process_CRC[0].toString());
				fromgraph.add(process_CRC[1].toString());
				fromscope.add(process_CRC[2].toString());
				fromcid.add(process_CRC[3].toString());
				relation.add(process_CRC[4].toString());
				tocid.add(process_CRC[5].toString());
				toscope.add(process_CRC[6].toString());
				tograph.add(process_CRC[7].toString());
				System.out.println("=============>" + process_CRC[0].toString()
						+ "\t" + process_CRC[1].toString() + "\t"
						+ process_CRC[2].toString() + "\t"
						+ process_CRC[3].toString() + "\t"
						+ process_CRC[4].toString() + "\t"
						+ process_CRC[5].toString() + "\t"
						+ process_CRC[6].toString() + "\t"
						+ process_CRC[7].toString());
			}
			process_Subgraphs();
		} catch (Exception e) {

		}
	}

	public void process_Subgraphs() {
		try {
			String sg = "";
			for (int index = 0; index < tograph.size(); index++) {
				String get_scpid = toscope.get(index).toString();
				System.out.println("get_scpid:" + get_scpid);
				if ( (!get_scpid.contains("scp2"))
						&& (!fromscope.get(index).toString().contains("scp1"))) {
					String set_fscpflag = fromgraph.get(index).toString();
					set_fscpflag = set_fscpflag.replace("frmscp", "fsf"
							+ fromcid.get(index).toString());
					System.out.println("set FScpflag:" + index + "<========>"
							+ set_fscpflag);
					fromgraph.remove(index);
					fromgraph.add(index, set_fscpflag);
					if (fromscope.contains(get_scpid)) {
						int newIndex = fromscope.indexOf(get_scpid);
						System.out
								.println("Inside FromScope index:" + newIndex);
						if (!toscope.get(newIndex).toString().contains("scp2")) {
							System.out.println("Inside if----------------->"
									+ get_scpid);
							String set_tscpflag = tograph.get(index).toString();
							set_tscpflag = set_tscpflag.replace("toscp", "tsf"
									+ tocid.get(index).toString());
							tograph.remove(index);
							tograph.add(index, set_tscpflag);
							
						/*	System.out.println("SET_SCOPE_FLAG:=======>"
									+ subgraph.get(index).toString() + "\t"
									+ fromgraph.get(index).toString() + "\t"
									+ fromscope.get(index).toString() + "\t"
									+ fromcid.get(index).toString() + "\t"
									+ relation.get(index).toString() + "\t"
									+ tocid.get(index).toString() + "\t"
									+ toscope.get(index).toString() + "\t"
									+ tograph.get(index).toString()); */
							sg = subgraph.get(index).toString() + "\t"
							+ fromgraph.get(index).toString() + "\t"
							+ fromscope.get(index).toString() + "\t"
							+ fromcid.get(index).toString() + "\t"
							+ relation.get(index).toString() + "\t"
							+ tocid.get(index).toString() + "\t"
							+ toscope.get(index).toString() + "\t"
							+ tograph.get(index).toString();							
							nSubGraph.add(sg);
						}
					} else {
				/*		System.out.println("^^^^^^^^^^^ SET_SCOPE_FLAG:=======>"
								+ subgraph.get(index).toString() + "\t"
								+ fromgraph.get(index).toString() + "\t"
								+ fromscope.get(index).toString() + "\t"
								+ fromcid.get(index).toString() + "\t"
								+ relation.get(index).toString() + "\t"
								+ tocid.get(index).toString() + "\t"
								+ toscope.get(index).toString() + "\t"
								+ tograph.get(index).toString());*/
					}
				/*	System.out.println("*********** SET_SCOPE_FLAG:=======>"
							+ subgraph.get(index).toString() + "\t"
							+ fromgraph.get(index).toString() + "\t"
							+ fromscope.get(index).toString() + "\t"
							+ fromcid.get(index).toString() + "\t"
							+ relation.get(index).toString() + "\t"
							+ tocid.get(index).toString() + "\t"
							+ toscope.get(index).toString() + "\t"
							+ tograph.get(index).toString());	*/
					sg = subgraph.get(index).toString() + "\t"
					+ fromgraph.get(index).toString() + "\t"
					+ fromscope.get(index).toString() + "\t"
					+ fromcid.get(index).toString() + "\t"
					+ relation.get(index).toString() + "\t"
					+ tocid.get(index).toString() + "\t"
					+ toscope.get(index).toString() + "\t"
					+ tograph.get(index).toString();
					
					nSubGraph.add(sg);
				} else {
			/*		System.out.println("############ SET_SCOPE_FLAG:=======>"
							+ subgraph.get(index).toString() + "\t"
							+ fromgraph.get(index).toString() + "\t"
							+ fromscope.get(index).toString() + "\t"
							+ fromcid.get(index).toString() + "\t"
							+ relation.get(index).toString() + "\t"
							+ tocid.get(index).toString() + "\t"
							+ toscope.get(index).toString() + "\t"
							+ tograph.get(index).toString());	*/
					sg = subgraph.get(index).toString() + "\t"
					+ fromgraph.get(index).toString() + "\t"
					+ fromscope.get(index).toString() + "\t"
					+ fromcid.get(index).toString() + "\t"
					+ relation.get(index).toString() + "\t"
					+ tocid.get(index).toString() + "\t"
					+ toscope.get(index).toString() + "\t"
					+ tograph.get(index).toString();
					nSubGraph.add(sg);
				}
			}
		} catch (Exception e) {

		}
	}

	public void concatResult() {
		concat = "[r]#";
		for (int i = 0; i < nSubGraph.size(); i++) {
			concat += nSubGraph.get(i).toString() + "#";
		}
		System.out.println("CONCAT:" + concat);
	}

	public void addToResult(String result) {
		concatResult();
		int i = result.indexOf("[r]");
		int j = result.indexOf("[/r]", i);
		System.out.println("i and j:" + i + ":" + j);
		String newRes = result.substring(i, j);

		// System.out.println("newRes:"+newRes);
		nestresult = result.replace(newRes, concat);
		System.out.println("Final OUTPUT RESULT:" + nestresult);
	}
}
