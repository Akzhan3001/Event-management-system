package OctopusCard.OctopusCard;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class OCTransactionDB {

    private ArrayList<OCTransaction> transactionList;

    //============================================================
    // constructor
    public OCTransactionDB() {
        transactionList = new ArrayList<>();
    }


    //============================================================
    // loadDB
    public void loadDB(String fName) throws OCTransactionDBException {
        int cnt = 0;
        int lineNo = 1;

        try {
            System.out.println("Loading transaction db from " + fName + "...");
            Scanner in = new Scanner(new File(fName));

            while (in.hasNext()) {
                String line = in.nextLine();
                try {
                    if (addTransaction(line) != null) {
                        cnt++;
                    }
                } catch (OCTransactionDBException | OCTransaction.OCTransactionFormatException e) {
                    System.out.println("OCTransactionDB.loadDB: error loading record from line " + lineNo + " of " + fName + " -- " + e.getMessage());
                }
                lineNo++;
            }
        } catch (FileNotFoundException e) {
            throw new OCTransactionDBException("loadDB failed: File not found (" + fName + ")!");
        }
        System.out.println(cnt + " Octopus card transactions loaded.");
    }


    //============================================================
    // saveDB
    public void saveDB(String fName) throws OCTransactionDBException {
        int cnt = 0;

        PrintWriter out = null;
        try {
            out = new PrintWriter(fName);

        for (OCTransaction ocTransaction : transactionList) {
            out.println(ocTransaction.toRecord());
            cnt++;
        }
        out.close();
        System.out.println(cnt + " Octopus card transactions saved to " + fName + ".");
        } catch (FileNotFoundException e) {
            throw new OCTransactionDBException("File not found: " + fName);
        }
    }


    //============================================================
    // list
    public void list(String type) {
        int cnt = 0;

        // fixme: go through transactionList, and print transactions
        // with matching type (or print all if type is an empty string)
        // Note: (1) should ignore letter case for the type; and
        //       (2) should count the number of records correctly.
        if (type.isEmpty()) {
            for (OCTransaction oct: transactionList) {
                System.out.println(oct.toRecord());
                cnt++;
            }
        } else {
            for (OCTransaction oct: transactionList) {
                if (oct.getType().equalsIgnoreCase(type)) {
                    System.out.println(oct.toRecord());
                    cnt++;
                }
            }
        }
        System.out.println(cnt + " record(s) found.");
    }

    public void list() {
        list("");
    }


    //============================================================
    // addTransaction
    public void addTransaction(OCTransaction newTransaction) throws OCTransactionDBException {
        if (searchIdx(newTransaction.getType(), newTransaction.getDate(), newTransaction.getTransactionID()) != -1) {
            throw new OCTransactionDBException("Duplicate OCTransaction");
        }

        transactionList.add(newTransaction);
    }

    public OCTransaction addTransaction(String record) throws OCTransactionDBException, OCTransaction.OCTransactionFormatException {
        OCTransaction transaction = OCTransaction.parseTransaction(record);

        // skip blank lines
        if (transaction == null) {
            return null;
        }

        addTransaction(transaction);

        if (transaction.getType().equalsIgnoreCase("MTR") && ((MTR) transaction).getMtrType() == MtrType.CheckOut) {
            mtrCheckOut((MTR) transaction);
        }
        return transaction;
    }


    //============================================================
    // mtrCheckOut
    private void mtrCheckOut(MTR chkOutTransaction) throws OCTransactionDBException {
        int i = transactionList.size() - 1;

        while(i>=0) {
            OCTransaction oct = transactionList.get(i);
            if (oct instanceof MTR
                    && ((MTR) oct).getMtrType().name().equalsIgnoreCase(MtrType.CheckIn.name())
                    && oct.getStatus().equals(Status.MTR_OUTSTANDING)) {
                oct.setStatus(Status.MTR_COMPLETED);
                chkOutTransaction.setStatus(Status.MTR_COMPLETED);
                return;
            }
            i--;
        }
        throw new OCTransactionDBException("No outstanding checkin transaction found");
    }

    //============================================================
    // search
    public OCTransaction [] search(String type, String[] criteria) throws OCTransaction.OCTransactionSearchException, ParseException {
        OCTransaction [] searchResult = new OCTransaction[0];
        ArrayList<OCTransaction> list = new ArrayList<OCTransaction>();
        if(!OCTransaction.typeIsValid(type)) {
            throw new OCTransaction.OCTransactionSearchException("Type is not valid: " + type);
        }

        // search through the transactions now
        for (OCTransaction transaction : transactionList) {
            if (transaction.getType().equalsIgnoreCase(type) && transaction.match(criteria)) {
                list.add(transaction);
            }
        }
        return list.toArray(searchResult);
    }


    //============================================================
    // searchIdx
    private int searchIdx(String type, Date date, String transactionID) {
        for (int i = 0; i < transactionList.size(); i++) {
            OCTransaction oct = transactionList.get(i);
            if(oct.getType().equalsIgnoreCase(type)
                && oct.getDate().equals(date)
                && oct.getTransactionID().equalsIgnoreCase(transactionID)) {
                return i;
            }
        }
        return -1;
    }


    //============================================================
    // OCTransactionDBException
    public static class OCTransactionDBException extends Exception {
        public OCTransactionDBException(String ocTransactionDBExMsg) {
            super(ocTransactionDBExMsg);
        }
    }
}
