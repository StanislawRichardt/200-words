package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static char[] polishLettersTable = {'a','ą','b','c','ć','d','e','ę','f','g','h','i','j','k','l','ł','m','n','ń','o',
            'ó','p','r','s','ś','t','u','w','y','z','ź','ż'};
    static String[] wordsTable= new String[200];
    static List<String[]> symbolCounterList = new ArrayList<>();
    static List<String[]> dictionaryCounterList = new ArrayList<>();
    static Random rand = new Random();
    static Scanner read= new Scanner(System.in);
    static BufferedReader bufferedReader = null;
    static String filePath = "E:\\Repozytoria\\200Words\\example\\4wyrazy.txt";
    static File file = new File(filePath);
    static String data;

    private static void sourceChoice() {
        System.out.println("Choose probability source: ");
        System.out.println("a- equal probabilities");
        System.out.println("b- Probabilities from 4wyrazy.txt");
        String choice = read.next();
        switch (choice) {
            case "a" -> equalProbabilityWordsGenerator();
            case "b" -> {
                dictionaryCreation();
                fixedProbabilityWordsGenerator();
            }
        }
        probabilityCount(symbolCounterList, 800,0);
        System.out.println("Own generator probability list: ");
        probabilityDisplay(symbolCounterList);

    }
    private static void equalProbabilityWordsGenerator(){
        for(int i=0;i<200;i++)
        {
            for(int j=0;j<4;j++)
            {
                int randomLetter = rand.nextInt(32);
                if(j==0)
                {
                    wordsTable[i] = Character.toString(polishLettersTable[randomLetter]) ;
                }
                else {wordsTable[i]= wordsTable[i] + polishLettersTable[randomLetter];}
                searchAndSave(polishLettersTable[randomLetter],symbolCounterList);
            }
        }
    }
    private static void probabilityCount(List<String[]> list, int length, int start){
        for (int i = start; i < list.size(); i++) {
            double counter = Double.parseDouble(list.get(i)[1]) / length;
            list.set(i, new String[]{list.get(i)[0], Double.toString(counter)});
        }
    }
    private static void wordsDisplay(){
        for(int i=0;i<wordsTable.length;i++)
        {
            System.out.println(i + " word is: "+wordsTable[i]);
        }
    }
    private static void dictionaryCreation(){
        int fileLength=0;
        try {
            dictionaryCounterList.add(new String[]{" - ", " - ", "0"});
            fileUtility();
            while ((data = bufferedReader.readLine()) != null) {
                fileLength += data.length();
                for(int j=0;j<data.length();j++)
                {
                    searchAndSave(data.charAt(j),dictionaryCounterList);
                }
            }
            probabilityCount(dictionaryCounterList, fileLength,1);
            cumulativeDistributionCount(dictionaryCounterList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("4wyrazy.txt probability list: ");
        probabilityDisplay(dictionaryCounterList);
    }
    private static void fileUtility(){
        try {
            FileInputStream fileStream = new FileInputStream(file);
            fileStream.getChannel().position(0);
            InputStreamReader input = new InputStreamReader(fileStream);
            bufferedReader = new BufferedReader(input);
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    private static void searchAndSave(char letter, List<String[]> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i)[0].charAt(0) == letter) {
                int counter = Integer.parseInt(list.get(i)[1]) + 1;
                list.set(i, new String[]{Character.toString(letter), Integer.toString(counter)});
                return;
            }
        }
        list.add(new String[]{Character.toString(letter), "1"});
    }
    private static void cumulativeDistributionCount(List<String[]> list){
        double cumulativeDistribution=0;

        for(int i=1;i< list.size();i++){
            cumulativeDistribution = cumulativeDistribution + Double.parseDouble(list.get(i)[1]) ;
            list.set(i, new String[]{list.get(i)[0], list.get(i)[1], Double.toString(cumulativeDistribution)});
        }
    }
    private static void probabilityDisplay(List<String[]> list){
        for (String[] strings : list) {

            System.out.println(strings[0] + '\t' + " | " + strings[1] + '\t');
        }
    }
    private static void fixedProbabilityWordsGenerator(){
        for(int i=0;i<200;i++)
        {
            for(int j=0;j<4;j++)
            {
                double randomLetter = rand.nextInt(999) +1;
                randomLetter/= 1000;
                for (int k = dictionaryCounterList.size()-1; k > 0; k--) {

                    if (Double.parseDouble(dictionaryCounterList.get(k)[2]) >= randomLetter && Double.parseDouble(dictionaryCounterList.get(k - 1)[2]) <= randomLetter) {
                        if(j==0)
                        {
                            wordsTable[i] = dictionaryCounterList.get(k)[0];
                        }
                        else {wordsTable[i]= wordsTable[i] + dictionaryCounterList.get(k)[0];}
                        searchAndSave(dictionaryCounterList.get(k)[0].charAt(0),symbolCounterList);
                        break;
                    }
                }

            }
        }
    }
    public static void main(String[] args) {
        sourceChoice();
        wordsDisplay();
    }
}
