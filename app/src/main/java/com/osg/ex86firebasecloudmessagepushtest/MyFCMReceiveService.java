package com.osg.ex86firebasecloudmessagepushtest;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFCMReceiveService extends FirebaseMessagingService {

    //푸시 서버에서 보낸 메세지가 수신되었을 때 자동으로 발동하는 메소드
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //이 안에서는 알림(Notification)만 적용할 수 있음! (모든 PUSH 메세지는 꼭! 무조건! Notification으로만 받을 수 있음)
        //우선, 리시버 확인용으로 Logcat에 출력을 해보자
        Log.i("TAG", "onMessageReceived");

        //메소드에 파라미터로 전달된 remoteMessage객체 : 받은 원격 메세지

        //메세지를 보낸 기기명(파이어베이스 서버에 자동으로 저장됨)
        String fromWho = remoteMessage.getFrom();
        //알림 데이터(파이어베이스에서 정한 것)
        String notiTitle = "title"; //제목이 안왔을 때 기본값
        String notiBody = "body text"; //글씨가 안왔을 때 기본값

        //파이어베이스 push 메세지에 추가로 데이터가 있을 경우 메세지는 key:value 형태로 송신된다
        Map<String, String> data  = remoteMessage.getData();

        String name = null;
        String msg = null;
        if(data!=null){
            name = data.get("name"); //get 안의 값은 파이어베이스에서 쓴 맞춤데이터 key값임
            msg = data.get("msg");//
        }

        Log.i("TAG", fromWho+" : "+ notiTitle + ":" + notiBody+ ","+name+","+msg); //이걸 MessageActivity에 보여줄 것임

        if(remoteMessage.getNotification()!=null){
            notiTitle = remoteMessage.getNotification().getTitle();
            notiBody = remoteMessage.getNotification().getBody();
//            Uri notiImgUri = remoteMessage.getNotification().getImageUrl(); //이건 유료!
        }

        //받은 값들을 알림객체를 이용, 공지하기
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("ch01", "Channel for FCM", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            builder = new NotificationCompat.Builder(this, "ch01");

        }else {
            builder = new NotificationCompat.Builder(this, null);
        }

        builder.setSmallIcon(R.drawable.ic_stat_name).setContentTitle(notiTitle).setContentText(notiBody);

        //알림을 선택했을 때 실행될 액티비티
        Intent intent = new Intent(this, MessageActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("message", msg);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //현재 메세지가 있다면 기존 것은 없애라

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, intent, PendingIntent.FLAG_UPDATE_CURRENT );
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);


        Notification notification = builder.build();
        notificationManager.notify(10, notification); //숫자는 알림 식별자, 나중에 삭제할 경우 필요함.


    }
}
