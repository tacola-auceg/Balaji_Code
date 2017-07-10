package org.apache.nutch.analysis.unl.ta;

//import org.apache.nutch.analysis.unl.ta.Analyser;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.LinkedList;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * 
 * @author root
 */
public class Pattern_Match {

	Pattern_TagUNL patt = new Pattern_TagUNL();
	Masking mask = new Masking();
	
	ArrayList reln_tag = new ArrayList();
	ArrayList level1 = new ArrayList();
	ArrayList level2 = new ArrayList();
	ArrayList level3 = new ArrayList();
	ArrayList level4 = new ArrayList();
	ArrayList level5 = new ArrayList();
	
	String result;
	String tword;
	String hword;
	String uword;
	String posword;
	String caseEnd;
	String postposition;
	String unlReln;
	String getFirst;
	String getNext;
	String getValue;
	String result_Reln;
	String firstReln;
	String nextReln;
	String resultTag;
	String getFirst1;
	String getNext1;
	String getValue1;
	String get1;
	String get2;
	String get3;
	int counter;
	ArrayList<String> store_tags;// = new ArrayList<String>();
	ArrayList<String> compl_tags;
	Hashtable pattern = new Hashtable();
	Hashtable inp_pattern_list = new Hashtable();
	PrintStream pattern_print;
	PrintStream result_print;
	PrintStream Tag_print;
	
	ArrayList<String> nlw;
	ArrayList<String> hw;
	ArrayList<String> uw;
	ArrayList<String> cs;
	ArrayList<String> lc;
	ArrayList<String> word_list;
	ArrayList<String> unl_reln;
	ArrayList<String> pattern_list;
	
	String V_N[] = {"agt", "aoj", "ben", "cag", "cao", "cob", "con", "dur", "gol", "ins", "man", "met", "obj", "opl", "plc", "plf", "plt", "pur", "rsn", "scn", "src", "tim", "tmf", "tmt", "via"};
	String N_N[] = {"and", "bas", "cnt", "coo", "equ", "fmt", "frm", "icl", "iof", "nam", "or", "per", "pof", "pos", "ptn", "qua", "seq", "to"};
	
	Hashtable V_N_Hash = new Hashtable();
	Hashtable N_N_Hash = new Hashtable();
	
	public void init() {
		result = "";
		
		nlw = new ArrayList<String>();
		hw = new ArrayList<String>();
		uw = new ArrayList<String>();
		cs = new ArrayList<String>();
		lc = new ArrayList<String>();
		word_list = new ArrayList<String>();
		unl_reln = new ArrayList<String>();
		pattern_list = new ArrayList<String>();

		tword = "";
		hword = "";
		uword = "";
		posword = "";
		caseEnd = "";
		unlReln = "";
		postposition = "";

		getFirst = "";
		getNext = "";
		getValue = "";
		result_Reln = "";
		firstReln = "";
		nextReln = "";
		resultTag = "";

		get1 = "";
		get2 = "";
		get3 = "";
		getFirst1 = "";
		getNext1 = "";
		getValue1 = "";

		store_tags = new ArrayList<String>();
		compl_tags = new ArrayList<String>();

	}

	public String process_TagSet(String st) {
		 load_VN();
		 load_NN();
		init();
		int i = 0;
		counter = 0;

		// int count = 1;
		// String caseEnd = "";
	//	System.out.println("St:"+st);
		StringTokenizer strToken1 = new StringTokenizer(st, "@");
		int cntwrds = strToken1.countTokens();
		while (i < cntwrds) {
			counter++;
			String getExp = strToken1.nextToken();
			// System.out.println("getExp:"+getExp);
			StringTokenizer strToken2 = new StringTokenizer(getExp, "+");
			tword = strToken2.nextToken();
			hword = strToken2.nextToken();
			uword = strToken2.nextToken();
			posword = strToken2.nextToken();
			caseEnd = strToken2.nextToken();
			postposition = strToken2.nextToken();
			unlReln = strToken2.nextToken();
			
			nlw.add(tword);
			hw.add(hword);
			uw.add(uword);
			cs.add(caseEnd);
			lc.add(posword);
			word_list.add(postposition);
			unl_reln.add(unlReln);

		/*	if (!unlReln.equals("None")) {
				pattern_Pairs();
			}	*/
			i++;
		}
		pattern_Pairs();
		return result;
	}

