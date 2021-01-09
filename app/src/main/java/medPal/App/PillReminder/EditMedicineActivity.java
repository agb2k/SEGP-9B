package medPal.App.PillReminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import medPal.App.R;

/**
 * Activity to edit (or delete) an existing medicine.
 */
public class EditMedicineActivity extends AppCompatActivity {

    Medicine medicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);
        getSupportActionBar().setTitle("Edit Medicine Detail");

        medicine = (Medicine) getIntent().getSerializableExtra("MedicineObject");

        putDataIntoInputField();

        Button editMedicineButton = (Button) findViewById(R.id.editMedicineConfirm);
        editMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getUserInput();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        /**
         * Onclick listener to delete pill medicine.
         */
        TextView deleteMedicine = (TextView) findViewById(R.id.deleteMedicineConfirm);
        deleteMedicine.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DeleteMedicine delete = null;
                try {
                    delete = new DeleteMedicine(medicine.getMedicineId());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(delete.success()){
                    Toast toast = Toast.makeText(getApplicationContext(), "Medicine deleted successfully", Toast.LENGTH_SHORT);
                    toast.show();
                    finish();
                }
            }
        });
    }

    /**
     * Automatically fill in the original data.
     */
    public void putDataIntoInputField() {
        ImageView pillImage = (ImageView) findViewById(R.id.PillImage);
        Picasso.get().load(medicine.getImagePath()).into(pillImage);

        TextView pillName = (TextView) findViewById(R.id.MedicineName);
        pillName.setText(medicine.getMedicineName());

        EditText manufacturer = (EditText) findViewById(R.id.editManufacturer);
        if(medicine.getManufacturer().length() > 0){
            manufacturer.setText(medicine.getManufacturer());
        }

        EditText dosage = (EditText) findViewById(R.id.editDosage);
        dosage.setText(String.valueOf(medicine.getDosage()));

        EditText purpose = (EditText) findViewById(R.id.editMedicinePurpose);
        if(medicine.getPurpose().length() > 0){
            purpose.setText(medicine.getPurpose());
        }

        EditText remark = (EditText) findViewById(R.id.editMedicineRemark);
        if(medicine.getMedicineRemarks().length() > 0){
            remark.setText(medicine.getMedicineRemarks());
        }
    }

    /**
     * Get user input and validate user input when user click finish.
     * If input is valid then parse the data to UpdateMedicine class to perform the database operations.
     * @throws InterruptedException
     * @throws UnsupportedEncodingException
     * @throws ExecutionException
     */
    public void getUserInput() throws InterruptedException, UnsupportedEncodingException, ExecutionException {
        // Indicate if there is invalid input field
        boolean warning = false;

        // ScrollView
        ScrollView scroll = (ScrollView) findViewById(R.id.editMedicineScrollView);
        // Y positions that might need to scroll to later
        int dosageY = (int) ((RelativeLayout) findViewById(R.id.DosageLayout)).getY();

        // Get manufacturer name (Optional)
        String manufacturerInput = ((EditText) findViewById(R.id.editManufacturer)).getText().toString();

        // Get dosage(strength)
        int dosageInput = 0;
        EditText dosageInputField = (EditText) findViewById(R.id.editDosage);
        if(TextUtils.isEmpty(dosageInputField.getText().toString())) {
            dosageInputField.setError("Please enter the strength of the medicine.");
            if(!warning){
                scroll.scrollTo(0,dosageY);
                warning = true;
            }
        }else{
            dosageInput = Integer.parseInt(dosageInputField.getText().toString());
            if(dosageInput <= 0){
                dosageInputField.setError("Please enter a valid number.");
                if(!warning){
                    scroll.scrollTo(0,dosageY);
                    warning = true;
                }
            }
        }

        // Get purpose
        String purposeInput = ((EditText) findViewById(R.id.editMedicinePurpose)).getText().toString();
        // Get remarks
        String remarkInput = ((EditText) findViewById(R.id.editMedicineRemark)).getText().toString();

        if(!warning){
            // Input to database
            UpdateMedicine postData = new UpdateMedicine(medicine.getMedicineId(),manufacturerInput,dosageInput,purposeInput,remarkInput);
            if(postData.getStatus() == 1){
                Toast toast = Toast.makeText(getApplicationContext(), "Medicine detail has been changed", Toast.LENGTH_SHORT);
                toast.show();
                finish();
            }
        }
    }
}