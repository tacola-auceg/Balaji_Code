package org.apache.nutch.analysis.unl.ta;
//package com.google.gdata.util.common.base;

//import static com.google.gdata.util.common.base.Preconditions.checkArgument;
//import static com.google.gdata.util.common.base.Preconditions.checkNotNull;



import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;


public class PreprocessDocs {

	/**
	 * @param args
	 */
	ArrayList al = new ArrayList();
	String output ="";
	boolean digit = false;
	String result = "";
	String word1 = "";
	public String get_words(String st){
		try{
		//	System.out.println(st);
			String alphaOnly = st.replaceAll("[^\\p{Alpha}]+","");
			String alphaAndDigits = st.replaceAll("[^\\p{Digit}]+","");
			
			System.out.println("length:"+alphaAndDigits.length());

			StringTokenizer strToken1 = new StringTokenizer(st," .");
			int count = strToken1.countTokens();
			int i = 0;
			while(i < count){
				String word = strToken1.nextToken();
				digit = checkIsNumber(word);
				if(digit == true){
				//	System.out.println("digit:"+digit +" "+word);
					word = word.replace(word, "");
					word1 += word + " ";
					digit = false;
				//	System.out.println("word1:"+word1);
				}else{
					word1 += word + " ";
				}
				i++;
			}
			result = word1;
			word1 = "";
			
		}catch(Exception e){
			
		}
		return result;
	}
	private boolean checkIsNumber(String toCheck) {
		try {
		Integer.parseInt(toCheck);
		return true;
		} catch (NumberFormatException numForEx) {
			return false;
		}
	}
	public void readFiles() {
		try {
			File fn = new File("./resource/unl/ta/TestDocs/content/");
			File[] list = fn.listFiles();
			for (int i = 0; i < list.length; i++) {
				String filename = list[i].getName();
				al.add(filename);
			}
			// System.out.println("Files:"+al+al.size());
		} catch (Exception e) {
		}
	}

	public void get_docs() {
		try {
			readFiles();
			for (int i = 0; i < al.size(); i++) {
				String get_file = al.get(i).toString();
				String fName = "./resource/unl/ta/TestDocs/content/" + get_file;
				File file = new File(fName);
				System.out.println("FILE:"+fName);
				Scanner scanner = new Scanner(file).useDelimiter("\\Z");
				String contents = scanner.next();
			//	System.out.println("content:"+contents);
				output += get_words(contents);
				System.out.println("output:"+" "+output.length());
			//	String s = CharMatcher.WHITESPACE.trimFrom(output);

				if(output.trim().isEmpty()){
					System.out.println("output length less than 10:"+output + " "+output.length());
				}
				output = "";
				result = "";
				scanner.close();
			}
		} catch (Exception e) {

		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PreprocessDocs preprocess = new PreprocessDocs();
		preprocess.get_docs();
	}

}
