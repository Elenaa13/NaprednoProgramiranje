package Lab2;

import java.util.*;
import java.util.stream.Collectors;

class Account {
    private String name;
    private long id;
    private double balance;

    public Account(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.id = new Random().nextLong();
    }

    public double getBalance() {
        return balance;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nBalance: " + String.format("%.2f$", balance) + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id && Objects.equals(name, account.name) && balance == account.balance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id, balance);
    }
}

abstract class Transaction {
    private final long fromId;
    private final long toId;
    private final String description;
    private final double amount;

    public Transaction(long fromId, long toId, String description, double amount) {
        this.fromId = fromId;
        this.toId = toId;
        this.description = description;
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public String getDescription() {
        return description;
    }

    public abstract double calculateProvision();
}

class FlatAmountProvisionTransaction extends Transaction {
    private final double flatProvision;

    public FlatAmountProvisionTransaction(long fromId, long toId, double amount, double flatProvision) {
        super(fromId, toId, "FlatAmount", amount);
        this.flatProvision = flatProvision;
    }

    public double getFlatAmount() {
        return flatProvision;
    }

    @Override
    public double calculateProvision() {
        return flatProvision;
    }
}

class FlatPercentProvisionTransaction extends Transaction {
    private final int percent;

    public FlatPercentProvisionTransaction(long fromId, long toId, double amount, int percent) {
        super(fromId, toId, "FlatPercent", amount);
        this.percent = percent;
    }

    public int getPercent() {
        return percent;
    }

    @Override
    public double calculateProvision() {
        return getAmount() * percent / 100.0;
    }
}

class Bank {
    private String name;
    private List<Account> accounts;
    private double totalTransfers;
    private double totalProvision;

    public Bank(String name, Account[] accounts) {
        this.name = name;
        this.accounts = new ArrayList<>(Arrays.asList(accounts));
        this.totalTransfers = 0;
        this.totalProvision = 0;
    }

    public boolean makeTransaction(Transaction t) {
        Account fromAccount = findAccountById(t.getFromId());
        Account toAccount = findAccountById(t.getToId());

        if (fromAccount == null || toAccount == null || fromAccount.getBalance() < t.getAmount()) {
            return false;
        }

        double provision = t.calculateProvision();
        double totalDeduct = t.getAmount() + provision;

        if (fromAccount.getBalance() < totalDeduct) {
            return false;
        }

        fromAccount.setBalance(fromAccount.getBalance() - totalDeduct);
        toAccount.setBalance(toAccount.getBalance() + t.getAmount());
        totalTransfers += t.getAmount();
        totalProvision += provision;

        return true;
    }

    public double totalTransfers() {
        return totalTransfers;
    }

    public double totalProvision() {
        return totalProvision;
    }

    public Account[] getAccounts() {
        return accounts.toArray(new Account[0]);
    }

    private Account findAccountById(long id) {
        for (Account account : accounts) {
            if (account.getId() == id) {
                return account;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Name: ").append(name).append("\n\n");
        for (Account account : accounts) {
            sb.append(account.toString());
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bank bank = (Bank) o;
        return name.equals(bank.name) && accounts.equals(bank.accounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, accounts);
    }
}

class BankTester {
    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        String test_type = jin.nextLine();
        switch (test_type) {
            case "typical_usage":
                testTypicalUsage(jin);
                break;
            case "equals":
                testEquals();
                break;
        }
        jin.close();
    }

    private static double parseAmount(String amount) {
        return Double.parseDouble(amount.replace("$", ""));
    }

    private static void testEquals() {
        Account a1 = new Account("Andrej", 20.0);
        Account a2 = new Account("Andrej", 20.0);
        Account a3 = new Account("Andrej", 30.0);
        Account a4 = new Account("Gajduk", 20.0);
        List<Account> all = Arrays.asList(a1, a2, a3, a4);
        if (!(a1.equals(a1) && !a1.equals(a2) && !a2.equals(a1) && !a3.equals(a1) && !a4.equals(a1) && !a1.equals(null))) {
            System.out.println("Your account equals method does not work properly.");
            return;
        }
        Set<Long> ids = all.stream().map(Account::getId).collect(Collectors.toSet());
        if (ids.size() != all.size()) {
            System.out.println("Different accounts have the same IDS. This is not allowed");
            return;
        }
        System.out.println("All your equals methods work properly.");
    }

    private static void testTypicalUsage(Scanner jin) {
        String bank_name = jin.nextLine();
        int num_accounts = jin.nextInt();
        jin.nextLine();
        Account accounts[] = new Account[num_accounts];
        for (int i = 0; i < num_accounts; ++i)
            accounts[i] = new Account(jin.nextLine(), parseAmount(jin.nextLine()));
        Bank bank = new Bank(bank_name, accounts);
        while (true) {
            String line = jin.nextLine();
            switch (line) {
                case "stop":
                    return;
                case "transaction":
                    String descrption = jin.nextLine();
                    double amount = parseAmount(jin.nextLine());
                    double parameter = parseAmount(jin.nextLine());
                    int from_idx = jin.nextInt();
                    int to_idx = jin.nextInt();
                    jin.nextLine();
                    Transaction t = getTransaction(descrption, from_idx, to_idx, amount, parameter, bank);
                    System.out.println("Transaction amount: " + String.format("%.2f$", t.getAmount()));
                    System.out.println("Transaction description: " + t.getDescription());
                    System.out.println("Transaction successful? " + bank.makeTransaction(t));
                    break;
                case "print":
                    System.out.println(bank.toString());
                    System.out.println("Total provisions: " + String.format("%.2f$", bank.totalProvision()));
                    System.out.println("Total transfers: " + String.format("%.2f$", bank.totalTransfers()));
                    System.out.println();
                    break;
            }
        }
    }

    private static Transaction getTransaction(String description, int fromIdx, int toIdx, double amount, double parameter, Bank bank) {
        Account[] accounts = bank.getAccounts();
        if (description.equals("FlatAmount")) {
            return new FlatAmountProvisionTransaction(accounts[fromIdx].getId(), accounts[toIdx].getId(), amount, parameter);
        } else {
            return new FlatPercentProvisionTransaction(accounts[fromIdx].getId(), accounts[toIdx].getId(), amount, (int) parameter);
        }
    }
}
