import java.io.File;
import java.util.*;
import java.util.stream.*;

import org.apache.commons.collections.ListUtils;
import org.apache.mahout.cf.taste.common.Refreshable;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.*;
import org.apache.mahout.common.LongPair;
import org.graalvm.compiler.code.DataSection;

public class src{
    private static FileDataModel dm_movies;
    private static FileDataModel dm_anime;
    private static FileDataModel dm_shows;
    private static FileDataModel dm_games;


    public static void main(String[] args) {
        try{
            dm_movies = new FileDataModel(new File(/*internal link to movies db file*/));
            DataModel dm_anime = new FileDataModel(new File(/*internal link to anime db file)*/);
            DataModel dm_shows = new FileDataModel(new File(/*internal link to shows db file)*/);
        }catch(Exception e){
            System.out.println("Some error occurred.");
        }
    }
    private List<RecommendedItem> giveRecommendation(DataModel dm, int UserID, int number){
        List<RecommendedItem> results = null;
        CityBlockSimilarity similarity = new CityBlockSimilarity(dm);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, dm);
        UserBasedRecommender r = new GenericUserBasedRecommender(dm, neighborhood, similarity);
        try {
            results = r.recommend(UserID, number);
        } catch (TasteException e) {
            e.printStackTrace();
        }
        return results;

    }
    public List<RecommendedItem> finalRecommendations(int UserID, int number){
        List<RecommendedItem> results = null;
        List<RecommendedItem> anime_r = giveRecommendation(dm_anime, UserID, number);
        List<RecommendedItem> movies_r = giveRecommendation(dm_movies, UserID, number);
        List<RecommendedItem> shows_r = giveRecommendation(dm_shows, UserID, number);
        List<RecommendedItem> games_r = giveRecommendation(dm_games, UserID, number);
        results = concatenate(anime_r,movies_r,games_r,shows_r);
        return results;
    }
    public static List<RecommendedItem> concatenate(List<RecommendedItem>... lists)
    {
        List<RecommendedItem> result = new ArrayList<RecommendedItem>();

        for (List<RecommendedItem> l: lists)
            result = ListUtils.union(result, l);

        return result;
    }


}