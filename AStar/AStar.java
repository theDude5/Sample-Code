/**
 * @author: Stuart Ussher
 Uses the AStar search algorithm to find the shortest path through an ascii art maze. 
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import java.awt.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AStar extends JPanel {
    private final int[][] Valid = {{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
    //private final int[][] Valid = {{1,0},{0,1},{-1,0},{0,-1}};
    private class Site{
        char val;
        int x,y;
        double h,c;
        Site prev;
        public Site(char val, int x, int y){
            this.val = val;
            this.x = x; this.y = y;
            if (val == 'G'){ goal = this; }
            else if (val == 'S') { start = this; frontier.add(this); }
        }
        public void calc_distance() { h = "S G".contains(val+"")? Math.sqrt(Math.pow(x-goal.x, 2) + Math.pow(y-goal.y, 2)) : -1; }
    }

    Site[][] map;
    Site goal, start, pos;
    ArrayList<Site> frontier;
    public AStar(ArrayList<String> _map) {
        frontier = new ArrayList<Site>();
        this.map = new Site[_map.size()][_map.get(0).length()];
        for (int i = 0; i < this.map.length; i++) {
            for (int j = 0; j < this.map[i].length; j++) { this.map[i][j] = new Site(_map.get(i).charAt(j), i, j); }
        }
        //printMap();
        for (Site[] row : map) { for (Site cell : row) { cell.calc_distance(); } }
        expand();
        try { while (pos != goal) { expand(); }} 
        catch (Error e) { System.err.println(e.getMessage()); return; }
        while (pos.prev != start) { pos = pos.prev; pos.val = '.'; }
        printMap();
    }

    public void expand() {
        if (frontier.isEmpty()){ throw new Error("Solution does not exist"); }
        pos = frontier.get(0);
        for (Site site : frontier) { if (site.h + site.c < pos.h + pos.c) { pos = site; }}
        if (pos == goal) { return; }
        Site temp;
        double cost;
        for (int[] coord : Valid) {
            temp = map[pos.x + coord[0]][pos.y + coord[1]];
            if (temp.h < 0) { continue; }
            cost = coord[0] == 0 || coord[1] == 0? 1 : Math.sqrt(2);
            if (temp.prev == null) {
                temp.prev = pos; temp.c = pos.c+cost;
                frontier.add(temp);
            }
            else if (pos.c+cost < temp.c) { temp.prev = pos; temp.c = pos.c+cost; }
        }
        frontier.remove(pos);
    }

    public void printMap() {
        for (Site[] row : map) {
            for (Site cell : row) { System.out.print(cell.val); }
            System.out.println();
        }
        //System.out.printf("Start: (%d,%d)\t Goal: (%d,%d)\n", start.x, start.y, goal.x, goal.y);
    }

    public void paint(Graphics g) {  
        Graphics2D g2 = (Graphics2D) g;
        g2.scale(10, 10);
        g2.setColor(Color.BLACK);
        for (Site[] row : map) { for (Site site : row) { if (site.h < 0) { g2.fillRect(site.y+2, site.x+1, 1,1); } } }
        g2.setColor(Color.BLUE);
        g2.fillRect(start.y+2, start.x+1, 1,1);
        g2.fillRect(goal.y+2, goal.x+1, 1,1);
        pos = goal.prev;
        g2.setColor(Color.RED);
        while (pos != null && pos != start) {
            g2.fillRect(pos.y+2, pos.x+1, 1,1);
            pos = pos.prev;
        }
    }
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length == 0){
            System.out.println("Usage: java AStar <filepath>");
            args = new String[]{"map3.txt"};
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
        frame.add(new JScrollPane(panel), BorderLayout.CENTER);
        frame.setBounds(0, 0, 11*map.get(0).length()+10, 11*map.size()+10);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
