package com.example.admin.janjaruka;

/**
 * Created by Admin on 24/05/2017.
 */

public class Law_categories {
    public int category_icon;
    public Integer category_id;
    public String category_text;

    public Law_categories() {
        super();
    }

    public Law_categories(Integer category_id, String category_text, int category_icon) {
        super();
        this.category_id = category_id;
        this.category_text = category_text;
        this.category_icon = category_icon;
    }
}
