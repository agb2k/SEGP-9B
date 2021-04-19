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

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

import medPal.App.DatabaseHelper;
import medPal.App.R;
import medPal.App.UserIdentification.UserIdentityDBHelper;

public class Login extends AppCompatActivity {

    //Create key for Intent to start VerifyEmail Activity
    public final static String EXTRA_LOGINEMAIL = "com.medPal.App.login_signup.LOGINEMAIL";
    public final static String EXTRA_LOGINPASSWORD = "com.medPal.App.login_signup.LOGINPASSWORD";

    //Declare View UI element to access
    EditText password_view, email_view;
    Button buttonLogin_view;
    TextView login_textview, login_forgotPasswordView;
    ProgressBar login_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Access UI View element
        password_view = (EditText) findViewById(R.id.login_password);
        email_view = (EditText) findViewById(R.id.login_email);
        buttonLogin_view = (Button) findViewById(R.id.login_buttonLogin);
        login_progress = (ProgressBar) findViewById(R.id.login_progress);
        login_textview = (TextView) findViewById(R.id.login_alreadyHaveAcc);
        login_forgotPasswordView = (TextView) findViewById(R.id.login_forgotPassword);

        //set OnClick listener for Textview to switch to SignUp activity
        login_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_toSignUp = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent_toSignUp);
            }
        });

        //set OnClick listener for Textview to switch to Forgot Password activity
        login_forgotPasswordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_toForgotPassword = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(intent_toForgotPassword);
            }
        });

        //set listener for button Sign Up view
        buttonLogin_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get input from View Element 'EditText'
                String email_input = email_view.getText().toString();
                String password_input = password_view.getText().toString();

                //set listener for button Sign Up view
                buttonLogin_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get input from View Element 'EditText'
                        String email_input = email_view.getText().toString();
                        String password_input = password_view.getText().toString();

                        //check if all input fields id filled
                        if (!email_input.equals("") && !password_input.equals("")) {
                            //Start ProgressBar (Set visibility VISIBLE)
                            login_progress.setVisibility(View.VISIBLE);
                            //set event handler for onClick
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //DATABASE CONNECTION CHANGE http://hpyzl1.jupiter.nottingham.edu.my/Login_php/login.php
                                    try {
                                        DatabaseHelper dbHelper = new DatabaseHelper("https://bulacke.xyz/medpal-db/login.php");
                                        //DatabaseHelper dbHelper = new DatabaseHelper("http://hpyzl1.jupiter.nottingham.edu.my/Login_php/login.php");
                                        dbHelper.encodeData("email",email_input);
                                        dbHelper.encodeData("password",password_input);

                                        String result = dbHelper.send();
                                        result = result.substring(1,result.length()-1);
                                        //End ProgressBar (Set visibility to GONE)
                                        login_progress.setVisibility(View.GONE);
                                        Log.v("PutData", result);
                                        //check php echo reply 'result'
                                        //if 'sign up success', show msg, open Login activity & close SignUp activity
                                        //else display echo as toast
                                        if (result.equals("Login Success")) {
                                            Toast toast_loginSuccess = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                                            toast_loginSuccess.show();
                                            setUserIdentification(email_input);
                                            finish();
                                        } else if (result.equals("Account not activated yet")) {
                                            Toast toast_accountNotVerified = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                                            toast_accountNotVerified.show();
                                            Intent intent_toVerifyEmail = new Intent(getApplicationContext(), VerifyEmail.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putString(EXTRA_LOGINEMAIL, email_input);
                                            intent_toVerifyEmail.putExtras(bundle);
                                            startActivity(intent_toVerifyEmail);
                                        } else {
                                            Toast toast_loginFail = Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT);
                                            toast_loginFail.show();
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
                        } else {
                            Toast toast_incompleteField = Toast.makeText(getApplicationContext(), R.string.unfilled_Parameter, Toast.LENGTH_LONG);
                            toast_incompleteField.show();
                        }
                    }
                });


            }
        });
    }

    private void setUserIdentification(String email) {
        UserIdentityDBHelper userDbHelper = new UserIdentityDBHelper(this.getApplicationContext());
        userDbHelper.insertRecord(email);
        userDbHelper.getRecord();
    }

    // Avoid users to by-pass the verification phase
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
