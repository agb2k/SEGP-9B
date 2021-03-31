package medPal.App.Tracker.BloodSugarLevel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import medPal.App.R;

public class NewSugarLevelRecord extends AppCompatActivity {
    EditText e1;
    EditText e2;
    EditText e3;
    Button b1;
    String s1;
    String s2;
    String s3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sugar_level_record);

        //Intent intent = new Intent(NewSugarLevelRecord.this, SugarLevelActivity.class);
        //startActivityForResult(intent,ACTIVITY_REQUEST_CODE);

        getSupportActionBar().setTitle("New Blood Sugar Level Record");

        e1 = (EditText) findViewById(R.id.editTextDateSugar);
        e2 = (EditText) findViewById(R.id.editTextTimeSugar);
        e3 = (EditText) findViewById(R.id.SugarLevel);
        b1 = (Button) findViewById(R.id.ConfirmSugarRecordButton);

        e1.setInputType(InputType.TYPE_NULL);
        e2.setInputType(InputType.TYPE_NULL);

        e1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(e1);
            }
        });

        e2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(e2);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1 = e1.getText().toString();
                s2 = e2.getText().toString();
                s3 = e3.getText().toString();
                String url = "https://bulacke.xyz/medpal-db/insertSugarRecord.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(NewSugarLevelRecord.this, response.trim(), Toast.LENGTH_LONG).show();
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(NewSugarLevelRecord.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Date", s1);
                        params.put("Time", s2);
                        params.put("Level", s3);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(NewSugarLevelRecord.this);
                requestQueue.add(stringRequest);
                finish();
                Intent intent = new Intent(NewSugarLevelRecord.this, SugarLevelActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

    }
    private void showDateDialog(final EditText e1){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                e1.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new DatePickerDialog(NewSugarLevelRecord.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void showTimeDialog(final EditText e2){
        final Calendar calendar = Calendar.getInstance();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                e2.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };

        new TimePickerDialog(NewSugarLevelRecord.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
    }
}