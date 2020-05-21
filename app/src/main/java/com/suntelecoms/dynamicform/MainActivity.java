package com.suntelecoms.dynamicform;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.suntelecoms.dynamicform.api.ResponseAddAnnonce;
import com.suntelecoms.dynamicform.api.RetrofitUser;

import com.suntelecoms.dynamicform.materialLeanBack.MaterialLeanBack;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class MainActivity extends AppCompatActivity {
EditText text; TextView hash;
String token = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImRkODhjN2RlZjNhYzlmMTYyYTQ1NDMzOTQxZDNmZTcyYjZkYzMzNmJlYTI3OWUyNjVhZmU2MDBkY2IyMDNmYTEwYWM3NWY0NDBhNTI4MDc1In0.eyJhdWQiOiIxIiwianRpIjoiZGQ4OGM3ZGVmM2FjOWYxNjJhNDU0MzM5NDFkM2ZlNzJiNmRjMzM2YmVhMjc5ZTI2NWFmZTYwMGRjYjIwM2ZhMTBhYzc1ZjQ0MGE1MjgwNzUiLCJpYXQiOjE1Nzk5NzA2MDIsIm5iZiI6MTU3OTk3MDYwMiwiZXhwIjoxNjExNTkzMDAyLCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.1klElNeb47hxb6p5_DVA3SyhfQRuUalLuLsMrrQqXmmHLugcVJMazvYa6qS_PVDbIi6IVZORyLhI8S1-H2Ak1UFJQ429H1PcS7mzC-Z2fcDIeGUw2FkBEHe1qWK5ZPRLxiMt9wZbos73Hx5FcN2Vw0idQ5j_d9ipZgd8gHnRH8imtZkZ38ByKGCwVkKJZ_9qqb9JH8xN8fngOnUr50vLNWVj0hoUf05sUF4cqZvGwqJ7-yKN9QeMuXonwDRPwOmV02rf_7NlEyigoBiLfyY20exOwbAc5Z3ICbEg9MQXJvoLfKA-k9cNeH-63Y6GCN3hvxYiO8O3JmJaeje1B0TmTe7ijCGv6wep_VsmmMHO4KE9KPDuUmS6JKZVwGrhEkM57aPSNmWfAA8zPx8KNARf_ZsBgx66tsPdJ-MA-HIvKZKGi24iobaxq5LCWzA_YqmNb2s9WUaEGaa_5wwIH1zuesANRBo6NL-Lh1bn0f4AITrwDbnIyZfd8DNbZQpAEDzb2Wqt26VEqO9QPq-ynzv3PGj3bIIuYwoRv0jqKAi5Fa9L85E3hqJe_WDvLhI4aoepFAIBPw8XrRUIzHvam7SWUgwNo2oBjVeAYfi00qKVGP17XrDGvnuTjRw7aA3xRcBip_E4a1hg6l_Enlmlqp5w7ZFtOZM_v5XAqIYFKXCQsSE";

    ImageView imageHolder;
    private int requestCode;
    String imagePath;
    MaterialLeanBack materialLeanBack;

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text);
        hash = findViewById(R.id.hash);

        verifyStoragePermissions(this);

        imageHolder = findViewById(R.id.photo_choc);
        imageHolder.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(photoCaptureIntent, 1);
            }
        });
        materialLeanBack = findViewById(R.id.materialLeanBack);

        ArrayList<Galery> galeries = new ArrayList<>();
        galeries.add(new Galery("https://via.placeholder.com/150/0000FF/808080 ?Text=Digital.com","Title 1"));
        galeries.add(new Galery("https://via.placeholder.com/150/FF0000/FFFFFF?Text=Down.com","Title 2"));
        galeries.add(new Galery("https://via.placeholder.com/150/FFFF00/000000?Text=WebsiteBuilders.com","Title 3"));
        galeries.add(new Galery("https://via.placeholder.com/150/000000/FFFFFF/?text=IPaddress.net","Title 4"));


        materialLeanBack.setAdapter(new GaleryAdapter(this, galeries, 1));
    }

    public void onForm(View v) {
        //Intent intent = new Intent(this, FormulaireActivity.class);
        //startActivity(intent);
       // hash.setText(md5(text.getText().toString()));

        File file = new File(imagePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        final MultipartBody.Part body =  MultipartBody.Part.createFormData("uploaded_file", file.getName(), requestFile);

       // imageHolder.setImageDrawable(null);
       // imagePath = null;


        RetrofitUser.getRoutes().getAdd_Annonce(token, body,
                RequestBody.create(MediaType.parse("text/plain"), "TestFile")).enqueue(new Callback<ResponseAddAnnonce>() {
            @Override
            public void onResponse(Call<ResponseAddAnnonce> call, Response<ResponseAddAnnonce> response) {

            }

            @Override
            public void onFailure(Call<ResponseAddAnnonce> call, Throwable t) {

            }
        });
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e( "image uri: ", requestCode+" - "+ RESULT_OK+" - "+requestCode );

        if (resultCode == RESULT_OK && requestCode == 1) {
            if (data == null) {
                Toast.makeText(getApplicationContext(),"Unable to pick image",Toast.LENGTH_LONG).show();
                return;
            }
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imagePath = saveImage(bitmap);

            Log.e( "image real path: ",  imagePath);
        }
    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e("TEstttt", "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + "SOA/Pictures/");
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
            Log.d("hhhhh",wallpaperDirectory.toString());
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";

    }

}
