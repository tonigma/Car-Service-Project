//For toni//
//Please set the names of the tables and the names of the columns as in the sql tables
public class Columns {
    public static String[] GetClientsColumns(){
        return new String[]{"ClientId","Fname","Lname","CarId"};
    }
    public static String[] GetCarColumns(){
        return new String[]{"CarId","CarName","CarModel","ProblemId"};
    }

    public static String[] GetProblemsColumns(){
        return new String[]{"ProblemId","ProblemName","Price","Duration"};
    }
    public static String GetClientsTableName(){
        return "Clients";
    }
    public static String GetCarsTableName(){
        return "Cars";
    }
    public static String GetProblemTableName(){
        return "Problems";
    }

    public static String[] GetClientCarJoinColumns(){
        return new String[]{"ClientId","First name","Last name","Car name"};
    }
    public static String[] GetCarProblemJoinColumns(){
        return new String[]{"CarId","Car name","Car model","Problem"};
    }
}
