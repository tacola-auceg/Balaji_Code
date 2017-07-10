package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.snippetgeneration.unl.ta;

import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Set;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author root
 */
public class SnippetTest {

    public static boolean isSnippetLoaded = false;
    public static HashMap<String, HashMap<String, String[]>> snippets;
    public static HashMap<String, HashMap<String, String[]>> snippetcrc;

    public void loadSnippet() {
        if (isSnippetLoaded) {
            return;
        }
        System.out.println("Welcome");
        ObjectInputStream snippetReader = IOHelper.getObjectInputStream("./crawl-unl/newsnippet/snippet.ser");
        snippets = (HashMap<String, HashMap<String, String[]>>) IOHelper.readObjectFromInputStream(snippetReader);
        IOHelper.closeObjectInputStream(snippetReader);
        System.out.println("SnippetSize" + snippets.size());
        isSnippetLoaded = true;
    }

    public void snippetTestcrc() {
        System.out.println("Welcome To CRC Snippet Testing");
        Set key = snippetcrc.keySet();
        for (Object docid : key) {
            System.out.println(docid.toString());
            HashMap<String, String[]> hash = new HashMap<String, String[]>();
            hash = snippetcrc.get(docid.toString());
            Set hashkey = hash.keySet();
            for (Object constrains : hashkey) {
                System.out.print("\t" + constrains.toString() + "----->");
                String[] output = (String[]) hash.get(constrains.toString());
                System.out.println("\t" + output[0] + "\t" + output[1] + "\t" + output[2] + "\n");
            }
        }
    }

    public void snippetTestc(String docid) {
        System.out.println("Welcome To C Snippet Testing");
        ObjectInputStream snippetReader = IOHelper.getObjectInputStream("./crawl-unl/Snippets/" + docid + ".ser");
        HashMap<String, String[]> hash = (HashMap<String, String[]>) IOHelper.readObjectFromInputStream(snippetReader);
        IOHelper.closeObjectInputStream(snippetReader);
        Set hashkey = hash.keySet();
        for (Object constrains : hashkey) {
            System.out.print("\t" + constrains.toString() + "----->");
            String[] output = (String[]) hash.get(constrains.toString());
            System.out.println("\t" + output[0] + "\t" + output[1] + "\t" + output[2] + "\n");
        }
    }

    public void snippetStroreAlone() {
        File file = new File("./crawl-unl/Snippets");
        file.mkdir();
        System.out.println("Welcome to Snippet Writing");
        Set key = snippets.keySet();
        for (Object docid : key) {
            System.out.println("Filename" + docid.toString());
            HashMap<String, String[]> hash = new HashMap<String, String[]>();
            hash = snippets.get(docid.toString());
            ObjectOutputStream objectOutputStream = IOHelper.getObjectOutputStream("./crawl-unl/Snippets/" + docid.toString() + ".ser");
            IOHelper.writeObjectToOutputStream(objectOutputStream, hash);
            IOHelper.closeObjectOutputStream(objectOutputStream);
        }
    }

    public void getSnippet(String constraint) {
        Set key = snippets.keySet();
        for (Object s : key) {
            HashMap<String, String[]> snippetForDoc = new HashMap<String, String[]>();
            snippetForDoc = snippets.get(s.toString());
            String[] snippet = snippetForDoc.get(constraint);
            if (snippet == null) {
                System.out.println("Snippet not available");
            } else {
                System.out.print("Docid :" + s.toString() + "---->");
                System.out.println(snippet[0] + "-->" + snippet[1] + "--->" + snippet[2]);
            }
        }
    }

    public static void main(String[] args) {
        SnippetTest snippetTest = new SnippetTest();
        
        snippetTest.snippetTestc("d17232");
        
    }
}
