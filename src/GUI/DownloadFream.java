package GUI;

import DownloadServise.Download;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.PrinterException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 2 on 04.05.2016.
 */
public class DownloadFream extends JFrame{

    private JPanel panelZagolovok = new JPanel();
    private JPanel panelTable = new JPanel();
    private JPanel panelButton = new JPanel();
    private JLabel label = new JLabel("Address URL");
    private JTextField textFieldURL = new JTextField(30);
    private JTable table;
    private TableModelDownload tableModelDownload = new TableModelDownload();
    private Download downoladSelected;
    private JButton buttonAdd = new JButton("Додати");
    private JButton   buttonStart= new JButton("Розпочати");
    private JButton  buttonStop= new JButton("Зупинити");
    private JButton  buttonPause= new JButton("Пауза");
    private JButton  buttonClose = new JButton("Видалити");


    private boolean isRemove = false;


    public DownloadFream(String title) throws HeadlessException {
        super(title);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(400, 100, 400, 400);

        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        textFieldURL.setText("URL");
        textFieldURL.setSelectionStart(0);
        textFieldURL.setSelectionEnd(3);

        startGUI();
        pack();
    }

    public   void startGUI() {

        panelButton.setLayout(new GridLayout(1,4));

        table=new JTable(tableModelDownload);
        table.setRowHeight((int)(new TableBarReander(0, 100).getPreferredSize().getHeight()));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setDefaultRenderer(TableBarReander.class, new TableBarReander(0, 100));

        panelZagolovok.add(label);
        panelZagolovok.add(textFieldURL);

        panelTable.setLayout(new BorderLayout());
        JScrollPane jScrollPane = new JScrollPane(table);
        jScrollPane.setPreferredSize(new Dimension(600,300));
        panelTable.add(jScrollPane,BorderLayout.CENTER);
        panelZagolovok.add(buttonAdd);
        panelButton.add(buttonClose);
        panelButton.add(buttonPause);
        panelButton.add(buttonStart);
        panelButton.add(buttonStop);

        panelTable.setBorder(BorderFactory.createTitledBorder("Загрузка"));

        add(panelZagolovok, BorderLayout.NORTH);
        add(panelTable, BorderLayout.CENTER);
        add(panelButton, BorderLayout.SOUTH);

        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addDownload();
            }
        });
    }

    private void  addDownload() {
        String strUrl = textFieldURL.getText();

        if (strUrl != null || strUrl != "") {

            try {
                tableModelDownload.add(new Download(new URL(strUrl)));
                textFieldURL.setText("URL");
            } catch (MalformedURLException e) {
                JOptionPane.showMessageDialog(null,"Помилка URL.Недійсний запит на скачування.Перевірте дані.");
            }

        }else JOptionPane.showMessageDialog(null,"Помилка у адресі URL.Перевірте адресу....");

    }

    private void tabelUpdate() {

        if (downoladSelected != null) {

            downoladSelected.deleteObserver(tableModelDownload);

            if (!isRemove) {
                downoladSelected = tableModelDownload.get(table.getSelectedRow());
                downoladSelected.addObserver(tableModelDownload);

            }
        }

    }



}
