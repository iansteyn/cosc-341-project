package com.example.cosc341_project.ui.post;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.cosc341_project.R;
import com.example.cosc341_project.databinding.FragmentPostBinding;

public class PostFragment extends Fragment {

    FragmentPostBinding binding;
    Button button1;
    Button button2;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Initialize ViewModel
        PostViewModel postViewModel =
                new ViewModelProvider(this).get(PostViewModel.class);

        // Inflate the layout using View Binding
        binding = FragmentPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        button1 = root.findViewById(R.id.button);
        button2 = root.findViewById(R.id.button2);


        button1.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), createPost.class);
            startActivity(intent);
        });

        button2.setOnClickListener(view -> {


        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
