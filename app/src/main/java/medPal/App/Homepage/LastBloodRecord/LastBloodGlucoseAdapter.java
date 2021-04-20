package medPal.App.Homepage.LastBloodRecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import medPal.App.R;

public class LastBloodGlucoseAdapter extends RecyclerView.Adapter<LastBloodGlucoseAdapter.ViewHolder> {
    private Context context;
    private ArrayList<LastBloodGlucose> lastBloodGlucoseList = new ArrayList<>();

    public LastBloodGlucoseAdapter(Context context, ArrayList<LastBloodGlucose> lastBloodGlucoseList) {
        this.context = context;
        this.lastBloodGlucoseList = lastBloodGlucoseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cardview = inflater.inflate(R.layout.home_last_record, null, false);

        LastBloodGlucoseAdapter.ViewHolder viewHolder = new LastBloodGlucoseAdapter.ViewHolder(cardview);

        viewHolder.bgTime = (TextView) cardview.findViewById(R.id.lastGlucoseTime);
        viewHolder.bgLevel = (TextView) cardview.findViewById(R.id.lastGlucose);

        return viewHolder;
    }


    @Override
    public void onBindViewHolder(LastBloodGlucoseAdapter.ViewHolder holder, int position) {

        //Set up view for last blood glucose record
        TextView bgTimeView = (TextView) holder.bgTime;
        String tempTime2 = lastBloodGlucoseList.get(position).getTime();
        String tempDate2 = lastBloodGlucoseList.get(position).getDate();
        String tempBGTime;

        if (Integer.parseInt(tempTime2.substring(0,2))>=12) {
            int convert = Integer.parseInt(tempTime2.substring(0,2));
            if(Integer.parseInt(tempTime2.substring(0,2))>12) {
                convert = convert - 12;
            }
            String tempStr;
            if (convert<10) {
                tempStr = "0" + convert;
            }
            else {
                tempStr = String.valueOf(convert);
            }
            tempBGTime = "Last Recorded: "+ tempDate2.substring(5,10)+ " " + tempStr + tempTime2.substring(2,5) + "PM " ;
        }
        else {
            tempBGTime = "Last Recorded: "+ tempDate2.substring(5,10) + " " + tempTime2.substring(0,5) + "AM " ;
        }

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
        TextView bgTime;
        TextView bgLevel;

        public ViewHolder(View itemView) {
            super(itemView);
            bgTime = (TextView) itemView.findViewById(R.id.lastGlucoseTime);
            bgLevel = (TextView) itemView.findViewById(R.id.lastGlucose);
        }
    }
}
