package com.artimanton.lavabird;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.artimanton.lavabird.adapter.NotifAdapter;
import com.artimanton.lavabird.fragments.EmptyFragment;
import com.artimanton.lavabird.fragments.ItemFragment;
import com.artimanton.lavabird.model.NotifDao;
import com.artimanton.lavabird.model.NotifEntity;
import com.artimanton.lavabird.services.ForegroundService;
import com.artimanton.lavabird.services.NotificationListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    final static String TAG_1 = "FRAGMENT_1";
    final static String TAG_2 = "FRAGMENT_2";

    @BindView(R.id.container) FrameLayout container;
    FragmentManager myFragmentManager;
    FragmentTransaction fragmentTransaction;
    EmptyFragment emptyFragment;
    ItemFragment itemFragment;

    private ReceiveBroadcastReceiver imageChangeBroadcastReceiver;
    private AlertDialog enableNotificationListenerAlertDialog;

    private NotifDao notifDao;
    private AppDatabase db;
    private List<NotifEntity> notifs;

    @BindView(R.id.button) Button button;
    //@BindView(R.id.textView) TextView tvMsg;
    //@BindView(R.id.imageView) ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        emptyFragment = new EmptyFragment();
        itemFragment = new ItemFragment();
        myFragmentManager = getSupportFragmentManager();

        db = App.getInstance().getDatabase();
        notifDao = db.notifDao();
        notifs = notifDao.getAll();
        if (notifs.size() == 0 || notifs == null) {
            fragmentTransaction = myFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, emptyFragment);
            fragmentTransaction.commit();
        }else{
            fragmentTransaction = myFragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, itemFragment);
            fragmentTransaction.commit();
        }


        if(!isNotificationServiceEnabled()){
            enableNotificationListenerAlertDialog = buildNotificationServiceAlertDialog();
            enableNotificationListenerAlertDialog.show();
        }

        // Finally we register a receiver to tell the MainActivity when a notification has been received



    }
    public void buttonClick(View view) {
        if (isMyServiceRunning(ForegroundService.class)){
            stopService();
            button.setBackgroundResource(R.drawable.btn_style_start);
            button.setText("Start");

        }else{
            startService();
            button.setBackgroundResource(R.drawable.btn_style_stop);
            button.setText("Stop");

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        NotifAdapter notifAdapter;
        switch(id){
            case R.id.menu_all :
                notifDao.getAll();
                notifAdapter = new NotifAdapter(notifs);
                notifAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_hour:
                notifDao.getById(1);
                notifAdapter = new NotifAdapter(notifs);
                notifAdapter.notifyDataSetChanged();
                return true;
            case R.id.menu_day:
                return true;
            case R.id.menu_mouth:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public void startService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
        ContextCompat.startForegroundService(this, serviceIntent);
        imageChangeBroadcastReceiver = new ReceiveBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.artimanton.lavabird");
        registerReceiver(imageChangeBroadcastReceiver,intentFilter);

    }
    public void stopService() {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
        /*imageChangeBroadcastReceiver = new ReceiveBroadcastReceiver();
        unregisterReceiver(imageChangeBroadcastReceiver);*/
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private static boolean isNotificationListenerServiceEnabled(Context context) {
        Set<String> packageNames = NotificationManagerCompat.getEnabledListenerPackages(context);
        if (packageNames.contains("com.artimanton.lavabird")) {
            return true;
        }
        return false;
    }

    /**
     * Is Notification Service Enabled.
     */
    private boolean isNotificationServiceEnabled(){
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Receive Broadcast Receiver.
     * */
    public class ReceiveBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*notifs = notifDao.getAll();
            NotifAdapter notifAdapter = new NotifAdapter(notifs);
            notifAdapter.notifyDataSetChanged();
            fragmentTransaction = myFragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, itemFragment);
            fragmentTransaction.commit();*/


           /* List<NotifEntity> notifEntities = notifDao.getAll();
            String info = "";

            for(NotifEntity notifEntity: notifEntities){
                long id = notifEntity.id;
                String packages = notifEntity.packages;
                String title = notifEntity.title;
                String text = notifEntity.text;
                info = info + "\n\n"+id+"\n Title: "+title+"\n Text: "+text;
            }

            tvMsg.setText(info);*/

            //int receivedNotificationCode = intent.getIntExtra("Notification Code",-1);
            String packages = intent.getStringExtra("package");
            String title = intent.getStringExtra("title");
            String text = intent.getStringExtra("text");
            byte[] byteArray = intent.getByteArrayExtra("icon");
            if(byteArray!= null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            //image.setImageBitmap(bitmap);
            }

            if(text != null) {

                    String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    String devicemodel = android.os.Build.MANUFACTURER+android.os.Build.MODEL+android.os.Build.BRAND+android.os.Build.SERIAL;

                    DateFormat df = new SimpleDateFormat("ddMMyyyyHHmmssSSS");
                    String date = df.format(Calendar.getInstance().getTime());

                    //tvMsg.setText("Notification : "  + "\nPackages : " + packages + "\nTitle : " + title + "\nText : " + text + "\nId : " + date+ "\nandroid_id : " + android_id+ "\ndevicemodel : " + devicemodel);

                }
            }
        }



    /**
     * Build Notification Listener Alert Dialog.
     */
    private AlertDialog buildNotificationServiceAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(R.string.notification_listener_service);
        alertDialogBuilder.setMessage(R.string.notification_listener_service_explanation);
        alertDialogBuilder.setPositiveButton(R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
                    }
                });
        alertDialogBuilder.setNegativeButton(R.string.no,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // If you choose to not enable the notification listener
                        // the app. will not work as expected
                    }
                });
        return(alertDialogBuilder.create());
    }
}
