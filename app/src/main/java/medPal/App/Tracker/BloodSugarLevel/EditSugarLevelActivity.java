package medPal.App.Tracker.BloodSugarLevel;

import android.content.Intent;
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

public class EditSugarLevelActivity extends AppCompatActivity {

    EditText editDate;
    EditText editTime;
    EditText editLevel;
    Button b1;
    Button b2;
    String s1;
    String s2;
    String s3;
    String s4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sugar_level);

        editDate = (EditText) findViewById(R.id.edit_editTextDateSugar);
        editTime = (EditText) findViewById(R.id.edit_editTextTimeSugar);
        editLevel = (EditText) findViewById(R.id.edit_SugarLevel);
        b1 = (Button) findViewById(R.id.edit_ConfirmSugarRecordButton);
        b2 = (Button) findViewById(R.id.DeleteSugarRecordButton);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            editDate.setText(bundle.getString("Date"));
            editDate.setTextIsSelectable(false);
            editTime.setText(bundle.getString("Time"), TextView.BufferType.EDITABLE);
            editTime.setTextIsSelectable(false);
            editLevel.setText(bundle.getString("Level"), TextView.BufferType.EDITABLE);
        }

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                s1 = editDate.getText().toString();
                s2 = editTime.getText().toString();
                s3 = editLevel.getText().toString();

                String url = "https://bulacke.xyz/medpal-db/updateSugarRecord.php";
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(medPal.App.Tracker.BloodSugarLevel.EditSugarLevelActivity.this, response.trim(), Toast.LENGTH_LONG).show();
                    }
                },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(medPal.App.Tracker.BloodSugarLevel.EditSugarLevelActivity.this, error.toString(), Toast.LENGTH_LONG).show();
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
                RequestQueue requestQueue = Volley.newRequestQueue(medPal.App.Tracker.BloodSugarLevel.EditSugarLevelActivity.this);
                requestQueue.add(stringRequest);
                finish();
                Intent intent = new Intent(EditSugarLevelActivity.this, SugarLevelActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DeleteSugar delete = null;
                try {
                    delete = new DeleteSugar(editDate.getText().toString(), editTime.getText().toString());
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
                    Intent intent2 = new Intent(EditSugarLevelActivity.this, SugarLevelActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent2);
                }
            }
        });
    }
}