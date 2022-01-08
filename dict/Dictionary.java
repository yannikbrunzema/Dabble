package dict;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


/**
 * A simple immutable dictionary of English word spellings that allows look-up
 * of words and providing all words in the dictionary of a specified length.
 * 
 * <p>
 * The dictionary can return a set of all words in the dictionary having
 * length 1, 2, 3, ..., 10 or greater.
 */
public class Dictionary {

	private Map<Integer, TreeSet<String>> dicts;
	private int size;

	/**
	 * Reads the dictionary file and stores the words from the file in 
	 * this dictionary. The words in this file are in all lower case.
	 * 
	 * <p>
	 * The dictionary file is named dictionary2.txt and needs to be located in the
	 * same package as this file.
	 * 
	 * @throws RuntimeException if dictionary2.txt cannot be found
	 * 
	 */
	private final void readDictionary() {
		InputStream in = this.getClass().getResourceAsStream("dictionary2.txt");
		if (in == null) {
			throw new RuntimeException("dictionary2.txt is missing");
		}
		Scanner dictionaryInput = new Scanner(in);
		while (dictionaryInput.hasNext()) {
			String word = dictionaryInput.next();
			this.add(word.trim());
		}
		dictionaryInput.close();
	}

	/**
	 * Initializes a dictionary by reading the default dictionary from a file.
	 */
	public Dictionary() {
		this.dicts = new HashMap<>();
		for (int wordLen = 1; wordLen <= 10; wordLen++) {
			this.dicts.put(wordLen, new TreeSet<String>());
		}
		this.readDictionary();
	}
	
	/**
	 * Add a word to this dictionary returning {@code true} if the specified word is
	 * not already in the dictionary. If the specified word is already in the
	 * dictionary then it is not added and {@code false} is returned.
	 * 
	 * <p>
	 * The size of this dictionary increases by 1 if a word is successfully added.
	 * 
	 * @param word a word to add to this dictionary
	 * @return true if the word is added to this dictionary
	 */
	private boolean add(String word) {
		Set<String> set = this.getSet(word.length());
		if (set.contains(word)) {
			return false;
		}
		set.add(word);
		this.size++;
		return true;
	}

	/**
	 * Returns the number of words in the dictionary.
	 * 
	 * @return the number of words in the dictionary
	 */
	public int size() {
		return this.size;
	}

	/**
	 * Returns the set containing words of the specified length.
	 * 
	 * @param wordLen the word length
	 * @return the set containing words of the specified length
	 */
	private TreeSet<String> getSet(int wordLen) {
		TreeSet<String> result = null;
		if (wordLen >= 10) {
			result = this.dicts.get(10);
		} else {
			result = this.dicts.get(wordLen);
		}
		return result;
	}
	
	/**
	 * Returns the list containing words of the specified length. For
	 * {@code wordLen >= 10} the list containing all words of length
	 * 10 or greater is returned.
	 * 
	 * @param wordLen the word length
	 * @return the list containing words of the specified length
	 * @throws IllegalArgumentException if wordLen is less than zero
	 */
	public List<String> getWordsByLength(int wordLen) {
		if (wordLen < 0) {
			throw new IllegalArgumentException("getWordsByLength(): negative wordlen = " + wordLen);
		}
		return new ArrayList<>(this.getSet(wordLen));
	}
	
	/**
	 * Returns true if the specified word is in the dictionary, and false otherwise.
	 * The case of the specified word is not important; {@code contains("hello")}
	 * returns the same result as {@code contains("HeLLo")}.
	 * 
	 * @param word a word to look up in the dictionary
	 * @return true if the specified word is in the dictionary, and false otherwise
	 */
	public boolean contains(String word) {
		return this.getSet(word.length()).contains(word.toLowerCase());
	}

}