	public void writeintoFile() {
		try {
			pattern_print = new PrintStream(new FileOutputStream(
					"./resource/unl/RE/BWA_Prob/Result/pattern.csv"));
			result_print = new PrintStream(new FileOutputStream(
					"./resource/unl/RE/BWA_Prob/Result/TagResult.txt"));
			Tag_print = new PrintStream(new FileOutputStream(
					"./resource/unl/RE/BWA_Prob/Result/Tags.txt"));
			pattern_print.println("Patterns$Total occurences");

		} catch (Exception e) {
		}
	}

	public void pattern_Process() {

		int count1 = 1;
		if (inp_pattern_list.isEmpty()) {
			inp_pattern_list.put(getValue, count1);
		} else if (inp_pattern_list.containsKey(getValue)) {
			Object cnt = inp_pattern_list.get(getValue);
			int cnt2 = (Integer) cnt;
			cnt2++;
			inp_pattern_list.put(getValue, cnt2);
		} else {
			count1 = 1;
			inp_pattern_list.put(getValue, count1);
		}
	}
	public void pattern_Pairs(){
		try{
			
			for(int i=nlw.size()-1;i>0; i--){
		//		System.out.println("Print:"+nlw.get(i).toString());
				String frm_cs = cs.get(i).toString();
				String frm_lc = lc.get(i).toString();
				String frm_uw = uw.get(i).toString();
				getFirst = frm_cs +":"+frm_lc+":"+frm_uw;
				String mod_pat = getFirst;
				for(int j=i-1;j>0;j--){
					String to_cs = cs.get(j).toString();
					String to_lc = lc.get(j).toString();
					String to_uw = uw.get(j).toString();
					String w_w = word_list.get(j).toString();
					getNext = to_cs +":"+to_lc+":"+to_uw;
					String unl_key = unl_reln.get(j).toString();
					if(frm_lc.contains("Verb")){
						if(V_N_Hash.containsKey(unl_key)){
					//	System.out.println("Inside IF Verb Noun Key----------->:"+unl_key);
							getValue = unl_key + "\t" +"("+getFirst+"-"+w_w+"-"+getNext+")";
						}else if(to_lc.contains("Adverb")){
							getValue = unl_key + "\t" +"("+getFirst+"-"+w_w+"-"+getNext+")";
						//	System.out.println("Adverb----------->"+getValue);
						}
					}else if(frm_lc.contains("Noun")){
						if(N_N_Hash.containsKey(unl_key)){
						//	System.out.println("Inside IF Verb Noun Key----------->:"+unl_key);
							
							if( ( (frm_lc.contentEquals("Entity"))||(frm_lc.contentEquals("Noun")) ) && ((to_lc.contentEquals("Entity"))||(to_lc.contentEquals("Noun"))) ){
								getValue = unl_key + "\t" +"("+getFirst+"-"+w_w+"-"+getNext+")";
							//	System.out.println("*************Inside IF***************");
							}							
						}
					}
					if(to_lc.contains("Adjective")){
						int k = i-j;
						if(k == 1){
						//	System.out.println("Integer:"+i+"-"+j+"="+k);
							getValue =  unl_key + "\t" + "("+getFirst+"-"+w_w+"-"+getNext+")"; 
						//	System.out.println("Inside Adjective:"+getValue);
						}	
					}
				//	System.out.println(getValue);
					pattern_list.add(getValue);
					pattern_Process();
					getValue = "";
				}
			}
		//	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+pattern_list.toString()+"\t"+pattern_list.size());
		}catch(Exception e){
			
		}
	}
	
