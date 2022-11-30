package com.example.mangot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onclicklogin(View view)
    {
        EditText nemail = findViewById(R.id.editTextTextEmailAddress);
        EditText npassword = findViewById(R.id.editTextTextPassword);
        EditText confirmpassword = findViewById(R.id.editTextTextConfirmationPassword);
        if(npassword == confirmpassword)
        {
            String email = nemail.toString();
            String password = npassword.toString();
            /*mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information


                            } else {
                                // If sign in fails, display a message to the user.

                            }


                        }
                    });*/

        }

    }
}