package OctopusCard.OctopusCard;

import OctopusCard.OctopusCard.Util.DateTimeUtil;
import OctopusCard.OctopusCard.Util.Tokenizer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OCTransaction {
    private final Date date;
    private final String transactionID;
    private final double amount;
    private final String type;
    private Status status;


    //============================================================
    // Constructors
    public OCTransaction(String type, String dateTimeStr, String transactionID, String amountStr) throws OCTransactionFormatException {
        this(type, parseDateTimeStr(dateTimeStr), transactionID, parseAmountStr(amountStr));
    }

    public OCTransaction(String type, Date date, String transactionID, double amount) throws OCTransactionFormatException {
        this.type = type;
        this.date = date;
        this.transactionID = transactionID;
        this.amount = amount;

        // chk type
        if (!typeIsValid(this.type)) {
            throw new OCTransactionFormatException("Invalid transaction type: " + this.type);
        }
    }

    public static boolean typeIsValid(String type) {
        if (type.equalsIgnoreCase(MTR.TypeHdrStr)) {
            return true;
        } else if (type.equalsIgnoreCase(BusFare.TypeHdrStr)) {
            return true;
        } else if (type.equalsIgnoreCase(Retail.TypeHdrStr)) {
            return true;
        } else if (type.equalsIgnoreCase(TopUp.TypeHdrStr)) {
            return true;
        } else {
            return false;
        }
    }


    //============================================================
    // parseTransaction: type dateTime transactionID amount...
    public static OCTransaction parseTransaction(String record) throws OCTransactionFormatException {
        String [] tokens = Tokenizer.getTokens(record);

        // chk for blank line (no tokens)
        if (tokens.length == 0) {
            return null;
        }

        // fixme: work on different types of OCTransactions,
        // chk transaction type
        String transactionType = tokens[0];
        if (transactionType.equalsIgnoreCase(MTR.TypeHdrStr)) {
            return MTR.parseTransaction(record);
        } else if (transactionType.equalsIgnoreCase(BusFare.TypeHdrStr)) {
            return BusFare.parseTransaction(record);
        } else if (transactionType.equalsIgnoreCase(Retail.TypeHdrStr)) {
            return Retail.parseTransaction(record);
        } else if (transactionType.equalsIgnoreCase(TopUp.TypeHdrStr)) {
            return TopUp.parseTransaction(record);
        } else {
            throw new OCTransactionFormatException("parseTransaction: Invalid transaction type: " + tokens[0]);
        }
    }


    //============================================================
    // Helper Methods
    public String getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public String getDateStr() {
        return new SimpleDateFormat("MMM. d, yyyy (E)").format(date);
    }

    public String getTimeStr() {
        return new SimpleDateFormat("HH:mm:ss").format(date);
    }

    public String getTransactionID() {
        return transactionID;
    }

    public double getAmount() {
        return amount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getRecordHdr() {
        return type + " " + DateTimeUtil.dateTime2Str(date) + " " + transactionID + " " + amount;
    }


    //============================================================
    // Helper Method -- parseDateTimeStr
    private static Date parseDateTimeStr(String dateStr) throws OCTransactionFormatException {
        try {
            return DateTimeUtil.str2DateTime(dateStr);
        } catch (ParseException e) {
            throw new OCTransactionFormatException("parseDateTimeStr: Corrupted dateStr: " + dateStr);
        }
    }


    //============================================================
    // Helper Method -- parseAmountStr
    private static double parseAmountStr(String amountStr) throws OCTransactionFormatException {
        try {
            return Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            throw new OCTransactionFormatException("parseAmountStr: Corrupted amount: " + amountStr);
        }
    }


    //============================================================
    // Helper Method -- matchDate
    protected boolean matchDate(String matchDateStr) throws ParseException {
        String dateFormat = "yyyy-MM-dd";

        // validate dateStr
        if (matchDateStr.length() != 10) {
            throw new ParseException("Invalid date format", 0);
        }
        new SimpleDateFormat(dateFormat).parse(matchDateStr);
        return new SimpleDateFormat(dateFormat).format(date).equals(matchDateStr);
    }


    //============================================================
    // toString
    @Override
    public String toString() {
        String str = "";
        str += "    TransactionID: " + transactionID + "\n";
        str += "    Date/Time: " + getDateStr() + " at " + getTimeStr() + "\n";
        str += "    Amount: " + amount + "\n";
        str += "    Status: " + status + "\n";
        return str;
    }

    public String toRecord() {
        return "";
    }

    public boolean match(String[] criteria) throws OCTransactionSearchException, ParseException {
        return false;
    }


    //============================================================
    // OCTransactionSearchException
    public static class OCTransactionSearchException extends Exception {
        public OCTransactionSearchException(String ocTransactionSearchExMsg) {
            super(ocTransactionSearchExMsg);
        }
    }


    //============================================================
    // OCTransactionFormatException
    public static class OCTransactionFormatException extends Exception {
        public OCTransactionFormatException(String ocTransactionFormatExMsg) {
            super(ocTransactionFormatExMsg);
        }
    }
}
