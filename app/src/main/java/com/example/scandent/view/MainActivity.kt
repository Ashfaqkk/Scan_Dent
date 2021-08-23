package com.example.scandent.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scandent.R
import com.honeywell.aidc.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), BarcodeReader.BarcodeListener,
    BarcodeReader.TriggerListener {

    private lateinit var todoAdapter: SaleorderAdapter
    private val sharedPrefFile = "DentcareScanner"
    private var barcodeReader: BarcodeReader? = null
    private var manager: AidcManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(
            sharedPrefFile,
            Context.MODE_PRIVATE
        )


        /*val sharedIdValue = sharedPreferences.getInt("id_key", 0)
        val sharedNameValue = sharedPreferences.getString("name_key", "defaultname")
        if (sharedIdValue.equals(0) && sharedNameValue.equals("defaultname")) {
            Toast.makeText(
                applicationContext,
                "Something Went Wrong",
                Toast.LENGTH_SHORT
            ).show()
        } else {

            Log.d("TAG", sharedNameValue.toString())
            Log.d("TAG", sharedIdValue.toString())

        }*/


        /*val actionBar = supportActionBar
        actionBar!!.title = "New Title"*/


        todoAdapter = SaleorderAdapter(mutableListOf())

        rvTodoItems.adapter = todoAdapter
        rvTodoItems.layoutManager = LinearLayoutManager(this)



        logout.setOnClickListener {
            intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }




        btnAddTodo.setOnClickListener {
            /* val todoTitle = etTodoTitle.text.toString()
             if(todoTitle.isNotEmpty()) {
                 val todo = Saleorder(todoTitle.trim())
                 todoAdapter.addTodo(todo)
                 etTodoTitle.text.clear()
             }*/
            getScannedEntry()
        }
        btnDeleteDoneTodos.setOnClickListener {
            todoAdapter.deleteDoneTodos()
        }

        activateBarcode()
        etTodoTitle.requestFocus()
        etTodoTitle.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                getScannedEntry()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        /*val handler = Handler()
        val runnable: Runnable = object : Runnable {
            override fun run() {
                etTodoTitle.setText(etTodoTitle.text.toString() + " " +System.currentTimeMillis())
                handler.postDelayed(this, 10)
            }
        }
        runnable.run()*/
    }

    override fun onResume() {
        super.onResume()
        try {
            barcodeReader?.claim()
        } catch (e: ScannerUnavailableException) {
            e.printStackTrace()
            Toast.makeText(this, "Scanner unavailable", Toast.LENGTH_SHORT).show()
        }
    }

    private fun activateBarcode() {

        AidcManager.create(this) { aidcManager ->
            if (aidcManager != null) {
                manager = aidcManager
                registerListener(manager)
            } else {
                Toast.makeText(this, "manager is null", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun registerListener(aidcManager: AidcManager?)
    {
        barcodeReader = aidcManager!!.createBarcodeReader()
        if (barcodeReader != null) {
            // register bar code event listener
            barcodeReader?.let {
                it.addBarcodeListener(this)

                // set the trigger mode to client control
                try {
                    it.setProperty(
                        BarcodeReader.PROPERTY_TRIGGER_CONTROL_MODE,
                        BarcodeReader.TRIGGER_CONTROL_MODE_AUTO_CONTROL
                    )
                } catch (e: UnsupportedPropertyException) {
                    Toast.makeText(this, "Failed to apply properties", Toast.LENGTH_SHORT)
                        .show()
                }
                // register trigger state change listener
                // register trigger state change listener
                it.addTriggerListener(this)

                val properties: MutableMap<String, Any> = HashMap()
                // Set Symbologies On/Off
                // Set Symbologies On/Off
                properties[BarcodeReader.PROPERTY_CODE_128_ENABLED] = true
                properties[BarcodeReader.PROPERTY_GS1_128_ENABLED] = true
                properties[BarcodeReader.PROPERTY_QR_CODE_ENABLED] = true
                properties[BarcodeReader.PROPERTY_CODE_39_ENABLED] = true
                properties[BarcodeReader.PROPERTY_DATAMATRIX_ENABLED] = true
                properties[BarcodeReader.PROPERTY_UPC_A_ENABLE] = true
                properties[BarcodeReader.PROPERTY_EAN_13_ENABLED] = false
                properties[BarcodeReader.PROPERTY_AZTEC_ENABLED] = false
                properties[BarcodeReader.PROPERTY_CODABAR_ENABLED] = false
                properties[BarcodeReader.PROPERTY_INTERLEAVED_25_ENABLED] = false
                properties[BarcodeReader.PROPERTY_PDF_417_ENABLED] = false
                // Set Max Code 39 barcode length
                // Set Max Code 39 barcode length
                properties[BarcodeReader.PROPERTY_CODE_39_MAXIMUM_LENGTH] = 10
                // Turn on center decoding
                // Turn on center decoding
                properties[BarcodeReader.PROPERTY_CENTER_DECODE] = true
                // Enable bad read response
                // Enable bad read response
                properties[BarcodeReader.PROPERTY_NOTIFICATION_BAD_READ_ENABLED] = true
                // Apply the settings
                it.setProperties(properties)
            }
        } else {
            Toast.makeText(this, "Reader is null", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onBackPressed() {
        Log.d("spinner", "Back presseed")

    }


    fun getScannedEntry() {
        val scannedEntry = etTodoTitle.text.toString().trim()
        if (scannedEntry.isNotEmpty()) {
            val entryArray = scannedEntry.split(" ")
            for (entry in entryArray) {
                processBarcodeEntry(entry)
            }
        }
        etTodoTitle.text.clear()
    }

    fun processBarcodeEntry(code: String) {
        if (code.isNotEmpty()) {
            if (!code.contains(Regex("[^A-Za-z]"))) {
                //employee code
                emp_id.text = code.trim()
            } else {
                //barcode
                val todo = Saleorder(code.trim())
                todoAdapter.addTodo(todo)
            }
        }

    }

    override fun onBarcodeEvent(event: BarcodeReadEvent?) {

//        event?.let {
//            val list: MutableList<String> = ArrayList()
//            list.add("Barcode data: " + event.getBarcodeData())
//            list.add("Character Set: " + event.getCharset())
//            list.add("Code ID: " + event.getCodeId())
//            list.add("AIM ID: " + event.getAimId())
//            list.add("Timestamp: " + event.getTimestamp())
//            processBarcodeEntry(event.getBarcodeData())
//        }
        runOnUiThread { // update UI to reflect the data
            processBarcodeEntry(event!!.getBarcodeData())
            Toast.makeText(this, event!!.getBarcodeData(), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onFailureEvent(p0: BarcodeFailureEvent?) {
        runOnUiThread { // update UI to reflect the data
            Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTriggerEvent(p0: TriggerStateChangeEvent?) {
        runOnUiThread { // update UI to reflect the data
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPause() {
        super.onPause()
        barcodeReader?.release()
    }

    override fun onDestroy() {
        // unregister barcode event listener
        barcodeReader?.removeBarcodeListener(this)

        // unregister trigger state change listener
        barcodeReader?.removeTriggerListener(this)
        super.onDestroy()
    }

}