package com.insighters.ash.note_maker.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.insighters.ash.note_maker.R;
import com.insighters.ash.note_maker.activities.ViewNote;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Akshay on 14/12/17.
 *
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    private Context context;
    private Map<String,Long> noteList;
    private ArrayList<String> noteTitles;
    private ArrayList<Long> noteIDs;

    public class NotesViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView data;
        public Long noteID;
        public CardView cardView;

        public NotesViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.note_title);
            data = (TextView)itemView.findViewById(R.id.note_data);
            cardView = (CardView)itemView.findViewById(R.id.card_view_note_list);
        }



    }

    public NotesAdapter (Context context, Map<String,Long> noteList) {
    this.context = context;
    this.noteList = noteList;
    setNoteIDs();
    setNoteTitles();

    }

    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_list_cardview,parent,false);
        return new NotesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        holder.noteID = noteIDs.get(position);

        String title = noteTitles.get(position);
        holder.title.setText(title);
        holder.cardView.setTag(noteIDs.get(position));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,ViewNote.class);
                // String sending=(String)listView.getItemAtPosition(i);
                Long sending = (Long) view.getTag();
                intent.putExtra("ID_TO_DISPLAY",sending);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public ArrayList<String> getNoteTitles() {
        return noteTitles;
    }

    public void setNoteTitles() {

        this.noteTitles = new ArrayList<>(noteList.keySet());
    }

    public ArrayList<Long> getNoteIDs() {
        return noteIDs;
    }

    public void setNoteIDs() {
        this.noteIDs = new ArrayList<>(noteList.values());
    }
}
