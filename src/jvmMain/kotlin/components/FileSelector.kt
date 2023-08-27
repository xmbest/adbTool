package components

import androidx.compose.ui.awt.ComposeWindow
import utils.DefFileFilter
import javax.swing.JFileChooser

object FileSelector {
    fun selectFile(vararg fileType: String): String {
        val defFileFilter = DefFileFilter()
        val sb = StringBuilder()
        fileType.forEach {
            defFileFilter.addExtension(it)
            sb.append(it).append(" ")
        }
        defFileFilter.setDescription(sb.toString())
        val jFileChooser = JFileChooser()
        jFileChooser.fileSelectionMode = JFileChooser.FILES_ONLY
        jFileChooser.addChoosableFileFilter(defFileFilter)
        jFileChooser.showOpenDialog(ComposeWindow())
        return jFileChooser.selectedFile?.absolutePath ?: ""
    }
}
