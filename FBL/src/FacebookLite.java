import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;

class FacebookLite
{
    private Profile[] profiles;
    private int index;
    private int nop; // number of profiles
    
    public FacebookLite()
    {
        profiles = new Profile[10];
        index = -1;
        nop = 0;
    }
    
    public void createProfile(String first , String last , int age)
    {
        index = nop-1;
        Profile p = new Profile(first , last , age);
        if (index < profiles.length)
        {
            index++;
            profiles[index] = p;
            nop++;
        }
        else
        {
            Util.print("Unable to add Profile");
        }
    }
    
    
    public void post(String input)
    {
        if (index >= 0)
        {
            profiles[index].post(input);
        }
    }
    
    public static void printMenu()
    {
        Util.print("0 - exit");
        Util.print("1 - create profile");
        Util.print("2 - delete last profile");
        Util.print("3 - delete all profiles");
        Util.print("4 - switch profile");
        Util.print("5 - print profile");
        Util.print("6 - add friend");
        Util.print("7 - remove last friend");
        Util.print("8 - remove all friends");
        Util.print("9 - post");
        Util.print("10 - remove last post");
        Util.print("11 - remove all posts");
        Util.print("12 - toggle age visibility");
        Util.print("13 - toggle friends visibility");
        Util.print("14 - toggle posts visibility");
        Util.print("15 - change status");
    }
    
    public int getProfileList()
    {
        if (index == -1)
        {
            Util.print("No profiles to switch");
        }
        else if (nop == 1)  // changed from index == 0 because it was causing a bug where wrong text was outputted
        {
            Util.print("Only one profile, cannot switch");
        }
        else
        {
            Util.print("Profiles:");
            for (int x = 0 ; x < nop ; x++)
            {
                Util.print(x + ": " + profiles[x].getUser().getFullName());
            }
            //Util.print("Which profile would you like to switch to.");
        }
        return nop;
    }
    
    public void setProfile(int index)
    {
        this.index = index;
    }
    
    public int getNOPS()
    {
        return nop;
    }
    
    public void deleteAllProfiles()
    {
        Util.init(profiles);
        nop = 0;
        index = -1;
    }
    
    public boolean deleteLastProfile()
    {
        boolean isDeleted = true;
        
        if (nop-1 == index && index != 0)
        {
            Util.print("Please switch profiles then try again");
            isDeleted = false;
        }
        else
        {
            profiles[nop-1] = null;
            nop--;
        }
        
        if (nop == 0 && index == 0)
        {
            index = -1;
        }
        
        return isDeleted;
    }
    
    public void printProfile()
    {
        profiles[index].printProfile();
    }
    
    public void addFriend(String frnd)
    {
        profiles[index].getFriends().addFriend(frnd);
    }
    
    public void removeLastFriend()
    {
        profiles[index].getFriends().deleteLastFriend();
    }
    
    public void removeAllFriends()
    {
        profiles[index].getFriends().deleteAllFriends();
    }
    
    public void removeLastPost()
    {
        profiles[index].getWall().deleteLastPost();
    }
    
    public void removeAllPosts()
    {
        profiles[index].getWall().deleteAllPosts();
    }
    
    public void toggleAge()
    {
        profiles[index].toggleAge();
    }
    
    public void toggleFriends()
    {
        profiles[index].toggleFriends();
    }
    
    public void togglePosts()
    {
        profiles[index].toggleWall();
    }
    
    public void changeStatus(String status)
    {
        profiles[index].setStatus(status);
    }
    
    public String getProfileNameCurrent()
    {
        return profiles[index].getUser().getFullName();
    }
    
    public String getProfileNameLast()
    {
        return profiles[nop-1].getUser().getFullName();
    }
    
