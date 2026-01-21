package com.carttonme.data

import com.carttonme.model.Smurf
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlinx.coroutines.suspendCancellableCoroutine
import com.google.firebase.database.DatabaseError

class SmurfRepository {
    suspend fun fetchSmurfs(): List<Smurf> {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("smurfs")
        return suspendCancellableCoroutine { continuation ->
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val smurfs = snapshot.children.mapNotNull { child ->
                        val id = child.key ?: return@mapNotNull null
                        val name = child.child("name").getValue(String::class.java) ?: return@mapNotNull null
                        val imageUrl =
                            child.child("imageUrl").getValue(String::class.java)
                                ?: child.child("image").getValue(String::class.java)
                                ?: return@mapNotNull null
                        val text = child.child("text").getValue(String::class.java)
                            ?: child.child("personality").getValue(String::class.java)
                            ?: ""
                        Smurf(
                            id = id,
                            name = name,
                            imageUrl = imageUrl,
                            text = text
                        )
                    }
                    continuation.resume(smurfs)
                }

                override fun onCancelled(error: DatabaseError) {
                    continuation.resumeWithException(error.toException())
                }
            }
            databaseReference.addListenerForSingleValueEvent(listener)
            continuation.invokeOnCancellation {
                databaseReference.removeEventListener(listener)
            }
        }
    }
}
