package com.tecsup.ghiblifilms.data.auth

import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.ghiblifilms.domain.model.Rol
import com.tecsup.ghiblifilms.domain.model.Sesion
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) : AuthRepository {

    override suspend fun iniciarSesion(correo: String, clave: String): Result<Sesion> {
        return try {
            val uid = auth.signInWithEmailAndPassword(correo, clave).await().user?.uid
                ?: return Result.failure(IllegalStateException("No se pudo iniciar sesión"))
            Result.success(leerSesion(uid, correo))
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Result.failure(IllegalArgumentException("Correo o contraseña incorrectos"))
        } catch (e: FirebaseAuthInvalidUserException) {
            Result.failure(IllegalArgumentException("Correo o contraseña incorrectos"))
        } catch (e: FirebaseNetworkException) {
            Result.failure(IllegalStateException("No hay conexión a internet"))
        } catch (e: FirebaseException) {
            auth.signOut()
            Result.failure(IllegalStateException(e.localizedMessage ?: "No se pudo iniciar sesión"))
        }
    }

    override suspend fun sesionActual(): Sesion? {
        val usuario = auth.currentUser ?: return null
        return leerSesion(usuario.uid, usuario.email ?: "")
    }

    override fun cerrarSesion() = auth.signOut()

    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val doc = db.collection("usuarios").document(uid).get().await()
        val rol = if (doc.getString("rol") == "admin") Rol.ADMIN else Rol.USUARIO
        return Sesion(uid, correo, rol)
    }
}
