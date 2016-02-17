
import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 * Huffman Encoding
 * Class that uses Huffman Encoding method to compress and decompress files
 * it creates a tree of prefix free codes based on character frequency
 * 2/12/16
 * @author Azhar Hussain
 */
public class HuffmanEncoding {

    // Name of the file being compressed
    private String pathName;

    // Map of character (key) and the times it occurs
    private Map<Character, Integer> frequencyTable;

    // Final tree of all characters and frequencies in descending order
    private BinaryTree<CharacterFrequency> finalTree;

    // Map to store code sequences
    private Map<Character, String> bitcodeSequenceMap;

    // name of compressed file
    private String compressedPathName;

    // name of decompressed file
    private String decompressedPathName;

    /**
     * Constructor
     * creates frequency table and map of character frequencies
     * @return path of chosen file (empty if user cancels)
     */
    public HuffmanEncoding(String pathName) {

        // create frequencyTable
        frequencyTable = new HashMap<Character, Integer>();

        // create bitCode HashMap
        bitcodeSequenceMap = new HashMap<Character, String>();

        // assign pathName
        this.pathName = pathName;
    }


    /**
     * getFilePath
     * Shows file chooser window to select file
     * @return path of chosen file (empty if user cancels)
     */
    public static String getFilePath() {

        // found answer on stack overflow which said to put a period in JFileChooser
        // to make it work. Without period, the file chooser does not open for me
        JFileChooser fc = new JFileChooser(".");
        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {

            File file = fc.getSelectedFile();
            String pathName = file.getAbsolutePath();
            return pathName;
        } else {

            return "";
        }
    }

    /**
     * makeFrequencyTable
     * Read each character in file, and update frequency table to reflect
     * how many times in appears in the file. Add to map.
     *
     */
    public void makeFrequencyTable() throws IOException {
        // Open file
        BufferedReader input = new BufferedReader(new FileReader(pathName));
        try {
            // made an int to hold unicode
            int characterCode;

            // loop through all character in file
            while ((characterCode = input.read()) != -1) {

                // current character
                char character = (char) characterCode;

                // Add to map first time
                if (!frequencyTable.containsKey(character)) {
                    frequencyTable.put(character, 1);
                }

                // else, update frequency
                else {

                    // Replace current with incremented value
                    frequencyTable.put(character, frequencyTable.get(character) + 1);
                }
            }
        }
        finally {

            // Always close file
            input.close();
        }
    }

    /**
     * createTree
     * create singleton tree for each character, add to priorityHeap
     * and then arrange into a final tree in order of occurrence
     */
    public void createTree() {

        //create comparator object to compare two trees
        TreeComparator comparator = new TreeComparator();

        //create priority queue of size one bigger than frequency table and with the comparator object to compare trees
        PriorityQueue<BinaryTree<CharacterFrequency>> treeQueue =
                new PriorityQueue<BinaryTree<CharacterFrequency>>(frequencyTable.size() + 1, comparator);

        //create set of characters keys in frequency table
        Set<Character> characterList = frequencyTable.keySet();

        // for each character in keySet
        for (char character: characterList) {

            //create singleton tree of character frequency object
            CharacterFrequency charFrequency = new CharacterFrequency(character, frequencyTable.get(character));
            BinaryTree<CharacterFrequency> charCountTree = new BinaryTree<CharacterFrequency>(charFrequency);

            // Add singleton to priority queue
            treeQueue.add(charCountTree);
        }

        // if priority queue is empty
        if (treeQueue.size() == 0) {

            // Create dummy tree
            finalTree = new BinaryTree<CharacterFrequency>(new CharacterFrequency('0', 0));
            return;
        }

        // if priority queue contains one item
        if (treeQueue.size() == 1) {

            // create new binary tree object with one real child and another dummy child
            BinaryTree<CharacterFrequency> leftChild = treeQueue.element();

            finalTree = new BinaryTree<CharacterFrequency>(null, leftChild,
                    new BinaryTree<CharacterFrequency>(new CharacterFrequency('0', 0)));

            return;
        }

        // Append to a single tree
        while (treeQueue.size() > 1) {

            // find two lowest frequencies
            BinaryTree<CharacterFrequency> treeOne = treeQueue.remove();
            BinaryTree<CharacterFrequency> treeTwo = treeQueue.remove();

            // create CharacterFrequency object with the frequency passed in from adding together tree frequencies
            CharacterFrequency characterSum =
                    new CharacterFrequency(treeOne.getValue().getFrequency() + treeTwo.getValue().getFrequency());

            // create a root parent tree to hold the sum object and the two respective trees as children
            BinaryTree<CharacterFrequency> rootTree = new BinaryTree<CharacterFrequency>(characterSum,
                    treeOne, treeTwo);

            // add tree back into queue
            treeQueue.add(rootTree);
        }
        // Single tree is remaining element in queue
        finalTree = treeQueue.element();
    }

