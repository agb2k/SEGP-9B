package medPal.App;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NewPressureRecord extends AppCompatActivity {

    EditText e1;
    EditText e2;
    EditText e3;
    EditText e4;
    Button b1;
    String s1;
    String s2;
    String s3;
    String s4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_pressure_record);

        getSupportActionBar().setTitle("New Blood Pressure Record");

        e1 = (EditText) findViewById(R.id.editTextDate_pressure);
        e2 = (EditText) findViewById(R.id.editTextTime_pressure);
        e3 = (EditText) findViewById(R.id.SYS);
        e4 = (EditText) findViewById(R.id.DIA);
        b1 = (Button) findViewById(R.id.ConfirmPressureRecordButton);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1 = e1.getText().toString();
                s2 = e2.getText().toString();
                s3 = e3.getText().toString();
                s4 = e4.getText().toString();
                String url = "https://bulacke.xyz/medpal-db/insertPressureRecord.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(NewPressureRecord.this, response.trim(), Toast.LENGTH_LONG).show();
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(NewPressureRecord.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Date", s1);
                        params.put("Time", s2);
                        params.put("SYS", s3);
                        params.put("DIA", s4);

                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(NewPressureRecord.this);
                requestQueue.add(stringRequest);
            }
        });

    }
}