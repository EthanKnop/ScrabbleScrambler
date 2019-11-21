import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class CircularTileSpread {
	
	public int size;
	public Node head;
	public Node tail;
	
	//Arrays to hold characters and their scrabble scores in order
	private String[] letters = {"A", "B", "C", "D", "E", "F", "G",
			"H", "I", "J", "K", "L", "M", "N", "O", "P", "Q",
			"R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
	private int[] letterScores = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5,
			1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
	
	//Instantiates an empty linked list
	public CircularTileSpread() {
		size = 0;
		head = null;
		tail = null;
		
	}
	
	//Fills linked list with random characters and their score
	public void fillRandom(int capacity) {
		if(size != 0)
			clear();
		while(size != capacity) {
			int ranIndex = new Random().nextInt(26);
			addToStart(letters[ranIndex], letterScores[ranIndex]);
		}
	}
	
	//Adds new node to beginning of linked list
	public void addToStart(String character, int score) {
		Node newNode = new Node(character, score);
		if(size == 0) {
			head = newNode;
			tail = newNode;
			newNode.next = head;
		}
		else {
			Node temp = head;
			newNode.next = temp;
			head = newNode;
			tail.next = head;
		}
		size++;
	}

	//Removes first node from linked list
	public void removeFromStart() {
		if(size != 0) {
			head = head.next;
			tail.next = head;
			size--;
		}
	}
	
	//Returns node at given index
	public Node elementAt(int index) {
		if(index > size) {
			return null;
		}
		Node n = head;
		while(index-1 != 0) {
			n = n.next;
			index--;
		}
		return n;
	}
	
	//Moves head and tail one node forward
	public void shiftOnce() {
		tail = head;
		head = head.next;
	}
	
	//Sorts list alphabetically
	public void sortAlphabetically() {
		String[] tempStringArray = convertToStringArray();
		Arrays.sort(tempStringArray, Collections.reverseOrder());
		clear();
		for(int i = 0; i < tempStringArray.length; i++)
			addToStart(tempStringArray[i], scoreFromString(tempStringArray[i]));
	}
	
	//Uses insertion sort to sort list by score
	public void sortNumerically() {
		int[] tempScoreArray = convertToIntArray();
		String[] tempStringArray = convertToStringArray();
		
		//Insertion sort 
		int n = tempScoreArray.length;
        for (int i=1; i<n; ++i)
        {
            int key = tempScoreArray[i];
            String charKey = tempStringArray[i];
            int j = i-1;

            while (j>=0 && tempScoreArray[j] > key)
            {
            		tempScoreArray[j+1] = tempScoreArray[j];
            		tempStringArray[j+1] = tempStringArray[j];
                j = j-1;
            }
            tempScoreArray[j+1] = key;
            tempStringArray[j+1] = charKey;
        }

        for(int i = 0; i < size; i++) {
        		head.setCharacter(tempStringArray[i]);
        		head.setScore(tempScoreArray[i]);
        		shiftOnce();
        }

    }
	
	//Returns scrabble score of given character
	public int scoreFromString(String givenString) {
		int foundIndex = 0;
		for(int i = 0; i < letters.length; i++) {
			if(letters[i].equals(givenString)) {
				foundIndex = i;
			}
		}
		return letterScores[foundIndex];
	}
	
	//Empties linked list
	public void clear() {
		while (size != 0)
			removeFromStart();
	}
	
	//Returns character of head node
	public String getFirstCharacter() {
		return head.character;
	}
	
	//Returns score of head node
	public int getFirstScore() {
		return head.score;
	}
	
	//Randomizes positions of nodes in linked list
	public void shuffle() {
		Node tempArray[] = convertToArray();
		shuffleArray(tempArray);
		clear();
		populateWithArray(tempArray);
	}
	
	//Returns true if linked list is empty, false if not
	public Boolean isEmpty() {
		return size == 0;
	}
	
	//Print contents of linked list to console
	public void printTiles() {
		for(int i = 0; i < size; i++) {
			System.out.printf("%s  %d\n",head.character, head.score);
			shiftOnce();
		}
	}

	//Swaps nodes based on index (0 being the head node)
	public void swapTiles(int firstIndex, int secondIndex) {
		Node[] tempArray = convertToArray();
		
		String tempCharacter = tempArray[firstIndex].getCharacter();
		int tempScore = tempArray[firstIndex].getScore();
		
		tempArray[firstIndex].setCharacter(tempArray[secondIndex].getCharacter());
		tempArray[firstIndex].setScore(tempArray[secondIndex].getScore());
		
		tempArray[secondIndex].setCharacter(tempCharacter);
		tempArray[secondIndex].setScore(tempScore);
		
		populateWithArray(tempArray);
	}
	
	//Shuffles a given array
	private void shuffleArray(Node[] givenArray)
	{
	  Random rnd = new Random();
	  for (int i = givenArray.length - 1; i > 0; i--)
	  {
	    int index = rnd.nextInt(i + 1);
	    Node a = givenArray[index];
	    givenArray[index] = givenArray[i];
	    givenArray[i] = a;
	  }
	}
	
	//Clears linked list and fills it with given array
	private void populateWithArray(Node[] givenArray) {
		clear();
		for(int i = givenArray.length-1; i >= 0; i--) {
			addToStart(givenArray[i].getCharacter(), givenArray[i].getScore());
		}
	}
		
	//Convert int values of linked list to an array
	private int[] convertToIntArray() {
		int[] tempIntArray = new int[size];
		for(int i = 0; i < size; i++) {
			tempIntArray[i] = head.getScore();
			shiftOnce();
		}
		return tempIntArray;
	}
		
	//Convert string values of linked list to an array
	private String[] convertToStringArray() {
		String[] tempStringArray = new String[size];
		for(int i = 0; i < size; i++) {
			tempStringArray[i] = head.getCharacter();
			shiftOnce();
		}
		return tempStringArray;
	}	
	
	//Convert nodes of linked list to an array
	private Node[] convertToArray() {
		Node[] tempArray = new Node[size];
		for(int i = 0; i < size; i++) {
			tempArray[i] = head;
			shiftOnce();
		}
		return tempArray;
	}	
	
	class Node{
		String character;
		int score;
		int index;
		Node next;
		
		public Node(String character, int score) {
			this.character = character;
			this.score = score;
		}
		
		public void setCharacter(String newChar) {
			this.character = newChar;
		}
		
		public void setScore(int newScore) {
			this.score = newScore;
		}
		
		public String getCharacter() {
			return character;
		}
		
		public int getScore() {
			return score;
		}
	}
}
