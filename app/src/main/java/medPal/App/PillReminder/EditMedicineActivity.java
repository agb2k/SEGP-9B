package medPal.App.PillReminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutionException;

import medPal.App.DatabaseHelper;
import medPal.App.R;

/**
 * Activity to edit (or delete) an existing medicine.
 */
public class EditMedicineActivity extends AppCompatActivity {

    Medicine medicine;
    private ImageButton pillImage;

    // This variable holds the user selected image. 'null' indicates that user does not select any image or user removed the selected image.
    private Bitmap bitmap = null;
    // This variable represents the user's action on original image. 'false' means there is no image initially, or the user wants to remove the original image.
    private Boolean originalImage = false;
    // This variable indicates whether there is an image for this medicine initially, this will not be affected by user's action.
    private Boolean initiallyNoImage = false;
    // This variable checks if user remove or replace the original image.
    // There is a scenario where this variable is true but no need to update the database:
    // Initially no image, user selected an image, this variable is set to true, then the user remove the selected image.
    private Boolean isImageChange = false;

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
                try {
                    deleteMedicine();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void deleteMedicine() throws UnsupportedEncodingException, ExecutionException, InterruptedException {
        DatabaseHelper dbHelper = new DatabaseHelper(DatabaseHelper.DELETE,DatabaseHelper.MEDICINE);
        dbHelper.encodeData("id",String.valueOf(medicine.getMedicineId()));
        if(Integer.parseInt(dbHelper.send()) == 1) {
            if(deleteImage()) {
                Toast toast = Toast.makeText(getApplicationContext(), "Medicine deleted successfully", Toast.LENGTH_SHORT);
                toast.show();
                setResult(Activity.RESULT_OK);
                finish();
            }else{
                Toast toast = Toast.makeText(getApplicationContext(), "Failed to delete medicine image.", Toast.LENGTH_SHORT);
                toast.show();
                setResult(Activity.RESULT_OK);
                finish();
            }
        }else {
            Toast toast = Toast.makeText(getApplicationContext(), "Failed to delete medicine.", Toast.LENGTH_SHORT);
            toast.show();
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

    /**
     * Automatically fill in the original data.
     */
    public void putDataIntoInputField() {
        pillImage = (ImageButton) findViewById(R.id.PillImage);
        Picasso.get().load(medicine.getImagePath()).into(pillImage);
        if(medicine.getImagePath().equals("https://bulacke.xyz/medpal-img/")){
            initiallyNoImage = true;
        }else{
            originalImage = true;
        }
        pillImage.setOnClickListener(event -> {
            if(!originalImage && bitmap==null){
                // If currently no image
                showSourceOption();
            }else{
                showActionOption();
            }
        });

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
     * If input is valid then parse the data to database.
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
            sendData(medicine.getMedicineId(),manufacturerInput,dosageInput,purposeInput,remarkInput);
        }
    }

    private void sendData(int id, String medicineManufacturer, int dosage, String purpose, String remark) throws UnsupportedEncodingException, ExecutionException, InterruptedException {
        int status = 0;
        DatabaseHelper dbHelper = new DatabaseHelper(DatabaseHelper.UPDATE,DatabaseHelper.MEDICINE);
        dbHelper.encodeData("id", String.valueOf(id));
        dbHelper.encodeData("manufacturer", medicineManufacturer);
        dbHelper.encodeData("dosage", String.valueOf(dosage));
        dbHelper.encodeData("purpose", purpose);
        dbHelper.encodeData("remark", remark);
        if(Integer.parseInt(dbHelper.send()) == 1) {
            if(isImageChange){
                if(!(initiallyNoImage && bitmap==null)) {
                    if (bitmap != null) {
                        // If user replace or upload image.
                        if(updateImage()) {
                            status = 1;
                        }
                    } else if (!initiallyNoImage && bitmap==null) {
                        // If user wants to remove image
                        if(deleteImage()) {
                            status = 1;
                        }
                    }
                }else{
                    // No need to update image.
                    status = 1;
                }
            }else{
                status = 1;
            }

            if(status == 1) {
                Toast toast = Toast.makeText(getApplicationContext(), "Medicine detail has been changed", Toast.LENGTH_SHORT);
                toast.show();
                setResult(Activity.RESULT_OK);
                finish();
            }else{
                Toast toast = Toast.makeText(getApplicationContext(), "Failed to update image.", Toast.LENGTH_SHORT);
                toast.show();
                setResult(Activity.RESULT_OK);
                finish();
            }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(), "Failed to update medicine detail.", Toast.LENGTH_SHORT);
            toast.show();
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

    private boolean updateImage() throws UnsupportedEncodingException, ExecutionException, InterruptedException {
        DatabaseHelper dbHelper = new DatabaseHelper(DatabaseHelper.INSERT,DatabaseHelper.MEDICINE_IMAGE);
        dbHelper.encodeImage(bitmap);
        dbHelper.encodeData("medId",String.valueOf(medicine.getMedicineId()));
        return Integer.parseInt(dbHelper.send())==1;
    }

    private boolean deleteImage() throws UnsupportedEncodingException, ExecutionException, InterruptedException {
        DatabaseHelper dbHelper = new DatabaseHelper(DatabaseHelper.DELETE,DatabaseHelper.MEDICINE_IMAGE);
        dbHelper.encodeData("medId",String.valueOf(medicine.getMedicineId()));
        return Integer.parseInt(dbHelper.send())==1;
    }

    /**
     * Display dialog for image source options
     */
    public void showSourceOption() {
        AlertDialog.Builder sourceDialog = new AlertDialog.Builder(this);
        sourceDialog.setTitle("Select Image Source");
        sourceDialog.setCancelable(true);
        String[] sourceDialogItems = {"Photo Gallery", "Camera"};
        sourceDialog.setItems(sourceDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case 0:
                        requestGalleryPermission();
                        break;
                    case 1:
                        requestCameraPermission();
                        break;
                }
            }
        });
        sourceDialog.show();
    }

