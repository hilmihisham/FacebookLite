class Profile
{
    private User user;  // declared
    private Wall wall;
    private Friends friends;
    
    public Profile(String firstName , String lastName , int age)
    {
        user = new User(firstName , lastName , age);  // initialized by constructor
        wall = new Wall();
        friends = new Friends();
    }
    
    public void printProfile()
    {
        // call all display() methods for user , wall , friends
        user.display();
        wall.display();
        friends.display();
    }
    
    public void toggleAge()
    {
        user.toggleVis();
    }
    
    public void toggleWall()
    {
        wall.toggleVis();
    }
    
    public void toggleFriends()
    {
        friends.toggleVis();
    }
    
    public void setStatus(String status)
    {
        user.setStatus(status);
    }
    
    public User getUser()
    {
        return user;
    }
    
    public void post(String input)
    {
        wall.addPost(input);
    }
    
    public Friends getFriends()
    {
        return friends;
    }
    
    public Wall getWall()
    {
        return wall;
    }
}