package Trnity.ITP.Recetta.ViewModel

import Trnity.ITP.Recetta.Model.repositories.InventoryRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ScannerViewModel @Inject constructor(private val inventroyRepository: InventoryRepository): ViewModel(){


    fun updateInventory(id : String, img: MultipartBody.Part)
    {
        viewModelScope.launch{
            inventroyRepository.updateInventoryWithImage(id,img)
        }

    }

}