
package org.apache.nutch.analysis.unl.ta;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;
import java.io.FileReader;
import java.util.StringTokenizer;
import java.io.PrintStream;
import java.io.FileOutputStream;

public class BaumWelch {

	ArrayList al = new ArrayList();
	ArrayList prob = new ArrayList();
	ArrayList seed = new ArrayList();
	Hashtable hash = new Hashtable();
	
	PrintStream print_newseed;

	String new_seed = "";
	String seed_case1 = "";
	String seed_case2 = "";
	String seed_pat1 = "";
	String seed_pat2 = "";
	String seed_cp1 = "";
	String seed_cp2 = "";
	String seed_subpat1 = "";
	String seed_subpat2 = "";
	
	
	String unl_relnlabel = "";
	String caseEnd = "";
	String pos = "";
	String sem = "";	
	String subpat1 = "";
	String subpat2 = "";
	String case_pos1 = "";
	String case_pos2 = "";

	String tuple1 = "";
	String tuple2 = "";
	String tuple3 = "";
	String tuple4 = "";
	String tuple5 = "";
	String tuple6 = "";

	String stuple1 = "";
	String stuple2 = "";
	String stuple3 = "";
	String stuple4 = "";
	String stuple5 = "";
	String stuple6 = "";

	double lex1;
	double lex2;
	double semt1;
	double semt2;
	double pat1;
	double pat2;

	double temp1;
	double temp2;
	double temp3;
	double temp4;
	double temp5;

	double itemp1;
	double itemp2;
	double itemp3;
	double itemp4;
	double itemp5;

	double lex_prob1;
	double sem_prob1;
	double lex_prob2;
	double sem_prob2;
	double pat_prob;
	
	double sum_prob;
	double final_prob;

	public void process(String inp_pattern) {
		try {
		//	new_seed = inp_pattern;
			StringTokenizer strToken1 = new StringTokenizer(inp_pattern, "-");
			// while(strToken1.hasMoreTokens()){
			String p1 = strToken1.nextToken();
			p1 = p1.replace("(", "");
			subpat1 = p1;
			StringTokenizer strToken3 = new StringTokenizer(p1, ":");
			String caseEnd1 = strToken3.nextToken();
			String pos1 = strToken3.nextToken();
			String sem1 = strToken3.nextToken();
			caseEnd = caseEnd1;
			tuple1 = caseEnd1;
			tuple2 = pos1;
			tuple3 = sem1;
			case_pos1 = caseEnd1 + ":" + pos1;
			String res_pat1 = read_LexProb(case_pos1);
			lex1 = Double.parseDouble(res_pat1);
			String res_sem1 = read_SemanticsProb(subpat1);
			semt1 = Double.parseDouble(res_sem1);

			String p2 = strToken1.nextToken();
			p2 = p2.replace(")", "");
			subpat2 = p2;
			String pattern = subpat1 + "@" + subpat2;

			StringTokenizer strToken2 = new StringTokenizer(p2, ":");
			String caseEnd2 = strToken2.nextToken();
			String pos2 = strToken2.nextToken();
			String sem2 = strToken2.nextToken();
			caseEnd = caseEnd2;
			tuple4 = caseEnd2;
			tuple5 = pos2;
			tuple6 = sem2;
			case_pos2 = caseEnd2 + ":" + pos2;
			// System.out.println("CASEENDING:"+caseEnd2);
			String res_pat2 = read_LexProb(case_pos2);
			lex2 = Double.parseDouble(res_pat2);
			String res_sem2 = read_SemanticsProb(subpat2);
			semt2 = Double.parseDouble(res_sem2);
			String res_pattern = read_PatternProb(pattern, subpat2);
			pat2 = Double.parseDouble(res_pattern);
			// }
			
		} catch (Exception e) {

		}
	}

	public String read_LexProb(String case_pos) {
		String lex = "";
		try {
			String str = "";
			String fName = "./resource/unl/BWA_Prob/CSV/LexProb.csv";
			FileReader fr = new FileReader(fName);
			BufferedReader buffer = new BufferedReader(fr);
			while ((str = buffer.readLine()) != null) {

				StringTokenizer strToken1 = new StringTokenizer(str, "$");
				while (strToken1.hasMoreTokens()) {
					String t1 = strToken1.nextToken();
					String t2 = strToken1.nextToken();
					String t3 = strToken1.nextToken();
					// System.out.println("T3:" + t3);
					if (t1.startsWith(caseEnd)) {
						if (t2.startsWith(case_pos)) {
					//		System.out.println("T2:" + t2 + ":" + t3);
							lex = t3;
						}
					}
				}
			}
		} catch (Exception e) {

		}
		return lex;
	}

