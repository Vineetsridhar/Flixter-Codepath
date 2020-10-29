package com.example.flixter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixter.Models.Movie;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.parceler.Parcels;

import okhttp3.Headers;

public class DetailActivity extends YouTubeBaseActivity {

    TextView tvTitle;
    RatingBar ratingBar;
    TextView tvOverview;
    YouTubePlayerView youTubePlayerView;

    public static final String videos_url = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    public void initializeVariables(){
        tvTitle = findViewById(R.id.tvTitle);
        ratingBar = findViewById(R.id.ratingsBar);
        tvOverview = findViewById(R.id.tvOverview);
        youTubePlayerView = findViewById(R.id.player);
    }

    public void setData(Movie movie){
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        ratingBar.setRating((float) movie.getRating());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initializeVariables();

        Intent i = getIntent();
        final Movie movie = Parcels.unwrap(i.getParcelableExtra("movie"));
        setData(movie);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(String.format(videos_url, movie.getMovieId()), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONArray results = json.jsonObject.getJSONArray("results");
                    if(results.length() == 0) return;
                    String youtubeKey = results.getJSONObject(0).getString("key");
                    initializeYoutube(youtubeKey, movie.getRating());
                } catch (JSONException e) {
                    Log.e("DetailActivity", "Error parsing JSON");
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("DetailActivity", "Fail to create youtube");
            }
        });



    }

    public void initializeYoutube(final String youtubeId, final double rating){
        youTubePlayerView.initialize(getString(R.string.youtube_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                if(rating >= 5)
                    youTubePlayer.loadVideo(youtubeId);
                else
                    youTubePlayer.cueVideo(youtubeId);

            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }
}