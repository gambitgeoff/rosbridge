package au.gov.defence.rosbridge.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import au.gov.defence.rosbridge.Topic;

public class ROSTopicsViewModel extends ViewModel {

    private static final String TAG = "au.gov.defence.dca.ros.ROSTopicsViewModel";
    private static final MutableLiveData<List<Topic>> mTopicsList = new MutableLiveData<>();

    public ROSTopicsViewModel() {
    }

    public MutableLiveData<List<Topic>> getTopicsList() {
        return mTopicsList;
    }

    public void setTopicsList(List<Topic> inTopicsList) {
        if (mTopicsList.hasObservers()) {
            Log.v(TAG, "topicslist has observers: updating");
        }
        mTopicsList.postValue(inTopicsList);
        Log.v(TAG, "Posted updated topics");
    }
}
