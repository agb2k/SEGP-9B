package medPal.App.Appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import medPal.App.R;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Appointment> Appointments = new ArrayList<>();

    public AppointmentAdapter(Context context, ArrayList<Appointment> Appointments) {
        this.context = context;
        this.Appointments = Appointments;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cardview = inflater.inflate(R.layout.list_appointment, null, false);
        ViewHolder viewHolder = new ViewHolder(cardview);

        viewHolder.date = (TextView) cardview.findViewById(R.id.date);
        viewHolder.time = (TextView) cardview.findViewById(R.id.time);
        viewHolder.doctor = (TextView) cardview.findViewById(R.id.doctor);
        viewHolder.venue = (TextView) cardview.findViewById(R.id.venue);
        viewHolder.contact = (TextView) cardview.findViewById(R.id.contact);
        viewHolder.email = (TextView) cardview.findViewById(R.id.email);
        viewHolder.purpose = (TextView) cardview.findViewById(R.id.purpose);
        viewHolder.remark = (TextView) cardview.findViewById(R.id.remark);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView dateTextView = (TextView) holder.date;
        dateTextView.setText(Appointments.get(position).getDate().toString());

        TextView timeTextView = (TextView) holder.time;
        timeTextView.setText(Appointments.get(position).getTime());

        TextView doctorTextView = (TextView) holder.doctor;
        doctorTextView.setText(Appointments.get(position).getDoctor());

        TextView venueTextView = (TextView) holder.venue;
        venueTextView.setText(Appointments.get(position).getVenue());

        TextView contactTextView = (TextView) holder.contact;
        contactTextView.setText(Integer.toString(Appointments.get(position).getContact()));

        TextView emailTextView = (TextView) holder.email;
        emailTextView.setText(Appointments.get(position).getEmail());

        TextView purposeTextView = (TextView) holder.purpose;
        purposeTextView.setText(Appointments.get(position).getPurpose());

        TextView remarkTextView = (TextView) holder.remark;
        remarkTextView.setText(Appointments.get(position).getRemark());
    }

    @Override
    public int getItemCount() {
        return Appointments.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView date;
        TextView time;
        TextView doctor;
        TextView venue;
        TextView contact;
        TextView email;
        TextView purpose;
        TextView remark;


        public ViewHolder(View itemView) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
            doctor = (TextView) itemView.findViewById(R.id.doctor);
            venue = (TextView) itemView.findViewById(R.id.venue);
            contact = (TextView) itemView.findViewById(R.id.contact);
            email = (TextView) itemView.findViewById(R.id.email);
            purpose = (TextView) itemView.findViewById(R.id.purpose);
            remark = (TextView) itemView.findViewById(R.id.remark);
        }
    }
}
