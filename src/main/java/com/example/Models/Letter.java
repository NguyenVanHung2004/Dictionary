package com.example.Models;

public class Letter{
    public char c;
    public int posX;
    public int posY;
    Letter( char c){
        this.c = c;
        this.posX = 0 ;
        this.posY = 0 ;
    }
    public Letter(char c, int x, int y){
        this.c = c;
        this.posX = x ;
        this.posY = y ;
    }


}
