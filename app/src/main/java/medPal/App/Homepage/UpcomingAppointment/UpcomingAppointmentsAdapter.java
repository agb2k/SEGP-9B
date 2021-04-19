package medPal.App.Homepage.UpcomingAppointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import medPal.App.Appointment.Appointment;
import medPal.App.R;

public class UpcomingAppointmentsAdapter extends RecyclerView.Adapter<UpcomingAppointmentsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Appointment> upcomingAppointments = new ArrayList<>();

    public UpcomingAppointmentsAdapter(Context context, ArrayList<Appointment> upcomingAppointments) {
        this.context = context;
        this.upcomingAppointments = upcomingAppointments;
    }


    @Override
    public UpcomingAppointmentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cardview = inflater.inflate(R.layout.home_next_appointment, null, false);
        ViewHolder viewHolder = new ViewHolder(cardview);

        viewHolder.apptDate = (TextView) cardview.findViewById(R.id.NextAppointmentDate);
        viewHolder.apptTime = (TextView) cardview.findViewById(R.id.NextAppointmentTime);
        viewHolder.hospital = (TextView) cardview.findViewById(R.id.NextAppointmentHospital);
        viewHolder.docName = (TextView) cardview.findViewById(R.id.NextAppointmentDoc);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView apptDateTextView = (TextView) holder.apptDate;
        apptDateTextView.setText(upcomingAppointments.get(position).getDate().toString());

        TextView apptTimeTextView = (TextView) holder.apptTime;
        String apptTime;
        String tempTime = upcomingAppointments.get(position).getTime();
        if (Integer.parseInt(tempTime.substring(0,2))>=12) {
            int convert = Integer.parseInt(tempTime.substring(0,2));
            if(Integer.parseInt(tempTime.substring(0,2))>12) {
                convert = convert - 12;
            }
            String tempStr;
            if (convert<10) {
                tempStr = "0" + convert;
            }
            else {
                tempStr = String.valueOf(convert);
            }
            apptTime = tempStr + ":" + tempTime.substring(2) + "PM " ;
        }
        else {
            apptTime = tempTime.substring(0,2) + ":" + tempTime.substring(2) + "AM " ;
        }
        apptTimeTextView.setText(apptTime);

        TextView hospitalTextView = (TextView) holder.hospital;
        hospitalTextView.setText(upcomingAppointments.get(position).getVenue());

        TextView docNameTextView = (TextView) holder.docName;
        docNameTextView.setText(upcomingAppointments.get(position).getDoctor());

    }

    @Override
    public int getItemCount() {
        return 1;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView apptTime;
        TextView apptDate;
        TextView hospital;
        TextView docName;

        public ViewHolder(View itemView) {
            super(itemView);
            apptDate = (TextView) itemView.findViewById(R.id.NextAppointmentDate);
            apptTime = (TextView) itemView.findViewById(R.id.NextAppointmentTime);
            hospital = (TextView) itemView.findViewById(R.id.NextAppointmentHospital);
            docName = (TextView) itemView.findViewById(R.id.NextAppointmentDoc);
        }
    }

}
