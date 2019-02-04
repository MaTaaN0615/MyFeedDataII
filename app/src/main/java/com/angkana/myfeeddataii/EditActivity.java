package com.angkana.myfeeddataii;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.angkana.myfeeddataii.Bean.ProductBean;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.angkana.myfeeddataii.Bean.ProductBean.BASE_URL;

public class EditActivity extends AppCompatActivity {

    private ProductBean mProductBean;

    // ----- user interface -----
    private EditText mInputProductIdEDT;
    private EditText mInputNameEDT;
    private EditText mInputDetailEDT;
    private EditText mInputImageProductEDT;
    private EditText mInputPriceEDT;
    private Button mSaveButton;
    private Button mDeleteButton;
    private ImageView mSelectImageIMV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        mProductBean = getIntent().getParcelableExtra("product_bean");

        if (mProductBean == null) {
            finish();
        }

        bindWidget();
        setUpEventWidget();
        setupToolbar();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Database");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpEventWidget() {
        mInputProductIdEDT.setText(mProductBean.getId_product());
        mInputNameEDT.setText(mProductBean.getName());
        mInputDetailEDT.setText(mProductBean.getDetail());
        mInputPriceEDT.setText(mProductBean.getPrice());
        mInputImageProductEDT.setText(mProductBean.getImg_product());

        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);

                builder.setMessage("Are you sure to want to Delete?");
                builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new DeleteAsyn().execute(BASE_URL + "delete.php");
                    }
                });

                builder.show();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdateAsyn().execute(BASE_URL + "update.php");
            }
        });

        mSelectImageIMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent _intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.th/search?dcr=0&tbm=isch&q=iphone+x&spell=1&sa=X&ved=0ahUKEwia3fO5t-jXAhWDsI8KHQAPBIEQvwUIbigA&biw=1366&bih=651&dpr=1"));
                startActivity(_intent);
            }
        });
    }

    private void bindWidget() {
        mInputProductIdEDT = (EditText) findViewById(R.id.inputProductIdEDT);
        mInputNameEDT = (EditText) findViewById(R.id.inputNameEDT);
        mInputDetailEDT = (EditText) findViewById(R.id.inputDetailEDT);
        mInputPriceEDT = (EditText) findViewById(R.id.inputPriceEDT);
        mInputImageProductEDT = (EditText) findViewById(R.id.inputImageProductEDT);
        mSaveButton = (Button) findViewById(R.id.saveBtn);
        mDeleteButton = (Button) findViewById(R.id.delete);
        mSelectImageIMV = (ImageView) findViewById(R.id.selectImageIMV);
    }

    private class DeleteAsyn extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {

            try {
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("id", mProductBean.getId())
                        .build();

                Request _request = new Request.Builder().url(strings[0]).post(_requestBody).build();

                _okHttpClient.newCall(_request).execute();

                return "successfully";

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                Toast.makeText(EditActivity.this, "delete successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditActivity.this, "delete failure", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class UpdateAsyn extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {

            try {
                OkHttpClient _okHttpClient = new OkHttpClient();
                RequestBody _requestBody = new FormBody.Builder()
                        .add("id",mProductBean.getId())
                        .add("product_id", mInputProductIdEDT.getText().toString())
                        .add("name", mInputNameEDT.getText().toString())
                        .add("detail", mInputDetailEDT.getText().toString())
                        .add("price", mInputPriceEDT.getText().toString())
                        .add("image", mInputImageProductEDT.getText().toString())
                        .build();

                Request _request = new Request.Builder().url(strings[0]).post(_requestBody).build();

                _okHttpClient.newCall(_request).execute();

                return "successfully";

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                Toast.makeText(EditActivity.this, "update successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(EditActivity.this, "update failure", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
