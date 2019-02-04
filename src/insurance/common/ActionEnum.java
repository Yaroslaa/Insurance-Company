package insurance.common;

public enum ActionEnum
{
  EXIT(4),
  SHOW_STATISTIC(1),
  CHANGE_CONTRACT_CONDITIONS(2),
  EXECUTE_ONE_MONTH(3),
  EXECUTE_ONE_MONTH_SHOW_STATISTIC_EXIT(4);

  private final int value;

  ActionEnum(final int newValue)
  {
    value = newValue;
  }

  public int getValue()
  {
    return value;
  }
}
