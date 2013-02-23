package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Coda.CounterParty;
import be.dafke.Accounting.Objects.Coda.Movement;
import be.dafke.Accounting.Objects.Coda.Movements;
import be.dafke.Utils;

import javax.swing.table.AbstractTableModel;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;

public class GenericMovementDataModel extends AbstractTableModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Statement", "Sequence", "Date", "D/C", "Amount", "CounterParty",
			"TransactionCode", "Communication" };
	private final Class[] columnClasses = { String.class, String.class, Calendar.class, String.class, BigDecimal.class,
			CounterParty.class, String.class, String.class };
	private Movement singleMovement;
	private final Accountings accountings;
    private SearchOptions searchOptions;

	public GenericMovementDataModel(SearchOptions searchOptions, Accountings accountings) {
		this.accountings = accountings;
        this.searchOptions = searchOptions;
	}

	public void setSingleMovement(Movement movement) {
		singleMovement = movement;
		fireTableDataChanged();
	}

	public void switchCounterParty(CounterParty newCounterparty) {
        searchOptions.setCounterParty(newCounterparty);
		fireTableDataChanged();
	}

	// DE GET METHODEN
	// ===============
	@Override
	public Object getValueAt(int row, int col) {
		Movement m = getAllMovements().get(row);
        if (col == 0) {
			return m.getStatementNr();
		} else if (col == 1) {
			return m.getSequenceNr();
		} else if (col == 2) {
			return Utils.toString(m.getDate());
		} else if (col == 3) {
			return (m.isDebit()) ? "D" : "C";
		} else if (col == 4) {
			return m.getAmount();
		} else if (col == 5) {
			CounterParty c = m.getCounterParty();
			if (c == null) c = m.getTmpCounterParty();
			return c;
		} else if (col == 6) {
			return m.getTransactionCode();
		} else if (col == 7) {
			return m.getCommunication();
		} else return "";
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return getAllMovements().size();
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

	public ArrayList<Movement> getAllMovements() {
        if (accountings == null || accountings.getCurrentAccounting() == null) {
            return new ArrayList<Movement>();
        }
        if (singleMovement != null) {
			ArrayList<Movement> result = new ArrayList<Movement>();
			result.add(singleMovement);
			return result;
		}
		Movements movements = accountings.getCurrentAccounting().getMovements();
		return movements.getMovements(searchOptions);
	}
}