import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.ArrayList;
import java.util.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.text.NumberFormat;

class Main {
    private static final int[] row = { -1, -1, -1, 0, 1, 0, 1, 1 };
    private static final int[] col = { -1, 1, 0, -1, -1, 1, 0, 1 };
    private static int size;

    private static final String[] Dictionary = readArray("Words.txt");
    private static final Set<String> finalDictionary = removeShortWords(convertToSet(Dictionary));
    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setGroupingUsed(true);

        while (true) {
            ArrayList < char[][] > maps = new ArrayList < char[][] > ();
            ArrayList < char[][] > parents = new ArrayList < char[][] > ();
            ArrayList < Integer > parentMapsPoints = new ArrayList < Integer > ();
            int gen = 1;
            System.out.println("Pick a size of the board");
            size = reader.nextInt();
            System.out.println("Starting parent?");
            String startingParent = reader.next();
            if(startingParent.equals("yes")) {
                System.out.println("type in starting parent");
                String input = reader.next();
                input = input.toUpperCase();
                input = flattenGrid(input);
                char[][] map = create2DArray(input, size);
                maps.add(map);
                parentMapsPoints.add(getTotalPointsFromDictionary(map, finalDictionary));
            }
            System.out.println("How many starting boards?");
            int numStart = reader.nextInt();
            System.out.println("How many parents per generation?");
            int numParents = reader.nextInt();
            System.out.println("How many children per generation?");
            int numChildren = reader.nextInt();
            System.out.println("Mutation Rate");
            double mutationRate = reader.nextDouble();
            System.out.println("____________________________");
            for (int i = 0; i < numStart; i++) { // number of starting children
                Board b = new Board(size);
                char[][] map = b.getMap();
                maps.add(map);
                System.out.println("Starting child: " + (i+1));
                printBoard(map);
                int totalPoints = getTotalPointsFromDictionary(map, finalDictionary);
                System.out.println("Total Points: " + nf.format(totalPoints));
                System.out.println("____________________________");
                parentMapsPoints.add(totalPoints);
            }
            parents = findParents (maps, parentMapsPoints, numParents);
            parentMapsPoints = findParentsPoints(parentMapsPoints, numParents);


            for (int j = 0; j < numParents; j++) {
                System.out.println("Parent " + (j+1));
                printBoard(parents.get(j));
                System.out.println("Total Points: " + nf.format(parentMapsPoints.get(j)));
                System.out.println("____________________________");
            }
            System.out.println("Generations");
            while (parentMapsPoints.get(0) > parentMapsPoints.get(numParents - 1)) { //i = number of generation
                System.out.println("____________________________");
                for(int j = 0; j < numChildren; j++) { //number of children per generation
                    char [][] childMap = new char [size][size];
                    System.out.println("Gen " + (gen));
                    System.out.println("Child " + (j+1));
                    for (int r = 0; r < childMap.length; r++) {
                        for (int c = 0; c < childMap[0].length; c++) {
                            boolean letter = true;
                            double number = Math.random();
                            for (int i = 1; i < parents.size(); i++) {
                                if (parents.get(0)[r][c] != (parents.get(i)[r][c])) {
                                    letter = false;
                                }
                            }
                            if (letter) {
                                childMap[r][c] = parents.get(0)[r][c];
                            }
                            else if (number < mutationRate) {
                                childMap[r][c] = getRandomLetter();
                            }
                            else {
                                childMap[r][c] = parents.get(getRandomNumber(numParents - 1))[r][c];
                            }
                        }
                    }
                    int childPoints = getTotalPointsFromDictionary (childMap, finalDictionary);
                    printBoard(childMap);
                    System.out.println("Total Points: " + nf.format(childPoints));
                    System.out.println("____________________________");
                    parents.add(childMap);
                    parentMapsPoints.add(childPoints);
                }
                parents = findParents (parents, parentMapsPoints, numParents);
                parentMapsPoints = findParentsPoints (parentMapsPoints, numParents);
                for (int j = 0; j < numParents; j++) {
                    System.out.println("Parent " + (j+1));
                    printBoard(parents.get(j));
                    System.out.println("Total Points: " + nf.format(parentMapsPoints.get(j)));
                    System.out.println("____________________________");
                }
                gen++;
            }

            ArrayList<String> list = setToList((searchBoggle(parents.get(0), finalDictionary)));

            sortWords(list);

            // print final code
            System.out.println("\n" + "All Words:");
            System.out.println("____________________________");
            for (int i = 0; i < list.size(); i++) {
                System.out.format("%-20s %8s", list.get(i), nf.format(getPoints(list.get(i))) + "\n");
            }
            System.out.format("%-20s %8s", "Total Words: ", nf.format(list.size()) + "\n");
            System.out.format("%-20s %8s", "Total Points: ", nf.format(parentMapsPoints.get(0)) + "\n");
            System.out.println("____________________________");
            System.out.println("Best Board");
            printBoard(parents.get(0));
            System.out.println("Gen: " + gen);
            System.out.println("____________________________");
        }
    }
    public static ArrayList<String> setToList(Set<String> set) {
        // Create a new list

        // Add the elements from the set to the list

        return new ArrayList<>(set);
    }
    public static int getPoints (String x) {

        if (x.length() < 3) {
            return 0;
        }
        if (x.length() == 3) {
            return 100;
        }
        if (x.length() == 4) {
            return 400;
        }
        if (x.length() == 5) {
            return 800;
        }
        if (x.length() == 6) {
            return 1400;
        }
        return (1400 + (400 * (x.length() - 6)));
    }
    public static void sortWords(ArrayList<String> words) {
        // Sort the list using a comparator
        words.sort((w1, w2) -> {
            int lengthDiff = w1.length() - w2.length();
            if (lengthDiff == 0) {
                // If the words have the same length, compare them lexicographically
                return w1.compareTo(w2);
            }
            // If the words have different lengths, compare their lengths
            return lengthDiff;
        });
    }
    public static char getRandomLetter() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int index = (int)(Math.random() * letters.length());
        return letters.charAt(index);
    }
    public static Set<String> convertToSet(String[] array) {
        return new HashSet<>(Arrays.asList(array));
    }
    public static void printBoard(char[][] x) {
        for (int r = 0; r < x.length; r++) {
            for (int c = 0; c < x[0].length; c++) {
                System.out.print(x[r][c] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public static String[] readArray(String file) {
        int ctr = 0;
        try {
            Scanner s1 = new Scanner(new File(file));
            while (s1.hasNextLine()) {
                ctr++;
                s1.next();
            }

            String[] Dictionary = new String[ctr];
            Scanner s2 = new Scanner(new File(file));
            for (int i = 0; i < ctr; i++) {
                Dictionary[i] = s2.next();
            }
            return Dictionary;
        } catch (FileNotFoundException ignored) {

        }
        return null;
    }
    public static ArrayList<Integer> getPoints(Set<String> words) {
        ArrayList<Integer> points = new ArrayList<>();

        for (String word : words) {
            int len = word.length();

            if (len < 3) {
                points.add(0);
            } else if (len == 3) {
                points.add(100);
            } else if (len == 4) {
                points.add(400);
            } else if (len == 5) {
                points.add(800);
            } else if (len == 6) {
                points.add(1400);
            } else {
                points.add(1400 + (400 * (len - 6)));
            }
        }

        return points;
    }
    public static Integer getTotalPoints (ArrayList <Integer> x) {
        int total = 0;
        for (Integer number : x) {
            total += number;
        }
        return total;
    }
    public static Integer getTotalPointsFromDictionary (char[][] map, Set<String> finalDictionary) {
        ArrayList < Integer > dictionaryPoints = new ArrayList < Integer > ();
        Set<String> validWords = searchBoggle(map, finalDictionary);
        dictionaryPoints = getPoints(validWords);
        return getTotalPoints(dictionaryPoints);
    }
    public static int[] getSortedArray(ArrayList<Integer> list) {
        // Convert the ArrayList to an array
        int[] array = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i);
        }

        // Sort the array
        java.util.Arrays.sort(array);

        // Return the sorted array
        return reverseArray(array);
    }
    public static int[] reverseArray(int[] array) {
        // Create a new array with the same length as the original array
        int[] reversedArray = new int[array.length];

        // Copy the elements of the original array to the new array in reverse order
        for (int i = 0; i < array.length; i++) {
            reversedArray[i] = array[array.length - 1 - i];
        }

        // Return the reversed array
        return reversedArray;
    }
    public static int getRandomNumber(int n) {
        // Use the Random class to generate a random integer between 0 and n
        Random random = new Random();
        return random.nextInt(n + 1);
    }
    public static ArrayList < char[][] > findParents (ArrayList<char[][]> maps, ArrayList<Integer> mapsPoints, int numParents) {
        int[] sortedMapPoints = new int[numParents];
        ArrayList < char[][] > parents = new ArrayList < char[][] > ();
        sortedMapPoints = getSortedArray(mapsPoints);
        for (int i = 0; i < numParents; i++) {
            parents.add(maps.get(mapsPoints.indexOf(sortedMapPoints[i])));
        }
        return parents;
    }
    public static ArrayList<Integer> findParentsPoints(ArrayList<Integer> mapsPoints, int numParents) {
        // Sort the mapsPoints ArrayList in descending order
        mapsPoints.sort(Collections.reverseOrder());

        // Create a new ArrayList to store the top parents
        ArrayList<Integer> topParents = new ArrayList<Integer>();

        // Add the first numParents elements from the sorted mapsPoints ArrayList to the topParents ArrayList
        for (int i = 0; i < numParents && i < mapsPoints.size(); i++) {
            topParents.add(mapsPoints.get(i));
        }

        // Return the topParents ArrayList
        return topParents;
    }
    private static void insert(Trie root, String str) {
        // start from the root node
        Trie curr = root;

        for (char ch: str.toCharArray())
        {
            // create a new node if the path doesn't exist
            curr.character.putIfAbsent(ch, new Trie());

            // go to the next node
            curr = curr.character.get(ch);
        }

        curr.isLeaf = true;
    }
    public static boolean isSafe(int x, int y, boolean[][] processed, char[][] board, char ch)
    {
        return (x >= 0 && x < size) && (y >= 0 && y < size) &&
                !processed[x][y] && (board[x][y] == ch);
    }
    public static void searchBoggle(Trie root, char[][] board, int i, int j, boolean[][] processed, String path, Set<String> result)
    {
        // if a leaf node is encountered
        if (root.isLeaf) {
            // update result with the current word
            result.add(path);
        }

        // mark the current cell as processed
        processed[i][j] = true;

        // traverse all children of the current Trie node
        for (var entry: root.character.entrySet())
        {
            // check for all eight possible movements from the current cell
            for (int k = 0; k < row.length; k++)
            {
                // skip if cell is invalid or entry is already processed
                // or doesn't lead to any path in the Trie
                if (isSafe(i + row[k], j + col[k], processed, board, entry.getKey()))
                {
                    searchBoggle(entry.getValue(), board, i + row[k], j + col[k],
                            processed, path + entry.getKey(), result);
                }
            }
        }

        // backtrack: mark the current cell as unprocessed
        processed[i][j] = false;
    }
    public static Set<String> searchBoggle(char[][] board, Set<String> words)
    {
        // construct a set for storing the result
        Set<String> result = new HashSet<>();

        // base case
        if (board.length == 0) {
            return result;
        }

        // `M × N` board
        int M = board.length;
        int N = board[0].length;

        // insert all words into a trie
        Trie root = new Trie();
        for (String word: words) {
            insert(root, word);
        }

        // construct a boolean matrix to store whether a cell is processed or not
        boolean[][] processed = new boolean[M][N];

        // consider each character in the matrix
        for (int i = 0; i < M; i++)
        {
            for (int j = 0; j < N; j++)
            {
                // current character
                char ch = board[i][j];

                // proceed only if the current character is a child of the Trie root
                if (root.character.containsKey(ch))
                {
                    searchBoggle(root.character.get(ch), board, i, j,
                            processed, Character.toString(ch), result);
                }
            }
        }

        // return the result set
        return result;
    }
    public static Set<String> removeShortWords(Set<String> words) {
        return words.stream()
                .filter(word -> word.length() >= 3)
                .collect(Collectors.toSet());
    }
    public static String flattenGrid(String input) {
        // Use a regular expression to match any sequence of whitespace characters
        String regex = "\\s+";

        // Split the input string into an array of strings using the regular expression
        String[] rows = input.split(regex);

        // Create a new StringBuilder to store the flattened grid
        StringBuilder sb = new StringBuilder();

        // Iterate through each row in the input string
        for (String s : rows) {
            // Append the current row to the StringBuilder
            sb.append(s);
        }

        // Return the flattened grid
        return sb.toString();
    }
    public static char[][] create2DArray(String input, int size) {

        // Create an empty sizexsize 2D array
        char[][] arr = new char[size][size];

        // Iterate through the input string and place each letter in the 2D array
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int index = i * size + j;
                if (index >= 0 && index < input.length()) {
                    arr[i][j] = input.charAt(index);
                }
            }
        }

        return arr;
    }
}