
package com.xyuai.app;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.View;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatingService extends Service {

    private WindowManager wm;
    private View panel;
    private WindowManager.LayoutParams params;

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        LinearLayout menu = new LinearLayout(this);
        menu.setOrientation(LinearLayout.VERTICAL);
        menu.setPadding(24, 20, 24, 20);
        menu.setBackgroundColor(Color.rgb(12, 20, 25));

        TextView title = new TextView(this);
        title.setText("XuyAI  •  MENU");
        title.setTextColor(Color.CYAN);
        title.setTextSize(20);
        menu.addView(title);

        addSwitch(menu, "ESP");
        addSwitch(menu, "Boxes");
        addSwitch(menu, "Names");
        addSwitch(menu, "Distance");

        TextView close = new TextView(this);
        close.setText("CLOSE");
        close.setTextColor(Color.WHITE);
        close.setPadding(0, 20, 0, 0);
        menu.addView(close);

        close.setOnClickListener(v -> stopSelf());

        panel = menu;

        params = new WindowManager.LayoutParams(
            650,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = 50;
        params.y = 200;

        wm.addView(panel, params);

        panel.setOnTouchListener(new View.OnTouchListener() {
            private int startX, startY;
            private float touchX, touchY;

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startX = params.x;
                    startY = params.y;
                    touchX = event.getRawX();
                    touchY = event.getRawY();
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    params.x = startX + (int)(event.getRawX() - touchX);
                    params.y = startY + (int)(event.getRawY() - touchY);
                    wm.updateViewLayout(panel, params);
                    return true;
                }
                return true;
            }
        });
    }

    private void addSwitch(LinearLayout menu, String name) {
        android.widget.Switch toggle = new android.widget.Switch(this);
        toggle.setText(name);
        toggle.setTextColor(Color.WHITE);
        menu.addView(toggle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (panel != null) wm.removeView(panel);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
