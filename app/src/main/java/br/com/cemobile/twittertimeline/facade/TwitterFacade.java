package br.com.cemobile.twittertimeline.facade;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TwitterListTimeline;
import com.twitter.sdk.android.tweetui.UserTimeline;

import br.com.cemobile.twittertimeline.Constant;
import io.fabric.sdk.android.Fabric;

/**
 * Created by celso on 24/04/16.
 */
public class TwitterFacade {

    private static final String TWITTER_API_KEY = "8ULfo0w6PRwUqir6hmb5RhESr";
    private static final String TWITTER_API_SECRET = "7ITPMzQWdltKFFL2Vm6svoLA4sPaDtQiP3m42tWuFOm2Mya2ia";

    public static void initFabric(Context context) {
        Log.d(Constant.TAG, "Initializing Fabric... ");

        if (!Fabric.isInitialized()) {
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_API_KEY, TWITTER_API_SECRET);
            Fabric.with(context, new Twitter(authConfig));
        }
    }

    public static TweetTimelineListAdapter retrieveTimeLineByHashtag(@NonNull Context context, @NonNull String hashtag){
        Log.d(Constant.TAG, "Loading tweets with hashtag " + hashtag);

        SearchTimeline searchTimeline = new SearchTimeline.Builder().
                query(hashtag).
                build();

        return new TweetTimelineListAdapter.Builder(context).
                setTimeline(searchTimeline).
                build();
    }

    public static TweetTimelineListAdapter retrieveTimeLineByAccount(@NonNull Context context, @NonNull String account){
        Log.d(Constant.TAG, "Loading tweets from user " + account);

        UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName(account)
                .build();

        return new TweetTimelineListAdapter.Builder(context)
                .setTimeline(userTimeline)
                .build();
    }

    public static TweetTimelineListAdapter retrieveTimeLineBySlugWithOwnerScreenName(@NonNull Context context, @NonNull String slug, @NonNull String ownerName) {
        Log.d(Constant.TAG, "Loading tweets with slug " + slug + " and owner name " + ownerName);

        TwitterListTimeline listTimeLine = new TwitterListTimeline.Builder()
                .slugWithOwnerScreenName(slug, ownerName)
                .includeRetweets(true)
                .build();

        return new TweetTimelineListAdapter.Builder(context)
                .setTimeline(listTimeLine)
                .build();
    }

}
