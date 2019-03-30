package groep3.hr.nl.techlabhr;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {


    private UserRegisterTask URTask;
    //UI reference
    private Button registerButton;
    private AutoCompleteTextView emailView;
    private EditText passwordView1;
    private EditText passwordView2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAttempt();
            }
        });


        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView1 = (EditText) findViewById(R.id.password1);
        passwordView2 = (EditText) findViewById(R.id.password2);



    }

    private void registerAttempt(){
        String password1 = passwordView1.getText().toString();
        String password2 = passwordView2.getText().toString();
        String email = emailView.getText().toString();

        Boolean permission = true;

        if (!isPasswordValid(password1) && password1.equals(password2)){
            permission = false;
            passwordView1.setError(getString(R.string.error_invalid_password));
            passwordView2.setError(getString(R.string.error_invalid_password));
        }
        if (!isEmailValid(email)){
            permission = false;
            emailView.setError(getString(R.string.error_invalid_email));
        }

        if (permission){

        }


    }

    private boolean isPasswordValid(String password){
        if(password.length() > 4){

            return true;
        }
        return false;
    }

    private boolean isEmailValid(String email){
        if(email.length() > 4){
            return true;
        }
        return false;
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean>{
        UserRegisterTask(){

        }
    }
}
