
public class IndexCreationRunner {

	public static void main(String[] args) {
				
		System.out.println(System.getProperty("user.dir")); // Get the directory of this Java file
		
		IndexCreation t1 = new IndexCreation(System.getProperty("user.dir")+"/src/stopwords.txt",System.getProperty("user.dir")+"/src/source.txt",System.getProperty("user.dir")+"/src/result.html");
		
		t1.createIndex();

	}

}
