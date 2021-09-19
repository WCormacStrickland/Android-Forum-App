package com.example.hw06;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class CreateAccountFragment extends Fragment {
    public CreateAccountFragment() {
        // Required empty public constructor
    }

    RegisterListener mListener;
    private FirebaseAuth mAuth;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText editTextEmailAddress, editTextPassword, editTextName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_account, container, false);

        getActivity().setTitle("Create Account");

        editTextEmailAddress = view.findViewById(R.id.editTextForumDesc);
        editTextPassword = view.findViewById(R.id.editTextPassword);
        editTextName = view.findViewById(R.id.editTextForumTitle);

        view.findViewById(R.id.buttonCreateAccountSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString();
                String email = editTextEmailAddress.getText().toString();
                String password = editTextPassword.getText().toString();

                if (name.isEmpty()) {
                    Toast.makeText(getActivity(), "Name is required", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(getActivity(), "Email is required", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(getActivity(), "Password is required", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mListener.gotoForumsList();
                                    } else {
                                        Toast.makeText(getActivity(), "Login Failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    FirebaseUser user = mAuth.getCurrentUser();
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();
                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                    }
                                }
                            });
                }
            }
        });

        view.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoLogin();
            }
        });

        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (RegisterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RegisterListener");
        }
    }

    interface RegisterListener {
        void gotoForumsList();

        void gotoLogin();
    }
}