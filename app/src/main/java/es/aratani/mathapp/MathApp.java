package es.aratani.mathapp;

import java.util.Random;

public class MathApp {

    Random ran = new Random();

    public int GenerateRandom(int rand){
        return ran.nextInt(rand);
    }

    public float GetOperation(float num1, float num2, int operationType){
        switch (operationType){
            case 0:
                System.out.println(num1*num2);
                return num1 + num2;
            case 1:
                System.out.println(num1*num2);
                return num1 - num2;
            case 2:
                System.out.println(num1*num2);
                return num1 * num2;
            case 3:
                System.out.println(num1/num2);
                return num1 / num2;
            default:
                return 0;
        }
    }

    String GenerateOpString(float num1, float num2, int opType){
        switch (opType){
            case 0:
                return num1 + " + " + num2;
            case 1:
                return num1 + " - " + num2;
            case 2:
                return num1 + " x " + num2;
            case 3:
                return num1 + " / " + num2;
            default:
                return "error";
        }
    }

}
