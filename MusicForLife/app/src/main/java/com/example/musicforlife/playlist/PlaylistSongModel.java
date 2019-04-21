package com.example.musicforlife.playlist;

public class PlaylistSongModel {
    public static final String TABLE_NAME = "playlist_song";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PLAYLIST_ID = "playlist_id";
    public static final String COLUMN_SONG_ID = "song_id";

    public static final String SCRIPT_CREATE_TABLE = new StringBuilder("CREATE TABLE ")
            .append(TABLE_NAME).append("(")
            .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ")
            .append(COLUMN_PLAYLIST_ID).append(" INTEGER, ")
            .append(COLUMN_SONG_ID).append(" INTEGER ")
            .append(" )")
            .toString();
    private int id;
    private int playlistId;
    private int songId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }
}
