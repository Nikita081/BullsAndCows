package com.fupm.skeb.androidclient;

public class BodyGame {

    private int bulls;
    private int cow;
    private int j = 0;
    private int [] riddle = new int [4];
    private int [] try_num = new int [4];
    private String log = "";
    private int attempt = 0;

    public BodyGame()
    {}

    public void riddle(){
        riddle = insert(riddle);
    }

    public int countALL(int tryNumber){
        for (j = 0; j < 4; j++){
            try_num[3 - j] = tryNumber % 10;
            tryNumber = (int) tryNumber / 10;
        }

        bulls = countBulls(riddle, try_num);
        cow = countCows(riddle, try_num);
        return bulls;
    }

    private static int[] insert(int [] riddle)
    {
        for (int i = 0; i < 4; i++) {
            riddle[i] = (int) (Math.random() * 10);
        }

        if ( (riddle[0] == riddle[1] || riddle[0] == riddle[2] || riddle[0] == riddle[3]) || (riddle[1] == riddle[2] || riddle[1] == riddle[3]) || (riddle[2] == riddle[3]) ){
            riddle = insert(riddle);
        }
        return riddle;
    }

    private static int countBulls(int [] riddle, int [] try_num){
        int bulls = 0;
        for (int i = 0; i < 4; i++){
            if (riddle[i] == try_num[i]){
                bulls++;
            }
        }
        return bulls;
    }

    private static int countCows(int [] riddle, int [] try_num){
        int cow = 0;
        for (int i = 0; i < 4; i++){
            for (int j = 0; j < 4; j++){
                if (riddle[i] == try_num[j] && i != j){
                    cow++;
                }
            }
        }
        return cow;
    }

    public String giveLog(String tryNumber){
        attempt++;
        String space = "";
        if (attempt < 10) space = "  ";
        log += "\n" +  attempt + ".  " + space + tryNumber + "     " + bulls + " Bulls    " + cow + " Cow";
        return log;
    }

    public String numberAttempts(){

        String message = "Вы совершили " + attempt + " ";
        if (attempt >= 5 && attempt <= 20) message += "ходов";
        else switch(attempt % 10) {
            case 1: message += "ход"; break;
            case 2:
            case 3:
            case 4: message += "хода"; break;
            default: message +=  "ходов"; break;
        }
        return message;
    }

    public int getAttempt(){
        return ++attempt;
    }
}
