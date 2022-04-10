package com.abm2.UI;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abm2.Database.DateConverter;
import com.abm2.Database.Repository;
import com.abm2.Entity.Note;
import com.abm2.R;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    //Declare CourseViewHolder inner class
    class NoteViewHolder extends RecyclerView.ViewHolder {
        private final TextView noteItemView;
        private NoteViewHolder(View itemView) {
            super(itemView);
            noteItemView = itemView.findViewById(R.id.textListItem);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //Get the current term that has been clicked using index of list (position)
                    int position = getAdapterPosition();
                    final Note currentNote = allNotes.get(position);
                    Intent intent = new Intent(context, NoteDetails.class);
                    intent.putExtra("noteId", currentNote.getNoteId());
                    intent.putExtra("text", currentNote.getText());
                    intent.putExtra("courseId", currentNote.getCourseId());
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Note> allNotes;
    private final Context context;
    private final LayoutInflater inflater;

    //Class constructor
    public NoteAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the list item
        View itemView = inflater.inflate(R.layout.note_list_item, parent, false);
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteAdapter.NoteViewHolder holder, int position) {
        //Used to put things on the text view
        if (allNotes != null) {
            Note currentNote = allNotes.get(position);
            String text = currentNote.getText();
            holder.noteItemView.setText(text);
        }
        else {
            holder.noteItemView.setText("No terms to display");
        }

    }

    public void setNotes(List<Note> notes) {
        allNotes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (allNotes != null) {
            return allNotes.size();
        }
        else {
            return 0;
        }
    }
}
