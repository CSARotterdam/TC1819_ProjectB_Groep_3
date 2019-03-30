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


//  private UserRegisterTask URTask;
    //UI reference
    private Button registerButton;
    private AutoCompleteTextView emailView;
    private EditText passwordView1;
    private EditText passwordView2;

    DatabaseHelper mDatabaseHelper;


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
        mDatabaseHelper = new DatabaseHelper(this);


    }

    private void registerAttempt(){
        String password1 = passwordView1.getText().toString();
        String password2 = passwordView2.getText().toString();
        String email = emailView.getText().toString();

        Boolean permission = true;

        if (!isPasswordValid(password1)){
            permission = false;
            passwordView1.setError(getString(R.string.error_invalid_password));
            passwordView2.setError(getString(R.string.error_invalid_password));
        }

        if(!password1.equals(password2)){
            permission = false;
            passwordView2.setError(getString(R.string.error_password_match));
        }
        if (!isEmailValid(email)){
            permission = false;
            emailView.setError(getString(R.string.error_invalid_email));
        }

        if(mDatabaseHelper.emailExists(email)){
            permission = false;
            emailView.setError(getString(R.string.error_email_already_exists));
        }

        if (permission){
//          URTask = new UserRegisterTask(email,password1);
//          URTask.execute((Void) null);
            Contacts newUser = new Contacts();
            newUser.setEmail(email);
            newUser.setPassword(password1);
            newUser.setPermission("user");
            mDatabaseHelper.insertContacts(newUser);
            Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);

        }


    }

    private boolean isPasswordValid(String password){
        if(password.length() > 4){

            return true;
        }
        return false;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

//    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mEmail;
//        private final String mPassword;
//
//        UserRegisterTask(String email, String password) {
//            mEmail = email;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
////            // TODO: attempt authentication against a network service.
////
//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            URTask = null;
////            showProgress(false);
//
//            if (success) {
////                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
////                intent.putExtra("email",mEmail);
////                intent.putExtra("password",mPassword);
////                startActivity(intent);
//                tv.setText(mDatabaseHelper.searchPass("eduard"));
//            } else {
////                mPasswordView.setError(getString(R.string.error_incorrect_password));
////                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            URTask = null;
////            showProgress(false);
//        }
//    }
}
