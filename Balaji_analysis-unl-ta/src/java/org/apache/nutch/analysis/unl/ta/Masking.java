package org.apache.nutch.analysis.unl.ta;

import java.util.LinkedList;

public class Masking {
	
	String dummy = "***";
	LinkedList mask_ll;// = new LinkedList<String>();
	
	/** MASK - LEVEL1 - Case Suffix and Semantic Constraint of C1 + WORD **/
	public LinkedList Level1_Mask(String tuple2, String tuple5, String tuple6, String tuple7){
		mask_ll = new LinkedList();
		try{
		mask_ll.add(0, dummy);
		mask_ll.add(1, tuple2);
		mask_ll.add(2, dummy);
		mask_ll.add(3, dummy);
		mask_ll.add(4, tuple5);
		mask_ll.add(5, tuple6);
		mask_ll.add(6, tuple7);
		}catch(Exception e){
			e.printStackTrace();
		}
		return mask_ll;
	}
	/**MASK - LEVEL2 - Case Suffix and Semantic Constraint of C1 & Semantic Constraint of C2 + WORD **/
	public LinkedList Level2_Mask(String tuple2, String tuple5, String tuple6){
		mask_ll = new LinkedList();
		mask_ll.add(0, dummy);
		mask_ll.add(1, tuple2);
		mask_ll.add(2, dummy);
		mask_ll.add(3, dummy);
		mask_ll.add(4, tuple5);
		mask_ll.add(5, tuple6);
		mask_ll.add(6, dummy);
		
		return mask_ll;
	}
	/**MASK - LEVEL3 - Case Suffix and Semantic Constraint of C1 & C2 + WORD **/
	public LinkedList Level3_Mask(String tuple2, String tuple6){
		mask_ll = new LinkedList();
		mask_ll.add(0, dummy);
		mask_ll.add(1, tuple2);
		mask_ll.add(2, dummy);
		mask_ll.add(3, dummy);
		mask_ll.add(4, dummy);
		mask_ll.add(5, tuple6);
		mask_ll.add(6, dummy);
		
		return mask_ll;
	}
	/**MASK - LEVEL4 - Semantic Constraint of C1 & C2 + WORD **/
	public LinkedList Level4_Mask(String tuple1, String tuple2, String tuple5, String tuple6){
		mask_ll = new LinkedList();
		mask_ll.add(0, tuple1);
		mask_ll.add(1, tuple2);
		mask_ll.add(2, dummy);
		mask_ll.add(3, dummy);
		mask_ll.add(4, tuple5);
		mask_ll.add(5, tuple6);
		mask_ll.add(6, dummy);
		
		return mask_ll;
	}
	/**MASK - LEVEL5 - Case Suffix and Semantic Constraint of C1 & C2 **/
	public LinkedList Level5_Mask(String tuple2, String tuple4, String tuple6){
		mask_ll = new LinkedList();
		mask_ll.add(0, dummy);
		mask_ll.add(1, tuple2);
		mask_ll.add(2, dummy);
		mask_ll.add(3, tuple4);
		mask_ll.add(4, dummy);
		mask_ll.add(5, tuple6);
		mask_ll.add(6, dummy);
		
		return mask_ll;
	}

}
