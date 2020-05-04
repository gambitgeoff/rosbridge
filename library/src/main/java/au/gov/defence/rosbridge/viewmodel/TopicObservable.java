package au.gov.defence.rosbridge.viewmodel;

import java.util.Vector;

import au.gov.defence.rosbridge.Topic;

public class TopicObservable {
    private Vector<TopicObserver> mObservers;

    public TopicObservable()
    {
        mObservers = new Vector<>();
    }

    protected void addTopicObserver(TopicObserver inObserver)
    {
        if(!mObservers.contains(inObserver))
            mObservers.add(inObserver);
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
