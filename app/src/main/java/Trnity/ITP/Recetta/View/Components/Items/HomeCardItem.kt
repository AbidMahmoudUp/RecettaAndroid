package Trnity.ITP.Recetta.View.Components.Items

import Trnity.ITP.Recetta.R

data class HomeCardItem(
    val title: String,
    val description: String,
    val imageRes: Int // Image resource ID
)

val sampleRecipes = listOf(
    HomeCardItem("Hot & Prawn Noodles", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged", R.drawable.food_image), // Replace with your image resources
    HomeCardItem("Chicken Alfredo Pasta", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the", R.drawable.food_image),
    HomeCardItem("Vegetable Stir Fry", "Healthy stir-fried vegetables", R.drawable.food_image)
)