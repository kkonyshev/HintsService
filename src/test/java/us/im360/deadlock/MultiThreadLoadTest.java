package us.im360.deadlock;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Ignore
public class MultiThreadLoadTest {

    @Test
    public void testDL() throws InterruptedException {
        Executor es = Executors.newFixedThreadPool(2);
        es.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("do1");
                RestTemplate rt = new RestTemplate();
                rt.getForEntity("http://localhost:8080/" + "/HintsService/v1.0/rest/deadlock/"+1+"/nameT11/"+2+"/nameT12", Object.class);
                System.out.println("done1");
            }
        });
        es.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("do2");
                RestTemplate rt = new RestTemplate();
                rt.getForEntity("http://localhost:8080/" + "/HintsService/v1.0/rest/deadlock/"+2+"/nameT22/"+1+"/nameT21", Object.class);
                System.out.println("done2");
            }
        });
        Thread.sleep(20000);
    }

    @Test
    public void testCacheReport() throws InterruptedException {
        Executor es = Executors.newFixedThreadPool(20);
        for (int i=0; i<200; i++) {
            buildCashCall(es);
            //buildProfitCall(es);
        }


        Thread.currentThread().join();
    }

    private void buildCashCall(Executor es) {
        es.execute(new Runnable() {
            @Override
            public void run() {
                String userId = String.valueOf(new Random(System.currentTimeMillis()).nextInt());
                String restaurantId = String.valueOf(new Random(System.currentTimeMillis()).nextInt());
                String closeDate = "2016-01-26";
                cashReport(userId, restaurantId, closeDate);
                System.out.println("Done cash");
            }
        });
    }

    private void cashReport(String userId, String restaurantId, String closeDate) {
        RestTemplate rt = new RestTemplate();
        rt.getForEntity("http://localhost:8080/" + "/HintsService/v1.0/rest/report/cash/userId/" + userId + "/restaurantId/" + restaurantId + "/closeDate/" + closeDate, Object.class);
    }

    private void buildProfitCall(Executor es) {
        es.execute(new Runnable() {
            @Override
            public void run() {
                String userId = String.valueOf(new Random(System.currentTimeMillis()).nextInt());
                String restaurantId = String.valueOf(new Random(System.currentTimeMillis()).nextInt());
                String startDate = "2014-01-01";
                String endDate = "2016-01-26";
                profitReport(userId, restaurantId, startDate, endDate);
                System.out.println("Done profit");
            }
        });
    }

    private void profitReport(String userId, String restaurantId, String startDate, String endDate) {
        RestTemplate rt = new RestTemplate();
        rt.getForEntity("http://localhost:8080/" + "/HintsService/v1.0/rest/report/profit/userId/" + userId + "/restaurantId/" + restaurantId + "/startDate/" + startDate + "/endDate/" + endDate, Object.class);
    }
}
