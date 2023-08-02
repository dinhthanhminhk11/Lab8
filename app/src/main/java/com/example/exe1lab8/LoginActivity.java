package com.example.exe1lab8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.exe1lab8.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private Animation star, cloudAni;
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        star = AnimationUtils.loadAnimation(this, R.anim.amistart);
        cloudAni = AnimationUtils.loadAnimation(this, R.anim.amicloud);
        binding.start.startAnimation(star);
        binding.cloud.startAnimation(cloudAni);

        binding.email.setText("dinhthanhminhk11@gmail.com");
        binding.password.setText("m01675784487");

        binding.login.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            login(binding.email.getText().toString(), binding.password.getText().toString());
        });

        binding.register.setOnClickListener(v->{
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    public void login(String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}