package org.apache.nutch.analysis.unl.ta;
import java.io.*;
import java.util.Stack;
import java.util.Set;
import java.util.HashSet;
import java.util.Vector;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.*;
import org.apache.nutch.unl.UNL;
import org.apache.nutch.analysis.unl.ta.Tag.*;

public class Analyser implements UNL
{

	static boolean analysePart = true;
	static boolean print;

	
	//public static ResourceBundle Tags = ResourceBundle.getBundle(Tag.ENGLISH_EXPAND,new Locale("en","US"));
	public static ResourceBundle Tags = ResourceBundle.getBundle("org/apache/nutch/analysis/unl/ta/Tag/" + Tag.ENGLISH_EXPAND,new Locale("en","US"));

	public static String analyseF(String input, boolean analysePart)
	{

		String output = "<"+input+">" + ":\n";
		try
		{
			Stack allStk = new Stack();
           		int count =0;
           		Method.analyse(input, allStk, analysePart);
	
			int size = allStk.size();
        
			if (size == 0)
			{
				output += "<Error>\n";
				return output;
			}
	
			for (int i = 0; i < size; i++)
			{
				Stack outputStack = (Stack) allStk.get(i);
				//System.out.println("outputStack"+outputStack);
				String part ="<";
				boolean flag = false;
				String stag = "~";
				while (!outputStack.empty())
				{
					Entry entry = (Entry) outputStack.pop();
					String s = UnicodeConverter.revert(entry.getPart());
					//System.out.println("s"+s);
					int tag = entry.getTag();
                    			//System.out.println("tag"+tag);
                    			if (tag != -1)
					{
			                        flag = true;
                		        if(part.length() ==1)
                		            part += s;
                		        else
                		        part +="+"+ s;
						s += " < ";
						s += Tags.getString(String.valueOf(tag));
						s +=" "+"&"+" ";
						stag =stag+"\t"+ String.valueOf(tag);
						s += String.valueOf(tag);
						
						s += " > ";
					}
					else
					{
						count++;
						s+= "<0>";
						break;
					}
					output += s;
				}
				
				if( !stag.equals("~"))
				
				if (allStk.size() != 1 && flag == true)
				{
					output += Utils.newLineStr + part +'>' + Utils.newLineStr;
				}

			}
			output +=" count="+ count+"\n";
			if(count==(size-1) && size==4)
				{
					if(output.startsWith("<#>:"))
					{
						output ="<<s>\n";
					}
					else
					{
				        output +="<unknown>\n";
				        //out.write("<unknown>\n");
				        //out1.write("<unknown>\n");
            		 }
				}
		  //out.write("@\n");
          //out.clo		se();
          //out1.write("@\n");
          //out1.close();
		}
		catch (Exception e)
		{
			System.err.println("Analyser analyseF: " + e);
			e.printStackTrace();
			return input;
		}
		//////System.out.println( "output =" + output);
		output+="<<wc>\n";

		return output;
	}

	public static String analyseW(String input, boolean analysePart)
	{
		String output = input + ":";

		try
		{
			Stack allStk = new Stack();

			// for dic ref count
			// output += "- " + Method.analyse(input,allStk);

			Method.analyse(input, allStk, analysePart);
			int size = allStk.size();

			if (size == 0)
			{
				output += " Unknown";
				return output;
			}
			for (int i = 0; i < size; i++)
			{
				Stack outputStack = (Stack) allStk.get(i);

				while (!outputStack.empty())
				{
					Entry entry = (Entry) outputStack.pop();
					String s = UnicodeConverter.revert(entry.getPart());

					output += Utils.newLineStr_OS;
					output += s;
					int tag = entry.getTag();

					if (tag != -1)
					{
						s = " < ";
						s += Tags.getString(String.valueOf(tag));
						s += " > ";
					} else
					{
						s = "";
					}
					output += s;
				}
				if (allStk.size() != 1)
				{
					output += Utils.newLineStr_OS + "***";
				}
			}
		} catch (Exception e)
		{
			System.err.println("Analyser analyseW: " + e);
			e.printStackTrace();
			return input;
		}
		return output;
	}

	public static Vector analyse(String input, boolean analysePart)
	{
		Vector output = new Vector();

		try
		{
			Stack allStk = new Stack();

			Method.analyse(input, allStk, analysePart);
			for (int i = 0; i < allStk.size(); i++)
			{
				Stack outputStack = (Stack) allStk.get(i);
				Vector part = new Vector();
				Vector tag = new Vector();

				while (!outputStack.empty())
				{
					Entry entry = (Entry) outputStack.pop();
					String s = UnicodeConverter.revert(entry.getPart());

					part.add(s);
					int tagInt = entry.getTag();

					tag.add(new Integer(tagInt));
				}
				output.add(new Element(part, tag));
			}
		} catch (Exception e)
		{
			System.err.println("Analyser analyse: " + e);
			e.printStackTrace();
			return null;
		}
		
		return output;
	}

