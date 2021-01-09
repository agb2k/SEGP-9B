package medPal.App.PillReminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import medPal.App.R;

/**
 * Adapter to handle the view of medicines on PillReminderFragment.
 */
public class MedicineAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> expandableListTitle = new ArrayList<>();
    private HashMap<String, ArrayList<Medicine>> medicineList;

    public MedicineAdapter(Context context, HashMap<String, ArrayList<Medicine>> medicineList) {
        this.context = context;
        expandableListTitle.add("Medicine Detail");
        this.medicineList = medicineList;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.medicineList.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        //final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_medicine, null);
        }

        ImageView medicineImageView = (ImageView) convertView.findViewById(R.id.medicineImage);
        Picasso.get().load(medicineList.get("Medicine Detail").get(expandedListPosition).getImagePath()).into(medicineImageView);

        TextView medicineNameText = (TextView) convertView.findViewById(R.id.medicineName);
        medicineNameText.setText(medicineList.get("Medicine Detail").get(expandedListPosition).getMedicineName());

        TextView manufacturerText = (TextView) convertView.findViewById(R.id.manufacturer);
        manufacturerText.setText(medicineList.get("Medicine Detail").get(expandedListPosition).getManufacturer());

        TextView dosageText = (TextView) convertView.findViewById(R.id.medicineDosage);
        String dosage = medicineList.get("Medicine Detail").get(expandedListPosition).getDosage() + " mg";
        dosageText.setText(dosage);

        TextView purposeText = (TextView) convertView.findViewById(R.id.medicinePurpose);
        purposeText.setText(medicineList.get("Medicine Detail").get(expandedListPosition).getPurpose());

        TextView remarkText = (TextView) convertView.findViewById(R.id.medicineRemark);
        remarkText.setText(medicineList.get("Medicine Detail").get(expandedListPosition).getMedicineRemarks());


        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.medicineList.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_medicine_expandable, null);
        }

        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.medicineDetailTitle);
        listTitleTextView.setText("Medicine Detail");

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}
