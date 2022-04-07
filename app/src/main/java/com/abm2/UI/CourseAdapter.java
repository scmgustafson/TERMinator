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
import com.abm2.Entity.Course;
import com.abm2.R;

import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseViewHolder> {

    //Declare CourseViewHolder inner class
    class CourseViewHolder extends RecyclerView.ViewHolder {
        private final TextView courseItemView;
        private CourseViewHolder(View itemView) {
            super(itemView);
            courseItemView = itemView.findViewById(R.id.textListItem);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    //Get the current term that has been clicked using index of list (position)
                    int position = getAdapterPosition();
                    final Course currentCourse = allCourses.get(position);
                    Intent intent = new Intent(context, CourseDetailsAssessmentList.class);
                    intent.putExtra("id", currentCourse.getCourseId());
                    intent.putExtra("term", currentCourse.getTermId()); //TODO IMPLEMENT RETURN OF COURSE NAME FROM ID VIA QUERY?
                    intent.putExtra("title", currentCourse.getTitle());
                    intent.putExtra("startDateLong", DateConverter.toTimestamp(currentCourse.getStartDate()));
                    intent.putExtra("endDateLong", DateConverter.toTimestamp(currentCourse.getEndDate()));
                    intent.putExtra("status", currentCourse.getStatus());
                    intent.putExtra("instructorName", currentCourse.getInstructorName());
                    intent.putExtra("instructorEmail", currentCourse.getInstructorEmail());
                    intent.putExtra("instructorPhone", currentCourse.getInstructorPhone());
                    intent.putExtra("notes", currentCourse.getNotes()); //TODO IMPLEMENT LIST OF NOTES
                    context.startActivity(intent);
                }
            });
        }
    }

    private List<Course> allCourses;
    private final Context context;
    private final LayoutInflater inflater;

    //Class constructor
    public CourseAdapter(Context context) {
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public CourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate the list item
        View itemView = inflater.inflate(R.layout.assessment_list_item, parent, false);
        return new CourseViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.CourseViewHolder holder, int position) {
        //Used to put things on the text view
        if (allCourses != null) {
            Course currentCourse = allCourses.get(position);
            String title = currentCourse.getTitle();
            holder.courseItemView.setText(title);
        }
        else {
            holder.courseItemView.setText("No terms to display");
        }

    }

    public void setCourses(List<Course> courses) {
        allCourses = courses;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (allCourses != null) {
            return allCourses.size();
        }
        else {
            return 0;
        }
    }
}
