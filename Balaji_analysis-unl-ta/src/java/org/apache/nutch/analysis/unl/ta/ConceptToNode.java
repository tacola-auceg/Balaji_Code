package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.enconversion.unl.ta;
import java.io.*;
public class ConceptToNode implements Serializable
	{
	public	String uwfrmconcept;
	public	String uwtoconcept;
	public	String relnlabel;
	public  String frmscopeid; 
	public  String toscopeid;
	public  String frmscopeflg; 
	public  String toscopeflg; 
	public  String subgraphid; 
	public	String docid;
	public	String sentid;
		
	
		
	public	ConceptToNode rownext;
	public	ConceptToNode colnext;
		
		// Node constructor
		
		public ConceptToNode()
		{
			subgraphid = "";
			frmscopeflg="";
			frmscopeid="";
			uwfrmconcept="";
			relnlabel="";
			uwtoconcept="";
			toscopeid="";
			toscopeflg="";
			docid="";
			sentid="";
			
			
			rownext = null;
			colnext = null;
		}
		
		public ConceptToNode(String sgid, String frmflg, String frmid, String frmcon,String rl,String tocon,String toid, String toflg, String did,String sid)
		{
			subgraphid = sgid;
			frmscopeflg = frmflg;
			frmscopeid = frmid;
			uwfrmconcept=frmcon;
			relnlabel = rl;
			uwtoconcept=tocon;
			toscopeid = toid;
			toscopeflg = toflg;
			docid = did;
			sentid = sid;
			
			
			rownext = null;
			colnext = null;
			
		}
		
		public void setData(String sgid, String frmflg, String frmid, String frmcon,String rl,String tocon,String toid, String toflg, String did,String sid)
		{
			subgraphid = sgid;
			frmscopeflg = frmflg;
			frmscopeid = frmid;
			uwfrmconcept=frmcon;
			relnlabel = rl;
			uwtoconcept=tocon;
			toscopeid = toid;
			toscopeflg = toflg;
			docid = did;
			sentid = sid;
			
		}
		
		public ConceptToNode getRowNext()
		{
			return rownext;
		}
		
		public ConceptToNode getColNext()
		{
			return colnext;
		}
		
		public void setRowNext(ConceptToNode rwnxt)
		{
			rownext = rwnxt;
		}
		
		public void setColNext(ConceptToNode colnxt)
		{
			colnext = colnxt;
		}
	}
