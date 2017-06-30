package com.android.javier.bycicles;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener{

    private static final String TAG = "SignInActivity";

    private static final int RC_SIGN_IN = 777;

        private GoogleApiClient mGoogleApiClient;
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;
    private SignInButton sign;
    private LoginButton botonfacelogin;
    private CallbackManager callbackManager;

 //   private FirebaseAuth firebaseAuth;
   // private FirebaseAuth.AuthStateListener firebaselistener;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);

        // Button listeners
     //   findViewById(R.id.sign_in_button).setOnClickListener(this);
       // findViewById(R.id.sign_out_button).setOnClickListener(this);
        //findViewById(R.id.disconnect_button).setOnClickListener(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        // [START customize_button]
        // Set the dimensions of the sign-in button.
        sign = (SignInButton) findViewById(R.id.sign_in_button);
        sign.setSize(SignInButton.SIZE_WIDE);
            sign.setColorScheme(SignInButton.COLOR_DARK);


        sign.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v){
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);


            }
        });





        //FACEBOOK

//        callbackManager =CallbackManager.Factory.create();
  //      botonfacelogin = (LoginButton)findViewById(R.id.login_button);
    //    botonfacelogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
      //      @Override
        //    public void onSuccess(LoginResult loginResult) {
//                handleFacebookAccessToken(loginResult.getAccessToken());
          //      goInicioface();
            //}




            // @Override
            //public void onCancel() {
              //  Toast.makeText(getBaseContext(), R.string.cancelface,Toast.LENGTH_SHORT);

            //}

            //@Override
            //public void onError(FacebookException error) {
              //  Toast.makeText(getBaseContext(), R.string.errorface,Toast.LENGTH_SHORT);

//            }
  //      });
        // [END customize_button]




        ///firebase


     //   firebaseAuth = FirebaseAuth.getInstance();
       // firebaselistener = new FirebaseAuth.AuthStateListener() {
         //   @Override
           // public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
             //   FirebaseUser user = firebaseAuth.getCurrentUser();
              //  if(user!=null){
                //    goInicio();
                //}

            //}
        //};
    //}

    //private void handleFacebookAccessToken(AccessToken accessToken) {

      //  AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        //firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
          //  @Override
            //public void onComplete(@NonNull Task<AuthResult> task) {
              //  if(!task.isComplete()){
                //    Toast.makeText(getBaseContext(), R.string.errorface,Toast.LENGTH_SHORT);

                //}

//            }
  //      });
    }

    @Override
    public void onStart() {
        super.onStart();
//        firebaseAuth.addAuthStateListener(firebaselistener);



    }

    @Override
    protected void onStop() {
        super.onStop();
    //    firebaseAuth.removeAuthStateListener(firebaselistener);
    }

    // [START onActivityResult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

//        callbackManager.onActivityResult(requestCode, resultCode,data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            
            goInicio();
            // Signed in successfully, show authenticated UI.
       //     GoogleSignInAccount acct = result.getSignInAccount();
         //   mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
           // updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }


//    private void goInicioface() {

  //      Intent intetn = new Intent (this,Navigation.class);
    //    intetn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
      //  startActivity(intetn);
    //}
    // [END handleSignInResult]

    // [START signIn]
  //  private void signIn() {
    //    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
      //  startActivityForResult(signInIntent, RC_SIGN_IN);
    //}
    // [END signIn]

    // [START signOut]
    //private void signOut() {
      //  Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
        //        new ResultCallback<Status>() {
          //          @Override
            //        public void onResult(Status status) {
                        // [START_EXCLUDE]
                        //updateUI(false);
                        // [END_EXCLUDE]
              //      }
                //});
    //}
    // [END signOut]

    // [START revokeAccess]
    //private void revokeAccess() {
      //  Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
        //        new ResultCallback<Status>() {
          //          @Override
            //        public void onResult(Status status) {
                        // [START_EXCLUDE]
                        //updateUI(false);
                        // [END_EXCLUDE]
              //      }
                //});
    //}
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }
/**
    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }
**/


    //@Override
    //public void onClick(View v) {
      //  switch (v.getId()) {
        //    case R.id.sign_in_button:
          //      signIn();
            //    break;
            //case R.id.sign_out_button:
                  //  signOut();
                //break;
            //case R.id.disconnect_button:
              //  revokeAccess();
//                break;
      //  }
    //}





private void goInicio() {

    Intent intetn = new Intent (this,Navigation.class);
    intetn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
    startActivity(intetn);
}




}
