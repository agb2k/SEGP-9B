package medPal.App.Homepage.LastBloodRecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Set;

import medPal.App.R;

/***
 * Adapter to handle the view of the last blood record at homepage
 */
public class LastBloodRecordAdapter extends RecyclerView.Adapter<LastBloodRecordAdapter.ViewHolder> {

    private Context context;
    private ArrayList<LastBloodPressure> lastBloodPressureList = new ArrayList<>();
    private ArrayList<LastBloodGlucose> lastBloodGlucoseList = new ArrayList<>();

    public LastBloodRecordAdapter(Context context, ArrayList<LastBloodPressure> lastBloodPressureList, ArrayList<LastBloodGlucose> lastBloodGlucoseList) {
        this.context = context;
        this.lastBloodPressureList = lastBloodPressureList;
        this.lastBloodGlucoseList = lastBloodGlucoseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cardview = inflater.inflate(R.layout.home_last_record, null, false);
        ViewHolder viewHolder = new ViewHolder(cardview);

        viewHolder.bpTime = (TextView) cardview.findViewById(R.id.lastPressureTime);
        viewHolder.bpRecord = (TextView) cardview.findViewById(R.id.lastPressure);
        viewHolder.bgTime = (TextView) cardview.findViewById(R.id.lastGlucoseTime);
        viewHolder.bgLevel = (TextView) cardview.findViewById(R.id.lastGlucose);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        if(lastBloodPressureList.size()>0) {
            //Set up view for last blood pressure record
            TextView bpTimeView = (TextView) holder.bpTime;
            String tempTime = lastBloodPressureList.get(position).getTime();
            String tempDate = lastBloodPressureList.get(position).getDate();
            String tempBPTime;

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
                tempBPTime = "Last Recorded: " + tempDate.substring(5, 10) + " " + tempStr + tempTime.substring(2, 5) + "PM ";
            } else {
                tempBPTime = "Last Recorded: " + tempDate.substring(5, 10) + " " + tempTime.substring(0, 5) + "AM ";
            }

            bpTimeView.setText(tempBPTime);

            TextView bpRecordView = (TextView) holder.bpRecord;
            String tempBPRecord = lastBloodPressureList.get(position).getSYS() + "/" + lastBloodPressureList.get(position).getDIA();
            bpRecordView.setText(tempBPRecord);
        }
        else {
            TextView bpTimeView = (TextView) holder.bpTime;
            bpTimeView.setText("No Record");
            TextView bpRecordView = (TextView) holder.bpRecord;
            bpRecordView.setText("-");
        }

        // Handle if no record for blood glucose
        if(lastBloodGlucoseList.size()>0) {
            //Set up view for last blood glucose record
            TextView bgTimeView = (TextView) holder.bgTime;
            String tempTime2 = lastBloodGlucoseList.get(position).getTime();
            String tempDate2 = lastBloodGlucoseList.get(position).getDate();
            String tempBGTime;

            if (Integer.parseInt(tempTime2.substring(0, 2)) >= 12) {
                int convert = Integer.parseInt(tempTime2.substring(0, 2));
                if (Integer.parseInt(tempTime2.substring(0, 2)) > 12) {
                    convert = convert - 12;
                }
                String tempStr;
                if (convert < 10) {
                    tempStr = "0" + convert;
                } else {
                    tempStr = String.valueOf(convert);
                }
                tempBGTime = "Last Recorded: " + tempDate2.substring(5, 10) + " " + tempStr + tempTime2.substring(2, 5) + "PM ";
            } else {
                tempBGTime = "Last Recorded: " + tempDate2.substring(5, 10) + " " + tempTime2.substring(0, 5) + "AM ";
            }

            bgTimeView.setText(tempBGTime);

            TextView bgLevelView = (TextView) holder.bgLevel;
            String tempLevel = String.valueOf(lastBloodGlucoseList.get(position).getLevel());
            bgLevelView.setText(tempLevel);

        }
        else {
            //Set view if no blood glucose record
            TextView bgTimeView = (TextView) holder.bgTime;
            bgTimeView.setText("No Record");
            TextView bgLevelView = (TextView) holder.bgLevel;
            bgLevelView.setText("-");

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

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView bpTime;
        TextView bpRecord;
        TextView bgTime;
        TextView bgLevel;

        public ViewHolder(View itemView) {
            super(itemView);
            bpTime = (TextView) itemView.findViewById(R.id.lastPressureTime);
            bpRecord = (TextView) itemView.findViewById(R.id.lastPressure);
            bgTime = (TextView) itemView.findViewById(R.id.lastGlucoseTime);
            bgLevel = (TextView) itemView.findViewById(R.id.lastGlucose);
        }
    }
}
