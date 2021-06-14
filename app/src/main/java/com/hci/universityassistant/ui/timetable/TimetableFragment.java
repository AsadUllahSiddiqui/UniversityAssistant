package com.hci.universityassistant.ui.timetable;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hci.universityassistant.AssessmentModel;
import com.hci.universityassistant.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;

import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.listeners.OnDateClickListener;
import sun.bob.mcalendarview.vo.DateData;

public class TimetableFragment extends Fragment
{

    private TimetableViewModel timetableViewModel;
    MCalendarView calendar;

    HashMap<String, AssessmentModel> hashMap = new HashMap<>();

    String closestDate = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        timetableViewModel =
                new ViewModelProvider(this).get(TimetableViewModel.class);
        View root = inflater.inflate(R.layout.fragment_timetable, container, false);

        //        final TextView textView = root.findViewById(R.id.text_slideshow);

        timetableViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>()
        {
            @Override
            public void onChanged(@Nullable String s)
            {
                /* No action on text change */
            }
        });

        calendar = root.findViewById(R.id.calendar);

//        calendar.markDate(2020, 12, 23);

        populateCalenderFromFirebase();

        calendar.setOnDateClickListener(new OnDateClickListener()
        {
            @Override
            public void onDateClick(View view, DateData date)
            {
//                Toast.makeText(getActivity(), String.format("%d-%d-%d", date.getDay(), date.getMonth(), date.getYear()), Toast.LENGTH_SHORT).show();

                AssessmentModel assessmentModel = hashMap.get(String.format("%d-%d-%d", date.getDay(), date.getMonth(), date.getYear()));
                if (assessmentModel != null)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle(assessmentModel.getSubject());

//                    alertDialog.setMessage("\n" + Html.fromHtml("<b>"+"Title: "+"</b>") + assessmentModel.getTitle() + "\n" + "Description: " + assessmentModel.getDescription());
                    alertDialog.setMessage(Html.fromHtml("<br><b>" + "Title: " + "</b>" + assessmentModel.getTitle() + "<br>" + "<b>" + "Description: " + "</b>" + assessmentModel.getDescription()));
                    alertDialog.setIcon(R.drawable.assignment_png);

                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
//                            Toast.makeText(getApplicationContext(), "You clicked on OK", Toast.LENGTH_SHORT).show();
                        }
                    });

                    alertDialog.show();
                }
            }
        });

        return root;
    }

    private void populateCalenderFromFirebase()
    {
        DatabaseReference assessmentsReference = FirebaseDatabase.getInstance().getReference().child("assessments");
        assessmentsReference.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        String uID = snapshot.getKey();
                        String subject = snapshot.child("subject").getValue().toString();
                        String description = snapshot.child("description").getValue().toString();
                        String title = snapshot.child("title").getValue().toString();
                        String dueDate = snapshot.child("dueDate").getValue().toString();

                        if (closestDate == null)
                            closestDate = dueDate;

                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.US);
                        try
                        {
                            sdf.parse(dueDate);
                        } catch (ParseException e)
                        {
                            e.printStackTrace();
                        }

                        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                        String day = null;
                        try
                        {
                            day = dayFormat.format(sdf.parse(dueDate));
                        } catch (ParseException e)
                        {
                            e.printStackTrace();
                        }

                        String month = dueDate.split(", ")[1].split(" ")[0];
                        month = month.toLowerCase();

                        switch (month)
                        {
                            case "january":
                            case "jan":
                                month = "1";
                                break;

                            case "febuary":
                            case "feb":
                                month = "2";
                                break;

                            case "march":
                            case "mar":
                                month = "3";
                                break;

                            case "april":
                            case "apr":
                                month = "4";
                                break;

                            case "may":
                                month = "5";
                                break;

                            case "june":
                            case "jun":
                                month = "6";
                                break;

                            case "july":
                            case "jul":
                                month = "7";
                                break;

                            case "august":
                            case "aug":
                                month = "8";
                                break;

                            case "september":
                            case "sep":
                            case "sept":
                                month = "9";
                                break;

                            case "october":
                            case "oct":
                                month = "10";
                                break;

                            case "november":
                            case "nov":
                                month = "11";
                                break;

                            case "december":
                            case "dec":
                                month = "12";
                                break;
                        }

                        String year = dueDate.split(", ")[2];

                        calendar.markDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));

                        hashMap.put(String.format("%s-%s-%s", day, month, year), new AssessmentModel(uID, subject, title, description, dueDate));
                    }

                    if (closestDate != null)
                        sendSmartNotification(closestDate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    public void sendSmartNotification(String date)
    {
        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01"; // The id of the channel.
        CharSequence name = "channel"; // The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

        // Create a notification and set the notification channel.
        Notification notification = new Notification.Builder(getContext())
                .setContentTitle("Smart Notification")
                .setContentText("Next assignment due on " + date + ".")
                .setSmallIcon(R.drawable.assessment_png)
                .setChannelId(CHANNEL_ID)
                .build();

        NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(getContext());
        mNotificationManager.createNotificationChannel(mChannel);

        // Issue the notification.
        mNotificationManager.notify(notifyID, notification);
    }
}