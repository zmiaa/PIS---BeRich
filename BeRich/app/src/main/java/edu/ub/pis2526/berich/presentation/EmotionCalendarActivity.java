package edu.ub.pis2526.berich.presentation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import edu.ub.pis2526.berich.R;
import edu.ub.pis2526.berich.databinding.ActivityEmotionCalendarBinding;

public class EmotionCalendarActivity extends AppCompatActivity {

    private ActivityEmotionCalendarBinding binding;
    private Button btnHappy, btnNeutral, btnSad;
    private MaterialCalendarView calendarView;

    private SharedPreferences emotionPrefs;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat monthFormat;

    // Mapa para guardar emociones por fecha
    private HashMap<CalendarDay, String> emotionMap;

    // Decoradores para cada tipo de emoción
    private EmotionDecorator happyDecorator;
    private EmotionDecorator neutralDecorator;
    private EmotionDecorator sadDecorator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmotionCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar vistas
        btnHappy = findViewById(R.id.btnHappy);
        btnNeutral = findViewById(R.id.btnNeutral);
        btnSad = findViewById(R.id.btnSad);
        calendarView = findViewById(R.id.calendarView);

        // Inicializar formatos y SharedPreferences
        emotionPrefs = getSharedPreferences("EmotionPrefs", MODE_PRIVATE);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());

        // Inicializar mapa de emociones
        emotionMap = new HashMap<>();

        // Configurar botón de retroceso
        binding.btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(EmotionCalendarActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        // Configurar calendario
        setupCalendar();

        // Cargar todas las emociones guardadas
        loadAllEmotions();

        // Configurar botones de emoji
        setupEmojiListeners();
    }

    private void setupCalendar() {
        // Configurar título del mes
        calendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                Calendar cal = day.getCalendar();
                return monthFormat.format(cal.getTime());
            }
        });

        // Seleccionar día actual
        calendarView.setSelectedDate(CalendarDay.today());

        // Listener para cuando seleccionan un día
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(MaterialCalendarView widget, CalendarDay date, boolean selected) {
                String emotion = emotionMap.get(date);
                if (emotion != null) {
                    Toast.makeText(EmotionCalendarActivity.this,
                            "Dia " + date.getDay() + ": " + emotion, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EmotionCalendarActivity.this,
                            "Sense emoció", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupEmojiListeners() {
        btnHappy.setOnClickListener(v -> {
            saveEmotionForToday("😊");
            refreshCalendar();
            Toast.makeText(this, "😊 Emoció guardada", Toast.LENGTH_SHORT).show();
        });

        btnNeutral.setOnClickListener(v -> {
            saveEmotionForToday("😐");
            refreshCalendar();
            Toast.makeText(this, "😐 Emoció guardada", Toast.LENGTH_SHORT).show();
        });

        btnSad.setOnClickListener(v -> {
            saveEmotionForToday("☹️");
            refreshCalendar();
            Toast.makeText(this, "☹️ Emoció guardada", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveEmotionForToday(String emoji) {
        // Guardar en SharedPreferences
        String today = dateFormat.format(new Date());
        SharedPreferences.Editor editor = emotionPrefs.edit();
        editor.putString(today, emoji);
        editor.apply();

        // Actualizar mapa
        emotionMap.put(CalendarDay.today(), emoji);
    }

    private void loadAllEmotions() {
        // Obtener todas las claves (fechas) de SharedPreferences
        // Como no podemos iterar fácilmente, cargamos solo el mes actual y anterior

        CalendarDay today = CalendarDay.today();
        Calendar cal = Calendar.getInstance();

        // Cargar emociones de los últimos 30 días (ejemplo)
        for (int i = -15; i <= 15; i++) {
            cal.set(today.getYear(), today.getMonth() - 1, today.getDay() + i);
            String dateKey = dateFormat.format(cal.getTime());
            String emotion = emotionPrefs.getString(dateKey, "");

            if (!emotion.isEmpty()) {
                CalendarDay day = CalendarDay.from(cal.getTime());
                emotionMap.put(day, emotion);
            }
        }

        // Aplicar decoradores
        applyDecorators();
    }

    private void applyDecorators() {
        // Limpiar decoradores anteriores
        calendarView.removeDecorators();

        // Separar fechas por tipo de emoción
        List<CalendarDay> happyDays = new ArrayList<>();
        List<CalendarDay> neutralDays = new ArrayList<>();
        List<CalendarDay> sadDays = new ArrayList<>();

        for (HashMap.Entry<CalendarDay, String> entry : emotionMap.entrySet()) {
            switch (entry.getValue()) {
                case "😊":
                    happyDays.add(entry.getKey());
                    break;
                case "😐":
                    neutralDays.add(entry.getKey());
                    break;
                case "☹️":
                    sadDays.add(entry.getKey());
                    break;
            }
        }

        // Crear y añadir decoradores
        if (!happyDays.isEmpty()) {
            happyDecorator = new EmotionDecorator(happyDays, "😊");
            calendarView.addDecorator(happyDecorator);
        }

        if (!neutralDays.isEmpty()) {
            neutralDecorator = new EmotionDecorator(neutralDays, "😐");
            calendarView.addDecorator(neutralDecorator);
        }

        if (!sadDays.isEmpty()) {
            sadDecorator = new EmotionDecorator(sadDays, "☹️");
            calendarView.addDecorator(sadDecorator);
        }
    }

    private void refreshCalendar() {
        // Limpiar y volver a aplicar decoradores
        applyDecorators();
        calendarView.invalidate(); // Redibujar
    }
}