    public void saveProfiles(String fileName)
    {
        //Profile profileSaver = new Profile();
        //int start = -1;
        try
        {
            File file = new File(fileName);
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            for(int x = 0 ; x < nop ; x++)
            {
                //profiles[x].saveProfiles(fileName)
                writer.write("//Info//\n" + profiles[x].getUser().getFormattedInfo() + "\n"); //first//last//age
                writer.flush();
                writer.write("//status//\n" + profiles[x].getUser().getStatus() + "\n"); // status
                writer.flush();
                writer.write("//friends//\n");
                writer.flush();
                for(int y = 0 ; y < profiles[x].getFriends().getNOF() ; y++)
                {
                    writer.write(profiles[x].getFriends().getFormattedFriends(y) + "\n"); // friends
                    writer.flush();
                }
                writer.write("--//\n//posts//\n");
                writer.flush();
                for(int z = 0 ; z < profiles[x].getWall().getNOPosts() ; z++)
                {
                    writer.write(profiles[x].getWall().getFormattedPosts(z) + "\n"); // posts
                    writer.flush();
                }
                writer.write("--//\n");
                writer.flush();
                
                if (profiles[x].getUser().ageVis())  //age vis
                {
                    writer.write("showAge\n");
                    writer.flush();
                }
                else
                {
                    writer.write("hideAge\n");
                    writer.flush();
                }
                
                if (profiles[x].getFriends().friendsVis())  //friends vis
                {
                    writer.write("showFriends\n");
                    writer.flush();
                }
                else
                {
                    writer.write("hideFriends\n");
                    writer.flush();
                }
                
                if (profiles[x].getWall().postsVis()) //posts vis
                {
                    writer.write("showPosts\n");
                    writer.flush();
                }
                else
                {
                    writer.write("hidePosts\n");
                    writer.flush();
                }
                
                
            }
            writer.close();
        }
        catch(IOException ex)
        {
            Util.print("Error writing to file: " + fileName);
        }
    }
    
    public void loadProfiles(String fileName)
    {
        String fileLine = "";
        try
        {
            FileReader data = new FileReader(fileName);
            BufferedReader br = new BufferedReader(data);
            while((fileLine = br.readLine()) != null)
            {
                if (fileLine.equals("//Info//"))
                {
                    fileLine = br.readLine();
                    String[] holdInfo = fileLine.split(";;");
                    createProfile(holdInfo[0] , holdInfo[1] , Integer.parseInt(holdInfo[2]));
                    fileLine = br.readLine();
                }
                
                if (fileLine.equals("//status//"))
                {
                    fileLine = br.readLine();
                    changeStatus(fileLine);
                    fileLine = br.readLine();
                }
                
                if (fileLine.equals("//friends//"))
                {
                    while(!(fileLine = br.readLine()).equals("--//"))
                    {
                        addFriend(fileLine);
                    }
                    fileLine = br.readLine();
                }
                
                if (fileLine.equals("//posts//"))
                {
                    while(!(fileLine = br.readLine()).equals("--//"))
                    {
                        post(fileLine);
                    }
                    //fileLine = br.readLine();
                }
                
                fileLine = br.readLine();
                if (fileLine.equals("hideAge"))
                {
                    toggleAge();
                }
                
                fileLine = br.readLine();
                if (fileLine.equals("hideFriends"))
                {
                    toggleFriends();
                }
                
                fileLine = br.readLine();
                if (fileLine.equals("hidePosts"))
                {
                    togglePosts();
                }
                
            }
        }
        catch(FileNotFoundException ex)
        {
            Util.print("File not found: " + fileName);
        }
        catch(IOException ex)
        {
            Util.print("Error reading file: " + fileName);
        }
        
        if (getNOPS() > 0)
        {
            setProfile(0);  // sets profile to first profile after loading all from file
        }
        
    }
    
