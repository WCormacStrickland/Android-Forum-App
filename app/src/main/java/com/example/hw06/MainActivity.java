//Cormac Strickland
//Chase Scallion
//Homework 06

package com.example.hw06;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener,
        CreateAccountFragment.RegisterListener, ForumsListFragment.ForumsListListener,
        CreateForumFragment.CreateForumListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.rootView, new LoginFragment())
                .commit();
    }


    @Override
    public void gotoForumsList() {
        //this.mUser = user;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new ForumsListFragment())
                .commit();
    }

    @Override
    public void gotoLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void gotoCreateAccount() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new CreateAccountFragment())
                .commit();
    }

    @Override
    public void gotoForumDetails(Data2.Forum forum) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, ForumFragment.newInstance(forum))
                .addToBackStack(null)
                .commit();
    }



    @Override
    public void logOut() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, new LoginFragment())
                .commit();
    }

    @Override
    public void gotoAddNewForum() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.rootView, CreateForumFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void doneCreateForum() {
        getSupportFragmentManager().popBackStack();
    }
}