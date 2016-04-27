package br.com.cemobile.twittertimeline.activity;

import android.app.ListActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import br.com.cemobile.twittertimeline.Constant;
import br.com.cemobile.twittertimeline.R;
import br.com.cemobile.twittertimeline.facade.TwitterFacade;

@EActivity(R.layout.activity_timeline)
public class TimelineActivity extends ListActivity {

    @ViewById(R.id.swipeLayout)
    protected SwipeRefreshLayout swipeLayout;

    @ViewById(R.id.hashtagEditText)
    protected EditText hashtagEditText;

    @ViewById(android.R.id.list)
    protected ListView listView;

    @AfterViews
    protected void afterViews() {
        Editable text = hashtagEditText.getText();
        Selection.setSelection(text, text.length());

        final TweetTimelineListAdapter adapter = TwitterFacade.retrieveTimeLineByHashtag(this, Constant.HASHTAG_ANDROID);

        setListAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            boolean enableRefresh = false;

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {}

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listView != null && listView.getChildCount() > 0) {
                    // check that the first item is visible and that its top matches the parent
                    enableRefresh = listView.getFirstVisiblePosition() == 0 &&
                            listView.getChildAt(0).getTop() >= 0;
                } else {
                    enableRefresh = false;
                }
                swipeLayout.setEnabled(enableRefresh);
            }
        });

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeLayout.setRefreshing(true);
                adapter.refresh(new Callback<TimelineResult<Tweet>>() {
                    @Override
                    public void success(Result<TimelineResult<Tweet>> result) {
                        swipeLayout.setRefreshing(false);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Log.e(Constant.TAG, "Error during refresh!" + e.getMessage());
                        Toast.makeText(TimelineActivity.this, Constant.REFRESH_ERROR + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

}
