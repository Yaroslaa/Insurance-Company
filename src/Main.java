import insurance.contract.ContractsInfo;


import static insurance.util.UserExperienceUtil.*;

public class Main
{

  public static void main(String[] args)
  {
    showWelcomeText();
    initializeBudget();

    ContractsInfo contractsInfo = new ContractsInfo();
    initializeContracts(contractsInfo);

    showMainMenu();
    processUserAction(contractsInfo);
  }
}
