package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.snippetgeneration.unl.ta;

import java.io.BufferedReader;
//import org.apache.nutch.enconversion.unl.ta.*;
import org.apache.nutch.analysis.unl.ta.*;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;

/**
 * @author karthikeyan
 * @version 2.0
 * @since AUCEG
 */
public class Snippet {

    public HashMap<String, String[]> snippets = new HashMap<String, String[]>();
    public String globalDocid = "start";
    public static Configuration configuration = NutchConfiguration.create();
    public static String SummaryHome = configuration.get("UNLCrawl");
    public static String EnconversionHome = configuration.get("unl-Graph");
    public static String sentenceExtractionHome = configuration.get("SentenceExtraction");

    public void callFunctions() {
        createDirectory();
        unlgraphRead();
    }

    public void createDirectory() {
        File file = new File(SummaryHome + "Snippets");
        file.mkdir();
    }

    public void unlgraphRead() {
        ArrayList FilenameList = unlgraphGetFile();
        for (Object fileName : FilenameList) {
            System.out.println("GraphName-->" + fileName.toString());
            ObjectInputStream unlgraph = IOHelper.getObjectInputStream(fileName.toString());
            FinalLLImpl[] finalllImp = (FinalLLImpl[]) IOHelper.readObjectFromInputStream(unlgraph);
            Traverselnode(finalllImp);
        }
    }

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

    public void Traverselnode(FinalLLImpl[] finalImp) {
        System.out.println("Welcome to Traverse in unlgraph");
        ConceptNode conceptnode = new ConceptNode();
        HeadNode headnode = new HeadNode();
        for (int i = 0; i < finalImp.length; i++) {
            if (finalImp[i] != null) {
                headnode = finalImp[i].head;
                conceptnode = headnode.colnext;
                while (conceptnode != null) {
                    getSnippet(conceptnode.docid, conceptnode.uwconcept.toString(), conceptnode.gn_word.toString(), conceptnode.sentid.toString());
                    conceptnode = conceptnode.getColNext();
                }
            }
        }
    }

    public void getSnippet(String docid, String constraint, String tamilWord, String sentid) {
        boolean isNextDocument = checkDocuments(docid);
        int lineNumber = Integer.parseInt(sentid.substring(1, sentid.length()));
        if (isNextDocument == true) {
            if (!globalDocid.equals("start")) {
                SnippetWriteinFile();
            }
            globalDocid = docid;
            snippets = new HashMap<String, String[]>();
            getSnippetLine(docid, constraint, tamilWord, lineNumber);
        } else {
        	SnippetWriteinFile();
            getSnippetLine(docid, constraint, tamilWord, lineNumber);
        }
    }

    public void SnippetWriteinFile() {
        System.out.println("SnippetFileWriting----->" + globalDocid);
        ObjectOutputStream objectOutputStream = IOHelper.getObjectOutputStream(SummaryHome + "Snippets/" + globalDocid + ".ser");
        IOHelper.writeObjectToOutputStream(objectOutputStream, snippets);
        IOHelper.closeObjectOutputStream(objectOutputStream);
    }

    public boolean checkDocuments(String docid) {
        if (globalDocid.equals(docid)) {
            return false;
        }
        return true;
    }

    public void getSnippetLine(String docid, String constraint, String tamilword, int lineNumber) {
        String snippetLine = getLineFromFile(docid, lineNumber, tamilword);
        String[] output = Linesplit(snippetLine, tamilword, lineNumber);
        snippets = Snippetput(snippets, tamilword + constraint, output);
    }

    public HashMap<String, String[]> Snippetput(HashMap<String, String[]> hasputs, String key, String[] value) {
        if (!hasputs.containsKey(key)) {
            hasputs.put(key, value);
        }
        return hasputs;
    }

    public String getLineFromFile(String docid, int lineNumber, String Tamilword) {
        BufferedReader bufferedReader = IOHelper.getBufferedReader(sentenceExtractionHome + "Output/content/" + docid + ".txt");
        int linecount = 1;
        String line = null;
        while ((line = IOHelper.readLineFromBufferedReader(bufferedReader)) != null) {
            if (linecount == lineNumber) {
                line = SnippetArrangement(line, Tamilword);
                IOHelper.closeBufferedReader(bufferedReader);
                return line;
            }
            linecount++;
        }
        IOHelper.closeBufferedReader(bufferedReader);
        return "SnippetNotAvailable";
    }

    public String SnippetArrangement(String line, String tamilWord) {
        String[] words = line.split(" ");
        if (words.length < 10) {
            return line;
        }
        int startIndex = 0;
        int startSnippet = 0;
        int endSnippet = words.length;

        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(tamilWord)) {
                startIndex = i;
            }
        }
        if (startIndex - 5 > 0) {
            startSnippet = startIndex - 5;
        }
        if ((startSnippet + 15) < words.length) {
            endSnippet = startSnippet + 15;
        }
        String snippet = "";
        for (int i = startSnippet; i < endSnippet; i++) {
            snippet = snippet + " " + words[i];
        }
        return snippet.trim();
    }

    public String[] Linesplit(String line, String tamilwords, int lineNumber) {
        String[] output = new String[3];
        String head = "", body = "";
        String[] linespt = line.split(" ");
        for (int i = 0; i < linespt.length; i++) {
            if (i == 4) {
                break;
            }
            head = head + linespt[i] + " ";
        }
        if (head.length() < line.length()) {
            body = line.substring(head.length() - 1, line.length() - 1);
        } else {
            body = head;
        }
        head = Highlightwords(head, tamilwords);
        body = Highlightwords(body, tamilwords);
        output[0] = head;
        output[1] = body;
        output[2] = String.valueOf(lineNumber);
        return output;
    }

    public String Highlightwords(String input, String highlightwords) {
        String[] spt = highlightwords.split(" ");
        for (String s : spt) {
            input = input.replace(s, "<b><font color=\"green\">" + s + "</font></b>");
        }
        return input;
    }

    public static void main(String[] args) {
        Snippet snippet = new Snippet();
        snippet.callFunctions();
    }
}
