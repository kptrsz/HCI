package ptr.hf.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import butterknife.BindView;
import butterknife.ButterKnife;
import ptr.hf.ChargerApplication;
import ptr.hf.R;
import ptr.hf.ui.auth.LoginActivity;
import ptr.hf.ui.map.MapFragment;
import ptr.hf.ui.map.MapFragment2;
import ptr.hf.ui.map.MapFragment3;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static MainActivity INSTANCE;


    public static MainActivity getInstance() {
        return INSTANCE;
    }

    FragmentManager fragmentManager = getSupportFragmentManager();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.container)
    FrameLayout container;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.veryTransparentColorPrimaryDark));

        toolbar.setTitle("Közeli töltőállomások");
        fragmentManager
                .beginTransaction()
                .replace(R.id.container, new MapFragment())
                .commit();

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        View headerLayout = navView.getHeaderView(0);
        ImageView profileImage = (ImageView) headerLayout.findViewById(R.id.profile_picture);
        TextView profileName = (TextView) headerLayout.findViewById(R.id.profile_name);
        TextView profileEmail = (TextView) headerLayout.findViewById(R.id.profile_email);
        if (user.getPhotoUrl() != null)
            Picasso
                    .with(this)
                    .load(user.getPhotoUrl())
                    .transform(new CircleTransform())
                    .into(profileImage);

        if (user.getDisplayName() != null)
            profileName.setText(user.getDisplayName());
        else
            profileName.setText("Vendég");

        if (user.getEmail() != null)
            profileEmail.setText(user.getEmail());
        else
            profileEmail.setText("");


        navView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ChargerApplication.activityResumed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ChargerApplication.activityPaused();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_maps) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        Fragment fragment = new MapFragment();

        if (id == R.id.nav_maps) {
//            toolbar.setTitle(R.string.map);
            toolbar.setTitle("Közeli töltőállomások");
//            onNavigationItemSelected(navView.getMenu().getItem(0));
            fragment = new MapFragment();
        } else if (id == R.id.nav_reservation) {
            toolbar.setTitle(R.string.reservation);
//            onNavigationItemSelected(navView.getMenu().getItem(1));
            fragment = new MapFragment2();
        } else if (id == R.id.nav) {
//            onNavigationItemSelected(navView.getMenu().getItem(2));
            toolbar.setTitle("Navigáció");
            fragment = new MapFragment3();
        } else if (id == R.id.nav_my_reservations) {
//            onNavigationItemSelected(navView.getMenu().getItem(3));
            toolbar.setTitle("Foglalásaim");
            fragment = new MyReservationFragment();
        } else if (id == R.id.nav_stats) {
//            onNavigationItemSelected(navView.getMenu().getItem(4));
            toolbar.setTitle(R.string.statistics);
            fragment = new StatisticsFragment();
        } else if (id == R.id.nav_settings) {
//            onNavigationItemSelected(navView.getMenu().getItem(5));
            toolbar.setTitle(R.string.settings);
            fragment = new SettingsFragment();
        } else if (id == R.id.nav_logout) {
//            onNavigationItemSelected(navView.getMenu().getItem(6));
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_info) {
//            onNavigationItemSelected(navView.getMenu().getItem(7));
            toolbar.setTitle(R.string.information);
            fragment = new InformationFragment();
        }

        fragmentManager
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();

        drawerLayout
                .closeDrawer(GravityCompat.START);
        return true;
    }
}
