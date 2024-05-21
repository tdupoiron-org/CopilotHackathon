package com.microsoft.hackathon.quarkus;

import com.google.gson.annotations.SerializedName;

public class Color {
  private String color;
  private String category;
  @SerializedName("code")
  private Code code;

  public static class Code {
    @SerializedName("rgba")
    private int[] rgba;
    @SerializedName("hex")
    private String hex;

    public int[] getRgba() {
      return rgba;
    }

    public void setRgba(int[] rgba) {
      this.rgba = rgba;
    }

    public String getHex() {
      return hex;
    }
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public Code getCode() {
    return code;
  }

  public void setCode(Code code) {
    this.code = code;
  }
  
}