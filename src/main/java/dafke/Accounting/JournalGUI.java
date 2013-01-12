package be.dafke.Accounting;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.math.BigDecimal;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import be.dafke.Utils;
import be.dafke.Accounting.Objects.Accounting;
import be.dafke.Accounting.Objects.Accountings;
import be.dafke.Accounting.Objects.Transaction;

public class JournalGUI extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JournalDataModel journalDataModel;
	private final JTextField debet, credit, dag, bewijs, ident;
	private final JButton ok, clear;
	protected String tekst;
	protected Calendar datum;
	private BigDecimal debettotaal, credittotaal;
	private final AccountingGUIFrame parent;

	public JournalGUI(AccountingGUIFrame parent) {
		this.parent = parent;
		debettotaal = new BigDecimal(0);
		credittotaal = new BigDecimal(0);
		datum = Calendar.getInstance();
		tekst = "";
		setLayout(new BorderLayout());
		journalDataModel = new JournalDataModel();
		JTable table = new JTable(journalDataModel);
		table.setPreferredScrollableViewportSize(new Dimension(800, 200));
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		ident = new JTextField(4);
		ident.setEditable(false);
		dag = new JTextField(8);
		dag.setText(Utils.toString(datum));
		dag.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
			}

			@Override
			public void focusLost(FocusEvent fe) {
				Calendar d = Utils.toCalendar(dag.getText());
				if (d != null) datum = d;
				Transaction.getInstance().setDate(datum);
				dag.setText(Utils.toString(datum));
			}

		});
		bewijs = new JTextField(30);
		bewijs.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
			}

			@Override
			public void focusLost(FocusEvent fe) {
				tekst = bewijs.getText();
				Transaction.getInstance().setDescription(tekst);
			}
		});
		Transaction.newInstance(datum, tekst);

		ok = new JButton(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("OK"));
		ok.addActionListener(this);
		ok.setEnabled(false);
		clear = new JButton(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("WIS_PANEEL"));
		clear.addActionListener(this);

		JPanel paneel1 = new JPanel();
		paneel1.add(new JLabel(
				java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("VERRICHTING")));
		paneel1.add(ident);
		paneel1.add(new JLabel(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("DATUM")));
		paneel1.add(dag);

		JPanel paneel2 = new JPanel();
		paneel2.add(new JLabel(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString(
				"BEWIJS-STUK(KEN)")));
		paneel2.add(bewijs);

		JPanel paneel3 = new JPanel();
		paneel3.add(ok);
		paneel3.add(clear);
		debet = new JTextField(8);
		credit = new JTextField(8);
		debet.setText(debettotaal.toString());
		credit.setText(credittotaal.toString());
		debet.setEditable(false);
		credit.setEditable(false);
		paneel3.add(new JLabel(
				java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString("DEBETTOTAAL")));
		paneel3.add(debet);
		paneel3.add(new JLabel(java.util.ResourceBundle.getBundle("be/dafke/Accounting/Bundle").getString(
				"CREDITTOTAAL")));
		paneel3.add(credit);

		JPanel onder = new JPanel(new GridLayout(0, 1));
		onder.add(paneel1);
		onder.add(paneel2);
		onder.add(paneel3);

		add(onder, BorderLayout.SOUTH);
	}

	public void setOK() {
		ok.setEnabled(true);
	}

	public void refresh() {
		debettotaal = Transaction.getInstance().getDebetTotaal();
		credittotaal = Transaction.getInstance().getCreditTotaal();
		debet.setText(debettotaal.toString());
		credit.setText(credittotaal.toString());
		journalDataModel.fireTableDataChanged();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ((JButton) e.getSource() == ok) {
			Transaction.getInstance().book(Accountings.getCurrentAccounting().getCurrentJournal());
			init();
			clear();
			parent.repaintAllFrames();
		}
		if ((JButton) e.getSource() == clear) {
			clear();
		}
	}

	public void clear() {
		ok.setEnabled(false);
		Transaction.newInstance(datum, tekst);
		refresh();
	}

	public void init() {
		Accounting accounting = Accountings.getCurrentAccounting();
		if (accounting != null) {
			if (accounting.getJournals().isEmpty()) {
				NewJournalGUI.getInstance(parent).setVisible(true);
			} else {
				ident.setText(accounting.getCurrentJournal().getAbbreviation() + " "
						+ accounting.getCurrentJournal().getId());
			}
		} else {
			ident.setText("");
		}
	}
}