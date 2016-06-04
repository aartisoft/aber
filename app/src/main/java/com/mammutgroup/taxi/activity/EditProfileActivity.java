package com.mammutgroup.taxi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mammutgroup.taxi.config.UserConfig;
import com.mammutgroup.taxi.service.remote.rest.api.user.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Iraj on 6/3/2016.
 */
public class EditProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CAMERA = 15;
    private static final int SELECT_FILE = 14;
    @Bind(R.id.input_email_layout)
    TextInputLayout emailTil;
    @Bind(R.id.input_email)
    EditText email;
    @Bind(R.id.input_family)
    EditText lastName;
    @Bind(R.id.btn_save)
    Button saveBtn;
    @Bind(R.id.input_first_name)
    EditText firstName;
    @Bind(R.id.sex)
    Spinner sexSpinner;
    @Bind(R.id.user_profile_name)
    TextView userProfileNameText;
    @Bind(R.id.user_profile_email)
    TextView userProfileEmail;
    @Bind(R.id.user_profile_photo)
    ImageButton userPhotoImgBtn;
    private String userChoosenTask = "";

    String profileImg;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    protected void setupToolbar() {
        getSupportActionBar().setTitle(R.string.title_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User user = UserConfig.getCurrentUser();
        setContentView(R.layout.edit_profile);
        ButterKnife.bind(this);
        emailTil.setErrorEnabled(true);
        if (user != null) {
            String email = user.getEmail();
            if (email != null) {
                this.email.setText(email);
                userProfileEmail.setText(user.getEmail());
            }
            String lastName = user.getLastName();
            if (lastName != null)
                this.lastName.setText(lastName);
            String firstName = user.getFirstName();
            if (firstName != null)
                this.firstName.setText(firstName);
            String gender = user.getGender();
            if (gender != null) {
                if (gender.equalsIgnoreCase("female"))
                    sexSpinner.setSelection(1);
            }
            String fullName = user.getFullName();
            if (fullName != null)
                userProfileNameText.setText(fullName);

            if (user.getProfileImg() != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                ContextWrapper cw = new ContextWrapper(this);
                File directory = cw.getDir("media", Context.MODE_PRIVATE);
                Bitmap bitmap = BitmapFactory.decodeFile(directory +"/" + user.getProfileImg(), options);
                userPhotoImgBtn.setImageBitmap(getRoundedCornerBitmap(bitmap));
            }
//            b
//            userPhotoImgBtn.setImageBitmap();
        }
        userPhotoImgBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Toast.makeText(getApplicationContext(),
                        "ImageButton (selector) is clicked!",
                        Toast.LENGTH_SHORT).show();

                selectImage();
            }
        });
        setupToolbar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        String name = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                name = saveImageToLocal(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        profileImg = name;
        userPhotoImgBtn.setImageBitmap(getRoundedCornerBitmap(bm));
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        String name = saveImageToLocal(thumbnail);
        profileImg = name;
        userPhotoImgBtn.setImageBitmap(getRoundedCornerBitmap(thumbnail));
    }

    @NonNull
    private String saveImageToLocal(Bitmap thumbnail) {
        ContextWrapper cw = new ContextWrapper(this);
        File directory = cw.getDir("media", Context.MODE_PRIVATE);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//        PackageManager m = getPackageManager();
//        String s = getPackageName();
//        try {
//            PackageInfo p = m.getPackageInfo(s, 0);
//            s = p.applicationInfo.dataDir;
//            System.out.println(s);
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.w("Mammut", "Error Package name not found ", e);
//        }
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String name = System.currentTimeMillis() + ".jpg";
        File destination = new File(directory,
                name);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        paint.setAntiAlias(true);
        Canvas c = new Canvas(circleBitmap);
        c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);

        return circleBitmap;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(i, "Select File"), SELECT_FILE);
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = checkPermission();
                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 12 && resultCode == Activity.RESULT_OK) {
//            if (data == null) {
//                //Display an error
//                return;
//            }
//            Uri selectedImage = data.getData();
//            String[] filePathColumn = {MediaStore.Images.Media.DATA};
//
//            Cursor cursor = getContentResolver().query(selectedImage,
//                    filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//
//            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
//            userPhotoImgBtn.setImageBitmap(bitmap);
//
//            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
//        }
//    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return true;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    @OnClick(R.id.btn_save)
    void save() {
        if (!isValidEmail(email.getText())) {
            emailTil.setError(getString(R.string.invalid_email_address));
            return;
        }
        User currentUser = UserConfig.getCurrentUser();
        //TODO remove this line
        if (currentUser == null)
            currentUser = new User();
        if (email != null) {
            String emailStr = email.getText().toString();
            if (emailStr != null) {
                currentUser.setEmail(emailStr);
                userProfileEmail.setText(emailStr);
            }
        }
        if (firstName != null) {
            String str = firstName.getText().toString();
            if (str != null)
                currentUser.setFirstName(str);
        }
        if (lastName != null) {
            String str = lastName.getText().toString();
            if (str != null)
                currentUser.setLastName(str);
        }
        if (firstName != null && lastName != null) {
            String str1 = firstName.getText().toString();
            String str2 = lastName.getText().toString();
            StringBuilder fullName = new StringBuilder();
            fullName = fullName.append(str1);
            fullName = fullName.append(" ");
            fullName = fullName.append(str2);
            currentUser.setFullName(fullName.toString());
            userProfileNameText.setText(fullName.toString());
        }
        if (profileImg != null)
            currentUser.setProfileImg(profileImg);
        currentUser.setGender(String.valueOf(sexSpinner.getSelectedItem()));

        UserConfig.setCurrentUser(currentUser);
        UserConfig.saveConfig();
        Toast.makeText(getApplicationContext(), R.string.profile_successfully_saved, Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String a[] = {android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(a, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return false;
        }
        return true;

//        int currentAPIVersion = Build.VERSION.SDK_INT;
//        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
//                    alertBuilder.setCancelable(true);
//                    alertBuilder.setTitle("Permission necessary");
//                    alertBuilder.setMessage("External storage permission is necessary");
//                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//                        public void onClick(DialogInterface dialog, int which) {
//                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                        }
//                    });
//                    AlertDialog alert = alertBuilder.create();
//                    alert.show();
//                } else {
//                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
//                }
//                return false;
//            } else {
//                return true;
//            }
//        } else {
//            return true;
//        }
    }
}
