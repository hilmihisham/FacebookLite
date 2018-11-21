class Friends implements IDisplay
{
    private Stack friends;
    //private String[] friends;
    //private int index;  // keep track of friends --- set to -1 to prevent crashes and know when array is empty because position zero is used by user for friends
    private boolean isfriendsVis;
    private int NOF;  // number of friends
    
    public Friends()
    {
        friends = new Stack(10);
        //index = -1;
        isfriendsVis = true;
        //Util.init(friends);
        NOF = 0;
    }
    /*
    public boolean isEmpty()
    {
        return index == -1;
    }
    
    public boolean isFull()
    {
        return index >= (friends.length -1); // can also remove = and (cont below)
    }
    */
    public void addFriend(String friend)
    {
        /*
        if (!isFull())
        {
            friends[++index] = friend;  // do index++ \n friends[index] = friend;
        }
        */
        friends.push(friend);
        NOF++;
    }
    
    public void deleteAllFriends()
    {
        /*
        Util.init(friends);
        index = -1;
        */
        friends.clear();
        NOF = 0;
    }
    
    public void deleteLastFriend()
    {
        /*
        if (!isEmpty())
        {
            friends[index--] = "";
        }
        */
        friends.pop();
        NOF--;
    }
    
    public void display()
    {
        Util.print("Friends:");
        if (isfriendsVis) //&& !isEmpty())
        {
            /*
            for (int x = 0 ; x <= index ; x++)
            {
                Util.print(friends[x]);
            }
            */
            Util.print(friends.getElements());
        }
        else
        {
            Util.print("No visible friends to Display");
        }
    }
    
    public void toggleVis()
    {
        isfriendsVis = !isfriendsVis;
    }
    
    public int getNOF()
    {
        return NOF;
    }
    
    public String getFormattedFriends(int y)
    {
        //String[10] frnds = friends.getElements();
        return friends.getElements()[y];//frnds[y];
    }
    
    public boolean friendsVis()
    {
        return isfriendsVis;
    }
}

