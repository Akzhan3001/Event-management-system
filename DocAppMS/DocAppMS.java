import java.util.Scanner;


public class DocAppMS {
    UserDB userDb;
    AppoinmentDB appDb;

    public static void main(String [] args) throws Exception{
        new DocAppMS().runApp(args);
    }
    public DocAppMS(){
        userDb= new UserDB();
        appDb = new AppoinmentDB();
    }
    void runApp(String [] args) throws  Exception{
        String userFile = args[0];
        String appointmentFile = args[1];
        userDb.login(userFile);
        UserDB userDb = new UserDB();
        AppoinmentDB appDb = new AppoinmentDB();
        userDb.login(args[0]);
        appDb.login(appointmentFile);
        String doctorId;
        String patientId;
        String timeslot1;
        String userId;
        String password;
        String userType;
        String fullName;
        for(boolean quit= false; !quit;){
            System.out.println("*** System Startup initialized! ***\n");
            Scanner in = new Scanner(System.in);
            welcomeMenu();
            ready();
            String line = in.nextLine();
            String[] tokensArray = getTokens(line);
            switch (tokensArray[0].toLowerCase()) {
                case "quit":
                    System.exit(0);
                case "login":
                    switch (userDb.searchUserType(tokensArray[1])){
                        case "a":
                            Scanner pas = new Scanner(System.in);
                            System.out.print("Password: ");
                            String passw = pas.nextLine();
                            switch (userDb.passwordCheck(tokensArray[1],passw)){
                                case "Yes":
                                    System.out.println("Login is successful."+"   Welcome to Doctor Appointment Management System!  "+tokensArray[1]);
                                    for(boolean loopingEnd = false;!loopingEnd;){
                                        ready();
                                        Scanner in2 = new Scanner(System.in);
                                        String line2 = in2.nextLine();
                                        String[] tokensArray2 = getTokens(line2);
                                        String exist="";
                                        String exist2Element="";
                                        String exist3Element="";
                                        String exist4Element="";
                                        if(tokensArray2.length>1){
                                            exist="something";
                                        }
                                        if(tokensArray2.length>2){
                                            exist2Element="something";
                                        }
                                        if(tokensArray2.length>3){
                                            exist3Element="something";
                                        }
                                        if(tokensArray2.length>4){
                                            exist4Element="something";
                                        }
                                        switch (tokensArray2[0].toLowerCase()){
                                            case "save":
                                                switch (exist) {
                                                    case "something":
                                                        switch (tokensArray2[1].toLowerCase()) {
                                                            case "user":
                                                                userDb.saveDB(tokensArray2[2]);
                                                                break;
                                                            case "appointment":
                                                                appDb.saveDB(tokensArray2[2]);
                                                                break;
                                                            default:
                                                                System.out.println("Error command for save. Try again!");
                                                                break;
                                                        }
                                                        break;
                                                    default:
                                                        System.out.println("Incomplete arguments for command save.");
                                                        break;
                                                }
                                                break;
                                            case "help":
                                                switch (exist) {
                                                    default:
                                                        helpAdmin();
                                                        break;
                                                    case "something":
                                                        helpCommandAdmin(tokensArray2[1]);
                                                        break;
                                                }
                                                break;
                                            case "logout":
                                                loopingEnd=true;
                                                break;
                                            case "load":
                                                switch (exist) {
                                                    case "something":
                                                        switch (tokensArray2[1].toLowerCase()) {
                                                            case "user":
                                                                userDb.loadDB(tokensArray2[2]);
                                                                break;
                                                            case "appointment":
                                                                appDb.loadDB(tokensArray2[2]);
                                                                break;
                                                            default:
                                                                System.out.println("Error command. Try again.");
                                                                break;
                                                    }
                                                        break;
                                                    default:
                                                        System.out.println("Incomplete arguments for command load.");
                                                        break;
                                                }
                                                break;
                                            case "add":
                                                switch (exist) {
                                                    case "something":
                                                        switch (tokensArray2[1].toLowerCase()) {
                                                            case "appointment":
                                                                switch(exist2Element){
                                                                    case "something":
                                                                        doctorId = tokensArray2[2];
                                                                        switch (exist3Element){
                                                                            case "something":
                                                                                patientId = tokensArray2[3];
                                                                                switch (exist4Element) {
                                                                                    case "something":
                                                                                        timeslot1 = tokensArray2[4];
                                                                                        appDb.addAppAsAdmin(doctorId, patientId, timeslot1);
                                                                                        break;
                                                                                }
                                                                        }
                                                                }
                                                                break;

                                                            default:
                                                                userId = tokensArray2[1];
                                                                switch (exist2Element){
                                                                    case "something":
                                                                        password = tokensArray2[2];
                                                                        switch (exist3Element){
                                                                            case "something":
                                                                                userType = tokensArray2[3];
                                                                                switch (exist4Element){
                                                                                case "something":
                                                                                    fullName="";
                                                                                    for(int j=4;j< tokensArray2.length;j++){
                                                                                        fullName += tokensArray2[j]+" ";
                                                                                    }

                                                                                    userDb.addUser(userId, password, userType, fullName);
                                                                            }
                                                                        }
                                                                }
                                                                break;
                                                        }
                                                        break;
                                                    default:
                                                        System.out.println("Incomplete arguments for add command.");
                                                        break;
                                                }
                                                break;
                                            case "delete":
                                                switch (exist) {
                                                    case "something":
                                                        switch (tokensArray2[1].toLowerCase()) {
                                                            case "appointment":
                                                                switch(exist2Element){
                                                                    case "something":
                                                                        doctorId = tokensArray2[2];
                                                                        switch (exist3Element){
                                                                            case "something":
                                                                                patientId = tokensArray2[3];
                                                                                switch (exist4Element) {
                                                                                    case "something":
                                                                                        timeslot1 = tokensArray2[4];
                                                                                        appDb.deleteAppAsAdmin(doctorId, patientId, timeslot1);
                                                                                        break;
                                                                                }
                                                                        }
                                                                }
                                                                break;

                                                            default:
                                                                userType = tokensArray2[1];
                                                                switch (exist2Element){
                                                                    case "something":
                                                                        userId = tokensArray2[2];
                                                                        userDb.deleteUser(userId,userType);
                                                                }
                                                                break;
                                                        }
                                                        break;
                                                    default:
                                                        System.out.println("Incomplete arguments for add command.");
                                                        break;
                                                }
                                                break;
                                            case "list":
                                                switch (exist) {
                                                    case "something":
                                                        switch (tokensArray2[1].toLowerCase()) {
                                                            case "appointment":
                                                                System.out.println(appDb.toString());
                                                                break;
                                                            case "user":
                                                                System.out.println(userDb.toString());
                                                                break;
                                                            default:
                                                                System.out.println("Invalid command for list.");
                                                        }
                                                        break;
                                                    default:
                                                        System.out.println("Incomplete arguments for list command.");
                                                        break;
                                                }
                                                break;
                                            default:
                                                System.out.println("Invalid data");
                                                break;
                                        }
                                    }
                                    break;
                                case "No":
                                    System.out.println("Wrong password!");
                                    break;
                            }
                            break;
                    case "d":
                        Scanner pas2 = new Scanner(System.in);
                        System.out.print("Password: ");
                        String passw2 = pas2.nextLine();
                        switch (userDb.passwordCheck(tokensArray[1],passw2)){
                            case "Yes":
                                System.out.println("Login is successful."+"   Welcome to Doctor Appointment Management System!  "+tokensArray[1]);
                                for(boolean loopingEnd = false;!loopingEnd;){
                                    ready();
                                    Scanner in3 = new Scanner(System.in);
                                    String line3 = in3.nextLine();
                                    String[] tokensArray2 = getTokens(line3);
                                    String exist="";
                                    String exist2Element="";
                                    if(tokensArray2.length>1){
                                        exist="something";
                                    }
                                    if(tokensArray2.length>2){
                                        exist2Element="something";
                                    }
                                    switch (tokensArray2[0].toLowerCase()){
                                        case "help":
                                            switch (exist) {
                                                default:
                                                    helpDoctorOrUser();
                                                    break;
                                                case "something":
                                                    helpCommandDoctor(tokensArray2[1]);
                                                    break;
                                            }
                                            break;
                                        case "logout":
                                            loopingEnd=true;
                                            break;
                                        case "add":
                                            switch (exist) {
                                                case "something":
                                                    patientId=tokensArray2[1];
                                                    switch (exist2Element){
                                                        case "something":
                                                            timeslot1=tokensArray2[2];
                                                            appDb.addAppAsDoctorOrUser(patientId,timeslot1);
                                                            break;
                                                    }
                                                default:
                                                    System.out.println("Incomplete arguments for add command.");
                                                    break;
                                            }
                                            break;
                                        case "delete":
                                            switch (exist) {
                                                case "something":
                                                    patientId=tokensArray2[1];
                                                    switch (exist2Element) {
                                                        case "something":
                                                            timeslot1=tokensArray2[2];
                                                            appDb.deleteAppAsDoctorOrUser(patientId,timeslot1);
                                                            break;
                                                    }
                                                default:
                                                    System.out.println("Incomplete arguments for delete command.");
                                                    break;
                                                   }
                                            break;
                                        case "show":
                                            switch (exist){
                                                case "reminder":
                                                    appDb.showReminderForDoctor();
                                                    break;
                                                default:
                                                    System.out.println("Sorry, timetable is not ready yet.");
                                                    break;
                                            }
                                            break;
                                        default:
                                            System.out.println("Invalid data");
                                            break;
                                    }
                                }
                                break;
                            case "No":
                                System.out.println("Wrong password!");
                                break;
                        }
                        break;
                    case "p":
                        Scanner pas3 = new Scanner(System.in);
                        System.out.print("Password: ");
                        String passw3 = pas3.nextLine();
                        switch (userDb.passwordCheck(tokensArray[1],passw3)){
                            case "Yes":
                                System.out.println("Login is successful."+"   Welcome to Doctor Appointment Management System!  "+tokensArray[1]);
                                for(boolean loopingEnd = false;!loopingEnd;){
                                    ready();
                                    Scanner in4 = new Scanner(System.in);
                                    String line4 = in4.nextLine();
                                    String[] tokensArray2 = getTokens(line4);
                                    String exist="";
                                    String exist2Element="";
                                    if(tokensArray2.length>1){
                                        exist="something";
                                    }
                                    if(tokensArray2.length>2){
                                        exist2Element="something";
                                    }
                                    switch (tokensArray2[0].toLowerCase()){
                                        case "help":
                                            switch (exist) {
                                                default:
                                                    helpDoctorOrUser();
                                                    break;
                                                case "something":
                                                    helpCommandPatient(tokensArray2[1]);
                                                    break;
                                            }
                                            break;
                                        case "logout":
                                            loopingEnd=true;
                                            break;
                                        case "add":
                                            switch (exist) {
                                                case "something":
                                                    doctorId=tokensArray2[1];
                                                    switch (exist2Element) {
                                                        case "something":
                                                            timeslot1 = tokensArray2[2];
                                                            appDb.addAppAsDoctorOrUser(doctorId, timeslot1);
                                                            break;
                                                    }
                                                default:
                                                    System.out.println("Incomplete arguments for add command.");
                                                    break;
                                            }
                                            break;
                                        case "delete":
                                            switch (exist) {
                                                case "something":
                                                    doctorId=tokensArray2[1];
                                                    switch (exist2Element) {
                                                        case "something":
                                                            timeslot1 = tokensArray2[2];
                                                            appDb.deleteAppAsDoctorOrUser(doctorId, timeslot1);
                                                            break;
                                                    }
                                                default:
                                                    System.out.println("Incomplete arguments for add command.");
                                                    break;
                                            }
                                            break;
                                        case "show":
                                            switch (exist){
                                                case "reminder":
                                                    appDb.showReminderForUser();
                                                    break;
                                                default:
                                                    System.out.println("Sorry, timetable is not ready yet.");
                                                    break;
                                            }
                                            break;
                                        default:
                                            System.out.println("Invalid data");
                                            break;
                                    }
                                }
                                break;
                            case "No":
                                System.out.println("Wrong password!");
                                break;
                        }
                        break;
                    default:
                            System.out.println("\nInvalid username!");
                            break;
                    }
                    break;
                default:
                    System.out.println("\nUnknown command. Try again...\n");
                    break;
                    }
            }
        }

