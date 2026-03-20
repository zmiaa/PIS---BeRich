package edu.ub.pis2526.berich.presentation.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import edu.ub.pis2526.berich.databinding.ActivityEmotionCalendarBinding;
import edu.ub.pis2526.berich.presentation.decorators.EmotionDecorator;
import edu.ub.pis2526.berich.presentation.viewmodels.EmotionViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EmotionCalendarActivity extends AppCompatActivity {
    private ActivityEmotionCalendarBinding binding;
    private EmotionViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmotionCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(EmotionViewModel.class);

        setupCalendar();
        setupListeners();

        // OBSERVADOR: La magia de MVVM
        viewModel.getEmotions().observe(this, this::updateDecorators);
    }

    private void setupCalendar() {
        binding.calendarView.setSelectedDate(CalendarDay.today());
        binding.calendarView.setOnDateChangedListener((widget, date, selected) -> {
            Map<CalendarDay, String> data = viewModel.getEmotions().getValue();
            if (data != null && data.containsKey(date)) {
                Toast.makeText(this, "Emoción: " + data.get(date), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupListeners() {
        binding.btnHappy.setOnClickListener(v -> viewModel.saveEmotion(CalendarDay.today(), "😊"));
        binding.btnNeutral.setOnClickListener(v -> viewModel.saveEmotion(CalendarDay.today(), "😐"));
        binding.btnSad.setOnClickListener(v -> viewModel.saveEmotion(CalendarDay.today(), "☹️"));
        binding.btnBack.setOnClickListener(v -> finish());
    }

    private void updateDecorators(Map<CalendarDay, String> emotions) {
        binding.calendarView.removeDecorators();
        List<CalendarDay> happy = new ArrayList<>(), neutral = new ArrayList<>(), sad = new ArrayList<>();

        for (Map.Entry<CalendarDay, String> entry : emotions.entrySet()) {
            if (entry.getValue().equals("😊")) happy.add(entry.getKey());
            else if (entry.getValue().equals("😐")) neutral.add(entry.getKey());
            else if (entry.getValue().equals("☹️")) sad.add(entry.getKey());
        }

        binding.calendarView.addDecorator(new EmotionDecorator(happy, Color.GREEN));
        binding.calendarView.addDecorator(new EmotionDecorator(neutral, Color.YELLOW));
        binding.calendarView.addDecorator(new EmotionDecorator(sad, Color.RED));
    }
}