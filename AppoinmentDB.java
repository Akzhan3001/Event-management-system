import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class AppoinmentDB {
    private Appointment [] adminAppointments = new Appointment[0];
    private Appointment [] appointments = new Appointment[0];

    public  void login(String newFile) {
        int cnt=0;
        Appointment [] newAppointment= new Appointment[adminAppointments.length+1];
        Appointment [] newAppointmentForUser= new Appointment[appointments.length+1];

        try {
            Appointment newApp,newAppForUser;
            File inFile = new File(newFile);
            Scanner inputFile = new Scanner(inFile);
            while (inputFile.hasNextLine()) {
                String str = inputFile.nextLine();
                String [] tokenArray = getTokens(str);
                newApp = new Appointment (tokenArray[0],tokenArray[1],tokenArray[2]);
                newAppForUser = new Appointment(tokenArray[1],tokenArray[2]);
                newAppointmentForUser = addAppToLoad(newAppointmentForUser,cnt,newAppForUser);
                newAppointment = addAppToLoad(newAppointment,cnt,newApp);
                cnt++;
            }
            adminAppointments = newAppointment;
            appointments = newAppointmentForUser;
            inputFile.close();
        } catch (FileNotFoundException e) {
            System.out.println("UserDB.loadDB failed: File not found ("+newFile+")!");
            e.printStackTrace();
        }
    }

    public Appointment search(Appointment a){
        for(int i=0;i<adminAppointments.length;i++){
            if(a.equals(adminAppointments[i])){
                return adminAppointments[i];
            }
        }
        return null;
    }
    public int searchIndex(Appointment a){
        for(int i=0;i<adminAppointments.length;i++){
            if(a.equals(adminAppointments[i])){
                return i;
            }
        }
        return 0;
    }

    public  void loadDB(String newFile){
        int cnt=0;
        Appointment [] newAppointment= new Appointment[cnt+1];
        Appointment [] fullListOfAppointment = new Appointment[newAppointment.length + adminAppointments.length];

        try {
            Appointment newUser;
            File inFile = new File(newFile);
            Scanner inputFile = new Scanner(inFile);
            while (inputFile.hasNextLine()) {
                String str = inputFile.nextLine();
                String [] tokenArray = getTokens(str);
                newUser = new Appointment (tokenArray[0],tokenArray[1],tokenArray[2]);
                if(search(newUser)!= null){
                    System.out.println("UserDB AppointmentDB: error adding record on line "+(cnt+1)+" of "+newFile+" -- Duplicated user record ("+newUser.getTimeslot() +")");
                    continue;
                }
                else{
                    newAppointment = addAppToLoad(newAppointment,cnt,newUser);
                    cnt++;
                }
            }
            inputFile.close();
            fullListOfAppointment = mergeArray(adminAppointments,newAppointment);
        } catch (FileNotFoundException e) {
            System.out.println("UserDB.loadDB failed: File not found ("+newFile+")!");
            e.printStackTrace();
        }
        adminAppointments = fullListOfAppointment;
        System.out.println(cnt+ " appointments are loaded.");
    }

    public static Appointment [] addAppToLoad(Appointment [] newListOfUser,int index,Appointment newUser){
        Appointment [] newList = new Appointment[index+1];
        for(int i=0;i<index;i++){
            newList[i] = newListOfUser[i];
        }
        newList[index] = newUser;
        return newList;
    }

    public static  Appointment [] mergeArray(Appointment [] a, Appointment [] b) {
        Appointment [] c = new Appointment[a.length + b.length];
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < a.length) {
            c[k++] = a[i++];
        }
        while (j < b.length) {
            c[k++] = b[j++];
        }
        return c;
    }

    public void saveDB(String outfile) throws Exception{
        FileWriter fwriter = new FileWriter(outfile, true);//printing the output file
        PrintWriter out = new PrintWriter(fwriter);
        for(int i=0;i< adminAppointments.length;i++) {
            out.println(adminAppointments[i]);
        }
        out.close();
    }

    public void addAppAsAdmin(String doctorId, String patientId, String timeslot){
        if(searchDoctor(doctorId)) {
            if(searchPatient(patientId)) {
                if(chckTimeslotDay(timeslot)=="day") {
                    if(checkTimeslotHour(timeslot)=="time"){
                            Appointment[] newAppList = new Appointment[adminAppointments.length + 1];
                            Appointment newApp = new Appointment(doctorId, patientId, timeslot);
                            if (search(newApp) != null) {
                                System.out.println("AppointmentDB addDB: error adding record " + " -- appointment record (" + search(newApp).getTimeslot() + ") is already assigned to another patient.");
                            } else {
                                for (int i = 0; i < adminAppointments.length; i++) {
                                    newAppList[i] = adminAppointments[i];
                                }
                                newAppList[adminAppointments.length] = newApp;
                                adminAppointments = newAppList;
                                System.out.println(newApp.getPatientId()+ "  is successfully added to  "+newApp.getDoctorId() + "  at  "+ newApp.getTimeslot()+"\n");
                                printAdminDB();
                            }
                    }
                    else{
                        System.out.println("Invalid entered time!");
                    }
                }
                else{
                    System.out.println("Invalid entered day char!");
                }
            }

            else{
                System.out.println("Entered patient " +patientId+" not found!");
            }
        }

        else{
            System.out.println("Entered doctorId "+doctorId+" not found!");
            }
    }

    public void printAdminDB(){
        for(int j=0;j< adminAppointments.length;j++){
            System.out.println(adminAppointments[j]);
        }
    }

    public void deleteAppAsAdmin(String doctorId, String patientId, String timeslot){
        if(searchDoctor(doctorId)) {
            if(searchPatient(patientId)) {
                if(chckTimeslotDay(timeslot)=="day") {
                    if(checkTimeslotHour(timeslot)=="time"){
                        Appointment[] newAppList = new Appointment[adminAppointments.length - 1];
                        Appointment newApp = new Appointment(doctorId, patientId, timeslot);
                        System.out.println(newApp);
                            for (int i = 0, k = 0; i < adminAppointments.length; i++) {
                                if (searchIndex(newApp)==i) {
                                    continue;
                                }
                                else {
                                    newAppList[k++] = adminAppointments[i];
                                }

                            }
                        adminAppointments = newAppList;
                        System.out.println(newApp.getPatientId()+   "  is successfully removed to  "+newApp.getDoctorId() + "  at  "+ newApp.getTimeslot()+"\n");
                        printAdminDB();
                    }
                    else{
                        System.out.println("Invalid entered time! " +substring(timeslot,1,3));
                    }
                }
                else{
                    System.out.println("Invalid entered day char! "+ substring(timeslot,0,1));
                }
            }

            else{
                System.out.println("Entered patient " +patientId+" not found!");
            }
        }

        else{
            System.out.println("Entered doctorId "+doctorId+" not found!");
        }
    }

    public void addAppAsDoctorOrUser(String patientId,String timeslot){
        if(searchPatient(patientId)) {
            if(chckTimeslotDay(timeslot)=="day"){
                if(checkTimeslotHour(timeslot)=="time") {
                    if(searchPatient(patientId)&& chckTimeslotDay(timeslot)==null) {
                        Appointment[] newAppList = new Appointment[appointments.length + 1];
                        Appointment newApp = new Appointment(patientId, timeslot);
                        if (search(newApp) != null) {
                            System.out.println("AppointmentDB addDB: error adding record " + " -- appointment record (" + search(newApp).getTimeslot() + ") is already assigned to another patient.");
                        } else {
                            for (int i = 0; i < appointments.length; i++) {
                                newAppList[i] = appointments[i];
                            }
                            newAppList[appointments.length + 1] = newApp;
                            appointments = newAppList;
                            System.out.println("Added successfully!");
                            printAppointmentForUser();
                        }
                    }
                }
                else{
                    System.out.println("Invalid entered time!");
                }
            }
            else {
                System.out.println("Invalid entered day char!");
            }
        }
        else{
                System.out.println("Entered patient " + patientId + " not found!");
        }

    }

    public void deleteAppAsDoctorOrUser(String patientId,String timeslot){
        if(searchPatient(patientId)) {
            if(chckTimeslotDay(timeslot)=="day"){
                if(checkTimeslotHour(timeslot)=="time") {
                    if(searchPatient(patientId)&& chckTimeslotDay(timeslot)==null) {
                        Appointment[] newAppList = new Appointment[appointments.length + 1];
                        Appointment newApp = new Appointment(patientId, timeslot);
                        if (search(newApp) != null) {
                            System.out.println("AppointmentDB addDB: error adding record " + " -- appointment record (" + search(newApp).getTimeslot() + ") is already assigned to another patient.");
                        } else {
                            for (int i = 0; i < appointments.length; i++) {
                                newAppList[i] = appointments[i];
                            }
                            newAppList[appointments.length + 1] = newApp;
                            appointments = newAppList;
                            System.out.println("Deleted successfully!");
                            printAppointmentForUser();
                        }
                    }
                }
                else{
                    System.out.println("Invalid entered time!");
                }
            }
            else {
                System.out.println("Invalid entered day char!");
            }
        }


        else{
            System.out.println("Entered patient " + patientId + " not found!");
        }

    }

    public void printAppointmentForUser(){
        for(int i=0;i<appointments.length;i++){
            System.out.println(appointments[i]);
        }
    }

    public void showReminderForDoctor(){
        for(int i=0;i<appointments.length;i++){
            System.out.println(appointments[i].getTimeslot()+" -- "+" Patient "+appointments[i].getPatientId());
        }
    }

    public void showReminderForUser(){
        for(int i=0;i<appointments.length;i++){
            System.out.println(appointments[i].getTimeslot()+" -- "+" Doctor "+appointments[i].getDoctorId());
        }
    }

    public boolean searchDoctor(String doctorId){
        for(int i=0;i<adminAppointments.length;i++){
            if(adminAppointments[i].getDoctorId()==doctorId);{
                return true;
            }
        }
        return false;
    }

    public boolean searchPatient(String patientId){
        for(int i=0;i<adminAppointments.length;i++){
            if(adminAppointments[i].getDoctorId()==patientId);{
                return true;
            }
        }
        return false;
    }

    public String chckTimeslotDay(String timeslot){
        String timeSlot = timeslot.toUpperCase();
        String dayChar = substring(timeSlot,0,1);
        if(dayChar.equals("M")|| dayChar.equals("T") || dayChar.equals("W")|| dayChar.equals("R")||dayChar.equals("F")) {
            return "day";
        }


        return null;
    }
    public String checkTimeslotHour(String timeslot){
        String timeChar = substring(timeslot,1,3);
        if(timeChar.equals("09")|| timeChar.equals("10")||timeChar.equals("11")||timeChar.equals("12")||timeChar.equals("13")||timeChar.equals("14")||timeChar.equals("15")||timeChar.equals("16")||timeChar.equals("17")||timeChar.equals("18")){
            return "time";
        }

        return null;
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

    String substring(String str, int beginIndex, int endIndex) {
        String substring ="";
        for (int i = beginIndex; i < endIndex; i++) {
            char c = str.charAt(i);
            substring +=c;
        }
        return  substring;
    }

    @Override
    public String toString() {
        String str = "";
        int i=0;
        while (i< adminAppointments.length){
            str= str + adminAppointments[i];
            i++;
        }
        return str;
    }
}
