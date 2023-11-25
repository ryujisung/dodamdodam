package com.example.dodamdodam.ui.chat
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dodamdodam.R
import com.example.dodamdodam.databinding.ActivityChatBinding
import com.example.dodamdodam.model.Friend
import com.example.dodamdodam.model.Message
import com.example.dodamdodam.ui.base.BaseActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class ChatActivity : BaseActivity<ActivityChatBinding>(R.layout.activity_chat) {
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var firebaseAuth: FirebaseAuth
    private val messagesList = mutableListOf<Message>()
    private lateinit var firestore: FirebaseFirestore

    private val client by lazy {
        OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser ?: return
        val currentUserId = currentUser.uid

        messageAdapter = MessageAdapter(currentUserId)
        binding.recyclerViewChat.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = messageAdapter
        }

        val chatRoomId = intent.getStringExtra("chatRoomId") ?: return

        setupChatRoom(chatRoomId)

        binding.buttonSendMessage.setOnClickListener {
            val messageText = binding.edittextChatMessage.text.toString().trim()
            if (messageText.isNotEmpty()) {
                val message = Message(
                    senderUid = currentUserId,
                    content = messageText,
                    senderName = currentUser.displayName ?: "Unknown",
                    sended_date = getCurrentDateTime()
                )
                sendMessage(chatRoomId, message)
                callGPTAPI(messageText) { response ->
                    val botMessage = Message(
                        senderUid = "bot",
                        content = response,
                        senderName = "ChatBot",
                        sended_date = getCurrentDateTime()
                    )
                    sendMessage(chatRoomId, botMessage)
                }
                binding.edittextChatMessage.text.clear()
            }
        }
    }

    // 현재 날짜와 시간을 문자열로 반환
    private fun getCurrentDateTime(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun setupChatRoom(chatRoomId: String) {
        val databaseReference = FirebaseDatabase.getInstance().getReference("chatrooms/$chatRoomId/messages")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                messagesList.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Message::class.java)
                    message?.let { messagesList.add(it) }
                }
                messageAdapter.setMessages(messagesList)
                binding.recyclerViewChat.scrollToPosition(messagesList.size - 1)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // 오류 처리
            }
        })
    }

    private fun sendMessage(chatRoomId: String, message: Message) {
        val database = FirebaseDatabase.getInstance()
        val messagesRef = database.getReference("chatrooms/$chatRoomId/messages")

        messagesRef.push().setValue(message).addOnCompleteListener { messageTask ->
            if (messageTask.isSuccessful) {
                val lastMessageRef = database.getReference("chatrooms/$chatRoomId/lastMessage")
                lastMessageRef.setValue(message.content)
            }
        }
    }

    // GPT API 호출 함수
    private fun callGPTAPI(question: String, onResult: (String) -> Unit) {
        val arr = JSONArray().apply {
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
                Log.e("ChatActivity", "API Request Failure", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (it.isSuccessful) {
                        val responseString = it.body?.string()
                        Log.d("ChatActivity", "Response: $responseString")
                        responseString?.let { responseBody ->
                            val jsonObject = JSONObject(responseBody)
                            val jsonArray = jsonObject.getJSONArray("choices")
                            val result = jsonArray.getJSONObject(0).getString("content")
                            runOnUiThread {
                                onResult(result)
                            }
                        }
                    } else {
                        Log.e("ChatActivity", "API Request Unsuccessful: ${it.code}")
                    }
                }
            }
        })
    }
}