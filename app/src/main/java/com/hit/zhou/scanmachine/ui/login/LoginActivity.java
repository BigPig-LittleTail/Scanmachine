package com.hit.zhou.scanmachine.ui.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hit.zhou.scanmachine.R;
import com.hit.zhou.scanmachine.common.IPresenter;
import com.hit.zhou.scanmachine.common.MyApplication;
import com.hit.zhou.scanmachine.ui.main.MainActivity;

public class LoginActivity extends AppCompatActivity implements LoginIView {
    private Button button;
    private LoginPresenterImp loginPresenterImp;
    private EditText myPassword;
    private EditText myPhone;
    private TextView newUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginPresenterImp = new LoginPresenterImp(this,this);
        loginPresenterImp.onCreate(savedInstanceState);
        button = findViewById(R.id.loginButton);
        myPhone = findViewById(R.id.loginUsername);
        myPassword =  findViewById(R.id.loginPassword);
        newUser = findViewById(R.id.newUser);
        button.setEnabled(false);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = myPhone.getText().toString();
                loginPresenterImp.checkUserAndPassword(phone,myPassword.getText().toString());
                //:TODO 要改成存起来的用户手机号
                MyApplication.phone = phone;
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();
        loginPresenterImp.onStart();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void setPresenter(IPresenter presenter) {

    }

    @Override
    public IPresenter getPresenter() {
        return null;
    }

    @Override
    public void bindServiceCallback(boolean isBind) {
        button.setEnabled(isBind);
    }

    @Override
    public void loginError(int errorType) {
        switch (errorType){
            case LoginPresenterImp.ERROR_NET_ERROR:
                Toast.makeText(this,"您的网络可能存在问题",Toast.LENGTH_SHORT).show();
                break;
            case LoginPresenterImp.ERROR_PHONE_FORM_OR_PASSWORD:
                Toast.makeText(this,"确保您的用户名格式正确且密码不为空",Toast.LENGTH_SHORT).show();
                break;
            case LoginPresenterImp.ERROR_USER_OR_PASSWORD_ERROR:
                Toast.makeText(this,"用户名或密码不正确",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void loginSuccess() {
        Toast.makeText(this,"登录成功",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        //startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this, findViewById(R.id.r), "sharedLogin").toBundle());
        finish();
    }

    @Override
    public void onStop(){
        super.onStop();
        loginPresenterImp.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        loginPresenterImp.onDestroy();
    }

}