    /**
     * getCompressionCode
     * traverse finalTree to get code map
     */
    public void getCompressionCode() {

        // create empty string
        String bitSequence = "";

        // traverse the tree to add to the string
        traverseTree(finalTree, bitSequence);
    }

    /**
     * traverseTree
     * Create a map (from a single traversal of the tree) that pairs characters
     * with code, where code describes path from root to that character
     * This is done recursively
     * @param tree pass in the tree that should be traversed
     * @param bitSequence pass in the string that should be appended to for sequence
     */
    public void traverseTree(BinaryTree<CharacterFrequency> tree, String bitSequence) {
        // if the tree has a left child
        if (tree.hasLeft()) {

            // add 0 since it is a left child
            traverseTree(tree.getLeft(), bitSequence + "0");
        }
        // if the tree has a right child
        if (tree.hasRight()) {

            // add 1 since it is a right child
            traverseTree(tree.getRight(), bitSequence + "1");
        }
        //if leaf
        else {
            // save in map with character as key
            bitcodeSequenceMap.put(tree.getValue().getCharacter(), bitSequence);
        }
    }

    /**
     * compress
     * use the codeMap created to compress file
     */
    public void compress() throws IOException {
        // get file name without suffix
        compressedPathName = pathName.substring(0, pathName.length() - 4);

        // add compressed suffix
        compressedPathName += "_compressed.txt";

        // call provided bitWriter class to create new writer object
        BufferedBitWriter bitOutput = new BufferedBitWriter(compressedPathName);

        // use Java BufferedReader class to read each character in file
        BufferedReader input = new BufferedReader(new FileReader(pathName));

        try {
            int character;    // store character as an int for unicode
            while ((character = input.read()) != -1) {

                // cast int as char
                char ch = (char) character;

                // find characters bitcode sequence
                String characterBitCode = bitcodeSequenceMap.get(ch);

                // Write each bit in the code to the new file
                for (char bit: characterBitCode.toCharArray()) {

                    // if left child, write 0 to output
                    if (bit == '0') {
                        bitOutput.writeBit(0);
                    }

                    // if right child, write 1 to output
                    else if (bit == '1') {
                        bitOutput.writeBit(1);
                    }
                }
            }
        } finally {

            // Close both files
            bitOutput.close();
            input.close();
        }
    }

    /**
     * decompress
     * use code tree to decompress the file to original state
     */
    public void decompress() throws IOException {

        // get file name and remove suffix
        decompressedPathName = pathName.substring(0, pathName.length() - 4);

        // add decompressed suffix
        decompressedPathName += "_decompressed.txt";

        // use Java BufferedReader class to read each character in class
        BufferedWriter output = new BufferedWriter(new FileWriter(decompressedPathName));

        // use provided bitReader class to read each bit from compressed
        BufferedBitReader bitInput = new BufferedBitReader(compressedPathName);

        try {

            // int value of current bit
            int c;

            // Tree to allow retrieval
            BinaryTree<CharacterFrequency> tree = finalTree;

            // Read compressed file bit by bit
            while ((c = bitInput.readBit()) != -1) {

                // if left child
                if (c == 0 && tree.hasLeft()) {

                    // get the left child
                    tree = tree.getLeft();
                }

                // if the bit has a right child
                else if (c == 1 && tree.hasRight()) {

                    // get the right child
                    tree = tree.getRight();
                }

                // if leaf
                if (!tree.hasRight() && !tree.hasLeft()) {

                    // get character contained in that location
                    char character = tree.getValue().getCharacter();

                    // write to the new output file
                    output.write(character);

                    // return to root
                    tree = finalTree;
                }
            }
        } finally {

            // close both files
            bitInput.close();
            output.close();
        }
    }

    public static void main(String[] args) {
        String textFile = getFilePath();

        try {

            // new Huffman Encoding object on the selected textFile
            HuffmanEncoding huffmanEncoding = new HuffmanEncoding(textFile);

            // order of functions to create a table, make tree, get code, compress, and decompress
            huffmanEncoding.makeFrequencyTable();
            huffmanEncoding.createTree();
            huffmanEncoding.getCompressionCode();
            huffmanEncoding.compress();
            huffmanEncoding.decompress();

        } catch (IOException e) {
            System.err.println("File not found or can't be read");
        }
    }
}
