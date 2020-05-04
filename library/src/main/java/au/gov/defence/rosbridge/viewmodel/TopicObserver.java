package au.gov.defence.rosbridge.viewmodel;

import au.gov.defence.rosbridge.Topic;

public interface TopicObserver {

        public abstract void topicUpdated(Topic inTopic);
}
