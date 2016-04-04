//package tokenize;
import org.jsoup.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
public class MainClass {

	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		ArrayList<String> docs = new ArrayList<String>();//an array of all the separate XML files in String form
		ArrayList<String> docsClean = new ArrayList<String>();//a copy of the above array with all tags removed
		ArrayList<Integer>results= new ArrayList<Integer>();//contains the index number of documents found in a search
		//CHANGE THIS URL TO THE LOCATION OF source.txt ON YOUR COMPUTER****
		Scanner s = new Scanner(new File(System.getProperty("user.dir")+"/source.txt"));
		Scanner input = new Scanner(System.in); //takes search input from user
		
		String temp = "";
		int count = 0;
		// Separates the source.txt file into an array of strings (<DOC> to </DOC>)
		while(s.hasNext()){
			String temp1 = s.nextLine();
			temp += "\n"+ temp1;
			if(temp1.contains("</DOC>")){
				docs.add(temp);
				temp = "";
				count++;
			}
			//count++;
		}
		//Creates a copy array, removes all <tags> (note: index values correspond to each other)
		for(int i =0; i<docs.size() ;i++){ //goes through each xml file
			String htmlString = docs.get(i);
			String noHTMLString = htmlString.replaceAll("\\<.*?\\>", "");//removes all <tags> from the string
			docsClean.add(noHTMLString); //adds this 'cleaned' string to the arraylist of cleaned files
		}
	
		//Allows the user to search using a key word or phrase
		System.out.println("Enter a Search keyword: ");
		String in = input.nextLine();
		for(String doc: docsClean){ //brute force searches the cleaned array for the input string
			if(doc.contains(in)){
				results.add(docsClean.indexOf(doc)); //if a match is found, the index number is added to the arrayList of indexes
			}
		}
		for(int i: results){ //Cycles through each result in the results ArrayList
			System.out.println("Found index: "+i+"-----------------------------------");
			System.out.println(docs.get(i));
			PrintWriter out = new PrintWriter("Document "+i+".xml");
			out.println(docs.get(i));
			out.close();
			
		}
		
		System.out.println("Files searched: "+count);
		System.out.println("Files found: "+results.size());
		

	}

}
