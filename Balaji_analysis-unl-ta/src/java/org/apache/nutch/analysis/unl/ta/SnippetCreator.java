package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.snippetgeneration.unl.ta;

import java.io.BufferedReader;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
//import org.apache.nutch.index.unl.*;
import org.apache.nutch.util.NutchConfiguration;
import org.apache.hadoop.conf.Configuration;


public class SnippetCreator {

    public BinarySearchTree Tree = new BinarySearchTree();
    public offline load = new offline();
    public HashMap<String, HashMap<String, String[]>> snippetc;
    public HashMap<String, HashMap<String, String[]>> snippetcrc;
    public static Configuration configuration = NutchConfiguration.create();
    public static String sentenceExtractionHome = configuration.get("SentenceExtraction");
    public static String snippetgenerationHome = configuration.get("UNLCrawl");
    public boolean isconceptonly = true;

    public void Indexload() {
        System.gc();
        File file=new File(snippetgenerationHome+"newsnippet");
        file.mkdir();
        snippetc = new HashMap<String, HashMap<String, String[]>>();
        snippetcrc = new HashMap<String, HashMap<String, String[]>>();
        //callcrc();
        callc();
    }

    public void callc() {
        Tree = load.getc();
        isconceptonly = true;
        TraverseIndex();
        Snippetwrite(snippetgenerationHome+"newsnippet/snippet.ser", snippetc);
        System.gc();
    }

    public void callcrc() {
        Tree = load.getcrc();
        isconceptonly = false;
        TraverseIndex();
        Snippetwrite(snippetgenerationHome+"newsnippet/snippetcrc.ser", snippetcrc);
        System.gc();
    }

