package org.safegees.safegees.gui.view;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.safegees.safegees.R;
import org.safegees.safegees.util.DataStorageManager;
import org.safegees.safegees.util.SafegeesConnectionController;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    EditText name;
    EditText surname;
    EditText email;
    EditText email2;
    EditText password;
    EditText password2;
    EditText phone;
    EditText bio;

    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) this.findViewById(R.id.name);
        surname = (EditText) this.findViewById(R.id.surname);
        email = (EditText) this.findViewById(R.id.email);
        email2 = (EditText) this.findViewById(R.id.email_repeat);
        password = (EditText) this.findViewById(R.id.password);
        password2 = (EditText) this.findViewById(R.id.password_re);
        phone = (EditText) this.findViewById(R.id.phone);
        bio = (EditText) this.findViewById(R.id.bio);
        register = (Button) this.findViewById(R.id.register_button);

        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String nameStr = name.getText().toString();
        String surnameStr = surname.getText().toString();
        String emailStr = email.getText().toString();
        String email2Str = email2.getText().toString();
        String passwordStr = password.getText().toString();
        String password2Str = password2.getText().toString();
        String phoneStr = phone.getText().toString();
        String bioStr = bio.getText().toString();

        //Develope restrictions
        if (!emailStr.equals(email2Str)) {
            Toast.makeText(this, "The email field doesn't match", Toast.LENGTH_SHORT).show();
        }else if (!passwordStr.equals(password2Str)) {
            Toast.makeText(this, "The password field doesn't match", Toast.LENGTH_SHORT).show();
        }else if(emailStr.length() <= 3 && passwordStr.length() <= 3){
            Toast.makeText(this, "Email or password no valid", Toast.LENGTH_SHORT).show();
        }else{
            UserRegisterTask urt = new UserRegisterTask(this, emailStr, passwordStr);
            urt.execute();
        }

    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final Activity parentActivity;
        private final String mEmail;
        private final String mPassword;

        UserRegisterTask(Activity parentActivity, String email, String password) {
            this.parentActivity = parentActivity;
            this.mEmail = email;
            this.mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            boolean isRegistered = false;
            try {
                // Simulate network access.
                SafegeesConnectionController scc = new SafegeesConnectionController();
                isRegistered = scc.userRegister(mEmail, mPassword);
            } catch (Exception e) {
                return false;
            }

            // TODO: register the new account here.
            return isRegistered;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Toast.makeText(parentActivity, "Registration success!", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                Toast.makeText(parentActivity, "Registration Error! Try again.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
