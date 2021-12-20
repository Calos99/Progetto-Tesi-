import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

public class Subchain {
    public static void main(String[] args) {
        System.out.println("Reading file "+args[0]+" into "+args[1]+" from block "+args[2]+" to block "+args[3]+", both included.");
        if(args.length<4){
          System.err.println("Not enough arguments.");
          System.exit(1);}
        int firstBlock=Integer.parseInt(args[2]);
        if(firstBlock<0){
          System.out.println("Not possible to read from block "+firstBlock+", starting from block 0 instead.");
          firstBlock = 0;
        }
        int lastBlock=Integer.parseInt(args[3]);
        if(lastBlock<firstBlock){
          System.err.println("Not possible to read from block "+firstBlock+" to "+lastBlock+" .");
          System.exit(1);
        }
        //NOTE: lastBlock>=firstBlock    
          
        int currentLine=0;
        boolean continueSkipping = true;
        long lastBlockRead=-1;
    
        String line = null;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]));  
             BufferedWriter bw=new BufferedWriter(new FileWriter(args[1]));) 
        {
          //skip first (firsBlock-1) blocks
          while ((continueSkipping)&&((line=reader.readLine()) != null)){
          //&& guarantees that line is read only if first check is true
          if((currentLine%1000000)==0)
            System.out.println("Read "+currentLine+" lines.");
          currentLine++;
          lastBlockRead = getBlockID(line);
          if(lastBlockRead >= firstBlock)
            continueSkipping = false;
          }
          if(continueSkipping){
            //not enough blocks read before EOF
            System.err.println("Read "+currentLine+" lines when EOF reached."
            + " Last block found at height "+lastBlockRead+" , unable to reach "+firstBlock+" block as requested.");
          System.exit(0);
          }
    
          //if we reach here we have at least one line with initial block id already read
          while ((lastBlockRead<=lastBlock)&&(line != null)){
            bw.write(line);
            bw.newLine();
            line=reader.readLine();
            if((currentLine%1000000)==0)
              System.out.println("Read "+currentLine+" lines.");
            currentLine++;
            if(line!=null){
              lastBlockRead = getBlockID(line);
            }
          }
      
          System.out.println("DONE writing blocks from height "+firstBlock+" to "+lastBlock+" in file "+args[1]+" . "+currentLine+" lines read.");
        } catch (Exception e) {
          e.printStackTrace();
        }
    }

    private static Long getBlockID(String line){
        if(line == null)
          return (long) -1;
        else
          return (Long.parseLong(line.split(":")[0].split(",")[1]));
    }   
}
