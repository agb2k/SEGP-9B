package medPal.App.Homepage.LastBloodRecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import medPal.App.R;


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
        TextView bpTimeView = (TextView) holder.bpTime;
        String tempStr = lastBloodPressureList.get(position).getTime();
        String tempBPTime = "Last Recorded at " + tempStr.substring(0,5) + " " + lastBloodPressureList.get(position).getDate();
        bpTimeView.setText(tempBPTime);

        TextView bpRecordView = (TextView) holder.bpRecord;
        String tempBPRecord = lastBloodPressureList.get(position).getSYS() + "/" + lastBloodPressureList.get(position).getDIA();
        bpRecordView.setText(tempBPRecord);

        TextView bgTimeView = (TextView) holder.bgTime;
        String tempStr2 = lastBloodGlucoseList.get(position).getTime();
        String tempBGTime = "Last Recorded at " + tempStr2.substring(0,5) + " " + lastBloodGlucoseList.get(position).getDate();
        bgTimeView.setText(tempBGTime);

        TextView bgLevelView = (TextView) holder.bgLevel;
        String tempLevel = String.valueOf(lastBloodGlucoseList.get(position).getLevel());
        bgLevelView.setText(tempLevel);
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
