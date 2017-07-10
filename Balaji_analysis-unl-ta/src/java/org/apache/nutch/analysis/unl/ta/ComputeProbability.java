
package org.apache.nutch.analysis.unl.ta;

//import org.apache.nutch.analysis.unl.ta.Analyser;

import java.lang.*;
import java.util.*;
import java.io.*;

/**
 * 
 */
/**
 * @author Balaji
 * 
 */
public class ComputeProbability {

    Lexical_Probability lpc = new Lexical_Probability();
    Transition_Probability tpc = new Transition_Probability();

    String result = "";
    String getFirst;
    String getValue;
    String getNext;
    String semFirst;
    String semValue;
    String semNext;
    String relnFirst;
    String relnValue;
    String relnNext;

    ArrayList getProb = new ArrayList();
    int a = 0, b = 0, c = 0, d = 0;
//	String frmEnd = "",plcEnd = "",plfTag = "", plcTag = "";
    String tword;
    String hword;
    String uword;
    String posword;
    String caseEnd;
    String unlReln;

    int cprob = 0;
    int cpprob = 0;
    int tprob = 0;
    int tfprob = 0;
    int sprob = 0;
    int sfprob = 0;
    int rprob = 0;
    int cp_prob = 0;
    int r_prob = 0;
    int rl_prob = 0;
    int sem_prob = 0;

    int counter;

    double lexProb;
    double transProb;
    double semProb;
    double relnProb;
    double reln_Prob;   
    double pattern_Prob;

    Hashtable caseEntry = new Hashtable();
    Hashtable semEntry = new Hashtable();
    Hashtable case_pos_Entry = new Hashtable();
    Hashtable trans_freq_Entry = new Hashtable();
    Hashtable semantics_Entry = new Hashtable();
    Hashtable RelnEntry = new Hashtable();
    Hashtable Relation_Entry = new Hashtable();

    PrintStream lex_print;
    PrintStream trans_print;
    PrintStream sem_print;
    PrintStream reln_print;
    PrintStream reln_trans_print;
    PrintStream patterns_print;

    public void init() {
        tword = "";
        hword = "";
        uword = "";
        posword = "";
        caseEnd = "";
        unlReln = "";

        getFirst = "";
        getValue = "";
        getNext = "";

        semFirst = "";
        semValue = "";
        semNext = "";

        relnFirst = "";
        relnValue = "";
        relnNext = "";
    }

    public void InitialProbability(String st) {
        init();
        int i = 0;
        counter = 0;
        //    int count = 1;
        //	String caseEnd = "";
        //      System.out.println("St:"+st);
        StringTokenizer strToken1 = new StringTokenizer(st, "@");
        int cntwrds = strToken1.countTokens();
        while (i < cntwrds) {
           counter++;
            String getExp = strToken1.nextToken();
            //    System.out.println("getExp:"+getExp);
            StringTokenizer strToken2 = new StringTokenizer(getExp, "+");
            tword = strToken2.nextToken();
            hword = strToken2.nextToken();
            uword = strToken2.nextToken();
            posword = strToken2.nextToken();
            caseEnd = strToken2.nextToken();
            unlReln = strToken2.nextToken();
            
            lpc.getCaseProbability();
            lpc.case_pos_Probability();
            lpc.getsem_Probability();
            lpc.getReln_Probability();

            tpc.cooccur_Probability();
            tpc.semantics_Probability();
            
            i++;
        }
        //      System.out.println("caseEntry:"+caseEntry);
        //      System.out.println("case_pos_Entry:"+case_pos_Entry);
        //	return result;
    }
    public void writeintoFile(){
        try{
            lex_print  = new PrintStream(new FileOutputStream("./resource/unl/RE/BWA_Prob/CSV/LexProb.csv"));
            trans_print = new PrintStream(new FileOutputStream("./resource/unl/RE/BWA_Prob/CSV/TransProb.csv"));
            sem_print = new PrintStream(new FileOutputStream("./resource/unl/RE/BWA_Prob/CSV/semanticsProb.csv"));
            reln_print  = new PrintStream(new FileOutputStream("./resource/unl/RE/BWA_Prob/CSV/RelnProb.csv"));
            reln_trans_print  = new PrintStream(new FileOutputStream("./resource/unl/RE/BWA_Prob/CSV/Reln_Trans_Prob.csv"));
            patterns_print  = new PrintStream(new FileOutputStream("./resource/unl/RE/BWA_Prob/CSV/Pattern_Prob.csv"));

            lex_print.println("caseEnding / Total Occurences$caseEnding with POS/Total occurences$Lexical Probability");
            trans_print.println("caseEnding / Total Occurences$caseEnding with POS/Total occurences$Transition Probability");
            sem_print.println("Semantics / Total Occurences$Semantics with POS/Total occurences$Transition Probability");
            reln_print.println("caseEnding with POS / Total Occurences$caseEnding with Relation/Total occurences$Relation Probability");
            reln_trans_print.println("caseEnding with POS and Reln / Total Occurences$caseEnding with pos/Total occurences$Relation_Transition Probability");
            patterns_print.println("caseEnding with POS and UWword / Total Occurences$caseEnding with pos and UWword/Total occurences$Patterns Probability");

        }catch(Exception e){

        }


    }
    /** This class computes the Lexical Probabilities of Morphological suffixes, Pos and Semantics**/
    class Lexical_Probability {

