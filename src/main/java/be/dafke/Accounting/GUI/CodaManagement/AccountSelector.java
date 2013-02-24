package be.dafke.Accounting.GUI.CodaManagement;

import be.dafke.Accounting.GUI.MainWindow.AccountingMenuBar;
import be.dafke.Accounting.Objects.Accounting.Account;
import be.dafke.Accounting.Objects.Accounting.Accountings;
import be.dafke.Accounting.Objects.Accounting.Accounts;
import be.dafke.RefreshableComponent;
import be.dafke.RefreshableDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AccountSelector extends RefreshableDialog implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JButton create, ok;
	private Account account;
	private final JComboBox<Account> combo;
    private final DefaultComboBoxModel<Account> model;
	private final Accountings accountings;

	public AccountSelector(Accountings accountings) {
		this.accountings = accountings;
		setTitle("Select Account");
        model = new DefaultComboBoxModel<Account>();
		combo = new JComboBox<Account>(model);
		combo.addActionListener(this);
		create = new JButton("Add accounts ...");
		create.addActionListener(this);
		ok = new JButton("Ok (Close popup)");
		ok.addActionListener(this);
		JPanel innerPanel = new JPanel(new BorderLayout());
		JPanel panel = new JPanel();
		panel.add(combo);
		panel.add(create);
		innerPanel.add(panel, BorderLayout.CENTER);
		innerPanel.add(ok, BorderLayout.SOUTH);
		setContentPane(innerPanel);
        refresh();
        pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == combo) {
			account = (Account) combo.getSelectedItem();
		} else if (e.getSource() == create) {
            RefreshableComponent frame = AccountingMenuBar.getFrame(AccountingMenuBar.NEW_ACCOUNT);
            frame.setVisible(true);
		} else if (e.getSource() == ok) {
			dispose();
		}

	}

	public Account getSelection() {
		return account;
	}

    @Override
    public void refresh() {
        model.removeAllElements();
        Accounts accounts = accountings.getCurrentAccounting().getAccounts();
        for(Account account : accounts.getAccounts()) {
            model.addElement(account);
        }
    }
}
