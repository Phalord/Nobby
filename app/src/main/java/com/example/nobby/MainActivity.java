package com.example.nobby;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin;
    private EditText etUsername;
    private EditText etPassword;
    private String ssUsername, ssPassword;
    private URL dbLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnLogIn);
        etUsername = findViewById(R.id.editText2);
        etPassword = findViewById(R.id.editText);
        ImageView ivHelp = findViewById(R.id.ivHelp);
        Button btnRegister = findViewById(R.id.btnRegister);

        ivHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tag", "Pressing Help");
                final Button[] btnResend = new Button[1];
                final EditText etResend;
                Dialog helpDialog = new Dialog(MainActivity.this);
                helpDialog.setContentView(R.layout.confirmation_pop_up);
                TextView tvBody = helpDialog.findViewById(R.id.tvBody);
                btnResend[0] = helpDialog.findViewById(R.id.btnResend);
                etResend = helpDialog.findViewById(R.id.etResend);

                tvBody.setMovementMethod(LinkMovementMethod.getInstance());

                btnResend[0].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activateResend(fillResend(etResend.getText().toString().trim()));

                    }
                });

                helpDialog.show();

            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    fillLogInUrl();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                activateLogIn();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(0);
            }
        });

        etUsername.addTextChangedListener(twLogIn);
        etPassword.addTextChangedListener(twLogIn);
    }

    private void activateResend(URL urlResend) {
        new ResendConnection().execute(urlResend);
    }

    private class ResendConnection extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            try {
                HttpsURLConnection rsndConnection = (HttpsURLConnection) urls[0].openConnection();
                BufferedReader bfReader = new BufferedReader(new InputStreamReader(rsndConnection.getInputStream()));
                String answer = bfReader.readLine();
                Log.d("tag", answer);
                bfReader.close();
                rsndConnection.disconnect();
                return  answer;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String answer) {
            super.onPostExecute(answer);
            switch (answer) {
                case "1":
                    Toast.makeText(MainActivity.this, "No se ha registrado el correo"
                            , Toast.LENGTH_SHORT).show();
                    break;
                case "2":
                    Toast.makeText(MainActivity.this, "Se ha reenviado el correo de confirmación"
                            , Toast.LENGTH_LONG).show();
                    break;
                case "3":
                    Toast.makeText(MainActivity.this, "Formato de email Inválido"
                            , Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private URL fillResend(String ssResend) {
        String resendFormat = "P/resend/"+ssResend;
        try {
            return new URL("https://nobbyapi.000webhostapp.com/"+resendFormat);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }


    private void fillLogInUrl() throws MalformedURLException {
        String liFormat = "E/login/"+ssUsername+","+ssPassword;
        dbLogIn = new URL("https://nobbyapi.000webhostapp.com/"+liFormat);
    }

    private void activateLogIn() {
        new DataBaseConnection().execute(dbLogIn);
    }

    private class DataBaseConnection extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            try {
                HttpsURLConnection nbConnection = (HttpsURLConnection) urls[0].openConnection();
                BufferedReader bfReader = new BufferedReader(new InputStreamReader(nbConnection.getInputStream()));
                String answer = bfReader.readLine();
                Log.d("tag", answer);
                bfReader.close();
                nbConnection.disconnect();
                return answer;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "0";
        }

        @Override
        protected void onPostExecute(String answer) {
            super.onPostExecute(answer);
            switch (answer) {
                case "1":
                    Toast.makeText(MainActivity.this, "El formato de correo es erróneo"
                            , Toast.LENGTH_SHORT).show();
                    break;
                case "2":
                    Toast.makeText(MainActivity.this, "Inicio de Sesión correcto"
                            , Toast.LENGTH_SHORT).show();
                    break;
                case "3":
                    Toast.makeText(MainActivity.this, "Cuenta no activada"
                            , Toast.LENGTH_SHORT).show();
                    break;
                case "4":
                    Toast.makeText(MainActivity.this, "Correo o contraseña no coinciden"
                            , Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private TextWatcher twLogIn = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            getStrings();
            enableButton();
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void getStrings() {
        ssUsername = etUsername.getText().toString().trim();
        ssPassword = etPassword.getText().toString().trim();
    }
    private void enableButton() {
        btnLogin.setEnabled(!TextUtils.isEmpty(ssUsername) && !TextUtils.isEmpty(ssPassword));
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void openActivity(int opc) {
        Intent intent;
        if(opc == 1) {
            intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        } else {
            intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}
