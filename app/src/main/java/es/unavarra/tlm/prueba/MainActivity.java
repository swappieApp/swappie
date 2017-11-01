package es.unavarra.tlm.prueba;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import es.unavarra.tlm.prueba.PantallaPrincipal.UsuarioRegistrado;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private RelativeLayout sesion;
    private RelativeLayout nosesion;


    private  SignInButton google;
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private static final String TAG="MAINACTIVITY";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private ProgressDialog progressDialog;

    String name = "";
    String email = "";
    String id = "";
    String birthday ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ----------------------  COMPROBAMOS SI YA HAY UNA SESIÓN INICIADA, Y SI LA HAY
        //                          INICIAMOS LA ACTIITY UsuarioRegistrado ----------------------//

        SharedPreferences settings = getSharedPreferences("Config", 0);
        boolean sesion = settings.getBoolean("sesion",false);
        if(sesion==true){

            Intent intent = new Intent(this, UsuarioRegistrado.class);
            startActivity(intent);
            finish();
        }


        // ----------------------  FIREBASE PARA LOGIN CON GOOGLE ----------------------//
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user!=null){

                    String name = user.getDisplayName();
                    String imagen = String.valueOf(user.getPhotoUrl());
                    String id = user.getUid();
                    String email = user.getEmail();

                    Log.d("etiqueta", name);
                    Log.d("etiqueta", imagen);
                    Log.d("etiqueta", id);

                    SharedPreferences info = getSharedPreferences("Config", 0);
                    SharedPreferences.Editor editor = info.edit();
                    editor.putString("metodo","google");
                    editor.putString("nombre",name);
                    editor.putString("email",email);
                    editor.putBoolean("sesion", true);
                    editor.putString("foto", imagen);
                    editor.commit();

                    Intent intent = new Intent(MainActivity.this,UsuarioRegistrado.class);
                    startActivity(intent);
                    finish();

                }
            }
        };

        google = (SignInButton) findViewById(R.id.loginButtonGoogle);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
                    }
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signIn();
            }
        });



        // ----------------------  LOGIN CON FACEBOOK ----------------------//

        loginButton = (LoginButton) findViewById(R.id.loginButton);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_friends", "email", "user_birthday"));

        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {


            @Override
            public void onSuccess(LoginResult loginResult) {



                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                displayUserInfo(object);

                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,link,gender,birthday,email,picture");
                request.setParameters(parameters);
                request.executeAsync();


            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), R.string.cancel_login, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), R.string.error_login, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void signIn() {

        progressDialog= new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Signing in...");
        progressDialog.show();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    protected void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }




    public void registro(View view){

        Intent intent = new Intent(this, Registrarse.class);
        startActivity(intent);
        finish();

    }

    public void logout(View view) {
        LoginManager.getInstance().logOut();
        SharedPreferences info = getSharedPreferences("Config", 0);
        SharedPreferences.Editor editor = info.edit();
        editor.putBoolean("sesion", false);
        sesion.setVisibility(View.GONE);
        nosesion.setVisibility(View.VISIBLE);

    }

    public void displayUserInfo(JSONObject object){


        String name ="";
        String email = "";
        String id="";

        try {
            name = object.getString("name");
            email = object.getString("email");
            id = object.getString("id");

            Intent i=new Intent(MainActivity.this, UsuarioRegistrado.class);
            i.putExtra("name",name);
            i.putExtra("id",id);
            i.putExtra("email",email);

            startActivity(i);
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        SharedPreferences info = getSharedPreferences("Config", 0);
        SharedPreferences.Editor editor = info.edit();
        editor.putString("metodo","facebook");
        editor.putBoolean("sesion", true);
        editor.putString("nombre",name);
        editor.putString("id",id);
        editor.putString("email",email);

        editor.commit();
        //prefs = getSharedPreferences("es.unavarra.tlm", Context.MODE_PRIVATE);
        //prefs.edit().putString("es.unavarra.tlm.sesion", "true").apply();
        //prefs.edit().putString("es.unavarra.tlm.email", email).apply();
        //prefs.edit().putString("es.unavarra.tlm.id", id).apply();
        //prefs.edit().putString("es.unavarra.tlm.birthday", birthday).apply();


    }


}
