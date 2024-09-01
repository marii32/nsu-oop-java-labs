package SecGame.Hero;

public class HeroModel {

    public int x;
    public int y;
    public int size;
    public int speed = 50;
    public boolean moving;
    public int WinWidth;
    public int WinHeight;
    public String side = "left";
    public HeroModel(int WinWidth, int WinHeight){

        this.WinWidth = WinWidth;
        this.WinHeight = WinHeight;
        this.x = WinWidth/2;
        this.y = WinHeight/2;
        this.size = 50;
    }



    public int getX() {

        return x;
    }

    public int getY() {

        return y;
    }

    public int getSize(){

        return size;
    }


}
