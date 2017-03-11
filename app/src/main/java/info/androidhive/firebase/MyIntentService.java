package info.androidhive.firebase;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    private  static final String TAG = "MYINTENTSERVICE";


    private DatabaseReference mUsers;
    private DatabaseReference mNoti_Invitation;
    private FirebaseAuth auth;

    @Override
    protected void onHandleIntent(Intent intent) {
        mUsers = FirebaseDatabase.getInstance().getReference().child("users");
        auth = FirebaseAuth.getInstance();
        final String UID = auth.getCurrentUser().getUid();
        mNoti_Invitation = mUsers.child(UID).child("notifications").child("invitations");
        mNoti_Invitation.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {//strange for loop
                    String InvitationUID = postSnapshot.getKey();
                    DataSnapshot tempSnapshot = dataSnapshot.child(InvitationUID);
                    String invitationTime = postSnapshot.child("time").getValue(String.class);
                    String invitationUsername = postSnapshot.child("username").getValue(String.class);
                    MyIntentService.this.sendNotification(invitationUsername + " has invited you at " + invitationTime);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        /*
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                handleActionFoo(param1);
            }
        }
        */
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        mUsers.child(auth.getCurrentUser().getUid()).child("notifications").setValue(null);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_noti)
                .setContentTitle("Mangeroo")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
