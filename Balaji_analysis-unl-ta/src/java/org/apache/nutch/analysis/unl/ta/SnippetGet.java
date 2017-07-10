package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.snippetgeneration.unl.ta;

import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Hashtable;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author karthikeyan
 * @version 2.0
 */
public class SnippetGet {

    public static boolean isSnippetLoaded = false;
    public HashMap<String, String[]> snippets;

    public void loadSnippet() {
        if (isSnippetLoaded) {
            return;
        }
        System.out.println("Welcome Snippet Reading");
        /*ObjectInputStream snippetReader = IOHelper.getObjectInputStream("./crawl-unl/snippetcrc.ser");
        snippetcrc = (HashMap<String, HashMap<String, String[]>>) IOHelper.readObjectFromInputStream(snippetReader);
        IOHelper.closeObjectInputStream(snippetReader);*/
        ObjectInputStream snippetcrcReader = IOHelper.getObjectInputStream("./crawl-unl/snippet.ser");
        snippets = (HashMap<String, String[]>) IOHelper.readObjectFromInputStream(snippetcrcReader);
        IOHelper.closeObjectInputStream(snippetcrcReader);
        isSnippetLoaded = true;
        System.out.println("Snippet Reader");
    }

    public void readSnippets(String docid){
        ObjectInputStream snippetReader = IOHelper.getObjectInputStream("./crawl-unl/Snippets/"+docid+".ser");
        snippets = (HashMap<String, String[]>) IOHelper.readObjectFromInputStream(snippetReader);
        IOHelper.closeObjectInputStream(snippetReader);
    }

    public String[] getSnippet(String docid, String constraint) {
        readSnippets(docid);
        String[] unsnippet=new String[3];
        String[] snippet =snippets.get(constraint);
        if (snippet == null) {
            unsnippet[0]="NoSnippetAvailable";
            unsnippet[1]="";
            unsnippet[2]="";
            return unsnippet;
        }
        return snippet;
    }
}
