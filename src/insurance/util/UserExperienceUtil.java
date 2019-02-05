package insurance.util;

import insurance.common.RateAlgorithm;
import insurance.contract.*;
import insurance.contract.type.CarContract;
import insurance.contract.type.HealthContract;
import insurance.contract.type.HouseContract;

import java.util.ArrayList;
import java.util.List;

import static insurance.common.ActionEnum.*;
import static insurance.common.RateAlgorithm.*;
import static insurance.util.ConsoleUtil.*;
import static insurance.util.ConsoleUtil.println;

public final class UserExperienceUtil
{
  private static final String POSITIVE_NATURAL_NUMBER_ERROR = "Пожалуйста, введите целое позитивное число большее нуля: ";
  private static final String POSITIVE_DECIMAL_NUMBER_ERROR = "Пожалуйста, введите позитивное (можно десятичное) число большее нуля: ";

  private UserExperienceUtil()
  {
  }

  public static void showWelcomeText()
  {
    println("Добро пожаловать в программу страхования!\n" +
        "В данной программе вы сможете моделировать сутиации страхования населения по трём направлениям:\n" +
        "- страхование жилища;\n" +
        "- страхование автомобиля;\n" +
        "- страхование здоровья.\n" +
        "Итак, давайте проинициализируем все необходимые данные для работы программы. Начнём...\n");
  }

  public static void initializeBudget()
  {
    print("Введите бюджет вашей компании: ");
    RateAlgorithm.initializeBudget(readPositiveDecimalNumberMoreThanZero(POSITIVE_DECIMAL_NUMBER_ERROR));
  }

  public static void initializeContracts(ContractsInfo contractsInfo)
  {
    List<Contract> currentContracts = new ArrayList<>();

    currentContracts.add(initializeContract(new HouseContract()));
    currentContracts.add(initializeContract(new CarContract()));
    currentContracts.add(initializeContract(new HealthContract()));

    println("\nСпасибо, все необходимые данные предзаполнены!");

    contractsInfo.setCurrentContracts(currentContracts);
  }

  public static void processUserAction(ContractsInfo contractsInfo)
  {
    int action = readNaturalNumber(POSITIVE_NATURAL_NUMBER_ERROR);

    if (SHOW_STATISTIC.getValue() == action)
    {
      showStatistic(contractsInfo);
    }
    else if (CHANGE_CONTRACT_CONDITIONS.getValue() == action)
    {
      initializeContracts(contractsInfo);
    }
    else if (EXECUTE_ONE_MONTH.getValue() == action)
    {
      executeOneStep(contractsInfo);
    }
    else if (EXECUTE_ONE_MONTH_SHOW_STATISTIC_EXIT.getValue() == action)
    {
      executeOneStep(contractsInfo, EXIT);
      showStatistic(contractsInfo);
      showExpandedStatistic(contractsInfo);

      System.exit(0);
    }
    else if (EXIT.getValue() == action)
    {
      System.exit(0);
    }

    showMainMenu();
    processUserAction(contractsInfo);
  }

  public static void showMainMenu()
  {
    println("\nСейчас вы находитесь в главном меню программы: ");
    println(" " + SHOW_STATISTIC.getValue() + ". Показать статистику.");
    println(" " + CHANGE_CONTRACT_CONDITIONS.getValue() + ". Изменить условия контрактов.");
    println(" " + EXECUTE_ONE_MONTH.getValue() + ". Выполнить один месяц.");
    println(" " + EXIT.getValue() + ". Выход: Выполнить один месяц, показать статистику и завершить программу.");
    //println("\n " + EXIT.getValue() + ". Выход.");
    println("\nВаше действие (число 1-4 или 0 для выхода): ");
  }

  public static void showExpandedStatistic(ContractsInfo contractsInfo)
  {
    List<Contract> carContracts = new ArrayList<>();
    List<Contract> healthContracts = new ArrayList<>();
    List<Contract> houseContracts = new ArrayList<>();

    populateCategories(contractsInfo, carContracts, healthContracts, houseContracts);

    println("\n*** Статистика на последний завершенный " + contractsInfo.getCurrentMonth() + "мес. *** ");
    println("\nБюджет вашей компании: ---> " + getBudgetDisplay() + " <---");

    showExpandedStatisticByCategory(houseContracts);
    showExpandedStatisticByCategory(carContracts);
    showExpandedStatisticByCategory(healthContracts);
  }

  public static void populateCategories(
      ContractsInfo contractsInfo,
      List<Contract> carContracts,
      List<Contract> healthContracts,
      List<Contract> houseContracts)
  {
    for (Contract contract : contractsInfo.getAllContracts())
    {
      if (contract instanceof CarContract)
      {
        carContracts.add(contract);
      }
      else if (contract instanceof HealthContract)
      {
        healthContracts.add(contract);
      }
      else if (contract instanceof HouseContract)
      {
        houseContracts.add(contract);
      }
    }
  }

  private static Contract initializeContract(Contract contract)
  {
    print("\nВведите длительность договора (кол-во месяцев) по категории \"" + contract.getInsuranceType() + "\": ");
    int contactDuration = readNaturalNumber(POSITIVE_NATURAL_NUMBER_ERROR);
    contract.setContractDuration(contactDuration);
    contract.setExpiresIn(contactDuration);

    print(
        "Введите стоимость договора за " + contract.getContractDuration() + " месяцев по категории \"" +
            contract.getInsuranceType() + "\": ");
    contract.setContractPrice(readPositiveDecimalNumberMoreThanZero(POSITIVE_DECIMAL_NUMBER_ERROR));

    print("Введите максимально возможную выплату по страховому случаю: ");
    contract.setMaxPayoutSum(readPositiveDecimalNumberMoreThanZero(POSITIVE_DECIMAL_NUMBER_ERROR));

    return contract;
  }

  private static void showStatistic(ContractsInfo contractsInfo)
  {
    println("\n*** Статистика на " + contractsInfo.getCurrentMonth() + "мес. *** ");
    println("\nБюджет вашей компании на текущий момент: ---> " + getBudgetDisplay() + " <---");

    for (Contract contract : contractsInfo.getCurrentContracts())
    {
      println("\n" + contract.getInsuranceType() + ": ");
      showCurrentContractValues(contract);
    }
  }

  private static void showExpandedStatisticByCategory(List<Contract> contracts)
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

    println("\nСтатистика по категории \"" + insuranceType + "\":");
    println("- кол-во всех произошедших страховых случаев: " + totalInsuranceCases);
    println("- суммарная стоимость всех выплат за все страховые случаи: " + totalPayoffs);
    println("- суммарное поступление денег в бюджет по этой категории: " +
        DECIMAL_FORMAT.format(totalEarn * RateAlgorithm.TAX_COEFFICIENT));
    println("- средняя выплата по этой категории: " +
        (totalInsuranceCases != 0 ? (totalPayoffs / totalInsuranceCases) : 0));
  }

  private static void showCurrentContractValues(Contract contract)
  {
    println("- текущая длительность договора: " + contract.getContractDuration() + " мес.");
    println("- текущая стоимость договора: " + contract.getContractPriceDisplay() + " за " +
        contract.getContractDuration() +
        " мес.");
    println("- максимально возможная выплата по страховому случаю " + contract.getMaxPayoutSumDisplay());
    println("- текущий спрос " + contract.getDemand() + " чел.");
  }

}
