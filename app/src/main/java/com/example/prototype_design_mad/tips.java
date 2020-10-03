package com.example.prototype_design_mad;

public class tips
{
    private String Caption, Category, Description, Tip_Image;

    public tips()
    {

    }

    public tips(String caption, String category, String description, String tip_Image)
    {
        Caption = caption;
        Category = category;
        Description = description;
        Tip_Image = tip_Image;
    }

    public String getCaption()
    {
        return Caption;
    }

    public void setCaption(String caption)
    {
        Caption = caption;
    }

    public String getCategory()
    {
        return Category;
    }

    public void setCategory(String category)
    {
        Category = category;
    }

    public String getDescription()
    {
        return Description;
    }

    public void setDescription(String description)
    {
        Description = description;
    }

    public String getTip_Image()
    {
        return Tip_Image;
    }

    public void setTip_Image(String tip_Image)
    {
        Tip_Image = tip_Image;
    }

}
