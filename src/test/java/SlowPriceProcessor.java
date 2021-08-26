import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@AllArgsConstructor
public class SlowPriceProcessor implements PriceProcessor{

    private static final Logger logger = LogManager.getLogger(SlowPriceProcessor.class);

    @SneakyThrows
    @Override
    public void onPrice(String ccPair, double rate) {
        logger.info("SlowPriceThrottler started");
        Thread.sleep(3000);
        logger.info("SlowPriceThrottler ended");
    }

    @Override
    public void subscribe(PriceProcessor priceProcessor) {
        priceProcessor.subscribe(this);
    }

    @Override
    public void unsubscribe(PriceProcessor priceProcessor) {
        priceProcessor.unsubscribe(this);
    }
}
