package org.apache.nutch.analysis.unl.ta;

import java.io.*;
import java.util.*;

public class MergeSER{
	Hashtable filelist = new Hashtable();
	Hashtable RecnoToURL = new Hashtable();
	Hashtable urlToRecno = new Hashtable();
	public void process_SERFiles(){
		try{
			Hashtable hash_new = loadserfiles("./crawl-unl-newversion/RecnotoURL.ser");
			for(Object key : hash_new.keySet()){
				String newkey = (String)key;
				if(newkey.startsWith("d")){
					newkey = newkey.replace("d","");
					int key_int = Integer.parseInt(newkey.toString());
					String value = hash_new.get(key).toString();
					RecnoToURL.put(key_int, value);
					urlToRecno.put(value, newkey.trim());
				}
				
			}
		//	RecnoToURL.putAll(hash_new);
			Hashtable hash_old = loadserfiles("./crawl-unl-oldversion/RecnoToURL.ser");
			RecnoToURL.putAll(hash_old);
			for(Object  key : hash_old.keySet()){
				int intkey = (Integer)key;
				String value = hash_old.get(key).toString();
				String newkey = Integer.toString(intkey);
			//	System.out.println("KEY:-------->"+newkey+"----"+value);
				urlToRecno.put(value, newkey.trim());
			}
			/*Hashtable hash_old1 = loadserfiles("./crawl-unl-oldversion/urlToRecno.ser");
			urlToRecno.putAll(hash_old1);*/
			String finalres = RecnoToURL.toString();
			finalres = finalres.replaceAll(",", "\n");
			System.out.println(finalres);	
			serializeHashTable(urlToRecno, "./urls/urlToRecno.ser");
	        	serializeHashTable(RecnoToURL, "./urls/RecnoToURL.ser");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public Hashtable loadserfiles(String path){
		try{
			FileInputStream fis = new FileInputStream(path);
			ObjectInputStream ois = new ObjectInputStream(fis);
			filelist = (Hashtable)ois.readObject();
			ois.close();
			fis.close();
		}catch(Exception e){
			filelist = new Hashtable();
			e.printStackTrace();
		}
		return filelist;
	}
	 private void serializeHashTable(Hashtable<String, String> hashtable, String fileName) {
	        ObjectOutputStream objectOutputStream = IOHelper.getObjectOutputStream(fileName);
	        IOHelper.writeObjectToOutputStream(objectOutputStream, hashtable);
	        IOHelper.closeObjectOutputStream(objectOutputStream);
	    }
	public static void main(String args[]){
		try{
		//	Hashtable hash_new = new Hashtable("/home/clia/workspace/clia-beta-working/crawl-unl-newversion/RecnotoURL.ser");
		//	Hashtable hash_old = new Hashtable("/home/clia/workspace/clia-beta-working/crawl-unl-oldversion/RecnotoURL.ser");
			MergeSER merge = new MergeSER();
			merge.process_SERFiles();
		}catch(Exception e){

		}
	}
}
