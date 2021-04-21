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
    private ArrayList<Appointment> confirmedAppointments = new ArrayList<>();

    public UpcomingAppointmentsAdapter(Context context, ArrayList<Appointment> upcomingAppointments, ArrayList<Appointment> confirmedAppointments) {
        this.context = context;
        this.upcomingAppointments = upcomingAppointments;
        this.confirmedAppointments = confirmedAppointments;
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

        viewHolder.apptDate2 = (TextView) cardview.findViewById(R.id.NextAppointmentDate2);
        viewHolder.apptTime2 = (TextView) cardview.findViewById(R.id.NextAppointmentTime2);
        viewHolder.hospital2 = (TextView) cardview.findViewById(R.id.NextAppointmentHospital2);
        viewHolder.docName2 = (TextView) cardview.findViewById(R.id.NextAppointmentDoc2);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //Handle if no planned appointment
        if(upcomingAppointments.size()>0) {
            //Set view for planned appointment
            TextView apptDateTextView = (TextView) holder.apptDate;
            apptDateTextView.setText(upcomingAppointments.get(position).getDate().toString());

            TextView apptTimeTextView = (TextView) holder.apptTime;
            String apptTime;
            String tempTime = upcomingAppointments.get(position).getTime();
            if (Integer.parseInt(tempTime.substring(0, 2)) >= 12) {
                int convert = Integer.parseInt(tempTime.substring(0, 2));
                if (Integer.parseInt(tempTime.substring(0, 2)) > 12) {
                    convert = convert - 12;
                }
                String tempStr;
                if (convert < 10) {
                    tempStr = "0" + convert;
                } else {
                    tempStr = String.valueOf(convert);
                }
                apptTime = tempStr + ":" + tempTime.substring(2) + "PM ";
            } else {
                apptTime = tempTime.substring(0, 2) + ":" + tempTime.substring(2) + "AM ";
            }
            apptTimeTextView.setText(apptTime);

            TextView hospitalTextView = (TextView) holder.hospital;
            hospitalTextView.setText(upcomingAppointments.get(position).getVenue());

            TextView docNameTextView = (TextView) holder.docName;
            docNameTextView.setText(upcomingAppointments.get(position).getDoctor());
        } else {
            //Set view for no appointment
            TextView apptDateTextView = (TextView) holder.apptDate;
            apptDateTextView.setText("No");
            TextView apptTimeTextView = (TextView) holder.apptTime;
            apptTimeTextView.setText("Appointment");
            TextView hospitalTextView = (TextView) holder.hospital;
            hospitalTextView.setText("");
            TextView docNameTextView = (TextView) holder.docName;
            docNameTextView.setText("");
        }

        //Handle if no planned appointment
        if(confirmedAppointments.size()>0) {
            //Set view for confirmed appointment
            TextView apptDateTextView = (TextView) holder.apptDate2;
            apptDateTextView.setText(confirmedAppointments.get(position).getDate().toString());

            TextView apptTimeTextView = (TextView) holder.apptTime2;
            String apptTime;
            String tempTime = confirmedAppointments.get(position).getTime();
            if (Integer.parseInt(tempTime.substring(0, 2)) >= 12) {
                int convert = Integer.parseInt(tempTime.substring(0, 2));
                if (Integer.parseInt(tempTime.substring(0, 2)) > 12) {
                    convert = convert - 12;
                }
                String tempStr;
                if (convert < 10) {
                    tempStr = "0" + convert;
                } else {
                    tempStr = String.valueOf(convert);
                }
                apptTime = tempStr + ":" + tempTime.substring(2) + "PM ";
            } else {
                apptTime = tempTime.substring(0, 2) + ":" + tempTime.substring(2) + "AM ";
            }
            apptTimeTextView.setText(apptTime);

            TextView hospitalTextView = (TextView) holder.hospital2;
            hospitalTextView.setText(confirmedAppointments.get(position).getVenue());

            TextView docNameTextView = (TextView) holder.docName2;
            docNameTextView.setText(confirmedAppointments.get(position).getDoctor());
        } else {
            //Set view for no appointment
            TextView apptDateTextView = (TextView) holder.apptDate2;
            apptDateTextView.setText("No");
            TextView apptTimeTextView = (TextView) holder.apptTime2;
            apptTimeTextView.setText("Appointment");
            TextView hospitalTextView = (TextView) holder.hospital2;
            hospitalTextView.setText("");
            TextView docNameTextView = (TextView) holder.docName2;
            docNameTextView.setText("");
        }
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

        TextView apptTime, apptDate, hospital, docName;
        TextView apptTime2, apptDate2, hospital2, docName2;

        public ViewHolder(View itemView) {
            super(itemView);
            apptDate = (TextView) itemView.findViewById(R.id.NextAppointmentDate);
            apptTime = (TextView) itemView.findViewById(R.id.NextAppointmentTime);
            hospital = (TextView) itemView.findViewById(R.id.NextAppointmentHospital);
            docName = (TextView) itemView.findViewById(R.id.NextAppointmentDoc);

            apptDate2 = (TextView) itemView.findViewById(R.id.NextAppointmentDate2);
            apptTime2 = (TextView) itemView.findViewById(R.id.NextAppointmentTime2);
            hospital2 = (TextView) itemView.findViewById(R.id.NextAppointmentHospital2);
            docName2 = (TextView) itemView.findViewById(R.id.NextAppointmentDoc2);
        }
    }

}
