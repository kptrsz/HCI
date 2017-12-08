package ptr.hf.ui.auth;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ptr.hf.ChargerApplication;
import ptr.hf.R;
import ptr.hf.ui.MainActivity;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    public static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 9001;

    @BindView(R.id.login_email)
    TextInputEditText loginEmail;
    @BindView(R.id.login_password)
    TextInputEditText loginPassword;
    @BindView(R.id.login_button)
    Button loginButton;
    @BindView(R.id.login_google)
    Button loginGoogle;
//    @BindView(R.id.login_reset)
//    TextView loginReset;
    @BindView(R.id.login_register)
    TextView loginRegister;
    @BindView(R.id.login_password2)
    TextInputEditText loginPassword2;
    @BindView(R.id.resetAndRegister)
    LinearLayout resetAndRegister;

    private FirebaseAuth firebaseAuth;
    private GoogleApiClient googleApiClient;
    private ProgressDialog progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        GoogleSignInOptions googleSignInOptions =
                new GoogleSignInOptions
                        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        progress = new ProgressDialog(this);
        progress.setTitle("Bejelentkezés");
        progress.setMessage("Kérem várjon...");
        progress.setCancelable(true);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth != null) {
            updateUI(firebaseAuth.getCurrentUser());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChargerApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChargerApplication.activityPaused();
    }

    private void createUserWithEmailAndPassword(final String email, final String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            makeSnack("Sikeres Regisztráció!");
                            updateUI(user);
                        } else {
                            Snackbar
                                    .make(findViewById(android.R.id.content),
                                            "Hiba lépett fel a regisztráció során." +
                                                    "\nKérem próbálja meg később!",
                                            Snackbar.LENGTH_LONG)
                                    .setAction("ÚJRA", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            createUserWithEmailAndPassword(email, password);
                                        }
                                    }).show();
                            progress.dismiss();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getRootView().getWindowToken(), 0);
                        }
                    }
                });
    }

    private void signInWithEmailAndPassword(final String email, final String password) {
        if (email.equals("") || password.equals("")) {
            makeSnack("Kérem töltsön ki minden mezőt!");
        }
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            Snackbar
                                    .make(findViewById(android.R.id.content),
                                            "Hiba lépett fel a belépés közben." +
                                                    "\nKérem próbálja meg később!",
                                            Snackbar.LENGTH_LONG)
                                    .setAction("ÚJRA", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            signInWithEmailAndPassword(email, password);
                                        }
                                    }).show();
                            progress.dismiss();
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getRootView().getWindowToken(), 0);

                        }
                    }
                });
    }

    private void updateUI(@Nullable FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private void googleSignIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            updateUI(null);
                        }
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        makeSnack("Hiba lépett fel a kapcsolódás során." +
                "\nKérem próbálja meg később!");
        progress.dismiss();
    }

    private void makeSnack(String message) {
        Snackbar
                .make(findViewById(R.id.placeSnackBar),
                        message,
                        Snackbar.LENGTH_LONG)
                .show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getRootView().getWindowToken(), 0);
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @OnClick({R.id.login_button, R.id.login_google/*, R.id.login_reset*/, R.id.login_register})
    public void onViewClicked(View view) {
        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        switch (view.getId()) {
            case R.id.login_button:
                if (loginPassword2.getVisibility() == View.GONE)
                    if (isEmailValid(email)) {
                        progress.show();
                        signInWithEmailAndPassword(email, password);
//                    progress.dismiss();
                    } else if (!isEmailValid(email)) {
                        makeSnack("Az e-mail cím nem megfelelő.");
                    } else
                        makeSnack("A jelszó nem megfelelő.");
                else if (!isEmailValid(email))
                makeSnack("Nem megfelelő e-mail cím.");
                else if (!loginPassword.getText().toString().equals(loginPassword2.getText().toString())){
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getRootView().getWindowToken(), 0);
//                    makeSnack("A két jelszó nem egyezik meg.");
                    Snackbar
                            .make(findViewById(android.R.id.content),
                                    "A két jelszó nem egyezik meg!",
                                    Snackbar.LENGTH_LONG)
                            .show();}
                else if (loginPassword.getText().toString().equals(loginPassword2.getText().toString()))
                    createUserWithEmailAndPassword(email, password);
                else makeSnack("Nem sikerült kapcsolódni a kiszolgálóhoz, kérem próbálja újra később.");
                break;
            case R.id.login_google:
                progress.show();
                googleSignIn();
//                progress.dismiss();
                break;
//            case R.id.login_reset:
//                if (isEmailValid(email)) {
////                    progress.show();
//                    makeSnack("A bejelentkezési információkat elküldtük.");
////                    forgotPassword();
////                    progress.dismiss();
//                } else
//                    makeSnack("Kérem adja meg az e-mail címet!");
//                break;
            case R.id.login_register:
                loginPassword2.setVisibility(View.VISIBLE);
                loginGoogle.setVisibility(View.GONE);
                loginButton.setText("Regisztráció és Bejelentkezés");
                resetAndRegister.setVisibility(View.GONE);
//                if (isEmailValid(email) /*&& password.length() > 6*/) {
//                    loginPassword2.setVisibility(View.VISIBLE);
//                    loginGoogle.setVisibility(View.GONE);
//                    loginButton.setText("Regisztráció és Bejelentkezés");
////                    createUserWithEmailAndPassword(email, password);
////                    progress.dismiss();
//                } else if (!isEmailValid(email))
//                    makeSnack("Az e-mail cím nem megfelelő");
//                else
//                    makeSnack("A jelszónak legalább 6 karakternek kell lennie");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (loginPassword2.getVisibility() == View.VISIBLE) {
            loginPassword2.setVisibility(View.GONE);
            loginGoogle.setVisibility(View.VISIBLE);
            loginButton.setText("Bejelentkezés");
            resetAndRegister.setVisibility(View.VISIBLE);

        } else
            super.onBackPressed();
    }
}

