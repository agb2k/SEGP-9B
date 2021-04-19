package medPal.App.PillReminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import java.io.UnsupportedEncodingException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;

import medPal.App.MainActivity;
import medPal.App.PillReminder.PillReminderPopUp.TakePillPopUp;
import medPal.App.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PillReminderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PillReminderFragment extends Fragment {

    public static final int UPDATE_PILL_REMINDER_REQUEST_CODE = 10001;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PillReminderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PillReminderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PillReminderFragment newInstance(String param1, String param2) {
        PillReminderFragment fragment = new PillReminderFragment();
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

    private ExpandableListView expandableMedicineList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_pill_reminder, container, false);

        // Get ExpandableListView
        ExpandableListView parentListView = (ExpandableListView) v.findViewById(R.id.prExpandableListView);
        // Call PillReminderController
        PillReminderController prController = null;
        try {
            prController = new PillReminderController(getContext());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Get today's pill reminder, grouped by time
        TreeMap<LocalTime,ArrayList<PillReminder>> prByTime = prController.getPillReminderByTime();
        // Get the list of time of reminders
        ArrayList<LocalTime> timeList = new ArrayList<LocalTime>(prByTime.keySet());

        // Add new reminder button
        Button b1 = v.findViewById(R.id.NewPillReminderButton);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNewPillReminder();
            }
        });

        // This is line sets the height of the pill reminder section (480dp for each reminder)
        RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,timeList.size()*460);
        parentListView.setLayoutParams(param);
        // Make some space between rows
        parentListView.setDividerHeight(50);

        // Set up ExpandableListView
        Fragment navhostFragment = requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragment);
        assert navhostFragment != null;
        Fragment currentFragment = navhostFragment.getChildFragmentManager().getFragments().get(0);
        ExpandableListAdapter parentAdapter = new PillReminderTimeAdapter(getContext(), currentFragment, timeList, prByTime, prController);
        parentListView.setAdapter(parentAdapter);

        // Expand all
        for(int i=0; i<timeList.size(); i++){
            parentListView.expandGroup(i);
        }

        // This will fix the display issue. Detail of the display issue is explained below.
        // Issue: When user insert new record, the last group of listview will not expand (ie last
        //        group of pill reminders will disappear)
        // Possible reason: When the expandGroup() is called above, the expandableListView have not
        //                  been loaded, therefore if we wait until the view has been completely
        //                  loaded, the expandGroup() will be working for last group.
        // This part of code will fix the issue, however, no evidence or similar issues found online.
        parentListView.post(new Runnable() {
            public void run() {
                parentListView.expandGroup(timeList.size()-1);
            }
        });

        // Create expandable list view for medicine
        expandableMedicineList = (ExpandableListView) v.findViewById(R.id.expandableMedicineList);
        ArrayList<Medicine> medicineList = prController.getAllMedicine();
        HashMap<String, ArrayList<Medicine>> medicineHashMap = new HashMap<>();
        medicineHashMap.put("Medicine Detail", medicineList);
        ExpandableListAdapter expandableMedicineAdapter = new MedicineAdapter(getContext(), medicineHashMap);
        expandableMedicineList.setAdapter(expandableMedicineAdapter);

        expandableMedicineList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(141 + medicineList.size()*260));
                expandableMedicineList.setLayoutParams(param);
            }
        });

        expandableMedicineList.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                expandableMedicineList.setLayoutParams(param);
            }
        });

        expandableMedicineList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Intent editMedicineIntent = new Intent(getActivity(), EditMedicineActivity.class);
                editMedicineIntent.putExtra("MedicineObject",medicineList.get(childPosition));
                startActivityForResult(editMedicineIntent,UPDATE_PILL_REMINDER_REQUEST_CODE);
                return false;
            }
        });

        return v;
    }

    public void openNewPillReminder() {
        Intent intent = new Intent(getActivity(), NewPillReminder.class);
        startActivityForResult(intent,UPDATE_PILL_REMINDER_REQUEST_CODE);
    }

    /**
     * Refresh view after updating database.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == UPDATE_PILL_REMINDER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Fragment navhostFragment = requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragment);
                assert navhostFragment != null;
                Fragment currentFragment = navhostFragment.getChildFragmentManager().getFragments().get(0);
                FragmentTransaction fragmentTransaction = currentFragment.getParentFragmentManager().beginTransaction();
                fragmentTransaction.detach(currentFragment);
                fragmentTransaction.attach(currentFragment);
                fragmentTransaction.commit();
            }
        }
    }
}