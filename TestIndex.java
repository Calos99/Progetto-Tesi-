import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

/**
* This program tests the construction of a MI-tree index for
* a certain set of 2D records.
*
* In order to run it, the following parameters are required:
*
*   <filename> <capacity>
*
* @author Matteo Loporchio, 491283
*/
public class TestIndex {
  public static void main(String[] args) {  //per ogni blocco, dopo aver costruito l'albero puoi salvare le info in un file
    try {
      // Read the contents from file.
      Blockchain b = Utility.filter(args);
      // Read the number of records.
      int nrec = b.getSize();
      // Read the page capacity from input.
      int c = Integer.parseInt(args[1]);
      // Time the execution of the construction algorithm.
      long tStart = System.nanoTime();
      LinkedList<MITreeNode> T = createTrees(b, c);
      long tEnd = System.nanoTime();
      System.out.println("Number of records: " + nrec);
      System.out.println("Index capacity: " + c);
      System.out.println("Elapsed time: " + (tEnd-tStart) + " ns");

      //analizzo tutti gli alberi costruiti
      ArrayList<Integer> values = new ArrayList<>();
      values.add(0); values.add(0); values.add(0);
      String line = null;
      int i = 0;                                          //usato per prendere gli ID dei blocchi nella Blockchain

      try (BufferedWriter bw=new BufferedWriter(new FileWriter("treesInfo.csv"));) {
        bw.write("ID_Block, Height, N_leaves, N_Transactions, TreeTime, TotalTime, Capacity");
        bw.newLine();
        for (MITreeNode TN : T) {
          analizeTree(TN, values);
          //calcolo l'altezza dell'albero
          values.set(0, hTree(TN, values.get(0)));
          //per ogni albero scrivo le info ottenute nel file "treesInfo"
          line = b.getStorage().get(i).getID()+", "+values.get(0)+", "+values.get(1)+
          ", "+values.get(2)+", "+b.getStorage().get(i).getTime()+", "+(tEnd-tStart)+", "+args[1];

          values = new ArrayList<>();
          values.add(0); values.add(0); values.add(0);

          i++;                    //passo al blocco successivo

          bw.write(line);
          bw.newLine();
        }
      }catch (Exception e) {
        e.printStackTrace();
      }


    }
    catch (Exception e) {
      System.err.println("Something went wrong!");
    }
  }

  //creare la funzione che analizzi un albero
  /* per ogni albero salvi una riga così
  ID_Block, Height, N_leaves, N_Transactions, TreeTime, TotalTime, Capacity 
  */
  private static void analizeTree (MITreeNode node, ArrayList<Integer> values){

    /*
    * values[1] : leaves count
    * values[2] : transactions count
    */

    int size = node.getData().size();

    if(node.isLeaf())
      values.set(1, values.get(1)+1);                             //aumento di 1 il numero delle foglie
    
    values.set(2, values.get(2)+size);                            //conto il numero di transazioni

    List<MITreeNode> temp = node.getChildren();    

    if(temp != null)
    {
      for (MITreeNode MTN : temp) {
          analizeTree(MTN, values);
      }
    }
  }

  //viene usato values[0] come h poiché memorizza l'altezza dell'albero
  private static int hTree (MITreeNode node, int h){

    if(node.getChildren() != null)
      h = hTree(node.getChildren().get(0), h+1);
    else
      return h+1;

    return h;
  }

  private static LinkedList<MITreeNode> createTrees (Blockchain bc, int c){

    LinkedList<MITreeNode> rt = new LinkedList<MITreeNode>();
    ArrayList<Block> temp = bc.getStorage();

    for (Block block : temp) {
      long tStart = System.nanoTime();
      rt.add(MITree.buildPacked(block.getContent(), c));
      long tEnd = System.nanoTime();

      //setting the time elapsed in the block
      bc.getStorage().get(bc.getStorage().indexOf(block)).setTime(tEnd-tStart);
    }

    return rt;
  }
}