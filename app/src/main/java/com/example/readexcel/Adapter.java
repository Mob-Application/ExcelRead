package com.example.readexcel;

import android.app.ProgressDialog;
import android.content.Context;
import android.provider.ContactsContract;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.client.utils.Rfc3492Idn;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    LayoutInflater layoutInflater;
    List<String> patientName, temperature;
    Context context;
    TextToSpeech textToSpeech;
    ProgressDialog progressDialog;

    public Adapter(Context context, List<String> patientName, List<String> temperature) {
        this.layoutInflater = LayoutInflater.from(context);
        this.patientName = patientName;
        this.temperature = temperature;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.custom_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Initiating speakon");
        progressDialog.setMessage("Please wait...");

        final String name = patientName.get(position);
        final String temp = temperature.get(position);

        Picasso.get().load(R.drawable.profile).into(holder.picasso);
        holder.patientName.setText(name);
        holder.tempDetect.setText(temp);

        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                progressDialog.show();
                if (status == TextToSpeech.SUCCESS) {
                    progressDialog.dismiss();
                   int result = textToSpeech.setLanguage(Locale.US);
                       holder.relativeLayout.setEnabled(true);
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Initialization failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double temperatureData = Double.parseDouble(temp);

                if (temperatureData < 37 && temperatureData > 15) {
                    String result = "Don't worry! You are alright" +name;
                    Toast.makeText(context, "Don't worry! You are alright", Toast.LENGTH_SHORT).show();
                    textToSpeech.speak(result, TextToSpeech.QUEUE_FLUSH, null);
                }
                else if (temperatureData == 37) {
                    String result = "Consult Doctor! You might have fever" +name;
                    Toast.makeText(context, "Consult Doctor! You might have fever", Toast.LENGTH_SHORT).show();
                    textToSpeech.speak(result, TextToSpeech.QUEUE_FLUSH, null);
                }
                else {
                    String result = "You have fever! Take care and Consult Doctor as soon as possible";
                    Toast.makeText(context, "You have fever! Take care and consult Doctor as soon as possilble", Toast.LENGTH_SHORT).show();
                    textToSpeech.speak(result, TextToSpeech.QUEUE_FLUSH, null);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return patientName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView patientName, tempDetect;
        ImageView picasso;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            picasso = itemView.findViewById(R.id.profile);
            patientName = itemView.findViewById(R.id.name);
            tempDetect = itemView.findViewById(R.id.temp);
            relativeLayout = itemView.findViewById(R.id.layout);

        }
    }
}
