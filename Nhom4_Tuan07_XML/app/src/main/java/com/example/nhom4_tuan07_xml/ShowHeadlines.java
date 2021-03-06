package com.example.nhom4_tuan07_xml;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;
import java.util.ArrayList;

public class ShowHeadlines extends Activity {

    ArrayList<SingleItem> newsList = new ArrayList<SingleItem>();
    ListView myListView;
    String urlAddress = "";
    String urlCaption = "";
    String newsArticle = "";
    SingleItem selectedNewsItem;
    Toolbar toolbarHeadlines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_headlines);
        toolbarHeadlines=findViewById(R.id.toolbarHeadlines);
        // myListView = (ListView) this.findViewById(R.id.myListView);

        // find out which intent is calling us
        Intent callingIntent = getIntent();

        // grab data bundle holding selected url & caption sent to us
        Bundle myBundle = callingIntent.getExtras();
        urlAddress = myBundle.getString("urlAddress");
        urlCaption = myBundle.getString("urlCaption");
        newsArticle = myBundle.getString("newsArticle");

        // this.setTitle("NPR - " + urlCaption + " - " + MenuRss.niceDate());
//        this.setTitle(newsArticle + " - " + urlCaption);
        toolbarHeadlines.setTitle(newsArticle + " - " + urlCaption);
        // clicking on a row shows dialogBox with more info about selected item
        myListView = (ListView) this.findViewById(R.id.myListView);
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedNewsItem = newsList.get(position);
                showNiceDialogBox(selectedNewsItem, getApplicationContext());
            }

        });

        // get stories for the selected news option
        DownloadRssFeed downloader = new DownloadRssFeed(ShowHeadlines.this);
        downloader.execute(urlAddress, urlCaption);

    }

    public void showNiceDialogBox(SingleItem selectedStoryItem, Context context) {
        // make a nice looking dialog box (story summary, btnClose, btnMore)
        // CAUTION: (check)on occasions title and description are the same!
        String title = selectedStoryItem.getTitle();

        String description = selectedStoryItem.getDescription();
        if (title.toLowerCase().equals(description.toLowerCase())) {
            description = "";
        }
        try {
            //CAUTION: sometimes TITLE and DESCRIPTION include HTML markers
            final Uri storyLink = Uri.parse(selectedStoryItem.getLink());
            AlertDialog.Builder myBuilder = new AlertDialog.Builder(this);
            myBuilder
//                    .setIcon(R.drawable.logo_npr)
                    .setTitle(Html.fromHtml(newsArticle + " - " + urlCaption))
                    .setMessage(title + "\n\n" + Html.fromHtml(description) + "\n")
                    .setPositiveButton("Close", null)
                    .setNegativeButton("More", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichOne) {
                            Intent browser = new Intent(Intent.ACTION_VIEW, storyLink);
                            startActivity(browser);
                        }
                    })//setNegativeButton
                    .show();
        } catch (Exception e) {
            Log.e("Error DialogBox", e.getMessage());
        }
    }
}
