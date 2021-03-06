package pl.jwojcik.gascompanion.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import pl.jwojcik.gascompanion.Constants;
import pl.jwojcik.gascompanion.R;
import pl.jwojcik.gascompanion.activities.LoginActivity;
import pl.jwojcik.gascompanion.activities.ResetPasswordActivity;
import pl.jwojcik.gascompanion.models.CurrentUserService;
import pl.jwojcik.gascompanion.models.User;


public class ProfileFragment extends Fragment implements View.OnClickListener {

    private View view;
    private TextView tvName;
    private Button btnResetPassword;
    private Button btnLogout;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    public View onCreateView(final LayoutInflater inflater, @NonNull ViewGroup container, @NonNull Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                }
            }
        };

        initView();

        return view;
    }

    private void initView() {

        tvName = view.findViewById(R.id.tv_name);
        btnResetPassword = view.findViewById(R.id.btn_reset_password);
        btnLogout = view.findViewById(R.id.btn_logout);

        btnResetPassword.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        User user = CurrentUserService.getLoggedUser();
        try {
            if (!TextUtils.isEmpty(user.getName())) {
                tvName.setText(user.getName());
            } else {
                btnResetPassword.setVisibility(View.VISIBLE);
            }
        } catch (NullPointerException e) {

        }



    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset_password:
                resetPassword();
                break;
            case R.id.btn_logout:
                showConfirmDialog();
                break;
        }
    }

    private void resetPassword() {
        Intent intent = new Intent(getContext(), ResetPasswordActivity.class);
        startActivity(intent);
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.btn_log_out);
        builder.setMessage(R.string.msg_log_out);
        builder.setCancelable(true);
        builder.setPositiveButton(R.string.ok_string, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                logout();
            }
        });
        builder.setNegativeButton(R.string.cancel_string, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void logout() {
        auth.signOut();
        CurrentUserService.logout();
    }

}
