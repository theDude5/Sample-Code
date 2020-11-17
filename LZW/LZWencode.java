/**
 * Implements the LZW data compression algorithm
 * @author : Stuart Ussher (1060184)
 */
import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;

class LZWencode {
    private final Node[] root; // trie root
    private Node pointer;
    private int count; // phrase counter
    private class Node {
        int rank; // phrase number
        ArrayList<Node> dictionary;
        ArrayList<Byte> index; // index of nodes
        private Node(int rank, byte key) {
            this.rank = rank;
            dictionary = new ArrayList<Node>();
            index = new ArrayList<Byte>();
        }
        
        public int query(byte key){
            int temp = index.indexOf(key);
            if (temp != -1) {
                pointer = dictionary.get(temp);
                return -1; // continue down tree
            }
            else { // add new node, index node and go back to root
                dictionary.add(new Node(count++, key));
                index.add(key);
                pointer = root[key - Byte.MIN_VALUE]; // start new phrase beginning with unmatched value
                return rank;
            }
        }
    }

    public LZWencode() {
        root = new Node[256];
        for (count = 0; count < root.length; count++) { root[count] = new Node(count,(byte) (count + Byte.MIN_VALUE)); }
    }

    /**
     * Implements LZW algorithm
     * @param file File to encode
     * @throws IOException
     */
    public void encode(File file) throws IOException {
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        if (in.available() > 0) {
            File output = new File(file.getPath().substring(0,file.getPath().indexOf('.'))+".lzw");
            output.createNewFile();
            DataOutputStream out = new DataOutputStream(new FileOutputStream(output));
            int result;
            pointer = root[in.readByte() - (byte) Byte.MIN_VALUE];
            while (in.available() > 0) {
                result = pointer.query(in.readByte());
                if (result != -1){
                    out.writeInt(result); 
                }
            }
            out.writeInt(pointer.rank);
            out.close();
        }
        in.close();
    }

    /**
     * LZW Encoder Implementation
     * @param args
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws FileNotFoundException, IOException { 
        if (args.length == 0){ 
            System.out.println("Usage: java LZWencode <filepath>");
            args = new String[]{"tests/BrownCorpus.txt"};
            //return;
        }
        File file = new File(args[0]);
        if (!file.exists() || file.length() == 0) { System.out.println("File is empty or does not exist"); }
        else {
            LZWencode encoder = new LZWencode();
            encoder.encode(file);
        }
    }
}