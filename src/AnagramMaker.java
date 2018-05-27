
import java.util.ArrayList;
import java.io.IOException;
import java.util.Scanner;
import java.util.TreeMap;
import java.nio.file.*;
public class AnagramMaker {
	public static void main(String args[]) {
		Path fileToRead = FileSystems.getDefault().getPath("enable1.txt");
		TreeMap<Character, ArrayList<String>> dictionary = new TreeMap<>();
		Scanner lineScanner = null;
		try {
			lineScanner = new Scanner(fileToRead);
		} catch (NoSuchFileException e) {
			System.out.println("No such file: " + fileToRead.getFileName());
			System.exit(-1);
		} catch (IOException e) {
			System.out.println("The file encountered a problem.");
			e.printStackTrace();
		}
		while (lineScanner.hasNextLine()) {
			String nextLine = lineScanner.nextLine();
			char startingChar = nextLine.charAt(0);
			if (dictionary.get(startingChar) == null) {
				dictionary.put(startingChar, new ArrayList<String>());
			}
			if (nextLine.length() > 1) {
				dictionary.get(startingChar).add(nextLine);
			}
		}
		lineScanner.close();
		System.out.println("Dictionary populated with " + dictionary.size() + " chapters.");
		Scanner scan = new Scanner(System.in);
		System.out.println("Enter the number of lines to process: ");
		int numberOfLines = Integer.parseInt(scan.nextLine());
		for (int i = 0; i <= numberOfLines - 1; i++) {
			String nextLine = scan.nextLine();
			nextLine = nextLine.replaceAll("[^/a-zA-Z]", "");
			System.out.println("Checking for anagrams...");
			System.out.print("Loading bar: ");
			
			ArrayList<String> anagrams = new ArrayList<String>();
			anagrams = makeAnagrams(nextLine, dictionary, true);
			System.out.println();
			for (String word: anagrams) {
				System.out.println(word);
			}
		}
		scan.close();
	}

	public static ArrayList<String> makeAnagrams(String userword, TreeMap<Character, ArrayList<String>> dictionary, boolean topLevel) {
		ArrayList<String> anagrams = new ArrayList<String>();
		for (Character c: userword.toCharArray()) {
			for (String term: dictionary.get(c)) {
				// for each dictionary term that starts with each letter in c
				String lettersLeft = userword;
				boolean isAnagram = true;
				for (int i = 0; i < term.length(); i++) {
					if (lettersLeft.contains(Character.toString(term.charAt(i)))) {
						lettersLeft = removeCharFrom(lettersLeft.indexOf(term.charAt(i)), lettersLeft);
					} else {
						isAnagram = false;
						break;
					}
				}
				if (isAnagram) {
					if (lettersLeft.length() > 0) {
						// found a term in the dictionary that matched the users word, but the user's word has more letters left
						// thus, the anagram needs to be two words to fit all the letters from userword
						ArrayList<String> moreAnagrams = new ArrayList<>();
						moreAnagrams = makeAnagrams(lettersLeft, dictionary, false);
						if (!moreAnagrams.isEmpty()) {
							for (String anagram: moreAnagrams) {
								anagrams.add(term + " " + anagram);
							}
						}
					} else {
						// all of the letters matched, so add it
						if (!anagrams.contains(term))
							anagrams.add(term);
					}
				}
			}
			if (topLevel) {
				System.out.print(c);
				dictionary.put(c, new ArrayList<String>());
			}
		}
		return anagrams;
	}
	private static String removeCharFrom(int position, String word) {
		if (word.length() == 1 && position == 0) {
			return "";
		}
		if (position > word.length() || position < 0) {
			return word;
		}
		if (position == word.length())
			return word.substring(0, word.length() - 1);
		return word.substring(0, position) + word.substring(position + 1);
	}
}
