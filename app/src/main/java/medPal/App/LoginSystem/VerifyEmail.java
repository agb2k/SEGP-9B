package medPal.App.LoginSystem;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import medPal.App.DatabaseHelper;
import medPal.App.R;

public class VerifyEmail extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

        // To access Textview UI Element
        TextView verifyAcc_view = (TextView) findViewById(R.id.verifyEmail_msg);

        //Get data from intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String login_email = bundle.getString(Login.EXTRA_LOGINEMAIL);
        String signup_email = bundle.getString(SignUp.EXTRA_SIGNUPEMAIL);
        String resend_email = bundle.getString(ResendVerificationEmail.EXTRA_RESENDEMAIL);
        String email;
        String verifyAcc_msg;
        if (login_email == null && resend_email == null) {
            email = signup_email;
        } else if (signup_email == null && resend_email == null) {
            email = login_email;
        } else {
            email = resend_email;
        }
        verifyAcc_msg = String.format(getResources().getString(R.string.verifyEmail_msg), email);
        //Display msg with Textview
        verifyAcc_view.setText(verifyAcc_msg);

        //set OnClick listener for Textview to resend email
        TextView verifyEmail_resend_view = (TextView) findViewById(R.id.verifyEmail_resend);
        verifyEmail_resend_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DATABASE CONNECTION CHANGE http://hpyzl1.jupiter.nottingham.edu.my/Login_php/login.php
                try {
                    DatabaseHelper dbHelper = new DatabaseHelper("http://hpyzl1.jupiter.nottingham.edu.my/medpal-db/LoginSystem/resendVerificationEmail.php");
                    //DatabaseHelper dbHelper = new DatabaseHelper("http://hpyzl1.jupiter.nottingham.edu.my/Login_php/login.php");
                    dbHelper.encodeData("email",email);
                    String result = dbHelper.send();
                    result = result.substring(1,result.length()-1);
                    Log.i("PutData", result);
                    //check php echo reply 'result'
                    if (result.equals("Verification email sent")) {
                        Toast toast_sent = Toast.makeText(getApplicationContext(), R.string.Resend_VerificationEmailResent, Toast.LENGTH_SHORT);
                        toast_sent.show();
                    } else {
                        Toast toast_resentFail = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                        toast_resentFail.show();
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


        //set OnClick listener for Button to switch to Login activity
        Button verifyEmail_backToLogin_view = (Button) findViewById(R.id.verifyEmail_backtoLogin);
        verifyEmail_backToLogin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}