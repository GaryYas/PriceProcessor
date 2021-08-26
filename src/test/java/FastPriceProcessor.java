import lombok.SneakyThrows;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class FastPriceProcessor implements PriceProcessor {


    private static final Logger logger = LogManager.getLogger(FastPriceProcessor.class);

    @SneakyThrows
    @Override
    public void onPrice(String ccPair, double rate) {

        logger.info("fast price processor started");
        Thread.sleep(100);
        logger.info("fast price processor ended");
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
