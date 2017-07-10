package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.snippetgeneration.unl.ta;
import java.util.*;
import java.io.*;
public class SummaryGet{
   public static boolean isSummaryLoaded = false;
    public static boolean isSummaryLoadedeng = false;
   public static HashMap<String,String> summaryunl,summaryeng;
//public static Hashtable summaryeng=new Hashtable();
   public void loadSummary(){
    if(isSummaryLoaded)
    {
       return;
    }
    ObjectInputStream summaryReader = IOHelper.getObjectInputStream("./crawl-unl/summary.ser");
    summaryunl = (HashMap<String,String>) IOHelper.readObjectFromInputStream(summaryReader);
    isSummaryLoaded = true;
   }

/*public void loadEngSummary(){
    if(isSummaryLoadedeng)
    {
       return;
    }
    ObjectInputStream summaryReader = IOHelper.getObjectInputStream("./crawl-unl/summaryeng.ser");
    summaryeng = (HashMap<String,String>) IOHelper.readObjectFromInputStream(summaryReader);
     System.out.println("SIZE OF HASH MAP"+summaryeng.size());
    isSummaryLoadedeng = true;
   }
    */
   public String getSummaryunl(String docid){
    loadSummary();
    String summarys="";
    Set key=summaryunl.keySet();
    if(!key.contains(docid))
      return "NoSummaryAvailable";
    else
       summarys=summaryunl.get(docid).toString();
           
    summarys="<a href=\"javascript:showhide('div"+docid+"');\" onmouseover=\"javascript:showhide('div"+docid+"');\" onmouseout=\"javascript:showhide('div"+docid+"');\" style=\"color:green\">குறுந்தொகுப்பு</a>"+"<div id=\"div"+docid+"\" style=\"display:none;border:ridge;margin-right:25%;color:green\">"+"<b> இப்பக்கத்தில் உடல் நலம் பற்றிய குறிப்பு</b><br/>"+summarys+"</div>";
    return summarys;
   }
/*public String getEngSummaryunl(String docid){
    loadEngSummary();
    String summaryenglish="";
    Set key=summaryeng.keySet();
 //  System.out.println("KEY :"+key.toString());
   if(!key.contains(docid))
      return "NoSummaryAvailable";
    else
    summaryenglish=summaryeng.get(docid).toString();
    summaryenglish="<a href=\"javascript:showhide('div"+docid+"s');\" onmouseover=\"javascript:showhide('div"+docid+"s');\" onmouseout=\"javascript:showhide('div"+docid+"s');\" style=\"color:green\">Summary</a>"+"<div id=\"div"+docid+"s\" style=\"display:none;border:ridge;margin-right:25%;color:green\">"+"<b> Tourism information available in this page</b><br/>"+summaryenglish+"</div>";
    //summaryenglish="<b> Tourism information available in this page</b><br/>"+summaryenglish;
    return summaryenglish;
   }*/
public static void main(String args[]){
SummaryGet s=new SummaryGet();
//String sum=s.getEngSummaryunl("d4024");
//System.out.println("SUM :"+sum);
}
}