    /**
     * Display dialog for actions on selected pill image.
     */
    public void showActionOption() {
        AlertDialog.Builder actionDialog = new AlertDialog.Builder(this);
        actionDialog.setTitle("Select Action");
        actionDialog.setCancelable(true);
        String[] actionDialogItems = {"Remove Image","Select Other Image"};
        actionDialog.setItems(actionDialogItems, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case 0:
                        pillImage.setImageResource(R.drawable.ic_baseline_add_a_photo_24);
                        originalImage = false;
                        bitmap = null;
                        isImageChange = true;
                        return;
                    case 1:
                        showSourceOption();
                }
            }
        });
        actionDialog.show();
    }

    /**
     * Request permission for accessing image gallery.
     */
    private void requestGalleryPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            showFileChooser();
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
    }

    /**
     * Request permission for accessing camera.
     */
    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            takePhotoFromCamera();
            return;
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
    }

    /**
     * Handle user permissions.
     * @param requestCode Request code. 1 for image gallery permission, and 2 for camera permission.
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //Checking the request code of our request
        switch (requestCode) {
            case 1:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow in your app.
                    showFileChooser();
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(this, "Unable to access photo gallery", Toast.LENGTH_LONG).show();
                }
                return;
            case 2:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow in your app.
                    takePhotoFromCamera();
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    Toast.makeText(this, "Unable to access camera.", Toast.LENGTH_LONG).show();
                }
                return;
        }
    }

    /**
     * Select image from image gallery.
     */
    private void showFileChooser(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 1);
    }

    /**
     * Take picture using camera.
     */
    private void takePhotoFromCamera(){
        Intent camera = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 2);
    }

    /**
     * Get bitmap from image gallery or camera.
     * @param requestCode Request code. 1 for image gallery and 2 for camera.
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if(requestCode == 1) {
                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(requestCode == 2){
                bitmap = (Bitmap) data.getExtras().get("data");
            }
            pillImage.setImageBitmap(bitmap);
            isImageChange = true;
        }
    }
}