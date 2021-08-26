public interface PriceProcessor {

    void onPrice(String ccPair, double rate);

    void subscribe(PriceProcessor priceProcessor);

    void unsubscribe(PriceProcessor priceProcessor);
}
