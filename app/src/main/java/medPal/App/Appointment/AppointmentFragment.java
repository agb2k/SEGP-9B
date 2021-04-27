package medPal.App.Appointment;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import medPal.App.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AppointmentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppointmentFragment extends Fragment {

    public static final int ACTIVITY_REQUEST_CODE = 20001;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AppointmentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AppointmentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppointmentFragment newInstance(String param1, String param2) {
        AppointmentFragment fragment = new AppointmentFragment();
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

    private Button b1;
    private AppointmentAdapter AppointmentListAdapter;
    private RecyclerView appointmentList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_appointment, container, false);

        // Set up Next Appointment Data
        appointmentList = v.findViewById(R.id.appointmentRVList);
        appointmentList.setLayoutManager(new LinearLayoutManager(v.getContext()));

        AppointmentController AppointmentController = null;
        try {
            AppointmentController = new AppointmentController();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ArrayList<Appointment> AppointmentArrayList = AppointmentController.getAllAppointments();

        AppointmentListAdapter = new AppointmentAdapter(getContext(), AppointmentArrayList);
        appointmentList.setAdapter(AppointmentListAdapter);

        // Add new reminder button
        Button b1 = v.findViewById(R.id.newAppointmentButton);
        b1.setOnClickListener(v1 -> openNewAppointment());

        return v;
    }

    public void openNewAppointment(){
        Intent intent = new Intent(getActivity(), NewAppointment.class);
        startActivityForResult(intent,ACTIVITY_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTIVITY_REQUEST_CODE) {
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