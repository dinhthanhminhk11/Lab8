package com.example.exe1lab8;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exe1lab8.databinding.ActivityExeOneBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ExeOneActivity extends AppCompatActivity {

    private ActivityExeOneBinding binding;
    private Animation star, cloudAni;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExeOneBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        star = AnimationUtils.loadAnimation(this, R.anim.amistart);
        cloudAni = AnimationUtils.loadAnimation(this, R.anim.amicloud);
        binding.start.startAnimation(star);
        binding.cloud.startAnimation(cloudAni);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        binding.email.setText(user.getEmail());

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                signOut();
            }
        });

        binding.changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeEmailDialog();
            }
        });

        binding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePassDialog();
            }
        });

        binding.forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.progressBar.setVisibility(View.VISIBLE);
                resetPassword(user.getEmail());
            }
        });
    }

    private void signOut() {
        binding.progressBar.setVisibility(View.GONE);
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(ExeOneActivity.this, LoginActivity.class));
    }

    private void resetPassword(String email) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Đã gửi đường link mật kẩu đến mail của bạn", Toast.LENGTH_LONG).show();
            } else {
                binding.progressBar.setVisibility(View.GONE);
                Toast.makeText(this, "Gửi lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void changePassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Đổi mật khẩu thất bại", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void changeEmail(String newEmail) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            user.updateEmail(newEmail).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Đổi email thành công", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Đổi email thất bại", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void showChangeEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Email");
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_change_email, null);
        builder.setView(dialogView);
        final EditText editTextNewEmail = dialogView.findViewById(R.id.editTextNewEmail);
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newEmail = editTextNewEmail.getText().toString();
                changeEmail(newEmail);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showChangePassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Password");
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_change_email, null);
        builder.setView(dialogView);
        final EditText editTextNewEmail = dialogView.findViewById(R.id.editTextNewEmail);
        editTextNewEmail.setHint("new pass");
        builder.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newEmail = editTextNewEmail.getText().toString();
                changePassword(newEmail);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}