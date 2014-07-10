package com.cienet.musicplayer.entity;

public class Song {
  private String name;
  private String album;
  private int image;
  private String url;
  private String singer;

  public Song() {
    super();
  }

  public Song(String name, String album, int image) {
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

  public int getImage() {
    return image;
  }

  public void setImage(int image) {
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
}
