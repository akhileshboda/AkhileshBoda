import java.util.ArrayList;
import java.util.InputMismatchException;

public class Calculator
{
    // global fields
    public static final int minMenuOption = 1;
    public static final int maxMenuOption = 4;
    // instance fields
    private ArrayList<Assessment> assessments;

    // -- CONSTRUCTORS --
    // Default constructor
    public Calculator() {
        assessments = new ArrayList<>();
    }

    // -- METHODS --
    public int runCalculator() {
        boolean calculatorError = false;

        // Print current Assessment information
        if (assessments.isEmpty())
        {
            System.out.println(Utility.YELLOW + "There are currently no assessments loaded in the calculator." + Utility.RESET + "\n");
        }

        // Display calculator menu options
        displayMenu();

        // Get user input
        boolean validInput = false;

        // declare user input
        int userInput;

        // loop to check that user has entered a valid input
        while (!validInput)
        {
            System.out.print("ENTER: ");
            try
            {
                // accept user integer input
                userInput = Utility.scanner.nextInt();

                // change loop condition if user has entered an integer
                validInput = true;
            }
            catch (InputMismatchException e) // catch incorrect input type, prevent program from crashinng
            {
                System.out.println("Please indicate a valid number for your choice.\n");
                Utility.scanner.next();
            }
        }

        user_input:
            switch (userInput)
            {
                case 1:
                    //boolean addAssessment = addAssessment();
                    break;
                case 2:
                    //boolean deleteAssessment = deleteAssessment();
                    break;
                case 3:
                    //boolean modifyAssessment = modifyAssessment();
                    break;
                case 4:
                    break user_input;;
            }

        return 1;
    }
    public void displayMenu() {
        System.out.println("1. Add new assessment to the calculator");
        System.out.println("2. Delete assessment from the calculator");
        System.out.println("3. Modify an assessment in the calculator\n");

        System.out.println("4. Exit the Calculator Program\n");
    }



}
