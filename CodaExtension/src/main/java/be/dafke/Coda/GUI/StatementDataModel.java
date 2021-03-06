package be.dafke.Coda.GUI;

import be.dafke.Coda.Objects.CounterParty;
import be.dafke.Coda.Objects.Statement;
import be.dafke.Coda.Objects.Statements;
import be.dafke.ComponentModel.RefreshableTableModel;
import be.dafke.Utils.Utils;

import java.math.BigDecimal;

public class StatementDataModel extends RefreshableTableModel<Statement> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String[] columnNames = { "Name", "Date", "D/C", "Amount", "CounterParty",
			"TransactionCode", "Communication" };
	private final Class[] columnClasses = { String.class, String.class, String.class, BigDecimal.class,
			CounterParty.class, String.class, String.class };
    private final Statements statements;

	public StatementDataModel(Statements statements) {
		this.statements = statements;
	}

	// DE GET METHODEN
	// ===============
	@Override
	public Object getValueAt(int row, int col) {
		Statement m = (Statement)statements.getBusinessObjects().get(row);
		if (col == 0) {
			return m.getName();
		} else if (col == 1) {
			return Utils.toString(m.getDate());
		} else if (col == 2) {
			return (m.isDebit()) ? "(D) -" : "(C) +";
		} else if (col == 3) {
			return m.getAmount();
		} else if (col == 4) {
			return m.getCounterParty();
		} else if (col == 5) {
			return m.getTransactionCode();
		} else if (col == 6) {
			return m.getCommunication();
		} else return "";
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return statements.getBusinessObjects().size();
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
	public Statement getObject(int row, int col) {
		return null;
	}

	@Override
	public int getRow(Statement statement) {
		return 0;
	}
}