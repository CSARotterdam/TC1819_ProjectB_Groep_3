package groep3.hr.nl.techlabhr;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class SignUp extends Activity {

    DatabaseHelper helper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
    }
//    public void onSignupClick(View v)
//    {
//        if (v.getId()==R.id.signupbtn)
//        {
////            EditText name = (EditText)findViewById(R.id.sName);
////            EditText username = (EditText)findViewById(R.id.sUsername);
////            EditText email = (EditText)findViewById(R.id.sEmail);
////            EditText password = (EditText)findViewById(R.id.sPassword);
////            EditText confirmpassword = (EditText)findViewById(R.id.sConfirmPassword);
////
////            String namestr = name.getText().toString();
////            String usernamestr = username.getText().toString();
////            String emailstr = email.getText().toString();
////            String passwordstr = password.getText().toString();
////            String confirmpasswordstr = confirmpassword.getText().toString();
////
////            if(!passwordstr.equals(confirmpasswordstr))
////            {
////                //error msg
////                Toast pass = Toast.makeText(SignUp.this, "Passwords don't match", Toast.LENGTH_SHORT);
////                pass.show();
////            }
////            else
////            {
////                //store the infos in the database
////                Contacts c = new Contacts();
////                c.setName(namestr);
////                c.setUsername(usernamestr);
////                c.setEmail(emailstr);
////                c.setPassword(passwordstr);
////
////                helper.insertContacts(c);
////            }
//
//        }
//    }
}
