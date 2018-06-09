package com.example.baeminsu.nodechat.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.example.baeminsu.nodechat.NetworkHelper.NetworkFacade;
import com.example.baeminsu.nodechat.R;
import com.example.baeminsu.nodechat.Util.PropertyManager;

import org.json.JSONObject;

/**
 * Created by baeminsu on 2018. 5. 29..
 */

public class CreateChatDialog extends Dialog {

    private EditText inputNickName;
    private Button submit;
    private Button cancel;
    private String nickname;
    private Context context;

    public CreateChatDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.dialog_create_chat);
        setWidget();
    }


    private void setWidget() {
        inputNickName = findViewById(R.id.cd_add_nickname_et);
        submit = findViewById(R.id.cd_add_category_submit_bt);
        cancel = findViewById(R.id.cd_add_category_cancel_bt);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickname = inputNickName.getText().toString();
                JSONObject data = new JSONObject();
                try {
                    data.put("nickname", nickname);
                    data.put("requestNickname", PropertyManager.getInstance().getNickname());
                    Log.e("체크", data.toString());

                    NetworkFacade.getInstace().requestCreateChatRoom(data);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                dismiss();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }


}
