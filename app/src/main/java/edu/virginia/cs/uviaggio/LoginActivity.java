package edu.virginia.cs.uviaggio;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private EditText emailInput;
    private EditText passwordInput;
    private TextView logoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        //Underline text
        TextView createAccountTextView = (TextView) findViewById(R.id.createAccountText);
        SpannableString content = new SpannableString(createAccountTextView.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        createAccountTextView.setText(content);

        //Style Logo Text
        logoText = findViewById(R.id.textView);
        logoText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Pacifico-Regular.ttf"));
        //logoText.setTextSize(72);

        //Define views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);

        //Attach onclick listeners
        findViewById(R.id.loginButton).setOnClickListener(this);
        findViewById(R.id.createAccountText).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart(){
        super.onStart();
        //Check for user being signed in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        try {
            Log.d("User", currentUser.getDisplayName());
        }catch(NullPointerException e){
            Log.e("No User", "Poop");
        }
    }

    private void signin(String email, String password){
        //TODO: Check for validation of form

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Success! Signed in",
                                    Toast.LENGTH_SHORT).show();

                            //Send to class list screen
                            Intent intent = new Intent(LoginActivity.this, ClassCardViewActivity.class);
                            //TODO: Load intent with any necessary info about user (do i need to do this?)
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.d("Logging in?", "Was Unsuccessful");
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //TODO: Update UI with login not accepted message
                        }


                    }
                });
    }

    @Override
    public void onClick(View v){
        int from = v.getId();
        if(from == R.id.loginButton){
            Log.d("Logging in?", "Shit");
            signin(emailInput.getText().toString(), passwordInput.getText().toString());
        }else if(from == R.id.createAccountText){
            //Go to CreateAccountActivity
            Intent intent = new Intent(this, CreateAccountActivity.class);
            startActivity(intent);
        }
    }
}
