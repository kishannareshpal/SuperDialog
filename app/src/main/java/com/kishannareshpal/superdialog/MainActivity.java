package com.kishannareshpal.superdialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SuperDialog s;
    String m = "m";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init components
        Button btn = findViewById(R.id.button);

        // Setup components
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // And From your main() method or any other method
                s = new SuperDialog();
                s.iconMode(IconMode.ERROR)
                        .checkable(true, "Também quero apagar os exames.")
                        .title("Oh snap!")
                        .message("Something went terribly wrong. Wanna try again?")
                        .negativeText("No")
                        .positiveText("Yes").positiveColorRes(R.color.colorPrimary)
                        .onPositive(new SuperDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(SuperDialog dialog, int whichButton) {
                                dialog.iconMode(IconMode.INDEFINITE_PROGRESS)
                                        .title("Loading the Universe")
                                        .positiveText(null)
                                        .negativeText(null)
                                        .cancelText("Cancel!").onCancel(new SuperDialog.OnButtonClickListener() {
                                            @Override
                                            public void OnButtonClick(SuperDialog dialog, int whichButton) {
                                                dialog.checkable(true, "wgat?");
                                                Toast.makeText(MainActivity.this, String.valueOf(dialog.isChecked()), Toast.LENGTH_SHORT).show();
                                                // dialog.dismiss();
                                            }
                                        })
                                        .message("This will only take aproximately about 150 billion years. Please hold on.");

                            }
                    }).cancelable(false);

                s.show(getSupportFragmentManager());
            }
        });
    }
}
