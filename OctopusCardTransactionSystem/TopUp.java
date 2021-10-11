package OctopusCard.OctopusCard;

import OctopusCard.OctopusCard.Util.DateTimeUtil;
import OctopusCard.OctopusCard.Util.Tokenizer;

import java.text.ParseException;
import java.util.Date;

public class TopUp extends OCTransaction {

    protected static final String TypeHdrStr = "TopUP";

    private TopUpType topUpType;
    private String agent;

    public TopUp(String type, String dateTimeStr, String transactionID, String amountStr, String topUpType, String agent) throws OCTransactionFormatException {
        super(type, dateTimeStr, transactionID, amountStr);
        if (topUpType.isEmpty() || agent.isEmpty()) {
            throw new OCTransactionFormatException("Some fields are empty");
        }
        try {
            this.topUpType = TopUpType.valueOf(topUpType);
        } catch (IllegalArgumentException e) {
            throw new OCTransactionFormatException("Wrong TopUpType: " + topUpType);
        }
        this.agent = agent;
        setStatus(Status.COMPLETED);
    }

    public TopUp(String type, Date date, String transactionID, double amount, String topUpType, String agent) throws OCTransactionFormatException {
        super(type, date, transactionID, amount);
        if (topUpType.isEmpty() || agent.isEmpty()) {
            throw new OCTransactionFormatException("Some fields are empty");
        }
        try {
            this.topUpType = TopUpType.valueOf(topUpType);
        } catch (IllegalArgumentException e) {
            throw new OCTransactionFormatException("Wrong TopUpType: " + topUpType);
        }
        this.agent = agent;
        setStatus(Status.COMPLETED);
    }

    public static OCTransaction parseTransaction(String record) throws OCTransactionFormatException {
        String[] tokens = Tokenizer.getTokens(record);

        if(tokens.length < 6)
            throw new OCTransactionFormatException("Record has not enough tokens"); // tokens must be at least 6 // TopUp dateTime transactionID amount topUpType agent

        if(!tokens[0].equalsIgnoreCase(TypeHdrStr))
            throw new OCTransactionFormatException(TypeHdrStr + " != " + tokens[0]); // wrong type

        String dateTime = tokens[1];
        String transactionId = tokens[2];
        String amount = tokens[3];
        String topUpType = tokens[4];
        String agent = "";

        int i = 5;

        while(i<tokens.length) {
            agent = agent + tokens[i] + " ";
            i++;
        }

        return new TopUp(TypeHdrStr, dateTime, transactionId, amount, topUpType, agent);
    }

    @Override
    public String toRecord() {
        return TypeHdrStr + " " + DateTimeUtil.dateTime2Str(getDate()) + " " + getTransactionID() + " " + getAmount()+ " " + topUpType.name() + " " + agent;
    }

    @Override
    public boolean match(String[] criteria) throws OCTransactionSearchException, ParseException {
        if(criteria.length < 1) {
            throw new OCTransactionSearchException("Not enough Criteria");
        }

        String c1 = criteria[0];
        if (c1.equalsIgnoreCase("Cash")) {
                if (getTopUpType() == TopUpType.Cash) {
                    return true;
            }
        } else if(c1.equalsIgnoreCase("Bank")) {
            if (criteria.length == 2) {
                if (getTopUpType() == TopUpType.Bank)
                    return true;
            }
            String bank = criteria[1];
            if (getAgent().toLowerCase().contains(bank.toLowerCase())) {
                return true;
            }

        } else if (c1.equalsIgnoreCase("date")) {
            String date = criteria[1];
            return matchDate(date);
        }

        return false;
    }

    public TopUpType getTopUpType() {
        return topUpType;
    }

    public void setTopUpType(TopUpType topUpType) {
        this.topUpType = topUpType;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
}
