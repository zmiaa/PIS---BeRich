package edu.ub.pis2526.berich.presentation;

import android.graphics.Color;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

public class EmotionDecorator implements DayViewDecorator {

    private HashSet<CalendarDay> dates;
    private String emoji;
    private int color;

    public EmotionDecorator(Collection<CalendarDay> dates, String emoji) {
        this.dates = new HashSet<>(dates);
        this.emoji = emoji;

        // Asignar color según emoji
        switch (emoji) {
            case "😊":
                this.color = Color.GREEN;
                break;
            case "😐":
                this.color = Color.YELLOW;
                break;
            case "☹️":
                this.color = Color.RED;
                break;
            default:
                this.color = Color.GRAY;
        }
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        // Añadir un punto de color para indicar que hay emoción
        view.addSpan(new DotSpan(8, color));
    }
}