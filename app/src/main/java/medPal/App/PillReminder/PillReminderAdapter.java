package medPal.App.PillReminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import medPal.App.R;

/**
 * Adapter to handle the view of pill reminders on PillReminderFragment.
 * Works with PillReminderTimeAdapter.
 */
public class PillReminderAdapter extends RecyclerView.Adapter<PillReminderAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PillReminder> pillReminders = new ArrayList<>();

    public PillReminderAdapter(Context context, ArrayList<PillReminder> pillReminders) {
        this.context = context;
        this.pillReminders = pillReminders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cardView = inflater.inflate(R.layout.list_child_pill_reminder, null, false);
        ViewHolder viewHolder = new ViewHolder(cardView);
        viewHolder.medicineImage = (ImageView) cardView.findViewById(R.id.pillReminderImage);
        viewHolder.medicineName = (TextView) cardView.findViewById(R.id.pillName);
        viewHolder.quantity = (TextView) cardView.findViewById(R.id.pillQuantity);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageView medicineImageView = (ImageView) holder.medicineImage;
        Picasso.get().load(pillReminders.get(position).getMedicine().getImagePath()).into(medicineImageView);

        TextView medicineTextView = (TextView) holder.medicineName;
        medicineTextView.setText(pillReminders.get(position).getMedicine().getMedicineName());

        TextView quantityTextView = (TextView) holder.quantity;
        String placeholder = "Take " + pillReminders.get(position).getQuantity();
        quantityTextView.setText(placeholder);
    }

    @Override
    public int getItemCount() {
        return pillReminders.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView medicineImage;
        TextView medicineName;
        TextView quantity;

        public ViewHolder(View itemView) {
            super(itemView);
            medicineImage = (ImageView) itemView.findViewById(R.id.pillReminderImage);
            medicineName = (TextView) itemView.findViewById(R.id.pillName);
            quantity = (TextView) itemView.findViewById(R.id.pillQuantity);
        }
    }

}
