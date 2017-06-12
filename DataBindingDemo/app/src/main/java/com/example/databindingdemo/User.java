package com.example.databindingdemo;

import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by sjk on 17-6-12.
 *
 * 实体类（JavaBean）
 */

public class User {

    private String username;
    private String password;
    private String imageUrl;

    public User() {
    }

    public User(String username, String password, String imageUrl) {
        this.username = username;
        this.password = password;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    /**
     * 给ImageView设定url属性后，将会触发该static方法
     * 为该ImageView设置图片源
     *
     * @param imageView XML中的ImageView
     * @param url       ImageView的url属性的值
     */
    @BindingAdapter("bind:url")
    public static void makeImage(ImageView imageView, String url) {
        Picasso.with(imageView.getContext()).load(url).into(imageView);
    }

    /**
     * 定义Adapter<User>的item点击事件
     * 不过在这里定义，不能使用adapter或者activity等外部对象的资源，局限性很大
     */
    public void onItemClick(View view, int position) {
        // ...
    }

    @Override
    public String toString() {
        return "Username: " + username + ";"
                + "Password: " + password;
    }
}
