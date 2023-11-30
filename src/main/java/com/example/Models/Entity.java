package com.example.Models;

abstract class Entity {
  public int posX;
  public int posY;

  public Entity(int posX, int posY) {
    this.posX = posX;
    this.posY = posY;
  }

  public int getPosX() {
    return posX;
  }

  public void setPosX(int posX) {
    this.posX = posX;
  }

  public int getPosY() {
    return posY;
  }

  public void setPosY(int posY) {
    this.posY = posY;
  }
}
