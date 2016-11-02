package co.broccli.spacify;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import android.app.ProgressDialog;
import android.util.Log;

import android.content.Intent;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.broccli.logic.SessionManager;
import co.broccli.logic.model.APIError.APIError;
import co.broccli.logic.model.APIError.ErrorUtils;
import co.broccli.logic.model.OAuth2AccessToken.AccessTokenRequest;
import co.broccli.logic.model.OAuth2AccessToken.OAuth2AccessToken;
import co.broccli.logic.rest.ApiClient;
import co.broccli.logic.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private SessionManager sessionManager;

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed("Login failed, Check your inputs");
            return;
        }

        _loginButton.setEnabled(false);

        final String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        doLogin(email, password);
    }

    protected void doLogin (final String email, final String password) {

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        ApiInterface apiService =
                ApiClient.createService(ApiInterface.class, getApplicationContext());

        Call<OAuth2AccessToken> call =
                apiService.getAccessTokenData(new AccessTokenRequest(email, password));

        call.enqueue(new Callback<OAuth2AccessToken>() {
            @Override
            public void onResponse(Call<OAuth2AccessToken> call, Response<OAuth2AccessToken> response) {

                if (response.isSuccessful()) {
                    if (!response.body().getAccessToken().isEmpty()) {
                        sessionManager = new SessionManager(getApplicationContext());
                        sessionManager.createLoginSession("", email, response.body().getAccessToken()); //TODO Add Name
                        onLoginSuccess();
                    } else {
                        onLoginFailed("Login failed, Try again later");
                        progressDialog.dismiss();
                    }
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    onLoginFailed(error.getMessage());
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<OAuth2AccessToken> call, Throwable t) {
                onLoginFailed("Login failed, Try again later");
                progressDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                String email = data.getExtras().getString("email");
                String password = data.getExtras().getString("password");

                doLogin(email, password);
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);

        Intent intent = new Intent(getApplicationContext(), StartActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
        finish();
    }

    public void onLoginFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 ) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}

