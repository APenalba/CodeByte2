package edu.ub.pis.usecases.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import edu.ub.pis.R;
import edu.ub.pis.usecases.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private Button login_button;
    private Button signup_button;
    private Button recuperaPassword_button;
    private TextView email_text;
    private TextView password_text;

    private CheckBox keepSession_cb;

    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        firebaseAuth = FirebaseAuth.getInstance();

        login_button = findViewById(R.id.login_bttn);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (email_text.getText().toString().isEmpty()) {
                        //TODO mensaje de error
                    }else if (password_text.getText().toString().isEmpty()) {
                        //TODO mensaje de error
                    }else {
                        firebaseAuth.
                                signInWithEmailAndPassword(email_text.getText().toString(),
                                        password_text.getText().toString()).
                                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            //TODO cambiar activity
                                        }else {
                                            //TODO manage error
                                        }
                                    }
                                });
                    }

                }catch (Exception e) {
                    showError(e);
                }
            }
        });


        signup_button = findViewById(R.id.signup_bttn);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(RegisterActivity.class);
            }
        });

        recuperaPassword_button = findViewById(R.id.recuperaPassword_bttn);
        email_text = findViewById(R.id.email_textView);
        password_text = findViewById(R.id.password_editText);
    }

    private void showError(Exception e) {
        //TODO
    }
    private void startNewActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        // finish(); No sé si deberia cerrar la actividad o deberia esperarme a hacer un login
    }
}