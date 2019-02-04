package insurance.contract.type;

import insurance.contract.Contract;

import java.util.ArrayList;
import java.util.List;

public class HouseContract implements Contract
{
  private int contractDuration;
  private int expiresIn;
  private double contractPrice;
  private double maxPayoutSum;
  private List<Double> payouts = new ArrayList<>();

  @Override
  public void setContractDuration(int contractDuration)
  {
    this.contractDuration = contractDuration;
  }

  @Override
  public int getContractDuration()
  {
    return contractDuration;
  }

  @Override
  public void setContractPrice(double contractPrice)
  {
    this.contractPrice = contractPrice;
  }

  @Override
  public double getContractPrice()
  {
    return contractPrice;
  }

  @Override
  public String getInsuranceType()
  {
    return "Страхование жилища";
  }

  @Override
  public void setExpiresIn(int expiresIn)
  {
    this.expiresIn = expiresIn;
  }

  @Override
  public int getExpiresIn()
  {
    return expiresIn;
  }

  @Override
  public void setMaxPayoutSum(double maxPayoutSum)
  {
    this.maxPayoutSum = maxPayoutSum;
  }

  @Override
  public double getMaxPayoutSum()
  {
    return maxPayoutSum;
  }

  @Override
  public List<Double> getPayouts()
  {
    return payouts;
  }
}
