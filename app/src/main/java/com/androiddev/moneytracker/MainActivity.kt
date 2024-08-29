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
    lateinit var binding: ActivityMainBinding

    private val SMS_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnFetch.setOnClickListener {
            if (checkSmsPermission()) {
                fetchAllSms()
                val adapter = MessageAdapter(messagesList)
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
                    messagesList.add(msg)
                } while (cursor.moveToNext())
            }
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