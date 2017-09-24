package com.shwetabh.ghost.studentdemo.adater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.shwetabh.ghost.studentdemo.R;
import com.shwetabh.ghost.studentdemo.model.Details;

import java.util.List;

public class ListViewAdapter extends ArrayAdapter<Details> {
 
    //the hero list that will be displayed
    private List<Details> detailsList;
    
    //the context object
    private Context mCtx;
 
    //here we are getting the herolist and context 
    //so while creating the object of this adapter class we need to give herolist and context
    public ListViewAdapter(List<Details> detailsList, Context mCtx) {
        super(mCtx, R.layout.list_items, detailsList);
        this.detailsList = detailsList;
        this.mCtx = mCtx;
    }
 
    //this method will return the list item 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //getting the layoutinflater
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        
        //creating a view with our xml layout
        View listViewItem = inflater.inflate(R.layout.list_items, null, true);
 
        //getting text views
        TextView textViewName = listViewItem.findViewById(R.id.textViewName);
        TextView textView_id = listViewItem.findViewById(R.id.stu_id);
        TextView textView_marks = listViewItem.findViewById(R.id.stu_marks);
        TextView textView_roll = listViewItem.findViewById(R.id.stu_roll);
        TextView textView_class= listViewItem.findViewById(R.id.stu_class);
        TextView textView_subject = listViewItem.findViewById(R.id.stu_subject);
 
        //Getting the details for the specified position
        Details details = detailsList.get(position);
 
        //setting details values to textviews
        textViewName.setText("Name: " + details.getName());
        textView_id.setText("Id: "+ details.getId());
        textView_marks.setText("Marks: "+ details.getMarks());
        textView_roll.setText("Rank: "+ details.getRoll());
        textView_class.setText("Class: "+ details.getClas());
        textView_subject.setText("Subject: "+ details.getSubject());

 
        //returning the listitem 
        return listViewItem;
    }

}