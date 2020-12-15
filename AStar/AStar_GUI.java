/**
 * @author: Stuart Ussher
   A simple GUI for the AStar class 
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AStar_GUI {
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0){
            System.out.println("Usage: java AStar <filepath>");
            args = new String[]{"60x60.txt"};
            //return;
        }

        Scanner scanner = new Scanner(new File((args[0])));
        ArrayList<String> map = new ArrayList<String>();
        while (scanner.hasNext()) { map.add(scanner.nextLine()); }
        scanner.close();
        AStar aStar = new AStar(map);

        JFrame frame = new JFrame(args[0]);
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        panel.setLayout(new GridLayout(1,1,0,0));
        panel.add(aStar);
        panel.setPreferredSize(aStar.getDimension(20));
        JScrollPane scrollPane  = new JScrollPane(panel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(100);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(100);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setBounds(0, 0, aStar.getDimension(20).width, aStar.getDimension(20).height);
        frame.setMinimumSize(new Dimension(200,200));
        frame.setMaximumSize(new Dimension(1000,750));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
