package com.example.premierepage

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_third.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ThirdActivity : AppCompatActivity(),DatePickerDialog.OnDateSetListener{

    var retrofitInterface: RetrofitInterface? = null



    private var cYear: Int? = null
    private var cAge: Int? = null
    private var radio: String? = null
    private lateinit var etHauteur : EditText
    private lateinit var etPoids : EditText
    private lateinit var etage : TextView




    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        val retrofit = RetrofitClient.getInstance()
        retrofitInterface = retrofit.create(RetrofitInterface::class.java)




        //get data from intent
        val intent = intent



        val extras = intent.extras
        val username = intent.getStringExtra("username")

        // textView
        val resultview = findViewById<TextView>(R.id.username)
        //setText
        resultview.text = username


        etHauteur = findViewById(R.id.Hauteur)
        etPoids = findViewById(R.id.Poids)
        etage = findViewById(R.id.age)

        valider.setOnClickListener{

            val hauteur = etHauteur.text.toString().trim()
            val poids = etPoids.text.toString().trim()
            val age = etage.text.toString().trim()
            val cAge = cAge.toString().trim()
            var radio: String? = "HOMME"
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            radioGroup.setOnCheckedChangeListener{ radioGroup,i ->
                var rb = findViewById<RadioButton>(i)
                if (rb!=null){
                    Toast.makeText(this,rb.text.toString(), LENGTH_SHORT).show()
                    radio = rb.text.toString()
                }
            }

            if (age.format(Date()).isEmpty()){
                etage.error="age est obligatoire";
                Toast.makeText(this, "Age est Obligatoire!", LENGTH_SHORT).show()}
            else if((currentYear - cYear!!)<15){
                etage.error="age est invalide";
                Toast.makeText(this, "Age minimale : 15 ans !", LENGTH_SHORT).show()
            }
            else if(hauteur.isEmpty())  {
                etHauteur.error = "Hauteur est obligatoire";
                Toast.makeText(this, "Hauteur est obligatoire !", LENGTH_SHORT).show()}
            else if (hauteur.toFloat()<1 || hauteur.toFloat()>3){
                etHauteur.error ="Hauteur est Invalide"
                Toast.makeText(this, "Hauteur est Invalide !", LENGTH_SHORT).show()
            }
            else if(poids.isEmpty() ) {
                etPoids.error = "Poids est obligatoire";
                Toast.makeText(this, "Poids est obligatoire !", LENGTH_SHORT).show()}
            else if (poids.toFloat()>200 ||poids.toFloat()<10){
                etPoids.error = "Poids est Invalide";
                Toast.makeText(this, "Poids est Invalide !", LENGTH_SHORT).show()}

            else {
                /* Toast.makeText(this, "Informations Collectées !", LENGTH_SHORT).show()
                 val intent = Intent(this,FifthActivity::class.java)
                 intent.putExtra("poids",poids)
                 intent.putExtra("hauteur",hauteur)
                 intent.putExtra("age",cAge)
                 intent.putExtra("radio",radio)
                 startActivity(intent)*/








                val map = HashMap<String?, String?>()
                val dates=cAge.format(Date())
                map["poids"] = poids
                map["taille"] = hauteur
                /*  if(etFemme.isChecked())
                      map["sexe"]="Femme"
                  else
                      map["sexe"]="Homme"
                  map["date"]=dates*/
                map["age"]=cAge


                val call = retrofitInterface!!.executeSave(map, extras!!.getString("token"))
                call!!.enqueue(object : Callback<Void?> {
                    override fun onResponse(
                        call: Call<Void?>,
                        response: Response<Void?>
                    ) {
                        if (response.code() == 200) {
                            Toast.makeText(
                                this@ThirdActivity,
                                " success", Toast.LENGTH_LONG
                            ).show()


                        } else if (response.code() == 400) {
                            Toast.makeText(
                                this@ThirdActivity,
                                "error", Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Void?>, t: Throwable) {
                        Toast.makeText(
                            this@ThirdActivity, t.message,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
            }

        }


        //Calendar
        val c = Calendar.getInstance()
        var year = c.get(Calendar.YEAR)
        var month = c.get(Calendar.MONTH)
        var day = c.get(Calendar.DAY_OF_MONTH)

        //button click to show DatePicker
        buttonDatePicker.setOnClickListener{
            val dpd = DatePickerDialog(this,DatePickerDialog.OnDateSetListener
            { view, mYear, mMonth, mDay ->
                day = mDay
                month = mMonth
                year = mYear
                //set to textView
                age.text= "$day/${month+1}/$year"
                cYear=year
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                cAge = currentYear - year
            },year,month,day)

            //Show dialog
            dpd.show()
        }

    }

    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        TODO("Not yet implemented")
    }



}
