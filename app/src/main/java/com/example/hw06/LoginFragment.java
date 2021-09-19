package com.example.hw06;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
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

public class LoginFragment extends Fragment {
    public LoginFragment() {
        // Required empty public constructor
    }
    LoginListener mListener;
    private FirebaseAuth mAuth;
    final private String TAG = "LoginTag";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText editTextEmailAddress, editTextPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editTextEmailAddress = view.findViewById(R.id.editTextForumDesc);
        editTextPassword = view.findViewById(R.id.editTextPassword);

        editTextEmailAddress.setText("c@c.com");
        editTextPassword.setText("test123");
        getActivity().setTitle("Login");

        view.findViewById(R.id.buttonLoginSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmailAddress.getText().toString();
                String password = editTextPassword.getText().toString();

                if(email.isEmpty()){
                    Toast.makeText(getActivity(), "Email is required", Toast.LENGTH_SHORT).show();
                } else if(password.isEmpty()){
                    Toast.makeText(getActivity(), "Password is required", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth = FirebaseAuth.getInstance();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Success");
                                        mListener.gotoForumsList();
                                    } else {
                                        Log.d(TAG, "Failure");
                                    }
                                }
                            });
                }
            }
        });

        view.findViewById(R.id.buttonCreateAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoCreateAccount();
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (LoginListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement LoginListener");
        }
    }

    interface LoginListener{
        void gotoForumsList();
        void gotoCreateAccount();
    }
}