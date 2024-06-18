package com.git.extc.activities.MoreActivities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;


import android.content.Context;

import android.os.Bundle;
import android.util.Log;


import com.android.volley.Response;
import com.git.extc.activities.MoreActivities.chatmodel.Message;
import com.git.extc.activities.MoreActivities.chatmodel.MessageAdapter;


import com.git.extc.databinding.ActivityChatWithBotBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


public class ChatwithBot extends AppCompatActivity {

    ActivityChatWithBotBinding binding;
    public Context context;

    String url = "https://api.openai.com/v1/chat/completions";
    String accessToken = "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";

    List<Message> messageList;
    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_chat_gpt);

        binding = ActivityChatWithBotBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = this;

        messageList = new ArrayList<>();

        messageAdapter = new MessageAdapter(messageList);
        binding.recyclerView.setAdapter(messageAdapter);

        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.recyclerView.setLayoutManager(linearLayoutManager);*/


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //binding.recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());

//        binding.backimage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, Home.class);
//                intent.putExtra("returnToFragment", "Fragment3");
//                startActivity(intent);
//            }
//        });

        binding.sendbtn.setOnClickListener((v)->{
            String question = binding.messagetexttext.getText().toString().trim();
            addToChat(question,Message.SEND_BY_ME);
            binding.messagetexttext.setText("");
            callAPI(question);

        });


    }




    void addToChat (String message, String sendBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sendBy));
                messageAdapter.notifyDataSetChanged();
                binding.recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }




    void callAPI(String question){
        // okhttp
        messageList.add(new Message("Typing...", Message.SEND_BY_BOT));

        JSONObject parametter = new JSONObject();
        try {
            parametter.put("model" , "gpt-3.5-turbo");
            parametter.put("prompt" ,question );
            parametter.put("max_tokens"  , 1000);
            parametter.put("temperature" , 0);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parametter, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray jsonArray = response.getJSONArray("choices");

                    String answer =  jsonArray.getJSONObject(0)
                            .getJSONObject("message")
                            .getString("content");

                    getGptResponse(answer);

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                getGptResponse("Failed due to : "+error.toString());
                Log.e("errorMessage", error.toString());

            }
        } ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String , String> header = new HashMap<>();
                header.put("Authorization" , "Bearer "+ accessToken);

                header.put("Content-Type" , "application/json");

                return  header;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);




    }

    private void getGptResponse(String answer) {
        messageList.remove(messageList.size()-1);
        addToChat(answer  , Message.SEND_BY_BOT);
    }





}