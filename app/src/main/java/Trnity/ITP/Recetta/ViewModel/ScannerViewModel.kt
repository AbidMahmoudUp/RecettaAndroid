package Trnity.ITP.Recetta.ViewModel

import Trnity.ITP.Recetta.Model.repositories.InventoryRepository
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

    fun resetProgressTrigger() {
        _progressTrigger.postValue(false)
    }
}
