package be.dafke.Accounting.GUI.MainWindow;

import be.dafke.Accounting.GUI.ComponentMap;
import be.dafke.Accounting.Objects.Accounting.Accounting;
import be.dafke.Accounting.Objects.Accounting.Journal;
import be.dafke.Accounting.Objects.Accounting.Journals;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.util.ResourceBundle.getBundle;

/**
 * @author David Danneels
 */
public class JournalsGUI extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JComboBox<Journal> combo;
	private final JButton journalManagement, details;
    private Journals journals;

	public JournalsGUI(ActionListener actionListener) {
		setBorder(new TitledBorder(new LineBorder(Color.BLACK), getBundle(
                "Accounting").getString("DAGBOEKEN")));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		combo = new JComboBox<Journal>();
        combo.addActionListener(this);
		combo.setEnabled(false);
		add(combo);
		JPanel paneel = new JPanel();
		journalManagement = new JButton(getBundle("Accounting").getString("NIEUW_DAGBOEK"));
		journalManagement.addActionListener(actionListener);
        journalManagement.setActionCommand(ComponentMap.JOURNAL_MANAGEMENT);
		journalManagement.setEnabled(false);
		paneel.add(journalManagement);
		details = new JButton(getBundle("Accounting").getString("DETAILS_DAGBOEK"));
		details.addActionListener(actionListener);
        details.setActionCommand(ComponentMap.JOURNAL_DETAILS);
		details.setEnabled(false);
		paneel.add(details);
		add(paneel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == combo) {
			Journal journal = (Journal) combo.getSelectedItem();
			if(journal!=null){
                journals.setCurrentJournal(journal);
                // TODO: check if this change is seen in JournalGUI
                ComponentMap.refreshAllFrames();
            }
		}
	}

    public void setAccounting(Accounting accounting){
        if(accounting==null){
            setJournals(null);
        } else {
            setJournals(accounting.getJournals());
        }
    }

    public void setJournals(Journals journals){
        this.journals = journals;
    }

	public void refresh() {
        combo.removeActionListener(this);
        combo.removeAllItems();
		if (journals!=null) {
            for(Journal journal: journals.values()){
                combo.addItem(journal);
            }
			combo.setSelectedItem(journals.getCurrentJournal());
            details.setEnabled(journals!=null && journals.getCurrentJournal()!=null);
            combo.setEnabled(journals!=null && journals.getCurrentJournal()!=null);
            journalManagement.setEnabled(journals != null);
		} else {
			combo.setSelectedItem(null);
            details.setEnabled(false);
            combo.setEnabled(false);
            journalManagement.setEnabled(false);
		}
        combo.addActionListener(this);
	}
}