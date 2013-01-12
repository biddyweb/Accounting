package be.dafke.Accounting;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.BoxLayout;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import be.dafke.ParentFrame;
import be.dafke.RefreshableFrame;
import be.dafke.Accounting.Objects.Account;
import be.dafke.Accounting.Objects.Account.AccountType;
import be.dafke.Accounting.Objects.Accountings;

public class NewAccountGUI extends RefreshableFrame implements ActionListener, ListSelectionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JTextField name;
	private final JComboBox type;
	private final JButton add, delete, modifyName, modifyType;
	private final NewAccountDataModel model;
	private final JTable tabel;
//	private final AccountingGUIFrame parent;
	private final DefaultListSelectionModel selection;

	private static NewAccountGUI newAccountGUI = null;

	public static NewAccountGUI getInstance(ParentFrame parent) {
		if (newAccountGUI == null) {
			newAccountGUI = new NewAccountGUI(parent);
		}
		parent.addChildFrame(newAccountGUI);
		return newAccountGUI;
	}

	private NewAccountGUI(ParentFrame parent) {
		super("Create and modify accounts", parent);
		this.parent = parent;
		model = new NewAccountDataModel(/*parent*/);
		tabel = new JTable(model);
		tabel.setPreferredScrollableViewportSize(new Dimension(500, 200));
		selection = new DefaultListSelectionModel();
		selection.addListSelectionListener(this);
		tabel.setSelectionModel(selection);
		JScrollPane scrollPane = new JScrollPane(tabel);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		JPanel north = new JPanel();
		north.setLayout(new BoxLayout(north, BoxLayout.Y_AXIS));
		JPanel line1 = new JPanel();
		line1.add(new JLabel("Name:"));
		name = new JTextField(20);
		name.addActionListener(this);
		line1.add(name);
		JPanel line2 = new JPanel();
		line2.add(new JLabel("Type:"));
		type = new JComboBox(AccountType.values());
		line2.add(type);
		add = new JButton("Create new Account");
		add.addActionListener(this);
		line2.add(add);
		north.add(line1);
		north.add(line2);
		panel.add(north, BorderLayout.NORTH);
		JPanel south = new JPanel();
		modifyName = new JButton("Modify name");
		modifyType = new JButton("Modify type");
		delete = new JButton("Delete account");
		modifyName.addActionListener(this);
		modifyType.addActionListener(this);
		delete.addActionListener(this);
		modifyName.setEnabled(false);
		modifyType.setEnabled(false);
		delete.setEnabled(false);
		south.add(modifyName);
		south.add(modifyType);
		south.add(delete);
		panel.add(south, BorderLayout.SOUTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setContentPane(panel);
		pack();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == add || event.getSource() == name) {
			addAccount();
		} else if (event.getSource() == modifyName) {
			modifyName();
		} else if (event.getSource() == modifyType) {
			modifyType();
		} else if (event.getSource() == delete) {
			deleteAccount();
		}
		parent.repaintAllFrames();
	}

	private void deleteAccount() {
		int[] rows = tabel.getSelectedRows();
		int nrNotEmpty = 0;
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(this, "Select an account first");
		} else {
			for(int row : rows) {
				Account account = (Account) model.getValueAt(row, 0);
				if (account.saldo().compareTo(BigDecimal.ZERO) != 0) {
					nrNotEmpty++;
				} else {
					// TODO: close account ? = add trailer to XML-file
					// or better: move account to list "To be closed"
					// When saving files to XML, add trailer to closed accounts
					// and remove them from the Accounts object
					// Note: if account was never used: just remove.i
					Accountings.getCurrentAccounting().getAccounts().remove(account.getName());
					parent.repaintAllFrames();
				}
			}
			if (nrNotEmpty > 0) {
				if (nrNotEmpty == 1) {
					if (rows.length == 1) {
						JOptionPane.showMessageDialog(this, "The saldo of the account was not zero,"
								+ "so it could not be deleted.");
					} else {
						JOptionPane.showMessageDialog(this, "The saldo of 1 account was not zero,"
								+ "so it could not be deleted.");
					}
				} else {
					JOptionPane.showMessageDialog(this, "The saldo of " + nrNotEmpty
							+ " accounts were not zero, so they could not be deleted");
				}
			}
			parent.repaintAllFrames();
		}
	}

	private void modifyName() {
		int[] rows = tabel.getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(this, "Select an account first");
		} else {
			for(int row : rows) {
				Account account = (Account) model.getValueAt(row, 0);
				String oldName = account.getName();
				String newName = JOptionPane.showInputDialog("New name", account.getName()).trim();
				Accountings.getCurrentAccounting().getAccounts().rename(oldName, newName);
			}
			parent.repaintAllFrames();
		}
	}

	private void modifyType() {
		int[] rows = tabel.getSelectedRows();
		if (rows.length == 0) {
			JOptionPane.showMessageDialog(this, "Select an account first");
		} else {
			boolean singleMove;
			if (rows.length == 1) {
				singleMove = true;
			} else {
				int option = JOptionPane.showConfirmDialog(this, "Apply same type for all selected accounts?", "All",
						JOptionPane.YES_NO_OPTION);
				singleMove = (option == JOptionPane.YES_OPTION);
			}
			if (singleMove) {
				AccountType[] types = AccountType.values();
				int nr = JOptionPane.showOptionDialog(this, "Choose new type", "Change type",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types, null);
				for(int row : rows) {
					Account account = (Account) model.getValueAt(row, 0);
					account.setType(types[nr]);
				}
			} else {
				for(int row : rows) {
					Account account = (Account) model.getValueAt(row, 0);
					AccountType[] types = AccountType.values();
					int nr = JOptionPane.showOptionDialog(this, "Choose new type for " + account.getName(),
							"Change type", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, types,
							account.getType());
					account.setType(types[nr]);
				}
			}
			parent.repaintAllFrames();
			// parent.refresh();
		}
	}

	private void addAccount() {
		String newName = name.getText().trim();
		if (newName.equals("")) {
			JOptionPane.showMessageDialog(this, "Account name cannot be empty");
		} else {
			if (Accountings.getCurrentAccounting().getAccounts().containsKey(newName)) {
				JOptionPane.showMessageDialog(this, "Account name already exists");
			} else {
				Account account = new Account(newName, (AccountType) type.getSelectedItem());
				account.setAccounting(Accountings.getCurrentAccounting());
				Accountings.getCurrentAccounting().getAccounts().add(account);
				parent.repaintAllFrames();
			}
		}
		name.setText("");
	}

	/**
	 * Herlaadt de data van de tabel
	 * @see javax.swing.table.AbstractTableModel#fireTableDataChanged()
	 */
	@Override
	public void refresh() {
		model.fireTableDataChanged();
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			int[] rows = tabel.getSelectedRows();
			if (rows.length != 0) {
				delete.setEnabled(true);
				modifyName.setEnabled(true);
				modifyType.setEnabled(true);
			}
		}
	}

}