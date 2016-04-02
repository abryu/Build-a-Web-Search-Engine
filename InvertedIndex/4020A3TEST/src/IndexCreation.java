import java.util.*; //For HashMap and List
import org.jsoup.Jsoup; // For removing HTML tags
import java.io.*; //For read and write files

//Pattern Match
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndexCreation {
	
	private String stopwordsFile;
	private String collectionFile;
	private String destinationFile;
	
	public IndexCreation (String stopwordsFileName, String collectionFileName, String destinationFileName) {
		this.stopwordsFile = stopwordsFileName;
		this.collectionFile = collectionFileName;
		this.destinationFile = destinationFileName;
	}
	
	//PLAN:
	//Open the collection file
	//Read each line of the file
	//Processing the file line by by line (remove stopwords, remove special characters, stemmer)
	//Store each processed line to a HashMap
	//Sort and display the HashMap
	
	public void createIndex() {
		long startTime = System.currentTimeMillis();
		
		HashMap<String, HashMap<Integer,List>> holderForWriteToDestinationFile = new HashMap<String, HashMap<Integer,List>>();
		
		//Read the collection source file
		try {
			BufferedReader collectionInput = new BufferedReader(new FileReader(this.collectionFile));		
		
			// List for storing stopwords
			Set<String> stopwordsList = getStopwordsList(this.stopwordsFile);
			
			//Read each line of the collection file, Process them
			String cline = null;
			
			String docidPattern = "<DOCNO>.*</DOCNO>";
			Pattern docidPat = Pattern.compile(docidPattern);
			int docNO = 1;
			List<String> wordsHolderForPerDOC = new ArrayList<String>();
			
			System.out.println("<!DOCTYPE html><html><head><title>RESULT</title></head><body><table cellpadding=\"10\" border=\"1\" style=\"width:80%\"><tr><td>KEY</td><td>POSTING</td></tr>");
			
			while ((cline = collectionInput.readLine())!=null) { // Loop each line of collection file
																
				if (cline.equals("</DOC>")) { //End of a DOC . Processing the extracted DOCNO and content within <DOC> and </DOC> . 
					
					for (int k = 0; k < wordsHolderForPerDOC.size(); k++) {
						
					    String term = wordsHolderForPerDOC.get(k);
					    
					    //Check if the term exists
					    if (holderForWriteToDestinationFile.containsKey(term)) { //EXIST
					    	// Check whether this term appears in the same document
					    	if (holderForWriteToDestinationFile.get(term).containsKey(docNO)) { // This term appears before within the same document
					    	
						    	// Get the HashMap for this term and append the K to the HashMap with the same docNO and update(append) to the list
						    	holderForWriteToDestinationFile.get(term).get(docNO).add(k);
						  
					    	} else {
							    HashMap<Integer,List> postingForThisTerm = new HashMap<Integer,List>();
							    List posHolderList = new ArrayList();
							    posHolderList.add(k);
							    postingForThisTerm.put(docNO, posHolderList);
						    	// Get the current value and copy it. Then add the new hashtable(posting) to the value. Then replace the old one.
						    	HashMap<Integer,List> tempHolder = holderForWriteToDestinationFile.get(term);
						    	tempHolder.put(docNO, posHolderList);
						    	holderForWriteToDestinationFile.put(term, tempHolder); 
					    	}
					    } else { // NOT EXIST
						    HashMap<Integer,List> postingForThisTerm = new HashMap<Integer,List>();
						    List posHolderList = new ArrayList();
						    posHolderList.add(k);
						    postingForThisTerm.put(docNO, posHolderList);
					    	holderForWriteToDestinationFile.put(term, postingForThisTerm);  
					    }
					}

					wordsHolderForPerDOC.clear(); // Clear the current document words list. Start a new one for next document

				} else { //Line of content  1. DOCNO 2. Content -> stop words, remove redundant texts, stem
					
					Matcher docidMatcher = docidPat.matcher(cline);
					
					if (docidMatcher.find()) { // DOCID
						String str = docidMatcher.group(0);
						String resultID = str.substring(str.indexOf("15-") + 3, str.indexOf("</")); // Extract the DOCNO
						docNO = Integer.parseInt(resultID);
					} else { //CONTENT
						List resultString;
						resultString = stopwordsCheckAndRemover(removeHTMLtags(cline),stopwordsList);
						resultString = stemmerReplacer(resultString);
						for (int i = 0; i < resultString.size(); i++) {
							if (((String)resultString.get(i)).trim().length()>=1) 
								wordsHolderForPerDOC.add((String)resultString.get(i));
						}
					}
					
				}		
				
			}
			
			resultWriter(holderForWriteToDestinationFile); //Displaying
			
			System.out.println("</table></body></html>");
			
			long endTime   = System.currentTimeMillis();
			long totalTime = endTime - startTime;
			System.out.println("RUN TIME  " + totalTime + " milliseconds");
			
			collectionInput.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("ERRORS IN FINDING collection file");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERRORS IN FINDING collection file");
			e.printStackTrace();
		} 
	}

	public HashSet<String> getStopwordsList(String fileName) {
		HashSet<String> swPlaceHolder = new HashSet<String>();
		
		try {
			BufferedReader stopwordsInput = new BufferedReader(new FileReader(fileName));
			
			//StringBuffer stopwordsBuffer = new StringBuffer();
			
			String swline = null;
			
			while ((swline = stopwordsInput.readLine()) != null) {
				swPlaceHolder.add(swline.trim());
			}
			
			stopwordsInput.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("ERRORS IN FINDING stopwords file");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERRORS IN FINDING stopwords file");
			e.printStackTrace();
		}
	
		return swPlaceHolder;
	}
		
	public String removeHTMLtags(String inputString) {
		inputString = Jsoup.parse(inputString.toString()).text(); //Remove HTML tags
		inputString = inputString.replaceAll("\\d+.*", ""); //Remove numbers
		inputString = inputString.replaceAll("_", ""); //Remove _
		inputString = inputString.toLowerCase().replaceAll("[^\\w\\s]",""); //Remove special characters
		return inputString;
	}
	public List stopwordsCheckAndRemover(String inputString, Set<String> stopwordsList) { 
		List<String> placeHolder = new ArrayList<String>();
		String[] input = inputString.split(" ");
		for (String word : input) {
	        if (!stopwordsList.contains(word))
	        {
	        	placeHolder.add(word);
	        }
		}
		return placeHolder;
	}
	
	public List stemmerReplacer(List terminput) {
		
		PorterStemmer stemmer = new PorterStemmer();
		
		List placeHolder = new ArrayList();
		
		for (int i = 0; i < terminput.size(); i++) {
			placeHolder.add(stemmer.stem((String)terminput.get(i)));
		}
		
		return placeHolder;
	}
	
	public void resultWriter(HashMap<String, HashMap<Integer,List>> inputHM) {
		
        Map<String, HashMap<Integer,List>> sortedHM = new TreeMap<String, HashMap<Integer,List>>(inputHM); //Sort the HashMap
	
		for (String key : sortedHM.keySet()) {
			
			//System.out.print("KEY = " + key + " POSTING ");
			
			System.out.print("<tr><td style = \"color:red\">" + key + "</td><td>");
			
			 Map<Integer,List> sortedHM2 = new TreeMap<Integer,List>(inputHM.get(key)); //Sort the HashMap
			 
			for (int key2 : sortedHM2.keySet() ) {
				
				System.out.print("[<span style=\"color:blue\">DOCNO:" + key2 + "</span>, [");
				
				int innerPosSize = inputHM.get(key).get(key2).size();
				
					for (int pos = 0; pos < innerPosSize; pos++) {
						
						
						if (pos == (innerPosSize - 1)) {
							System.out.print(inputHM.get(key).get(key2).get(pos));
						} else {
							System.out.print(inputHM.get(key).get(key2).get(pos) + ",");
						}
					}	
					System.out.print("]] Term Frquence in this Document : " + innerPosSize + "<br/>");			
			}		
			//System.out.println("");
			System.out.println("</td></tr>");
		}
	}
	
}
