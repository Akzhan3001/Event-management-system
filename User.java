import java.util.Objects;

public class User {
    private String userID;
    private String password;
    private String userType;
    private String fullName;

    public User(String userID, String password, String userType, String fullName){
        this.userID = userID;
        this.password = password;
        this.userType = userType;
        this.fullName = fullName;
    }
    public User(String userID){
        this.userID = userID;
        this.password = getPassword();
        this.userType = getUserType();
        this.fullName = getFullName();
    }

    public User(String [] user){
        this.userID = user[0];
        this.userType = user[1];
        this.password = user[2];
        this.fullName = getFullName();
    }

    public User(){
        this.userID = null;
        this.userType= null;
        this.password = null;
        this.fullName = null;
    }

    public String getUserID() {
        return userID;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public boolean equals(String newUserId) {
        return this.getUserID().toLowerCase() ==newUserId.toLowerCase();
    }

    public boolean equals(String newUserId,String password) {
        return this.userID.toLowerCase() ==newUserId.toLowerCase() && this.password.toLowerCase()==password.toLowerCase();
    }

    @Override
    public String toString() {
        return  userID + "\t" + password +"\t"+ userType + "\t"+ fullName + "\n";
    }
}
