package be.dafke.BasicAccounting.GUI.JournalManagement;

import be.dafke.BasicAccounting.Objects.Accounting;
import be.dafke.BasicAccounting.Objects.Journal;

import javax.swing.table.AbstractTableModel;

import static java.util.ResourceBundle.getBundle;

public class NewJournalDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { getBundle("Accounting").getString("JOURNAL_NAME"),
            getBundle("Accounting").getString("TYPE"), getBundle("Accounting").getString("NEXT_INDEX") };
	private final Class[] columnClasses = { Journal.class, String.class, Integer.class };
	private final Accounting accounting;

	public NewJournalDataModel(Accounting accounting) {
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
}
