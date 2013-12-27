package be.dafke.BasicAccounting.Objects;

import be.dafke.ObjectModel.BusinessCollection;
import be.dafke.ObjectModel.BusinessCollectionProvider;
import be.dafke.ObjectModel.BusinessTypeCollection;
import be.dafke.ObjectModel.BusinessTypeProvider;
import be.dafke.ObjectModel.Exceptions.DuplicateNameException;
import be.dafke.ObjectModel.Exceptions.EmptyNameException;

import java.util.ArrayList;

import static java.util.ResourceBundle.getBundle;

/**
 * User: Dafke
 * Date: 27/02/13
 * Time: 12:07
 */
public class Balances extends BusinessCollection<Balance> implements BusinessCollectionProvider<Account>, BusinessTypeProvider<AccountType>{

    @Override
    public String getChildType(){
        return "Balance";
    }

    public static String RESULT_BALANCE = "ResultBalance";
    public static String RELATIONS_BALANCE = "RelationsBalance";
    public static String YEAR_BALANCE = "YearBalance";
    private BusinessCollection<Account> businessCollection;
    private BusinessTypeCollection<AccountType> businessTypeCollection;

    public void addDefaultBalances(Accounting accounting){
        ArrayList<AccountType> costs = new ArrayList<AccountType>();
        ArrayList<AccountType> revenues = new ArrayList<AccountType>();
        ArrayList<AccountType> credit = new ArrayList<AccountType>();
        ArrayList<AccountType> debit = new ArrayList<AccountType>();
        ArrayList<AccountType> active = new ArrayList<AccountType>();
        ArrayList<AccountType> passive = new ArrayList<AccountType>();
//        costs.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("KOST")));
//        revenues.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("OPBRENGST")));
//        credit.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("TEGOED_VAN_KLANT")));
//        debit.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("SCHULD_AAN_LEVERANCIER")));
//        active.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("ACTIEF")));
//        active.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("TEGOED_VAN_KLANT")));
//        passive.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("PASSIEF")));
//        passive.add(accounting.getAccountTypes().getBusinessObject(getBundle("Accounting").getString("SCHULD_AAN_LEVERANCIER")));

        costs.add(accounting.getAccountTypes().getBusinessObject(("Cost")));
        revenues.add(accounting.getAccountTypes().getBusinessObject(("Revenue")));
        credit.add(accounting.getAccountTypes().getBusinessObject(("Credit")));
        debit.add(accounting.getAccountTypes().getBusinessObject(("Debit")));
        active.add(accounting.getAccountTypes().getBusinessObject(("Active")));
        active.add(accounting.getAccountTypes().getBusinessObject(("Credit")));
        passive.add(accounting.getAccountTypes().getBusinessObject(("Passive")));
        passive.add(accounting.getAccountTypes().getBusinessObject(("Debit")));

        Balance resultBalance = createNewChild(RESULT_BALANCE);
        resultBalance.setLeftName(getBundle("Accounting").getString("KOSTEN"));
        resultBalance.setRightName(getBundle("Accounting").getString("OPBRENGSTEN"));
        resultBalance.setLeftTotalName(getBundle("Accounting").getString("TOTAAL_KOSTEN"));
        resultBalance.setRightTotalName(getBundle("Accounting").getString("TOTAAL_OPBRENGSTEN"));
        resultBalance.setLeftResultName(getBundle("Accounting").getString("VERLIES"));
        resultBalance.setRightResultName(getBundle("Accounting").getString("WINST"));
        resultBalance.setLeftTypes(costs);
        resultBalance.setRightTypes(revenues);

        Balance relationsBalance = createNewChild(RELATIONS_BALANCE);
        relationsBalance.setLeftName(getBundle("Accounting").getString("TEGOEDEN_VAN_KLANTEN"));
        relationsBalance.setRightName(getBundle("Accounting").getString("SCHULDEN_AAN_LEVERANCIERS"));
        relationsBalance.setLeftTotalName(getBundle("Accounting").getString("TOTAAL_TEGOEDEN"));
        relationsBalance.setRightTotalName(getBundle("Accounting").getString("TOTAAL_SCHULDEN"));
        relationsBalance.setLeftResultName(getBundle("Accounting").getString("RESTEREND_TEGOED"));
        relationsBalance.setRightResultName(getBundle("Accounting").getString("RESTERENDE_SCHULD"));
        relationsBalance.setLeftTypes(credit);
        relationsBalance.setRightTypes(debit);

        Balance yearBalance = createNewChild(YEAR_BALANCE);
        yearBalance.setLeftName(getBundle("Accounting").getString("ACTIVA"));
        yearBalance.setRightName(getBundle("Accounting").getString("PASSIVA"));
        yearBalance.setLeftTotalName(getBundle("Accounting").getString("TOTAAL_ACTIVA_TEGOEDEN"));
        yearBalance.setRightTotalName(getBundle("Accounting").getString("TOTAAL_PASSIVA_SCHULDEN"));
        yearBalance.setLeftResultName(getBundle("Accounting").getString("WINST"));
        yearBalance.setRightResultName(getBundle("Accounting").getString("VERLIES"));
        yearBalance.setLeftTypes(active);
        yearBalance.setRightTypes(passive);

        try {
            addBusinessObject(resultBalance);
            addBusinessObject(relationsBalance);
            addBusinessObject(yearBalance);
        } catch (EmptyNameException e) {
            System.err.println("The Name of a Balance can not be empty.");
        } catch (DuplicateNameException e) {
            System.err.println("The Name of a Balance already exists.");
        }
    }

    @Override
    public Balance createNewChild(String name) {
        Balance balance = new Balance(name);
        balance.setBusinessCollection(businessCollection);
        balance.setBusinessTypeCollection(businessTypeCollection);
        return balance;
    }

    /*@Override
    public void readCollection() {
        readCollection("Balance", false);
    }*/

    @Override
    public BusinessCollection<Account> getBusinessCollection() {
        return businessCollection;
    }

    @Override
    public void setBusinessCollection(BusinessCollection<Account> businessCollection) {
        this.businessCollection = businessCollection;
    }

    @Override
    public BusinessTypeCollection<AccountType> getBusinessTypeCollection() {
        return businessTypeCollection;
    }

    @Override
    public void setBusinessTypeCollection(BusinessTypeCollection<AccountType> businessTypeCollection) {
        this.businessTypeCollection = businessTypeCollection;
    }
}