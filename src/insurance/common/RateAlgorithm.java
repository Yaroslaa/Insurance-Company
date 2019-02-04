package insurance.common;

import insurance.contract.Contract;
import insurance.contract.ContractsInfo;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.List;

import static insurance.util.ConsoleUtil.println;
import static insurance.util.FileUtil.writeToFile;
import static insurance.util.UserExperienceUtil.showExpandedStatistic;

public class RateAlgorithm
{
  public static final Format DECIMAL_FORMAT = new DecimalFormat("#0.00");
  public static final double TAX_COEFFICIENT = 0.91;
  private static final int PERCENT_PROBABILITY_OF_INSURANCE_EVENT = 25;
  private static double budget;

  public static void executeOneStep(ContractsInfo contractsInfo, ActionEnum action)
  {
    contractsInfo.incrementCurrentMonth();

    reduceContractsDuration(contractsInfo);

    makeFeeByContractsToBudget(contractsInfo.getCurrentContracts());

    addCurrentContractsToAll(contractsInfo);

    coverInsuranceEvents(contractsInfo.getAllContracts());

    writeToFile(contractsInfo, action);

    checkBudgetAndExitIfNegative(contractsInfo);

    checkForExit();
  }

  public static void executeOneStep(ContractsInfo contractsInfo)
  {
    executeOneStep(contractsInfo, null);
  }

  public static void initializeBudget(double budget)
  {
    RateAlgorithm.budget = budget;
  }

  public static String getBudgetDisplay()
  {
    return DECIMAL_FORMAT.format(budget);
  }

  public static boolean isBudgetLessThanZero()
  {
    return budget < 0;
  }

  private static void addCurrentContractsToAll(ContractsInfo contractsInfo)
  {
    contractsInfo.getAllContracts().addAll(contractsInfo.getCurrentContracts());
  }

  private static void checkForExit()
  {
    if (budget < 0)
    {
      System.exit(0);
    }
  }

  private static void reduceContractsDuration(ContractsInfo contractsInfo)
  {
    contractsInfo.getCurrentContracts()
        .forEach(Contract::decrementExpiration);
    contractsInfo.getAllContracts()
        .forEach(Contract::decrementExpiration);
  }

  private static void makeFeeByContractsToBudget(List<Contract> contracts)
  {
    budget += findAmountPaidByClientsForContracts(contracts);
  }

  private static double findAmountPaidByClientsForContracts(List<Contract> contracts)
  {
    return contracts.stream()
        .map(Contract::getContactPaymentToBudget)
        .mapToDouble(Double::doubleValue)
        .sum();
  }

  private static void coverInsuranceEvents(List<Contract> contracts)
  {
    contracts.forEach(RateAlgorithm::coverInsuranceEvent);
  }

  private static void coverInsuranceEvent(Contract contract)
  {
    if (contract.isExpired())
    {
      return;
    }

    int demand = contract.getDemand();
    for (int i = 0; i < demand; i++)
    {
      if (Math.random() * 100 < PERCENT_PROBABILITY_OF_INSURANCE_EVENT)
      {
        double currentPayout = getCurrentPayout(contract);
        contract.getPayouts().add(currentPayout);

        budget = budget - currentPayout;
      }
    }
  }

  /*
    Generates a payout amount depending on the amount of damage calculated as
    a dynamic value that represents a percent (in a range 20-100) of maximum payout

    @param contract represents any contract type
  */
  private static double getCurrentPayout(Contract contract)
  {
    // dynamic number from 20 to 100
    double dynamicNumberFrom20To100 = 20 + Math.random() * 80;

    return contract.getMaxPayoutSum() * dynamicNumberFrom20To100 / 100;
  }

  private static void checkBudgetAndExitIfNegative(ContractsInfo contractsInfo)
  {
    if (budget < 0)
    {
      println("\nК сожалению, закончились все деньги в бюджете =(" +
          "\nПопробуйте сыграть ещё раз и может всё получится!\n");

      showExpandedStatistic(contractsInfo);
    }
  }

}
