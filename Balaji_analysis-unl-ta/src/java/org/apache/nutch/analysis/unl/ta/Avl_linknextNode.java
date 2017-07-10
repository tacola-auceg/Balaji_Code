package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.index.unl;

/**
 * version history**
 *version number:1.1
@author subalalitha
 *
 * This class stores all the indices in a particular unl graph.This is designed for forward indexing.
 * The purpose of forward indexing is used for Re-ranking.
 */
import java.io.*;

public class Avl_linknextNode implements Serializable {

    public Avl_linknextNode() {
        String con = null;
        String Reln_name = null;
        String Toconcept_name = null;
        int frequency = 0;
        String vbcheck = null;
        String gn_word = null;
        Avl_linknextNode next = null;
        String senid = null;
        int weight = 1;
        String tamcon = null;
    }
    public Comparable docid;//The document identifier of the UNL graph.
    public String concept_name;//The universal word1 
    public String Reln_name;//the UNL relation
    public String Toconcept_name;//The universal word2 which is connected to universal word1 throuh the UNL relation REln_name
    public int frequency;//The frequency of the tamil word which is calculated and passed form enconversion module.
    public String vbcheck;//POS tag of the universal word1.
    public String gn_word;//tamil word of the universal word1.
    public Avl_linknextNode next;//pointer to the next Avl_linknextNode.
    public String senid;
    public int weight;//weight of the universal word1 calculated using its sentence position and it's frequency in the UNL graph
    public String tamcon;//tamil word of the universal word2.
    public String topos;//POS tag of universal word2.
}
