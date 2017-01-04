package GUI;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by 2 on 04.05.2016.
 */
public class TableBarReander extends JProgressBar implements TableCellRenderer {

    public TableBarReander(int min, int max) {
        super(min, max);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setValue(Integer.valueOf(String.valueOf(value)));
        return this;
    }
}
