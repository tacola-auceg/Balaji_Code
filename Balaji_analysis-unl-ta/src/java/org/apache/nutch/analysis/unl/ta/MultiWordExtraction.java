package org.apache.nutch.analysis.unl.ta;

import java.io.*;
import java.util.*;
import java.lang.*;

public class MultiWordExtraction{
	MWDict mwdict = new MWDict();
	
//	String mwe = "";
//	int now = 0;
	
	public ArrayList processMW(ArrayList<String> anal, ArrayList<String> root, ArrayList<String> pos){
		ArrayList final_list = new ArrayList();
		try{
			String mwe = "";
			ArrayList get_list = new ArrayList();
		//	System.out.println("MWE:"+"\n"+root+"\n"+pos);
			for(int i=0;i<root.size();i++){
				String lc = pos.get(i).toString();
				String tw = root.get(i).toString();
				
				if( (lc.equals("Noun")) || (lc.equals("Entity")) ){
					int hc = tw.hashCode();
					int indx = root.indexOf(tw);
					if(mwdict.mwdict.containsKey(hc)){
						LinkedList ll = (LinkedList)mwdict.mwdict.get(hc);
						System.out.println("ll----------->"+ll);
						int len = ll.size();
						for(int j = 0; j < len; j++){
							LinkedList get_mw_ll = (LinkedList)ll.get(j);
							String mw = (String)get_mw_ll.get(0);
							String mw_hw = (String)get_mw_ll.get(1);
							String mw_uw = (String)get_mw_ll.get(2);
							int now = (Integer)get_mw_ll.get(3);
							int endIndex = now-1;
							int toIndex = indx + now;
						//	System.out.println("Index:"+indx+"+"+endIndex+"("+now+")"+"==="+toIndex);
							ArrayList new_list = get_Multiwords(mw);
							get_list.addAll(root.subList(indx, toIndex));
						//	System.out.println("NEW LIST:---------->"+new_list+"\t"+get_list);
							if(get_list.containsAll(new_list)){
							//	System.out.println("MULTIWORD MATCHING--------->"+new_list);
								String multiword = mw+"/"+mw_hw+"/"+mw_uw+"/"+lc;
								final_list.add(multiword);
								break;
							}else{
							//	System.out.println("ELSE NOT A MULTIWORD");
							}
							get_list.clear();
						}
					}
			//		mwe += tw + " ";
				}
			}
		}catch(Exception e){
			
		}
		return final_list;
	}
	public ArrayList get_Multiwords(String mwe){
		ArrayList mwe_list = new ArrayList();
		try{
			StringTokenizer strToken = new StringTokenizer(mwe, " ");
			while(strToken.hasMoreTokens()){
				String word = strToken.nextToken().trim();
				mwe_list.add(word);
			}
		}catch(Exception e){
			
		}
		return mwe_list;
	}
}