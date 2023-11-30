package com.example.Models;

public class Letter extends Entity implements  Moving{
    public char c;
    public Letter(char c, int x, int y){
        super(x,y);
        this.c = c;
    }
    @Override
    public void moveRight(){
        this.posX++;
    }
    @Override
    public void moveLeft(){
        this.posX--;
    }
    @Override
    public void moveUp(){
        this.posY--;
    }
    @Override
    public void moveDown(){
        this.posY+=30;
    }


}
