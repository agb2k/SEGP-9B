package medPal.App.Tracker.BloodPressure;

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

import medPal.App.DatabaseHelper;
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

                try {
                    DatabaseHelper dbHelper = new DatabaseHelper(DatabaseHelper.UPDATE, DatabaseHelper.BLOOD_PRESSURE);
                    dbHelper.setUserInfo();
                    dbHelper.encodeData("Date", s1);
                    dbHelper.encodeData("Time", s2);
                    dbHelper.encodeData("SYS", s3);
                    dbHelper.encodeData("DIA", s4);
                    dbHelper.send();
                } catch (UnsupportedEncodingException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

                finish();
                Intent intent = new Intent(EditPressureActivity.this, BloodPressureActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
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
                    Intent intent2 = new Intent(EditPressureActivity.this, BloodPressureActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent2);
                }
            }
        });
    }
}