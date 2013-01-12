package be.dafke.Coda.Objects;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import be.dafke.Coda.BankAccount;
import be.dafke.Coda.CodaParser;
import be.dafke.Coda.CounterParties;
import be.dafke.Coda.CounterParty;
import be.dafke.Coda.TmpCounterParty;

public class Movement implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final String sequenceNumber, detailNumber, bankReference, transactionCode, communication, statementNr;
	private final boolean debit, structured, part2;
	private final BigDecimal amount;
	private final Calendar date, entryDate;

	private String counterPartyName, counterPartyAccount, counterPartyBic, counterPartyCurrency;

	private String customerReference, communication2, communication3;
	private CounterParty counterParty;
	private TmpCounterParty tmpCounterParty;
	private boolean info, part3;
	private Information information;

	public static Movement parse(String line) {
		String nr1 = line.substring(2, 6);
		String nr2 = line.substring(6, 10);
		String bankRef = line.substring(10, 31);
		String sign = line.substring(31, 32);
		boolean debit = "1".equals(sign);
		String amountString = line.substring(32, 47);
		BigDecimal amount = CodaParser.convertBigDecimal(amountString);
		String date = line.substring(47, 53);
		Calendar cal = CodaParser.convertDate(date);
		String transCode = line.substring(53, 61);
		String struc = line.substring(61, 62);
		boolean structured = "1".equals(struc);
		String comm = line.substring(62, 115);
		String entryDate = line.substring(115, 121);
		Calendar cal2 = CodaParser.convertDate(entryDate);
		String nr = line.substring(121, 124);
		String part2 = line.substring(125, 126);
		boolean part2Coming = "1".equals(part2);
		String info = line.substring(127, 128);
		boolean infoComing = "1".equals(info);
		return new Movement(nr1, nr2, bankRef, debit, amount, cal, transCode, structured, comm, cal2, nr, part2Coming,
				infoComing);
	}

	public Movement(String sequenceNumber, String detailNumber, String bankReference, boolean debit, BigDecimal amount,
			Calendar date, String transactionCode, boolean structured, String communication, Calendar entryDate,
			String statementNr, boolean part2, boolean info) {
		this.sequenceNumber = sequenceNumber;
		this.detailNumber = detailNumber;
		this.bankReference = bankReference;
		this.debit = debit;
		this.amount = amount;
		this.date = date;
		this.transactionCode = transactionCode.trim();
		this.structured = structured;
		this.communication = communication;
		this.entryDate = entryDate;
		this.statementNr = statementNr;
		this.part2 = part2;
		this.info = info;
		/*
		 * counterParty = new CounterParty(transactionCode); CounterParties counterParties =
		 * CounterParties.getInstance(); counterParty = counterParties.put(transactionCode, counterParty);
		 */}

	public void addPart2(String line) {
		String nr1 = line.substring(2, 6);
		String nr2 = line.substring(6, 10);
		if (!nr1.equals(sequenceNumber)) System.err.println("SequenceNumber not equal [" + nr1 + "!=" + sequenceNumber
				+ "]");
		if (!nr2.equals(detailNumber)) System.err.println("DetailNumber not equal [" + nr2 + "!=" + detailNumber + "]");
		communication2 = line.substring(10, 63);
		customerReference = line.substring(63, 98);
		counterPartyBic = line.substring(98, 109);
		String part3String = line.substring(125, 126);
		part3 = "1".equals(part3String);
		String infoString = line.substring(127, 128);
		info = "1".equals(infoString);
	}

	public void addPart3(String line) {
		String nr1 = line.substring(2, 6);
		String nr2 = line.substring(6, 10);
		if (!nr1.equals(sequenceNumber)) System.err.println("SequenceNumber not equal [" + nr1 + "!=" + sequenceNumber
				+ "]");
		if (!nr2.equals(detailNumber)) System.err.println("DetailNumber not equal [" + nr2 + "!=" + detailNumber + "]");
		counterPartyAccount = line.substring(10, 44).trim();
		counterPartyCurrency = line.substring(44, 47).trim();
		counterPartyName = line.substring(47, 82).trim();
		counterParty = new CounterParty(counterPartyName);
		if (!counterPartyAccount.trim().equals("")) {
			BankAccount account = new BankAccount(counterPartyAccount);
			account.setBic(counterPartyBic);
			account.setCurrency(counterPartyCurrency);
			counterParty.addAccount(account);
		}
		CounterParties counterParties = CounterParties.getInstance();
		// counterParties.remove(transactionCode);
		counterParty = counterParties.put(counterPartyName, counterParty);
		communication3 = line.substring(82, 125).trim();
		String infoString = line.substring(127, 128);
		info = "1".equals(infoString);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("MOVEMENT\r\n");
		builder.append("sequenceNumber=" + sequenceNumber + "\r\n");
		builder.append("detailNumber=" + detailNumber + "\r\n");
		builder.append("bankReference=" + bankReference + "\r\n");
		if (debit) builder.append("D/C = D" + "\r\n");
		else builder.append("D/C = C" + "\r\n");
		builder.append("amount=" + amount + "\r\n");
		builder.append("date=" + date.get(Calendar.DAY_OF_MONTH) + "/" + (date.get(Calendar.MONTH) + 1) + "/"
				+ date.get(Calendar.YEAR) + "\r\n");
		builder.append("transactionCode=" + transactionCode + "\r\n");
		builder.append("structured=" + structured + "\r\n");
		builder.append("communication=" + communication + "\r\n");
		if (structured && communication.startsWith("101")) {
			// +++123/1234/12345+++

		}
		builder.append("entryDate=" + entryDate.get(Calendar.DAY_OF_MONTH) + "/" + (entryDate.get(Calendar.MONTH) + 1)
				+ "/" + entryDate.get(Calendar.YEAR) + "\r\n");
		builder.append("statementNr=" + statementNr + "\r\n");
		if (part2) {
			builder.append("PART2\r\n");
			builder.append("communication2=" + communication2 + "\r\n");
			builder.append("customerReference=" + customerReference + "\r\n");
			builder.append("counterPartyBic=" + counterPartyBic + "\r\n");
		}
		if (part3) {
			builder.append("PART3\r\n");
			builder.append("communication3=" + communication3 + "\r\n");
			builder.append("counterPartyAccount=" + counterPartyAccount + "\r\n");
			builder.append("counterPartyCurrency=" + counterPartyCurrency + "\r\n");
			builder.append("counterPartyName=" + counterPartyName + "\r\n");
		}
		if (info) {
			builder.append(information);
		}
		return builder.toString();
	}

	public void setInformation(Information information) {
		this.information = information;
	}

	/*
	 * public String getCounterPartyName() { return counterPartyName; } public String getCounterPartyAccount() { return
	 * counterPartyAccount; } public String getCounterPartyCurrency() { return counterPartyCurrency; } public String
	 * getCounterPartyBic() { return counterPartyBic; }
	 */
	public String getStatementNr() {
		return statementNr;
	}

	public String getSequenceNr() {
		return sequenceNumber;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public boolean isDebit() {
		return debit;
	}

	public Calendar getDate() {
		return date;
	}

	public String getTransactionCode() {
		return transactionCode;
	}

	public CounterParty getCounterParty() {
		return counterParty;
	}

	public Object getCommunication() {
		StringBuilder builder = new StringBuilder(communication.trim());
		if (communication2 != null && !communication2.trim().isEmpty()) {
			builder.append("|(2)" + communication2.trim());
		}
		if (communication3 != null && !communication3.trim().isEmpty()) {
			builder.append("|(3)" + communication3.trim());
		}
		return builder.toString();
	}

	public void setCounterParty(CounterParty counterParty) {
		this.counterParty = counterParty;
	}

	public void setTmpCounterParty(TmpCounterParty counterParty) {
		this.tmpCounterParty = counterParty;
	}

	public TmpCounterParty getTmpCounterParty() {
		return tmpCounterParty;
	}
}