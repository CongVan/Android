package com.example.musicforlife;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.SearchView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity  {

    FragmentListSong fragmentListSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadFragment();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_main);
//
//
//        // Sets the Toolbar to act as the ActionBar for this Activity window.
//        // Make sure the toolbar exists in the activity and is not null
//        setSupportActionBar(toolbar);
//
        BottomNavigationView bottomNavigationView=findViewById(R.id.navigation_main);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.navigation_song:{
                        Toast.makeText(MainActivity.this,"HOME",Toast.LENGTH_SHORT).show();

                        loadFragment();
                        break;
                    }
//                    case  R.id.navigation_song:{
//                        Toast.makeText(MainActivity.this,"SONG",Toast.LENGTH_SHORT).show();
//                        break;
//                    }
                    case  R.id.navigation_album:{
                        Toast.makeText(MainActivity.this,"ALBUM",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                return true;
            }
        });
    }
    private  void loadFragment(){
        //load fragment
        FragmentTransaction transaction= getFragmentManager().beginTransaction();
        fragmentListSong=FragmentListSong.newInstance();
        transaction.replace(R.id.frame_container,fragmentListSong);
        transaction.commit();
    }


}
