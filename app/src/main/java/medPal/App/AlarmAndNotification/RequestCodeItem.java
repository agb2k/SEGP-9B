package medPal.App.AlarmAndNotification;

public class RequestCodeItem {
    int row_id;
    int type;
    long time;
    long repeating_interval;
    int reference_id;

    RequestCodeItem(int row_id, int type, long time, long repeating_interval, int reference_id) {
        this.row_id = row_id;
        this.type = type;
        this.time = time;
        this.repeating_interval = repeating_interval;
        this.reference_id = reference_id;
    }
}


