package org.apache.nutch.analysis.unl.ta;

import java.io.BufferedReader;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;
import java.io.FileReader;

//import org.w3c.dom.Node;

public class Ontology_Building {

	public String load_Elements(String str) {
		String output = "";
		try{
			StringTokenizer strToken1 =  new StringTokenizer(str,"\t");
			String value = strToken1.nextToken();
			String key = strToken1.nextToken();
			
		//s	System.out.println("Node:"+node);
		}catch(Exception e){	
			
		}
		return output;
	}

	public void readFile() {
		try {
			//Node node = new Node();
			String output = "";
			String str = "";
			BufferedReader buffer = new BufferedReader(new FileReader(
					"./resource/unl/BWA_Prob/UNL Ontology/UNL_OntoLevel.txt"));
			while ((str = buffer.readLine()) != null) {
				output = load_Elements(str);				
			}
			System.out.println("Node:"+output);
		} catch (Exception e) {

		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Ontology_Building onto_build = new Ontology_Building();
		onto_build.readFile();

	}
}
