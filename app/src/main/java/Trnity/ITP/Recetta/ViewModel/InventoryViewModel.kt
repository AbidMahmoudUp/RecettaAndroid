package Trnity.ITP.Recetta.ViewModel

import Trnity.ITP.Recetta.Data.remote.Requests.UpdateUserInventory
import Trnity.ITP.Recetta.Model.entities.IngredientRecipe
import Trnity.ITP.Recetta.Model.entities.Inventory
import Trnity.ITP.Recetta.Model.repositories.InventoryRepository
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import javax.inject.Inject


@HiltViewModel
class InventoryViewModel @Inject
constructor(
    private val repository: InventoryRepository
) : ViewModel() {

    private val _inventory = MutableStateFlow(Inventory())
    val inventory: StateFlow<Inventory> = _inventory
    private val _isLoading  = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()
    private val _errorMessage  = mutableStateOf<String?>(null)
    val errorMessage : State<String?> get() = _errorMessage
  //  val   sharedPreferences = ApplicationContext..getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
   // val preferences = context.getSharedPreferences("checkbox", Context.MODE_PRIVATE)
   // val userId = preferences.getString("userId","")
    // Automatically fetch ingredients when ViewModel is created
    init {
        println("TEST-----------------------------------------------------------------------")
        //println(fetchInventory())
    }
    fun loadStuff()
    {
        viewModelScope.launch {
            _isLoading.value = true
            delay(3000)
            _isLoading.value = false
        }

    }
    fun fetchInventory(id : String) {
        viewModelScope.launch {
            try {
                // Fetch ingredients from the repository
                _isLoading.value = true
                delay(1000)
                _inventory.value = repository.getInventory(id)


                _isLoading.value = false
                println("Fetched ingredients: ${_inventory.value}")
            } catch (e: Exception) {
                e.printStackTrace()
                // Log or handle errors here
            }
        }
    }

    fun updateInventory( id :String , ingredients: Set<IngredientRecipe>) {

        val requestBody = UpdateUserInventory(ingredients)
        viewModelScope.launch {
            try {
                val responseBody = repository.updateInventory(id, requestBody)
                Log.d("Update Response", responseBody.toString())
            } catch (e: Exception) {
                Log.e("Update Error", e.message ?: "Unknown error")
            }
        }
    }

    fun updateInventoryForRequieredRecipe(id :String ,ingredients: Set<IngredientRecipe>) {
        _errorMessage.value = null
        val requestBody = UpdateUserInventory(ingredients)
        viewModelScope.launch {
            try {
                Log.d("Request Body",requestBody.toString())
                val responseBody = repository.startCooking(id, requestBody)
                Log.d("Update Response", responseBody.toString())
                _errorMessage.value = null
            } catch (e: HttpException) { // Catch HTTP exceptions
                val errorMessage = try {
                    // Parse the error body if available
                    e.response()?.errorBody()?.string()?.let { errorBody ->
                        JSONObject(errorBody).optString("message", "Unknown error occurred")
                    } ?: "Unknown error occurred"
                } catch (jsonException: Exception) {
                    "Unknown error occurred"
                }
                Log.e("Update Error", errorMessage)
                _errorMessage.value = errorMessage // Update the state with the error message
            } catch (jsonException: Exception) {
                Log.e("JSON Parsing Error", jsonException.message ?: "Unknown JSON error")
                _errorMessage.value =  "Unknown error occurred"
            }
    }
    }

    fun clearErrorMessage(){
        _errorMessage.value = null
    }

}