	public void load_VN(){
		for(int i=0;i<V_N.length;i++){
			String key = V_N[i].toString();
			String value = "1";
			V_N_Hash.put(key, value);
		}
	}
	public void load_NN(){
		for(int i=0;i<N_N.length;i++){
			String key = N_N[i].toString();
			String value = "1";
			N_N_Hash.put(key, value);
		}
	}
	public void writeTags() {
		for (Object key : inp_pattern_list.keySet()) {
			pattern_print.println(key.toString() + "$"
					+ String.valueOf(inp_pattern_list.get(key)));
		}
	}

	public StringBuffer fineTuneDocs(StringBuffer docbuff) {
		String getBuff = docbuff.toString();
		StringBuffer sBuff = new StringBuffer();
		if (getBuff.contains("+@")) {
			// System.out.println("getbuff:"+getBuff);
			getBuff = getBuff.replace("+@", "+None@");
			getBuff = getBuff.replace("<.", "");
			getBuff = getBuff.replace("+src@", "+plc#src@");
			// System.out.println("gbuff:"+getBuff);
			sBuff.append(getBuff);
		}else{
			sBuff.append(getBuff);
		}
		// System.out.println("sbuff:"+sBuff);
		return sBuff;
	}

	class Pattern_TagUNL {
		
		public void process_Docs() {
			String s = null;
			FileReader fr = null;
			String temp = null;
			ArrayList al = new ArrayList();
			String output = "";
			String str = "";
			String sent = "", recv = "", token = "", fn = "";
			int temp1 = 0, j = 0;
			// Pattern_Match P_M = new Pattern_Match();
			try {
				BufferedReader br = new BufferedReader(
						new FileReader(
								"./resource/unl/RE/tagDocswoR/Output/nonemptyfiles.txt"));
				while ((s = br.readLine()) != null) {
					fn = "./resource/unl/RE/tagDocswoR/Output/" + s;
					// tag_file = s;
					System.out.println("fn:" + fn);
					StringBuffer docbuff = new StringBuffer();
					StringBuffer docbuff1 = new StringBuffer();
					fr = new FileReader(fn);

					// System.out.println("fr:"+fr);
					BufferedReader buff = new BufferedReader(new FileReader(fn));
					while ((str = buff.readLine()) != null) {
						docbuff.append(str);
						// System.out.println("docbuff:"+docbuff);
						docbuff1 = fineTune(docbuff);
					}

					// System.out.println("docbuff1:"+docbuff1);
					StringTokenizer sentToken1 = new StringTokenizer(docbuff1
							.toString().trim(), ".", false);
					// l.loadTaggedData();
					// j = 0;
					while (sentToken1.hasMoreTokens()) {
						sent = sentToken1.nextToken();
					//	System.out.println(sent);
						process_RelationTag(sent);
					}
					output = "";
				}
				// P_M.writeintoFile();
				// P_M.writeTags();
				// System.out.println("GET_CASE:");
				// System.out.println("pattern:" + P_M.pattern);
				// System.out.println("case_pos_Entry:" + cp.case_pos_Entry);
				// System.out.println("trans_freq_Entry:" +
				// cp.trans_freq_Entry);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public StringBuffer fineTune(StringBuffer docbuff) {
		String getBuff = docbuff.toString();
		StringBuffer sBuff = new StringBuffer();
		// if (getBuff.contains("+@")) {
		// System.out.println("getbuff:" + getBuff);
		getBuff = getBuff.replace("+@", "+???@");
		getBuff = getBuff.replace("<.", "");
		getBuff = getBuff.replace("<தொகு+collect+icl>action+Verb+None+None+???@",
				"<");
		getBuff = getBuff.replace("<தாவு+jump+icl>action+Verb+None+None+???@", "<");
		getBuff = getBuff.replace("தொகு+collect+icl>action+Verb+None+None+???@", "");
		// System.out.println("gbuff:"+getBuff);
		sBuff.append(getBuff);
		// } else {
		// sBuff.append(getBuff);
		// }
		// System.out.println("sbuff:"+sBuff);
		return sBuff;
	}

	public String process_RelationTag(String st) {
		init();
		int i = 0;
		counter = 0;
		// int count = 1;
		// String caseEnd = "";
		//System.out.println("St:" + st);
	//	if (!st.equals("<")) {
			StringTokenizer strToken1 = new StringTokenizer(st, "@");
			int cntwrds = strToken1.countTokens();
			while (i < cntwrds) {
				counter++;
				String getExp = strToken1.nextToken();
				if ((!(getExp.contains("தாவு")))
						&& (!(getExp.contains("தேடல்")))
						&& (!(getExp.contains("கலைக்களஞ்சியம்")))
						&& (!(getExp.contains("விக்கிபீடியா")))
						&& (!(getExp.contains("உள்ளடக்கம்")))
						&& (!(getExp.contains("இணைப்பு")))
						&& (!(getExp.contains("பகுப்பு")))) {
					// System.out.println("getExp:" + getExp);
					StringTokenizer strToken2 = new StringTokenizer(getExp, "+");
					tword = strToken2.nextToken();
					hword = strToken2.nextToken();
					uword = strToken2.nextToken();
					posword = strToken2.nextToken();
					caseEnd = strToken2.nextToken();
					postposition = strToken2.nextToken();
					unlReln = strToken2.nextToken();
					
					nlw.add(tword);
					hw.add(hword);
					uw.add(uword);
					lc.add(posword);
					cs.add(caseEnd);
					word_list.add(postposition);
					unl_reln.add(unlReln);

				/*	String get_tag = caseEnd + ":" + posword + ":" + uword;
					String com_tag = tword+ ":" + caseEnd + ":"
							+ posword  + ":" + postposition  + ":" + hword + ":" + uword;
					store_tags.add(get_tag);
					compl_tags.add(com_tag);*/
					// if (!unlReln.equals("None")) {
				} else {

				}
				// get_Pattern_Pairs();
				// System.out.println("get3:"+get3);
				// }
				i++;
			}
		//	if (store_tags.size() > 1) {
				// System.out.println("Inside IF > 1");		
			get_Pattern_Pairs();
			//	pattern_Storage();
		//	} else {

		//	}
			// System.out.println("Store Tags:"+
			// store_tags+":"+store_tags.size());
			// System.out.println("Complete Tags:"+
			// compl_tags+":"+compl_tags.size());
	//	} else {

	//	}
		return result;
	}
	public void get_Pattern_Pairs(){
		try{
			
			for(int i=nlw.size()-1;i>0; i--){
		//		System.out.println("Print:"+nlw.get(i).toString());
				String frm_nlw = nlw.get(i).toString();
				String frm_hw = hw.get(i).toString();
				String frm_cs = cs.get(i).toString();
				String frm_lc = lc.get(i).toString();
				String frm_uw = uw.get(i).toString();
				getFirst = frm_cs +":"+frm_lc+":"+frm_uw;
				String mod_pat = getFirst;
				for(int j=i-1;j>0;j--){
					String to_nlw = nlw.get(i).toString();
					String to_hw = hw.get(i).toString();
					String to_cs = cs.get(j).toString();
					String to_lc = lc.get(j).toString();
					String to_uw = uw.get(j).toString();
					String w_w = word_list.get(j).toString();
					getNext = to_cs +":"+to_lc+":"+to_uw;
				//	String unl_key = unl_reln.get(j).toString();
					if(frm_lc.contains("Verb")){
					//	if(V_N_Hash.containsKey(unl_key)){
					//	System.out.println("Inside IF Verb Noun Key----------->:"+unl_key);
							getValue = "("+getFirst+"-"+w_w+"-"+getNext+")";
					/*	}else if(to_lc.contains("Adverb")){
							getValue = "("+getFirst+"-"+w_w+"-"+getNext+")";
							System.out.println("Adverb----------->"+getValue);
						}*/
					}else if(frm_lc.contains("Noun")){
					//	if(N_N_Hash.containsKey(unl_key)){
						//	System.out.println("Inside IF Verb Noun Key----------->:"+unl_key);
							
							if( ( (frm_lc.contentEquals("Entity"))||(frm_lc.contentEquals("Noun")) ) && ((to_lc.contentEquals("Entity"))||(to_lc.contentEquals("Noun"))) ){
								getValue = "("+getFirst+"-"+w_w+"-"+getNext+")";
							//	System.out.println("*************Inside IF***************");
							}							
					//	}
					}
					if(to_lc.contains("Adjective")){
						int k = i-j;
						if(k == 1){
					//		System.out.println("Integer:"+i+"-"+j+"="+k);
							getValue =  "("+getFirst+"-"+w_w+"-"+getNext+")"; 
					//		System.out.println("Inside Adjective:"+getValue);
						}	
					}
				//	System.out.println("Unlabeled data:"+getValue);
					if(!getValue.isEmpty()){
						Tag_print.println(getValue);
						store_tags.add(getValue);
						match_Pattern();
					}
					getValue = "";
				//	pattern_Process();
					
				}
				
			}
		//	System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+pattern_list.toString()+"\t"+pattern_list.size());
		}catch(Exception e){
			
		}
	}
	
	public void pattern_Storage() {
		int tot_cnt = store_tags.size();
		for (int i = tot_cnt; i > 0; i--) {
			getFirst1 = compl_tags.get(i - 1).toString();
			get1 = store_tags.get(i - 1).toString();
			for (int j = i - 1; j > 0; j--) {
				getNext1 = compl_tags.get(j - 1).toString();
				get2 = store_tags.get(j - 1).toString();
				if ((!(get2.equals(get1))) && (!(get2.contains("Numbers")))
						&& (!(get1.contains("Numbers")))
						&& (!(get1.contains("Adjective")))
						&& (!(get2.contains("Verb")))) {
					get3 = "(" + get1 + "-" + postposition + "-" + get2 + ")";
					if ((!(get1.contains("Particle")))
							&& (!(get2.contains("Particle")))
							&& (!(get1.contains("None:Noun")))
							&& (!(get1.contains("None:Entity")))
							&& (!(get1.contains("Postposition")))) {
						// get3 = "(" + get1 + "-" + get2 + ")";
						if ((get2.contains("இல்")) || (get2.contains("க்கு"))) {
							Tag_print.println(get3);
						} else if ((!(get1.contains("இல்")))
								&& (!(get1.contains("க்கு")))) {
							Tag_print.println(get3);
						} else {
							Tag_print.println(get3);
						}
					}
					getValue1 = "(" + getFirst1 + "-" + postposition + "-"
							+ getNext1 + ")";
				}
				// if(!getNext1.equals(getFirst1)){

				// }
				// if( (!get3.startsWith("(None:Numbers:icl>number")) ){

				match_Pattern();
				// }
			}
		}
		// }
	}

	public void match_Pattern() {
	//	System.out.println("Inside Match Pattern");
		try{
		Enumeration e1 = pattern.keys();
		while (e1.hasMoreElements()) {
			String key1 = (String) e1.nextElement();
	//		System.out.println("key1:"+key1);
			String spt[] = key1.split("\t");
			String reln = spt[0].toString();
			String patn = spt[1].toString();
			// System.out.println("reln:"+reln + patn+":"+get3);
			if (patn.equals(getValue)) {
			//	System.out.println(patn+":"+getValue);
				// if(getNext1.equals(getFirst1)){
				// System.out.println("getValue1:"+getValue1);
				// }else{
				resultTag = reln + getValue;
			//	System.out.println("RESULT:" + resultTag);
				result_print.println(resultTag);
				// }
			}else{
				/************Partial Match******************/
			//	System.out.println("/************Partial Match******************/");
				String spt_pattern[] = getValue.split("-");
				//System.out.println(getValue+"------------>"+spt_pattern[0]+":"+getValue.indexOf("-"));
				String left_cmp = spt_pattern[0].toString().replace("(", "");
				String mid_cmp = spt_pattern[1].toString();
				String right_cmp = spt_pattern[2].toString().replace(")", "");
				
				String spt_cmp1[] = left_cmp.split(":");
				String t1 = spt_cmp1[0].toString();
				String t2 = spt_cmp1[1].toString();
				String t3 = spt_cmp1[2].toString();
				
				String t4 = mid_cmp;
				
				String spt_cmp2[] = right_cmp.split(":");
				String t5 = spt_cmp2[0].toString();
				String t6 = spt_cmp2[1].toString();
				String t7 = spt_cmp2[2].toString();		
				
				LinkedList inp_ll1 = mask.Level1_Mask(t2, t5, t6, t7);
				if(level1.contains(inp_ll1)){
					int index = level1.indexOf(inp_ll1);
					String label = reln_tag.get(index).toString();
					System.out.println("LEVEL1:"+index+"|"+reln +"|"+inp_ll1 +"| is equal to |"+level1.get(index));
					break;
				}
				LinkedList inp_ll2 = mask.Level2_Mask(t2, t5, t6);
				if(level2.contains(inp_ll2)){
					int index = level2.indexOf(inp_ll2);
					String label = reln_tag.get(index).toString();
					System.out.println("LEVEL2:"+index+"|"+reln +"|"+inp_ll2+"| is equal to |"+level2.get(index));
					break;
				}
				LinkedList inp_ll3 = mask.Level3_Mask(t2, t6);
				if(level3.contains(inp_ll3)){
					int index = level3.indexOf(inp_ll3);
					String label = reln_tag.get(index).toString();
					System.out.println("LEVEL3:"+index+"|"+reln +"|"+inp_ll3+"| is equal to |"+level3.get(index));
					break;
				}
				LinkedList inp_ll4 = mask.Level4_Mask(t1, t2, t5, t6);
				if(level4.contains(inp_ll4)){
					int index = level4.indexOf(inp_ll4);
					String label = reln_tag.get(index).toString();
					System.out.println("LEVEL1:"+index+"|"+reln +"|"+inp_ll4+"| is equal to |"+level4.get(index));
					break;
				}
				LinkedList inp_ll5 = mask.Level5_Mask(t2, t4, t6);
				if(level5.contains(inp_ll5)){
					int index = level5.indexOf(inp_ll5);
					String label = reln_tag.get(index).toString();
					System.out.println("LEVEL5:"+index+"|"+reln +"|"+inp_ll5+"| is equal to |"+level5.get(index));
					break;
				}
				
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void Mask_Seed_Tuples(){
		try{
			reln_tag = new ArrayList();
		Enumeration e1 = pattern.keys();
		while (e1.hasMoreElements()) {
			String key1 = (String) e1.nextElement();
	//		System.out.println("key1:"+key1);
			String spt[] = key1.split("\t");
			String reln = spt[0].toString();
			String patn = spt[1].toString();
			
			reln_tag.add(reln);
			
			String spt_pattern[] = patn.split("-");
		//	System.out.println(patn+"------------>"+spt_pattern.length);
			String left_cmp = spt_pattern[0].toString().replace("(", "");
			String mid_cmp = spt_pattern[1].toString();			
			String right_cmp = spt_pattern[2].toString().replace(")", "");
			
			String spt_cmp1[] = left_cmp.split(":");
			String t1 = spt_cmp1[0].toString();
			String t2 = spt_cmp1[1].toString();
			String t3 = spt_cmp1[2].toString();
			
			String t4 = mid_cmp;
			
			String spt_cmp2[] = right_cmp.split(":");
		//	System.out.println("length of :"+spt_cmp2.length);
			String t5 = spt_cmp2[0].toString();			
			String t6 = spt_cmp2[1].toString();
			String t7 = spt_cmp2[2].toString();
		//	System.out.println("tuples:"+t1+"|"+t2+"|"+t3+"|"+t4+"|"+t5+"|"+t6+"|"+t7);
			
			LinkedList get_ll1 = mask.Level1_Mask(t2, t5, t6, t7);
			level1.add(get_ll1);
			LinkedList get_ll2 = mask.Level2_Mask(t2, t5, t6);
			level2.add(get_ll2);
			LinkedList get_ll3 = mask.Level3_Mask(t2, t6);
			level3.add(get_ll3);
			LinkedList get_ll4 = mask.Level4_Mask(t1, t2, t5, t6);
			level4.add(get_ll4);
			LinkedList get_ll5 = mask.Level5_Mask(t2, t4, t6);
			level5.add(get_ll5);		
		}
		}catch(Exception e){
			
		}
	}
	public void load_Tagset() {
		String str = "";
		System.out.println("Inside Load Tagset");
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"./resource/unl/RE/BWA_Prob/PATTERNS/TagSet.txt"));
			while ((str = br.readLine()) != null) {
				pattern.put(str, 1);
			}
		} catch (Exception e) {

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = null;
		FileReader fr = null;
		String temp = null;
		ArrayList al = new ArrayList();
		String output = "";
		String str = "";
		String sent = "", recv = "", token = "", fn = "";
		int temp1 = 0, j = 0;
		Pattern_Match P_M = new Pattern_Match();
		try {
			BufferedReader br = new BufferedReader(new FileReader(
					"./resource/unl/RE/tagDocs/Output/nonemptyfiles.txt"));

			while ((s = br.readLine()) != null) {
				fn = "./resource/unl/RE/tagDocs/Output/" + s; // tag_file = s; //
			//	System.out.println("fn:" + fn);
				StringBuffer docbuff = new StringBuffer();
				StringBuffer docbuff1 = new StringBuffer();
				fr = new FileReader(fn);

				// System.out.println("fr:"+fr); 
				BufferedReader buff = new BufferedReader(new FileReader(fn));
				while ((str = buff.readLine()) != null) {
					docbuff.append(str);
					docbuff1 = P_M.fineTuneDocs(docbuff);
				//	System.out.println("Inside While------->:"+docbuff1.toString());
				}

				// System.out.println("docbuff1:"+docbuff1);
				StringTokenizer sentToken1 = new StringTokenizer(docbuff1
						.toString().trim(), ".", false); // l.loadTaggedData();
															// // j = 0;
				while (sentToken1.hasMoreTokens()) {
					sent = sentToken1.nextToken();
				//	System.out.println("SENT:"+sent);
					P_M.process_TagSet(sent);
				}
				output = "";
			}
			P_M.writeintoFile();
			P_M.writeTags();//*/

			P_M.load_Tagset();
			P_M.Mask_Seed_Tuples();
			System.out.println("size of Reln_tag:"+P_M.reln_tag+"|size is|"+P_M.reln_tag.size());
			P_M.writeintoFile();
			P_M.writeTags();
			// System.out.println("pattern:" + P_M.pattern);
			P_M.patt.process_Docs();
			// System.out.println("GET_CASE:");
			// System.out.println("pattern:" + P_M.pattern);
			// System.out.println("case_pos_Entry:" + cp.case_pos_Entry);
			// System.out.println("trans_freq_Entry:" + cp.trans_freq_Entry);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
