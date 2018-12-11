import org.bson.Document;

public class UserDoc{
    public Document doc;

    public String getFirstName(){
        return doc.getString("firstName");
    }

    public String getLastName(){
        return doc.getString("lastName");
    }

    public int getAge(){
        return doc.getInteger("age");
    }

    public String getStatus(){
        return doc.getString("status");
    }

    public boolean getHideFriends(){
        return doc.getBoolean("hidefriends");
    }

    public boolean getHidePosts(){
        return doc.getBoolean("hideposts");
    }

    public boolean getHideAge(){
        return doc.getBoolean("hideage");
    }

    public boolean getHideStatus(){
        return doc.getBoolean("hidestatus");
    }
}
