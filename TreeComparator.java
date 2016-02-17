
import java.util.*;

/**
 * TreeComparator
 * Takes two Tree nodes and compares their frequency values to determine order
 * @author Azhar Hussain
 *
 */
public class TreeComparator implements Comparator<BinaryTree<CharacterFrequency>> {

    /**
     * compare
     * Return different values depending on the comparison of frequencies
     * for CharCount objects in the binary tree
     */
    public int compare(BinaryTree<CharacterFrequency> firstTree, BinaryTree<CharacterFrequency> secondTree) {
        // if firstTree frequency value is less than secondTree frequency value, return -1
        if (firstTree.getValue().getFrequency() < secondTree.getValue().getFrequency()) {
            return -1;
        }
        // if firstTree frequency value is greater than secondTree frequency value, return 1
        else if (firstTree.getValue().getFrequency() > secondTree.getValue().getFrequency()) {
            return 1;
        }
        // if the frequency is the same, return 0
        else {
            return 0;
        }
    }
}