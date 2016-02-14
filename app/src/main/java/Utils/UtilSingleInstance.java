package Utils;

/**
 * Created by Narendra on 14-02-2016.
 */
public class UtilSingleInstance {

   public static UtilSingleInstance utilSingleInstance=null;
    private UtilSingleInstance(){

    }

    public static UtilSingleInstance getInstance(){

        if(utilSingleInstance==null){
            utilSingleInstance=new UtilSingleInstance();
        }
        return utilSingleInstance;
    }

    public String getLoggedInType() {
        return LoggedInType;
    }

    public void setLoggedInType(String loggedInType) {
        LoggedInType = loggedInType;
    }

    String LoggedInType;




}
