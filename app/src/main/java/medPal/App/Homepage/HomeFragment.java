package medPal.App.Homepage;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.TreeMap;


import medPal.App.Homepage.LastBloodRecord.LastBloodGlucose;
import medPal.App.Homepage.LastBloodRecord.LastBloodPressure;
import medPal.App.Homepage.LastBloodRecord.LastBloodRecordAdapter;
import medPal.App.Homepage.LastBloodRecord.LastBloodRecordController;
import medPal.App.Homepage.NextAppointment.NextAppointment;
import medPal.App.Homepage.NextAppointment.NextAppointmentAdapter;
import medPal.App.Homepage.NextAppointment.NextAppointmentController;
import medPal.App.Homepage.NextPillReminder.NextPillReminderAdapter;
import medPal.App.PillReminder.PillReminder;
import medPal.App.PillReminder.PillReminderController;
import medPal.App.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    private Button healthBtn;
    private RecyclerView nextAppointmentList;
    private NextAppointmentAdapter nextAppointmentListAdapter;
    private ExpandableListView nextPillReminderList;
    private ExpandableListAdapter nextPillReminderAdapter;
    private RecyclerView lastBloodRecordList;
    private LastBloodRecordAdapter lastBloodRecordAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        // Set up Next Appointment Data
        nextAppointmentList = v.findViewById(R.id.nextAppointmentExpandableList);
        nextAppointmentList.setLayoutManager(new LinearLayoutManager(v.getContext()));

        NextAppointmentController nextAppointmentController = new NextAppointmentController();
        ArrayList<NextAppointment> nextAppointmentArrayList = nextAppointmentController.getNextApptList();

        nextAppointmentListAdapter = new NextAppointmentAdapter(getContext(), nextAppointmentArrayList);
        nextAppointmentList.setAdapter(nextAppointmentListAdapter);

        PillReminderController nextPillReminderController = new PillReminderController();
        TreeMap<LocalTime,ArrayList<PillReminder>> nextPillReminder = nextPillReminderController.getUpcomingPillReminder();
        // Get the list of time of reminders
        ArrayList<LocalTime> timeList = new ArrayList<LocalTime>();
        timeList.addAll(nextPillReminder.keySet());

        nextPillReminderList = (ExpandableListView)v.findViewById(R.id.nextPillReminder);
        TextView noPillReminderMessage = (TextView)v.findViewById(R.id.noPillReminderMessage);
        if(timeList.size() > 0) {
            nextPillReminderList.setVisibility(View.VISIBLE);
            noPillReminderMessage.setVisibility(View.GONE);
            // Show upcoming pill reminders
            // This is line sets the height of the pill reminder section (480dp for each reminder)
            RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,420);
            nextPillReminderList.setLayoutParams(param);
            // Make some space between rows
            nextPillReminderList.setDividerHeight(50);

            // Set up ExpandableListView
            nextPillReminderAdapter = new NextPillReminderAdapter(getContext(), timeList, nextPillReminder, nextPillReminderController);
            nextPillReminderList.setAdapter(nextPillReminderAdapter);
            // Expand all
            for(int i=0; i<1; i++){
                nextPillReminderList.expandGroup(i);
            }
        }else{
            // If no upcoming pill reminders for today, show message
            nextPillReminderList.setVisibility(View.GONE);
            noPillReminderMessage.setVisibility(View.VISIBLE);
        }


        // Set up last blood record
        lastBloodRecordList = v.findViewById(R.id.lastBloodRecord);
        lastBloodRecordList.setLayoutManager(new LinearLayoutManager(v.getContext()));

        LastBloodRecordController lastBloodRecordController = new LastBloodRecordController();
        ArrayList<LastBloodPressure> lastBloodPressureList = lastBloodRecordController.getLastBloodPressureList();
        ArrayList<LastBloodGlucose> lastBloodGlucoseList = lastBloodRecordController.getLastBloodGlucoseList();


        lastBloodRecordAdapter = new LastBloodRecordAdapter(getContext(),lastBloodPressureList,lastBloodGlucoseList);
        lastBloodRecordList.setAdapter(lastBloodRecordAdapter);

        /*healthBtn = v.findViewById(R.id.healthConditionButton);

        healthBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), healthPopUp.class));
            }
        });*/

        return v;

    }
}