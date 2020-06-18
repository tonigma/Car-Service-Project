import javax.swing.*;
import java.util.regex.Pattern;

public class MyTable {
    private String tableName;
    private String[] joinColumnsInTable = new String[4];
    private String[] columnNames=new String[4];
    private int count = 0;

    public MyTable(String tableName){
        this.tableName = tableName;
    }
    public void AddJoinColumnsInTable(String[] columns){
        joinColumnsInTable = columns;
    }
    public MyTable(String tableName,String[] columns){
        this.tableName = tableName;
        for (int i = 0;i<columns.length;i++){
            AddColumn(columns[i]);
        }
    }

    //Get insert query for the table
    public String GetInsetQuery(Object column1Value,Object column2Value,Object column3Value){
        return "INSERT INTO "+tableName+" values(null,'"+column1Value+"','"+column2Value+"','"+column3Value+"')";
    }

    //Get update query for the table
    public String GetUpdateQuery(String query,int id){
        return "UPDATE "+tableName+query+" WHERE "+columnNames[0]+"= '"+id+"'";
    }

    //Creating the update query
    public String CreateUpdateQuery(String columnName,Object value,String query){
        //String updateQuery = "UPDATE "+tableName+" SET "+columnName+"="+value+" WHERE "+columnNames[0]+"="+id;
        query = UpdateQuery(columnName,value,query);
        return query;
    }

    //Checks if the query that is passed is empty
    private String UpdateQuery(String columnName, Object value,String query) {
        if (!query.equals("")){
            query = ","+columnName+" = '"+value+"'";
            return query;
        }
        query =" SET "+columnName+" = "+"'"+value+"'";
        return query;
    }

    //Get delete query for the table
    public String GetDeleteQuery(int id) {
        return "DELETE FROM "+tableName+" WHERE "+columnNames[0]+" = '"+id+"'";
    }

    //Checks if the query passed is empty
    public String GetDataFromTable(String comparison,String orderBy){
        if (!comparison.equals("")){
            return GetSearchedElementsFromTable(comparison,orderBy);
        }
        return GetEverythingFormSqlTable(orderBy);
    }

    public String GetJoinTableInfo(String comparison,String orderBy,MyTable table){
        return "SELECT m."+columnNames[0]+",m."+columnNames[1]+",m."+columnNames[2]+",t."+
                table.getColumnName(1)+" FROM "+tableName+" m  join "+table.tableName+" t  ON m."+columnNames[3]+"= t."
                +table.getColumnName(0)+" "+comparison+" ORDER BY "+columnNames[0]+" "+orderBy;
    }
    //Gets LIKE query
    private String GetSearchedElementsFromTable(String comparison,String orderBy){
        return "SELECT * FROM "+tableName+" "+comparison+" ORDER BY "+columnNames[0]+" "+orderBy;
    }

    //selects everything from the table order by id
    private String GetEverythingFormSqlTable(String orderBy){
        return "SELECT * FROM "+tableName+" ORDER BY "+columnNames[0]+" "+orderBy;
    }

    //Gets search element
    public String SearchQuery(JTextField field1,String query){
        query = MakeQuery(field1,query);
        return query;
    }

    //Checks if the text field passed can be parsed
    private String MakeQuery(JTextField field1,String query){
        if (field1.isEnabled()){
            try {
                Double.parseDouble(field1.getText());
                query = SqlNumbersSearchQuery(query,field1.getName(),field1.getText());
            }catch (Exception x){
                query = SqlStringSearchQuery(query,field1.getName(),field1.getText());
            }
        }
        return query;
    }

    //If we want to search for a string in the DB
    private String SqlStringSearchQuery(String query, String column, String searchString) {//CREATE THE QUERY FOR STRINGS
        if (query.equals("")) {
            query +="WHERE "+column + " LIKE '%" + searchString + "%'";
        } else {
            query += " AND " + column + " LIKE '%" + searchString + "%'";
        }
        return query;
    }

    //If we want to search for a number in the DB
    private String SqlNumbersSearchQuery(String query, String column, String searchString) {//CREATE THE QUERY FOR NUMBERS
        if (query.equals("")) {
            query +="WHERE "+column + "= '" + searchString+"'";
        } else {
            query += " AND " + column + "= '" + searchString+"'";
        }
        return query;
    }

    //Gets the 2 columns we want from the table
    public String SelectTwoColumns(int column1,int column2){
        return "SELECT "+columnNames[column1]+","+columnNames[column2]+" FROM "+tableName;
    }

    //Select everyThing from table without id
    public String SelectWithoutId(int id){
        return "SELECT "+columnNames[1]+","+columnNames[2]+","+columnNames[3]+" FROM "+tableName+" WHERE "+columnNames[0]+"= '"+id+"'";
    }

    //Get the id if an element by the first column
    public String GetIdByFirstColumn(String columnName,String firstColumnValue){
        return "SELECT "+columnNames[0]+" FROM "+tableName+" WHERE "+columnName+" = '"+firstColumnValue+"' ORDER BY "+columnNames[0]+" DESC";
    }

    //add column by name
    public void AddColumn(String name){
        if (count<columnNames.length){
            columnNames[count]=name;
            count++;
        }else {
            columnNames=MakeMoreColumns();
            columnNames[count]=name;
        }
    }

    //if the limit of the array is passed,makes the array bigger
    private String[] MakeMoreColumns() {
        String[] newColumns = new String[columnNames.length+1];
        for (int i = 0;i<=columnNames.length;i++){
            newColumns[i]=columnNames[i];
        }
        return newColumns;
    }

    //sets all the columns
    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public String[] getJoinColumnsInTable() {
        return joinColumnsInTable;
    }

    public void setJoinColumnsInTable(String[] joinColumnsInTable) {
        this.joinColumnsInTable = joinColumnsInTable;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName(int index){
        return columnNames[index];
    }
}