    public void TraverseIndex() {
        try {
            if (isEmpty(Tree.root)) {
                System.out.println("This is Empty Roots");
            } else {
                TraverseStart(Tree.root);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isEmpty(BinaryNode binaryNode) {
        return binaryNode == null;
    }

    public void TraverseStart(BinaryNode binaryNode) {
        if (binaryNode != null) {
            TraverseStart(binaryNode.left);
            Traversing(binaryNode);
            TraverseStart(binaryNode.right);
        }
    }

    public void Traversing(BinaryNode binaryNode) {
        DocNode docNode = new DocNode();
        try {
            docNode = binaryNode.docnext;
            if (docNode != null) {
                BinaryNextNode binaryNextNode = new BinaryNextNode();
                BinaryNodeTraverse(binaryNode);
                DocNodeTraverse(docNode, binaryNode.con, binaryNode.tocon);
                binaryNextNode = binaryNode.linknext;
                BinaryNextNodeTraverse(binaryNextNode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void BinaryNodeTraverse(BinaryNode binaryNode) {
        System.out.println("\n");
        System.out.println("the concept" + "-" + binaryNode.con);
        System.out.println("the toconcept" + "-" + binaryNode.tocon);
        System.out.println("the document id" + "-" + binaryNode.docid);
        System.out.println("the sentence id " + "-" + binaryNode.senid);
        System.out.println("tamil word  from concept" + "-" + binaryNode.ind_tamil);
        System.out.println("the tamil toconcept" + binaryNode.tamcon + "\n");
        SnippetGenerator(binaryNode.con, binaryNode.tocon, binaryNode.ind_tamil, binaryNode.tamcon, String.valueOf(binaryNode.docid), binaryNode.senid);
    }

    public void DocNodeTraverse(DocNode docNode, String fromCon, String toCon) {
        while (docNode != null) {
            DocNodeget(docNode, fromCon, toCon);
            docNode = docNode.docnext;
        }
    }

    public void DocNodeget(DocNode docNode, String fromCon, String toCon) {
        System.out.println("\t From constrains" + fromCon);
        System.out.println("\t From Tamil words" + docNode.gn_word);
        System.out.println("\t To constrains" + toCon);
        System.out.println("\t To Tamil words" + docNode.tamcon);
        System.out.println("\t the document id" + "-" + docNode.docid);
        System.out.println("\t the sentence id " + "-" + docNode.senid + "\n");
        SnippetGenerator(fromCon, toCon, docNode.gn_word, docNode.tamcon, String.valueOf(docNode.docid), docNode.senid);
    }

    public void BinaryNextNodeTraverse(BinaryNextNode binaryNextNode) {
        if (binaryNextNode != null) {
            DocNode docNode = new DocNode();
            while (binaryNextNode != null) {
                BinaryNextNodeget(binaryNextNode);
                docNode = binaryNextNode.docnext;
                DocNodeTraverse(docNode, binaryNextNode.con, binaryNextNode.tocon);
                binaryNextNode = binaryNextNode.linknext;
            }
        }
    }

    public void BinaryNextNodeget(BinaryNextNode binaryNextNode) {
        System.out.println("\n");
        System.out.println("the concept" + "-" + binaryNextNode.con);
        System.out.println("the toconcept" + "-" + binaryNextNode.tocon);
        System.out.println("the document id" + "-" + binaryNextNode.docid);
        System.out.println("the sentence id" + "-" + binaryNextNode.senid);
        System.out.println("tamil from concept " + "-" + binaryNextNode.ind_tamil);
        System.out.println("the tamil toconcept" + binaryNextNode.tamcon + "\n");
        SnippetGenerator(binaryNextNode.con, binaryNextNode.tocon, binaryNextNode.ind_tamil, binaryNextNode.tamcon, String.valueOf(binaryNextNode.docid), binaryNextNode.senid);
    }

    public void SnippetGenerator(String fromCon, String toCon, String fromTamil, String toTamil, String docid, String sentid) {
        if (isconceptonly) {
            ConceptonlySnippet(fromCon, fromTamil, docid, sentid);
        } else {
            CRCSnippet(fromCon, toCon, fromTamil, toTamil, docid, sentid);
        }
    }

    public void ConceptonlySnippet(String fromCon, String fromTamil, String docid, String sentid) {
        int lineNumber = getLineNumber(sentid);
        String line = getSnippet(docid, lineNumber, fromTamil);
        String[] output = Linesplit(line, fromTamil,lineNumber);
        snippetc = snippetput(snippetc, "d" + docid, output, fromTamil + fromCon);
    }

    public void CRCSnippet(String fromCon, String toCon, String fromTamil, String toTamil, String docid, String sentid) {
        int lineNumber = getLineNumber(sentid);
        String line = getSnippet(docid, lineNumber, fromTamil);
        String[] output = Linesplit(line, fromTamil + " " + toTamil,lineNumber);
        snippetcrc = snippetput(snippetcrc, "d" + docid, output, fromTamil + fromCon + toTamil + toCon );
    }

    public int getLineNumber(String sentid) {
        System.out.println(sentid);
        int lineNumber = Integer.parseInt(sentid);
        sentid = Integer.toBinaryString(lineNumber);
        StringBuffer stringBuffer = new StringBuffer(sentid);
        sentid = stringBuffer.reverse().toString();
        lineNumber = sentid.indexOf("1");
        if (lineNumber == -1) {
            return 1;
        } else {
            return lineNumber + 1;
        }
    }

    public HashMap<String, HashMap<String, String[]>> snippetput(HashMap<String, HashMap<String, String[]>> snippet, String docid, String[] output, String constrains) {
        HashMap<String, String[]> Snippet = new HashMap<String, String[]>();
        Snippet.put(constrains, output);
        Set key = snippet.keySet();
        if (key.contains(docid)) {
            HashMap<String, String[]> snippets = snippet.get(docid);
            snippets.putAll(Snippet);
            snippet.put(docid, snippets);
        } else {
            snippet.put(docid, Snippet);
        }
        return snippet;
    }

    public String getSnippet(String docid, int lineNumber, String Tamilword) {
        System.out.println(lineNumber);
        BufferedReader bufferedReader = IOHelper.getBufferedReader(sentenceExtractionHome+"Output/content/" + "d" + docid + ".txt");
        int linecount = 1;
        String line = null;
        while ((line = IOHelper.readLineFromBufferedReader(bufferedReader)) != null) {
            if (linecount == lineNumber) {
                line = returnSnippet(line, Tamilword);
                IOHelper.closeBufferedReader(bufferedReader);
                return line;
            }
            linecount++;
        }
        IOHelper.closeBufferedReader(bufferedReader);
        return "SnippetNotAvailable";
    }

    public String returnSnippet(String line, String tamilWord) {
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


    public String[] Linesplit(String line, String tamilwords,int lineNumber) {
        String[] output = new String[3];
        String head = "", body = "";
        String[] linespt = line.split(" ");
        for (int i = 0; i < linespt.length; i++) {
            if (i == 4) {
                break;
            }
            head = head + linespt[i]+" ";
        }
        if(head.length()<line.length()){
            body = line.substring(head.length() - 1, line.length() - 1);            
        }
        else{
            body=head;
        }
        head=Highlightwords(head, tamilwords);
        body=Highlightwords(body, tamilwords);
        output[0] = head;
        output[1] = body;
        output[2]=String.valueOf(lineNumber);
        return output;
    }

    public String Highlightwords(String input, String highlightwords) {
        String[] spt=highlightwords.split(" ");
        for(String s:spt){
            input = input.replace(s, "<b><font color=\"green\">" + s + "</font></b>");
        }
        return input;
    }

    public void Snippetwrite(String fileName, HashMap<String, HashMap<String, String[]>> snippet) {
        ObjectOutputStream objectOutputStream = IOHelper.getObjectOutputStream(fileName);
        IOHelper.writeObjectToOutputStream(objectOutputStream, snippet);
        IOHelper.closeObjectOutputStream(objectOutputStream);
    }

    public static void main(String[] args) {
        SnippetCreator snippetCreator = new SnippetCreator();
        snippetCreator.Indexload();
    }
}
