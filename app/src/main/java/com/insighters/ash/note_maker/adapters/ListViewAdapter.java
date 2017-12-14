package com.insighters.ash.note_maker.adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.insighters.ash.note_maker.NoteMaker.DBHelper;
import com.insighters.ash.note_maker.activities.SwipeListShowNotes;
import com.insighters.ash.note_maker.R;
import com.insighters.ash.note_maker.activities.AddNew;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends ArrayAdapter<String> {

    private SwipeListShowNotes activity;
    private List<String> titleList;
    private List<Long> idList;
    private Long id_of_note_to_edit;
    private int click_delete_position;
    private boolean todeleteListEntry=false;



    public ListViewAdapter(SwipeListShowNotes context, int resource, ArrayList<String> objects , ArrayList<Long> idList) {
        super(context, resource, objects);
        this.activity = context;
        this.titleList = ((List<String>) objects);
        this.idList= ((List<Long>) idList);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        // If holder not exist then locate all view from UI file.
        if (convertView == null) {
            // inflate UI from XML file
            convertView = inflater.inflate(R.layout.item_listview, parent, false);
            // get all UI view
            holder = new ViewHolder(convertView);
            // set tag for holder
            convertView.setTag(holder);
        } else {
            // if holder created, get tag from view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(getItem(position));

        //handling buttons event
        holder.btnEdit.setOnClickListener(onEditListener(position, holder));
        holder.btnDelete.setOnClickListener(onDeleteListener(position, holder));

        return convertView;
    }

    private View.OnClickListener onEditListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent_edit=new Intent(getContext(),AddNew.class);
                intent_edit.putExtra("column_name",0);
                intent_edit.putExtra("colmun_id_to_edit",idList.get(position));

                Log.i("===ListVA/id_to_edit", String.valueOf(idList.get(position)));
                activity.startActivityForResult(intent_edit, 1);
            }
        };
    }

    /**
     * Editting confirm dialog
     * @param position
     * @param holder
     */


    private View.OnClickListener onDeleteListener(final int position, final ViewHolder holder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                click_delete_position=position;
             Long id_of_note_to_delete=idList.get(position);

                //String sending=(String)listView.getItemAtPosition(i);
                Log.i("===ListVA/idListSize", String.valueOf(idList.size()));
                Log.i("===ListVA/id_to_delete", String.valueOf(id_of_note_to_delete));
                Log.i("===ListVA/delete_pos", String.valueOf(position));



                  android.support.v7.app.AlertDialog diaBox = AskOption();
                  diaBox.show();
/*
                    if(todeleteListEntry)
                    {
                        todeleteListEntry=false;
                        titleList.remove(position);
                    }*/
              // activity.updateAdapter();
                Log.i("===ListVA/afterdelete", "done");
            }
        };
    }

    private class ViewHolder {
        private TextView name;
        private View btnDelete;
        private View btnEdit;
        private SwipeLayout swipeLayout;

        public ViewHolder(View v) {
            swipeLayout = (SwipeLayout)v.findViewById(R.id.swipe_layout);
            btnDelete = v.findViewById(R.id.delete);
            btnEdit = v.findViewById(R.id.edit_query);
            name = (TextView) v.findViewById(R.id.name);

            swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        }
    }

    private android.support.v7.app.AlertDialog AskOption()
    {
        android.support.v7.app.AlertDialog myQuittingDialogBox =new AlertDialog.Builder(getContext())
                //set message, title, and icon
                .setTitle("Delete")
                .setMessage("Do you want to Delete")
                .setIcon(R.mipmap.ic_delete_forever_black_24dp)

                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                       // todeleteListEntry=true;
                        DBHelper dbHelper=new DBHelper(getContext());

                       Long id_of_note_to_delete=idList.get(click_delete_position);
                        dbHelper.delete_note(id_of_note_to_delete);

                        dialog.dismiss();
                      //  if(click_delete_position!=0)
                        titleList.remove(click_delete_position);
                        activity.updateAdapter();
                        dbHelper.close();
                    }

                })



                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                })
                .create();
        return myQuittingDialogBox;

    }//end of alertdialogbox
}//rnd of class