    public static void main(String[] args)
    {
        
        FacebookLite fbl = new FacebookLite();
        Scanner keyboard = new Scanner(System.in);
        int opt = -1; // option provided by user
        boolean checkMenu = true;
        String fileName = "profile.txt";
        File f = new File(fileName);
        
        if (f.exists())
        {
            Util.print("Profile record found.");
            fbl.loadProfiles(fileName);
        }
        else
        {
            Util.print("No profile record found.");
        }
        
        while(true)
        {
            printMenu();
            try
            {
                opt = Integer.parseInt(keyboard.nextLine()); // needs try catch -- done
                checkMenu = true;
            }
            catch (NumberFormatException NFE)
            {
                Util.print("Enter numbers only");
                checkMenu = false;
            }
            
            if (checkMenu)
            {
                switch(opt)
                {
                    case 0: // exit
                        //if (fbl.getNOPS() > 0)
                        //{
                            fbl.saveProfiles(fileName);
                            Util.print("Profile configuration recorded");
                        //}
                        //else
                        //{
                            //Util.print("No Profiles to save");
                        //}
                        Util.print("Exiting App");
                        break;
                    case 1: // create profile
                        boolean checkFirst = false;
                        boolean checkLast = false;
                        boolean checkAge = false;
                        String first = "";
                        String last = "";
                        int age = 0;
                        while(!checkFirst || !checkLast || !checkAge)
                        {
                            Util.print("Please enter your First Name");
                            first = keyboard.nextLine(); // needs blank line check  --  done
                            if (first.length() == 0 || first.charAt(0) == ' ')
                            {
                                checkFirst = false;
                                Util.print("Invalid entry");
                            }
                            else
                            {
                                checkFirst = true;
                            }
                            
                            if (checkFirst)
                            {
                                Util.print("Please enter your Last Name");
                                last = keyboard.nextLine(); // needs blank line check  -- done
                                if (last.length() == 0 || last.charAt(0) == ' ')
                                {
                                    checkLast = false;
                                    Util.print("Invalid entry");
                                }
                                else
                                {
                                    checkLast = true;
                                }
                            }
                            
                            if (checkFirst && checkLast)
                            {
                                Util.print("Please enter your age");
                                try
                                {
                                age = Integer.parseInt(keyboard.nextLine()); // needs age limiter between 0 and 120 and try catch  -- done and done
                                }
                                catch (NumberFormatException NFE)
                                {
                                    Util.print("Enter numbers only");
                                }
                                if ((age > 0 && age < 120))
                                {
                                    checkAge = true;
                                }
                                else
                                {
                                    checkAge = false;
                                    Util.print("Age must be greater than 0 and less than 120");
                                }
                            }
                        }
                        fbl.createProfile(first , last , age);
                        break;
                    case 2: // delete last profile
                        if (fbl.getNOPS() > 0 && fbl.deleteLastProfile())
                        {
                            //Util.print(fbl.getProfileNameLast() + " profile is Deleted");
                            Util.print("Last profile deleted");
                        }
                        else if (fbl.getNOPS() == 0)
                        {
                            Util.print("No Profiles");
                        }
                        break;
                    case 3: // delete all profiles
                        if (fbl.getNOPS() > 0)
                        {
                            fbl.deleteAllProfiles();
                            Util.print("Profiles cleared");
                        }
                        else
                        {
                            Util.print("No profiles");
                        }
                        break;
                    case 4: // switch profile
                        if (fbl.getProfileList() > 1)
                        {
                            int i = -1;
                            boolean checkSwitch = false;
                            while(!checkSwitch)
                            {
                                Util.print("Which profile would you like to switch to.");
                                try
                                {
                                    i = Integer.parseInt(keyboard.nextLine()); // needs try catch -- done
                                }
                                catch (NumberFormatException NFE)
                                {
                                    Util.print("Invalid input");
                                }
                                
                                if (i > fbl.getNOPS()-1 || i < 0)
                                {
                                    checkSwitch = false;
                                    Util.print("Invalid profile number");
                                }
                                else
                                {
                                    checkSwitch = true;
                                }
                                //Util.print("Which profile would you like to switch to.");
                            }
                            fbl.setProfile(i);
                            Util.print(fbl.getProfileNameCurrent() + " Is the current profile");
                        }
                        break;
                    case 5: // print profile
                        if (fbl.getNOPS() > 0)
                        {
                            fbl.printProfile();
                        }
                        else
                        {
                            Util.print("No Profile");
                        }
                        break;
                    case 6: // add friend
                        if (fbl.getNOPS() > 0)
                        {
                            String frnd = "";
                            boolean checkFrnd = false;
                            while(!checkFrnd)
                            {
                                Util.print("Enter Friend Name");
                                frnd = keyboard.nextLine(); // needs blank line check -- done
                                if (frnd.length() == 0 || frnd.charAt(0) == ' ')
                                {
                                    checkFrnd = false;
                                    Util.print("Invalid input");
                                }
                                else
                                {
                                    checkFrnd = true;
                                }
                            }
                            fbl.addFriend(frnd);
                        }
                        else
                        {
                            Util.print("No Profile");
                        }
                        break;
                    case 7: // remove last friend
                        if (fbl.getNOPS() > 0)
                        {
                            fbl.removeLastFriend();
                        }
                        else
                        {
                            Util.print("No Profile");
                        }
                        break;
                    case 8: // remove all friends
                        if (fbl.getNOPS() > 0)
                        {
                            fbl.removeAllFriends();
                        }
                        else
                        {
                            Util.print("No Profile");
                        }
                        break;
                    case 9: // add post
                        if (fbl.getNOPS() > 0)
                        {
                            String userInput = "";
                            boolean checkPost = false;
                            while(!checkPost)
                            {
                                Util.print("Enter post");
                                userInput = keyboard.nextLine(); // needs blank line check -- done
                                if (userInput.length() == 0 || userInput.charAt(0) == ' ')
                                {
                                    checkPost = false;
                                    Util.print("Invalid input");
                                }
                                else
                                {
                                    checkPost = true;
                                }
                            }
                            fbl.post(userInput);
                        }
                        else
                        {
                            Util.print("No profile to post to");
                        }
                        break;
                    case 10: // remove last post
                        if (fbl.getNOPS() > 0)
                        {
                            fbl.removeLastPost();
                        }
                        else
                        {
                            Util.print("No Profile");
                        }
                        break;
                    case 11: // remove all posts
                        if (fbl.getNOPS() > 0)
                        {
                            fbl.removeAllPosts();
                        }
                        else
                        {
                            Util.print("No Profile");
                        }
                        break;
                    case 12: // toggle age
                        if (fbl.getNOPS() > 0)
                        {
                            fbl.toggleAge();
                        }
                        else
                        {
                            Util.print("No Profile");
                        }
                        break;
                    case 13: // toggle friends
                        if (fbl.getNOPS() > 0)
                        {
                            fbl.toggleFriends();
                        }
                        else
                        {
                            Util.print("No Profile");
                        }
                        break;
                    case 14: // toggle posts
                        if (fbl.getNOPS() > 0)
                        {
                            fbl.togglePosts();
                        }
                        else
                        {
                            Util.print("No Profile");
                        }
                        break;
                    case 15: // change status
                        if (fbl.getNOPS() > 0)
                        {
                            String status = "";
                            boolean checkStatus = false;
                            
                            while(!checkStatus)
                            {
                                Util.print("Enter Status");
                                status = keyboard.nextLine(); // needs blank line check -- done
                                if (status.length() == 0 || status.charAt(0) == ' ')
                                {
                                    checkStatus = false;
                                    Util.print("Invalid input");
                                }
                                else
                                {
                                    checkStatus = true;
                                }
                            }
                            fbl.changeStatus(status);
                        }
                        else
                        {
                            Util.print("No Profile");
                        }
                        break;
                    default:
                        Util.print("Not a supported option");
                        break;
                }
            }
            
            if (opt == 0)
            {
                break;
            }
        }
        
        keyboard.close();
        
        
    }
}



