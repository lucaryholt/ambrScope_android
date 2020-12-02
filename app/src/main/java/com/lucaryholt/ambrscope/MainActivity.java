package com.lucaryholt.ambrscope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lucaryholt.ambrscope.Repo.Repo;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int RC_SIGN_IN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Repo.r().startListener();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null) {
            login();
        }

        // TODO
        // 1. DetailView for Spots - Done!
        // 2. Decide either to have spot added at the location of device or new MapsActivity for choosing spot (with long press) - Done!
        // 3. Photo implemented - both choosing/taking and upload - Done!
        // 4. Better solution for updating markers on map - Not needed.
        // 5. Login - Done!
        // 6. My page with own added markers in a list - Done!
        // 7. MapsActivity with spots integrated on MainActivity
        // 8. Toasts
        // 9. Styling
        // 10. Other marker icon, maybe small amber icon
        // 11. App icon
    }

    public void test0Button(View view) {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public void test1Button(View view) {
        Intent intent = new Intent(this, AddSpotMapsActivity.class);
        startActivity(intent);
    }

    public void logoutButton(View view) {
        FirebaseAuth.getInstance().signOut();
        login();
    }

    public void myPageButton(View view) {
        Intent intent = new Intent(this, MyPage.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                Log.i("AuthInfo", "User " + user.getUid() + " has logged in.");
            } else {
                Log.i("AuthInfo", "Login failed.");
                Log.i("AuthInfo", response.getError().getErrorCode() + "");
                login();
            }
        }
    }

    private void login() {
        Log.i("AuthInfo", "Not logged in.");
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(), RC_SIGN_IN);
    }
}