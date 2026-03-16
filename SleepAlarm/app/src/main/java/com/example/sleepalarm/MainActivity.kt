package com.example.sleepalarm

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private var wakeUpHour = -1
    private var wakeUpMinute = -1
    private var alarmHour = -1
    private var alarmMinute = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPickTime = findViewById<Button>(R.id.btnPickTime)
        val btnSetAlarm = findViewById<Button>(R.id.btnSetAlarm)
        val tvWakeUpTime = findViewById<TextView>(R.id.tvWakeUpTime)
        val tvAlarmTime = findViewById<TextView>(R.id.tvAlarmTime)

        btnPickTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
            val currentMinute = calendar.get(Calendar.MINUTE)

            val timePicker = TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    wakeUpHour = hourOfDay
                    wakeUpMinute = minute

                    // Calculate alarm time (9 hours earlier)
                    var calcHour = hourOfDay - 9
                    if (calcHour < 0) calcHour += 24
                    alarmHour = calcHour
                    alarmMinute = minute

                    tvWakeUpTime.text = "Wake-Up Time: ${formatTime(wakeUpHour, wakeUpMinute)}"
                    tvAlarmTime.text = "Set Alarm At: ${formatTime(alarmHour, alarmMinute)}"
                    btnSetAlarm.isEnabled = true
                },
                currentHour,
                currentMinute,
                false
            )
            timePicker.setTitle("Select Wake-Up Time")
            timePicker.show()
        }

        btnSetAlarm.setOnClickListener {
            if (alarmHour >= 0) {
                val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
                    putExtra(AlarmClock.EXTRA_HOUR, alarmHour)
                    putExtra(AlarmClock.EXTRA_MINUTES, alarmMinute)
                    putExtra(AlarmClock.EXTRA_MESSAGE, "Sleep Alarm - Wake up at ${formatTime(wakeUpHour, wakeUpMinute)}")
                    putExtra(AlarmClock.EXTRA_SKIP_UI, false)
                }
                startActivity(intent)
            }
        }
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val amPm = if (hour < 12) "AM" else "PM"
        val displayHour = when {
            hour == 0 -> 12
            hour > 12 -> hour - 12
            else -> hour
        }
        return String.format("%d:%02d %s", displayHour, minute, amPm)
    }
}
