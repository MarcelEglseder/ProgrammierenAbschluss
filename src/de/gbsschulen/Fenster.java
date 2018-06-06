package de.gbsschulen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class Fenster extends JFrame {

    private JPanel jpNorth, jpSouth;
    private JComboBox<Gegenstand> jComboBox;
    private JTextField jtxtAnzahl;
    private JButton jbtnEintragen, jbtnLöschen;
    private JLabel jlGesamtpreis;
    private JMenuBar jMenuBar;
    private JMenu jmenuDatei;
    private JMenuItem jmiNeu, jmiSpeichern, jmiBeenden;
    private JScrollPane jScrollPane;
    
    private JFileChooser jFileChooser;

    private  JTable jTable;
    private MeinTableModel meinTableModel;

    private DAO dao;

    public Fenster() throws HeadlessException, SQLException {
        super("Einkaufsliste");
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.dao = new DAO();
        this.initComponents();
        this.initMenu();
        this.initEvents();
        this.setSize(700, 400);
        this.setVisible(true);
    }

    private void initEvents() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                beenden();
            }
        });
        jmiBeenden.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                beenden();
            }
        });

        jbtnEintragen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eintragen();
            }
        });

        jmiNeu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                neu();
            }
        });
        
        jmiSpeichern.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speichern();
            }
        });

        jbtnLöschen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loeschen();
            }
        });
}

    private void loeschen() {
        int selectedRow = jTable.getSelectedRow();
        if (selectedRow >= 0) {
            String bezeichnung = (String) meinTableModel.getValueAt(selectedRow, 1);
            anzeigeAktualisieren();
        }
    }

    private void speichern() {
        int result = jFileChooser.showSaveDialog(this);
        if (result == JFileChooser.CANCEL_OPTION) {
            return;
        }
        File file = jFileChooser.getSelectedFile();
        try {
            meinTableModel.speichern(file);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Datei kann nicht gespeichert werden", "Fehler" , JOptionPane.WARNING_MESSAGE);
        }

        JOptionPane.showMessageDialog(this,"Datei erfolgreich gespeichert", "Erfolg", JOptionPane.INFORMATION_MESSAGE);
        this.setTitle("Einkaufsliste:" + file.getName());

    }

    private void anzeigeAktualisieren() {
        jlGesamtpreis.setText(String.valueOf(meinTableModel.getGesamtPreis()));
    }


    private void neu() {
        this.meinTableModel = new MeinTableModel();
        jTable.setModel(meinTableModel);
        anzeigeAktualisieren();
        }

    private void eintragen() {
                String eingabe = jtxtAnzahl.getText();
                int anzahl = 0;
                try {
                     anzahl = Integer.parseInt(eingabe);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "falsche Eingabe", "Fehler", JOptionPane.WARNING_MESSAGE);
                    return;
                    }
                    Gegenstand gegenstand = (Gegenstand) jComboBox.getSelectedItem();
                Gegenstand neuerGegenstand = new Gegenstand(gegenstand.getBezeichnung(), gegenstand.getPreis(), gegenstand.getAnzahl());
                if (anzahl > 0) {
                    neuerGegenstand.setAnzahl(anzahl);
                    meinTableModel.hinzufuegen(neuerGegenstand);
                    jComboBox.setSelectedIndex(0);
                    jtxtAnzahl.setText("");
                    anzeigeAktualisieren();
                }

    }


    private void beenden() {
        int result =  JOptionPane.showConfirmDialog(this, "Wollen Sie wirklich beenden?", "Beenden?", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION) {
            System.exit(NORMAL);
        }

    }

    private void initMenu() {
        jMenuBar = new JMenuBar();
        jmenuDatei = new JMenu("Datei");
        jmiNeu = new JMenuItem("Neu");
        jmiSpeichern = new JMenuItem("Speichern...");
        jmiBeenden = new JMenuItem("Beenden");

        jmenuDatei.add(jmiNeu);
        jmenuDatei.add(jmiSpeichern);
        jmenuDatei.add(jmiBeenden);

        jMenuBar.add(jmenuDatei);

        this.setJMenuBar(jMenuBar);
    }

    private void initComponents() {
        jFileChooser = new JFileChooser("D:");
        jpNorth = new JPanel();
        jComboBox = new JComboBox<>();

        befuelleCombobox();
        jtxtAnzahl = new JTextField(3);
        jbtnEintragen = new JButton("Eintragen");
        jbtnLöschen = new JButton("Löschen");
        jpNorth.add(jComboBox);
        jpNorth.add(new Label("Anzahl"));
        jpNorth.add(jtxtAnzahl);
        jpNorth.add(jbtnEintragen);
        jpNorth.add(jbtnLöschen);

        meinTableModel = new MeinTableModel();
        jTable = new JTable(meinTableModel);
        jScrollPane = new JScrollPane(jTable);


        jpSouth = new JPanel();
        jpSouth.setLayout(new FlowLayout(FlowLayout.RIGHT));
        jpSouth.add(new JLabel("Gestamtpreis:"));
        jlGesamtpreis = new JLabel("0.00");
        jpSouth.add(jlGesamtpreis);

        this.add(jpNorth, BorderLayout.NORTH);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(jpSouth, BorderLayout.SOUTH);

    }

    private void befuelleCombobox() {
        jComboBox.addItem(new Gegenstand("Bitte auswählen...", 0, 0));
        try {
            dao.findeArtikel("%%");
            for (Gegenstand gegenstand : dao.getGegenstaende()) {
                jComboBox.addItem(gegenstand);
            }
            dao.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                dao.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            new Fenster();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
