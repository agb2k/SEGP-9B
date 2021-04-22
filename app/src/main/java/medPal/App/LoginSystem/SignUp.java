package medPal.App.LoginSystem;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import medPal.App.DatabaseHelper;
import medPal.App.R;

public class SignUp extends AppCompatActivity {

    //Create key for Intent to start VerifyEmail Activity
    public final static String EXTRA_SIGNUPEMAIL = "com.medPal.App.login_signup.SIGNUPEMAIL";


    //Declare View UI element to access
    EditText password_view, email_view, confirmPassword_view;
    Button buttonSignUp_view;
    TextView signUp_textview, resend_view;
    ProgressBar signUp_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Access UI View element
        password_view = (EditText) findViewById(R.id.signUp_password);
        email_view = (EditText) findViewById(R.id.signUp_email);
        confirmPassword_view = (EditText) findViewById(R.id.signUp_confirmPassword);
        buttonSignUp_view = (Button) findViewById(R.id.signUp_buttonSignUp);
        signUp_progress = (ProgressBar) findViewById(R.id.signUp_progress);
        signUp_textview = (TextView) findViewById(R.id.signUp_alreadyHaveAcc);
        resend_view = (TextView) findViewById(R.id.signUp_resendEmail);
        //set OnClick listener for Textview to switch to Login activity
        signUp_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //set OnClick listener for Textview to switch to Resend activity
        resend_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_toResendVerification = new Intent(getApplicationContext(), ResendVerificationEmail.class);
                startActivity(intent_toResendVerification);
                finish();
            }
        });
        //set listener for button Sign Up view
        buttonSignUp_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get input from View Element 'EditText'
                String email_input = email_view.getText().toString();
                String password_input = password_view.getText().toString();
                String confirmPassword_input = confirmPassword_view.getText().toString();

                if (!email_input.equals("") && !password_input.equals("")) { //check if all input fields id filled
                    if (password_input.equals(confirmPassword_input)) {       //check if confirm password field is same as password field
                        signUp_progress.setVisibility(View.VISIBLE);            //Start ProgressBar (Set visibility VISIBLE)
                        Handler handler = new Handler(Looper.getMainLooper());  //set event handler for onClick
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    //DATABASE CONNECTION CHANGE http://hpyzl1.jupiter.nottingham.edu.my/Login_php/signup.php
                                    DatabaseHelper dbHelper = new DatabaseHelper("http://hpyzl1.jupiter.nottingham.edu.my/medpal-db/signup.php");
                                    //DatabaseHelper dbHelper = new DatabaseHelper("http://hpyzl1.jupiter.nottingham.edu.my/Login_php/signup.php");
                                    dbHelper.encodeData("email",email_input);
                                    dbHelper.encodeData("password",password_input);

                                    String result = dbHelper.send();
                                    result = result.substring(1,result.length()-1);
                                    //End ProgressBar (Set visibility to GONE)
                                    signUp_progress.setVisibility(View.GONE);
                                    Log.i("PutData", result);
                                    //check php echo 'result'
                                    //if 'sign up success', show msg, open Login activity & close SignUp activity
                                    //else display echo as toast
                                    if (result.equals("Sign Up Success")) {
                                        Toast toast_signUpSuccess = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                                        toast_signUpSuccess.show();
                                        Intent intent_toVerifyEmail = new Intent(getApplicationContext(), VerifyEmail.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString(EXTRA_SIGNUPEMAIL, email_input);
                                        intent_toVerifyEmail.putExtras(bundle);
                                        startActivity(intent_toVerifyEmail);
                                        finish();
                                    } else if (result.equals("Account already exist")) {
                                        Toast toast_signUpAccExist = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                                        toast_signUpAccExist.show();
                                        finish();
                                    } else if (result.equals("Account already exist, but not activated yet")) {
                                        Toast toast_ExistingAccNotActivated = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                                        toast_ExistingAccNotActivated.show();
                                        Intent intent_toVerifyEmail = new Intent(getApplicationContext(), VerifyEmail.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putString(EXTRA_SIGNUPEMAIL, email_input);
                                        intent_toVerifyEmail.putExtras(bundle);
                                        startActivity(intent_toVerifyEmail);
                                        finish();
                                    } else {
                                        Toast toast_signUpFail = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                                        toast_signUpFail.show();
                                    }
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                    } else {   //if password & confirm password field mismatch
                        Toast toast_passwordMismatch = Toast.makeText(getApplicationContext(), R.string.signUp_passwordMismatch, Toast.LENGTH_LONG);
                        toast_passwordMismatch.show();
                    }
                } else {  //if exist unfilled parameters
                    Toast toast_incompleteField = Toast.makeText(getApplicationContext(), R.string.unfilled_Parameter, Toast.LENGTH_LONG);
                    toast_incompleteField.show();
                }
            }
        });


    }
}
