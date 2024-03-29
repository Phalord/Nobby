package com.example.nobby;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    EditText etName, etEmail, etPassword, etPassword2;
    String ssName, ssEmail, ssPassword, ssPassword2;
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword1);
        etPassword2 = findViewById(R.id.etPassword2);
        Toolbar toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Register");
        toolbar.setBackgroundColor(Color.parseColor("#00B9AE"));
        setSupportActionBar(toolbar);

        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity();
            }
        });

        etName.addTextChangedListener(twRegister);
        etEmail.addTextChangedListener(twRegister);
        etPassword.addTextChangedListener(twRegister);
        etPassword2.addTextChangedListener(twRegister);
    }

    private TextWatcher twRegister = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            fillStrings();
            if(validatePasswords()) {
                btnNext.setEnabled(!TextUtils.isEmpty(ssName) && !TextUtils.isEmpty(ssEmail)
                        &&!TextUtils.isEmpty(ssPassword) && !TextUtils.isEmpty(ssPassword2));
            } else {
                btnNext.setEnabled(false);
                Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void fillStrings() {
        ssName = etName.getText().toString().trim();
        ssEmail = etEmail.getText().toString().trim();
        ssPassword = etPassword.getText().toString().trim();
        ssPassword2 = etPassword2.getText().toString().trim();
    }


    private boolean validatePasswords() {
        return ssPassword.equals(ssPassword2);
    }

    private void openActivity() {
        Intent intent = new Intent(this, Register2Activity.class);
        intent.putExtra("Username", ssName);
        intent.putExtra("EMail", ssEmail);
        intent.putExtra("Password1", ssPassword);
        intent.putExtra("Password2", ssPassword2);
        startActivity(intent);
    }
}
