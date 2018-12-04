import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FBLManager {

    private Profile[] profiles;
    private int index;
    private int nop; // number of profiles

    public FBLManager()
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
}
