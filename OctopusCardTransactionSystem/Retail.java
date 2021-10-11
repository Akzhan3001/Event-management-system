package OctopusCard.OctopusCard;

import OctopusCard.OctopusCard.Util.DateTimeUtil;
import OctopusCard.OctopusCard.Util.Tokenizer;

import java.text.ParseException;
import java.util.Date;

public class Retail extends OCTransaction{

    protected static final String TypeHdrStr = "Retail";

    private String retailer;
    private String description;

    public Retail(String type, String dateTimeStr, String transactionID, String amountStr, String retailer, String description) throws OCTransactionFormatException {
        super(type, dateTimeStr, transactionID, amountStr);
        if(retailer.isEmpty() || description.isEmpty()) {
            throw new OCTransactionFormatException("Some fields are not entered");
        }
        this.retailer = retailer;
        this.description = description;
        setStatus(Status.COMPLETED);
    }

    public Retail(String type, Date date, String transactionID, double amount, String retailer, String description) throws OCTransactionFormatException {
        super(type, date, transactionID, amount);
        if(retailer.isEmpty() || description.isEmpty()) {
            throw new OCTransactionFormatException("Some fields are not entered");
        }
        this.retailer = retailer;
        this.description = description;
        setStatus(Status.COMPLETED);
    }

    public static OCTransaction parseTransaction(String record) throws OCTransactionFormatException {
        String[] tokens = Tokenizer.getTokens(record);

        if(tokens.length < 6)
            throw new OCTransactionFormatException("Record has not enough tokens"); // tokens must be at least 6 // Retail dateTime transactionID amount retailer, description

        if(!tokens[0].equalsIgnoreCase(TypeHdrStr))
            throw new OCTransactionFormatException(TypeHdrStr + " != " + tokens[0]); // wrong type

        String dateTime = tokens[1];
        String transactionId = tokens[2];
        String amount = tokens[3];
        String retailer = "";
        String description = "";

        int i = 4;

        while(!tokens[i].endsWith(",")) {
            retailer = retailer + tokens[i] + " ";
            i++;
            if(i >= tokens.length)
                throw new OCTransactionFormatException("Record hasn't Description");
        }
        retailer = retailer + tokens[i];
        i++;
        retailer = retailer.replace(",", ""); //deleting comma from retailer

        String desctiption = "";
        while(i<tokens.length) {
            desctiption = desctiption + tokens[i] + " ";
            i++;
        }

        return new Retail(TypeHdrStr, dateTime, transactionId, amount, retailer, desctiption);
    }

    @Override
    public String toRecord() {
        return TypeHdrStr + " " + DateTimeUtil.dateTime2Str(getDate()) + " " + getTransactionID() + " " + getAmount() + " " +  retailer + ", " + description;
    }

    @Override
    public boolean match(String[] criteria) throws OCTransactionSearchException, ParseException {
        if(criteria.length < 2) {
            throw new OCTransactionSearchException("Not enough Criteria");
        }

        String c1 = criteria[0];
        if (c1.equalsIgnoreCase("retailer")) {
            String retailer = "";
            for (int i = 1; i < criteria.length; i++) {
                retailer = retailer + criteria[i];
            }
            if (this.retailer.toLowerCase().contains(retailer.toLowerCase())) {
                return true;
            }
        } else if(c1.equalsIgnoreCase("description")) {
            String description = "";
            for (int i = 1; i < criteria.length; i++) {
                description = description + criteria[i];
            }
            if (this.description.toLowerCase().contains(description.toLowerCase())) {
                return true;
            }
        } else if (c1.equalsIgnoreCase("date")) {
            String date = criteria[1];
            return matchDate(date);
        }

        return false;
    }

    public String getRetailer() {
        return retailer;
    }

    public void setRetailer(String retailer) {
        this.retailer = retailer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
