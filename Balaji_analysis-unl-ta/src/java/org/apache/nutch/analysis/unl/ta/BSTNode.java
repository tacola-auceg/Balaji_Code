package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.enconversion.unl.ta;
public class BSTNode {
	
	public int el;
	public String lexeme;
	public String headword;
	public String constraint;
	
    BSTNode next;
    BSTNode left, right;
    
    public BSTNode() 
    {
        next=left = right = null;
    }
    public BSTNode(int el,String lex,String hw,String CL) 
    {
   		this.el = el;
        lexeme = lex;
        headword = hw;
        constraint = CL;
     }
     
     public BSTNode getNext()
	 {
			return next;
	 }
	
	public void setNext(BSTNode nxt)
	{
			next = nxt;
		}
  }
