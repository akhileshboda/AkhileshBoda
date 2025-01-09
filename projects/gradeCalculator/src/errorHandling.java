public class errorHandling
{
    public static boolean checkGrade(double grade) {
        if (grade > 0 && grade < 100)
            return true;
        else {
            System.out.println("Please enter a grade between 0 and 100.");
            return false;
        }
    }

    public static boolean checkWeighting(double weighting) {
        if (weighting > 0 && weighting < 100)
            return true;
        else {
            System.out.println("Please enter a weighting between 0 and 100.");
            return false;
        }
    }
}
