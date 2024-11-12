package Trnity.ITP.Recetta.ViewModel

import Trnity.ITP.Recetta.Model.entities.Inventory
import Trnity.ITP.Recetta.Model.repositories.InventoryRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    // Automatically fetch ingredients when ViewModel is created
    init {
        println("TEST-----------------------------------------------------------------------")
        println(fetchInventory())
    }
    fun loadStuff()
    {
        viewModelScope.launch {
            _isLoading.value = true
            delay(3000)
            _isLoading.value = false
        }

    }
    fun fetchInventory() {
        viewModelScope.launch {
            try {
                // Fetch ingredients from the repository
                _isLoading.value = true
                delay(1000)
                _inventory.value = repository.getInventory("6730c43b986803e32821be1f")


                _isLoading.value = false
                println("Fetched ingredients: ${_inventory.value}")
            } catch (e: Exception) {
                e.printStackTrace()
                // Log or handle errors here
            }
        }
    }
}