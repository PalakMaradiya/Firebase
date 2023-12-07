package com.example.firebase.ACTIVITY

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initview()
    }

    private fun initview() {


//        for crete Account

        binding.btnCreteAccount.setOnClickListener {


            var email = binding.edtEmail.text.toString()
            var pass = binding.edtPassword.text.toString()


            auth = Firebase.auth


            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        Toast.makeText(this, "Account Create Succsefully", Toast.LENGTH_SHORT)
                            .show()

                    } else {

                        Log.e("TAG", "initview: " + task)
                        Toast.makeText(this, "Invalid Email & Password ", Toast.LENGTH_SHORT).show()
                    }
                }




//            for login
            binding.btnLogin.setOnClickListener {


                var email = binding.edtEmail.text.toString()
                var pass = binding.edtPassword.text.toString()



                auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            Toast.makeText(this, "Login Succsefully", Toast.LENGTH_SHORT).show()
                            Log.e("TAG", "initview: "+ task.exception.toString() )
                            val i = Intent(this@LoginActivity, HomeActivity::class.java)
                            startActivity(i)

                        } else {

                            Log.e("TAG", "initview: "+ task.exception.toString())
                            Toast.makeText(this, "Invaild UserName & PassWord", Toast.LENGTH_SHORT).show()

                        }
                    }

            }

        }


    }
}