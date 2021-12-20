import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

/**
* This class contains a set of static utility methods that can be used to
* load blockchains and sets of records from files. 
*
* @author	Matteo Loporchio, 491283
*/
public final class Utility {

  /**
  * This function reads a binary file containing 2D points and returns
  * a list that contains them.
  * @param filename path of the input file
  * @return a list of points
  */
  /*public static List<Transaction> readTransactionsB(String filename) throws Exception {
    Path path = Paths.get(filename);
    byte[] content = Files.readAllBytes(path);
    ByteBuffer buf = ByteBuffer.wrap(content);
    List<Transaction> pts = new ArrayList<Transaction>();
    while (buf.hasRemaining()) {
      int x = buf.get();
      pts.add(x);
    }
    return pts;
  }*/

  /**
  * This function creates a new blockchain by reading its content
  * from a given binary file. The index inside each block is built
  * with a page capacity equal to c.
  * @param filename path of the input file
  * @param c page capacity of the block index
  * @param m size of the skip list
  * @return a blockchain result object with the chain and construction times
  */
 /* public static BlockchainRes readChainB(String filename, int c, int m)
  throws Exception { //questo è il metodo da riscrivere per il parsing
    long indexAvg = 0, skipAvg = 0;
    Path path = Paths.get(filename);
    byte[] content = Files.readAllBytes(path);
    ByteBuffer buf = ByteBuffer.wrap(content);
    // Create a new blockchain object.
    Blockchain chain = new Blockchain(c);
    // Read the number of blocks.
    int nblocks = buf.getInt();
    // Now, for each block...
    for (int i = 0; i < nblocks; i++) {
      // Read the number of records it contains.
      int nrec = buf.getInt();
      // Now collect all records and insert them into a list.
      ArrayList<Transaction> records = new ArrayList<Transaction>();
      for (int j = 0; j < nrec; j++) {
        Long x = buf.getLong();
        records.add(x);
      }
      // Create a new block and append it to the chain.
      BlockRes res = chain.append(records);
      indexAvg += res.indexTime;
      skipAvg += res.skipTime;
    }
    return new BlockchainRes(chain, indexAvg/nblocks, skipAvg/nblocks);
  }*/
    
  /**
  first arg name input file
  * second arg name output files
  * third arg initial block heigth (>=0) INCLUDED
  * fourth arg final block heigth INCLUDED 
  * fifth arg blocks capacity
  * 
  * e.g.
  * last 100 blocks:
  * java -jar BCsemplifyAndSplit.jar finalBC txList100blocks 479869 479969
  */
        
  public static Blockchain filter(String[] args) {
    String line = null;
    ArrayList<Transaction> ts = null;
    List<Transaction> transactions = null;
    Blockchain bc = new Blockchain(Integer.parseInt(args[1]));
    long blockID = 0;

    try (//BufferedReader reader = new BufferedReader(new FileReader(args[0]));  BufferedWriter bw=new BufferedWriter(new FileWriter(args[1]));
         BufferedReader read = new BufferedReader(new FileReader(args[0]));
        ) 
      {
        //dopo aver copiato i blocchi interessati in args[1] lo leggo per costruire l'albero
        while((line = read.readLine()) != null)
        {
          //creo un blocco e vi pongo tutte le transazioni
          ts = readInfo(line);

          if(blockID == getBlockID(line))
          {
            //se ho già trovato questo ID aggiungo tutte le transazioni al blocco che attualmente è l'ultimo inserito
            for (Transaction transaction : ts) {
              bc.getLast().AddTransaction(transaction);
            }
          }
          else
          {
            blockID = getBlockID(line);
            bc.append(ts, blockID);
          }
        }

      read.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return bc;
  }

  private static Long getBlockID(String line){
    if(line == null)
      return (long) -1;
    else
      return (Long.parseLong(line.split(":")[0].split(",")[1]));
  }

  /*
  public static BlockchainRes readChainLarge(String filename, int c, int m)
  throws Exception {
    long indexAvg = 0, skipAvg = 0;
    // Open the file.
    File f = new File(filename);
    InputStream is = new BufferedInputStream(new FileInputStream(f));
    // Create a new blockchain object.
    Blockchain chain = new Blockchain(c, m);
    // Read the number of blocks.
    byte[] buf = new byte[4];
    is.read(buf);
    int nblocks = ByteBuffer.wrap(buf).getInt();
    // Now, for each block...
    for (int i = 0; i < nblocks; i++) {
      System.out.println("reading block " + i);
      // Read the number of records it contains.
      is.read(buf);
      int nrec = ByteBuffer.wrap(buf).getInt();
      // Read the content of the block.
      byte[] block = new byte[nrec * 16];
      is.read(block);
      ByteBuffer blockBuf = ByteBuffer.wrap(block);
      // Now collect all records and insert them into a list.
      List<Point> records = new ArrayList<Point>();
      for (int j = 0; j < nrec; j++) {
        double x = blockBuf.getDouble(), y = blockBuf.getDouble();
        records.add(new Point(x, y));
      }
      // Create a new block and append it to the chain.
      BlockRes res = chain.append(records);
      indexAvg += res.indexTime;
      skipAvg += res.skipTime;
    }
    is.close();
    return new BlockchainRes(chain, indexAvg/nblocks, skipAvg/nblocks);
  }
  */

  /**
  * This function chops a list into sublists of a given length.
  * @param line the string to be divided
  * @return a list containing all the transactions
  */
  public static ArrayList<Transaction> readInfo (String line) {
    String[] str;                           //è la riga letta, viene suddivisa per ':'
    String[] info;                          //prima porzione ottenuta dividendo str

    String[] outputs;                       //terza porzione di str
    String[] output = null;                 //come per input, si ottiene dividendo ouputs per ';'

    str = line.split(":");
    info = str[0].split(",");
    outputs = str[str.length-1].split(";"); 

    ArrayList<Transaction> l = new ArrayList<Transaction>();
    Transaction t;

    for(int i = 0; i < outputs.length; i++)
    {
      output = outputs[i].split(",");

      t = new Transaction(Long.valueOf(info[2]), Long.valueOf(output[1]));
      l.add(t);
    }   

    return l;
  }

  /**
  * This function chops a list into sublists of a given length.
  * @param l the list to be divided
  * @param k the number of elements in each sublist
  * @return a list containing all the sublists
  */
  public static <T> List<List<T>> split(List<T> l, int k) {
    List<List<T>> parts = new ArrayList<List<T>>();
    for (int i = 0; i < l.size(); i += k) {
      //parts.add(new ArrayList<T>(l.subList(i, Math.min(l.size(), i + k))));
      parts.add(l.subList(i, Math.min(l.size(), i + k)));
    }
    return parts;
  }
}
