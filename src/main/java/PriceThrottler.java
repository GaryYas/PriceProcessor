import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.ReentrantLock;


public class PriceThrottler implements PriceProcessor, AutoCloseable {

    private Map<PriceProcessor, BlockingDeque<PriceRatePair>> proccessorsQueueByPriceProcessor = new ConcurrentHashMap<>();
    private Set<PriceProcessor> alreadyRunPricesProcessorSet = ConcurrentHashMap.newKeySet();
    private ExecutorService executor = Executors.newCachedThreadPool();
    private ReentrantLock reentrantLock = new ReentrantLock();
    private static final Logger logger = LogManager.getLogger(PriceThrottler.class);


    @Override
    public void onPrice(String ccPair, double rate) {
        proccessorsQueueByPriceProcessor.forEach((priceProcessor, queue) -> {
            queue.add(new PriceRatePair(ccPair, rate));

            //double lock checking
            if (!alreadyRunPricesProcessorSet.contains(priceProcessor)) {
                reentrantLock.lock();
                if (!alreadyRunPricesProcessorSet.contains(priceProcessor)) {
                    executor.execute(new ProcessorRunner(priceProcessor, queue));
                    alreadyRunPricesProcessorSet.add(priceProcessor);
                }
                reentrantLock.unlock();
            }

        });
    }


    @Override
    public void subscribe(PriceProcessor priceProcessor) {
        proccessorsQueueByPriceProcessor.put(priceProcessor, new LinkedBlockingDeque<>());
        logger.info(priceProcessor.getClass().getName() + " subscribed to the Throttler");
    }

    @Override
    public void unsubscribe(PriceProcessor priceProcessor) {
        proccessorsQueueByPriceProcessor.remove(priceProcessor);
        alreadyRunPricesProcessorSet.remove(priceProcessor);
        logger.info(priceProcessor.getClass().getName() + " unSubscribed from the Throttler");
    }

    @Override
    public void close() throws Exception {
        logger.info("closing Throttler");
        executor.shutdown();
        proccessorsQueueByPriceProcessor.clear();
        alreadyRunPricesProcessorSet.clear();
    }
}
