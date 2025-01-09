public class Main
{
    public static void main(String[] args)
    {
        // Instantiate Scanner object
        Scanner scanner = new Scanner(System.in);
        // Instantiate Assessment ArrayList to hold assessment details
        ArrayList<Assessment> assessments = new ArrayList<Assessment>();

        System.out.println("Grade Calculator");
        System.out.println("Press CTRL + C at any point to terminate the program");

        // ENTER GRADE FOR A1
        String name;
        double grade = 0;
        boolean validGrade = false;
        System.out.println("Please enter the details for the first assessment.");

        // Input validation and error handling for user entering A1 details
        while (!validGrade)
        {
            try
            {
                System.out.print("Name (optional): ");
                name = scanner.nextLine();

                System.out.print("Grade: ");
                grade = scanner.nextDouble();
                validGrade = errorHandling.checkGrade(grade);
            } catch (InputMismatchException e)
            {
                System.out.println("Please enter the score correctly");
                scanner.next();
            }
        }

        // Check for any further potential grades

        // Close scanner to release resources
        scanner.close();

    }
}