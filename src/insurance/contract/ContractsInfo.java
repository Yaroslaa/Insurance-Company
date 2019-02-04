package insurance.contract;

import java.util.ArrayList;
import java.util.List;

public class ContractsInfo
{
  private int currentMonth;

  /*
    Contracts which are available for current month
   */
  private List<Contract> currentContracts = new ArrayList<>();

  /*
    All signed contracts by clients
   */
  private List<Contract> allContracts = new ArrayList<>();

  public int getCurrentMonth()
  {
    return currentMonth;
  }

  public void incrementCurrentMonth()
  {
    this.currentMonth++;
  }

  public List<Contract> getCurrentContracts()
  {
    return currentContracts;
  }

  public void setCurrentContracts(List<Contract> currentContracts)
  {
    this.currentContracts = currentContracts;
  }

  public List<Contract> getAllContracts()
  {
    return allContracts;
  }

  public void setAllContracts(List<Contract> allContracts)
  {
    this.allContracts = allContracts;
  }
}
