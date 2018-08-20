package com.example.chris.baking.Widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.chris.baking.DataTypes.Recipe;
import com.example.chris.baking.UI.RecipeActivity;

public class UpdateWidgetService extends IntentService {

    @Override
    public void onCreate() {
        super.onCreate();

// ************************************************************************************************************************** //
// ************************************************************************************************************************** //
// ************************************************************************************************************************** //

//        This block in onCrate was added to fix the following error:
/*
        08-17 13:54:39.518 28493-28493/com.example.chris.baking E/AndroidRuntime: FATAL EXCEPTION: main
        Process: com.example.chris.baking, PID: 28493
        android.app.RemoteServiceException: Context.startForegroundService() did not then call Service.startForeground()
        at android.app.ActivityThread$H.handleMessage(ActivityThread.java:1768)
        at android.os.Handler.dispatchMessage(Handler.java:106)
        at android.os.Looper.loop(Looper.java:164)
        at android.app.ActivityThread.main(ActivityThread.java:6494)
        at java.lang.reflect.Method.invoke(Native Method)
        at com.android.internal.os.RuntimeInit$MethodAndArgsCaller.run(RuntimeInit.java:438)
        at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:807)
*/

// For details see:
// https://stackoverflow.com/questions/44425584/context-startforegroundservice-did-not-then-call-service-startforeground

        if (Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
// ************************************************************************************************************************** //
// ************************************************************************************************************************** //
// ************************************************************************************************************************** //
    }

    public static final String ACTION_UPDATE_WIDGET_INGREDIENTS = "com.example.chris.baking.action.update_widget_ingredients";


    public UpdateWidgetService(){
        super("UpdateWidgetService");
    }


    public static void startActionUpdateRecipe (Context context){
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET_INGREDIENTS);
        //Allows starting a service without app running
        ContextCompat.startForegroundService(context, intent);
    }

    public static void startActionUpdateRecipe (Context context, Recipe recipe){
        Intent intent = new Intent(context, UpdateWidgetService.class);
        intent.setAction(ACTION_UPDATE_WIDGET_INGREDIENTS);
        //TODO string resource
        intent.putExtra(RecipeActivity.EXTRA_RECIPE, recipe);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null){
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET_INGREDIENTS.equals(action)){
                Recipe recipe = intent.getParcelableExtra(RecipeActivity.EXTRA_RECIPE);
                handleActionUpdateWidget(recipe);
            }
        }

    }

    private void handleActionUpdateWidget(@Nullable Recipe recipe){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        int[] appIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingAppWidget.class));

        BakingAppWidget.updateRecipe(this, appWidgetManager, appIds, recipe);

    }


}
