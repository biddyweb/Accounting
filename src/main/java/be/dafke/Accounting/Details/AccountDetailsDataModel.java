package be.dafke.Accounting.Details;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.swing.table.AbstractTableModel;

import be.dafke.ParentFrame;
import be.dafke.Utils;
import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Booking;

/**
 * @author David Danneels
 */
public class AccountDetailsDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Account rekening;
	private final String[] columnNames = {
			java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("NR"),
			java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("DATUM"),
			java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("DEBET"),
			java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("CREDIT"),
			java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("OMSCHRIJVING") };
	private final Class[] columnClasses = { String.class, String.class, BigDecimal.class, BigDecimal.class,
			String.class };
	private final ParentFrame parent;

	public AccountDetailsDataModel(Account account, ParentFrame parent) {
		this.parent = parent;
		rekening = account;
	}

// DE GET METHODEN
// ===============

	@Override
	public int getRowCount() {
		return rekening.getBookings().size();
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
		Booking boeking = rekening.getBookings().get(row);
		if (col == 0) {
			return boeking.getAbbreviation() + boeking.getId();
		} else if (col == 1) {
			return Utils.toString(boeking.getDate());
		} else if (col == 2) {
			if (boeking.isDebet()) return boeking.getAmount();
			return "";
		} else if (col == 3) {
			if (!boeking.isDebet()) return boeking.getAmount();
			return "";
		} else return boeking.getDescription();
	}

	@Override
	public Class getColumnClass(int col) {
		return columnClasses[col];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return (col == 1 || col == 4);
	}

// DE SET METHODEN
// ===============

	@Override
	public void setValueAt(Object value, int row, int col) {
		Booking boeking = rekening.getBookings().get(row);
		if (col == 1) {
			Calendar oudeDatum = boeking.getDate();
			Calendar nieuweDatum = Utils.toCalendar((String) value);
			if (nieuweDatum != null) boeking.getTransaction().setDate(nieuweDatum);
			else setValueAt(Utils.toString(oudeDatum), row, col);
		} else if (col == 4) {
			boeking.getTransaction().setDescription((String) value);
		}
		parent.repaintAllFrames();
	}
}