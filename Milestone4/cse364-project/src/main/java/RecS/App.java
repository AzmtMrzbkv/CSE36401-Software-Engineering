package RecS;

// this class is the controller

import RecS.Models.*;


import RecS.MongoReps.MovieRepository;
import RecS.MongoReps.RatingRepository;
import RecS.MongoReps.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.*;

import static RecS.Utils.CsvReader.*;
import static RecS.Utils.Recommender.*;

@RestController
@EnableMongoRepositories
public class App {
    private final MovieRepository movieRepository;
    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;

    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @Autowired
    public App(MovieRepository movieRepository, RatingRepository ratingRepository, UserRepository userRepository) throws IOException {
        this.movieRepository = movieRepository;
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;

        LOG.info("\n*************** Loading csv files to Mongo DB ... ***************");
        this.movieRepository.saveAll(readMoviesCsv());
        LOG.info("\n*************** Loading movies to Movies Mongo DB: Success ***************");
        this.ratingRepository.saveAll(readRatingsCsv());
        LOG.info("\n*************** Loading ratings to Ratings Mongo DB: Success ***************");
        this.userRepository.saveAll(readUsersCsv());
        LOG.info("\n*************** Loading users to Users Mongo DB: Success ***************");
    }

    @GetMapping("/movies")
    public List<Movies> listAllMovies(){
        //return all movies
        LOG.info("\n*************** Returning all movies ***************");
        return movieRepository.findAll();
    }

    @GetMapping("/user/reccomendation")
    public List<Movies> recommendByUser(@RequestParam("gender") String gender, @RequestParam("age") String age, @RequestParam("occupation") String occupation, @RequestParam("genres") String genre){
        List<Users> userList = userRepository.findAll();
        List<Ratings> ratingList = ratingRepository.findAll();
        List<Movies> movieList = movieRepository.findAll();
        UserRec user = new UserRec(gender, age, occupation, genre);

        if (!user.getGenre().equals("")) return getMovies(limitedTop(promoteFavGenre(gradeMovies(user, userList, ratingList), user.getGenre(), movieList), 10));
        return getMovies(limitedTop(gradeMovies(user, userList, ratingList), 10));
    }

    // Returns a List of Movies given List of movieIDs
    public List<Movies> getMovies(List<String> movieIDs){
        List<Movies> movies = new ArrayList<>();
        for(String movieID: movieIDs){
            Optional<Movies> movie = movieRepository.findById(movieID);
            movie.ifPresent(movies::add);
        }
        return movies;
    }
}
