package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.enconversion.unl.ta;


import java.lang.*;
import java.io.*;
import java.util.*;
import org.apache.nutch.analysis.unl.ta.SentExtr;
import java.util.regex.*;
public class SentAppln
{
	public static Pattern pattern1 = Pattern.compile("[1234567890.]");
    public static Matcher match1;
		public int docid=0;
		
		int total_pos=0;
			
		
                public Hashtable ht=new Hashtable();
public SentAppln()
{
	start();
}		
public void start()
{
	
	try
	{
		long timestart  = System.currentTimeMillis();
		
		String s = "";
		StringBuffer docbuff  = new StringBuffer();		
		//ei.ExtractList();
	//	BufferedReader in = new BufferedReader(new FileReader("./resource/unl/ta/SentenceExtraction/Input/nonemptyfiles.txt"));		
		BufferedReader in_file = new BufferedReader(new FileReader("./resource/unl/ta/SentenceExtraction/Input/nonemptyfiles.txt"));
		while((s=in_file.readLine())!=null)
		{ 
			docid++;
			
			String did=Integer.toString(docid);
	//		BufferedReader in = new BufferedReader(new FileReader("./resource/unl/ta/SentenceExtraction/Input/content"+did+".txt"));
		//	System.out.println("DID "+s);
		//	BufferedReader in_txt = new BufferedReader(new FileReader("/home/clia/workspace/clia-beta-working/resource/unl/ta/TestDocs/content/"+s.substring(s.lastIndexOf('/'),s.length())));
		//	String txt="",s1;
		//	while((s1=in_txt.readLine())!=null){
		//		txt=txt+s1;
		///	}
		///	in_txt.close();
		//	int length=preprocess(txt);
		//	System.out.println(length);
		//	if(length!=0){
		//		System.out.println(s+" "+did);
				SentExtr se = new SentExtr();			
					
				ht= se.impSentExtr(s,did);
		//	}
		}	
		in_file.close();			
				long timeend = System.currentTimeMillis();
			
	}
	catch(Exception e)		
	{	
		System.out.println(e);
	}

}
public int preprocess(String s1){
    //String s="21 3423 43";
    match1 = pattern1.matcher(s1);
    String getConcept = match1.replaceAll("");
   // System.out.println(getConcept.trim().length());
    return getConcept.trim().length();
}
public static void main(String args[])throws IOException
{
	SentAppln ap=new SentAppln();	
}
}


