package com.abm2.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.abm2.Database.DateConverter;
import com.abm2.Entity.Term;
import com.abm2.R;

import java.util.List;
import java.util.zip.Inflater;

public class TermAdapter extends RecyclerView.Adapter<TermAdapter.TermViewHolder> {

    //Declare TermViewHolder inner class
    class TermViewHolder extends RecyclerView.ViewHolder {
        private final TextView termItemView;
        private TermViewHolder(View itemView) {
            super(itemView);
            termItemView = itemView.findViewById(R.id.textListItem);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //Get the current term that has been clicked using index of list (position)
                    int position = getAdapterPosition();
                    final Term currentTerm = allTerms.get(position);
                    //TODO LEARN MORE ABOUT SENDING OBJECT DATA TO NEXT SCREEN WITH Intent.putExtra()
                    Intent intent = new Intent(context, TermDetailsCourseList.class);
                    intent.putExtra("id", currentTerm.getTermId());
                    intent.putExtra("title", currentTerm.getTitle());
                    intent.putExtra("startDateLong", DateConverter.toTimestamp(currentTerm.getStartDate()));
                    intent.putExtra("endDateLong", DateConverter.toTimestamp(currentTerm.getEndDate()));
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Term> allTerms;
    private final Context context;
    private final LayoutInflater inflater;

    //Class constructor
    public TermAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public TermViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the list item
        View itemView = inflater.inflate(R.layout.term_list_item, parent, false);
        return new TermViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TermAdapter.TermViewHolder holder, int position) {
        //Used to put things on the text view
        if (allTerms != null) {
            Term currentTerm = allTerms.get(position);
            String title = currentTerm.getTitle();
            holder.termItemView.setText(title);
        }
        else {
            holder.termItemView.setText("No terms to display");
        }

    }

    public void setTerms(List<Term> terms) {
        allTerms = terms;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (allTerms != null) {
            return allTerms.size();
        }
        else {
            return 0;
        }
    }




}
