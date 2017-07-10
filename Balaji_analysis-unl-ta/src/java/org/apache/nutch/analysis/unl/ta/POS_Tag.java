package org.apache.nutch.analysis.unl.ta;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Scanner;
import java.util.Collections;
import java.io.FileReader;
import java.util.Hashtable;

import org.apache.nutch.analysis.unl.ta.Analyser;
import org.apache.nutch.unl.UNL;
import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;

public class POS_Tag {
	
	Acqusition_of_Patterns aop = new Acqusition_of_Patterns();
	UNL_Attributes unl_attr = new UNL_Attributes();
	Check chk = new Check();

	ArrayList<String> al = new ArrayList<String>();
	ArrayList<String> anal;
	ArrayList<String> root;
	ArrayList<String> morph;
	ArrayList<String> delimiter;
	ArrayList<String> multiword;
	ArrayList<String> get_result;
	ArrayList word_list;
	ArrayList pos_list;

	Hashtable MW_hash = new Hashtable();
	Hashtable tam_head = new Hashtable();
	Hashtable head_entry = new Hashtable();
	int count = 0;
	int cnt1 = 0;
	
	String sent_tag;
	String word_tag;
	String final_res;
	String result;
	String get_tag;
	String delimit;
	String m_word;
	String m_pos;
	String prev_pos;
	String prev_word;
	String a_pos;
	String prev;
	String a_word;
	String a_prev;
	

	int counter;
	
	PrintStream write_MW;
	PrintStream final_MW;

	public void init() {

		delimiter = new ArrayList<String>();
		multiword = new ArrayList<String>();
		anal = new ArrayList<String>();
		get_result = new ArrayList<String>();
		word_list = new ArrayList();
		pos_list = new ArrayList();
		
		get_tag = "";
		final_res = "";
		result = "";
		delimit = "";
		m_word = "";
		m_pos = "";
		prev_pos = "";
		prev_word = "";
		a_pos = "";
		prev = "";
		a_word = "";
		a_prev = "";

		counter = 0;

		sent_tag = "<s>";
		word_tag = "<w>";
	}

