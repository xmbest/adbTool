package entity

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector


data class Page(val index:Int, val name:String, val icon: ImageVector,val color: Color,val comp: @Composable (() -> Unit))