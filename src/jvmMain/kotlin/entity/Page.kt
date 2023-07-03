package entity

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import config.route_left_item_color


data class Page(val index:Int, val name:String, val icon: String,val color: Color = route_left_item_color,val comp: @Composable (() -> Unit))