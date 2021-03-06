package be.dafke.BasicAccounting.GUI.JournalManagement;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.ComponentModel.RefreshableTableModel;

import static java.util.ResourceBundle.getBundle;

/**
 * User: david
 * Date: 5-1-14
 * Time: 16:22
 */
public class JournalManagementTableModel extends RefreshableTableModel<Journal> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final String[] columnNames = { getBundle("Accounting").getString("JOURNAL_NAME"),
            getBundle("Accounting").getString("TYPE"), getBundle("Accounting").getString("NEXT_INDEX") };
    private final Class[] columnClasses = { Journal.class, String.class, Integer.class };
    private final Accounting accounting;

    public JournalManagementTableModel(Accounting accounting) {
        this.accounting = accounting;
    }

    @Override
    public int getColumnCount() {
        return columnClasses.length;
    }

    @Override
    public int getRowCount() {
        return accounting.getJournals().getBusinessObjects().size();
    }

    @Override
    public Object getValueAt(int row, int col) {
        Journal journal = accounting.getJournals().getBusinessObjects().get(row);
        if (col == 0) {
            return journal;
        } else if (col == 1) {
            return journal.getType();
        } else if (col == 2) {
            return journal.getId();
        } else return null;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Class getColumnClass(int col) {
        return columnClasses[col];
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }

    // DE SET METHODEN
    // ===============
    @Override
    public void setValueAt(Object value, int row, int col) {
    }

    @Override
    public Journal getObject(int row, int col) {
        return null;
    }

    @Override
    public int getRow(Journal o) {
        return 0;
    }
}