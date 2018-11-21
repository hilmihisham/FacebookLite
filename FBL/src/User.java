public class User extends Person implements IDisplay
{
    private String status;
    private boolean isAgeVis;
    
    public User(String firstName , String lastName , int age)
    {
        super(firstName , lastName , age);
        status = "";
        isAgeVis = true;
    }
    
    public void display()
    {
        Util.print(getFirstName() + " " + getLastName());
        if (isAgeVis)
        {
            Util.print(String.valueOf(getAge()) + " Years old");
        }
        
        Util.print("Status: " + getStatus());
    }
    
    public void toggleVis()
    {
        isAgeVis = !isAgeVis;
    }
    
    // getter
    public String getStatus()
    {
        return status;
    }
    
    // setter
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getFullName()
    {
        return getFirstName() + " " + getLastName();
    }
    
    public String getFormattedInfo()
    {
        return getFirstName() + ";;" + getLastName() + ";;" + getAge();
    }
    
    public boolean ageVis()
    {
        return isAgeVis;
    }
}