    	/**
    	 * Total occurrences of Morphological Suffixes
    	 */
        public void getCaseProbability() {
            int count = 1;
            if (caseEntry.isEmpty()) {
                caseEntry.put(caseEnd, count);
            } else if (caseEntry.containsKey(caseEnd)) {
                Object cnt = caseEntry.get(caseEnd);
                int cnt1 = (Integer) cnt;
                cnt1++;
                caseEntry.put(caseEnd, cnt1);
            } else {
                count = 1;
                caseEntry.put(caseEnd, count);
            }
        }     
        
        /**
    	 * Total occurrences of Morphological Suffixes with POS 
    	 */
        public void case_pos_Probability() {
            int count = 1;
            String get_entry = caseEnd + ":" + posword;
            if (case_pos_Entry.isEmpty()) {
                case_pos_Entry.put(get_entry, count);
            } else if (case_pos_Entry.containsKey(get_entry)) {
                Object cnt = case_pos_Entry.get(get_entry);
                int cnt1 = (Integer) cnt;
                cnt1++;
                case_pos_Entry.put(get_entry, cnt1);
            } else {
                count = 1;
                case_pos_Entry.put(get_entry, count);
            }
        }
        
        /**
    	 * Total occurrences of UNL Constraints
    	 */
        public void getsem_Probability() {
            int count = 1;
            if (semEntry.isEmpty()) {
                semEntry.put(uword, count);
            } else if (semEntry.containsKey(uword)) {
                Object cnt = semEntry.get(uword);
                int cnt1 = (Integer) cnt;
                cnt1++;
                semEntry.put(uword, cnt1);
            } else {
                count = 1;
                semEntry.put(uword, count);
            }
        }
        /**
    	 * Total occurrences of UNL Relations with Morphological suffixes and POS   
    	 */
        public void getReln_Probability(){
            int count = 1;
             String get_entry = caseEnd + ":"+ posword + ":" + uword;
            if(RelnEntry.isEmpty()){
                RelnEntry.put(get_entry, count);
            }else if (RelnEntry.containsKey(get_entry)) {
                Object cnt = RelnEntry.get(get_entry);
                int cnt1 = (Integer) cnt;
                cnt1++;
                RelnEntry.put(get_entry, cnt1);
            } else {
                count = 1;
                RelnEntry.put(get_entry, count);
            }
        }
        /**
         * Frequency of Morphological Suffixes with the POS
         * Number of times the morphological suffixes occur with the POS / Total occurrences of Morphological Suffixes
         */
        public void getCase_Frequency() {
            //     System.out.println("Inside Frequency:");
            Enumeration e1 = caseEntry.keys();
            while (e1.hasMoreElements()) {
                String key1 = (String) e1.nextElement();
                //    System.out.println(key1 + " : " + caseEntry.get(key1));
                Enumeration e2 = case_pos_Entry.keys();
                while (e2.hasMoreElements()) {
                    String key2 = (String) e2.nextElement();
                    //       System.out.println(key2 + " : " + case_pos_Entry.get(key2));
                    if (key2.contains(key1)) {
                        cpprob = (Integer) case_pos_Entry.get(key2);
                        cprob = (Integer) caseEntry.get(key1);
                        compute_lexicalProbability();
                    /**    lex_print.println("caseEnding / Total Occurences:"+"\t"+key1+"--->"+cprob);
                        lex_print.println("caseEnding with POS/Total occurences:"+"\t"+key2+"--->"+cpprob);
                        lex_print.println("Lexical Probability:"+"\t"+lexProb+"\n");*/
                        lex_print.println(key1+":"+cprob+"$"+key2+":"+cpprob+"$"+lexProb);
                    }
                }
            }
        }
        /**
         * Frequency of UNL Relations with Morphological Suffixes and POS 
         * Number of times the UNL Relations occur with morphological suffixes and POS / Total occurrences of Morphological Suffixes with POS
         */
        public void getReln_Frequency(){
              //     System.out.println("Inside Frequency:");
            Enumeration e1 = case_pos_Entry.keys();
            while (e1.hasMoreElements()) {
                String key1 = (String) e1.nextElement();
                //    System.out.println(key1 + " : " + caseEntry.get(key1));
                Enumeration e2 = RelnEntry.keys();
                while (e2.hasMoreElements()) {
                    String key2 = (String) e2.nextElement();
                    //       System.out.println(key2 + " : " + case_pos_Entry.get(key2));
                    if (key2.contains(key1)) {
                        rprob = (Integer) RelnEntry.get(key2);
                        cpprob = (Integer) case_pos_Entry.get(key1);
                        compute_lexicalProbability();
                    /**    reln_print.println("caseEnding / Total Occurences:"+"\t"+key1+"--->"+cpprob);
                        reln_print.println("caseEnding with Relation/Total occurences:"+"\t"+key2+"--->"+rprob);
                        reln_print.println("Relation Probability:"+"\t"+relnProb+"\n"); */
                        reln_print.println(key1+":"+cpprob+"$"+key2+":"+rprob+"$"+relnProb);
                    }
                }
            }
        }  
        /**
         * Computing the Probabilities
         */
        public void compute_lexicalProbability() {
        //    System.out.println("cpprob:" + cpprob);
       //     System.out.println("cprob:" + cprob);
            lexProb = (double) cpprob / cprob;
            relnProb = (double) rprob / cpprob;
        //    System.out.println("lexProb:" + lexProb);
        }
    }
    /**
     * Transition Probablities
     * @author ubuntu
     *
     */
    class Transition_Probability{
        public void cooccur_Probability(){
            if(counter == 1){
                getFirst = caseEnd+":"+posword;
         //       System.out.println("getFirst:"+getFirst);

            }else if(counter > 1){
                getNext = caseEnd+":"+posword;
                getValue = getFirst+"@"+getNext;
          //      System.out.println("getValue:"+getFirst+"@"+getNext);
                cooccur_Frequency();
            }
        }       
        public void cooccur_Frequency(){
            int count1 = 1;
            if(trans_freq_Entry.isEmpty()){
                trans_freq_Entry.put(getValue, count1);
            }else if(trans_freq_Entry.containsKey(getValue)){
                Object cnt = trans_freq_Entry.get(getValue);
                int cnt2 = (Integer)cnt;
                cnt2++;
                trans_freq_Entry.put(getValue, cnt2);
            }else{
                count1 = 1;
                trans_freq_Entry.put(getValue, count1);
            }
       //     System.out.println("trans_freq:"+trans_freq_Entry);
        }
        public void getcooccur_Frequency(){
              Enumeration e1 = caseEntry.keys();
            while (e1.hasMoreElements()) {
                String key1 = (String) e1.nextElement();
                //    System.out.println(key1 + " : " + caseEntry.get(key1));
                Enumeration e2 = trans_freq_Entry.keys();
                while (e2.hasMoreElements()) {
                    String key2 = (String) e2.nextElement();
                   //        System.out.println(key2 + " : " + trans_freq_Entry.get(key2));
                    if (key2.contains(key1)) {
                        tfprob = (Integer) trans_freq_Entry.get(key2);
                        tprob = (Integer) caseEntry.get(key1);
                        compute_transitionProbability();
                    /**    trans_print.println("caseEnding / Total Occurences:"+"\t"+key1+"--->"+tprob);
                        trans_print.println("caseEnding with POS/Total occurences:"+"\t"+key2+"--->"+tfprob);
                        trans_print.println("Transition Probability:"+"\t"+transProb+"\n"); */
                        trans_print.println(key1+":"+tprob+"$"+key2+":"+tfprob+"$"+transProb);
                    }
                }
            }
        }
         public void semantics_Probability(){
              if(counter == 1){
                semFirst = caseEnd+":"+posword+":"+uword;
         //       System.out.println("getFirst:"+getFirst);

            }else if(counter > 1){
                semNext = caseEnd+":"+posword+":"+uword;
                semValue = semFirst+"@"+semNext;
          //      System.out.println("getValue:"+getFirst+"@"+getNext);
                semantics_Frequency();
            }
        }
        public void semantics_Frequency(){
             int count2 = 1;
            if(semantics_Entry.isEmpty()){
                semantics_Entry.put(semValue, count2);
            }else if(semantics_Entry.containsKey(semValue)){
                Object cnt = semantics_Entry.get(semValue);
                int cnt3 = (Integer)cnt;
                cnt3++;
                semantics_Entry.put(semValue, cnt3);
            }else{
                count2 = 1;
                semantics_Entry.put(semValue, count2);
            }
       //     System.out.println("trans_freq:"+trans_freq_Entry);
        }
         public void getsemantics_Frequency(){
              Enumeration e1 = caseEntry.keys();
            while (e1.hasMoreElements()) {
                String key1 = (String) e1.nextElement();
                //    System.out.println(key1 + " : " + caseEntry.get(key1));
                Enumeration e2 = semantics_Entry.keys();
                while (e2.hasMoreElements()) {
                    String key2 = (String) e2.nextElement();
                   //        System.out.println(key2 + " : " + trans_freq_Entry.get(key2));
                    if (key2.contains(key1)) {
                        sfprob = (Integer) semantics_Entry.get(key2);
                        sprob = (Integer) caseEntry.get(key1);
                        compute_transitionProbability();
                    /**    sem_print.println("semantics / Total Occurences:"+"\t"+key1+"--->"+sprob);
                        sem_print.println("semantics with POS/Total occurences:"+"\t"+key2+"--->"+sfprob);
                        sem_print.println("Transition Probability:"+"\t"+semProb+"\n"); */
                        sem_print.println(key1+":"+sprob+"$"+key2+":"+sfprob+"$"+semProb);
                    }
                }
            }
        }
  