	public String read_SemanticsProb(String sub_pattern) {
		String sem = "";
		try {
			String str = "";
			String fName = "./resource/unl/BWA_Prob/CSV/RelnProb.csv";
			FileReader fr = new FileReader(fName);
			BufferedReader buffer = new BufferedReader(fr);
			while ((str = buffer.readLine()) != null) {

				StringTokenizer strToken1 = new StringTokenizer(str, "$");
				while (strToken1.hasMoreTokens()) {
					String t1 = strToken1.nextToken();
					String t2 = strToken1.nextToken();
					String t3 = strToken1.nextToken();
					// System.out.println("T3:" + t3);
					if (t2.startsWith(sub_pattern)) {
				//		System.out.println("T######3 " + t3);
						sem = t3;
					}
				}
			}
		} catch (Exception e) {

		}
		return sem;
	}

	public String read_PatternProb(String pattern, String sub_pattern) {
		String inp_pat = "";
		try {
			String str = "";
			String fName = "./resource/unl/BWA_Prob/CSV/Pattern_Prob.csv";
			FileReader fr = new FileReader(fName);
			BufferedReader buffer = new BufferedReader(fr);
			while ((str = buffer.readLine()) != null) {

				StringTokenizer strToken1 = new StringTokenizer(str, "$");
				while (strToken1.hasMoreTokens()) {
					String t1 = strToken1.nextToken();
					String t2 = strToken1.nextToken();
					String t3 = strToken1.nextToken();
					// System.out.println("T3:" + t3);
					if (t1.startsWith(sub_pattern)) {
						if (t2.startsWith(pattern)) {
				//			System.out.println("get T3:" + t3);
							inp_pat = t3;
						}
					}
				}
			}
		} catch (Exception e) {

		}
		return inp_pat;
	}

	public void read_UNL_Ontology() {
		try {
			String str = "";
			BufferedReader buffer = new BufferedReader(new FileReader(
					"./resource/unl/BWA_Prob/UNL Ontology/semantic.txt"));
			while ((str = buffer.readLine()) != null) {
				StringTokenizer strToken5 = new StringTokenizer(str, "-");
				String key = strToken5.nextToken();
				String value = strToken5.nextToken();
				hash.put(key.trim(), value);
			}
		} catch (Exception e) {

		}
	}
	public void process_Seed() {
		try {
			int j = 0;
			for(int i = 0; i<seed.size(); i++){
				String input = seed.get(i).toString();
			//	System.out.println("Input:"+i+"--------->"+input);
				StringTokenizer strToken1 = new StringTokenizer(input.trim(), "-");
				// while(strToken1.hasMoreTokens()){
				String p1 = strToken1.nextToken();
				p1 = p1.replace("(", "");
				seed_subpat1 = p1;
			//	System.out.println("Seed_sub pattern1:----------->"+seed_subpat1);
				StringTokenizer strToken3 = new StringTokenizer(p1, ":");
				String caseEnd1 = strToken3.nextToken();
				String pos1 = strToken3.nextToken();
				String sem1 = strToken3.nextToken();
				seed_case1 = caseEnd1;
				caseEnd = caseEnd1;
				stuple1 = caseEnd1;
				stuple2 = pos1;
				stuple3 = sem1;
				seed_cp1 = caseEnd1 + ":" + pos1;
			//	System.out.println("Seed_Tuples of pattern1:----------->"+stuple1+":"+stuple2+":"+stuple3);
				String res_pat1 = read_LexProb(seed_cp1);
				lex1 = Double.parseDouble(res_pat1);
				String res_sem1 = read_SemanticsProb(seed_subpat1);
				semt1 = Double.parseDouble(res_sem1);
			//	System.out.println("Probability1:"+lex1+":"+semt1);
				String p2 = strToken1.nextToken();
				p2 = p2.replace(")", "");
				seed_subpat2 = p2;
				String pattern = seed_subpat1 + "@" + seed_subpat2;

				StringTokenizer strToken2 = new StringTokenizer(p2, ":");
				String caseEnd2 = strToken2.nextToken();
				String pos2 = strToken2.nextToken();
				String sem2 = strToken2.nextToken();
				seed_case2 = caseEnd2;
				caseEnd = caseEnd2;
				stuple4 = caseEnd2;
				stuple5 = pos2;
				stuple6 = sem2;
				seed_cp2 = caseEnd2 + ":" + pos2;
				// System.out.println("CASEENDING:"+caseEnd2);
				String res_pat2 = read_LexProb(seed_cp2);
				lex2 = Double.parseDouble(res_pat2);
				String res_sem2 = read_SemanticsProb(seed_subpat2);
				semt2 = Double.parseDouble(res_sem2);
				String res_pattern = read_PatternProb(pattern, seed_subpat2);
				pat2 = Double.parseDouble(res_pattern);
			//	System.out.println("Probability2:"+lex2+":"+semt2+":"+pat2);
			//	System.out.println("Before Similarity");
				compute_Similarity();
				j = i;
			//	System.out.println("J:"+j+seed.size());
				/*if(j==seed.size()){
					System.out.println("SEED INSIDE:----------" + i);
					i=0;
				}*/
			}
		} catch (Exception e) {

		}
	}
	public void compute_Similarity(){
	//	System.out.println("Inside Similarity");
		itemp1 = lex1;
		itemp2 = semt1;
		itemp3 = lex2;
		itemp4 = semt2;
		itemp5 = pat2;
		if ( (tuple4.equals(stuple4)) && (tuple2.equals(stuple2)) ){
	//		System.out.println("STUPLE:--------->" + "=====" + stuple4 + ">>>>>>" + tuple4);
			if (itemp1 > temp1) {
				lex_prob1 = temp1 / itemp1;
			} else if(itemp1 < temp1){
				lex_prob1 = itemp1 / temp1;
			}else{
				lex_prob1 = itemp1 / temp1;
			}
			if (itemp2 > temp2) {
				sem_prob1 = temp2 / itemp2;
			} else if (itemp2 < temp2) {
				sem_prob1 = itemp2 / temp2;
			}else{
				sem_prob1 = itemp2 / temp2;
			}
			if (itemp3 > temp3) {
				lex_prob2 = temp3 / itemp3;
			} else if (itemp3 < temp3){
				lex_prob2 = itemp3 / temp3;
			}else{
				lex_prob2 = itemp3 / temp3;
			}
			if (itemp4 > temp4) {
				sem_prob2 = temp4 / itemp4;
			} else if (itemp4 < temp4){
				sem_prob2 = itemp4 / temp4;
			}else{
				sem_prob2 = itemp4 / temp4;
			}
			double sum = lex_prob1+sem_prob1+lex_prob2+sem_prob2;
			sum_prob = sum/4;
			
		//	System.out.println("Probability:" + lex_prob1 + ":" + sem_prob1
		//			+ ":" + lex_prob2 + ":" + sem_prob2);
			lex_prob1 = 0.0;
			lex_prob2 = 0.0;
			sem_prob1 = 0.0;
			sem_prob2 = 0.0;
			
			compute_semanticSimilarity();
		}
	}
	
