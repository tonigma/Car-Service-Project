import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SearchForm extends JFrame {
    //main panel
    private JPanel TablePanel;
    //panels inside Table panel
    private JPanel BottomPanel;
    private JPanel TopPanel;
    //panels inside TopPanel
    private JPanel ClientSearchPanel;
    private JPanel CarSearchPanel;
    private JPanel ProblemsSearchPanel;
    //Drop down
    private JComboBox orderByDropDown;
    //radiobuttons
    private JRadioButton ProblemsRB;
    private JRadioButton CarsTableRB;
    private JRadioButton ClientsTableRB;
    //textfield
    private JTextField CarNameInClientsTF;
    private JTextField LnameTF;
    private JTextField CarModelTF;
    private JTextField CarProblemTF;
    private JTextField ProblemTF;
    private JTextField ProblemPriceTF;
    //checkboxes
    private JCheckBox lastNameCheckBox;
    private JCheckBox CarNameInClientsCheckBox;
    private JCheckBox carModelCheckBox;
    private JCheckBox carProblemCheckBox;
    private JCheckBox problemNameCheckBox;
    private JCheckBox problemPriceCheckBox;
    //buttons
    private JButton EditTableButton;
    private JButton applyButton;
    private JButton backButton;
    private JButton searchButton;
    //using the model to fill jtable
    private MyModel model;
    //tables in the sql db
    private MyTable Clients;
    private MyTable Cars;
    private MyTable Problems;
    //panel table
    private JTable SqlTable;
    private JButton deleteButton;


    public SearchForm() {
        FrameSettings();//settings of the frame;
        init();//initial data
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                MainForm form = new MainForm();
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String comparison = SearchTable();
                GetTableFromSql(SqlTable, comparison, (String) orderByDropDown.getSelectedItem());
            }
        });

        EditTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                TopPanelVisibility();
            }
        });

        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                EnableTextFields();
            }
        };
        lastNameCheckBox.addActionListener(listener);
        CarNameInClientsCheckBox.addActionListener(listener);
        problemNameCheckBox.addActionListener(listener);
        problemPriceCheckBox.addActionListener(listener);
        carModelCheckBox.addActionListener(listener);
        carProblemCheckBox.addActionListener(listener);

        applyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                GetTableFromSql(SqlTable, "", (String) orderByDropDown.getSelectedItem());
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CheckSelectedItem();
            }
        });
        ActionListener listener1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ShowSearchPanel();
                GetTableFromSql(SqlTable, "", (String) orderByDropDown.getSelectedItem());
            }
        };
        CarsTableRB.addActionListener(listener1);
        ClientsTableRB.addActionListener(listener1);
        ProblemsRB.addActionListener(listener1);
    }


    //initial JFRAME info
    private void FrameSettings() {
        this.setTitle("Car service-Search");
        this.setSize(820, 400);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
    }
    //initial info
    private void init() {
        add(TablePanel);
        AddToDropDown(orderByDropDown);
        SQLTables();
        SetTextFieldDisabled();
        GetTableFromSql(SqlTable, "", (String) orderByDropDown.getSelectedItem());
        TopPanel.setVisible(false);
        ProblemTF.setEnabled(false);
        ConnectRadioButtons();
        ShowSearchPanel();
    }

    //gets the selected table from the radioButtons
    private MyTable GetSelectedTable() {
        model = new MyModel();
        if (ClientsTableRB.isSelected()) {
            return Clients;
        } else if (CarsTableRB.isSelected()) {
            return Cars;
        } else if (ProblemsRB.isSelected()) {
            return Problems;
        } else {
            return Clients;//the default table
        }
    }
    //For TONI , set the name of the tables and columns from sql here
    private void SQLTables() {
        //Clients table
        Clients = new MyTable(Columns.GetClientsTableName(),Columns.GetClientsColumns());
        Clients.AddJoinColumnsInTable(Columns.GetClientCarJoinColumns());
        //Cars table
        Cars = new MyTable(Columns.GetCarsTableName(),Columns.GetCarColumns());
        Cars.AddJoinColumnsInTable(Columns.GetCarProblemJoinColumns());
        //Problems table
        Problems = new MyTable(Columns.GetProblemTableName(),Columns.GetProblemsColumns());
        Problems.AddJoinColumnsInTable(Columns.GetProblemsColumns());
        SettingTextFieldsNames();
    }
    private void SettingTextFieldsNames(){
        //tHE NAMES OF THE TextFields are like the columns
        CarNameInClientsTF.setName(Cars.getColumnName(1));//==Fname
        LnameTF.setName(Clients.getColumnName(2));//==CarName for clients table
        CarModelTF.setName(Cars.getColumnName(2));//==CarName
        CarProblemTF.setName(Problems.getColumnName(1));//==Probelm name for car table
        ProblemTF.setName(Problems.getColumnName(1));//==ProblemName
        ProblemPriceTF.setName(Problems.getColumnName(2));//==Price
    }

    //when you click the delete button
    private void CheckSelectedItem() {
        Object obj;
        try{
            obj = SqlTable.getModel().getValueAt(SqlTable.getSelectedRow(),0);
            int index = (int)obj;
            int result = MessageBox.ConfirmationMessage("Are you sure you want to delete this item");
            CheckMessageBoxAnswer(result,index);
        }catch (Exception e){
            MessageBox.Message("Item not selected");
        }

    }

    //Checking if the messageDialog returns yes/no or cancel on the delete statement
    private void CheckMessageBoxAnswer(int result,int index) {
        if (result!=0){
            return;
        }
        MyTable table = GetSelectedTable();
        String deleteQuery = table.GetDeleteQuery(index);
        DeleteSelectedItem(deleteQuery);
        GetTableFromSql(SqlTable, "", (String) orderByDropDown.getSelectedItem());
    }
    //For Toni
    //Connect to sql to delete the the selected record
    private void DeleteSelectedItem(String deleteQuery) {
        //FOR TONI
        //JUST CONNECT TO THE DB and pass the query;
        System.out.println(deleteQuery);//DELETE FROM Problems WHERE id = {selectedIndex};
        DBConnection.delete(deleteQuery);
    }

    //when you click the search button
    //create sql search query
    private String SearchTable() {
        String comparison="";
        MyTable table = GetSelectedTable();
        comparison = GetQuery(table,comparison);
        return comparison;
    }

    //get the text for the search query -- SearchTable()
    private String GetQuery(MyTable table,String query) {
        if (table.equals(Clients)){
            query = Clients.SearchQuery(LnameTF,query);
            query = Clients.SearchQuery(CarNameInClientsTF,query);
        }else if(table.equals(Cars)){
            query = Cars.SearchQuery(CarModelTF,query);
            query = Cars.SearchQuery(CarProblemTF,query);
        }else if (table.equals(Problems)){
            query = Problems.SearchQuery(ProblemTF,query);
            query = Problems.SearchQuery(ProblemPriceTF,query);
        }
        return query;
    }

    //For toni
    //SQL REQUIRED//
    //Sends request to db for the table;
    private void GetTableFromSql(JTable table, String comparison, String order) {
        MyTable mytable = GetSelectedTable();
        MyTable joinTable = GetJoinTable(mytable);
        String sql = "";
        if (joinTable==null){
            sql = mytable.GetDataFromTable(comparison,order);//selects every thing from db
            System.out.println(sql);
        }else{
            sql = mytable.GetJoinTableInfo(comparison,order,joinTable);//selects 2 tables thing from db
            System.out.println(sql);
        }
        //this is for test
        model.AddColumns(mytable.getJoinColumnsInTable());
        table.setModel(DBConnection.filltable(model,sql));
        model.HideColumn(table);//hides the column with id
    }

    private MyTable GetJoinTable(MyTable mytable) {
        if (mytable.equals(Clients)){
            return Cars;
        }else if(mytable.equals(Cars)){
            return Problems;
        }
        return null;
    }

    //enables textFields  when checkBox is selected
    private void EnableTextFields() {
        SetTextFieldDisabled();
        if (lastNameCheckBox.isSelected()) {
            LnameTF.setEnabled(true);
        }
        if (CarNameInClientsCheckBox.isSelected()) {
            CarNameInClientsTF.setEnabled(true);
        }
        if (carProblemCheckBox.isSelected()) {
            CarProblemTF.setEnabled(true);
        }
        if (carModelCheckBox.isSelected()) {
            CarModelTF.setEnabled(true);
        }
        if (problemNameCheckBox.isSelected()) {
            ProblemTF.setEnabled(true);
        }
        if (problemPriceCheckBox.isSelected()) {
            ProblemPriceTF.setEnabled(true);
        }
    }

    //makes all textFields disabled
    private void SetTextFieldDisabled() {
        CarNameInClientsTF.setEnabled(false);
        LnameTF.setEnabled(false);
        CarProblemTF.setEnabled(false);
        CarModelTF.setEnabled(false);
        ProblemTF.setEnabled(false);
        ProblemPriceTF.setEnabled(false);
    }


    //depending on the radioButton displays panel
    private void ShowSearchPanel() {
        //ClearTextFields();
        SearchPanelsVisibility();
        if (ClientsTableRB.isSelected()) {
            ClientSearchPanel.setVisible(true);
        } else if (CarsTableRB.isSelected()) {
            CarSearchPanel.setVisible(true);
        } else if (ProblemsRB.isSelected()) {
            ProblemsSearchPanel.setVisible(true);
        }
    }

    //sets all panels in topPanel invisible
    private void SearchPanelsVisibility() {
        ClientSearchPanel.setVisible(false);
        CarSearchPanel.setVisible(false);
        ProblemsSearchPanel.setVisible(false);
    }

    //clears all fields
    private void ClearTextFields() {
        CarNameInClientsTF.setText("");
        LnameTF.setText("");
        CarModelTF.setText("");
        CarProblemTF.setText("");
        ProblemTF.setText("");
        ProblemPriceTF.setText("");
    }

    //edit table action,shows and hides topPanel
    private void TopPanelVisibility() {
        if (TopPanel.isVisible()) {
            TopPanel.setVisible(false);
            return;
        }
        TopPanel.setVisible(true);
    }

    //connecting the radio buttons in topPanel
    private void ConnectRadioButtons() {
        ButtonGroup tablesRB = new ButtonGroup();
        tablesRB.add(ClientsTableRB);
        tablesRB.add(CarsTableRB);
        tablesRB.add(ProblemsRB);
    }

    //Adding options to the drop down
    private void AddToDropDown(JComboBox box) {
        box.addItem("ASC");
        box.addItem("DESC");
    }
}
