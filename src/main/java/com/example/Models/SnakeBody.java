package com.example.Models;

public class SnakeBody extends Entity implements Moving {

  public SnakeBody(int posX, int posY) {
    super(posX, posY);
  }

  @Override
  public void moveRight() {
    this.posX++;
  }

  @Override
  public void moveLeft() {
    this.posX--;
  }

  @Override
  public void moveUp() {
    this.posY--;
  }

  @Override
  public void moveDown() {
    this.posY++;
  }
}
