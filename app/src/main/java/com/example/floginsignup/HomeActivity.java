package com.example.floginsignup;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.floginsignup.ui.ActivityFragment;
import com.example.floginsignup.ui.DashboardFragment;
import com.example.floginsignup.ui.ParkingFragment;
import com.example.floginsignup.ui.ProfileFragment;

public class HomeActivity extends AppCompatActivity {

    private NavItem[] items;
    private int selectedIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.homeRoot), (v, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(bars.left, bars.top, bars.right, 0);
            return insets;
        });

        items = new NavItem[]{
                new NavItem(findViewById(R.id.navHome),    R.drawable.ic_nav_home,    R.string.nav_home),
                new NavItem(findViewById(R.id.navParking), R.drawable.ic_nav_parking, R.string.nav_parking),
                new NavItem(findViewById(R.id.navActivity),R.drawable.ic_nav_activity,R.string.nav_activity),
                new NavItem(findViewById(R.id.navProfile), R.drawable.ic_nav_profile, R.string.nav_profile)
        };

        for (int i = 0; i < items.length; i++) {
            final int index = i;
            items[i].init(this);
            items[i].container.setOnClickListener(v -> select(index));
        }

        if (savedInstanceState == null) select(0);
    }

    private void select(int index) {
        if (selectedIndex == index) return;
        selectedIndex = index;
        for (int i = 0; i < items.length; i++) items[i].setActive(this, i == index);

        Fragment f;
        switch (index) {
            case 1: f = new ParkingFragment(); break;
            case 2: f = new ActivityFragment(); break;
            case 3: f = new ProfileFragment(); break;
            case 0:
            default: f = new DashboardFragment(); break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.homeContainer, f)
                .commit();
    }

    public static Intent intent(Context ctx) {
        return new Intent(ctx, HomeActivity.class);
    }

    private static class NavItem {
        final LinearLayout container;
        final int iconRes;
        final int labelRes;
        ImageView icon;
        TextView label;

        NavItem(LinearLayout container, int iconRes, int labelRes) {
            this.container = container;
            this.iconRes = iconRes;
            this.labelRes = labelRes;
        }

        void init(Context ctx) {
            container.removeAllViews();
            icon = new ImageView(ctx);
            icon.setImageResource(iconRes);
            label = new TextView(ctx);
            label.setText(labelRes);
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            label.setAllCaps(true);
            label.setLetterSpacing(0.05f);
            container.addView(icon);
            container.addView(label);
        }

        void setActive(Context ctx, boolean active) {
            float density = ctx.getResources().getDisplayMetrics().density;
            container.setOrientation(LinearLayout.VERTICAL);
            container.setGravity(Gravity.CENTER);

            LinearLayout.LayoutParams iconLp = new LinearLayout.LayoutParams(dp(density, 20), dp(density, 20));
            icon.setLayoutParams(iconLp);

            LinearLayout.LayoutParams lblLp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lblLp.setMarginStart(0);
            lblLp.topMargin = dp(density, 4);
            label.setLayoutParams(lblLp);
            label.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            label.setVisibility(View.VISIBLE);

            if (active) {
                container.setBackgroundResource(R.drawable.bg_nav_pill_active);
                container.setPadding(dp(density, 12), dp(density, 14), dp(density, 12), dp(density, 14));
                icon.setImageTintList(ContextCompat.getColorStateList(ctx, R.color.white));
                label.setTextColor(ContextCompat.getColor(ctx, R.color.white));
                label.setTypeface(null, android.graphics.Typeface.BOLD);
            } else {
                container.setBackground(null);
                container.setPadding(dp(density, 8), dp(density, 14), dp(density, 8), dp(density, 14));
                icon.setImageTintList(ContextCompat.getColorStateList(ctx, R.color.text_muted));
                label.setTextColor(ContextCompat.getColor(ctx, R.color.text_muted));
                label.setTypeface(null, android.graphics.Typeface.NORMAL);
            }
        }

        private int dp(float density, int v) { return Math.round(v * density); }
    }
}
