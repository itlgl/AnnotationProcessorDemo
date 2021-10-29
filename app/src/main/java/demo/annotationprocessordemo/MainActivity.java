package demo.annotationprocessordemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    private LinearLayoutCompat layoutContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layoutContainer = findViewById(R.id.layout_container);

        AppCompatButton button;

        button = new AppCompatButton(this);
        button.setText("test getName");
        button.setAllCaps(false);
        button.setOnClickListener(v -> {
            // 避免ide报错：编写ide插件、反射
            User user = new User();
            String name = user.getName();
            System.out.println("name=" + name);
        });
        layoutContainer.addView(button);
    }
}