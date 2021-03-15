package OctopusCard.OctopusCard;

import OctopusCard.OctopusCard.Util.DateTimeUtil;
import OctopusCard.OctopusCard.Util.Tokenizer;

import java.text.ParseException;
import java.util.Date;

public class BusFare extends OCTransaction {

    protected static final String TypeHdrStr = "BusFare";

    private String route;
    private String station;
    private String terminal;

    public BusFare(String type, String dateTimeStr, String transactionID, String amountStr, String route, String station, String terminal) throws OCTransactionFormatException {
        super(type, dateTimeStr, transactionID, amountStr);
        if (route.isEmpty() || station.isEmpty() || terminal.isEmpty()) {
            throw new OCTransactionFormatException("Some fields are not entered");
        }
        this.route = route;
        this.station = station;
        this.terminal = terminal;
        setStatus(Status.COMPLETED);
    }

    public BusFare(String type, Date date, String transactionID, double amount, String route, String station, String terminal) throws OCTransactionFormatException {
        super(type, date, transactionID, amount);
        if (route.isEmpty() || station.isEmpty() || terminal.isEmpty()) {
            throw new OCTransactionFormatException("Some fields are not entered");
        }
        this.route = route;
        this.station = station;
        this.terminal = terminal;
        setStatus(Status.COMPLETED);
    }

    public static OCTransaction parseTransaction(String record) throws OCTransactionFormatException {
        String[] tokens = Tokenizer.getTokens(record);

        if(tokens.length < 8)
            throw new OCTransactionFormatException("Record has not enough tokens"); // tokens must be at least 8 // BusFare dateTime transactionID amount route station to terminal

        if(!tokens[0].equalsIgnoreCase(TypeHdrStr))
            throw new OCTransactionFormatException(TypeHdrStr + " != " + tokens[0]); // wrong type

        String dateTime = tokens[1];
        String transactionId = tokens[2];
        String amount = tokens[3];
        String route = tokens[4];
        String station = "";

        int i = 5;

        while(!tokens[i].equalsIgnoreCase("to")) {
            station = station + tokens[i] + " ";
            i++;
            if(i >= tokens.length)
                throw new OCTransactionFormatException("Record hasn't Terminal");
        }

        i++; // for get token after 'to'

        String terminal = "";
        while(i<tokens.length) {
            terminal = terminal + tokens[i] + " ";
            i++;
        }

        return new BusFare(TypeHdrStr, dateTime, transactionId, amount, route, station, terminal);
    }

    @Override
    public String toRecord() {
        return TypeHdrStr + " " + DateTimeUtil.dateTime2Str(getDate()) + " " + getTransactionID() + " " + getAmount() + " " + route + " " + station + " to " + terminal;
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
        } else if(c1.equalsIgnoreCase("route")) {
            String route = criteria[1];
            if (this.route.equalsIgnoreCase(route)) {
                return true;
            }
        } else if(c1.equalsIgnoreCase("terminal")) {
            String terminal = criteria[1];
            if (this.terminal.equalsIgnoreCase(terminal))
                return true;
        } else if (c1.equalsIgnoreCase("date")) {
            String date = criteria[1];
            return matchDate(date);
        }

        return false;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getTerminal() {
        return terminal;
    }

    public void setTerminal(String terminal) {
        this.terminal = terminal;
    }
}
