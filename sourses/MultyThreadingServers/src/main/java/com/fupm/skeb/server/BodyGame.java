package com.fupm.skeb.server;

public class BodyGame {

	private int [] res_attempt = new int[2];
    private int j = 0;
    private int [] riddle = new int [4];
    private int [] try_num = new int [4];
    private int attempt = 0;

    public BodyGame(int riddle){
    	for (j = 0; j < 4; j++){
            this.riddle[3 - j] = riddle % 10;
            riddle = (int) riddle / 10;
        }
    }

    public int [] countAll(int tryNumber){
        for (j = 0; j < 4; j++){
            try_num[3 - j] = tryNumber % 10;
            tryNumber = (int) tryNumber / 10;
        }

        res_attempt[0] = countBulls(riddle, try_num);
        res_attempt[1] = countCows(riddle, try_num);
        return res_attempt;
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


    public String numberAttempts(){

        String message = "Вы совершили " + attempt + " ";
        if (attempt >= 5 && attempt <= 20) message += "ходов";
        else switch(attempt % 10) {
            case 1: message += "ход"; break;
            case 2:
            case 3:
            case 4: message += "хода"; break;
            default: message += "ходов"; break;
        }
        return message;
    }
}
