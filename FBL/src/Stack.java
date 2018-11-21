class Stack
{
    private String[] stack;
    private int index;
    
    public Stack(int size)
    {
        stack = new String[size];
        index = -1;
        Util.init(stack);
    }
    
    public void push(String item)
    {
        if (!item.equals("")) //prevent user from adding blank post
        {
            if (!isFull())
            {
                stack[++index] = item;
            }
            else
            {
                Util.print("Unable to add");
            }
        }
        else
        {
            Util.print("input empty");
        }
    }
    
    public void pop()
    {
        if (!isEmpty())
        {
            stack[index--] = "";
        }
        else
        {
            Util.print("Nothing to delete");
        }
    }
    
    public void clear()
    {
        Util.init(stack);
        index = -1;
    }
    
    public String[] getElements()
    {
        return stack;
    }
    
    public boolean isEmpty()
    {
        return index == -1;
    }
    
    public boolean isFull()
    {
        return index >= (stack.length -1);
    }
}