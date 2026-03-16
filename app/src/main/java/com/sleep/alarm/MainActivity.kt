package com.sleep.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.sleep.alarm.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var wakeHour = -1
    private var wakeMinute = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPickTime.setOnClickListener {
            showTimePicker()
        }

        binding.btnSetAlarm.setOnClickListener {
            if (wakeHour == -1) {
                binding.tvStatus.text = "Please select a wake-up time first."
                return@setOnClickListener
            }
            setAlarm()
        }
    }

    private fun showTimePicker() {
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(7)
            .setMinute(0)
            .setTitleText("Select Wake-Up Time")
            .build()

        picker.addOnPositiveButtonClickListener {
            wakeHour = picker.hour
            wakeMinute = picker.minute

            val alarmHour = (wakeHour - 9 + 24) % 24

            val wakeAmPm = if (wakeHour < 12) "AM" else "PM"
            val displayWakeHour = if (wakeHour % 12 == 0) 12 else wakeHour % 12

            val alarmAmPm = if (alarmHour < 12) "AM" else "PM"
            val displayAlarmHour = if (alarmHour % 12 == 0) 12 else alarmHour % 12

            val minuteStr = String.format("%02d", wakeMinute)

            binding.tvWakeTime.text = "⏰ Wake-Up Time: $displayWakeHour:$minuteStr $wakeAmPm"
            binding.tvAlarmTime.text = "😴 Sleep Alarm At: $displayAlarmHour:$minuteStr $alarmAmPm"
            binding.tvStatus.text = "Tap 'Set Alarm' to confirm."
        }

        picker.show(supportFragmentManager, "TIME_PICKER")
    }

    private fun setAlarm() {
        val alarmHour = (wakeHour - 9 + 24) % 24

        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_HOUR, alarmHour)
            putExtra(AlarmClock.EXTRA_MINUTES, wakeMinute)
            putExtra(AlarmClock.EXTRA_MESSAGE, "Time to sleep! Wake up in 9 hours.")
            putExtra(AlarmClock.EXTRA_SKIP_UI, false)
        }

        startActivity(intent)
        binding.tvStatus.text = "✅ Alarm set! Opening your clock app..."
    }
}
