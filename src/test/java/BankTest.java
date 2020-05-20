import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

public class BankTest {
    private Bank superbank = new Bank();
    private final int NUMBER_OF_ACCOUNTS = 2;
    private final int CORES_AMOUNT = 8;
    private final int FRAUD_LIMIT = /*Integer.MAX_VALUE;*/ 50000;
    private final int NUMBER_OF_OPERATIONS_FOR_THREAD = 1000000;
    private final int MONEY_IN_ACCOUNT = 10000000;
    private final ThreadFactory threadFactory = new ThreadFactoryBuilder()
            .setNameFormat("Поток переводов-%d")
            .setDaemon(true)
            .build();
    @Test
    public void transferMethodMultithreadedTest() throws InterruptedException {
        //given

        for (int i = 0; i < NUMBER_OF_ACCOUNTS; i++) {
            superbank.addAccounts(String.valueOf(i), new Account(MONEY_IN_ACCOUNT, String.valueOf(i)));
        }
        // ON/OFF FRAUD:
        superbank.setFraudLimit(FRAUD_LIMIT);
        long expectedResult = superbank.getBankBalance();
        long result;
        long[] transferAmount = new long[NUMBER_OF_OPERATIONS_FOR_THREAD];
        for (int x = 0; x < NUMBER_OF_OPERATIONS_FOR_THREAD; x++) {
            transferAmount[x] = Math.round(10000 * Math.random());
        }

        for (int i = 0; i < NUMBER_OF_ACCOUNTS; i++) {
            superbank.addAccounts(String.valueOf(i), new Account(MONEY_IN_ACCOUNT, String.valueOf(i)));
        }

        //run
        ExecutorService executor = Executors.newFixedThreadPool(CORES_AMOUNT, threadFactory);
for (int i = 0; i < CORES_AMOUNT; i++) {
    executor.submit(() -> {

        for (int x = 0; x < NUMBER_OF_OPERATIONS_FOR_THREAD; x++) {
            String fromAccNum;
            String toAccNum;
            do {
                fromAccNum = String.valueOf((int) (Math.random() * (NUMBER_OF_ACCOUNTS)));
                toAccNum = String.valueOf((int) (Math.random() * (NUMBER_OF_ACCOUNTS)));
            } while (fromAccNum.contains(toAccNum));
            try {
                superbank.transfer(fromAccNum, toAccNum, transferAmount[x]);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    });

}
        executor.shutdown();
        boolean done = executor.awaitTermination(1, TimeUnit.HOURS);
        System.out.println("Все ли потоки завершены? " + done);

        result = superbank.getBankBalance();
        System.out.println("--------------РЕЗУЛЬТАТ------------------\n" + expectedResult + " - " + result);


        //assert
        Assert.assertEquals(expectedResult, result);

    }

  /*  @Test
    void getBalance() {
    }*/
}