         public void getRelation_Frequency(){
              Enumeration e1 = RelnEntry.keys();
            while (e1.hasMoreElements()) {
                String key1 = (String) e1.nextElement();
            //    System.out.println(key1 + " : " + RelnEntry.get(key1));
                Enumeration e2 = case_pos_Entry.keys();
                while (e2.hasMoreElements()) {
                    String key2 = (String) e2.nextElement();
                //    System.out.println(key2 + " : " + case_pos_Entry.get(key2));
                    if (key1.contains(key2)) {
                        r_prob = (Integer) RelnEntry.get(key1);
                        cp_prob = (Integer) case_pos_Entry.get(key2);
                        compute_transitionProbability();                   
                        reln_trans_print.println(key1+":"+r_prob+"$"+key2+":"+cp_prob+"$"+reln_Prob);
                    }
                }
            }
        }   
         
         public void patterns_Frequency(){
           Enumeration e1 = RelnEntry.keys();
           while (e1.hasMoreElements()) {
               String key1 = (String) e1.nextElement();
           //    System.out.println(key1 + " : " + RelnEntry.get(key1));
               Enumeration e2 = semantics_Entry.keys();
               while (e2.hasMoreElements()) {
                   String key2 = (String) e2.nextElement();
               //    System.out.println(key2 + " : " + case_pos_Entry.get(key2));
                   if (key2.contains(key1)) {
                       rl_prob = (Integer) RelnEntry.get(key1);
                       sem_prob = (Integer) semantics_Entry.get(key2);
                       compute_transitionProbability();                   
                       patterns_print.println(key1+":"+rl_prob+"$"+key2+":"+sem_prob+"$"+pattern_Prob);
                   }
               }
           }
       }   
        public void compute_transitionProbability(){
        //    System.out.println("tfprob:" + tfprob);
        //    System.out.println("tprob:" + tprob);
            transProb = (double)tfprob/tprob;
            semProb = (double)sfprob/sprob;
            reln_Prob = (double)r_prob/cp_prob;
            pattern_Prob = (double)sem_prob/rl_prob;
        //    System.out.println("transProb:"+transProb);
        }
    }

