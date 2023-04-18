package edu.ub.pis.view.login;

import static android.content.ContentValues.TAG;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import edu.ub.pis.R;
import edu.ub.pis.view.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {

    private static final int REQ_ONE_TAP = 2;
    private boolean showOneTapUI = true;
    private Button login_button;
    private Button signup_button;
    private Button google_button;
    private Button recuperaPassword_button;
    private TextView email_text;
    private TextView password_text;

    private CheckBox keepSession_cb;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private GoogleSignInClient mSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("LOGIN");
        setContentView(R.layout.login_activity);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            goToHome();
        }

        login_button = findViewById(R.id.login_bttn);
        signup_button = findViewById(R.id.signup_bttn);
        google_button = findViewById(R.id.google_bttn);

        recuperaPassword_button = findViewById(R.id.recuperaPassword_bttn);

        email_text = findViewById(R.id.email_textView);
        password_text = findViewById(R.id.password_editText);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient( this, gso);


        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (email_text.getText().toString().isEmpty()) {
                        email_text.setError("Correo electrónico no válido");
                    } else if (password_text.getText().toString().isEmpty()) {
                        password_text.setError("Contraseña no válida");
                    } else {
                        mAuth.
                                signInWithEmailAndPassword(email_text.getText().toString(),
                                        password_text.getText().toString()).
                                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            goToHome();
                                        } else {
                                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }

                } catch (Exception e) {
                    showError(e);
                }
            }
        });
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewActivity(RegisterActivity.class);
            }
        });

        google_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void showError(Exception e) {
        Toast.makeText(this, e.toString(),
                Toast.LENGTH_SHORT).show();
    }

    private void startNewActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }
    private void goToHome() {
        currentUser = mAuth.getCurrentUser();
        //TODO cambiar activity
        //startNewActivity(HomeActivity.class);
        Log.v(TAG, "AQUI DEBERIA IR AL HOME ACTIVITY");
        //finish();
    }

    private void signIn() {
        Intent signInIntent = mSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, REQ_ONE_TAP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ONE_TAP) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                    goToHome();
                })
                .addOnFailureListener(this, e -> Toast.makeText(LoginActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }
}