package edu.virginia.cs.uviaggio;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText emailInput;
    private EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();

        //Define Views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        //Attach onClick listeners
        findViewById(R.id.createButton).setOnClickListener(this);
        findViewById(R.id.mapButton).setOnClickListener(this);

    }

    private void createAccount(String email, String password){
        //Check for validation of form!
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Create user success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user); -- Update UI here
                            Toast.makeText(CreateAccountActivity.this, "Create Account Success!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // If create user fails, display a message to the user.
                            Log.e("Creation failed", task.getException().toString());
                            Toast.makeText(CreateAccountActivity.this, "Account Creation failed!", Toast.LENGTH_SHORT).show();
                            //Update UI
                        }
                    }

                });
    }

    public void onClick(View v){
        int from = v.getId();
        if(from == R.id.createButton){
            //Create account and return to signin page
            //TODO: Pre-populate signin credentials for just-created account
            createAccount(emailInput.getText().toString(), passwordInput.getText().toString());
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        if(from == R.id.mapButton) {
            Intent intent = new Intent(this, GpsActivity.class);
            startActivity(intent);
        }
    }
}