    public StringBuffer fineTuneDocs(StringBuffer docbuff) {
        String getBuff = docbuff.toString();
        StringBuffer sBuff = new StringBuffer();
        if (getBuff.contains("+@")) {
            //       System.out.println("getbuff:"+getBuff);
            getBuff = getBuff.replace("+@", "+None@");
            getBuff = getBuff.replace("<.", "");
            getBuff = getBuff.replace("+src@", "+plc#src@");
            //   System.out.println("gbuff:"+getBuff);
            sBuff.append(getBuff);
        }
        //   System.out.println("sbuff:"+sBuff);
        return sBuff;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String s = null;
        FileReader fr = null;
        String temp = null;
        ArrayList al = new ArrayList();
        String output = "";
        String str = "";
        String sent = "", recv = "", token = "", fn = "";
        int temp1 = 0, j = 0;
        ComputeProbability cp = new ComputeProbability();
        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("./resource/unl/RE/tagDocs/Output/nonemptyfiles.txt"));
            while ((s = br.readLine()) != null) {
                fn = "./resource/unl/RE/tagDocs/Output/" + s;
                // tag_file = s;
                // System.out.println("fn:" + fn);
                StringBuffer docbuff = new StringBuffer();
                StringBuffer docbuff1 = new StringBuffer();
                fr = new FileReader(fn);

                //	 System.out.println("fr:"+fr);
                BufferedReader buff = new BufferedReader(new FileReader(fn));
                while ((str = buff.readLine()) != null) {
                    docbuff.append(str);
                    docbuff1 = cp.fineTuneDocs(docbuff);
                }

                //	 System.out.println("docbuff1:"+docbuff1);
                StringTokenizer sentToken1 = new StringTokenizer(docbuff1.toString().trim(), ".", false);
                // l.loadTaggedData();
                // j = 0;
                while (sentToken1.hasMoreTokens()) {
                    sent = sentToken1.nextToken();
                    cp.InitialProbability(sent);
                }
                output = "";
            }
            //     System.out.println("GET_CASE:");
          //  System.out.println("caseEntry:" + cp.caseEntry);
          //  System.out.println("case_pos_Entry:" + cp.case_pos_Entry);
          //  System.out.println("trans_freq_Entry:" + cp.trans_freq_Entry);
            cp.writeintoFile();
            cp.lpc.getCase_Frequency();
            cp.lpc.getReln_Frequency();
            cp.tpc.getcooccur_Frequency();
            cp.tpc.getsemantics_Frequency();
            cp.tpc.getRelation_Frequency();
            cp.tpc.patterns_Frequency();
        } catch (Exception e) {
        }
    }
}
