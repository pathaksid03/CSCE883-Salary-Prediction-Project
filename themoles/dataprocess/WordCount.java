package themoles.dataprocess;
//This program prompts the user for the name of a file and then counts the
//occurrences of words in the file (ignoring case).  It then reports the
//frequencies using a cutoff supplied by the user that limits the output
//to just those words with a certain minimum frequency.

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

/**
 * ..
 * @author Ibrahim
 *
 */
public class WordCount {
 public static void main(String[] args) throws IOException {
     // open the file
    Scanner console = new Scanner(System.in);
    ArrayList<String> listOfKeyWords = new ArrayList<String>();
    String fileName = "./dataset/training/train_job_description.csv";
    String dictName = "./dataset/dictionary/webster_simple.dict";
    String stoplist = "./dataset/dictionary/stopwords_en.txt";
    String statFileName = "./dataset/dictionary/word_stat.txt";
    String originalDataset = "./dataset/training/Train_For_Modeling.csv";
	String trainSet = "./dataset/training/train/train_all.csv";
	String validationSet = "./dataset/training/valid/valid_all.csv";
    String fileOfKeyword = "./dataset/dictionary/keywords.txt";
    String idFile = "./dataset/training/all/ids_all.csv";
    double validRate = 0.3;
     
    DataProcesser processer = new DataProcesser();
 	TreeMap<String, Integer> wordStat = new TreeMap<String, Integer>();
    System.out.println("Working ...");
    Scanner input = new Scanner(new File(fileName));

    // count occurrences
    TreeMap<String, Integer> wordCounts = new TreeMap<String, Integer>();
    while (input.hasNext()) {
        String next = input.next().toLowerCase();
        if (!wordCounts.containsKey(next)) {
            wordCounts.put(next, 1);
        } else {
            wordCounts.put(next, wordCounts.get(next) + 1);
        }
    }
    
	System.out.println("Total words = " + wordCounts.size());
	 
	// remove non-words from wordCount list
	wordCounts = processer.clean(wordCounts, dictName);
	 
	// remove stop words from wordCount list
	wordCounts = processer.filter(wordCounts, stoplist);
	 
	System.out.println("Minimum number of occurrences for printing? ");
	int min = console.nextInt();
	
	// filter keywords
	for (Entry<String, Integer> entry : 
					wordCounts.entrySet()) {
		if (entry.getValue() >= min) {
			wordStat.put(entry.getKey(), entry.getValue());
			listOfKeyWords.add(entry.getKey());

		}
	}

    // sort by values, Ying
    System.out.println("\tSorting ...");
    wordStat = sortByValues(wordStat);
    System.out.println("\tDone sorting!");	
	
    // write wordStat into a text file
    processer.parse2Txt(wordStat, statFileName);
    
    System.out.println("listOfKeyWords: " + listOfKeyWords.size());
    System.out.println(listOfKeyWords.get(listOfKeyWords.size()-1));
//    worker.removeUnwantedRecords(originalDataset, listOfKeyWords);
    // save ids
    processer.splitIDsFromDataset(originalDataset, idFile);
    // split data set into train set and validation set
    processer.splitDataset(originalDataset, trainSet, validationSet, idFile, validRate);
    // reorganize train set by keywords
    processer.organizeDatasetByKey(trainSet, listOfKeyWords);
    // reorganize validation set by keywords
    processer.organizeDatasetByKey(validationSet, listOfKeyWords);
 }
 
// added by Ying Meng
static <String, Integer extends Comparable<Integer>> TreeMap<String, Integer> sortByValues(final Map<String, Integer> map) {
	    Comparator<String> valueComparator =  new Comparator<String>() {
	        public int compare(String key1, String key2) {
	            int compare = map.get(key2).compareTo(map.get(key1));
	            if (compare == 0) return 1;
	            else return compare;
	        }
	    };
	    TreeMap<String, Integer> sortedByValues = new TreeMap<String, Integer>(valueComparator);
	    sortedByValues.putAll(map);
	    return sortedByValues;
	}
}
