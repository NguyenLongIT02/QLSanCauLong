package com.example.badmintoncourtmanager.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.badmintoncourtmanager.R;
import com.example.badmintoncourtmanager.database.DatabaseHelper;
import com.example.badmintoncourtmanager.model.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText editUsername, editPassword, editConfirmPassword;
    private Button buttonRegister;
    private ImageButton buttonBack;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseHelper = new DatabaseHelper(this);

        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        editConfirmPassword = findViewById(R.id.edit_confirm_password);
        buttonRegister = findViewById(R.id.button_register);
        buttonBack = findViewById(R.id.button_back);

        buttonBack.setOnClickListener(v -> finish());
        buttonRegister.setOnClickListener(v -> register());
    }

    private void register() {
        String username = editUsername.getText().toString().trim();
        String password = editPassword.getText().toString().trim();
        String confirmPassword = editConfirmPassword.getText().toString().trim();

        if (username.isEmpty()) {
            editUsername.setError("Vui lòng nhập tên đăng nhập");
            editUsername.requestFocus();
            return;
        }

        if (username.length() < 4) {
            editUsername.setError("Tên đăng nhập phải có ít nhất 4 ký tự");
            editUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editPassword.setError("Vui lòng nhập mật khẩu");
            editPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editPassword.setError("Mật khẩu phải có ít nhất 6 ký tự");
            editPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            editConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            editConfirmPassword.requestFocus();
            return;
        }

        if (databaseHelper.isUsernameExists(username)) {
            editUsername.setError("Tên đăng nhập đã tồn tại");
            editUsername.requestFocus();
            return;
        }

        User user = new User(username, password, "", "", "");
        long result = databaseHelper.addUser(user);

        if (result > 0) {
            Toast.makeText(this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Đăng ký thất bại. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
        }
    }
}
