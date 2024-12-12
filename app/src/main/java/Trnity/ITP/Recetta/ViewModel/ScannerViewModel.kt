package Trnity.ITP.Recetta.ViewModel

import Trnity.ITP.Recetta.Model.entities.Recipe
import Trnity.ITP.Recetta.Model.repositories.InventoryRepository
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(private val inventoryRepository: InventoryRepository) : ViewModel() {
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    private val _updateSuccess = MutableLiveData<Boolean>()
    val updateSuccess: LiveData<Boolean> = _updateSuccess
    private val _progressTrigger = MutableLiveData(false)
    val progressTrigger: LiveData<Boolean> = _progressTrigger

    private val _isLoadingRecipe = MutableLiveData(false)
    val isLoadingRecipe: LiveData<Boolean> = _isLoadingRecipe
    private val _updateSuccessRecipe = MutableLiveData<Boolean>()
    val updateSuccessRecipe: LiveData<Boolean> = _updateSuccessRecipe
    private val _progressTriggerRecipe = MutableLiveData(false)
    val progressTriggerRecipe: LiveData<Boolean> = _progressTriggerRecipe
    private val _recipes = MutableLiveData<List<Recipe>>()
    var recipes: LiveData<List<Recipe>> = _recipes


    fun updateInventory(id: String, img: MultipartBody.Part) {
        _isLoading.postValue(true) // Indicate loading started
        _progressTrigger.postValue(true)
        viewModelScope.launch {
            try {
                inventoryRepository.updateInventoryWithImage(id, img)
                _updateSuccess.postValue(true) // Update successful
            } catch (e: Exception) {
                e.printStackTrace()
                _updateSuccess.postValue(false) // Update failed
            } finally {
                _isLoading.postValue(false) // Loading finished
            }
        }
    }
    fun scanRecipe(img: MultipartBody.Part) {
        _isLoadingRecipe.postValue(true)
        _progressTriggerRecipe.postValue(true) // Start progress animation
        viewModelScope.launch {
            try {
                val response = inventoryRepository.scanRecipe(img)
                if (response?.isSuccessful == true && response.body() != null) {
                    val recipeList = response.body()?.let { listOf(it) } ?: emptyList() // Wrap the result into a list if it's a single recipe
                    _recipes.postValue(recipeList) // Store recipes as a list
                    _updateSuccessRecipe.postValue(true)
                    Log.d("response Recipes Scanner View Model " , response.body().toString())
                } else {
                    _updateSuccessRecipe.postValue(false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _updateSuccessRecipe.postValue(false)
            } finally {
                _isLoadingRecipe.postValue(false)
                _progressTriggerRecipe.postValue(false)
            }
        }
    }

    fun resetProgressTrigger() {
        _progressTrigger.postValue(false)
    }
    fun resetProgressTriggerRecipe() {
        _progressTriggerRecipe.postValue(false)
    }
    fun resetRecipes()
    {
        _recipes.value = emptyList()
        recipes = recipes
    }
}
