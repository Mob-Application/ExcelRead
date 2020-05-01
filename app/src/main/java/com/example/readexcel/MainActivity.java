package com.example.readexcel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.FileAsyncHttpResponseHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    Adapter adapter;
    AsyncHttpClient asyncHttpClient;
    Workbook workbook;
    List<String> name, temp;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Fetching data");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();

        String url = "https://github.com/Karthick986/Excel-Data/blob/master/Book1.xls?raw=true";

        recyclerView = findViewById(R.id.recycler);
        name = new ArrayList<>();
        temp = new ArrayList<>();

        asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new FileAsyncHttpResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Excel cannot read", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {

                progressDialog.dismiss();
                WorkbookSettings settings = new WorkbookSettings();
                settings.setGCDisabled(true);

                if (file != null) {
                    try {
                        workbook = workbook.getWorkbook(file);

                        Sheet sheet = workbook.getSheet(0);
                        for (int i=0; i<sheet.getRows(); i++) {
                            Cell[] row = sheet.getRow(i);
                            name.add(row[0].getContents());
                            temp.add(row[1].getContents());
                        }
                        showData();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (BiffException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void showData() {
        adapter = new Adapter(this, name, temp);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
