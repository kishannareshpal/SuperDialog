package com.kishannareshpal.sample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.kishannareshpal.superdialog.SuperDialog;

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

                new SuperDialog()
                        .title("Outra disciplina")
                        .prompt(true, "disciplina...", null, 1)
                        .positiveText("Confirmar")
                        .cancelable(false)
                        .onPositive(new SuperDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(SuperDialog sd, int whichButton) {
                                String pt = sd.getPromptText();
                                sd.setPromptToFail("Hello");
                            }
                        })
                        .negativeText("hello")
                        .onNegative(new SuperDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(SuperDialog superDialog, int whichButton) {
                                superDialog.resetPrompt(true);
                            }
                        })
                        .onPromptTextChanged(new SuperDialog.OnTextInputListener() {
                            @Override
                            public void OnTextInput(SuperDialog superDialog, String text) {
                                if (text.equals("Kishan")) {
                                    superDialog.setPromptToHelp("Yeahh man!!");

                                } else {
                                    superDialog.resetPrompt(true);
                                }
                            }
                        })
                        .cancelText("Hello hello")
                        .onCancel(new SuperDialog.OnButtonClickListener() {
                            @Override
                            public void OnButtonClick(SuperDialog superDialog, int whichButton) {
                                superDialog.setPromptToHelp("Help me make this");
                            }
                        })
                        .show(getSupportFragmentManager());

//                s = new SuperDialog();
//                s.iconMode(IconMode.ERROR)
//                        .checkable(true, false)
//                        .checkboxText("Também quero apagar os meus exames")
//                        .title("Oh snap!")
//                        .message("Something went terribly wrong. Wanna try again?")
//                        .negativeText("No")
//                        .positiveText("Yes").positiveColorRes(R.color.colorPrimary)
//                        .onPositive(new SuperDialog.OnButtonClickListener() {
//                            @Override
//                            public void OnButtonClick(SuperDialog dialog, int whichButton) {
//                                dialog.iconMode(IconMode.INDEFINITE_PROGRESS)
//                                        .title("Loading the Universe")
//                                        .positiveText(null)
//                                        .negativeText(null)
//                                        .cancelText("Cancel!").onCancel(new SuperDialog.OnButtonClickListener() {
//                                            @Override
//                                            public void OnButtonClick(SuperDialog dialog, int whichButton) {
//                                                dialog.checkable(true, false);
//                                                Toast.makeText(MainActivity.this, String.valueOf(dialog.isChecked()), Toast.LENGTH_SHORT).show();
//                                                // dialog.dismiss();
//                                            }
//                                        })
//                                        .message("This will only take aproximately about 150 billion years. Please hold on.");
//
//                            }
//                    }).cancelable(false);
//                s.show(getSupportFragmentManager());
            }
        });
    }
}
