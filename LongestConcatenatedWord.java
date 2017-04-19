import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.Map.Entry;
import weka.core.Trie;

public class LongestConcatenatedWord {

	//maintain a  lookup table to check if the word is present in test file 
	public HashMap<String,Boolean> lookupTable=new HashMap<String,Boolean>() ;
	public Trie trie=new Trie();
	// array to store result
	public static String[] longestconcatWord = new String[5];
	
	static <K,V extends Comparable<? super V>>
	//sort map by value using Generic and Comparator
	SortedSet<Map.Entry<K,V>> entriesSortedByValues(Map<K,V> map) {
		SortedSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
				new Comparator<Map.Entry<K,V>>() {
					
					@Override 
					public int compare(Map.Entry<K,V> element1, Map.Entry<K,V> element2) {
						
						//returns +ve value - If first string is lexicographically greater than second string
						//returns -ve value - If first string is lexicographically less than second string
						//returns 0 -  If first string is lexicographically equal to second string
						int res =  element1.getValue().compareTo(element2.getValue());
						if(res == 0) return 1;
						return res ; 
						
					}
				}
				);
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
	}
	
	//method to find whether the word is concatenated or not
	boolean concatCanForm(String word,boolean firstTimeCall)
	{
		//if its the firstCall remove that word from trie
		if (firstTimeCall)
			trie.remove(word);
				
		//if the word is already present in the lookup table and its not firstCall then return the table value for the word
		if(!firstTimeCall && lookupTable.containsKey(word))
			return lookupTable.get(word);
		
		//from n=word.length down to 1 check if different the word is concatenated using already present words in the trie
		//if we reach end of loop then we shall conclude that the word is not a concatenated word and return false
		for(int i=word.length()-1;i>=0;i--)
		{
			//if substring is exist in the trie
			if(trie.contains(word.substring(0, i+1))) 
			{
				//check whether the entire word exist in the trie or not by using recursive function
				if((i==word.length()-1)||concatCanForm(word.substring(i+1,word.length()), false))
				{
					//add that word into lookup table with the true value
					lookupTable.put(word, true);
					//if it is firstTimeCall then add that word into trie
					if(firstTimeCall) trie.add(word);
					return true;			

				}
			}
		}

		//word is not exist in the file
		//add that word into lookup table with the false value
		lookupTable.put(word, false);
		if (firstTimeCall) trie.add(word); 

		return false;
	}
	
	//method to store all the words into trie structure
	public void initialTrie(LinkedHashMap<String,Integer> lengthMap)
	{
		for(String key:lengthMap.keySet())
		{
			trie.add(key);
		}
	}
	
	// method to find list of longest concatenated word in the file 
	public void findLongestConcatWord(LinkedHashMap<String,Integer> lengthMap)
	{
		List<Entry<String,Integer>> lengthMapList = new ArrayList<>(lengthMap.entrySet());
		int index = 0;
		//traverse from longest to smallest word in the list
		for(int i = lengthMapList.size() -1; i >= 0 ; i --)
		{
			if(concatCanForm(lengthMapList.get(i).getKey(),true))
			{
				longestconcatWord[index]=lengthMapList.get(i).getKey();
				if(index == (longestconcatWord.length - 1))
				 	return;
				else
					index++;
			}
		}
		return;
	}
	
	// method to calculate total count of all the concatenated words in the file
	public int findTotalConcatWords(LinkedHashMap<String,Integer> lengthMap)
	{
		int flagCounter=0;
		List<Entry<String,Integer>> lengthMapList = new ArrayList<>(lengthMap.entrySet());
		
		//traverse from longest to smallest word in the list
		for(int i = lengthMapList.size() -1; i >= 0 ; i --)
		{
			// if concatenated is there in the list then increment the flag.
			if(concatCanForm(lengthMapList.get(i).getKey(),true))
			{
				flagCounter++;
			}
		}
		return flagCounter;

	}
	public void findWord(String filePath)
	{
		FileInputStream f;
		
		try{
			//read data from file
			f = new FileInputStream(filePath);
			Map<String,Integer> wordList = new HashMap<String,Integer>();
			
			BufferedReader br = new BufferedReader(new InputStreamReader(f));

			String strLine;
			
			while ((strLine = br.readLine()) != null)   {
				  // store key and value pair into HashMap
				  // Key - word
				  // Value - word length
				  wordList.put(strLine,strLine.length());
				}
			
			
			LinkedHashMap<String, Integer> lengthMap=new LinkedHashMap<String,Integer>();
			
			//store all the entries sorted by value(length of word) into LinkedHashmap
			for (Map.Entry<String, Integer> entry  : entriesSortedByValues(wordList)) {

				lengthMap.put(entry.getKey(), entry.getValue());

			}		

			//Initialize Trie using HashMap
			initialTrie(lengthMap);
			findLongestConcatWord(lengthMap);
			System.out.println("Total Concatenated Words in file : "+findTotalConcatWords(lengthMap));
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

	}

	public static void main(String args[]) throws Exception 
	{
		String filePath="src/wordsforproblem.txt";
		LongestConcatenatedWord findLongestWord=new LongestConcatenatedWord();
		long start=System.currentTimeMillis();
		findLongestWord.findWord(filePath);
		if(longestconcatWord[0] !=null)
		{
			System.out.println("\n1st Longest Concatenated word is="+longestconcatWord[0]);
			System.out.println("Size of 1st Longest Concatenated word is="+longestconcatWord[0].length());
			
			if(longestconcatWord[1] !=  null){
				System.out.println("\n2nd Longest Concatenated word is="+longestconcatWord[1]);
				System.out.println("Size of 2nd Longest Concatenated word is="+longestconcatWord[1].length());
			}
			else
				System.out.println("\nno 2nd Longest Concatenated word exist in the file..!!");	
		}
		else 
			System.out.println("no Concatenated word exist in the file..!!");
		long end=System.currentTimeMillis();
		System.out.println("\ntime taken="+(end-start)/1000+" sec");
		  

	}
}