package com.androiddev.moneytracker

import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.androiddev.moneytracker.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {
    val messagesList = ArrayList<Messages>()
    val creditMessages = mutableListOf<Messages>()
    val debitMessages = mutableListOf<Messages>()
    lateinit var binding: ActivityMainBinding

    private val SMS_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreditms.setOnClickListener {
            if (checkSmsPermission()) {
                fetchAllSms()
                val adapter = MessageAdapter(creditMessages)
                binding.recyclerview.layoutManager = LinearLayoutManager(this)
                binding.recyclerview.adapter = adapter
            } else {
                requestSmsPermission()
            }
        }
        binding.btnDebitmsg.setOnClickListener {
            if (checkSmsPermission()) {
                fetchAllSms()
                val adapter = MessageAdapter(debitMessages)
                binding.recyclerview.layoutManager = LinearLayoutManager(this)
                binding.recyclerview.adapter = adapter
            } else {
                requestSmsPermission()
            }
        }


    }

    private fun checkSmsPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.READ_SMS
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestSmsPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.READ_SMS),
            SMS_PERMISSION_CODE
        )
    }

    private fun fetchAllSms() {
        val uri = Telephony.Sms.CONTENT_URI
        val projection = arrayOf(Telephony.Sms.ADDRESS, Telephony.Sms.BODY, Telephony.Sms.DATE)
        val cursor: Cursor? =
            contentResolver.query(uri, projection, null, null, Telephony.Sms.DEFAULT_SORT_ORDER)

        cursor?.use {
            if (cursor.moveToFirst()) {
                do {
                    val phoneNo =
                        cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.ADDRESS))
                    val body = cursor.getString(cursor.getColumnIndexOrThrow(Telephony.Sms.BODY))
                    val datedata = cursor.getLong(cursor.getColumnIndexOrThrow(Telephony.Sms.DATE))
                    val date = Date(datedata)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val formattedDate = dateFormat.format(date)
                    val msg = Messages(phoneNo, formattedDate, body)
                    when {
                        body.contains("credited", ignoreCase = true) -> creditMessages.add(msg)
                        body.contains("debited", ignoreCase = true) || body.contains("Sent", ignoreCase = true) -> debitMessages.add(msg)
                    }
                } while (cursor.moveToNext())
            }
        }

        displayMessages("Credit", creditMessages)
        displayMessages("Debit", debitMessages)
    }

    private fun displayMessages(s: String, debitMessages: List<Messages>) {
        Log.d("SMS", "---- $s Messages ----")
        debitMessages.forEach { message ->
            Log.d("SMS", "Phone No: ${message.number}, Date: ${message.date}, Body: ${message.sms}")
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                fetchAllSms()
            } else {
                Toast.makeText(this, "Permisson Denied for Access Contact", Toast.LENGTH_SHORT)
                    .show()
                Log.d("SMS", "Permission denied")
            }
        }
    }
}