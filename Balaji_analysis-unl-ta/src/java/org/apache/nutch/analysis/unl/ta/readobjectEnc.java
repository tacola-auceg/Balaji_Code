package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.enconversion.unl.ta;
import org.apache.nutch.unl.UNL;

//import org.apache.nutch.util.NutchConfiguration;
//import org.apache.hadoop.conf.Configuration;

import java.util.*;
import java.io.*;

public class readobjectEnc implements Serializable
{
//	public static Configuration conf = NutchConfiguration.create();
//	public static String path=conf.get("UNL resource");
	public static void main(String args[])
	{
	//	System.out.println("hello)");
		//ArrayList ll_new=new ArrayList();
		//String filename[]={"unlgraph1.txt","unlgraph2.txt"};
		
		int n=0;
		int l=0;
		File f=new File("./resource/unl/Enconversion/unl-graph");
	//	File f=new File("./crawl-unl");
		File filename[]=f.listFiles();
		FinalLLImpl [] ll_new=new FinalLLImpl[5000];
		try
		{
			for(int i=0;i<filename.length;i++)
			{
		//	System.out.println("hello1");
					
					FileInputStream fis1=new FileInputStream("./resource/unl/Enconversion/unl-graph/"+"unlgraph"+n+".txt");
				//	FileInputStream fis1=new FileInputStream("./crawl-unl/unlgraph"+n+".txt");
					System.out.println("FILE READING"+"./resource/unl/Enconversion/unl-graph/"+"unlgraph"+n+".txt");
					n++;
				//	System.out.println("hello2");	
			 		ObjectInputStream ois1=new ObjectInputStream(fis1);					
			 		ll_new=( FinalLLImpl [])ois1.readObject();
				//	System.out.println("hello");
					//System.out.println("size is"+ll_new.size());
					//for(int i=0;i<ll_new.size();i++)
					//{
					//FinalLLImpl 
					//System.out.println("Object"+ll_new.get(i));
				       
				   	 FinalLLImpl ll[] =ll_new;//(FinalLLImpl[] )ll_new.get(i);
			
				     	System.out.println("ll.length"+ll.length);
					int counter=0;
					for(int z=0;z<ll.length;z++)
					{
				         	if(ll[z]!=null) 
						{
							HeadNode h=new HeadNode();
						       ConceptNode c=new ConceptNode();
							ConceptToNode current1=new ConceptToNode();
						       h=ll[z].head;
						       c=h.colnext; 
							counter++;                       
				       			while(c!=null)
				       			{
				       				System.out.println("Inside While Concept");
								current1 = c.rownext;
								while(current1!=null){
									System.out.println("Inside While TO----------------Concept");
				         			System.out.println(c.gn_word+":"+current1.uwfrmconcept+" "+current1.relnlabel+" "+current1.uwtoconcept+" "+c.sentid+" "+c.docid);
									current1 = current1.rownext;
								}		
			System.out.println("the concepts"+c.gn_word+" "+c.uwconcept+" "+c.docid+" "+c.sentid+" "+c.conceptid+" "+c.freq_count+" "+c.con_count);	
								l++;		         			
								c=c.colnext;
				        		}
							
				   //   System.out.println("***************end of first object*****************"+counter);
						}
					} 
					System.out.println("CONCEPTS SIZE:"+l);
					ois1.close();
		 			fis1.close(); 
					ll=null;
					ll_new=null;
					System.out.println(""+filename[i]);
				
			}
		//}
 
	 		
		}catch(Exception e)
		{
			e.printStackTrace();
		}	
	}
}
