package com.example.nhom4_tuan07_xml;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toolbar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends Activity {

    // Main GUI - A NEWS application based on National Public Radio RSS material
    ArrayAdapter<String> adapterMainSubjects;
    GridView myMainListView;
    Context context;
    Toolbar toolbarMain;
    SingleItem selectedNewsItem;

    // hard-coding main NEWS categories (TODO: use a resource file)
    String[] myUrlCaptionMenu = {
            "THANH NIÊN",
            "VTC",
            "VOV",
            "VIETNAMNET",
            "VnExpress"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        toolbarMain=findViewById(R.id.toolbarMain);
//        this.setTitle("News App\n");
        toolbarMain.setTitle("News App");
        // user will tap on a ListView’s row to request category’s headlines
        myMainListView = (GridView) this.findViewById(R.id.myListView);
        myMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String urlCaption = myUrlCaptionMenu[position];
        //create an Intent to talk to activity: ShowHeadlines
                Intent callSMenuRss = new Intent(MainActivity.this, MenuRss.class);
                //prepare a Bundle and add the input arguments: url & caption
                Bundle myData = new Bundle();
                myData.putString("positionCaption", position + "");
                myData.putString("urlCaption", urlCaption + "");
                callSMenuRss.putExtras(myData);
                startActivity(callSMenuRss);
            }
        });

        // fill up the Main-GUI’s ListView with main news categories
        adapterMainSubjects = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, //android's default
                myUrlCaptionMenu);
        myMainListView.setAdapter(adapterMainSubjects);
    }

}
