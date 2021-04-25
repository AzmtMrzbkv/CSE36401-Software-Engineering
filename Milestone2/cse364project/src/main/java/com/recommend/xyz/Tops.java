package com.recommend.xyz;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class Tops {
    public static void main(String[] args) throws Exception {

        BufferedReader movies = new BufferedReader(new FileReader("../data/movies.dat"));
        BufferedReader ratings = new BufferedReader(new FileReader("../data/ratings.dat"));
        BufferedReader users = new BufferedReader(new FileReader("../data/users.dat"));
        BufferedReader links = new BufferedReader(new FileReader("../data/links.dat"));

        if (args.length == 3) recTop10(args, movies, ratings, users, links);
        else recTop10WithCat(args, movies, ratings, users, links);
    }


    private static void recTop10(String[] args, BufferedReader movies, BufferedReader ratings, BufferedReader users, BufferedReader links) {

    }

    private static void recTop10WithCat(String[] args, BufferedReader movies, BufferedReader ratings, BufferedReader users, BufferedReader links) {

    }

    //Below are methods from previous Milestone. We can for sure reuse them fully or partly.
    private static Set<String> getUserIDFromOccupation(String occupation, FileReader file) throws IOException {
        if(occupation.equals("-1")) return new HashSet<>();

        // UserID::Gender::Age::Occupation::Zip-code    in users.dat
        BufferedReader read = new BufferedReader(file);
        occupation = occupation.toLowerCase(Locale.ROOT);

        Set<String> userIDs = new HashSet<>();

        String line = read.readLine();
        while (line != null) {
            String[] arrOfStr = line.toLowerCase(Locale.ROOT).split("::");
            if (occupation.equals(arrOfStr[3])) userIDs.add(arrOfStr[0]);
            line = read.readLine();
        }

        return userIDs;
    }

    private static Set<String> getMovieIDFromGenres(String genreString, FileReader file) throws IOException {
        BufferedReader read = new BufferedReader(file);
        String[] genres = genreString.toLowerCase(Locale.ROOT).split("\\|");
        // this set will be returned
        Set<String> movieIDs = new HashSet<>();

        String line = read.readLine();
        // every line in .dat file => MovieID::Title::Genres
        while (line != null) {
            String[] lineArray = line.toLowerCase(Locale.ROOT).split("::");
            Set<String> genreSet = new HashSet<>(Arrays.asList(lineArray[2].split("\\|")));
            boolean correctMov = true;

            for (String genre : genres) {
                if (!genreSet.contains(genre)) {
                    correctMov = false;
                    break;
                }
            }

            if (correctMov) movieIDs.add(lineArray[0]);

            line = read.readLine();
        }

        if(movieIDs.isEmpty()){
            System.out.println("No movies with such genre(s) found!");
            System.exit(0);
        }

        return movieIDs;
    }

    private static String parseStringOccupation(String occupation) {
        String occupationIndex = "";
        switch (occupation) {
//            case "other":
//                //0:  "other" or not specified??
//                occupationIndex = "0";
//                break;
            case "academic":
            case "educator":
                occupationIndex = "1";
                break;
            case "artist":
                occupationIndex = "2";
                break;
            case "clerical":
            case "admin":
                occupationIndex = "3";
                break;
            case "college":
            case "grad student":
            case "gradstudent":
                occupationIndex = "4";
                break;
            case "customer service":
            case "customerservice":
                occupationIndex = "5";
                break;
            case "doctor":
            case "health care":
            case "healthcare":
                occupationIndex = "6";
                break;
            case "executive":
            case "managerial":
                occupationIndex = "7";
                break;
            case "farmer":
                occupationIndex = "8";
                break;
            case "homemaker":
                occupationIndex = "9";
                break;
            case "K-12 studio":
            case "K-12studio":
                occupationIndex = "10";
                break;
            case "lawyer":
                occupationIndex = "11";
                break;
            case "programmer":
                occupationIndex = "12";
                break;
            case "retired":
                occupationIndex = "13";
                break;
            case "sales":
            case "marketing":
                occupationIndex = "14";
                break;
            case "scientist":
                occupationIndex = "15";
                break;
            case "self-employed":
                occupationIndex = "16";
                break;
            case "technician":
            case "engineer":
                occupationIndex = "17";
                break;
            case "tradesman":
            case "craftsman":
                occupationIndex = "18";
                break;
            case "unemployed":
                occupationIndex = "19";
                break;
            case "writing":
                occupationIndex = "20";
                break;
            default:
                if(occupation.equals("")) occupation = "\"\"";
                System.out.println("No such occupation: "+ occupation);
                occupationIndex = "-1";
        }
        return occupationIndex;
    }
}
