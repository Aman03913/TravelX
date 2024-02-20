package com.example.travex.Utils

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID
import kotlin.contracts.contract

fun uploadImage(uri: Uri, folderName: String,callback:(String?)->Unit) {
    var imageUrl:String?=null
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri).addOnSuccessListener {
          it.storage.downloadUrl.addOnSuccessListener {
              imageUrl=it.toString()
              callback(imageUrl)
          }
    }
}
fun uploadVideo(uri: Uri, folderName: String,progressDialog: ProgressDialog,callback:(String?)->Unit) {
    var imageUrl:String?=null
    progressDialog.setTitle("Uploading ...")
    progressDialog.show()
    FirebaseStorage.getInstance().getReference(folderName).child(UUID.randomUUID().toString())
        .putFile(uri).addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener {
                imageUrl=it.toString()
                progressDialog.dismiss()
                callback(imageUrl)
            }
        }
        .addOnPausedListener {
            var uploadedValue:Long=it.bytesTransferred/it.totalByteCount
            progressDialog.setMessage(("Uploaded Value $uploadedValue %"))
        }

}