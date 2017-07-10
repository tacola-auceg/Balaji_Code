package org.apache.nutch.enconversion.unl.ta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.nutch.analysis.unl.ta.Analyser;
public class Analyser_Unknown {
	ArrayList al = new ArrayList();
	String result = "";
	
	public String get_Unknown(String st){
		try{
			StringTokenizer strToken1 = new StringTokenizer(st," ");
			int cnt = strToken1.countTokens();
			int initialcnt = 0;
			while(initialcnt < cnt){
				String word = strToken1.nextToken().trim();
				String analysed = org.apache.nutch.analysis.unl.ta.Analyser.analyseF(word, true);
				if( (analysed.contains("count=3")) || (analysed.contains("count=4")) || (analysed.contains("Unknown")) ){
					System.out.println(word);
				}			
				initialcnt++;
			}
		}catch(Exception e){
			
		}
		return result;
	}
	public void process_Input(){
		try{
		for(int i=0;i < al.size();i++){
			 String get1 = al.get(i).toString();
             String filename = "./resource/unl/ta/SentenceExtraction/Output/content/" + get1;          
             File fn = new File(filename);
             FileReader fr = new FileReader(fn);
             BufferedReader buffer = new BufferedReader(fr);           
		}
		}catch(Exception e){
			
		}
	}
	public void readFiles() {
		try {
			File fn = new File("./resource/unl/ta/SentenceExtraction/Output/content/");
			File[] list = fn.listFiles();
			for (int i = 0; i < list.length; i++) {
				String filename = list[i].getName();
				al.add(filename);
			}
			// System.out.println("Files:"+al+al.size());
		} catch (Exception e) {
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Analyser_Unknown a_u = new Analyser_Unknown();
		a_u.readFiles();

	}

}
