package medPal.App.Tracker.BloodPressure;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import medPal.App.R;

public class EditPressureActivity extends AppCompatActivity {

    EditText editDate;
    EditText editTime;
    EditText editSys;
    EditText editDia;
    Button b1;
    Button b2;
    String s1;
    String s2;
    String s3;
    String s4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pressure);

        editDate = (EditText) findViewById(R.id.Edit_editTextDate_pressure);
        editTime = (EditText) findViewById(R.id.Edit_editTextTime_pressure);
        editSys = (EditText) findViewById(R.id.Edit_SYS);
        editDia = (EditText) findViewById(R.id.Edit_DIA);
        b1 = (Button) findViewById(R.id.Edit_ConfirmPressureRecordButton);
        b2 = (Button) findViewById(R.id.DeletePressureRecordButton);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            editDate.setText(bundle.getString("Date"));
            editDate.setTextIsSelectable(false);
            editTime.setText(bundle.getString("Time"), TextView.BufferType.EDITABLE);
            editTime.setTextIsSelectable(false);
            editSys.setText(bundle.getString("Sys"), TextView.BufferType.EDITABLE);
            editDia.setText(bundle.getString("Dia"), TextView.BufferType.EDITABLE);
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1 = editDate.getText().toString();
                s2 = editTime.getText().toString();
                s3 = editSys.getText().toString();
                s4 = editDia.getText().toString();

                String url = "https://bulacke.xyz/medpal-db/updatePressureRecord.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditPressureActivity.this, response.trim(), Toast.LENGTH_LONG).show();
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(EditPressureActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                RequestQueue requestQueue = Volley.newRequestQueue(EditPressureActivity.this);
                requestQueue.add(stringRequest);
            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DeletePressure delete = null;
                try {
                    delete = new DeletePressure(editDate.getText().toString(), editTime.getText().toString());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(delete.success()){
                    Toast toast = Toast.makeText(getApplicationContext(), "Record deleted successfully", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
            }
        });
    }
}