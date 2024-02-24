package com.example.jako

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object MyToast {

    fun showWarning(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

class MainActivity : AppCompatActivity() {
    private var selectedFileUri: Uri? = null
    private lateinit var uploadFileTextView: TextView
    private lateinit var textFileName: TextView
    private lateinit var enterButtonFile: TextView
    private lateinit var keyFileName: TextView
    private lateinit var enterButtonKey: TextView
    private lateinit var userChoice: Spinner
    private lateinit var startButton: TextView
    private lateinit var resetButtonText: TextView
    private lateinit var resetButtonKey: TextView
    private lateinit var fileContentText: String
    private lateinit var fileContentKey: String


    private fun writeToFile(file: File, data: String) {
        try {
            // Создаем FileOutputStream для записи данных в файл
            val fileOutputStream = FileOutputStream(file)
            // Преобразуем строку в байты и записываем их в файл
            fileOutputStream.write(data.toByteArray())
            // Закрываем поток
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun downloadFile(file: File) {
        try {
            val fileUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", file)

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(fileUri, "text/plain")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            // Проверка наличия приложений, которые могут обработать Intent
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Если не найдено приложение, выводим предупреждение
                MyToast.showWarning(this, "No application found to handle the action.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }



    private fun readTextFromUri(uri: Uri): String {
        contentResolver.openInputStream(uri)?.use { inputStream ->
            try {
                val content = inputStream.reader(Charsets.UTF_8).use { it.readText() }
                // Добавьте логирование
                Log.d("FileContent", content)
                return content
            } catch (e: Exception) {
                // В случае ошибки, также добавьте логирование
                Log.e("ReadFileError", "Error reading file", e)
            }
        }
        return ""
    }




    private val readFileKey = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                // Получаем содержимое файла
                fileContentKey = readTextFromUri(uri)

            }
        }
    }


    private val readFileText = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                // Получаем содержимое файла
                fileContentText = readTextFromUri(uri)
                // Теперь fileContentText инициализирован и может быть использован в других частях кода
            }
        }
    }

    private val filePickerLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    try {
                        // Сохраните Uri выбранного файла
                        selectedFileUri = uri
                        // Получите имя файла из Uri и отобразите его в TextView
                        val fileName = getFileName(uri)
                        uploadFileTextView.text = fileName
                    } catch (e: Exception) {
                        e.printStackTrace()
                        MyToast.showWarning(this, "Error: ${e.message}")
                    }
                }
            }
        }



    private val filePickerLauncherKey: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    try {
                        // Сохраните Uri выбранного файла
                        selectedFileUri = uri
                        // Получите имя файла из Uri и отобразите его в TextView
                        val fileName = getFileName(uri)
                        keyFileName.text = fileName
                    } catch (e: Exception) {
                        e.printStackTrace()
                        MyToast.showWarning(this, "Error: ${e.message}")
                    }
                }
            }
        }


    private fun getFileName(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                return displayName
            }
        }
        return ""
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }

        uploadFileTextView = findViewById(R.id.upload_file_text)
        enterButtonFile = findViewById(R.id.enter_btn_textfile)
        keyFileName = findViewById(R.id.filename_key)
        enterButtonKey = findViewById(R.id.enter_btn_keyfile)
        userChoice = findViewById(R.id.user_choice)
        startButton = findViewById(R.id.start)
        resetButtonText = findViewById(R.id.reset_btn_textfile)
        resetButtonKey = findViewById(R.id.reset_btn_keyfile)
        val defaultOutputText = "Text File"
        val defaultOutputKey = "Key File"
        val fileNameEncryption = "encryption.txt"
        val fileNameDecryption = "decryption.txt"
        val file = File(cacheDir, fileNameEncryption)
        enterButtonFile.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
            filePickerLauncher.launch(intent)

        }

        enterButtonKey.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
            }
            filePickerLauncherKey.launch(intent)
        }
        resetButtonText.setOnClickListener {
            uploadFileTextView.text = defaultOutputText
        }


        resetButtonKey.setOnClickListener {
            keyFileName.text = defaultOutputKey
        }

        val listChoice = listOf("Encryption", "Decryption")
        //связка Spinner и listUsername
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listChoice)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        userChoice.adapter = arrayAdapter
        var outputText: String? = null

        // Установка обработчиков выбора элементов Spinner
        userChoice.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }
        }




        startButton.setOnClickListener {
            try {
                var outputText: String? = null
                if (userChoice.selectedItem.toString() == listChoice[0]){
                    outputText = MainLBC.mainLbcEncryption(fileContentText)
                } else if (userChoice.selectedItem.toString() == listChoice[1]){
                    outputText = MainLBC.mainLbcDecryption(fileContentText)
                }

                val intent = Intent(this, OutputActivity::class.java)
                intent.putExtra("yourKey", outputText)
                startActivity(intent)
            } catch (e: Exception) {
                MyToast.showWarning(this, "${e.message}")
            }
        }



    }







}
