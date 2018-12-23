package com.kishannareshpal.superdialog;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

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
                    .title("Oh snap!")
                    .message("Something went terribly wrong. Wanna try again?")
                    .negativeText("No").negativeColorRes(R.color.md_grey_200)
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
                                            dialog.dismiss();
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
