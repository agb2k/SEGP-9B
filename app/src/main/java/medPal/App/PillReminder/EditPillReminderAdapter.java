package medPal.App.PillReminder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import medPal.App.R;

/**
 * Adapter for pill reminder. Handles the view of the EditPillReminder class.
 */
public class EditPillReminderAdapter extends BaseAdapter {

    private Activity context;
    private ArrayList<PillReminder> prList = new ArrayList<>();

    public EditPillReminderAdapter(Activity context, ArrayList<PillReminder> prList) {
        this.context = context;
        this.prList = prList;
    }

    @Override
    public View getView(int position,View view,ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.list_edit_pill_reminder, parent, false);
        }

        viewHolder.medicineImg = (ImageView) view.findViewById(R.id.pillReminderImage);
        viewHolder.medicineName = (TextView) view.findViewById(R.id.editPillReminderPillName);
        viewHolder.quantity = (TextView) view.findViewById(R.id.editPillReminderQuantity);
        viewHolder.frequency = (TextView) view.findViewById(R.id.editPillReminderFrequency);

        if(prList.get(position).getMedicine().getImagePath().length() > 0)
            Picasso.get().load(prList.get(position).getMedicine().getImagePath()).into(viewHolder.medicineImg);
        else
            Picasso.get().load(Medicine.MEDICINE_IMAGE_PLACEHOLDER).into(viewHolder.medicineImg);
        viewHolder.medicineName.setText(prList.get(position).getMedicine().getMedicineName());
        String quantityStr = "Take " + prList.get(position).getQuantity();
        viewHolder.quantity.setText(quantityStr);
        String frequencyStr = "";
        int type = prList.get(position).getType();
        if (type == 1) {
            frequencyStr = "Everyday";
        } else if (type == 2) {
            frequencyStr = "Every " + prList.get(position).getFrequency() + " days";
        } else if (type == 3) {
            String week_bit = prList.get(position).getWeek_bit();
            frequencyStr = "Every";
            for (int i = 0; i < week_bit.length(); i++) {
                if (week_bit.charAt(i) == '1') {
                    switch (i) {
                        case 0:
                            frequencyStr += " Mon,";
                            break;
                        case 1:
                            frequencyStr += " Tue,";
                            break;
                        case 2:
                            frequencyStr += " Wed,";
                            break;
                        case 3:
                            frequencyStr += " Thu,";
                            break;
                        case 4:
                            frequencyStr += " Fri,";
                            break;
                        case 5:
                            frequencyStr += " Sat,";
                            break;
                        case 6:
                            frequencyStr += " Sun,";
                            break;
                    }
                }
            }
            frequencyStr = frequencyStr.substring(0, frequencyStr.length()-1);
        }
        viewHolder.frequency.setText(frequencyStr);

        return view;
    }

    @Override
    public int getCount() {
        return prList.size();
    }

    @Override
    public Object getItem(int position) {
        return prList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView medicineImg;
        TextView medicineName;
        TextView quantity;
        TextView frequency;
    }

}
