package com.example.notificationsjava;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final int NOTIFICATION_ID = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container_view), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initialize();
    }

    void onDialogResult(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void showToast() {
        Toast.makeText(this, "Toast", Toast.LENGTH_SHORT).show();
    }

    private void showSnackBar() {
        Snackbar.make(findViewById(R.id.container_view), "SnackBar", Snackbar.LENGTH_SHORT).show();
    }

    private void showSnackBarWithAction() {
        Snackbar.make(findViewById(R.id.container_view), "SnackBar with Action", Snackbar.LENGTH_INDEFINITE)
                .setAction("Push me", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showToast();
                    }
                })
                .show();
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("AlertDialog!")
                .setMessage("Your message here")
                // Можно указать и пиктограмму
                //.setIcon(R.mipmap.ic_launcher_round)
                // Из этого окна нельзя выйти кнопкой Back
                //.setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Yes!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "No!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    private void showAlertDialogWithCustomView() {
        final View customView = getLayoutInflater().inflate(R.layout.custom_view, null);
        customView.findViewById(R.id.button_custom_view).setOnClickListener(view -> showToast());

        new AlertDialog.Builder(this)
                .setTitle("AlertDialog with custom view")
                .setView(customView)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Yes!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "No!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("Cancel", null)
                .show();
    }

    private void showDialogFragment() {
        new MyDialogFragment().show(getSupportFragmentManager(), MyDialogFragment.TAG);
    }

    private void showDialogFragmentWithCustomView() {
        new MyDialogFragmentWithCustomView().show(getSupportFragmentManager(), MyDialogFragmentWithCustomView.TAG);
    }

    private void showBottomSheetDialogFragment() {
        new MyBottomSheetDialogFragment().show(getSupportFragmentManager(), MyBottomSheetDialogFragment.TAG);
    }

    private void showNotification() {
        // Создаем NotificationChannel, но это делается только для API 26+
        // Потому что NotificationChannel -- это новый класс и его нет в support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        // Все цветные иконки отображаются только в оттенках серого
        builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Hello")
                .setContentText("Have a nice day!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        NOTIFICATION_ID); // константа вашего выбора
            }
            return;
        }
        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Пользователь разрешил показывать уведомления
            showNotification(); // Можно показать уведомление повторно
        } else {
            // Пользователь отказался давать разрешение
            Toast.makeText(this, "Разрешение на уведомления не предоставлено.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        String name = "Name";
        String descriptionText = "Description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(descriptionText);

        // Регистрируем канал в системе
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void initialize() {
        //Способ №1: Toast
        findViewById(R.id.button_toast).setOnClickListener(view -> showToast());

        //Способ №2: SnackBar
        findViewById(R.id.button_snack_bar).setOnClickListener(view -> showSnackBar());
        findViewById(R.id.button_snack_bar_with_action).setOnClickListener(view -> showSnackBarWithAction());

        //Способ №3: Dialog
        findViewById(R.id.button_alert_dialog).setOnClickListener(view -> showAlertDialog());
        findViewById(R.id.button_alert_dialog_with_view).setOnClickListener(view -> showAlertDialogWithCustomView());
        findViewById(R.id.button_dialog_fragment).setOnClickListener(view -> showDialogFragment());
        findViewById(R.id.button_dialog_fragment_custom_view).setOnClickListener(view -> showDialogFragmentWithCustomView());

        //Способ №4: BottomSheet
        findViewById(R.id.button_bottom_sheet_dialog_fragment).setOnClickListener(view -> showBottomSheetDialogFragment());

        //Способ №5: Notification
        findViewById(R.id.button_notification).setOnClickListener(view -> showNotification());
    }
}