import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;


public class UpdateForm extends JFrame {
    //main panel
    private JPanel UpdatePanel;
    //drop downs
    private JComboBox ProblemDropDown;
    private JComboBox CarDropDown;
    private JComboBox ClientDropDown;
    private JComboBox CarProblemDrowDown;
    private JComboBox ClientCarDropDown;
    //buttons
    private JButton backButton;
    private JButton updateTableButton;
    //update panels
    private JPanel UpdateClientPanel;
    private JPanel UpdateProblemPanel;
    private JPanel UpdateCarPanel;
    //radio buttons
    private JRadioButton updateClientsRadioButton;
    private JRadioButton updateCarsRadioButton;
    private JRadioButton updateProblemsRadioButton;
    //Text fields
    private JTextField ProbelmNameTF;
    private JTextField ProblemDurationTF;
    private JTextField ProblemPriceTF;
    private JTextField CarNameTF;
    private JTextField CarModelTF;
    private JTextField ClientFnameTF;
    private JTextField ClientLnameTF;
    //Message box
    private MessageBox box;
    //Tables
    private MyTable Clients;
    private MyTable Cars;
    private MyTable Problems;

    public UpdateForm() {
        Init();
        ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                ClearFields();
                DisplaySelectedPanel();
                FillPanelInfo();
            }
        };
        updateCarsRadioButton.addActionListener(listener);
        updateProblemsRadioButton.addActionListener(listener);
        updateClientsRadioButton.addActionListener(listener);

        updateTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                UpdateTable();
                ClearFields();
            }
        });
        ActionListener listener1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                FillPanelInfo();
            }
        };
        ClientDropDown.addActionListener(listener1);
        CarDropDown.addActionListener(listener1);
        ProblemDropDown.addActionListener(listener1);
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                dispose();
                MainForm form = new MainForm();
            }
        });
    }
    //Initial form data
    private void Init() {
        //Adding the panel to the form
        this.add(UpdatePanel);
        //Setting the properties for the form
        this.setTitle("Car service-update");
        this.setSize(650, 350);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        SettingMyTables();
        ConnectRadioButtons();
        DisplaySelectedPanel();
        FillAllComboBoxes();
        FillPanelInfo();
    }

    //Gets the selected table and calls the method to get the query
    private void UpdateTable() {
        MyTable table = CheckSelectedTable();
        GetUpdateQuery(table);
    }

    private boolean CheckDropDownInput(JComboBox dropDown) {
        if (dropDown.getSelectedIndex()==0){
            MessageBox.Message("Selected drop down value is wrong");
            return false;
        }
        return true;
    }

    //finds out which the selected table
    private void GetUpdateQuery(MyTable table) {
        if (table.getTableName().equals(Columns.GetClientsTableName())){
            if (!CheckDropDownInput(ClientDropDown)||!CheckDropDownInput(ClientCarDropDown)){
                return;
            }
            CreateUpdateQuery(table,ClientDropDown,ClientCarDropDown,ClientFnameTF,ClientLnameTF);
        }else if (table.getTableName().equals(Columns.GetCarsTableName())){
            if (!CheckDropDownInput(CarDropDown)||!CheckDropDownInput(CarProblemDrowDown)){
                return;
            }
            CreateUpdateQuery(table,CarDropDown,CarProblemDrowDown,CarNameTF,CarModelTF);
        }else if (table.getTableName().equals(Columns.GetProblemTableName())){
            if (!CheckDropDownInput(ProblemDropDown)){
                return;
            }
            CreateUpdateQuery(table,ProblemDropDown,ProbelmNameTF,ProblemDurationTF,ProblemPriceTF);
        }
    }

    //creates the update query
    private void CreateUpdateQuery(MyTable table,JComboBox itemToChange,JComboBox selectedValue,JTextField field1,JTextField field2){
        String query = "";
        int id = GetKeyFromComboBox(itemToChange);
        int value = GetKeyFromComboBox(selectedValue);
        query+=table.CreateUpdateQuery(field1.getName(),field1.getText(),query);
        query+=table.CreateUpdateQuery(field2.getName(),field2.getText(),query);
        query+=table.CreateUpdateQuery(selectedValue.getName(),value,query);
        query=table.GetUpdateQuery(query,id);
        UpdateSQLTable(query);
    }
    //creates the update query
    private void CreateUpdateQuery(MyTable table,JComboBox itemToChange,JTextField field1,JTextField field2,JTextField field3){
        String query = "";
        int id = GetKeyFromComboBox(itemToChange);
        query+=table.CreateUpdateQuery(field1.getName(),field1.getText(),query);
        query+=table.CreateUpdateQuery(field3.getName(),field3.getText(),query);
        query+=table.CreateUpdateQuery(field2.getName(),field2.getText(),query);
        query=table.GetUpdateQuery(query,id);
        UpdateSQLTable(query);
    }
    // FOR TONI//
    //SQL REQUIRED
    private void UpdateSQLTable(String query) {
        //this is the query,just run it
        System.out.println(query);
        DBConnection.update(query);
        FillAllComboBoxes();
    }

    //Get the selected table and fills the tables panel
    private void FillPanelInfo() {
        MyTable table = CheckSelectedTable();
        CheckDropDowns(table);
    }
    private void CheckDropDowns(MyTable table) {
        if (ProblemDropDown.getSelectedIndex()==0&&CarDropDown.getSelectedIndex()==0&&ClientDropDown.getSelectedIndex()==0){
            return;
        }
        FillTextFields(table);
    }

    //finds out witch is the selected table
    private void FillTextFields(MyTable table) {
        if (table.getTableName().equals(Columns.GetClientsTableName())){
            FillFieldsAndDropDown(table,ClientDropDown,ClientFnameTF,ClientLnameTF,ClientCarDropDown);
        }else if (table.getTableName().equals(Columns.GetCarsTableName())){
            FillFieldsAndDropDown(table,CarDropDown,CarNameTF,CarModelTF,CarProblemDrowDown);
        }else if (table.getTableName().equals(Columns.GetProblemTableName())){
            FillTextFields(table,ProblemDropDown,ProbelmNameTF,ProblemDurationTF,ProblemPriceTF);
        }
    }
    //FOR TONI//
    //SQL required
    private void FillTextFields(MyTable table,JComboBox getIndex,JTextField field1,JTextField field2,JTextField field3){
//        Object comboItem = getIndex.getSelectedItem();
//        int index = ((ComboBoxItem)comboItem).getKey();
        int index = GetKeyFromComboBox(getIndex);
        String query = table.SelectWithoutId(index);
        //run the query and fill the text in the textFields with it
        System.out.println(query);
        ArrayList<String> columns = DBConnection.SelectColumns(query);
        if (columns.size()>=3){
            field1.setText(columns.get(0));
            field2.setText(columns.get(2));
            field3.setText(columns.get(1));
        }
    }
    //FOR TONI//
    //SQL required
    private void FillFieldsAndDropDown(MyTable table,JComboBox getIndex,JTextField field1,JTextField field2,JComboBox dropDown) {
//        Object comboItem = getIndex.getSelectedItem();
//        int index = ((ComboBoxItem)comboItem).getKey();
        int index = GetKeyFromComboBox(getIndex);
        String query = table.SelectWithoutId(index);
        //run the query and fill the text in the textFields and comboBox with it
        System.out.println(query);
        ArrayList<String> columns = DBConnection.SelectColumns(query);
        if (columns.size()>=3){
            field1.setText(columns.get(0));
            field2.setText(columns.get(1));
            int id = Integer.parseInt(columns.get(2));
            dropDown.setSelectedIndex(FindElementInDropDown(dropDown,id));
            System.out.println(id);
            System.out.println(dropDown.getItemCount());
        }

    }

    private int FindElementInDropDown(JComboBox dropDown,int id) {
        int elementId = 0;
        for (int i = 1;i<dropDown.getItemCount();i++){
             Object item = dropDown.getItemAt(i);
             int temp = ((ComboBoxItem)item).getKey();
             if(temp==id){
                 elementId = i;
                 return elementId;
             }
        }
            return elementId;
    }

    //setting the tables
    private void SettingMyTables(){
        Clients = new MyTable(Columns.GetClientsTableName(),Columns.GetClientsColumns());
        Cars = new MyTable(Columns.GetCarsTableName(),Columns.GetCarColumns());
        Problems = new MyTable(Columns.GetProblemTableName(),Columns.GetProblemsColumns());
        SetTextFieldNames();
    }
    //Setting the textFields names to the table columns names
    private void SetTextFieldNames() {
        //Client table columns
        ClientFnameTF.setName(Clients.getColumnName(1));
        ClientLnameTF.setName(Clients.getColumnName(2));
        ClientCarDropDown.setName(Clients.getColumnName(3));
        //Car table columns
        CarNameTF.setName(Cars.getColumnName(1));
        CarModelTF.setName(Cars.getColumnName(2));
        CarProblemDrowDown.setName(Cars.getColumnName(3));
        //Problem table columns
        ProbelmNameTF.setName(Problems.getColumnName(1));
        ProblemDurationTF.setName(Problems.getColumnName(3));
        ProblemPriceTF.setName(Problems.getColumnName(2));
    }
    private void ClearFields() {
       ClientDropDown.setSelectedIndex(0);
       ClientCarDropDown.setSelectedIndex(0);
       CarDropDown.setSelectedIndex(0);
       CarProblemDrowDown.setSelectedIndex(0);
       ProblemDropDown.setSelectedIndex(0);
       ProbelmNameTF.setText("");
       ProblemDurationTF.setText("");
       ProblemPriceTF.setText("");
       CarNameTF.setText("");
       CarModelTF.setText("");
       ClientFnameTF.setText("");
       ClientLnameTF.setText("");
    }

    //calls the method to make  all panels invisible, checks which is the selected panel and makes it visible
    private void DisplaySelectedPanel() {
        DisableAllPanels();
        CheckSelectedPanel();
    }

    //checks which is the selected panel and makes it visible
    private void CheckSelectedPanel() {
        if (updateClientsRadioButton.isSelected()) {
            UpdateClientPanel.setVisible(true);
        } else if (updateCarsRadioButton.isSelected()) {
            UpdateCarPanel.setVisible(true);
        } else if (updateProblemsRadioButton.isSelected()){
            UpdateProblemPanel.setVisible(true);
        }

    }

    //returns the selected table,depending on the selected radio button
    private MyTable CheckSelectedTable() {
        if (updateClientsRadioButton.isSelected()){
            return Clients;
        }else if (updateCarsRadioButton.isSelected()){
            return Cars;
        }else if (updateProblemsRadioButton.isSelected()){
            return Problems;
        }
        return null;
    }

    //make  all panels invisible
    private void DisableAllPanels() {
        UpdateProblemPanel.setVisible(false);
        UpdateCarPanel.setVisible(false);
        UpdateClientPanel.setVisible(false);
    }

    //connects the radio buttons
    private void ConnectRadioButtons(){
        ButtonGroup RBgroup = new ButtonGroup();
        RBgroup.add(updateCarsRadioButton);
        RBgroup.add(updateClientsRadioButton);
        RBgroup.add(updateProblemsRadioButton);
    }

    private void FillAllComboBoxes(){
        FillComboBox(Clients,ClientDropDown);
        FillComboBox(Cars,ClientCarDropDown);
        FillComboBox(Cars,CarDropDown);
        FillComboBox(Problems,CarProblemDrowDown);
        FillComboBox(Problems,ProblemDropDown);
    }

    //gets the key from the ComboBoxItem
    private int GetKeyFromComboBox(JComboBox box){
        Object obj = box.getSelectedItem();
        Object key = ((ComboBoxItem)obj).getKey();
        return (int)key;
    }
    //FOR TONI
    //SQL REQUIRED//
    private void FillComboBox(MyTable table,JComboBox combo){
        //This is the required query
        String query = table.SelectTwoColumns(0,1);
        System.out.println(query);
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        ComboBoxItem select = new ComboBoxItem(0,"Select");
        model.addElement(select);
        model =DBConnection.fillDropDown(model,query);
        try{
            combo.setModel(model);
        }catch(Exception e){
            MessageBox.Message("Database is empty");
        }
    }

}
