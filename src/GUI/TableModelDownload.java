package GUI;

import DownloadServise.Download;

import javax.swing.table.AbstractTableModel;
import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by 2 on 04.05.2016.
 */
public class TableModelDownload extends AbstractTableModel implements Observer {

    private String zagolowok[] = {"URL","Size","Process","Status"};
    private Class vauleClass[] = {URL.class,int.class,TableBarReander.class,Download.StatusDownload.class};
    private ArrayList<Download> list = new ArrayList<>();


    public   Download get(int i) {
        return list.get(i);

    }

    public void add(Download download) {
        list.add(download);
        download.addObserver(this);
        fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
    }

    public void remove(int r) {
        list.remove(r);
        fireTableRowsDeleted(r, r);
    }

    @Override
    public int getRowCount() {
        return list.size();

    }

    @Override
    public int getColumnCount() {
        return zagolowok.length;

    }

    @Override
    public String getColumnName(int column) {
        return zagolowok[column];

    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return vauleClass[columnIndex];

    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Download download = list.get(rowIndex);

        switch (columnIndex) {
            case 0:
               return download.getURL();
            case 1:
                return download.getSize();
            case 2:
                return download.getProgres();
            case 3:
                return download.getStatus();

        }


        return null;
    }

    @Override
    public void update(Observable o, Object arg) {
        int i = list.indexOf(o);
        fireTableRowsUpdated(i,i);


    }
}
