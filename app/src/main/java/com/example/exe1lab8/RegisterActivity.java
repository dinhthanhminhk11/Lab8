package com.example.exe1lab8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.exe1lab8.databinding.ActivityRegisterBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private Animation star, cloudAni;
    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        star = AnimationUtils.loadAnimation(this, R.anim.amistart);
        cloudAni = AnimationUtils.loadAnimation(this, R.anim.amicloud);
        binding.start.startAnimation(star);
        binding.cloud.startAnimation(cloudAni);

        binding.register.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            register(binding.email.getText().toString(), binding.password.getText().toString());
        });

        binding.login.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    public void register(String email, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(task -> {
                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
                    if (isNewUser) {
                        mAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this, registerTask -> {
                                    if (registerTask.isSuccessful()) {
                                        binding.progressBar.setVisibility(View.GONE);
                                        Toast.makeText(this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        binding.progressBar.setVisibility(View.GONE);
                                        Toast.makeText(this, "Đăng kí thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(this, "Email đã được đăng kí", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}