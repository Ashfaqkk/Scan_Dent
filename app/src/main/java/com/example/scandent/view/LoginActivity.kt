package com.example.scandent.view

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_login.*
import android.graphics.Color
import android.view.View
import android.widget.AdapterView
import com.example.scandent.model.registerRequestModel
import com.example.scandent.network.APIClient
import com.example.scandent.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.system.exitProcess
import cn.pedant.SweetAlert.SweetAlertDialog
import android.widget.TextView
import android.view.ViewGroup
import cn.pedant.SweetAlert.SweetAlertDialog.OnSweetClickListener
import android.R





class LoginActivity : AppCompatActivity() {

    private val sharedPrefFile = "DentcareScanner"
    //lateinit var progress:ProgressDialog



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.scandent.R.layout.activity_login)
        //btnLogin.setEnabled(false);
        //val progress = ProgressDialog(this)
        val sharedPreferences: SharedPreferences = this.getSharedPreferences(sharedPrefFile,
            Context.MODE_PRIVATE)
        val spinner: Spinner = findViewById(com.example.scandent.R.id.section)
        val spinner1: Spinner = findViewById(com.example.scandent.R.id.department)
        val spinner2: Spinner = findViewById(com.example.scandent.R.id.location)



        ArrayAdapter.createFromResource(
            this,
            com.example.scandent.R.array.DENTCARE_ARRAY,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner1.adapter = adapter

        }

        spinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener
        {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                Log.d("parentView", parentView.toString())
                Log.d("selectedItemView", selectedItemView.toString())
                Log.d("position", position.toString())
                val text: String =spinner.getSelectedItem().toString()
                Log.d("Value", text)
                if (position == 0)
                {
                    Toast.makeText(applicationContext,"Please select a section.",Toast.LENGTH_SHORT).show()
                }
                else
                {

                }
            }
            override fun onNothingSelected(parentView: AdapterView<*>?) {
                Log.d("CHECKKKKK", "DONE NOTHING")
            }
        })




        btnLogin.setOnClickListener {

            val pDialog = SweetAlertDialog(this@LoginActivity, SweetAlertDialog.PROGRESS_TYPE)
            pDialog.progressHelper.barColor = Color.parseColor("#349EC8")
            pDialog.titleText = "Loading ..."
            pDialog.setCancelable(false)
            pDialog.show()

            setClick()

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


                /*val sharedIdValue = sharedPreferences.getInt("id_key", 0)
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
                }*/
            }
        }
    }



    override fun onBackPressed() {
        Log.d("spinner", "Back presseed")
        moveTaskToBack(true);
        exitProcess(-1)
    }

    private fun setClick() {
        btnLogin.setOnClickListener {
            if(inputEmail.text.toString().trim().isNotEmpty() && inputPassword.text.toString().trim().isNotEmpty())
                //progress.show()
            CoroutineScope(Dispatchers.Default).launch {
                login()
            }

        }
    }

    suspend fun login()
    {
        val loginRequest = registerRequestModel(inputEmail.text.toString().trim(),inputPassword.text.toString().trim())
        val data = APIClient().client.create(ApiService::class.java).register(loginRequest)

        if (data.isSuccessful)
        {
            CoroutineScope(Dispatchers.Main).launch {
                //progress.dismiss()
                //pDialog.dismiss()
                Toast.makeText(this@LoginActivity,data.body()!!.token, Toast.LENGTH_SHORT).show()
                Log.d("Result from API", data.body()!!.token)
            }
        } else
        {
            CoroutineScope(Dispatchers.Main).launch {
                //progress.dismiss()
                //pDialog.dismiss()
                Toast.makeText(this@LoginActivity,"Failed", Toast.LENGTH_SHORT).show()
            }
        }

    }
}