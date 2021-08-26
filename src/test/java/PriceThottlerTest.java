import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * Self testing to see via logger whether the whole system works fine
 * As no unit test were required did not add sophisticated unit tests
 */
@RunWith(JUnit4.class)
public class PriceThottlerTest {


    @SneakyThrows
    @Test
    public void testThrottlerProcessor(){

        PriceThrottler priceThrottler = new PriceThrottler();
        PriceProcessor slowPriceProcessor = new SlowPriceProcessor();
        PriceProcessor fastPriceProcessor = new FastPriceProcessor();
        slowPriceProcessor.subscribe(priceThrottler);
        fastPriceProcessor.subscribe(priceThrottler);
        priceThrottler.onPrice("EuroDollar",5.4);
        priceThrottler.onPrice("NisDollar",7.4);
        Thread.sleep(4000);
        slowPriceProcessor.unsubscribe(priceThrottler);
        fastPriceProcessor.unsubscribe(priceThrottler);
        Thread.sleep(5000);
        priceThrottler.close();

    }
}