    public void welcomeMenu(){
        System.out.println("+-------------------------------+"+"\n" +
                           "|                               |"+"\n"+
                           "|   Doctor Appointment System   |"+"\n" +
                           "|                               |"+"\n"+
                           "+-------------------------------+");
        System.out.println("Available commands:"+"\n"+
                           "  Login User"+"\n" +
                           "  Quit");

    }
    public void ready(){
        System.out.print("\nready> ");
    }

    public static int skipSpaces(String str, int start) {
        int pos = start;
        while (pos < str.length()) {
            char c = str.charAt(pos);
            if (c != ' ' ) {
                break;
            } else {
                pos++;
            }
        }
        return pos;
    }

    public static String nextToken(String str, int start) {
        int pos = start;
        String newToken="";
        while (pos < str.length()) {
            char c = str.charAt(pos);
            if (c != ' ') {
                newToken= newToken+ c;
                pos++;
            } else {
                break;
            }
        }
        return newToken;
    }

    public static String [] addToTokenArray(String [] tokenArray, String newToken) {
        // create a new array, newTokenArray, which is longer than tokenArray by ONE element
        if(newToken.length()==0){
            return tokenArray;
        }
        String [] newTokenArray = new String[tokenArray.length+1];
        // copy all elements from tokenArray to newTokenArray
        for(int i=0;i< tokenArray.length;i++){
            newTokenArray[i]=tokenArray[i];
        }
        newTokenArray[newTokenArray.length-1] = newToken;
        // add newToken as the last element of newTokenArray

        return newTokenArray;
    }

