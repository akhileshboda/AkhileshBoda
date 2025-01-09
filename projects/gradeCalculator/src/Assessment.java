public class Assessment
{
    // Fields
    private String name; //optional
    private double grade;
    private double weighting;

    // -- CONSTRUCTORS --
    // Two parameter constructor
    public Assessment(double grade, double weighting) {
        this.grade = grade;
        this.weighting = weighting;
    }

    // -- GETTERS --

    public String getName()
    {
        return name;
    }

    public double getGrade()
    {
        return grade;
    }

    public double getWeighting()
    {
        return weighting;
    }

    // -- SETTERS --

    public void setName(String name)
    {
        this.name = name;
    }
}
