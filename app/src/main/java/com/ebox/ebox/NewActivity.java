package com.ebox.ebox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.frosquivel.magicalcamera.Functionallities.PermissionGranted;
import com.frosquivel.magicalcamera.MagicalCamera;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NewActivity extends AppCompatActivity {


    String category_id;
    String subject;
    String subject_cn;
    EditText txt_message_new, txt_other_new;
    TextView txt_path_new, txt_other;
ProgressDialog progressDialog;
    ImageView imageView;

    String imagefilename, imagepath;
    MagicalCamera magicalTakePhoto;
    private int RESIZE_PHOTO_PIXELS_PERCENTAGE = 80;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        this.setTitle("Compose");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        PermissionGranted permissionGranted = new PermissionGranted(this);
        //permission for take photo, it is false if the user check deny
        permissionGranted.checkCameraPermission();
        //for search and write photoss in device internal memory
        //normal or SD memory
        permissionGranted.checkReadExternalPermission();
        permissionGranted.checkWriteExternalPermission();
        //permission for location for use the `photo information device.
        permissionGranted.checkLocationPermission();

        magicalTakePhoto = new MagicalCamera(this, RESIZE_PHOTO_PIXELS_PERCENTAGE, permissionGranted);

        Spinner spinner_category = (Spinner) findViewById(R.id.spinner_category);
        final Spinner spinner_subject = (Spinner) findViewById(R.id.spinner_subject);
        imageView = (ImageView) findViewById(R.id.imageView);
        txt_message_new = (EditText) findViewById(R.id.txt_message_new);
        txt_other_new = (EditText) findViewById(R.id.txt_other_new);

        txt_other = (TextView) findViewById(R.id.txt_other);
        FillList<category_Model> f = new FillList<category_Model>();
        f.FillSpinner(getApplicationContext(), new Object[]{R.id.txt_id_categoryy, R.id.txt_spiner_value}, new Object[]{"cn", "category_name"}, R.layout.row_spinner, spinner_category, category_Model.class, variable.url_category, Request.Method.POST, new String[]{""}, new String[]{""});

        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                category_id = ((TextView) view.findViewById(R.id.txt_id_categoryy)).getText().toString();
                FillList<subject_model> s = new FillList<subject_model>();
                s.FillSpinner(getApplicationContext(), new Object[]{R.id.txt_id_categoryy, R.id.txt_spiner_value}, new Object[]{"cn", "subject_name"}, R.layout.row_spinner, spinner_subject, subject_model.class, variable.url_subject, Request.Method.POST, new String[]{"Category_cn"}, new String[]{category_id});

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_subject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subject = ((TextView) view.findViewById(R.id.txt_spiner_value)).getText().toString();
                subject_cn = ((TextView) view.findViewById(R.id.txt_id_categoryy)).getText().toString();
                if (subject.contains("Other")) {
                    subject = txt_other_new.getText().toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void cmd_new_Clicked(final View view) {
        Button cmd_new_Clicked = (Button) view;
        int i = Integer.valueOf(cmd_new_Clicked.getTag().toString());
        if (i == 1) {
            // capture image
            magicalTakePhoto.takePhoto();
        } else if (i == 2) {
            //Send message
            progressDialog.show();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, variable.url_insertRecord, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            if (response.contains("Failed!")) {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                                Snackbar.make(view, response, Snackbar.LENGTH_SHORT).setAction("Action", null).show();
                            } else {
                                upLoadImage();
                                startActivity(new Intent(getApplicationContext(), EmptyActivity.class));
                                overridePendingTransition(0, 0);
                                finish();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("Sender_cn", variable.Current_user_cn);
                            params.put("Subject_cn", subject_cn);
                            params.put("Subject_description", subject);
                            params.put("Messsage", txt_message_new.getText().toString());
                            params.put("File_path", imagefilename);

                            return params;
                        }
                    };
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }
            };
            new Thread(runnable).start();


        } else if (i == 3) {

            ///rowse image to gallery
            File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Camera");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return;
                }
            }
            imagepath = mediaStorageDir.toString().substring(mediaStorageDir.toString().lastIndexOf("/") + 1);
            magicalTakePhoto.selectedPicture(imagepath);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == android.R.id.home) {

            backToEmtyActivity();
        }

        return true;
    }

    private void backToEmtyActivity() {
        startActivity(new Intent(getApplicationContext(), EmptyActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }


    public void upLoadImage() {


        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, variable.uploadPhoto,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("INSERT_PHOTO", response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("CAMERA_ERROR", error.getMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("image", Tools.getStringImage(BitmapFactory.decodeFile(imagepath)));
                        params.put("file_name", imagefilename);
                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }
        };
        new Thread(runnable).start();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToEmtyActivity();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

      //  try {

        //CALL THIS METHOD EVER
        magicalTakePhoto.resultPhoto(requestCode, resultCode, data);

        //this is for rotate picture in this method
        //magicalCamera.resultPhoto(requestCode, resultCode, data, MagicalCamera.ORIENTATION_ROTATE_180);

            Bitmap originalBitmap = magicalTakePhoto.getPhoto();
        if(originalBitmap != null) {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 333, 500, false);
            //with this form you obtain the bitmap (in this example set this bitmap in image view)
            imageView.setImageBitmap(resizedBitmap);

            //if you need save your bitmap in device use this method and return the path if you need this
            //You need to send, the bitmap picture, the photo name, the directory name, the picture type, and autoincrement photo name if           //you need this send true, else you have the posibility or realize your standard name for your pictures.
            //   String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            //  imagefilename = "IMG_" + timeStamp + ".jpg";


            final File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + "eConcern");
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return;
                }
            }


            imagepath = magicalTakePhoto.savePhotoInMemoryDevice(resizedBitmap, "", mediaStorageDir.getAbsolutePath(), MagicalCamera.PNG, true);
            //  imagepath =  new Tools().decodeFile(imagepath,144,180);
            imagefilename = imagepath.substring(imagepath.lastIndexOf("/") + 1);
            Log.d("Path_result1", imagepath + " - " + imagefilename);

        }

       // } catch (Exception ex) {
         //   Log.d("error", ex.getMessage());
        //}


    }
}
