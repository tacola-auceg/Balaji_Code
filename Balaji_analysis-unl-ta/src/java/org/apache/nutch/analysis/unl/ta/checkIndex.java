package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.snippetgeneration.unl.ta;

//import org.apache.nutch.index.unl.*;

public class checkIndex {

    public BinarySearchTree Tree = new BinarySearchTree();
    public offline load = new offline();

    public void Indexload() {
        callcrc();
        callcr();
        callc();
    }

    public void callcrc() {
        Tree = load.getcrc();
        TraverseIndex();
    }

    public void callcr() {
        Tree = load.getcr();
        TraverseIndex();
    }

    public void callc() {
        Tree = load.getc();
        TraverseIndex();
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
                while (docNode != null) {
                    DocNodeTraverse(docNode);
                    docNode = docNode.docnext;
                }
                binaryNextNode = binaryNode.linknext;
                if (binaryNextNode != null) {
                    DocNode docNode1 = new DocNode();
                    while (binaryNextNode != null) {
                        BinaryNextNodeTraverse(binaryNextNode);
                        docNode1 = binaryNextNode.docnext;
                        while (docNode1 != null) {
                            DocNodeTraverse(docNode1);
                            docNode1 = docNode1.docnext;
                        }
                        binaryNextNode = binaryNextNode.linknext;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void BinaryNodeTraverse(BinaryNode binaryNode) {
        System.out.println("\n");
        System.out.println("the concept" + "-" + binaryNode.con);
        //System.out.println("the relation" + "-" + binaryNode.rel + "\n");
        System.out.println("the toconcept" + "-" + binaryNode.tocon);
        System.out.println("the document id" + "-" + binaryNode.docid);
        System.out.println("the sentence id " + "-" + binaryNode.senid);
        //System.out.println("the weight of the index " + "-" + binaryNode.weight + "\n");
        //System.out.println("the ascii of the index " + "-" + binaryNode.asciival + "\n");
        //System.out.println("Freq count of actual tamil word " + "-" + binaryNode.frequency + "\n");
        System.out.println("tamil word  from concept" + "-" + binaryNode.ind_tamil);
        System.out.println("the tamil toconcept" + binaryNode.tamcon + "\n");
        //System.out.println("tamil word POS" + "-" + binaryNode.vbcheck + "\n");
        //System.out.println("tamil topos" + binaryNode.tovbcheck + "\n");
    }

    public void DocNodeTraverse(DocNode docNode) {
        System.out.println("\t From Tamil words" + docNode.gn_word);
        System.out.println("\t To Tamil words" + docNode.tamcon);
        System.out.println("\t the document id" + "-" + docNode.docid);
        System.out.println("\t the sentence id " + "-" + docNode.senid + "\n");
    }

    public void BinaryNextNodeTraverse(BinaryNextNode binaryNextNode) {
        System.out.println("\n");
        System.out.println("the concept" + "-" + binaryNextNode.con );
        //System.out.println("the relation" + "-" + binaryNextNode.rel + "\n");
        System.out.println("the toconcept" + "-" + binaryNextNode.tocon);
        System.out.println("the document id" + "-" + binaryNextNode.docid);
        System.out.println("the sentence id" + "-" + binaryNextNode.senid);
        //System.out.println("the weight of the index " + "-" + binaryNextNode.weight + "\n");
        //System.out.println("Freq count of actual tamil word " + "-" + binaryNextNode.frequency + "\n");
        System.out.println("tamil from concept " + "-" + binaryNextNode.ind_tamil);
        System.out.println("the tamil toconcept" + binaryNextNode.tamcon + "\n");
        //System.out.println("tamil word POS " + "-" + binaryNextNode.vbcheck + "\n");
        //System.out.println("tamil word topos" + "-" + binaryNextNode.tovbcheck + "\n");
    }

    public static void main(String[] args) {
        checkIndex chIndex = new checkIndex();
        chIndex.callc();
    }
}
