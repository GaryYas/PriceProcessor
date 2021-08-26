import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceRatePair {

    String ccyPair;
    double rate;

}
