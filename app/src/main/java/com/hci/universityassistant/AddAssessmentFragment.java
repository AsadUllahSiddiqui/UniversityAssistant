package com.hci.universityassistant;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class AddAssessmentFragment extends Fragment
{
    EditText subject, title, description, dueDate;
    MaterialButton saveButton, backButton;

    final Calendar myCalendar = Calendar.getInstance();

    boolean updateExisting = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.fragment_add_assessment, container, false);

        subject = root.findViewById(R.id.assessment_subject);
        title = root.findViewById(R.id.assessment_title);
        description = root.findViewById(R.id.assessment_description);
        dueDate = root.findViewById(R.id.assessment_date);

        Bundle args = getArguments();
        if (args!=null)
        {
            subject.setText(args.getString("subject"));
            title.setText(args.getString("title"));
            description.setText(args.getString("description"));
            dueDate.setText(args.getString("dueDate"));
            updateExisting = true;
        }

        saveButton = root.findViewById(R.id.assessment_save_button);
        backButton = root.findViewById(R.id.assessment_back_button);

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // Set calendar attributes
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dueDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), DashboardActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String subjectString, titleString, descriptionString, dueDateString;

                if (subject.getText().toString().isEmpty())
                {
                    subject.setError("Subject is required!");
                    return;
                }
                else if (title.getText().toString().isEmpty())
                {
                    title.setError("Title is required!");
                    return;
                }
                else if (description.getText().toString().isEmpty())
                {
                    description.setError("Description is missing!");
                    return;
                }
                else if (dueDate.getText().toString().isEmpty())
                {
                    dueDate.setError("Due date is required!");
                    return;
                }
                else
                {
                    subjectString = subject.getText().toString();
                    titleString = title.getText().toString();
                    descriptionString = description.getText().toString();
                    dueDateString = dueDate.getText().toString();

                    String key = null;
                    if (!updateExisting)
                    {
                        key = FirebaseDatabase.getInstance().getReference().child("assessments").push().getKey();
                    }
                    else
                    {
                        key = args.getString("key");
                    }

                    AssessmentModel assessmentModel = new AssessmentModel(key, subjectString, titleString, descriptionString, dueDateString);

                    FirebaseDatabase.getInstance().getReference().child("assessments").child(key).setValue(assessmentModel);

                    backToDashboard();
                }
            }
        });

        return root;
    }

    private void backToDashboard()
    {
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        intent.putExtra("status", "ASSESSMENT_ADDED");
        startActivity(intent);
        getActivity().finish();
    }

    private void updateLabel() {
        String myFormat = "EEEE, MMMM dd, yyyy"; /* Date Format */
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dueDate.setText(sdf.format(myCalendar.getTime()));
    }
}