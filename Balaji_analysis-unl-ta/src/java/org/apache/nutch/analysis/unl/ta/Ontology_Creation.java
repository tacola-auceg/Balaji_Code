package org.apache.nutch.analysis.unl.ta;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.io.FileReader;
import java.util.Scanner;
import java.io.File;
import java.io.PrintStream;
import java.io.FileOutputStream;


public class Ontology_Creation {
	PrintStream print_unl_onto;
	Hashtable hash = new Hashtable();
	ArrayList al = new ArrayList();
	ArrayList cnt = new ArrayList();
	public void readFile(){
		try{
			print_unl_onto = new PrintStream(new FileOutputStream("./resource/unl/RE/BWA_Prob/UNL Ontology/UNL_OntoLevel.txt"), true);
			String str = "";
				BufferedReader buffer = new BufferedReader(new FileReader("./resource/unl/RE/BWA_Prob/UNL Ontology/UNL_Ontology.txt"));
			while((str = buffer.readLine())!=null){
				int len = str.length();
			//	System.out.println(str+"::::::"+str.length());
				str = str.trim();
				int len1 = str.length();
				int level1 = len-len1;
			//	int findex = str.indexOf("(");
			//	String str1 = str.substring(findex);
			//	System.out.println("str1:"+str1);
			//	System.out.println(str+"::::::"+len +"+"+len1+"========="+level1);
				if( (str.equals("adverbial concept")) || (str.equals("attributive concept")) || (str.equals("nominal concept")) || (str.equals("predicative concept")) ){
					al.add(str);
					int level = len - len1;
					cnt.add(level);
				}
				 if(str.contains("(")){
					StringTokenizer strToken1 = new StringTokenizer(str, "(");
					while(strToken1.hasMoreTokens()){
				//	String token = strToken1.nextToken();
				//	System.out.println("------------->"+token);
						String token1 = strToken1.nextToken();
					//	System.out.println(token1);
						if(token1.contains(")")){
							token1 = token1.replace(")", "");
						//	hash.put(token1, "1");
						//	al.add(token1);
							if(al.isEmpty()){
								al.add(token1);
								int level = len - len1;
								cnt.add(level);
							}else if(!(al.contains(token1))){
								al.add(token1);
								int level = len - len1;
								cnt.add(level);
							}
						}
					}	
					
				/**	char[] chars = str.toCharArray();
					char tabb = '\t';
					int count = 1;
					for(int j=0;j<chars.length;j++){
						if(chars[j]==tabb){
					//	if(Character.isSpace(chars[j])){
							count++;
						}
						cnt.add(count);
					}*/
				}	
				
			}	
		/**	String output = hash.toString();
			output = output.replaceAll("=1,", "\n");
			output = output.replaceAll("=1", "");
			System.out.println("hash:"+output);*/
			for(int i=0;i<al.size();i++){
			//	System.out.println(cnt.get(i)+"\t"+al.get(i).toString());
				print_unl_onto.println(cnt.get(i)+"\t"+al.get(i).toString());
				//al.get(i).toString()+"\t"+cnt.get(i)
			}
		}catch(Exception e){
			
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ontology_Creation onto = new Ontology_Creation();
		onto.readFile();
	}

}
