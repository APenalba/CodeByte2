package edu.ub.pis.usecases.login;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;

import edu.ub.pis.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

    }
}