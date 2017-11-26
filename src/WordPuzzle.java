import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class WordPuzzle<AnyType> extends MyHashTable<AnyType> {
	
	/**
	 * readDictionary -  reads the dictionary text file provided into a scanner.
	 * @return
	 */
	public Scanner readDictionary(){
		Scanner dict=null;				//contains the dictionary
		try
		{
			dict= new Scanner(getClass().getResourceAsStream("dictionary.txt")).useDelimiter("\n");
		}
		catch(Exception e)
		{
			System.out.println(e.getLocalizedMessage());
		}
		return dict;
	}
	/**
	 * fillMatrix - creates a row * column matrix/grid of random characters generated on runtime.
	 * @param row
	 * @param col
	 */
	public void fillMatrix(int row,int col)
	{
		charMatrix=new char[row][col];	    
	    Random ranChar=new Random();	    
	    String charList="abcdefghijklmnopqrstuvwxyz";
	    	    
	    if(charMatrix!=null)
	    {
	    	for(int r=0;r<row;r++)
	    	{
	    		for(int c=0;c<col;c++)
	    		{
	    			charMatrix[r][c]=charList.charAt(ranChar.nextInt(charList.length()));
	    			System.out.print(charMatrix[r][c]+" ");
	    		}
	    		System.out.println();
	    	}
	    }
	}
	
	/**
	 * fillHashTable - reads list words from a dictionary provided as input into the hash - table using linear probing  
	 * @param isEnhanced
	 */
	public void fillHashTable(boolean isEnhanced)
	{
		String eachWord=null;
		while(dict.hasNext())
    	{
    		eachWord= dict.nextLine();
    		H.insert(eachWord,false);

    		if(isEnhanced)
    		{
	    		if(eachWord.length()>1)
	    			for(int i=1;i<eachWord.length();i++)
	    			{
						H.insertPrefix(eachWord.substring(0, i));
	    			}
    		}
    	}
	}
	
	public void horizonalSearch()
	{
		 //Horizontal Search //forwards
	    for(int i=0;i<rowsCount;i++)
	    {
	    	String currentMatrixString="";
	    	
	    	for(int j=0;j<colsCount;j++)	
	    	{
	    		if(H.contains(Character.toString(charMatrix[i][j])))
	    			System.out.println(charMatrix[i][j]);
	    		
	    		currentMatrixString+=charMatrix[i][j];
	    	}	 	    		  
	    	checkForWord(currentMatrixString);            //forward word search
	    	String revLongestString=new StringBuffer(currentMatrixString).reverse().toString();
	    	checkForWord(revLongestString);
	    }
	}
	
	public void verticallSearch()
	{
	    for(int j=0;j<colsCount;j++)
	    {
	    	String currentMatrixString="";
	    	
	    	for(int i=0;i<rowsCount;i++)	
	    	{
	    		currentMatrixString+=charMatrix[i][j];	    			    
	    	}	 	    
	    	checkForWord(currentMatrixString);            //forward word search
	    	String revLongestString=new StringBuffer(currentMatrixString).reverse().toString();
	    	checkForWord(revLongestString);            //backwards word search
	    }	    	  	    
	}
	
	public void diagonalSearch()
	{
		 //Diagonal Search : search passes from left-top to bottom right  : DOWNRIGHT  -TILL MIDDLE
	    
	    for (int i=0;i<rowsCount;i++)
	    {
	    	String currentMatrixString="";
	    	for(int j=0, k=i;k>=0 && j< colsCount;j++ , k--)
	    	{
	    		currentMatrixString+=charMatrix[k][j];
	    	}	    		    
	    	checkForWord(currentMatrixString);            //forward word search
	    	String revLongestString=new StringBuffer(currentMatrixString).reverse().toString();
	    	checkForWord(revLongestString);            //backwards word search
	    }
   
	    //Diagonal Search : search passes from left-top to bottom right : DOWNRIGHT  -BEYOND MIDDLE
	    
	    for(int j=1;j< colsCount;j++)
	    {
	    	String currentMatrixString="";
	    	for(int k=rowsCount-1,i=j;k>=0 && i< colsCount;i++, k--)
	    	{
	    		currentMatrixString+=charMatrix[k][i];
	    	}	    		    
	    	checkForWord(currentMatrixString);            //forward word search
	    	String revLongestString=new StringBuffer(currentMatrixString).reverse().toString();
	    	checkForWord(revLongestString);            //backwards word search
	    }	    
	    
	    //Diagonal Search : search passes from left-bottom to top right - till middle : UPRIGHT
        
        for(int i=rowsCount-1;i>=0;i--)
        {
        	String currentMatrixString="";
        	for (int j=0,k=i;k<rowsCount && j<colsCount;j++,k++)
        	{
	    		currentMatrixString+=charMatrix[k][j];
        	}
        	checkForWord(currentMatrixString);            //forward word search
        	String revLongestString=new StringBuffer(currentMatrixString).reverse().toString();
	    	checkForWord(revLongestString);            //backwards word search
        }
        
     	//Diagonal Search : search passes from left-bottom to top right - beyond middle : UPRIGHT
        for(int j=1;j<colsCount;j++)
        {
        	String currentMatrixString="";
        	for(int i=0,k=j;i<rowsCount && k<colsCount;i++,k++)
        	{
	    		currentMatrixString+=charMatrix[i][k];
        	}
        	checkForWord(currentMatrixString);            //forward word search
        	String revLongestString=new StringBuffer(currentMatrixString).reverse().toString();
	    	checkForWord(revLongestString);            //backwards word search
        }        	
	}
	/**
	 * checkForWord - searches for words in the longest string in a row/column/diagonals - upwards/downwards
	 * @param longestString
	 */
	public void checkForWord(String longestString)
	{
		int lastCharInd=longestString.length()-1;
				
		for(int startInd=0;startInd<lastCharInd;startInd++)
		{
			String currentMatrixString="";
			
			for(int currentPostition=startInd;currentPostition<=lastCharInd;currentPostition++)
			{
				currentMatrixString+=longestString.charAt(currentPostition);
				if(H.contains(currentMatrixString) && currentMatrixString.length()!=1)
				{
					wordsFound.add(currentMatrixString);
					//System.out.println(currentMatrixString);
				}
				if(!H.containsPrefix(currentMatrixString) && !H.contains(currentMatrixString) && enhancedSearch)
					break;
			}
		}		
	}

	private static char[][] charMatrix=null;
	private static MyHashTable<String> H = new MyHashTable<>( );
	private static Scanner dict=null;
	private static int rowsCount=0,colsCount=0;
	private static boolean enhancedSearch;
	public static LinkedList<String> wordsFound=new LinkedList<String>();
	
	public static void main(String[] args)
	{		
		WordPuzzle<String> puzz= new WordPuzzle<>();			    		       
	    	   
		try
		{
			dict=puzz.readDictionary();
		}
		catch(Exception e)
		{
			System.out.println("Error reading dictionary file");	
		}
		
	    Scanner isEnhancedScan=new Scanner(System.in);
    	System.out.println("Do you want search with enhancement (Y/N): ");	    
	    String isEnhanced="";
	    try
	    {
		    while(isEnhancedScan.hasNext())
		    {
	
		    	isEnhanced=isEnhancedScan.next();
		    	
		    	if(isEnhanced.equalsIgnoreCase("Y")||isEnhanced.equalsIgnoreCase("N"))
		    		break;	    	
		    	System.out.println("Please enter Valid input (Y/N only) - Do you want search with enhancement (Y/N): ");		
		    }
		    
		    
		    if(dict!=null && !isEnhanced.equals(""))
		    {
		    	if(isEnhanced.equalsIgnoreCase("Y"))
		    	{
		    		enhancedSearch=true;
		    		puzz.fillHashTable(enhancedSearch);
		    	}
		    	else
		    	{
		    		enhancedSearch=false;
		    		puzz.fillHashTable(enhancedSearch);
		    	}
		    }
	    }
	    catch(Exception e)
	    {
	    	System.out.println("Error reading the dictionary file into hashtable");
	    }
	    
	    Scanner rows=new Scanner(System.in);
	    System.out.println("Enter input for number of rows...");
	    
	    while(rows.hasNextInt())
	    {
	    	rowsCount=rows.nextInt();
		    	if(rowsCount>0)
		    		break;
	    	System.out.println("Enter valid input for number of rows...");
	    }
	    
	    Scanner cols=new Scanner(System.in);
	    System.out.println("Enter input for number of Columns...");
	    while(cols.hasNextInt())
	    {	   
	    	colsCount=cols.nextInt();	    
		    	if(colsCount>0)    	
		    		break;
	    		System.out.println("Enter valid input for number of Columns...");
	    }
	    	    	    
	    if(rowsCount>0 && colsCount>0)
	    {
	    	try
	    	{
		    	puzz.fillMatrix(rowsCount, colsCount);	    
		   
			    System.out.println();
			    System.out.println("The grid contains the below words (words taken from provided dictionary) ");
			    System.out.println();
			    long startTime = System.currentTimeMillis( );
			    if(rowsCount>0 && colsCount>0 && H!=null )
			    {
			    	puzz.horizonalSearch();
			    	puzz.verticallSearch();
			    }
			    
			    if(rowsCount>1 && colsCount>1 && H!=null)
			    	puzz.diagonalSearch();			    			   
			    
			    long endTime = System.currentTimeMillis( );
		        
			    for(String word : WordPuzzle.wordsFound)
			    	System.out.println(word);
			    
			    if(enhancedSearch)
			    	System.out.println( "Elapsed time for Enhanced Search: " + (endTime - startTime) );
			    else
			    	System.out.println( "Elapsed time for NON Enhanced Search: " + (endTime - startTime) );
	    	}
	    	catch(Exception e)
	    	{
	    		System.out.println("Error while performing search!!!");
	    	}
	    }
	    else
	    	System.out.println("Invalid input provided....Please try again!!!");
	    
	    isEnhancedScan.close();
    	rows.close();
	    cols.close();
		    
}
}