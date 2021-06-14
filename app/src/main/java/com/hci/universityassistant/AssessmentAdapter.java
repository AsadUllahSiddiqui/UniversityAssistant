package com.hci.universityassistant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AssessmentAdapter extends RecyclerView.Adapter<AssessmentAdapter.AssessmentHolder>
{

    ArrayList<AssessmentModel> assessmentModels;
    boolean allowManagement;
    Context context;

    public AssessmentAdapter(Context context, ArrayList<AssessmentModel> assessmentModels, boolean allowManagement)
    {
        this.assessmentModels = assessmentModels;
        this.allowManagement = allowManagement;
        this.context = context;
    }

    @NonNull
    @Override
    public AssessmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.assessment_item, parent, false);

        return new AssessmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssessmentHolder holder, int position)
    {
        holder.title.setText(assessmentModels.get(position).getTitle());
        holder.description.setText(assessmentModels.get(position).getDescription());
        holder.subject.setText("Subject: " + assessmentModels.get(position).getSubject());
        holder.dueDate.setText("Due date: " + assessmentModels.get(position).getDueDate());

        ImageView editButton = holder.buttonLayout.findViewById(R.id.edit_button);
        ImageView deleteButton = holder.buttonLayout.findViewById(R.id.delete_button);

        editButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(context, DashboardActivity.class);
                intent.putExtra("goto", "EDIT_ASSESSMENT");
                intent.putExtra("key", assessmentModels.get(position).getuID());
                intent.putExtra("title", holder.title.getText().toString());
                intent.putExtra("subject", assessmentModels.get(position).getSubject());
                intent.putExtra("description", holder.description.getText().toString());
                intent.putExtra("dueDate", assessmentModels.get(position).getDueDate());
                context.startActivity(intent);
                return;
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new AlertDialog.Builder(context)
                        .setTitle("Confirm Deletion")
                        .setMessage("Do you really want to delete this assessment?")
                        .setIcon(R.drawable.delete)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                DatabaseReference deletionReference = FirebaseDatabase.getInstance().getReference().child("assessments");
                                deletionReference.child(assessmentModels.get(position).getuID()).removeValue();

                                assessmentModels.remove(position);
                                notifyDataSetChanged();

                                Toast.makeText(context, "Assessment deleted successfully!", Toast.LENGTH_SHORT).show();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
            }
        });

        if (!allowManagement)
        {
            editButton.setVisibility(ImageView.INVISIBLE);
            deleteButton.setVisibility(ImageView.INVISIBLE);
        }
    }

    @Override
    public int getItemCount()
    {
        return assessmentModels.size();
    }

    class AssessmentHolder extends RecyclerView.ViewHolder
    {

        TextView title, description, subject, dueDate;
        LinearLayout buttonLayout;

        public AssessmentHolder(@NonNull View itemView)
        {
            super(itemView);

            title = itemView.findViewById(R.id.assessment_item_title);
            description = itemView.findViewById(R.id.assessment_item_description);
            subject = itemView.findViewById(R.id.assessment_item_subject);
            dueDate = itemView.findViewById(R.id.assessment_item_date);

            buttonLayout = itemView.findViewById(R.id.button_layout);
        }
    }

    public void filterList(ArrayList<AssessmentModel> filteredModels)
    {
        this.assessmentModels = filteredModels;
        notifyDataSetChanged();
    }
}
