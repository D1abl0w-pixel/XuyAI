
package com.xyuai.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 50, 30, 30);
        layout.setBackgroundColor(Color.rgb(12, 15, 20));

        TextView title = new TextView(this);
        title.setText("XuyAI");
        title.setTextSize(36);
        title.setTextColor(Color.CYAN);
        layout.addView(title);

        TextView info = new TextView(this);
        info.setText("FLOATING CONTROL PANEL");
        info.setTextColor(Color.WHITE);
        layout.addView(info);

        Button activate = new Button(this);
        activate.setText("ACTIVATE XuyAI");
        layout.addView(activate);

        activate.setOnClickListener(v -> {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())
                );
                startActivity(intent);
            } else {
                startService(new Intent(this, FloatingService.class));
            }
        });

        setContentView(layout);
    }
}
