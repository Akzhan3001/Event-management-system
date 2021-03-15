package OctopusCard.OctopusCard;

import OctopusCard.OctopusCard.Util.DateTimeUtil;
import OctopusCard.OctopusCard.Util.Tokenizer;

import java.text.ParseException;
import java.util.Date;

public class MTR extends OCTransaction{

    protected static final String TypeHdrStr = "MTR"; //protected field is seen from parent class (e.g. OCTransaction)

    private MtrType mtrType;
    private String station;

    public MTR(String type, String dateTimeStr, String transactionID, String amountStr, String mtrType, String station) throws OCTransactionFormatException {
        super(type, dateTimeStr, transactionID, amountStr);
        try {
            this.mtrType = MtrType.valueOf(mtrType);
        } catch (IllegalArgumentException e) {
            throw new OCTransactionFormatException("Wrong MtrType: " + mtrType);
        }
        this.station = station;
        setStatus(Status.MTR_OUTSTANDING);
    }

    public MTR(String type, Date date, String transactionID, double amount, String mtrType, String station) throws OCTransactionFormatException {
        super(type, date, transactionID, amount);
        try {
            this.mtrType = MtrType.valueOf(mtrType);
        } catch (IllegalArgumentException e) {
            throw new OCTransactionFormatException("Wrong MtrType: " + mtrType);
        }
        this.station = station;
        setStatus(Status.MTR_OUTSTANDING);
    }

    public static OCTransaction parseTransaction(String record) throws OCTransactionFormatException {
        String[] tokens = Tokenizer.getTokens(record);

        if(tokens.length < 6)
            throw new OCTransactionFormatException("Record has not enough tokens"); // tokens must be at least 6 // MTR dateTime transactionID amount mtrType station

        if(!tokens[0].equalsIgnoreCase(TypeHdrStr))
            throw new OCTransactionFormatException("Type is not " + TypeHdrStr); // wrong type

        String dateTime = tokens[1];
        String transactionId = tokens[2];
        String amount = tokens[3];
        String mtrType = tokens[4];
        String station = "";

        for (int i = 5; i < tokens.length; i++) {
            station = station + tokens[i] + " ";
        }
        return new MTR(TypeHdrStr, dateTime, transactionId, amount, mtrType, station);
    }

    @Override
    public String toRecord() {
        return TypeHdrStr + " " + DateTimeUtil.dateTime2Str(getDate()) + " " + getTransactionID() + " " + getAmount() + " " + mtrType.name() + " " + station;
    }

    @Override
    public boolean match(String[] criteria) throws OCTransactionSearchException, ParseException {
        if(criteria.length < 2) {
            throw new OCTransactionSearchException("Not enough Criteria");
        }

        String c1 = criteria[0];
        if (c1.equalsIgnoreCase("station")) {
            String station = "";
            for (int i = 1; i < criteria.length; i++) {
                station = station + criteria[i];
            }
            if (this.station.toLowerCase().contains(station.toLowerCase())) {
                return true;
            }
        } else if(c1.equalsIgnoreCase("mtrType")) {
            String mtrType = criteria[1];
            if (this.mtrType.name().equalsIgnoreCase(mtrType)) {
                return true;
            }
        } else if(c1.equalsIgnoreCase("status")) {
            String status = criteria[1];
            if (getStatus().name().equalsIgnoreCase("MTR_"+status))
                return true;
        } else if (c1.equalsIgnoreCase("date")) {
            String date = criteria[1];
            return matchDate(date);
        }

        return false;
    }

    public MtrType getMtrType() {
        return mtrType;
    }

    public void setMtrType(MtrType mtrType) {
        this.mtrType = mtrType;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }
}
