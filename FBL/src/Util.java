class Util
{
    public static void print(String s)
    {
        System.out.println(s);
    }
    
    public static void init(String[] a)
    {
        for(int x = 0; x < a.length ; x++)
        {
            a[x] = "";
        }
    }
    
    public static void init(Profile[] a)
    {
        for(int x = 0; x < a.length ; x++)
        {
            a[x] = null;
        }
    }
    
    public static void print(String[] arr)
    {
        for (int x = 0 ; x < arr.length ; x++)
        {
            if (arr[x].equals(""))
            {
                break;
            }
            
            System.out.println(arr[x]);
        }
    }
}