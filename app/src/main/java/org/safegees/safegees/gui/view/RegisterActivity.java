/**
 *   RegisterActivity.java
 *
 *   Future class description
 *
 *
 *   Copyright (C) 2016  Victor Purcallas <vpurcallas@gmail.com>
 *
 *   Safegees is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Safegees is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with ARcowabungaproject.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.safegees.safegees.gui.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.safegees.safegees.R;
import org.safegees.safegees.util.SafegeesConnectionManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        email = (EditText) this.findViewById(R.id.reg_email);
        email2 = (EditText) this.findViewById(R.id.email_repeat);
        password = (EditText) this.findViewById(R.id.password);
        password2 = (EditText) this.findViewById(R.id.password_re);
        phone = (EditText) this.findViewById(R.id.phone);
        bio = (EditText) this.findViewById(R.id.bio);
        register = (Button) this.findViewById(R.id.buton_send_register);

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
        }else if(!isEmailValid(emailStr)){
            Toast.makeText(this, "Email not valid", Toast.LENGTH_SHORT).show();
        }else if (!passwordStr.equals(password2Str)) {
            Toast.makeText(this, "The password field doesn't match", Toast.LENGTH_SHORT).show();
        }else if(passwordStr.length() <= 4 ){
            Toast.makeText(this, "Password too short", Toast.LENGTH_SHORT).show();
        }else{
            View view = this.getCurrentFocus();
            if (view != null) {
                //Hide keyboard
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            UserRegisterTask urt = new UserRegisterTask(this,email2Str,password2Str,nameStr,surnameStr,phoneStr,bioStr);
            urt.execute();
        }

    }

    private boolean isEmailValid(String email) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        boolean m = matcher.matches();
        return ((!email.isEmpty()) && (email!=null) && (matcher.matches()));
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private Context context;
        private String name;
        private String surname;
        private String email;
        private String password;
        private String phone;
        private String bio;

        UserRegisterTask(Context context, String email, String password, String name, String surname, String phone, String bio) {
            this.context = context;
            this.email = email;
            this.password = password;
            this.name = name;
            this.surname = surname;
            this.phone = phone;
            this.bio = bio;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            boolean isRegistered = false;
            try {
                // Simulate network access.
                SafegeesConnectionManager scc = new SafegeesConnectionManager();
                isRegistered = scc.userRegister(this.context,this.email,this.password,this.name,this.surname,this.phone,this.bio);
            } catch (Exception e) {
                return false;
            }

            // TODO: register the new account here.
            return isRegistered;
        }

        @Override
        protected void onPostExecute(final Boolean success) {

            if (success) {
                Toast.makeText(this.context, "Registration success!", Toast.LENGTH_SHORT).show();
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            } else {
                Toast.makeText(this.context, "Registration Error! Try again.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }
}
