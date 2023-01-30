package com.example.mangot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseUser fbUser = mAuth.getCurrentUser();
        if(fbUser!=null)
        {
            Intent npage = new Intent(this,DashboardActivity.class);
            startActivity(npage);

        }

    }
    //
    public void onclicklogin(View view)
    {
        EditText nemail = findViewById(R.id.editTextTextEmailAddress);
        EditText npassword = findViewById(R.id.editTextTextPassword);
        EditText confirmpassword = findViewById(R.id.editTextTextConfirmationPassword);
        String password = npassword.getText().toString();
        String passConfirm = confirmpassword.getText().toString();
        if(password.equals(passConfirm))
        {
            String email = nemail.getText().toString();

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task< AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Intent npage = new Intent(Login.this,DashboardActivity.class);
                                startActivity(npage);
                                // move to user activity
                                //Intent npage = new Intent(this,MainActivity.class);

                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this,"failed " + task.getException(),Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }

    }
}