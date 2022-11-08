package chatClient.presentation;

import chatProtocol.User;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModel extends AbstractTableModel implements javax.swing.table.TableModel {
    List<User> rows;
    int[] cols;

    public TableModel(int[] cols, List<User> rows) {
        initColNames();
        this.cols = cols;
        this.rows = rows;
    }

    public int getColumnCount() {
        return cols.length;
    }

    public String getColumnName(int col) {
        return colNames[cols[col]];
    }

    public Class<?> getColumnClass(int col) {
        switch (cols[col]) {
            default:
                return super.getColumnClass(col);
        }
    }

    public int getRowCount() {
        return rows.size();
    }

    public Object getValueAt(int row, int col) {
        User usuario = rows.get(row);
        switch (cols[col]) {
            case NOMBRE:
                return usuario.getNombre();

            default:
                return "";
        }
    }

    public static final int NOMBRE = 0;

    String[] colNames = new String[1];

    private void initColNames() {
        colNames[NOMBRE] = "Nombre";
    }
}
