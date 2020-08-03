package com.osg.ex86firebasecloudmessagepushtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void clickBtn(View view) {
        //앱을 FCM서버에 등록하는 과정
        //앱을 FCM 서버에 등록하면 앱을 식별할 수 있는 고유 token값을 준다(문자열)
        //토큰값(InstanceID)을 통해서 앱들(디바이스들)을 구별하여 메세지가 전달된다(원래는 onCreate에 넣어서 앱을 실행하는 즉시 토큰이 발급되도록 해야함)

        FirebaseInstanceId firebaseInstanceId = FirebaseInstanceId.getInstance();
        Task<InstanceIdResult> task = firebaseInstanceId.getInstanceId(); //task가 별도 스레드로 동작하며 완료되면 그걸 듣는 리스너를 달자
        task.addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                String token = task.getResult().getToken();

                //토큰값 출력
                Toast.makeText(MainActivity.this, token+"", Toast.LENGTH_SHORT).show();
                //로그캣 창에 토큰값을 출력하자(나중에 php파일에 써야함)
                Log.i("TAG", token);
                //실무에서는 이 토큰값을 본인의 웹서버(dothome...)에 전송하여 웹DB에 토큰값을 저장하도록 해야한다!

            }
        });
    }
}