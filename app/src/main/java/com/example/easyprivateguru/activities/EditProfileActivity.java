package com.example.easyprivateguru.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.easyprivateguru.BuildConfig;
import com.example.easyprivateguru.R;
import com.example.easyprivateguru.UserHelper;
import com.example.easyprivateguru.api.ApiInterface;
import com.example.easyprivateguru.api.RetrofitClientInstance;
import com.example.easyprivateguru.models.User;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class EditProfileActivity extends AppCompatActivity {
    private static final String TAG = "EditProfile";

    private RetrofitClientInstance rci = new RetrofitClientInstance();
    private ApiInterface apiInterface = rci.getApiInterface();
    private UserHelper userHelper;

    private EditText etName, etTelp;
    private CircleImageView civProfilePic;
    private LinearLayout llGallery, llCamera;
    private Button btnSubmit;

    private User currUser;

    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PHOTO = 1888;
    private static final int CAMERA_PIC_REQUEST = 1111;
    public static final String IMAGE_DIRECTORY_NAME = "Android File Upload";

    private Uri imageUri;
    private String picturePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        init();

//        civProfilePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickFromGallery();
//            }
//        });

        llGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickFromGallery();
            }
        });

        llCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllFilled()){
                    Log.d(TAG, "onClick: formIsFilled and hasChanged");
                    callEditGuru();
                }else if(!isAllFilled()){
                    Log.d(TAG, "onClick: formIsNotFilled");
                    Toast.makeText(EditProfileActivity.this, "Mohon lengkapi data diri Anda", Toast.LENGTH_LONG).show();
                }else if(!formHasChanged()){
                    Log.d(TAG, "onClick: Form has not changed");
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(formHasChanged()){
            showDialogConfirmExit();
        }else{
            super.onBackPressed();
        }
    }

    private boolean formHasChanged(){
        if(!etName.getText().toString().equals(currUser.getName())
                || !etTelp.getText().toString().equals(currUser.getNoHandphone())){
            return true;
        }else{
            return false;
        }
    }

    private void init(){
        userHelper = new UserHelper(this);
        currUser = userHelper.retrieveUser();

        civProfilePic = findViewById(R.id.civPic);
        llGallery = findViewById(R.id.llBtnGallery);
        llCamera = findViewById(R.id.llBtnTakePhoto);

        etName = findViewById(R.id.etName);
        etTelp = findViewById(R.id.etTelp);

        btnSubmit = findViewById(R.id.btnSubmit);

        userHelper.putIntoImage(currUser.getAvatar(), civProfilePic);
        etName.setText(currUser.getName());
        etTelp.setText(currUser.getNoHandphone());

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void pickFromGallery(){
        //Create an Intent with action as ACTION_PICK
        Intent intent = new Intent(Intent.ACTION_PICK);

        // Sets the type as image/*. This ensures only components of type image are selected
        intent.setType("image/*");

        //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
        String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);

        // Launching the Intent
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void captureImage() {
        if (Build.VERSION.SDK_INT > 21) { //use this if Lollipop_Mr1 (API 22) or above
            Intent callCameraApplicationIntent = new Intent();
            callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

            // We give some instruction to the intent to save the image
            File photoFile = null;

            try {
                // If the createImageFile will be successful, the photo file will have the address of the file
                photoFile = createImageFile();
                // Here we call the function that will try to catch the exception made by the throw function
            } catch (IOException e) {
                Logger.getAnonymousLogger().info("Exception error in generating the file");
                e.printStackTrace();
            }
            // Here we add an extra file to the intent to put the address on to. For this purpose we use the FileProvider, declared in the AndroidManifest.
            Uri outputUri = FileProvider.getUriForFile(
                    this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    photoFile);
            callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

            // The following is a new line with a trying attempt
            callCameraApplicationIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

            Logger.getAnonymousLogger().info("Calling the camera App by intent");

            // The following strings calls the camera app and wait for his file in return.
            startActivityForResult(callCameraApplicationIntent, CAMERA_PIC_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            imageUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            Log.d(TAG, "captureImage: imageUri: "+imageUri.toString());

            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_PIC_REQUEST);
        }
    }

    File createImageFile() throws IOException {
        Logger.getAnonymousLogger().info("Generating the image - method started");

        // Here we create a "non-collision file name", alternatively said, "an unique filename" using the "timeStamp" functionality
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmSS").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp;
        // Here we specify the environment location and the exact path where we want to save the so-created file
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + "/photo_saving_app");
        Logger.getAnonymousLogger().info("Storage directory set");

        // Then we create the storage directory if does not exists
        if (!storageDirectory.exists()) storageDirectory.mkdir();

        // Here we create the file using a prefix, a suffix and a directory
        File image = new File(storageDirectory, imageFileName + ".jpg");
        // File image = File.createTempFile(imageFileName, ".jpg", storageDirectory);

        // Here the location is saved into the string mImageFileLocation
        Logger.getAnonymousLogger().info("File name and path set");

        picturePath = image.getAbsolutePath();
        Log.d(TAG, "createImageFile: picturePath: "+picturePath);
        // fileUri = Uri.parse(mImageFileLocation);
        // The file is returned to the previous intent across the camera application
        return image;
    }

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + ".jpg");
        }  else {
            return null;
        }

        return mediaFile;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", imageUri);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imageUri = savedInstanceState.getParcelable("file_uri");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case PICK_IMAGE:
                    imageUri = data.getData();
                    civProfilePic.setImageURI(imageUri);

                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(imageUri, filePathColumn, null, null, null);
                    assert cursor != null;
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                    Log.d(TAG, "onActivityResult: picturePath: "+picturePath.trim());
                    cursor.close();

                    break;
                case CAMERA_PIC_REQUEST:
                    if (Build.VERSION.SDK_INT > 21) {

                        Glide.with(this).load(picturePath).into(civProfilePic);
                        Log.d(TAG, "onActivityResult: picturePath: "+picturePath);

                    }else{
                        Glide.with(this).load(imageUri).into(civProfilePic);
                        civProfilePic.setImageURI(imageUri);
                        picturePath = imageUri.getPath();
                        Log.d(TAG, "onActivityResult: imageUri: "+imageUri.toString());
                    }
                    break;
            }
        }
    }

    private boolean isAllFilled(){
        if(etName.getText().toString().length() <= 0 || etTelp.getText().toString().length() <= 0){
            return false;
        }else{
            return true;
        }
    }

    private void callEditGuru(){
        RequestBody id = RequestBody.create(MultipartBody.FORM, String.valueOf(currUser.getId()));
        RequestBody name = RequestBody.create(MultipartBody.FORM, etName.getText().toString());
        RequestBody noTelp = RequestBody.create(MultipartBody.FORM, etTelp.getText().toString());

        MultipartBody.Part image = null;
        Call<User> call;

        File file = null;
        if (picturePath != null && !picturePath.equals("")){
            file = new File(picturePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(imageUri)), file);
            Log.d(TAG, "callEditGuru: image name: "+file.getName());
            Log.d(TAG, "callEditGuru: image type: "+getContentResolver().getType(imageUri));
            image = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);

            call = apiInterface.editGuru(id, name, noTelp, image);
        }else{
            Log.d(TAG, "callEditGuru: picturePath is null");
            call = apiInterface.editGuru(id, name, noTelp,null);
        }

        ProgressDialog progressDialog = rci.getProgressDialog(this);
        progressDialog.show();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d(TAG, "onResponse: "+response.message());
                progressDialog.dismiss();
                if(!response.isSuccessful()){
                    ResponseBody rb = response.errorBody();
                    Log.d(TAG, "onResponse: "+rb.toString());
                    return;
                }
                try {
                    User user = response.body();
                    userHelper.storeUser(user);
                }catch (Exception e){
                    e.printStackTrace();
                }
                showDialogConfirmEdit();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
                t.printStackTrace();
                Toast.makeText(EditProfileActivity.this, "Hmmm... sepertinya ada yang bermasalah", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDialogConfirmEdit(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Edit profil");
        alertDialog.setMessage("Profil Anda berhasil disimpan!");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Kembali", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
                finish();
            }
        });
        alertDialog.show();
    }

    private void showDialogConfirmExit(){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Edit profil");
        alertDialog.setMessage("Apakah anda ingin menyimpan perubahan profil?");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Simpan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callEditGuru();
                finish();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Batalkan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
//        alertDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Kembali", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
        alertDialog.setCancelable(true);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
}