    public static String [] getTokens(String str){
        int pos =0;
        String[] tokenArray = new String[0];
        while(pos<str.length()){
            pos =skipSpaces(str, pos);
            String newToken =nextToken(str,pos);
            pos+=newToken.length();
            tokenArray = addToTokenArray(tokenArray,newToken);
        }
        return tokenArray;
    }
    private void helpAdmin() {
        System.out.println("Available commands:");
        System.out.println("  load    [user | appointment ]");
        System.out.println("  save    [user | appointment ]");
        System.out.println("  list    [user | appointment ]");
        System.out.println("  show    [reminder | timetable ]");
        System.out.println("  add     [user | appointment ]");
        System.out.println("  delete  [user | appointment ]");
        System.out.println("  help    [ command ]");
        System.out.println("  logout");
    }
    private void helpCommandAdmin(String command) {
        switch (command.toLowerCase()){
            case "add":
                System.out.println("\"add\" -- adds a new user (admin/doctor/patient) or appointment to the system.");
                System.out.println("Usage of \"add\":");
                System.out.println("   add admin userID passwd [ fullname ]");
                System.out.println("   add doctor userID passwd [ fullname ]");
                System.out.println("   add patient userID passwd [ fullname ]");
                System.out.println("   add appointment doctorID patientID timeslot");
                break;
            case "delete":
                System.out.println("\"delete\" -- delete a user (admin/doctor/patient) or appointment from the system.");
                System.out.println("Usage of \"delete\":");
                System.out.println("   delete admin userID");
                System.out.println("   delete doctor userID");
                System.out.println("   delete patient userID");
                System.out.println("   delete appointment doctorID patientID timeslot");
                break;
            case "show":
                System.out.println("\"show\" -- shows reminders or timetable of a user.");
                System.out.println("Usage of \"show\":");
                System.out.println("   show reminder userID");
                System.out.println("   show timetable userID");
                break;
            case "list":
                System.out.println("\"list\" -- lists all users or appointments.");
                System.out.println("Usage of \"show\":");
                System.out.println("   list user");
                System.out.println("   list appointment");
                break;
            case "load":
                System.out.println("\"load\" -- loads users or appointments from a file.");
                System.out.println("Usage of \"load\":");
                System.out.println("   load user filename");
                System.out.println("   load appointment filename");
                break;
            case "save":
                System.out.println("\"save\" -- saves users or appointments to a file.");
                System.out.println("Usage of \"save\":");
                System.out.println("   save user fName");
                System.out.println("   save appointment fName");
                break;
            case "help":
                System.out.println("\"help\" -- shows this help message.");
                System.out.println("Usage of \"show\":");
                System.out.println("   help");
                System.out.println("   help command");
                break;
        }
    }