	public static Vector getVector(Stack inputStk)
	{
		Vector output = new Vector();

		try
		{
			for (int i = 0; i < inputStk.size(); i++)
			{
				Stack outputStack = (Stack) inputStk.get(i);
				Vector part = new Vector();
				Vector tag = new Vector();

				while (!outputStack.empty())
				{
					Entry entry = (Entry) outputStack.pop();
					String s = UnicodeConverter.revert(entry.getPart());

					part.add(s);
					int tagInt = entry.getTag();

					if (tagInt != -1)
					{
						s = String.valueOf(tagInt);
					} else
					{
						s = "";
					}
					tag.add(s);
				}
				output.add(new Element(part, tag));
			}
		} catch (Exception e)
		{
			System.err.println("Analyser analyse: " + e);
			e.printStackTrace();
			return null;
		}
		return output;
	}

	// call this before analysing a large document
	// load btree into memory
	public static void init()
	{
		analyse(UnicodeConverter.revert(Constant.vel), analysePart);
	}

	public static void main(String args[])
	{
		try{		
		Vector v = new Vector();
		String str="";
		BufferedReader inbuff = new BufferedReader(new FileReader("in.txt"));
		BufferedWriter outbuff = new BufferedWriter(new FileWriter("out.txt"));

		while((str= inbuff.readLine()) != null)
                {
			if(str.indexOf("#") == -1){
				String analysed =Analyser.analyseF(str,true);
				outbuff.write(analysed);
			}
		}
		inbuff.close();
		outbuff.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	// for procesing analyser output------------------
	static Set d = new HashSet();
	static String y = "";

	static
	{
		d.add(String.valueOf(Tag.Numbers));
		d.add(String.valueOf(Tag.Noun));
		d.add(String.valueOf(Tag.Pronoun));
		d.add(String.valueOf(Tag.InterrogativeNoun));
		d.add(String.valueOf(Tag.PronounOblique));
		d.add(String.valueOf(Tag.Verb));
		d.add(String.valueOf(Tag.FiniteVerb));
		d.add(String.valueOf(Tag.NegativeFiniteVerb));
		d.add(String.valueOf(Tag.Adjective));
		d.add(String.valueOf(Tag.DemonstrativeAdjective));
		d.add(String.valueOf(Tag.InterrogativeAdjective));
		d.add(String.valueOf(Tag.Adverb));
		d.add(String.valueOf(Tag.Interjection));
		d.add(String.valueOf(Tag.Conjunction));
		d.add(String.valueOf(Tag.Particle));
		d.add(String.valueOf(Tag.Intensifier));
		d.add(String.valueOf(Tag.Postposition));
	}

	public static boolean isNoun(Vector v)
	{
		for (int i = 0; i < v.size(); i++)
		{
			Element elmt = (Element) v.get(i);
			Vector tag = elmt.getTag();
			int index = elmt.containsTag(new Integer(Tag.Noun));

			if (index != -1)
			{
				return true;
			}
		}
		return false;
	}

	public static boolean is2320(Vector v)
	{
		for (int i = 0; i < v.size(); i++)
		{
			Element elmt = (Element) v.get(i);
			Vector tag = elmt.getTag();
			int index = elmt.containsTag(new Integer(2320));

			if (index != -1)
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isNoun(String word)
	{
		return isNoun(analyse(word, analysePart));
	}

	public static boolean isVerb(Vector v)
	{
		for (int i = 0; i < v.size(); i++)
		{
			Element elmt = (Element) v.get(i);
			Vector tag = elmt.getTag();
			int index = elmt.containsTag(new Integer(Tag.Verb));

			if (index != -1)
			{
				return true;
			}
		}
		return false;
	}

	public static boolean isVerb(String word)
	{
		return isVerb(analyse(word, analysePart));
	}

	public static String getRoot(Vector v)
	{
		for (int i = 0; i < v.size(); i++)
		{
			Element elmt = (Element) v.get(i);
			Vector partV = elmt.getPart();

			// String tag = null;

			if (partV.size() > 0)
			{
				return (String) partV.get(0);
			}
		}
		return null;
	}

	public static String getRoot(String word)
	{
		return getRoot(analyse(word, analysePart));
	}

	public static boolean isAnalysed(Vector v)
	{
		if (v.size() > 0)
		{
			return true;
		}
		return false;
	}

	public static boolean isAnalysed(String input)
	{
		Vector v = analyse(input, analysePart);

		if (v.size() > 0)
		{
			return true;
		}
		return false;
	}
	
	//Added just to implement UNL.java interface, never used anywhere
 public ArrayList process(String queryString){
ArrayList arr = null;return arr;}

public String debugQueryExpansionOutput(boolean debugQE,boolean singleWdwithanalyser,boolean singleWdwithoutanalyser,boolean multiWd){return null;}
  public String debugQueryTranslationOutput(boolean translation,boolean ActualQW,boolean expansion,boolean icliofCombination){return null;}
  public String debugSearchOutput(boolean Search,boolean Rank){return null;}
  public  Hashtable<String,Hashtable<String,String>> getOutput(){return null;}
  public  Hashtable<String,Hashtable<String,Double>> getTime(){return null;}
  public  Hashtable<String,Hashtable<String,String>> getCount(){return null;}
  public Hashtable<String,Hashtable<String,String>>  getRank(){return null;}
  public Hashtable<String,String> getVersion(){return null;}
  public void finishedDebugTool(){}
}
