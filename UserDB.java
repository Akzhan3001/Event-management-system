import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.io.File;


public class UserDB {
    private User [] users;
    public UserDB(){
        this.users = new User[0];
    }

    public User[] getUsers() {
        return users;
    }

    public  void login(String newFile) {
        int cnt=0;
        User [] addingUsers = new User[users.length+1];
        try{
            User newUser;
            File inFile = new File(newFile);
            Scanner inputFile = new Scanner(inFile);
            while (inputFile.hasNextLine()) {
                String str = inputFile.nextLine();
                String [] tokenArray = getTokens(str);
                String fullN= "";
                for(int i=3;i< tokenArray.length;i++){
                    fullN += " "+tokenArray[i];
                }
                newUser = new User (tokenArray[0],tokenArray[1],tokenArray[2],fullN);
                addingUsers = addUserToLoad(addingUsers, cnt, newUser);
                cnt++;
            }
            users = addingUsers;
            inputFile.close();
        }
        catch (FileNotFoundException e){
            System.out.println("UserDB.loadDB failed: File not found ("+newFile+")!");
            e.printStackTrace();
        }
    }

    public  void loadDB(String newFile){
        int cnt=0;
        User [] newListOfUsers= new User[cnt+1];
        User [] fullListOfUsers = new User[newListOfUsers.length + users.length];

        try {
            User newUser;
            File inFile = new File(newFile);
            Scanner inputFile = new Scanner(inFile);
            while (inputFile.hasNextLine()) {
                String str = inputFile.nextLine();
                String [] tokenArray = getTokens(str);
                String fullN= "";
                for(int i=3;i< tokenArray.length;i++){
                    fullN += " "+tokenArray[i];
                }
                if(tokenArray.length!=0) {
                    newUser = new User(tokenArray[0], tokenArray[1], tokenArray[2], fullN);
                    if (searchUser(tokenArray[0]) == true) {
                        if(newListOfUsers[0]!=null && searchUserInNewArray(newListOfUsers,tokenArray[0])) {
                            System.out.println("UserDB LoadDB: error adding record on line " + (cnt + 1) + " of " + newFile + " -- Duplicated user record (" + newUser.getUserID() + ")");
                            continue;
                        }
                        else{
                            System.out.println("UserDB LoadDB: error adding record on line " + (cnt + 1) + " of " + "User.db" + " -- Duplicated user record (" + newUser.getUserID() + ")");
                            continue;
                        }
                        }
                    else {
                        newListOfUsers = addUserToLoad(newListOfUsers, cnt, newUser);
                        cnt++;
                    }
                }
            }
            inputFile.close();
            fullListOfUsers = mergeArray(users,newListOfUsers);
        } catch (FileNotFoundException e) {
            System.out.println("UserDB.loadDB failed: File not found ("+newFile+")!");
            e.printStackTrace();
        }
        users = fullListOfUsers;
        System.out.println(cnt+" users are loaded.");
    }

    public void saveDB(String userDB) throws Exception{
        FileWriter fwriter = new FileWriter(userDB, true);//printing the output file
        PrintWriter out = new PrintWriter(fwriter);
        for(int i=0;i< users.length;i++) {
            out.println(users[i]);
        }
        System.out.println("Successfully saved!");
        out.close();
    }

    public static User [] addUserToLoad(User [] newListOfUser,int index,User newUser){
        User [] newList = new User[index+1];
        for(int i=0;i<index;i++){
            newList[i] = newListOfUser[i];
        }
        newList[index] = newUser;
        return newList;
    }

    public static  User [] mergeArray(User [] a, User [] b) {
        User [] c = new User[a.length + b.length];
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

    public  boolean searchUser(String newUser){
        for(int i=0;i<users.length;i++){
            if(users[i].getUserID().equals(newUser)){
                return true;
            }
        }
        return false;
    }

    public  boolean searchUserInNewArray(User [] newUsers,String newUser){
        for(int i=0;i< newUsers.length;i++){
            if(newUsers[i].getUserID().equals(newUser)){
                return true;
            }
        }
        return false;
    }

    public String passwordCheck(String userId,String password){
        for(int i=0;i<users.length;i++){
            if(users[i].getPassword().equals(password)){
                return "Yes";
            }
        }
        return "No";
    }

    public void addUser(String userID, String password,String userType,String fullName){
        User newUser = new User(userID,password,userType,fullName);
        User [] newList = new User[users.length+1];
        if(!searchUser(userID)) {
            if( !userType.equals("a") && !userType.equals("d") && !userType.equals("p")){
                System.out.println("Access denied. Invalid user type.");
            }
            else {
                for (int i = 0; i < users.length; i++) {
                    newList[i] = users[i];
                }
                newList[users.length] = newUser;
                System.out.println(userID+" is added successfully!\n");
                users = newList;
                printDB();
            }
        }
        else {
            System.out.println("UserDB addDB: error adding record " + " -- user record with ID ( " + newUser.getUserID() + " ) is already exists");
        }
    }

    public String searchUserType(String userId){
        for(int i=0;i< users.length;i++){
            if(users[i].getUserID().equals(userId)){
                return users[i].getUserType();
            }
        }
        return "";
    }

    public String userTypeCheck(String userType){
        for(int i=0;i< users.length;i++){
            if(users[i].getUserType().toLowerCase().equals(userType.toLowerCase())){
                return users[i].getUserType();
            }
        }
        return "";
    }

    public void deleteUser(String userID,String userType){
        User [] newList = new User[users.length-1];
        if(searchUser(userID) &&  userTypeCheck(userType)==""){
            System.out.println("Access denied. Invalid user type.");}

        else if(!searchUser(userID)){
            System.out.println("UserDB deleteDB: error deleting record " + " -- user record (" + userID + ") not found!");
        }
        else {
            for (int i = 0, k = 0; i < users.length; i++) {
                if (users[i].getUserID().equals(userID)) {
                    continue;
                }
                else {
                    newList[k++] = users[i];
                }
                }
            System.out.println(userID+" is deleted successfully!\n");
            users = newList;
            printDB();
        }
    }

    public void printDB(){
        for(int j=0;j< users.length;j++){
            System.out.println(users[j]);
        }
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

    @Override
    public String toString() {
        String str = "";
        int i=0;
        while (i< users.length){
            str= str + users[i];
            i++;
        }
        return str;
    }

}
