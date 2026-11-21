
package com.xyuai.app;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Switch;

public class FloatingService extends Service {
    private WindowManager wm;
    private LinearLayout panel;
    private TextView bubble;
    private WindowManager.LayoutParams params;
    private WindowManager.LayoutParams bubbleParams;

    private int dp(float value) {
        return (int)(value * getResources()
            .getDisplayMetrics().density);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        wm = (WindowManager)getSystemService(WINDOW_SERVICE);

        panel = new LinearLayout(this);
        panel.setOrientation(LinearLayout.VERTICAL);
        panel.setPadding(dp(16), dp(12), dp(16), dp(12));
        panel.setBackgroundColor(Color.rgb(12, 20, 25));

        TextView title = new TextView(this);
        title.setText("XuyAI  |  DRAG HERE");
        title.setTextColor(Color.CYAN);
        title.setTextSize(18);
        panel.addView(title);

        addSwitch("ESP");
        addSwitch("Boxes");
        addSwitch("Names");
        addSwitch("Distance");

        TextView minimize = new TextView(this);
        minimize.setText("MINIMIZE");
        minimize.setTextColor(Color.WHITE);
        minimize.setTextSize(16);
        minimize.setPadding(0, dp(12), 0, dp(4));
        panel.addView(minimize);

        params = new WindowManager.LayoutParams(
            dp(280),
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );

        params.gravity = Gravity.TOP | Gravity.START;
        params.x = dp(20);
        params.y = dp(100);

        wm.addView(panel, params);

        title.setOnTouchListener(new View.OnTouchListener() {
            int startX, startY;
            float touchX, touchY;

            public boolean onTouch(View v, MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    startX = params.x;
                    startY = params.y;
                    touchX = e.getRawX();
                    touchY = e.getRawY();
                    return true;
                }

                if (e.getAction() == MotionEvent.ACTION_MOVE) {
                    params.x = startX + (int)(e.getRawX() - touchX);
                    params.y = startY + (int)(e.getRawY() - touchY);
                    wm.updateViewLayout(panel, params);
                    return true;
                }
                return true;
            }
        });

        minimize.setOnClickListener(v -> collapse());
    }

    private void addSwitch(String name) {
        Switch toggle = new Switch(this);
        toggle.setText(name);
        toggle.setTextColor(Color.WHITE);
        panel.addView(toggle);
    }

    private void collapse() {
        if (panel.getParent() != null) {
            wm.removeView(panel);
        }

        bubble = new TextView(this);
        bubble.setText("X");
        bubble.setTextColor(Color.WHITE);
        bubble.setTextSize(22);
        bubble.setGravity(Gravity.CENTER);
        bubble.setBackgroundColor(Color.rgb(0, 150, 160));

        bubbleParams = new WindowManager.LayoutParams(
            dp(55), dp(55),
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        );

        bubbleParams.gravity = Gravity.TOP | Gravity.START;
        bubbleParams.x = params.x;
        bubbleParams.y = params.y;

        wm.addView(bubble, bubbleParams);
        bubble.setOnClickListener(v -> expand());
    }

    private void expand() {
        if (bubble != null && bubble.getParent() != null) {
            wm.removeView(bubble);
        }
        wm.addView(panel, params);
    }

    @Override
    public void onDestroy() {
        if (panel != null && panel.getParent() != null) {
            wm.removeView(panel);
        }
        if (bubble != null && bubble.getParent() != null) {
            wm.removeView(bubble);
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
