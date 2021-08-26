import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingDeque;


@Data
@AllArgsConstructor
public class ProcessorRunner implements Runnable {

    PriceProcessor priceProcessor;
    BlockingDeque<PriceRatePair> queue;
    private static final Logger logger = LogManager.getLogger(ProcessorRunner.class);
    ProcessorRunner() {
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                PriceRatePair priceRatePair = queue.take();
                logger.info(priceProcessor.getClass().getName() + " running on price on " + priceRatePair.getCcyPair() + " and " + priceRatePair.getRate());
                priceProcessor.onPrice(priceRatePair.getCcyPair(), priceRatePair.getRate());
            }
        } catch (InterruptedException e) {
            //System.out.println(e); add logger
        }
    }

    public void cancel(){
        Thread.currentThread().interrupt();
    }
}