package medPal.App.Homepage.NextAppointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import medPal.App.R;

public class NextAppointmentAdapter extends RecyclerView.Adapter<NextAppointmentAdapter.ViewHolder> {

    private Context context;
    private ArrayList<NextAppointment> nextAppointments = new ArrayList<>();

    public NextAppointmentAdapter(Context context, ArrayList<NextAppointment> nextAppointments) {
        this.context = context;
        this.nextAppointments =  nextAppointments;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cardview = inflater.inflate(R.layout.home_next_appointment, null, false);
        ViewHolder viewHolder = new ViewHolder(cardview);

        viewHolder.apptDate = (TextView) cardview.findViewById(R.id.NextAppointmentDate);
        viewHolder.apptTime = (TextView) cardview.findViewById(R.id.NextAppointmentTime);
        viewHolder.hospital = (TextView) cardview.findViewById(R.id.NextAppointmentHospital);
        viewHolder.hospital = (TextView) cardview.findViewById(R.id.NextAppointmentDoc);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView apptDateTextView = (TextView) holder.apptDate;
        apptDateTextView.setText(nextAppointments.get(position).getDate());

        TextView apptTimeTextView = (TextView) holder.apptTime;
        apptTimeTextView.setText(nextAppointments.get(position).getTime());

        TextView hospitalTextView = (TextView) holder.hospital;
        hospitalTextView.setText(nextAppointments.get(position).getHospital());

        TextView docNameTextView = (TextView) holder.docName;
        docNameTextView.setText(nextAppointments.get(position).getDocName());

    }

    @Override
    public int getItemCount() {
        return nextAppointments.size();
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
