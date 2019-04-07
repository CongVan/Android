package com.example.nhom4_tuan07_xml;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MenuRss extends AppCompatActivity {
    ArrayAdapter<String> adapterMainSubjects;
    ListView myMainListView;
    Context context;
    SingleItem selectedNewsItem;
    String[][] myUrlCaptionMenu;
    //define convenient URL and CAPTIONs arrays
    String[] myUrlCaption;
    String[] myUrlAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_rss);

        // find out which intent is calling us
        Intent callingIntent = getIntent();

        // grab data bundle holding selected url & caption sent to us
        Bundle myBundle = callingIntent.getExtras();
        String positioncap = myBundle.getString("positionCaption");

        myUrlCaptionMenu = getUrlRss(positioncap);
        myUrlCaption = new String[myUrlCaptionMenu.length];
        myUrlAddress = new String[myUrlCaptionMenu.length];
        for (int i = 0; i < myUrlCaptionMenu.length; i++) {
            myUrlAddress[i] = myUrlCaptionMenu[i][0];
            myUrlCaption[i] = myUrlCaptionMenu[i][1];
        }
        context = getApplicationContext();
        this.setTitle("Menu News\n" + niceDate());

        // user will tap on a ListView’s row to request category’s headlines
        myMainListView = (ListView) this.findViewById(R.id.myListViewMenu);
        myMainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String urlAddress = myUrlAddress[position];
                String urlCaption = myUrlCaption[position];
                //create an Intent to talk to activity: ShowHeadlines
                Intent callShowHeadlines = new Intent(MenuRss.this, ShowHeadlines.class);
                //prepare a Bundle and add the input arguments: url & caption
                Bundle myData = new Bundle();
                myData.putString("urlAddress", urlAddress);
                myData.putString("urlCaption", urlCaption);
                callShowHeadlines.putExtras(myData);
                startActivity(callShowHeadlines);
            }
        });

        // fill up the Main-GUI’s ListView with main news categories
        adapterMainSubjects = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, //android's default
                myUrlCaption);
        myMainListView.setAdapter(adapterMainSubjects);
    }

    public static String niceDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM d, yyyy ", Locale.US);
        return sdf.format(new Date());
    }

    private String[][] getUrlRss(String news){
        String[][] result = {};
        switch (news){
            case "0":
                result = new String[][]{
                        {"https://thanhnien.vn/rss/home.rss", "Trang chủ"},
                        {"https://thanhnien.vn/rss/viet-nam.rss", "Thời sự"},
                        {"https://thanhnien.vn/rss/viet-nam/phap-luat.rss", "Pháp luật"},
                        {"https://thanhnien.vn/rss/phap-luat/trong-an.rss", "Trọng án"},
                        {"https://thanhnien.vn/rss/chinh-tri-xa-hoi/phong-su.rss", "Phóng sự"},
                        {"https://thanhnien.vn/rss/thoi-su/quoc-phong.rss", "Quốc phòng"},
                        {"https://thanhnien.vn/rss/chinh-tri-xa-hoi/doc-quyen.rss", "Độc quyền"},
                        {"https://thanhnien.vn/rss/doi-song/dan-sinh.rss", "Dân sinh"},
                        {"https://thanhnien.vn/rss/thoi-su/viec-lam.rss", "Việc làm"},
                        {"https://thanhnien.vn/rss/viec-lam/can-biet.rss", "Cần biết"},
                        {"https://thanhnien.vn/rss/viec-lam/nghe-hot.rss", "nghề hot"},
                        {"https://thanhnien.vn/rss/viec-lam/ky-nang-tim-viec.rss", "Kỹ năng tìm việc"},
                        {"https://thanhnien.vn/rss/viec-lam/tuyen-dung.rss", "Tuyển dụng"},
                        {"https://thanhnien.vn/rss/viec-lam/san-viec.rss", "Săn việc"},
                        {"https://thanhnien.vn/rss/chinh-tri.rss", "Chính trị"},
                        {"https://thanhnien.vn/rss/the-gioi/quan-su.rss", "Quân sự"},
                        {"https://thanhnien.vn/rss/the-gioi/kinh-te-the-gioi.rss", "Kinh tế thới giới"}
                };
                break;
            case "1":
                result = new String[][]{
                        {"https://vtc.vn/truyenhinh.rss", "Truyền hình"},
                        {"https://vtc.vn/phap-luat.rss", "Pháp luật"},
                        {"https://vtc.vn/quoc-te.rss", "Quốc tế"},
                        {"https://vtc.vn/dia-oc.rss", "Địa ốc"},
                        {"https://vtc.vn/giai-tri.rss", "Giải trí"},
                        {"https://vtc.vn/the-thao.rss", "Thể thao"},
                        {"https://vtc.vn/gioi-tre.rss", "Giới trẻ"},
                        {"https://vtc.vn/doanh-nghiep-doanh-nhan.rss", "Doanh nghiệp"},
                        {"https://vtc.vn/phong-su-kham-pha.rss", "Phóng sự"},
                        {"https://vtc.vn/giao-duc.rss", "Giáo dục"},
                        {"https://vtc.vn/cong-nghe.rss", "Công nghệ"},
                        {"https://vtc.vn/oto-xe-may.rss", "Oto xe máy"},
                        {"https://vtc.vn/suc-khoe-doi-song.rss", "Sức khỏe"}
                };
                break;
            case "2":
                result = new String[][]{
                        {"https://vov.vn/rss/chinh-tri-209.rss", "Chính trị"},
                        {"https://vov.vn/rss/doi-song-218.rss", "Đời sống"},
                        {"https://vov.vn/rss/vov-binh-luan-352.rss", "Bình luận"},
                        {"https://vov.vn/rss/kinh-te/doanh-nghiep-277.rss", "Doanh nghiệp"},
                        {"https://vov.vn/rss/xa-hoi-314.rss", "Xã hội"},
                        {"https://vov.vn/rss/kinh-te-212.rss", "Kinh tế"},
                        {"https://vov.vn/rss/the-gioi-213.rss", "Thế giới"},
                        {"https://vov.vn/rss/phap-luat-237.rss", "Pháp luật"},
                        {"https://vov.vn/rss/quan-su-quoc-phong-445.rss", "quân sự"},
                        {"https://vov.vn/rss/cong-nghe-449.rss", "Công nghệ"},
                        {"https://vov.vn/rss/the-thao-214.rss", "Thể thao"},
                        {"https://vov.vn/rss/van-hoa-giai-tri-215.rss", "Văn hóa"},
                        {"https://vov.vn/rss/nguoi-viet-287.rss", "Người Việt"},
                        {"https://vov.vn/rss/suc-khoe-311.rss", "Sức khỏe"},
                        {"https://vov.vn/rss/oto-xe-may-423.rss", "oto xe máy"},
                        {"https://vov.vn/rss/e-magazine-470.rss", "E magazine"}
                };
                break;
            case "3":
                result = new String[][]{
                        {"https://vietnamnet.vn/rss/phap-luat.rss", "Pháp luật"},
                        {"https://vietnamnet.vn/rss/cong-nghe.rss", "Công nghệ"},
                        {"https://vietnamnet.vn/rss/kinh-doanh.rss", "Kinh doanh"},
                        {"https://vietnamnet.vn/rss/giao-duc.rss", "Giáo dục"},
                        {"https://vietnamnet.vn/rss/thoi-su.rss", "Thời sự"},
                        {"https://vietnamnet.vn/rss/giai-tri.rss", "Giải trí"},
                        {"https://vietnamnet.vn/rss/suc-khoe.rss", "Sức khỏe"},
                        {"https://vietnamnet.vn/rss/the-thao.rss", "Thể thao"},
                        {"https://vietnamnet.vn/rss/doi-song.rss", "Đời sống"},
                        {"https://vietnamnet.vn/rss/the-gioi.rss", "Thế giới"},
                        {"https://vietnamnet.vn/rss/bat-dong-san.rss", "Bất động sản"},
                        {"https://vietnamnet.vn/rss/ban-doc.rss", "Bạn đọc"},
                        {"https://vietnamnet.vn/rss/tin-moi-nong.rss", "Tin mới nóng"},
                        {"https://vietnamnet.vn/rss/tuanvietnam.rss", "Tuần Việt Nam"},
                        {"https://vietnamnet.vn/rss/goc-nhin-thang.rss", "Góc nhìn tháng"}
                };
                break;
        }
        return  result;
    }
}
