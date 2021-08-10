package com.example.scandent

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import android.app.ProgressDialog




class LoginActivity : AppCompatActivity() {

    private val sharedPrefFile = "DentcareScanner"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //btnLogin.setEnabled(false);
        //val progress = ProgressDialog(this)

        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val spinner: Spinner = findViewById(R.id.section)
        val spinner1: Spinner = findViewById(R.id.department)
        val spinner2: Spinner = findViewById(R.id.location)

        ArrayAdapter.createFromResource(
            this,
            R.array.DENTCARE_ARRAY,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner1.adapter = adapter
            spinner2.adapter = adapter
        }

        btnLogin.setOnClickListener {

/*            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
            progress.show();*/



            val emp_code = inputEmail.text.toString()
            val id = 1
            if (emp_code.isNotEmpty()) {
                //btnLogin.setEnabled(true);
                val id: Int = id
                val name: String = emp_code
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putInt("id_key", id)
                editor.putString("name_key", name)
                editor.apply()
                editor.commit()


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
                    val text: String =spinner.getSelectedItem().toString()
                    Log.d("spinner", text)
                }
            }
        }





    }
}