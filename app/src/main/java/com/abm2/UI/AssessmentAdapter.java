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
import com.abm2.Entity.Assessment;
import com.abm2.R;

import java.util.List;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentViewHolder> {

    //Declare CourseViewHolder inner class
    class AssessmentViewHolder extends RecyclerView.ViewHolder {
        private final TextView assessmentItemView;
        private AssessmentViewHolder(View itemView) {
            super(itemView);
            assessmentItemView = itemView.findViewById(R.id.textListItem);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //Get the current term that has been clicked using index of list (position)
                    int position = getAdapterPosition();
                    final Assessment currentAssessment = allAssessments.get(position);
                    //TODO LEARN MORE ABOUT SENDING OBJECT DATA TO NEXT SCREEN WITH Intent.putExtra()
                    Intent intent = new Intent(context, AssessmentDetails.class);
                    intent.putExtra("title", currentAssessment.getTitle());
                    intent.putExtra("endDate", DateConverter.toTimestamp(currentAssessment.getEndDate()));
                    intent.putExtra("type", currentAssessment.getType());
                    intent.putExtra("course", String.valueOf(currentAssessment.getCourseId()));
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Assessment> allAssessments;
    private final Context context;
    private final LayoutInflater inflater;

    //Class constructor
    public AssessmentAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public AssessmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the list item
        View itemView = inflater.inflate(R.layout.assessment_list_item, parent, false);
        return new AssessmentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentAdapter.AssessmentViewHolder holder, int position) {
        //Used to put things on the text view
        if (allAssessments != null) {
            Assessment currentAssessment = allAssessments.get(position);
            String title = currentAssessment.getTitle();
            holder.assessmentItemView.setText(title);
        }
        else {
            holder.assessmentItemView.setText("No terms to display");
        }

    }

    public void setAssessments(List<Assessment> assessments) {
        allAssessments = assessments;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (allAssessments != null) {
            return allAssessments.size();
        }
        else {
            return 0;
        }
    }
}
