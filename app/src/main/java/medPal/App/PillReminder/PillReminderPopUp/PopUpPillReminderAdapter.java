package medPal.App.PillReminder.PillReminderPopUp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import medPal.App.PillReminder.PillReminder;
import medPal.App.R;

public class PopUpPillReminderAdapter extends RecyclerView.Adapter<PopUpPillReminderAdapter.ViewHolder> {

    private ArrayList<PillReminder> prList = new ArrayList<>();

    // RecyclerView recyclerView;
    public PopUpPillReminderAdapter(ArrayList<PillReminder> prList) {
        this.prList = prList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.pop_up_pill_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final PillReminder pr = prList.get(position);

        Picasso.get().load(pr.getMedicine().getImagePath()).into((ImageView) holder.pillImage);

        holder.pillName.setText(pr.getMedicine().getMedicineName());

        String quantity = "Take " + String.valueOf(pr.getQuantity());
        holder.pillQuantity.setText(quantity);
    }


    @Override
    public int getItemCount() {
        return prList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView pillImage;
        public TextView pillName;
        public TextView pillQuantity;
        public ViewHolder(View itemView) {
            super(itemView);
            this.pillImage = (ImageView) itemView.findViewById(R.id.pillReminderImage);
            this.pillName = (TextView) itemView.findViewById(R.id.pillName);
            this.pillQuantity = (TextView) itemView.findViewById(R.id.pillQuantity);
        }
    }

}
