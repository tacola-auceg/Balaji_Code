package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.template.unl;

//import org.apache.nutch.enconversion.unl.ta.*;
import org.apache.nutch.analysis.unl.ta.*;
import clia.unl.Source.Word_Gen.Generator.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;

/**
 * @author karthikeyan.S
 * @version 2.0
 * @since AUCEG
 */
public class OfflineSummary {

    public HashMap<String, String[]> Templates;
    public static boolean isTemplateLoaded = false;
    public static  HashMap<String, String> Disease, Medicine,  Treatment, summary;
    public HashSet<String> docid, EmptySummary;
    public int Tempwordcount = 0;
    public static Configuration configuration = NutchConfiguration.create();
    public static String templateHome = configuration.get("UNLCrawl");
    public static String EnconversionHome = configuration.get("unl-Graph");
    

    /**
     * This method define for call the all methods in Summary
     */
    public void callFunctions() {
        TemplateLoaded();
        unlgraphRead();
        AddTemplatewords();
        hashTest();
        summaryWrite();
        EmptySummaryFileWrite();
     
    }   
   

    /**
     *This method defien for Load the Templates
     */

    public void TemplateLoaded() {
        if (isTemplateLoaded) {
            return;
        }
        Templates = new HashMap<String, String[]>();
       String[] Diseasearray = {"இப்பக்கம்","போன்ற நோய்கள் பற்றிய தகவல்களை பற்றி எடுத்துரைக்கிறது  "};
        String[] treatmentarray = {" போன்ற சிகிச்சை பற்றிய தகவல்கள் இப்பக்கத்தில் கொடுக்கப்பட்டுள்ளன "};
        String[] medicinearray = {" போன்ற மருந்துகள் பற்றிய தகவல்கள்  இப்பக்கத்தில் கிடைக்கும் "};
	Templates.put("Disease",Diseasearray);
         Templates.put("Medicine", medicinearray);
        Templates.put("Treatment", treatmentarray);
      
        intialize();
        isTemplateLoaded = true;
    }

    /**
     *This method define for Intialize the all variable for Summary
     */
    public void intialize() {
        Disease = new HashMap<String, String>();
             Medicine = new HashMap<String, String>();
        Treatment = new HashMap<String, String>();

        docid = new HashSet<String>();
        summary = new HashMap<String, String>();
        EmptySummary = new HashSet<String>();

    }

    /**
     *This method define for Read unlgraph Object files
     */
    public void unlgraphRead() {
        ArrayList FilenameList = unlgraphGetFile();
        for (Object fileName : FilenameList) {
            ObjectInputStream unlgraph = IOHelper.getObjectInputStream(fileName.toString());
            FinalLLImpl[] finalllImp = (FinalLLImpl[]) IOHelper.readObjectFromInputStream(unlgraph);
            Traverselnode(finalllImp);
        }
    }

    /**
     *This method define for How many unlgraph(input-files) stored in one ArrayList name as FileNameList
     * @return a ArrayList name as FileNameList. This List contains a How many(input-files) unl-graph files in unl-graph directory
     */
    public ArrayList unlgraphGetFile() {
        ArrayList FileNameList = new ArrayList();
        File f = new File(EnconversionHome);
        File[] filename = f.listFiles();
        for (Object filenameIterate : filename) {
            if (filenameIterate.toString().contains("unlgraph")) {
                FileNameList.add(filenameIterate.toString());
            }
        }
        return FileNameList;
    }

    /**
     *This method define for Travese in unlgraph
     * @param finalImp is a unlgraph Objects. This object used for Traverse in unl-graph
     */
    public void Traverselnode(FinalLLImpl[] finalImp) {
	try{
        System.out.println("Welcome to Traverse in unlgraph");
        ConceptNode conceptnode = new ConceptNode();
        HeadNode headnode = new HeadNode();
        for (int i = 0; i < finalImp.length; i++) {
            if (finalImp[i] != null) {
                headnode = finalImp[i].head;
                conceptnode = headnode.colnext;
                while (conceptnode != null) {
                    docid.add(conceptnode.docid);
                    store(conceptnode.uwconcept, conceptnode.gn_word, conceptnode.docid);
                  
                    conceptnode = conceptnode.getColNext();
                }
            }
        }
}catch(Exception e){} 
    }

