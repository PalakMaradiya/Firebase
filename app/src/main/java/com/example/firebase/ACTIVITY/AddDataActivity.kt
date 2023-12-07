package com.example.firebase.ACTIVITY

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebase.MODALCLASS.ModalClass
import com.example.firebase.databinding.ActivityAddDataBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID

class AddDataActivity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 100
    lateinit var uri: Uri
    lateinit var d: Uri



    lateinit var binding: ActivityAddDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initview()

    }

    private fun initview() {


        var reference = FirebaseDatabase.getInstance().reference



        binding.btnUplodImage.setOnClickListener {


            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(
                    intent,
                    "Select Image from here..."
                ),
                PICK_IMAGE_REQUEST
            )

        }







        binding.btnSumbit.setOnClickListener {


            var name = binding.edtName.text.toString()
            var age = binding.edtAge.text.toString()
            var number = binding.edtNumber.text.toString()
            var city = binding.edtCity.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Name :", Toast.LENGTH_SHORT).show()
            } else if (age.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Age ", Toast.LENGTH_SHORT).show()
            } else if (number.isEmpty()) {
                Toast.makeText(this, "Please Enter Your Number", Toast.LENGTH_SHORT).show()
            } else if (city.isEmpty()) {
                Toast.makeText(this, "Please Enter Your City", Toast.LENGTH_SHORT).show()
            } else {
                var key = reference.root.child("InformestionTb").push().key ?: ""

                var modelclass = ModalClass(name, age, number, city, key,d)


                reference.root.child("InformestionTb").child(key).setValue(modelclass)
                    .addOnCompleteListener {

                        if (it.isSuccessful) {
                            Toast.makeText(this, "Data Added SuccessFully", Toast.LENGTH_SHORT)
                                .show()

                            var i = Intent(this@AddDataActivity, ShowDataAvtivity::class.java)
                            startActivity(i)
                        }
                    }.addOnFailureListener {

                        Log.e("TAG", "Error: " + it)
                    }


            }

        }

    }
    fun uploadImage() {
        if (uri != null) {

            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            // Defining the child of storageReference
            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child("images/"+UUID.randomUUID().toString())

            // adding listeners on upload
            // or failure of image
            if (ref != null) {
                ref.putFile(uri).continueWith{

                    ref.downloadUrl.addOnCompleteListener {
                        d=it.result
                    }


                }

                    .addOnSuccessListener { // Image uploaded successfully
                        // Dismiss dialog
                        progressDialog.dismiss()
                        Toast
                            .makeText(
                                this@AddDataActivity,
                                "Image Uploaded!!",
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }
                    .addOnFailureListener { e -> // Error, Image not uploaded
                        progressDialog.dismiss()
                        Toast
                            .makeText(
                                this@AddDataActivity,
                                "Failed " + e.message,
                                Toast.LENGTH_SHORT
                            )
                            .show()
                    }

            }
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(
            requestCode,
            resultCode,
            data
        )

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

            // Get the Uri of data
            uri = data.data!!
            uploadImage()

        }
    }


}
