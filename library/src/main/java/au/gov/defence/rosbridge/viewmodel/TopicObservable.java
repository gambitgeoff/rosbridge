package au.gov.defence.rosbridge.viewmodel;

import android.util.Log;

import java.util.Vector;

import au.gov.defence.rosbridge.Topic;

public class TopicObservable {
    private static final String TAG = "au.gov.defence.rosbridge.TopicObservable";
    protected Vector<TopicObserver> mObservers;

    public TopicObservable()
    {
        Log.v(TAG, "TopicObservable()");
        mObservers = new Vector<>();
    }

    protected void addTopicObserver(TopicObserver inObserver)
    {
        if(!mObservers.contains(inObserver)) {
            mObservers.add(inObserver);
            Log.v(TAG,"Added Topic Observer: " + inObserver.toString());
        }
    }

    protected void removeTopicObserver(TopicObserver inTopicObserver)
    {
        if(mObservers.contains(inTopicObserver))
            mObservers.remove(inTopicObserver);
    }

    protected void updateTopicObservers(Topic inTopic)
    {
        for(TopicObserver t: mObservers)
            t.topicUpdated(inTopic);
    }
}
