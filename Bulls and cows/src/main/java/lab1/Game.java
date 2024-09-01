package lab1;

import java.util.Random;
import java.util.Scanner;
import java.lang.String;
public class Game {

    public Game(){

        generation_num();
        in = new Scanner(System.in);
    }

    public void generation_num(){

        int kol = 1;
        num = new StringBuilder();
        Random random = new Random();
        num.append(Integer.toString((random.nextInt(9) + 1)));
        while(!(kol==4)){

            String w = Integer.toString(random.nextInt(10));
            if (!(num.toString().contains(w))){ //List<String>

                num.append(w);
                kol++;
            }
        }
        System.out.print("Число сгенерировано\nУгадывай)\n");

    }

    public void play(){

        int bulls = 0;
        int cows;
        while (bulls != 4){

            String version = in.nextLine();
            cows = 0;
            bulls = 0;
            for (int i = 0; i<4; i++){

                int ind = num.toString().indexOf(version.charAt(i));
                if (ind == i){
                    bulls++;
                }
                else if (ind != -1) {
                    cows++;
                }
            }
            System.out.println("Быков "+ bulls + " Коров " + cows);
        }
        System.out.println("ПОБЕДА\nДелай следующую лабу");
    }

    public void setNum(String num) {
        this.num = new StringBuilder(num);
    }

    public String getOutput() {
        return output.toString();
    }

    private StringBuilder num;
    private final Scanner in;
    private StringBuilder output = new StringBuilder();
}
