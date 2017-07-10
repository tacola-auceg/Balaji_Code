package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.index.unl;
//import org.apache.nutch.index.unl.*;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.*;
import java.util.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
//import org.apache.nutch.enconversion.unl.ta.*;
//import org.apache.nutch.search.unl.*;
import org.apache.nutch.analysis.unl.ta.*;



public class Docid implements Serializable{
		//public static org.apache.nutch.index.unl.MyInteger;// my = new MyInteger();	
	 	public  BinaryNode root;  	 	
		ArrayList temp=new ArrayList();
		TreeSet arlist = new TreeSet();
		ArrayList arlist1= new ArrayList();
		ArrayList<String> GetMW = new  ArrayList <String>();
		ArrayList ArlCon = new ArrayList();
		ArrayList arltemp= new ArrayList();
		int EQW_Cnt;
		public static int cnt=0;
		public static BinarySearchTree t= new BinarySearchTree();
		public static boolean check_Tree=true;
		public static boolean chkflag=true;
		public static BinarySearchTree bst = new BinarySearchTree();
		public static org.apache.nutch.analysis.unl.ta.Rules rs;
		
		public static boolean chek=true;


	public Docid(){
	EQW_Cnt=0;		
	}	

				
	public BinarySearchTree getBinarySearchTree()	
		{
		     try
		       { 

			 FileInputStream fis2=new FileInputStream("./crawl-unl/c.txt");
			 ObjectInputStream ois2=new ObjectInputStream(fis2);
			 String str="";
			 //binaryTree.root=binaryTree.tree_FileRead1(binaryTree.root, ois2,str);
			 t.root=t.tree_FileRead(t.root, ois2,str);
			 	ois2.close();
			 	fis2.close();   
	
		       }
			
		       catch(Exception e){t = new BinarySearchTree();return t;
			} 

		return t;   
		}


	public BinarySearchTree printquerytree()throws IOException{
			t=getBinarySearchTree();		
			return t;
		}

	public String CheckMultiword(String g_word){
	String MWtag_Qw="";
	String[] QW_MWCheck	= g_word.split(" ");
	int QW_MWCheckCnt=QW_MWCheck.length;		
		if(QW_MWCheckCnt>=2){		
			MWtag_Qw="MW";		
		}
		else{
			MWtag_Qw="NMW ";
		//System.out.println("Not Multiword"+MWtag_Qw);
		}
	return MWtag_Qw;
	}	


	//public String visitTree(BinaryNode t,String s)throws IOException
	public ArrayList visitTree(BinarySearchTree t,Comparable QryHC)throws Exception
	{
		
		String MWTag ="";
		ArrayList GetIndx=new ArrayList();
		ArlCon.clear();
		BinaryNode bn= t.find(QryHC);		
		DocNode d=bn.docnext;	
		System.out.println("Iam in visit tree");
		BinaryNextNode bt = new BinaryNextNode();		
		
		if(d!=null)		
		{

		
			//System.out.println("Traverse Binary Node:"+d.docid);
			//bt=d.docnext;
			arlist.add(d.docid);
				while (d!=null){		
				//System.out.println("Traverse Binary Node:"+d.docid);
				arlist.add(d.docid);
				d=d.docnext;
				}
		}

		//System.out.println("New ArrayList: "+GetIndx);
		//System.out.println("ArrayList Size: "+GetIndx.size());	
		System.out.println("Doc id ArrayList:"+arlist);
		//arlist1.add(arlist);
		//System.out.println("All"+arlist1);
		//return ArlCon;
		return GetIndx;
	}

public void printz()
{
System.out.println("All"+arlist1);
}


//public String EnconvertUNL(String str)
public int EnconvertUNL(String str)throws Exception
{
//rs= new Rules();
        System.out.println("Expansion UNL:"+str);
	MWDict mwdict= new MWDict();
	UWDict uwdict= new UWDict();
	BST dict;

	BSTNode bn=new BSTNode();
	dict=new BST();
	
	MWBST dict_mw=new MWBST();
	MWBSTNode bn_mw=new MWBSTNode() ;

 	dict_mw=mwdict.get_mwbst();
	dict=uwdict.get_bst();
	
		
	int IndxHc=0;

	int hc;
	String temp[]= str.split(" ");
	System.out.println("STR "+str +"\n");
	System.out.println("Temp "+temp +"\t"+temp.length);
	
	if(temp.length >1){	
	hc = temp[0].hashCode();
	bn_mw=dict_mw.search(hc);
	  if(bn_mw!=null){
	  MWBSTNode bn_mw1=new MWBSTNode() ;
        	while(bn_mw!=null){
        	   bn_mw1=bn_mw;
        		   if(bn_mw.lexeme.equals(str))
        		    break;
          	bn_mw=bn_mw.getNext();  
           	}            
		//System.out.println("bn_mw"+bn_mw);
		//System.out.println("Multi Dict Word"+bn_mw1.lexeme +"\t"+ bn_mw1.headword+"\n");	
		String Final=   bn_mw1.headword+"("+bn_mw1.constraint.trim()+")";
		System.out.println("Final"+Final);
		IndxHc=Final.trim().hashCode();
	  }
	}else{
		hc=str.hashCode();
		bn=dict.search(hc);	
			if(bn!=null){
				String Final=bn.headword+"("+bn.constraint.trim()+")";
				//System.out.println("Final"+Final);
				IndxHc=Final.trim().hashCode();
				//System.out.println("Dict Word"+bn.lexeme +"\t"+ bn.headword+"\n");
			}			
	}
System.out.println("Hash Value"+IndxHc);
return IndxHc;
}



public ArrayList processUNLMW(Comparable queryHC)
{
	try{
			if(check_Tree){
			t=printquerytree();
			check_Tree=false;
			}			
			ArrayList getExpQry= visitTree(t,queryHC);
				if(getExpQry!=null){
				temp=getExpQry;
				//System.out.println("Temp "+temp);	
				}
				else
				temp=null;								
			}catch(Exception e){}
	return temp;								
}




/**public static void main(String args[])throws Exception
{
Docid Q = new Docid();
String Query="கன்னியாகுமரி அருகில்  உள்ள கோட்டை";
String temp[]=Query.split(" ");
for(String st:temp){
int i= Q.EnconvertUNL(st);
//System.out.println("Hash Value"+ i);
ArrayList arl=Q.processUNLMW(new MyInteger(i));
System.out.println("ArrayList"+arl);
//Q.printz();
//System.out.println("Final Array:"+printz);	
}
}*/
}


