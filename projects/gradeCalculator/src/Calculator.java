import java.util.ArrayList;
public class Calculator
{
    // class fields
    private ArrayList<Assessment> assessments;

    // -- CONSTRUCTORS --
    // Default constructor
    public Calculator() {
        assessments = new ArrayList<>();
    }

    // -- METHODS --
    public boolean runCalculator() {
        // Print current Assessment information
        if (assessments.isEmpty())
        {
            System.out.println(Utility.YELLOW + "There are currently no assessments loaded in the calculator." + Utility.RESET + "\n");
        }
        // Display calculator menu options
        displayMenu();

        return false;
    }

    public void displayMenu() {
        System.out.println("1. Add new assessment to the calculator");
        System.out.println("2. Delete assessment from the calculator");
        System.out.println("3. Modify an assessment in the calculator");
    }



}
