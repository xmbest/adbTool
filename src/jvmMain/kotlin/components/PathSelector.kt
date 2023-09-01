package components

import androidx.compose.ui.awt.ComposeWindow
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter
import kotlin.text.StringBuilder

object PathSelector {
    fun selectFile(vararg fileType: String): String {
        return selectPath("", JFileChooser.FILES_ONLY, *fileType)
    }

    fun selectFile(title: StringBuilder = StringBuilder(""),vararg fileType: String): String {
        return selectPath(title.toString(), JFileChooser.FILES_ONLY, *fileType)
    }

    fun selectFileOrDir(title: String = ""): String {
        return selectPath(title, JFileChooser.FILES_AND_DIRECTORIES)
    }

    fun selectDir(title: String = ""): String {
        return selectPath(title, JFileChooser.DIRECTORIES_ONLY)
    }

    fun selectPath(title: String, properties: Int, vararg fileType: String): String {
        var path = ""
        JFileChooser().apply {
            if (title.isNotBlank()) {
                dialogTitle = title
            }
            fileSelectionMode = properties
            if (fileType.isNotEmpty())
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
