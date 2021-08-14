package com.example.scandent.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scandent.R
import com.example.scandent.model.registerRequestModel
import com.example.scandent.network.APIClient
import com.example.scandent.network.ApiService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var todoAdapter: SaleorderAdapter
    private val sharedPrefFile = "DentcareScanner"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)


        val sharedIdValue = sharedPreferences.getInt("id_key", 0)
        val sharedNameValue = sharedPreferences.getString("name_key", "defaultname")
        if (sharedIdValue.equals(0) && sharedNameValue.equals("defaultname")) {
            Toast.makeText(
                applicationContext,
                "Something Went Wrong",
                Toast.LENGTH_SHORT
            ).show()
        } else
        {

            Log.d("TAG", sharedNameValue.toString())
            Log.d("TAG", sharedIdValue.toString())

        }




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
    }

    override fun onBackPressed() {
        Log.d("spinner", "Back presseed")

    }


    fun getScannedEntry()
    {
        val scannedEntry = etTodoTitle.text.toString().trim()
        if(scannedEntry.isNotEmpty())
        {
            val entryArray = scannedEntry.split(" ")
            for(entry in entryArray)
            {
                processBarcodeEntry(entry)
            }
        }
        etTodoTitle.text.clear()
    }
    fun processBarcodeEntry(code:String)
    {
        if(code.isNotEmpty())
        {
            if(!code.contains(Regex("[^A-Za-z]")))
            {
                //employee code
                emp_id.text = code.trim()
            }
            else{
                //barcode
                val todo = Saleorder(code.trim())
                todoAdapter.addTodo(todo)
            }
        }

    }


}