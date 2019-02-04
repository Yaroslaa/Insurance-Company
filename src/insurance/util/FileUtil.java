package insurance.util;

import insurance.common.ActionEnum;
import insurance.common.RateAlgorithm;
import insurance.contract.Contract;
import insurance.contract.ContractsInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

import static insurance.common.RateAlgorithm.DECIMAL_FORMAT;
import static insurance.common.RateAlgorithm.getBudgetDisplay;
import static insurance.common.RateAlgorithm.isBudgetLessThanZero;
import static insurance.util.ConsoleUtil.println;
import static insurance.util.UserExperienceUtil.populateCategories;

public final class FileUtil
{
  private FileUtil()
  {
  }

  public static void writeToFile(ContractsInfo contractsInfo, ActionEnum action)
  {
    try
    {
      String fileName = "output.txt";
      Path fileToDeletePath = Paths.get(fileName);
      StringBuilder statistic = new StringBuilder(generateStatistic(contractsInfo));

      if (isBudgetLessThanZero() || action == ActionEnum.EXIT)
      {
        generateExpandedStatistic(contractsInfo, statistic);
      }

      boolean fileExists = Files.exists(fileToDeletePath);

      if (contractsInfo.getCurrentMonth() == 1 && fileExists)
      {
        Files.delete(fileToDeletePath);

        Files.write(Paths.get(fileName), statistic.toString().getBytes());
      }
      else if (fileExists)
      {
        Files.write(
            Paths.get(fileName),
            statistic.toString().getBytes(),
            StandardOpenOption.APPEND);
      }
      else
      {
        Files.write(
            Paths.get(fileName),
            statistic.toString().getBytes(),
            StandardOpenOption.CREATE);
      }
    }
    catch (IOException e)
    {
      println("Something went wrong with file writer: " + e.getMessage());
    }
  }

  private static String generateStatistic(ContractsInfo contractsInfo)
  {
    StringBuilder sb = new StringBuilder();

    sb.append("\n\n*** Статистика на " + contractsInfo.getCurrentMonth() + "мес. *** \n")
        .append("\nБюджет вашей компании на текущий момент: ---> " + getBudgetDisplay() + " <---\n");

    for (Contract contract : contractsInfo.getCurrentContracts())
    {
      sb.append("\n" + contract.getInsuranceType() + ": ");
      showCurrentContractValues(contract, sb);
    }

    return sb.toString();
  }

  private static void showCurrentContractValues(Contract contract, StringBuilder sb)
  {
    sb.append("\n- текущая длительность договора: " + contract.getContractDuration() + " мес.")
        .append(
            "\n- текущая стоимость договора: " + contract.getContractPriceDisplay() + " за " +
                contract.getContractDuration() +
                " мес.")
        .append("\n- максимально возможная выплата по страховому случаю " + contract.getMaxPayoutSumDisplay())
        .append("\n- текущий спрос " + contract.getDemand() + " чел.");
  }

  private static void generateExpandedStatistic(ContractsInfo contractsInfo, StringBuilder sb)
  {
    List<Contract> carContracts = new ArrayList<>();
    List<Contract> healthContracts = new ArrayList<>();
    List<Contract> houseContracts = new ArrayList<>();

    populateCategories(contractsInfo, carContracts, healthContracts, houseContracts);

    sb.append("\n\n*** Общая статистика *** ")
        .append("\n\nБюджет вашей компании: ---> " + getBudgetDisplay() + " <---");

    showExpandedStatisticByCategory(houseContracts, sb);
    showExpandedStatisticByCategory(carContracts, sb);
    showExpandedStatisticByCategory(healthContracts, sb);
  }

  private static void showExpandedStatisticByCategory(List<Contract> contracts, StringBuilder sb)
  {
    int totalInsuranceCases = 0;
    int totalPayoffs = 0;
    int totalEarn = 0;
    String insuranceType = contracts.get(0).getInsuranceType();

    for (Contract contract : contracts)
    {
      totalInsuranceCases += contract.getPayouts().size();

      totalPayoffs += contract.getPayouts().stream().mapToDouble(Double::doubleValue).sum();

      totalEarn += contract.getContractPrice() * contract.getDemand();
    }

    sb.append("\nСтатистика по категории \"" + insuranceType + "\":")
        .append("\n- кол-во всех произошедших страховых случаев: " + totalInsuranceCases)
        .append("\n- суммарная стоимость всех выплат за все страховые случаи: " + totalPayoffs)
        .append(
            "\n- суммарное поступление денег в бюджет по этой категории: " +
                DECIMAL_FORMAT.format(totalEarn * RateAlgorithm.TAX_COEFFICIENT))
        .append("\n- средняя выплата по этой категории: " +
            (totalInsuranceCases != 0 ? (totalPayoffs / totalInsuranceCases) : 0));

  }
}