	public String process_Docs(String st) {
		try {
		//	System.out.println("Sentence:"+st);
			write_MW = new PrintStream(new FileOutputStream(new File("./resource/unl/ta/Multiwords.txt"), true));
			final_MW = new PrintStream(new FileOutputStream(new File("./resource/unl/ta/MWE_UNL.txt"), true));
			init();
			String sent = "";
			StringTokenizer strToken2 = new StringTokenizer(st, " :,)(/\"&", true);
			while (strToken2.hasMoreTokens()) {
				String word = strToken2.nextToken();
				if (word.equals(" ")) {
					delimit = "<SPACE>";
					delimiter.add(delimit);
				} else if (word.equals(",")) {
					delimit = "<COMMA>";
					delimiter.add(delimit);
				} else if (word.equals(":")) {
					delimit = "<COLON>";
					delimiter.add(delimit);
				} else if (word.equals("/")) {
					delimit = "<FSLASH>";
					delimiter.add(delimit);
				} else {
					String analysed = org.apache.nutch.analysis.unl.ta.Analyser
							.analyseF(word, true);
					// System.out.println("Analysed:" + analysed);
					anal.add(analysed);
					StringTokenizer strToken3 = new StringTokenizer(analysed,
							">");
					int count = strToken3.countTokens();
					ArrayList<String> store_Tokens = new ArrayList<String>();
					while (strToken3.hasMoreTokens()) {
						String get_token = strToken3.nextToken();
						// System.out.println("get:"+get_token);
						store_Tokens.add(get_token);
					}
					get_tag = tag_POSFeatures(count, word, store_Tokens);
					delimiter.add(get_tag);
					// sent += get_tag + " ";
				}
			}
		//	aop.pattern_Acqusition(word_list,pos_list);
		//	get_result = unl_attr.process_Analysed_Features(anal);
			// find_Multiwords();
			// System.out.println("Output:"+multiword);
			for (int i = 0; i < delimiter.size(); i++) {
				String get_val = delimiter.get(i).toString();
				if (get_val.equals("<SPACE>")) {
					get_val = " ";
				} else {
					sent += get_val + " ";
				}

			}
			result = sent_tag + " ";
			result += sent;
			result += "<#s>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("RESULT:"+result);
		return result;
	}
	
	

	public String tag_POSFeatures(int count, String word,
			ArrayList<String> store_Tokens) {
		try {
			String tag = "";
			root = new ArrayList<String>();
			morph = new ArrayList<String>();
			// System.out.println("COUNT:" + count);
			int i = 0;
			// System.out.println("WORD::----------->" + word);
			// String tag_word = sent_tag +" "+ word;
			while (i < count) {
				String get_word = store_Tokens.get(i).toString();

				if ((get_word.contains("<")) && (get_word.contains("&"))) {
					StringTokenizer strToken4 = new StringTokenizer(get_word,
							"<&");
					// while(strToken4.hasMoreTokens()){
					String first_word = strToken4.nextToken("<");
					String next_word = strToken4.nextToken("&");
					first_word = first_word.replace(":", "");
					next_word = next_word.replace("<", "");
					root.add(first_word.trim());
					morph.add(next_word.trim());
					// System.out.println("FWORD::----------->"+first_word);
					// System.out.println("NEXTWORD::----------->"+next_word);
					// }
				}
				i++;
			}
			String get_morph = morph.get(0).toString();
			if ((get_morph.equals("Demonstrative Adjective"))
					&& (morph.get(1).toString().equals("Sandhi"))
					&& (morph.size() > 2)) {
				Collections.swap(root, 0, 2);
				Collections.swap(morph, 0, 2);
			} else if (!(get_morph.equals("Noun"))
					&& !(get_morph.equals("Entity"))
					&& !(get_morph.equals("Verb"))
					&& !(get_morph.equals("Adjective"))
					&& !(get_morph.equals("Adjectival Noun"))
					&& !(get_morph.equals("Adverb"))
					&& !(get_morph.equals("Numbers"))
					&& !(get_morph.equals("Particle"))
					&& !(get_morph.equals("Intensifier"))
					&& !(get_morph.equals("Postposition"))
					&& !(get_morph.equals("Pronoun"))
					&& !(get_morph.equals("Interrogative Noun"))
					&& !(get_morph.equals("Interrogative Adjective"))
					&& !(get_morph.equals("Finite Verb"))
					&& !(get_morph.equals("Negative Finite Verb"))
					&& !(get_morph.equals("Interjection"))
					&& !(get_morph.equals("Enpavar"))
					&& !(get_morph.equals("Enpathu"))
					&& !(get_morph.equals("Date Time"))
					&& !(get_morph.equals("Conjunction"))
					&& !(get_morph.equals("Non Tamil Noun"))) {
				root.remove(0);
				root.add(0, word);
				morph.remove(0);
				morph.add(0, "Unknown");
			}
		//	System.out.println("root:" + root + "----->" + root.size());
		//	System.out.println("morph:" + morph + "----->" + morph.size());
			if (m_word.isEmpty()) {
			//	System.out
			//			.println("INSIDE M_WORD NULL *************************");
				pos_Disambiguation();
			} else {
			//	System.out.println("INSIDE ELSE :" + m_word + "======="
			//			+ prev_pos);
				pos_Disambiguation();
			}
			String inp_word = "";
			String inp_pos = "";
			String tag_word = "";
			String rword = root.get(0).toString().trim();
			String NE_word = morph.get(0).toString().trim();
			ArrayList temp_word = new ArrayList();
			ArrayList temp_pos = new ArrayList();
			ArrayList t_w = new ArrayList();
			ArrayList t_p = new ArrayList();
			if (root.size() == 1) {
				// System.out.println("INSIDE ROOT SIZE: 1");
				if ((NE_word.equals("Entity")) || (NE_word.equals("Noun"))) {
				//	 System.out.println("<-----------> INSIDE IF NEWORD ENTITY NOUN <-------->:");
					// System.out.println("NE WORD:" + NE_word);
					if (m_word.isEmpty()) {
					//	System.out.println("MWORD NULL:");
						m_word += rword + " ";
						m_pos += NE_word + " ";
						prev_pos = NE_word;
						prev_word = rword;
						// System.out.println("MWORD NULL:" + m_word);
						// System.out.println("MWORD and PREV_POS:------------>"+
						// m_word + "<------>" + prev_pos);
					} else {
					//	System.out.println("INSIDE MWORD NOT NULL");
						if (!(m_word.contains(rword))) {
						//	System.out.println("INSIDE NOT MWORD ====== RWORD <-------->:");
							if (NE_word.equals("Entity")) {
								if (!(prev_pos.equals("Noun"))) {
								//	System.out.println("IF PREV_POS INSIDE ENTITY NOT NULL -------->:"+ prev_pos);
									m_word += rword + " ";
									m_pos += NE_word + " ";
									// System.out.println("MWORD  NOT NULL INSIDE ENTITY NOT NOUN ---------->:"+
									// m_word);
								} else {
								//	System.out.println("ELSE PREV_POS INSIDE ENTITY NOT NULL -------->:"+ prev_pos);
									System.out.println(m_word);
									remove_Duplicates(m_word);
								//	write_MW.println(m_word);
									
									tag_word = word_tag + m_word + "," + m_word
											+ "," + m_pos + "</w>";
									temp_word.add(m_word);
									temp_pos.add(m_pos);
									word_list.add(temp_word);
									pos_list.add(temp_pos);
									inp_word = m_word;
									inp_pos = prev_pos;
									// System.out.println("ELSE TAG_WORD INSIDE ENTITY NOT NULL -------->:"+
									// tag_word);
									m_word = "";
									m_pos = "";
							//		temp_word.clear();
							//		temp_pos.clear();
									m_word += rword + " ";
									m_pos += NE_word + " ";
									prev_pos = NE_word;
									// System.out.println("ELSE M_WORD INSIDE ENTITY NOT NULL -------->:"+
									// m_word);
								}

							} else if (NE_word.equals("Noun")) {
							//	System.out.println("IF PREV_POS INSIDE NOUN NOT NULL -------->:"+ prev_pos);
								// counter++;
								m_word += rword + " ";
								m_pos += NE_word + " ";
								// System.out.println("MWORD INSIDE NOUN NOT NULL----------->:"+
								// m_word);
								// if(counter == 2){
								System.out.println(m_word);
							//	write_MW.println(m_word);
								remove_Duplicates(m_word);
								tag_word = word_tag + m_word + "," + m_word;
							//	inp_word = m_word;
								temp_word.add(m_word);
								temp_pos.add(m_pos);
								word_list.add(temp_word);
								pos_list.add(temp_pos);
							//	temp_word.clear();
							//	temp_pos.clear();
								// System.out.println("TAGWORD INSIDE NOUN NOT NULL------------>:"
								// + tag_word);
								m_word = "";
								m_pos = "";
								prev_word = "";
								prev_pos = "";
								// counter = 0;
								// }

							} else {
							//	System.out.println("INSIDE OTHERS >>>>>>>>>-------->>>>>>>>>"+ m_word);
								System.out.println(m_word);
							//	write_MW.println(m_word);
								remove_Duplicates(m_word);
								tag_word = word_tag + m_word + "," + m_word;
								//inp_word = m_word;
								temp_word.add(m_word);
								temp_pos.add(m_pos);
								word_list.add(temp_word);
								pos_list.add(temp_pos);
							//	temp_word.clear();
							//	temp_pos.clear();
								// System.out.println("ELSE OTHERS INSIDE NOT NULL -------->:"
								// + tag_word);
								m_word = "";
								m_pos = "";
							}
							// tag_word = word_tag + m_word + "," + m_word;
						} else {
					//		System.out.println("INSIDE ELSE MWORD ====== RWORD <-------->:");
							System.out.println(m_word);
						//	write_MW.println(m_word);
							remove_Duplicates(m_word);
							tag_word = word_tag + m_word + "," + m_word;
						//	inp_word = m_word;
							temp_word.add(m_word);
							temp_pos.add(m_pos);
							word_list.add(temp_word);
							pos_list.add(temp_pos);
						//	temp_word.clear();
						//	temp_pos.clear();
							// System.out.println("INSIDE ELSE MWORD ====== RWORD TAGWORD<-------->:"+
							// tag_word);
							m_word = "";
							m_pos = "";
							m_word += rword + " ";
							m_pos += NE_word + " ";
							// System.out.println("INSIDE ELSE MWORD ====== RWORD M_WORD<-------->:"+
							// m_word);
						}
						// tag_word = word_tag + m_word + "," + m_word;
						// m_word = "";
					}
				} else {
				//	System.out.println("<-----------> INSIDE ELSE NEWORD ENTITY NOUN <-------->:");
					String new_tag = word_tag + prev_word + "," + prev_word
							+ "," + m_pos + "</w>";
					tag_word = new_tag + " " + word_tag + word.trim() + ","
							+ rword.trim();
					inp_word = prev_word;
					temp_word.add(m_word);
					temp_pos.add(m_pos);
					word_list.add(temp_word);
					pos_list.add(temp_pos);
				//	temp_word.clear();
				//	temp_pos.clear();
					
					t_w.add(word);
					t_p.add(NE_word);
					word_list.add(t_w);
					pos_list.add(t_p);
				//	t_w.clear();
				//	t_p.clear();
				//	inp_pos = prev_pos;
					// System.out.println("<-----------> INSIDE IF NEWORD ENTITY NOUN TAG_WORD <-------->:"+
					// tag_word);
					m_word = "";
					m_pos = "";
					new_tag = "";
					prev_word = "";
					prev_pos = "";
				}
			} else {
			//	System.out.println("<-----------> INSIDE ELSE ROOT SIZE <-------->:");
				if ((NE_word.equals("Entity")) || (NE_word.equals("Noun"))) {
				//	System.out.println("<-----------> INSIDE ELSE ROOT SIZE ENTITY NOUN<-------->:"+ root);
					if (!(m_word.isEmpty())) {
					//	 System.out.println("<-----------> INSIDE ELSE ROOT SIZE  MWORD NOT ISEMPTY<-------->:"+ m_word);
						m_word += rword + " ";
						m_pos += NE_word + " ";
					//	System.out.println("<-----------> INSIDE ELSE ROOT SIZE M-----WORD <-------->:"+ m_word);
						System.out.println(m_word);
					//	write_MW.println(m_word);
						remove_Duplicates(m_word);
						tag_word = word_tag + m_word + "," + m_word;
						inp_word = prev_word;
						m_word += root.subList(1, root.size());
						m_pos += morph.subList(1, morph.size());
					//	inp_pos = prev_pos;
						temp_word.add(m_word);
						temp_pos.add(m_pos);
						word_list.add(temp_word);
						pos_list.add(temp_pos);
					//	word_list.add(root);
					//	pos_list.add(morph);
					//	temp_word.clear();
					//	temp_pos.clear();
						// System.out.println("<-----------> INSIDE ELSE ROOT SIZE <-------->TAG_WORD:"+
						// tag_word);
						m_word = "";
						m_pos = "";
						// new_tag = "";
						prev_word = "";
						prev_pos = "";
					} else {
					//    System.out.println("<-----------> INSIDE ELSE ROOT SIZE MWORD NULL<-------->:"+ root);
						tag_word = word_tag + word.trim() + "," + rword.trim();
						word_list.add(root);
						pos_list.add(morph);
						// System.out.println("<-----------> INSIDE ELSE ROOT SIZE MWORD NULL TAG_WORD <-------->:"+
						// tag_word);
					}
				} else {
					String new_tag = word_tag + prev_word + "," + prev_word
							+ "," + m_pos + "</w>";
				//	System.out.println("<-----------> INSIDE ELSE ROOT SIZE ENTITY NOUN IFFFFFF<-------->:");
					tag_word = new_tag + " " + word_tag + word.trim() + ","
							+ rword.trim();
					inp_word = prev_word;
					inp_pos = prev_pos;
					temp_word.add(m_word);
					temp_pos.add(m_pos);
					word_list.add(temp_word);
					pos_list.add(temp_pos);
					word_list.add(root);
					pos_list.add(morph);
				//	temp_word.clear();
				//	temp_pos.clear();
					// System.out.println("<-----------> INSIDE ELSE ROOT SIZE ENTITY NOUN IFFFFFFFF<-------->:"+
					// tag_word);
					new_tag = "";
					prev_word = "";
					prev_pos = "";
					m_word = "";
					m_pos = "";
				}
			}
			// System.out.println("TAG_WORD:-------->" + tag_word);
			if (tag_word != null) {
				if (!(tag_word.endsWith("</w>"))) {
					for (int j = 0; j < morph.size(); j++) {
						tag += "," + morph.get(j).toString();
					}				
					final_res = tag_word + tag + "</w>";
				} else {
					final_res = tag_word;
					morph.clear();
					// root.clear();
				}
			} else {
				final_res = "";
				tag = "";
			}
			if (final_res.startsWith(",")) {
				final_res = "";
			}
			if (final_res.contains("<w>,,</w>")) {
				final_res = final_res.replace("<w>,,</w>", "");
			}

			tag_word = " ";
	//		System.out.println("FINAL RESULT:" + final_res);
			//System.out.println(word_list);
			//System.out.println(pos_list);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return final_res;
	}

	public void pos_Disambiguation() {
		if ((morph.contains("Verb")) && (morph.contains("Noun"))
				&& (morph.contains("Entity"))) {
		//	System.out.println("VERB<----->NOUN<----->ENTITY");
			if (morph.contains("Sandhi")) {
				chk.sandhi_Check();
			} else {
				chk.context_Check_Noun();
			}
		} else if ((morph.contains("Adjective")) && (morph.contains("Verb"))
				&& (morph.contains("Noun"))) {
		//	System.out.println("VERB<----->NOUN<----->ADJECTIVE");
			if (morph.contains("Sandhi")) {
				chk.sandhi_Check();
			}else{
				chk.context_Check_Adjective();
			}
		} else if ((morph.contains("Particle")) && (morph.contains("Postposition"))
				&& (morph.contains("Verb"))
				&& (morph.contains("Noun"))) {
		//	System.out
		//			.println("VERB<----->NOUN<----->PARTICLE<----->POSTPOSITION");
			
		} else if ((morph.contains("Particle")) && (morph.contains("Verb"))
				&& (morph.contains("Noun"))) {
		//	System.out.println("PARTICLE<----->VERB<----->NOUN");
			if (morph.contains("Sandhi")) {
				chk.sandhi_Check();
			} else {

			}
		} else if ((morph.contains("Verb")) && (morph.contains("Noun"))) {
		//	System.out.println("VERB<----->NOUN");
			if (morph.contains("Sandhi")) {
				chk.sandhi_Check();
			} else {
				chk.context_Check_Noun();
			}
		} else if ((morph.contains("Particle"))
				&& (morph.contains("Postposition")) && (morph.contains("Noun"))) {
		//	System.out.println("PARTICLE<----->POSTPOSITION<----->NOUN");
			if (morph.contains("Sandhi")) {
				chk.sandhi_Check();
			} else if (root.contains("முதல்")) {
				morph.clear();
				morph.add("Postposition");
			}
		} else if ((morph.contains("Adjective"))
				&& (morph.contains("Intensifier"))
				&& (morph.contains("Adverb"))) {
		//	System.out.println("ADJECTIVE<----->INTENSIFIER<----->ADVERB");
		} else if ((morph.contains("Adjective"))
				&& (morph.contains("Postposition"))) {
		//	System.out.println("ADJECTIVE<----->POSTPOSITION");
		} else if ((morph.contains("Conjunction"))
				&& (morph.contains("Adverb"))) {
		//	System.out.println("CONJUNCTION<----->ADVERB");
			if(root.contains("என்று")){
				morph.clear();
				morph.add("Conjunction");
			}
		} else if ((morph.contains("Postposition"))
				&& (morph.contains("Adverb"))) {
		//	System.out.println("POSTPOSITION<----->ADVERB");
			
		} else if ((morph.contains("Adjective")) && (morph.contains("Adverb"))) {
		//	System.out.println("ADJECTIVE<----->ADVERB");
			
		} else if ((morph.contains("Adjective")) && (morph.contains("Verb"))) {
		//	System.out.println("ADJECTIVE<----->VERB");
			
		} else if ((morph.contains("Postposition")) || (morph.contains("Verb"))) {
		//	System.out.println("POSTPOSITION<----->VERB");
			if (morph.size() == 2) {
				morph.clear();
				morph.add("Postposition");
			}
		}
	}

	class Check {
		public void sandhi_Check() {
			// if(morph.contains("Sandhi")){
			int cnt = Collections.frequency(morph, "Sandhi");
		//	System.out.println("frequency:------------------->" + cnt);
			morph.clear();
			morph.add("Noun");
			morph.add("Sandhi");
			// }
		}
		public void context_Check_Noun() {
			if (!(prev_pos.isEmpty())) {
				if ((prev_pos.contains("Noun"))
						|| (prev_pos.contains("Entity"))) {
					morph.clear();
					morph.add("Noun");
				}
			} else {
				morph.clear();
				morph.add("Noun");
			}
		}
		
		public void context_Check_Adjective(){
			if(prev_pos.isEmpty()){
				morph.clear();
				morph.add("Adjective");
			}else{
				
			}
		}
		public void context_Check_Number(){
			if(prev_pos.contains("Numbers")){
				morph.clear();
				morph.add("Noun");
			}
		}
	}
	
	public void remove_Duplicates(String key){
		try{
			if(MW_hash.isEmpty()){
				int cnt = 1;
				MW_hash.put(key, cnt);
			}else if(MW_hash.containsKey(key)){
				int get_value = Integer.parseInt(MW_hash.get(key).toString());
				get_value++;
				MW_hash.remove(key);
				MW_hash.put(key, get_value);
			}else{
				int cnt1 = 1;
				MW_hash.put(key, cnt1);
			}
		}catch(Exception e){
			
		}
	}
	
	public void readFiles1() {
		try {
			File fn = new File(
					"./resource/unl/ta/SentenceExtraction/Input/content/");
			File[] list = fn.listFiles();
			for (int i = 0; i < list.length; i++) {
				String filename = list[i].getName();
				al.add(filename);
			}
			// System.out.println("Files:"+al+al.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void readFiles(){
		try{
			String file = "";
			BufferedReader buff = new BufferedReader(new FileReader("./resource/unl/ta/SentenceExtraction/Input/nonemptyfiles.txt"));
			while((file=buff.readLine())!=null){
				al.add(file);
			}
			buff.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void writeintofile(String fname, String tagresult) {
		try {
			Writer output = null;
			File fn = new File("./resource/unl/POSTAG/content/" + fname);
			fn.createNewFile();
			output = new BufferedWriter(new FileWriter(fn, false));
			output.write(tagresult);
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void process_Files() {
		try {
			String tag_result = "";
			readFiles();
			for (int i = 0; i < al.size(); i++) {
				String get_file = al.get(i).toString();
				System.out.println(get_file);
				String fn = "./resource/unl/ta/SentenceExtraction/Input/"
						+ get_file;
				// System.out.println("File Process:"+fn);
				// File fr = new File(fn);
				Scanner scanner = new Scanner(new File(fn)).useDelimiter("\\Z");
				String contents = scanner.next();
			//	String alphaAndDigits = contents.replaceAll("[^a-zA-Z0-9]+"," ");

			//	System.out.println("symbols:"+alphaAndDigits);
				if(contents.isEmpty()){
					
				}else{
				StringTokenizer strToken1 = new StringTokenizer(contents, ".");
				while (strToken1.hasMoreTokens()) {
					String sent = strToken1.nextToken();
				//	tag_result += process_Docs(sent) + "\n";
				}
				}
				// System.out.println("RESULT:"+tag_result);
				scanner.close();
			//	writeintofile(get_file, tag_result);
				tag_result = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void process_docs() {
		try {
			String str = "";
			String tag_result = "";
			readFiles();
			for (int i = 0; i < al.size(); i++) {
				String get_file = al.get(i).toString();
				System.out.println(get_file);
				String fn = "./resource/unl/ta/SentenceExtraction/Input/"
						+ get_file;
				// System.out.println("File Process:"+fn);
				// File fr = new File(fn);
			//	Scanner scanner = new Scanner(new File(fn)).useDelimiter("\\Z");
			//	String contents = scanner.next();
				BufferedReader buffer = new BufferedReader(new FileReader(fn));
				while((str=buffer.readLine())!=null){
					//String alphaDigits = str.replaceAll("[^a-zA-Z0-9]+","");
					if( (str.charAt(0)>=2949) && (str.charAt(0)<=2997) ){ 
				//	System.out.println("string:"+str);
						StringTokenizer strToken1 = new StringTokenizer(str, ".");
						while (strToken1.hasMoreTokens()) {
							String sent = strToken1.nextToken();
							tag_result += process_Docs(sent) + "\n";
						}
			//		System.out.println("symbols:"+alphaDigits);
					}else{
					
					}
				}
				buffer.close();
			//	String alphaAndDigits = contents.replaceAll("[^a-zA-Z0-9]+"," ");

			//	System.out.println("symbols:"+alphaAndDigits);				
			// System.out.println("RESULT:"+tag_result);
			//	scanner.close();
			//	writeintofile(get_file, tag_result);
				tag_result = "";
			}
		//	System.out.println(MW_hash.keySet());
			get_UNLConstraints();
			String final_MW = MW_hash.keySet().toString();
			final_MW = final_MW.replaceAll(",", "\n");
			write_MW.println(final_MW);
			write_MW.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void get_UNLConstraints(){
		try{
			get_UNL();
			for(Object key:MW_hash.keySet()){
				String get_key = (String) key;
				String[] tokens = get_key.split(" ");
				String concat = "";
				int cnt = tokens.length;
				int i = 0;
				//int j = cnt -2;
				while(i<cnt){
					String get_remain = tokens[i].toString().trim();
					if(head_entry.containsKey(get_remain)){
						concat += head_entry.get(get_remain).toString().trim()+" ";
					}else{
						concat += "XXXXX"+" ";
					}
					i++;
				}
			//	if(cnt>1){
					String first_word = tokens[0].toString().trim();
					String last_word = tokens[cnt-1].toString().trim();
					if(tam_head.containsKey(last_word)){
						String get_constraint = tam_head.get(last_word).toString();
						final_MW.println(get_key.trim()+"/"+concat.trim()+"/"+get_constraint.trim()+"/"+cnt+"/"+first_word.trim());
					}
			//	}	
			}
		}catch(Exception e){
			
		}
		final_MW.close();
	}
	public void get_UNL(){
		try{	
			//String value = "1";		
				String uwentry = "";
				BufferedReader buffer = new BufferedReader(new FileReader("./resource/unl/uwdict.txt"));
				while ((uwentry = buffer.readLine()) != null) {
				//	uw.add(uwentry);
					StringTokenizer strTok1 = new StringTokenizer(uwentry,"/");
					cnt1 = strTok1.countTokens();
					//	System.out.println(cnt1);
					String t_word = strTok1.nextToken();
					String h_word = strTok1.nextToken();
					String u_word = strTok1.nextToken(); 			
						count++;
						String t_W_h = h_word+"/"+u_word;
						if(tam_head.isEmpty()){
							tam_head.put(t_word, u_word);
						//	head_entry.put(t_word,h_word);
							//System.out.println(t_W_h+"/"+u_word);
							//ps3.println(t_W_h+"/"+u_word);
						}else if(tam_head.containsKey(t_word)){
						//	System.out.println(t_W_h+"/"+u_word);
							//ps4.println(t_W_h+"/"+u_word);
						}else{
							tam_head.put(t_word, u_word);	
							//head_entry.put(t_word,h_word);
						}	
						
						if(head_entry.isEmpty()){
						//	tam_head.put(t_word, t_W_h);
							head_entry.put(t_word,h_word);
							//System.out.println(t_W_h+"/"+u_word);
							//ps3.println(t_W_h+"/"+u_word);
						}else if(head_entry.containsKey(t_word)){
						//	System.out.println(t_W_h+"/"+u_word);
							//ps4.println(t_W_h+"/"+u_word);
						}else{
							//tam_head.put(t_word, t_W_h);	
							head_entry.put(t_word,h_word);
						}					
					}	
				buffer.close();
		}catch(Exception e){			
		}	
	}	
	public static void main(String args[]) {
		POS_Tag pos_tag = new POS_Tag();
	//	pos_tag.process_Files();
		pos_tag.process_docs();
	}
}
