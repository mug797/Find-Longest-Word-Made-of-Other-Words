import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class FindLongestString {	

	//For each string cache result in a HashMap<Node, Boolean>
	class Node {
		int start;
		int end;
		
		public Node(int start, int end) {
			this.start = start;
			this.end = end;
		}
		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Node))
	            		return false;
	        	if (obj == this)
	            		return true;
	
	        	Node rhs = (Node) obj;
	        	return ((this.start == rhs.start) && (this.end == rhs.end));
		}
	
		//Prime numbers in hashcode to minimise collision
		@Override
		public int hashCode() {
			int hash = 7;
			hash = 101 * hash + Integer.valueOf(this.start).hashCode();
			hash = 161 * hash + Integer.valueOf(this.end).hashCode();
			return hash;
		}
		
		public String toString() {
			return this.start + " " + this.end;
		}
	}
		
	ArrayList<String> sortedlist; 
	ArrayList<String> listlength; 
	int minLength;
	
	public FindLongestString() {
		sortedlist = new ArrayList<String>();
		listlength = new ArrayList<String>();
		
	}
	
	//remove the string when processing from the sorted arraylist
	void removefromList(String str){
		int index = BinarySearch(str, this.sortedlist, 0, this.sortedlist.size()-1);
		this.sortedlist.remove(index);
	}
	
	//read the strings from input file and put in an arraylist
	void readFile(String path){		
		String str = null;
		BufferedReader reader = null;
		
		minLength = Integer.MAX_VALUE;
		try {		
			reader = new BufferedReader(new FileReader(path));
			while (( str = reader.readLine()) != null){
				if(str.length() == 0) {
					continue;
				}
				if (str.length() < minLength)
					minLength = str.length();
				
				sortedlist.add(str);
			}
			 reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//Binary search for the substring in the sorted list, return the index where found.
	int BinarySearch(String searchString, ArrayList<String> stringsArr, int start, int end) {
		if(start > end || searchString.length() < minLength)
			return -1;
		int mid = start + (end - start)/2;
		
		int result = stringsArr.get(mid).compareTo(searchString);
		if(result == 0)
			return mid;
		else if(result < 0)
			return BinarySearch(searchString, stringsArr, mid+1, end);
		else 	
			return BinarySearch(searchString, stringsArr, start, mid-1);
	}
	
	//Wrapper for Binary Search
	boolean CheckIfPresent(String str) {
		
		if(BinarySearch(str, this.sortedlist, 0,this. sortedlist.size()-1) == -1) {
			return false;
		}
		
		return true;
	}
		
	//Generate substring combinations for the string and find if present in sorted list
	boolean generateCombinations(String str, int beg, int last,  HashMap<Node, Boolean> cache) {
		
		if(str.length() == 0) {
			return true;
		}
		
		if (str.length() < minLength)
			return false;
		
		
		Node n = new Node(beg, last);
		if(cache.containsKey(n)) {
			return cache.get(n);
		}
		
		if (CheckIfPresent(str)) {
			cache.put(n, true);
			return true;
		}
		
		int start = 0;
		int end = str.length();
		for(int i = start; i < end-1; i++) {
			if (CheckIfPresent(str.substring(start, i+1)) && generateCombinations(str.substring(i+1), i+beg + 1,  i+last, cache)){
				cache.put(n, true);
				return true;
			}
		}
		
		cache.put(n, false);	
		return false;
	}
	
	//Compare strings based on length in the descending order
	static class myComparator implements Comparator<String> {
		@Override
		public int compare(String o1, String o2) {
			return Integer.valueOf(o2.length()).compareTo(Integer.valueOf(o1.length()));
		}
	} 
	
	//Get the first, second longest string and count of strings that are formed from strings present in the sorted list
	void getOutput(String path) {
		readFile(path);
		this.listlength = new ArrayList<String>(this.sortedlist);
		Collections.sort(this.listlength, new myComparator());
		int counter = 0;
		while(this.listlength.size() > 0) {
			String str = this.listlength.remove(0);
			this.removefromList(str);
			
			HashMap<Node, Boolean> cache = new HashMap<Node, Boolean>(); 
			
			if(this.generateCombinations(str, 0, str.length()-1, cache)) {
				counter += 1;
				if(counter == 1) 
					System.out.println("1st longest: "+str);
				else if(counter == 2)
					System.out.println("2nd longest: "+str);
			}		
		}
		System.out.println("Total number of words: " +counter);
	}

	public static void main(String[] args) {
		FindLongestString f = new FindLongestString();
		String path = "wordsforproblem.txt";
		f.getOutput(path);
	}
}
