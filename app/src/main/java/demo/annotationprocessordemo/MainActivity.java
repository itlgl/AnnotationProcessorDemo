package demo.annotationprocessordemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;

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

            // 删除map后编译找不到map属性
            // Map<String, String> map = user.map;
        });
        layoutContainer.addView(button);
    }
}