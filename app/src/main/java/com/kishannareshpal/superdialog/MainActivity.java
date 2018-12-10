package com.kishannareshpal.superdialog;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import static com.kishannareshpal.superdialog.AnimatedIcon.Mode.ERROR;
import static com.kishannareshpal.superdialog.AnimatedIcon.Mode.SUCCESS;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btn = findViewById(R.id.button);
        final AnimatedIcon asi = findViewById(R.id.asi);
        final AnimatedIcon asi2 = findViewById(R.id.asi2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asi.setMode(SUCCESS);
                asi2.setMode(ERROR);
            }
        });
    }
}