    private void helpCommandDoctor(String command){
        switch (command.toLowerCase()) {
            case "add":
                System.out.println("\"add\" -- adds a new appointment to the system.");
                System.out.println("Usage of \"add\":");
                System.out.println("   add patientID timeslot");
                break;
            case "delete":
                System.out.println("\"delete\" -- delete an appointment from the system.");
                System.out.println("Usage of \"delete\":");
                System.out.println("   delete patientID timeslot");
                break;

            case "show":
                System.out.println("\"show\" -- shows reminders or timetable.");
                System.out.println("Usage of \"show\":");
                System.out.println("   show reminder userID");
                System.out.println("   show timetable userID");
                break;
            case "help":
                System.out.println("\"help\" -- shows this help message.");
                System.out.println("Usage of \"show\":");
                System.out.println("   help");
                System.out.println("   help command");
                break;
        }
    }

    private void helpDoctorOrUser() {
        System.out.println("show     [ reminded | timetable ]");
        System.out.println("add      appointment");
        System.out.println("delete   appointment");
        System.out.println("help     [ command ]");
        System.out.println("logout");
    }
    private void helpCommandPatient(String command){
        switch (command.toLowerCase()) {
            case "add":
                System.out.println("\"add\" -- adds a new appointment to the system.");
                System.out.println("Usage of \"add\":");
                System.out.println("   add doctorID timeslot");
                break;
            case "delete":
                System.out.println("\"delete\" -- delete an appointment from the system.");
                System.out.println("Usage of \"delete\":");
                System.out.println("   delete doctorID timeslot");
                break;

            case "show":
                System.out.println("\"show\" -- shows reminders or timetable.");
                System.out.println("Usage of \"show\":");
                System.out.println("   show reminder userID");
                System.out.println("   show timetable userID");
                break;
            case "help":
                System.out.println("\"help\" -- shows this help message.");
                System.out.println("Usage of \"show\":");
                System.out.println("   help");
                System.out.println("   help command");
                break;
        }
    }
}
