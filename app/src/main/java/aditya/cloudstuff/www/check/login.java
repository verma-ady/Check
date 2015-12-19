package aditya.cloudstuff.www.check;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.Scope;

public class login extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private boolean checkLogOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("MyApp", "onCreate() start");
        super.onCreate(savedInstanceState);
        checkLogOut = getIntent().getBooleanExtra("LogOut", false);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStatusTextView = (TextView) findViewById(R.id.status);
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();
        Log.v("MyApp", "onCreate() 1");
        //Build a GoogleApiClient with access to the Google Sign-In API and the
                // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this )
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).addScope(new Scope(Scopes.EMAIL)).build();
        Log.v("MyApp", "onCreate() 2");

        // Customize sign-in button. The sign-in button can be displayed in
        // multiple sizes and color schemes. It can also be contextually
        // rendered based on the requested scopes. For example. a red button may
        // be displayed when Google+ scopes are requested, but a white button
        // may be displayed when only basic profile is requested. Try adding the
        // Scopes.PLUS_LOGIN scope to the GoogleSignInOptions to see the
        // difference.
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setScopes(gso.getScopeArray());
        Log.v("MyApp", "onCreate() end");
    }//onCreate()

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            Log.v("MyApp", "onStart() if Login");
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
//             showProgressDialog();
            Log.v("MyApp", "onStart() else Login");
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
//                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        Log.v("MyApp", "onResume() Login");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.v("MyApp", "onPause() Login");
        super.onPause();
//        hideProgressDialog();
    }

    @Override
    protected void onStop() {
        Log.v("MyApp", "onStop() Login");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.v("MyApp", "onDestroy() Login");
        super.onDestroy();
    }

    @Override
    public void finish() {
        Log.v("MyApp", "finish() Login");
        super.finish();
    }

    @Override
    public void onBackPressed() {
        Log.v("MyApp", "onBackPresses() Login");
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            Log.v("MyApp", "handleSignInResult() if Login");
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText("Signed In as " + acct.getDisplayName());
            updateUI(true);

            if (checkLogOut){
                Log.v("MyApp", "checkLogOut = true");
            }

            else {
                Toast.makeText(this, "GOT IT", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, Home.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        } else {
            // Signed out, show unauthenticated UI.
            Log.v("MyApp", "handleSignInResult() else Login");
            updateUI(false);
        }
    }

    private void signIn(){
        Log.v("MyApp", "signIn Login");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut(){
        Log.v("MyApp", "signOut Login");
        checkLogOut = false;
        if(!mGoogleApiClient.isConnected()){
            Log.v("MyApp", "SignOut GAC is not connected");
            mGoogleApiClient.connect();
        } else if (mGoogleApiClient.isConnected()){
            Log.v("MyApp", "SignOut GAC is connected");
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(Status status) {
                    updateUI(false);
                }
            });
        }
    }

    private void revokeAccess() {
        checkLogOut = false ;
        Log.v("MyApp", "revokeAccess Login");
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                updateUI(false);
            }
        });
    }

    private void updateUI( boolean signedIn ){
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText("Signed Out");

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

}
