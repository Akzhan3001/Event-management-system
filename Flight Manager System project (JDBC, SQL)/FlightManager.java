import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.*;

import java.util.Properties;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * This is a flight manager to support: (1) add a flight (2) delete a flight (by
 * flight_no) (3) print flight information (by flight_no) (4) search a flight
 * (by source, dest, stop_no, duration) (5) book a flight (by passport no,
 * flight_no (multiple flight numbers available)) (6) show bookings (by passport
 * no) (7) cancel booking (by booking_id)
 * 
 * @author group17
 */

public class FlightManager {

	Scanner in = null;
	Connection conn = null;
	// Database Host
	final String databaseHost = "orasrv1.comp.hkbu.edu.hk";
	// Database Port
	final int databasePort = 1521;
	// Database name
	final String database = "pdborcl.orasrv1.comp.hkbu.edu.hk";
	final String proxyHost = "faith.comp.hkbu.edu.hk";
	final int proxyPort = 22;
	final String forwardHost = "localhost";
	int forwardPort;
	Session proxySession = null;
	boolean noException = true;

	// JDBC connecting host
	String jdbcHost;
	// JDBC connecting port
	int jdbcPort;

	String[] options = { "add a flight", "delete a flight (by flight_no)", "print flight information (by flight_no)",
			"search a flight (by source, dest, stop_no, duration)",
			"book a flight (by passportno, flight_no (multiple flight numbers available))",
			"show bookings (by passport no)", "cancel booking (by booking_id)", "exit" };

