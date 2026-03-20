package edu.ub.pis2526.berich.presentation.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EmotionViewModel extends AndroidViewModel {
    private final MutableLiveData<Map<CalendarDay, String>> emotionMap = new MutableLiveData<>(new HashMap<>());
    private final SharedPreferences prefs;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public EmotionViewModel(@NonNull Application application) {
        super(application);
        prefs = application.getSharedPreferences("EmotionPrefs", Context.MODE_PRIVATE);
        loadEmotionsFromPrefs();
    }

    public LiveData<Map<CalendarDay, String>> getEmotions() {
        return emotionMap;
    }

    public void saveEmotion(CalendarDay day, String emoji) {
        String dateKey = dateFormat.format(day.getDate());
        prefs.edit().putString(dateKey, emoji).apply();

        Map<CalendarDay, String> current = emotionMap.getValue();
        if (current != null) {
            current.put(day, emoji);
            emotionMap.setValue(current);
        }
    }

    private void loadEmotionsFromPrefs() {
        Map<CalendarDay, String> loaded = new HashMap<>();
        Calendar cal = Calendar.getInstance();
        for (int i = -30; i <= 30; i++) {
            cal.setTime(new java.util.Date());
            cal.add(Calendar.DAY_OF_YEAR, i);
            String key = dateFormat.format(cal.getTime());
            String emotion = prefs.getString(key, "");
            if (!emotion.isEmpty()) {
                loaded.put(CalendarDay.from(cal.getTime()), emotion);
            }
        }
        emotionMap.setValue(loaded);
    }
}