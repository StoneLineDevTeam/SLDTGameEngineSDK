package net.sldt_team.animationMaker.gameEngineToAWT;

import net.sldt_team.animationMaker.Main;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.Map;

public class PropertyTableModel extends AbstractTableModel {

    private String[] columnNames = {"Property", "Value"};
    private Map<String, Object> propertyMap;

    public PropertyTableModel(){
        propertyMap = new HashMap<String, Object>();
    }

    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    public int getRowCount() {
        return propertyMap.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int row, int col) {
        int i = 0;
        for (Map.Entry e : propertyMap.entrySet()){
            if (i == row){
                String key = (String) e.getKey();
                Object value = e.getValue();
                if (col == 0){
                    return key;
                } else if (col == 1){
                    return value;
                }
            }
            i++;
        }
        return null;
    }

    public boolean isCellEditable(int row, int col) {
        if (col == 0){
            return false;
        } else if (col == 1) {
            return true;
        }
        return false;
    }

    public void setValueAt(Object value, int row, int col) {
        String keyToUse = null;

        int i = 0;
        for (Map.Entry e : propertyMap.entrySet()){
            if (i == row){
                String key = (String) e.getKey();
                keyToUse = key;
            }
            i++;
        }

        propertyMap.put(keyToUse, value);
        fireTableChanged(new TableModelEvent(this, row));
    }

    /**
     * Removes a property from Table
     */
    public void removeProperty(String propertyName){
        if (propertyMap.containsKey(propertyName)) {
            propertyMap.remove(propertyName);
            fireTableDataChanged();
        } else {
            Main.log.warning("Failed to remove property : property does not existing !");
        }
    }

    /**
     * Returns a property value from the given property name
     */
    public Object getPropertyValue(String propertyName){
        return propertyMap.get(propertyName);
    }

    /**
     * Adds a new property to the table
     */
    public void addProperty(String propertyName, Object var){
        if (!propertyMap.containsKey(propertyName)) {
            propertyMap.put(propertyName, var);
            fireTableDataChanged();
        } else {
            Main.log.warning("Failed to add new property : property already exists !");
        }
    }
}
