package RecS;

// this class will be the controller

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class App {
    //here will be functions mapped to GET

    @GetMapping("/users/recommendations")
    public List<Movies> recommendForUser(@RequestBody Users newUser) throws IOException {
        // this part to be implemented
        String age = newUser.getAge();
        String genre = newUser.getGenres();
        String gender = newUser.getGender();
        String occupation = newUser.getOccupation();

        // check the input validity
        if (!Recommender.isValidInput(gender, age, occupation, genre)) {
            return null;
        }

        return Recommender.limitedTop(Recommender.gradeMovies(new String[]{gender, age, occupation, genre}), 10);
    }

    @GetMapping("/movies/recommendations")
    public List<Movies> recommendByMovie(@RequestBody LimitedRec newRequest) throws IOException {
        // this part to be implemented
        Users newUser = Recommender.posFanFromMovieID(Recommender.getIdByTitle(newRequest.getTitle()));
        return Recommender.limitedTop(Recommender.gradeMovies(new String[]{newUser.getGender(), newUser.getAge(), newUser.getOccupation(), newUser.getGenres()}), (newRequest.getLimit() == null) ? 10 :Integer.parseInt(newRequest.getLimit()));
    }

}
