package be.dafke.BasicAccounting.GUI.MainWindow;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.ComponentModel.RefreshableTableModel;

import java.math.BigDecimal;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */

public class JournalDataModel extends RefreshableTableModel<Booking> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String[] columnNames = { getBundle("Accounting").getString("DEBIT"),
			getBundle("Accounting").getString("CREDIT"),
			getBundle("Accounting").getString("D"),
			getBundle("Accounting").getString("C") };
	Class[] columnClasses = { Account.class, Account.class, BigDecimal.class, BigDecimal.class };

    private Transaction transaction;

// DE GET METHODEN
// ===============
	@Override
	public Object getValueAt(int row, int col) {
		Booking booking = transaction.getBusinessObjects().get(row);
		if (booking.getBusinessObjects().get(0).isDebit()) {
			if (col == 0) {
				return booking.getAccount();
			}
			if (col == 2) {
				return booking.getBusinessObjects().get(0).getAmount();
			}
			return null;
		}// else credit
		if (col == 1) {
			return booking.getAccount();
		}
		if (col == 3) {
			return booking.getBusinessObjects().get(0).getAmount();
		}
		return null;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
        if(transaction == null){
            return 0;
        }
		return transaction.getBusinessObjects().size();
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
//        data[row][col] = value;
	}

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

	@Override
	public Booking getObject(int row, int col) {
		return transaction.getBusinessObjects().get(row);
	}

	@Override
	public int getRow(Booking booking) {
		return 0;
	}
}