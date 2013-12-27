package be.dafke.BasicAccounting.GUI.Details;

import be.dafke.BasicAccounting.Objects.Account;
import be.dafke.BasicAccounting.Objects.Booking;
import be.dafke.BasicAccounting.Objects.Journal;
import be.dafke.BasicAccounting.Objects.Transaction;
import be.dafke.Utils.Utils;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class JournalDetailsDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Journal journal;
	private final String[] columnNames = {
			getBundle("Accounting").getString("NR"),
			getBundle("Accounting").getString("DATUM"),
			getBundle("Accounting").getString("REKENING"),
			getBundle("Accounting").getString("DEBET"),
			getBundle("Accounting").getString("CREDIT"),
			getBundle("Accounting").getString("OMSCHRIJVING") };
	private final Class[] columnClasses = { String.class, String.class, Account.class, BigDecimal.class,
			BigDecimal.class, String.class };

	public JournalDetailsDataModel(Journal journal) {
		this.journal = journal;
	}

// DE GET METHODEN
// ===============

	@Override
	public int getRowCount() {
		int size = 0;
        for(Transaction transaction : journal.getBusinessObjects()){
			size += transaction.getBookings().size();
		}
		return size;
	}

	@Override
	public int getColumnCount() {
		return 6;
	}

	@Override
	public String getColumnName(int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col) {
        ArrayList<Booking> boekingen = new ArrayList<Booking>();
        for(Transaction transaction : journal.getBusinessObjects()){
            boekingen.addAll(transaction.getBookings());
        }
		Booking boeking = boekingen.get(row);
        boolean first = (boeking == boeking.getTransaction().getBookings().get(0));
        if (col == 0) {
            if(first){
                return boeking.getTransaction().getAbbreviation() + boeking.getTransaction().getId();
            } else return "";
        } else if (col == 1) {
            if(first){
                return Utils.toString(boeking.getTransaction().getDate());
            } else return "";
        } else if (col == 2) {
            return boeking.getAccount();
        } else if (col == 3) {
            if (boeking.getMovement().isDebit()) return boeking.getMovement().getAmount();
            return "";
        } else if (col == 4) {
            if (!boeking.getMovement().isDebit()) return boeking.getMovement().getAmount();
            return "";
        } else{
            if(first){
                return boeking.getTransaction().getDescription();
            } else return "";
        }
    }

	@Override
	public Class getColumnClass(int col) {
		return columnClasses[col];
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return (col == 1 || col == 5);
	}

// DE SET METHODEN
// ===============

	@Override
	public void setValueAt(Object value, int row, int col) {
        ArrayList<Booking> boekingen = new ArrayList<Booking>();
        for(Transaction transaction : journal.getBusinessObjects()){
            boekingen.addAll(transaction.getBookings());
        }
        Booking booking = boekingen.get(row);
        Transaction transaction = booking.getTransaction();
		if (col == 1) {
			Calendar oudeDatum = transaction.getDate();
			Calendar nieuweDatum = Utils.toCalendar((String) value);
			if (nieuweDatum != null){
                journal.removeBusinessObject(transaction);
                transaction.setDate(nieuweDatum);
                journal.addBusinessObject(transaction);
            }
			else setValueAt(Utils.toString(oudeDatum), row, col);
		} else if (col == 5) {
			transaction.setDescription((String) value);
		}
		// ouder.repaint();
		// parent.repaintAllFrames();
//		super.refresh();
	}
}