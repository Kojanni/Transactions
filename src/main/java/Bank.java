import java.util.HashMap;
import java.util.Hashtable;
import java.util.Random;

public class Bank {
    private Hashtable<String, Account> accounts = new Hashtable<String, Account>();
    private final Random random = new Random();
    private int fraudLimit = 50000;
    private int cardLimit = 0;

    public synchronized boolean isFraud(String fromAccountNum, String toAccountNum, long amount)
            throws InterruptedException {
        Thread.sleep(1000);
        return random.nextBoolean();
    }

    /**
     * TODO: реализовать метод. Метод переводит деньги между счетами.
     * Если сумма транзакции > 50000, то после совершения транзакции,
     * она отправляется на проверку Службе Безопасности – вызывается
     * метод isFraud. Если возвращается true, то делается блокировка
     * счетов (как – на ваше усмотрение)
     */
    public void transfer(String fromAccountNum, String toAccountNum, long amount) throws InterruptedException {
        Account fromAccount = accounts.get(fromAccountNum);
        Account toAccount = accounts.get(toAccountNum);
        boolean controlFlagTopAccount = Long.parseLong(fromAccountNum) > Long.parseLong(toAccountNum);
        Account topAccount = controlFlagTopAccount ? fromAccount : toAccount;
        Account lowAccount = controlFlagTopAccount ? toAccount : fromAccount;
        if (fromAccountNum.contains(toAccountNum)) {
            System.out.println("Ошибка перевода. Причина: Указан один и тот же лицевой счет");
        } else
            synchronized (topAccount) {
                synchronized (lowAccount) {
                    if (fromAccount.isBlockedAcc()) {
                        System.out.println("Ошибка перевода. Причина: Блокировка текущего аккаунта");
                    } else if (toAccount.isBlockedAcc()) {
                        System.out.println("Ошибка перевода. Причина: Блокировка аккаунта получателя");
                    } else {
                        if (fromAccount.getMoney() < amount) {
                            System.out.println("Ошибка перевода. Недостаточно средств для совершения данной операции");
                        } else {
                            System.out.println("Перевод " + amount + " с " + fromAccountNum + " на " + toAccountNum);
                            fromAccount.withdrawMoney(amount);
                            toAccount.putMoney(amount);
                            if (amount > fraudLimit) {
                                if (isFraud(fromAccountNum, toAccountNum, amount)) {
                                    System.out.println("Блокируем");
                                    fromAccount.blockAcc();
                                    toAccount.blockAcc();
                                }
                            }
                        }
                    }
                }
            }
    }

    /**
     * TODO: реализовать метод. Возвращает остаток на счёте.
     */
    public long getAccountBalance(String accountNum) {
        return accounts.get(accountNum).getMoney();
    }

    public long getBankBalance() {
        long balance = 0;
        for (int i = 0; i < accounts.size(); i++) {
            balance += accounts.get(String.valueOf(i)).getMoney();
        }
        return balance;
    }

    public Hashtable<String, Account> getAccounts() {
        return accounts;
    }

    public void addAccounts(String personalAcc, Account account) {
        if (!(accounts.size() <= 1)) {
            accounts.put(personalAcc, account);
        } else if (!accounts.containsKey(personalAcc)) {
            accounts.put(personalAcc, account);
        } else {
            System.out.println("Неверно введен личный счет. Такой уже существует в базе.");
        }
    }

    public void setFraudLimit(int fraudLimit) {
        this.fraudLimit = fraudLimit;
    }
}
