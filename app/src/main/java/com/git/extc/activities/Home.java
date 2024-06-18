package com.git.extc.activities;

import static com.git.extc.loaderConfiguarations.LoaderConfig.LOADER_VERSION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.git.extc.BuildConfig;
import com.git.extc.R;
import com.git.extc.activities.AdminPanel.AddBooks;
import com.git.extc.activities.AdminPanel.AddSyllabus;
import com.git.extc.activities.AdminPanel.AddYoutube;
import com.git.extc.databinding.ActivityHomeBinding;
import com.git.extc.fragments.AnnouncementFragment;
import com.git.extc.fragments.ProfileFragment;
import com.git.extc.fragments.SubjectsFragment;
import com.git.extc.fragments.ToolsFragment;
import com.git.extc.preferences.Preferences;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;
    public Context context;
    public Preferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

       // getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                //WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        prefs = Preferences.with(this);
        if (!prefs.contains(LOADER_VERSION))
        {
            prefs.write(LOADER_VERSION, BuildConfig.VERSION_NAME);
        }
        String verifiedUser = prefs.read("VerificationIsConfirmed?");
        if (verifiedUser.equals("Verified")) {
            // User is verified, set visibility of drawer items
            Menu menu = binding.navigationview.getMenu();
            menu.findItem(R.id.uploadbooks).setVisible(true);
            menu.findItem(R.id.uploadsyllabus).setVisible(true);
            menu.findItem(R.id.uploadyoutube).setVisible(true);
        } else {
            // User is not verified, hide specific menu items
            Menu menu = binding.navigationview.getMenu();
            menu.findItem(R.id.uploadbooks).setVisible(false);
            menu.findItem(R.id.uploadsyllabus).setVisible(false);
            menu.findItem(R.id.uploadyoutube).setVisible(false);
        }



        Fragment fragment = new AnnouncementFragment();
        Fragment fragment1 = new SubjectsFragment();
        Fragment fragment2 = new ToolsFragment();
        Fragment fragment3 = new ProfileFragment();

        replace(fragment);

        BadgeDrawable badgeDrawable = binding.bottombar.getOrCreateBadge(R.id.layout2);
        badgeDrawable.setBackgroundColor(getResources().getColor(R.color.red));
        badgeDrawable.setVisible(true);

        boolean isFirstTime = prefs.readBoolean("isFirstTime", true);
        if (isFirstTime) {
            // Show badge on Subjects tab
            badgeDrawable.setNumber(1); // You can set any number you want to display
            prefs.writeBoolean("isFirstTime", false); // Update isFirstTime flag
        } else {
            badgeDrawable.clearNumber(); // Clear badge if not the first time
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String returnToFragment = extras.getString("returnToFragment");
            if (returnToFragment != null) {

                if (returnToFragment.equals("Fragment4")) {

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.homebg, fragment3);
                    transaction.commit();
                    binding.homeimg.setImageResource(R.drawable.ic_home_icon_idle);
                    binding.subjectsimg.setImageResource(R.drawable.book_inactivator);
                    binding.Moreimg.setImageResource(R.drawable.ic_more);
                    binding.profileimg.setImageResource(R.drawable.ic_profile_active);
                    binding.hometxt.setTextColor(Color.parseColor("#000000"));
                    binding.subjecttxt.setTextColor(Color.parseColor("#000000"));
                    binding.Moretxt.setTextColor(Color.parseColor("#000000"));
                    binding.profiletxt.setTextColor(Color.parseColor("#113251"));
                }

                if (returnToFragment.equals("Fragment3")) {

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.homebg, fragment2);
                    transaction.commit();
                    binding.homeimg.setImageResource(R.drawable.ic_home_icon_idle);
                    binding.subjectsimg.setImageResource(R.drawable.book_inactivator);
                    binding.Moreimg.setImageResource(R.drawable.ic_more_selected);
                    binding.profileimg.setImageResource(R.drawable.ic_profile_inactive);
                    binding.hometxt.setTextColor(Color.parseColor("#000000"));
                    binding.subjecttxt.setTextColor(Color.parseColor("#000000"));
                    binding.Moretxt.setTextColor(Color.parseColor("#113251"));
                    binding.profiletxt.setTextColor(Color.parseColor("#000000"));
                }

            }
        } else {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.homebg, fragment);
            transaction.commit();
            binding.homeimg.setImageResource(R.drawable.ic_home_icon__selected);
            binding.subjectsimg.setImageResource(R.drawable.book_inactivator);
            binding.Moreimg.setImageResource(R.drawable.ic_more);
            binding.profileimg.setImageResource(R.drawable.ic_profile_inactive);
            binding.hometxt.setTextColor(Color.parseColor("#113251"));
            binding.subjecttxt.setTextColor(Color.parseColor("#000000"));
            binding.Moretxt.setTextColor(Color.parseColor("#000000"));
            binding.profiletxt.setTextColor(Color.parseColor("#000000"));
        }

      binding.btndrawertoggle.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {
            binding.drawerlayout.open();

         }
     });

        binding.drawerlayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Drawer is opened, hide the bottom bar
                binding.bottombar.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Drawer is closed, make the bottom bar visible
                binding.bottombar.setVisibility(View.VISIBLE);
            }
        });

        binding.navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
          @Override
          public boolean onNavigationItemSelected(@NonNull MenuItem item) {
              int itemId = item.getItemId();
               if(itemId == R.id.uploadbooks){
                   //Toast.makeText(context, "Menu clicked",Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(context, AddBooks.class);
                   startActivity(intent);
               }

              if(itemId == R.id.uploadsyllabus){
                 // Toast.makeText(context, "Menu clicked",Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent(context, AddSyllabus.class);
                  startActivity(intent);
              }

              if(itemId == R.id.uploadyoutube){
                  //Toast.makeText(context, "Menu clicked",Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent(context, AddYoutube.class);
                  startActivity(intent);
              }

              if(itemId == R.id.visitwebsite){
                  Intent webIntent = new Intent(Intent.ACTION_VIEW,
                          Uri.parse("https://www.git-india.edu.in/git/index.html"));
                  try {
                      context.startActivity(webIntent);
                  } catch (ActivityNotFoundException ex) {
                  }
              }

              if(itemId == R.id.logout){
                  new android.app.AlertDialog.Builder(context)
                          .setTitle("Logout")
                          .setMessage("Do you really want to logout?")
                          .setIcon(android.R.drawable.ic_dialog_alert)
                          .setCancelable(false)

                          .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int which) {
                                  prefs.write("oAuthVerificationOpenedIsConfirmed?", "");
                                  prefs.write("fullname", "");
                                  prefs.write("department", "");
                                  prefs.write("VerificationIsConfirmed?", "");
                                  Toast.makeText(context, "Logout Success", Toast.LENGTH_SHORT).show();
                                  Intent intent1 = new Intent(context, Login.class);
                                  startActivity(intent1);
                                  dialogInterface.dismiss();
                              }
                          })
                          .setNegativeButton("No", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int which) {

                              }
                          })
                          .show();
              }

              binding.drawerlayout.close();
             // binding.bottombar.setVisibility(View.VISIBLE);
              return false;
          }
      });


        binding.layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.homeimg.setImageResource(R.drawable.ic_home_icon__selected);
                binding.subjectsimg.setImageResource(R.drawable.book_inactivator);
                binding.Moreimg.setImageResource(R.drawable.ic_more);
                binding.profileimg.setImageResource(R.drawable.ic_profile_inactive);
                binding.hometxt.setTextColor(Color.parseColor("#113251"));
                binding.subjecttxt.setTextColor(Color.parseColor("#000000"));
                binding.Moretxt.setTextColor(Color.parseColor("#000000"));
                binding.profiletxt.setTextColor(Color.parseColor("#000000"));
                replace(fragment);
            }
        });

        binding.layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.homeimg.setImageResource(R.drawable.ic_home_icon_idle);
                binding.subjectsimg.setImageResource(R.drawable.book_activator);
                binding.Moreimg.setImageResource(R.drawable.ic_more);
                binding.profileimg.setImageResource(R.drawable.ic_profile_inactive);
                binding.hometxt.setTextColor(Color.parseColor("#000000"));
                binding.subjecttxt.setTextColor(Color.parseColor("#113251"));
                binding.Moretxt.setTextColor(Color.parseColor("#000000"));
                binding.profiletxt.setTextColor(Color.parseColor("#000000"));
                replace(fragment1);

                BadgeDrawable badgeDrawable = binding.bottombar.getBadge(R.id.layout2);
                if (badgeDrawable != null) {
                    badgeDrawable.clearNumber();
                }
            }
        });

        binding.layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.homeimg.setImageResource(R.drawable.ic_home_icon_idle);
                binding.subjectsimg.setImageResource(R.drawable.book_inactivator);
                binding.Moreimg.setImageResource(R.drawable.ic_more_selected);
                binding.profileimg.setImageResource(R.drawable.ic_profile_inactive);
                binding.hometxt.setTextColor(Color.parseColor("#000000"));
                binding.subjecttxt.setTextColor(Color.parseColor("#000000"));
                binding.Moretxt.setTextColor(Color.parseColor("#113251"));
                binding.profiletxt.setTextColor(Color.parseColor("#000000"));
                replace(fragment2);
            }
        });

        binding.layout4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.homeimg.setImageResource(R.drawable.ic_home_icon_idle);
                binding.subjectsimg.setImageResource(R.drawable.book_inactivator);
                binding.Moreimg.setImageResource(R.drawable.ic_more);
                binding.profileimg.setImageResource(R.drawable.ic_profile_active);
                binding.hometxt.setTextColor(Color.parseColor("#000000"));
                binding.subjecttxt.setTextColor(Color.parseColor("#000000"));
                binding.Moretxt.setTextColor(Color.parseColor("#000000"));
                binding.profiletxt.setTextColor(Color.parseColor("#113251"));
                replace(fragment3);
            }
        });




    }



    @Override
    public void onBackPressed() {
       // super.onBackPressed();

        new AlertDialog.Builder(context)
                .setTitle("Confirmation")
                .setMessage("Do you really want to Exit?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setCancelable(false)

                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        finishAffinity();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {

                    }
                })
                .show();
    }

    private void replace (Fragment fragment)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.homebg, fragment);
        transaction.commit();
    }
}