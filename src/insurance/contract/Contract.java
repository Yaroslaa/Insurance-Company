package insurance.contract;

import static insurance.common.RateAlgorithm.DECIMAL_FORMAT;

import insurance.common.RateAlgorithm;

import java.util.List;

public interface Contract
{
  void setContractDuration(int contractDuration);

  int getContractDuration();

  void setContractPrice(double contractPrice);

  double getContractPrice();

  default String getContractPriceDisplay()
  {
    return DECIMAL_FORMAT.format(getContractPrice());
  }

  String getInsuranceType();

  void setExpiresIn(int expiresIn);

  int getExpiresIn();

  default boolean isExpired()
  {
    return getExpiresIn() < 0;
  }

  default void decrementExpiration()
  {
    if (!isExpired())
    {
      setExpiresIn(getExpiresIn() - 1);
    }
  }

  default int getDemand()
  {
    return (int) (getMaxPayoutSum() / getContractPrice());
  }

  void setMaxPayoutSum(double maxPayoutSum);

  double getMaxPayoutSum();

  default String getMaxPayoutSumDisplay()
  {
    return DECIMAL_FORMAT.format(getMaxPayoutSum());
  }

  List<Double> getPayouts();

  default double getContactPaymentToBudget()
  {
    return getContractPrice() * getDemand() * RateAlgorithm.TAX_COEFFICIENT;
  }

}
