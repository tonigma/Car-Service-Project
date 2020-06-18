import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.desktop.SystemSleepEvent;

//custom model to fill JTable
public class MyModel extends DefaultTableModel {

    public MyModel(){

    }
    public void AddRow(Object[] obj){
        this.addRow(obj);
    }
    public void AddColumns(String[] columns){
            if (columns.length>0){
                for (String col : columns) {
                    this.addColumn(col);
                }
            }else{
                System.err.println("Columns are empty");
            }
    }

    //Makes the cells uneditable
    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    public void HideColumn(JTable table){
        TableColumnModel tcm = table.getColumnModel();
        tcm.removeColumn(tcm.getColumn(0));
    }
}