	public void read_seedPatterns() {
		try {
			String str = "";
			BufferedReader buffer = new BufferedReader(new FileReader(
					"./resource/unl/BWA_Prob/PATTERNS/seed-patterns.txt"));
			while ((str = buffer.readLine()) != null) {
				StringTokenizer strToken4 = new StringTokenizer(str, "\t");
				String reln = strToken4.nextToken();
				unl_relnlabel = reln;
		//		System.out.println("UNL Relation:" + reln);
				String patt = strToken4.nextToken();
				seed.add(patt);
				process_Seed();			
			}
			
		} catch (Exception e) {

		}
	}

	public void read_Input() {
		try {
			print_newseed = new PrintStream(new FileOutputStream("./resource/unl/BWA_Prob/PATTERNS/newseed.txt")); 
			String str = "";
			BufferedReader buffer = new BufferedReader(new FileReader(
					"./resource/unl/BWA_Prob/PATTERNS/Input_Patterns.txt"));
			while ((str = buffer.readLine()) != null) {
				new_seed = str;
		//		System.out.println("INPUT STRING ----------->"+str);
				process(str);
				
				temp1 = lex1;
				temp2 = semt1;
				temp3 = lex2;
				temp4 = semt2;
				
			//	System.out.println("T-----E-----M--------P:"+temp1 + ":" + temp2 + ":" + temp3 + ":" + temp4);
			//	System.out.println("Process================>");
				read_seedPatterns();				
				
			}
		} catch (Exception e) {

		}
	}
	public void compute_semanticSimilarity(){
		try{
		//	System.out.println("Inside Semantic Similarity"+hash + ":"+tuple6);
			if(hash.containsKey(tuple6.trim())){
		//		System.out.println("TUPLES:"+tuple6);
				if(itemp5 > sum_prob){
					final_prob = sum_prob/itemp5;
				}else if(itemp5 < sum_prob){
					final_prob = itemp5/sum_prob;
				}else{
					final_prob = itemp5/sum_prob;
				}
				
				String result = unl_relnlabel + "\t" + new_seed + "\t" + final_prob;
			//	System.out.println("RESULT========>>>:"+result);
			}
			if(seed.isEmpty()){
				seed.add(new_seed);				
				print_newseed.println(new_seed);
			}else if(!(seed.contains(new_seed)) ){
				seed.add(new_seed);
				print_newseed.println(new_seed);
			}
			
		}catch(Exception e){
			
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BaumWelch bw = new BaumWelch();
		// String input = "";
		bw.read_UNL_Ontology();
		bw.read_Input();
	//	boot.read_seedPatterns();
		

	}

}
