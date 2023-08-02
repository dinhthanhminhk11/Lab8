package com.example.exe1lab8;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exe1lab8.databinding.ItemTodoBinding;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<TodoItem> data;
    private TodoItemClickListener itemClickListener;

    public TodoAdapter(List<TodoItem> todoList, TodoItemClickListener itemClickListener) {
        this.data = todoList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TodoViewHolder(ItemTodoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        TodoItem todoItem = data.get(position);
        if (todoItem != null) {
            holder.binding.txtTitle.setText(todoItem.getTitle());
            holder.binding.txtDescription.setText(todoItem.getDescription());
            holder.binding.checkBox.setChecked(todoItem.isCompleted());

            holder.binding.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (itemClickListener != null) {
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onTodoCompletedClick(position, isChecked);
                    }
                }
            });

            holder.binding.btnDelete.setOnClickListener(v -> {
                if (itemClickListener != null) {
                    if (position != RecyclerView.NO_POSITION) {
                        itemClickListener.onTodoDeleteClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public class TodoViewHolder extends RecyclerView.ViewHolder {
        private ItemTodoBinding binding;

        public TodoViewHolder(@NonNull ItemTodoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public interface TodoItemClickListener {
        void onTodoCompletedClick(int position, boolean isChecked);

        void onTodoDeleteClick(int position);
    }
}