package MyClass;

import DownloadServise.Download;
import GUI.DownloadFream;

import javax.swing.*;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Created by 2 on 04.05.2016.
 */
public class Main {
    public static void main(String[] args) {


        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
             DownloadFream jFrame = new DownloadFream("MyTorent");
                jFrame.setVisible(true);
            }
        });


    }
}
