package Trnity.ITP.Recetta.View.StaticScreens

import Trnity.ITP.Recetta.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun FAQsScreen(navController: NavController) {
    Surface {

        Spacer(modifier = Modifier.height(32.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            Icon(
                tint = Color.Blue,
                painter = painterResource(id = R.drawable.arrow_back_r),
                contentDescription = "go Back",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable {
                        navController.popBackStack()
                    }
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {

            Scaffold { padding ->
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(horizontal = 16.dp)
                ) {

                    val questions = listOf(
                        "What does the app do?",
                        "How do I add items to my inventory?",
                        "Can I use the camera to scan multiple ingredients at once?",
                        "How are recipes generated?",
                        "Can I filter recipes by cuisine or dietary preferences?",
                        "Is the app free to use? Are there any premium features?",
                        "Does the app store my inventory or camera data?",
                        "How do I report a bug or suggest a feature?"
                    )

                    val answers = listOf(
                        "The app generates recipes based on your ingredients or inventory filled through the camera.",
                        "You can manually add items or use your camera to scan ingredients.",
                        "Currently, you can scan one ingredient at a time.",
                        "Recipes are created using AI according to a combination of your provided ingredients and inventory items.",
                        "Yes, you can apply filters to customize the recipe suggestions.",
                        "Yes, it's free to use. No, not at the moment.",
                        "Your data is stored locally and securely. We prioritize user privacy.",
                        "You can't because you won't find any."
                    )

                    var listedQuestions by remember { mutableStateOf(questions) }  // Make this mutable

                    Text(
                        text = "We're here to help you with anything and everything.",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(top = 16.dp)
                    )

                    Text(
                        text = "Check out our frequently asked questions below.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )

                    // Search Bar
                    var searchText by remember { mutableStateOf("") }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp)
                            .background(Color(0xFFF3F3F3), shape = MaterialTheme.shapes.medium)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        BasicTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                // Filter the questions based on the search text
                                listedQuestions = questions.filter { question ->
                                    question.contains(searchText, ignoreCase = true)  // Case-insensitive search
                                }
                            },
                            textStyle = TextStyle(fontSize = 14.sp),
                            modifier = Modifier.fillMaxWidth(),
                            decorationBox = { innerTextField ->
                                if (searchText.isEmpty()) {
                                    Text(
                                        text = "Search Help",
                                        fontSize = 14.sp,
                                        color = Color.Gray
                                    )
                                }
                                innerTextField()
                            }
                        )
                    }

                    // FAQ Section
                    Text(
                        text = "FAQ",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // FAQ Items
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        for (index in listedQuestions.indices) {
                            FAQStyledItem(
                                question = listedQuestions[index],
                                answer = answers[index],
                                show = true
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FAQStyledItem(question: String, answer: String, show: Boolean) {
    var isExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .background(Color(0xFFF2ECF4))
            .fillMaxWidth()
            .size(if (show) Dp.Unspecified else 50.dp)
            .clickable { isExpanded = !isExpanded }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = question,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = answer,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
