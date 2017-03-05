package info.androidhive.firebase;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        Bundle extras = getIntent().getExtras();
        String name = null;
        if(extras !=null)
        {
            name = extras.getString("NAME");
        }
        final TextView tv_name = (TextView) findViewById(R.id.tv_name);
        tv_name.setText(name);
        String status = extras.getString("STATUS");
        Toast.makeText(getApplicationContext(),status, Toast.LENGTH_SHORT);

        if (status.equals("free friend")){
            Button btn_Add = (Button)findViewById(R.id.btn_add);
            btn_Add.setVisibility(View.GONE);
        }
        Button btn_Invite = (Button)findViewById(R.id.btn_invite);
        btn_Invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Invitation has been sent", Toast.LENGTH_LONG);
            }
        });

    }
}
