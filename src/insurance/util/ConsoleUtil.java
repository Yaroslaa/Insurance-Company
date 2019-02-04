package insurance.util;

import java.util.Scanner;

public final class ConsoleUtil
{
  private ConsoleUtil()
  {
  }

  public static int readNaturalNumber(String errorMessage)
  {
    Scanner sc = new Scanner(System.in);
    int number;

    do
    {
      while (!sc.hasNextInt())
      {
        print(errorMessage);
        sc.next();
      }

      number = sc.nextInt();

      if (number <= 0)
      {
        print(errorMessage);
      }
    }
    while (number <= 0);

    return number;
  }

  public static double readPositiveDecimalNumberMoreThanZero(String errorMessage)
  {
    Scanner sc = new Scanner(System.in);
    double number;

    do
    {
      while (!sc.hasNextDouble())
      {
        print(errorMessage);
        sc.next();
      }

      number = sc.nextDouble();

      if (number <= 0)
      {
        print(errorMessage);
      }
    }
    while (number <= 0);

    return number;
  }

  public static void println(String text)
  {
    System.out.println(text);
  }

  public static void print(String text)
  {
    System.out.print(text);
  }
}
