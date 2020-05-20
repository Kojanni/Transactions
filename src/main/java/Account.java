public class Account {
    private long money;
    private String accNumber;
    private boolean blockedAcc;

    public Account(long money, String accNumber) {
        this.money = money;
        this.accNumber = accNumber;
        this.blockedAcc = false;
    }

    public long getMoney() {
        return money;
    }

    public void putMoney(long money) {
        if (!blockedAcc) {
            this.money += money;
        } else {
            System.out.println("Аккаунт " + getAccNumber() + "   заблокирован. Обратитесь в ближайшее отделение банка или позвоните по телефону горячей линии");
        }
    }

    public void withdrawMoney(long money) {
        if (!blockedAcc) {
            if (this.money >= money) {
                this.money -= money;
            } else {
                System.out.println("Недостаточно средств для совершения данной операции");
            }
        } else {
            System.out.println("Аккаунт " + getAccNumber() + "  заблокирован. Обратитесь в ближайшее отделение банка или позвоните по телефону горячей линии");
        }
    }

    public String getAccNumber() {
        return accNumber;
    }

    public boolean isBlockedAcc() {
        return blockedAcc;
    }

    public void blockAcc() {
        this.blockedAcc = true;
    }
}
