//Custom combobox item to get the id and the value from a table in sql
public class ComboBoxItem {
    //the index of the item
    private int key;
    //the value of the item
    private String value;

    public ComboBoxItem(int key,String value){
        this.key = key;
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public int getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
