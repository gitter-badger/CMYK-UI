package com.cmykui.framework.cmykui;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cmykui.framework.cmykui.base.OnClick;
import com.cmykui.framework.cmykui.component.ButtonComponent;
import com.cmykui.framework.cmykui.component.FloatLabelText;

public class JerkoPlayground extends AppCompatActivity {

    ButtonComponent button;
    FloatLabelText floatLabel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jerko_playground);

        button = findViewById(R.id.button);
        floatLabel=findViewById(R.id.floatLabel);


        button.setButtonText("Gumb");
        button.setButtonSuccessText("Uspijeh");
        button.setButtonErrorText("Pogreška");
        floatLabel.setHintAndLabel("pokusaj");


        button.TextLoading =false;






        button.isButtonLoader = true;
        button.setOnClickListener(new OnClick<Void>() {
            @Override
            public Void onClick() throws Exception {
                mojaMetoda(JerkoPlayground.this);
                return null;
            }
        });


    }



    public Void mojaMetoda(Context ctx){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.setView(R.layout.dialog_component).create();

     dialog.show();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogTheme; //style id


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {




                button.setActionError();

            }
        }, 5000);
        return null;
    }
}
