package com.example.dodamdodam.ui.chat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.dodamdodam.R
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

class ChatFragment : Fragment() {

    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private lateinit var editTextQuestion: EditText
    private lateinit var textViewResponse: TextView
    private lateinit var buttonSend: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        editTextQuestion = view.findViewById(R.id.editTextQuestion)
        textViewResponse = view.findViewById(R.id.textViewResponse)
        buttonSend = view.findViewById(R.id.buttonSend)

        buttonSend.setOnClickListener {
            callAPI(editTextQuestion.text.toString())
        }

        return view
    }

    private fun callAPI(question: String) {
        val arr = JSONArray().apply {
            put(JSONObject().apply {
                put("role", "user")
                put("content", "You are a helpful and kind AI Assistant.")
            })
            put(JSONObject().apply {
                put("role", "user")
                put("content", question)
            })
        }

        val requestBody = JSONObject().apply {
            put("model", "gpt-3.5-turbo")
            put("messages", arr)
        }.toString().toRequestBody("application/json; charset=utf-8".toMediaType())

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .header("Authorization", "Bearer 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")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("ChatFragment", "API Request Failure", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseString = response.body?.string()
                    Log.d("ChatFragment", "Response: $responseString")
                    responseString?.let {
                        try {
                            val jsonObject = JSONObject(it)
                            val jsonArray = jsonObject.getJSONArray("choices")
                            val result = jsonArray.getJSONObject(0).getString("content")
                            // 결과 처리 관련 코드 추가...
                        } catch (e: Exception) {
                            Log.e("ChatFragment", "JSON Parsing Error", e)
                        }
                    }
                } else {
                    Log.e("ChatFragment", "API Request Unsuccessful: ${response.code}")
                }
            }
        })
    }
}

