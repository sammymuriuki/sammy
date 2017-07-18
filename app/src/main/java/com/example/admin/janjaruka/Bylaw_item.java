package com.example.admin.janjaruka;

/**
 * Created by Admin on 30/05/2017.
 */

public class Bylaw_item {
    public String bylaw_text, penalty;
    public Integer bylaw_id, category_id;
    public Bylaw_item(){
        super();
    }

    public Bylaw_item(Integer bylaw_id, Integer category_id, String bylaw_text, String penalty){
        super();
        this.bylaw_id = bylaw_id;
        this.bylaw_text = bylaw_text;
        this.category_id = category_id;
        this.penalty = penalty;
    }
}
