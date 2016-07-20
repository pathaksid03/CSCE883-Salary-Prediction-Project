package themoles.dataprocess;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * @author Ying Meng
 * Purpose: To provide methods of
 * 			1. building a set of words for given dictionary (.txt)
 * 			2. filtering stop words from given collection (TreeMap<String, Integer>)
 * 			3. writing a list of word statistics (TreeMap<String, Integer>) into a file (.txt)
 * 			4. splitting a big data set into train set and validation set, given a ratio
 * 			5. reorganizing the data set: splitting by keywords
 */
public class DataProcesser {

	public ArrayList<String> loadFile(String fileName) {
		ArrayList<String> content = new ArrayList<String>();
		File file = new File(fileName);
		Scanner fileScanner = null;

		try {
			fileScanner = new Scanner(file);
			while (fileScanner.hasNext()) {
				content.add(fileScanner.nextLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileScanner != null) {
				fileScanner.close();
			}
		}

		return content;
	}
	
	// build a set of words in given dictionary.
	public TreeSet buildWordList(String dictionary) {
		TreeSet wordlist = new TreeSet();
		String newWord;
		
		// building word set
		try {
			Scanner input = new Scanner(new File(dictionary));
			
			System.out.println("Building ...");
			while (input.hasNextLine()) {
				// in the dictionaries, one word per line.
				newWord = input.nextLine().toLowerCase();
				wordlist.add(newWord);
			}
			input.close();
			System.out.println("Done building!");
			System.out.println("There are " + wordlist.size() + " words in " + dictionary);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
		return wordlist;
	}
	
	// clean the list.
	// that is, remove non-words from given list.
	// "*", "csce", and the like will be removed.
	public TreeMap<String, Integer> clean(TreeMap<String, Integer> dirtyList, String dictName) {
		TreeMap<String, Integer> cleanList = new TreeMap<String, Integer>();
		TreeSet dictionary = buildWordList(dictName);
		
		for (Entry<String, Integer> entry : 
					dirtyList.entrySet()) {
			if (dictionary.contains(entry.getKey())) {
				cleanList.put(entry.getKey(), entry.getValue());
			}
		}
		
		return cleanList;
	}
	
	// filter the list according to the given stop words list.
	// that is, remove stop words from given list.
	public TreeMap<String, Integer> filter(TreeMap<String, Integer> dirtyList, String stoplist) {
		TreeMap<String, Integer> cleanList = new TreeMap<String, Integer>();
		TreeSet stopwords = buildWordList(stoplist);
		
		for (Entry<String, Integer> entry : 
					dirtyList.entrySet()) {
			if (!stopwords.contains(entry.getKey())) {
				cleanList.put(entry.getKey(), entry.getValue());
			}
		}
		
		return cleanList;
	}
	
	// output keys and corresponding values into a txt file
	public void parse2Txt(TreeMap<String, Integer> toParse, String file) {
		try {
			System.out.println("\tstart writing keywords" + toParse.size());
			FileWriter stream = new FileWriter(file);
			BufferedWriter out = new BufferedWriter(stream);
			
			for (Entry<String, Integer> entry : 
					toParse.entrySet()) {
//				System.out.println(entry.getKey() + "\t" + entry.getValue());
				out.write(entry.getValue() + ", " + entry.getKey() + "\n");
				out.flush();
			}
			out.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		System.out.println("\tDone writing");
	}
	
	/**
	 * To randomly pick (total_amount * rate) many records
	 * from original data set for validation.
	 * @param allIDs - list of ids in the big data set
	 * @param rate - the percent of records in validation set
	 * @return the list of validation ids which were picked randomly
	 */
	ArrayList<String> getRandValidIDList(String allIDs, double rate) {
		ArrayList<String> vIds = new ArrayList<String> ();
		HashMap<Integer, String> allIds = new HashMap<Integer, String> ();
		Random rand = new Random();
		String line;
		int counter = 0;
		int amount = 0;
		int pickedIndex = 0;
		String pickedId;
		
		try {
			InputStream fis = new FileInputStream(allIDs);
		    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		    BufferedReader reader = new BufferedReader(isr);
		    
		    // get all ids and assign index for them
		    while ((line = reader.readLine()) != null) {
		    	counter++;
		    	allIds.put(counter, line);
		    }
		    reader.close();
		    
		    // amount of records in validation set
		    amount = (int) (counter * rate);
			for (int i = 0; i < amount; i++) {
				pickedIndex = rand.nextInt(counter);
				pickedId = allIds.get(pickedIndex);
				if (! vIds.contains(pickedId)) {
					// if a record has not been added to verification set
					vIds.add(pickedId);
				} else {
					// otherwise, pick next random record
					i--;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return vIds;
	}
	
	/**
	 * To randomly split the given big data set into a train set and a validation set
	 * @param originalDataset - the big data set to split
	 * @param idsFile - the file stores only the all the ids in original big data set
	 * @param validRate - the percent of records in train set, it must be greater than 0 and less than 1.
	 */
	public void splitDataset(String originalDataset, 
							String trainSet, String validationSet,
							String idsFile, double validRate) {
		try {
			InputStream fis = new FileInputStream(originalDataset);
		    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
		    BufferedReader reader = new BufferedReader(isr);
		    
			PrintWriter trainWriter = new PrintWriter(trainSet, "UTF-8");
			PrintWriter validWriter = new PrintWriter(validationSet, "UTF-8");
		    
			File train = new File(trainSet);
			File valid = new File(validationSet);
			
			// Since data set is split randomly, confirm with user before overwrite existing sets.
			if (train.isFile() && valid.isFile()) {
				System.out.println(trainSet + " and " + validationSet + " already exist!\n" 
									+ "Do you want to use existing data sets?\n"
									+ "Type \"yes\" to use existing data sets!");
				Scanner console = new Scanner(System.in);
				if (console.nextLine().equalsIgnoreCase("yes")) {
					System.out.println("Skip splitting data set. Use existing train set and validation set.");
					return;
				}
			}
			
		    ArrayList<String> validationIDList = getRandValidIDList(idsFile, validRate);
		    String line = reader.readLine();
		    boolean isFeature = true;
		    String[] items;
		    
		    System.out.println("\tSplitting data set ...");
		    // split data set into train set and validation set
		    while (line != null) {
		    	if (isFeature) {
					// print column headers
		    		validWriter.println(line);
		    		trainWriter.println(line);
		    		isFeature = false;
		    	}
		    	
		    	items = line.split(",");
		    	if (validationIDList.contains(items[0])) {
		    		validWriter.println(line);
		    	} else {
		    		trainWriter.println(line);
		    	}
		    	line = reader.readLine();
		    }
		    validWriter.close();
		    trainWriter.close();
		    reader.close();
		    
		    System.out.println("\tData set has been split!");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	
	/**
	 * Reorganize data set by keywords
	 * @param originalDataset
	 * @param listOfKeyWords
	 */
	public void organizeDatasetByKey(String originalDataset, 
									ArrayList<String> listOfKeyWords) {
		
		String path = originalDataset.substring(0, originalDataset.lastIndexOf('/') + 1);
		String postfix = ".csv";
		String tmpFileName = "output.csv";
		String overviewFile = path + "overview" + postfix;
		int counter = 0;
		
		tmpFileName = path + tmpFileName;
		String fileName = "";
		
		try {
			System.out.println("\tReorganizing " + originalDataset);
			PrintWriter overviewWriter = new PrintWriter(overviewFile, "UTF-8");
			
			overviewWriter.println("keyword" + "," + "amount");
			
		    for (String key: listOfKeyWords) {
		    	counter = 0;
		    	InputStream fis = new FileInputStream(originalDataset);
			    InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
				PrintWriter writer = new PrintWriter(tmpFileName, "UTF-8");
				
			    BufferedReader reader = new BufferedReader(isr);
			    
		    	fileName = path + key + postfix;
		    	String line = reader.readLine();
		    	System.out.println("\t\tWriting data set of " + key);
		    	boolean isFeature = true;
		    	
		    	while (line != null) {
		    		if (isFeature) {
		    			writer.println(line);
		    			isFeature = false;
		    		}
		    		if (line.contains(key)) {
		    			
		    			writer.println(line);
		    			counter++;
		    		}
		    		line = reader.readLine();
		    	}
		    	reader.close();
		    	writer.close();
		    	
		    	line = key + "," + counter;
		    	overviewWriter.println(line);
		    	
		    	// rename the file with keyword
		    	File oldFile = new File(tmpFileName);
		    	File newFile = new File(fileName);
		    	oldFile.renameTo(newFile);
		    	
		    	// remove full description from data set
		    	removeColsFromCsvByColIndex(fileName, 2);
		    	
		    	System.out.println("\t\tDataset (" + key + ") has been created.");

		    }
		    overviewWriter.close();
		    
		    System.out.println("\t" + originalDataset + " has been reorganized.");
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	
	/**
	 * Save IDs of entire data set in a file for later calculation
	 * @param originalDataset dataset
	 */
	public void splitIDsFromDataset(String originalDataset, String idFile) {
		
		try {
			InputStream fis = new FileInputStream(originalDataset);
			InputStreamReader isr =new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader reader = new BufferedReader(isr);
			PrintWriter writer = new PrintWriter(idFile, "UTF-8");
			String regTex = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
			
			File fileOfIds = new File(idFile);
			// Since data set is split randomly, confirm with user before overwrite existing sets.
			if (fileOfIds.isFile()) {
				System.out.println(fileOfIds + " already exists!\n" 
									+ "Do you want to use the existing file?\n"
									+ "Type \"Yes\" to use the existing file!");
				Scanner console = new Scanner(System.in);
				if (console.nextLine().equalsIgnoreCase("yes")) {
					System.out.println("Skip saving all ids. Use existing file.");
					return;
				}
			}
			
			String line = reader.readLine();
			while (line != null) {
				String[] items = line.split(regTex, -1);
				writer.println(items[0]);
				line = reader.readLine();
			}
			writer.close();
			reader.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	
	/**
	 * To remove specific column from given csv file
	 * @param csvFile - file name, including path
	 * @param colHeader - column header, A, B, C, etc.
	 */
	public void removeColsFromCsvByColHeader(String csvFile, String colHeader) {
		int len = colHeader.length();
		int diff = colHeader.toLowerCase().charAt(len - 1) - 'a';
		// get corresponding column index
		int colIndex = (len - 1) * 26 + diff;
		
		// remove by column index
		removeColsFromCsvByColIndex(csvFile, colIndex);
	}
	
	/**
	 * To remove specific column from given csv file
	 * @param csvFile - file name, including path
	 * @param title - content in the first row of certain column
	 */
	public void removeColsFromCsvByTitle(String csvFile, String title) {
		int colIndex = -1;
		
		try {
			InputStream fis = new FileInputStream(csvFile);
			InputStreamReader isr =new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader reader = new BufferedReader(isr);
			
			String line = reader.readLine();
			String regEx = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
			// get corresponding column index
			if (line != null) {
				String[] items = line.split(regEx, -1);
				for (int i = 0; i < items.length; i++) {
					if (title.equalsIgnoreCase(items[i])) {
						colIndex = i;
						break;
					}
				}
			}
			reader.close();
			
			// remove column by index if found
			if (colIndex > -1) {
				removeColsFromCsvByColIndex(csvFile, colIndex);
			} else {
				// otherwise, terminate the program
				System.out.println("Cannot find the column in given csv file!");
				System.exit(0);
			}
		} catch ( Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		
	}
	
	/**
	 * To remove specific column from given csv file
	 * @param csvFile - file name, including path
	 * @param colIndex - index of column, starts from 0. e.g., 0 refers to column A, 1 refers to column B, and so on.
	 */
	public void removeColsFromCsvByColIndex(String csvFile, int colIndex) {
		String path = csvFile.substring(0, csvFile.lastIndexOf('/') + 1);
		String tmp = path + "removed.csv";
		
		try {
			InputStream fis = new FileInputStream(csvFile);
			InputStreamReader isr =new InputStreamReader(fis, Charset.forName("UTF-8"));
			BufferedReader reader = new BufferedReader(isr);
			
			PrintWriter writer = new PrintWriter(tmp, "UTF-8");
			String regEx = ",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
			String line = reader.readLine();
			String[] items;
			System.out.println("\t\t\t Remove full description from data set ...");
			while (line != null) {
				items = line.split(regEx, -1);
				line = "";
				for (int i = 0; i < items.length; i++) {
					if (i != colIndex) {
						line += items[i] + ",";
					}
				}
				writer.println(line);
				line = reader.readLine();
			}
			writer.close();
			reader.close();
			
			File original = new File(csvFile);
			File fileRmCol = new File(tmp);

			fileRmCol.renameTo(original);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
	}
	
	public static void main(String[] args) {
		DataProcesser worker = new DataProcesser();
		String csv = "./dataset/training/valid/validstore.csv";
		int colIndex = 2;
		
		worker.removeColsFromCsvByColIndex(csv, colIndex);
	}
}
