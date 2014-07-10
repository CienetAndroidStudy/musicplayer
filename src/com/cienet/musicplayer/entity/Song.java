package com.cienet.musicplayer.entity;

import android.graphics.Bitmap;

public class Song {
  private String name;
  private String album;
  private Bitmap image;
  private String url;
  private String singer;
  private long duration;
  private long songId;
  private long albumId;

  public Song() {
    super();
  }

  public Song(String name, String album, Bitmap image) {
    super();
    this.name = name;
    this.album = album;
    this.image = image;
  }

  public Song(String name, String album) {
    super();
    this.name = name;
    this.album = album;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAlbum() {
    return album;
  }

  public void setAlbum(String album) {
    this.album = album;
  }

  public Bitmap getImage() {
    return image;
  }

  public void setImage(Bitmap image) {
    this.image = image;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getSinger() {
    return singer;
  }

  public void setSinger(String singer) {
    this.singer = singer;
  }

  public long getDuration() {
    return duration;
  }

  public void setDuration(long duration) {
    this.duration = duration;
  }

  public long getSongId() {
    return songId;
  }

  public void setSongId(long songId) {
    this.songId = songId;
  }

  public long getAlbumId() {
    return albumId;
  }

  public void setAlbumId(long albumId) {
    this.albumId = albumId;
  }
}
