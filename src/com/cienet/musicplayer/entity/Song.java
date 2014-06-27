package com.cienet.musicplayer.entity;

public class Song {
  private String name;
  private String album;
  private int image;

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
}
