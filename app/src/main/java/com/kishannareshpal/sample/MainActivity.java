package com.kishannareshpal.sample;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.kishannareshpal.superdialog.IconMode;
import com.kishannareshpal.superdialog.SuperDialog;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init components
        Button btn = findViewById(R.id.button);

        // Setup components
        SuperDialog sd = new SuperDialog()
                .iconMode(IconMode.ERROR)
                .positiveText("change icon")
                .prompt()
                .onPositive((superDialog, whichButton) -> {
                    superDialog.iconMode(IconMode.SUCCESS);
                })
                .cancelable(true);

        btn.setOnClickListener(v -> {
            sd.show(getSupportFragmentManager());
            sd.prompt(true, null, "Kishan", 1);
        });
//         never change the properties immediately.
    }
}