    /**
     *This method define for Stor the summary in particular Templats
     * @param constraint is a String. This String has contains a Constraints for words
     * @param tamilWord is a String. This String has  contains a tamilword
     * @param docid is a String. This String has contains a file name of Documents
     */
    public void store(String constraints, String tamilwords, String docid) {
        if (constraints.contains(">disease)")) {
          
	 		
	 TemplatesDisease(tamilwords, docid);
	 	
        }
        if (constraints.contains(">treatment")) {
           TemplatesTreatment(tamilwords, docid);
        }
        if (constraints.contains(">medicine)")) {
            TemplatesMedicine(tamilwords, docid);
        }
   
    }

    /**
     *This method defien for Add more details in particular Templates
     * @param tamilWord is a String. This String has  contains a tamilword
     * @param docid is a String. This String has contains a file name of Documents
     * @param templates is HasMap. This HashMap has contains a document-id and summary's
     */
    public void TemplatesDisease(String tamilwords, String docid) {

System.out.println("the docid in templates function:"+docid);
        if (Disease.size() == 0) {
           Disease.put(docid, tamilwords);
        } else {
            if (Disease.containsKey(docid)) {
                String value = Disease.get(docid).toString();
                if (!value.contains(tamilwords)) {
                    value = value + " " + tamilwords;
		System.out.println("docid"+docid+"\t"+value);
                  Disease.put(docid, value);
                }
            } else {
               Disease.put(docid, tamilwords);
            }
        }
System.out.println("docid and value for Disease"+Disease.get(docid));
}
 public void TemplatesTreatment(String tamilwords, String docid) {

System.out.println("the docid in templates function:"+docid);
        if ( Treatment.size() == 0) {
           Treatment.put(docid, tamilwords);
        } else {
            if ( Treatment.containsKey(docid)) {
                String value = Treatment.get(docid).toString();
                if (!value.contains(tamilwords)) {
                    value = value + " " + tamilwords;
		System.out.println("docid"+docid+"\t"+value);
                 Treatment.put(docid, value);
                }
            } else {
             Treatment.put(docid, tamilwords);
            }
        }
System.out.println("docid and value for Treatment"+Treatment.get(docid));
}
 public void TemplatesMedicine(String tamilwords, String docid) {

System.out.println("the docid in templates function:"+docid);
        if (Medicine.size() == 0) {
           Medicine.put(docid, tamilwords);
        } else {
            if (Medicine.containsKey(docid)) {
                String value = Medicine.get(docid).toString();
                if (!value.contains(tamilwords)) {
                    value = value + " " + tamilwords;
		System.out.println("docid"+docid+"\t"+value);
               Medicine.put(docid, value);

                }
            } else {
             Medicine.put(docid, tamilwords);
            }
System.out.println("docid and value for medicine"+Medicine.get(docid));
        }

//return templates;
    }

    /**
     *This method define for getting places from documents
     * @param tamilWord is a String. This String has  contains a tamilword
     * @param docid is a String. This String has contains a file name of Documents
     * @param constraint is a String. This String has contains a Constraints for words
     */


    /**
     *This method define for put the places in PlaceLists
     * @param constraint is a String. This String has contains a Constraints for words
     * @param docid is a String. This String has contains a file name of Documents
     */
  
    /**
     *This method define for Traverse the Toconcpet in unl-graphPlace
     * @param conceptToNode is a Object. This Object has contains ConceptToNode details
     * @param finalImp is a object.This Object has contains a Object of unl-graph
     * @param conceptnode is a Object. This Object has contains Conceptnode details
     * @param i is Integer. This integer has contains which unl-graph count
     * @param headNode is also one of the object. This Object has contains HeadNode Deatails
     */
   