	/**
	 * Get YES or NO. Do not change this function.
	 * 
	 * @return boolean
	 */
	boolean getYESorNO(String message) {
		JPanel panel = new JPanel();
		panel.add(new JLabel(message));
		JOptionPane pane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION);
		JDialog dialog = pane.createDialog(null, "Question");
		dialog.setVisible(true);
		boolean result = JOptionPane.YES_OPTION == (int) pane.getValue();
		dialog.dispose();
		return result;
	}

	/**
	 * Get username & password. Do not change this function.
	 * 
	 * @return username & password
	 */
	String[] getUsernamePassword(String title) {
		JPanel panel = new JPanel();
		final TextField usernameField = new TextField();
		final JPasswordField passwordField = new JPasswordField();
		panel.setLayout(new GridLayout(2, 2));
		panel.add(new JLabel("Username"));
		panel.add(usernameField);
		panel.add(new JLabel("Password"));
		panel.add(passwordField);
		JOptionPane pane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION) {
			private static final long serialVersionUID = 1L;

			@Override
			public void selectInitialValue() {
				usernameField.requestFocusInWindow();
			}
		};
		JDialog dialog = pane.createDialog(null, title);
		dialog.setVisible(true);
		dialog.dispose();
		return new String[] { usernameField.getText(), new String(passwordField.getPassword()) };
	}

	/**
	 * Login the proxy.
	 * @return boolean
	 */
	public boolean loginProxy() {
		if (getYESorNO("Using ssh tunnel or not?")) { // if using ssh tunnel
			String[] namePwd = getUsernamePassword("Login cs lab computer");
			String sshUser = namePwd[0];
			String sshPwd = namePwd[1];
			try {
				proxySession = new JSch().getSession(sshUser, proxyHost, proxyPort);
				proxySession.setPassword(sshPwd);
				Properties config = new Properties();
				config.put("StrictHostKeyChecking", "no");
				proxySession.setConfig(config);
				proxySession.connect();
				proxySession.setPortForwardingL(forwardHost, 0, databaseHost, databasePort);
				forwardPort = Integer.parseInt(proxySession.getPortForwardingL()[0].split(":")[0]);
			} catch (JSchException e) {
				e.printStackTrace();
				return false;
			}
			jdbcHost = forwardHost;
			jdbcPort = forwardPort;
		} else {
			jdbcHost = databaseHost;
			jdbcPort = databasePort;
		}
		return true;
	}

	/**
	 * Login the oracle system. 
	 * @return boolean
	 */
	public boolean loginDB() {
		String username = "e9201761"; 
		String password = "e9201761"; 

		/* Do not change the code below */
		if (username.equalsIgnoreCase("e9201761") || password.equalsIgnoreCase("e9201761")) {
			String[] namePwd = getUsernamePassword("Login sqlplus");
			username = namePwd[0];
			password = namePwd[1];
		}
		String URL = "jdbc:oracle:thin:@" + jdbcHost + ":" + jdbcPort + "/" + database;

		try {
			System.out.println("Logging " + URL + " ...");
			conn = DriverManager.getConnection(URL, username, password);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Show the options.
	 */
	public void showOptions() {
		System.out.println("Please choose following option:");
		for (int i = 0; i < options.length; ++i) {
			System.out.println("(" + (i + 1) + ") " + options[i]);
		}
	}

	/**
	 * Run the manager
	 */
	public void run() {
		while (noException) {
			showOptions();
			String line = in.nextLine();
			if (line.equalsIgnoreCase("exit"))
				return;
			int choice = -1;
			try {
				choice = Integer.parseInt(line);
			} catch (Exception e) {
				System.out.println("This option is not available");
				continue;
			}
			if (!(choice >= 1 && choice <= options.length)) {
				System.out.println("This option is not available");
				continue;
			}
			if (options[choice - 1].equals("add a flight")) {
				addFlight();
			} else if (options[choice - 1].equals("delete a flight (by flight_no)")) {
				deleteFlight();
			} else if (options[choice - 1].equals("print flight information (by flight_no)")) {
				printFlightByNo();
			} else if (options[choice - 1].equals("search a flight (by source, dest, stop_no, duration)")) {
				searchFlights();
			} else if (options[choice - 1]
					.equals("book a flight (by passportno, flight_no (multiple flight numbers available))")) {
				addBooking();
			} else if (options[choice - 1].equals("show bookings (by passport no)")) {
				showCustomerBookings();
			} else if (options[choice - 1].equals("cancel booking (by booking_id)")) {
				cancelBooking();
			} else if (options[choice - 1].equals("exit")) {
				break;
			}
		}
	}

	/**
	 * Print out the infomation of a flight given a flight_no
	 * 
	 * @param flight_no
	 */
	private void printFlightInfo(String flight_no) {
		try {
			Statement stm = conn.createStatement();
			String sql = "SELECT * FROM FLIGHTS WHERE Flight_no = '" + flight_no + "'";
			ResultSet rs = stm.executeQuery(sql);
			if (!rs.next())
				return;
			String[] heads = { "Flight_no", "Depart_Time", "Arrive_Time", "Duration", "Fare", "Seat_Limit", "Source",
					"Dest" };
			for (int i = 0; i < 8; ++i) { // flight table 6 attributes
				try {
					System.out.println(heads[i] + " : " + rs.getString(i + 1));

				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
			noException = false;
		}
	}

	/**
	 * List all flights in the database.
	 */
	private void listAllFlights() {
		System.out.println("All flights in the database now:");
		try {
			Statement stm = conn.createStatement();
			String sql = "SELECT Flight_no FROM FLIGHTS";
			ResultSet rs = stm.executeQuery(sql);

			int resultCount = 0;
			while (rs.next()) {
				System.out.println(rs.getString(1));
				++resultCount;
			}
			System.out.println("Total " + resultCount + " flight(s).");
			rs.close();
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
			noException = false;
		}
	}

	/**
	 * Select out a flight according to the flight_no.
	 */
	private void printFlightByNo() {
		listAllFlights();
		System.out.println("Please input the flight_no to print info:");
		String line = in.nextLine();
		line = line.trim();
		if (line.equalsIgnoreCase("exit"))
			return;
		printFlightInfo(line);
	}
	/**
	 * Search flights
	 */
	private void searchFlights() {
		System.out.println("Please input source, dest, number of stops, maximum duration:");
		String line = in.nextLine();

		if (line.equalsIgnoreCase("exit"))
			return;

		String[] values = line.split(",");
		for (int i = 0; i < values.length; ++i)
			values[i] = values[i].trim();

		int stops = Integer.parseInt(values[2]);
		if (stops == 0) {
			selectFlightsInZeroStop(values);
		} else if (stops == 1) {
			selectFlightsInOneStop(values);
		} else if (stops == 2) {
			selectFlightsInTwoStops(values);
		}

	}

	/**
	 * Get customer id by passport number
	 */

	private String getCustomerID(String passport) {
		String result = "";

		try {

			Statement stm = conn.createStatement();

			String sql = "SELECT CID FROM CUSTOMERS " + "where " + "PASSPORT_NO='" + passport + "'";

			ResultSet rs = stm.executeQuery(sql);

			while (rs.next()) {
				result = rs.getString(1);
			}
			rs.close();
			stm.close();

		} catch (SQLException e) {
			e.printStackTrace();
			noException = false;
		}

		return result;
	}

	/**
	 * Given source and dest, select all the flights can arrive the dest directly.
	 * For example, given HK, Tokyo, you may find HK -> Tokyo.
	 */
	private void selectFlightsInZeroStop(String[] values) {
		try {
			Statement stm = conn.createStatement();

			String source = values[0];
			String dest = values[1];
			String duration = values[3];

			String sql = "SELECT * FROM FLIGHTS " + "WHERE " + "Source='" + source + "' " + "AND Dest='" + dest + "'"
					+ "AND Duration < " + duration;

			ResultSet rs = stm.executeQuery(sql);

			int resultCount = 0;

			while (rs.next()) {
				resultCount++;
				printFlightInfo(rs.getString(1));
				System.out.println("=================================================");

			}
			System.out.println("Total " + resultCount + " choice(s).");
			rs.close();
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
			noException = false;
		}
	}

	/**
	 * Given source and dest, select all the flights can arrive the dest in one
	 * stop. For example, given HK, Tokyo, you may find HK -> Beijing, Beijing ->
	 * Tokyo
	 */
	private void selectFlightsInOneStop(String[] values) {

		try {

			Statement stm = conn.createStatement();
			String source = values[0];
			String dest = values[1];
			String duration = values[3];

			String sql = "SELECT F1.FLIGHT_NO, F2.FLIGHT_NO " + "FROM FLIGHTS F1, FLIGHTS F2  " + "where "
					+ "(F1.Source='" + source + "'" + " and F2.Dest='" + dest + "'" + " and F1.Dest=F2.Source"
					+ " and F1.arrive_time < F2.depart_time)" + " and (f1.duration + f2.duration) < " + duration;

			ResultSet rs = stm.executeQuery(sql);
			int resultCount = 0;

			while (rs.next()) {
				resultCount++;
				System.out.println(rs.getString(1) + " -> " + rs.getString(2));
				System.out.println("-------------------------------------------------");

			}
			System.out.println("Total " + resultCount + " choice(s).");
			rs.close();
			stm.close();

		} catch (SQLException e) {
			e.printStackTrace();
			noException = false;
		}
	}

	/**
	 * Given source and dest, select all the flights can arrive the dest in two
	 * stops. For example, given HK, Tokyo, you may find HK -> Beijing, Beijing ->
	 * Tokyo, Tokyo -> New York.
	 */
	private void selectFlightsInTwoStops(String[] values) {
		try {

			Statement stm = conn.createStatement();
			String source = values[0];
			String dest = values[1];
			String duration = values[3];
			String sql = "select f1.flight_no, f2.flight_no, f3.flight_no " + "from "
					+ "flights f1,flights f2, flights f3 " + "where " + "f1.source='" + source + "'"
					+ " and f1.dest = f2.source" + " and f1.dest<>'" + dest + "'" + " and f2.dest = f3.source"
					+ " and f2.dest<>'" + dest + "'" + " and f3.dest='" + dest + "'"
					+ " and f2.depart_time > f1.arrive_time" + " and f3.depart_time > f2.arrive_time"
					+ " and (f1.duration + f2.duration + f3.duration) < " + duration;

			ResultSet rs = stm.executeQuery(sql);
			int resultCount = 0;
			while (rs.next()) {
				resultCount++;
				System.out.println(rs.getString(1) + " -> " + rs.getString(2) + " -> " + rs.getString(3));
				System.out.println("-------------------------------------------------");

			}
			System.out.println("Total " + resultCount + " choice(s).");
			rs.close();
			stm.close();

		} catch (SQLException e) {
			e.printStackTrace();
			noException = false;
		}
	}

	/**
	 * Insert data into database
	 * @return
	 */
	private void addFlight() {
		/**
		 * A sample input is: CX109, 2015/03/15/13:00:00, 2015/03/15/19:00:00, 2000, 10,
		 * Beijing, Tokyo
		 */
		System.out.println("Please input the flight_no, depart_time, arrive_time, fare, source, dest:");
		String line = in.nextLine();
		if (line.equalsIgnoreCase("exit"))
			return;
		String[] values = line.split(",");
		if (values.length < 6) {
			System.out.println("The value number is expected to be 6");
			return;
		}
		for (int i = 0; i < values.length; ++i)
			values[i] = values[i].trim();

		try {
			Statement stm = conn.createStatement();

			String sql = "INSERT INTO FLIGHTS VALUES(" + "'" + values[0] + "', " + // FLIGHT NUMBER
					"to_date('" + values[1] + "', 'yyyy/mm/dd/hh24:mi:ss'), " + // DEPARTURE TIME
					"to_date('" + values[2] + "', 'yyyy/mm/dd/hh24:mi:ss'), " + // ARRIVAL TIME
					"(to_date('" + values[2] + "', 'yyyy/mm/dd/hh24:mi:ss') - to_date('" + values[1]
					+ "', 'yyyy/mm/dd/hh24:mi:ss')) * 24, " + // DURATION IN HOURS
					values[3] + ", " + // FARE
					"'" + values[4] + "', " + // SEAT LIMIT
					"'" + values[5] + "', " + // SOURCE
					"'" + values[6] + "'" + // DEST
					")";

			stm.executeUpdate(sql);
			stm.close();
			System.out.println("succeed to add flight ");
			printFlightInfo(values[0]);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("fail to add a flight " + line);
			noException = false;
		}
	}

	private void addBooking() {
		/**
		 * A sample input is: N09637323, CX102, CX103, CX101
		 */
		System.out.println("Please input the passport number, flight numbers:");
		String line = in.nextLine();
		if (line.equalsIgnoreCase("exit"))
			return;
		String[] values = line.split(",");
		if (values.length < 2) {
			System.out.println("The value number is expected to be at least 2");
			return;
		}
		for (int i = 0; i < values.length; ++i)
			values[i] = values[i].trim();
		String cid = getCustomerID(values[0]);
		if (cid == "") {
			System.out.println("The customer does not exist");
			return;
		}
		try {
			Statement stm = conn.createStatement();
			int fare = 0;
			int flights = 0;
			for (int i = 1; i < values.length; i++) {
				flights++;
				String sql = "SELECT FARE FROM FLIGHTS WHERE FLIGHT_NO='" + values[i] + "'";
				ResultSet rs = stm.executeQuery(sql);
				while (rs.next()) {
					fare = fare + rs.getInt(1);
				}
				rs.close();
			}
			double bookingFare = 0;
			if (flights == 1)
				bookingFare = fare;
			if (flights == 2)
				bookingFare = fare * 0.9;
			if (flights == 3)
				bookingFare = fare * 0.75;
			String uuid = UUID.randomUUID().toString();
			String insertBookingSql = "INSERT INTO BOOKINGS VALUES (" + "'" + uuid + "', " + bookingFare + ", " + "'"
					+ cid + "')";
			stm.executeUpdate(insertBookingSql);
			for (int i = 1; i < values.length; i++) {
				String insertTicketSql = "INSERT INTO TICKETS VALUES (" + "'" + values[i] + "', " + "'" + uuid + "')";
				stm.executeUpdate(insertTicketSql);
			}
			stm.close();
			System.out.println("succeed to make a booking");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("fail to add a booking " + line);
			noException = false;
		}
	}

	/**
	 * show bookings by customer's passport number
	 */

	private void showCustomerBookings() {
		listAllFlights();
		System.out.println("Please input the customer's passport number:");
		String line = in.nextLine();
		line = line.trim();
		if (line.equalsIgnoreCase("exit"))
			return;

		String cid = getCustomerID(line);

		try {
			/**
			 * Create the statement and sql
			 */
			Statement stm = conn.createStatement();

			String sql = "SELECT * FROM BOOKINGS WHERE CID='" + cid + "'";

			ResultSet rs = stm.executeQuery(sql);

			int resultCount = 0;

			while (rs.next()) {
				resultCount++;
				System.out.println("Booking ID: " + rs.getString(1));
				System.out.println("Booking total fare: " + rs.getInt(2));
				System.out.println("=================================================");

			}
			System.out.println("Total " + resultCount + " choice(s).");
			rs.close();
			stm.close();
		} catch (SQLException e) {
			e.printStackTrace();
			noException = false;
		}

	}

	/**
	 * Function to delete a flight.
	 */
	public void deleteFlight() {
		listAllFlights();
		System.out.println("Please input the flight_no to delete:");
		String line = in.nextLine();

		if (line.equalsIgnoreCase("exit"))
			return;
		line = line.trim();

		try {
			Statement stm = conn.createStatement();

			String sql = "DELETE FROM FLIGHTS WHERE Flight_No='" + line + "'";

			stm.executeUpdate(sql);

			stm.close();

			System.out.println("succeed to delete flight " + line);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("fail to delete flight " + line);
			noException = false;
		}
	}

	public void cancelBooking() {
		System.out.println("Please input the booking_id to delete:");
		String line = in.nextLine();

		if (line.equalsIgnoreCase("exit"))
			return;
		line = line.trim();

		try {
			Statement stm = conn.createStatement();

			String sql = "DELETE FROM BOOKINGS WHERE BOOKING_ID='" + line + "'";

			stm.executeUpdate(sql);

			stm.close();

			System.out.println("succeed to delete booking " + line);
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("fail to delete booking " + line);
			noException = false;
		}
	}

	/**
	 * Close the manager. Do not change this function.
	 */
	public void close() {
		System.out.println("Thanks for using this manager! Bye...");
		try {
			if (conn != null)
				conn.close();
			if (proxySession != null) {
				proxySession.disconnect();
			}
			in.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Constructor of flight manager Do not change this function.
	 */
	public FlightManager() {
		System.out.println("Welcome to use this manager!");
		in = new Scanner(System.in);
	}

	/**
	 * Main function
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		FlightManager manager = new FlightManager();
		if (!manager.loginProxy()) {
			System.out.println("Login proxy failed, please re-examine your username and password!");
			return;
		}
		if (!manager.loginDB()) {
			System.out.println("Login database failed, please re-examine your username and password!");
			return;
		}
		System.out.println("Login succeed!");
		try {
			manager.run();
		} finally {
			manager.close();
		}
	}
}
