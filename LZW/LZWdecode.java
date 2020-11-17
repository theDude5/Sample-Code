 /**
  * Implements the LZW data Decompression algorithm
  * @author : Stuart Ussher (1060184)
  */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.util.Stack;

class LZWdecode {
    Node[] nodes;
    int count;
    File file;
    private class Node {
        Stack<Byte> phrase;
        public Node(int pointer) {
            phrase = new Stack<Byte>();
            if (count < 256) { phrase.push((byte) (count + Byte.MIN_VALUE)); }
            else { 
                phrase.addAll(nodes[pointer].phrase);
                if (count > 256) { nodes[count-1].phrase.add(0,phrase.peek()); }
            }         
        }
    }

    public LZWdecode(File file){
        this.file = file;
        nodes = new Node[(int) file.length()/Integer.BYTES+256];
        for (count = 0; count < 256; count++) { nodes[count] = new Node(count); }
    }
    
    public void decode() throws IOException{
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        File output = new File(file.getPath().substring(0,file.getPath().indexOf('.'))+"_lzw.txt");
        DataOutputStream out = new DataOutputStream(new FileOutputStream(output));
        output.createNewFile();
        while (in.available() > 0){
            nodes[count] = new Node(in.readInt()); 
            count++;
        }
        for (int i = 256; i < nodes.length-1; i++) { while (nodes[i].phrase.size() > 1) { out.write(nodes[i].phrase.pop()); } }
        while (!nodes[nodes.length-1].phrase.isEmpty()) { out.write(nodes[nodes.length-1].phrase.pop()); }
        in.close();
        out.close();
    }

    public static void main(String[] args) throws IOException{
        if (args.length == 0){ 
            System.out.println("Usage: java LZWencode <filepath>");
            args = new String[]{"tests/BrownCorpus.lzw"};
            //return;
        }
        File file = new File(args[0]);
        if (!file.exists() || file.length() == 0) { System.out.println("File is empty or does not exist"); }
        else {
            LZWdecode decoder = new LZWdecode(file);
            decoder.decode();
        }
    } 
}