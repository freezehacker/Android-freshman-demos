package com.me.chatting.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.me.chatting.R;
import com.me.chatting.constant.ServerConfig;
import com.me.chatting.constant.UserInfo;
import com.me.chatting.util.LogUtil;

import org.jivesoftware.smack.AbstractConnectionListener;
import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by sjk on 17-6-10.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.login)
    public Button login;
    @BindView(R.id.account)
    public TextInputEditText account;
    @BindView(R.id.account_wrapper)
    public TextInputLayout accountWrapper;
    @BindView(R.id.password)
    public TextInputEditText password;
    @BindView(R.id.password_wrapper)
    public TextInputLayout passwordWrapper;
    @BindView(R.id.login_progress_bar)
    public ProgressBar progressBar;

    public static AbstractXMPPConnection conn = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        account.setText("kobe");
        password.setText("kobe1947");
    }

    @OnClick(R.id.login)
    public void attemptLogin() {
        if (TextUtils.isEmpty(account.getText().toString())
                || TextUtils.isEmpty(password.getText().toString())) {
            Snackbar snackbar = Snackbar.make(account, "请输入账号与密码", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }

        new ConnectLoginTask(account.getText().toString(), password.getText().toString()).execute();
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    /*登录是网络请求，所以要在子线程中进行*/
    private class ConnectLoginTask extends AsyncTask<Void, Void, Boolean> {
        private String accountStr;
        private String passwordStr;

        public ConnectLoginTask(String accountStr, String passwordStr) {
            this.accountStr = accountStr;
            this.passwordStr = passwordStr;
        }

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                XMPPTCPConnectionConfiguration config = XMPPTCPConnectionConfiguration.builder()
                        .setHost(ServerConfig.IP)
                        .setPort(ServerConfig.PORT)
                        .setXmppDomain(ServerConfig.DOMAIN_NAME)
                        .setUsernameAndPassword(accountStr, passwordStr)
                        //.setConnectTimeout(5000)
                        .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                        .build();
                conn = new XMPPTCPConnection(config);
                conn.addConnectionListener(new AbstractConnectionListener() {   // 使用更灵活的AbstractConnectionListener类，而不是ConnectionListener接口
                    @Override
                    public void connected(XMPPConnection connection) {
                        LogUtil.log("connected! Thread id: " + Thread.currentThread().getId());
                    }

                    @Override
                    public void authenticated(XMPPConnection connection, boolean resumed) {
                        LogUtil.log("authenticated! Thread id: " + Thread.currentThread().getId());
                    }

                    @Override
                    public void connectionClosed() {
                        LogUtil.log("connectionClosed");
                    }
                });
                conn.connect();
                conn.login();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean ok) {
            progressBar.setVisibility(View.GONE);

            if (ok) {
                LogUtil.log("onPostExecute: 登录成功");
                UserInfo.userAccount = this.accountStr;
                startActivity(new Intent(LoginActivity.this, ChatDetailActivity.class));
                finish();
            } else {
                Snackbar snackbar = Snackbar.make(LoginActivity.this.account, "登录失败", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        }
    }
}
