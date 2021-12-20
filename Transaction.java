public class Transaction implements Comparable<Transaction>{
    
    private long TransactionID;
    private long AddressID;
    private long Amount;
    
    public Transaction(long id, Long amount)
    {
        this.TransactionID = id;
        this.Amount = amount;
    }

    public long getID(){
        return this.TransactionID;
    }

    public long getAddressID(){
        return this.AddressID;
    }

    public long getAmount(){
        return this.Amount;
    }

    @Override
    public int compareTo(Transaction o) {
        return Long.compare(o.Amount, this.Amount);
    }

}
