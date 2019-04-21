package com.example.musicforlife.playlist;

public class PlaylistModel {
    public static final String TABLE_NAME = "playlist";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PLAYLIST_TITLE = "title";
    public static final String COLUMN_PATH_IMAGE = "path_image";


    public static final String SCRIPT_CREATE_TABLE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(COLUMN_PLAYLIST_TITLE).append(" TEXT ")
            .append(" )")
            .toString();

    private int id;
    private String title;
    private String pathImage;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }
}
