package es.unavarra.tlm.prueba;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.R.attr.duration;

public class Camara extends AppCompatActivity {

    Activity activity = this;
    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
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

    private static final String TAG = "MyActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap mImageBitmap;
    private String mCurrentPhotoPath;
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camara);
        verifyStoragePermissions(this);

        Button boton = (Button)findViewById(R.id.boton_camara);
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView texto = (TextView)findViewById(R.id.texto);
                texto.setText("Hola");

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;


                        photoFile = createImageFile(getApplicationContext());
                     /*catch (IOException ex) {
                        // Error occurred while creating the File
                        Log.i("tag", "IOException");
                    }*/
                    //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                    //startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        cameraIntent.putExtra("crop", "true");
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                        startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                    }
                }
            }
        });

        Button botonDos = (Button)findViewById(R.id.boton_crop);
        botonDos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // start picker to get image for cropping and then use the image in cropping activity
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(3,4)
                        .setFixAspectRatio(true)
                        .start(activity);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            try {
                mImageView = (ImageView)findViewById(R.id.image_test);
                mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse("file:"+mCurrentPhotoPath));
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(mCurrentPhotoPath);
                    mImageBitmap.compress(Bitmap.CompressFormat.JPEG, 70, out); // bmp is your Bitmap instance
                    // PNG is a lossless format, the compression factor (100) is ignored
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                mImageView.setImageBitmap(mImageBitmap);
            } catch (IOException e) {
                StringWriter errors = new StringWriter();
                e.printStackTrace(new PrintWriter(errors));
                TextView texto = (TextView)findViewById(R.id.texto);
                texto.setText(errors.toString());

            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                try {
                    mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FileOutputStream out = null;
                    try {
                        System.out.println(resultUri.getPath());
                        out = new FileOutputStream(resultUri.getPath());
                        Bitmap b = Bitmap.createScaledBitmap(mImageBitmap, 1200, 1600, false);
                        b.compress(Bitmap.CompressFormat.JPEG, 70, out);
                        File photoFile = null;
                        photoFile = createImageFile(getApplicationContext());
                        out = new FileOutputStream(mCurrentPhotoPath);
                        b.compress(Bitmap.CompressFormat.JPEG, 70, out);
                        ImageView imageTest = (ImageView) findViewById(R.id.image_test);
                        imageTest.setImageBitmap(b);

                        // bmp is your Bitmap instance
                        // PNG is a lossless format, the compression factor (100) is ignored
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private File createImageFile(Context context) {
        // Create an image file name

        TextView texto = (TextView)findViewById(R.id.texto);

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES),"/Swappie/");
        Boolean mkdirs=false;
        if (!storageDir.exists()) {
            mkdirs = storageDir.mkdirs();
        }
        texto.setText(storageDir.getAbsolutePath()+" "+mkdirs);
        Toast toast = Toast.makeText(context, storageDir.getAbsolutePath(), Toast.LENGTH_SHORT);
        toast.show();
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".jpg",         // suffix
                    storageDir     // directory
            );
        } catch (IOException e) {
            StringWriter errors = new StringWriter();
            e.printStackTrace(new PrintWriter(errors));
            Toast.makeText(context,errors.toString(),Toast.LENGTH_LONG).show();
        }

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


}