  /*   *This method define for found the unit in Conceptnode
     * @param conceptnode is a Object. This Object has contains Conceptnode details
     * @param headNode is also one of the object. This Object has contains HeadNode Deatails
     * @return values is a object. This object has contanins Concpetnode
     
    public ConceptNode Unit(ConceptNode conceptNode, HeadNode head) {
        ConceptNode traverseNode = new ConceptNode();
        traverseNode = head.colnext;
        while (traverseNode != null) {
            if (traverseNode == conceptNode) {
                ////System.out.println("Welcome");
                break;
            }
            traverseNode = traverseNode.getColNext();
        }
        return conceptNode;
    }
*/
    /**
     *This method define for call the generator
     * @param words is a String. This string has contains word
     * @param rule is a String. This string has contains rule meand(பெயர்ச்சொல்+வேற்றுமைஉருபு)
     * @param caseend is String. This string has contains a word as இலிருந்து
     * @return
     */
    public String callgenerator(String words, String rule, String caseend) {
        String returnwords = "";
        try {
            word_noun noun = new word_noun();
            StringBuffer genwords = noun.NounCMGen1(words, rule, caseend);
            returnwords = genwords.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnwords;
    }

    /**
     *This method define for Add The templates for each documents
     */
    public void AddTemplatewords() {
try{


Iterator itr = docid.iterator();
//System.out.println("HashSet contains : ");
while(itr.hasNext()){
Object docids=itr.next();
System.out.println("docids in new iter:"+docids.toString());
System.out.println("values for disease in new iter:"+Disease.get(docids.toString()));
System.out.println("values for treatment in new iter:"+Treatment.get(docids.toString()));
System.out.println("values for medicine in new iter:"+Medicine.get(docids.toString()));
//}

   //   for (Object docids : docid) {
System.out.println("the docids in  before if add template words DISEASE:"+docids.toString());
            String summarywords = "";

            if (Disease.get(docids.toString()) != null) {
System.out.println("the docids in add template words DISEASE:"+docids);
System.out.println("the Disease string:"+Disease.get(docids.toString()));
                String[] TemplateWords = (String[]) Templates.get("Disease");
               summarywords =  TemplateWords[0]+" "+summarywords + Disease.get(docids.toString()) +" "+ TemplateWords[1] +".  ";
  //summarywords = summarywords + Disease.get(docids.toString()) + ".  ";
            }
            if (Treatment.get(docids.toString()) != null) {
                String[] TemplateWords = (String[]) Templates.get("Treatment");
               summarywords = summarywords + Treatment.get(docids.toString()) + TemplateWords[0] + ".  ";
		//summarywords = summarywords + Treatment.get(docids.toString()) + ".  ";
            }
            if (Medicine.get(docids.toString()) != null) {
                String[] TemplateWords = (String[]) Templates.get("Medicine");
              summarywords = summarywords + Medicine.get(docids.toString()) + TemplateWords[0] + ".  ";
		//summarywords = summarywords + Treatment.get(docids.toString()) +  ".  ";
            }
    
     	 //  Tempwordcount = wordCountcheck(Tempwordcount);
       		 //Tempwordcount++;
System.out.println("the docids in add template words:"+docids+"summary for the docid:"+summarywords);
            summaryput(docids.toString(), summarywords);
        }
}catch(Exception e){e.printStackTrace();}
    }

    /**
     *This method define for wordCount for Templates
     * @param count is a integer. This integer has contais templateword counts
     * @return value is integer. This integer value has contains count
     */
 public int wordCountcheck(int count) {
        if (count == 4) {
            count = 0;
        }
        return count;
    }

    /**
     *This method define for put the summary for each documents
     * @param docid is a String. This String has contains a file name of Documents
     * @param summarywords is a String. This string has contaisn summary words for document
     */
    public void summaryput(String docids, String summarywords) {
	//System.out.println("DOCIDin put"+ docids+"\t"+summarywords);
        if (summarywords.length() != 0) {
            summary.put(docids, summarywords);
        } else {
            EmptySummary.add(docids);
        }
    }

    /**
     *This method defien for Summary Testing
     */
    public void hashTest() {
        Set key = summary.keySet();
        for (Object docids : key) {
           System.out.println("Docid" + docids.toString());
           System.out.println("Summarys" + summary.get(docids.toString()));
        }
    }

    /**
     *This method define for write the summary to Serialize file
     */
    public void summaryWrite() {
        File file = new File(templateHome + "newsummary");
        file.mkdir();
        ObjectOutputStream objectOutputStream = IOHelper.getObjectOutputStream(templateHome + "newsummary/summary.ser");
        IOHelper.writeObjectToOutputStream(objectOutputStream, summary);
        IOHelper.closeObjectOutputStream(objectOutputStream);
    }

 

    /**
     *This method define for write Empty Summar document to text file
     */
    public void EmptySummaryFileWrite() {
        BufferedWriter bufferedWriter = IOHelper.getBufferedWriter(templateHome + "newsummary/summaryEmpty.txt");
        for (Object s : EmptySummary) {
            IOHelper.writeLineToBufferedWriter(bufferedWriter, s.toString());
        }
        IOHelper.closeBufferedWriter(bufferedWriter);
    }

    public static void main(String args[]) {
        OfflineSummary summary1 = new OfflineSummary();
        summary1.callFunctions();
    }
}
