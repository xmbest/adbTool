package components

import androidx.compose.ui.awt.ComposeWindow
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter

object PathSelector {
    fun selectFile(vararg fileType: String): String {
        return selectPath("", JFileChooser.FILES_ONLY, *fileType)
    }

    fun selectDir(vararg fileType: String): String {
        return selectPath("", JFileChooser.DIRECTORIES_ONLY, *fileType)
    }

    fun selectFileOrDir(vararg fileType: String): String {
        return selectPath("", JFileChooser.FILES_AND_DIRECTORIES, *fileType)
    }

    fun selectPath(title: String, properties: Int, vararg fileType: String): String {
        var path = ""
        JFileChooser().apply {
            if (title.isNotBlank()) {
                dialogTitle = title
            }
            fileSelectionMode = properties
            fileFilter = FileNameExtensionFilter(
                fileType.joinToString(","), *fileType
            )
            val state: Int = showOpenDialog(ComposeWindow())
            if (state == JFileChooser.APPROVE_OPTION) {
                path = selectedFile.absolutePath
            }
        }
        return path
    }
}
