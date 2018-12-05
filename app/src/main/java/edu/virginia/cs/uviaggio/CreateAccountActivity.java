package edu.virginia.cs.uviaggio;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
    private TextView logoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getSupportActionBar().setTitle("UViaggio // Create an Account");

        mAuth = FirebaseAuth.getInstance();

        logoText = findViewById(R.id.textView);
        logoText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Pacifico-Regular.ttf"));

        //Define Views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        //Attach onClick listeners
        findViewById(R.id.createButton).setOnClickListener(this);
    }

    private void createAccount(String email, String password){
        //Check for validation of form!
        if(!email.equals("") && !password.equals("") && email.contains("@") && password.length() >= 6) {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Create user success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(CreateAccountActivity.this, "Account Creation Success!",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                                intent.putExtra("userEmail", emailInput.getText().toString());
                                startActivity(intent);
                            } else {
                                // If create user fails, display a message to the user.
                                Log.e("Creation failed", task.getException().getMessage());
                                Toast.makeText(CreateAccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                //Update UI
                            }
                        }

                    });
        }else if(!email.equals("") && password.equals("")){
            Toast.makeText(CreateAccountActivity.this, "Please enter a valid, secure password", Toast.LENGTH_LONG).show();
        }else if(email.equals("") && !password.equals("")){
            passwordInput.setText("");
            Toast.makeText(CreateAccountActivity.this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
        }else if(password.length() < 6) {
            passwordInput.setText("");
            Toast.makeText(CreateAccountActivity.this, "Please enter a valid, secure password longer than 6 characters", Toast.LENGTH_LONG).show();
        }else if(!email.contains("@")) {
            passwordInput.setText("");
            Toast.makeText(CreateAccountActivity.this, "Please enter a valid email address", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(CreateAccountActivity.this, "Please enter valid account credentials", Toast.LENGTH_LONG).show();
        }
    }

    public void onClick(View v){
        int from = v.getId();
        if(from == R.id.createButton){
            //Create account and return to signin page
            //TODO: Pre-populate signin credentials for just-created account
            createAccount(emailInput.getText().toString(), passwordInput.getText().toString());
        }
    }
}
