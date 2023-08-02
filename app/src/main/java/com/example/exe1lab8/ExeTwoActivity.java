package com.example.exe1lab8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.example.exe1lab8.databinding.ActivityExeTwoBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ExeTwoActivity extends AppCompatActivity {

    private ActivityExeTwoBinding binding;
    private Animation star, cloudAni;

    private TodoAdapter todoAdapter;
    private List<TodoItem> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExeTwoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        star = AnimationUtils.loadAnimation(this, R.anim.amistart);
        cloudAni = AnimationUtils.loadAnimation(this, R.anim.amicloud);
        binding.start.startAnimation(star);
        binding.cloud.startAnimation(cloudAni);

        binding.listTodo.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        todoList = new ArrayList<>();
        todoAdapter = new TodoAdapter(todoList, new TodoAdapter.TodoItemClickListener() {
            @Override
            public void onTodoCompletedClick(int position, boolean isChecked) {
                TodoItem todoItem = todoList.get(position);
                todoItem.setCompleted(isChecked);
                updateTodoItem(todoItem);
            }

            @Override
            public void onTodoDeleteClick(int position) {
                TodoItem todoItem = todoList.get(position);
                deleteTodoItem(todoItem);
            }
        });
        binding.listTodo.setAdapter(todoAdapter);

        getTodoList();

        binding.add.setOnClickListener(v -> {
            showAddTodoDialog();
        });
    }

    private void getTodoList() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference todoRef = databaseRef.child("todos").child(currentUser.getUid());
            todoRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    todoList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TodoItem todoItem = snapshot.getValue(TodoItem.class);
                        if (todoItem != null) {
                            todoList.add(todoItem);
                        }
                    }
                    todoAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void showAddTodoDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Todo");
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_add_todo, null);
        builder.setView(dialogView);
        final EditText editTextTitle = dialogView.findViewById(R.id.editTextTitle);
        final EditText editTextDescription = dialogView.findViewById(R.id.editTextDescription);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String title = editTextTitle.getText().toString();
                String description = editTextDescription.getText().toString();
                addTodoItem(title, description);
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

    private void addTodoItem(String title, String description) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference todoRef = databaseRef.child("todos").child(currentUser.getUid()).push();

            TodoItem todoItem = new TodoItem(todoRef.getKey(), title, description, false);
            todoRef.setValue(todoItem)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void updateTodoItem(TodoItem todoItem) {
        if (todoItem == null || todoItem.getId() == null) {
            return;
        }
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference todoRef = databaseRef.child("todos").child(currentUser.getUid()).child(todoItem.getId());

            todoRef.child("completed").setValue(todoItem.isCompleted())
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void deleteTodoItem(TodoItem todoItem) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference todoRef = databaseRef.child("todos").child(currentUser.getUid()).child(todoItem.getId());

            todoRef.removeValue()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}